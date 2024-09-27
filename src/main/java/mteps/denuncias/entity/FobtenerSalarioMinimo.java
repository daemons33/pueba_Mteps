package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_salario_minimo", procedureName = "mteps_denuncias.f_obtener_salario_minimo", resultClasses = {
		FobtenerSalarioMinimo.class })
@Entity
public class FobtenerSalarioMinimo {
	
	 @Id
	 public Integer gestion;
	 public Double salario_minimo;

}
