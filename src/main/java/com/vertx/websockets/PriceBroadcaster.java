package com.vertx.websockets;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Kedar Erande
 */
public class PriceBroadcaster {

  Map<String, ServerWebSocket> connectedClients = new HashMap<>();
  private static final Logger LOG = LoggerFactory.getLogger(PriceBroadcaster.class);

  public PriceBroadcaster(Vertx vertx) {
    this.periodicUpdate(vertx);
  }

  protected void registerForStreaming(ServerWebSocket serverWebSocket) {
    connectedClients.put(serverWebSocket.textHandlerID(), serverWebSocket);
  }

  protected void unRegisterForStreaming(ServerWebSocket serverWebSocket) {
    connectedClients.remove(serverWebSocket.textHandlerID());
  }

  private void periodicUpdate(Vertx vertx) {
    vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), message -> {
      LOG.debug("Push message to {} clients", connectedClients.size());
      connectedClients.values().forEach(client -> {
        client.writeTextMessage(new JsonObject()
          .put("symbl", "IND")
          .put("value", new Random().nextInt(100)).toString());
      });
    });
  }
}
