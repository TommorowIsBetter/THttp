package com.cn.plase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @author wangyan
 * @date 2018年10月26日
 * @Description 这个类的作用是实现对HDFS文件的删除，删除之后的返回码应该是200才是正确的。
 * @version 2018年10月26日
 */
public class DeleteFile {
	private static String URL;
	static {
		try{
			Properties props = new Properties();
			props.load(DeleteFile.class.getClassLoader().getResourceAsStream("deletefile.properties"));
			URL = props.getProperty("url");
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL url = null;
		try {
		    url = new URL(URL);
		} catch (MalformedURLException exception) {
		    exception.printStackTrace();
		}
		HttpURLConnection httpURLConnection = null;
		try {
		    httpURLConnection = (HttpURLConnection) url.openConnection();
		    httpURLConnection.setRequestProperty("Content-Type",
		                "application/x-www-form-urlencoded");
		    httpURLConnection.setRequestMethod("DELETE");
		    System.out.println("返回码:"+httpURLConnection.getResponseCode());
		} catch (IOException exception) {
		    exception.printStackTrace();
		} finally {         
		    if (httpURLConnection != null) {
		        httpURLConnection.disconnect();
		    }
		}
	}

}
