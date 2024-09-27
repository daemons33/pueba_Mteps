package mteps.tickets.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name = "mteps_tickets.f_obtener_dependiente", procedureName = "mteps_tickets.f_obtener_dependiente", resultClasses = {
		FunctionObtenerDependiente.class }, parameters = {
				@StoredProcedureParameter(name = "usuario", type = String.class, mode = ParameterMode.IN) })
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FunctionObtenerDependiente {
	@Id
	public Integer id;
	public String nombre;
	public String puesto;
	public String area;
}
