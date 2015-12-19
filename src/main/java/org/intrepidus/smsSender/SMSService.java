package org.intrepidus.smsSender;

import android.telephony.SmsManager;
import android.util.Log;

public class SMSService {
    /**
	 * 
	 */

	private static final String TAG = "org.intrepidus.smsSender.SMSService";
	
    public boolean sendTextMessage(String destinationAddress, String scAddress, String text) {
    	Log.i(TAG, "Sending sms: "+ text + " to " + destinationAddress + " from " + scAddress);
    	
    	SmsManager smsManager = SmsManager.getDefault();
    	smsManager.sendTextMessage(destinationAddress, null, text, null, null);
    	
    	return true;
    }
}
