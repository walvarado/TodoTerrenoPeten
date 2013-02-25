package com.researchmobile.todoterreno.pedidos.ws;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.researchmobile.todoterreno.pedidos.entity.Articulo;
import com.researchmobile.todoterreno.pedidos.entity.Cliente;
import com.researchmobile.todoterreno.pedidos.entity.ClienteNuevo;
import com.researchmobile.todoterreno.pedidos.entity.ListaArticulos;
import com.researchmobile.todoterreno.pedidos.entity.ListaCategoria;
import com.researchmobile.todoterreno.pedidos.entity.ListaClientes;
import com.researchmobile.todoterreno.pedidos.entity.ListaPromocion;
import com.researchmobile.todoterreno.pedidos.entity.LoginEntity;
import com.researchmobile.todoterreno.pedidos.entity.NoVenta;
import com.researchmobile.todoterreno.pedidos.entity.Pedido;
import com.researchmobile.todoterreno.pedidos.entity.Portafolio;
import com.researchmobile.todoterreno.pedidos.entity.Promocion;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.entity.Ruta;
import com.researchmobile.todoterreno.pedidos.entity.User;
import com.researchmobile.todoterreno.pedidos.entity.Usuario;
import com.researchmobile.todoterreno.pedidos.entity.Vendedor;
import com.researchmobile.todoterreno.pedidos.utility.Fecha;
import com.researchmobile.todoterreno.pedidos.utility.rmString;

public class RequestWS {
	private static String WS_LOGIN = "ws_login.php?a=login&";
	private static String WS_CLIENTES = "ws_clientes.php?";
	private static String WS_PRODUCTOS = "ws_articulos.php?a=catalogo&idportafolio=";
	private static String WS_ENVIO = "json.php?username=";
	private static String WS_ENVIAMOTIVO = "ws_noventa.php?cliente=";
	private static String WS_CATEGORIAS = "ws_categorias.php?";
	private static String WS_NUEVOCLIENTE = "ws_clientenuevo.php?idUsuario=";
	private static String WS_CLIENTEVISITADO = "ws_clientes.php?a=visitado&clicodigo=";
	private static String WS_PROMOCIONES = "ws_promociones.php?a=catalogo";
	
