package f.jhandy.ignite.ipfinder;

import f.jhandy.ignite.impl.KubernetesIpFinder2;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * @author sunmoonone
 * @version 2019/01/04
 */
public class KubernetesIpFinderConfig {

    @NotEmpty
    private String namespace;

    //default: ignite
    private String serviceName;
    //default: https://kubernetes.default.svc.cluster.local:443
    private String masterUrl;
    //default: /var/run/secrets/kubernetes.io/serviceaccount/token
    private String accountToken;

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

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public TcpDiscoveryIpFinder build() {

        KubernetesIpFinder2 finder = new KubernetesIpFinder2();
        finder.setNamespace(namespace);

        if (!StringUtils.isEmpty(serviceName)) {
            finder.setServiceName(serviceName);
        }
        if (!StringUtils.isEmpty(masterUrl)) {
            finder.setMasterUrl(masterUrl);
        }
//        if (!StringUtils.isEmpty(accountToken)) {
//            finder.setAccountToken(accountToken);
//        }
        return finder;
    }
}
