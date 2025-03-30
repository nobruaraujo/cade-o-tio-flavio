package br.com.nobru.cade_o_tio_flavio.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRoutesResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private String duration; // Duration is a string, e.g., "154s"
        private double distanceMeters;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public double getDistanceMeters() {
            return distanceMeters;
        }

        public void setDistanceMeters(double distanceMeters) {
            this.distanceMeters = distanceMeters;
        }

        // Helper method to parse duration string (e.g., "154s") into seconds
        public double getDurationSeconds() {
            if (duration != null && duration.endsWith("s")) {
                return Double.parseDouble(duration.replace("s", ""));
            }
            throw new IllegalArgumentException("Invalid duration format: " + duration);
        }
    }
}