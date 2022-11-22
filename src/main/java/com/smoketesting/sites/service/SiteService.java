package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.obj.WhoIsData;
import com.smoketesting.sites.util.WhoIsConverter;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SiteService {

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

    public static String getIpForURL(String url) throws Exception {
        InetAddress inetAddress = InetAddress.getByName(new URL(url).getHost());
        return inetAddress.getHostAddress();
    }

    public static int getStatusCodeForURL(String testUrl) throws Exception {
        URL url = new URL(testUrl);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestProperty("User-Agent", "Mozilla/5.0");
        return http.getResponseCode();
    }

    public static WhoIsData getWhoIsDataForUrl(String url) throws UnknownHostException, URISyntaxException {
        String whoIs = getWhoIsInfo(url);
        return WhoIsConverter.convertWhoIsStringToWhoIsData(whoIs);
    }

    public static void queryWhoIsAndPingUrl(TestCase test) throws Exception {
        String url = test.getUrl();

        WhoIsData whoIsData = getWhoIsDataForUrl(url);
        test.setWhoIsData(whoIsData);

        String ipForUrl = getIpForURL(url);
        test.setIp(ipForUrl);

        int statusCode = getStatusCodeForURL(url);
        test.setStatusCode(statusCode);
    }
}
