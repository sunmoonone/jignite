package f.jhandy.ignite.ipfinder;

import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;

/**
 * @author sunmoonone
 * @version 2019/01/04
 */
public class IpFinderConfig {

    private KubernetesIpFinderConfig kubernetes;
    private MulticastIpFinderConfig multicast;
    private ZookeeperIpFinderConfig zookeeper;
    private StaticIpFinderConfig staticip;

    public KubernetesIpFinderConfig getKubernetes() {
        return kubernetes;
    }

    public void setKubernetes(KubernetesIpFinderConfig kubernetes) {
        this.kubernetes = kubernetes;
    }

    public MulticastIpFinderConfig getMulticast() {
        return multicast;
    }

    public void setMulticast(MulticastIpFinderConfig multicast) {
        this.multicast = multicast;
    }

    public ZookeeperIpFinderConfig getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperIpFinderConfig zookeeper) {
        this.zookeeper = zookeeper;
    }

    public StaticIpFinderConfig getStaticip() {
        return staticip;
    }

    public void setStaticip(StaticIpFinderConfig staticip) {
        this.staticip = staticip;
    }

    public DiscoverySpi buildDiscoverySpi() {
        if (zookeeper != null) {
            return zookeeper.build();
        } else {

            TcpDiscoverySpi spi = new TcpDiscoverySpi();

            if (kubernetes != null) {
                spi.setIpFinder(kubernetes.build());
            } else if (multicast != null) {
                spi.setIpFinder(multicast.build());
            } else if (staticip != null) {
                spi.setIpFinder(staticip.build());
            } else {
                throw new IllegalStateException("ipFinder not configured, supported: multicast, kubernetes, zookeeper, staticip");
            }

            return spi;
        }
    }
}
