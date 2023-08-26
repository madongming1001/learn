# spring假设去掉二级缓存？

如果去掉了二级缓存，则需要直接在 `singletonFactory.getObject()` 阶段初始化完毕，并放到一级缓存中。

![c7df46e575e7ea06ce02bd741d85b616.png](https://img-blog.csdnimg.cn/img_convert/c7df46e575e7ea06ce02bd741d85b616.png)

那有这么一种场景，B 和 C 都依赖了 A。要知道在有代理的情况下 `singletonFactory.getObject()` 获取的是代理对象。

![7bcf852475b501c0dcd46c5be78e17a9.png](https://img-blog.csdnimg.cn/img_convert/7bcf852475b501c0dcd46c5be78e17a9.png)

而多次调用 `singletonFactory.getObject()` 返回的代理对象是不同的，就会导致 B 和 C 依赖了不同的 A。那如果获取 B 到之后直接放到一级缓存，然后 C 再获取呢？对于普通的bean没有影响，但对于AOP代理的bean会导致重复创建bean实例，违法了单例原则。



# spring假设去掉三级缓存？

去掉三级缓存之后，Bean 直接创建 **earlySingletonObjects**， 看着好像也可以。如果有代理的时候，在 earlySingletonObjects 直接放代理对象就行了。但是会导致一个问题：**在实例化阶段就得执行后置处理器，判断有AnnotationAwareAspectJAutoProxyCreator 并创建代理对象**。这么一想，是不是会对 Bean 的生命周期有影响。同样，先创建 singletonFactory 的好处就是：在真正需要实例化的时候，再使用 singletonFactory.getObject() 获取 Bean 或者 Bean 的代理。相当于是延迟实例化。

**AnnotationAwareAspectJAutoProxyCreator后置处理器**

![img](https://pic3.zhimg.com/80/v2-8eeeb324b3b527f7808925986c12568c_720w.jpg?source=1940ef5c)

如果单纯为了解决循环依赖问题，那么使用二级缓存足够解决问题，三级缓存存在的意义是为了避免代理，如果没有代理对象，二级缓存足够解决问题。

### Spring 2.6默认去除循环依赖

circular refernences prohibited by default，也可以通过参数开启

```java
spring.main.allow-circular-references=true  
```



## 循环依赖+动态代理

![6ff44764aade35144ef61879e129b47a.png](https://img-blog.csdnimg.cn/img_convert/6ff44764aade35144ef61879e129b47a.png)

**存在提前暴露的情况下，比如Bean A的情况，最后才会把二级缓存的对象赋值给当前bean然后返回。**

```java
// 6. 存在提前曝光情况下
if (earlySingletonExposure) {
   // earlySingletonReference：二级缓存，缓存的是经过提前曝光提前Spring AOP代理的bean 经过实例化 没有经过属性填充和初始化
   Object earlySingletonReference = getSingleton(beanName, false);
   // earlySingletonReference只有在检测到有循环依赖的情况下才会不为空
   if (earlySingletonReference != null) {
      // exposedObject跟bean一样，说明初始化操作没有应用Initialization后置处理器(指AOP操作)改变exposedObject
      // 主要是因为exposedObject如果提前代理过，就会跳过Spring AOP代理，所以exposedObject没被改变，也就等于bean了
      // 如果exposedObject没有在初始化方法中改变，说明没有被增强
      if (exposedObject == bean) {
         // 将二级缓存中的提前AOP代理的bean赋值给exposedObject，并返回
         //自动注入是空的
         exposedObject = earlySingletonReference;
      }
      // 引用都不相等了，也就是现在的bean已经不是当时提前曝光的bean了
      else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
         // dependentBeans也就是B, C, D
         String[] dependentBeans = getDependentBeans(beanName);
         Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
         for (String dependentBean : dependentBeans) {
            if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
               actualDependentBeans.add(dependentBean);
            }
         }
         //被依赖检测异常
         //因为bean创建后所依赖的bean一定是已经创建的
         //actualDependentBeans不为空则表示当前bean创建后其依赖的bean却没有全部创建完，也就说存在循环依赖
         if (!actualDependentBeans.isEmpty()) {
            throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been " + "wrapped. This means that said other beans do not use the final version of the " + "bean. This is often the result of over-eager type matching - consider using " + "'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.");
         }
      }
   }
}
```

```java
createBean()
//遇到Aop的 BeanPostProcessor 的话就findCandidateAdvisors找到所有的advisor放到容器中，advisor指的是spring中封装pointcut和advisor的对象，这里不会对具体需要代理的对象创建代理类，而是有一个插口（getCustomTargetSource）让我们可以做，但一般不会做。
标注@Aspectj的对象是在shouldSkip方法返回null，而普通的bean在方法结束返回null，因为没有自定义targetSource
@Nullable
protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
   Object bean = null;
   if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
      // Make sure bean class is actually resolved at this point.
      if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
         Class<?> targetType = determineTargetType(beanName, mbd);
         if (targetType != null) {
            //AbstractAutoProxyCreator
            bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
            if (bean != null) {
               bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
            }
         }
      }
      mbd.beforeInstantiationResolved = (bean != null);
   }
   return bean;
}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
		Object cacheKey = getCacheKey(beanClass, beanName);

		//1.不需要创建代理
		if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
			if (this.advisedBeans.containsKey(cacheKey)) {
				return null;
			}
			//2.如果是基础设施或者应该跳过的类，则说明这个类不需要创建代理，缓存起来
			//默认shouldSkip是false，都不应该跳过
			//但是AspectJAwareAdvisorAutoProxyCreator实现了该方法
			//解析所有@Aspectj标注的对象里面的每个通知连接点称为advisor对象
			//Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class
			if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
				this.advisedBeans.put(cacheKey, Boolean.FALSE);
				return null;
			}
		}

		// Create proxy here if we have a custom TargetSource.
		// Suppresses unnecessary default instantiation of the target bean:
		// The TargetSource will handle target instances in a custom fashion.
		// 2.对于自定义的 TargetSource 会立即创建代理，并缓存
		TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
		if (targetSource != null) {
			if (StringUtils.hasLength(beanName)) {
				this.targetSourcedBeans.add(beanName);
			}
			Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
			Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
			this.proxyTypes.put(cacheKey, proxy.getClass());
			return proxy;
		}

		return null;
	}
```





```java
 <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:
 * <ol>Bean工厂实现应该支持标准的Bean生命周期接口尽可能地。整套初始化方法及其标准顺序为：
 * <li>BeanNameAware's {@code setBeanName}
 * <li>BeanClassLoaderAware's {@code setBeanClassLoader}
 * <li>BeanFactoryAware's {@code setBeanFactory}
 * <li>EnvironmentAware's {@code setEnvironment}
 * <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * <li>ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)
 * <li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)
 * <li>MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)
 * <li>ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)
 * <li>ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)
 * <li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * <li>InitializingBean's {@code afterPropertiesSet}
 * <li>a custom init-method definition
 * <li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
 * </ol>
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:
 * <ol>
 * <li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * <li>DisposableBean's {@code destroy}
 * <li>a custom destroy-method definition
 * </ol>
```

```java
// Invoke factory processors registered as beans in the context.
invokeBeanFactoryPostProcessors(beanFactory);
InternalConfigurationAnnotationProcessor
ConfigurationClassPostProcessor帮助我们做了注解识别和解析的过程
ConfigurationClassParser#doProcessConfigurationClass
//@Component
//@PropertySources
//@ComponentScans
//@ComponentScan
//@ImportResource

//配置文件读取是GenericBeanDefinition
//注解扫描是ScannedGenericBeanDefintion
//父类AbstractBeanDefinition
//AnnotatedBeanDefinition有这个注解元数据信息  private final AnnotationMetadata metadata;

```

#### ConfigurationClassPostProcessor

```java
@SpringBootApplication  -> @EnableAutoConfiguration  -> @Import(AutoConfigurationImportSelector.class) 就是在 ConfigurationClassPostProcessor去解析加载的
```

#### https://juejin.cn/post/6844903944146124808

![image-20211203170110834](noteImg/image-20211203170110834.png)

![image-20211203153845270](noteImg/image-20211203153845270.png)

![image-20211203210201852](noteImg/image-20211203210201852.png)

## Bean名称生成策略

![image-20211203155749557](noteImg/image-20211203155749557.png)





启动流程

![image-20211203221333010](noteImg/image-20211203221333010.png)

## BeanPostProcessor关键实现类

![image-20211203224648324](noteImg/image-20211203224648324.png)



## Spring的观察者模式

**refresh()#initApplicationEventMulticaster()**

![image-20211203233156035](noteImg/image-20211203233156035.png)

![image-20211203233700666](noteImg/image-20211203233700666.png)

```txt
initialMulticaster（springboot的监听器）
applicationEventMulticaster（spring上下文监听器）
```



# Spring Aop

**入口**

```java
@EnableAspectJAutoProxy -> AspectJAutoProxyRegistrar
注册一个 AnnotationAwareAspectJAutoProxyCreator.java
```

**优先级**

```java
//AopConfigUtils.class 拿到数组下标最大的
static {
   // Set up the escalation list...
   APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
   APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
   APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
}

				int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
				int requiredPriority = findPriorityForClass(cls);
				if (currentPriority < requiredPriority) {
					apcDefinition.setBeanClassName(cls.getName());
				}

	private static int findPriorityForClass(@Nullable String className) {
		for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
			Class<?> clazz = APC_PRIORITY_LIST.get(i);
			if (clazz.getName().equals(className)) {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"Class name [" + className + "] is not a known auto-proxy creator class");
	}
```

## Advice的执行顺序

```java
success
// Around advice start exec ...
// Before advice exec ...
// 方法 A 执行
// AfterReturning advice exec ...
// After advice exec ...
// Around advice end exec ...

throwable
// Around advice start exec ...
// Before advice exec ...
// AfterThrowing advice exec ...
// After advice exec ...d
// java.lang.ArithmeticException: / by zero
// Around advice end exec ...
```

**注：可以通过Order注解或者实现Order接口修改优先级，数越大优先级越低**

**可以把 Spring AOP 想象成一个同心圆。被增强的原始方法在圆心，每一层 AOP 就是增加一个新的同心圆。同时，优先级最高的在最外层。方法被调用时，从最外层按照 AOP1、AOP2 的顺序依次执行 around、before 方法，然后执行 method 方法，最后按照 AOP2、AOP1 的顺序依次执行 after 方法**。

![图片](https://mmbiz.qpic.cn/mmbiz_png/Qooo5wPkibGrxHm3hNHbVsbrkxOUJ6M6rTYXRsUuYmDbGic0QHiaEUHfN2yUvOQM0lmlFOQdjLSibUenoGTC9PDia2A/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

## Aop调用链条组装

```java
由于cglib代理创建的时候callback数组0的位置是 DynamicAdvisedInterceptor，而在调用目标方法的时候都会走到 DynamicAdvisedInterceptor.intercept()，而里面最终就会创建一个 CglibMethodInvocation 对象，把符合的list<Advice>传给构造方法，还有其他参数，代理对象等，接着就会调用 proceed(),由于 CglibMethodInvocation 的 proceed()就是调用父类的 ReflectiveMethodInvocation.proceed(),而父类就会有一个计数器不断的调用list<advice>对应坐标的类，当前 ReflectiveMethodInvocation.proceed()最后面会调用每个advice的invoke方法，并会把当前对象传过去，因为当前proceed()是 CglibMethodInvocation 调用过来的，所以this对象就是他( CglibMethodInvocation),接下来调用的第一个invoke()肯定是 ExposeInvocationInterceptor 的（AbstractAdvisorAutoProxyCreator&findEligibleAdvisors()extendAdvisors()添加的，是从AbstractAutoProxyCreator的warpIfNecessary方法来的），首先会把 CglibMethodInvocation 方到一个threadlocal里面，以保证同线程其他位置可以使用，proceed(this(代指 CglibMethodInvocation))最后会调用
CglibMethodInvocation.proceed()，由于里面是调用父类的proceed(),这是父类的计数器每次调用就会+1，然后还有走到invoke(this),周而复始。各通知类的方法不尽相同，就是会在调用方法的前后穿插自己的逻辑比如 MethodBeforeAdviceInterceptor（）。

XML的时候Advice是以下表作为顺序的，最终在拼出来的chain按照这个顺序
对于拓扑排序结果 只会在Before Around After会出现，现在都是按照默认值来排序的，getAdvisors()方法声明了顺序都是0，默认顺序是 getAdvisorMethods() 
  Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class
先按照注解比较，相同在按照方法名比较


0 = {ExposeInvocationInterceptor@2024} 
1 = AspectJAroundAdvice
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		if (!(mi instanceof ProxyMethodInvocation)) {
			throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
		}
		ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
		// MethodInvocationProceedingJoinPoint.proceed()
		ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
		JoinPointMatch jpm = getJoinPointMatch(pmi);
		return invokeAdviceMethod(pjp, jpm, null, null);
	}

	/**
	 * Return the ProceedingJoinPoint for the current invocation,
	 * instantiating it lazily if it hasn't been bound to the thread already.
	 * @param rmi the current Spring AOP ReflectiveMethodInvocation,
	 * which we'll use for attribute binding
	 * @return the ProceedingJoinPoint to make available to advice methods
	 */
	protected ProceedingJoinPoint lazyGetProceedingJoinPoint(ProxyMethodInvocation rmi) {
		return new MethodInvocationProceedingJoinPoint(rmi);
	}
2 = MethodBeforeAdviceInterceptor
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		return mi.proceed();
	}
3 = AspectJAfterAdvice
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			//调用 ReflectiveMethodInvocation 的proceed()方法
			return mi.proceed();
		}
		finally {
			invokeAdviceMethod(getJoinPointMatch(), null, null);
		}
	}
	@Nullable
	protected JoinPointMatch getJoinPointMatch() {
		MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();//取出 CglibaMethodInvocation
		if (!(mi instanceof ProxyMethodInvocation)) {
			throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
		}
		return getJoinPointMatch((ProxyMethodInvocation) mi);
	}
4 = AfterReturningAdviceInterceptor
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object retVal = mi.proceed();
		this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
		return retVal;
	}
5 = AspectJAfterThrowingAdvice
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			//执行下一个通知/拦截器 methodInvocation 通过调用joinpoint接口的proceed
			return mi.proceed();
		}
		catch (Throwable ex) {
			if (shouldInvokeOnThrowing(ex)) {
				invokeAdviceMethod(getJoinPointMatch(), null, ex);
			}
			throw ex;
		}
	}
	
FastClassInfo
fci.f1 = 被代理对象
fci.f2 = 代理对象
```

**DynamicAdvisedInterceptor.intercept组装6个MethodInterceptor**

```java
1、ExposeInvocatinoInterceptor
2、AspectjAroundAdvice
3、AspectJMethodBeforeAdvice -> MethodBeforeAdviceAdapter -> MethodBeforeAdviceInterceptor
4、AspectJAfterAdvice
5、AspectJAfterReturningAdvice  -> AfterReturningAdviceAdapter->AfterReturningAdviceInterceptor
6、AspectJAfterThrowingAdvice
```

**findCandidateAdvisors()找到了所有的advisor，并把每一个aspectj的切面方法给到了AbstractAspectJAdvice。InstantiationModelAwarePointcutAdvisorImpl**

![image-20230308175427012](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230308175427012.png)

## 注解对应Advice接口

![image-20220701190744135](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220701190744135.png)

```java
只有注解方式声明aop的时候才组装类上面添加@Aspectj
InstantiationModelAwarePointcutAdvisorImpl
内部注解所对应的AspectAdvice对象
AtAround -> AspectJAroundAdvice
AtBefore -> AspectJMethodBeforeAdvice
AtAfter -> AspectJAfterAdvice
AtAfterReturning -> AspectJAfterReturningAdvice
AtAfterThrowing -> AspectJAfterThrowingAdvice
```

![image-20221106231422821](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221106231422821.png)

## DefaultListableBeanFactory类图

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/12b53fb8183c40c2a71b575d9a2d91da~tplv-k3u1fbpfcp-watermark.awebp)

![image-20211210134538464](noteImg/image-20211210134538464.png)

![image-20211210173929956](noteImg/image-20211210173929956.png)

```java
1.创建AspectJPointcutAdvisor#0-4，先使用其带参的构造方法进行对象的创建，但是想使用带参数的构造方法，必须要把参数对象准备好，因此要准备创建内置包含的对象AspectJAroundAdvice

2.创建AspectJAroundAdvice，也需要使用带参的构造方法进行创建，也需要提前准备好具体的参数对象，包含三个参数：

  1. MethodLocatingFactoryBean
  2. AspectJExpressionPointcut
  3. SimpleBeanFactoryAwareAspectInstanceFactory

3.分别创建上述的三个对象，上述三个对象的创建过程都是调用无参的构造方法，直接发射调用即可。
```



## SpringAop运行过程

## 1.调用被代理的方法

Cglib无法代理final修饰的方法和类还有私有方法。

2.找到cglib文件

3.根据var00000找到DynamicAdvisedInterceptor#intercept方法ExposeInvocationInterceptor中间的调用循环结构

适配器是以Interceptor结尾的 其他的是直接继承自MethodInterceptor

![image-20211213210833775](noteImg/image-20211213210833775.png)

![image-20211213225426346](noteImg/image-20211213225426346.png)

![image-20211213225442536](noteImg/image-20211213225442536.png)

![AnnatationAwareAspectJAutoProxyCreator.png](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/11/8/16e4ad7b38872724~tplv-t2oaga2asx-watermark.awebp)

```java
@EnableAspectJAutoProxy 
//注入了一个关键的类 AnnotationAwareAspectJAutoProxyCreator
//两种方式创建代理类
//resolveBeforeInstantiation&postProcessBeforeInstantiation
//initializeBean&postProcessAfterInitialization
```

```java
@Override
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
   if (bean != null) {
      //获取当前bean的key：如果beanName不为空，则以beanName为key，如果为FactoryBean类型，
      //前面还会添加&符号，如果beanName为空，则以当前bean对应的class为key
      Object cacheKey = getCacheKey(bean.getClass(), beanName);
      //判断当前 bean 是否正在被代理，如果正在被代理则不进行封装
      //根据是否提前调用过 getEarlyBeanReference 方法
      //null != bean 说明对象没有提前暴露过 就是没有提前调用getEarlyBeanReference()方法
      //earlyProxyReferences 只有两种情况 一个有值 一个没值
      if (this.earlyProxyReferences.remove(cacheKey) != bean) {
         //如果他需要被代理，则需要封装指定的bean
         return wrapIfNecessary(bean, beanName, cacheKey);
      }
   }
   return bean;
}
```

## 可以被代理的类执行流程

```java
getAdvicesAndAdvisorsForBean()
		findEligibleAdvisors()
    		//根据配置文件以及注解的方式找到所有声明aspectj的切面，然后生成对应的advisor，advisor主要作用是						封装pointcut和advice
				findCandidateAdvisors()
  					//找到所有 implements Advisor 接口的
  					findAdvisorBeans()
     				//注解方式，找到系统中使用@Aspectj标注的Bean，并且找到该bean中使用@Before、@After等标注的							方法，将这些方法封装成一个一个Advisor
	 				  buildAspectJAdvisors()
			  //根据当前类的所有方法找到所有符合的advisors
				findAdvisorsThatCanApply()
			  //向advisors0号位置添加对象ExposeInvocationInterceptor，ExposeInvocationInterceptor 就是					 用来传递MethodInvocation的。在后续的任何下调用链环节，只需要用到当前的MethodInvocation就通过					 ExposeInvocationInterceptor.currentInvocation()静态方法获得 在一个threadlocal中
				extendAdvisors()
			  //按Order接口、@Order注解进行排序 拓扑排序
				  ()
```



# Spring事务

**EnableTransactionManagement的selector 注册类**

```java
TransactionManagementConfigurationSelector.java
AutoProxyRegistrar -> InfrastructureAdvisorAutoProxyCreator

ProxyTransactionManagementConfiguration -> BeanFactoryTransactionAttributeSourceAdvisor
																					 TransactionAttributeSource
																					 TransactionInterceptor
```

**注意⚠️：ExposeInvocationInterceptor是专属于AOP的，事务没有。**

**注意⚠️：如果内层方法出现了异常外层没有捕获，那会使得外层方法也会回滚，影响到了外层方法。 外层方法异常不会影响内层方法的异常。内层是nested的时候。**

当项目中只有事务没有aop的时候链条里面是不会有**ExposeInvocationInterceptor**，因为InfrastructureAdvisor**AutoProxyCreator**和AnnotationAwareAspectJ**AutoProxyCreator**的父类AspectJAwareAdvisorAutoProxyCreator是同级的，而往链条里面添加**ExposeInvocationInterceptor**的方法extendAdvisors()就是在父类里面。

**有两个地方会加载TransactionAttribute!!!**

1、一个是后置处理器Aop的时候会走getAdvicesAndAdvisorsForBean()最里面会走一个叫findAdvisorsThatCanApply(),这个时候会拿类的所有方法和advisor一一匹配有一个匹配上就会认为需要aop，匹配条件是classFilter和MethodMatcher，在methodMatcher的时候就会读取方法的注解信息并放到Map<Object, TransactionAttribute> 里面

2、在执行到拦截器getInterceptorsAndDynamicInterceptionAdvice()的时候会再次调用classFilter和MethodMatch进行比较。

common suffix：**AutoProxyCreator

![image-20221104175940869](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221104175940869.png)

## Advice执行顺序

```java
//未异常
@Around
@Before
business process
@AfterReturning
@After
@Around
//异常
@Around
@Before
business process
@AfterThrowing
@After
@Around
//抛异常
```

Introduction 是对于类级别的切面

ClassFilter

MethodMatcher

## 五种Advice

```java
MethodBeforeAdvice
 
AfterReturningAdvice

ThrowsAdvice

MethodInterceptor

IntroductionInterceptor
```

```java
PointcutAdvisor
ClassFilter getClassFilter();
MethodMatcher getMethodMatcher();
```

```java
//AfterReturningAdviceAdapter
//MethodBeforeAdviceAdapter
//ThrowsAdviceAdapter
```

spirng boot项目的@EnableAspectJAutoProxy 最终会通过Import注册一个AnnotationAwareAspectJAutoProxyCreator类

```java
			//AfterReturningAdviceAdapter AfterReturningAdviceInterceptor
			//MethodBeforeAdviceAdapter   MethodBeforeAdviceInterceptor
			//ThrowsAdviceAdapter         ThrowsAdviceInterceptor       
```
ConfigurationClassPostprocessor.java加载的类多了3个

![image-20220212164550955](/Users/madongming/Library/Application Support/typora-user-images/image-20220212164550955.png)

![image-20220212164641809](/Users/madongming/Library/Application Support/typora-user-images/image-20220212164641809.png)

![image-20220301115638985](/Users/madongming/Library/Application Support/typora-user-images/image-20220301115638985.png)

![image-20220301115730713](/Users/madongming/Library/Application Support/typora-user-images/image-20220301115730713.png)

![image-20220301124750577](/Users/madongming/notes/noteImg/image-20220301124750577.png)

![image-20220301125446023](/Users/madongming/notes/noteImg/image-20220301125446023.png)

![image-20220401224937167](/Users/madongming/notes/noteImg/image-20220401224937167.png)

![image-20220401225005587](/Users/madongming/notes/noteImg/image-20220401225005587.png)



## 事务传播行为

参考文章：https://juejin.cn/post/6844903608224333838

![image-20211216223807073](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20211216223807073.png)

**先简单介绍一下Spring事务的传播行为：**

所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在`TransactionDefinition`定义中包括了如下几个表示传播行为的常量：

- `TransactionDefinition.PROPAGATION_REQUIRED`：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
- `TransactionDefinition.PROPAGATION_REQUIRES_NEW`：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
- `TransactionDefinition.PROPAGATION_SUPPORTS`：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
- `TransactionDefinition.PROPAGATION_NOT_SUPPORTED`：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
- `TransactionDefinition.PROPAGATION_NEVER`：以非事务方式运行，如果当前存在事务，则抛出异常。
- `TransactionDefinition.PROPAGATION_MANDATORY`：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
- `TransactionDefinition.PROPAGATION_NESTED`：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于`TransactionDefinition.PROPAGATION_REQUIRED`。



## Spring事务管理接口介绍

Spring 框架中，事务管理相关最重要的 3 个接口如下：

- **PlatformTransactionManager**： （平台）事务管理器，Spring 事务策略的核心。
- **TransactionDefinition**： 事务定义信息(事务隔离级别、传播行为、超时、只读、回滚规则)。
- **TransactionStatus**： 事务运行状态。

我们可以把 **PlatformTransactionManager** 接口可以被看作是事务上层的管理者，而 **TransactionDefinition** 和 **TransactionStatus** 这两个接口可以看作是事务的描述。

**PlatformTransactionManager** 会根据 **TransactionDefinition** 的定义比如事务超时时间、隔离级别、传播行为等来进行事务管理 ，而 **TransactionStatus** 接口则提供了一些方法来获取事务相应的状态比如是否新事务、是否可以回滚等等。

![image-20220701104752560](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220701104752560.png)

```java
//TransactionDefinition
//隔离级别
//传播行为
//回滚规则
//是否只读
//事务超时

public interface TransactionStatus{
    boolean isNewTransaction(); // 是否是新的事务
    boolean hasSavepoint(); // 是否有恢复点
    void setRollbackOnly();  // 设置为只回滚
    boolean isRollbackOnly(); // 是否为只回滚
    boolean isCompleted; // 是否已完成
}
DataSourceTransactionObject
设置保存点
设置通过TransactionSynchronizationManager&resource属性获取 key DataSource value为ConnectionHolder
```



## spring支持两种事务

### 编程式事务管理

通过 `TransactionTemplate`或者`TransactionManager`手动管理事务，实际应用中很少使用，但是对于你理解 Spring 事务管理原理有帮助。`TransactionTemplate`是执行事务，`PlatformTransactionManager`是提交事务

使用`TransactionTemplate` 进行编程式事务管理的示例代码如下：

```java
@Autowired
private TransactionTemplate transactionTemplate;
public void testTransaction() {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                try {

                    // ....  业务代码
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }

            }
        });
}
```

使用 `TransactionManager` 进行编程式事务管理的示例代码如下：

```java
@Autowired
private PlatformTransactionManager transactionManager;

public void testTransaction() {

  TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
          try {
               // ....  业务代码
              transactionManager.commit(status);
          } catch (Exception e) {
              transactionManager.rollback(status);
          }
}
```

### 非编程式事务

**声明式事务管理**

推荐使用（代码侵入性最小），实际是通过 AOP 实现（基于`@Transactional` 的全注解方式使用最多）。

使用 `@Transactional`注解进行事务管理的示例代码如下：

```java
@Transactional(propagation=propagation.PROPAGATION_REQUIRED)
public void aMethod {
  //do something
  B b = new B();
  C c = new C();
  b.bMethod();
  c.cMethod();
}
```



**<u>如果外层方法是Propagation.REQUIRED，并且捕获了内层方法的异常，对于整个数据库连接来说是否回滚？</u>**

**内层Propagation.REQUIRED：**

使用的是同一个连接，**processRollback()**进行回滚的时候设置标记

```java
if (status.hasTransaction()) {
  //globalRollbackOnParticipationFailure始终是true
   if (status.isLocalRollbackOnly() || isGlobalRollbackOnParticipationFailure()) {
      if (status.isDebug()) {
         logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
      }
      // 直接将rollbackOnly设置到ConnectionHolder中去，表示整个事务的sql都需要回滚
      // 设置连接要回滚标记，也就是全局回滚 设置当前的 ConnectionHolder的rollbackOnly = true
      doSetRollbackOnly(status);
   }
   else {
      if (status.isDebug()) {
         logger.debug("Participating transaction failed - letting transaction originator decide on rollback");
      }
   }
}
```

由于外层事务本身catch了所以走正常的**commitTransactionAfterReturning()**方法，由于内层事务设置了**rollbackOnly=true**，也因为外层事务是一个新事务，所以会直接走**doRollback(status)**;。



**内层PROPAGATION_NESTED：**

使用的也是同一个连接，在刚一执行到内层的**intercept()**的时候，因为之前已经存在事务，所以会走存在事务逻辑**handleExistingTransaction()**，会进入**PROPAGATION_NESTED**这个分支的代码，

```java
//嵌套事务
if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
   //不允许就报异常
   if (!isNestedTransactionAllowed()) {
      throw new NestedTransactionNotSupportedException(
            "Transaction manager does not allow nested transactions by default - " +
            "specify 'nestedTransactionAllowed' property with value 'true'");
   }
   if (debugEnabled) {
      logger.debug("Creating nested transaction with name [" + definition.getName() + "]");
   }
   //嵌套事务的处理
   if (useSavepointForNestedTransaction()) {
      // Create savepoint within existing Spring-managed transaction,
      // through the SavepointManager API implemented by TransactionStatus.
      // Usually uses JDBC 3.0 savepoints. Never activates Spring synchronization.
      //如果没有可以使用的保存点的方式控制事务回滚，那么在嵌入式事务的建立出时建立保存点
      DefaultTransactionStatus status =
            prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
      //调用jdbc的con创建一个保存点，并把 ConnectionHolder 也标记一下
      status.createAndHoldSavepoint();
      return status;
   }
   else {
      // Nested transaction through nested begin and commit/rollback calls.
      // Usually only for JTA: Spring synchronization might get activated here
      // in case of a pre-existing JTA transaction.
      return startTransaction(definition, transaction, debugEnabled, null);
   }
}
```

在这里往当前ConnectionHolder设置一个保存点，当方法出现异常的时候，内层事务走到异常捕获分支，**completeTransactionAfterThrowing()**

```java
//有保存点回滚到保存点
if (status.hasSavepoint()) {
   if (status.isDebug()) {
      logger.debug("Rolling back transaction to savepoint");
   }
   status.rollbackToHeldSavepoint();
}
```

这里回退保存点，并把**rollbackOnly = false;**再回到外层事务的时候，没有rollbackonly和异常被捕获所以外层事务正常commit；



为什么**PROPAGATION_REQUIRES_NEW**内层可以使用新事务？

因为在**handleExistingTransaction()**方法里面走到**PROPAGATION_REQUIRES_NEW**分支的时候，**suspend(transaction);**会把当前线程事务状态清空，并返回一个清空前数据的封装对象**SuspendedResourcesHolder**，之后会重新调用**startTransaction()** 创建新线程新事务，在business logic方法执行结束之后，如果有异常会执行**completeTransactionAfterThrowing()**、没有异常会执行**commitTransactionAfterReturning()**方法，都会执行**cleanupAfterCompletion()**，方法里面就会清空当前事务信息对象，如果有挂起的事务要恢复就走**resume()**方法。

```java
/**
	*根据条件，完成后数据清除，和线程的私有资源解绑，重置连接自动提交，隔离级别，是否只读，释放连接，恢复挂起事务等
	*/
private void cleanupAfterCompletion(DefaultTransactionStatus status) {
   //设置完成状态
   status.setCompleted();
   if (status.isNewSynchronization()) {
      //线程同步状态清除
      TransactionSynchronizationManager.clear();
   }
   //如果是新事务的话，进行数据清除，线程的私有资源解绑，重制连接自动提交，隔离级别，是否只读，释放连接等
   if (status.isNewTransaction()) {
      doCleanupAfterCompletion(status.getTransaction());
   }
   //有挂起的事务要恢复
   if (status.getSuspendedResources() != null) {
      if (status.isDebug()) {
         logger.debug("Resuming suspended transaction after completion of inner transaction");
      }
      Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
      //结束之前事务的挂起状态
      resume(transaction, (SuspendedResourcesHolder) status.getSuspendedResources());
   }
}
```

**为什么需要resume之前的事务？**

因为在外层方法的时候很正常的情况下一个方法中会调用很多事务方法，而每个事务的方法传播行为不一样，有的需要依赖外层事务，并且当前方法执行结束还需要commit，保证原子性。  

#### **事务执行中可以修改事务状态的几种方式**

```java
commitTransactionAfterReturning()
可以通过 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();来设置
会检查当前如果在事务涟中已经被标记回滚，那么不会尝试提交事务，直接回滚

//			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//				@Override
//				public void suspend() {
//
//				}
//			});
```

![image-20221108180630321](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221108180630321.png)

![image-20221108180953857](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221108180953857.png)

```
AbstractPlatformTransactionManager$prepareForCommit()提交事务之前回掉方法
```



### 事务失效几种方式

**1、spring默认只会回滚非检查异常和error异常**

​	解决方法：配置Transactional(rollbackfor=Exceptionclass)

**2、spring事务只有捕获到了业务抛出去的异常，才能进行后续的处理，如果业务自己捕获了异常，则事务无法感知**

​	解决方法：1、将异常原样抛出

​						2、设置TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

**3、spring事务切面的优先级顺序最低，但如果自定义的切面优先级和他一样且自定义的切面没有正确处理异常，则会同业务自己捕获异常的那种场景一样。**

​	解决方法：1、在切面中将异常原样抛出

​						2、在切面中设置TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()

​						3、使用@Order注解提高切面优先级，order值越小，优先级越高

**4、spring是无默认生效的方法权限都必须为public**

​	解决方法：1、将方法改为public

​						2、修改TransactionAttributeSource.将publicMethodsOnly修改为false

```java
@Bean
public TransactionAttributeSource transactionAttributeSource(){
    return new AnnotationTansactionAttributeSource(publicMethodsOnly:false);
}
```

​						3、开启Aspectj代理模式

1. 数据库引擎是否支持事务（Mysql 的 MyIsam引擎不支持事务）

2. 注解所在的类是否被加载为 Bean（是否被Spring 管理）

3. **注解所在的方法是否为 public 修饰的**

4. **是否存在自身调用的问题**

5. 所用数据源是否加载了事务管理器

6. **@Transactional的扩展配置propagation是否正确**

7. 异常没有被抛出, 或异常类型错误

8. 方法用final修饰或static修饰

9. 多线程调用

   如果你在方法中有`try{}catch(Exception e){}`处理，那么try里面的代码块就脱离了事务的管理，若要事务生效需要在catch中`throw new RuntimeException ("xxxxxx");`这一点也是面试中会问到的事务失效的场景。

![图片](https://mmbiz.qpic.cn/mmbiz_png/JdLkEI9sZfebk1L685vAHGkJrjc4fDWYVgKvBtfoDS5Im3FibV2PbIkoMtnbqXE9ia2qknGBZ7D5YOStXbP6iaYjQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

![图片](https://mmbiz.qpic.cn/mmbiz_png/JdLkEI9sZfebk1L685vAHGkJrjc4fDWYzO10TRrKd4Dyk9syXnpYPE74K98MoSwaLahoysfMKJFHutV3Vns4uw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

1、就是`@Transactional`注解保证的是每个方法处在一个事务，如果有try一定在catch中抛出运行时异常。

2、方法必须是public修饰符。否则注解不会生效，但是加了注解也没啥毛病，不会报错，只是没卵用而已。

3、this.本方法的调用，被调用方法上注解是不生效的，因为无法再次进行切面增强

**解决同类调用方式事务不生效的方法**

- 方式一：通过在service里面自己注入自己 循环依赖
- 方式二：把方法拆成两个类的方法
- 方式三：SpringBoot上启动类上添加@EnableAspectJAutoProxy(exposeProxy = true)注解，testA()中通过 (TransactionService) AopContext.currentProxy()获取代理类通过代理类调用testB()。

### 重要对象

```java
protected static final class TransactionInfo {
		/**

- 事务管理器 作为和数据库操作的中转
  */
     @Nullable
     private final PlatformTransactionManager transactionManager;
   // @Transactional注解的信息内容
     @Nullable
     private final TransactionAttribute transactionAttribute;
   // 表示类名+方法名
     private final String joinpointIdentification;
   // 当前事务状态
     @Nullable
     private TransactionStatus transactionStatus;
   // 外层事务信息
     @Nullable
     private TransactionInfo oldTransactionInfo;
  }
```

```java
public abstract class TransactionSynchronizationManager {

   private static final Log logger = LogFactory.getLog(TransactionSynchronizationManager.class);
   //线程私有事务资源 key为DataSource对象，value为ConnectionHolder对象
   private static final ThreadLocal<Map<Object, Object>> resources =
         new NamedThreadLocal<>("Transactional resources");
   //事务同步
   private static final ThreadLocal<Set<TransactionSynchronization>> synchronizations =
         new NamedThreadLocal<>("Transaction synchronizations");
   //当前事务的名称
   private static final ThreadLocal<String> currentTransactionName =
         new NamedThreadLocal<>("Current transaction name");
   //当前事务是否只读
   private static final ThreadLocal<Boolean> currentTransactionReadOnly =
         new NamedThreadLocal<>("Current transaction read-only status");
   //当前事务隔离级别
   private static final ThreadLocal<Integer> currentTransactionIsolationLevel =
         new NamedThreadLocal<>("Current transaction isolation level");
   //当前事务是否激活
   private static final ThreadLocal<Boolean> actualTransactionActive =
         new NamedThreadLocal<>("Actual transaction active");
}
```

```java
	public class DefaultTransactionStatus{
		//新创建事务
		this.transaction = transaction;
		//是都需要新事务
		this.newTransaction = newTransaction;
		//是都需要新同步
		this.newSynchronization = newSynchronization;
		//是否只读
		this.readOnly = readOnly;
		//是否要debug
		this.debug = debug;
		//是否有挂起的连接资源
		this.suspendedResources = suspendedResources;
	}

```

```java

class TransactionDefinition{
//propagation 传播行为
//isolation 隔离级别
//timeout 超时时间
//readOnly 只读
//rollbackFor 回滚类
//rollbackForClassName 回滚类名称
//noRollbackFor 不让回滚类
//noRollbackForClassName 不让回滚类名称
}
```

```java
//数据库资源持有对象
private static class DataSourceTransactionObject extends JdbcTransactionObjectSupport {
	
   private boolean newConnectionHolder;

   private boolean mustRestoreAutoCommit;
}
public class ConnectionHolder extends ResourceHolderSupport {

	/**
	 * Prefix for savepoint names.
	 */
	public static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";

	@Nullable
	private ConnectionHandle connectionHandle;
  /**
  	* 数据库资源
  	*/
	@Nullable
	private Connection currentConnection;

	private boolean transactionActive = false;

	@Nullable
	private Boolean savepointsSupported;

	private int savepointCounter = 0;
  //rollbackOnly 不是本类属性 调用父类方法赋值
}
```



# Feign底层实现细节

https://www.cnblogs.com/rickiyang/p/11802487.html

@EnableFeignClients -> @Import(FeignClientsRegistrar.class)

```java
class FeignClientsRegistrar
		implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

}
```

**通过@import注解来注册bean有几种方式**

1. 实现ImportSelector接口，spring容器就会实例化类，并且调用其selectImports方法,原类不需要加任何注解；
2. 实现ImportBeanDefinitionRegistrar接口，spring容器就会调用其registerBeanDefinitions方法，原类不需要加任何注解；
3. 带有Configuration注解的配置类。
4. 带有Component的注解

```java
	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata,
			BeanDefinitionRegistry registry) {
    //把EnableFeignClients的属性信息根据主启动类名称和信息绑定到FeignClientSpecification并注入到容器中，在FeignAutoConfiguration类中，FeignContext会使用
		registerDefaultConfiguration(metadata, registry);
    //生成FeignClient对应的bean，注入到Spring 的IOC容器。
		registerFeignClients(metadata, registry);
	}
```

在registerFeignClient方法中构造了一个BeanDefinitionBuilder对象，BeanDefinitionBuilder的主要作用就是构建一个AbstractBeanDefinition，AbstractBeanDefinition类最终被构建成一个BeanDefinitionHolder 然后注册到Spring中。

beanDefinition类为FeignClientFactoryBean，故在Spring获取类的时候实际返回的是FeignClientFactoryBean类。

`FeignClientFactoryBean`作为一个实现了`FactoryBean`的工厂类，那么每次在Spring Context 创建实体类的时候会调用它的`getObject()`方法。

##Springboot对于配置文件的解析关键类

**ConfigFileApplicationListener、YamlPropertySourceLoader、PropertiesPropertySourceLoader** 

首先在**SpringApplication run()**里面**prepareEnvironment()**会发布一个事件，**ApplicationEnvironmentPreparedEvent**

```java
//prepareEnvironment方法里面的
void environmentPrepared(ConfigurableEnvironment environment) {
   for (SpringApplicationRunListener listener : this.listeners) {
      listener.environmentPrepared(environment);
   }
}
//EventPublishingRunListener.java
	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		this.initialMulticaster
				.multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
	}
```

ConfigFileApplicationListener监听了事件会走到其**onApplicationEvent()**，最终会进入**addPropertySources()**方法

```java
protected void addPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
   RandomValuePropertySource.addToEnvironment(environment);
   new Loader(environment, resourceLoader).load();
}
```

初始化**Loader**的时候会读取spring.factories下面的所有实现了PropertySourceLoader类的配置，**YamlPropertySourceLoader、PropertiesPropertySourceLoader**，在进入load方法就会按照顺序读取默认地址下的文件，根据上述两loader加载文件名称不同去加载不同配置文件，有active拼接active，

```java
//YamlPropertySourceLoader
@Override
public String[] getFileExtensions() {
   return new String[] { "yml", "yaml" };
}
//PropertiesPropertySourceLoader
	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties", "xml" };
	}
```

# 须知事项

spring-configuration-metadata.json配置文件用来在配置文件输入的时候提示

## Spring实例化bean的5种方式

1. 通过BeanPostProcessor实现 InstantiationBeanPostProcessor 来返回一个cglib执行的bean，resolveBeforeInstantiation(beanName, mbdToUse);
2. 通过无参构造器实例化bean
3. 通过实例供应器创建bean，mbd.getInstanceSupplier()
4. 通过factoryMethod工厂方法创建bean，getFactoryMethodName
5. 通过factoryBean创建对象



### 常用类类图

![image-20220609215903471](/Users/madongming/notes/noteImg/image-20220609215903471.png)

![image-20220614234842826](/Users/madongming/notes/noteImg/image-20220614234842826.png)

![image-20220615010019655](/Users/madongming/notes/noteImg/image-20220615010019655.png)

![image-20220615010055719](/Users/madongming/notes/noteImg/image-20220615010055719.png)



### spring-Expression

SpelExpressionParser、EvaluationContext、rootObject

参考文章：未读 https://blog.51cto.com/u_3631118/3121519

关于SpEL的几个概念：

- 表达式（“干什么”）：SpEL的核心，所以表达式语言都是围绕表达式进行的
- 解析器（“谁来干”）：用于将字符串表达式解析为表达式对象
- 上下文（“在哪干”）：表达式对象执行的环境，该环境可能定义变量、定义自定义函数、提供类型转换等等
- root根对象及活动上下文对象（“对谁干”）：root根对象是默认的活动上下文对象，活动上下文对象表示了当前表达式操作的对象

![image-20220616213358868](/Users/madongming/notes/noteImg/image-20220616213358868.png)

![image-20220616213510808](/Users/madongming/notes/noteImg/image-20220616213510808.png)

![image-20220616213948252](/Users/madongming/notes/noteImg/image-20220616213948252.png)

## SPel主要对象：

```java
Expression :表示的是表达式对象。能够根据上下文对象对自身进行计算的表达式。
ExpressionParser：表达式解析器，将表达式字符串解析为可计算的已编译表达式。支持分析模板（Template）和标准表达式字符串。
EvaluationContext：评估/计算的上下文，表达式在计算上下文中执行。在表达式计算期间遇到引用时，正是在这种上下文中解析引用。它的默认实现为：StandardEvaluationContext。
```

# MDC

**MDC 全称是 Mapped Diagnostic Context，可以粗略的理解成是一个线程安全的存放诊断日志的容器。**

**log4j**
log4j可以控制日志信息输送的目的地是控制台、文件、GUI组件，甚至是套接口服务器、NT的时间记录器、UNIX Syslog护进程等。

可以控制每一条日志信息的级别，能够更加细致的控制日志的生产过程，可以通过一个配置文件来灵活的进行配置，不需要修改应用代码。

**logback**
是由log4j创始人设计的又一个开源日志组件。logback当前分成三个模块：logback-core,logback- classic和logback-access。logback-core是其它两个模块的基础模块。logback-classic是log4j的一个 改良版本。此外logback-classic完整实现SLF4J API使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging。logback-access访问模块与Servlet容器集成提供通过Http来访问日志的功能。

SLF4J所提供的核心API是一些接口以及一个LoggerFactory的工厂类。从某种程度上，SLF4J有点类似JDBC，不过比JDBC更简单，在JDBC中，你需要指定驱动程序，而在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统。如同使用JDBC基本不用考虑具体数据库一样，SLF4J提供了统一的记录日志的接口，只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。
Logback与SLF4J结合起来用,两个组件的官方网站如下：

1、更快的实现 Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小了。

2、非常充分的测试 Logback经过了几年，数不清小时的测试。Logback的测试完全不同级别的。这是简单重要的原因选择logback而不是log4j。

3、Logback-classic非常自然实现了SLF4j Logback-classic实现了 SLF4j。在使用SLF4j中，你都感觉不到logback-classic。而且因为logback-classic非常自然地实现了SLF4J， 所 以切换到log4j或者其他，非常容易，只需要提供成另一个jar包就OK，根本不需要去动那些通过SLF4JAPI实现的代码。

4、非常充分的文档 官方网站有两百多页的文档。
5、自动重新加载配置文件 当配置文件修改了，Logback-classic能自动重新加载配置文件。扫描过程快且安全，它并不需要另外创建一个扫描线程。这个技术充分保证了应用程序能跑得很欢在JEE环境里面。

6、Lilith Lilith是log事件的观察者，和log4j的chainsaw类似。而lilith还能处理大数量的log数据 。

7、谨慎的模式和非常友好的恢复 在谨慎模式下，多个FileAppender实例跑在多个JVM下，能 够安全地写道同一个日志文件。RollingFileAppender会有些限制。Logback的FileAppender和它的子类包括 RollingFileAppender能够非常友好地从I/O异常中恢复。

8、配置文件可以处理不同的情况 开发人员经常需要判断不同的Logback配置文件在不同的环境下（开发，测试，生产）。而这些配置文件仅仅只有一些很小的不同，可以通过,和来实现，这样一个配置文件就可以适应多个环境。

9、Filters（过滤器） 有些时候，需要诊断一个问题，需要打出日志。在log4j，只有降低日志级别，不过这样会打出大量的日志，会影响应用性能。在Logback，你可以继续 保持那个日志级别而除掉某种特殊情况，如alice这个用户登录，她的日志将打在DEBUG级别而其他用户可以继续打在WARN级别。要实现这个功能只需 加4行XML配置。可以参考MDCFIlter 。

10、SiftingAppender（一个非常多功能的Appender） 它可以用来分割日志文件根据任何一个给定的运行参数。如，SiftingAppender能够区别日志事件跟进用户的Session，然后每个用户会有一个日志文件。

11、自动压缩已经打出来的log RollingFileAppender在产生新文件的时候，会自动压缩已经打出来的日志文件。压缩是个异步过程，所以甚至对于大的日志文件，在压缩过程中应用不会受任何影响。

12、堆栈树带有包版本 Logback在打出堆栈树日志时，会带上包的数据。

13、自动去除旧的日志文件 通过设置TimeBasedRollingPolicy或者SizeAndTimeBasedFNATP的maxHistory属性，你可以控制已经产生日志文件的最大数量。如果设置maxHistory 12，那那些log文件超过12个月的都会被自动移除。

总之，logback比log4j优秀，可以取代之。





# 解析@Service、@Repository、@Controller等注解

参考文章：https://blog.csdn.net/m0_68615056/article/details/124371422?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522165658284316782391839919%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=165658284316782391839919&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-2-124371422-null-null.142^v27^pc_search_result_control_group,157^v15^new_3&utm_term=MergedAnnotationsCollection&spm=1018.2226.3001.4187

```java
ConfigurationClassPostProcessor&processConfigBeanDefinitions -> ConfigurationClassParser&doProcessConfigurationClass -> 
ComponentScanAnnotationParser&parse ->
ClassPathBeanDefinitionScanner&doScan ->
ClassPathScanningCandidateComponentProvider&scanCandidateComponents ->

ConfigurationClassPostProcessor ==》ConfigurationClassParser ==》ComponentScanAnnotationParser ==》ClassPathBeanDefinitionScanne ==》ClassPathScanningCandidateComponentProvider

//通过指定的包路径，扫描所有的class文件，asm字节码解析获取class的注解信息封装成MetadataReader，并通过   MergedAnnotationsCollection 递归合并父级的注解，最后判断class是否满足注入ico容器，即有没有@Component注解。


SimpleAnnotationMetadataReadingVisitor
CachingMetdataReaderFactory
SimpleMetadataReader 构造方法构建了一个 SimpleAnnotationMetadataReadingVisitor
SimpleAnnotationMetadataReadingVisitor&visitEnd 回掉方法
MergedAnnotationsCollection
```





# @Import注解

支持注入：

```java
//@Configuration
//实现ImportSelector接口的类
//实现ImportBeanDefinitionRegistrar接口的类
//@component类
```

参考文章：https://mp.weixin.qq.com/s/7arh4sVH1mlHE0GVVbZ84Q

# 为什么sprinboot启动服务就不退出？

参考文章：https://cloud.tencent.com/developer/article/1590215

我们知道**System.exit()**`或`**Runtime.exit()** 可以退出JVM进程，我们以SpringBoot默认使用的Tomcat[容器](https://cloud.tencent.com/product/tke?from=10680)为例，在我之前SpringBoot源码分析的文章中也提到过，在启动Tomcat的时候，会调用`TomcatWebServer`的`initialize`方法，在这个方法中会调用一个`startDaemonAwaitThread`方法

```javascript
 private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread("container-" + containerCounter.get()) {
            public void run() {
                TomcatWebServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setContextClassLoader(this.getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
```

下面我们在深挖一下，在Tomcat的`this.tomcat.getServer().await()`这个方法中，线程是如何实现不退出的。这里为了阅读方便，去掉了不相关的代码。

```javascript
public void await() {
        // ...
        if( port==-1 ) {
            try {
                awaitThread = Thread.currentThread();
                while(!stopAwait) {
                    try {
                        Thread.sleep( 10000 );
                    } catch( InterruptedException ex ) {
                        // continue and check the flag
                    }
                }
            } finally {
                awaitThread = null;
            }
            return;
        }
        // ...
    }
```

在await方法中，实际上当前线程在一个while循环中每10秒检查一次 stopAwait这个变量，它是一个volatile类型变量，用于确保被另一个线程修改后，当前线程能够立即看到这个变化。如果没有变化，就会一直处于while循环中。这就是该线程不退出的原因，也就是整个spring-boot应用不退出的原因。

# spring扩展点

- **BeanFactoryPostProcessor**

  - **BeanDefinitionRegistryPostProcessor （ConfigurationClassPostProcessor）**

- **BeanPostProcessor**
  - **InstantiationAwareBeanPostProcessor （resolveBeforeInstantiation返回一个代理对象）**
  - **AbstractAutoProxyCreator （代理）**

- **@Import**

  - **ImportBeanDefinitionRegistrar**
  - **ImportSelector**

- **Aware**

- **InitializingBean （afterPropertiesSet）**

- **FactoryBean （getObject，getObjectType，isSingleton）**

- **SmartInitializingSingleton (获取完所有单例bean之后，afterSingletonsInstantiated)**

- ```java
  EventListenerMethodProcessor
  判断是否需要处理TransactionEventListener
  ```
```
  
- **ApplicationListener**

- **Lifecycle （finishRefresh）**

  - **SmartLifecycle**
  - **LifecycleProcessor**

- **HandlerInterceptor （mvc）**

- **MethodInterceptor （mvc）**



**spring中的HandlerInterceptor和MethodInterceptor的区别？？**

在Spring框架中，HandlerInterceptor和MethodInterceptor都是拦截器，用于对请求进行拦截和处理。 1. HandlerInterceptor是Spring MVC框架中的一个接口，用于拦截处理程序（也就是Controller）的执行。它可以在请求到达Controller之前、之后以及视图渲染之前对请求进行拦截，并可以修改请求或响应。HandlerInterceptor主要用于处理与请求处理程序相关的任务，例如日志记录、权限验证、异常处理等。 2. MethodInterceptor是Spring AOP框架中的一个接口，用于拦截被代理对象的方法调用。它可以在目标方法执行前后添加额外的逻辑，例如性能监控、事务管理等。MethodInterceptor主要用于处理与方法调用相关的任务，而不限于特定的请求处理过程。 区别： - HandlerInterceptor是Spring MVC框架的一部分，用于拦截和处理HTTP请求。而MethodInterceptor是Spring AOP框架的一部分，用于拦截和处理被代理对象的方法调用。 - HandlerInterceptor主要关注请求处理过程中的操作，例如请求的预处理、后处理以及视图渲染前的拦截。而MethodInterceptor主要关注方法调用过程中的操作，例如方法执行前后的逻辑增强。 - HandlerInterceptor可以获取和修改请求和响应对象，同时也可以访问和修改处理程序的上下文信息。而MethodInterceptor只能获取和修改方法参数和返回值，无法直接访问请求和响应对象。 - HandlerInterceptor可以同时拦截多个请求处理程序，对于每个请求都会创建一个新的实例。而MethodInterceptor通常对单个被代理对象进行拦截，并且可以在整个应用程序中共享同一个拦截器实例。 总之，HandlerInterceptor和MethodInterceptor都是Spring框架中用于拦截和处理不同层次操作的拦截器接口。它们在不同的模块中起到不同的作用，并满足了不同的功能需求。

# Spring注解

**@Async默认使用的是spring的通用线程池 ThreadPoolTaskExecutor**

​```java
//自动注入类
TaskExecutionAutoConfiguration  
/**
 *默认核心线程数：8，最大线程数：Integet.MAX_VALUE，队列使用LinkedBlockingQueue，容量是：
 *Integet.MAX_VALUE，空闲线程保留时间：60s，线程池拒绝策略：AbortPolicy。
 */
```



# ApplicationListener的bean什么时候注册到多播器中的？

参考文章：https://blog.csdn.net/zxd1435513775/article/details/121241025

Listener是怎么被保存到广播器`ApplicationEventMulticaster`中的呢？

ApplicationListenerDetector 在 prepareBeanFactory 注入的

答案：通过`ApplicationListenerDetector`这个`BeanPostProcessor`后置处理器。




# ConfigurationClassPostProcessor

​		首先，ConfigurationClassPostProcessor后置处理器的处理入口为`postProcessBeanDefinitionRegistry()`方法。其主要使用了`ConfigurationClassParser`配置类解析器解析`@Configuration`配置类上的诸如`@Component`、`@ComponentScan`、`@Import`、`@Bean`等注解，并尝试发现所有的配置类；还使用了`ConfigurationClassBeanDefinitionReader`注册所发现的所有配置类中的所有Bean定义；结束执行的条件是所有配置类都被发现和处理，相应的bean定义注册到容器。

**@Configuration的类为什么会生成代理?**

目的是防止@Bean方法的手动重复调用造成单例的破坏。类被分为full模式和lite模式，加了**@Configuration的是full模式**，**@Bean、@Component、@ComponentScan、@Import、@ImportResource注解的就是lite**，之后会在BeanDefinition上设置configurationClass的属性值。

**当配置类使用@Component修饰的时候**

```java
@Bean
public MyService myService() {
   MyService myService = new MyService();
   myService.setUserService(userService());
   return myService;
}

@Bean
public UserService userService() {
   return new UserService();
}
```

**⚠️注意**：就会出现myService使用的是自己new出来的，**走的正常方法调用**，正常想使用的是spring中的userService，所以就会导致用的不是同一个对象，比较的话肯定是false。

@Bean修饰的方法走的是工厂方法方式创建对象（**createBeanInstance()&instantiateUsingFactoryMethod()**），之后会把当前工厂方法存入到一个**isCurrentlyInvokedFactoryMethod()** Threadlocal之中，再然后调用方法走到拦截器，拦截器里面判断isCurrentlyInvokedFactoryMethod()是否有值，如果有值说明是spring正常在创建对象，如果没值的话说明是方法里面自己调用的，当执行到userService的时候由于该配置类每个方法都有拦截器所以又回到了拦截器的逻辑，又因为在**isCurrentlyInvokedFactoryMethod()**中不是当前工厂方法，所以获取的对象从spring中获取，存入到一级缓存，正常获取对象的逻辑，完事之后回到myService方法继续执行。当spring加载配置类下一个userSerivice方法创建对象的时候，由于spring容器中已经有该对象，就不需要创建了。，最终是为了保证两次调用用的都是同一个容器对象。

参考文章：https://blog.csdn.net/weixin_37689658/article/details/125664876

![image-20230815212334444](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230815212334444.png)

# RefreshScope

参考文章：https://www.modb.pro/db/104172，https://www.bmabk.com/index.php/post/38126.html

## 主要类

ContextRefresher：主要功能是清空RefreshScope缓存，重新加载配置到Context中，发布事件（事件监听程序接收到事件后会重写配置相关的配类）

RefreshEventListener：会监听RefreshEvent事件，发生事件之后调用ContextRefresher.refresh()方法

AbstractApplicationContext => refresh() => prepareBeanFactory()

```java
beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
beanFactory.registerResolvableDependency(ResourceLoader.class, this);
beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
beanFactory.registerResolvableDependency(ApplicationContext.class, this);
```

注册了自己，所以在声明@Bean的时候通过构造方法可以注入ConfigurableApplicationContext(这里是浅拷贝，所以后续的操作取出来的都是最新的值)



# Spring内置的事件

**ApplicationContextEvent 是 Spring Context 相关的事件基类，如下图所示：**

![图片](https://mmbiz.qpic.cn/mmbiz_png/JdLkEI9sZfdnf4LkFxFWiavibb3ia7qGHfIOtxUeZALUBPecOP305crXtI8Vt1Uql16syHZpO0z4YchtTia8zLTu0g/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

- ContextStartedEvent：Spring Context 启动**完成** 事件。
- ContextStoppedEvent：Spring Context 停止**完成** 事件。
- ContextClosedEvent：Spring Context 停止**开始** 事件。
- ContextRefreshedEvent：Spring Context 初始化或刷新**完成** 事件。



**SpringApplicationEvent 是 Spring Boot Application（应用）相关的事件基类**

![图片](https://mmbiz.qpic.cn/mmbiz_png/JdLkEI9sZfdnf4LkFxFWiavibb3ia7qGHfIQ9e9RYIGU8J84A8cAjsPic9e54pCFS80RpkrZ1RlLBqWIvpKpO8RpZw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

**EventPublishingRunListener** 类至关重要

- ApplicationStartingEvent：Application 启动**开始** 事件。**starting（）**
- ApplicationEnvironmentPreparedEvent：Spring Environment 准备完成的事件。**environmentPrepared（）**
- ApplicationContextInitializedEvent：Spring Context 准备完成，但是 BeanDefinition 未加载时的事件**contextPrepared（）**
- ApplicationPreparedEvent：Spring Context 准备完成，但是未刷新时的事件。**contextLoaded（）**
- ApplicationReadyEvent：Application 启动**成功** 事件。**running（）**
- ApplicationFailedEvent：Application 启动**失败** 事件。**failed（）**



# CORS

**CORS** （Cross-Origin Resource Sharing，跨域资源共享）是一个系统，它由一系列传输的[HTTP 头](https://developer.mozilla.org/zh-CN/docs/Glossary/HTTP_header)组成，这些 HTTP 头决定浏览器是否阻止前端 JavaScript 代码获取跨域请求的响应。

# spring创建对象的五种方式



![image-20211216155602502](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20211216155602502.png)

![image-20211216220650964](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20211216220650964.png)

## 通过BeanPostProcess实现InstantiationBeanPostProcessor创建对象

```java
public class CustomTargetSource extends AbstractBeanFactoryBasedTargetSource {

    private static final long serialVersionUID = 1231212121L;

    @Override
    public Object getTarget() throws Exception {
        return getBeanFactory().getBean(getTargetBeanName());
    }
}
public class CustomTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {
    @Override
    protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {
        if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
            if (beanClass.isAssignableFrom(UserServiceImpl.class)) {
                return new CustomTargetSource();
            }
        }
        return null;
    }
}
@Component
public class SetCustomTargetSourceCreator implements PriorityOrdered, BeanFactoryAware, InitializingBean {

    private BeanFactory beanFactory;
    private boolean load = false;

    @Override
    public int getOrder() {
        return 45;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!load) {
            AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator = beanFactory.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
            CustomTargetSourceCreator customTargetSourceCreator = new CustomTargetSourceCreator();
            customTargetSourceCreator.setBeanFactory(beanFactory);
            annotationAwareAspectJAutoProxyCreator.setCustomTargetSourceCreators(customTargetSourceCreator);
            load = !load;
        }
    }
}
public interface UserService {
    void userInfo();
}
@Service
public class UserServiceImpl implements UserService{
    @Override
    public void userInfo() {
        System.out.println("打印了用户信息 UserServiceImpl1");
    }
}
```

## RestTemplate的Ribbon

![image-20220119111513269](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220119111513269.png)

# SpringBoot启动流程

## Spring默认启动的时候就会创建几个BeanDefinition

```java
new SpringApplication()
//的时候构造方法完成了几件事情，本地设置从spring.factories加载的 ApplicationContextInitializer.class相关的类，本地设置从spring.factories加载的 ApplicationListener.class相关的类。两个比较重要的类
//BootstrapApplicationListener
//ConfigFileApplicationListener
执行run方法（）主要步骤
1、获取 SpringApplicationRunListener相关的类
2、prepareEnvironment 发布事件 ApplicationEnvironmentPreparedEvent(BootstrapApplicationListener) 去加载 bootstrap.yml 文件
3、createApplicationContext，根据 webApplicationType 类型不同创建 (ConfigurableApplicationContext)AnnotationConfigApplicationContext，这个时候就会通过其创建对象的构造方法，创建两个对象，一个是 
AnnotatedBeanDefinitionReader，
ClassPathBeanDefinitionScanner对象，其中
//AnnotationConfigUtils.java
AnnotatedBeanDefinitionReader 在new对象的时候就是往容器里面注册几个beandefinition，其中比较重要的是，
	1. AutowiredAnnotationBeanPostProcessor.java
	2. CommonAnnotationBeanPostProcessor.java
	3. ConfigurationClassPostProcessor.java
	4、EventListenerMethodProcessor.java
ClassPathBeanDefinitionScanner是一个扫描器对象
4、准备上下文 
	nacos config 整合 springboot 位置
  1、applyInitializers
  2、PropertySourceBootstrapConfiguration
  3、PropertySourceLocator.locateCollection
  4、NacosPropertySourceLocator.locate
```

```java
//SpringBoot的启动过程划分了多个阶段
public interface SpringApplicationRunListener {
    // run 方法第一次被执行时调用，早期初始化工作
    void starting();
    // environment 创建后，ApplicationContext 创建前
    void environmentPrepared(ConfigurableEnvironment environment);
    // ApplicationContext 实例创建，部分属性设置了
    void contextPrepared(ConfigurableApplicationContext context);
    // ApplicationContext 加载后，refresh 前
    void contextLoaded(ConfigurableApplicationContext context);
    // refresh 后
    void started(ConfigurableApplicationContext context);
    // 所有初始化完成后，run 结束前
    void running(ConfigurableApplicationContext context);
    // 初始化失败后
    void failed(ConfigurableApplicationContext context, Throwable exception);
}
```



# SpringBoot整合Naocs

## 配置中心整合spring

springboot提供加载资源properties .yml
PropertySourceLoader.java
SpringApplication&run&prepareEnvironment去加载bootstrap.yml文件
读取nacos配置文件是在SpringApplication&run&prepareContext方法&applyInitializers&PropertySourceBootstrapConfiguration&PropertySourceLocator.locateCollection&NacosPropertySourceLocator.locate



## 注册中心整合spring

**参考文章：**https://ost.51cto.com/posts/16647

nacos divcovery是通过事件发布的方式注册的

**onRefresh()**方法的时候往bean工厂注入一个类**WebServerStartStopLifecycle**，在**finishRefresh()**会调用**getLifecycleProcessor().onRefresh();**方法走到**startBeans()**，会先获取所有实现了**Lifecycle**接口的bean

，然后调用他的**start()**,因为**WebServerStartStopLifecycle**父类实现了**Lifecycle**接口，所有最终会走到他的**strat()**发布一个**ServletWebServerInitializedEvent**事件。

又因为Nacos的**spring.factories**中有一个自动注入类**NacosServiceRegistryAutoConfiguration**，注入了一个**bean**，**NacosAutoServiceRegistration**父类实现了**ApplicationListener**监听了**WebServerInitializedEvent**时间所以会走到他的**onApplicationEvent**方法,最终就会调到**NacosServiceRegistry**的**register**方法进行注册。

![Spring Cloud集成Nacos服务发现源码解析?翻了三套源码,保质保鲜-开源基础软件社区](https://dl-harmonyos.51cto.com/images/202208/27ecf1915beee9dbedf34436233880453a1cdf.jpg)

# SpringBoot整合Mybatisplus

## mybatisplus使用spring事务创建的数据库连接

**mybatis的datasource是创建MybatisSqlSessionFactoryBean的时候必须传的所以在后面调用他的getObject()方法的时候可以拿到。**

```java
MybatisPlusAutoConfiguration.java
@Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // TODO 使用 MybatisSqlSessionFactoryBean 而不是 SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
    }
    

```

通过Configuration的方式创建的DefaultSqlSessionFactory()。**里面有一个很重要的参数决定了mybatis是否使用spring的事务连接(
transactionFactory)**，Mybatis在创建**SqlSession**时，需要为其**添加一个Executor执行器**，*
*构建Executor执行器时需要的Transaction对象就是通过TransactionFactory(SpringManagedTransactionFactory)的newTransaction()
方法创建的**
，后续prepareStatement的时候会通过TransactionFactory的newTransaction获取数据库连接，而这里面获取连接靠的也是DataSourceUtils.getConnection(
this.dataSource)的连接。事务有连接就用事务的，没有就自己创建。从**TransactionSynchronizationManager.getResource(
dataSource);**获取ConnectionHolder。

```java
@Override
public Connection getConnection() throws SQLException {
  if (this.connection == null) {
    openConnection();
  }
  return this.connection;
}

/**
 * Gets a connection from Spring transaction manager and discovers if this {@code Transaction} should manage
 * connection or let it to Spring.
 * <p>
 * It also reads autocommit setting because when using Spring Transaction MyBatis thinks that autocommit is always
 * false and will always call commit/rollback so we need to no-op that calls.
 */
private void openConnection() throws SQLException {
  this.connection = DataSourceUtils.getConnection(this.dataSource);
  this.autoCommit = this.connection.getAutoCommit();
  this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

  LOGGER.debug(() -> "JDBC Connection [" + this.connection + "] will"
      + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
}
```

**MapperScannerConfigurer 它 将 会 查 找 类 路 径 下 的 mapper接口并 自 动 将 它 们 创 建 成 MapperFactoryBean。**

**ClassPathMapperScanner会先调用父类的doscan方法扫描所有的类，但是重写了isCandidateCompenment方法，就会让所有的接口也可以别扫描到。**

![image-20221110152120016](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221110152120016.png)

1. 实现BeanDefinitionRegistryPostProcessor接口中的**postProcessBeanDefinitionRegistry**方法，扫描 basePackage 路径下的类，并注册到 Spring 容器中。
2. 实现 InitializingBean 中的 **afterPropertiesSet** 方法，校验 basePackage 属性不能为空。
3. 实现 ApplicationContextAware 中的 **setApplicationContext** 方法，用于获取 Spring 容器 applicationContext。
4. 实现 BeanNameAware 中的 **setBeanName** 方法动态的设置 beanName。

**BeanDefinitionRegistries** 是在应用启动的早期，且在**BeanFactoryPostProcessors**之前运行的。
这意味着**PropertyPlaceholderConfigurer**还不会被加载，并且此类的所有属性的占位符替换都将会失败。
为了避免这种情况，需要查找在上下文中定义的**PropertyResourceConfigurer**配置，并且在此类的定义过程中运行它们，然后更新 basePackage、sqlSessionFactoryBeanName、sqlSessionTemplateBeanName的值。

**PropertyPlaceholderConfigurer自定义的解析器还没有加载，只能用spring中有的。通过PropertySourcesPlaceholderConfigurer注入的。**

**mybatisPlus 的整个加载过程概括如下：**

1. **（MapperScan注入或者MybatisPlusAutoConfiguration in missing bean注入） MapperScannerConfigurer** 扫描 mapper 接口，并在 spring 中注册 deanDefinition，类型为 **MapperFactoryBean**
2. **MybatisPlusAutoConfiguration**自动注入**SessionFactory** 解析 mapper.xml 和 mapper 接口 中的 sql 语句保存到 Configuration 中，同时加入 mybatisPlus 提供的动态 sql。 最后注册对应 mapper 的 **MybatisMapperProxyFactory**。
3. **MybatisPlusAutoConfiguration（自动注入）SqlSessionTemplate** 使用 **SqlSessionInterceptor** 代理实现一个线程安全的 spring 管理的 SqlSession，并最终通过 **MybatisMapperProxyFactory** 获取 mapper 的代理对象 **MybatisMapperProxy**.

**参考文章：**https://blog.csdn.net/Wu_Shang001/article/details/125356883

**MapperScannerConfigurer** 实现了 **BeanDefinitionRegistryPostProcessor**这个类 所以在 refresh()
调用invokeBeanFactoryPostProcessor方法的时候就会去调用这个类的postProcessBeanDefinitionRegistry方法，这个方法会初始化一个
**ClassPathMapperScanner**对象，他继承自 **ClassPathBeanDefinitionScanner**,**ConfigurationClassPostProcessor**
中用来实现**@ComenmentScan**注解扫描bean的，正常它通过方法设置不会去扫描接口或者实现类，但是在**ClassPathMapperScanner*
*中覆盖了这个方法**isCandidateComponent**让他可以扫描接口，回到**postProcessBeanDefinitionRegistry**方法中，注册完**
ClassPathMapperScanner**方法之后有设置了一系列的值，最后调用它的**scan**方法，先调用父类的**doscan*
*方法扫描出所有的beandefinition（是接口的），就是对应我们创建的mapper，父类**doscan*
*调用完成之后会回到自己的方法中继续处理这些个接口，因为接口不能实例话，所以在后来把它beandefinition的beanclass变为了**
MapperFactoryBean**,这样后来就可以实例话了。在docreatebean -> initializeBean中会调用afterpropertiesset方法，里面就会把当前的bean包装成一个
**MybatisMapperProxyFactory**类型。然后放入到configuration的一个map类型的缓存中，key=mapper名字，value就是MybatisMapperProxyFactory对象。

然后谁需要mapper类型的bean的话就需要注入，而因为它是一个**MapperFactoryBean**类型的就会调用到**getObject**()
方法,而最终就回去之前的map里面去找，找到取出来的时候在进行下面的**newInstance**也就是jdk代理的生成，变为代理类。调用其方法的时候就会走到
**MybatisMapperProxy**的**invoke**方法 最终会调用到**SqlSessionInterceptor**
的invoke中。因为sqlsession是sqlsessiontemplate类型，会走到对应的方法，而因为内部会走代理类，sqlSessionProxy（SqlSessionInterceptor）的invoke方法，就会先获取sqlsession（defaultsqlsession），因为是每一个数据库方法一个，是在方法区域的，所以是线程安全的，他会先去当前事务中查看有没有绑定资源
。没有就自己创建一个return new DefaultSqlSession(configuration, executor, autoCommit);

SpringManagedTransactionFactory。

![image-20230823232212130](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823232212130.png)

![image-20230823232037484](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823232037484.png)

![image-20230823225346789](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823225346789.png)

![image-20230823225141048](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823225141048.png)

![image-20230823231005007](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823231005007.png)



### **入口**

**@MapperScan("com.madm.learnroute.mapper") -> @Import(MapperScannerRegistrar.class)**

```java
public class MapperScannerConfigurer
    implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
		  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    if (this.processPropertyPlaceHolders) {
      //找到一个 PropertyResourceConfigurer 的bean，这个bean是通过PropertySourcesPlaceholderConfigurer注入的，通过这个去声明一个DefaultListableBeanFactory然后注册一个MapperScannerRegistrar对象，在通过prc获取到注解的属性值 basepackage
      processPropertyPlaceHolders();
    }

    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
    scanner.setAddToConfig(this.addToConfig);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setSqlSessionFactory(this.sqlSessionFactory);
    scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
    scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
    scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
    if (StringUtils.hasText(lazyInitialization)) {
      scanner.setLazyInitialization(Boolean.valueOf(lazyInitialization));
    }
    if (StringUtils.hasText(defaultScope)) {
      scanner.setDefaultScope(defaultScope);
    }
    scanner.registerFilters();
    scanner.scan(
        StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }
}
//processPropertyPlaceHolders()方法就会

```

**参考文章：**https://qiuyadongsite.github.io/2019/01/15/mybatis-sources-code-6/ 

**sqlsession数据不安全问题？**

![image-20230823195743954](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230823195743954.png)

**FactoryBean通过getObject方法获取对象的源码调用地址？**

```
doGetBean#getObjectForBeanInstance(sharedInstance, name, beanName, mbd)#getObjectFromFactoryBean#doGetObjectFromFactoryBean(factory, beanName);
```





# springboot2.0默认创建什么代理？

**为了防止某些讨厌的人在类属性上不用接口注入，所以使用的是Cglib代理，AopAutoConfiguration默认设置的。**

**参考文章：**https://note.youdao.com/ynoteshare/index.html?id=ca8cc5711375e0fd4e605aa4f5aa4be3&type=note&_time=1656590927414

# BeanDefinition

**参考文章：**https://cloud.tencent.com/developer/article/1497805

![image-20221104215842689](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221104215842689.png)

![img](https://ask.qcloudimg.com/http-save/yehe-6158873/mku1x7r0xe.png?imageView2/2/w/1620)

**@Configuration注解的类会成为一个工厂类，而所有的@Bean注解的方法会成为工厂方法，通过工厂方法实例化Bean，而不是直接通过构造函数初始化。**

**一个`RootBeanDefinition`定义表明它是一个可合并的beandefinition：即在spring beanFactory运行期间，可以返回一个特定的bean。但在Spring2.5以后，我们绝大多数情况还是可以使用`GenericBeanDefinition`来做。**

# Spring获取运行主类Class对象

```java
private Class<?> deduceMainApplicationClass() {
   try {
      StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
      for (StackTraceElement stackTraceElement : stackTrace) {
         if ("main".equals(stackTraceElement.getMethodName())) {
            return Class.forName(stackTraceElement.getClassName());
         }
      }
   }
   catch (ClassNotFoundException ex) {
      // Swallow and continue
   }
   return null;
}
```

# IDEA中External Libraries多余的jar删除办法

**原因分析：External Libraries中显示的jar，都是从iml文件中读取的，所以我们及时更新iml文件即可解决该问题重新生成iml文件即可。**
**生成.iml文件: mvn idea:module**

**参考文章：**https://blog.csdn.net/qq_30054961/article/details/102938002

```java
public int intValue() {
    return value;
}


@FunctionalInterface
public interface ToIntFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    int applyAsInt(T value);
}
Integer::intValue
```

# refresh()

```java
@Override
public void refresh() throws BeansException, IllegalStateException {
   synchronized (this.startupShutdownMonitor) {
      prepareRefresh();

			/*
			 * 1、创建BeanFactory对象
			 * 2、xml解析
			 *  传统标签解析：bean、import等
			 *  自定义标签解析 如：<context:component-scan base-package="com.xiangxue.jack"/>
			 *  自定义标签解析流程：
			 *     a、根据当前解析标签的头信息找到对应的namespaceUri
			 *     b、加载spring所以jar中的spring.handlers文件。并建立映射关系
			 *     c、根据namespaceUri从映射关系中找到对应的实现了NamespaceHandler接口的类
			 *     d、调用类的init方法，init方法是注册了各种自定义标签的解析类
			 *     e、根据namespaceUri找到对应的解析类，然后调用paser方法完成标签解析
			 * 3、把解析出来的xml标签封装成BeanDefinition对象
			 * */
      ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

      // Prepare the bean factory for use in this context.
      // 准备两个BeanPostProcessor和排除一些aware
      // ApplicationContextAwareProcessor
      // ApplicationListenerDetector
      prepareBeanFactory(beanFactory);

      try {
         // Allows post-processing of the bean factory in context subclasses.
         postProcessBeanFactory(beanFactory);

         //最为关键的类通过 ConfigurationClassPostProcessor
        //有两个核心实现方法postProcessBeanDefinitionRegistry（定位、加载、解析、注册相关注解，如：@Controller、@Service、@Component等注解类到IOC容器之中，自动化配置类的解析、注册）、			postProcessBeanFactory（添加CGLIB增强处理及ImportAwareBeanPostProcessor（BeanPostProcessor）后处理类）
         invokeBeanFactoryPostProcessors(beanFactory);

         // Register bean processors that intercept bean creation.
       	 // 已经把所有的BeanPostProcessor注册到了容器中是指的是写入到了singletonOjbects中
        
         registerBeanPostProcessors(beanFactory);

         // Initialize message source for this context.
         initMessageSource();

         // Initialize event multicaster for this context.
         initApplicationEventMulticaster();

         // Initialize other special beans in specific context subclasses.
         onRefresh();

         // Check for listener beans and register them.
         registerListeners();

         // Instantiate all remaining (non-lazy-init) singletons.
         finishBeanFactoryInitialization(beanFactory);

         // Last step: publish corresponding event.
         finishRefresh();
      }

      catch (BeansException ex) {
         if (logger.isWarnEnabled()) {
            logger.warn("Exception encountered during context initialization - " +
                  "cancelling refresh attempt: " + ex);
         }

         // Destroy already created singletons to avoid dangling resources.
         destroyBeans();

         // Reset 'active' flag.
         cancelRefresh(ex);

         // Propagate exception to caller.
         throw ex;
      }

      finally {
         // Reset common introspection caches in Spring's core, since we
         // might not ever need metadata for singleton beans anymore...
         resetCommonCaches();
      }
   }
}
```

**XSD**是指XML结构定义( XML Schemas Definition )XML Schema 是DTD的替代品。 XML Schema语言也就是XSD。 XML Schema描述了XML文档的结构。 **可以用一个指定的XML Schema来验证某个XML文档，以检查该XML文档是否符合其要求。**



# 须知

**加载有参构造器缺失参数流程**

```java
AbstractAutowireCapableBeanFactory
createBeanInstance -> autowireConstructor -> resolvePreparedArguments -> resolveValueIfNecessary -> resolveInnerBean -> createBean
```

instantiateBean

isSynthetic



**advisor是spring内部实现的方式，封装切点和通知的，而切面类就是真实声明我们通知以及切点的类。**

有两处地方可以进行注入Aop一个是getEarlyBeanReference，另一个是postProcessAfterInitialization。

# 面试题

## **说一下Spring @Autowired 注解自动注入流程**

核心两个地方

在**doCreateBean()**的时候会收集当前类需要的属性注入的信息

```java
// Allow post-processors to modify the merged bean definition.
// 允许beanPostProcessor去修改合并的beanDefinition
synchronized (mbd.postProcessingLock) {
   if (!mbd.postProcessed) {
      try {
         // 收集信息 所有实现 MergedBeanDefinitionPostProcessor 接口的类
         // AutowiredAnnotationBeanPostProcessor 找到所有注解 生成InjectionMetadata对象保存到 injectionMetadataCache
         // CommonAnnotationBeanPostProcessor 父类解析 @PostConstruct 和 @PreDestory 本类解析 @Resource注解 @WebServceRef注解 @EJB注解
         applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
      } catch (Throwable ex) {
         throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", ex);
      }
      mbd.postProcessed = true;
   }
```

在**populateBean()**的时候由于实现了**InstantiationAwareBeanPostProcessor**接口所以后走这里的方法进行属性注入

```java
if (hasInstAwareBpps) {
   if (pvs == null) {
      pvs = mbd.getPropertyValues();
   }
   for (BeanPostProcessor bp : getBeanPostProcessors()) {
      //CommonAnnotationBeanPostProcessor @Resource（JDK提供的）
      //AnnotationAutowiredPostPorcessor @Value @autowired（Spring提供）
      if (bp instanceof InstantiationAwareBeanPostProcessor) {
         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
         PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
         if (pvsToUse == null) {
            if (filteredPds == null) {
               filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
            }
            pvsToUse = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
            if (pvsToUse == null) {
               return;
            }
         }
         pvs = pvsToUse;
      }
   }
}
```

**参考文章：**https://blog.51cto.com/u_14849432/2553615

## Spring为什么不支持构造方法的循环依赖

因为通过**@Autowired**属性注入的时候会先把自己放入到三级缓存，然后去填充属性**populateBean**()在之后填充B的时候发现需要A，再去填充A这个时候发现在三级缓存有就使用了。

而在通过构造器构造的时候，由于没有放到三级缓存之前就去通过bean工厂获取所以来的bean，最终就会报错。

```java
protected void beforeSingletonCreation(String beanName) {
		if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
			throw new BeanCurrentlyInCreationException(beanName);
		}
	}
public BeanCurrentlyInCreationException(String beanName) {
   super(beanName,
         "Requested bean is currently in creation: Is there an unresolvable circular reference?");
}
```

**参考文章：**https://blog.csdn.net/csdn_wyl2016/article/details/108146174

**HandlerInterceptor是什么时候被调用的？**

**WebMvcAutoConfiguration**类会注入一个**@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)**类，里面创建了一个**RequestMappingHandlerMapping**,

```java
mapping.setInterceptors(getInterceptors(conversionService, resourceUrlProvider));
```

里面会注册所有的interceptor。

调用的地方在DispatcherServlet

```java
// Determine handler for the current request.
mappedHandler = getHandler(processedRequest);//1016
if (!mappedHandler.applyPreHandle(processedRequest, response)) //1035
mappedHandler.applyPostHandle(processedRequest, response, mv);//1047
processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);//1057
```

其他的类似

（1）**preHandle**: 在执行controller处理之前执行，返回值为boolean ,返回值为true时接着执行postHandle和afterCompletion，如果我们返回false则中断执行
（2）**postHandle**:在执行controller的处理后，在ModelAndView处理前执行
（3）**afterCompletion** ：在DispatchServlet执行完ModelAndView之后执行



# Condition注解原理

在@SpringbootApplication内部@Enableautoconfiguration注册了一个**AutoConfigurationImportSelector**的类，他是实现了DeferredImportSelector接口，就会先走到getImportSelector方法，返回一个**AutoConfigurationGroup**的类，而**AutoConfigurationGroup**的类有两个方法先后会被执行，一个是process，另一个是selectimportor，至于DeferredImportSelector的selectimportor就不会被执行。

在执行到process方法的时候会调用**AutoConfigurationImportSelector**的方法getAutoConfigurationEntry(),

![image-20230818165803540](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230818165803540.png)

![image-20230818165816925](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230818165816925.png)

![image-20230818165848668](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230818165848668.png)

找到spring.factories类中所有对应的value

由两部分内容需要验证

一、项目自定义的bean是否添加了@Conditional相关的注解及按条件匹配

二、通过@EnableAutoConfiguration spring.factories文件自动扫描的类 按条件是否匹配注入

**ConfigurationClassParser**构造方法会创建一个对象**ConditionEvaluator（用来验证@Conditional注解的）**，

因为对于每个类来说可能包含内部类bean之类的，所以整个是递归调用的。**processConfigurationClass**主要用来判断是否符合**Conditional**条件。

```java
protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {
   // 检查当前解析的配置bean是否包含Conditional注解，如果不包含则不需要跳过
   // 如果包含了则进行match方法得到匹配结果
   if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
      return;
   }

   ConfigurationClass existingClass = this.configurationClasses.get(configClass);
   if (existingClass != null) {
      if (configClass.isImported()) {
         if (existingClass.isImported()) {
            existingClass.mergeImportedBy(configClass);
         }
         // Otherwise ignore new imported config class; existing non-imported class overrides it.
         return;
      }
      else {
         // Explicit bean definition found, probably replacing an import.
         // Let's remove the old one and go with the new one.
         this.configurationClasses.remove(configClass);
         this.knownSuperclasses.values().removeIf(configClass::equals);
      }
   }

   // Recursively process the configuration class and its superclass hierarchy.
   SourceClass sourceClass = asSourceClass(configClass, filter);
   do {
      sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
   }
   while (sourceClass != null);

   this.configurationClasses.put(configClass, configClass);
}
```

## 1.处理@Component注解

```java
// Process any @Component annotations
if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
   // Recursively process any member (nested) classes first
   processMemberClasses(configClass, sourceClass, filter);
}
```

## 2.处理@Import注解的时候

```java
// Candidate class not an ImportSelector or ImportBeanDefinitionRegistrar ->
// process it as an @Configuration class
this.importStack.registerImport(
      currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());
