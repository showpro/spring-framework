package com.zhan.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

/**
 * @author zhanzhan
 * @date 2021/12/14 22:05
 */
@Repository
public class IndexDao implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public IndexDao() {
        System.out.println("IndexDao 实例化");
    }

    @PostConstruct
    public void init() {
        System.out.println("IndexDao 初始化");
    }

    public void query() {
        System.out.println("query...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("Aware");
    }
}
