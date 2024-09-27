package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_solicitud_certificacion_proceso", procedureName = "mteps_plan.f_lista_solicitud_certificacion_proceso", resultClasses = {
		F_lista_solicitud_certificacion_proceso.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_proceso", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_solicitud_certificacion_proceso {
    @Id
	public Integer id_solicitud;
    
    public Integer id_proceso;
    public String codigo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_solicitud;
    public String descripcion_procesos;
    public String estado_solicitud;
    public Integer id_estado;
    public Integer usr_cre;
    public String host_cre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_cre;
    public Integer usr_mod;
    public String host_mod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_mod;
    public String bd_usr;
    public String bd_host;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-BO", timezone = "America/La_Paz")
    public Date bd_timestamp;
    public Integer id_usuario;
    public String observacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_aprobacion;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public Object solicitud_detalle;
}
