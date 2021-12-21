package com.zhan.services;

import org.springframework.stereotype.Component;

/**
 * 特殊的对象---bean
 *
 * bean和普通Java对象有什么区别？
 * 普通的Java对象并不一定是bean，但是一个bean一定是Java对象
 *
 * @author zhanzhan
 * @since 2020/3/27 18:35
 */
//@Component //此时Z没有交给spring管理，容器中是拿不到的
public class Z {
    public Z() {
        System.out.println("create Z");
    }
}
