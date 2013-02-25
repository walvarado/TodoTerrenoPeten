package com.researchmobile.todoterreno.facturacion.entity;

public class ResultLogin {

	private boolean resultado;
	private String mensaje;
	private Repartidor[] repartidor;
	public boolean isResultado() {
		return resultado;
	}
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Repartidor[] getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(Repartidor[] repartidor) {
		this.repartidor = repartidor;
	}
	
	
}
