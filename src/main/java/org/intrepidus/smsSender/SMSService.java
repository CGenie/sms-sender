package org.intrepidus.smsSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


public class SMSService {
    /**
	 * 
	 */
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	private static final String TAG = "org.intrepidus.smsSender.SMSService";
	
	public static String ENDPOINTS_FILENAME = "endpoints";
	
    public boolean sendTextMessage(String destinationAddress, String scAddress, String text) {
    	Log.i(TAG, "Sending sms: "+ text + " to " + destinationAddress + " from " + scAddress);
    	
    	SmsManager smsManager = SmsManager.getDefault();
    	smsManager.sendTextMessage(destinationAddress, null, text, null, null);
    	
    	return true;
    }
    
    public static void addSMSReceiveEndpoint(Context context, String endpoint) {
    	String toWrite = endpoint + "\n";
    	
    	try {
        	FileOutputStream fos = context.openFileOutput(SMSService.ENDPOINTS_FILENAME, Context.MODE_APPEND);
			fos.write(toWrite.getBytes());
			fos.close();
   		} catch (FileNotFoundException e) {
   			Log.e(TAG, "File not found: " + e.toString());
   		} catch (IOException e) {
   			Log.e(TAG, "IOException: " + e.toString());
   		}
    }
    
    public static String[] getSMSReceiveEndpoints(Context context) {
    	int n;
    	StringBuffer fileContent = new StringBuffer("");
    	byte[] buffer = new byte[1024];
    	    	
    	try {
    		FileInputStream fis = context.openFileInput(SMSService.ENDPOINTS_FILENAME);

			while((n = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer, 0, n));
			}
   		} catch (FileNotFoundException e) {
   			Log.e(TAG, "File not found: " + e.toString());
   			return null;
   		} catch (IOException e) {
   			Log.e(TAG, "IOException: " + e.toString());
   			return null;
		}
    	
    	
    	String content = fileContent.toString();
    	
    	Log.i(TAG, "Endpoints: " + content);

    	return content.split("\n");
    }
	
    public void broadcastSMS(Context context, String source, String text) {
		Log.i(TAG,  "Broadcasting SMS [" + source + "]" + text);
			
		String[] endpoints = getSMSReceiveEndpoints(context);
			
		for(int i = 0; i < endpoints.length; i++) {
			forwardSMSToEndpoint(endpoints[i], source, text);
		}
	}


    private void forwardSMSToEndpoint(String endpoint, String source, String text) {
    	Log.i(TAG, "Forwarding SMS [" + source + "] '" + text + "' to endpoint " + endpoint);
    	
    	OkHttpClient client = new OkHttpClient();
    	JSONObject data = new JSONObject();
    	RequestBody body;
    	
    	try {
        	data.put("source", source);
        	data.put("text", text);
    	} catch (JSONException e) {
    		Log.e(TAG,  "Error serializing JSON: " + e.toString());
    		return;
    	}
    	
		body = RequestBody.create(JSON, data.toString());
    	
    	try {
    		Request request = new Request.Builder().url(endpoint).post(body).build();
    		Response response = client.newCall(request).execute();
    		Log.i(TAG, "Forwarded to endpoint " + endpoint + ", response: " + response.body().string());
    	} catch (Exception e) {
    		Log.i(TAG,  "Error forwarding to endpoint " + endpoint + ": " + e.toString());
    	}
    }
}
