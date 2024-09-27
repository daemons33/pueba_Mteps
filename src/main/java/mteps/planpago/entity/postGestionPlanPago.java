package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class postGestionPlanPago {
	public String nit;
	public String matricula;
}
