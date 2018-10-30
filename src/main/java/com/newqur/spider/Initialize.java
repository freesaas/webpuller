package com.newqur.spider;

import java.io.File;

/**
 * 初始化
 * @author freesaas
 */
public class Initialize {
	/**
	 * 初始化判断，创建缓存文件
	 */
	public static void createCacheFile(){
		String path = Global.getLocalTemplatePath();
		createCacheFileByPath(path);
		createCacheFileByPath(path+"images/");
		createCacheFileByPath(path+"js/");
		createCacheFileByPath(path+"css/");
	}
	public static void createCacheFileByPath(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
	}
}
