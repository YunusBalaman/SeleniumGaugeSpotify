package com.spotify.web.methods;

import com.spotify.web.methods.selenium.Methods;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonProcess {

    Methods methods;
    MethodsUtil methodsUtil;

    public CommonProcess(){

        methods = new Methods();
        methodsUtil = new MethodsUtil();
    }

    public void clickButton(By by, long milliSeconds){

        checkElementVisible(by);
        checkElementClickable(by);
        if (milliSeconds > 0)
            methodsUtil.waitByMilliSeconds(milliSeconds,false);
        methods.click(by);
    }

    public void clickButtonWithJs(By by, long timeout){

        checkElementVisible(by);
        checkElementClickable(by);
        methodsUtil.waitByMilliSeconds(timeout,false);
        methods.clickElementJs(by);
    }

    public void scrollElementCenter(By by){

        scrollElementCenter(by,300,500);
    }

    public void scrollElementCenter(By by, long timeBefore, long timeAfter){

        methodsUtil.waitByMilliSeconds(timeBefore,false);
        methods.scrollElementCenterJs(by,"1");
        methodsUtil.waitByMilliSeconds(timeAfter,false);
    }

    public void scrollElement(By by, long timeBefore, long timeAfter){

        methodsUtil.waitByMilliSeconds(timeBefore,false);
        methods.scrollElementJs(by,"1");
        methodsUtil.waitByMilliSeconds(timeAfter,false);
    }

    public By getKeyValueChangerElement(String element, String newElement, String value){

        methods.keyValueChangerMethodWithNewElement(element, newElement, value,"|!");
        return methods.getBy(newElement);
    }

    public void checkElementVisible(By by){

        assertTrue(methods.isElementVisible(by,30));
    }

    public void checkElementClickable(By by){

        assertTrue(methods.isElementClickable(by,30));
    }
}
