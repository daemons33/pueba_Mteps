package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;
@NamedStoredProcedureQuery(name = "rrhh.f_obtener_usuario", procedureName = "rrhh.f_obtener_usuario", resultClasses = {
		FuncionDatosUsuariov2.class }, parameters = {
				@StoredProcedureParameter(name = "v_dato", type = String.class, mode = ParameterMode.IN)})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionDatosUsuariov2 {
	@Id
	public Integer idUsuario;
	public String usuario;
	public String nombres;
	public String apellidoPaterno;
	public String apellidoMaterno;
	public String email;
	public String departamento;
	public String unidadOrganizacional;
	public Integer idUnidad;
	public Integer idUnidadDependiente;
	public String cargo;
	public Boolean estado;
	public String ci;
	public String ci_exp;
	public Integer item;
	public String area;
}
