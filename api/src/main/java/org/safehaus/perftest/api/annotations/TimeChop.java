package org.safehaus.perftest.api.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Annotate Jukito enabled test classes with this annotation to run time based stress tests.
 * The difference here is not the number of times your test runs but the amount of time for
 * which your tests should run.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TimeChop {

    /**
     * The amount of time to run the tests.
     *
     * @return the amount of time in milliseconds to run tests
     */
    long time() default AnnotationDefaults.DEFAULT_TIME;


    /**
     * The number of threads to use for concurrent invocation per runner.
     *
     * @return the number of concurrent threads per runner
     */
    int threads() default AnnotationDefaults.DEFAULT_THREADS;


    /**
     * The number of distributed runners to use for the chop.
     *
     * @return the number of distributed runners to use
     */
    int runners() default AnnotationDefaults.DEFAULT_RUNNERS;


    /**
     * Whether or not to perform a saturation test before running this test and
     * use those discovered saturation parameters as overrides to the provided
     * parameters.
     *
     * @return whether or not to run a preliminary saturation test
     */
    boolean saturate() default AnnotationDefaults.DEFAULT_SATURATE;


    /**
     * This parameter allows you to introduce a delay between test iterations.
     *
     * @return the delay between test iterations in milliseconds
     */
    long delay() default AnnotationDefaults.DEFAULT_DELAY;
}