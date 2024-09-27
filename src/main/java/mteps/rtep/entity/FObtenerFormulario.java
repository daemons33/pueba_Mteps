package mteps.rtep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_rtep.f_obtener_formulario", procedureName = "mteps_rtep.f_obtener_formulario", resultClasses = {
		FObtenerFormulario.class })
@Entity
public class FObtenerFormulario {
	
	@Id
	public Integer idFormulario;
	public String codigo;
	public String nombre;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object grupos;

}
