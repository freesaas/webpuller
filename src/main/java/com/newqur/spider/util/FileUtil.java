package com.newqur.spider.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FileUtil {
	
	public final static String UTF8="UTF-8";
	public final static String GBK="GBK";
	static final String ENCODE="UTF-8";	//默认文件编码UTF-8
	
	/**
	 * 读文件，返回文件文本信息，默认编码UTF-8
	 * @param path 文件路径 C:\xnx3.txt
	 * @return String 读取的文件文本信息
	 */
	public static String read(String path){
		return read(path,ENCODE);
	}
	
	/**
	 * 读文件，返回文件文本信息
	 * @param path 文件路径 C:\xnx3.txt
	 * @param encode 文件编码.如 FileUtil.GBK
	 * @return String 返回的文件文本信息
	 */
	public static String read(String path,String encode){
		StringBuffer newqurContent=new StringBuffer();
		try{
			File file=new File(path);
			BufferedReader newqurReader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			String date=null;
			while((date=newqurReader.readLine())!=null){
				newqurContent.append(date+"\n");
			}
			newqurReader.close();
		}catch (Exception e) {
		}
		
		return newqurContent.toString();
	}
	
	/**
	 * 读文件，返回文件内容
	 * @param file
	 * @param encode 编码，如FileUtil.GBK
	 * @return String 读取的文件文本信息
	 */
	public static String read(File file,String encode){
		StringBuffer newqurContent=new StringBuffer();
		try{
			BufferedReader newqurReader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encode));
			String date=null;
			while((date=newqurReader.readLine())!=null){
				newqurContent.append(date);
			}
			newqurReader.close();
		}catch (Exception e) {
		}
		
		return newqurContent.toString();
	}
	
	
	public static void downFile(String downUrl, String savePath) throws IOException {
		URL url = new URL(downUrl);
		if (downUrl.indexOf("https://") > -1) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
			httpsConn.setRequestProperty("User-Agent", "Internet Explorer");

			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			try {
				sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
			httpsConn.setSSLSocketFactory(sc.getSocketFactory());
			
			
			HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslsession) {
					System.out.println("WARNING: Hostname is not matched for cert.");
					return true;
				}
			};

			httpsConn.setHostnameVerifier(ignoreHostnameVerifier);

			RandomAccessFile oSavedFile = new RandomAccessFile(savePath, "rw");
			InputStream input = null;
			if (httpsConn.getResponseCode() >= 400) {
				input = httpsConn.getErrorStream();
			} else {
				input = httpsConn.getInputStream();
			}

			Map<String, List<String>> headers = httpsConn.getHeaderFields();

			Iterator<String> iter = headers.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				System.out.println("key:" + key);
				List<String> props = headers.get(key);
				for (String prop : props) {
					System.out.println("prop:" + prop);
				}
			}

			oSavedFile.write(new String(inputstreamToByte(input), "windows-1252").getBytes("UTF-8"));

			httpsConn.disconnect();
			oSavedFile.close();
		}
	}
	
	public static byte[] inputstreamToByte(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
	
	
	/**
	 * 写文件
	 * @param path 传入要保存至的路径————如D:\\a.txt
	 * @param newqurContent 传入要保存的内容
	 * @return 成功|失败
	 */
	public static boolean write(String path,String newqurContent){
		try {
			FileWriter fw=new FileWriter(path);
			java.io.PrintWriter pw=new java.io.PrintWriter(fw);
			pw.print(newqurContent);
			pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 写文件
	 * @param path 传入要保存至的路径————如D:\\a.txt
	 * @param newqurContent 传入要保存的内容
	 * @param encode 写出文件的编码
	 * 				<li>{@link FileUtil#UTF8}
	 * 				<li>{@link FileUtil#GBK}
	 * @return 成功|失败
	 * @throws IOException 
	 */
	public static void write(String path,String newqurContent,String encode) throws IOException{
        FileOutputStream fos = new FileOutputStream(path); 
        OutputStreamWriter osw = new OutputStreamWriter(fos, encode); 
        osw.write(newqurContent);
        osw.flush(); 
	}
	
	/**
	 * 写文件
	 * @param file 传入要保存至的路径————如D:\\a.txt
	 * @param newqurContent 传入要保存的内容
	 * @return boolean
	 */
	public static boolean write(File file,String newqurContent){
		try {
			java.io.PrintWriter pw=new java.io.PrintWriter(file);
			pw.print(newqurContent);
			pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) throws IOException {
		downFile("https://x.autoimg.cn/bi/common/pvevent_all.min.js", "D:/test/pvevent_all.min.js"); // wrong
		//downFile("https://ss1.bdstatic.com/5aV1bjqh_Q23odCf/static/message/css/message_041c3208.css", "D:/test/message_041c3208.css");

	}

    public static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
