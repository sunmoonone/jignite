package f.jhandy.ignite.ipfinder;

import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author sunmoonone
 * @version 2019/01/04
 */
public class StaticIpFinderConfig {
    @NotEmpty
    private List<String> ips;

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public TcpDiscoveryVmIpFinder build(){
        TcpDiscoveryVmIpFinder finder = new TcpDiscoveryVmIpFinder();
        finder.setAddresses(ips);
        return finder;
    }
}
