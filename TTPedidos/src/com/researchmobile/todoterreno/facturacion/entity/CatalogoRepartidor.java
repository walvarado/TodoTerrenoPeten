package com.researchmobile.todoterreno.facturacion.entity;

import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;

public class CatalogoRepartidor {
	
	private Repartidor repartidor;
	private RespuestaWS respuesta;

	
	public Repartidor getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	public RespuestaWS getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(RespuestaWS respuesta) {
		this.respuesta = respuesta;
	}
	
}
