package com.spotify.web.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElementInfoSub
{
  @SerializedName("value")
  @Expose
  private String value;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("copyAnotherEnviroment")
  @Expose
  private String copyAnotherEnviroment;

  public ElementInfoSub(){

  }

  public ElementInfoSub(String value, String type, String copyAnotherEnviroment) {
    this.value = value;
    this.type = type;
    this.copyAnotherEnviroment = copyAnotherEnviroment;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCopyAnotherEnviroment() {
    return copyAnotherEnviroment;
  }

  public void setCopyAnotherEnviroment(String copyAnotherEnviroment) {
    this.copyAnotherEnviroment = copyAnotherEnviroment;
  }
}
