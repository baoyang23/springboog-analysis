package com.iyang.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Yang on 2021/2/6 0:23
 */

@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = { "${baoyang.test.topic:baoyang_test_1}" } , containerFactory = "consumerListener")
    public void listener(List<ConsumerRecord<?, ?>> records, Acknowledgment ack){

        try {
            records.stream().forEach(consumerRecord -> {
               /* System.out.println(consumerRecord.offset());
                System.out.println(consumerRecord.value().toString());
                System.out.println("----------  分割线 -----");*/
                LOGGER.info(" Offset 的值是 ---> {} ",consumerRecord.offset());
                LOGGER.info(" Value  的值是 ---> {} " , consumerRecord.value().toString());
                LOGGER.info(" 消费的 topic 对应的分区 ---> {} ", consumerRecord.partition());
            });

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            ack.acknowledge();
        }

    }


}
