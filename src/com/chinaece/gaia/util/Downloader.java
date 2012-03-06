package com.chinaece.gaia.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.chinaece.gaia.gui.FilesActivity;

public class Downloader implements Runnable{
	private String urlStr;
	
	private String path = "ece";
	
	private String fileName;
	
	private Context context;
	
	@SuppressWarnings("unused")
	private Downloader() {
	}
	
	public Downloader(Context context, String urlStr, String fileName ,String extPath) {
		this.context = context;
		this.urlStr = urlStr;
		this.fileName = fileName;
		if(extPath != null)
			this.path += "/"+extPath;
	}
	
	@Override
	public void run() {
        OutputStream output=null;   
        try {
            URL url=new URL(urlStr);   
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();   
            String SDCard=Environment.getExternalStorageDirectory().getPath();   
            String pathName=SDCard+"/"+path+"/"+fileName;   
            File file=new File(pathName);   
            InputStream input=conn.getInputStream();   
           if(file.exists()){   
                Intent mintent = FileUtil.openFile(file);
      		   	NotificationCenter.sendNormalNotification(mintent, context, "文件已存在请点击查看","文件下载", "文件已存在请点击查看");
                return;
           }else{   
               File dir = new File(SDCard+"/"+path);
               if(!dir.exists())
            	   dir.mkdirs();  
               file.createNewFile(); 
               output=new FileOutputStream(file);  
               byte[] buffer=new byte[1024];
               int length;
               while((length = input.read(buffer))!=-1){
                   output.write(buffer, 0 ,length); 
                }
               output.flush(); 
               Intent mintent = FileUtil.openFile(file);
     		   NotificationCenter.sendNormalNotification(mintent, context,  "下载完成点击附件管理查看详细内容","文件下载", "下载完成点击附件管理查看详细内容");
            }   
       } catch (MalformedURLException e) {   
           e.printStackTrace();   
        } catch (IOException e) { 
        	Intent mintent = new Intent(context,FilesActivity.class);
 		   NotificationCenter.sendNormalNotification(mintent, context, fileName+"下载失败","附件下载", fileName+"下载失败");
            e.printStackTrace();   
       }
	}

}
