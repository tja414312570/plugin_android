package com.yanan.framework.dto.proxy;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.event.BindEvent;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoProxy implements InvocationHandler {
    private final Method setListenerMethod;
    private Activity activity;
    private Method listenerMethod;
    private Method bindMethod;
    private BindEvent bindEvent;
    private Object instance;
    private Object view;
    private Class<?> listenerClass[];

    public Activity getActivity() {
        return activity;
    }

    public Method getListenerMethod() {
        return listenerMethod;
    }

    public DtoProxy(Activity activity, Object instance, Object view, Method setListenerMethod, Method bindMethod, BindEvent bindEvent) {
        this.activity = activity;
        this.setListenerMethod = setListenerMethod;
        this.listenerClass = this.setListenerMethod.getParameterTypes();
        this.bindMethod = bindMethod;
        this.listenerMethod = this.listenerClass[0].getMethods()[0];
        this.bindEvent = bindEvent;
        this.instance = instance;
        this.view = view;
    }

    public Method getBindMethod() {
        return bindMethod;
    }

    public BindEvent getBindEvent() {
        return bindEvent;
    }

    public Object getInstance() {
        return instance;
    }

    public View getView() {
        return (View) view;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("BIND_EVENT_PROXY",method+ Arrays.toString(args));
        //获取SqlSession
        //获取类名和方法并组装为sqlId
//        String clzz = methodHandler.getPlugsProxy().getInterfaceClass().getName();
//        String method = methodHandler.getMethod().getName();
//        String sqlId=clzz+"."+method;
//        Object parameter = null;
//        //此部分代码用于判断是否接口参数中使用了@Param注解
//        parameter = decodeParamerters(methodHandler);
//        //从映射中获取sqlId对应的映射，并通过映射获取SQL的类型，对应增删查改
//        BaseMapping mapping = sqlSession.getContext().getWrapper(sqlId);
//        if(mapping==null)
//            throw new SqlExecuteException("could not found sql mapper id \""+method+"\" at namespace \""+clzz+"\"");
//        if(mapping.getNode().trim().toLowerCase().equals("select")){
//            if(ReflectUtils.implementsOf(methodHandler.getMethod().getReturnType(), List.class)){
//                methodHandler.interrupt(sqlSession.selectList(sqlId, parameter));
//            }else{
//                methodHandler.interrupt(sqlSession.selectOne(sqlId, parameter));
//            }
//        }else if(mapping.getNode().trim().toLowerCase().equals("insert")){
//            methodHandler.interrupt(sqlSession.insert(sqlId, parameter));
//        }else if(mapping.getNode().trim().toLowerCase().equals("updaete")){
//            methodHandler.interrupt(sqlSession.update(sqlId, parameter));
//        }else if(mapping.getNode().trim().toLowerCase().equals("delete")){
//            methodHandler.interrupt(sqlSession.delete(sqlId, parameter));
//        }
        return method.invoke(this);
    }
//    /**
//     * 查询参数是否有注解，有Param注解则将参数组装成Map返回。
//     * @param methodHandler method handler
//     * @return instance
//     */
//    public Object decodeParamerters(MethodHandler methodHandler) {
//        Map<String,Object> parameter = new HashMap<>();
//        ClassHelper classHelper = ClassHelper.getClassHelper(methodHandler.getPlugsProxy().getInterfaceClass());
//        MethodHelper methodHelper = classHelper.getMethodHelper(methodHandler.getMethod());
//        Parameter[] actParameters = methodHelper.getParameters();
//        for(int i = 0;i<actParameters.length ; i++) {
//            ParameterHelper parameterHelper = methodHelper.getParmeterHelper(actParameters[i]);
//            Param param = parameterHelper.getAnnotation(Param.class);
//            if(param != null) {
//                parameter.put(param.value(), methodHandler.getParameters()[i]);
//            }else {
//                parameter.put(parameterHelper.getParameter().getName(), methodHandler.getParameters()[i]);
//            }
//        }
//        return parameter;
//    }
}
