package com.iyang.springboot.condition;

import com.iyang.springboot.condition.condition.SelfConditionAnno;
import com.iyang.springboot.condition.pojo.Student;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
@SpringBootApplication
public class SpringBootConditionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBootConditionApplication.class, args);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Object student = null;
        try {
            student = beanFactory.getBean("student");
        } catch (Exception e){
        }
        System.out.println(student);

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

       /* List<String> list = new ArrayList<>();
        if(false){
            list.add("1");
        }
        System.out.println(list.size() == 0);
        boolean i = !CollectionUtils.isEmpty(list);*/

    }

    // @SelfConditionAnno
    @Bean
    // @ConditionalOnBean(name = { "springBootConditionApplication" })
    // @ConditionalOnClass(name = { "com.iyang.springboot.condition.pojo.Student" })
    // @ConditionalOnExpression("#{1==2}")
    public Student student(){
        return new Student(12,"GavinYang");
    }

    public void loadContext(){
        SpringFactoriesLoader.loadFactoryNames(SpringApplication.class,this.getClass().getClassLoader());
    }

}
