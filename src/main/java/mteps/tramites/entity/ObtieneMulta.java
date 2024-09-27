package mteps.tramites.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_multa_retraso",
procedureName = "mteps_tramites.f_obtener_multa_retraso",
resultClasses = {ObtieneMulta.class},
parameters = {
		@StoredProcedureParameter(name = "var_montoplanilla", type = BigDecimal.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "var_minera", type = Boolean.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "var_fechapagoprima", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "var_fecha_fin_declaracion", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class ObtieneMulta {
	@Id
	public Double vTotalMulta;
	public Integer vTotalDiasRetraso;
}
