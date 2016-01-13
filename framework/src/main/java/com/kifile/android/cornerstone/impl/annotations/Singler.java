package com.kifile.android.cornerstone.impl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Singler interface means the POJO object only exist
 *
 * @author kifile
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Singler {

    /**
     * The primary key of data.
     *
     * @return
     */
    String primary();
}
