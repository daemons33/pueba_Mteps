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

@NamedStoredProcedureQuery(name="mteps_d_tickets.f_obtener_datos_ticket",
procedureName = "mteps_d_tickets.f_obtener_datos_ticket",
resultClasses = {FObtenerDatosTicket.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_ticket", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class FObtenerDatosTicket {

	@Id
	 public Integer id;
	 public String codigo;
	 public String departamento;
	 public String tipoTramite;
	 public Integer nroTramites;
	 public String usuario;
	 @Temporal(TemporalType.TIMESTAMP)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	 public Date fechaSolicitudTicket;
	 @Temporal(TemporalType.DATE)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="GMT-4")
	 public Date fechaAtencion;
	 public Time horaInicio;
	 public Time horaFin;
	 public String nombre;
	 public String correo;
	 public Integer idEmpresa;
	 public String estado;
}
