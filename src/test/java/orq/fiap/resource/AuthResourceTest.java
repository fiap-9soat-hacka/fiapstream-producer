package orq.fiap.resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import orq.fiap.dto.AuthSignInRequest;
import orq.fiap.dto.UserCreateRequest;
import orq.fiap.services.AuthService;

@QuarkusTest
public class AuthResourceTest {

    @InjectMock
    AuthService authService;

    @Test
    void testaSucessoSignUp() {
        Long userId = 1L;
        when(authService.signUp(any())).thenReturn(userId);

        UserCreateRequest userCreateRequest = new UserCreateRequest("teste", "teste", "teste@teste.com.br");

        given()
                .contentType("application/json")
                .body(userCreateRequest)
                .when()
                .post("/auth/sign-up")
                .then()
                .statusCode(201)
                .header("Location", containsString("/user/" + userId));
    }

    @Test
    void testaSucessoSignIn() {
        when(authService.signIn(any(), any())).thenReturn("123");
        AuthSignInRequest signInRequest = new AuthSignInRequest("teste", "teste");

        given()
                .contentType("application/json")
                .body(signInRequest)
                .when()
                .post("/auth/sign-in")
                .then()
                .statusCode(200)
                .body(containsString("123"));
    }

}
