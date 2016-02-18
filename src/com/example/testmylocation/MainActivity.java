package com.example.testmylocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MainActivity extends Activity {
	Button button;
	TextView textView;
	//MyLocation location;
	private LocationClient mLocClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocClient = ((MyLocation)getApplication()).mLocationClient;		
		((MyLocation)getApplication()).SetLocationMainActivity(this);
		

		setLocationOption();
		mLocClient.start();		
		mLocClient.requestOfflineLocation();
		//setLocationOption();
		mLocClient.requestLocation();
		
		
		setContentView(R.layout.activity_main);
		 button=(Button)findViewById(R.id.button1);
		 textView=(TextView)findViewById(R.id.textView1);
		 button.setOnClickListener(new OnClickListener() {			
		
			public void onClick(View arg0) {
				
			textView.setText(MyLocation.getCurrentPoint().x+"  "+MyLocation.getCurrentPoint().y+
					MyLocation.getPlace().Address);
				
			}
		});
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onDestroy() {
		mLocClient.stop();
		
		super.onDestroy();
	}

	//������ز���
		private void setLocationOption(){
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);	
			option.setOpenGps(true);				//��gps
			option.setCoorType("bd09ll");		//������������				
			option.setIsNeedAddress(true);//���õ�ַ��Ϣ��Ĭ���޵�ַ��Ϣ
			option.setScanSpan(1000);	//���ö�λģʽ��С��1����һ�ζ�λ;���ڵ���1����ʱ��λ
			mLocClient.setLocOption(option);
		}
	

}
