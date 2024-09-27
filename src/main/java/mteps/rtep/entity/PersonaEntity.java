package mteps.rtep.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
public class PersonaEntity {
	
	@Id
	public Long idPersona;
    public Integer tipoDocumento;
    public Integer lugarExpedido;
    public String nroDocumento;
    public String complemento;
    public String nombres;
    public String apellidoPaterno;
    public String apellidoMaterno;
    @Temporal(TemporalType.DATE)
    public Date fechaNacimiento;
    public Integer genero;
    public Integer pais;
    public Integer nivelEducativo;
    public Integer departamento;
    public Integer municipio;
    public String zona;
    public String barrio;
    public String calleAvenida;
    public String piso;
    public String numeroDomicilio;
    public String contactoTelefonoFijo;
    public String contactoCelular;
    public String contactoWhatsapp;
    public String contactoNombre;
    public String estado;
    public String transaccion;
    public Integer usuarioCreacion;
    public Integer usuarioModificacion;
    public Timestamp fechaCreacion;
    public Timestamp fechaModificacion;
    public String observacion;
    public Long idUsuario;
    public Boolean validacionSegip;
    public Integer provincia;
    public Timestamp fechaProgramadaValidacionSegip;
    public Boolean notificado;
    public Boolean activo;
    
    public String tipoDocumentoNombre;
    public String lugarExpedidoNombre;
    public String generoNombre;
    public String paisNombre;
    public String departamentoNombre;
    public String municipioNombre;
    public String provinciaNombre;
    public String correoElectronico;
}
