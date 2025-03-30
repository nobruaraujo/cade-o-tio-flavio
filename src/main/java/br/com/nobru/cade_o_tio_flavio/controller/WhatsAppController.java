package br.com.nobru.cade_o_tio_flavio.controller;

import br.com.nobru.cade_o_tio_flavio.service.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/webhook")
    public String receiveMessage(@RequestParam Map<String, String> params) {
        String from = params.get("From");
        String latitude = params.get("Latitude");
        String longitude = params.get("Longitude");

        if (latitude != null && longitude != null) {
            return "Localização recebida de " + from + ": " + latitude + ", " + longitude;
        }
        return "Envie sua localização pelo WhatsApp";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        twilioService.sendWhatsAppMessage(message);
        return "Mensagem enviada: " + message;
    }

}
