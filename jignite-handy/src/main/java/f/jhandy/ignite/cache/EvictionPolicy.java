package f.jhandy.ignite.cache;

/**
 * @author sunmoonone
 * @version 2018/12/25
 */
public class EvictionPolicy {
    final public static String POLICY_NONE="none";
    final public static String POLICY_LRU="lru";
    final public static String POLICY_FIFO="fifo";

    private String name;
    private int maxEntryCount;
    private long maxMemSize;

    public EvictionPolicy(){}

    public EvictionPolicy(String name){
        this.name = name;
    }

    public EvictionPolicy(String name, int maxEntryCount, long maxMemSize) {
        this.name = name;
        this.maxEntryCount = maxEntryCount;
        this.maxMemSize = maxMemSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxEntryCount() {
        return maxEntryCount;
    }

    public void setMaxEntryCount(int maxEntryCount) {
        this.maxEntryCount = maxEntryCount;
    }

    public long getMaxMemSize() {
        return maxMemSize;
    }

    public void setMaxMemSize(long maxMemSize) {
        this.maxMemSize = maxMemSize;
    }
}
