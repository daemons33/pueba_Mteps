package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_requisitos",
procedureName = "mteps_tramites.f_obtener_requisitos",
resultClasses = {Requisitos.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_clasificador", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Requisitos {
	@Id
	public Integer idRequisito;
	public String nombre;
}


