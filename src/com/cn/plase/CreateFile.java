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
import java.util.Properties;

	/**
	 * 
	 * @author wangyan
	 * @date 2018年10月26日
	 * @Description 这个类的作用是实现文件上传到HDFS文件系统里面去，根据WebHDFS的REST API接口要求。实现上传需要两步，第一步是不带文件的PUT提交操作，正常
	 * 的返回值是307，第二步操作是带文件的PUT操作，上传创建成功返回值为201。如果文件已经存在，继续上传操作的话，会报403的错误。
	 * @version 1.1
	 */
public class CreateFile {
	private static String URL1;
	private static String URL2;
	private static String SRC;
	static {
		try{
			Properties props = new Properties();
			props.load(CreateFile.class.getClassLoader().getResourceAsStream("createfile.properties"));
			URL1 = props.getProperty("url1");
			URL2 = props.getProperty("url2");
			SRC  = props.getProperty("src");
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 3000;
    public static final int DEF_READ_TIMEOUT = 3000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	
    private static final String nextLine = "\r\n"; 
    private static final String twoHyphens = "--"; 
    private static final String boundary = "wk_file_2519775"; 
  /**
   * 
   * @param urlPath 要访问的ulr连接。
   * @param method  方法是HTTP方法中其中之一，例如GET,POST,PUT等操作。
   * @return HttpURLConnection  返回一个连接
   * @throws IOException
   * @Description 这个方法的作用，是为uploadFile()方法进行调用。
   */
    private static HttpURLConnection createConnection(String urlPath, String method) 
    		throws IOException 
    { 
    	URL url = new URL(urlPath); 
    	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); 
    	httpURLConnection.setRequestMethod(method); 
    	httpURLConnection.setRequestProperty("Charsert", "UTF-8"); 
    	return httpURLConnection; 
    }

	/**
	 * 
	 * @param file 要上传的文件
	 * @param url  要上传的连接
	 * @Description 这个方法的作用是输入参数，实现文件的上传。此方法应该在第二步，因为WebHDFS的要求第二步可以
	 * 带文件PUT操作。
	 */
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
    
    
    /**
     * 
     * @param urlT 第一步操作，实现URL的连接操作，此时不需要带文件，正常的返回值应该是307才正确。
     * @return int 
     * @throws Exception
     * @Description 用于WebHDFS文件上传的第一步操作。
     */
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
    /**
     * 
     * @param args
     * @throws Exception
     * @Description 对写好的方法进行调用。
     */
    public static void main(String[] args) throws Exception {
    	int statuscode = get(URL1);
		System.out.println("第一次返回码："+statuscode);//此时的返回码应该是307才是正常的
		File file = new File(SRC);
		/**
		 * 第二步上传操作，返回值应该是201才是正确的
		 */
		uploadFile(file,URL2);
    }
}

