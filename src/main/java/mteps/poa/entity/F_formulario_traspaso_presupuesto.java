package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_formulario_traspaso_presupuesto", procedureName = "mteps_plan.f_formulario_traspaso_presupuesto", resultClasses = {
		F_formulario_traspaso_presupuesto.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_formulario_traspaso_presupuesto {
	@Id
	public Integer id;
	public String apertura;
	public String unidad;
	public String act_sigla; 
	public String act_nombre; 
	public String te_sigla;
	public String te_nombre;
	public String cod_mc;
	public String tipo;
	public String partida;
	public String ff;
	public String nombre_partida;
	public double importe;
	public String justificacion;
	public String observacion;
}
