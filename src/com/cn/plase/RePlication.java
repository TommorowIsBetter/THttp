package com.cn.plase;

import java.net.HttpURLConnection;
import java.net.URL;

public class RePlication {
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 3000;
    public static final int DEF_READ_TIMEOUT = 3000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	public static int put(String urlT) throws Exception {
	        HttpURLConnection conn = null;
	        URL url = new URL(urlT);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("User-agent", userAgent);
	        conn.setUseCaches(false);
	        conn.setConnectTimeout(DEF_CONN_TIMEOUT);
	        conn.setReadTimeout(DEF_READ_TIMEOUT);
	        conn.setInstanceFollowRedirects(false);
	        conn.connect();
	        int statusCode = conn.getResponseCode();
            return statusCode;
	        
	    }
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int statusCode = put("http://10.28.150.93:50070/webhdfs/v1/copy/testwangyan.txt?op=SETREPLICATION&replication=4");
		System.out.println(statusCode);
	}

}
