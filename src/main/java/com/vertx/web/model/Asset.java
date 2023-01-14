package com.vertx.web.model;

import io.vertx.core.json.JsonObject;

/**
 * @author Kedar Erande
 */
public class Asset {

  private String productId;
  private String productName;
  private Double price;

  public Asset() {

  }

  public Asset(String productId, String productName, Double price) {
    this.productId = productId;
    this.productName = productName;
    this.price = price;
  }

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }


}
