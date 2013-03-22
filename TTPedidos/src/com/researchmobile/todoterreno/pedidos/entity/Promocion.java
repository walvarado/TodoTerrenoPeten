package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;

public class Promocion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String artCodigo;
	private int artUnidadesFardo;
	private int fardosCompra;
	private int unidadesCompra;
	private String artCodigoBoni;
	private String artDescripcionBoni;
	private int fardosBoni;
	private int unidadesBoni;
	private float precioVentaBoni;
	private int limiteOfertaCliente;
	private int limiteOfertasVenta;
	private int totalUnidades;
	private float artPrecioVentaNormal;
	public String getArtCodigo() {
		return artCodigo;
	}
	public void setArtCodigo(String artCodigo) {
		this.artCodigo = artCodigo;
	}
	public int getArtUnidadesFardo() {
		return artUnidadesFardo;
	}
	public void setArtUnidadesFardo(int artUnidadesFardo) {
		this.artUnidadesFardo = artUnidadesFardo;
	}
	public int getFardosCompra() {
		return fardosCompra;
	}
	public void setFardosCompra(int fardosCompra) {
		this.fardosCompra = fardosCompra;
	}
	public int getUnidadesCompra() {
		return unidadesCompra;
	}
	public void setUnidadesCompra(int unidadesCompra) {
		this.unidadesCompra = unidadesCompra;
	}
	public String getArtCodigoBoni() {
		return artCodigoBoni;
	}
	public void setArtCodigoBoni(String artCodigoBoni) {
		this.artCodigoBoni = artCodigoBoni;
	}
	public String getArtDescripcionBoni() {
		return artDescripcionBoni;
	}
	public void setArtDescripcionBoni(String artDescripcionBoni) {
		this.artDescripcionBoni = artDescripcionBoni;
	}
	public int getFardosBoni() {
		return fardosBoni;
	}
	public void setFardosBoni(int fardosBoni) {
		this.fardosBoni = fardosBoni;
	}
	public int getUnidadesBoni() {
		return unidadesBoni;
	}
	public void setUnidadesBoni(int unidadesBoni) {
		this.unidadesBoni = unidadesBoni;
	}
	public float getPrecioVentaBoni() {
		return precioVentaBoni;
	}
	public void setPrecioVentaBoni(float precioVentaBoni) {
		this.precioVentaBoni = precioVentaBoni;
	}
	public int getLimiteOfertaCliente() {
		return limiteOfertaCliente;
	}
	public void setLimiteOfertaCliente(int limiteOfertaCliente) {
		this.limiteOfertaCliente = limiteOfertaCliente;
	}
	public int getLimiteOfertasVenta() {
		return limiteOfertasVenta;
	}
	public void setLimiteOfertasVenta(int limiteOfertasVenta) {
		this.limiteOfertasVenta = limiteOfertasVenta;
	}
	public int getTotalUnidades() {
		return totalUnidades;
	}
	public void setTotalUnidades(int totalUnidades) {
		this.totalUnidades = totalUnidades;
	}
	public float getArtPrecioVentaNormal() {
		return artPrecioVentaNormal;
	}
	public void setArtPrecioVentaNormal(float artPrecioVentaNormal) {
		this.artPrecioVentaNormal = artPrecioVentaNormal;
	}
}
