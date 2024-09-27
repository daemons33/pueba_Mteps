package mteps.config.security.entity;



public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	private String username;
	private String email;
	private String codDepartamentos;

	public JwtResponse(String accessToken,  String username, String email, String codDepartamentos) {
		this.token = accessToken;
		this.username = username;
		this.email = username+"@mintrabajo.gob.bo";
		this.codDepartamentos = null;
			
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCodDepartamentos() {
		return codDepartamentos;
	}

	public void setCodDepartamentos(String codDepartamentos) {
		this.codDepartamentos = codDepartamentos;
	}

}
