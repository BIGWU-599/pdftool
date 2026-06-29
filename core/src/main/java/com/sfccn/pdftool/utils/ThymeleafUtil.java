package com.sfccn.pdftool.utils;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class ThymeleafUtil {
    private TemplateEngine templateEngine;
    private static ThymeleafUtil instant = null;
    public ThymeleafUtil(){
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("data/thymeleaf/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();

        templateEngine.setTemplateResolver(resolver);
    }

    public void run1(){
        try{
            //构造模板引擎
//            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            FileTemplateResolver resolver = new FileTemplateResolver();
            resolver.setPrefix("data/thymeleaf/");//模板所在目录，相对于当前classloader的classpath。
            resolver.setSuffix(".html");//模板文件后缀
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            //构造上下文(Model)
            Context context = new Context();
            context.setVariable("name", "蔬菜列表");
            context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});

            //渲染模板
            FileWriter write = new FileWriter("data/thymeleaf/example2.html");
            templateEngine.process("example", context, write);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public void run2(){
        try{
            TemplateEngine engine = new TemplateEngine();
            FileWriter writer = new FileWriter("data\\thymeleaf\\example3.html");
            Context context = new Context();
            context.setVariable("name", "蔬菜列表");
            context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});

            String s = FileUtils.readFileToString(new File("data\\thymeleaf\\example.html"), "UTF-8");
            TemplateSpec spec = new TemplateSpec(s, "HTML");
            engine.process(spec, context,writer);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static ThymeleafUtil getInstance(){
        if(instant == null){
            instant = new ThymeleafUtil();
        }
        return instant;
    }
    public String process(Map<String, Object> map, String temple){
        Context context = new Context();
        context.setVariables(map);
        String html = templateEngine.process(temple, context);
        return html;
    }
}
