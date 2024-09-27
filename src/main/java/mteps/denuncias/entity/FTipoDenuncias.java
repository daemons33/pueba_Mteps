package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_tipo_denuncia", procedureName = "mteps_denuncias.f_obtener_tipo_denuncia", resultClasses = {
		FTipoDenuncias.class })
@Entity
public class FTipoDenuncias {
@Id
public Integer idClasificador;
public String nombre;

}
