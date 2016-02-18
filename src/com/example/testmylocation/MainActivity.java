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

	//设置相关参数
		private void setLocationOption(){
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);	
			option.setOpenGps(true);				//打开gps
			option.setCoorType("bd09ll");		//设置坐标类型				
			option.setIsNeedAddress(true);//设置地址信息，默认无地址信息
			option.setScanSpan(1000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
			mLocClient.setLocOption(option);
		}
	

}
