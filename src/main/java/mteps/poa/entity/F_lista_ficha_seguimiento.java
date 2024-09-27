package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_ficha_seguimiento", procedureName = "mteps_plan.f_lista_ficha_seguimiento", resultClasses = {
		F_lista_ficha_seguimiento.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_seguimiento", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_ficha_seguimiento {
	@Id
	public Integer id_seguimiento;
	public Integer id_plan;
	public String prog_eje_fis_plan_json;
	public String prog_ejec_ptto_plan_json;
	public String resultados;
	public String dificultades;
	public Integer id_usuario_creado;
	public Date fecha_creado;
	public Integer id_usuario_modificado;
	public Date fecha_modificado;
	public String transaccion;
	public String observacion;
	public String estado;
}
