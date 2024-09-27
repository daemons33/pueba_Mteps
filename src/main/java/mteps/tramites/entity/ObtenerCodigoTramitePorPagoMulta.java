package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_tramites.f_pago_multa",
procedureName = "mteps_tramites.f_pago_multa",
resultClasses = {ObtenerCodigoTramitePorPagoMulta.class},
parameters = {
		@StoredProcedureParameter(name = "codigo", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class ObtenerCodigoTramitePorPagoMulta {
 @Id
 public String codTramite;
}
