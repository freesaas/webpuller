package com.newqur.spider.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class X509TrustUtiil implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
