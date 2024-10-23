package com.pierandrei.isisfibras.Service.MessageSenderService;

import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneNotFoundException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    // Inicializa a Twilio com SID e Auth Token após as variáveis serem carregadas
    public void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendMessage(String to, String body) throws PhoneNotFoundException {
        // Verifica se o número de telefone é válido
        if (to == null || to.isEmpty() || !isValidPhoneNumber(to)) {
            throw new PhoneNotFoundException("Número de telefone não existe ou é inválido!");
        }

        // Certifica-se de que Twilio está inicializado
        initializeTwilio();

        Message message = Message.creator(
                new PhoneNumber(to),  // Usa com.twilio.type.PhoneNumber
                new PhoneNumber(fromPhoneNumber),  // Usa o número configurado
                body
        ).create();

        System.out.println("Message sent: " + message.getSid());
    }

    // Método para verificar se o número de telefone é válido
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Implementação de validação simples: pode incluir regex ou regras específicas
        return phoneNumber.matches("\\+?[0-9]+"); // Exemplo: número deve ser composto apenas por dígitos
    }
}
