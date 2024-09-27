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

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_reporte_denuncias", procedureName = "mteps_denuncias.f_reporte_denuncias", resultClasses = {
		FReporteDenuncias.class }, parameters = {
				@StoredProcedureParameter(name = "v_login", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_ini", type = Date.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_fin", type = Date.class, mode = ParameterMode.IN)})
@Entity
public class FReporteDenuncias {
	
	@Id
	public Integer id;
	public String departamento;
	public Integer total;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle> detalle;
	
	public static class Detalle{
	    public int id_denuncia;
	    public String departamento;
	    public String responsable;
	    public String cargo;
	    public String codigo_denuncia;
	    @Temporal(TemporalType.DATE)
	    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss",timezone="GMT-4")
	    public Date fecha_creacion;
	    public String nombret;
	    public String nombrest;
	    public String detalle_denuncia;
	    public String estado;
	}

}
