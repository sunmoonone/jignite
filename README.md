# jignite-handy
项目主要提供了一些方便使用的配置类和IgniteBean

## 概念说明
ignite提供了丰富的分布式内存使用场景，通常一个ignite实例或集群可以管理多个cache,一个cache可以对应一个数据库表或者一个java model。
默认情况下，ignite将所有的cache存储到一个默认数据分区里(dataRegion)。
也可以将不同的cache存到不同的dataRegion里，jignite-handy采用的是将不同cache存到不同dataRegion的方式。

ignite主要有两个配置类：  
实例配置类：IgniteConfiguration 对应jignite-handy 里的IgniteProperties 或　CacheInstanceProperties  
缓存配置类：CacheConfiguration 对应jignite-handy 里的 CacheConfig, 因为缓存配置的元素很多，CacheConfig又嵌套了若干其他配置类。

## 使用说明

该使用说明适用于不使用 ignite-cache-spring-boot-starter 而只是用 jignite-handy 的场景。  
由于ignite-cache-spring-boot-starter 跟其他的CacheManager 实现是互斥的，当程序中使用了其他的cacheManager的时候（比如redis)
可以使用如下的方式来使用ignite cache。

**推荐使用**　ignite-spring-boot-starter

1.首先对ignite进行配置:

以下示例以ignite作为属性配置前缀，可自由设定：
```yaml
ignite:
  #igniteInstanceName: "igniteInstance"
  #peerClassLoadingEnabled: true
  #clientMode: false
  ipFinder:
    multicast:
      multicastGroup: "228.10.10.161"
    #kubernetes:
    #  namespace: xjs
    #  serviceName: igite
    #  masterUrl: https://kubernetes.default.svc.cluster.local:443
    #  accountToken: /var/run/secrets/kubernetes.io/serviceaccount/token
    #zookeeper:
    #  zkConnectionString: "localhost:2181"
    #  zkNodeRootPath: "/apacheIgnite"
    #  zkServiceBasePath: "/services/ignite"
  caches:
    userCache:
      #onHeap: false
      #maxMemSize: 102400000
      #cacheMode: PARTITIONED
      #indexKeyType: AffinityKey
      indexKeyType: Long
      indexValueType: f.example.ignite.cache.starter.user.model.User

      cacheStoreFactory:
        type: jdbc
        #default
        dataSourceBean: dataSource
        dialect: mysql
        orms:
          - schema: auth
            table: user
            model: f.example.ignite.cache.starter.user.model.User
            fields:
              id:
                pk: true
                column: id
                sqlType: bigint
              nick:
                column: nick
                sqlType: varchar
              head:
                column: head
                sqlType: varchar
              gender:
                column: gender
                sqlType: tinyint
    uservoCache:
      indexKeyType: long
      indexValueType: f.example.ignite.cache.starter.user.model.UserVo
      cacheStoreFactory:
        type: custom
        cacheStoreClass: f.example.ignite.cache.starter.service.UserVoCacheStore
    goodsCache:
      expiryPolicy:
        name: created
        timeout: 3000
      evictionPolicy:
        name: none
    orderCache:
      onHeap: true
      evictionPolicy:
        name: fifo
        maxEntryCount: 200
        maxMem: 4096000
    default:
      evictionPolicy:
        name: lru
```
#### 集群发现
集群节点的互相发现包括multicast,zookeeper,kubernetes和staticip方式  
使用multicast方式时，将multicastGroup设置为不同的组播组ip可以隔离不同的集群;  
使用zookeeper方式时，将 (zkNodeRootPath, zkServiceBasePath)同时设置成不同的值可以隔离不同的集群。  
kubernetes方式参考文档: [kubernetes deployment](https://apacheignite.readme.io/docs/kubernetes-deployment)

### 缓存配置
可以配置多个缓存，为了查询性能，需要给缓存配置缓存索引，可通过indexKeyType 和　indexValueType配置索引，注意这两个配置项要同时使用。

#### cacheStoreFactory 
cacheStoreFactory 在需要使用缓存自动对数据库加速的时候才使用，自动加速指的是读取缓存时若缓存中没有则自动读取对应数据表，写入缓存时自动更新数据库。  
注意自动加速时使用select语句只能查询缓存中已有的数据。   

cacheStoreFactory的配置目前支持 jdbc 和 custom 两种配置，参考上文 userCache 和 uservoCache 的配置。  
userCache使用的是jdbc的方式，该方式使用ignite自带的cacheStore实现CacheJdbcPojoStore，用户需要提供dataSourceBean等信息。  
uservoCache采用的是自定义方式，需要提供cacheStore实现类。  

自动加速目前不支持[Affinity Collocation](https://apacheignite.readme.io/docs/affinity-collocation)。

#### sql join查询
针对cache的sql join查询为了性能和准确性建议使用 [Affinity Collocation](https://apacheignite.readme.io/docs/affinity-collocation)，并设置cacheMode为 REPLICATED。
参考：[SqlQueriesExample.java](https://github.com/apache/ignite/blob/master/examples/src/main/java/org/apache/ignite/examples/sql/SqlQueriesExample.java)

#### evictionPolicy 删除策略 
删除策略在内存达到使用限制的时候触发。如果策略是fifo的时候要使用onHeap模式。

#### expiryPolicy 过期策略 

```yaml
someCache:
  timeout: 300

# or
someCache:
   expiryPolicy:
     timeout: 300
     name: touched

```
timeout 单位为秒  
如果使用第一种方式，即只提供timeout，则默认使用created模式，根据创建时间来判断是否过期；  
如果使用第二种方式，可以在提供timeout的同时，还需指定使用什么模式，支持的模式见下表：  
如果两种方式都配置，优先选择第二种方式。

expire policy name    | creation time |	last access time  |	last update time
----------------------|---------------|-------------------| ----------------
created               |  used         |                   |
accessed              |  used         | used              |
modified              |  used         |                   |  used
touched               |  used         | used              |  used



2.然后读取配置，构造ignite实例

创建配置类：

```java
@Component
@ConfigurationProperties(prefix="ignite")
class SubIgniteProperties extends CacheInstanceProperties{
    //通常类内部不需要写逻辑
}
```

3.在SpringbootApplication里配置ignite bean:

```java
@SpringBootApplication
public class IgniteSpringCacheTestApplication {
    
    @Bean
    public IgniteBean buildIgnite(CacheInstanceProperties cacheInstanceProperties){
        return new IgniteBean(cacheInstanceProperties);
    }
    
    //or
    //@Bean
    //public IgniteBean buildIgnite(CacheInstanceProperties cacheInstanceProperties, CacheConfigurationCustomizer customizer){
    //    return new IgniteBean(cacheInstanceProperties);
    //}
}
```

4.在service类里使用：

```java
@Service
public class MyService{
    
    @Autowired
    IgniteBean igniteBean;

    public User getUser(Long uid){
        return (User)igniteBean.cache("userCache").get(uid);
    }
}
```
