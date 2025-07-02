package orq.fiap.services;

import io.quarkus.logging.Log;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.dto.ResponseData;

@ApplicationScoped
public class WebhookService {

    WebClient client = WebClient.create(Vertx.vertx());

    public void sendData(String url, ResponseData payload) {
        JsonObject json = JsonObject.mapFrom(payload);

        client.postAbs(url)
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(json)
                .onSuccess(response -> {
                    Log.info("Response: " + response.bodyAsString());
                })
                .onFailure(err -> {
                    Log.info("Failed to send webhook: " + err.getMessage());
                });
    }
}
