package orq.fiap.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import io.quarkus.mailer.Mailer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class EmailServiceTest {

    @Inject
    EmailService emailService;

    @Inject
    Mailer mailer;

    @Test
    void testaSucessoEnviarEmail() {
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail("teste", "teste"));
    }
}
