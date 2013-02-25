package com.researchmobile.todoterreno.pedidos.entity;

import java.io.Serializable;


public class ClienteNuevo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long idDB;
	private String idNuevoCliente;
	private String nit;
	private String nombreNegocio;
	private String categoriaCliente;
	private String nombreContacto;
	private String telefono;
	private String direccion;
	private String ruta;
	private String diaVisita;
	public long getIdDB() {
		return idDB;
	}
	public void setIdDB(long idDB) {
		this.idDB = idDB;
	}
	public String getIdNuevoCliente() {
		return idNuevoCliente;
	}
	public void setIdNuevoCliente(String idNuevoCliente) {
		this.idNuevoCliente = idNuevoCliente;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getNombreNegocio() {
		return nombreNegocio;
	}
	public void setNombreNegocio(String nombreNegocio) {
		this.nombreNegocio = nombreNegocio;
	}
	public String getCategoriaCliente() {
		return categoriaCliente;
	}
	public void setCategoriaCliente(String categoriaCliente) {
		this.categoriaCliente = categoriaCliente;
	}
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getDiaVisita() {
		return diaVisita;
	}
	public void setDiaVisita(String diaVisita) {
		this.diaVisita = diaVisita;
	}
	
}