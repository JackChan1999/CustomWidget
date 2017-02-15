package com.google.widget.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：Widgets
 * Package_Name：com.google.widget
 * Version：1.0
 * time：2016/2/15 14:09
 * des ：
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class ProxyFactory {
    private Object target;//目标对象
    private BeforeAdvice mBeforeAdvice;//前置增强
    private AfterAdvice mAfterAdvice;//后置增强

    /**生成代理对象*/
    public Object createProxy(){

        ClassLoader loader = this.getClass().getClassLoader();
        Class[] interaces = target.getClass().getInterfaces();

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //执行前置增强
                if (mBeforeAdvice != null){
                    mBeforeAdvice.before();
                }

                //执行目标对象的目标方法
                Object result = method.invoke(target, args);

                //执行后置增强
                if (mAfterAdvice != null){
                    mAfterAdvice.after();
                }
                return result;//返回目标对象的返回值
            }
        };

        //得到代理对象
        Object proxy = Proxy.newProxyInstance(loader, interaces, handler);
        return proxy;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setBeforeAdvice(BeforeAdvice beforeAdvice) {
        mBeforeAdvice = beforeAdvice;
    }

    public void setAfterAdvice(AfterAdvice afterAdvice) {
        mAfterAdvice = afterAdvice;
    }

    public Object getTarget() {
        return target;
    }

    public BeforeAdvice getBeforeAdvice() {
        return mBeforeAdvice;
    }

    public AfterAdvice getAfterAdvice() {
        return mAfterAdvice;
    }
}
