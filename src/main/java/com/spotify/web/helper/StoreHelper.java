package com.spotify.web.helper;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spotify.web.model.ElementInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.fail;

public enum StoreHelper {
  INSTANCE;
  final Logger logger = LogManager.getLogger(getClass());
  private static final String DEFAULT_DIRECTORY_PATH = "elementValues";
  private static String filePath = "";
  ConcurrentHashMap<String, Object> elementMapList;
  List<String> fileNameList;

  StoreHelper() {
    initMap(getFileList());
  }

  private void initMap(List<File> fileList) {

    boolean initMapError = false;
    elementMapList = new ConcurrentHashMap<String, Object>();
    Type elementType = new TypeToken<List<ElementInfo>>() {}.getType();
    Gson gson = new Gson();
    List<ElementInfo> elementInfoList = null;
    File file = null;
    fileNameList = new ArrayList<>();
    for (int i = 0; i < fileList.size(); i++) {
      file = fileList.get(i);
      filePath = file.getPath().split(DEFAULT_DIRECTORY_PATH,2)[1];
      fileNameList.add(filePath);
      try {
        elementInfoList = gson.fromJson(new FileReader(file), elementType);
        int fileNameListIndex = i;
        elementInfoList.parallelStream()
                .forEach(elementInfo -> setElementListMap(elementInfo, fileNameListIndex));
      } catch (FileNotFoundException e) {
        logger.warn("{} not found", e);
      }catch (JsonIOException | JsonSyntaxException e) {
        initMapError = true;
        logger.error(filePath + " dosyasında syntax hatalı olabilir");
      }
    }
    if (initMapError){
      fail("Json dosya okuma hatası Logu kontrol edip hatalı dosya yollarını belirleyebilirsiniz");
    }
  }

  private void setElementListMap(ElementInfo elementInfo, int fileNameListIndex){

    if (elementInfo == null){
      fail(filePath + " dosyasında syntax hatalı");
    }
    elementInfo.setFileNameIndex(fileNameListIndex);
    if (elementInfo.getKey() != null)
      elementMapList.put(elementInfo.getKey(), elementInfo);
  }

  private List<File> getFileList() {
    URI uri = null;
    String jsonPath = "";
    try {
      uri = new URI(this.getClass().getClassLoader().getResource(DEFAULT_DIRECTORY_PATH).getFile());
      File file = new File(uri.getPath());
      jsonPath = file.getAbsolutePath();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      throw new NullPointerException("File Directory Is Not Found! file name: " + DEFAULT_DIRECTORY_PATH);
    }
    List<File> list = new ArrayList<>();
    try {
      Files.walk(Paths.get(jsonPath))
              .filter(Files::isRegularFile)
              .forEach(path -> addFileList(path, list));
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("json uzantılı dosya sayısı: " + list.size());
    if (list.size() == 0){
      throw new NullPointerException("Json uzantılı dosya bulunamadı."
              + " Default Directory Path = " + uri.getPath());
    }
    return list;
  }

  private void addFileList(Path path, List<File> list){

    File file = path.toFile();
    if (file.getName().endsWith(".json")){
      list.add(file);
    }
  }

  public ElementInfo findElementInfoByKey(String key) {

    if(!elementMapList.containsKey(key)){
      fail(key + " adına sahip element json dosyalarında mevcut elementler arasında bulunamadı. Lütfen kontrol ediniz...");
    }
    return (ElementInfo) elementMapList.get(key);
  }

  public String getFileName(int i){

    return fileNameList.get(i);
  }

  public void addElementInfoByKey(String key, ElementInfo elementInfo){elementMapList.put(key,elementInfo);}

  public boolean containsKey(String key){return elementMapList.containsKey(key);}

}