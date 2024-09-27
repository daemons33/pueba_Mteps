package mteps.planpago.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_pagos.f_obtener_lista_detalle_plan_pago",
procedureName = "mteps_pagos.f_obtener_lista_detalle_plan_pago",
resultClasses = {listadetallePlanPago.class},
parameters = {
		@StoredProcedureParameter(name = "p_id_plan_pago", type = Integer.class, mode = ParameterMode.IN),

})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class listadetallePlanPago {
	@Id
	public Integer idDetPlanPago;
	public Integer idPlanPago;
	public Integer dplNro;
	public String  dplConcepto;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date dplFecLimite;
	public double dplMonto;
	public double dplAcuenta;
	public double dplSaldo;
	public String estado;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public Integer usuarioCreacion;
	public Integer usuarioModificacion;
	public String transaccion;
	public String observacion;
	public Integer idDeposito;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date depFecDeposito;
	
	public Date getDepFecDeposito() {
		return depFecDeposito;
	}
	public void setDepFecDeposito(Date depFecDeposito) {
		this.depFecDeposito = depFecDeposito;
	}
	protected Integer getIdDetPlanPago() {
		return idDetPlanPago;
	}
	protected void setIdDetPlanPago(Integer idDetPlanPago) {
		this.idDetPlanPago = idDetPlanPago;
	}
	protected Integer getIdPlanPago() {
		return idPlanPago;
	}
	protected void setIdPlanPago(Integer idPlanPago) {
		this.idPlanPago = idPlanPago;
	}
	protected Integer getDplNro() {
		return dplNro;
	}
	protected void setDplNro(Integer dplNro) {
		this.dplNro = dplNro;
	}
	protected String getDplConcepto() {
		return dplConcepto;
	}
	protected void setDplConcepto(String dplConcepto) {
		this.dplConcepto = dplConcepto;
	}
	protected Date getDplFecLimite() {
		return dplFecLimite;
	}
	protected void setDplFecLimite(Date dplFecLimite) {
		this.dplFecLimite = dplFecLimite;
	}
	protected double getDplMonto() {
		return dplMonto;
	}
	protected void setDplMonto(double dplMonto) {
		this.dplMonto = dplMonto;
	}
	protected double getDplAcuenta() {
		return dplAcuenta;
	}
	protected void setDplAcuenta(double dplAcuenta) {
		this.dplAcuenta = dplAcuenta;
	}
	protected double getDplSaldo() {
		return dplSaldo;
	}
	protected void setDplSaldo(double dplSaldo) {
		this.dplSaldo = dplSaldo;
	}
	protected String getEstado() {
		return estado;
	}
	protected void setEstado(String estado) {
		this.estado = estado;
	}
	protected Date getFechaCreacion() {
		return fechaCreacion;
	}
	protected void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	protected Date getFechaModificacion() {
		return fechaModificacion;
	}
	protected void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	protected Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	protected void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	protected Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	protected void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	protected String getTransaccion() {
		return transaccion;
	}
	protected void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	protected String getObservacion() {
		return observacion;
	}
	protected void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	protected Integer getIdDeposito() {
		return idDeposito;
	}
	protected void setIdDeposito(Integer idDeposito) {
		this.idDeposito = idDeposito;
	}
	

}
