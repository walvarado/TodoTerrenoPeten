package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class ListaCategoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Categoria[] categoria;
	private RespuestaWS respuesta;

	public Categoria[] getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria[] categoria) {
		this.categoria = categoria;
	}

	public RespuestaWS getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(RespuestaWS respuesta) {
		this.respuesta = respuesta;
	}

}
