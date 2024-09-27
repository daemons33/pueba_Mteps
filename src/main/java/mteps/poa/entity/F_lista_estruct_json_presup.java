package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_estruct_json_presup", procedureName = "mteps_plan.f_lista_estruct_json_presup", resultClasses = {
		F_lista_estruct_json_presup.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_proceso", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_indicador", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_estruct_json_presup {
	
	public Integer id_indicador_presupuesto;
	@Id
	public Integer interval_valor;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date inicio;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fin;
	public double inversion_publica;
	public double gasto_corriente;
}
