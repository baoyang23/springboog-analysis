# 			SpringBoot 启动流程分析



#### 题记

​     现在做 java 项目，基本都是使用 SpringBoot 这套了, 所以看下 SpringBoot 的启动流程分析是很重要的. 最主要的是,如果以前使用过Spring配置文件的那种，然后你再切换到 SpringBoot 这里面来的话，你就会发现好精致，没有配置文件。 所以阅读SpringBoot的执行流程是非常有必要的.



#### 流程分析

​    SpringApplication.run(SpringBootWorkFlowApplication.class, args);   就这一行代码，那么我们就从这一行代码来分析.  当然啦,类上面是还有一个 @SpringBootApplication 注解的,有兴趣的也可以去看看.

  

##### SpringApplication 构造函数 阅读

```java
public SpringApplication(Class<?>... primarySources) {
   this(null, primarySources);
}
```



先是进入到 SpringApplication 这个构造函数中来.

```java
/**
 * Create a new {@link SpringApplication} instance. The application context will load
 * beans from the specified primary sources (see {@link SpringApplication class-level}
 * documentation for details. The instance can be customized before calling
 * {@link #run(String...)}.
 * @param resourceLoader the resource loader to use
 * @param primarySources the primary bean sources
 * @see #run(Class, String[])
 * @see #setSources(Set)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
   this.resourceLoader = resourceLoader;
// 检验下 primarySources 是要有值的    
   Assert.notNull(primarySources, "PrimarySources must not be null");
// 转化为 Set 集合,并且使用的是 LinkedHashSet 来确保顺序.    
   this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
   this.webApplicationType = WebApplicationType.deduceFromClasspath();
// 这里的spring.factories 也是可以在我们的自己的项目中定义扩展的.    
// 这里可以定位到 SpringBoot 源码里面,在META-INF文件夹下面,是有一个 spring.factories 文件的.
// 从 spring.factories 文件中获取出配置 key 是Bootstrapper 的value, 然后实例化放入到集合中来.     
   this.bootstrappers = new ArrayList<>(getSpringFactoriesInstances(Bootstrapper.class));
// 从 spring.factories 文件中获取出配置 key 是 ApplicationContextInitializer 的value,然后实例化,放入到 this.initializers 集合中来.    
   setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
// 从 spring.factories 文件中获取出配置 key 是 ApplicationListener 的value,也会实例化,放入到this.listeners集合中来.  
   setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
   this.mainApplicationClass = deduceMainApplicationClass();
}
```

这里是可以看到 SpringApplication 构造函数， 主要是从 spring.factories 文件中根据 key 来获取相应的 value , 并且对获取出来的value 实例化 , 放入到 SpringApplication 中对应集合信息中来.



##### run 方法阅读

