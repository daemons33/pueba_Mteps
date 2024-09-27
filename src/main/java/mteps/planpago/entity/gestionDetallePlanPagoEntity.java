package mteps.planpago.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_pagos.gestiondetalleplanpago",
procedureName = "mteps_pagos.gestiondetalleplanpago",
resultClasses = {gestionDetallePlanPagoEntity.class},
parameters = {
		@StoredProcedureParameter(name = "band", type = double.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cpla_id", type = Integer.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_tipo", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_nro", type = Integer.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_concepto", type =String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_fec_limite", type =Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_monto", type = double.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_acuenta", type = double.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_saldo", type = double.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_fec_comprobante", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_nro_comprobante", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cdpl_estado", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "caud_fec_creacion", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "caud_fec_modificacion", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cusu_id_creacion", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "cusu_id_modificacion", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class gestionDetallePlanPagoEntity {
@Id
public Integer id;
}
