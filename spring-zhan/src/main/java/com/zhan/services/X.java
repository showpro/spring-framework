package com.zhan.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zhanzhan
 * @since 2020/3/27 18:35
 */
@Component
public class X implements ApplicationContextAware, InitializingBean {

    @Autowired
    private Y y;

    public X() {
        System.out.println("调用X构造方法，create X， X实例化了");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean");
    }

    /**
     * ApplicationContextAware 回调方法
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Aware");
    }

    @PostConstruct
    public void init() {
        System.out.println("anno init");
    }

    // public void setY(Y y) {
    //     this.y = y;
    // }

}
