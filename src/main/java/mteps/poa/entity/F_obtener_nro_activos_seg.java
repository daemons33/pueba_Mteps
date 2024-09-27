package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_obtener_nro_activos_seg", procedureName = "mteps_plan.f_obtener_nro_activos_seg", resultClasses = {
		F_obtener_nro_activos_seg.class }, parameters = {
				@StoredProcedureParameter(name = "i_estado", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_nro_activos_seg {
	
	@Id
	public Integer nro;

}
