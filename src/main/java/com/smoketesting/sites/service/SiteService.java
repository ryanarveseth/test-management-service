package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.Alert;
import com.smoketesting.sites.data.obj.Constants;
import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.obj.WhoIsData;
import com.smoketesting.sites.util.WhoIsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class SiteService {

    @Autowired
    WhoIsService whoIsService;

    public static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        long dots = domain.chars().filter(c -> c == '.').count();
        if (dots > 1) {
            domain = Arrays
                    .stream(domain.split("\\."))
                    .skip(dots - 1)
                    .collect(Collectors.joining("."));
        }
        return domain;
    }

    private boolean lastCheckedIsToday(Date date) {
        if (date == null) return false;
        return date
                .toInstant()
                .truncatedTo(ChronoUnit.DAYS)
                .equals(new Date()
                        .toInstant()
                        .truncatedTo(ChronoUnit.DAYS));
    }

    public WhoIsData getWhoIsInfo(String url) throws URISyntaxException {
        try {
            String domain = getDomain(url);
            WhoIsData whoIsData = whoIsService.getWhoIsByDomain(domain);
            if (whoIsData == null || !lastCheckedIsToday(whoIsData.getLastChecked())) {
                String whoIs = whoIsService.queryWhoIsForDomain(domain);
                WhoIsData newWhoIsData = WhoIsConverter.convertWhoIsStringToWhoIsData(whoIs);
                if (whoIsData != null && whoIsData.getId() != null)
                    newWhoIsData.setId(whoIsData.getId());
                newWhoIsData.setLastChecked(new Date());
                return whoIsService.saveWhoIs(newWhoIsData);
            } else {
                return whoIsData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getIpForURL(String url) throws Exception {
        InetAddress inetAddress = InetAddress.getByName(new URL(url).getHost());
        return inetAddress.getHostAddress();
    }

    public int getStatusCodeMinimalHeaders(String testUrl) throws IOException {
        URL url = new URL(testUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("HEAD");
        http.setRequestProperty("User-Agent", Constants.getRandomUserAgent());
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);
        return http.getResponseCode();
    }

    public int getStatusCodeAdditionalHeaders(String testUrl) throws IOException {
        URL url = new URL(testUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("HEAD");
        http.setRequestProperty("User-Agent", Constants.getRandomUserAgent());
        http.setRequestProperty("accept", "*/*");
        http.setRequestProperty("accept-encoding", "gzip,deflate,br");
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);
        return http.getResponseCode();
    }

    public int getStatusCodeForURL(String testUrl) {
        try {
            return getStatusCodeMinimalHeaders(testUrl);
        } catch (RuntimeException | IOException e) {
            try {
                return getStatusCodeAdditionalHeaders(testUrl);
            } catch (Exception ex) {
                // Request Timed out
                ex.printStackTrace();
                return 0;
            }
        }
    }

    public void queryWhoIsAndPingUrl(TestCase test) throws Exception {
        String url = test.getUrl();

        WhoIsData whoIsData = getWhoIsInfo(url);
        test.setWhoIsData(whoIsData);

        String ipForUrl = getIpForURL(url);
        test.setIp(ipForUrl);

        int statusCode = getStatusCodeForURL(url);
        test.setStatusCode(statusCode);
        if (statusCode == 0) {
            test.addAlert(new Alert("URL could not be reached or Pinged.\nThe Server may be getting blocked by the client."));
        }
    }
}
