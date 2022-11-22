package com.smoketesting.sites.service;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SiteService {

    public static boolean isReachable(String url) throws IOException {
        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
        httpUrlConnection.setRequestMethod("GET");

        try {
            int responseCode = httpUrlConnection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (UnknownHostException noInternetConnection) {
            return false;
        }
    }

    public static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        long dots = domain.chars().filter(c -> c == '.').count();
        if (dots > 1) {
            domain = Arrays
                    .stream(domain.split("\\."))
                    .skip(dots -1)
                    .collect(Collectors.joining("."));
        }
        return domain;
    }

    public static String getWhoIsInfo(String url) throws URISyntaxException, UnknownHostException {
        try {
            String domain = getDomain(url);
            WhoIsService whoIsService = new WhoIsService();
            return whoIsService.getWhoIs(domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
