package mteps.workflow.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;



@NamedStoredProcedureQuery(name = "workflow.f_lista_roles_perfil", procedureName = "workflow.f_lista_roles_perfil", resultClasses = {
		F_lista_roles_perfil.class }, parameters = {
				@StoredProcedureParameter(name = "p_modulo", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "p_perfil", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_roles_perfil {
	
	@Id
	public String rol;
	public String descripcion_rol;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creacion;
	public String estado;
}
