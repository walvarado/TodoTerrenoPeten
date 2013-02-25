package com.researchmobile.todoterreno.pedidos.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.researchmobile.todoterreno.facturacion.view.LoginRepartidor;

public class MenuPrincipal extends Activity implements OnClickListener{
	private Button repartidorButton;
	private Button vendedorButton;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipal);
        
        setRepartidorButton((Button)findViewById(R.id.menuprincipal_repartidor_button));
        setVendedorButton((Button)findViewById(R.id.menuprincipal_vendedor_button));
        getRepartidorButton().setOnClickListener(this);
        getVendedorButton().setOnClickListener(this);
        
	}

	@Override
	public void onClick(View view) {
		if (view == getRepartidorButton()){
			iniciaRepartidor();
		}else if(view == getVendedorButton()){
			iniciaVendedor();
		}
	}
	
	private void iniciaRepartidor(){
		Intent intent = new Intent(MenuPrincipal.this, LoginRepartidor.class);
		startActivity(intent);
	}
	
	private void iniciaVendedor(){
		Intent intent = new Intent(MenuPrincipal.this, Login.class);
		startActivity(intent);
	}

	public Button getRepartidorButton() {
		return repartidorButton;
	}

	public void setRepartidorButton(Button repartidorButton) {
		this.repartidorButton = repartidorButton;
	}

	public Button getVendedorButton() {
		return vendedorButton;
	}

	public void setVendedorButton(Button vendedorButton) {
		this.vendedorButton = vendedorButton;
	}
	
}
