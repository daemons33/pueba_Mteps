package mteps.correos.entity;

import org.springframework.core.io.ByteArrayResource;

public class DatosCorreov2 {

	public String usuario;
	public String clave;
	public String cuentaUsuario;
	
	public String[] enviarA;
	public String [] cc;
	public String asunto;	
	public ByteArrayResource[] adjunto;
	public String[] nombreAdjunto;
	
	public String cuerpoMensaje;
	
}
