package com.smoketesting.sites.util;

import com.smoketesting.sites.data.obj.WhoIsData;
import org.openqa.selenium.remote.server.handler.interactions.touch.Up;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhoIsConverter {
    static String DOMAIN = "Domain Name: ";
    static String REGISTRAR = "Registrar: ";
    static String REGISTERED_ON = "Creation Date: ";
    static String EXPIRES_ON = "Registry Expiry Date: ";
    static String UPDATED_ON = "Updated Date: ";
    static String DNS = "Name Server: ";

    static List<String> whoIsLookups = Arrays.asList(
            DOMAIN,
            REGISTRAR,
            REGISTERED_ON,
            EXPIRES_ON,
            UPDATED_ON,
            DNS
    );

    private static boolean entryIsInText(String text) {
        return whoIsLookups
                .stream()
                .anyMatch(text::contains);
    }

    public static WhoIsData convertWhoIsStringToWhoIsData(String whoIsString) {
        WhoIsData whoIsData = new WhoIsData();

        String whoIsRaw = Arrays.stream(whoIsString.split("Terms of Use")).findFirst().orElse("");
        whoIsData.setRawData(whoIsRaw);
        List<String> splitWhoIs = List.of(whoIsRaw.split("\n"));
        splitWhoIs
                .stream()
                .filter(WhoIsConverter::entryIsInText)
                .map(entry -> entry
                        .replaceAll("\r", "")
                        .replaceAll("\n", "")
                        .trim()
                )
                .forEach(entry -> {
                    if (entry.contains(DOMAIN)) {
                        whoIsData.setDomain(entry.replace(DOMAIN, ""));
                    } else if (entry.contains(REGISTRAR)) {
                        whoIsData.setRegistrar(entry.replace(REGISTRAR, ""));
                    } else if (entry.contains(REGISTERED_ON)) {
                        whoIsData.setRegisteredOn(entry.replace(REGISTERED_ON, ""));
                    } else if (entry.contains(EXPIRES_ON)) {
                        whoIsData.setExpiresOn(entry.replace(EXPIRES_ON, ""));
                    } else if (entry.contains(UPDATED_ON)) {
                        whoIsData.setUpdatedOn(entry.replace(UPDATED_ON, ""));
                    } else if (entry.contains(DNS)) {
                        if (whoIsData.getDNS() != null) {
                            String newDNS = whoIsData.getDNS() + "\n" + entry.replace(DNS, "");
                            whoIsData.setDNS(newDNS);
                        } else {
                            whoIsData.setDNS(entry.replace(DNS, ""));
                        }
                    }
                });

        return whoIsData;
    }
}
