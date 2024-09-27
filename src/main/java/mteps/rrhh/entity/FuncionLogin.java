package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name = "mteps_rrhh.f_obtener_login", procedureName = "mteps_rrhh.f_obtener_login", resultClasses = {
		FuncionLogin.class }, parameters = {
				@StoredProcedureParameter(name = "v_dato", type = String.class, mode = ParameterMode.IN)})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionLogin {
	@Id
	public String usuario;
	public String email;
	public String nombreCompleto;
	public String departamento;
	public String puesto;
}
