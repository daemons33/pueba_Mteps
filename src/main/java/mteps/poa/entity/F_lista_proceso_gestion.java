package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_procesos_gestion", procedureName = "mteps_plan.f_lista_procesos_gestion", resultClasses = {
		F_lista_proceso_gestion.class }, parameters = {
				@StoredProcedureParameter(name = "p_idtipoproceso", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "p_gestion", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_proceso_gestion {
	@Id
	public Integer id_proceso;
	public Integer id_proceso_superior;
	public Integer id_plan;
	public Integer id_tipo_proceso;
	public String sigla;
	public String nombre;
	public String descripcion;
	public String sigla_pei;
	public String nombre_pei;
}
