package orq.fiap.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {

    @Inject
    Mailer mailer;

    public void sendEmail() {
        mailer.send(
                Mail.withText("du_ikei@hotmail.com", "Hello from Quarkus", "This is a test email."));
    }
}
