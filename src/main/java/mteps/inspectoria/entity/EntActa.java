package mteps.inspectoria.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class EntActa {
	@Id
	public String nit; 
	public String roe;
	public String login;
	public String estado;
	public String correos;
	public String direccion;
	public String telefonos;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	public Date horaVisita;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<actas> detalleActa;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaVisita;
	public String nroPatronal;
	public String razonSocial;
	public String transaccion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EareasEmpresa> areasEmpresa;
	public Integer idActaPrevia;
	public Integer idInspeccion;
	public String nroDocumento;
	public Integer nroEventuales;
	public Integer nroOperativos;
	public Integer nroMenoresEdad; 
	public String nombreComercial;
	public String tipoInstitucion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date antiguedadEmpresa; 
	public String matriculaComercio;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EpersonasTrabajadores> personasEmpleador;
	public Integer nroAdministrativos;
	public String representanteLegal;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EpersonasTrabajadores> personasTrabajadores;
	public Boolean condicionPeligrosidad;
	public String descActividadDeclarada;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaPlazoParalizacion;
	public String descripcionTipoSociedad;
}
