package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;


@NamedStoredProcedureQuery(name = "mteps_plan.f_ejecucion_presupuestaria_consolidada", procedureName = "mteps_plan.f_ejecucion_presupuestaria_consolidada", resultClasses = {
		F_ejecucion_presupuestaria_consolidada.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_gestion", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_ejecucion_presupuestaria_consolidada {
	@Id
	public Integer id_clasificador;
	public String sigla;
	public String nombre;
	public double presup_inicial;
	public double modificacion_aprobada;
	public double presupuesto_vigente;
	public double preventivo;
	public double saldo;

}
