package mteps.dtickets.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name="mteps_d_tickets.f_obtener_ticket_vigente",
procedureName = "mteps_d_tickets.f_obtener_ticket_vigente",
resultClasses = {FObtenerTicketVigente.class},
parameters = {
		@StoredProcedureParameter(name = "v_estado", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_login", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FObtenerTicketVigente {
	@Id
    public Integer id;    
    public String codigo;    
    public Integer idDepartamento;    
    public String departamento;    
    public Integer idTipoTramite;    
    public String tipoTramite;    
    public Integer nroTramites;    
    public String estado;    
    public String transaccion;    
    public Integer usuarioCreacion;
    @Temporal(TemporalType.TIMESTAMP)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	 public Date fechaSolicitudTicket;    
    @Temporal(TemporalType.DATE)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="GMT-4")
	 public Date  fechaAtencion;    
    public Time horaInicio;    
    public Time horaFin;    
    public Integer idUsuarioAsignado;    
    public String usuario;
    public String nombre;
}
