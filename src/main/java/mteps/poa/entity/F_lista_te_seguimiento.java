package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_te_seguimiento", procedureName = "mteps_plan.f_lista_te_seguimiento", resultClasses = {
		F_lista_te_seguimiento.class }, parameters = {
				@StoredProcedureParameter(name = "i_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_seguimiento", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_te_seguimiento {
	@Id
	public Integer id_proceso;
	public Integer id_plan;
	public String actsigla;
	public String actdescripcion; 
	public String tesigla; 
	public String tedescripcion;
	public String medios_verificacion; 
	public double meta_programada;
	public double meta_ejecutada; 
	public double eficiencia; 
	public String calificacion;
	public String medidas_correctivas;
	public String estado;
	public String obs;
	public String indicador;
	public String fuente_indicador;
}
