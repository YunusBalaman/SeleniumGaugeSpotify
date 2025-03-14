package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.utils.ReadProperties;
import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

public class SpotifyStepImplementation {

    private static final Logger logger = LogManager.getLogger(SpotifyStepImplementation.class);
    MethodsUtil methodsUtil;

    public SpotifyStepImplementation() {

        methodsUtil = new MethodsUtil();
    }

    @Step("<language> Spotify Language Properties")
    public void spotifyLanguageProperties(String language){

        language = methodsUtil.setValueWithMapKey(language);
        ResourceBundle resourceBundle = null;
        String path = methodsUtil.getResourceTargetPath("languages");
        switch(language){
            case "en":
            case "English":
                language = "en";
                break;
            case "tr":
            case "Türkçe":
                language = "tr";
                break;
            default:
                Assertions.fail("Hata " + path);
        }
        resourceBundle = ReadProperties.readPropDir(path + Driver.slash + "spotify" + language.toUpperCase() + ".properties");
        ResourceBundle finalResourceBundle = resourceBundle;
        resourceBundle.keySet().forEach(key -> Driver.TestMap.put("spotifyL" + key, finalResourceBundle.getString(key)));
    }

}
