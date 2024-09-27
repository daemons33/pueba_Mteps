package mteps.planpago.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(
        name = "mteps_pagos.f_obtener_lista_plan_pagos3",
        procedureName = "mteps_pagos.f_obtener_lista_plan_pagos3",
        resultClasses = { planpagoEstado.class },
        parameters = {
                @StoredProcedureParameter(name = "var_estado", type = String.class, mode = ParameterMode.IN)
        }
)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class planpagoEstado {
	@Id
	public Integer id_plan_pago;
	public String pla_razon_social;
	public String pla_periodo_planilla;
	public String pla_nit_empresa;
	public Integer pla_nro_sucursal;
	public String pla_tipo_declaracion;
	public String pla_tipo_planilla;
	 @Temporal(TemporalType.DATE)
	public Calendar pla_fec_solicitud;

	public double pla_total_multa;
	public Integer pla_nro_cuotas;

	public String estado;

	public String pla_gestion_planilla;

		
	

	public String getPla_razon_social() {
		return pla_razon_social;
	}
	public void setPla_razon_social(String pla_razon_social) {
		this.pla_razon_social = pla_razon_social;
	}
	public Integer getId_plan_pago() {
		return id_plan_pago;
	}
	public void setId_plan_pago(Integer id_plan_pago) {
		this.id_plan_pago = id_plan_pago;
	}
	public String getPla_periodo_planilla() {
		return pla_periodo_planilla;
	}
	public void setPla_periodo_planilla(String pla_periodo_planilla) {
		this.pla_periodo_planilla = pla_periodo_planilla;
	}
	public String getPla_nit_empresa() {
		return pla_nit_empresa;
	}
	public void setPla_nit_empresa(String pla_nit_empresa) {
		this.pla_nit_empresa = pla_nit_empresa;
	}
	public Integer getPla_nro_sucursal() {
		return pla_nro_sucursal;
	}
	public void setPla_nro_sucursal(Integer pla_nro_sucursal) {
		this.pla_nro_sucursal = pla_nro_sucursal;
	}
	public String getPla_tipo_declaracion() {
		return pla_tipo_declaracion;
	}
	public void setPla_tipo_declaracion(String pla_tipo_declaracion) {
		this.pla_tipo_declaracion = pla_tipo_declaracion;
	}
	public String getPla_tipo_planilla() {
		return pla_tipo_planilla;
	}
	public void setPla_tipo_planilla(String pla_tipo_planilla) {
		this.pla_tipo_planilla = pla_tipo_planilla;
	}
	public Calendar getPla_fec_solicitud() {
		return pla_fec_solicitud;
	}
	public void setPla_fec_solicitud(Calendar pla_fec_solicitud) {
		this.pla_fec_solicitud = pla_fec_solicitud;
	}

	public double getPla_total_multa() {
		return pla_total_multa;
	}
	public void setPla_total_multa(double pla_total_multa) {
		this.pla_total_multa = pla_total_multa;
	}
	public Integer getPla_nro_cuotas() {
		return pla_nro_cuotas;
	}
	public void setPla_nro_cuotas(Integer pla_nro_cuotas) {
		this.pla_nro_cuotas = pla_nro_cuotas;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	public String getPla_gestion_planilla() {
		return pla_gestion_planilla;
	}
	public void setPla_gestion_planilla(String pla_gestion_planillla) {
		this.pla_gestion_planilla = pla_gestion_planillla;
	}

	
	
}
