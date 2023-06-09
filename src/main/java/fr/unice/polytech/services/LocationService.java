package fr.unice.polytech.services;

import fr.unice.polytech.interfaces.ILocationService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService implements ILocationService {

    @Override
    public String getRequest(String url) throws IOException {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

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

    @Override
    public String buildURL(String address) {
        StringBuffer query= new StringBuffer();
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
        return query.toString();
    }
    @Override
    public Map<String, Double> getCoordinates(String address) throws IOException {
        Map<String, Double> res = new HashMap<String, Double>();
        String query = buildURL(address);
        String queryResult = null;

        queryResult = getRequest(query);

        Object obj = JSONValue.parse(queryResult);
        if (obj instanceof JSONArray array) {
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

    @Override
    public double distance(String address1, String address2) throws IOException {
        Map<String,Double> cord1 = getCoordinates(address1);
        Map<String,Double> cord2 = getCoordinates(address2);
        return distance(cord1.get("lat"),cord1.get("lon"),cord2.get("lat"),cord2.get("lon"),"km");

    }

    @Override
    public double distance(String address1, String address2, String unit) throws IOException {
        Map<String,Double> cord1 = getCoordinates(address1);
        Map<String,Double> cord2 = getCoordinates(address2);
        return distance(cord1.get("lat"),cord1.get("lon"),cord2.get("lat"),cord2.get("lon"),unit);

    }

}
