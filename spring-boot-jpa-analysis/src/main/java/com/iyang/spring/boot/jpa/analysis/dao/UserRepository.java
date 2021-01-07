package com.iyang.spring.boot.jpa.analysis.dao;

import com.iyang.spring.boot.jpa.analysis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

/**
 * Created by Yang on 2021/1/7 22:07
 */

public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 异步查询结果.
     * @param name
     * @return
     */
    @Async
    Future<User> findByName(String name);

}
