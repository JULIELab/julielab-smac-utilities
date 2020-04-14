package de.julielab.smac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

import static de.julielab.smac.HttpParamOptServer.*;

public class EvaluateConfigurationRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(EvaluateConfigurationRoute.class);

    public EvaluateConfigurationRoute() {
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        if (req.queryParams().contains("SHUTDOWN")) {
            log.info("Committing all caches is done, server can be shutdown.");
            return 0;
        }
        String score;
        try {
            Set<String> queryParams = req.queryParams();
            String instanceName = null;
            String instanceInfo = null;
            int cutoffTime = 0;
            int cutoffLength = 0;
            int seed = 0;
            String indexSuffix = null;
            String[] metricsToReturn = null;
            boolean metricsPerTopic = false;
            List<String> parameters = new ArrayList<>(queryParams.size());
            // Fill the beginning of the list because the parameter parsing algorithm is expecting it
            parameters.addAll(Arrays.asList(null, null, null, null, null));
            for (String queryParam : queryParams) {
                switch (queryParam) {
                    case INSTANCE:
                        instanceName = req.queryParams(queryParam);
                        break;
                    case INSTANCE_INFO:
                        instanceInfo = req.queryParams(queryParam);
                        break;
                    case CUTOFF_TIME:
                        cutoffTime = (int) Math.round(Double.valueOf(req.queryParams(queryParam)));
                        break;
                    case CUTOFF_LENGTH:
                        cutoffLength = (int) Math.round(Double.valueOf(req.queryParams(queryParam)));
                        break;
                    case SEED:
                        seed = Integer.valueOf(req.queryParams(queryParam));
                        break;
                    default:
                        parameters.add("-" + queryParam);
                        parameters.add(req.queryParams(queryParam));
                        break;
                }
            }
            Map<String, Object> parameterMap = parseParameters(parameters);
            score = calculateScore(instanceName, parameterMap, seed);
        } catch (Exception e) {
            throw e;
        }
        return score;
    }

    protected Map<String, Object> parseParameters(List<String> parameters) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++) {
            String s = parameters.get(i);
            if (i % 2 == 1)
                map.put(parameters.get(i - 1), s);
        }
        return map;
    }


    protected String calculateScore(String instance, Map<String, Object> parameterMap,  int seed) {
        return "-0";
    }

}
