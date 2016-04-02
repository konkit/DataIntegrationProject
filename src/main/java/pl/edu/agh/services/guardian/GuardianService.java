package pl.edu.agh.services.guardian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.json.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Mere on 2016-04-02.
 */
@Service
public class GuardianService {

    @Value("${guardian.key}")
    private String key;

    @Value("${guardian.api.endpoint}")
    private String apiEndpoint;

    static Logger logger = LoggerFactory.getLogger(GuardianService.class);

    public void start() throws IOException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

        String address = new String(apiEndpoint + "&api-key=" + key);

        URL url = new URL(address);
        Scanner scanner = new Scanner(url.openStream());
        String str = new String();
        while(scanner.hasNext()) {
            str += scanner.nextLine();
        }
        scanner.close();

        JSONObject jsonObject = new JSONObject(str);
        if(! jsonObject.getString("status").equals("OK")) return;

        JSONObject result = jsonObject.getJSONArray("results").getJSONObject(0);
    }

}

