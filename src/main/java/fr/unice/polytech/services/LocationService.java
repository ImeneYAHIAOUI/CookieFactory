package fr.unice.polytech.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LocationService {

    private String getRequest(String url) throws IOException {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int a = con.getResponseCode();

        if (con.getResponseCode() != 200) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public Map<String, Double> getCoordinates(String address) {
        Map<String, Double> res = new HashMap<String, Double>();
        StringBuffer query= new StringBuffer();
        String queryResult = null;
        String[] split = address.split(",");
        query.append("https://nominatim.openstreetmap.org/search?q=");
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split(" ");
            for (int j = 0; j < split2.length; j++) {
                query.append(split2[j]);
                if (j != split2.length - 1) {
                    query.append("+");
                }
            }
            if (i < (split.length - 1)) {
                query.append("%2C");
            }
        }
        query.append("&format=json&addressdetails=1");

        try {
            queryResult = getRequest(query.toString());

        } catch (Exception e) {
            System.err.println(e);
        }
        Object obj = JSONValue.parse(queryResult);
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (array.size() > 0) {
                JSONObject jsonObject = (JSONObject) array.get(0);

                String lon = (String) jsonObject.get("lon");
                String lat = (String) jsonObject.get("lat");
                res.put("lon", Double.parseDouble(lon));
                res.put("lat", Double.parseDouble(lat));

            }
        }
        return res;
    }

    public double distance(String address1, String address2)
    {
        Map<String,Double> cord1 = getCoordinates(address1);
        Map<String,Double> cord2 = getCoordinates(address2);
        return distance(cord1.get("lat"),cord1.get("lon"),cord2.get("lat"),cord2.get("lon"),"km");

    }

    public double distance(String address1, String address2, String unit)
    {
        Map<String,Double> cord1 = getCoordinates(address1);
        Map<String,Double> cord2 = getCoordinates(address2);
        return distance(cord1.get("lat"),cord1.get("lon"),cord2.get("lat"),cord2.get("lon"),unit);

    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit)
    {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("Km")) {
                dist = dist * 1.609344;
            } else if (unit.equals("m")) {
                dist = dist * 1.609344 * 1000;
            }
            return (dist);
        }
    }

}
