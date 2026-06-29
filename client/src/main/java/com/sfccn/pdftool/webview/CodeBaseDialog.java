package com.sfccn.pdftool.webview;

import com.sfccn.pdftool.ui.LineNumberView;
import com.sfccn.pdftool.util.GB;
import com.sfccn.pdftool.utils.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cef.browser.CefBrowser;
import org.openqa.selenium.WebDriver;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeBaseDialog extends JFrame {
    public WebDriver driver = null;
    public CefBrowser browser = null;
    public final Logger log = LogManager.getLogger(CodeBaseDialog.class);
    private JTextField timeTextField = null;
    private JTextField fileTextField = null;
    private JFileChooser fileChooser;
    private JTextArea codeText = null;
    private JButton loopButton = null;
    private JButton stoplButton = null;
    private JButton runButton = null;
    private String loopKey = "loop_";
    private Map<String, Boolean> loopStatus = new HashMap<>();
    public CodeBaseDialog(){
        String os = System.getProperty("os.name").toLowerCase();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try{
                if(browser != null){
                    browser.doClose();
                }

            }
            catch(Exception e){
                log.error("ShutdownHook info:",e);
            }
        }));
        this.setTitle(getAppTitle());
        initPanel();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * 浏览器缓存路径
     * @return
     */
    protected abstract String getTempPath();

    /**
     * 默认首页
     * @return
     */
    protected abstract String getBaseUrl();

    /**
     * 应用标题
     * @return
     */
    protected abstract String getAppTitle();

    /**
     * 执行代码
     * @return
     */
    protected abstract String getCodeFile();

    private void initPanel(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());

        initScriptPanel(contentPane, 0, 0, 3, 1);


        // 代码框
        codeText = new JTextArea(20,20);
