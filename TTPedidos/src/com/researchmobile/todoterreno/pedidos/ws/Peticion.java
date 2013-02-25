package com.researchmobile.todoterreno.pedidos.ws;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.researchmobile.todoterreno.pedidos.entity.Articulo;
import com.researchmobile.todoterreno.pedidos.entity.Cliente;
import com.researchmobile.todoterreno.pedidos.entity.ClienteNuevo;
import com.researchmobile.todoterreno.pedidos.entity.DetallePedido;
import com.researchmobile.todoterreno.pedidos.entity.EncabezadoPedido;
import com.researchmobile.todoterreno.pedidos.entity.ListaArticulos;
import com.researchmobile.todoterreno.pedidos.entity.ListaCategoria;
import com.researchmobile.todoterreno.pedidos.entity.ListaClientes;
import com.researchmobile.todoterreno.pedidos.entity.ListaPromocion;
import com.researchmobile.todoterreno.pedidos.entity.LoginEntity;
import com.researchmobile.todoterreno.pedidos.entity.NoVenta;
import com.researchmobile.todoterreno.pedidos.entity.Pedido;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.entity.User;
import com.researchmobile.todoterreno.pedidos.entity.Vendedor;
import com.researchmobile.todoterreno.pedidos.utility.ConnectState;
import com.researchmobile.todoterreno.pedidos.utility.Fecha;
import com.researchmobile.todoterreno.pedidos.utility.FormatDecimal;
import com.researchmobile.todoterreno.pedidos.utility.Mail;
import com.researchmobile.todoterreno.pedidos.utility.rmString;

public class Peticion {
	//Temp
	//private RequestWSTemp requestWS = new RequestWSTemp();
	//private RequestDBTemp requestDB = new RequestDBTemp();
	
	private RequestWS requestWS = new RequestWS();
	private RequestDB requestDB = new RequestDB();
	private RespuestaWS respuesta = new RespuestaWS();
	private ConnectState connectState = new ConnectState();
	private rmString rm = new rmString();
	private FormatDecimal formatDecimal = new FormatDecimal();
	private Fecha fecha = new Fecha();

