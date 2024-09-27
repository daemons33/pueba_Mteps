package mteps.tickets.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_tickets.f_obtener_clasificadores",
procedureName = "mteps_tickets.f_obtener_clasificadores",
resultClasses = {FuncionObtenerClasificador.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_clasificador", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionObtenerClasificador {
	@Id
	public Integer idClasificador;
	public String dominio;
	public String nombre;
	public Integer idClasificadorRaiz;
	public String variables;
}
