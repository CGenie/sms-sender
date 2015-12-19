package org.intrepidus.smsSender;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import org.intrepidus.smsSender.HTTPServer;


public class HTTPService extends IntentService {
    private static final String TAG = "org.intrepidus.smsSender.HTTPService";

    private HTTPServer server;
    
    public static final int SMS_RECEIVED = 0;


    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public HTTPService() {
    	super("HTTPService");

    	try {
    		server = new HTTPServer();
    		server.setHttpService(this);
    		Log.i(TAG, "server created");
    	} catch (java.io.IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, "error creating server");
    		Log.e(TAG, ExceptionUtils.getStackTrace(e));
    	};
    }

    @Override
    public void onCreate() {
    	super.onCreate();
    }
  

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.d(TAG, "onHandleIntent");
    }
    
    public void sendTextMessage(String destination, String source, String text) {
    	Intent intent = new Intent("send-sms");
    	intent.putExtra("destination", destination);
    	intent.putExtra("source", source);
    	intent.putExtra("text", text);
    	LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
