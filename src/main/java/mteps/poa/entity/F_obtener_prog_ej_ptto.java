package mteps.poa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;


@NamedStoredProcedureQuery(name = "mteps_plan.f_obtener_prog_ej_ptto", procedureName = "mteps_plan.f_obtener_prog_ej_ptto", resultClasses = {
		F_obtener_prog_ej_ptto.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_prog_ej_ptto {
	@Id
	public Integer id;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle>  detalle;
	
	public static class Detalle{
	    public int mes_programa;
	    public double programado;
	    public double ejecutado;
	}

	

}
