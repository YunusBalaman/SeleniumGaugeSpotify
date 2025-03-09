package com.spotify.web.methods.selenium;

import com.spotify.web.driver.Driver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.stream.IntStream;

public class ActionMethods {

    Keys controlKey = Driver.osName.equals("WINDOWS") ? Keys.CONTROL : Keys.COMMAND;
    WebDriver driver;

    public ActionMethods(){

        driver = Driver.webDriver;
    }
    
    public Actions getActions(){
        return new Actions(driver);
    }

    public void hoverElement(WebElement webElement) {

        getActions().moveToElement(webElement).build().perform();
    }

    public void sendKeys(WebElement webElement, String text) {


        getActions().sendKeys(webElement, text).build().perform();
    }

    public void clickElement(WebElement webElement){

        getActions().click(webElement).build().perform();
    }

    public void doubleClickElement(WebElement webElement){

        getActions().doubleClick(webElement).build().perform();
    }

    public void moveAndClickElement(WebElement webElement){

        getActions().moveToElement(webElement).click().build().perform();
    }

    public void moveAndDoubleClickElement(WebElement webElement){

        getActions().moveToElement(webElement).doubleClick().build().perform();
    }

    public void select(WebElement webElement, int optionIndex){

        Select select = new Select(webElement);
        getActions().keyDown(controlKey).click(select.getOptions().get(optionIndex)).keyUp(controlKey).build().perform();
    }

    public void swipeToElement(WebElement firstElement, WebElement secondElement){

        getActions().clickAndHold(firstElement).pause(Duration.ofMillis(1000)).moveToElement(secondElement).release().build().perform();
    }

    public void swipeToElement(WebElement firstElement, int x, int y){

        getActions().clickAndHold(firstElement).pause(Duration.ofMillis(1000)).moveByOffset(x,y).release().build().perform();
    }

    public void swipeToElement(WebElement firstElement, WebElement secondElement, int x, int y){

        getActions().clickAndHold(firstElement).pause(Duration.ofMillis(1000)).moveToElement(secondElement,x,y).release().build().perform();
    }

    public void selectItemByIndex(WebElement element, int index) {

        Actions actions = getActions();
        actions.moveToElement(element).click(element).pause(Duration.ofSeconds(1));
        IntStream.range(0, index).mapToObj(i -> Keys.DOWN).forEach(actions::sendKeys);
        actions.sendKeys(Keys.SPACE).build().perform();
    }

    public void selectDropdownItemByText(WebElement element, int index) {

        Actions actions = getActions();
        actions.moveToElement(element).click(element).pause(Duration.ofSeconds(1));
        IntStream.range(0, index).mapToObj(i -> Keys.DOWN).forEach(actions::sendKeys);
        actions.sendKeys(Keys.SPACE).build().perform();
    }

    public void keyDownUp(String keyName){

        getActions().keyDown(controlKey).sendKeys(keyName).keyUp(controlKey).perform();
    }

    public void sendKeysWithKey(String keyName){

        getActions().sendKeys(Keys.valueOf(keyName)).perform();
    }

    public void sendKeys(String text){

        getActions().sendKeys(text).perform();
    }

}