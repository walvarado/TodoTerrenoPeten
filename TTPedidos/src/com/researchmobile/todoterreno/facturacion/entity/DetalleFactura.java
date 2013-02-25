package com.researchmobile.todoterreno.facturacion.entity;

import java.io.Serializable;

public class DetalleFactura implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String movDocumento;
	private String artCodigo;
	private Integer artUnidadesFardo;
	private Integer movUnidades;
	private Integer movUnidadesEntregadas;
	private float movPrecio;
	private String movFechaEntregar;
	private Integer cajaCodigo;
	private float moCostoUltimo;
	private Integer movUniDevueltos;
	private String movNotas;
	private String vendedor;
	private String ruta;
	private String idRepartidor;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMovDocumento() {
		return movDocumento;
	}
	public void setMovDocumento(String movDocumento) {
		this.movDocumento = movDocumento;
	}
	public String getArtCodigo() {
		return artCodigo;
	}
	public void setArtCodigo(String artCodigo) {
		this.artCodigo = artCodigo;
	}
	public Integer getArtUnidadesFardo() {
		return artUnidadesFardo;
	}
	public void setArtUnidadesFardo(Integer artUnidadesFardo) {
		this.artUnidadesFardo = artUnidadesFardo;
	}
	public Integer getMovUnidades() {
		return movUnidades;
	}
	public void setMovUnidades(Integer movUnidades) {
		this.movUnidades = movUnidades;
	}
	public Integer getMovUnidadesEntregadas() {
		return movUnidadesEntregadas;
	}
	public void setMovUnidadesEntregadas(Integer movUnidadesEntregadas) {
		this.movUnidadesEntregadas = movUnidadesEntregadas;
	}
	public float getMovPrecio() {
		return movPrecio;
	}
	public void setMovPrecio(float movPrecio) {
		this.movPrecio = movPrecio;
	}
	public String getMovFechaEntregar() {
		return movFechaEntregar;
	}
	public void setMovFechaEntregar(String movFechaEntregar) {
		this.movFechaEntregar = movFechaEntregar;
	}
	public Integer getCajaCodigo() {
		return cajaCodigo;
	}
	public void setCajaCodigo(Integer cajaCodigo) {
		this.cajaCodigo = cajaCodigo;
	}
	public float getMoCostoUltimo() {
		return moCostoUltimo;
	}
	public void setMoCostoUltimo(float moCostoUltimo) {
		this.moCostoUltimo = moCostoUltimo;
	}
	public Integer getMovUniDevueltos() {
		return movUniDevueltos;
	}
	public void setMovUniDevueltos(Integer movUniDevueltos) {
		this.movUniDevueltos = movUniDevueltos;
	}
	public String getMovNotas() {
		return movNotas;
	}
	public void setMovNotas(String movNotas) {
		this.movNotas = movNotas;
	}
	public String getVendedor() {
		return vendedor;
	}
	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getIdRepartidor() {
		return idRepartidor;
	}
	public void setIdRepartidor(String idRepartidor) {
		this.idRepartidor = idRepartidor;
	}
	
	
}
