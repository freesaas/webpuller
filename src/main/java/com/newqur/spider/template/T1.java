package com.newqur.spider.template;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newqur.spider.util.StringUtil;
import com.newqur.spider.util.net.UrlUtil;


public class T1 {
	public static void main(String[] args) throws IOException {
		String path = "/Users/apple/Desktop/%e6%92%92%e6%97%a6%20/null/";
		if(path.indexOf("%") > -1){
			System.out.println(StringUtil.urlToString(path));
		}
		
		
    }
}  