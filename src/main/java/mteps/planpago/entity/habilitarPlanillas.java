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

@NamedStoredProcedureQuery(name="mteps_pagos.f_habilitar_planillas",
procedureName = "mteps_pagos.f_habilitar_planillas",
resultClasses = {habilitarPlanillas.class},
parameters = {
		@StoredProcedureParameter(name = "p_id_plan_pago", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class habilitarPlanillas {
@Id
public Integer pPlaGestionPlanilla;
public String pPlaPeriodoPlanilla;
public String pPlaNitEmpresa;
public String pPlaRazonSocial;
public String pPlaMatricula;
public String pPcTipoSigla;
public String pPcNombre;
@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "YYYY-MM-dd")
public Date pFechaModificacion;
public Integer pPlaCodigoSucursal;
public String pEmail;
public String pItmCiudad;


public String getpPlaRazonSocial() {
	return pPlaRazonSocial;
}
public void setpPlaRazonSocial(String pPlaRazonSocial) {
	this.pPlaRazonSocial = pPlaRazonSocial;
}


public Integer getpPlaCodigoSucursal() {
	return pPlaCodigoSucursal;
}
public void setpPlaCodigoSucursal(Integer pPlaCodigoSucursal) {
	this.pPlaCodigoSucursal = pPlaCodigoSucursal;
}
public Integer getpPlaGestionPlanilla() {
	return pPlaGestionPlanilla;
}
public void setpPlaGestionPlanilla(Integer pPlaGestionPlanilla) {
	this.pPlaGestionPlanilla = pPlaGestionPlanilla;
}
public String getpPlaPeriodoPlanilla() {
	return pPlaPeriodoPlanilla;
}
public void setpPlaPeriodoPlanilla(String pPlaPeriodoPlanilla) {
	this.pPlaPeriodoPlanilla = pPlaPeriodoPlanilla;
}
public String getpPlaNitEmpresa() {
	return pPlaNitEmpresa;
}
public void setpPlaNitEmpresa(String pPlaNitEmpresa) {
	this.pPlaNitEmpresa = pPlaNitEmpresa;
}
public String getpPlaMatricula() {
	return pPlaMatricula;
}
public void setpPlaMatricula(String pPlaMatricula) {
	this.pPlaMatricula = pPlaMatricula;
}
public String getpPcTipoSigla() {
	return pPcTipoSigla;
}
public void setpPcTipoSigla(String pPcTipoSigla) {
	this.pPcTipoSigla = pPcTipoSigla;
}
public String getpPcNombre() {
	return pPcNombre;
}
public void setpPcNombre(String pPcNombre) {
	this.pPcNombre = pPcNombre;
}
public Date getpFechaModificacion() {
	return pFechaModificacion;
}
public void setpFechaModificacion(Date pFechaModificacion) {
	this.pFechaModificacion = pFechaModificacion;
}

public String getpEmail() {
	return pEmail;
}
public void setpEmail(String pEmail) {
	this.pEmail = pEmail;
}
public String getpItmCiudad() {
	return pItmCiudad;
}
public void setpItmCiudad(String pItmCiudad) {
	this.pItmCiudad = pItmCiudad;
}


}
