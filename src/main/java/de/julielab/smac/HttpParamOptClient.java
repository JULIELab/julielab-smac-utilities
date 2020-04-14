package de.julielab.smac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * <p>Simple HTTP client to connect to {@link HttpParamOptServer}.</p>
 * <p>While this class could be used in a SMAC wrapper, the preferred method to run SMAC is the bash
 * script in <tt>scripts/smacOverHttpWrapper.sh</tt>. This class is here to reproduce results later, e.g.
 * for evaluating best found configurations on a dev or test set.</p>
 */
public class HttpParamOptClient {
    public static Double requestScoreFromServer(Map<String, String> configuration, String instance, String host, String endpoint, int port) throws IOException {
        String urlString = String.format("http://%s:%d/%s", host, port, endpoint);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        // Create the request string
        String data = String.format("instance=%s&instance_info=none&cutoff_time=0&cutoff_length=0&seed=1", instance);
        StringBuilder sb = new StringBuilder(data);
        for (String key : configuration.keySet())
            sb.append("&").append(key).append("=").append(configuration.get(key));
        writer.write(sb.toString());

        writer.flush();
        String line;
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));
        Double score = null;
        while ((line = reader.readLine()) != null) {
            if (score != null)
                throw new IllegalStateException("The response has multiple lines. Only one line was expected.");
            try {
                score = Double.parseDouble(line);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The response should be a double value reflecting the score of the run. It actually was " + line);
            }
        }
        writer.close();
        reader.close();

        return score;
    }
}
