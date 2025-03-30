package br.com.nobru.cade_o_tio_flavio.controller;

import br.com.nobru.cade_o_tio_flavio.domain.BusLocation;
import br.com.nobru.cade_o_tio_flavio.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(@RequestBody BusLocation busLocation) {
        locationService.processBusLocation(busLocation);
        return ResponseEntity.ok("Localização recebida com sucesso.");
    }
}
