package com.dean.mobileauto;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity  implements View.OnClickListener, View.OnTouchListener{
	ImageView iv;
	ImageButton iBup;
	ImageButton iBleft;
	ImageButton iBright;
	ImageButton iBdown;
	ImageButton iBcom;
	ImageButton iBclose;
	ImageButton speakButton;
	ImageView circle;
	private ListView mList;
	TextView command;
	String s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		// 语音识别
       PackageManager pm = getPackageManager();
       List activities = pm.queryIntentActivities(
                 new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0); //本地识别程序
       if (activities.size() != 0) {
                speakButton.setOnClickListener(this);
       } else {                 // 若检测不到语音识别程序在本机安装，测将扭铵置灰
                speakButton.setEnabled(false);
                speakButton.setImageResource(R.drawable.mic);
       }
	}

	private void init() {
		// TODO Auto-generated method stub
		iv = (ImageView) findViewById (R.id.imageViewTop);
		circle = (ImageView) findViewById (R.id.imageCircle);
		iBup = (ImageButton) findViewById (R.id.iBup);
		iBleft = (ImageButton) findViewById (R.id.iBleft);
		iBright = (ImageButton) findViewById (R.id.iBright);
		iBdown = (ImageButton) findViewById (R.id.iBdown);
		iBcom = (ImageButton) findViewById (R.id.iBcom);
		iBclose = (ImageButton) findViewById (R.id.iBclose);
		speakButton = (ImageButton) findViewById(R.id.iBvoiceMain); // 识别按钮
        mList = (ListView) findViewById(R.id.lvVoiceMain);
        command = (TextView) findViewById(R.id.tvCommand);
		iBup.setOnTouchListener(this);
		iBleft.setOnTouchListener(this);
		iBright.setOnTouchListener(this);
		iBdown.setOnTouchListener(this);
		iBcom.setOnClickListener(this);
		iBclose.setOnClickListener(this);
		speakButton.setOnClickListener(this);
		
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.iBcom:
			communicate();
			break;
		case R.id.iBclose:
			
			break;
		case R.id.iBvoiceMain:
			Intent a = new Intent(Main.this, Voice.class);
			a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(a);
            break;
		}
	}

	private void communicate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.aboutUs:
			Intent i = new Intent("com.dean.mobileauto.ABOUT");
			startActivity(i);
			break;
		case R.id.preferences:
			//Intent s = new Intent("com.dean.mobileauto.PREFS");
			//startActivity(s);
			break;
		case R.id.blue_client:
			Intent b = new Intent(Main.this, ClientActivity.class);
			b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(b);
			break;	
		case R.id.blue_server:
			Intent c = new Intent(Main.this, ServerActivity.class);
			c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(c);
			break;	
		case R.id.exit:
			finish();
			break;
	}
	return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		 if(event.getAction() == MotionEvent.ACTION_UP){
				BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.bk);
				circle.setImageDrawable(draw);
		 } else {
			switch(v.getId()){
			case R.id.iBup:
				if(event.getAction() == MotionEvent.ACTION_DOWN){
						BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.upbk);
						circle.setImageDrawable(draw);
				}
				break;
			case R.id.iBdown:
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.downbk);
					circle.setImageDrawable(draw);
				}
				break;
			case R.id.iBleft:
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.leftbk);
					circle.setImageDrawable(draw);
				}
				break;
			case R.id.iBright:
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					BitmapDrawable draw=(BitmapDrawable) getResources().getDrawable(R.drawable.rightbk);
					circle.setImageDrawable(draw);
				}
				break;
			}
		
		 }
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter("ncn");
		registerReceiver(myBroadcast,intentFilter);
	}

	private BroadcastReceiver myBroadcast = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			s = intent.getStringExtra("name");
			command.setText(s);
		}
		
	};
}
