package br.com.nobru.cade_o_tio_flavio.service;

import br.com.nobru.cade_o_tio_flavio.domain.BusLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class LocationServiceTest {

    @Mock
    private GoogleRoutesService googleRoutesService;

    @Mock
    private TwilioService twilioService;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testProcessBusLocationWithinThreshold() {
        // Arrange
        BusLocation busLocation = new BusLocation();
        busLocation.setLatitude(10.0);
        busLocation.setLongitude(20.0);

        double[] travelTimeAndDistance = {4.0, 2.0}; // 4 minutes, 2 km
        when(googleRoutesService.getTravelTimeAndDistance(busLocation, anyDouble(), anyDouble()))
                .thenReturn(travelTimeAndDistance);

        // Act
        locationService.processBusLocation(busLocation);

        // Assert
        verify(twilioService, times(1)).sendWhatsAppMessage(anyString());
    }

    @Test
    public void testProcessBusLocationOutsideThreshold() {
        // Arrange
        BusLocation busLocation = new BusLocation();
        busLocation.setLatitude(10.0);
        busLocation.setLongitude(20.0);

        double[] travelTimeAndDistance = {10.0, 2.0}; // 10 minutes, 2 km
        when(googleRoutesService.getTravelTimeAndDistance(busLocation, anyDouble(), anyDouble()))
                .thenReturn(travelTimeAndDistance);

        // Act
        locationService.processBusLocation(busLocation);

        // Assert
        verify(twilioService, times(0)).sendWhatsAppMessage(anyString());
    }
}