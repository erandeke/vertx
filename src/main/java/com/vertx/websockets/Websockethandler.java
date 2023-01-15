package com.vertx.websockets;

import com.vertx.scalingwebserver.MainVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kedar Erande
 */
public class Websockethandler implements Handler<ServerWebSocket> {

  private static final Logger LOG = LoggerFactory.getLogger(Websockethandler.class);
  public static final String PATH_PRICES = "/ws/path/prices/";

  private PriceBroadcaster priceBroadcaster;

  public Websockethandler(Vertx vertx) {
    this.priceBroadcaster = new PriceBroadcaster(vertx);
  }


  @Override
  public void handle(ServerWebSocket serverWebSocket) {
    if (!PATH_PRICES.equals(serverWebSocket.path())) {
      LOG.info("Rejecting wrong path {}", serverWebSocket.path());
      serverWebSocket.writeFinalTextFrame("Wrong path" + PATH_PRICES + "is allowed");
      closeClientConnection(serverWebSocket);
      return;
    }
    LOG.info("Opening the websocket connection {} for path {}", serverWebSocket.textHandlerID(), serverWebSocket.path());
    serverWebSocket.accept();
    serverWebSocket.endHandler(close -> {
      LOG.info("Closed {}", serverWebSocket.textHandlerID()); //id of the connection
      priceBroadcaster.unRegisterForStreaming(serverWebSocket);
    });
    //To receive the messages from the client
    websocketFramehandler(serverWebSocket);
    serverWebSocket.writeTextMessage("Connected!"); //send back to client;
    priceBroadcaster.registerForStreaming(serverWebSocket);
  }



  private void websocketFramehandler(ServerWebSocket serverWebSocket) {
    serverWebSocket.frameHandler(received -> {
      if ("disconnectme".equalsIgnoreCase(received.textData())) {
        LOG.info("Closing the client connection {}", serverWebSocket.textHandlerID());
        closeClientConnection(serverWebSocket);
        return;
      } else {
        String text = received.textData();
        LOG.info("Message got  {} from {}", text, serverWebSocket.textHandlerID());
      }
    });
  }

  private Future<Void> closeClientConnection(ServerWebSocket serverWebSocket) {
    return serverWebSocket.close((short) 1000, "Closing normally");
  }
}
