/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import java.util.function.Supplier;

import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Standalone application context, accepting <em>component classes</em> as input &mdash;
 * in particular {@link Configuration @Configuration}-annotated classes, but also plain
 * {@link org.springframework.stereotype.Component @Component} types and JSR-330 compliant
 * classes using {@code javax.inject} annotations.
 *
 * <p>Allows for registering classes one by one using {@link #register(Class...)}
 * as well as for classpath scanning using {@link #scan(String...)}.
 *
 * <p>In case of multiple {@code @Configuration} classes, {@link Bean @Bean} methods
 * defined in later classes will override those defined in earlier classes. This can
 * be leveraged to deliberately override certain bean definitions via an extra
 * {@code @Configuration} class.
 *
 * <p>See {@link Configuration @Configuration}'s javadoc for usage examples.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 * @see #register
 * @see #scan
 * @see AnnotatedBeanDefinitionReader
 * @see ClassPathBeanDefinitionScanner
 * @see org.springframework.context.support.GenericXmlApplicationContext
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    /**
     * 这个类顾名思义是一个reader，一个bd读取器
     * 读取什么呢？还是顾名思义AnnotatedBeanDefinition意思是读取一个被加了注解的bean，帮你转换成bd
     * 这个类在构造方法中实例化的
     */
	private final AnnotatedBeanDefinitionReader reader;

    /**
     * 同意顾名思义，这是一个扫描器，扫描一个类，包，并且帮你转换成bd
     * 同样是在构造方法中被实例化的
     */
	private final ClassPathBeanDefinitionScanner scanner;


	/**
     * 调用默认的构造方法时会先执行父类（GenericApplicationContext）的构造方法
     *
	 * Create a new AnnotationConfigApplicationContext that needs to be populated
	 * through {@link #register} calls and then manually {@linkplain #refresh refreshed}.
	 */
	public AnnotationConfigApplicationContext() {
        /**
         * 先调父类的构造方法：实例化一个BeanFactory工厂：DefaultListableBeanFactory
         *
         * 创建一个Bean读取器，用来读取加了注解的Bean定义，
         * Bean读取器的构造方法中同时向容器(BeanDefinitionMap)中注册了 5个spring自带的后置处理器(包括BeanPostProcessor和BeanFactoryPostProcessor)
         * 这里的注册指的是将这 5 个类对应的BeanDefinition放入到BeanDefinitionMap中。其中如果存在JPA，会注册JPA的后置处理器，则是共注册 6 个（其中1个BeanFactoryPostProcessor，5个 BeanPostProcessor）
         *
         * 什么是bean定义（或bean的描述）？即BeanDefinition。BeanDefinition对象来描述spring中bean的信息，类似于Class对象来描述java类的信息
         *
         */
	    // 读取加了注解的bean
        // this.registerBeanDefinition();
		this.reader = new AnnotatedBeanDefinitionReader(this);
		// 会创建一个扫描器，可以用来扫描包或者类，继而转换成bd
        // 但是实际上我们扫描包工作并没有用到这个scanner扫描器，在refresh()中使用的是spring自己重新new的一个扫描器ClassPathBeanDefinitionScanner。
        // 这里的scanner仅仅是为了程序员能够在外部调用AnnotationConfigApplicationContext对象的scan方法完成一些其他的扫描
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new AnnotationConfigApplicationContext with the given DefaultListableBeanFactory.
	 * @param beanFactory the DefaultListableBeanFactory instance to use for this context
	 */
	public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
		super(beanFactory);
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
     * 这个构造方法需要传入一个被java config注解了的配置类
     * 然后会把这个被注解了java config的类通过注解读取器读取后继而解析
	 * Create a new AnnotationConfigApplicationContext, deriving bean definitions
	 * from the given component classes and automatically refreshing the context.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 */
	public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
	    //参数componentClasses 这里代表被加了注解的AppConfig.class配置类
	    //调用构造方法 ②
        /**
         * 等同于
         * AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
         */

        // 是否完成了X Y的扫描？看beanDefinitionMap中是否有X , Y?有，则完成了扫描，否则没有

        // 这里有没有完成定义的包扫描？没有
        // 这里由于它有父类，故而会先调用父类的构造方法，然后才会调用自己的构造方法
        // 在自己构造方法中初始一个读取器和扫描器
	    this();//调用默认的无参的构造方法（java语法是先调用父类的无参构造方法）

        // 注册配置类 ③
        // 等同于 ac.register(AppConfig.class) 这里为什么要注册配置类到bDMap中？因为配置类中可能有@Bean注解的方法，方法的调用需要bean实例化
        // 这里有没有完成定义的包扫描？没有
		register(componentClasses);//最终将bean的描述信息(bd) 和 beanName读取到DefaultListableBeanFactory工厂的bdmap当中
                                   //register方法主要用来读取加了注解的bean，并把它转换成bean的定义(bd)，注册到工厂的bdmap中。里面是通过reader.register(componentClasses)来完成的

		// 初始化spring容器 ④
        // 等同于 ac.refresh()
        // 这一步有没有完成扫描？有
		refresh();
	}

	/**
	 * Create a new AnnotationConfigApplicationContext, scanning for components
	 * in the given packages, registering bean definitions for those components,
	 * and automatically refreshing the context.
	 * @param basePackages the packages to scan for component classes
	 */
	public AnnotationConfigApplicationContext(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}


	/**
	 * Propagate the given custom {@code Environment} to the underlying
	 * {@link AnnotatedBeanDefinitionReader} and {@link ClassPathBeanDefinitionScanner}.
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Provide a custom {@link BeanNameGenerator} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}, if any.
	 * <p>Default is {@link AnnotationBeanNameGenerator}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 * @see AnnotationBeanNameGenerator
	 * @see FullyQualifiedAnnotationBeanNameGenerator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.reader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
		getBeanFactory().registerSingleton(
				AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
	}

	/**
	 * Set the {@link ScopeMetadataResolver} to use for registered component classes.
	 * <p>The default is an {@link AnnotationScopeMetadataResolver}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 */
	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.reader.setScopeMetadataResolver(scopeMetadataResolver);
		this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
	}


	//---------------------------------------------------------------------
	// Implementation of AnnotationConfigRegistry
	//---------------------------------------------------------------------

    /**
     * 注册单个或多个bean给容器
     * 比如有新加的类可以用这个方法进行注册
     * 但是注册之后需要手动调用refresh()方法去触发容器解析注解
     *
     * 这个注册有两个意思:
     * 他可以注册一个配置类 ，如 AppConfig.class
     * 他还可以单独注册一个bean，如 X.class
     *
	 * Register one or more component classes to be processed.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 * @see #scan(String...)
	 * @see #refresh()
	 */
	@Override
	public void register(Class<?>... componentClasses) {
		Assert.notEmpty(componentClasses, "At least one component class must be specified");
        //bd读取器读取加了注解的bean,通过构造方法的方式把它转换成bd 或者说 解析成bd(实际类型为AnnotatedGenericBeanDefinition),
        //然后注册到工厂的容器(BeanDefinitionMap)当中,
        //这样后面在ConfigurationClassPostProcessor中能解析componentClasses，例如demo中的AppConfig类，
        //只有解析了AppConfig类，才能知道Spring要扫描哪些包(因为在AppConfig类中添加了@ComponentScan注解)，
        //只有知道要扫描哪些包了，才能扫描出需要交给Spring管理的bean有哪些，这样才能利用Spring来创建bean。
		this.reader.register(componentClasses);
	}

	/**
	 * Perform a scan within the specified base packages.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param basePackages the packages to scan for component classes
	 * @see #register(Class...)
	 * @see #refresh()
	 */
	@Override
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		this.scanner.scan(basePackages);
	}


	//---------------------------------------------------------------------
	// Adapt superclass registerBean calls to AnnotatedBeanDefinitionReader
	//---------------------------------------------------------------------

	@Override
	public <T> void registerBean(@Nullable String beanName, Class<T> beanClass,
			@Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {

		this.reader.registerBean(beanClass, beanName, supplier, customizers);
	}

}
