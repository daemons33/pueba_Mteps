package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_tipo_retiro", procedureName = "mteps_denuncias.f_obtener_tipo_retiro", resultClasses = {
		FTipoRetiro.class })
@Entity
public class FTipoRetiro {
	@Id
	public Integer idClasificador;
	public String nombre;
}
