package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_rrhh.f_obtener_dependientes", procedureName = "mteps_rrhh.f_obtener_dependientes", resultClasses = {
		FuncionObtenerDependientes.class }, parameters = {
				@StoredProcedureParameter(name = "login", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FuncionObtenerDependientes {
	
	@Id
	public Integer idPersona;
	public String nombreCargo;
	public String nombre;
	public String cargo;
}
