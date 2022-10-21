package com.SEG2505_Group8.mealer.Database.Serialize;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class with static methods for serializing objects with {@link MealerSerializable} annotation.
 */
public class MealerSerializer {

    /**
     * Create a map where key/values are extracted using {@link MealerSerializableElement}
     * IllegalArgumentException is thrown if object does not have @MealerSerializable annotation
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
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

    /**
     * Returns true if object has @MealerSerializable annotation
     * @param o
     * @return
     */
    public static boolean isSerializable(Object o) {
        if (Objects.isNull(o)) {
            return false;
        }

        Class<?> clazz = o.getClass();
        return clazz.isAnnotationPresent(MealerSerializable.class);
    }
}
