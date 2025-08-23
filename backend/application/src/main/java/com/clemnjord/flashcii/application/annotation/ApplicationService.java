package com.clemnjord.flashcii.application.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A Domain Service, i.e. a feature that belongs to the domain and the ubiquitous language.
 *
 * @see <a href=
 *     "https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Domain-Driven
 *     Design Reference</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationService {
}
