package br.com.nobru.cade_o_tio_flavio.service;

import br.com.nobru.cade_o_tio_flavio.domain.BusLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.nobru.cade_o_tio_flavio.domain.PointLocationCoordinates.PICKUP_POINT_LAT;
import static br.com.nobru.cade_o_tio_flavio.domain.PointLocationCoordinates.PICKUP_POINT_LON;

@Service
public class LocationService {

    private static final double NOTIFICATION_THRESHOLD_MINUTES = 5.0;

    @Autowired
    private GoogleRoutesService googleRoutesService;

    @Autowired
    private TwilioService twilioService;

    public void processBusLocation(BusLocation busLocation) {
        double[] result = googleRoutesService.getTravelTimeAndDistance(
                busLocation,
                PICKUP_POINT_LAT,
                PICKUP_POINT_LON
        );

        if (result[0] <= NOTIFICATION_THRESHOLD_MINUTES) {
            twilioService.sendWhatsAppMessage(createPhrase(result[0]));
        }
    }

    private String createPhrase(Double travelTime) {
        return "A Van está aproximadamente a " + Math.round(travelTime) + " minutos do ponto de encontro.";
    }
}
