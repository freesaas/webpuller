package com.newqur.spider.util;

import com.newqur.spider.version.G;
import com.newqur.spider.version.VersionUtil;
import com.newqur.spider.version.VersionVO;

/**
 * 检查新版本
 * @author freesaas
 */
public class CheckVersion {
	
	/**
	 * 云端检测
	 * @return false：没有最新的，当前是最新版本
	 */
	public static boolean cloudCheck(){
		VersionVO vo = VersionUtil.cloudContrast("http://version.newqur.com/templatespider.html", G.VERSION);
		if(vo.isFindNewVersion()){
			if(UI.showConfirmDialog("发现新版本：v"+vo.getNewVersion()) == UI.CONFIRM_YES){
				SystemUtil.openUrl(vo.getPreviewUrl());
			}
			return true;
		}
		return false;
	}
	
}
