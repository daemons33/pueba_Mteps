package mteps.poa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_te_memoria_ref", procedureName = "mteps_plan.f_lista_te_memoria_ref", resultClasses = {
		F_lista_te_memoria_ref.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_te_memoria_ref {
	@Id
	public Integer id_proceso;
	public String sigla_te;
	public String desc_te;
	public Integer id_proceso_a;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Object memorias_detalle;
}
