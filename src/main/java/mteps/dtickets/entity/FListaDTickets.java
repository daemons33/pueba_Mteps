package mteps.dtickets.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;


@NamedStoredProcedureQuery(name="mteps_d_tickets.f_lista_d_tickets",
procedureName = "mteps_d_tickets.f_lista_d_tickets",
resultClasses = {FListaDTickets.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_usuario", type = Integer.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_estado", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FListaDTickets {
	@Id
	public Integer id;
    public String codigo;
    public Integer idTipoTramite;
    public String tipoTramite;
    public Integer idDepartamento;
    public String departamento;
    public Integer nroTramites;
    public String estado;
    public String transaccion;
    public Integer usuarioCreacion;
    public String usuario;
    @Temporal(TemporalType.TIMESTAMP)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	 public Date fechaSolicitudTicket;
    @Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaAtencion;
    public Time horaInicio;
    public Time horaFin;
    @Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object formularios;

}
