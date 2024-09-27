package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_obtener_dato_firma", procedureName = "mteps_plan.f_obtener_dato_firma", resultClasses = {
		F_obtener_dato_firma.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_usuario", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_dato_firma {
	
	@Id
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd/MM/yyy")
	public Date fecha_creacion;
	public String img;

}
