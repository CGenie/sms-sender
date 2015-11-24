package org.intrepidus.smsSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import org.intrepidus.smsSender.HTTPService;

public class MainActivity extends Activity {
    private static final String TAG = "org.intrepidus.smsSender.MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
	Log.i(TAG, "onCreate done");

	Intent intent = new Intent(this, HTTPService.class);
	startService(intent);
    }

    @Override
    protected void onResume() {
	super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Hello world!");
    }

}
