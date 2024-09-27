package mteps.planpago.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_tickets.f_obtener_datos_correo",
procedureName = "mteps_tickets.f_obtener_datos_correo",
resultClasses = {obtenerDatosCorreo.class},
parameters = {
		@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class obtenerDatosCorreo {
	
	public String nombre;
	@Id
	public String codigoTicket;
	public String subCategoria;
	public String fechaModificacion;
	public String email;
	
}