//        codeText.setText(readCode());
        JScrollPane codeTextScrollPane = new JScrollPane(codeText);
        codeTextScrollPane.setRowHeaderView(new LineNumberView());
        contentPane.add(codeTextScrollPane, new GB(0,1,3,1).setInsets(10, 10,0,10).setFill(GB.BOTH).setWeight(100, 100));

        JPanel loopPanel = new JPanel();
        loopPanel.setLayout(new GridBagLayout());

        initTimePanel(loopPanel, 1, 0);

        // 循环运行运行
        initLoopBtn(loopPanel, 2, 0);

        // 停止循环
        initStopLoopBtn(loopPanel, 3, 0);

        // 运行一次
        initRunOnceBtn(loopPanel, 4, 0);

        loopPanel.add(new JPanel(), new GB(5,0,1,1).setInsets(10, 0,10,10).setFill(GB.BOTH).setWeight(100, 0));

        contentPane.add(loopPanel, new GB(0,2,2,1).setInsets(10, 10,10,10).setFill(GB.BOTH).setWeight(100, 0));

        initSaveCodeBtn(contentPane, 2, 2);

    }

    private void initScriptPanel(Container contentPane, int gridx, int gridy, int width, int height){
        JPanel panel = new JPanel(new GridBagLayout());
        var label = new JLabel("脚本文件:");
        panel.add(label, new GB(0, 0, 1, 1));

        fileTextField = new JTextField();
        panel.add(fileTextField, new GB(1, 0, 1, 1).setFill(GB.BOTH)
                .setWeight(100, 0).setInsets(0,0,0,0));
        JButton openBtn = new JButton("浏览");
        openBtn.addActionListener(l->{
            JFileChooser fileChooser = getFileChooser();
            fileChooser.showOpenDialog(null);
            File f = fileChooser.getSelectedFile();
            if(f != null){
                String code = readCode(f);
                codeText.setText(code);
                fileTextField.setText(f.getAbsolutePath());
            }
        });
        panel.add(openBtn, new GB(2, 0, 1, 1).setInsets(0,10,0,0));
        contentPane.add(panel, new GB(gridx, gridy, width, height).setWeight(100, 0).setFill(GB.BOTH)
                            .setInsets(10,10, 10, 10));
    }
    public JFileChooser getFileChooser(){
        if(fileChooser == null){
            File defaultFile = new File("doc/script");
            if(defaultFile.exists() == false){
                defaultFile = new File(".");
            }
            fileChooser = new JFileChooser(defaultFile);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "采集脚本(*.js)", "js");
            fileChooser.setFileFilter(filter);
        }
        return fileChooser;
    }
    private void test(){
        StringBuffer html = new StringBuffer();
        browser.getSource(s -> html.append(s));
        driver.getPageSource();
        driver.getTitle();
//        WeakReference
    }

    public void runcode(String code){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try{
            engine.put("log", log);
            engine.put("browser", this.browser);
            engine.put("driver", this.driver);
            engine.put("tool", CodeBaseDialog.this);
            engine.put("DateUtil", new DateUtil());
            // 执行这个脚本
            engine.eval(code);
        }
        catch(Exception ex){
            log.error("脚本执行出错======", ex);
        }
    }


    public void initTimePanel(JPanel loopPanel, int gridx, int gridy){
        JLabel timeLabel = new JLabel("循环间隔(分钟):");
        loopPanel.add(timeLabel, new GB(0,0,1,1).setInsets(10, 0,10,0));
        timeTextField = new JTextField(4);

        timeTextField.setDocument(new PlainDocument(){
            Pattern pattern = Pattern.compile("[0-9]{1,2}");
            public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
                Matcher matcher = pattern.matcher(var2);
                if(matcher.find()){
                    super.insertString(var1, var2, var3);
                }
                else{
                    Toolkit.getDefaultToolkit().beep();
                }
            }

        });
        timeTextField.setText(String.valueOf(60));
        loopPanel.add(timeTextField, new GB(gridx,gridy,1,1).setInsets(10, 0,10,10));

    }

    public void initLoopBtn(JPanel loopPanel, int gridx, int gridy){
        // 循环运行运行
        loopButton = new JButton("循环运行");
        loopButton.addActionListener(e -> {
            loopButton.setEnabled(false);
            stoplButton.setEnabled(true);
            String code = codeText.getText();
            //isLoopRun = true;

            new Thread(() -> {
                try{
                    loopKey = "loop_" + System.currentTimeMillis();
                    loopStatus.put(loopKey, true);
                    while(loopStatus.get(loopKey) == true){
                        log.info("isLoopRun run====" + loopStatus.get(loopKey));
                        runcode(code);
                        int time = Integer.valueOf(timeTextField.getText());
                        Thread.sleep(time * 60 * 1000);
                        log.info("isLoopRun state====" + loopStatus.get(loopKey));
                    }
                }
                catch(Exception e1){
                    log.error("循环运行错误，", e1);
                }
            }).start();
        });
        loopPanel.add(loopButton, new GB(gridx,gridy,1,1).setInsets(10, 0,10,10));

    }

    public void initStopLoopBtn(JPanel loopPanel, int gridx, int gridy){
        // 停止循环
        stoplButton = new JButton("停止循环");
        stoplButton.addActionListener(e -> {
            loopButton.setEnabled(true);
            stoplButton.setEnabled(false);
            loopStatus.put(loopKey, false);
            log.info("isLoopRun will stop====" + loopStatus.get(loopKey));
        });
        loopPanel.add(stoplButton, new GB(gridx,gridy,1,1).setInsets(10, 0,10,10));

    }

    public void initRunOnceBtn(Container contentPane, int gridx, int gridy){
        runButton = new JButton("运行一次");
        runButton.addActionListener(e -> {
            String code = codeText.getText();
            runButton.setEnabled(false);
            new Thread(() -> {
                runcode(code);
                runButton.setEnabled(true);
            }).start();
        });
        contentPane.add(runButton, new GB(gridx,gridy,1,1).setInsets(10, 0,10,10));
    }

    public void initSaveCodeBtn(Container contentPane, int gridx, int gridy){
        JButton runButton = new JButton("保存脚本");
        runButton.addActionListener(new  ActionListener(){
            public void actionPerformed(ActionEvent e){
                //String code = codeText.getText();
                saveCode();
            }
        });
        contentPane.add(runButton, new GB(gridx,gridy,1,1).setInsets(10, 0,10,10));
    }

    private void saveCode(){
        String file = fileTextField.getText();
        String code = codeText.getText();
        try{
            FileUtils.write(new File(file), code, "UTF-8");
        }
        catch(Exception e){
            log.error("保存脚本出错：" + e.getMessage());
        }

    }
    private String readCode(File file){
        String code = "";
        try{
            code = FileUtils.readFileToString(file, "UTF-8");
        }
        catch(Exception e){
            log.error("读取脚本出错：" + e.getMessage());
        }
        return code;
    }


    /**
     * 暂停多少秒
     * @param second
     */
    public void sleep(int second){
        try{
            Thread.sleep(second * 1000);
        }
        catch(Exception e){
            log.error("sleep error====",e);
        }
    }

}
