package com.researchmobile.todoterreno.pedidos.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.researchmobile.todoterreno.pedidos.entity.Categoria;
import com.researchmobile.todoterreno.pedidos.entity.ClienteNuevo;
import com.researchmobile.todoterreno.pedidos.entity.ListaCategoria;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.entity.Vendedor;
import com.researchmobile.todoterreno.pedidos.utility.Fecha;
import com.researchmobile.todoterreno.pedidos.ws.Peticion;

public class NuevoCliente extends Activity implements OnClickListener, OnKeyListener{
	//nit, nombreNegocio, nombreContacto, telefono, direccion

	private EditText rutaEditText;
	private EditText nombreNegocioEditTExt;
	private EditText nitEditText;
	private EditText contactoEditText;
	private EditText telefonoEditText;
	private EditText direccionEditText;
	private Button crearButton;
	private Button cancelarButton;
	private CheckBox lunesCheckBox;
	private CheckBox martesCheckBox;
	private CheckBox miercolesCheckBox;
	private CheckBox juevesCheckBox;
	private CheckBox viernesCheckBox;
	private CheckBox sabadoCheckBox;
	
	private ListaCategoria listaCategoria;
	private ProgressDialog pd = null;
	private ArrayAdapter<Categoria> categoriaAdapter;
	private Peticion peticion = new Peticion();
	private RespuestaWS respuestaWS = new RespuestaWS();
	private String codigoNuevoCliente;
	private String ruta;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo);
        
        setRutaEditText((EditText)findViewById(R.id.nuevo_ruta_edittext));
        setNombreNegocioEditTExt((EditText)findViewById(R.id.nuevo_nombre_edittext));
        setNitEditText((EditText)findViewById(R.id.nuevo_nit_edittext));
        setContactoEditText((EditText)findViewById(R.id.nuevo_contacto_edittext));
        setTelefonoEditText((EditText)findViewById(R.id.nuevo_telefono_edittext));
        setDireccionEditText((EditText)findViewById(R.id.nuevo_direccion_edittext));
        
        getRutaEditText().setOnKeyListener(this);
        getNombreNegocioEditTExt().setOnKeyListener(this);
        getNitEditText().setOnKeyListener(this);
        getContactoEditText().setOnKeyListener(this);
        getTelefonoEditText().setOnKeyListener(this);
        getDireccionEditText().setOnKeyListener(this);
        
        setCrearButton((Button)findViewById(R.id.nuevo_crear_button));
        setCancelarButton((Button)findViewById(R.id.nuevo_cancelar_button));
        getCrearButton().setOnClickListener(this);
        getCancelarButton().setOnClickListener(this);
        
        setLunesCheckBox((CheckBox)findViewById(R.id.nuevo_lunes_checkBox));
        setMartesCheckBox((CheckBox)findViewById(R.id.nuevo_martes_checkBox));
        setMiercolesCheckBox((CheckBox)findViewById(R.id.nuevo_miercoles_checkBox));
        setJuevesCheckBox((CheckBox)findViewById(R.id.nuevo_jueves_checkBox));
        setViernesCheckBox((CheckBox)findViewById(R.id.nuevo_viernes_checkBox));
        setSabadoCheckBox((CheckBox)findViewById(R.id.nuevo_sabado_checkBox));
        
        Vendedor vendedor = new Vendedor();
		vendedor = peticion.vendedor(NuevoCliente.this);
        setRuta(vendedor.getRuta());
        
        Fecha fecha = new Fecha();
        setCodigoNuevoCliente(""+fecha.diaAnio()+getRuta()+"-"+fecha.fechaUnida());
        
    }

	@Override
	public void onClick(View view) {
		if (view == getCrearButton()){
			dialogCrear();
		}else if (view == getCancelarButton()){
			dialogCancelar();
		}
	}
	
	private void dialogCancelar(){
		new AlertDialog.Builder(this)
        .setTitle("Cancelar")
        .setMessage("Desea Cancelar y regresar al Rol de hoy?")
        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	finish();
                }
        })
        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                }
        })
        .show();
	}
	
	private void dialogCrear(){
		new AlertDialog.Builder(this)
        .setTitle("Crear Cliente")
        .setMessage("Esta seguro que desea crear el cliente?")
        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	new crearAsync().execute("");
                }
        })
        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                }
        })
        .show();

	}

	 @Override
     public boolean onKey(View view, int keyCode, KeyEvent event)
     {
         if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
         {
        	 if (view == getRutaEditText()){
        		 getNombreNegocioEditTExt().requestFocus();
        	 }else if (view == getNombreNegocioEditTExt()){
        		 getNitEditText().requestFocus();
        	 }else if (view == getNitEditText()){
        		 getContactoEditText().requestFocus();
        	 }else if (view == getContactoEditText()){
        		 getTelefonoEditText().requestFocus();
        	 }else if (view == getTelefonoEditText()){
        		 getDireccionEditText().requestFocus();
        	 }else if (view == getDireccionEditText()){
        		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(getDireccionEditText().getWindowToken(), 0);
               return true;

        	 }
             //editText2.requestFocus();//TODO: When the enter key is released
             return true;
         }
         if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
         {
             //TODO: When the enter key is pressed
             return true;
         }
         return false;
     }
	 
	 @SuppressWarnings("null")
	public RespuestaWS enviarDatos(){
		 
		 RespuestaWS res = new RespuestaWS();
		 
		 ClienteNuevo cliente = new ClienteNuevo();
		 
		 String ruta = getRutaEditText().getText().toString();
		 String nombre = getNombreNegocioEditTExt().getText().toString();
		 String nit = getNitEditText().getText().toString();
		 String contacto = getContactoEditText().getText().toString();
		 String telefono = getTelefonoEditText().getText().toString();
		 String direccion = getDireccionEditText().getText().toString();
		 
		 boolean lunes = getLunesCheckBox().isChecked();
		 boolean martes = getMartesCheckBox().isChecked();
		 boolean miercoles = getMiercolesCheckBox().isChecked();
		 boolean jueves = getJuevesCheckBox().isChecked();
		 boolean viernes = getViernesCheckBox().isChecked();
		 boolean sabado = getSabadoCheckBox().isChecked();
		 
		 String dia = new String();
		 
		 if (lunes) {dia = dia + "L";};
		 if (martes) {dia = dia + "M";};
		 if (miercoles) {dia = dia + "K";};
		 if (jueves) {dia = dia + "J";};
		 if (viernes) {dia = dia + "V";};
		 if (sabado) {dia = dia + "S";};
		 
		 if (ruta.equalsIgnoreCase("") || nombre.equalsIgnoreCase("") || nit.equalsIgnoreCase("") || contacto.equalsIgnoreCase("") || telefono.equalsIgnoreCase("") || direccion.equalsIgnoreCase("")){
			 res.setMensaje("Debe llenar todos los campos");
			 res.setResultado(false);
			 return res;
		 }
		 
		 if (dia.equalsIgnoreCase("")){
			 res.setMensaje("Debe marcar al menos un día");
			 res.setResultado(false);
			 return res;
		 }else{
			 
			 cliente.setRuta(ruta);
			 cliente.setNombreNegocio(nombre);
			 cliente.setNit(nit);
			 cliente.setNombreContacto(contacto);
			 cliente.setTelefono(telefono);
			 cliente.setDireccion(direccion);
			 
			 cliente.setDiaVisita(dia);
			 res = peticion.enviarNuevoCliente(NuevoCliente.this, cliente);
		 }
		 return res;
	 }
	 
	 
	// Clase para ejecutar en Background
	class crearAsync extends AsyncTask<String, Integer, Integer> {

		// Metodo que prepara lo que usara en background, Prepara el progress
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(NuevoCliente.this, "GUARDANDO DATOS", "ESPERE UN MOMENTO");
			pd.setCancelable(false);
		}

		// Metodo con las instrucciones que se realizan en background
		@Override
		protected Integer doInBackground(String... urlString) {
			try {
				respuestaWS = enviarDatos();
			} catch (Exception exception) {

			}
			return null;
		}

		// Metodo con las instrucciones al finalizar lo ejectuado en background
		protected void onPostExecute(Integer resultado) {
			pd.dismiss();
			if (respuestaWS.isResultado()){
				Toast.makeText(NuevoCliente.this, respuestaWS.getMensaje(), Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(NuevoCliente.this, respuestaWS.getMensaje(), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void buscarCategorias(){
		
		setListaCategoria(peticion.buscaCategoria(NuevoCliente.this));
	}

	public EditText getNombreNegocioEditTExt() {
		return nombreNegocioEditTExt;
	}

	public void setNombreNegocioEditTExt(EditText nombreNegocioEditTExt) {
		this.nombreNegocioEditTExt = nombreNegocioEditTExt;
	}

	public EditText getNitEditText() {
		return nitEditText;
	}

	public void setNitEditText(EditText nitEditText) {
		this.nitEditText = nitEditText;
	}

	public EditText getContactoEditText() {
		return contactoEditText;
	}

	public void setContactoEditText(EditText contactoEditText) {
		this.contactoEditText = contactoEditText;
	}

	public EditText getTelefonoEditText() {
		return telefonoEditText;
	}

	public void setTelefonoEditText(EditText telefonoEditText) {
		this.telefonoEditText = telefonoEditText;
	}

	public EditText getDireccionEditText() {
		return direccionEditText;
	}

	public void setDireccionEditText(EditText direccionEditText) {
		this.direccionEditText = direccionEditText;
	}

	public Button getCrearButton() {
		return crearButton;
	}

	public void setCrearButton(Button crearButton) {
		this.crearButton = crearButton;
	}

	public Button getCancelarButton() {
		return cancelarButton;
	}

	public void setCancelarButton(Button cancelarButton) {
		this.cancelarButton = cancelarButton;
	}

	public ListaCategoria getListaCategoria() {
		return listaCategoria;
	}

	public void setListaCategoria(ListaCategoria listaCategoria) {
		this.listaCategoria = listaCategoria;
	}

	public CheckBox getLunesCheckBox() {
		return lunesCheckBox;
	}

	public void setLunesCheckBox(CheckBox lunesCheckBox) {
		this.lunesCheckBox = lunesCheckBox;
	}

	public CheckBox getMartesCheckBox() {
		return martesCheckBox;
	}

	public void setMartesCheckBox(CheckBox martesCheckBox) {
		this.martesCheckBox = martesCheckBox;
	}

	public CheckBox getMiercolesCheckBox() {
		return miercolesCheckBox;
	}

	public void setMiercolesCheckBox(CheckBox miercolesCheckBox) {
		this.miercolesCheckBox = miercolesCheckBox;
	}

	public CheckBox getJuevesCheckBox() {
		return juevesCheckBox;
	}

	public void setJuevesCheckBox(CheckBox juevesCheckBox) {
		this.juevesCheckBox = juevesCheckBox;
	}

	public CheckBox getViernesCheckBox() {
		return viernesCheckBox;
	}

	public void setViernesCheckBox(CheckBox viernesCheckBox) {
		this.viernesCheckBox = viernesCheckBox;
	}

	public CheckBox getSabadoCheckBox() {
		return sabadoCheckBox;
	}

	public void setSabadoCheckBox(CheckBox sabadoCheckBox) {
		this.sabadoCheckBox = sabadoCheckBox;
	}

	public ArrayAdapter<Categoria> getCategoriaAdapter() {
		return categoriaAdapter;
	}

	public void setCategoriaAdapter(ArrayAdapter<Categoria> categoriaAdapter) {
		this.categoriaAdapter = categoriaAdapter;
	}

	public String getCodigoNuevoCliente() {
		return codigoNuevoCliente;
	}

	public void setCodigoNuevoCliente(String codigoNuevoCliente) {
		this.codigoNuevoCliente = codigoNuevoCliente;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public EditText getRutaEditText() {
		return rutaEditText;
	}

	public void setRutaEditText(EditText rutaEditText) {
		this.rutaEditText = rutaEditText;
	}
	
	
	
}
