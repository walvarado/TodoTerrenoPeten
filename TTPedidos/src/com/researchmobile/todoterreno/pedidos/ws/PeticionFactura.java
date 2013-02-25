package com.researchmobile.todoterreno.pedidos.ws;

import android.content.Context;
import android.util.Log;

import com.researchmobile.todoterreno.facturacion.entity.CatalogoRepartidor;
import com.researchmobile.todoterreno.pedidos.entity.LoginEntity;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.entity.User;
import com.researchmobile.todoterreno.pedidos.utility.ConnectState;


public class PeticionFactura {
	
	private RequestDBFactura requestDB = new RequestDBFactura();
	private RespuestaWS respuesta = new RespuestaWS();
	private ConnectWSFactura connectWS;
	private ConnectState connectState;
	private CatalogoRepartidor catalogoRepartidor;
	private RequestWSFactura requestWS = new RequestWSFactura();
	
	public RespuestaWS login(Context context) {
		setCatalogoRepartidor(new CatalogoRepartidor());

		try{
			respuesta = requestDB.verificaLoginDB(context, User.getUsername(), User.getClave());
			if(respuesta.isResultado()){
				if (connectState.isConnectedToInternet(context)){
//					pedidosPendientes(context);
				}
				return respuesta;
			}else{
				if (connectState.isConnectedToInternet(context)){
//					pedidosPendientes(context);
					LoginEntity loginEntity = new LoginEntity();
					setCatalogoRepartidor(requestWS.login(User.getUsername(), User.getClave()));
				}else{
					respuesta.setResultado(false);
					respuesta.setMensaje("No cuenta con conexion a internet");
					return respuesta;
				}
			}
		}catch(Exception exception){
			Log.e("TT", "Peticion.login - " + exception);
			respuesta.setResultado(false);
			respuesta.setMensaje("Error 0040. Verifique sus datos, Intente nuevamente");
//			limpiaDB(context);
			return respuesta;
			
		}
		return respuesta;
	}

	public CatalogoRepartidor getCatalogoRepartidor() {
		return catalogoRepartidor;
	}

	public void setCatalogoRepartidor(CatalogoRepartidor catalogoRepartidor) {
		this.catalogoRepartidor = catalogoRepartidor;
	}

	
}
