package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_estado_ptto_ref", procedureName = "mteps_plan.f_estado_ptto_ref", resultClasses = {
		F_estado_ptto_ref.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_estado_ptto_ref {

	@Id
	public Integer id;
	public String ff;
	public String partida;
	public String nombre_partida;
	public double ptto_actual ;
	public double diferencias;
	public double reformulado; 
	public Integer nro_mc_actual ;
	public Integer nro_mc_ref;
	public Integer nro_mc_creadas;

}
