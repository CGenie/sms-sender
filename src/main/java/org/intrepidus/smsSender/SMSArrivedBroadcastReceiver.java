package org.intrepidus.smsSender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSArrivedBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "org.intrepidus.smsSender.SMSArrivedBroadcastReceiver";
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: remove this hardcoded string
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
	        Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
	        SmsMessage[] msgs = null;
	        String msg_from;
	        if (bundle != null) {
	            //---retrieve the SMS message received---
	            try {
	                Object[] pdus = (Object[]) bundle.get("pdus");
	                msgs = new SmsMessage[pdus.length];
	                for(int i=0; i < msgs.length; i++) {
	                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	                    msg_from = msgs[i].getOriginatingAddress();
	                    String msgBody = msgs[i].getMessageBody();
		                Log.i(TAG, "SMS Arrived: " + msgBody + " from: " + msg_from);
	                }
	            }catch(Exception e){
                    Log.e(TAG, "SMS Arrived exception caught" + e.getMessage());
	            }
	        }
	    }
	}
}
