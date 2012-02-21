package com.chinaece.gaia.gui;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaece.gaia.R;

public class ContactActivity extends Activity{
	protected static final Uri Uri = null;
	private TextView txtname;
	private EditText edtphone;
	private EditText edtemail;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);	
		txtname = (TextView)findViewById(R.id.txtName);
		txtname.setText(getIntent().getExtras().getString("name"));		
		edtphone = (EditText)findViewById(R.id.edtTelphone);
		edtphone.setText(getIntent().getExtras().getString("telephone"));		
		edtemail = (EditText)findViewById(R.id.edtEmail);		
		edtemail.setText(getIntent().getExtras().getString("email"));		
		Button btnsave = (Button) findViewById(R.id.btnSaveTel);
		btnsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {						
				if (edtphone.getText()!= null && !edtphone.getText().toString().equals(""))
				{
					savecontacts();			
					Toast.makeText(ContactActivity.this, "该用户电话号码保存成功", 
							Toast.LENGTH_SHORT).show();
				}
				else 
				{	
					Toast.makeText(ContactActivity.this, "该用户没有相应的电话号码", 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
}
	 public void savecontacts(){
		    ContentValues values = new ContentValues();
	        Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
	        long rawContactId = ContentUris.parseId(rawContactUri);
	        values.clear();
	        values.put(Data.RAW_CONTACT_ID, rawContactId);
	        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
	        values.put(StructuredName.DISPLAY_NAME, txtname.getText().toString());
	        getContentResolver().insert(
	                android.provider.ContactsContract.Data.CONTENT_URI, values);
	        values.clear();
	        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
	        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	        values.put(Phone.NUMBER, edtphone.getText().toString());
	        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
	        getContentResolver().insert(
	                android.provider.ContactsContract.Data.CONTENT_URI, values);
	        values.clear();
	        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
	        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
	        values.put(Email.DATA, edtemail.getText().toString());
	        values.put(Email.TYPE, Email.TYPE_WORK);
	        getContentResolver().insert(
	                android.provider.ContactsContract.Data.CONTENT_URI, values);
			 }
}