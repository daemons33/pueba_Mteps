package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_reformulaciones", procedureName = "mteps_plan.f_lista_reformulaciones", resultClasses = {
		F_lista_reformulaciones.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_reformulaciones {
	@Id
	public Integer id_reformulacion;
	public String justificacion;
	public String observacion;

}
