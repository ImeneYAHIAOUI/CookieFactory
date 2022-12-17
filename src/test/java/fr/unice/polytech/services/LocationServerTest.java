package fr.unice.polytech.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class LocationServerTest {

    @Spy
    LocationService locationService = spy(LocationService.class);

    @Test
    public void testBuildUrl() {
        Assertions.assertEquals("https://nominatim.openstreetmap.org/search?q=2400+route+des+dolines%2C+06560+valbonne&format=json&addressdetails=1", locationService.buildURL("2400 route des dolines, 06560 valbonne"));
        Assertions.assertEquals("https://nominatim.openstreetmap.org/search?q=avenue+saint-philippe%2C+sophia+antipolis&format=json&addressdetails=1", locationService.buildURL("avenue saint-philippe, sophia antipolis"));
    }

    @Test
    public void getCoordinates() throws Exception {
        doReturn("[{\"place_id\":195510936,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":386064552,\"boundingbox\":[\"43.623867\",\"43.624461\",\"7.050783\",\"7.051055\"],\"lat\":\"43.624172\",\"lon\":\"7.050919660340314\",\"display_name\":\"Résidence Newton, 2400, Route des Dolines, Garbejaïre, Sophia Antipolis, Valbonne, Grasse, Alpes-Maritimes, Provence-Alpes-Côte d'Azur, France métropolitaine, 06560, France\",\"class\":\"building\",\"type\":\"dormitory\",\"importance\":0.6201,\"address\":{\"building\":\"Résidence Newton\",\"house_number\":\"2400\",\"road\":\"Route des Dolines\",\"neighbourhood\":\"Garbejaïre\",\"suburb\":\"Sophia Antipolis\",\"town\":\"Valbonne\",\"municipality\":\"Grasse\",\"county\":\"Alpes-Maritimes\",\"ISO3166-2-lvl6\":\"FR-06\",\"state\":\"Provence-Alpes-Côte d'Azur\",\"ISO3166-2-lvl4\":\"FR-PAC\",\"region\":\"France métropolitaine\",\"postcode\":\"06560\",\"country\":\"France\",\"country_code\":\"fr\"}}]").when(locationService).getRequest("https://nominatim.openstreetmap.org/search?q=2400+route+des+dolines%2C+06560+valbonne&format=json&addressdetails=1");
        doReturn("[{\"place_id\":117413608,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":42559240,\"boundingbox\":[\"43.6169133\",\"43.6175282\",\"7.0757417\",\"7.0767974\"],\"lat\":\"43.6171275\",\"lon\":\"7.0761307\",\"display_name\":\"Avenue Saint-Philippe, Saint-Philippe, Sophia Antipolis, Biot, Grasse, Alpes-Maritimes, Provence-Alpes-Côte d'Azur, France métropolitaine, 06410, France\",\"class\":\"highway\",\"type\":\"residential\",\"importance\":0.6,\"address\":{\"road\":\"Avenue Saint-Philippe\",\"retail\":\"Saint-Philippe\",\"suburb\":\"Sophia Antipolis\",\"village\":\"Biot\",\"municipality\":\"Grasse\",\"county\":\"Alpes-Maritimes\",\"ISO3166-2-lvl6\":\"FR-06\",\"state\":\"Provence-Alpes-Côte d'Azur\",\"ISO3166-2-lvl4\":\"FR-PAC\",\"region\":\"France métropolitaine\",\"postcode\":\"06410\",\"country\":\"France\",\"country_code\":\"fr\"}},{\"place_id\":153966958,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":204277133,\"boundingbox\":[\"43.6159855\",\"43.6165295\",\"7.0781168\",\"7.0801461\"],\"lat\":\"43.6160426\",\"lon\":\"7.0790297\",\"display_name\":\"Avenue Saint-Philippe, Saint-Philippe, Sophia Antipolis, Biot, Grasse, Alpes-Maritimes, Provence-Alpes-Côte d'Azur, France métropolitaine, 06410, France\",\"class\":\"highway\",\"type\":\"unclassified\",\"importance\":0.6,\"address\":{\"road\":\"Avenue Saint-Philippe\",\"neighbourhood\":\"Saint-Philippe\",\"suburb\":\"Sophia Antipolis\",\"village\":\"Biot\",\"municipality\":\"Grasse\",\"county\":\"Alpes-Maritimes\",\"ISO3166-2-lvl6\":\"FR-06\",\"state\":\"Provence-Alpes-Côte d'Azur\",\"ISO3166-2-lvl4\":\"FR-PAC\",\"region\":\"France métropolitaine\",\"postcode\":\"06410\",\"country\":\"France\",\"country_code\":\"fr\"}},{\"place_id\":214834491,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":498567208,\"boundingbox\":[\"43.6161205\",\"43.6163919\",\"7.0770568\",\"7.0773325\"],\"lat\":\"43.6162165\",\"lon\":\"7.0771693\",\"display_name\":\"Avenue Saint-Philippe, Sophia Antipolis, Biot, Grasse, Alpes-Maritimes, Provence-Alpes-Côte d'Azur, France métropolitaine, 06410, France\",\"class\":\"highway\",\"type\":\"unclassified\",\"importance\":0.6,\"address\":{\"road\":\"Avenue Saint-Philippe\",\"suburb\":\"Sophia Antipolis\",\"village\":\"Biot\",\"municipality\":\"Grasse\",\"county\":\"Alpes-Maritimes\",\"ISO3166-2-lvl6\":\"FR-06\",\"state\":\"Provence-Alpes-Côte d'Azur\",\"ISO3166-2-lvl4\":\"FR-PAC\",\"region\":\"France métropolitaine\",\"postcode\":\"06410\",\"country\":\"France\",\"country_code\":\"fr\"}}]").when(locationService).getRequest("https://nominatim.openstreetmap.org/search?q=avenue+saint-philippe%2C+sophia+antipolis&format=json&addressdetails=1");
        Map<String, Double> result1 =  new HashMap<>(); //locationService.getCoordinates("2400 route des dolines, 06560 valbonne");
        Map<String, Double> result2 = new HashMap<>();  //locationService.getCoordinates("avenue saint-philippe, sophia antipolis");
        result1.put("lat", 43.624172);
        result1.put("lon", 7.050919660340314);
        result2.put("lat", 43.6171275);
        result2.put("lon", 7.0761307);
        Assertions.assertEquals(result1, locationService.getCoordinates("2400 route des dolines, 06560 valbonne"));
        Assertions.assertEquals(result2, locationService.getCoordinates("avenue saint-philippe, sophia antipolis"));
    }

    @Test
    public void testGetCoordinatesWithException() throws Exception {
        doThrow(new IOException()).when(locationService).getRequest("https://nominatim.openstreetmap.org/search?q=snow+white+and+the+dwarfs+house%2C+1234+fairy+tale+land&format=json&addressdetails=1");
        assertThrows(IOException.class,() -> locationService.getCoordinates("snow white and the dwarfs house, 1234 fairy tale land"));
    }

    @Test
    public void testDistance() throws IOException {
        Map<String, Double> cords1 = new HashMap<>();
        Map<String, Double> cords2 = new HashMap<>();
        cords1.put("lat", 43.624172);
        cords1.put("lon", 7.050919660340314);
        cords2.put("lat", 43.6171275);
        cords2.put("lon", 7.0761307);
        doReturn(cords1).when(locationService).getCoordinates("2400 route des dolines, 06560 valbonne");
        doReturn(cords2).when(locationService).getCoordinates("avenue saint-philippe, sophia antipolis");
        Assertions.assertEquals(2.17, locationService.distance("2400 route des dolines, 06560 valbonne", "avenue saint-philippe, sophia antipolis"), 0.1);
        Assertions.assertEquals(2.17, locationService.distance("2400 route des dolines, 06560 valbonne", "avenue saint-philippe, sophia antipolis","km"), 0.1);
        Assertions.assertEquals(2175, locationService.distance("2400 route des dolines, 06560 valbonne", "avenue saint-philippe, sophia antipolis","m"), 10);
    }








}
