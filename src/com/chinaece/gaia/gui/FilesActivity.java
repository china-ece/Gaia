package com.chinaece.gaia.gui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.chinaece.gaia.R;
import com.chinaece.gaia.util.FileBrowser;
import com.chinaece.gaia.util.FileUtil;
import com.chinaece.gaia.util.OnFileBrowserListener;

public class FilesActivity extends Activity implements OnFileBrowserListener {
	private String value;

	@Override
	public void onFileItemClick(String filename) {
		try{
			startActivity(FileUtil.openFile(new File(filename)));
		}
		 catch (Exception e) {
            Toast.makeText(getApplicationContext(), "没有相关软件打开此文件", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

	        }
	}

	@Override
	public void onFlieLongItemClick(String filename) {
		modifyFile(new File(filename));
	}

	@Override
	public void onDirItemClick(final String path) {
		CharSequence[] items = {  "清空文件夹", "取消操作" };
		AlertDialog.Builder builder = new AlertDialog.Builder(FilesActivity.this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					FileUtil.delAllFile(path);
					break;
				case 1:
					dialog.dismiss();
				default:
					break;
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);
		FileBrowser fileBrowser = (FileBrowser) findViewById(R.id.filebrowser);
		fileBrowser.setOnFileBrowserListener(this);
	}

	private void modifyFile(final File f) {
		CharSequence[] items = { "重命名文件", "删除文件", "取消操作" };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FilesActivity.this);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					final EditText input = new EditText(FilesActivity.this);
					input.setText(f.getName().toString());
					AlertDialog.Builder buider = new AlertDialog.Builder(
							FilesActivity.this);
					buider.setTitle("请输入新文件名");
					buider.setView(input);
					buider.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									value = input.getText().toString();
									File newFile = new File("/mnt/sdcard/ece/"
											+ value);
									f.renameTo(newFile);
									dialog.dismiss();
									FileBrowser browser = (FileBrowser) findViewById(R.id.filebrowser);
									browser.refreshFiles();
									ArrayAdapter<File> adapter = (ArrayAdapter<File>) browser
											.getAdapter();
									adapter.notifyDataSetChanged();

								}
							}).setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					buider.show();
					break;
				case 1:
					f.delete();
					FileBrowser browser = (FileBrowser) findViewById(R.id.filebrowser);
					browser.refreshFiles();
					ArrayAdapter<File> adapter = (ArrayAdapter<File>) browser
							.getAdapter();
					adapter.notifyDataSetChanged();
				case 2:
					dialog.dismiss();
				default:
					break;
				}
			}
		});
		builder.show();
	}

	
}