	public void limpiaDB(Context context){
		requestDB.eliminarArticulos(context);
		requestDB.eliminarClientes(context);
		requestDB.eliminarDetallePedido(context);
		requestDB.eliminarDetallePedidoTemp(context);
		requestDB.eliminarEncabezadoPedidoTemp(context);
		requestDB.eliminarPedido(context);
		requestDB.eliminarPortafolio(context);
		requestDB.eliminarRuta(context);
		requestDB.eliminarUsuario(context);
		requestDB.eliminarVendedor(context);
		requestDB.eliminarNoVenta(context);
		requestDB.eliminarNuevoCliente(context);
		requestDB.eliminarPromociones(context);
	}
	public RespuestaWS login(Context context) {
		try{
			respuesta = requestDB.verificaLoginDB(context, User.getUsername(), User.getClave());
			if(respuesta.isResultado()){
				if (connectState.isConnectedToInternet(context)){
					pedidosPendientes(context);
				}
				return respuesta;
			}else{
				if (connectState.isConnectedToInternet(context)){
					pedidosPendientes(context);
					LoginEntity loginEntity = new LoginEntity();
					loginEntity = requestWS.login(User.getUsername(), User.getClave());
					respuesta = loginEntity.getRespuesta();
					if (respuesta.isResultado()){
						if (loginEntity.getPortafolio().length > 0 && loginEntity.getRuta().length > 0){
							pedidosPendientes(context);
							reiniciaDB(context);
							guardarDatos(context, loginEntity);
							cargarClientes(context, loginEntity);
							cargarArticulos(context, loginEntity);
							cargarPromociones(context);
							return respuesta;
						}else{
							respuesta.setResultado(false);
							respuesta.setMensaje("Problemas al cargar datos, intente nuevamente");
							return respuesta;
						}
						
					}else{
						return respuesta;
					}
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
			limpiaDB(context);
			return respuesta;
			
		}
	}
	
	private void reiniciaDB(Context context){
		requestDB.eliminarUsuario(context);
		requestDB.eliminarVendedor(context);
		requestDB.eliminarPortafolio(context);
		requestDB.eliminarRuta(context);
		requestDB.eliminarArticulos(context);
		requestDB.eliminarClientes(context);
		requestDB.eliminarDetallePedido(context);
		requestDB.eliminarDetallePedidoTemp(context);
		requestDB.eliminarEncabezadoPedidoTemp(context);
		requestDB.eliminarPedido(context);
	}
	
	public void actualizarClientes(Context context) {
		LoginEntity clientes = new LoginEntity();
		clientes = requestWS.actualizaClientes();
		if (clientes.getRespuesta().isResultado()){
			int tamanoRuta = clientes.getRuta().length;
			ListaClientes[] listaClientes = new ListaClientes[tamanoRuta];
			if (tamanoRuta > 0){
				int intentos = 0;
				for (int i = 0; i < tamanoRuta; i++){
					ListaClientes listaClientesTemp = requestWS.listaClientes("catalogo", clientes.getRuta()[i].getId());
					if (listaClientesTemp.getCliente().length > 0){
						listaClientes[i] = listaClientesTemp;
					}
				}
			}
			
			requestDB.eliminarClientes(context);
			
			for (int i = 0; i < tamanoRuta; i++){
				ListaClientes nuevosClientes = new ListaClientes();
				nuevosClientes = listaClientes[i];
				if (nuevosClientes != null){
					guardarClientes(context, nuevosClientes);
				}
			}
		}else{
			Log.e("TT", "no fue posible consultar las rutas");
		}
	}
	
	private void cargarArticulos(Context context, LoginEntity loginEntity) {
		ListaArticulos listaArticulos = new ListaArticulos();
		int tamanoPortafolio = loginEntity.getPortafolio().length;
		
		int intentos = 0;
		for (int i = 0; i < tamanoPortafolio; i++){
			do{
				listaArticulos = requestWS.listaArticulos(loginEntity.getPortafolio()[i].getIdPortafolio());
				if (listaArticulos.getArticulo().length > i){
					guardarArticulos(context, listaArticulos);
					
				}else{
					intentos++;
					Log.e("TT", "intentos en articulos = " + intentos);
				}
			}while(listaArticulos.getArticulo().length < 1 && intentos < 3);
			if (intentos > 2 ){
				limpiaDB(context);
			}else{
				intentos = 0;
			}
		}
	}
	
	private void cargarPromociones(Context context){
		ListaPromocion listaPromocion = new ListaPromocion();
		try{
			listaPromocion = requestWS.listaPromocion();
			if (listaPromocion.getPromocion().length > 0){
				guardaPromocion(context, listaPromocion);
			}
		}catch(Exception exception){
			
		}
		
	}
	
	private void guardaPromocion(Context context, ListaPromocion listaPromocion){
		requestDB.guardaPromocion(context, listaPromocion);
	}
	
	public void insertaEncabezado(Context context, EncabezadoPedido encabezadoPedido) {
		requestDB.guardaEncabezadoPedido(context, encabezadoPedido);
		
	}

	private void guardarArticulos(Context context, ListaArticulos listaArticulos) {
		requestDB.guardaArticulo(context, listaArticulos.getArticulo());
		
	}

	private void cargarClientes(Context context, LoginEntity loginEntity) {
		ListaClientes listaClientes = new ListaClientes();
		int tamanoRuta = loginEntity.getRuta().length - 1;
		Log.e("TT", "Peticion.cargarClientes = " + tamanoRuta);
		int intentos = 0;
		for (int i = 0; i < tamanoRuta; i++){
			do{
				listaClientes = requestWS.listaClientes("catalogo", loginEntity.getRuta()[i].getId());
				if (listaClientes.getCliente().length > 0){
					guardarClientes(context, listaClientes);
					
				}else{
					intentos++;
				}
			}while(listaClientes.getCliente().length < 1 && intentos < 3);
			if (intentos > 2 ){
				limpiaDB(context);
			}else{
				intentos = 0;
			}
		}
	}

	private void guardarClientes(Context context, ListaClientes listaClientes) {
		Log.e("TT", "Peticion.guardarClientes 1");
		requestDB.guardaCliente(context, listaClientes.getCliente());
	}

	private void guardarDatos(Context context, LoginEntity loginEntity) {
		requestDB.guardaUsuario(context, loginEntity.getUsuario());
		requestDB.guardaVendedor(context, loginEntity.getVendedor());
		requestDB.guardaPortafolio(context, loginEntity.getPortafolio());
		requestDB.guardaRuta(context, loginEntity.getRuta());
		requestDB.guardaNoVenta(context, loginEntity.getNoVenta());
	}
	
	public void insertaArticuloTemp(Context context, DetallePedido articulo, int numeroPedido) {
		requestDB.guardaDetallePedidoTemp(context, articulo, numeroPedido);
		
	}

	private void cargarDatosWS(LoginEntity loginEntity) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<HashMap<String, String>> ListaClientesPendientes (Context context){
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		Cliente[] cliente = requestDB.clientePendienteDB(context);
		//int tamano = cliente.length;
		for (int i = 0; i < cliente.length; i++){
			
			String visitado = cliente[i].getVisitado();
			if (visitado.equalsIgnoreCase("null") || !visitado.equalsIgnoreCase(fecha.fechaInversa())){
				if (rm.semanaVisitaHoy(cliente[i].getSemana()) && rm.diaVisitaHoy(cliente[i].getDiaVisita())){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("codigoCliente", cliente[i].getCliCodigo());
			        map.put("empresa", cliente[i].getCliEmpresa());
			        map.put("contacto", cliente[i].getCliContacto());
			        map.put("direccion", cliente[i].getCliDireccion());
			        map.put("telefono", cliente[i].getCliTelefono());
			        map.put("nit", cliente[i].getCliNit());
			        mylist.add(map);
				}
			}
		}
		return mylist;
		
	}
	
	public ArrayList<HashMap<String, String>> listaArticulos(Context context, int precio) {
		Log.d("TT", "precio = " + precio);
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		Articulo[] articulo = requestDB.articuloDB(context);
		int tamano = articulo.length;
		for (int i = 0; i < tamano; i++){
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("codigoProducto", articulo[i].getArtCodigo());
        	map.put("nombreProducto", articulo[i].getArtDescripcion());
        	map.put("cajas", "0");
        	map.put("unidades", "0");
        	switch (precio){
        	case 1:
        		map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecioDes1()));
        		break;
        	case 2:
        		map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecioDes2()));
        		break;
        	case 3:
        		map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecioDes3()));
        		break;
        	default:
        		map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecioVenta()));
        		break;
        	}
        	
