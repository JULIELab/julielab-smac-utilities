package de.julielab.smac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;
import static spark.Spark.post;

public class HttpParamOptServer {
    public static final String GET_CONFIG_SCORE = "get_configuration_score_pm";
    public static final String INSTANCE = "instance";
    public static final String INSTANCE_INFO = "instance_info";
    public static final String CUTOFF_TIME = "cutoff_time";
    public static final String CUTOFF_LENGTH = "cutoff_length";
    public static final String SEED = "seed";
    private static final Logger log = LoggerFactory.getLogger(HttpParamOptServer.class);


    public HttpParamOptServer(int port) {
        port(port);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: " + HttpParamOptServer.class.getSimpleName() + " <http port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        HttpParamOptServer server = new HttpParamOptServer(port);
        server.startServer();
    }

    public void startServer() {

        post("/" + GET_CONFIG_SCORE, new EvaluateConfigurationRoute());

        log.info("Server is ready for requests.");
    }

}
