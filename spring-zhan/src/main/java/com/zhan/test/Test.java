package com.zhan.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.zhan.app.AppConfig;
import com.zhan.services.X;

/**
 * @author zhanzhan
 * @since 2020/3/27 18:45
 *
 * 测试类
 */
public class Test {
    /**
     * java类初始化：
     * java类的生命周期：加载——验证——准备——解析——初始化——使用——卸载
     * 虚拟机规范严格规定了有且只有5种情况必须立即对类进行初始化：
     *
     * java对象（实例）初始化：编译器为每个类生成至少一个实例初始化方法，即<init>()方法。
     * Java对象初始化的时机：对象初始化又称为对象实例化。Java对象在其被创建时初始化。
     *
     * @param args
     */

    public static void main(String[] args) {

        // *********初始化spring环境***********
        // spring 上下文初始化完成, 就完成了包扫描和实例化bean的过程
        // spring会去扫描com.zhan包下符合spring规则的类（加注解的类）
        // 这里要明白选用的AnnotationConfigApplicationContext容器创建的是哪个bean工厂，因为有很多Spring容器，不同容器创建的bean工厂不一样
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("获取容器中的bean：" + ac.getBean(X.class));

        // ********* 也可以这样初始化spring环境(注册单个bean的方式) ***********
        // AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        // ac.register(Appconfig.class);
        // ac.refresh();// 这个方法里初始化spring容器
        // System.out.println(ac.getBean(X.class));

        // 测试改变BeanDefinitionMap中 X bean 为 Z
        // System.out.println(ac.getBean(Z.class));
        // System.out.println(ac.getBean("x")); //结果为Z

        /**
         * 在进行Spring源码阅读之前，需要先理解几个概念。
         * 1、Spring会将所有交由Spring管理的类，扫描其class文件，将其解析成BeanDefinition，
         * 在BeanDefinition中会描述类的信息，例如:这个类是否是单例的，Bean的类型，是否是懒加载，依赖哪些类，自动装配的模型。
         * Spring创建对象时，就是根据BeanDefinition中的信息来创建Bean。
         *
         * 2、Spring容器在本文可以简单理解为DefaultListableBeanFactory,它是BeanFactory的实现类，
         * 这个类有几个非常重要的属性：beanDefinitionMap是一个map，用来存放bean所对应的BeanDefinition；
         * beanDefinitionNames是一个List集合，用来存放所有bean的name；
         * singletonObjects是一个Map，用来存放所有创建好的单例Bean。
         *
         * 3、Spring中有很多后置处理器，但最终可以分为两种，一种是BeanFactoryPostProcessor，一种是BeanPostProcessor。
         * 前者的用途是用来干预BeanFactory的创建过程，后者是用来干预Bean的创建过程。
         * 后置处理器的作用十分重要，bean的创建以及AOP的实现全部依赖后置处理器。
         */

        /**
         *  bean 生命周期 / 怎么被实例化出来的？
         *
         * 1、扫描包---->得到包下的class（类）
         * 2、每个class（类）信息--->以bd对象的形式(来保存类的信息)--->put map当中
         * 3、遍历 map 拿出 bd
         * 4、validate验证（验证这些类是否抽象，是不是单列，是否懒加载，实例化之前需要做哪些事情等等......）
         * 5、通过beanDefinition.getBeanClass()得到class（类） ---> 根据类推断构造方法(因为有可能有多个构造方法，具体选哪一个) ---> 通过反射 实例化一个对象（此时并不是bean,因为bean此时还不在spring容器当中）
         * 6、合并beanDefinition
         * 7、提前暴露一个bean工厂对象  #######（重中之重）
         * 8、判断是否需要完成属性注入？填充属性----自动注入
         * 9、执行部分Aware接口（Aware接口的实现有很多）
         * 10、执行部分Aware接口 和 执行spring生命周期初始化回调方法（bean创建实例完成后立马调用）
         *      ---anno注解版(生命周期初始化回调方法有三种方式：1、自定义方法，然后方法上加@PostConstruct注解 ，@PostConstruct注解的方法在项目启动的时候执行这个方法，也可以理解为在spring容器启动的时候执行，可作为一些数据的常规化加载
         *                                                           2、实现InitializingBean接口     3、通过xml配置    这三种可以同时存在一个bean当中，因为三种执行时机是不一样的
         * 11、接口版的生命周期回调方法
         * 12、BeanPostProcessors的前置方法---aop
         *      ---postProcessBeforeInitialization:在bean创建实例之后，bean任何初始化方法调用之前一些拦截处理工作
         * 13、put map单例池
         * 14、销毁
         *
         */

        /**
         * 总结：spring的循环依赖，不支持原型（prototype），不支持构造方法注入的bean；
         * 默认情况下单例bean是支持循环依赖的，但是也支持关闭，关闭的原理就是设置allowCircularReferences=false；
         * spring提供了api来设置这个值；
         */

    }
}
