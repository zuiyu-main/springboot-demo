
* term学习
[term学习官网资料](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_finding_multiple_exact_values.html)
## es模块 Rest-High-Level-Client

最近在学习es，起码要先有个es环境吧，然后再是整合到代码中使用一下，毕竟只有实践才会有深刻的记忆，这就是所谓的经验啊，下面开始吧，本文分两部分，第一部分配置es环境，第二部分整合到springboot中进行简单的操作，本人也是初次学习，如有错误欢迎指出修正，
本文示例代码不严谨，仅供参考
一、 docker 安装es
1、 拉取镜像
```
# 搜索镜像
docker search elasticsearch


# 拉取镜像
docker pull elasticsearch:7.4.0
```

2、创建并启动容器

```
docker run -d --name elasticsearch_7.4 --net somenetwork -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -v /cxt/docker/es/es_7.4.0/es1.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:7.4.0
   
# 命令解析
-e 指定运行内存
-d 后台运行
-p 指定端口
--restart=always 启动docker自动启动容器
-v 指定映射文件
--name 容器名称
最后的elasticsearch是刚才拉取的镜像名称

```
3. es1.yml
```
cluster.name: elasticsearch-cluster
node.name: es-node1
#network.host: 0.0.0.0
network.bind_host: 0.0.0.0
network.publish_host: localhost
http.port: 9200
transport.tcp.port: 9300
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true 
node.data: true  
discovery.zen.ping.unicast.hosts: ["localhost:9300","localhost:9301"]
discovery.zen.minimum_master_nodes: 1
```
4、浏览器插件访问es，telnet9200可以通说明都可以说明搭建成功， http://127.0.0.1:9200/
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

### 坑3 
在集成高版本的es中，boot，data版本都升级报错信息如下
```text
java.lang.IncompatibleClassChangeError: Found class org.elasticsearch.common.bytes.BytesReference, but interface was expected
```
##### 解决就是pom中引入es的版本改为7.6.2 就可以了
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190724090522300.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)然后其他注意的地方就是cluster-name 这个名称要和es.yml配置文件中cluster.name名称一致
,即都elasticsearch-cluster

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190724090445665.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019072409050498.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0MxMDQxMDY3MjU4,size_16,color_FFFFFF,t_70)

* 基础配置 elasticsearch +springData
[https://docs.spring.io/spring-data/elasticsearch/docs/3.2.0.RELEASE/reference/html/](https://docs.spring.io/spring-data/elasticsearch/docs/3.2.0.RELEASE/reference/html/)

* 最新配置
[https://docs.spring.io/spring-data/elasticsearch/docs/4.0.1.RELEASE/reference/html/#elasticsearch.operations.template](https://docs.spring.io/spring-data/elasticsearch/docs/4.0.1.RELEASE/reference/html/#elasticsearch.operations.template)

* IK分词器的安装

[https://github.com/medcl/elasticsearch-analysis-ik 注意需要与es版本一致的分词器](https://github.com/medcl/elasticsearch-analysis-ik)

* pinyin分词器安装

[https://github.com/medcl/elasticsearch-analysis-pinyin 需要与安装es版本保持一致](https://github.com/medcl/elasticsearch-analysis-pinyin)

* 自定义 分词器 ik+pinyin

[https://mp.weixin.qq.com/s/JhHBQsaQZdPL9cqPxXS2Ww](https://mp.weixin.qq.com/s/JhHBQsaQZdPL9cqPxXS2Ww)

