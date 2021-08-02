package com.yanan.util.xml;

import com.yanan.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryMapping {
    static Map<Class,EntryMapping> entryMappingMap = new HashMap<>();
    public static EntryMapping getEntryMapping(Class mappingClass){
        EntryMapping entryMapping =  entryMappingMap.get(mappingClass);
        if(entryMapping == null){
            entryMapping = new EntryMapping(mappingClass);
            entryMappingMap.put(mappingClass,entryMapping);
        }
        return entryMapping;
    }
    private Class mappingClass;
    private Map<Class<? extends Annotation>, List> entryMap = new HashMap<>();
    public EntryMapping(Class mappingClass){
        this.mappingClass = mappingClass;
        Field[] fields = ReflectUtils.getAllFields(mappingClass);
        for(Field field: fields){
            Annotation[] annotations = field.getAnnotations();
            for(Annotation annotation : annotations){
                Class<? extends Annotation> annotationsType = annotation.annotationType();
                List<Entry<? extends Annotation, Field>> annotationFieldMap = entryMap.get(annotationsType);
                if(annotationFieldMap == null){
                    annotationFieldMap = new ArrayList<>();
                    entryMap.put(annotationsType,annotationFieldMap);
                }
                annotationFieldMap.add(new Entry<>(annotation,field));
            }
        }
    }
    public Class getMappingClass(){
        return mappingClass;
    }
    public <T extends Annotation> List<Entry<T, Field>> getAnnotationsField(Class<T> annotationsType){
        List<Entry<T, Field>> list =  (List<Entry<T, Field>>)entryMap.get(annotationsType);
        if(list == null)
            list = Collections.emptyList();
        return list;
    }
    @Override
    public String toString() {
        return "EntryMapping{" +
                "mappingClass=" + mappingClass +
                ", entryMap=" + entryMap +
                '}';
    }
}