//        	map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecioVenta()));
        	map.put("presentacion", String.valueOf(articulo[i].getUnidadesFardo()));
        	map.put("existencia", "0");
        	map.put("bonificacion", "0");
        	map.put("total", "0.00");
        	mylist.add(map);
        }
		return mylist;
	}
	
	public ArrayList<HashMap<String, String>> pedidoTemp(Context context, int numeroPedido) {
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		DetallePedido[] articulo = requestDB.buscaDetallePedido(context, numeroPedido);
		
		int tamano = articulo.length;
		for (int i = 0; i < tamano; i++){
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("codigoProducto", articulo[i].getCodigo());
        	map.put("nombreProducto", articulo[i].getNombre());
        	map.put("cajas", String.valueOf(articulo[i].getCaja()));
        	map.put("unidades", String.valueOf(articulo[i].getUnidad()));
        	map.put("valor", formatDecimal.convierteFloat(articulo[i].getPrecio()));
        	map.put("presentacion", String.valueOf(articulo[i].getUnidadesFardo()));
        	map.put("existencia", "0");
        	map.put("bonificacion", "0");
        	map.put("total", formatDecimal.convierteFloat(articulo[i].getSubTotal()));
        	mylist.add(map);
//        	Verificar bonificacion de este artículo
        	ListaPromocion listaPromocion = new ListaPromocion();
        	listaPromocion = buscaBoni(context, articulo[i].getCodigo());
//        	Si encuentra artículos bonificados para este artículo, agrega el artículo bonificado
        	if (listaPromocion.getRespuesta().isResultado()){
        		
        		int unidadesCompra = articulo[i].getTotalUnidades();
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
//        			for (int a = 0; a < cantidadBoni; a++){
        				HashMap<String, String> mapBoni = new HashMap<String, String>();
                    	mapBoni.put("codigoProducto", "BONI");
                    	mapBoni.put("nombreProducto", listaPromocion.getPromocion()[0].getArtDescripcionBoni());
                    	mapBoni.put("cajas", String.valueOf(listaPromocion.getPromocion()[0].getFardosBoni()));
                    	mapBoni.put("unidades", String.valueOf(cantidadBoni * listaPromocion.getPromocion()[0].getUnidadesBoni()));
                    	mapBoni.put("valor", formatDecimal.convierteFloat(listaPromocion.getPromocion()[0].getPrecioVentaBoni()));
                    	mapBoni.put("presentacion", String.valueOf(listaPromocion.getPromocion()[0].getUnidadesBoni()));
                    	mapBoni.put("existencia", "0");
                    	mapBoni.put("bonificacion", "0");
                    	mapBoni.put("total", formatDecimal.convierteFloat(precioBoni * totalUnidadesBoni));
                    	mylist.add(mapBoni);
//        			}
        		}
        	}
        }
		return mylist;
	}

	public ArrayList<HashMap<String, String>> ListaClientesVisitados (Context context){
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		Cliente[] cliente = requestDB.clientePendienteDB(context);
		//int tamano = cliente.length;
		for (int i = 0; i < cliente.length; i++){
			String visitado = cliente[i].getVisitado();
			if (visitado.equalsIgnoreCase(fecha.fechaInversa())){
				if (rm.semanaVisitaHoy(cliente[i].getSemana()) && rm.diaVisitaHoy(cliente[i].getDiaVisita())){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("codigoCliente", cliente[i].getCliCodigo());
			        map.put("empresa", cliente[i].getCliEmpresa());
			        map.put("contacto", cliente[i].getCliContacto());
			        map.put("direccion", cliente[i].getCliDireccion());
			        map.put("telefono", cliente[i].getCliTelefono());
			        map.put("nit", cliente[i].getCliNit());
			        mylist.add(map);
				}
			}
		}
		return mylist;
	}
	
	public ArrayList<HashMap<String, String>> listaPedidos(Context context) {
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		EncabezadoPedido[] encabezados = requestDB.encabezadoPedido(context);
		int tamano = encabezados.length;
		Log.e("Peticion", "encabezados = " + tamano);
		for (int i = 0; i < tamano; i++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("codigoCliente", encabezados[i].getCodigoCliente());
				map.put("total", String.valueOf(encabezados[i].getTotal()));
				map.put("fecha", encabezados[i].getFecha());
				map.put("hora", encabezados[i].getHora());
				mylist.add(map);
		}
		return mylist;
	}

	public Cliente DetalleCliente(Context context, String codigoCliente) {
		Cliente cliente = new Cliente();
		cliente = requestDB.buscaCliente(context, codigoCliente);
		Log.e("TT", "Peticion.detalleCliente = " + cliente.getCliCodigo());
		return cliente;
	}
	
	public void pedidosPendientes(Context context){
		try{
			if (connectState.isConnectedToInternet(context)){
				//Enviar Pedidos Pendientes
				EncabezadoPedido[] pedidos = requestDB.encabezadoPedidoNoSinc(context);
				int tamano = pedidos.length;
				Log.e("TT", "Peticion.pedidosPendientes - tamano == " + tamano);
				for (int i = 0; i < tamano; i++){
					if (pedidos[i].getMotivo() == 0){
						String ruta = rutaCliente(context, pedidos[i]);
						enviarPedido(context, pedidos[i], (int)pedidos[i].getId(), ruta);
					}else{
						Log.e("TT", "Peticion.pedidosPendientes - motivo == 1");
						enviarMotivoPendiente(context, pedidos[i].getId(), pedidos[i].getCodigoCliente(), pedidos[i].getMotivoNoCompra());
					}
					
				}
				
				//Enviar nuevos clientes pendientes
			}
		}catch(Exception exception){
			
		}
		
	}
	
	public String rutaCliente (Context context, EncabezadoPedido encabezado){
		String ruta = requestDB.rutaCliente(context, encabezado.getCodigoCliente());
		return ruta;
	}

	public DetallePedido buscaArticulo(Context context, String codigoProducto, int precio) {
		DetallePedido articulo = new DetallePedido();
		articulo = requestDB.buscaArticulo(context, codigoProducto, precio);
		
		// TODO Auto-generated method stub
		return articulo;
	}
	
	public DetallePedido buscaArticuloPedido(Context context, String codigoProducto, int numeroPedido) {
		DetallePedido articulo = new DetallePedido();
		articulo = requestDB.buscaArticuloPedido(context, codigoProducto, numeroPedido);
		
		// TODO Auto-generated method stub
		return articulo;
	}
	
	public ListaPromocion buscaBoni(Context context, String idArticulo) {
		ListaPromocion promociones = new ListaPromocion();
		promociones = requestDB.buscaBoni(context, idArticulo);
		System.out.println("resultado buscaBoni = " + promociones.getRespuesta().isResultado());
		return promociones;
	}
	
	public void eliminaArticuloPedido(Context context, long idDb) {
		requestDB.eliminaArticuloPedido(context, idDb);
	}

	public int numeroPedido(Context context) {
		int numero = 0;
		numero = requestDB.ultimoEncabezado(context);
		return numero;
	}

	public void enviarPedido(Context context, EncabezadoPedido encabezado, int numeroPedido, String ruta) {
		requestDB.actualizarCampoVisitado(context, encabezado.getCodigoCliente());
		if (connectState.isConnectedToInternet(context)){
			Pedido pedido = new Pedido();
			pedido.setEncabezadoPedido(encabezado);
			pedido.setDetallePedido(requestDB.buscaDetallePedido(context, numeroPedido));
			Vendedor vendedor = new Vendedor();
			vendedor = requestDB.vendedorDB(context);
			RespuestaWS respuesta = new RespuestaWS();
			respuesta = requestWS.enviaPedido(context, pedido, ruta, vendedor.getIdusuario());
			if (!respuesta.isResultado()){
				requestWS.clienteVisitado(pedido.getEncabezadoPedido().getCodigoCliente());
				Log.e("TT", "resultado del envio = " + respuesta.getMensaje());
				requestDB.actualizarCampoVisitado(context, encabezado.getCodigoCliente());
				requestDB.actualizarSincEncabezadoPedido(context, numeroPedido);
				
			}
		}
	}
	
	public RespuestaWS enviarMotivo(Context context, String codigoCliente, String motivoSeleccionado) {
		requestDB.actualizarCampoVisitado(context, codigoCliente);
		int numeroPedido = requestDB.ultimoEncabezado(context); 
		requestDB.enviarMotivo(context, codigoCliente, motivoSeleccionado);
		RespuestaWS respuesta = new RespuestaWS();
		Vendedor vendedor = new Vendedor();
		vendedor = requestDB.vendedorDB(context);
		String ruta = requestDB.rutaCliente(context, codigoCliente);
		if (connectState.isConnectedToInternet(context)){
			respuesta = requestWS.enviarMotivo(codigoCliente, ruta, vendedor.getIdusuario(), motivoSeleccionado);
			if (!respuesta.isResultado()){
				requestDB.actualizarCampoVisitado(context, codigoCliente);
				requestDB.actualizarSincEncabezadoPedido(context, numeroPedido);
			}
		}else{
			respuesta.setResultado(false);
			respuesta.setMensaje("No cuenta con conexión a Internet");
		}
		return null;
	}
	
	public RespuestaWS enviarMotivoPendiente(Context context,Long numeroPedido, String codigoCliente, String motivoSeleccionado) {
		RespuestaWS respuesta = new RespuestaWS();
		Vendedor vendedor = new Vendedor();
		vendedor = requestDB.vendedorDB(context);
		
		String ruta = requestDB.rutaCliente(context, codigoCliente);
		if (connectState.isConnectedToInternet(context)){
			respuesta = requestWS.enviarMotivo(codigoCliente, ruta, vendedor.getIdusuario(), motivoSeleccionado);
			Log.e("TT", "Peticion.enviarMotivoPendiente - respuesta = " + respuesta.isResultado());
			if (respuesta.isResultado()){
				Log.e("TT", "Peticion.enviarMotivoPendiente");
				requestDB.actualizarCampoVisitado(context, codigoCliente);
				requestDB.actualizarSincEncabezadoPedido(context, numeroPedido);
				return respuesta;
			}return respuesta;
		}else{
			respuesta.setResultado(false);
			respuesta.setMensaje("No cuenta con conexión a Internet");
		}
		return null;
	}
	
	public RespuestaWS enviarNuevoCliente(Context context, ClienteNuevo cliente) {
		RespuestaWS respuesta = new RespuestaWS();
		Vendedor vendedor = new Vendedor();
		vendedor = requestDB.vendedorDB(context);
		Fecha fecha = new Fecha();
		try{
			if (connectState.isConnectedToInternet(context)){
				
				System.out.println("enviarNuevoCliente.....");
				Mail m = new Mail("walvarado@researchmobile.co", "JavaBuilder");
				System.out.println("enviarNuevoCliente.....");
			      String[] toArr = {"eclaudio@grupotodoterreno.com", "william.ale20@gmail.com", "wlevy@researchmobile.co", "walvarado@researchmobile.co"};
//			      "eclaudio@grupotodoterreno.com", "william.ale20@gmail.com", "wlevy@researchmobile.co", 
			      m.set_to(toArr); 
			      m.set_from("todoterrenosc@gmail.com"); 
			      m.set_subject("Peticion de nuevo Cliente"); 
			      m.setBody("El vendedor " + vendedor.getNombre() + ", ha solicitado la creación de un nuevo cliente en la ruta " + cliente.getRuta() + " con los siguientes datos: " + 
			    		  "\nNombre de Negocio: " + cliente.getNombreNegocio() +
			    		  "\nNit: " + cliente.getNit() +
			    		  "\nContacto: " + cliente.getNombreContacto() +
			    		  "\nTelefono: " + cliente.getTelefono() +
			    		  "\nDirección: " + cliente.getDireccion() +
			    		  "\nDias de Visita: " + cliente.getDiaVisita() +
			    		  "\nFecha : " + fecha.FechaHoy() +
			    		  "\nHora : " + fecha.Hora());
			      if(m.send()) { 
			    	  respuesta.setResultado(true);
			    	  respuesta.setMensaje("correo enviado");
			    	  return respuesta;
			        } else { 
			        respuesta.setResultado(false);
				    respuesta.setMensaje("correo no enviado");
			        return respuesta;   
			        } 
			    }else{
			    	respuesta.setResultado(false);
				    respuesta.setMensaje("los datos se guardaran en la base de datos");
			    }
			
		}catch(Exception exception){
			respuesta.setResultado(false);
		    respuesta.setMensaje("error en la creacion del cliente");
		}
		return null;
	}

	public float totalGeneral(Context context) {
		float total = requestDB.totalVentas(context);
		return total;
	}
	
	public int totalEnviados(Context context) {
		int total = requestDB.pedidosSincronizados(context);
		return total;
	}
	
	public int totalPendientes(Context context) {
		int total = requestDB.pedidosNoSincronizados(context);
		return total;
	}
	public NoVenta[] noVenta(Context context) {
		NoVenta[] noVenta = requestDB.noVentaDB(context);
		return noVenta;
	}
	public ListaCategoria buscaCategoria(Context context) {
		ListaCategoria listaCategoria = requestDB.buscaCategoria(context);
		return listaCategoria;
	}
	public void cancelarPedido(Context context, int numeroPedido) {
		requestDB.cancelarPedido(context, numeroPedido);
		
	}
	public void editaArticuloPedido(Context context, DetallePedido articuloSeleccionado) {
		requestDB.editarArticuloPedido(context, articuloSeleccionado);		
	}
	public float totalActual(Context context, int numeroPedido) {
		float totalActual = requestDB.totalActual(context, numeroPedido);
		return totalActual;
	}
	public Vendedor vendedor(Context context) {
		Vendedor vendedor = requestDB.vendedorDB(context);
		return vendedor;
	}
	public int precioAplica(Context context, String codigoCliente) {
		Cliente cliente = requestDB.buscaCliente(context, codigoCliente);
		Log.d("TT", "buscando precio para cliente = " + codigoCliente);
		int precio = 0;
		Log.d("TT", "buscando precio para cliente = " + codigoCliente);
		if (!cliente.getCliDes1().equalsIgnoreCase("0")){
			Log.d("TT", "buscando precio1 para cliente = " + codigoCliente);
			precio = 1;
		}
		if (!cliente.getCliDes2().equalsIgnoreCase("0")){
			Log.d("TT", "buscando precio2 para cliente = " + codigoCliente);
			precio = 2;
		}
		if (!cliente.getCliDes3().equalsIgnoreCase("0")){
			Log.d("TT", "buscando precio3 para cliente = " + codigoCliente);
			precio = 3;
		}
		return precio;
	}
	
}
