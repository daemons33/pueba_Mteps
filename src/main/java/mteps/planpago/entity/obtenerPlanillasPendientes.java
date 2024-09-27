package mteps.planpago.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;


@NamedStoredProcedureQuery(name="mteps_pagos.f_obtener_planillas_pendientes",
procedureName = "mteps_pagos.f_obtener_planillas_pendientes",
resultClasses = {obtenerPlanillasPendientes.class},
parameters = {
		@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_json_cd", type = String.class, mode = ParameterMode.IN)
})

@Entity

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)

@JsonInclude(JsonInclude.Include.NON_NULL)
public class obtenerPlanillasPendientes {
	@Id
	public String idplanilla;
	public String periodo;
	public Integer gestion;
	public String tipoplanilla;
	public String estadodeclaracion;
	public String modalidad;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public List<listaSucursalPlanillaPendiente> sucursal;
	

	protected String getIdplanilla() {
		return idplanilla;
	}
	protected void setIdplanilla(String idplanilla) {
		this.idplanilla = idplanilla;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Integer getGestion() {
		return gestion;
	}
	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}
	public String getTipoplanilla() {
		return tipoplanilla;
	}
	public void setTipoplanilla(String tipoplanilla) {
		this.tipoplanilla = tipoplanilla;
	}
	public String getEstadodeclaracion() {
		return estadodeclaracion;
	}
	public void setEstadodeclaracion(String estadodeclaracion) {
		this.estadodeclaracion = estadodeclaracion;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public List<listaSucursalPlanillaPendiente> getSucursal() {
		return sucursal;
	}
	public void setSucursal(List<listaSucursalPlanillaPendiente> sucursal) {
		this.sucursal = sucursal;
	}


	
}
