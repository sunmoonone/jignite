package f.jhandy.ignite;

import f.jhandy.ignite.ipfinder.IpFinderConfig;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sunmoonone
 * @version 2018/12/21
 */
public class IgniteProperties {
    private String igniteInstanceName;
    private boolean peerClassLoadingEnabled;
    private List<String> eventTypes;

    private boolean clientMode;

    private IpFinderConfig ipFinder;

    public boolean isPeerClassLoadingEnabled() {
        return peerClassLoadingEnabled;
    }

    public void setPeerClassLoadingEnabled(boolean peerClassLoadingEnabled) {
        this.peerClassLoadingEnabled = peerClassLoadingEnabled;
    }

    public String getIgniteInstanceName() {
        return (igniteInstanceName==null || "".equals(igniteInstanceName))? "igniteInstance": igniteInstanceName;
    }

    public void setIgniteInstanceName(String igniteInstanceName) {
        this.igniteInstanceName = igniteInstanceName;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public boolean isClientMode() {
        return clientMode;
    }

    public void setClientMode(boolean clientMode) {
        this.clientMode = clientMode;
    }

    public IpFinderConfig getIpFinder() {
        return ipFinder;
    }

    public void setIpFinder(IpFinderConfig ipFinder) {
        this.ipFinder = ipFinder;
    }

    public IgniteConfiguration buildIgniteConfiguration() throws Exception {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        igniteConfiguration.setClientMode(clientMode);

        igniteConfiguration.setIgniteInstanceName(getIgniteInstanceName());

        postConfigDiscoverySpi(igniteConfiguration);
        postConfigEventTypes(igniteConfiguration);

        postConfig(igniteConfiguration);

        return igniteConfiguration;
    }

    private void postConfigDiscoverySpi(IgniteConfiguration igniteConfiguration) {
        if(ipFinder!=null){
            igniteConfiguration.setDiscoverySpi(ipFinder.buildDiscoverySpi());
        }
    }

    private void postConfigEventTypes(IgniteConfiguration igniteConfiguration) throws Exception {
        if (null == eventTypes || eventTypes.size() <= 0)
            return;

        Class cls = EventType.class;
        Object obj = new EventType() {
        };
        List<Integer> evts = new ArrayList<>();

        for (String evtName : eventTypes) {
            Field f = cls.getField(evtName);
            if (f == null) {
                throw new Exception("event not supported:" + evtName);
            }
            Object v = f.get(obj);
            if (v.getClass().isAssignableFrom(Integer.class)) {
                evts.add((Integer) v);
            } else if (v.getClass().isAssignableFrom(Integer[].class)) {
                Integer[] arr = (Integer[]) v;
                evts.addAll(Arrays.asList(arr));
            }
        }// enfor

        if (evts.size() > 0) {
            int[] args = new int[evts.size()];
            int i = 0;
            for (Integer v : evts) {
                args[i] = v;
                i++;
            }

            igniteConfiguration.setIncludeEventTypes(args);
        }
    }

    /**
     * 子类可以重写该方法以支持更多的配置，比如提供CacheConfiguration
     */
    protected void postConfig(IgniteConfiguration configuration){
    }
}
