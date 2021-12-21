package com.zhan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanzhan
 * @since 2020/3/27 18:35
 */
@Component
public class Y {
    @Autowired
    private X x;

    public Y() {
        System.out.println("调用Y构造方法，create Y，Y实例化了");
    }

    // public void setY(X x) {
    //     this.x = x;
    // }
}
