ignite-spring-boot-starter
-----------

## Usage

该 starter 会自动创建类型为 IgniteBean 的 bean，并自动启动该 ignite 实例
具体参考项目:
`jignite/examples/ignite-starter-example`

1.Add pom dependency

```xml

<dependency>
    <groupId>f.s</groupId>
    <artifactId>ignite-spring-boot-starter</artifactId>
    <version>${jignite.version}</version>
</dependency>

```
2.在application.yml 对ignite进行配置:  
参考 [ignite-starter-example/src/main/resources/application.yml](../examples/ignite-starter-example/src/main/resources/application.yml)

That's all.

除此之外，可以提供一个实现了 f.jhandy.ignite.CacheConfigurationCustomizer 接口的 bean 以对缓存配置进行自定义。
