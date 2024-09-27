package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

public class autenticacionAgetic {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public class AutenticacionNit {
	    
	    public String usuario;
	    public String password;

	}
}
