package com.researchmobile.todoterreno.pedidos.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.researchmobile.todoterreno.pedidos.utility.FormatDecimal;
import com.researchmobile.todoterreno.pedidos.ws.Peticion;

public class ListaPedidos extends Activity implements OnItemClickListener, OnClickListener{
	
	private TextView totalGenralTextView;
	private Button enviarPendientesButton;
	private TextView enviadosTextView;
	private TextView pendientesTextView;
	private ListView pedidosListView;
	private ArrayList<HashMap<String, String>> pedidosHashMap;
	private SimpleAdapter simpleAdapter;
	private Peticion peticion;
	private ProgressDialog pd = null;
	FormatDecimal formatDecimal = new FormatDecimal();
	
	private float totalGeneral;
	private int enviados;
	private int pendientes;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listapedidos);
        
        setPeticion(new Peticion());
        setTotalGenralTextView((TextView)findViewById(R.id.listapedidos_totalvendido_textview));
        setEnviadosTextView((TextView)findViewById(R.id.listapedidos_enviados_textview));
        setPendientesTextView((TextView)findViewById(R.id.listapedidos_pendientes_textview));
        setPedidosListView((ListView)findViewById(R.id.listapedidos_listView));
        setEnviarPendientesButton((Button)findViewById(R.id.listapedidos_enviar_button));
        getEnviarPendientesButton().setOnClickListener(this);
        getPedidosListView().setOnItemClickListener(this);
        
        new pedidosAsync().execute("");
	}
	
	//Capturar boton back del Teclado
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
	            Intent intent = new Intent(ListaPedidos.this, Rol.class);
	            startActivity(intent);
	            return true;
	      }
	      
	      return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	// Clase para buscar estado de los pedidos en Background
    class pedidosAsync extends AsyncTask<String, Integer, Integer> {

          // Metodo que prepara lo que usara en background, Prepara el progress
          @Override
          protected void onPreExecute() {
                pd = ProgressDialog. show(ListaPedidos.this, "VERIFICANDO DATOS",
                            "ESPERE UN MOMENTO");
                pd.setCancelable( false);
         }

          // Metodo con las instrucciones que se realizan en background
          @Override
          protected Integer doInBackground(String... urlString) {
                try {
                	buscaPedidos();

               } catch (Exception exception) {
            	   Log.e("TT", "ListaPedidos error = " + exception);

               }
                return null ;
         }

          // Metodo con las instrucciones al finalizar lo ejectuado en background
          protected void onPostExecute(Integer resultado) {
                pd.dismiss();
                llenaPedidos();
         }
   }
    
 // Clase para enviar los pedidos pendientes en Background
    class pendientesAsync extends AsyncTask<String, Integer, Integer> {

          // Metodo que prepara lo que usara en background, Prepara el progress
          @Override
          protected void onPreExecute() {
                pd = ProgressDialog. show(ListaPedidos.this, "ENVIANDO PEDIDOS", "ESPERE UN MOMENTO");
                pd.setCancelable( false);
         }

          // Metodo con las instrucciones que se realizan en background
          @Override
          protected Integer doInBackground(String... urlString) {
                try {
                	enviarPedidosPendientes();
               } catch (Exception exception) {

               }
                return null ;
         }

          // Metodo con las instrucciones al finalizar lo ejectuado en background
          protected void onPostExecute(Integer resultado) {
                pd.dismiss();
                new pedidosAsync().execute("");

         }
   }
    
    public void enviarPedidosPendientes(){
    	getPeticion().pedidosPendientes(this);
    	
    }

    
    public void buscaPedidos(){
    	setTotalGeneral(getPeticion().totalGeneral(this));
    	setEnviados(getPeticion().totalEnviados(this));
    	setPendientes(getPeticion().totalPendientes(this));
    	
    	setPedidosHashMap(getPeticion().listaPedidos(ListaPedidos.this));
    	Log.e("TT", "ListaPedidos.buscaPedidos");
    }
    
    public void llenaPedidos(){
    	
    	getTotalGenralTextView().setText(formatDecimal.convierteFloat(getTotalGeneral()));
    	getEnviadosTextView().setText(String.valueOf(getEnviados()));
    	getPendientesTextView().setText(String.valueOf(getPendientes()));
    	setSimpleAdapter(new SimpleAdapter(ListaPedidos.this, 
				getPedidosHashMap(), 
				R.layout.fila_lista_pedidos,
				new String[] {"codigoCliente",
					"total",
					"fecha",
					"hora"},
				new int[] {R.id.fila_lista_pedidos_codigo_textview,
				 R.id.fila_lista_pedidos_total_textview, 
					 R.id.fila_lista_pedidos_fecha_textview,
					 R.id.fila_lista_pedidos_hora_textview}));
		
    	getPedidosListView().setAdapter(getSimpleAdapter());
		Log.e("TT", "async");
    	
    }
    
	@Override
	public void onClick(View view) {
		if (view == getEnviarPendientesButton()){
			new pendientesAsync().execute("");
		}
		
	}


	public TextView getTotalGenralTextView() {
		return totalGenralTextView;
	}

	public void setTotalGenralTextView(TextView totalGenralTextView) {
		this.totalGenralTextView = totalGenralTextView;
	}

	public TextView getEnviadosTextView() {
		return enviadosTextView;
	}

	public void setEnviadosTextView(TextView enviadosTextView) {
		this.enviadosTextView = enviadosTextView;
	}

	public TextView getPendientesTextView() {
		return pendientesTextView;
	}

	public void setPendientesTextView(TextView pendientesTextView) {
		this.pendientesTextView = pendientesTextView;
	}

	public ListView getPedidosListView() {
		return pedidosListView;
	}

	public void setPedidosListView(ListView pedidosListView) {
		this.pedidosListView = pedidosListView;
	}

	public ArrayList<HashMap<String, String>> getPedidosHashMap() {
		return pedidosHashMap;
	}

	public void setPedidosHashMap(ArrayList<HashMap<String, String>> pedidosHashMap) {
		this.pedidosHashMap = pedidosHashMap;
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

	public float getTotalGeneral() {
		return totalGeneral;
	}

	public void setTotalGeneral(float totalGeneral) {
		this.totalGeneral = totalGeneral;
	}

	public int getEnviados() {
		return enviados;
	}

	public void setEnviados(int enviados) {
		this.enviados = enviados;
	}

	public int getPendientes() {
		return pendientes;
	}

	public void setPendientes(int pendientes) {
		this.pendientes = pendientes;
	}

	public Button getEnviarPendientesButton() {
		return enviarPendientesButton;
	}

	public void setEnviarPendientesButton(Button enviarPendientesButton) {
		this.enviarPendientesButton = enviarPendientesButton;
	}

}
