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

@NamedStoredProcedureQuery(name="mteps_pagos.f_obtener_lista_plan_pagos",
							procedureName = "mteps_pagos.f_obtener_lista_plan_pagos",
							resultClasses = {listaPlanPago.class},
							parameters = {
									@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)
							})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class listaPlanPago  {
	
	@Id
	public Integer idPlanPago;
	public String plaNitEmpresa;
	public String plaRazonSocial;
	public Integer plaNroSucursal;
	public String plaPeriodoPlanilla;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date plaFecSolicitud;
	public String estado;
	public Integer tipoDeclaracionId;
	public String tipoDeclaracion;
	public Integer idDepositoAnticipo;
	public Integer idDepositoServicio;
	public double plaTotalMulta;
	public Integer plaNroCuotas;
	public String plaGestionPlanilla;
	public Integer idEmpresa;
	public String observacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public Integer usuarioCreacion;
	public Integer usuarioModificacion;
	public String plaMatricula;
	public Integer tipoPlanillaId;
	public String tipoPlanilla;
	public String transaccion;
	public double plaMontoPlanillaAnt;
	public Integer plaCodigoSucursal;
	public String plaDepartamento;
	
	public String getPlaDepartamento() {
		return plaDepartamento;
	}
	public void setPlaDepartamento(String plaDepartamento) {
		this.plaDepartamento = plaDepartamento;
	}
	protected Integer getPlaCodigoSucursal() {
		return plaCodigoSucursal;
	}
	protected void setPlaCodigoSucursal(Integer plaCodigoSucursal) {
		this.plaCodigoSucursal = plaCodigoSucursal;
	}
	public double getPlaMontoPlanillaAnt() {
		return plaMontoPlanillaAnt;
	}
	public void setPlaMontoPlanillaAnt(double plaMontoPlanillaAnt) {
		this.plaMontoPlanillaAnt = plaMontoPlanillaAnt;
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
	public String getPlaRazonSocial() {
		return plaRazonSocial;
	}
	public void setPlaRazonSocial(String plaRazonSocial) {
		this.plaRazonSocial = plaRazonSocial;
	}
	public Integer getPlaNroSucursal() {
		return plaNroSucursal;
	}
	public void setPlaNroSucursal(Integer plaNroSucursal) {
		this.plaNroSucursal = plaNroSucursal;
	}
	public String getPlaPeriodoPlanilla() {
		return plaPeriodoPlanilla;
	}
	public void setPlaPeriodoPlanilla(String plaPeriodoPlanilla) {
		this.plaPeriodoPlanilla = plaPeriodoPlanilla;
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
	public Integer getTipoDeclaracionId() {
		return tipoDeclaracionId;
	}
	public void setTipoDeclaracionId(Integer tipoDeclaracionId) {
		this.tipoDeclaracionId = tipoDeclaracionId;
	}
	public String getTipoDeclaracion() {
		return tipoDeclaracion;
	}
	public void setTipoDeclaracion(String tipoDeclaracion) {
		this.tipoDeclaracion = tipoDeclaracion;
	}
	public Integer getIdDepositoAnticipo() {
		return idDepositoAnticipo;
	}
	public void setIdDepositoAnticipo(Integer idDepositoAnticipo) {
		this.idDepositoAnticipo = idDepositoAnticipo;
	}
	public Integer getIdDepositoServicio() {
		return idDepositoServicio;
	}
	public void setIdDepositoServicio(Integer idDepositoServicio) {
		this.idDepositoServicio = idDepositoServicio;
	}

	public double getPlaTotalMulta() {
		return plaTotalMulta;
	}
	public void setPlaTotalMulta(double plaTotalMulta) {
		this.plaTotalMulta = plaTotalMulta;
	}
	public Integer getPlaNroCuotas() {
		return plaNroCuotas;
	}
	public void setPlaNroCuotas(Integer plaNroCuotas) {
		this.plaNroCuotas = plaNroCuotas;
	}
	public String getPlaGestionPlanilla() {
		return plaGestionPlanilla;
	}
	public void setPlaGestionPlanilla(String plaGestionPlanilla) {
		this.plaGestionPlanilla = plaGestionPlanilla;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public String getPlaMatricula() {
		return plaMatricula;
	}
	public void setPlaMatricula(String plaMatricula) {
		this.plaMatricula = plaMatricula;
	}
	public Integer getTipoPlanillaId() {
		return tipoPlanillaId;
	}
	public void setTipoPlanillaId(Integer tipoPlanillaId) {
		this.tipoPlanillaId = tipoPlanillaId;
	}
	public String getTipoPlanilla() {
		return tipoPlanilla;
	}
	public void setTipoPlanilla(String tipoPlanilla) {
		this.tipoPlanilla = tipoPlanilla;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	


}