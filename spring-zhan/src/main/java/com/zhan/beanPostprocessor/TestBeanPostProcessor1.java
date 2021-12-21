package com.zhan.beanPostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义BeanPostProcessor
 *
 * @author zhanzhan
 * @date 2021/12/18 21:20
 */
@Component
public class TestBeanPostProcessor1 implements BeanPostProcessor, Ordered {

    /**
     * 在bean实例初始化前执行
     *
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("indexDao")) {
            System.out.println("BeforeInitialization1...");
        }

        // todo 这里可以对传入的bean进行自定义代理，然后将代理对象返回出去。AOP就是这么做的

        return bean;
    }

    /**
     * 在bean实例初始化完成时执行
     *
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("indexDao")) {
            System.out.println("AfterInitialization1...");
        }

        return bean;
    }

    /**
     * 值越小越先执行
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 100;
    }
}
