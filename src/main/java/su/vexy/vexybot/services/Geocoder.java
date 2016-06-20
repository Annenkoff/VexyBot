package su.vexy.vexybot.services;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import org.telegram.telegrambots.api.objects.Message;

import java.io.IOException;
import java.util.List;

public class Geocoder {
    private static com.google.code.geocoder.Geocoder geocoder;

    public static String getTextByCoordinates(Message message) throws IOException {
        geocoder = new com.google.code.geocoder.Geocoder();
        String lat = String.valueOf(message.getLocation().getLatitude());
        String lng = String.valueOf(message.getLocation().getLongitude());
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                .setLocation(new LatLng(lat, lng))
                .setLanguage("ru")
                .getGeocoderRequest();
        GeocodeResponse geocodeResponse = geocoder.geocode(geocoderRequest);
        List<GeocoderResult> s = geocodeResponse.getResults();
        return s.get(0).getFormattedAddress();
    }
}
