package functional.utilities;

import functional.genericKeywords.GenericKeywords;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

import static io.qameta.allure.Allure.step;


/**
 * This class provides utility methods for reporting in a testing framework.
 * It includes functionalities for updating test status, logging messages,
 * and executing actions with logging. It integrates with the Allure reporting
 * framework and Log4j for logging.
 */
public class ReporterUtilities {
    private static final Logger logger = LoggerUtility.getLogger(ReporterUtilities.class);

    /**
     * Updates the test status in the Allure report and takes a screenshot.
     * Logs the test status using Log4j. If the test status is not 'PASSED',
     * it logs as info, otherwise, it logs as an error.
     *
     * @param status The status of the test (e.g., PASSED, FAILED, SKIPPED, etc.) from Allure's Status enum.
     */
    public static void updateTestStatus(Status status){
        GenericKeywords.takeScreenshot();
        if(status != Status.PASSED) logger.info("Testcase Passed");
        else logger.error(status);
        step("Test Case "+status.value(), status);
    }

    /**
     * Logs a message and executes a Callable action, returning the result of the action.
     * If an exception occurs during the execution of the action, it logs the error and prints the stack trace.
     *
     * @param <T>     The type of the result returned by the action.
     * @param message The message to log before executing the action.
     * @param action  The action to be executed.
     * @return The result of the Callable action.
     */
    @Step("{0}")
    public static <T> T log(String message, Callable<T> action){
        T result = null;
        logger.info(message);
        try {
            result = action.call();
        } catch (Exception e) {
            logger.error("Error during action execution", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Logs a message and executes a Runnable action.
     * If an exception occurs during the execution, it logs the error.
     *
     * @param message The message to log before executing the action.
     * @param action  The action to be executed.
     */
    @Step("{0}")
    public static void log(String message, Runnable action) {
        try {
            logger.info(message);
            action.run();
        } catch (Exception e) {
            logger.error("Error during action execution", e);
        }
    }

    /**
     * Logs a message at the info level.
     *
     * @param message The message to log.
     */
    @Step("{0}")
    public static void log(String message){
        logger.info(message);
    }
}
