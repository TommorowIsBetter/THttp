package com.cn.plase;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CreateFile {
	
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 3000;
    public static final int DEF_READ_TIMEOUT = 3000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	
    private static final String nextLine = "\r\n"; 
    private static final String twoHyphens = "--"; 
    private static final String boundary = "wk_file_2519775"; 
    private static HttpURLConnection createConnection(String urlPath, String method) 
    		throws IOException 
    { 
    	URL url = new URL(urlPath); 
    	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); 
    	httpURLConnection.setRequestMethod(method); 
    	httpURLConnection.setRequestProperty("Charsert", "UTF-8"); 
    	return httpURLConnection; 
    }

	
    private static void uploadFile(File file, String url)
    { 
    	HttpURLConnection connection = null; 
    	OutputStream outputStream = null; 
    	FileInputStream inputStream = null; 
    	try 
    	{   //获取HTTPURLConnection连接 
    		connection = createConnection(url, "PUT"); 
    		//运行写入默认为false，置为true 
    		connection.setDoOutput(true); 
    		//禁用缓存 
    		connection.setUseCaches(false); 
    		//设置接收编码 
    		connection.setRequestProperty("Accept-Charset", "utf-8"); 
    		//开启长连接可以持续传输 
    		connection.setRequestProperty("Connection", "keep-alive"); 
    		//设置请求参数格式以及boundary分割线 
    		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); 
    		//设置接收返回值的格式 
    		connection.setRequestProperty("Accept", "application/json"); 
    		//开启连接 
    		connection.connect(); 
    		//获取http写入流 
    		outputStream = new DataOutputStream(connection.getOutputStream()); 
    		//分隔符头部 
    		String header = twoHyphens + boundary + nextLine; 
    		//分隔符参数设置 
    		header += "Content-Disposition: form-data;name=\"file\";" + "filename=\"" + file.getName() + "\"" + nextLine + nextLine; 
    		//写入输出流 
    		outputStream.write(header.getBytes());
    		//读取文件并写入
    		inputStream = new FileInputStream(file); 
    		byte[] bytes = new byte[1024]; 
    		int length; 
    		while ((length = inputStream.read(bytes))!= -1)
    		{ 
    			outputStream.write(bytes, 0, length); 
    		} 
    		//文件写入完成后加回车 
    		outputStream.write(nextLine.getBytes()); 
    		//写入结束分隔符 
    		String footer = nextLine + twoHyphens + boundary + twoHyphens + nextLine; 
    		outputStream.write(footer.getBytes()); 
    		outputStream.flush(); 
    		//文件上传完成 
    		InputStream response = connection.getInputStream(); 
    		InputStreamReader reader = new InputStreamReader(response); 
    		while (reader.read() != -1)
    		{ 
    			System.out.println(new String(bytes, "UTF-8")); 
    		} 
    		if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED)
    		 { 
    			System.out.println("第二次返回码："+connection.getResponseCode());
    			System.out.println(connection.getResponseMessage()); 
    		}else 
    		    { 
    			System.err.println("上传失败"+connection.getResponseCode()); 
    			} 
    		} catch (IOException e) 
    		{ 
    			e.printStackTrace(); 
    		}finally { 
    			try { 
    				if (outputStream != null)
    			       { 
    					outputStream.close(); 
    				   } 
    				if (inputStream != null)
    				   {  
    					inputStream.close(); 
    				   }
    				if (connection != null)
    				   { 
    					connection.disconnect(); 
    				   } 
    				} catch (IOException e) 
    				{ 
    					e.printStackTrace(); 
    				} 
    		} 
    	}
    
    

    	
    	
    public static int get(String urlT) throws Exception {
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
    	int statuscode = get("http://10.28.150.93:50070/webhdfs/v1/TestHttp?op=CREATE");
		System.out.println("第一次返回码："+statuscode);
		File file = new File("e://demo/testhttp.txt");
		uploadFile(file,"http://10.28.150.94:50075/webhdfs/v1/TestHttp/testhttp.txt?op=CREATE&namenoderpcaddress=10.28.150.93:9000");
        

    }

}

