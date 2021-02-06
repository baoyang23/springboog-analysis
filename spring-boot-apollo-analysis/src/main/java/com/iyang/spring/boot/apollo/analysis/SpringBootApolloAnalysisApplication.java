package com.iyang.spring.boot.apollo.analysis;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.iyang.spring.boot.apollo.analysis.bean.FBean;
import com.iyang.spring.boot.apollo.analysis.bean.SonBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yang
 */

@RestController
@RequestMapping("/boot")
@EnableApolloConfig
@SpringBootApplication
public class SpringBootApolloAnalysisApplication {

    @Value("${name:GavinYang}")
    private String name;

    /**
     * Get Method Request.
     * @return
     */
    @GetMapping
    public String boot(){
        return name;
    }

    /**
     * 使用 apollo.longPollingInitialDelayInMills 也就是启动线程来从阿波罗上面 pull 消息操作.
     * 可以发现如果是设置的时间长了的话, ${name} 的配置属性是更新不过来的.
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("apollo.longPollingInitialDelayInMills","1000000000");

        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(SpringBootApolloAnalysisApplication.class, args);

        FBean bean = applicationContext.getBean(FBean.class);
        System.out.println(bean == null);
        bean.doIt();

    }


    /**
     * 注入进来的 SonBean , 会将其父类 FBean 也给注入进来.
     * @return
     */
    @Bean
    public SonBean sonBean(){

        System.out.println(name);
        return new SonBean();
    }

}
