package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_programado_ejecutado", procedureName = "mteps_plan.f_programado_ejecutado", resultClasses = {
		F_programado_ejecutado.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_seguimiento", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_programado_ejecutado {
	@Id
	public Integer id;
	public Integer id_plan;
	public String unidad_funcional;
	public String descripcion;
	public String nombre_seguimiento;
	public String mes_ini;
	public String mes_fin;
	public double p_ene;
	public double p_feb;
	public double p_mar;
	public double p_abr; 
	public double p_may; 
	public double p_jun;
	public double p_jul;
	public double p_ago; 
	public double p_sep;
	public double p_oct; 
	public double p_nov;
	public double p_dic; 
	public double e_ene; 
	public double e_feb;
	public double e_mar;
	public double e_abr; 
	public double e_may; 
	public double e_jun;
	public double e_jul; 
	public double e_ago; 
	public double e_sep; 
	public double e_oct;
	public double e_nov;
	public double e_dic;
	public String medidas_correctivas;
	public String resultados;
	public String dificultades;
}
