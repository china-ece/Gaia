package com.chinaece.gaia.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;

public class DataStorage {
	private static final String Filename = "Gaia.cfg";
	public static final Properties properties = new Properties();

	public static boolean save(Activity act) {
		FileOutputStream stream = null;
		try {
			stream = act.openFileOutput(Filename,
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

	public static boolean load(Activity act) {
		FileInputStream stream = null;
		try {
			stream = act.openFileInput(Filename);
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

	public static boolean clear(Activity act) {
		FileOutputStream stream = null;
		try {
			stream = act.openFileOutput(Filename, Context.MODE_WORLD_WRITEABLE);
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
