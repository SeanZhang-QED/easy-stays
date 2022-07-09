package com.easy.easystays.service;

import com.easy.easystays.exception.GeoCodingException;
import com.easy.easystays.exception.InvalidStayAddressException;
import com.easy.easystays.model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingService {

    private GeoApiContext context; // which is built in GeiCodingConfig, added API Key into the context

    @Autowired
    public GeoCodingService(GeoApiContext context) {
        this.context = context;
    }

    // get location from address, and will be stored into ES for further use
    public Location getLocation(int id, String address) throws GeoCodingException {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
            if (result.partialMatch) {
                throw new InvalidStayAddressException("Failed to find stay address");
            }
            return new Location(id, new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
            throw new GeoCodingException("Failed to encode stay address");
        }
    }
}
