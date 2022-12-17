package fr.unice.polytech.interfaces;

import java.io.IOException;
import java.util.Map;

public interface ILocationService {
    String getRequest(String url) throws IOException;

    String buildURL(String address);

    Map<String, Double> getCoordinates(String address) throws IOException;

    double distance(String address1, String address2) throws IOException;

    double distance(String address1, String address2, String unit) throws IOException;

    default double distance(double lat1, double lon1, double lat2, double lon2, String unit)
    {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(
                    Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("km")) {
                dist = dist * 1.609344;
            } else if (unit.equals("m")) {
                dist = dist * 1.609344 * 1000;
            }
            return (dist);
        }
    }
}
