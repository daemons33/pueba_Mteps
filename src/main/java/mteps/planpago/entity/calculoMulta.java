package mteps.planpago.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name="mteps_pagos.f_calculo_multas",
procedureName = "mteps_pagos.f_calculo_multas",
resultClasses = { calculoMulta.class },

parameters = {
		@StoredProcedureParameter(name = "p_periodo", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_gestion",type = Integer.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_monto_total_ganado",type = BigDecimal.class , mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_caso_lp",type =Boolean.class , mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class calculoMulta {
@Id
public double vTotalcalculomulta; 
public double vAnticipo;
protected double getvTotalcalculomulta() {
	return vTotalcalculomulta;
}
protected void setvTotalcalculomulta(double vTotalcalculomulta) {
	this.vTotalcalculomulta = vTotalcalculomulta;
}
protected double getvAnticipo() {
	return vAnticipo;
}
protected void setvAnticipo(double vAnticipo) {
	this.vAnticipo = vAnticipo;
}

}
