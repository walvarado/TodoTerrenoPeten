package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class NoVenta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String descripcion;
	
	public String toString() {
    	return descripcion;
    }
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
