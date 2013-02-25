package com.researchmobile.todoterreno.pedidos.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class ConnectWS {

//	private static String IP_SERVER = "200.6.222.110";
	private static String IP_SERVER = "107.22.146.254";
    private static int PUERTO = 8080;
    
    public static JSONObject obtenerJson(String url) {
    	Log.e("TT", "ConnectWS.obtenerJson");
        JSONObject jsonObject = null;
        try {
            URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/ws/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            System.out.println("Login - url = " + urlCon);
            InputStream inputStream = urlConnection.getInputStream();
            System.out.println("Login - input = " + inputStream);
            String responseInputStream = convertStreamToString(inputStream);
            System.out.println(responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
        } catch (Exception exception) {
            System.out.println(exception);
            return jsonObject;
        }
        return jsonObject;
    }
    
    public static JSONObject enviaPedidoJson(String url) {
    	Log.e("TT", "ConnectWS.obtenerJson");
        JSONObject jsonObject = null;
        try {
            URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            System.out.println("ENVIAR_PEDIDO - url = " + urlCon);
            InputStream inputStream = urlConnection.getInputStream();
            
            String responseInputStream = convertStreamToString(inputStream);
            System.out.println(responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
        } catch (Exception exception) {
            System.out.println(exception);
            return jsonObject;
        }
        return jsonObject;
    }
    
    public static JSONObject enviaMotivoJson(String url) {
		JSONObject jsonObject = null;
		try{
			URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/ws/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            System.out.println("ENVIAR_MOTIVO - url = " + urlCon);
            InputStream inputStream = urlConnection.getInputStream();
            
            String responseInputStream = convertStreamToString(inputStream);
            System.out.println(responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
		}catch(Exception exception){
			System.out.println(exception);
            return jsonObject;
        }
        return jsonObject;
	}
    
    public static JSONObject clienteVisitado(String url) {
    	JSONObject jsonObject = null;
		try{
			URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/ws/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            System.out.println("ENVIAR_CLIENTE VISITADO - url = " + urlCon);
            InputStream inputStream = urlConnection.getInputStream();
            
            String responseInputStream = convertStreamToString(inputStream);
            System.out.println(responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
		}catch(Exception exception){
			System.out.println(exception);
            return jsonObject;
        }
        return jsonObject;
		
	}
    
    public static JSONObject enviaNuevoCliente(String url) {
    	JSONObject jsonObject = null;
		try{
			URL urlCon = new URL("http", IP_SERVER, PUERTO, "/megainfo/ws/" + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlCon.openConnection();
            System.out.println("ENVIAR_CLIENTE NUEVO - url = " + urlCon);
            InputStream inputStream = urlConnection.getInputStream();
            
            String responseInputStream = convertStreamToString(inputStream);
            System.out.println(responseInputStream);
            jsonObject = new JSONObject(responseInputStream);
		}catch(Exception exception){
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

	

