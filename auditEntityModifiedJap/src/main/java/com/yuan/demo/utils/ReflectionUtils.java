package com.yuan.demo.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yuan.demo.custom.ColumnConf;
import com.yuan.demo.entity.Comment;
import com.yuan.demo.vo.CommentVo;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 

// Ref:https://blog.csdn.net/qq_26834611/article/details/117569456
// java通过反射获取类名、属性名称以及@注解信息

public class ReflectionUtils {
	
    public static final String TABLE_NAME = "tableName";
    public static final String SCHEMA_NAME = "schemaName";
    public static final String CLASS_NAME = "className";
    public static final String FIELD_NAME = "fieldNames";
    public static final String COLUMN_NAME = "column";
 
 
    /**
     * * 获取属性名数组
     *
     * @param clazz
     * @return
     */
    public static String[] getPropertiesName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNameStrings = new String[fields.length];
        for (int i = 0; i < fieldNameStrings.length; i++) {
            fieldNameStrings[i] = fields[i].getName();
        }
 
        return fieldNameStrings;
    }
 
    /**
     * 通过反射设置属性的值
     *
     * @param fieldName      属性名
     * @param fieldValue     属性值
     * @param object         实体类对象
     * @param parameterTypes 设置属性值的类型  String.class
     * @throws
     */
    public static void setPropertyValue(String fieldName, Object fieldValue, Object object, Class<?>... parameterTypes) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //字段名称
                String name = field.getName();
                if (name.equals(fieldName)) {
                    field.setAccessible(true);
                    //field.set(object,fieldValue); //可代替下面的拼接set方法;
                    //将属性的首字符大写，方便构造get，set方法
                    String methname = name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method m = object.getClass().getMethod("set" + methname, parameterTypes);
                    m.invoke(object, fieldValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 根据主键名称获取实体类主键属性值
     *
     * @param clazz     实体类对象
     * @param fieldName 属性名
     * @return
     */
    public static Object getPropertyValue(Object clazz, String fieldName) {
        try {
            /** 方法一
             Field field = clazz.getClass().getDeclaredField(fieldName);
             //设置对象的访问权限，保证对private的属性的访问
             field.setAccessible(true);
             Object value = field.get(clazz);
             */
 
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = clazz.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(clazz, new Object[]{});
 
            return value;
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * 通过反射将 source不为空的值赋值给target
     *
     * @param source 实体类对象
     * @param target 实体类对象
     * @throws Exception
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        Field[] field = source.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m1 = source.getClass().getMethod("get" + name);
            Object value = m1.invoke(source);
            if (value != null) {
                Field f = field[i];
                //设置对象的访问权限，保证对private的属性的访问
                f.setAccessible(true);
                f.set(target, value);
            }
        }
    }
 
    /**
     * 获取实体类主键
     *
     * @param clazz
     * @return
     */
    public static Field getJpaIdField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field item = null;
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                field.setAccessible(true);
                item = field;
                break;
            }
        }
        if (item == null) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                item = getJpaIdField(superclass);
            }
        }
 
        return item;
    }
 
    /**
     * 获取实体类 @Column 的其中一个属性名称
     *
     * @param clazz
     * @return
     */
    public static List<String> getJpaColumns(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column declaredAnnotation = field.getDeclaredAnnotation(Column.class);
                String column = declaredAnnotation.name();
                list.add(column);
            }
        }
 
        return list;
    }
 
    /**
     * 通过获取类上的@Table注解获取表名称
     *
     * @param clazz
     * @return
     */
    public static Map<String, String> getJpaTableName(Class<?> clazz) {
        Map<String, String> map = new ConcurrentHashMap<>();
        Table annotation = clazz.getAnnotation(Table.class);
        String name = annotation.name();
        String schema = annotation.schema();
        String className = clazz.getSimpleName();
        map.put(TABLE_NAME, name);
        map.put(SCHEMA_NAME, schema);
        map.put(CLASS_NAME, className);
 
        return map;
    }
 
    /**
     * 获取VO @ColumnConf注解 属性与中文名称
     *
     * @param clazz
     * @return
     */
    public static List<String> getVoColumns(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ColumnConf cc = field.getAnnotation(ColumnConf.class);
            if (cc == null) {
                continue;
            }
            list.add(cc.value());
        }
        return list;
    }
 
    public static void main(String[] args) throws Exception {
    	
    	Comment comment = new Comment();
    	comment.setArticleId(2);
		comment.setAuthor("Martin2");
		comment.setContent("Smith2");
		comment.setInsertedDt(new Date());
		
		Map<String, String> jpaTableNameMap = getJpaTableName(Comment.class);
		System.out.println("jpaTableName=>" + jpaTableNameMap.get(SCHEMA_NAME) + "." + jpaTableNameMap.get(TABLE_NAME));
		System.out.println("=================================1");
		
		String[] propertiesNames= getPropertiesName(Comment.class);
		Arrays.stream(propertiesNames).forEach(System.out::println);
		System.out.println("=================================2");
		
		Comment target = new Comment();
		copyProperties(comment, target);
		System.out.println("vo=>" + target);
		System.out.println("=================================3");
		
		Field jpaIdField = getJpaIdField(Comment.class);
		System.out.println("id=>" + jpaIdField.getName());
		System.out.println("=================================4");
		
		List<String> jpaColumnsList = getJpaColumns(Comment.class);
		jpaColumnsList.stream().forEach(System.out::println);
		System.out.println("=================================5");
    
		List<String> voColumnsList = getVoColumns(CommentVo.class);
		voColumnsList.stream().forEach(System.out::println);
		System.out.println("=================================6");
    }
}