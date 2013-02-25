package com.researchmobile.todoterreno.pedidos.ws;

import org.json.JSONArray;
import org.json.JSONObject;

import com.researchmobile.todoterreno.facturacion.entity.CatalogoRepartidor;
import com.researchmobile.todoterreno.facturacion.entity.Repartidor;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;

public class RequestWSFactura {
	private static String WS_LOGIN = "ws_login.php?a=login&";
	private static String WS_CLIENTES = "ws_clientes.php?";
	private static String WS_PRODUCTOS = "ws_articulos.php?a=catalogo&idportafolio=";
	private static String WS_ENVIO = "json.php?username=";
	private static String WS_ENVIAMOTIVO = "ws_noventa.php?cliente=";
	private static String WS_CATEGORIAS = "ws_categorias.php?";
	
	private ConnectWSFactura connectWSFactura = new ConnectWSFactura();
	
	public CatalogoRepartidor login(String username, String clave) {
		CatalogoRepartidor catalogoRepartidor = new CatalogoRepartidor();
		catalogoRepartidor.setRespuesta(new RespuestaWS());
		catalogoRepartidor.setRepartidor(new Repartidor());
		String url = WS_LOGIN + username + "&clave=" + clave;
		try{
			JSONObject jsonObject = connectWSFactura.obtenerJsonObject(url);
			if (jsonObject != null){
				catalogoRepartidor.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
				catalogoRepartidor.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
				if (catalogoRepartidor.getRespuesta().isResultado()){
					JSONArray jsonArray = jsonObject.getJSONArray("repartidor");
					int tamano = jsonArray.length();
					for (int i = 0; i < tamano; i++){
						JSONObject jsonTemp = jsonArray.getJSONObject(i);
						catalogoRepartidor.getRepartidor().setId(jsonTemp.getString("Idrepartidor"));
						catalogoRepartidor.getRepartidor().setClave(jsonTemp.getString("clave"));
						catalogoRepartidor.getRepartidor().setRepartidor(jsonTemp.getString("repartidor"));
						catalogoRepartidor.getRepartidor().setIdentificacion(jsonTemp.getString("identificacion"));
						catalogoRepartidor.getRepartidor().setLicencia(jsonTemp.getString("licencia"));
						catalogoRepartidor.getRepartidor().setDireccion(jsonTemp.getString("direccion"));
						catalogoRepartidor.getRepartidor().setTelefono(jsonTemp.getString("telefono"));
						catalogoRepartidor.getRepartidor().setSaldo(jsonTemp.getString("saldo"));
					}
					return catalogoRepartidor;
				}else{
					return catalogoRepartidor;
				}
				
			}else{
				catalogoRepartidor.getRespuesta().setResultado(false);
				catalogoRepartidor.getRespuesta().setMensaje("Problemas al logear, intente nuevamente");
				return catalogoRepartidor;
			}
			
		}catch(Exception exception){
			catalogoRepartidor.getRespuesta().setResultado(false);
			catalogoRepartidor.getRespuesta().setMensaje("Problemas al logear, intente nuevamente");
			return catalogoRepartidor;
		}
	}
	
	

}
