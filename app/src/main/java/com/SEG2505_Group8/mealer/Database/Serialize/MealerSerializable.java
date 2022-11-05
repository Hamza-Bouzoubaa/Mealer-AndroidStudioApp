package com.SEG2505_Group8.mealer.Database.Serialize;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for classes that can be serialized into database
 */

public interface MealerSerializable extends Serializable {

    /**
     * Return the document id of the object
     * @return
     */
    String getId();

    /**
     * Set the document id of the object
     * @param id
     */
    void setId(String id);

    /**
     * Create a map where key/values are extracted using {@link MealerSerializableElement}
     * @return
     */
    default Map<String, Object> toMap() {
        try {
            Class<?> clazz = getClass();
            Map<String, Object> map = new HashMap<>();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(MealerSerializableElement.class)) {
                    map.put(field.getAnnotation(MealerSerializableElement.class).key(), field.get(this));
                }
            }

            return map;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
