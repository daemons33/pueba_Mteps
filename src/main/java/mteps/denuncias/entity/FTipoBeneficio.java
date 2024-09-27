package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_tipo_beneficios", procedureName = "mteps_denuncias.f_obtener_tipo_beneficios", resultClasses = {
		FTipoBeneficio.class })
@Entity
public class FTipoBeneficio {
	@Id
	public Integer idClasificador;
	public String nombre;

}
