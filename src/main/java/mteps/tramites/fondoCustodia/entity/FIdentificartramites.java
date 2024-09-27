package mteps.tramites.fondoCustodia.entity;

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

@NamedStoredProcedureQuery(name="mteps_tramites.f_identificartramites",
procedureName = "mteps_tramites.f_identificartramites",
resultClasses = {FIdentificartramites.class},
parameters = {
		@StoredProcedureParameter(name = "i_cod_tramite", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "i_tipo", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "i_transaccion", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FIdentificartramites {
	@Id
	public Integer id;
	public String codigo;
	public String estado;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fecha;
	public Integer idClasifTramite;
	public String tramite;
	public Double monto;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object depositos;
		
}
