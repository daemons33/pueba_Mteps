package mteps.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "workflow.f_obtener_lista_menu_por_usuario", procedureName = "workflow.f_obtener_lista_menu_por_usuario", resultClasses = {
		F_obtener_lista_menu_por_usuario.class }, parameters = {
				@StoredProcedureParameter(name = "p_login", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "p_modulo", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_lista_menu_por_usuario {
	
	@Id	
	public Integer nro_menu; 
	public Integer nro_menu_padre;
	public String descripcion; 
	public String ruta;
	public String icono;
	public Integer orden;
	public String nombre; 
	public Boolean expand;

}
