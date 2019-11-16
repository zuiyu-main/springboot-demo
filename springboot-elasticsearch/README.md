# springboot-demo
基于springboot后台框架，涉及技术，通用mapper，shiro，mysql，redis
## es模块

最近在学习es，起码要先有个es环境吧，然后再是整合到代码中使用一下，毕竟只有实践才会有深刻的记忆，这就是所谓的经验啊，下面开始吧，本文分两部分，第一部分配置es环境，第二部分整合到springboot中进行简单的操作，本人也是初次学习，如有错误欢迎指出修正，
本文示例代码不严谨，仅供参考
一、 docker 安装es
1、 拉取镜像
```
# 搜索镜像
docker search elasticsearch


# 拉取镜像
docker pull elasticsearch
```
2、创建本地映射文件，我的本地es映射主目录是/docker/es

```
cd /docker/es
mkdir data
mkdir plugins
mkdir config
cd config
vim es.yml

```
3、es.yml文件内容如下

```
cluster.name: elasticsearch-cluster
node.name: es-node1
network.bind_host: 0.0.0.0
network.publish_host: localhost
http.port: 9200
transport.tcp.port: 9300
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true 
node.data: true  
```

4、创建并启动容器

```
docker run -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -d  -p 9200:9200 -p 9300:9300 -p 5601:5601 --restart=always -v /docker/es/config/es.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /docker/es/plugins1:/usr/share/elasticsearch/plugins  -v /docker/es/data1:/usr/share/elasticsearch/data --name my_es elasticsearch
   
# 命令解析
-e 指定运行内存
-d 后台运行
-p 指定端口
--restart=always 启动docker自动启动容器
-v 指定映射文件
--name 容器名称
最后的elasticsearch是刚才拉取的镜像名称
```
5、浏览器插件访问es，telnet9200可以通说明都可以说明搭建成功， http://127.0.0.1:9200/
二、 整合springboot里面去
1、pom.xml

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
            <version>2.1.3.RELEASE</version>
        </dependency>
```
2、application.yml

```
spring：
  data:
    elasticsearch:
      cluster-name: elasticsearch-cluster
      cluster-nodes: 127.0.0.1:9300
      repositories:
        enabled: true

```
3、 ElasticSearchConfig

```
@SpringBootConfiguration
public class ElasticSearchConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
```
4、 bean

```
ESDocument
@Data
@Document(indexName = "poms", type = "content")
public class ESDocument {
    @Id
    private String id;

    @Field(analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;

    private String projectId;

    public ESDocument(String id, String name, String projectId) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
    }
        public ESDocument() {

        }
}
/**
 * @Document 作用在类，标记实体类为文档对象，一般有两个属性
 * indexName：对应索引库名称
 * type：对应在索引库中的类型
 * shards：分片数量，默认5
 * replicas：副本数量，默认1
 * @Id 作用在成员变量，标记一个字段作为id主键
 * @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：
 * type：字段类型，是枚举：FieldType，可以是text、long、short、date、integer、object等
 * text：存储数据时候，会自动分词，并生成索引
 * keyword：存储数据时候，不会分词建立索引
 * Numerical：数值类型，分两类
 * 基本数据类型：long、integer、short、byte、double、float、half_float
 * 浮点数的高精度类型：scaled_float
 * 需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原。
 * Date：日期类型
 * elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long，节省空间。
 * index：是否索引，布尔类型，默认是true
 * store：是否存储，布尔类型，默认是false
 * analyzer：分词器名称，这里的ik_max_word即使用ik分词器
 * 
*/
```
5、DocumentSearchService

```
    void save(ESDocument esDocument);
    void delete(String id);
    void getById(String id);
    void getByName(String name,String projectId);
```
6.DocumentSearchServiceImpl

```
    @Autowired
    private DocumentSearchRepository documentSearchRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

@Override
    public void save(ESDocument esDocument) {
        ESDocument save = documentSearchRepository.save(esDocument);
        System.out.println(save.toString());
    }

    @Override
    public void delete(String id) {
        documentSearchRepository.deleteById(id);
    }

    @Override
    public void getById(String id) {
        ESDocument esDocument = documentSearchRepository.findById(id).orElse(new ESDocument());
        System.out.println(esDocument.toString());

    }

    @Override
    public void getByName(String name, String projectId) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("name",name)).withQuery(matchQuery("projectId",projectId)).build();
        List<ESDocument> esDocuments = elasticsearchTemplate.queryForList(searchQuery, ESDocument.class);
        esDocuments.forEach(e-> System.out.println(e.toString()));
    }


```
7、DocumentSearchRepository

```
@Repository
public interface DocumentSearchRepository extends ElasticsearchRepository<ESDocument,String> {
}

```
8、test

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTests {

    @Autowired
    private DocumentSearchService documentSearchService;

    @Test
    public void save() {
      documentSearchService.save(new ESDocument(UUID.randomUUID().toString(),"name1","1"));
    }
    @Test
    public void getById(){
        documentSearchService.getById("98c717e2-0e17-4887-86f6-e9cd347f97f7");
    }
    @Test
    public void getByName(){
        documentSearchService.getByName("name1","1");
    }
    @Test
    public void delete(){
        documentSearchService.delete("98c717e2-0e17-4887-86f6-e9cd347f97f7");
    }

}
```
9、到这基本整合就结束了，展示了一个增删查，没有修改，修改本人暂时也没有看。，待后续学习继续分享经验吧，下面说一下整合过程遇到的坑
### 坑1
##### 报错信息

