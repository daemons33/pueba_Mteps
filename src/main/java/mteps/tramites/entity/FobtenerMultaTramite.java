package mteps.tramites.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_multa_tramite",
procedureName = "mteps_tramites.f_obtener_multa_tramite",
resultClasses = {FobtenerMultaTramite.class},
parameters = {
		@StoredProcedureParameter(name = "i_codigo", type = String.class, mode = ParameterMode.IN)
})


@Entity
public class FobtenerMultaTramite {
	@Id
	public String nit; 
	public String matricula_comercio; 
	public String periodo;
	public String gestion; 
	public Integer dias_retraso; 
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_fin_declaracion;
	public Double monto_planilla;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_pago_prima;
	public Double multa;
	public Boolean minera;
	public String estado;
	public Integer id_multa; 

}
