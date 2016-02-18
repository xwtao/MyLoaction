package com.example.testmylocation;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;


public class MyLocation extends Application {

	public LocationClient mLocationClient = null;
	public static LocationClient LocationClient = null;

	public MyLocationListenner myListener = new MyLocationListenner();
	public String Imei;
	public static BDLocation location1=new BDLocation();
	private MainActivity activity;     

	
//	public TextView mTv;
//	public NotifyLister mNotifyer=null;
//	public Vibrator mVibrator01;
	private static MyLocation instance = null;
	public static MyLocation getInstance(){
		return instance;
	}
	public  void SetLocationMainActivity(MainActivity activity)     
	{        this.activity = activity;     }
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient( getApplicationContext() );
		LocationClient = new LocationClient( getApplicationContext() );
		mLocationClient.registerLocationListener( myListener );

		super.onCreate(); 
		instance = this;
		Log.d("locSDK_Demo1", "... Application onCreate... pid=" + Process.myPid());
	}
	
	
	public void logMsg(String str) {
		Log.d("locSDK_Demo1", str);
	}
	public static Point getCurrentPoint() {	
		LocationClient.registerLocationListener(new BDLocationListener() {			
						
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				location1=arg0;
			}
		});
		return new Point(location1.getLatitude(),location1.getLongitude());
	}
	public static Place getPlace(){
		LocationClient.registerLocationListener(new BDLocationListener() {			
			
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				location1=arg0;
			}
		});
		return new Place(location1.getProvince(),location1.getCity(),
				location1.getDistrict(),location1.getStreet(),location1.getAddrStr());
	}
	public static double getDistance(Point p1,Point p2){
		double lat1=p1.x;
		double lon1=p1.y;
		double lat2=p2.x;
		double lon2=p2.y;
		  int MAXITERS = 20;
	        // Convert lat/long to radians
	        lat1 *= Math.PI / 180.0;
	        lat2 *= Math.PI / 180.0;
	        lon1 *= Math.PI / 180.0;
	        lon2 *= Math.PI / 180.0;

	        double a = 6378137.0; // WGS84 major axis
	        double b = 6356752.3142; // WGS84 semi-major axis
	        double f = (a - b) / a;
	        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

	        double L = lon2 - lon1;
	        double A = 0.0;
	        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
	        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

	        double cosU1 = Math.cos(U1);
	        double cosU2 = Math.cos(U2);
	        double sinU1 = Math.sin(U1);
	        double sinU2 = Math.sin(U2);
	        double cosU1cosU2 = cosU1 * cosU2;
	        double sinU1sinU2 = sinU1 * sinU2;

	        double sigma = 0.0;
	        double deltaSigma = 0.0;
	        double cosSqAlpha = 0.0;
	        double cos2SM = 0.0;
	        double cosSigma = 0.0;
	        double sinSigma = 0.0;
	        double cosLambda = 0.0;
	        double sinLambda = 0.0;

	        double lambda = L; // initial guess
	        for (int iter = 0; iter < MAXITERS; iter++) {
	            double lambdaOrig = lambda;
	            cosLambda = Math.cos(lambda);
	            sinLambda = Math.sin(lambda);
	            double t1 = cosU2 * sinLambda;
	            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
	            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
	            sinSigma = Math.sqrt(sinSqSigma);
	            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
	            sigma = Math.atan2(sinSigma, cosSigma); // (16)
	            double sinAlpha = (sinSigma == 0) ? 0.0 :
	                cosU1cosU2 * sinLambda / sinSigma; // (17)
	            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
	            cos2SM = (cosSqAlpha == 0) ? 0.0 :
	                cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

	            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
	            A = 1 + (uSquared / 16384.0) * // (3)
	                (4096.0 + uSquared *
	                 (-768 + uSquared * (320.0 - 175.0 * uSquared)));
	            double B = (uSquared / 1024.0) * // (4)
	                (256.0 + uSquared *
	                 (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
	            double C = (f / 16.0) *
	                cosSqAlpha *
	                (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
	            double cos2SMSq = cos2SM * cos2SM;
	            deltaSigma = B * sinSigma * // (6)
	                (cos2SM + (B / 4.0) *
	                 (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
	                  (B / 6.0) * cos2SM *
	                  (-3.0 + 4.0 * sinSigma * sinSigma) *
	                  (-3.0 + 4.0 * cos2SMSq)));

	            lambda = L +
	                (1.0 - C) * f * sinAlpha *
	                (sigma + C * sinSigma *
	                 (cos2SM + C * cosSigma *
	                  (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

	            double delta = (lambda - lambdaOrig) / lambda;
	            if (Math.abs(delta) < 1.0e-12) {
	                break;
	            }
	        }

	        return  b * A * (sigma - deltaSigma);
	    }
	
	
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {		
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				Log.e("GPS", location.getLocType()+"");
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				sb.append("\n�?/县：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			logMsg(sb.toString());
			Log.e("locDemo", sb.toString());
		///////////////////////////////	
			Log.e("locDemo", "Longitude: " + location.getLongitude());
			Log.e("locDemo", "Latitude: " + location.getLatitude());
			Map<String, String> map = new HashMap<String, String>();
			map.put("latitude", String.valueOf(location.getLatitude()));
			map.put("longitude", String.valueOf(location.getLongitude()));
			
			location1=location;
	
			
			
		}
	
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		}
	}
	
	
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){
		//	mVibrator01.vibrate(1000);
		}
	}
	
	
}