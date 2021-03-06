package com.yanan.framework;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.yanan.framework.fieldhandler.AutoInject;
import com.yanan.framework.fieldhandler.Singleton;
import com.yanan.util.CacheHashMap;
import com.yanan.util.DexUtils;
import com.yanan.util.ExtReflectUtils;
import com.yanan.util.ReflectUtils;
import com.yanan.util.TypeToken;
import com.yanan.util.asserts.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin基础框架，用于提供各类注入服务的逻辑功能，本省无任何注入或处理实现
 * 具体功能完全依赖于各自注入实现
 */
public class Plugin {
    //属性处理集合
    private static final Map<Class<? extends Annotation>,FieldHandler<? extends Annotation>> handlerMap = new HashMap<>();
    //类处理集合
    private static final Map<Class<? extends Annotation>,ClassHandler<? extends Annotation>> classHandlerMap = new HashMap<>();
    //实例缓存
    private static final CacheHashMap<Class<?>,Object> instanceMap = new CacheHashMap<>(new TypeToken<WeakReference<Object>>(){}.getTypeClass());
    //方法处理集合
    private static final Map<Class<? extends Annotation>,MethodHandler<? extends Annotation>> methodHandlerMap = new HashMap<>();
    //上下文
    private static final ThreadLocal<Context> currentActivity = new ThreadLocal<>();

    static final String TAG = "PLUGIN";
    static {
        try {
            Log.i(TAG,"   PPPPPPPP      LL      UU     UU       GGGGG      IIIIII      NNN     NN ");
            Log.i(TAG,"   PP    PP     LL      UU     UU      GG             II       NN NN  NN   ");
            Log.i(TAG,"  PPPPPPP      LL      UU     UU      GG   GGG        II      NN  NN NN    ");
            Log.i(TAG," PP           LL      UU     UU       GG     GG      II      NN   NNN      ");
            Log.i(TAG,"PP           LLLLLL    UUUUUU          GGGGGG GG   IIIIII   NN    NN       ");
            loadPlugins("com.yanan");
        } catch (ClassNotFoundException e) {
            Log.e(TAG,"failed to init Plugin",e);
        } catch (IOException e) {
            Log.e(TAG,"failed to init Plugin",e);
        } catch (NoSuchFieldException e) {
            Log.e(TAG,"failed to init Plugin",e);
        } catch (IllegalAccessException e) {
            Log.e(TAG,"failed to init Plugin",e);
        }
    }
    public static void loadPlugins(String pack) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException {
        Log.d(TAG,"loaded plugins services:"+DexUtils.getClasses(pack).toString());
    }
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<?> clazz){
        return (T)instanceMap.get(clazz);
    }
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> clazz,boolean injected,Object... args){
        Object instance = instanceMap.get(clazz);
        if(instance != null)
            return (T) instance;
        try {
            Constructor<?> constructor = ExtReflectUtils.getEffectiveConstructor(clazz,args);
            instance = constructor.newInstance(args);
            Singleton singleton = clazz.getAnnotation(Singleton.class);
            if(singleton == null || singleton.value())
                Plugin.setInstance(clazz,instance);
            if(clazz.getAnnotation(AutoInject.class) != null || injected){
                Plugin.inject((Activity) currentActivity.get(),instance);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) instance;
    }
    public static void setInstance(Class<?> fieldType, Object instance) {
        instanceMap.puts(fieldType,instance);
    }
    public static <T extends Annotation> void register(Class<T> type, FieldHandler<T> handler){
        handlerMap.put(type,handler);
    }
    public static <T extends Annotation> void register(Class<T> type, ClassHandler<T> handler){
        classHandlerMap.put(type,handler);
    }
    public static <T extends Annotation> void register(Class<T> type, MethodHandler<T> handler){
        methodHandlerMap.put(type,handler);
    }

    public static void inject(Activity context){
        inject(context,context);
    }
    public static void inject(Object instance) {
        inject((Activity) currentActivity.get(),instance);
    }
    public static void inject(Fragment fragment) {
        inject(fragment.getActivity(),fragment);
    }
    public static void inject(Activity context,Object instance){
        Assert.isNotNull(context);
        Assert.isNotNull(instance);
        currentActivity.set(context);
        injectClass(context,instance);
        Field[] fields = ReflectUtils.getAllFields(instance.getClass());
        Log.d(TAG,Arrays.toString(fields));
        for(Field field : fields){
            injectField(context,instance,field);
        }
        injectMethod(context,instance);
    }

    public static void injectMethod(Activity context, Object instance) {
        Class<?> classes = instance.getClass();
        Method[] methods =ReflectUtils.getAllMethods(classes);
        for(Method method : methods){
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation : annotations){
                Class<? extends Annotation> annoType = annotation.annotationType();
                MethodHandler handler = methodHandlerMap.get(annoType);
                if(handler != null){
                    Log.d(TAG,"Annotations "+annoType.getSimpleName()+" use handler "+handler.getClass());
                    handler.process(context,instance,method,annotation);
                }
            }
        }
    }

    public static void injectClass(Activity context, Object instance) {
        Class<?> classes = instance.getClass();
        setInstance(classes,instance);
        Annotation[] annotations = order(classes.getAnnotations());
        if(annotations != null){
            for(Annotation annotation : annotations){
                Class<? extends Annotation> annoType = annotation.annotationType();
                ClassHandler handler = classHandlerMap.get(annoType);
                if(handler != null){
                    Log.d(TAG,"Annotations "+annoType.getSimpleName()+" use handler "+handler.getClass());
                    handler.process(context,instance,annotation);
                }
            }
        }
    }
    public static void injectField(Activity context,Object instance,Field field){
        Annotation[] annotations = order(field.getAnnotations());
        Log.d(TAG,field+"==>"+Arrays.toString(annotations));
        if(annotations == null || annotations.length == 0)
            return;
        for(Annotation annotation : annotations){
            Class<? extends Annotation> annoType = annotation.annotationType();
            FieldHandler handler = handlerMap.get(annoType);
            if(handler != null){
                Log.d(TAG,"Annotations "+annoType.getSimpleName()+" use handler "+handler.getClass());
                handler.process(context,instance,field,annotation);
            }
        }
    }
    public static Annotation[] order(Annotation[] annotations){
        if(annotations == null || annotations.length < 2 )
            return annotations;
//        Annotation[] temp = new Annotation[annotations.length];
        for(int i = 0;i<annotations.length;i++){
            Annotation annotation = annotations[i];
            Class<? extends Annotation> annoType = annotation.annotationType();
            After after = annoType.getAnnotation(After.class);
            if(after != null){
                int found = -1; ;
                if((found = found(annotations,after.value(),i+1)) != -1){
                    order(annotations,i,found);
                }
            }
        }
        return annotations;
    }
    // a e c d b
    // a b e c d
    private static void order(Annotation[] annotations, int start, int end) {
        if(end>start){
            Annotation temp = annotations[start];
            annotations[start] = annotations[end];
            int index = start+2;
            while(index < end){
                annotations[index] = annotations[index-1];
                index ++;
            }
            annotations[start+1] = temp;
        }
    }
    private static int found(Annotation[] annotations, Class<? extends Annotation> value, int start) {
        for(int i = start;i<annotations.length;i++){
            if(annotations[i].annotationType().equals(value))
                return i;
        }
        return -1;
    }

    public static Context currentContext() {
        return currentActivity.get();
    }
}