	// Metodo que llena el Entity del login
	@SuppressWarnings("unused")
	public LoginEntity login(String usuario, String password) {
		
		String url = WS_LOGIN + "usuario=" + usuario + "&" + "password=" + password; // string de conexi—n
		JSONObject jsonObject = ConnectWS.obtenerJson(url);
		Log.e("TT", "respuesta ws login = " + jsonObject.toString());
		LoginEntity loginEntity = new LoginEntity();
		try {
			
			if(jsonObject.has("resultado")){ // si se produjo un error al consumir el WS
				// creamos el LoginEntity y le asignamos el resultado recibido
				
				loginEntity.setRespuesta(new RespuestaWS());
				loginEntity.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
				loginEntity.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
				Log.e("TT", "RequestWS - login. resultado = " + loginEntity.getRespuesta().isResultado());
				if(loginEntity.getRespuesta().isResultado()){ //  si existe resultado relleno los campos del usuario
					loginEntity.setUsuario(new Usuario());
					JSONArray usuarioJsonArray = jsonObject.getJSONArray("usuario");
					JSONObject usuarioJsonObject = usuarioJsonArray.getJSONObject(0);
					loginEntity.getUsuario().setActivo(usuarioJsonObject.getString("activo"));
					loginEntity.getUsuario().setUsuario(usuarioJsonObject.getString("usuario"));
					loginEntity.getUsuario().setPassword(usuarioJsonObject.getString("password"));
					loginEntity.getUsuario().setLastLogin(usuarioJsonObject.getString("lastLogin"));
					loginEntity.getUsuario().setId(usuarioJsonObject.getString("id"));
					Log.e("TT", "RequestWS.login Usuario");
					if(jsonObject.has("vendedor")){ // si viene el Array de vendedor tambien ingreso los resultados al Entity del Login
						loginEntity.setVendedor(new Vendedor());
						JSONArray vendedorJsonArray = jsonObject.getJSONArray("vendedor");
						JSONObject vendedorJsonObject = vendedorJsonArray.getJSONObject(0);
						loginEntity.getVendedor().setVendedor(nullToString(vendedorJsonObject.getString("Vendedor")));
						loginEntity.getVendedor().setNombre(nullToString(vendedorJsonObject.getString("Nombre")));
						loginEntity.getVendedor().setDireccion(nullToString(vendedorJsonObject.getString("Direccion")));
						loginEntity.getVendedor().setTelefono(nullToString(vendedorJsonObject.getString("Telefono")));
						loginEntity.getVendedor().setIdentificacion(nullToString(vendedorJsonObject.getString("Identificacion")));
						loginEntity.getVendedor().setComision(nullToString(vendedorJsonObject.getString("Comision")));
						loginEntity.getVendedor().setRuta(nullToString(vendedorJsonObject.getString("Ruta")));
						loginEntity.getVendedor().setClidesnormal(nullToString(vendedorJsonObject.getString("clidesnormal")));
						loginEntity.getVendedor().setClides1(nullToString(vendedorJsonObject.getString("clides1")));
						loginEntity.getVendedor().setClides2(nullToString(vendedorJsonObject.getString("clides2")));
						loginEntity.getVendedor().setClides3(nullToString(vendedorJsonObject.getString("clides3")));
						loginEntity.getVendedor().setTurno(nullToString(vendedorJsonObject.getString("turno")));
						loginEntity.getVendedor().setOtnumero(nullToString(vendedorJsonObject.getString("otnumero")));
						loginEntity.getVendedor().setIdusuario(nullToString(vendedorJsonObject.getString("idusuario")));
						Log.e("TT", "RequestWS.login Vendedor");
					}else{
						System.out.println("No se obtuvieron datos del vendedor");
					}
					if(jsonObject.has("portafolios")){ // si viene el Array de portafolios asigno los campos al portafolio
						JSONArray portafoliosJsonArray = jsonObject.getJSONArray("portafolios");
						int tamano = portafoliosJsonArray.length();
						Portafolio[] portafolios = new Portafolio[tamano];
						for(int i=0; i < tamano; i++){
						JSONObject portafoliosJsonObject = portafoliosJsonArray.getJSONObject(0);
						Portafolio temp = new Portafolio();
						temp.setIdPortafolio(nullToString(portafoliosJsonObject.getString("IDportafolio")));
						temp.setDescripcion(nullToString(portafoliosJsonObject.getString("descripcion")));
						portafolios[i] = temp;
						} loginEntity.setPortafolio(portafolios);
						Log.e("TT", "RequestWS.login Portafolios");
					}else{
						System.out.println("No se obtuvieron datos del portafolios");
					}
					if(jsonObject.has("rutas")){ // si viene el Array de rutas asigno los campos al array de rutas y lo envio al LoginEntity
						JSONArray rutasJsonArray = jsonObject.getJSONArray("rutas");
						int tamano = rutasJsonArray.length();
						System.out.println("RWS - tamano rutas = " + tamano);
						Ruta[] rutas = new Ruta[tamano];
						for(int i=0; i < tamano; i++){ // recorro el Array para asignar cada registro a una variable a un objeto temporal y luego agregarlo al Array de listaClientes
							JSONObject rutasJsonObject = rutasJsonArray.getJSONObject(i);
							Ruta temp = new Ruta();
							temp.setId(nullToString(rutasJsonObject.getString("ID")));
							temp.setDescripcion(nullToString(rutasJsonObject.getString("Descripcion")));
						rutas[i] = temp;
						} loginEntity.setRuta(rutas);
						Log.e("TT", "RequestWS.login Rutas");
					}else{
						System.out.println("No se obtuvieron datos de las rutas");
					}
					
					if(jsonObject.has("motivonoventa")){ // si viene el Array de motivos no venta asigno los campos al array de no venta y lo envio al LoginEntity
						JSONArray noVentaJsonArray = jsonObject.getJSONArray("motivonoventa");
						int tamano = noVentaJsonArray.length();
						NoVenta[] noVenta = new NoVenta[tamano];
						Log.e("TT", "RequestWS.login tamaño noventa = " + tamano);
						for(int i=0; i < tamano; i++){ // recorro el Array para asignar cada registro a una variable a un objeto temporal y luego agregarlo al Array de listaClientes
							JSONObject noVentaJsonObject = noVentaJsonArray.getJSONObject(i);
							NoVenta temp = new NoVenta();
							//temp.setIdPortafolio(nullToString(rutasJsonObject.getString("IDportafolio")));
							temp.setId(nullToString(noVentaJsonObject.getString("IDnoventa")));
							temp.setDescripcion(nullToString(noVentaJsonObject.getString("descripnoventa")));
							noVenta[i] = temp;
						} loginEntity.setNoVenta(noVenta);
						Log.e("TT", "RequestWS.login No Venta");
						return loginEntity;
						
					}else{
						System.out.println("No se obtuvieron datos de los motivos No Venta");
					}
				}
				return loginEntity;
			}else{
				System.out.println("No se obtuvo resultado del JSON");	
				return null;    
			}
		} catch (Exception e) {
			System.out.println("OCURRIO UN ERROR AL PARSEAR EL JSON Error: " + e );
			return null;
		}

		
	}
	
//	Metodo usado para actualizar clientes
	public LoginEntity actualizaClientes() {
		String url = WS_LOGIN + "usuario=" + User.getUsername() + "&" + "password=" + User.getClave(); // string de conexi—n
		JSONObject jsonObject = ConnectWS.obtenerJson(url);
		Log.e("TT", "respuesta ws actualizar clientes = " + jsonObject.toString());
		LoginEntity loginEntity = new LoginEntity();
		
		try{
			if(jsonObject.has("resultado")){
				loginEntity.setRespuesta(new RespuestaWS());
				loginEntity.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
				loginEntity.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
				if(loginEntity.getRespuesta().isResultado()){ //  si existe resultado relleno los campos del usuario
					if(jsonObject.has("rutas")){ // si viene el Array de rutas asigno los campos al array de rutas y lo envio al LoginEntity
						JSONArray rutasJsonArray = jsonObject.getJSONArray("rutas");
						int tamano = rutasJsonArray.length();
						Ruta[] rutas = new Ruta[tamano];
						for(int i=0; i < tamano; i++){ // recorro el Array para asignar cada registro a una variable a un objeto temporal y luego agregarlo al Array de listaClientes
						JSONObject rutasJsonObject = rutasJsonArray.getJSONObject(i);
						Ruta temp = new Ruta();
						temp.setId(nullToString(rutasJsonObject.getString("ID")));
						temp.setDescripcion(nullToString(rutasJsonObject.getString("Descripcion")));
						rutas[i] = temp;
						} loginEntity.setRuta(rutas);
					}else{
						loginEntity.getRespuesta().setResultado(false);
						loginEntity.getRespuesta().setMensaje("No tiene rutas asignadas");
					}
				}
			}
			return loginEntity;
		}catch(Exception exception){
			loginEntity.getRespuesta().setResultado(false);
			loginEntity.getRespuesta().setMensaje("Ocurrio un problema en la consulta");
			return null;
		}
	}
	
