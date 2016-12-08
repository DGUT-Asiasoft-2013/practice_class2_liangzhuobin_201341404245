package com.example.hello;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import inputcells.SimpleTextInputCellFragment;

public class FeedContentActivity extends Activity{
@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_content);
		String text=getIntent().getStringExtra("text");
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		
		
		TextView textview1=(TextView)findViewById(R.id.textView1);
		textview1.setText(text);
	}

}
