package com.researchmobile.todoterreno.pedidos.utility;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.researchmobile.todoterreno.pedidos.entity.ClienteNuevo;
import com.researchmobile.todoterreno.pedidos.entity.ListaPromocion;
import com.researchmobile.todoterreno.pedidos.entity.Pedido;
import com.researchmobile.todoterreno.pedidos.ws.Peticion;

public class rmString {
	private Fecha fecha = new Fecha();
	
	public boolean diaVisitaHoy(String diaVisita){
		
		if (diaVisita.indexOf(fecha.diaLetra()) > -1){
			return true;
		}
		return false;
	}
	
	public boolean semanaVisitaHoy(String semana){
		if (semana.indexOf(String.valueOf(fecha.semanaMes())) > -1){
			return true;
		}
		return false;
	}
	
	public String jsonNuevoCliente(ClienteNuevo cliente){
		String json = "";
		try{
			JSONObject allData = new JSONObject();
			allData.put("codigoCliente", cliente.getIdNuevoCliente());
			allData.put("nit", cliente.getNit());
			allData.put("nombreNegocio", cliente.getNombreNegocio());
			allData.put("categoria", cliente.getCategoriaCliente());
			allData.put("contacto", cliente.getNombreContacto());
			allData.put("telefono", cliente.getTelefono());
			allData.put("direccion", cliente.getDireccion());
			allData.put("ruta", cliente.getRuta());
			allData.put("diaVisita", cliente.getDiaVisita());
			allData.put("fecha", fecha.FechaHoy());
			allData.put("hora", fecha.Hora());
			json = allData.toString();
			return json;
		}catch(Exception exception){
			return json;
		}
	}

	public String jsonPedido(Context context, Pedido pedido, String ruta, String vendedor) {
		String jsonPedido = "";
		
		JSONObject allDataJson = new JSONObject();
		JSONArray encabezadoJsonArray = new JSONArray();
		JSONArray detalleJsonArray = new JSONArray();
		
		try {
			JSONObject encabezadoJson = new JSONObject();
			encabezadoJson.put("clicodigo", pedido.getEncabezadoPedido().getCodigoCliente());
			encabezadoJson.put("id", pedido.getEncabezadoPedido().getCodigoPedidoTemp());
			encabezadoJson.put("movfecha", pedido.getEncabezadoPedido().getFecha());
			encabezadoJson.put("movhora", pedido.getEncabezadoPedido().getHora());
			encabezadoJson.put("movcredito", pedido.getEncabezadoPedido().getCredito());
			encabezadoJson.put("movtotal", pedido.getEncabezadoPedido().getTotal());
			encabezadoJson.put("movefectivo", "0");
			encabezadoJson.put("movcheque", "0");
			encabezadoJson.put("autorizado", "0");
			encabezadoJson.put("movfacturado", "0");
			encabezadoJson.put("movanulado", "0");
			encabezadoJsonArray.put(encabezadoJson);
			int tamano = pedido.getDetallePedido().length;
			for (int i = 0; i < tamano; i++){
				JSONObject temp = new JSONObject();
				//temp.put("idencabezado", pedido.getEncabezadoPedido().getCodigoPedidoTemp());
				temp.put("movprecio", pedido.getDetallePedido()[i].getPrecio());
				temp.put("movunidades", pedido.getDetallePedido()[i].getTotalUnidades());
				temp.put("artcodigo", pedido.getDetallePedido()[i].getCodigo());
				temp.put("movtipoprecio", "0");
				detalleJsonArray.put(temp);
//				Verificar si el articulo tiene bonificacion
				ListaPromocion listaPromocion = new ListaPromocion();
				Peticion peticionBoni = new Peticion();
	        	listaPromocion = peticionBoni.buscaBoni(context, pedido.getDetallePedido()[i].getCodigo());
//	        	Si encuentra artículos bonificados para este artículo, agrega el artículo bonificado
	        	if (listaPromocion.getRespuesta().isResultado()){
	        		
	        		int unidadesCompra = pedido.getDetallePedido()[i].getTotalUnidades();
	        		int fardosBoni = listaPromocion.getPromocion()[0].getFardosBoni();
	        		int unidadesBoni = listaPromocion.getPromocion()[0].getUnidadesBoni();
	        		int totalUnidadesBoni = ((fardosBoni * unidadesBoni) + unidadesBoni);
	        		float precioBoni = listaPromocion.getPromocion()[0].getPrecioVentaBoni();
	        		
	        		Log.e("TT", "unidadesCompra = " + unidadesCompra);
	        		Log.e("TT", "fardosBoni = " + fardosBoni);
	        		Log.e("TT", "unidadesBoni = " + unidadesBoni);
	        		Log.e("TT", "totalUndiadeBoni = " + totalUnidadesBoni);
	        		
	        		if (unidadesCompra > totalUnidadesBoni){
	        			int cantidadBoni = unidadesCompra / totalUnidadesBoni;
	        			
	        			JSONObject tempBoni = new JSONObject();
	    				//temp.put("idencabezado", pedido.getEncabezadoPedido().getCodigoPedidoTemp());
	    				tempBoni.put("movprecio", listaPromocion.getPromocion()[0].getPrecioVentaBoni());
	    				tempBoni.put("movunidades", cantidadBoni * totalUnidadesBoni);
	    				tempBoni.put("artcodigo", listaPromocion.getPromocion()[0].getArtCodigo());
	    				tempBoni.put("movtipoprecio", "PROMOCION");
	    				detalleJsonArray.put(tempBoni);
	        		}
	        	}
			}
			allDataJson.put("encabezado", encabezadoJsonArray);
			allDataJson.put("detalle", detalleJsonArray);
			jsonPedido = allDataJson.toString();
			return jsonPedido;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public String jsonMotivo(String codigoCliente, String ruta, String idusuario, String motivoSeleccionado) {
		JSONObject jsonString = new JSONObject();
		try {
			jsonString.put("clicodigo", codigoCliente);
			jsonString.put("movfecha", fecha.FechaHoy());
			jsonString.put("movhora", fecha.Hora());
			jsonString.put("idnoventa", motivoSeleccionado);
			jsonString.put("idvendedor", idusuario);
			String finalString = jsonString.toString();
			return finalString;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