	//Metodo captura caterog’a
	public ListaCategoria listaCategoria(){
		String url = WS_CATEGORIAS;
		
		JSONObject jsonObject = ConnectWS.obtenerJson(url);
		System.out.println("RESPUESTA WS LOGIN: " + jsonObject.toString());
		ListaCategoria listaCategoria = new ListaCategoria();
		try{
			if(jsonObject.has("resultado")){
				listaCategoria.setRespuesta(new RespuestaWS());
				listaCategoria.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
				listaCategoria.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
			}
		}catch(Exception e){
			
		}
		return null;
		
	}
	
//	Metodo que captura las promociones
	public ListaPromocion listaPromocion() {
		ListaPromocion listaPromocion = new ListaPromocion();
		RespuestaWS respuesta = new RespuestaWS();
		String url = WS_PROMOCIONES;
		int intentos = 0;
		do{
		
			JSONObject jsonObject = ConnectWS.obtenerJson(url);
		
		try{
			
			if (jsonObject != null){
				if (jsonObject.has("resultado")){
					respuesta.setResultado(jsonObject.getBoolean("resultado"));
					respuesta.setMensaje(jsonObject.getString("mensaje"));
					listaPromocion.setRespuesta(respuesta);
					if (respuesta.isResultado()){
						JSONArray jsonArray = jsonObject.getJSONArray("articulos");
						int tamano = jsonArray.length();
						Promocion[] promocion = new Promocion[tamano];
						for (int i = 0; i < tamano; i++){
							JSONObject jsonTemp = jsonArray.getJSONObject(i);
							Promocion promocionTemp = new Promocion();
							promocionTemp.setArtCodigo(nullToString(jsonTemp.getString("artcodigo")));
							promocionTemp.setArtUnidadesFardo(jsonTemp.getInt("artunidadesfardo"));
							promocionTemp.setFardosCompra(jsonTemp.getInt("fardosc"));
							promocionTemp.setUnidadesCompra(jsonTemp.getInt("unidadesc"));
							promocionTemp.setArtCodigoBoni(jsonTemp.getString("artcodigob"));
							promocionTemp.setArtDescripcionBoni(jsonTemp.getString("artdescripcionb"));
							promocionTemp.setFardosBoni(jsonTemp.getInt("fardosb"));
							promocionTemp.setUnidadesBoni(jsonTemp.getInt("unidadesb"));
							promocionTemp.setPrecioVentaBoni(jsonTemp.getInt("precioventab"));
							promocionTemp.setLimiteOfertaCliente(jsonTemp.getInt("limiteofertascli"));
							promocionTemp.setLimiteOfertasVenta(jsonTemp.getInt("limiteofertasven"));
							promocion[i] = promocionTemp;
							Log.e("TT", "promocione capturada = " + promocionTemp.getArtCodigo());
						}
						Log.e("TT", "promociones capturadas = " + promocion.length);
						listaPromocion.setPromocion(promocion);
						return listaPromocion;
					}else{
						return listaPromocion;
					}
				}
			}else{
				if (intentos < 3){
					intentos++;
					respuesta.setResultado(false);
					respuesta.setMensaje("No se han cargado las promociones");
					listaPromocion.setRespuesta(respuesta);
					return listaPromocion;
				}
			}
		}catch(Exception exception){
				respuesta.setResultado(false);
				respuesta.setMensaje("Error al cargar las promociones");
				listaPromocion.setRespuesta(respuesta);
				return listaPromocion;
		}
		}while(intentos < 3);
		return listaPromocion;
		
	}
	
