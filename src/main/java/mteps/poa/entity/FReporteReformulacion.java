package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;



@NamedStoredProcedureQuery(name="mteps_plan.f_reporte_reformulacion",
procedureName = "mteps_plan.f_reporte_reformulacion",
resultClasses = {FReporteReformulacion.class},
parameters = {
		@StoredProcedureParameter(name = "i_id_gestion", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class FReporteReformulacion {
	
	@Id
	public Integer  id_plan ;
	public String sigla ;
	public String nombre ;
	public String estado ;
	public String tipo ;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fecha_cre ;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fecha_act;

}
