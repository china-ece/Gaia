package com.chinaece.gaia.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class FileUtil {
	
	public static Intent openFile(final File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		try {
			intent.setDataAndType(Uri.fromFile(f), type);
		} catch (Exception e) {
			intent.setDataAndType(Uri.fromFile(f), "*/*");
		}
		return intent;
	}
	
	private static String getMIMEType(File f) {
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
		if (mime == null)
			return "*/*";
		return mime;
	}
	
	public static void delAllFile(String path) {
	     File file = new File(path);
	     if (!file.exists()) {
	        return;
	     }
	     if (!file.isDirectory()) {
	   	  	return;
	     }
	     String[] tempList = file.list();
	     File temp = null;
	     for (int i = 0; i < tempList.length; i++) {
	         if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	         }
	         else {
	             temp = new File(path + File.separator + tempList[i]);
	         }
	         if (temp.isFile()) {
	             temp.delete();
	         }
	         if (temp.isDirectory()) {
	             delAllFile(path+"/"+ tempList[i]);
	         }
	     }
	 }

}