	// Metodo que retorna la lista de clientes obtenidas desde el WS se necesita como parametros el cat‡logo y la ruta del vendedor
	public ListaClientes listaClientes(String catalogo, String ruta){
		String url = WS_CLIENTES + "a=" + catalogo + "&" + "idruta=" + ruta; // string de conexi—n
		JSONObject jsonObject = null;
		int intentos = 0;
		do{
			 jsonObject = ConnectWS.obtenerJson(url);
			if (jsonObject != null){
				intentos = 5;
			}else{
				intentos++;
			}
		}while (intentos < 4);
		Log.e("TT", "RequestWS.listaclientes intentos = " + intentos);
		Log.e("TT", "RequestWS.listaclientes jsonObject = " + jsonObject);
//		System.out.println("RESPUESTA WS LOGIN:" + jsonObject.toString());
		ListaClientes listaClientes = new ListaClientes(); // creamos el objeto que se va a retornar, instanciando la clase ListaClientes
		try {
				if(jsonObject.has("resultado")){ // si se produjo un error al consumir el WS
				// creamos el listaClientes y le asignamos el resultado recibido
					listaClientes.setRespuesta(new RespuestaWS());
					listaClientes.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
					listaClientes.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
					if(jsonObject.has("clientes")){
						JSONArray clientesJsonArray = jsonObject.getJSONArray("clientes"); // obtengo el Array de clientes que viene el el JSON
						Cliente[] clientes = new Cliente[clientesJsonArray.length()];
						for(int i=0;i<clientesJsonArray.length();i++){
							JSONObject clientesJsonObject = clientesJsonArray.getJSONObject(i);
							Cliente temp = new Cliente();
							temp.setCliCodigo(nullToString(clientesJsonObject.getString("clicodigo")));
							temp.setCliEmpresa(nullToString(clientesJsonObject.getString("cliempresa")));
							temp.setCliContacto(nullToString(clientesJsonObject.getString("clicontacto")));
							temp.setCliDireccion(nullToString(clientesJsonObject.getString("clidireccion")));
							temp.setCliNit(nullToString(clientesJsonObject.getString("clinit")));
							temp.setCliTelefono(nullToString(clientesJsonObject.getString("clitelefono")));
							temp.setDiaVisita(nullToString(clientesJsonObject.getString("diavisita")));
							temp.setCliRuta(nullToString(clientesJsonObject.getString("clruta")));
							temp.setSemana(nullToString(clientesJsonObject.getString("semana")));
							temp.setCliDesnormal(nullToString(clientesJsonObject.getString("clidesnormal")));
							temp.setCliDes1(nullToString(clientesJsonObject.getString("clides1")));
							temp.setCliDes2(nullToString(clientesJsonObject.getString("clides2")));
							temp.setCliDes3(nullToString(clientesJsonObject.getString("clides3")));
							temp.setCliSaldo(nullToString(clientesJsonObject.getString("clisaldo")));
							temp.setFechaVisitado(nullToString(clientesJsonObject.getString("fechavisitado")));
														
							clientes[i]=temp;
							
						} listaClientes.setCliente(clientes);
						return listaClientes;
						
					}else{
						return null;
					}
				}else{
					System.out.println("No se obtuvo resultado del WS");
					return null;
				}
			
			}catch(Exception e){
				System.out.println("OCURRIO UN ERROR AL PARSEAR EL JSON Error: " + e );
				return null;
				}
		
	}
	
