package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class Pedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private DetallePedido[] detallePedido;
	private EncabezadoPedido encabezadoPedido;
	public DetallePedido[] getDetallePedido() {
		return detallePedido;
	}
	public void setDetallePedido(DetallePedido[] detallePedido) {
		this.detallePedido = detallePedido;
	}
	public EncabezadoPedido getEncabezadoPedido() {
		return encabezadoPedido;
	}
	public void setEncabezadoPedido(EncabezadoPedido encabezadoPedido) {
		this.encabezadoPedido = encabezadoPedido;
	}
	
	

}
