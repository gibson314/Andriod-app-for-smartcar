package com.dean.mobileauto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.dean.mobileauto.util.BluetoothClientService;
import com.dean.mobileauto.util.BluetoothTools;
import com.dean.mobileauto.util.TransmitBean;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends Activity {

	private TextView serversText;
	private EditText chatEditText;
	private EditText sendEditText;
	private Button sendBtn;
	private Button startSearchBtn;
	private Button selectDeviceBtn;
	private Button modeImgButton;
	private Button modeTextButton;
	private int mode;
	
	private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
	
	//�㲥������
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
				//δ�����豸
				serversText.append("not found device\r\n");
				
			} else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
				//��ȡ���豸����
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				deviceList.add(device);
				serversText.append(device.getName() + "\r\n");
				
			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				//���ӳɹ�
				serversText.append("���ӳɹ�");
				sendBtn.setEnabled(true);
				
			} else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				//��������
				if (mode==1)
				{
					
					chatEditText.append("Receive a img\r\n");
					//File file2 = new File(Environment.getExternalStorageDirectory()+"/456.jpg");
					//file2 = file;
				}
				else {
				TransmitBean data = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA);
				String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
				chatEditText.append(msg);
				}
			
			} 
		}
	};
	
	
	@Override
	protected void onStart() {
		//����豸�б�
		deviceList.clear();
		
		//������̨service
		Intent startService = new Intent(ClientActivity.this, BluetoothClientService.class);
		startService(startService);
		
		//ע��BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
		intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		
		registerReceiver(broadcastReceiver, intentFilter);
		
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		
		serversText = (TextView)findViewById(R.id.clientServersText);
		chatEditText = (EditText)findViewById(R.id.clientChatEditText);
		sendEditText = (EditText)findViewById(R.id.clientSendEditText);
		sendBtn = (Button)findViewById(R.id.clientSendMsgBtn);
		startSearchBtn = (Button)findViewById(R.id.startSearchBtn);
		selectDeviceBtn = (Button)findViewById(R.id.selectDeviceBtn);
		modeImgButton = (Button)findViewById(R.id.modeImg);
		modeTextButton = (Button)findViewById(R.id.modeText);
		
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//������Ϣ
				if ("".equals(sendEditText.getText().toString().trim())) {
					Toast.makeText(ClientActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
				} else {
					//������Ϣ
					TransmitBean data = new TransmitBean();
					data.setMsg(sendEditText.getText().toString());
					Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
					sendDataIntent.putExtra(BluetoothTools.DATA, data);
					sendBroadcast(sendDataIntent);
				}
			}
		});
		modeImgButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				mode = 1;
			}
		});
		modeTextButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				mode = 0;
			}
		});
		
		startSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ʼ����
				Intent startSearchIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
				sendBroadcast(startSearchIntent);
			}
		});
		
		selectDeviceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ѡ���һ���豸
				Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
				selectDeviceIntent.putExtra(BluetoothTools.DEVICE, deviceList.get(0));
				sendBroadcast(selectDeviceIntent);
			}
		});
	}

	@Override
	protected void onStop() {
		//�رպ�̨Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		sendBroadcast(startService);
		
		unregisterReceiver(broadcastReceiver);
		super.onStop();
	}
}