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

@NamedStoredProcedureQuery(name = "workflow.f_obtener_lista_perfiles", procedureName = "workflow.f_obtener_lista_perfiles", resultClasses = {
		F_obtener_lista_perfiles.class }, parameters = {
				@StoredProcedureParameter(name = "p_modulo", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_lista_perfiles {
	@Id
	public String perfil;
	public String descripcion_perfil;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creacion;
	public String estado;
	public String title; 
	public String rol;
}
