package f.jhandy.ignite.cache.store;

import org.apache.ignite.resources.SpringApplicationContextResource;
import org.springframework.context.ApplicationContext;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;

/**
 * @author sunmoonone
 * @version 2018/12/27
 */
public class DataSourceFactory implements Factory<DataSource>{

    private static final long serialVersionUID = 1L;

    @SpringApplicationContextResource
    private transient Object appCtx;

    private String dataSourceBean;

    public DataSourceFactory(){}

    public DataSourceFactory(String dataSourceBean){
        this.dataSourceBean= dataSourceBean;
    }

    public String getDataSourceBean() {
        return dataSourceBean;
    }

    public void setDataSourceBean(String dataSourceBean) {
        this.dataSourceBean = dataSourceBean;
    }

    @Override
    public DataSource create() {
        ApplicationContext context = (ApplicationContext) appCtx;
        if(null!=dataSourceBean && !"".equals(dataSourceBean)){
            return context.getBean(dataSourceBean,DataSource.class);
        }

        if(context.getBeansOfType(DataSource.class).size()>1){
            throw new IllegalStateException("multiple datasource bean found");
        }
        return context.getBean(DataSource.class);
    }

}
