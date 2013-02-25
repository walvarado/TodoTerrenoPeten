package com.researchmobile.todoterreno.facturacion.entity;

import com.researchmobile.todoterreno.pedidos.entity.RespuestaWS;

public class CatalogoFactura {
	
	private RespuestaWS respuesta;
	private Factura[] factura;
	public RespuestaWS getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(RespuestaWS respuesta) {
		this.respuesta = respuesta;
	}
	public Factura[] getFactura() {
		return factura;
	}
	public void setFactura(Factura[] factura) {
		this.factura = factura;
	}
	
}
