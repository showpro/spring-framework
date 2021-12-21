package com.zhan.beanfactoryprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * 自定义BeanFactoryPostProcessor(bean工厂的后置处理器)，可以让你插手bean工厂，在spring的bean创建之前修改bean的定义属性。
 * 将BeanDefinition对象put到bdmap后，spring会调用自定义bean工厂的后置处理器重写的方法
 *
 * @author zhanzhan
 * @since 2020/3/30 16:53
 */
// @Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 在这个方法中插手bean工厂
     *
     * BeanDefinitionMap是封装在BeanFactory类中，而不是直接传进postProcessBeanFactory方法中去的
     * spring容器会在启动的时候调用你重写的这个方法，并且把ConfigurableListableBeanFactory类的对象传给你；
     *
     * @param beanFactory the bean factory used by the application context  里面有BeanDefinitionMap
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 根据beanName从bean工厂中的beanDefinitionMap取出X的BeanDefinition
        AbstractBeanDefinition x = (AbstractBeanDefinition) beanFactory.getBeanDefinition("x");

        // 这里可以改变x 的BeanDefinition中bean类型为Z,运行Test测试,那么此时容器中beanName=x没有对应的X类，beanName=x对应的类为Z
        // ac.getBean(X.class) 此时获取bean X Spring报错
        // x.setBeanClass(Z.class);



    }
}
