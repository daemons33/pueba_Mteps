package mteps.tramites.fondoCustodia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_cta_fc",
procedureName = "mteps_tramites.f_obtener_cta_fc",
resultClasses = {FObtenerCtaFc.class},
parameters = {
		@StoredProcedureParameter(name = "i_jefatura", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "i_tipo", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class FObtenerCtaFc {
	@Id
	public Integer resultado;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object datoadicional;
	public Boolean correcto;
	public String notificacion;
}
