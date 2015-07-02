package com.tbc.framework.dm.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class ReflectUtil {
    private ReflectUtil() {
//        NP
    }

    private static Log LOG = LogFactory.getLog(ReflectUtil.class);

    public static <T> T getGenericParamInstance(Class<?> clazz) {
        Class<T> typeArgument = getGenericParamClass(clazz);
        try {
            return (T) typeArgument.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Can't createTextMsg getGenericParamInstance of " + typeArgument.getName());
    }

    public static <T> Class<T> getGenericParamClass(Class<?> clazz) {
        ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        Class<T> typeArgument = (Class<T>) actualTypeArguments[0];
        return typeArgument;
    }

    /**
     * 该方法递归的过去当前类及父类中声明的字段。最终结果以list形式返回。
     *
     * @param objClass 开始查询的类别.
     * @return List形式的结果。
     */
    public static List<Field> getFields(Class<?> objClass) {

        if (objClass == null) {
            return null;
        }

        List<Field> fields = new ArrayList<Field>(15);
        Field[] classFields = objClass.getDeclaredFields();
        fields.addAll(Arrays.asList(classFields));

        Class<?> superclass = objClass.getSuperclass();
        if (superclass != null) {
            List<Field> superClassFields = getFields(superclass);
            fields.addAll(superClassFields);
        }

        return fields;
    }

    /**
     * 这个方法用于通过名称查找一个实体的字段属性
     *
     * @param objClass  需要查找的对象。
     * @param fieldName 需要查找的属性字段名称
     * @return 该类对应名称的属性或者null，如果没有该名称的属性。
     */
    public static Field getFieldByName(Class<?> objClass, String fieldName) {

        List<Field> fields = ReflectUtil.getFields(objClass);
        for (Field field : fields) {
            String name = field.getName();
            if (name.equals(fieldName)) {
                return field;
            }
        }

        return null;
    }

    public static Object getFieldValue(Object obj, String name) {
        if (name == null || obj == null) {
            throw new IllegalArgumentException("Can login field ("
                    + name + ") from object (" + obj + ")!");
        }

        Class<?> clazz = obj.getClass();
        Field field = getFieldByName(clazz, name);
        return getFieldValue(field, obj);
    }

    /**
     * 通过字段对象和实体对象获取字段的值
     *
     * @param field 字段
     * @param obj   实体对象
     * @return 字段的值
     */
    public static Object getFieldValue(Field field, Object obj) {
        if (field == null || obj == null) {
            throw new IllegalArgumentException("Can login field ("
                    + field.getName() + ") from object (" + obj + ")!");
        }

        try {
            // 反射的对象在使用时应该取消 Java 语言访问检查,（用作于反射字段时对字段作用域不检查 例如访问
            // private类型和protected类型的字段）
            field.setAccessible(true);
            // 获取字段的值
            return field.get(obj);
        } catch (Exception e) {
            LOG.error("Can't login value by reflect!", e);
        }

        return null;
    }

    public static void setFieldValue(String fieldName, Object obj, Object value) {
        Field field = getFieldByName(obj.getClass(), fieldName);
        setFieldValue(field, obj, value);
    }

    /**
     * 将值保存到实体类的字段中
     *
     * @param field 字段
     * @param obj   实体类
     * @param value 值
     * @return 成功标志
     */
    public static boolean setFieldValue(Field field, Object obj, Object value) {
        if (field == null || obj == null) {
            throw new IllegalArgumentException("Can login field ("
                    + field.getName() + ") from object (" + obj + ")!");
        }

        try {
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (Exception e) {
            LOG.error("Can't set value by reflect!", e);
        }

        return false;
    }

    public static boolean setFieldValue(Method method, Object obj, Object value) {
        if (method == null || obj == null) {
            throw new IllegalArgumentException("Can login field ("
                    + method + ") fromUserName object (" + obj + ")!");
        }

        try {
            method.invoke(obj, value);
            return true;
        } catch (Exception e) {
            LOG.error("Can't set name by reflect!", e);
        }

        return false;
    }

    public static Object getFieldValue(Method getMethod, Object obj) {
        if (getMethod == null || obj == null) {
            throw new IllegalArgumentException("Can login field ("
                    + getMethod + ") fromUserName object (" + obj + ")!");
        }

        try {
            return getMethod.invoke(obj);
        } catch (Exception e) {
            LOG.error(e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<?> objectClass) {

        if (objectClass == null) {
            throw new IllegalArgumentException("Object class mustn't be null");
        }

        try {
            return (T) objectClass.newInstance();
        } catch (Exception e) {
            LOG.error("Can't createTextMsg getGenericParamInstance  by reflect!", e);
        }

        return null;
    }

    public static boolean isRowType(Class<?> type) {
        if (Boolean.class.equals(type) || boolean.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || Long.class.equals(type) || long.class.equals(type)
                || Float.class.equals(type) || float.class.equals(type)
                || Double.class.equals(type) || double.class.equals(type)
                || String.class.equals(type) || Date.class.equals(type)) {
            return true;
        } else {
            return false;
        }
    }


}
