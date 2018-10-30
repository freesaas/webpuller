package com.newqur.spider.template;

import com.newqur.spider.util.StringUtil;

public class TT {
	public static void main(String[] args) {
		String a = StringUtil.getDomainByUrl("http://www.chaqudao.com/index.php?u=3847");
		System.out.println(a);
	}
	
}