	// Metodo que retorna la lista de clientes obtenidas desde el WS se necesita como parametros el cat‡logo y la ruta del vendedor
		public ListaArticulos listaArticulos(String portafolio){
			String url = WS_PRODUCTOS + portafolio; // string de conexi—n
			Log.e("TT", "RequestWS.listaArticulos");

			JSONObject jsonObject = ConnectWS.obtenerJson(url);
			System.out.println("RESPUESTA WS LOGIN:" + jsonObject.toString());
			ListaArticulos listaArticulos = new ListaArticulos(); // creamos el objeto que se va a retornar, instanciando la clase ListaArticulos
			try {
					if(jsonObject.has("resultado")){ // si se produjo un error al consumir el WS
					// creamos el listaArticulos y le asignamos el resultado recibido
						listaArticulos.setRespuesta(new RespuestaWS());
						listaArticulos.getRespuesta().setResultado(jsonObject.getBoolean("resultado"));
						listaArticulos.getRespuesta().setMensaje(jsonObject.getString("mensaje"));
						Log.e("TT", "RequestWS.listaArticulos Respuesta");
						if(jsonObject.has("articulos")){
							JSONArray articulosJsonArray = jsonObject.getJSONArray("articulos"); // obtengo el Array de clientes que viene el el JSON
							Articulo[] articulos = new Articulo[articulosJsonArray.length()];
							for(int i=0;i<articulosJsonArray.length();i++){
								JSONObject articulosJsonObject = articulosJsonArray.getJSONObject(i);
								Articulo temp = new Articulo();
								
								temp.setArtCodigo(nullToString(articulosJsonObject.getString("artcodigo")));
								temp.setArtDescripcion(nullToString(nullToString(articulosJsonObject.getString("artdescripcion"))));
								temp.setPrecioVenta(Float.parseFloat(nullToString(articulosJsonObject.getString("artprecioventa"))));
								temp.setPrecioDes1(Float.parseFloat(nullToString(articulosJsonObject.getString("artpreciodes1"))));
								temp.setPrecioDes2(Float.parseFloat(nullToString(articulosJsonObject.getString("artpreciodes2"))));
								temp.setPrecioDes3(Float.parseFloat(nullToString(articulosJsonObject.getString("artpreciodes3"))));
								temp.setPrecioOferta(Float.parseFloat(nullToString(articulosJsonObject.getString("artprecioferta"))));
								temp.setOfertado(Boolean.parseBoolean(nullToString(articulosJsonObject.getString("artofertado"))));
								temp.setArtOfertaFecha(nullToString(articulosJsonObject.getString("artofertafecha")));
								temp.setUnidadesFardo(Integer.parseInt(nullToString(articulosJsonObject.getString("artunidadesfardo"))));
								temp.setExistencia(nullToInteger(articulosJsonObject.getString("existencia")));
								
							    articulos[i]=temp; // asigno el articulo temporal al Array de articulos
								
							} listaArticulos.setArticulo(articulos);
							Log.e("TT", "RequestWS.listaArticulos Articulos");
							  return listaArticulos;
						}else{
							System.out.println("No se obtuvo el Array de articulos");
						}
						return listaArticulos;
					}else{
						System.out.println("No se obtuvo resultado del WS");
						return null;
					}
				
				}catch(Exception e){
					System.out.println("OCURRIO UN ERROR AL PARSEAR EL JSON Error: " + e );
					return null;
					}
			
		}
 //Metodo para enviar el pedido		
		public RespuestaWS enviaPedido(Context context, Pedido pedido, String ruta, String vendedor) {
			try{
			RespuestaWS respuesta = new RespuestaWS();
			rmString rm = new rmString();
			String jsonString = rm.jsonPedido(context, pedido, ruta, vendedor);
			String urlTemp = WS_ENVIO + User.getUsername() + "&password=" + User.getClave() + "&action=pedido&idruta=" + ruta + "&json=" + jsonString; // string de conexi—n
			Log.e("TT", "RequestWS.enviaPedido - url = " + urlTemp);
			String url = urlTemp.replace(" ", "%20");
			
			
			JSONObject jsonObject = ConnectWS.enviaPedidoJson(url);
			Log.e("TT", "RequestWS.enviaPedido respuesta = " + jsonObject.toString());
			if (jsonObject.has("error") ){
				respuesta.setResultado(jsonObject.getBoolean("error"));
				respuesta.setMensaje(jsonObject.getString("mensaje"));
				return respuesta;
			}
			}catch(Exception exception){
				return null;
			}
			return null;
		}
		
// Metodo para enviar el motivo no compra
		public RespuestaWS enviarMotivo(String codigoCliente, String ruta, String idusuario, String motivoSeleccionado) {
			try{
				Fecha fecha = new Fecha();
				RespuestaWS respuesta = new RespuestaWS();
				String urlTemp = WS_ENVIAMOTIVO + codigoCliente + "&fecha=" + fecha.FechaHoy() + "&hora=" + fecha.Hora() + "&idnoventa=" + motivoSeleccionado + "&vendedor=" + idusuario; 
				String url = urlTemp.replace(" ", "%20");
				JSONObject jsonObject = ConnectWS.enviaMotivoJson(url);
				if (jsonObject.has("resultado")){
					respuesta.setResultado(jsonObject.getBoolean("resultado"));
					respuesta.setMensaje(jsonObject.getString("mensaje"));
					return respuesta;
				}
			}catch(Exception exception){
				return null;
			}
			return null;
		}
		
