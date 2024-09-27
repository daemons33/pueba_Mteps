package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name = "mteps_rrhh.f_obtener_datos_usuario", procedureName = "mteps_rrhh.f_obtener_datos_usuario", resultClasses = {
		FuncionDatosUsuario.class }, parameters = {
				@StoredProcedureParameter(name = "v_dato", type = String.class, mode = ParameterMode.IN)})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionDatosUsuario {
	@Id
	public Integer idUsuario;
	public String usuario;
	public String password;
	public String nombres;
	public String apellidoPaterno;
	public String apellidoMaterno;
	public String email;
	public String departamento;
	public Integer idUnidad;
	public Integer idUnidadDependiente;
	public String puesto;
	public Boolean estado;
		
}
