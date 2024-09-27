package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;



@NamedStoredProcedureQuery(name = "rrhh.f_lista_personal", procedureName = "rrhh.f_lista_personal", resultClasses = {
		F_lista_personal.class }, parameters = {})
@Entity
public class F_lista_personal {
	@Id 
	public String usuario;
	public Integer id_persona; 
	public String nombres; 
	public String apellido_paterno;
	public String apellido_materno; 
	public String nombre_completo; 
	public String correo;
	public Integer id_puesto; 
	public String puesto;
	public Integer id_org;
	public String org_unidad_funcional; 
	public Integer org_depende;
	public String unidad_area; 
	public String estado;
	public String perfiles;
}