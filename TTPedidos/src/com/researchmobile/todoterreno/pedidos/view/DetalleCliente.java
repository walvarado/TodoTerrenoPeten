package com.researchmobile.todoterreno.pedidos.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.researchmobile.todoterreno.pedidos.entity.Cliente;
import com.researchmobile.todoterreno.pedidos.entity.NoVenta;
import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.utility.Mensaje;
import com.researchmobile.todoterreno.pedidos.ws.Peticion;

public class DetalleCliente extends Activity implements OnClickListener{
	
	private Cliente cliente;
	private String codigoCliente;
	private TextView codigoClienteTextView;
	private TextView empresaTextView;
	private TextView contactoTextView;
	private TextView direccionTextView;
	private TextView telefonoTextView;
	private TextView diasCreditoTextView;
	private TextView limiteTextView;
	private TextView saldoTextView;
	private TextView descuento1TextView;
	private TextView descuento2TextView;
	private TextView descuento3TextView;
	private TextView saldo2TextView;
	private Button enviarNoVentaButton;
	private CheckBox noVentaCheckBox;
	private Spinner noVentaSpinner;
	private LinearLayout noVentaLinearLayout;
	private Peticion peticion;
	private NoVenta[] noventa;
	private ArrayAdapter<NoVenta> noVentaAdapter;
	private Mensaje mensaje;
	private String motivoSeleccionado;
	private ProgressDialog pd = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_cliente);
        setPeticion(new Peticion());
        setMensaje(new Mensaje());
        setCodigoCliente((String)this.getIntent().getSerializableExtra("codigoCliente"));
        
        setCodigoClienteTextView((TextView)findViewById(R.id.detalle_cliente_codigo_textview));
        setEmpresaTextView((TextView)findViewById(R.id.detalle_cliente_empresa_textview));
        setContactoTextView((TextView)findViewById(R.id.detalle_cliente_contacto_textview));
        setDireccionTextView((TextView)findViewById(R.id.detalle_cliente_direccion_textview));
        setTelefonoTextView((TextView)findViewById(R.id.detalle_cliente_telefono_textview));
        setDiasCreditoTextView((TextView)findViewById(R.id.detalle_cliente_dias_credito_textview));
        setLimiteTextView((TextView)findViewById(R.id.detalle_cliente_limite_textview));
        setSaldoTextView((TextView)findViewById(R.id.detalle_cliente_saldo_textview));
        setEnviarNoVentaButton((Button)findViewById(R.id.detalle_cliente_enviarnovente_button));
        setNoVentaCheckBox((CheckBox)findViewById(R.id.detalle_cliente_noventa_checkbox));
        setNoVentaSpinner((Spinner)findViewById(R.id.detalle_cliente_motivos_spinner));
        setDescuento1TextView((TextView)findViewById(R.id.detalle_cliente_descuento_textview));
        setNoVentaLinearLayout((LinearLayout)findViewById(R.id.detalle_cliente_noventa_layout));
        getNoVentaLinearLayout().setVisibility(View.INVISIBLE);
        getEnviarNoVentaButton().setOnClickListener(this);
        getNoVentaCheckBox().setOnClickListener(this);
        
        new detalleClienteAsync().execute("");
    }
	
	@Override
	public void onClick(View view) {
		if (view == getEnviarNoVentaButton()){
			new motivoAsync().execute("");
		}else if (view == getNoVentaCheckBox()){
			if (getNoVentaCheckBox().isChecked()){
				fillDataSpinner();
				getNoVentaLinearLayout().setVisibility(View.VISIBLE);
			}else{
				getNoVentaLinearLayout().setVisibility(View.INVISIBLE);
			}
		}
		
	}
	
	public void fillDataSpinner(){
		setNoventa(getPeticion().noVenta(this));
		setNoVentaAdapter(new ArrayAdapter<NoVenta>(this, R.layout.item_spinner, R.id.item_spinner_textview, getNoventa()));
		getNoVentaSpinner().setAdapter(getNoVentaAdapter());
	}
	
	/**
	 * MENU
	 */

	// To change item text dynamically
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detalle_cliente_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.detalle_cliente_menu_tomar_pedido_opcion:
			TomarPedido();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	// Clase para ejecutar en Background
	class detalleClienteAsync extends AsyncTask<String, Integer, Integer> {

		// Metodo que prepara lo que usara en background, Prepara el progress
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(DetalleCliente.this, "VERIFICANDO DATOS", "ESPERE UN MOMENTO");
			pd.setCancelable(false);
		}

		// Metodo con las instrucciones que se realizan en background
		@Override
		protected Integer doInBackground(String... urlString) {
			try {
				BuscarDetalleCliente();

			} catch (Exception exception) {

			}
			return null;
		}

		// Metodo con las instrucciones al finalizar lo ejectuado en background
		protected void onPostExecute(Integer resultado) {
			pd.dismiss();
			MostrarDetalleCliente();

		}

	}
	
	// Clase para enviar el motivo
	class motivoAsync extends AsyncTask<String, Integer, Integer> {

		// Metodo que prepara lo que usara en background, Prepara el progress
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(DetalleCliente.this, "ENVIANDO UN MOTIVO", "ESPERE UN MOMENTO");
			pd.setCancelable(false);
		}

		// Metodo con las instrucciones que se realizan en background
		@Override
		protected Integer doInBackground(String... urlString) {
			try {
				enviarMotivo();

			} catch (Exception exception) {

			}
			return null;
		}

		// Metodo con las instrucciones al finalizar lo ejectuado en background
		protected void onPostExecute(Integer resultado) {
			pd.dismiss();
			Intent intent = new Intent(DetalleCliente.this, Rol.class);
			startActivity(intent);

		}

	}
	
	public void enviarMotivo(){
		
		setMotivoSeleccionado(((NoVenta)getNoVentaSpinner().getSelectedItem()).getId());
		RespuestaWS respuesta = new RespuestaWS();
		respuesta = getPeticion().enviarMotivo(this, getCodigoCliente(), getMotivoSeleccionado());
		getMensaje().VerMensaje(this, respuesta.getMensaje());
	}
	private void BuscarDetalleCliente() {
		
		Peticion peticion = new Peticion();
		setCliente(peticion.DetalleCliente(DetalleCliente.this, getCodigoCliente()));
		Log.e("TT", "fecha visitado de cliente = " + getCliente().getFechaVisitado());
		
	}
	
	private void MostrarDetalleCliente() {
		getCodigoClienteTextView().setText(getCliente().getCliCodigo());
        getEmpresaTextView().setText(getCliente().getCliEmpresa());
        getContactoTextView().setText(getCliente().getCliContacto());
        getDireccionTextView().setText(getCliente().getCliDireccion());
        getTelefonoTextView().setText(getCliente().getCliTelefono());
        getLimiteTextView().setText(getCliente().getCliLimite());
        getSaldoTextView().setText(getCliente().getCliSaldo());
        getDescuento1TextView().setText(getCliente().getCliDes1());
		
	}


	private void TomarPedido() {
		Intent intent = new Intent(DetalleCliente.this, TomarPedido.class);
		Log.e("TT", "idruta = " + getCliente().getCliRuta());
		intent.putExtra("codigoCliente", getCodigoCliente());
		intent.putExtra("idRuta", getCliente().getCliRuta());
		startActivity(intent);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TextView getTelefonoTextView() {
		return telefonoTextView;
	}

	public void setTelefonoTextView(TextView telefonoTextView) {
		this.telefonoTextView = telefonoTextView;
	}

	public TextView getDireccionTextView() {
		return direccionTextView;
	}

	public void setDireccionTextView(TextView direccionTextView) {
		this.direccionTextView = direccionTextView;
	}

	public TextView getCodigoClienteTextView() {
		return codigoClienteTextView;
	}

	public void setCodigoClienteTextView(TextView codigoClienteTextView) {
		this.codigoClienteTextView = codigoClienteTextView;
	}

	public TextView getEmpresaTextView() {
		return empresaTextView;
	}

	public void setEmpresaTextView(TextView empresaTextView) {
		this.empresaTextView = empresaTextView;
	}

	public TextView getContactoTextView() {
		return contactoTextView;
	}

	public void setContactoTextView(TextView contactoTextView) {
		this.contactoTextView = contactoTextView;
	}

	public TextView getDiasCreditoTextView() {
		return diasCreditoTextView;
	}

	public void setDiasCreditoTextView(TextView diasCreditoTextView) {
		this.diasCreditoTextView = diasCreditoTextView;
	}

	public TextView getLimiteTextView() {
		return limiteTextView;
	}

	public void setLimiteTextView(TextView limiteTextView) {
		this.limiteTextView = limiteTextView;
	}

	public TextView getSaldoTextView() {
		return saldoTextView;
	}

	public void setSaldoTextView(TextView saldoTextView) {
		this.saldoTextView = saldoTextView;
	}

	public TextView getDescuento1TextView() {
		return descuento1TextView;
	}

	public void setDescuento1TextView(TextView descuento1TextView) {
		this.descuento1TextView = descuento1TextView;
	}

	public TextView getDescuento2TextView() {
		return descuento2TextView;
	}

	public void setDescuento2TextView(TextView descuento2TextView) {
		this.descuento2TextView = descuento2TextView;
	}

	public TextView getDescuento3TextView() {
		return descuento3TextView;
	}

	public void setDescuento3TextView(TextView descuento3TextView) {
		this.descuento3TextView = descuento3TextView;
	}

	public TextView getSaldo2TextView() {
		return saldo2TextView;
	}

	public void setSaldo2TextView(TextView saldo2TextView) {
		this.saldo2TextView = saldo2TextView;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public Button getEnviarNoVentaButton() {
		return enviarNoVentaButton;
	}

	public void setEnviarNoVentaButton(Button enviarNoVentaButton) {
		this.enviarNoVentaButton = enviarNoVentaButton;
	}

	public CheckBox getNoVentaCheckBox() {
		return noVentaCheckBox;
	}

	public void setNoVentaCheckBox(CheckBox noVentaCheckBox) {
		this.noVentaCheckBox = noVentaCheckBox;
	}

	public Spinner getNoVentaSpinner() {
		return noVentaSpinner;
	}

	public void setNoVentaSpinner(Spinner noVentaSpinner) {
		this.noVentaSpinner = noVentaSpinner;
	}

	public LinearLayout getNoVentaLinearLayout() {
		return noVentaLinearLayout;
	}

	public void setNoVentaLinearLayout(LinearLayout noVentaLinearLayout) {
		this.noVentaLinearLayout = noVentaLinearLayout;
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

	public NoVenta[] getNoventa() {
		return noventa;
	}

	public void setNoventa(NoVenta[] noventa) {
		this.noventa = noventa;
	}

	public ArrayAdapter<NoVenta> getNoVentaAdapter() {
		return noVentaAdapter;
	}

	public void setNoVentaAdapter(ArrayAdapter<NoVenta> noVentaAdapter) {
		this.noVentaAdapter = noVentaAdapter;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public String getMotivoSeleccionado() {
		return motivoSeleccionado;
	}

	public void setMotivoSeleccionado(String motivoSeleccionado) {
		this.motivoSeleccionado = motivoSeleccionado;
	}
}
