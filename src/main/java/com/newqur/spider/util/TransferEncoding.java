package com.newqur.spider.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class TransferEncoding {
	private static String encoding = "UTF-8";
	
	public static void main(String[] args) {
		try {
			getForGet("https://www.jianyu360.com/js/jquery.cookie.js");
			//getForGet("https://www.jianyu360.com/js/jyWebScoket.js");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void getForGet(String downUrl) throws IOException {
		URL url = new URL(downUrl);
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
			sslcontext.init(null, new TrustManager[] { new X509TrustUtiil() }, new java.security.SecureRandom());
		} catch (Exception e) {
			throw new IOException(e);
		}
		HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
			public boolean verify(String s, SSLSession sslsession) {
				System.out.println("WARNING: Hostname is not matched for cert.");
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent", "Internet Explorer");
		conn.setConnectTimeout(6000);
		conn.setReadTimeout(6000);
		int code = conn.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK) {
			throw new IOException("文件读取失败:" + code);
		}
//		if (conn.getHeaderField("Content-Length") != null) {
//			inputStreamToFile(conn.getInputStream(), savePath);
//		} else if (conn.getHeaderField("Transfer-Encoding") != null) {
//			
//		}
		
		
		
		
		boolean isGzip = false;
        // 读取所有服务器发送过来的请求参数头部信息  
        String contentEncoding = conn.getHeaderField("Content-Encoding");
        System.out.println(contentEncoding);
        if (contentEncoding.startsWith("gzip")) {
    		isGzip = true;
    	}
        String sContLength = conn.getHeaderField("Content-Length");
        int coentLength = isBlank(sContLength) ? 0 : Integer.parseInt(sContLength);
    	
        String body;
        if (isGzip) {
        	body = readGzipBodyToString(conn.getInputStream());
        } else {
        	body = readBody(conn.getInputStream(), coentLength);
        }
        
        //--输消息的体  
        System.out.print(body);  

		
	}
	
	/**
	 * 读取压缩的消息体，并且转换为字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String readGzipBodyToString(InputStream is) throws IOException {
		// 读取压缩主体
        List<Byte> bodyByteList = readGzipBody(is);
        // 把读取出来的Byte List转换为数组
		byte[] tmpByteArr = new byte[bodyByteList.size()];  
        for (int i = 0; i < bodyByteList.size(); i++) {  
            tmpByteArr[i] = ((Byte) bodyByteList.get(i)).byteValue();  
        }  
        bodyByteList.clear();  // 释放内存
        // 构建GZIPInputStream对象，准备从这里读取数据
        ByteArrayInputStream bais = new ByteArrayInputStream(tmpByteArr);
		GZIPInputStream gzis = new GZIPInputStream(bais);

        StringBuffer body = new StringBuffer();
    	InputStreamReader reader = new InputStreamReader(gzis, encoding);
    	BufferedReader bin = new BufferedReader(reader);
        String str = null;
        // 开始读取
        while((str = bin.readLine()) != null) {
        	body.append(str).append("\r\n");
        }
        // 读取完成，关闭流
        bin.close();
		return body.toString();
	}

	/**
	 * 获取没有压缩的消息体
	 * 
	 * @param is
	 * @param contentLe
	 * @return
	 */
	private static String readBody(InputStream is, int contentLe) {
		List<Byte> lineByteList = new ArrayList<Byte>();  
        byte readByte;  
        int total = 0; 
        try {
			do {  
				readByte = (byte) is.read();
	            lineByteList.add(Byte.valueOf(readByte));  
	            total++;  
	        } while (total < contentLe);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
        byte[] tmpByteArr = new byte[lineByteList.size()];  
        for (int i = 0; i < lineByteList.size(); i++) {  
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();  
        }  
        lineByteList.clear();  
  
        String line = "";
		try {
			line = new String(tmpByteArr, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return line;
	}

	/**
	 * 读取一行数据
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String readLine(InputStream is) throws IOException {  
        List<Byte> lineByteList = new ArrayList<Byte>();  
        byte readByte;
        
        do { 
            readByte = (byte) is.read();  
            lineByteList.add(Byte.valueOf(readByte));  
            
        } while (readByte != 10);// 读取到最后一个"\n"换行的字符
        
        byte[] tmpByteArr = new byte[lineByteList.size()];  
        for (int i = 0; i < lineByteList.size(); i++) {  
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();  
        }  
        lineByteList.clear();  
        String line = new String(tmpByteArr, encoding);
        return line;
    } 
	
	
	private static boolean isNotBlank(String content) {
		if (content != null && content.trim().length() > 0)
			return true;
		return false;
	}

	/**
	 * 读取gzip压缩的消息体
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
    private static List<Byte> readGzipBody(InputStream is) throws IOException {
    	// 压缩块的大小，由于chunked编码块的前面是一个标识压缩块大小的16进制字符串，在开始读取前，需要获取这个大小
    	int chunk = getChunkSize(is);
    	List<Byte> bodyByteList = new ArrayList<Byte>();
        byte readByte = 0;
        int count = 0;
        
        while (count < chunk) {  // 读取消息体，最多读取chunk个byte
            readByte = (byte) is.read();  
            bodyByteList.add(Byte.valueOf(readByte));
            count ++;
        }
        if (chunk > 0) { // chunk为读取到最后，如果没有读取到最后，那么接着往下读取。
        	List<Byte> tmpList = readGzipBody(is);
        	bodyByteList.addAll(tmpList);
        }
        return bodyByteList;
    }
	
    /**
     * 获取压缩包块的大小
     * 
     * @param is
     * @return
     * @throws IOException
     */
	private static int getChunkSize(InputStream is) throws IOException {
		String sLength = readLine(is).trim();
		if (isBlank(sLength)) {  // 字符串前面有可能会是一个回车换行。
			// 读了一个空行，继续往下读取一行。
			sLength = readLine(is).trim();
		}
        if (sLength.length() < 4) {
        	sLength = 0 + sLength;
        }
        // 把16进制字符串转化为Int类型
        int length = Integer.valueOf(sLength, 16);
        return length;
	}

	private static boolean isBlank(String sLength) {
		if (sLength == null)
			return true;
		if (sLength.trim().length() == 0)
			return true;
		return false;
	}
}
