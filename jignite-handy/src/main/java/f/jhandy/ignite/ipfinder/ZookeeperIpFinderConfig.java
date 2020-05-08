package f.jhandy.ignite.ipfinder;

import org.apache.ignite.spi.discovery.tcp.ipfinder.zk.TcpDiscoveryZookeeperIpFinder;
import org.apache.ignite.spi.discovery.zk.ZookeeperDiscoverySpi;

import javax.validation.constraints.NotEmpty;

/**
 * @author sunmoonone
 * @version 2019/01/04
 */
public class ZookeeperIpFinderConfig {
    @NotEmpty
    private String zkConnectionString;
    private String zkServiceBasePath;
    private String zkNodeRootPath;

    public String getZkConnectionString() {
        return zkConnectionString;
    }

    public void setZkConnectionString(String zkConnectionString) {
        this.zkConnectionString = zkConnectionString;
    }

    public String getZkServiceBasePath() {
        return zkServiceBasePath;
    }

    public void setZkServiceBasePath(String zkServiceBasePath) {
        this.zkServiceBasePath = zkServiceBasePath;
    }

    public String getZkNodeRootPath() {
        return zkNodeRootPath;
    }

    public void setZkNodeRootPath(String zkNodeRootPath) {
        this.zkNodeRootPath = zkNodeRootPath;
    }

    public ZookeeperDiscoverySpi build(){
        ZookeeperDiscoverySpi spi = new ZookeeperDiscoverySpi();
        spi.setZkConnectionString(zkConnectionString);
        if (!"".equals(zkNodeRootPath)) {
            spi.setZkRootPath(zkNodeRootPath);
        }
        if (!"".equals(zkServiceBasePath)) {

            TcpDiscoveryZookeeperIpFinder zkIpFinder = new TcpDiscoveryZookeeperIpFinder();
            zkIpFinder.setBasePath(zkServiceBasePath);
        }
        return spi;
    }
}
