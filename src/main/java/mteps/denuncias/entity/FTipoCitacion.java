package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_tipo_citacion", procedureName = "mteps_denuncias.f_obtener_tipo_citacion", resultClasses = {
		FTipoCitacion.class })
@Entity
public class FTipoCitacion {
	@Id
	public Integer idClasificador;
	public String nombre;
}
