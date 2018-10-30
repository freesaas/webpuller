package com.newqur.spider.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.newqur.spider.util.net.HttpsUtil.TrustAnyHostnameVerifier;
import com.newqur.spider.util.net.HttpsUtil.TrustAnyTrustManager;
import com.newqur.spider.util.net.UrlUtil;

/**
 * 文件操作
 * 
 * @author freesaas
 *
 */
public class FileUtil {

	/**
	 * InputStream转为文件并保存，为jar包内的资源导出而写
	 * 
	 * <pre>
	 * FileUtil.inputStreamToFile(getClass().getResourceAsStream("dm.dll"), "C:\\dm.dll");
	 * </pre>
	 * 
	 * @param inputStream
	 *            输入流
	 * @param targetFilePath
	 *            要保存的文件路径
	 */
	public static void inputStreamToFile(InputStream inputStream, String targetFilePath) {
		File file = new File(targetFilePath);
		OutputStream os = null;

		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * <pre>
	 * copyFile("E:\\a.txt", "E:\\aa.txt");
	 * </pre>
	 * 
	 * @param sourceFile
	 *            源文件，要复制的文件所在路径
	 * @param targetFile
	 *            复制到那个地方
	 */
	public static void copyFile(String sourceFile, String targetFile) {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (inBuff != null)
				try {
					inBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (outBuff != null)
				try {
					outBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 删除单个文件，java操作
	 * 
	 * @param fileName
	 *            文件名，包含路径。如E:\\a\\b.txt
	 * @return boolean true：删除成功
	 */
	public static boolean deleteFile(String fileName) {
		boolean newqurResult = false;

		java.io.File f = new java.io.File(fileName);
		if (f.isFile() && f.exists()) {
			f.delete();
			newqurResult = true;
		}

		return newqurResult;
	}

	/**
	 * 传入绝对路径，判断该文件是否存在
	 * 
	 * @param filePath
	 *            文件的绝对路径，如 "C:\\WINDOWS\\system32\\msvcr100.dll"
	 * @return Boolean true:存在
	 */
	public static boolean exists(String filePath) {
		java.io.File f = new java.io.File(filePath);
		return f.exists();
	}

	/**
	 * 通过网址获得文件长度
	 * 
	 * @param url
	 *            文件的链接地址
	 * @return 文件长度(Hander里的Content-Length)
	 *         <li>失败返回-1
	 */
	public static long getFileSize(String url) {
		int nFileLength = -1;
		try {
			URL newqurUrl = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection) newqurUrl.openConnection();
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");

			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				System.err.println("Error Code : " + responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection.getHeaderField(sHeader));
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nFileLength;
	}

	/**
	 * 从互联网下载文件。适用于http、https协议
	 * <li>下载过程会阻塞当前线程
	 * <li>若文件存在，会先删除存在的文件，再下载
	 * 
	 * @param downUrl
	 *            下载的目标文件网址 如 "http://www.xnx3.com/down/java/j2se_util.zip"
	 * @param savePath
	 *            下载的文件保存路径。如 "C:\\test\\j2se_util.zip"
	 * @return 返回下载出现的异常
	 *         <li>若返回null，则为下载成功，下载完毕，没有出现异常
	 *         <li>若返回具体字符串，则出现了异常，被try捕获到了，返回e.getMessage()异常信息
	 * @throws IOException
	 */
	public static void downloadFile(String downUrl, String savePath) throws IOException {
		// 判断文件是否已存在，若存在，则先删除
		if (exists(savePath)) {
			FileUtil.deleteFile(savePath);
		}

		int nStartPos = 0;
		int nRead = 0;

		URL url = new URL(downUrl);
		if (downUrl.indexOf("http://") > -1) {
			// 打开连接
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			// 获得文件长度
			long nEndPos = getFileSize(downUrl);

			RandomAccessFile oSavedFile = new RandomAccessFile(savePath, "rw");
			httpConnection.setRequestProperty("User-Agent", "Internet Explorer");
			String sProperty = "bytes=" + nStartPos + "-";
			// 告诉服务器book.rar这个文件从nStartPos字节开始传
			httpConnection.setRequestProperty("RANGE", sProperty);
			InputStream input = httpConnection.getInputStream();
			if (nEndPos == -1) {
				// 没有取得长度字节数，那么就直接将其保存就好了
				oSavedFile.write(inputstreamToByte(input));
			} else {
				byte[] b = new byte[1024];
				// 读取网络文件,写入指定的文件中
				while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
					oSavedFile.write(b, 0, nRead);
					nStartPos += nRead;
				}
			}

			httpConnection.disconnect();
			oSavedFile.close();
		} else if (downUrl.indexOf("https://") > -1) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

			// 文件的长度
			String contentLength = conn.getHeaderField("Content-Length");
			int nEndPos = Integer.parseInt(contentLength != null ? contentLength : "0");

			RandomAccessFile oSavedFile = new RandomAccessFile(savePath, "rw");
			// conn.setRequestProperty("User-Agent", "Internet Explorer");
			String sProperty = "bytes=" + nStartPos + "-";
			// 告诉服务器book.rar这个文件从nStartPos字节开始传
			// conn.setRequestProperty("RANGE", sProperty);
			InputStream input = conn.getInputStream();

			if (nEndPos == -1) {
				// 没有取得长度字节数，那么就直接将其保存就好了
				oSavedFile.write(inputstreamToByte(input));
			} else {
				byte[] b = new byte[1024];
				// 读取网络文件,写入指定的文件中
				while ((nRead = input.read(b, 0, 1024)) > 0 && nStartPos < nEndPos) {
					oSavedFile.write(b, 0, nRead);
					nStartPos += nRead;
				}
			}

			conn.disconnect();
			oSavedFile.close();
		}

	}

	/**
	 * 将 {@link BufferedReader} 转换为 {@link String}
	 * 
	 * @param br
	 *            {@link BufferedReader}
	 * @return String 若失败，返回 ""
	 */
	public static String BufferedReaderToString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将 {@link InputStream} 转化为 byte[]
	 * 
	 * @throws IOException
	 */
	public static byte[] inputstreamToByte(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	public static void main(String[] args) throws IOException {
		String url = "http://www.szkjcyy.com/uploadfiles//4.17 我院首次顺利开展心电监护下拔牙.jpg";
		String domainName = UrlUtil.getDomain(url);
		String path = url.substring(url.indexOf(domainName) + domainName.length());
		System.out.println(domainName);
		System.out.println(path);
		String encode = URLEncoder.encode(path, "utf-8");
		System.out.println(encode);
		String lasturl = url.substring(0, url.indexOf(domainName) + domainName.length());
		System.out.println(lasturl);
		downloadFile(lasturl + encode, "D:/test/aaa.jpg");

		downloadFile("http://www.szkjcyy.com/uploadfiles//4.17%20我院首次顺利开展心电监护下拔牙.jpg", "D:/test/aaa.jpg");
	}
}
