package mteps.tramites.fondoCustodia.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_requisitos_fc",
procedureName = "mteps_tramites.f_obtener_requisitos_fc",
resultClasses = {FObtenerRequisitosFC.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_clasificador", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FObtenerRequisitosFC {
	@Id
	public Integer idRequisito;
	public String nombre;
}
