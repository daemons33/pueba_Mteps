package mteps.denuncias.entity;

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

import mteps.inspectoria.entity.EPreFiniquito;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_denuncia_id", procedureName = "mteps_denuncias.f_obtener_denuncia_id", resultClasses = {
		FobtenerDenuncia.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FobtenerDenuncia {
	@Id
	public Integer idDenuncia ;
	public String  codigoDenuncia ;
	public Integer idConsulta ;
	public Integer idEmpresa ;
	public Boolean empresaExterna ;
	public String nit ;
	public String razonSocial ;
	public String matriculaComercio ;
	public String direccion;
	public String representanteLegal;
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
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public EPreFiniquito jsonPreFiniquito ;
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
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<ECitacion> citaciones ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	public Date fecCuartoIntermedio;

}
