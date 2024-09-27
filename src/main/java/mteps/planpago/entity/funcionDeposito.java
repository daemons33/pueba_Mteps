package mteps.planpago.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_pagos.p_gestion_depositos",
procedureName = "mteps_pagos.p_gestion_depositos",
parameters = {
		@StoredProcedureParameter(name = "p_deposito", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_monto", type = Integer.class, mode = ParameterMode.INOUT)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class funcionDeposito {
@Id	
public Integer monto;


}
