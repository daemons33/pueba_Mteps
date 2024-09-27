package mteps.consultas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "externos_mteps.f_obtener_persona", procedureName = "externos_mteps.f_obtener_persona", resultClasses = {
		FobtenerPersona.class }, parameters = {
				@StoredProcedureParameter(name = "i_nro_documento", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_tipo_documento", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_complemento", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FobtenerPersona {
	@Id
	public Integer idpersona;
	public Integer tipodocumento;
	public String nrodocumento;
	public String complemento;
	public Integer lugarexpedicion;
	public String nombre;
	public String domicilio;
	public String telefono;
	public String correo;
	public Integer genero;
	public Integer edad;
}
