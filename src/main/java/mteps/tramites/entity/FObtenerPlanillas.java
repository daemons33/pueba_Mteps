package mteps.tramites.entity;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="parametro.f_obtener_planillas",
procedureName = "parametro.f_obtener_planillas",
resultClasses = {FObtenerPlanillas.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_clasificador", type = Integer.class, mode = ParameterMode.IN)
})
@Entity
public class FObtenerPlanillas {
	@Id
	public Integer id_clasificador; 
	public String dominio; 
	public String nombre; 
	public Integer id_clasificador_raiz;
	public String variables;
	
/*	public int[] getVariablesFormateada() {
		 String[] subcadenas = this.variables.split(",");
		 int[] array = new int[subcadenas.length];
		 for (int i = 0; i < subcadenas.length; i++) {
	            array[i] = Integer.parseInt(subcadenas[i]);
	        }
		return array;
	  }*/
}
