package com.newqur.spider.template.vo;

import com.newqur.spider.vo.BaseVO;

public class StringDiffVO extends BaseVO {
	private String duan[];		//剩余的，没有在chang中发现相同的字符串，这里的要继续进行二分判断

	
	public String[] getDuan() {
		return duan;
	}

	public void setDuan(String[] duan) {
		this.duan = duan;
	}


}
