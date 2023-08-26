# SpringCloudGateway

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/12/5/1677ca514d6ba46b~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

![spring-cloud-gateway-work.png](https://segmentfault.com/img/remote/1460000019101832)

Spring Cloud Gateway 的Filter的种类有两种，一：GatewayFilter (单一的过滤器)。二：GlobalFilter(全局过滤器)

# @Valid与@Validated注解

@Valid常见用在方法，类中字段上进行校验,java的核心包

@Validated是spring提供的对@Valid的封装，常见用在方法上进行校验

但在分组、注解位置、嵌套验证等功能上有所不同，这里主要就这几种情况进行说明。

- `@Validated`：用在类型、方法和方法参数上。但不能用于成员属性（field）
- `@Valid`：可以用在构造函数、方法、方法参数、和成员属性（field）上

## **[分组校验]**

- `@Validated`：提供分组功能，可以在参数验证时，根据不同的分组采用不同的验证机制
- `@Valid`：没有分组功能

## **[嵌套校验]**

一个待验证的pojo类，其中还包含了待验证的对象，需要在待验证对象上注解`@Valid`
，才能验证待验证对象中的成员属性，这里不能使用`@Validated`。

