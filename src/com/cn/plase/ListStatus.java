package com.cn.plase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListStatus {
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 3000;
    public static final int DEF_READ_TIMEOUT = 3000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int statusCode = get("http://10.28.150.93:50070/webhdfs/v1/copy?op=LISTSTATUS");
		System.out.println(statusCode);

	}
	public static int get(String urlT) throws Exception {
        BufferedReader reader = null;
        String rs = null;
        HttpURLConnection conn = null;
        StringBuffer sb = new StringBuffer();
        URL url = new URL(urlT);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-agent", userAgent);
        conn.setUseCaches(false);
        conn.setConnectTimeout(DEF_CONN_TIMEOUT);
        conn.setReadTimeout(DEF_READ_TIMEOUT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        InputStream is = conn.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            sb.append(strRead);
        }
        rs = sb.toString();
        System.out.println(rs);
        return conn.getResponseCode();
    }

}
