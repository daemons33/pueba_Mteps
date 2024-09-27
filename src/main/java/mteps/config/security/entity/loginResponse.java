package mteps.config.security.entity;



public class loginResponse {
	private boolean correcto;
	private String notificacion;
	private Integer codigoResultado;
	
	private JwtResponse datoAdicional;

	
	
	
	
	public loginResponse(boolean finalizado, Integer codigo, String notificacion, JwtResponse jwtResponse) {
		super();
		this.correcto = finalizado;
		this.codigoResultado = codigo;
		this.notificacion = notificacion;
		this.setDatoAdicional(jwtResponse);
	}



	public boolean isCorrecto() {
		return correcto;
	}


	public void setCorrecto(boolean correcto) {
		this.correcto = correcto;
	}


	public String getNotificacion() {
		return notificacion;
	}


	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}





	public Integer getCodigoResultado() {
		return codigoResultado;
	}





	public void setCodigoResultado(Integer codigoResultado) {
		this.codigoResultado = codigoResultado;
	}





	public JwtResponse getDatoAdicional() {
		return datoAdicional;
	}





	public void setDatoAdicional(JwtResponse datoAdicional) {
		this.datoAdicional = datoAdicional;
	}
	

	
	
}
