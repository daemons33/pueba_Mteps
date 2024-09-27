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

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_estruct_json_meta", procedureName = "mteps_plan.f_lista_estruct_json_meta", resultClasses = {
		F_lista_estruct_json_meta.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_proceso", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_id_indicador", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_estruct_json_meta {

	
	public Integer id_indicador_meta;
	@Id
	public Integer interval_valor; 
	public String etiqueta;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date inicio; 
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fin; 
	public double valor_programado; 
	public double valor_ejecutado;
}
