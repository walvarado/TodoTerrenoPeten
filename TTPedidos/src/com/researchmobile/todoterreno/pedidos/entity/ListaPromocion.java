package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class ListaPromocion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Promocion[] promocion;
	private RespuestaWS respuesta;
	public Promocion[] getPromocion() {
		return promocion;
	}
	public void setPromocion(Promocion[] promocion) {
		this.promocion = promocion;
	}
	public RespuestaWS getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(RespuestaWS respuesta) {
		this.respuesta = respuesta;
	}
	
}
