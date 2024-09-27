package mteps.tickets.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name="mteps_tickets.f_obtener_lista_tickets",
procedureName = "mteps_tickets.f_obtener_lista_tickets",
resultClasses = {FuncionListaTicket.class},
parameters = {
		@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionListaTicket {
	@Id
	public Integer idTicket;
	public String codigoTicket;
	public String detalle;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public String estado;
	public Integer id_area;
	public String area;
	public Integer idCategoria;
	public String categoria; 
	public Integer idSubCategoria;
	public String SubCategoria;
	public float dias;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaTicket;

	
}
