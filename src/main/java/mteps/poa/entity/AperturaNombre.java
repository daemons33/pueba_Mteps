package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_apertura", procedureName = "mteps_plan.f_apertura", resultClasses = {
		AperturaNombre.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_apertura", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class AperturaNombre {
	@Id
	public String nombre;
}
