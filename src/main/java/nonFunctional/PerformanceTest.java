package nonFunctional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to mark test methods or classes for performance testing.
 * This annotation can be used to indicate that a specific test requires
 * performance-related settings or capabilities.
 *
 * It is retained at runtime to enable dynamic processing during test execution.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceTest {

}