```java
/**
 * Run the Spring application, creating and refreshing a new
 * {@link ApplicationContext}.
 * @param args the application arguments (usually passed from a Java main method)
 * @return a running {@link ApplicationContext}
 */
public ConfigurableApplicationContext run(String... args) {
// StopWatch 是 SpringBoot 自己写的计时器.    
   StopWatch stopWatch = new StopWatch();
// 开始计时.    
   stopWatch.start();
//  先是new一个DefaultBootstrapContext,然后迭代bootstrappers集合,该集合中存放的类信息也就是从spring.factories中根据key是 Bootstrapper 获取出对应的 value , 然后这里调用集合中每个类的intitialize方法,传入DefaultBootstrapContext进去,最后返回回来.   
   DefaultBootstrapContext bootstrapContext = createBootstrapContext();
   ConfigurableApplicationContext context = null;
   configureHeadlessProperty();
    
// 从 spring.factories 中获取出 key 是 SpringApplicationRunListener的value,这里也就是一个org.springframework.boot.context.event.EventPublishingRunListener.
// 所以这里SpringApplicationRunListeners 中有一个 List<SpringApplicationRunListener> listeners 集合,集合中有一个  org.springframework.boot.context.event.EventPublishingRunListener, 而 EventPublishingRunListener 中又有我们的 SpringApplication, 而 SpringApplication 中就又有Listeners , 也就是从 spring.factories 中获取出来 key 是 ApplicationListener 的value 集合.最后将ApplicationListener都给放入到 org.springframework.context.event.SimpleApplicationEventMulticaster中来,SimpleApplicationEventMulticaster也是EventPublishingRunListener的一个属性.
// 也就说明调用 listeners 来传播 event 的话, 最后也就是挨个调用 listener 的每个 onApplicationEvent 方法来传播 event 的.
//这里主要是将spring.factories放入到一个地方,等到传播event的时候,再拿出来用上.    
   SpringApplicationRunListeners listeners = getRunListeners(args);
// 调用 starting 方法来发送事件,
// org.springframework.boot.context.event.EventPublishingRunListener#starting,直接看到这个方法,可以看到的是: 发送了一个 ApplicationStartingEvent 事件.    
   listeners.starting(bootstrapContext, this.mainApplicationClass);
   try {
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
   
// 准备环境.
// 先是获取出 ConfigurableEnvironment 对象
// 再调用 environmentPrepared 方法,来发送 ApplicationEnvironmentPreparedEvent 事件.
// 对 profiles 进行确认.
// 这里主要是对环境配置信息进行确认.       
      ConfigurableEnvironment environment = prepareEnvironment(listeners, bootstrapContext, applicationArguments);
// 配置 spring.beaninfo.ignore 到 System 中来.       
      configureIgnoreBeanInfo(environment);
// 打印 banner , 这里默认打印的是 SpringBoot 当前使用的版本.
// org.springframework.boot.SpringBootBanner , 
// 可以看到整个类的 BANNER 属性,就是我们控制台打印出来的信息.       
      Banner printedBanner = printBanner(environment);
// 创建 context, 这里创建的是这个AnnotationConfigServletWebServerApplicationContext       
      context = createApplicationContext();
// org.springframework.context.support.GenericApplicationContext#setApplicationStartup
// 设置 ApplicationStartup 到 GenericApplicationContext 中以及其父类上去.       
      context.setApplicationStartup(this.applicationStartup);
       
// 设置 environment 到 context 中来. 设置 ApplicationConversionService 到 beanFactory
// 调用 initializers 集合中每个对象的 initialize 方法,也就是调用初始化方法的意思.
// 再发送一个 ApplicationContextInitializedEvent 事件出来.
// 调用 bootstrapContext.close(context) 方法, 意思是 bootstrap的配置结束.
// 注册 springApplicationArguments 到 beanFactory 中来.
// 注册 springBootBanner 到 beanFactory 中来.
// 设置 beanFactory 的 allowBeanDefinitionOverriding 属性是 false.
// 然后发送 ApplicationPreparedEvent 事件出来. 在发送这个事件之前,如果 listener 是 ApplicationContextAware 类型的话,就会调用 setApplicationContext 给 context 给设置进去.       
      prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner);
       
// 这里可以参考之前,分析 Spring 的时候有进行说明到的, 只是不同的是, 这里多了一个 web 的初始化.       
      refreshContext(context);
       
// 暂时没有发现做了什么事情.       
      afterRefresh(context, applicationArguments);
       
// 计时结束.       
      stopWatch.stop();
      if (this.logStartupInfo) {
// 这里给计时用的时间给打印出来.          
         new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
      }
// 发送一个 ApplicationStartedEvent 事件出来.
// 接着再发送一个 AvailabilityChangeEvent 事件出来.       
      listeners.started(context);
// 这里可以看出来是对 runners 进行调用.
// 先是找出 ApplicationRunner / CommandLineRunner 这二种类型的
// 迭代调用其run方法,也算是一种回调了      
      callRunners(context, applicationArguments);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, listeners);
      throw new IllegalStateException(ex);
   }

   try {
// 发送 ApplicationReadyEvent 事件, 接着也会发送一个 AvailabilityChangeEvent 事件出来.       
      listeners.running(context);
   }
   catch (Throwable ex) {
      handleRunFailure(context, ex, null);
      throw new IllegalStateException(ex);
   }
// 最后是返回了一个 Context 回来.    
   return context;
}
```



#### 总结

   可以看到 SpringBoot先是约定,读取配置中的 spring.factories 中设定好的 key 以及 key 对应的 value , 然后对 value 进行初始化,存放再 SpringApplication中后面需要使用到的.  注意,这里面还是有配置 监听器的,然后发送 event 出来的时候, 这些监听器就会拿到 event .

   接着就是 SpringApplicationRunListeners 中 有 EventPublishingRunListener 对象, EventPublishingRunListener 中有 SpringApplication , 而 SpringApplication 中又有我们在 spring.factories 中先配置好的 Listener .

   发送 ApplicationStartingEvent 事件,  准备好 environment 信息, 打印定义的 banner 出来. 在 prepareContext 方法中,有对spring.factories 中配置的 Initializer 来进行调用 initialize 方法. 发送 ApplicationContextInitializedEvent 事件出来. 接着发送 ApplicationPreparedEvent 事件.

   refreshContext 方法最后调用的就是 refresh 方法，对比之前的 refresh 的话，这里是多了 create tomcat 等 web 环境的操作.

   接下来发送 ApplicationStartedEvent / AvailabilityChangeEvent  /  ApplicationReadyEvent 以及 回调  ApplicationRunner 和 CommandLineRunner 读取到的子类，就是会调用其 runner 方法. 