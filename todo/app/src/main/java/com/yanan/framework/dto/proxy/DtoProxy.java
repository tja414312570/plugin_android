package com.yanan.framework.dto.proxy;

import android.app.Activity;
import android.util.Log;

import com.yanan.framework.dto.SqlExecuteException;
import com.yanan.framework.dto.SqlFragmentManager;
import com.yanan.framework.dto.SqlSession;
import com.yanan.framework.dto.annotations.Param;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoProxy implements InvocationHandler {
    private final Class<?> interfaceClass;
    private Activity activity;
    private SqlFragmentManager sqlFragmentManager;
    private SqlSession sqlSession;
    public Activity getActivity() {
        return activity;
    }

    public DtoProxy(Activity activity, Class<?> interfaceClass,SqlFragmentManager sqlFragmentManager,SqlSession sqlSession) {
        this.activity = activity;
        this.interfaceClass = interfaceClass;
        this.sqlFragmentManager = sqlFragmentManager;
        this.sqlSession = sqlSession;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("toString"))
            return this.toString();
        Log.d("BIND_DTO_PROXY",method+ Arrays.toString(args));
        //获取SqlSession
        //获取类名和方法并组装为sqlId
        String className = interfaceClass.getName();
        String methodName = method.getName();
        String sqlId=className+"."+methodName;
        Object parameter = null;
        //此部分代码用于判断是否接口参数中使用了@Param注解
        parameter = decodeParameters(method,args);
        //从映射中获取sqlId对应的映射，并通过映射获取SQL的类型，对应增删查改
        BaseMapping mapping = sqlFragmentManager.getWrapper(sqlId);
        if(mapping==null)
            throw new SqlExecuteException("could not found sql mapper id \""+methodName+"\" at namespace \""+className+"\"");
        if(mapping.getNode().trim().toLowerCase().equals("select")){
            if(ReflectUtils.implementsOf(method.getReturnType(), List.class)){
                return sqlSession.selectList(sqlId, parameter);
            }else{
               return sqlSession.selectOne(sqlId, parameter);
            }
        }else if(mapping.getNode().trim().toLowerCase().equals("insert")){
            return sqlSession.insert(sqlId, parameter);
        }else if(mapping.getNode().trim().toLowerCase().equals("updaete")){
            return sqlSession.update(sqlId, parameter);
        }else if(mapping.getNode().trim().toLowerCase().equals("delete")){
            return sqlSession.delete(sqlId, parameter);
        }
        return method.invoke(this);
    }
    /**
     * 查询参数是否有注解，有Param注解则将参数组装成Map返回。
     * @param method method handler
     * @param args method param
     * @return instance
     */
    public Object decodeParameters(Method method, Object[] args) {
        Map<String,Object> parameter = new HashMap<>();
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if(paramTypes.length == 1 && getAnnotation(Param.class,parameterAnnotations[0]) == null)
            return args[0];
        for(int i = 0;i<paramTypes.length;i++){
            Annotation[] annotations = parameterAnnotations[i];
            Param param = getAnnotation(Param.class,annotations);
            String paramName;
            if(param == null){
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    paramName = "arg"+i;
                }else{
                    paramName = method.getParameters()[i].getName();
                }
            }else{
                paramName = param.value();
            }
            parameter.put(paramName, args[i]);
        }
        return parameter;
    }

    private <T extends Annotation> T getAnnotation(Class<T> paramClass, Annotation[] annotations) {
        for(Annotation annotation : annotations){
            if(annotation.annotationType().equals(paramClass))
                return (T) annotation;
        }
        return null;
    }
}
