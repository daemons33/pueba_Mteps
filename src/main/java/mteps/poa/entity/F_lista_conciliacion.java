package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_conciliacion", procedureName = "mteps_plan.f_lista_conciliacion", resultClasses = {
		F_lista_conciliacion.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_doc_conciliacion", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_fecha_corte", type = Date.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_conciliacion {
	@Id
	public Integer id;
	public String actividad_ptto;
	public String cat_prog;
	public Integer nro_partida;
	public String nombre;
	public String fuente;
	public String org;
	public double ptto_inicial;
	public double ptto_modificaciones; 
	public double ptto_vigente;
	public double ptto_preventivo;
	public double inicial_sispoa;
	public double vigente_sispoa; 
	public double devengado_sispoa;
}
