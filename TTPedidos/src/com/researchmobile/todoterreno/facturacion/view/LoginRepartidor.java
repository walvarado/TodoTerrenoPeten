package com.researchmobile.todoterreno.facturacion.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;
import com.researchmobile.todoterreno.pedidos.entity.User;
import com.researchmobile.todoterreno.pedidos.utility.Mensaje;
import com.researchmobile.todoterreno.pedidos.view.R;
import com.researchmobile.todoterreno.pedidos.ws.PeticionFactura;

public class LoginRepartidor extends Activity implements OnClickListener, OnKeyListener{
	
	private EditText usuarioEditText;
	private EditText contraseniaEditText;
	private Button entrarButton;
	private ProgressDialog pd = null;
	private RespuestaWS respuesta;
	private Mensaje mensaje;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginrepartidor);
        
        setRespuesta(new RespuestaWS());
        setMensaje(new Mensaje());
        setUsuarioEditText((EditText)findViewById(R.id.loginrepartidor_usuario_edittext));
        setContraseniaEditText((EditText)findViewById(R.id.loginrepartidor_contrasenia_edittext));
        getUsuarioEditText().setOnKeyListener(this);
        getContraseniaEditText().setOnKeyListener(this);
        setEntrarButton((Button)findViewById(R.id.loginrepartidor_entrar_button));
        getEntrarButton().setOnClickListener(this);
        getUsuarioEditText().setText("");
        getContraseniaEditText().setText("");
    }

	@Override
	public void onClick(View view) {
		if (view == getEntrarButton()){
			new LoginAsync().execute("");
		}
		
	}
	
	@Override
    public boolean onKey(View view, int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
        {
            if (view == getUsuarioEditText()){
            	getContraseniaEditText().requestFocus();
            }else if (view == getContraseniaEditText()){
            	new LoginAsync().execute("");
            }
        	
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            //TODO: When the enter key is pressed
            return true;
        }
        return false;
    }
	
	private void peticionLogin() {
		User.setUsername(getUsuarioEditText().getText().toString());
		User.setClave(getContraseniaEditText().getText().toString());
		PeticionFactura peticion = new PeticionFactura();
		setRespuesta(peticion.login(this));
	}

	// Clase para ejecutar en Background
	class LoginAsync extends AsyncTask<String, Integer, Integer> {

		// Metodo que prepara lo que usara en background, Prepara el progress
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(LoginRepartidor.this, "VERIFICANDO DATOS", "ESPERE UN MOMENTO");
			pd.setCancelable(false);
		}

		// Metodo con las instrucciones que se realizan en background
		@Override
		protected Integer doInBackground(String... urlString) {
			try {
				peticionLogin();
			} catch (Exception exception) {

			}
			return null;
		}

		// Metodo con las instrucciones al finalizar lo ejectuado en background
		protected void onPostExecute(Integer resultado) {
			pd.dismiss();
//			if (getRespuesta().isResultado()){
				activityRol();
//			}else{
//				getMensaje().VerMensaje(LoginRepartidor.this, getRespuesta().getMensaje());
//			}

		}

		private void activityRol() {
			Intent intent = new Intent(LoginRepartidor.this, RolFacturacion.class);
			startActivity(intent);
		}

	}
	
	//Inhabilitar boton "back"
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
        	getUsuarioEditText().setText("");
        	getContraseniaEditText().setText("");
            moveTaskToBack(true);
              return true;
        }
        
        return super.onKeyDown(keyCode, event);
      }

	public EditText getUsuarioEditText() {
		return usuarioEditText;
	}

	public void setUsuarioEditText(EditText usuarioEditText) {
		this.usuarioEditText = usuarioEditText;
	}

	public EditText getContraseniaEditText() {
		return contraseniaEditText;
	}

	public void setContraseniaEditText(EditText contraseniaEditText) {
		this.contraseniaEditText = contraseniaEditText;
	}

	public Button getEntrarButton() {
		return entrarButton;
	}

	public void setEntrarButton(Button entrarButton) {
		this.entrarButton = entrarButton;
	}
	
	public RespuestaWS getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(RespuestaWS respuesta) {
		this.respuesta = respuesta;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}
}