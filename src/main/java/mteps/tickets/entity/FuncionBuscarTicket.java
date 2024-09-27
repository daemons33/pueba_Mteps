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

@NamedStoredProcedureQuery(name="mteps_tickets.f_buscador_ticket",
procedureName = "mteps_tickets.f_buscador_ticket",
resultClasses = {FuncionBuscarTicket.class},
parameters = {
		@StoredProcedureParameter(name = "v_dato", type = String.class, mode = ParameterMode.IN)
})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionBuscarTicket {
 @Id
 public Integer idTicket;
 public String codigoTicket;
 public String area;
 public String categoria;
 public String subCategoria;
 @Temporal(TemporalType.DATE)
 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
 public Date fechaCreacion;
 public String estado;
}
