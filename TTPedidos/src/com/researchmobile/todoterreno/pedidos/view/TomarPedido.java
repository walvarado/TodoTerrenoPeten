package com.researchmobile.todoterreno.pedidos.view;
/**
 * El precio que aplica se maneja de la seiguiente manera
 * Si clidesnormal = 1 -> PRECIO = 0
 * Si clides1 = 1 -> PRECIO = 1
 * Si clides2 = 1 -> PRECIO = 2
 * Si clides3 = 1 -> PRECIO = 3
 * 
 * El precio que se muestra es
 * Si PRECIO = 0 -> artprecioventa
 * Si PRECIO = 1 -> artpreciodes1
 * Si PRECIO = 2 -> artpreciodes2
 * Si PRECIO = 3 -> artpreciodes3
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.researchmobile.todoterreno.pedidos.entity.DetallePedido;
import com.researchmobile.todoterreno.pedidos.entity.EncabezadoPedido;
import com.researchmobile.todoterreno.pedidos.entity.ListaPromocion;
import com.researchmobile.todoterreno.pedidos.entity.Pedido;
import com.researchmobile.todoterreno.pedidos.utility.Fecha;
import com.researchmobile.todoterreno.pedidos.utility.FormatDecimal;
import com.researchmobile.todoterreno.pedidos.utility.Mensaje;
import com.researchmobile.todoterreno.pedidos.ws.Peticion;

public class TomarPedido extends Activity implements TextWatcher, OnItemClickListener, OnClickListener, OnKeyListener {
	
	private int PRECIO = 0;
	private ProgressDialog pd = null;
	private TextView totalGeneralTextView;
	private EditText buscarEditText;
	private ImageButton borrarImageButton;
	private ArrayList<HashMap<String, String>> articulosHashMap;
	private ArrayList<HashMap<String, String>> pedidoHashMap;
	private SimpleAdapter simpleAdapter;
	private ListView articulosListView;
	private Peticion peticion;
	private Pedido pedido;
	private float total;
	private DetallePedido articuloSeleccionado;
	private EncabezadoPedido encabezado;
	private Fecha fecha;
	private Mensaje mensaje;
	private int numeroPedido;
	private String codigoCliente;
	private String ruta;
	private boolean nuevo;
	private FormatDecimal formatDecimal;
	private ListaPromocion listaPromocion;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tomar_pedido);
		Bundle bundle = (Bundle)getIntent().getExtras();
		setCodigoCliente((String)bundle.getString("codigoCliente"));
		setRuta((String)bundle.getString("idRuta"));
		Log.e("TT", "idruta = " + getRuta());
		setNuevo(true);
		setMensaje(new Mensaje());
		setTotal(0);
		setFecha(new Fecha());
		setTotalGeneralTextView((TextView)findViewById(R.id.tomar_pedido_total_textview));
		getTotalGeneralTextView().setText(String.valueOf(getTotal()));
		setBuscarEditText((EditText)findViewById(R.id.tomar_pedido_buscar_edittext));
		setBorrarImageButton((ImageButton)findViewById(R.id.tomar_pedido_borrar_imagebutton));
		setArticulosListView((ListView)findViewById(R.id.tomar_pedido_lista_productos_listview));
		setPeticion(new Peticion());
		setPedido(new Pedido());
		PRECIO = getPeticion().precioAplica(this, getCodigoCliente());
		setFormatDecimal(new FormatDecimal());
		
		getBuscarEditText().setOnKeyListener(this);
		getBuscarEditText().addTextChangedListener(this);
		getBorrarImageButton().setOnClickListener(this);
		getArticulosListView().setOnItemClickListener(this);
		
		new articulosAsync().execute("");
	}

	@Override
	public void onClick(View view) {
		if (view == getBorrarImageButton()){
			getBuscarEditText().setText("");
		}
		
	}	
	
	@Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
        {
       	 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getBuscarEditText().getWindowToken(), 0);
       	 return true;
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            //TODO: When the enter key is pressed
            return true;
        }
        return false;
    }
	
	public DetallePedido seleccionaArticulo(String codigoProducto){
		DetallePedido articulo = new DetallePedido();
		if (isNuevo()){
			articulo = getPeticion().buscaArticulo(TomarPedido.this, codigoProducto, PRECIO);
		}else{
			articulo = getPeticion().buscaArticuloPedido(TomarPedido.this, codigoProducto, getNumeroPedido());
		}
		
		return articulo;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		   @SuppressWarnings("unchecked")
           HashMap<String, String> selected = (HashMap<String, String>) getSimpleAdapter().getItem(position);
           String codigoProducto = selected.get("codigoProducto");
           if (codigoProducto.equalsIgnoreCase("BONI")){
        	   Toast.makeText(getBaseContext(), "Este producto es una Bonificación, No puede editar", Toast.LENGTH_SHORT).show();
           }else{
        	   setArticuloSeleccionado(seleccionaArticulo(codigoProducto));
               agregarDialog();
           }
           
    }
	
	/**
	 * MENU
	 */

	// To change item text dynamically
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (getTotal() < 0.1){
			menu.getItem(3).setEnabled(false);
		}else{
			menu.getItem(3).setEnabled(true);
		}
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tomar_pedido_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tomar_pedido_ver_pedido_opcion:
			//setControlMenu(false);
			verPedido();
			return true;
		
		case R.id.tomar_pedido_agregar_opcion:
			//setControlMenu(true);
			agregarArticulo();
			return true;
			
		case R.id.tomar_pedido_cancelar_opcion:
			cancelarPedido();
			return true;
		case R.id.tomar_pedido_enviar_opcion:
			enviarPedido();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void agregarArticulo(){
		setNuevo(true);
		new articulosAsync().execute("");
	}
	
	public void verPedido(){
		setNuevo(false);
		new pedidoAsync().execute("");
	}
	
	public void enviarPedido(){
		new AlertDialog.Builder(this)
        .setTitle("Enviar Pedido")
        .setMessage("¿Esta seguro de enviar el pedido?")
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                     new enviarAsync().execute("");
                     
                }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                     
                }
        })
        .show();
	}
	
	public void terminarPedido(){
		llenaEncabezado();
		getPeticion().pedidosPendientes(this);
		getPeticion().insertaEncabezado(this, getEncabezado());	
		getPeticion().enviarPedido(this, getEncabezado(), getNumeroPedido(), getRuta());
	}
	
	public void llenaEncabezado(){
		setEncabezado(new EncabezadoPedido());
		getEncabezado().setCodigoCliente(getCodigoCliente());
		getEncabezado().setCodigoPedidoTemp(String.valueOf(getNumeroPedido()));
		getEncabezado().setCredito("0");
		getEncabezado().setFecha(getFecha().FechaHoy());
		getEncabezado().setHora(getFecha().Hora());
		getEncabezado().setTotal(getTotal());
	}
	
	public void cancelarPedido(){
		new AlertDialog.Builder(this)
        .setTitle("Cancelar Pedido")
        .setMessage("¿Esta seguro de cancelar el pedido?")
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	getPeticion().cancelarPedido(TomarPedido.this, getNumeroPedido());
                     finish();
                }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                     
                }
        })
        .show();

	}

	private void agregarDialog() {
				
		LayoutInflater factory = LayoutInflater.from(TomarPedido.this);            
     
		final View textEntryView = factory.inflate(R.layout.dialog_tomar_pedido, null);
		final EditText cajaEditText = (EditText) textEntryView.findViewById(R.id.tomarpedido_dialog_caja_edittext);
		final EditText unidadEditText = (EditText) textEntryView.findViewById(R.id.tomarpedido_dialog_unidad_edittext);
		final TextView precioTextViewDialog = (TextView) textEntryView.findViewById(R.id.precioTextViewDialog);
		
//		Elementos para mostrar bonificación
		final LinearLayout boniLayout = (LinearLayout) textEntryView.findViewById(R.id.boni_layout);
		final TextView descripcionBoni = (TextView) textEntryView.findViewById(R.id.boni_descripcion_textview);
//		descripcionBoni.setVisibility(View.INVISIBLE);
		buscaPromocion(getArticuloSeleccionado().getCodigo());
		if (getListaPromocion().getRespuesta().isResultado()){
			Log.e("TT", "promocion es true = ");
//			descripcionBoni.setVisibility(View.VISIBLE);
				descripcionBoni.setText("Por la compra de " +
						getListaPromocion().getPromocion()[0].getFardosCompra() + 
						" fardos y " + 
						getListaPromocion().getPromocion()[0].getUnidadesCompra() +
						" unidades recibe " +
						getListaPromocion().getPromocion()[0].getFardosBoni() +
						" fardos y " +
						getListaPromocion().getPromocion()[0].getUnidadesBoni() +
						" unidades de " +
						getListaPromocion().getPromocion()[0].getArtDescripcionBoni() + 
						" a Q." + 
						getListaPromocion().getPromocion()[0].getPrecioVentaBoni());
		}
		
		if (getArticuloSeleccionado().getCaja() > 0){
			cajaEditText.setText(String.valueOf(getArticuloSeleccionado().getCaja()));
		}
		if (getArticuloSeleccionado().getUnidad() > 0){
			unidadEditText.setText(String.valueOf(getArticuloSeleccionado().getUnidad()));
		}
		precioTextViewDialog.setText(getFormatDecimal().convierteFloat(getArticuloSeleccionado().getPrecio()));
		final AlertDialog.Builder alert = new AlertDialog.Builder(TomarPedido.this);
		alert.setTitle(getArticuloSeleccionado().getNombre());
		alert.setView(textEntryView);
		alert.setPositiveButton("   OK   ", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton) {
			insertaDetalle();
			actualizaTotalGeneral();
		}

			private void insertaDetalle() {
				String cajaTemp = cajaEditText.getText().toString();
				String unidadTemp = unidadEditText.getText().toString();
				
				int cajas = 0;
				int unidades = 0;
				
				if (!cajaTemp.equalsIgnoreCase("")){
					cajas = Integer.parseInt(cajaEditText.getText().toString());
				}
				
				if (!unidadTemp.equalsIgnoreCase("")){
					unidades = Integer.parseInt(unidadEditText.getText().toString());
				}
				
				int totalUnidades = (unidades + (cajas * getArticuloSeleccionado().getUnidadesFardo()));
				float subTotal = totalUnidades * getArticuloSeleccionado().getPrecio();
				
				getArticuloSeleccionado().setCaja(cajas);
				getArticuloSeleccionado().setUnidad(unidades);
				getArticuloSeleccionado().setTotalUnidades(totalUnidades);
				getArticuloSeleccionado().setSubTotal(subTotal);
				if(isNuevo()){
					getPeticion().insertaArticuloTemp(TomarPedido.this, getArticuloSeleccionado(), getNumeroPedido());
				}else{
					getPeticion().editaArticuloPedido(TomarPedido.this, getArticuloSeleccionado());
					actualizaTotalGeneral();
					verPedido();
				}
				
			}
		
     });

     alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener()
     {
         public void onClick(DialogInterface dialog, int whichButton)
         {
         // Canceled.
         }
     });
     
     alert.setNeutralButton("X", new DialogInterface.OnClickListener()
     {
         public void onClick(DialogInterface dialog, int whichButton)
         {
        	 if (!isNuevo()){
					eliminarArticulo();
				}else{
					cajaEditText.setText("");
					unidadEditText.setText("");
				}
         }
     });
     alert.show();
	}
	
	private void buscaPromocion(String idArticulo){
		setListaPromocion(getPeticion().buscaBoni(this, idArticulo));
	}

	private void eliminarArticulo() {
		getPeticion().eliminaArticuloPedido(TomarPedido.this, getArticuloSeleccionado().getIdDb());
		actualizaTotalGeneral();
		verPedido();
	}
	protected void actualizaTotalGeneral() {
		setTotal(getPeticion().totalActual(TomarPedido.this, getNumeroPedido()));
		getTotalGeneralTextView().setText(getFormatDecimal().convierteFloat(getTotal()));
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		getSimpleAdapter().getFilter().filter(s);
		getSimpleAdapter().notifyDataSetChanged();
		
	}
	
	public void buscaArticulos(){
		Log.e("TT", "precio = ");
		setArticulosHashMap(getPeticion().listaArticulos(TomarPedido.this, PRECIO));
		setNumeroPedido(getPeticion().numeroPedido(this));
		Log.e("TT", "numero pedido = " + getNumeroPedido());
	}
	
	public void llenaListaArticulos(){
		setSimpleAdapter( 
	        	new SimpleAdapter(this, 
	        					  getArticulosHashMap(),	
	        					  R.layout.fila_lista_productos,
	        					  new String[] {"codigoProducto",
	        									"nombreProducto", 
	        									"cajas",
												"unidades",
												"valor",
												"presentacion",
												"existencia",
												"bonificacion",
												"total"},
	        					  new int[] {R.id.fila_lista_producto_codigo_textview, 
	        								 R.id.fila_lista_producto_nombreProducto_textview, 
	        								 R.id.fila_lista_producto_cajas_textview,
	        								 R.id.fila_lista_producto_unidades_textview,
	        								 R.id.fila_lista_producto_precioi_textview,
	        								 R.id.fila_lista_producto_presentacion_textview,
	        								 R.id.fila_lista_producto_existencia_textview,
	        								 R.id.fila_lista_producto_bonificacion_textview,
	        								 R.id.fila_lista_producto_valor_textview}));
		getArticulosListView().setAdapter(getSimpleAdapter());
	}
	
	// Clase para ejecutar en Background
    class articulosAsync extends AsyncTask<String, Integer, Integer> {

          // Metodo que prepara lo que usara en background, Prepara el progress
          @Override
          protected void onPreExecute() {
                pd = ProgressDialog. show(TomarPedido.this, "VERIFICANDO DATOS", "ESPERE UN MOMENTO");
                pd.setCancelable( false);
         }

          // Metodo con las instrucciones que se realizan en background
          @Override
          protected Integer doInBackground(String... urlString) {
                try {
                	buscaArticulos();
               } catch (Exception exception) {

               }
                return null ;
         }

          // Metodo con las instrucciones al finalizar lo ejectuado en background
          protected void onPostExecute(Integer resultado) {
                pd.dismiss();
                llenaListaArticulos();

         }

   }

 // Clase para ejecutar en Background
    class pedidoAsync extends AsyncTask<String, Integer, Integer> {

          // Metodo que prepara lo que usara en background, Prepara el progress
          @Override
          protected void onPreExecute() {
                pd = ProgressDialog. show(TomarPedido.this, "VERIFICANDO DATOS", "ESPERE UN MOMENTO");
                pd.setCancelable( false);
         }

          // Metodo con las instrucciones que se realizan en background
          @Override
          protected Integer doInBackground(String... urlString) {
                try {
                	buscaPedido();
               
               } catch (Exception exception) {

               }
                return null ;
         }

          // Metodo con las instrucciones al finalizar lo ejectuado en background
          protected void onPostExecute(Integer resultado) {
                pd.dismiss();
                llenaListaPedido();

         }

   }
    
 // Clase para ejecutar en Background
    class enviarAsync extends AsyncTask<String, Integer, Integer> {

          // Metodo que prepara lo que usara en background, Prepara el progress
          @Override
          protected void onPreExecute() {
                pd = ProgressDialog. show(TomarPedido.this, "VERIFICANDO DATOS", "ESPERE UN MOMENTO");
                pd.setCancelable( false);
         }

          // Metodo con las instrucciones que se realizan en background
          @Override
          protected Integer doInBackground(String... urlString) {
                try {
                	terminarPedido();
               
               } catch (Exception exception) {

               }
                return null ;
         }

          // Metodo con las instrucciones al finalizar lo ejectuado en background
          protected void onPostExecute(Integer resultado) {
                pd.dismiss();
                Intent intent = new Intent(TomarPedido.this, Rol.class);
                startActivity(intent);
         
         }

   }
    
    
    public void llenaListaPedido(){
    	setSimpleAdapter( 
	        	new SimpleAdapter(this, 
	        					  getPedidoHashMap(),	
	        					  R.layout.fila_lista_productos,
	        					  new String[] {"codigoProducto",
	        									"nombreProducto", 
	        									"cajas",
												"unidades",
												"valor",
												"presentacion",
												"existencia",
												"bonificacion",
												"total"},
	        					  new int[] {R.id.fila_lista_producto_codigo_textview, 
	        								 R.id.fila_lista_producto_nombreProducto_textview, 
	        								 R.id.fila_lista_producto_cajas_textview,
	        								 R.id.fila_lista_producto_unidades_textview,
	        								 R.id.fila_lista_producto_precioi_textview,
	        								 R.id.fila_lista_producto_presentacion_textview,
	        								 R.id.fila_lista_producto_existencia_textview,
	        								 R.id.fila_lista_producto_bonificacion_textview,
	        								 R.id.fila_lista_producto_valor_textview}));
		getArticulosListView().setAdapter(getSimpleAdapter());
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
              // Preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR     
              return true;
        }
        
        return super.onKeyDown(keyCode, event);
      }

    
    public void buscaPedido(){
    	setPedidoHashMap(getPeticion().pedidoTemp(this, getNumeroPedido()));
    }
    
	public EditText getBuscarEditText() {
		return buscarEditText;
	}

	public void setBuscarEditText(EditText buscarEditText) {
		this.buscarEditText = buscarEditText;
	}

	public ImageButton getBorrarImageButton() {
		return borrarImageButton;
	}

	public void setBorrarImageButton(ImageButton borrarImageButton) {
		this.borrarImageButton = borrarImageButton;
	}

	public ArrayList<HashMap<String, String>> getArticulosHashMap() {
		return articulosHashMap;
	}

	public void setArticulosHashMap(
			ArrayList<HashMap<String, String>> articulosHashMap) {
		this.articulosHashMap = articulosHashMap;
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public ListView getArticulosListView() {
		return articulosListView;
	}

	public void setArticulosListView(ListView articulosListView) {
		this.articulosListView = articulosListView;
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public TextView getTotalGeneralTextView() {
		return totalGeneralTextView;
	}

	public void setTotalGeneralTextView(TextView totalGeneralTextView) {
		this.totalGeneralTextView = totalGeneralTextView;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public DetallePedido getArticuloSeleccionado() {
		return articuloSeleccionado;
	}

	public void setArticuloSeleccionado(DetallePedido articuloSeleccionado) {
		this.articuloSeleccionado = articuloSeleccionado;
	}

	public Fecha getFecha() {
		return fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public EncabezadoPedido getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(EncabezadoPedido encabezado) {
		this.encabezado = encabezado;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public ArrayList<HashMap<String, String>> getPedidoHashMap() {
		return pedidoHashMap;
	}

	public void setPedidoHashMap(ArrayList<HashMap<String, String>> pedidoHashMap) {
		this.pedidoHashMap = pedidoHashMap;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public FormatDecimal getFormatDecimal() {
		return formatDecimal;
	}

	public void setFormatDecimal(FormatDecimal formatDecimal) {
		this.formatDecimal = formatDecimal;
	}

	public ListaPromocion getListaPromocion() {
		return listaPromocion;
	}

	public void setListaPromocion(ListaPromocion listaPromocion) {
		this.listaPromocion = listaPromocion;
	}
	
}
