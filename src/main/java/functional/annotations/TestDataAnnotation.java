package functional.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation used to define test data properties for TestNG test methods.
 * This annotation allows specifying the Google Sheet details and data provider behavior.
 */
@Retention(RetentionPolicy.RUNTIME) // Annotation is available at runtime
@Target(ElementType.METHOD) // Applicable to methods only
public @interface TestDataAnnotation {

    /**
     * Specifies the name of the sheet in the Google Sheet document from which to retrieve test data.
     *
     * @return The name of the sheet.
     */
    String sheetName() default "";

    /**
     * Specifies the ID of the Google Sheet document from which to retrieve test data.
     *
     * @return The ID of the Google Sheet.
     */
    String sheetId() default "";

    /**
     * Flag to indicate whether a data provider should be used or not.
     * If set to true, no data provider will be used for the test method.
     *
     * @return True if no data provider is to be used, false otherwise.
     */
    boolean isNoDataProvider() default false;

    /**
     * Specifies a specific row number or range of rows in the sheet to use for test data.
     * Default is "All", which means all rows are considered.
     *
     * @return The row number or range as a string.
     */
    String rowNumber() default "All";
}
