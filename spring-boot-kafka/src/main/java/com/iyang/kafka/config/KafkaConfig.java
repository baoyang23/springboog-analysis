package com.iyang.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yang on 2021/2/6 0:17
 *
 * Kafka Config.
 */


@Configuration
public class KafkaConfig {

    @Value("${consumer.groupid:baoyang_consumer}")
    private String consumerGroupId;

    @Value("${consumer.bootstrap.servers:thisforyou.cn:9092}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfigs(String groupId) {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public Map<String,Object> consumerConfig(){
        return consumerConfigs(consumerGroupId);
    }

    @Bean
    public KafkaListenerContainerFactory<?> consumerListener(){

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000L);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;

    }


}
