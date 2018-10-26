package com.cn.plase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author wangyan
 * @date 2018年10月26日
 * @Description 这个类的作用是实现对HDFS文件的删除，删除之后的返回码应该是200才是正确的。
 * @version 2018年10月26日
 */
public class DeleteFile {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL url = null;
		try {
		    url = new URL("http://10.28.150.93:50070/webhdfs/v1/copy/b.txt?op=DELETE");
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
