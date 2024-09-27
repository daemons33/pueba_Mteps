package mteps.planpago.entity;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_pagos.p_gestion_plan_pagos",
procedureName = "mteps_pagos.p_gestion_plan_pagos",
resultClasses = {gestionPlanPagoEntity.class},
parameters = {
		@StoredProcedureParameter(name = "p_postulacion_vac", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_id_tabla", type = Integer.class, mode = ParameterMode.INOUT)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class gestionPlanPagoEntity {
	@Id
	public Integer id;
}
