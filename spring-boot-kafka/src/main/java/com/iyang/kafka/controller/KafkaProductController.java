package com.iyang.kafka.controller;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Yang on 2021/2/6 0:26
 */


@RestController
@RequestMapping("/kafka")
public class KafkaProductController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping
    public void sendMesToKafka(String message){

        kafkaTemplate.send("baoyang_test_1",message);


    }


}
