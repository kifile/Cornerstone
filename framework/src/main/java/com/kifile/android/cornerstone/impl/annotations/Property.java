package com.kifile.android.cornerstone.impl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property Annotation is used in DataConverter to auto-wired data.
 * <p/>
 *
 * @author kifile
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    String name();

    Class<?> type() default Void.class; // type is used to mark which type the list will hold.
}
