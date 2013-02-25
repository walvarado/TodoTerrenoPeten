package com.researchmobile.todoterreno.pedidos.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class ConnectWSFactura {
	
	private static String IP_SERVER = "200.6.222.110";
    private static int PUERTO = 8080;
    
    public static JSONObject obtenerJsonObject(String url) {
    	Log.e("TT", "ConnectWSFactura.obtenerJson");
        JSONObject jsonObject = null;
        try {
            URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/ws/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String responseInputStream = convertStreamToString(inputStream);
            Log.v("TT", responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
        } catch (Exception exception) {
            System.out.println(exception);
            return jsonObject;
        }
        return jsonObject;
    }
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder(); 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

	

}
