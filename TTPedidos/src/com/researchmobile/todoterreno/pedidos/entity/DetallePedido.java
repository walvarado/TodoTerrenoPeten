package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class DetallePedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String nombre;
	private int caja;
	private int unidad;
	private float precio;
	private float subTotal;
	private int totalUnidades;
	private int unidadesFardo;
	private long idDb;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCaja() {
		return caja;
	}
	public void setCaja(int caja) {
		this.caja = caja;
	}
	public int getUnidad() {
		return unidad;
	}
	public void setUnidad(int unidad) {
		this.unidad = unidad;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public float getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}
	public int getTotalUnidades() {
		return totalUnidades;
	}
	public void setTotalUnidades(int totalUnidades) {
		this.totalUnidades = totalUnidades;
	}
	public int getUnidadesFardo() {
		return unidadesFardo;
	}
	public void setUnidadesFardo(int unidadesFardo) {
		this.unidadesFardo = unidadesFardo;
	}
	public long getIdDb() {
		return idDb;
	}
	public void setIdDb(long idDb) {
		this.idDb = idDb;
	}
	
}
