package com.cn.plase;

import java.io.IOException;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.File;
import java.io.FileOutputStream;
/**
 * 
 * @author wangyan
 * @date 2018年10月26日
 * @Description 这个类的作用是实现对HDFS文件的下载操作
 * @version 2018年10月26日
 */
public class GetFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url="http://10.28.150.93:50070/webhdfs/v1/copy/a.iso?op=OPEN";
	    String token="v32Eo2Tw+qWI/eiKW3D8ye7l19mf1NngRLushO6CumLMHIO1aryun0/Y3N3YQCv/TqzaO/TFHw4=";
        GetFile.downLoadFromUrl(url,"b.iso","E:\\",token);
        System.out.println("下载完成");
	}
	/**
     * 从网络Url中下载文件
     * @param urlStr 要下载的URL的连接
     * @param fileName 下载之后重新定义的文件名字
     * @param savePath 下载之后的保存路径
     * @param toekn 与服务器进行连接的令牌
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath,String toekn) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("lfwywxqyh_token",toekn);
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
        System.out.println("返回码:"+conn.getResponseCode());
        System.out.println("info:"+url+" download success");

    }
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     * @description downLoadFromUrl()方法会调用这个方法，帮助实现下载操作。
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
