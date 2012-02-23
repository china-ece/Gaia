package com.chinaece.gaia.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;

public class DataStorage {
	private static final String Filename = "Gaia.cfg";
	public static final Properties properties = new Properties();

	public static boolean save(Context context) {
		FileOutputStream stream = null;
		try {
			stream = context.openFileOutput(Filename,
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}finally{
			try {
				if(stream != null){
					stream.flush();
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		return true;
	}

	public static boolean load(Context context) {
		FileInputStream stream = null;
		try {
			stream = context.openFileInput(Filename);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

	public static boolean clear(Context context) {
		FileOutputStream stream = null;
		try {
			stream = context.openFileOutput(Filename, Context.MODE_WORLD_WRITEABLE);
			properties.clear();
			properties.store(stream, "");
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		return true;
	}

}
