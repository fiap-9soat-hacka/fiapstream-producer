package orq.fiap.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {

    @Inject
    Mailer mailer;

    public void sendEmail(String email, String filename) {
        mailer.send(
                Mail.withText(email,
                        "FiapStream - Falha na Requisição",
                        "Esse é um email automático para avisá-lo que a sua requisição para o processamento do video \""
                                + filename +
                                "\" falhou. Por favor, verifique se o seu input é válido e tente novamente."));
    }
}
