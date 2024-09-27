package mteps.ovt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntidadPersona {
	public Integer tipo;
	@Id
	public String idPersona;
	public String nombres;
	public String paterno;
	public String materno;
	public String tipoDocumento;
	public String nroDocumento;
	public String complemento;
	public String lugarExpedicion;
	public String nacionalidad;
	public String genero;
	public Date fechaNacimiento;
	public Boolean jubilado;
	public Boolean aportaAfp;
	public String nroNuaCua;
	public Date fechaRegJubilado;
	public Date fechaRegDecapafp;
	public Boolean compRegistrado;
	public Integer estadoSegip;
	public Date fechaVerificacion;
	public Date fechaExpiracion;
	public String estadoVerificacion;
	public String observacionSegip;
	public Boolean complementoVisible;
	public Date fechaRetiro;
	public String descripcion;
	public String periodoPlanilla;
	public Integer gestionPlanilla;
	public String nit;
	public Integer idTrabajador;
}
