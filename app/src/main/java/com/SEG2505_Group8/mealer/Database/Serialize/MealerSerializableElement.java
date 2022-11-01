package com.SEG2505_Group8.mealer.Database.Serialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for field that can be serialized into database
 * Annotated fields must be in class with {@link MealerSerializable} annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MealerSerializableElement {
    /**
     * Key to associate with element
     * @return
     */
    String key();
}
