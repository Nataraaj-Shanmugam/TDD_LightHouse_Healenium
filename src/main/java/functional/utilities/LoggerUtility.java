package functional.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Utility class for configuring and obtaining Log4j Logger instances.
 * It initializes Log4j with a custom layout and a console appender.
 */
public class LoggerUtility {
    static {
        initializeLog4j();
    }

    /**
     * Default constructor for {@link LoggerUtility}.
     * Initializes a new instance of this class with default settings.
     */
    public LoggerUtility() {
    }

    /**
     * Initializes Log4j configuration with a custom layout and console appender.
     * This method configures a console appender with a specific pattern layout
     * and attaches it to a logger configuration, which is then used by the Log4j context.
     */
    private static void initializeLog4j() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        Layout layout = PatternLayout.newBuilder().setPattern("%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n")
                .build();

        Appender appender = ConsoleAppender.newBuilder()
                .setLayout(layout)
                .setName("ConsoleAppender")
                .setTarget(ConsoleAppender.Target.SYSTEM_OUT)
                .build();
        appender.start();

        config.addAppender(appender);

        LoggerConfig loggerConfig = new LoggerConfig("Default", Level.INFO, false);
        loggerConfig.addAppender(appender, null, null);

        config.addLogger("Default", loggerConfig);
        ctx.updateLoggers(config);
    }

    /**
     * Retrieves a Logger instance for a given class.
     * This method provides a centralized way to obtain a logger from the LogManager,
     * ensuring consistent logging behavior across the application.
     *
     * @param clazz The class for which the logger is to be obtained.
     * @return A Logger instance associated with the specified class.
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
