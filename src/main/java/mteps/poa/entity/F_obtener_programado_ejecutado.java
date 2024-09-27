package mteps.poa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_plan.f_obtener_programado_ejecutado", procedureName = "mteps_plan.f_obtener_programado_ejecutado", resultClasses = {
		F_obtener_programado_ejecutado.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_seguimiento", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_programado_ejecutado {
	@Id
	public Integer idSeguimiento;
	public String nombreSeguimiento;
	public Integer gestionSeguimiento;
	public String mesInicio; 
	public String mesFin;
	public Integer idPlan;
	public String unidadFuncional;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<NombreAcp> nombreAcp;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle> detalle;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<ProgEjec> detalle_p_e;
	
	public static class NombreAcp{
		public Integer idProceso;
		public String nombre;
		public String descripcion;
	}
		

	public static class Detalle{
	    public int idProceso;
	    public String nombre;
	    public String medidaCorrectiva;
	    public String resultados;
	    public String dificultades;
	   // public List<ProgEjec> progEjec;
	}
	
	public static class ProgEjec{
	    public Integer intervalValor;
	    public Double valorProgramado;
	    public Double valorEjecutado;
	}
}
