package com.sfccn.pdftool.webview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseClient {
    public final Logger log = LogManager.getLogger(BaseClient.class);
    /**
     * 返回chrome
     * @param proxy
     * @param headless
     * @return
     */
    public WebDriver getChrome(Proxy proxy, boolean headless) {
        return getChrome(proxy, headless, true);
    }

    /**
     * 返回chrome
     * @param proxy
     * @param headless
     * @return
     */
    public WebDriver getChrome(Proxy proxy, boolean headless, boolean isEnableJavaScript) {

        String os = System.getProperty("os.name").toLowerCase();
        // linux上运行
        if(os.indexOf("windows") == -1){
            System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver");
        }
        // window上运行，本机调试
        else{
            System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");

        }
        ChromeOptions options = new ChromeOptions();
        if(proxy != null) {
            options.setProxy(proxy);
        }
        options.setHeadless(headless);
        options.setAcceptInsecureCerts(true);
//        // 禁止加载图片
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.images", 2);
        prefs.put("profile.default_content_setting_values.plugins", 2);
        prefs.put("profile.default_content_setting_values.geolocation", 2);
        prefs.put("profile.default_content_setting_values.notifications", 2);

        if(isEnableJavaScript == false){
            prefs.put("profile.default_content_setting_values.javascript", 2);
        }

        options.setExperimentalOption("prefs", prefs);

        String user_agent = UserAgents.getPcBrowser();
        options.addArguments("user-agent=\"" + user_agent + "\"");

//		options.addArguments("applicationCacheEnabled=true");
//		options.addArguments("user-data-dir=C:\\Users\\adminp\\AppData\\Local\\Google\\Chrome\\User Data");
//		options.addArguments("user-data-dir=temp\\Chrome\\User Data");
        options.addArguments("window-size=1920x1080"); //分辨率

        ChromeDriver driver = new ChromeDriver(options);

        //driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        return driver;

    }

    public WebDriver getChrome(boolean headless){
        return getChrome(null, headless);
    }


    public Document getDoc(String url) {
//        WebPage webPage = new WebPage();
//        return webPage.getDoc(url);
        try{
            Document doc = Jsoup.connect(url)
//                    .data("query", "Java")
                    .userAgent(UserAgents.getPcBrowser())
//                    .cookie("auth", "token")
                    .timeout(60000)
                    .get();

            return doc;
        }
        catch(Throwable e){
            log.error("getJsoup error=========", e);
        }
        return null;
    }


}