		public void clienteVisitado(String codigoCliente) {
			try{
				String urlTemp = WS_CLIENTEVISITADO + codigoCliente;
				String url = urlTemp.replace(" ", "%20");
				ConnectWS.clienteVisitado(url);
				
			}catch(Exception exception){
				
			}
			
		}
		
		public RespuestaWS enviarNuevoCliente(ClienteNuevo cliente, String vendedor) {
			try{
				Fecha fecha = new Fecha();
				RespuestaWS respuesta = new RespuestaWS();
				rmString jsonString = new rmString();
				String json = jsonString.jsonNuevoCliente(cliente);
				
				String urlTemp = WS_NUEVOCLIENTE + vendedor + "&cliente=" + json; 
				String url = urlTemp.replace(" ", "%20");
				JSONObject jsonObject = ConnectWS.enviaNuevoCliente(url);
				if (jsonObject.has("resultado")){
					respuesta.setResultado(jsonObject.getBoolean("resultado"));
					respuesta.setMensaje(jsonObject.getString("mensaje"));
					return respuesta;
				}
			}catch(Exception exception){
				return null;
			}
			return null;
		}
	
	// Metodo convierte un valor null ingresado a un String y devuelve un espacio en blanco
	public String nullToString(String variable){
		if(variable == null){
			return " ";
		}else if (variable.equalsIgnoreCase("")){
			return " ";
		}
		return variable;
	}
	
	public int nullToInteger(String variable){
		if (variable == null){
			return 0;
		}else{
			int va = Integer.parseInt(variable);
			return va;
		}
	}
}
