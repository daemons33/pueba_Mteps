package mteps.tramites.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;
@NamedStoredProcedureQuery(name="parametro.f_valor_tramite",
procedureName = "parametro.f_valor_tramite",
resultClasses = {CostoTramite.class},
parameters = {
		@StoredProcedureParameter(name = "monto", type = BigDecimal.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "codigo_tramite", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostoTramite {
	@Id
	public Double costo;
}
