package com.iyang.spring.boot.jpa.analysis.controller;

import com.iyang.spring.boot.jpa.analysis.dao.UserRepository;
import com.iyang.spring.boot.jpa.analysis.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yang on 2021/1/7 22:08
 */

@RestController
@RequestMapping
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 这里注入进来的是一个代理的加强对象.
     */
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/add")
    public Object addUser(@RequestParam String name , @RequestParam String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        //  class com.sun.proxy.$Proxy76  JDK 代理
        LOGGER.info(" The userRepository class adress string is ---> {} " , userRepository.getClass().toString());
        return userRepository.save(user);
    }

    @GetMapping(value = "/all")
    public Object getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping(value = "/page")
    public Object page(){
        Sort.Order name = new Sort.Order(Sort.Direction.ASC, "name");
        List<Sort.Order> orders = Arrays.asList(name);
        Sort sort = Sort.by(orders);
        return userRepository.findAll(PageRequest.of(1,2,sort));
    }

}
