package com.sfccn.pdftool.webview;

import java.util.Random;

public class UserAgents {
	
	// <p>Chrome｜谷歌浏览器<br />
	public static String[] chrome = {

	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36"
	};
	
	// <p>Firefox｜火狐浏览器<br />
	public static String[] firefox = {
	"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0"
	};
	
	// Opera｜欧朋浏览器
	public static String[] opera = {
	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32"
	};
	
	// Safari｜苹果浏览器
	public static String[] safari = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2"
	};
	
	// 360极速浏览器
	public static String[] jixu360 = {
	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"
	};
	
	// 360安全浏览器
	public static String[] safe360 = {
	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"
	};
	
	// <p>微软 Edge 浏览器<br />
	public static String[] edge = {
	"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586"
	};
	
	//<p>Internet Explorer 11 浏览器<br />
	public static String[] ie = {
	"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko", //11
	"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)", //10
	"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",  //9
	"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)"   //8
	};
	
	
	//<p>百度浏览器<br />
	public static String[] baidu = {
	"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36"
	};
	
	//<p>遨游浏览器<br />
	public static String[] maxthon = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.9.2.1000 Chrome/39.0.2146.0 Safari/537.36"
	};
	
	//<p>QQ浏览器<br />
	public static String[] qq = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400"
	};
	
	//<p>UC浏览器电脑版<br />
	public static String[] uc = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36"
	};
	
	//<p>搜狗浏览器<br />
	public static String[] sogou = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0"
	};
	
	// <p>猎豹浏览器<br />
	public static String[] liebao = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.154 Safari/537.36 LBBROWSER"
	};
	
	//<p>世界之窗浏览器<br />
	public static String[] theworld = {
		"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7"
	};
	
	public static String getPcBrowser() {
		String[] browser = {
			chrome[0],firefox[0],opera[0],safari[0],jixu360[0],safe360[0],edge[0],ie[0],ie[1],ie[2],ie[3],qq[0],uc[0],sogou[0],liebao[0],theworld[0]
		};
		Random random = new Random();
		int index = random.nextInt(browser.length); 
		return browser[index];
	}
}
