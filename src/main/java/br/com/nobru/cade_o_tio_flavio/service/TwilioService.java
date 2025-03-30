package br.com.nobru.cade_o_tio_flavio.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String twilioNumber;

    @Value("${user.whatsapp.number}")
    private String userNumber;

    public void sendWhatsAppMessage(String messageBody) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new PhoneNumber(userNumber),
                new PhoneNumber(twilioNumber),
                messageBody)
                .create();

        System.out.println("Mensagem enviada com sucesso: " + message.getSid());
    }
}
