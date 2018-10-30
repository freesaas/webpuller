package com.newqur.spider.template.vo;

import java.io.File;

import org.jsoup.nodes.Element;

import com.newqur.spider.vo.BaseVO;

/**
 * {@link Element} 的对比结果
 * @author freesaas
 *
 */
public class ElementDiffVO extends BaseVO {
	
	private double d;				//对比结果 0~1，越接近1，越相似。1是完全一样
	private Element diffElement;	//对比的元素，将此元素跟多个模板进行比较
	private Element targetElement;	//目标元素。多个模板中，其中一个目标的元素。
	private File targetFile;		//对比的目标模板文件
	
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public Element getDiffElement() {
		return diffElement;
	}
	public void setDiffElement(Element diffElement) {
		this.diffElement = diffElement;
	}
	public Element getTargetElement() {
		return targetElement;
	}
	public void setTargetElement(Element targetElement) {
		this.targetElement = targetElement;
	}
	public File getTargetFile() {
		return targetFile;
	}
	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}
	@Override
	public String toString() {
		return "ElementDiffVO [d=" + d + ", diffElement=" + diffElement
				+ ", targetElement=" + targetElement + ", targetFile="
				+ targetFile + "]";
	}
	
}
