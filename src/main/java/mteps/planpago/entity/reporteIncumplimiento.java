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
@NamedStoredProcedureQuery(name="mteps_pagos.f_reporte_incumplimiento",
procedureName = "mteps_pagos.f_reporte_incumplimiento",
resultClasses = {reporteIncumplimiento.class},
parameters = {
		@StoredProcedureParameter(name = "p_id_plan_pago", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class reporteIncumplimiento {
	
	public Integer idPlanPago;
	public String plaNitEmpresa;
	public String plaMatricula;
	public String plaRazonSocial;
	public String plaGestionPlanilla;
	public String plaPeriodoPlanilla;
	public Integer plaNroSucursal;
	public double plaTotalMulta;
	public double montoAnticipo;
	public String depNroDeposito;
	public Integer PlaNroCuotas;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date plaFecSolicitud;
	public String estado;
	@Id
	public Integer dplNro;
	public String dplConcepto;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date dplFecLimite;
	public double dplMonto;
	public double dplAcuenta;
	public double dplSaldo;
	public String estadoDet;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaPagoCuota;
	public String NroDepositoCuenta;
	public String login;
	public String itmCiudad;
	
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getItmCiudad() {
		return itmCiudad;
	}
	public void setItmCiudad(String itmCiudad) {
		this.itmCiudad = itmCiudad;
	}
	public Integer getIdPlanPago() {
		return idPlanPago;
	}
	public void setIdPlanPago(Integer idPlanPago) {
		this.idPlanPago = idPlanPago;
	}
	public String getPlaNitEmpresa() {
		return plaNitEmpresa;
	}
	public void setPlaNitEmpresa(String plaNitEmpresa) {
		this.plaNitEmpresa = plaNitEmpresa;
	}
	public String getPlaMatricula() {
		return plaMatricula;
	}
	public void setPlaMatricula(String plaMatricula) {
		this.plaMatricula = plaMatricula;
	}
	public String getPlaRazonSocial() {
		return plaRazonSocial;
	}
	public void setPlaRazonSocial(String plaRazonSocial) {
		this.plaRazonSocial = plaRazonSocial;
	}
	public String getPlaGestionPlanilla() {
		return plaGestionPlanilla;
	}
	public void setPlaGestionPlanilla(String plaGestionPlanilla) {
		this.plaGestionPlanilla = plaGestionPlanilla;
	}
	public String getPlaPeriodoPlanilla() {
		return plaPeriodoPlanilla;
	}
	public void setPlaPeriodoPlanilla(String plaPeriodoPlanilla) {
		this.plaPeriodoPlanilla = plaPeriodoPlanilla;
	}
	public Integer getPlaNroSucursal() {
		return plaNroSucursal;
	}
	public void setPlaNroSucursal(Integer plaNroSucursal) {
		this.plaNroSucursal = plaNroSucursal;
	}
	public double getPlaTotalMulta() {
		return plaTotalMulta;
	}
	public void setPlaTotalMulta(double plaTotalMulta) {
		this.plaTotalMulta = plaTotalMulta;
	}
	public double getMontoAnticipo() {
		return montoAnticipo;
	}
	public void setMontoAnticipo(double montoAnticipo) {
		this.montoAnticipo = montoAnticipo;
	}
	public String getDepNroDeposito() {
		return depNroDeposito;
	}
	public void setDepNroDeposito(String depNroDeposito) {
		this.depNroDeposito = depNroDeposito;
	}
	public Integer getPlaNroCuotas() {
		return PlaNroCuotas;
	}
	public void setPlaNroCuotas(Integer plaNroCuotas) {
		PlaNroCuotas = plaNroCuotas;
	}
	public Date getPlaFecSolicitud() {
		return plaFecSolicitud;
	}
	public void setPlaFecSolicitud(Date plaFecSolicitud) {
		this.plaFecSolicitud = plaFecSolicitud;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getDplNro() {
		return dplNro;
	}
	public void setDplNro(Integer dplNro) {
		this.dplNro = dplNro;
	}
	public String getDplConcepto() {
		return dplConcepto;
	}
	public void setDplConcepto(String dplConcepto) {
		this.dplConcepto = dplConcepto;
	}
	public Date getDplFecLimite() {
		return dplFecLimite;
	}
	public void setDplFecLimite(Date dplFecLimite) {
		this.dplFecLimite = dplFecLimite;
	}
	public double getDplMonto() {
		return dplMonto;
	}
	public void setDplMonto(double dplMonto) {
		this.dplMonto = dplMonto;
	}
	public double getDplAcuenta() {
		return dplAcuenta;
	}
	public void setDplAcuenta(double dplAcuenta) {
		this.dplAcuenta = dplAcuenta;
	}
	public double getDplSaldo() {
		return dplSaldo;
	}
	public void setDplSaldo(double dplSaldo) {
		this.dplSaldo = dplSaldo;
	}
	public String getEstadoDet() {
		return estadoDet;
	}
	public void setEstadoDet(String estadoDet) {
		this.estadoDet = estadoDet;
	}
	public Date getFechaPagoCuota() {
		return fechaPagoCuota;
	}
	public void setFechaPagoCuota(Date fechaPagoCuota) {
		this.fechaPagoCuota = fechaPagoCuota;
	}
	public String getNroDepositoCuenta() {
		return NroDepositoCuenta;
	}
	public void setNroDepositoCuenta(String nroDepositoCuenta) {
		NroDepositoCuenta = nroDepositoCuenta;
	}
	
	
	
}
