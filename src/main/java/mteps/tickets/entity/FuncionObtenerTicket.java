package mteps.tickets.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name = "mteps_tickets.f_obtener_ticket", procedureName = "mteps_tickets.f_obtener_ticket", resultClasses = {
		FuncionObtenerTicket.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_ticket", type = Integer.class, mode = ParameterMode.IN) })
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionObtenerTicket {
	@Id
	public Integer idTicket;
	public String codigoTicket;
	public String puestoSolicitante;
	public String nombreSolicitante;
	public String departamentoSolicitante;
	public String puestoAsignado;
	public String nombreAsignado;
	public String departamentoAsignado;
	public String detalle;
	public String estado;
	public String observacion;
	public String transaccion;
	public Integer idArea;
	public String area;
	public Integer idCategoria;
	public String categoria;
	public Integer idSubCategoria;
	public String subcategoria;
	public String detalleAtendido;
	
	
	
	public String getDetalleAtendido() {
		return detalleAtendido;
	}
	public void setDetalleAtendido(String detalleAtendido) {
		this.detalleAtendido = detalleAtendido;
	}
	public Integer getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}
	public String getCodigoTicket() {
		return codigoTicket;
	}
	public void setCodigoTicket(String codigoTicket) {
		this.codigoTicket = codigoTicket;
	}
	public String getPuestoSolicitante() {
		return puestoSolicitante;
	}
	public void setPuestoSolicitante(String puestoSolicitante) {
		this.puestoSolicitante = puestoSolicitante;
	}
	public String getNombreSolicitante() {
		return nombreSolicitante;
	}
	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}
	public String getDepartamentoSolicitante() {
		return departamentoSolicitante;
	}
	public void setDepartamentoSolicitante(String departamentoSolicitante) {
		this.departamentoSolicitante = departamentoSolicitante;
	}
	public String getPuestoAsignado() {
		return puestoAsignado;
	}
	public void setPuestoAsignado(String puestoAsignado) {
		this.puestoAsignado = puestoAsignado;
	}
	public String getNombreAsignado() {
		return nombreAsignado;
	}
	public void setNombreAsignado(String nombreAsignado) {
		this.nombreAsignado = nombreAsignado;
	}
	public String getDepartamentoAsignado() {
		return departamentoAsignado;
	}
	public void setDepartamentoAsignado(String departamentoAsignado) {
		this.departamentoAsignado = departamentoAsignado;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
	public Integer getIdArea() {
		return idArea;
	}
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Integer getIdSubCategoria() {
		return idSubCategoria;
	}
	public void setIdSubCategoria(Integer idSubCategoria) {
		this.idSubCategoria = idSubCategoria;
	}
	public String getSubcategoria() {
		return subcategoria;
	}
	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
	}
	
	
}
