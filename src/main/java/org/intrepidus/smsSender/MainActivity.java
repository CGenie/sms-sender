package org.intrepidus.smsSender;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "org.intrepidus.smsSender.MainActivity";
    
    private class WifiConnectedBroadcastReceiver extends BroadcastReceiver {
    	private Context context;
    	
    	public WifiConnectedBroadcastReceiver(Context context) {
    		this.context = context;
    	}
    	
    	public void onReceive(Context context, Intent intent) {
            TextView textView = (TextView) findViewById(R.id.text_view);
            
            if(isOnline(intent)) {
            	textView.setText(wifiIpAddress());
     	        // e.g. To check the Network Name or other info:
	            //WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
  	            //WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            } else {
            	textView.setText("Wifi not connected");
            }
    	}
    	
    	public boolean isOnline(Intent intent) {
    		NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
   	        return info != null && info.isConnected();
    	} 
    	
        protected String wifiIpAddress() {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(ipAddress);
            }

            byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

            String ipAddressString;
            try {
                ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
            } catch (UnknownHostException ex) {
                Log.e("WIFIIP", "Unable to get host address.");
                ipAddressString = null;
            }

            return ipAddressString;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
	    Log.i(TAG, "onCreate done");
	    
	    Context context = getApplicationContext();
	    IntentFilter intentFilter = new IntentFilter();
	    WifiConnectedBroadcastReceiver wifiConnection = new WifiConnectedBroadcastReceiver(context);
	    intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	    registerReceiver(wifiConnection, intentFilter);

	    Intent intent = new Intent(this, HTTPService.class);
		startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    
}
