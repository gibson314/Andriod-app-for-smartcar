package com.dean.mobileauto;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Voice extends Activity  {
       private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
       private ListView mList;          // ��ʾʶ����ִ���list�ؼ�
       boolean flag = false;

       @Override
       public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.voice);
                 mList = (ListView) findViewById(R.id.lvVoiceReturn);
                PackageManager pm = getPackageManager();
                List activities = pm.queryIntentActivities(
                          new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0); //����ʶ�����
//                       new Intent(RecognizerIntent.ACTION_WEB_SEARCH), 0); // ����ʶ�����
                if (activities.size() != 0) {
                         startMysttActivityActivity();
                         if(flag)
                        	 finish();
                } 
       }


       private void startMysttActivityActivity() {          // ��ʼʶ��
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                   RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "����ʶ����~");
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
                // ����ʶ�����
    }

       @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
                         // Fill the list view with the strings the recognizer thought it could have heard
                         final ArrayList<String> matches = data.getStringArrayListExtra(
                                            RecognizerIntent.EXTRA_RESULTS);
                         mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                                            matches));
                         mList.setOnItemClickListener(new OnItemClickListener(){
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
										String s = matches.get(position);
										
										Intent intent = new Intent("ncn");
										intent.putExtra("name", s);
										sendBroadcast(intent);
 								}
                         });
                         flag = true;
                }
                // ����ʶ���Ļص�����ʶ����ִ���list����ʾ
                super.onActivityResult(requestCode, resultCode, data);
       }
}

