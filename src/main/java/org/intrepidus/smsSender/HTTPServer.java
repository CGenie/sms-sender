package org.intrepidus.smsSender;

import java.util.Map;
import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

import android.util.Log;

public class HTTPServer extends NanoHTTPD {
    private static final String TAG = "org.intrepidus.smsSender.HTTPServer";

    public HTTPServer() throws IOException {
        super("0.0.0.0", 8080);
        start();
        Log.i( TAG, "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    public static void main(String[] args) {
        try {
            new HTTPServer();
        }
        catch( IOException ioe ) {
            Log.e( TAG, "Couldn't start server:\n" + ioe );
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}
