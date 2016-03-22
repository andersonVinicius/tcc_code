
package com.smarthomeintegracao.shi2.util;

/**
 * Created by anderson on 26/02/15.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils {

    public static InputStream OpenHttpConnection(String urlString, Context context) throws IOException
    {
        InputStream is = null;
        int response = -1;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Não é uma conexão HTTP");

            try{
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    is = httpConn.getInputStream();
                }
            }
            catch (Exception ex)
            {
                Log.i("HttpConnection", ex.getLocalizedMessage());
                throw new IOException(ex.getCause());
            }
        }

        return is;
    }
//    public static Boolean statusConnect(String urlString, Context context) throws IOException
//    {
//        boolean is;
//        int response = -1;
//
//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if(isConnected)
//        {
//            URL url = new URL(urlString);
//            URLConnection conn = url.openConnection();
//            if (!(conn instanceof HttpURLConnection))
//                throw new IOException("Não é uma conexão HTTP");
//
//            try{
//                HttpURLConnection httpConn = (HttpURLConnection) conn;
//                httpConn.setInstanceFollowRedirects(true);
//                httpConn.setRequestMethod("GET");
//                httpConn.connect();
//                response = httpConn.getResponseCode();
//
//                if (response == HttpURLConnection.HTTP_OK) {
//                    is = httpConn.getInputStream();
//                }
//            }
//            catch (Exception ex)
//            {
//                Log.i("HttpConnection", ex.getLocalizedMessage());
//                throw new IOException("Erro ao se conectar");
//            }
//        }
//
//        return is;
//    }
}