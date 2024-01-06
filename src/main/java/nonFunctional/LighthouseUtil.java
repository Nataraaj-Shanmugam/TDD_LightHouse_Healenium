package nonFunctional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Utility class for running Lighthouse performance tests.
 * Provides methods to find a free network port and to execute Lighthouse with specific parameters.
 */
public class LighthouseUtil {

    /**
     * Default constructor for {@link LighthouseUtil}.
     * Initializes a new instance of this class with default settings.
     */
    public LighthouseUtil() {
    }

    /**
     * Finds a free network port within a specified range.
     *
     * @return An available port number between 50000 and 60000.
     */
    public int getFreePort(){
        int port = new Random().nextInt(60000 - 50000 + 1)+50000;
        while (true){
            try (Socket socket = new Socket("127.0.0.1", port)) {
            } catch (IOException ignored) {
                return port;
            }
            port = new Random().nextInt(60000 - 50000 + 1)+50000;
        }
    }

    /**
     * Runs a Lighthouse performance test for a given URL and outputs the report to a specified file.
     *
     * @param URL The URL to be tested.
     * @param reportName The name to be appended to the report file.
     * @param connectionPort The port number to be used by Lighthouse for the test.
     */
    public void performanceTest(String URL, String reportName, int connectionPort){
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "lighthouse", URL, "--port="+ connectionPort,
                "--preset=desktop", "--output=html", "--output-path=" + Paths.get("").toAbsolutePath() + File.separator + "LighthouseReport"+reportName + ".html");
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
