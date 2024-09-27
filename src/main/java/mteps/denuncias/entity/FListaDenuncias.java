package mteps.denuncias.entity;

import java.sql.Array;
import java.util.Date;
import java.util.List;

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

import mteps.consultas.entity.Prefiniquito;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_denuncias", procedureName = "mteps_denuncias.f_obtener_denuncias", resultClasses = {
		FListaDenuncias.class }, parameters = {
				@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FListaDenuncias {
	@Id
	public Integer idDenuncia ;
	public String  codigoDenuncia ;
	public Integer idConsulta ;
	public Integer idEmpresa ;
	public Boolean empresaExterna ;
	public String nit ;
	public String razonSocial ;
	public String matriculaComercio ;
	public String correoElectronico;
	public String departamento;
	public String direccion;
	public String telefonoEmpresa;
	public Integer idClasificadorDenuncia ;
	public String nombreDenuncia ;
	public Boolean sectorTrabajador ;
	public Boolean estadoLaboral ;
	public String hojaRuta ;
	public Integer idTipoDenuncia ;
	public String nombreTipoDenuncia ;
	public Integer idTipoDerecho ;
	public String nombreTipoDerecho ;
	public Integer idSubtipoDerecho ;
	public String nombreSubtipoDerecho ;
	public String detalleDenuncia ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntDenunciantes> denunciantes ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EndDenunciados> denunciados ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public  List<EntBeneficiosSociales> beneficiosSociales;
	public String observacion ;
	
	public String periodo;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaIngreso;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaRetiro;
	public Integer idTipoRetiro;
	public String tipoRetiro;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List jsonTiposBeneficios;
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Prefiniquito jsonPreFiniquito ;
	public String estado ;
	public String transaccion ;
	public Integer usuarioCreacion ;
	public String nombreUsuarioCreacion ;
	public Integer usuarioModificacion ;
	public String nombreUsuarioModificacion ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	public Date fecCuartoIntermedio;
	/*
	 *NO APLICAR EN PROD
	*/
	public Integer idcit1; 
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date feccit1;
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	public Date horacit1;
	public Integer idcit2;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date feccit2;
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	public Date horacit2;
	public Integer idcitu;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date feccitu;
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	public Date horacitu;	 
	public Integer id_pre_finiquito;
	
}
