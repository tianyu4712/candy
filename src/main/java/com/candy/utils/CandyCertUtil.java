package com.candy.utils;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class CandyCertUtil implements X509TrustManager {
	private X509TrustManager utils ;

	public static HttpsURLConnection getUrlConnection(String strUrl)
			 {
		try{
		 // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = { new CandyCertUtil() };
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        // 创建URL对象
        URL myURL = new URL(strUrl);
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
        httpsConn.setSSLSocketFactory(ssf);

		return httpsConn;


		}catch(Exception e){
			e.printStackTrace();
		}
		return null;


	}

	private static File getSystemKeystoreFile()
	{
		char sep = File.separatorChar;
		File dir = new File(System.getProperty("java.home") + sep + "lib" + sep + "security");
		File file = new File(dir, "jssecacerts");
		if (file.isFile() == false) {
			file = new File(dir, "cacerts");
		}
		return file;
	}

	private CandyCertUtil() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(getSystemKeystoreFile()),
           null);
        TrustManagerFactory tmf =
        TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        tmf.init(ks);
        TrustManager tms [] = tmf.getTrustManagers();
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                utils = (X509TrustManager) tms[i];
                return;
            }
        }
        throw new Exception("Couldn't initialize");
    }

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		try {
			utils.checkClientTrusted(arg0, arg1);
        } catch (CertificateException excep) {
        }
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		try {
            utils.checkServerTrusted(arg0, arg1);
        } catch (CertificateException excep) {
        }
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		 return utils.getAcceptedIssuers();
	}


}
