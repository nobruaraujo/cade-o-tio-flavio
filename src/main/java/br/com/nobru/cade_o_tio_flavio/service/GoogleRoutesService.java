package br.com.nobru.cade_o_tio_flavio.service;
import br.com.nobru.cade_o_tio_flavio.domain.BusLocation;
import br.com.nobru.cade_o_tio_flavio.domain.GoogleRoutesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

@Service
public class GoogleRoutesService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public double[] getTravelTimeAndDistance(BusLocation busLocation, double destLat, double destLon) {
        try {
            // Load the JSON template from the resources folder
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("routes_request.json");
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonRequest = (ObjectNode) objectMapper.readTree(inputStream);

            // Replace the necessary attributes
            jsonRequest.withObject("/origin/location/latLng").put("latitude", busLocation.getLatitude());
            jsonRequest.withObject("/origin/location/latLng").put("longitude", busLocation.getLongitude());
            jsonRequest.withObject("/destination/location/latLng").put("latitude", destLat);
            jsonRequest.withObject("/destination/location/latLng").put("longitude", destLon);

            // Convert the modified JSON object back to a string
            String requestBody = objectMapper.writeValueAsString(jsonRequest);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("X-Goog-Api-Key", apiKey);
            headers.set("X-Goog-FieldMask", "routes.duration,routes.distanceMeters"); // Include both duration and distanceMeters

            // Make the HTTP POST request
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<GoogleRoutesResponse> response = restTemplate.postForEntity(
                    "https://routes.googleapis.com/directions/v2:computeRoutes",
                    entity,
                    GoogleRoutesResponse.class
            );

            // Parse the response
            if (response.getBody() != null && !response.getBody().getRoutes().isEmpty()) {
                double distanceMeters = response.getBody().getRoutes().get(0).getDistanceMeters();
                double durationSeconds = response.getBody().getRoutes().get(0).getDurationSeconds();
                return new double[]{durationSeconds / 60.0, distanceMeters / 1000.0}; // Convert seconds to minutes and meters to kilometers
            }

            throw new RuntimeException("Failed to calculate travel time and distance.");
        } catch (Exception e) {
            throw new RuntimeException("Error processing the Routes API request", e);
        }
    }
}