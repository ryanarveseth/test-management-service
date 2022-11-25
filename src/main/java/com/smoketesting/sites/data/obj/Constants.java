package com.smoketesting.sites.data.obj;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Constants {
    public static List<Integer> STATUSES = Arrays.asList(
            200,
            201
    );

    private static List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:77.0) Gecko/20100101 Firefox/77.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
    );

    public static String getRandomUserAgent() {
        Random random = new Random();
        return USER_AGENTS.get(random.nextInt(USER_AGENTS.size()));
    }
}
