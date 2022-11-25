package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.WhoIsData;
import com.smoketesting.sites.data.repository.WhoIsRepository;
import org.apache.commons.net.whois.WhoisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WhoIsService {
    private static Pattern pattern;
    private Matcher matcher;

    @Autowired
    WhoIsRepository whoIsRepo;

    private static final String WHOIS_SERVER = "whois.iana.org";

    private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";

    static {
        pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
    }

    public String queryWhoIsForDomain(String domainName) {
        StringBuilder result = new StringBuilder();
        try {
            String extension = Arrays
                    .stream(domainName.split("\\."))
                    .reduce((first, second) -> second)
                    .orElse(null);

            Socket socket = new Socket(WHOIS_SERVER, 43);
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF(extension + "\r\n");
            dataOutputStream.flush();

            List<String> res = in.lines().collect(Collectors.toList());
            String whoisServerUrl = Arrays.stream(
                            res
                                    .stream()
                                    .filter(str -> str.contains("whois:")).collect(Collectors.joining()).split(" ")
                    )
                    .reduce((first, second) -> second)
                    .orElse("");

            if (!whoisServerUrl.equals("")) {
                String whoisData2 = queryWithWhoisServer(domainName, whoisServerUrl);
                result.append(whoisData2);
            } else {
                return String.join("\n", res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String queryWithWhoisServer(String domainName, String whoisServer) throws UnknownHostException {
        String result = "";
        WhoisClient whois = new WhoisClient();
        try {
            whois.connect(whoisServer);
            result = whois.query(domainName);
            whois.disconnect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public WhoIsData getWhoIsByDomain(String domain) {
        return whoIsRepo.getByDomainIgnoreCase(domain);
    }

    public WhoIsData saveWhoIs(WhoIsData whoIs) {
        if (whoIs.getId() == null && whoIsRepo.existsByDomain(whoIs.getDomain())) {
            String id = whoIsRepo.getByDomainIgnoreCase(whoIs.getDomain()).getId();
            whoIs.setId(id);
        }
        return whoIsRepo.save(whoIs);
    }

}
