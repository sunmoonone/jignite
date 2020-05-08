package f.jhandy.ignite.cache.store;

import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.dialect.*;
import org.springframework.util.ClassUtils;

import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author sunmoonone
 * @version 2018/12/26
 */
public class CacheStoreFactoryConfig {
    final public static String CACHE_STORE_TYPE_JDBC = "jdbc";
    final public static String CACHE_STORE_TYPE_CUSTOM = "custom";


    private int batchSize = CacheJdbcPojoStoreFactory.DFLT_BATCH_SIZE;
    private int maximumPoolSize;
    @NotEmpty
    private String type;
    private String dataSourceBean;
    private String dialect;
    private List<OrmConfig> orms;
    private String cacheStoreClass;

    public String getCacheStoreClass() {
        return cacheStoreClass;
    }

    public void setCacheStoreClass(String cacheStoreClass) {
        this.cacheStoreClass = cacheStoreClass;
    }

    public static boolean checkSupport(String cacheStoreType) {
        return CACHE_STORE_TYPE_JDBC.equals(cacheStoreType) || CACHE_STORE_TYPE_CUSTOM.equals(cacheStoreType);
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSourceBean() {
        return dataSourceBean;
    }

    public void setDataSourceBean(String dataSourceBean) {
        this.dataSourceBean = dataSourceBean;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public List<OrmConfig> getOrms() {
        return orms;
    }

    public void setOrms(List<OrmConfig> orms) {
        this.orms = orms;
    }

    public Factory<? extends CacheStore> buildCacheStoreFactory(String cacheName) {
        if (!checkSupport(type)) {
            throw new IllegalStateException("unsupported cacheStoreType " + type);
        }

        if(type.equals(CACHE_STORE_TYPE_CUSTOM)){
            if(cacheStoreClass == null || "".equals(cacheStoreClass)){
                throw new IllegalStateException("cacheStoreClass required for custom cacheStoreFactory");
            }

            try {
                ClassUtils.forName(cacheStoreClass,null);

                return FactoryBuilder.factoryOf(cacheStoreClass);

            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }


        } else if (type.equals(CACHE_STORE_TYPE_JDBC)) {
            if (dialect == null || "".equals(dialect)) {
                throw new IllegalStateException("dialect required for CacheJdbcPojoStoreFactory");
            }

            CacheJdbcPojoStoreFactory factory = new CacheJdbcPojoStoreFactory();

            factory.setDialect(mapDialect(dialect));

            if (batchSize > 0) {
                factory.setBatchSize(batchSize);
            }

            if (maximumPoolSize > 0) {
                factory.setMaximumPoolSize(maximumPoolSize);
            }


            if (null != dataSourceBean && !"".equals(dataSourceBean)) {

                factory.setDataSourceBean(dataSourceBean);
            } else {
                factory.setDataSourceFactory(new DataSourceFactory(dataSourceBean));
            }

            factory.setTypes(buildTypes(cacheName));
            return factory;
        }
        return null;
    }


    private JdbcType[] buildTypes(String cacheName) {
        JdbcType[] types = new JdbcType[orms.size()];
        int i = 0;
        for (OrmConfig orm : orms) {
            types[i] = orm.buildJdbcType(cacheName);
            i++;
        }
        return types;
    }

    private JdbcDialect mapDialect(String dialect) {
        switch (dialect.toLowerCase()) {
            case "mysql":
                return new MySQLDialect();
            case "oracle":
                return new OracleDialect();
            case "db2":
                return new DB2Dialect();
            case "h2":
                return new H2Dialect();
            case "sqlserver":
                return new SQLServerDialect();
        }
        throw new IllegalStateException("unsupported dialect " + dialect);
    }
}
