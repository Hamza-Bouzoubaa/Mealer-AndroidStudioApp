package com.SEG2505_Group8.mealer.Database.Serialize;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MealerSerializer {
    public static Map<String, Object> toMap(Object o) throws IllegalArgumentException {
        if (isSerializable(o)) {
            try {
                Class<?> clazz = o.getClass();
                Map<String, Object> map = new HashMap<>();

                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(MealerSerializableElement.class)) {
                        map.put(field.getAnnotation(MealerSerializableElement.class).key(), field.get(o));
                    }
                }

                return map;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Object is not MealerSerializable");
        }

        return null;
    }

    public static boolean isSerializable(Object o) {
        if (Objects.isNull(o)) {
            return false;
        }

        Class<?> clazz = o.getClass();
        return clazz.isAnnotationPresent(MealerSerializable.class);
    }
}
