package f.jhandy.ignite.impl;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1EndpointAddress;
import io.kubernetes.client.models.V1EndpointSubset;
import io.kubernetes.client.models.V1Endpoints;
import io.kubernetes.client.models.V1EndpointsList;
import io.kubernetes.client.util.Config;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.internal.IgniteInterruptedCheckedException;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.spi.IgniteSpiException;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinderAdapter;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunmoonone
 * @version 2019/01/05
 */
public class KubernetesIpFinder extends TcpDiscoveryIpFinderAdapter {
    @LoggerResource
    private IgniteLogger log;

    private ApiClient client ;
    /** Init routine guard. */
    private final AtomicBoolean initGuard = new AtomicBoolean();

    /** Init routine latch. */
    private final CountDownLatch initLatch = new CountDownLatch(1);

    /** Ignite's Kubernetes Service name. */
    private String serviceName = "ignite";

    /** Ignite Pod setNamespace name. */
    private String namespace = "default";
    private String masterUrl = null; // = "https://kubernetes.default.svc.cluster.local:443";

    public KubernetesIpFinder(){
        setShared(true);
    }

    @Override
    public Collection<InetSocketAddress> getRegisteredAddresses() throws IgniteSpiException {
        init();
        Collection<InetSocketAddress> addrs = new ArrayList<>();
        try {
            CoreV1Api api = new CoreV1Api();
            log.debug(String.format("listing endpoints in namespace %s of service %s",namespace, serviceName));

            V1EndpointsList list = api.listNamespacedEndpoints(namespace,null,null,
                    null,null, null,
                    null,null,60,null);

            for (V1Endpoints item : list.getItems()) {
                if(item.getSubsets()!=null){
                    for(V1EndpointSubset subset: item.getSubsets()){
                        if(subset.getAddresses()!=null){
                            for(V1EndpointAddress address: subset.getAddresses()){
                                addrs.add(new InetSocketAddress(address.getIp(), 0));
                            }
                        }
                    }
                }
            }

            log.debug("adding endpoints: "+ StringUtils.arrayToCommaDelimitedString(addrs.toArray()));

        }catch (Exception e){
            log.error("failed to get endpoints",e);
        }
        return addrs;
    }

    private void init() throws IgniteSpiException {
        if (initGuard.compareAndSet(false, true)) {
            log.debug("initializing kubernetes apiClient");

            if (serviceName == null || serviceName.isEmpty() ||
                    namespace == null || namespace.isEmpty() ) {
                throw new IgniteSpiException(
                        "One or more configuration parameters are invalid [setServiceName=" +
                                serviceName + ", setNamespace=" + namespace + "]");
            }

            try {
                client = Config.defaultClient();
                if(masterUrl!=null && !"".equals(masterUrl)){

                    client.setBasePath(masterUrl);
                }
                Configuration.setDefaultApiClient(client);

            }
            catch (Exception e) {
                throw new IgniteSpiException("Failed to init kubernetes apiClient", e);
            }
            finally {
                initLatch.countDown();
            }
        }
        else {
            try {
                U.await(initLatch, 120, TimeUnit.SECONDS);
            }
            catch (IgniteInterruptedCheckedException e) {
                throw new IgniteSpiException("Thread has been interrupted.", e);
            }

            if (client == null)
                throw new IgniteSpiException("IP finder has not been initialized properly.");
        }
    }

    @Override
    public void registerAddresses(Collection<InetSocketAddress> addrs) throws IgniteSpiException {
        //non-op
    }

    @Override
    public void unregisterAddresses(Collection<InetSocketAddress> addrs) throws IgniteSpiException {
        //non-op
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }
}
