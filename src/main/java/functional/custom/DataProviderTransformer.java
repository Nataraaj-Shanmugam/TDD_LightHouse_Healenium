package functional.custom;

import functional.annotations.TestDataAnnotation;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * Implements IAnnotationTransformer to customize TestNG annotations at runtime.
 * This class specifically focuses on transforming data provider annotations based
 * on the presence of the TestDataAnnotation on test methods.
 */
public class DataProviderTransformer implements IAnnotationTransformer {


    /**
     * Default constructor for DataProviderTransformer.
     * Initializes a new instance of this class with default settings.
     */
    public DataProviderTransformer() {
    }

    @Override
    /**
     * Transforms TestNG annotations at runtime. This method is invoked by TestNG
     * to give you a chance to modify the annotation behavior for test methods.
     *
     * @param annotation The annotation that needs to be transformed.
     * @param testClass The test class that contains the test method. May be null.
     * @param testConstructor The constructor of the test class. May be null.
     * @param testMethod The test method that is being annotated. May be null.
     */
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if(testMethod.isAnnotationPresent(TestDataAnnotation.class) && !testMethod.getAnnotation(TestDataAnnotation.class).isNoDataProvider()){
            // Set the data provider to 'customDataProvider' if 'isNoDataProvider' is false
            annotation.setDataProvider("customDataProvider");
        }else
            // Set the data provider to 'noDataProvider' if 'isNoDataProvider' is true or no 'TestDataAnnotation' annotation found
            annotation.setDataProvider("noDataProvider");
    }
}
