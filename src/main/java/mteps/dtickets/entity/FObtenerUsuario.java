package mteps.dtickets.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_d_tickets.f_obtener_usuario", procedureName = "mteps_d_tickets.f_obtener_usuario", resultClasses = {
		FObtenerUsuario.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_usuario", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FObtenerUsuario {
	@Id
	public int idUsuario;
    public String login;
    public int tipoDocumento;
    public int lugarExpedido;
    public String nroDocumento;
    public String complemento;
    public String nombres;
    public String apellidoPaterno;
    public String apellidoMaterno;
    @Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    public Date fechaNacimiento;
    public String direccion;
    public String correo;
    public String telefono;
    public String observacion;
    
}
