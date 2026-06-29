package com.sfccn.pdftool.webview.selenium;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CefWebElement implements WebElement {
    private Element element = null;
    private WebDriver driver = null;
    public CefWebElement(Element element, WebDriver driver){
        this.element = element;
        this.driver = driver;
    }

    @Override
    public void click() {

    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... charSequences) {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getTagName() {
        return null;
    }

    @Override
    public String getAttribute(String attributeKey) {
        if(attributeKey.equalsIgnoreCase("href") || attributeKey.equalsIgnoreCase("src")){
            String href = element.attr(attributeKey);
            String url = driver.getCurrentUrl();
            if(href != null && href.startsWith("http") == false){
                href = getAbsUrl(url, href);
            }

            return href;
        }
        else if(attributeKey.equalsIgnoreCase("innerhtml")){ //
            return element.html();
        }
        else if(attributeKey.equalsIgnoreCase("outerhtml")){ //
            return element.outerHtml();
        }
        else{
            return element.attr(attributeKey);
        }
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return element.text();
    }
    public List<WebElement> findElements(By by){
        String selector = by.toString().split(":")[1].trim();
        Elements elements = element.select(selector);
        List<WebElement> list = new ArrayList<>();
        for(Element e:elements){
            list.add(new CefWebElement(e, driver));
        }
        return list;
    }
    public WebElement findElement(By by){
        String selector = by.toString().split(":")[1].trim();
        Element e = element.selectFirst(selector);
        return new CefWebElement(e, driver);
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String s) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return null;
    }
    public String getAbsUrl(String absolutePath, String relativePath){
        try {
            URL absoluteUrl = new URL(absolutePath);
            URL parseUrl = new URL(absoluteUrl ,relativePath );
            return parseUrl.toString();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
