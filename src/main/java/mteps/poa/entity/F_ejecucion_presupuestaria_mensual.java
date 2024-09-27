package mteps.poa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;


@NamedStoredProcedureQuery(name = "mteps_plan.f_ejecucion_presupuestaria_mensual", procedureName = "mteps_plan.f_ejecucion_presupuestaria_mensual", resultClasses = {
		F_ejecucion_presupuestaria_mensual.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_clasificador", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_gestion", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_ejecucion_presupuestaria_mensual {
	@Id
	public Integer id_clasificador;
	public String sigla;
	public String nombre;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle>  detalle;
	
	public static class Datalle{
	    public int mes_programa;
	    public double programado;
	    public double ejecutado;
	}

	public static class Detalle{
	    public int id_clasificador;
	    public String sigla;
	    public String nombre;
	    public ArrayList<Datalle> detalle_prog;
	}
}
