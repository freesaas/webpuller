package com.newqur.spider.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.newqur.spider.util.CheckVersion;
import com.newqur.spider.util.UI;
import com.newqur.spider.version.G;

/**
 * 菜单
 * @author freesaas
 */
public class Menu {
	
	public static JMenu aboutMenu(){
		JMenu aboutMenu = new JMenu("关于");
		
		JMenuItem mntmNewMenuItem = new JMenuItem("关于我们");
		aboutMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UI.showMessageDialog(""
						+ "\n当前版本：v"+G.VERSION
						+ "\n作者：freesaas"
						+ "\n邮箱：support@newqur.com"
						+ "\n官网：www.newqur.com"
						+ "\n微信公众号: freesaas"
						+ "\n开源发布：https://github.com/freesaas/webpuller");
			}
		});
		
		JMenuItem hezuoMenuItem = new JMenuItem("各种合作");
		aboutMenu.add(hezuoMenuItem);
		hezuoMenuItem.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
				UI.showMessageDialog(""
						+ "无论你是哪个行业，若我们对你有用，欢迎联系我们！只要对我方有好处，能推动我方前进，都有机会合作！");
			}
		});
		
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("检查更新");
		aboutMenu.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!CheckVersion.cloudCheck()){
					UI.showMessageDialog("当前已是最新版本！");
				}
			}
		});
		
		return aboutMenu;
	}
	
}