```
java.lang.IllegalStateException: Failed to load ApplicationContext

	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:125)
	at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:108)
	at org.springframework.test.context.web.ServletTestExecutionListener.setUpRequestContextIfNecessary(ServletTestExecutionListener.java:190)
	at org.springframework.test.context.web.ServletTestExecutionListener.prepareTestInstance(ServletTestExecutionListener.java:132)
	at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:246)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.createTest(SpringJUnit4ClassRunner.java:227)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner$1.runReflectiveCall(SpringJUnit4ClassRunner.java:289)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.methodBlock(SpringJUnit4ClassRunner.java:291)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:246)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'elasticsearchClient' defined in class path resource [org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.elasticsearch.client.transport.TransportClient]: Factory method 'elasticsearchClient' threw exception; nested exception is java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:590)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1247)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1096)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:535)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:495)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:317)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:315)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:759)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:869)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:762)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:398)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:330)
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:139)
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:99)
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:117)
	... 24 more
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.elasticsearch.client.transport.TransportClient]: Factory method 'elasticsearchClient' threw exception; nested exception is java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:582)
	... 41 more
Caused by: java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]
	at io.netty.util.NettyRuntime$AvailableProcessorsHolder.setAvailableProcessors(NettyRuntime.java:51)
	at io.netty.util.NettyRuntime.setAvailableProcessors(NettyRuntime.java:87)
	at org.elasticsearch.transport.netty4.Netty4Utils.setAvailableProcessors(Netty4Utils.java:85)
	at org.elasticsearch.transport.netty4.Netty4Transport.<init>(Netty4Transport.java:140)
	at org.elasticsearch.transport.Netty4Plugin.lambda$getTransports$0(Netty4Plugin.java:93)
	at org.elasticsearch.client.transport.TransportClient.buildTemplate(TransportClient.java:177)
	at org.elasticsearch.client.transport.TransportClient.<init>(TransportClient.java:257)
	at org.springframework.data.elasticsearch.client.TransportClientFactoryBean$SpringDataTransportClient.<init>(TransportClientFactoryBean.java:234)
	at org.springframework.data.elasticsearch.client.TransportClientFactoryBean.buildClient(TransportClientFactoryBean.java:103)
	at org.springframework.data.elasticsearch.client.TransportClientFactoryBean.afterPropertiesSet(TransportClientFactoryBean.java:98)
	at org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration.elasticsearchClient(ElasticsearchAutoConfiguration.java:59)
	at org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration$$EnhancerBySpringCGLIB$$18bf6a65.CGLIB$elasticsearchClient$0(<generated>)
	at org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration$$EnhancerBySpringCGLIB$$18bf6a65$$FastClassBySpringCGLIB$$49c9363b.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:228)
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:361)
	at org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration$$EnhancerBySpringCGLIB$$18bf6a65.elasticsearchClient(<generated>)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154)
	... 42 more
```
##### 解决
这个错误解决的时候主要是百度的下面这一行，然后处理思路是把netty的一个东西关闭，具体还没有深入了解

```
java.lang.IllegalStateException: availableProcessors is already set to [8], rejecting [8]

解决
添加配置类
@SpringBootConfiguration
public class ElasticSearchConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
```

### 坑2
##### 报错信息

```
NoNodeAvailableException[None of the configured nodes are available: [{#transport#-1}{b1p9-W8ISSK9ibEqyd3bgg}{127.0.0.1}{127.0.0.1:9200}]
]
	at org.elasticsearch.client.transport.TransportClientNodesService.ensureNodesAreAvailable(TransportClientNodesService.java:347)
	at org.elasticsearch.client.transport.TransportClientNodesService.execute(TransportClientNodesService.java:245)
	at org.elasticsearch.client.transport.TransportProxyClient.execute(TransportProxyClient.java:59)
	at org.elasticsearch.client.transport.TransportClient.doExecute(TransportClient.java:366)
	at org.elasticsearch.client.support.AbstractClient.execute(AbstractClient.java:408)
	at org.elasticsearch.client.support.AbstractClient.execute(AbstractClient.java:397)
	at org.elasticsearch.client.support.AbstractClient$IndicesAdmin.execute(AbstractClient.java:1250)
	at org.elasticsearch.client.support.AbstractClient$IndicesAdmin.exists(AbstractClient.java:1272)
	at org.springframework.data.elasticsearch.core.ElasticsearchTemplate.indexExists(ElasticsearchTemplate.java:650)
	at org.springframework.data.elasticsearch.core.ElasticsearchTemplate.createIndexIfNotCreated(ElasticsearchTemplate.java:945)
	at org.springframework.data.elasticsearch.core.ElasticsearchTemplate.createIndex(ElasticsearchTemplate.java:189)
	at com.cxt.demo.es.EsTests.contextLoads(EsTests.java:19)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:73)
	at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:83)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)

```
##### 解决
出现这个问题一般就是查看端口，集群名称，ip地址是否写对，这里有个地方 需要注意，
<font color="#FF0000">java链接端口默认9300，不是es的9200</font> 

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190724090522300.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)然后其他注意的地方就是cluster-name 这个名称要和es.yml配置文件中cluster.name名称一致
,即都elasticsearch-cluster

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190724090445665.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019072409050498.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)

博客地址

【https://blog.csdn.net/C1041067258/article/details/97035765】
* 过滤显示字段类似mysql select id
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(null, new String[]{"permission"});  //查询结果不返回content
        new NativeSearchQueryBuilder().withSourceFilter(fetchSourceFilter)
