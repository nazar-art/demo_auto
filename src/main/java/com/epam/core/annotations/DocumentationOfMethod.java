package com.epam.core.annotations;

import java.lang.annotation.*;

/**
 * Describes the author of method
 * Describes the date of method creation
 * Describes the date of method update
 * Describes the name of user who updated method
 *
 * @author Taras Rusynyak
 * @version 1.0
 * @since 14 December 2012
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentationOfMethod {
    String author();

    String creationDate();

    String dateOfUpdate();

    String ApdatedBy();

}
