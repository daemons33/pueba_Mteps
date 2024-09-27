package mteps.inspectoria.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_inspecciones.f_obtener_acta", procedureName = "mteps_inspecciones.f_obtener_acta", resultClasses = {
		FObtenerActa.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FObtenerActa {
	@Id
	public Integer id ; 
	public String condicion ;
	public String orientacion ;
	public String norma ;
	public Integer ordenClasificador;
}
