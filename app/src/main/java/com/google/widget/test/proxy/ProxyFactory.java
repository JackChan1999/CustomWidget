package com.google.widget.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/19 19:54
 * 描 述 ：
 * 修订历史 ：
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