processConfigurationClass(candidate.asConfigClass(configClass), exclusionFilter);
```

## 3.最外层解析入口处do while循环

```java
do {
   //主要调用 doProcessConfigurationClass() 解析配置类，并将解析得到的 Bean 缓存在Map集合 configurationClasses 中供后续注册使用
   parser.parse(candidates);
   parser.validate();
```

**参考文章：**https://blog.csdn.net/One_L_Star/article/details/114058971



## application.yml提示配置信息原理

我们在编写 `application.yml` 文件时，当你输入一个字母时，IDE 是不是会提示很多选项供你选择，这个就要归功于 `META-INF/spring-configuration-metadata.json`、`META-INF/additional-spring-configuration-metadata.json` 两个文件，在这两个文件里面可以定义你需要的配置的信息，例如 Spring Boot 提供的：

```json
{
  "groups": [
    {
      "name": "logging",
      "type": "org.springframework.boot.context.logging.LoggingApplicationListener"
    }
  ],
  "properties": [
    {
      "name": "logging.config",
      "type": "java.lang.String",
      "description": "Location of the logging configuration file. For instance, `classpath:logback.xml` for Logback.",
      "sourceType": "org.springframework.boot.context.logging.LoggingApplicationListener"
    },
    {
      "name": "spring.application.name",
      "type": "java.lang.String",
      "description": "Application name.",
      "sourceType": "org.springframework.boot.context.ContextIdApplicationContextInitializer"
    },
    {
      "name": "spring.profiles",
      "type": "java.util.List<java.lang.String>",
      "description": "Comma-separated list of profile expressions that at least one should match for the document to be included.",
      "sourceType": "org.springframework.boot.context.config.ConfigFileApplicationListener"
    },
    {
      "name": "spring.profiles.active",
      "type": "java.util.List<java.lang.String>",
      "description": "Comma-separated list of active profiles. Can be overridden by a command line switch.",
      "sourceType": "org.springframework.boot.context.config.ConfigFileApplicationListener"
    }
  ],
  "hints": [
    {
      "name": "logging.level.values",
      "values": [
        {
          "value": "trace"
        },
        {
          "value": "debug"
        },
        {
          "value": "info"
        },
        {
          "value": "warn"
        },
        {
          "value": "error"
        },
        {
          "value": "fatal"
        },
        {
          "value": "off"
        }
      ],
      "providers": [
        {
          "name": "any"
        }
      ]
    }
  ]
}
```

上面仅列出了部分内容，可以看到定义了每个配置的名称、类型、描述和来源，同时可以定义每个配置能够输入的值，这样一来，我们就能够在 IDE 中快速的输入需要的配置项。

这个文件是通过 Spring Boot 提供的 `spring-boot-configuration-processor` 工具模块生成的，借助于 SPI 机制配置了一个 `ConfigurationMetadataAnnotationProcessor` 注解处理器，它继承 `javax.annotation.processing.AbstractProcessor` 抽象类。也就是说这个处理器在编译阶段，会解析每个 `@ConfigurationProperties` 注解标注的类，将这些类对应的一些配置项（key）的信息保存在 `META-INF/spring-configuration-metadata.json` 文件中，例如类型、默认值，来帮助你编写 `application.yml` 的时候会有相关提示。

而且，当我们使用 `@ConfigurationProperties` 注解后，IDE 会提示我们引入这个工具类：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

关于这部分内容可参考 [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/2.2.13.RELEASE/reference/html/appendix-configuration-metadata.html#configuration-metadata-annotation-processor)

**参考文章：**https://www.cnblogs.com/lifullmoon/p/14957836.html

**参照网址：**https://blog.csdn.net/xby7437/article/details/115010642?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522165452814216782350967629%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=165452814216782350967629&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-2-115010642-null-null.142



## AOP, AspectJ, Spring AOP

我们先来把它们的概念和关系说说清楚。

AOP 要实现的是在我们原来写的代码的基础上，进行一定的包装，如在方法执行前、方法返回后、方法抛出异常后等地方进行一定的拦截处理或者叫增强处理。

AOP 的实现并不是因为 Java 提供了什么神奇的钩子，可以把方法的几个生命周期告诉我们，而是我们要实现一个代理，实际运行的实例其实是生成的代理类的实例。

作为 Java 开发者，我们都很熟悉 **AspectJ** 这个词，甚至于我们提到 AOP 的时候，想到的往往就是 AspectJ，即使你可能不太懂它是怎么工作的。这里，我们把 AspectJ 和 Spring AOP 做个简单的对比：

**Spring AOP：**

- 它基于动态代理来实现。默认地，如果使用接口的，用 JDK 提供的动态代理实现，如果没有接口，使用 CGLIB 实现。大家一定要明白背后的意思，包括什么时候会不用 JDK 提供的动态代理，而用 CGLIB 实现。
- Spring 3.2 以后，spring-core 直接就把 CGLIB 和 ASM 的源码包括进来了，这也是为什么我们不需要显式引入这两个依赖
- Spring 的 IOC 容器和 AOP 都很重要，Spring AOP 需要依赖于 IOC 容器来管理。
- 如果你是 web 开发者，有些时候，你可能需要的是一个 Filter 或一个 Interceptor，而不一定是 AOP。
- Spring AOP 只能作用于 Spring 容器中的 Bean，它是使用纯粹的 Java 代码实现的，只能作用于 bean 的方法。
- Spring 提供了 AspectJ 的支持，后面我们会单独介绍怎么使用，一般来说我们用**纯的** Spring AOP 就够了。
- 很多人会对比 Spring AOP 和 AspectJ 的性能，Spring AOP 是基于代理实现的，在容器启动的时候需要生成代理实例，在方法调用上也会增加栈的深度，使得 Spring AOP 的性能不如 AspectJ 那么好。

**AspectJ：**

- AspectJ 出身也是名门，来自于 Eclipse 基金会，link：https://www.eclipse.org/aspectj

- 属于静态织入，它是通过修改代码来实现的，它的织入时机可以是：
  - Compile-time weaving：编译期织入，如类 A 使用 AspectJ 添加了一个属性，类 B 引用了它，这个场景就需要编译期的时候就进行织入，否则没法编译类 B。
  - Post-compile weaving：也就是已经生成了 .class 文件，或已经打成 jar 包了，这种情况我们需要增强处理的话，就要用到编译后织入。
  - **Load-time weaving**：指的是在加载类的时候进行织入，要实现这个时期的织入，有几种常见的方法。1、自定义类加载器来干这个，这个应该是最容易想到的办法，在被织入类加载到 JVM 前去对它进行加载，这样就可以在加载的时候定义行为了。2、在 JVM 启动的时候指定 AspectJ 提供的 agent：`-javaagent:xxx/xxx/aspectjweaver.jar`。

- AspectJ 能干很多 Spring AOP 干不了的事情，它是 **AOP 编程的完全解决方案**。Spring AOP 致力于解决的是企业级开发中最普遍的 AOP 需求（方法织入），而不是力求成为一个像 AspectJ 一样的 AOP 编程完全解决方案。

- 因为 AspectJ 在实际代码运行前完成了织入，所以大家会说它生成的类是没有额外运行时开销的。

- ~~很快我会专门写一篇文章介绍 AspectJ 的使用，以及怎么在 Spring 应用中使用 AspectJ。~~

  > 已成文：https://www.javadoop.com/post/aspectj



# Spring的设计模式

1、**简单工厂**(非23种设计模式中的一种) 

​	BeanFactory

2、**工厂方法**

​	FactoryBean接口。getObject(),getObjectType()

![图片](https://mmbiz.qpic.cn/mmbiz_jpg/1J6IbIcPCLb9RusArEDib1jibcb4Q4r8ruKsyHOwyZAKneeRI8ib5EM0XxntF4gna7KJrXibsN3Ijich0r3p7cKpHMA/640?wx_fmt=jpeg&random=0.4829789242312086&wxfrom=5&wx_lazy=1&wx_co=1)

3、**单例模式**	

```java
/** Cache of singleton objects: bean name to bean instance. */
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

/** Cache of singleton factories: bean name to ObjectFactory. */
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

/** Cache of early singleton objects: bean name to bean instance. */
private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);
```

4、**适配器模式**

AOP 的注解解析出来时Advice，部分是实现了MethodInterceptor接口，而在调用链中，必须得是MethodInterceptor类型，这个时候就需要适配器了，**需要一个类的方法返回实现了MethodInterceptor的类并包含advice属性。**

![image-20220701190744135](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220701190744135.png)

5、**装饰器模式**

Spring 的 `ApplicationContext` 中配置所有的 `DataSource`。这些 DataSource 可能是各种不同类型的， 比如不同的数据库：Oracle、 SQL Server、 MySQL 等， 也可能是不同的数据源。然后 SessionFactory 根据客户的每次请求， 将 DataSource 属性设置成不同的数据源， 以到达切换数据源的目的。

在 spring 的命名体现：Spring 中用到的包装器模式在类名上有两种表现：一种是类名中含有 `Wrapper`， 另一种是类名中含有`Decorator`。基本上都是动态地给一个对象添加一些额外的职责，比如

- `org.springframework.cache.transaction` 包下的 `TransactionAwareCacheDecorator` 类
- `org.springframework.session.web.http` 包下的 `SessionRepositoryFilter` 内部类 `SessionRepositoryRequestWrapper`

```java
//AbstractAutowireCapableBeanFactory.class
protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
      throws BeanCreationException {

   // Instantiate the bean.
   BeanWrapper instanceWrapper = null;
   if (mbd.isSingleton()) {
      instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
   }
   if (instanceWrapper == null) {
      instanceWrapper = createBeanInstance(beanName, mbd, args);
   }
```

BeanWrapper 相当于是Spring中的一个包装类，**对Bean 进行包装，具有（单独或批量）获取和设置属性值，获取属性描述符以及查询属性的可读性/可写性的能力。** **还可以进行类型的转换等功能**。

6、**代理模式**

​	AOP底层，就是动态代理模式的实现。动态代理就是，在程序运行期，创建目标对象的代理对象，并对目标对象中的方法进行功能性增强的一种技术。

7、**观察者模式**

spring的事件驱动模型使用的是 观察者模式 ，Spring中Observer模式常用的地方是listener的实现。

**ApplicationEvent** 事件

**ApplicationListener** 监听事件

**ApplicationEventPublisher** 发布事件

**ApplicationEventMulticaster** 转发事件

8、**策略模式**

Spring框架的资源访问Resource接口。该接口提供了更强的资源访问能力，Spring 框架本身大量使用了 Resource 接口来访问底层资源。

Resource 接口主要提供了如下几个方法:

- **getInputStream()：** 定位并打开资源，返回资源对应的输入流。每次调用都返回新的输入流。调用者必须负责关闭输入流。
- **exists()：** 返回 Resource 所指向的资源是否存在。
- **isOpen()：** 返回资源文件是否打开，如果资源文件不能多次读取，每次读取结束应该显式关闭，以防止资源泄漏。
- **getDescription()：** 返回资源的描述信息，通常用于资源处理出错时输出该信息，通常是全限定文件名或实际 URL。
- **getFile：** 返回资源对应的 File 对象。
- **getURL：** 返回资源对应的 URL 对象。

最后两个方法通常无须使用，仅在通过简单方式访问无法实现时，Resource 提供传统的资源访问的功能。

Resource 接口本身没有提供访问任何底层资源的实现逻辑，**针对不同的底层资源，Spring 将会提供不同的 Resource 实现类，不同的实现类负责不同的资源访问逻辑。**

Spring 为 Resource 接口提供了如下实现类：

- **UrlResource：** 访问网络资源的实现类。
- **ClassPathResource：** 访问类加载路径里资源的实现类。
- **FileSystemResource：** 访问文件系统里资源的实现类。
- **ServletContextResource：** 访问相对于 ServletContext 路径里的资源的实现类.
- **InputStreamResource：** 访问输入流资源的实现类。
- **ByteArrayResource：** 访问字节数组资源的实现类。

这些 Resource 实现类，针对不同的的底层资源，提供了相应的资源访问逻辑，并提供便捷的包装，以利于客户端程序的资源访问。

9、**模版方法模式**

好多都是这种方式，父类定义好总体逻辑，子类实现部分功能，好处代码复用，减少代码量，子类只需要关注部分功能的实现，整体功能的实现已在父类实现。



# AspectJ

[AspectJ](https://www.eclipse.org/aspectj/) 作为 AOP 编程的完全解决方案，提供了三种织入时机，分别为

1. compile-time：**编译期织入**，在编译的时候一步到位，直接编译出包含织入代码的 .class 文件
2. post-compile：**编译后织入**，增强已经编译出来的类，如我们要增强依赖的 jar 包中的某个类的某个方法
3. load-time：**在 JVM 进行类加载的时候进行织入**

**参考文章：**https://www.javadoop.com/post/aspectj



# SpringIOC执行流程

1. 加载资源（ResourceLoader），解析配置（BeanDefinitionReader）**DocumentLoader将Bean定义资源转换成Document对象的源码如下：该解析过程调用JavaEE标准的JAXP标准进行处理。**
2. 根据配置生成bean的实例，将bean放在容器中，生成常规其他应用bean
3. 根据name/type获取bean

![img](https://pdai.tech/images/spring/springframework/spring-framework-ioc-source-100.png)

![img](https://pdai.tech/images/spring/springframework/spring-framework-ioc-source-9.png)

- 初始化的入口在容器实现中的 refresh()调用来完成
- 对 bean 定义载入 IOC 容器使用的方法是 loadBeanDefinition,其中的大致过程如下：
  - 通过 ResourceLoader 来完成资源文件位置的定位，DefaultResourceLoader 是默认的实现，同时上下文本身就给出了 ResourceLoader 的实现，可以从类路径，文件系统, URL 等方式来定为资源位置。如果是 XmlBeanFactory作为 IOC 容器，那么需要为它指定 bean 定义的资源，也就是说 bean 定义文件时通过抽象成 Resource 来被 IOC 容器处理的
  - 通过 **BeanDefinitionReader**来完成定义信息的解析和 Bean 信息的注册, 往往使用的是XmlBeanDefinitionReader 来解析 bean 的 xml 定义文件 - 实际的处理过程是委托给 BeanDefinitionParserDelegate 来完成的，从而得到 bean 的定义信息，这些信息在 Spring 中使用 BeanDefinition 对象来表示 - 这个名字可以让我们想到loadBeanDefinition,RegisterBeanDefinition 这些相关的方法 - 他们都是为处理 BeanDefinitin 服务的
  - 容器解析得到 BeanDefinition 以后，需要把它在 IOC 容器中注册，这由 IOC 实现 **BeanDefinitionRegistry** 接口来实现。注册过程就是在 IOC 容器内部维护的一个HashMap 来保存得到的 BeanDefinitiond 的过程。这个 HashMap 是 IoC 容器持有 bean 信息的场所，以后对 bean 的操作都是围绕这个HashMap 来实现的.
- 然后我们就可以通过 BeanFactory 和 ApplicationContext 来享受到 Spring IOC 的服务了,在使用 IOC 容器的时候，我们注意到除了少量粘合代码，绝大多数以正确 IOC 风格编写的应用程序代码完全不用关心如何到达工厂，因为容器将把这些对象与容器管理的其他对象钩在一起。基本的策略是把工厂放到已知的地方，最好是放在对预期使用的上下文有意义的地方，以及代码将实际需要访问工厂的地方。 Spring 本身提供了对声明式载入 web 应用程序用法的应用程序上下文,并将其存储在ServletContext 中的框架实现。

# Bean的生命周期

**doGetBean大体流程**

- 解析bean的真正name，如果bean是工厂类，name前缀会加&，需要去掉
- 无参单例先从缓存中尝试获取
- 如果bean实例还在创建中，则直接抛出异常
- 如果bean definition 存在于父的bean工厂中，委派给父Bean工厂获取
- 标记这个beanName的实例正在创建
- 确保它的依赖也被初始化
- 真正创建 
  - 单例时
  - 原型时
  - 根据bean的scope创建

**Spring如何解决循环依赖问题**

> Spring只是解决了单例模式下属性依赖的循环问题；Spring为了解决单例的循环依赖问题，使用了三级缓存。

**Bean的完整生命周期经历了各种方法调用，这些方法可以划分为以下几类**：(结合上图，需要有如下顶层思维)

- **Bean自身的方法**： 这个包括了Bean本身调用的方法和通过配置文件中`<bean>`的**init-method**和**destroy-method**指定的方法

![image-20230310100053240](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230310100053240.png)

- **Bean级生命周期接口方法**： 这个包括了**BeanNameAware**、**BeanFactoryAware**、**ApplicationContextAware**；当然也包括**InitializingBean**和**DiposableBean**这些接口的方法（可以被**@PostConstruct**和**@PreDestroy**注解替代)
- **容器级生命周期接口方法**： 这个包括了**InstantiationAwareBeanPostProcessor** 和 **BeanPostProcessor** 这两个接口实现，一般称它们的实现类为“后处理器”。
- **工厂后处理器接口方法**： 这个包括了**AspectJWeavingEnabler**, **ConfigurationClassPostProcessor**, **CustomAutowireConfigurer**等等非常有用的工厂后处理器接口的方法。工厂后处理器也是容器级的。在应用上下文装配配置文件之后立即调用。

![img](https://pdai.tech/images/spring/springframework/spring-springframework-aop-3.png)

**should()方法执行流程**

```java
//AspectJAwareAdvisorAutoProxyCreator#shouldSkip()
//AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors()
//		BeanFactoryAdvisorRetrievalHelper#findAdvisorBeans()
//
	@Override
	protected boolean shouldSkip(Class<?> beanClass, String beanName) {
		// TODO: Consider optimization by caching the list of the aspect names
		//考虑通过缓存切面名称列表进行优化
		List<Advisor> candidateAdvisors = findCandidateAdvisors();
		for (Advisor advisor : candidateAdvisors) {
			//对于<aop:aspect/>中的<aop:before/>类似的生成的是 AspectJPointcutAdvisor
			//Advice就是AbstractAspectJAdvice的子类
			if (advisor instanceof AspectJPointcutAdvisor &&
					((AspectJPointcutAdvisor) advisor).getAspectName().equals(beanName)) {
				return true;
			}
		}
		return super.shouldSkip(beanClass, beanName);
	}

	@Override
	protected List<Advisor> findCandidateAdvisors() {
		// Add all the Spring advisors found according to superclass rules.
    /**
    * 找到所有 implements Advisor 接口的
    * xml配置的 advice 最终都会转成 AspectJPointcutAdvisor
    * BeanFactoryAdvisorRetrievalHelper#isEligibleBean() 钩子方法，默认为true
    */ 
		List<Advisor> advisors = super.findCandidateAdvisors();
		// Build Advisors for all AspectJ aspects in the bean factory.
		// 在从所有切面中解析得到Advisor对象 getAdvisor()
		if (this.aspectJAdvisorsBuilder != null) {
			//注解方式，找到系统中使用@Aspectj标注的Bean，并且找到该bean中使用@Before、@After等标注的方法，将这些方法封装成一个一个Advisor
			advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
		}
		return advisors;
	}


```

# 揭秘Java热部署原理及JRebel(Hotcode)的实现原理

**参考文章：**https://blog.csdn.net/weixin_34221036/article/details/86264463



# Spring版版本发展

![image-20230814174306615](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230814174306615.png)



# HikariCP为什么这么快

- 它使用 `FastList` 替代 `ArrayList`，通过初始化的默认值，减少了越界检查的操作；
- 优化并精简了字节码，通过使用 `Javassist`，减少了动态代理的性能损耗，比如使用 `invokestatic` 指令代替 `invokevirtual` 指令；
- 实现了无锁的 `ConcurrentBag`，减少了并发场景下的锁竞争。

注：Javassist是一个开源的分析、编辑和创建Java字节码的类库，可以直接编辑和生成Java生成的字节码。相对于bcel, asm等这些工具，开发者不需要了解虚拟机指令，就能动态改变类的结构，或者动态生成类。javassist简单易用， 快速。

# @Autowired 和 @Resource 区别

1. 来源不同：@Autowired 来自 Spring 框架，而 @Resource 来自于（Java）JSR-250；

   **小知识：**JSR 是 Java Specification Requests 的缩写，意思是“Java 规范提案”。任何人都可以提交 JSR 给 Java 官方，但只有最终确定的 JSR，才会以 JSR-XXX 的格式发布，如 JSR-250，而被发布的 JSR 就可以看作是 Java 语言的规范或标准。

2. 依赖查找的顺序不同：@Autowired 先根据类型再根据名称查询，而 @Resource 先根据名称再根据类型查询；

3. 支持的参数不同：@Autowired 只支持设置 1 个参数，而 @Resource 支持设置 7 个参数；

4. 依赖注入的用法支持不同：@Autowired 既支持构造方法注入，又支持属性注入和 Setter 注入，而 @Resource 只支持属性注入和 Setter 注入；

5. 编译器 IDEA 的提示不同：当注入 Mapper 对象时，使用 @Autowired 注解编译器会提示错误，而使用 @Resource 注解则不会提示错误。

# CURL是什么？

**curl是一个非常实用的、用来与服务器之间传输数据的工具**；支持的协议包括 (DICT, FILE, FTP, FTPS, GOPHER, HTTP, HTTPS, IMAP, IMAPS, LDAP, LDAPS, POP3, POP3S, RTMP, RTSP, SCP, SFTP, SMTP, SMTPS, TELNET and TFTP)，curl设计为无用户交互下完成工作；curl提供了一大堆非常有用的功能，包括代理访问、用户认证、ftp上传下载、HTTP POST、SSL连接、cookie支持、断点续传...。



# springAOP和AspectJ有关系吗？

**AOP是通过“预编译方式”和“运行期间动态代理”实现程序功能的统一维护的一种技术。AOP是一个概念，其实现技术有AspectJ和springAOP**。

### 1、AspectJ

AspcetJ作为AOP的一种实现，是基于编译的方式实现的AOP，在程序运行期是不会做任何事情的，因为类和切面是直接编译在一起的。AspectJ 使用了三种不同类型的织入方式，**使用的是编译期和类加载时进行织入**

1. Compile-time weaving：**编译期织入**。编译器将切面和应用的源代码编译在一个字节码文件中。
2. Post-compile weaving：**编译后织入**。也称为二进制织入。将已有的字节码文件与切面编制在一起。
3. Load-time weaving:**加载时织入**。与编译后织入一样，只是织入时间会推迟到类加载到jvm时。

### 2、springAOP

springAOP作为AOP的一种实现，基于动态代理的实现AOP，意味着实现目标对象的切面会创建一个代理类，代理类的实现有两种不同的模式，分为两种不同的代理，**Spring AOP利用的是运行时织入，在springAOP中连接点是方法的执行。**

1. JDK动态代理；
2. cglib动态代理；



## RT响应时间

响应时间是指系统对请求作出响应的时间。



# @TransactionalEventListener

事务完成后处理，

1、工作方法：使用@**TransactionalEventListener**在方法上，监听对应的事件，**TransactionalEventListenerFactory**就会在匹配的方法里面创建一个监听器**ApplicationListenerMethodTransactionalAdapter**

![image-20230821113050566](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230821113050566.png)

2、在对应事务方法中发布事件，这个类就会注册事务方法并设置到当前事务线程中

![image-20230821113219701](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230821113219701.png)

![image-20230821113136945](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230821113136945.png)

3、当事务结束之后

**completeTransactionAfterThrowing**嵌套方法异常抛出调用
**triggerBeforeCompletion**

triggerAfterCompletion

cleanupAfterCompletion

**commitTransactionAfterReturning**

​		processRollback

​				triggerBeforeCompletion

​				triggerAfterCompletion

​				cleanupAfterCompletion

​		processCommit

​				triggerBeforeCommit(status);

​		        triggerBeforeCompletion(status);

​				triggerAfterCompletion

​				triggerAfterCommit

​				cleanupAfterCompletion

@EnableTransactionManagement（TransactionManagementConfigurationSelector）会注入两个类，一个是AutoProxyRegistrar，另一个是**ProxyTransactionManagementConfiguration**，其中ProxyTransactionManagementConfiguration父类会注入一个**TransactionalEventListenerFactory**的bean,在所有bean注册完成之后会查找所有实现了SmartInitializingSingleton接口的调用其afterSingletonsInstantiated方法，其中调用TransactionalEventListenerFactory的就是**EventListenerMethodProcessor**类。

[EventListenerMethodProcessor创建方式](#Spring默认启动的时候就会创建几个BeanDefinition) 

![image-20230821114312050](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230821114312050.png)

