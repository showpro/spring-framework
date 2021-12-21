package com.zhan.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.zhan.app.AppConfig;
import com.zhan.beanfactoryprocessor.MyBeanFactoryPostProcessor1;
import com.zhan.dao.IndexDao;

/**
 * @author zhanzhan
 * @since 2020/3/27 18:45
 *
 * 测试类
 */
public class Test1 {

    public static void main(String[] args) {

        /**
         * 程序启动时，需要对spring环境进行初始化工作。
         * 对spring大的环境进行初始化，其中又需要对beanFactory工厂进行初始化。
         * spring开放了BeanFactoryPostProcessor接口，实现bean工厂后置处理器可以让你插手bean工厂实例化过程
         *
         * Spring容器在本文可以简单理解为DefaultListableBeanFactory,它是BeanFactory的实现类，
         */


        // *********初始化spring环境***********
        // AnnotationConfigApplicationContext：整个spring上下文环境
        // 1、准备工厂== DefaultListableBeanFactory
        // 实例化一个bdReader 和 scanner
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();

        // 2、把一个class转换成bd,最后put到map
        // map位置: DefaultListableBeanFactory 的属性 Map<String, BeanDefinition> beanDefinitionMap
        ac.register(AppConfig.class);//传一个配置类
        // ac.register(IndexDao.class);//传一个普通类

        // 程序员自己写的，并且没有交给spring管理, 也就是没有加上@Component, 需手动给spring
        ac.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor1());

        // 3、初始化spring环境
        ac.refresh();
        System.out.println("spring环境初始化完成...");

        IndexDao indexDao = ac.getBean(IndexDao.class);
        System.out.println("-------------------------");
        IndexDao indexDao1 = ac.getBean(IndexDao.class);
        // indexDao单例改成原型了，两者hashCode不相同了
        System.out.println(indexDao.hashCode() + "=========" + indexDao1.hashCode());
        //System.out.println(indexDao);
        //indexDao.query();

    }
}
