package mteps.denuncias.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_lista_salario_minimo", procedureName = "mteps_denuncias.f_lista_salario_minimo", resultClasses = {
		FlistaSalarioMinimo.class })
@Entity
public class FlistaSalarioMinimo {
	@Id
	public Integer idsalario;
	public Integer gestion;
	public Double monto;
	public String decreto;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechadecreto;

}
