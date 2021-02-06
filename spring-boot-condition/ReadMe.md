## 						SpringBoot 中条件注解分析



#### 题记

​	SpringBoot 中的条件注解就是可以根据是否有对应的条件来判断是不是要注入bean到Spring容器中来.

​    所以去看这些 Condition 是否满足的条件是很有必要的.

​	

#### 介绍

可以看到 源码中的 Condition 条件.

![](https://raw.githubusercontent.com/baoyang23/images_repository/master/springboot/condition/all_sb_condition.png)



| 条件注解                       | 作用                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| ConditionalOnBean              | 当Spring容器中有你配置的bean的时候,就会满足条件.可以根据class/type/annotation/name/parameterizedContainer(也是class)来进行条件匹配. |
| ConditionalOnCloudPlatform     | 对应的云平台. 可以看org.springframework.boot.cloud.CloudPlatform中的枚举 NONE/CLOUD_FOUNDRY/HEROKU/SAP/KUBERNETES 选项. |
| ConditionalOnClass             | 判断是否有对应的class,如果有的话,就满足条件. 可以根据 name(全类名) / value(class) 来进行条件匹配. |
| ConditionalOnExpression        | 根据你的el表达式是不是成立,也就是是不是true.如果是true的话,就是满足条件的. |
| ConditionalOnJava              | 根据java的版本判断是不是成立的.  org.springframework.boot.system.JavaVersion 可以参考这个类中存放的  Java 版本 , 也就是说 SpringBoot 最低都是需要 java8 才可以支持的. |
| ConditionalOnJndi              |                                                              |
| ConditionalOnMissingBean       | Spring容器中不存在某个bean的时候，就满足condition的条件.     |
| ConditionalOnMissingClass      | 被Spring扫描到的类中,不存在配置的class就满足条件.            |
| ConditionalOnNotWebApplication | 不是一个Web的应用就满足条件.                                 |
| ConditionalOnProperty          | 根据存在某个 property 属性就满足条件.                        |
| ConditionalOnResource          | 在resource 下存在某个资源就是满足条件的.                     |
| ConditionalOnSingleCandidate   |                                                              |
| ConditionalOnWarDeployment     | 是不是war包方式部署                                          |
| ConditionalOnWebApplication    | 是WebApplication项目就满足条件.                              |



可以看到SpringBoot还是提供了蛮多Condition的条件匹配, 并且可以看到每个 条件注解里面都有一个 @Conditional , 并且每个 @Condition 都指向了一个类，也就是指向的这个类,就是真正干活的,最后返回的是一个true/false来决定是不是满足条件.



#### 源码阅读



​	

这里我们直接将断点给打到该类的该方法上

**org.springframework.boot.autoconfigure.condition.SpringBootCondition#matches**

```java
@Override
public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
   String classOrMethodName = getClassOrMethodName(metadata);
   try {
// 可以看到该方法是一个抽象的方法,也就是留给了子类去实现的.       
      ConditionOutcome outcome = getMatchOutcome(context, metadata);
// 如果log的级别是trace级别的话,这里就会打印出来的.       
      logOutcome(classOrMethodName, outcome);
// 记录一下到 ConditionEvaluationReport report 中来,也就是记录到report中来.       
      recordEvaluation(context, classOrMethodName, outcome);
       // 返回是否匹配到的结果.
      return outcome.isMatch();
   }
   catch (NoClassDefFoundError ex) {
      throw new IllegalStateException("Could not evaluate condition on " + classOrMethodName + " due to "
            + ex.getMessage() + " not found. Make sure your own configuration does not rely on "
            + "that class. This can also happen if you are "
            + "@ComponentScanning a springframework package (e.g. if you "
            + "put a @ComponentScan in the default package by mistake)", ex);
   }
   catch (RuntimeException ex) {
      throw new IllegalStateException("Error processing condition on " + getName(metadata), ex);
   }
}
```





**org.springframework.boot.autoconfigure.condition.OnBeanCondition#getMatchOutcome**

可以看到 OnBeanCondition中是处理了 @ConditionalOnBean / @ConditionalOnSingleCandidate / @ConditionalOnMissingBean 等三个条件注解.

```java
@Override
public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
 // new 一个ConditionMessage 出来,用于记录.   
   ConditionMessage matchMessage = ConditionMessage.empty();
   MergedAnnotations annotations = metadata.getAnnotations();

// 判断是不是@ConditionalOnBean这个注解的    
   if (annotations.isPresent(ConditionalOnBean.class)) {
 // 创建一个 Spec 对象,该对象中存context,metadata等信息.     
      Spec<ConditionalOnBean> spec = new Spec<>(context, metadata, annotations, ConditionalOnBean.class);
// 这里是去配置 bean 等信息的.
// 最后返回的也是 匹配的结果.       
      MatchResult matchResult = getMatchingBeans(context, spec);
// 这里是全部没有匹配上       
      if (!matchResult.isAllMatched()) {
// did not find any beans of type org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration
// 这里是没有匹配上返回的 reason 内容.
         String reason = createOnBeanNoMatchReason(matchResult);
// 最后返回的一个 ConditionOutcome回来.
// 这里也就是返回到父类里面来了.          
         return ConditionOutcome.noMatch(spec.message().because(reason));
      }
      matchMessage = spec.message(matchMessage).found("bean", "beans").items(Style.QUOTE,
            matchResult.getNamesOfAllMatches());
   }
    
// 对 @ConditionalOnSingleCandidate 注解进行判断    
   if (metadata.isAnnotated(ConditionalOnSingleCandidate.class.getName())) {
      Spec<ConditionalOnSingleCandidate> spec = new SingleCandidateSpec(context, metadata, annotations);
      MatchResult matchResult = getMatchingBeans(context, spec);      
      if (!matchResult.isAllMatched()) {
         return ConditionOutcome.noMatch(spec.message().didNotFind("any beans").atAll());
      }
      else if (!hasSingleAutowireCandidate(context.getBeanFactory(), matchResult.getNamesOfAllMatches(),
            spec.getStrategy() == SearchStrategy.ALL)) {
         return ConditionOutcome.noMatch(spec.message().didNotFind("a primary bean from beans")
               .items(Style.QUOTE, matchResult.getNamesOfAllMatches()));
      }
      matchMessage = spec.message(matchMessage).found("a primary bean from beans").items(Style.QUOTE,
            matchResult.getNamesOfAllMatches());
   }
    
// 对 是否有 @ConditionalOnMissingBean 注解进行判断.    
   if (metadata.isAnnotated(ConditionalOnMissingBean.class.getName())) {
      Spec<ConditionalOnMissingBean> spec = new Spec<>(context, metadata, annotations,
            ConditionalOnMissingBean.class);
      MatchResult matchResult = getMatchingBeans(context, spec);
 // 任意匹配上一个即可.      
      if (matchResult.isAnyMatched()) {
         String reason = createOnMissingBeanNoMatchReason(matchResult);
         return ConditionOutcome.noMatch(spec.message().because(reason));
      }
      matchMessage = spec.message(matchMessage).didNotFind("any beans").atAll();
   }
   return ConditionOutcome.match(matchMessage);
}
```



   其实该方法返回的 ConditionOutcome 中的属性 match 就已经表示是否已经匹配到了.

   其他的Condition条件也可以根据这个来进行类似的分析.



#### 溯源分析

​    这里也对进入到 Condition 这步来进行分析.

​    org.springframework.context.annotation.ConditionEvaluator  这里我们可以看到这个类,该类就是进行Condition的解析已经shouldSkip方法调用的.

​     shouldSkip 方法分析

```java
/**
 * Determine if an item should be skipped based on {@code @Conditional} annotations.
 * @param metadata the meta data
 * @param phase the phase of the call
 * @return if the item should be skipped
 */
public boolean shouldSkip(@Nullable AnnotatedTypeMetadata metadata, @Nullable ConfigurationPhase phase) {
// 没有注解或者解析没有@Conditional就会返回false,也就是不会跳过.    
   if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
      return false;
   }

// 如果是注解 @Component/@ComponentScan/@Import/@ImportResource/@Bean注解的话,
// 就会递归调用shouldSkip方法来进行解析, 并且使用PARSE_CONFIGURATION阶段来标识. 
   if (phase == null) {
      if (metadata instanceof AnnotationMetadata &&
            ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
         return shouldSkip(metadata, ConfigurationPhase.PARSE_CONFIGURATION);
      }
// 否则就不是上面的那些注解,使用的事REGISTER_BEAN来标识.       
      return shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN);
   }

   List<Condition> conditions = new ArrayList<>();
// 获取出 Condition进行迭代,最后添加到conditions中来.    
   for (String[] conditionClasses : getConditionClasses(metadata)) {
      for (String conditionClass : conditionClasses) {
 // 获取注解 @Conditional 中配置的类.         
         Condition condition = getCondition(conditionClass, this.context.getClassLoader());
 // 添加到集合中来.          
         conditions.add(condition);
      }
   }

// 对 conditions 进行排序.
   AnnotationAwareOrderComparator.sort(conditions);

   for (Condition condition : conditions) {
      ConfigurationPhase requiredPhase = null;
   // 如果 condition 是ConfigurationCondition,就从其获取出 ConfigurationPhase 来.     
      if (condition instanceof ConfigurationCondition) {
         requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
      }
 // 如果 requiredPhase 是null或者与传入进来的是一样的 并且 matches 返回的结果是 false的话
 // 就要跳过解析. 否则就不跳过该bean的解析.
      if ((requiredPhase == null || requiredPhase == phase) && !condition.matches(this.context, metadata)) {
         return true;
      }
   }

   return false;
}
```



**Report 记录**

  org.springframework.boot.autoconfigure.condition.SpringBootCondition#recordEvaluation  我们可以看到该方法.

 可以看到 recordConditionEvaluation 方法.

```java
private void recordEvaluation(ConditionContext context, String classOrMethodName, ConditionOutcome outcome) {
   if (context.getBeanFactory() != null) {
      ConditionEvaluationReport.get(context.getBeanFactory()).recordConditionEvaluation(classOrMethodName, this,
            outcome);
   }
}
```



org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport#recordConditionEvaluation

最后也是到  autoConfigurationReport  这个 bean 中来了 , 也就是每次的 ConditionOutcome 都会被记录到ConditionEvaluationReport 中来.

```java
/**
 * Record the occurrence of condition evaluation.
 * @param source the source of the condition (class or method name)
 * @param condition the condition evaluated
 * @param outcome the condition outcome
 */
public void recordConditionEvaluation(String source, Condition condition, ConditionOutcome outcome) {
   Assert.notNull(source, "Source must not be null");
   Assert.notNull(condition, "Condition must not be null");
   Assert.notNull(outcome, "Outcome must not be null");
   this.unconditionalClasses.remove(source);
   if (!this.outcomes.containsKey(source)) {
      this.outcomes.put(source, new ConditionAndOutcomes());
   }
   this.outcomes.get(source).add(condition, outcome);
   this.addedAncestorOutcomes = false;
}
```



 在启动后，可以从 context 中获取出获取出来.

 然后迭代出来打印出信息来.

```java
ConditionEvaluationReport report = context.getBean(ConditionEvaluationReport.class);
Map<String, ConditionEvaluationReport.ConditionAndOutcomes> conditionAndOutcomesBySource =
        report.getConditionAndOutcomesBySource();
for(Map.Entry<String,ConditionEvaluationReport.ConditionAndOutcomes> entry:
        conditionAndOutcomesBySource.entrySet()){
    String key = entry.getKey();
    ConditionEvaluationReport.ConditionAndOutcomes value = entry.getValue();
    while (value.iterator().hasNext()) {
        ConditionEvaluationReport.ConditionAndOutcome next = value.iterator().next();
        System.out.println("key is ---> " + key + "  /// " +
                           next.getCondition().getClass().getSimpleName() + " ## " + next.getOutcome());
    }
}
```



#### 总结

   可以看到SpringBoot在处理Condition的时候,大致也是利用一个注解对应一个类来进行处理.  

   TODO