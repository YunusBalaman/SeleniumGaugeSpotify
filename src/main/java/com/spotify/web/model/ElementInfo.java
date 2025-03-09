package com.spotify.web.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import org.openqa.selenium.By;

import java.util.ArrayList;

public class ElementInfo
{
  @SerializedName("key")
  @Expose
  private String key;
  @SerializedName("value")
  @Expose
  private String value;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("keyValueChanger")
  @Expose
  private boolean keyValueChanger;
  @SerializedName("shadowRoot")
  @Expose
  private ArrayList<String> shadowRoot;
  @SerializedName("enviroment")
  @Expose
  private boolean enviroment;
  @SerializedName("enviromentMap")
  @Expose
  private LinkedTreeMap<String,ElementInfoSub> enviromentMap;

  private int fileNameIndex;

  private By by;

  public ElementInfo(){

  }

  public ElementInfo(String key, String value, String type, int fileNameIndex) {
    this.key = key;
    this.value = value;
    this.type = type;
    this.fileNameIndex = fileNameIndex;
  }

  public ElementInfo(String key, String value, String type, int fileNameIndex, boolean keyValueChanger) {
    this.key = key;
    this.value = value;
    this.type = type;
    this.fileNameIndex = fileNameIndex;
    this.keyValueChanger = keyValueChanger;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
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

  public boolean getKeyValueChanger() {
    return keyValueChanger;
  }

  public void setKeyValueChanger(boolean keyValueChanger) {
    this.keyValueChanger = keyValueChanger;
  }

  public int getFileNameIndex() {
    return fileNameIndex;
  }

  public void setFileNameIndex(int fileNameIndex) {
    this.fileNameIndex = fileNameIndex;
  }

  public ArrayList<String> getShadowRoot() {
    return shadowRoot;
  }

  public void setShadowRoot(ArrayList<String> shadowRoot) {
    this.shadowRoot = shadowRoot;
  }

  public boolean getEnviroment() {
    return enviroment;
  }

  public void setEnviroment(boolean enviroment) {
    this.enviroment = enviroment;
  }

  public LinkedTreeMap<String, ElementInfoSub> getEnviromentMap() {
    return enviromentMap;
  }

  public void setEnviromentMap(LinkedTreeMap<String, ElementInfoSub> enviromentMap) {
    this.enviromentMap = enviromentMap;
  }

  public By getBy() {
    return by;
  }

  public void setBy(By by) {
    this.by = by;
  }
}
