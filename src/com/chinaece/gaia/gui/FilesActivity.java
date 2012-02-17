package com.chinaece.gaia.gui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.chinaece.gaia.R;
import com.chinaece.gaia.util.FileBrowser;
import com.chinaece.gaia.util.OnFileBrowserListener;

public class FilesActivity extends Activity implements OnFileBrowserListener {
	private String value;

	@Override
	public void onFileItemClick(String filename) {
		openFile(new File(filename));
	}

	@Override
	public void onFlieLongItemClick(String filename) {
		modifyFile(new File(filename));
	}

	@Override
	public void onDirItemClick(String path) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);
		FileBrowser fileBrowser = (FileBrowser) findViewById(R.id.filebrowser);
		fileBrowser.setOnFileBrowserListener(this);
	}

	private void openFile(final File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		try {
			intent.setDataAndType(Uri.fromFile(f), type);
			startActivity(intent);
		} catch (Exception e) {
			intent.setDataAndType(Uri.fromFile(f), "*/*");
			startActivity(intent);
		}
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

	private String getMIMEType(File f) {
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
		if (mime == null)
			return "*/*";
		return mime;
	}
}
