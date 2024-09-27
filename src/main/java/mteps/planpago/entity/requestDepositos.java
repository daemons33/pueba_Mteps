package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class requestDepositos {
	
	public String nroDeposito;
	public String fechaDeposito;
	public Number monto;
	public String estado;
	
}
