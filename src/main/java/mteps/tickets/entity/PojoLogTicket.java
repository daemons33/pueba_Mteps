package mteps.tickets.entity;


import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PojoLogTicket {
	 public String estado;
	 public String nombre;
	 public String puesto;
	 public String detalle;
	 @Id
	 public Integer idTicket;
	 public String observacion;
	 public String transaccion;
	 public String codigoTicket;
	 public String area;
	 public String categoria;
	 public String subCategoria;
	 public String fecha;
	 public String detalleAtendido;
	 
	 
	 
	public String getDetalleAtendido() {
		return detalleAtendido;
	}
	public void setDetalleAtendido(String detalleAtendido) {
		this.detalleAtendido = detalleAtendido;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public Integer getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	public String getCodigoTicket() {
		return codigoTicket;
	}
	public void setCodigoTicket(String codigoTicket) {
		this.codigoTicket = codigoTicket;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSubCategoria() {
		return subCategoria;
	}
	public void setSubCategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	 
}
