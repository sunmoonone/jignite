package f.jhandy.ignite.ipfinder;

import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import javax.validation.constraints.NotEmpty;

/**
 * @author sunmoonone
 * @version 2019/01/04
 */
public class MulticastIpFinderConfig {
    @NotEmpty
    private String multicastGroup;

    public String getMulticastGroup() {
        return multicastGroup;
    }

    public void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }

    public TcpDiscoveryIpFinder build(){

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();

        ipFinder.setMulticastGroup(multicastGroup);
        return ipFinder;
    }
}
