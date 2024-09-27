package mteps.correos;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.ValidationException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import mteps.config.Resultado;
import mteps.correos.entity.DatosCorreoMteps;
import mteps.correos.entity.DatosCorreov2;
import mteps.correos.entity.DatosProtocolos;
import mteps.correos.entity.EnvioCorreoConfiguracion;

@RestController
@RequestMapping("/correo")
public class MtepsCorreos {

	
	public EnvioCorreoConfiguracion envioCorreoConfiguracion;
	
	public MtepsCorreos(EnvioCorreoConfiguracion envioCorreoConfiguracion) {
		this.envioCorreoConfiguracion = envioCorreoConfiguracion;
	}
	
	@RequestMapping(path = "/correosprotocolo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Resultado envioPROT(@RequestBody List<DatosProtocolos> pObjEnvioCorreo, BindingResult bindingResult)
			throws ValidationException, MessagingException {
		String certificatesTrustStorePath = "<JAVA HOME>/jre/lib/security/cacerts";
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		Resultado vObjResultado = new Resultado();
		if (pObjEnvioCorreo != null) {
			if (bindingResult.hasErrors()) {
				throw new ValidationException("La retroalimentación no es válida");
			}
		}

		final String username = this.envioCorreoConfiguracion.getCount();
		final String userCorreo = this.envioCorreoConfiguracion.getUsername();
		final String password = this.envioCorreoConfiguracion.getPassword();
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.store.protocol", "pop3");
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Authenticator authenticator = new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getDefaultInstance(prop, authenticator);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userCorreo));
		message.setSubject("Ministerio de Trabajo, Empleo y previsión Social");

		message.addRecipients(Message.RecipientType.CC,
				InternetAddress.parse("seguridadocupacional@mintrabajo.gob.bo"));
		int i = 0;
		int j = 0;

		for (i = pObjEnvioCorreo.size() - 1; i > -1; i--) {

			DatosProtocolos dato = pObjEnvioCorreo.get(i);

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dato.email));
			if ("".equals(dato.sucursal)) {
				dato.sucursal = "";
			}
			String mensaje = "<p style=\"text-align: justify;\">Se&ntilde;ores:&nbsp;<br />" + dato.razonSocial
					+ " Sucursal: " + dato.sucursal
					+ "<br /><br />El presente correo electr&oacute;nico es emitido como&nbsp;<strong>CONSTANCIA DEL CUMPLIMIENTO DE LOS REQUISITOS PARA LA PRESENTACI&Oacute;N Y REGISTRO DEL PROTOCOLO DE BIOSEGURIDAD RECIBIDO</strong>, siendo la Implementaci&oacute;n del mismo entera Responsabilidad de su Empresa/ Establecimiento/ Instituci&oacute;n/ Entidad.</p>\r\n"
					+ "<p style=\"text-align: justify;\">La Resoluci&oacute;n Ministerial 186/21 de 04 de marzo de 2021, en el par&aacute;grafo V del Articulo Cuarto indica lo siguiente: &ldquo;El Registro del protocolo referido por las empresas, establecimientos laborales e instituciones del sector p&uacute;blico y privado, tendr&aacute; la calidad de Declaraci&oacute;n Jurada y estar&aacute;n sujetas a verificaci&oacute;n por el Ministerio de Trabajo, Empleo y Previsi&oacute;n Social&rdquo;.<br /><br />El Ministerio de Trabajo, Empleo y Previsi&oacute;n Social, a trav&eacute;s de las Jefaturas Departamentales y Regionales de Trabajo, realizar&aacute; las Verificaciones permanentes para garantizar el cumplimiento de lo dispuesto en el Decreto Supremo N&deg; 4451, Resoluci&oacute;n Biministerial 001/21 y dem&aacute;s disposiciones Reglamentarias.&nbsp;<br /><br />Sin otro particular, nos despedimos muy cordialmente.<br /><br />Ministerio de Trabajo, Empleo y Previsi&oacute;n Social<br />Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional<br />Calle Yanacocha esq. Mercado<br /><br /><span style=\"color: #ff0000;\">Tenga en cuenta que este mensaje de correo electr&oacute;nico se envi&oacute; desde una direcci&oacute;n exclusivamente de avisos y notificaciones, por lo que no se responder&aacute; correos electr&oacute;nicos entrantes. Por favor no responda a este mensaje</span>.</p>\r\n"
					+ "<p style=\"text-align: center;\"><strong>Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional</strong>&nbsp;</p>\r\n"
					+ "<p style=\"text-align: center;\"><strong>Telf: 2408606 - Int. 2164</strong></p>\r\n"
					+ "<p style=\"text-align: center;\"><strong>La Paz - Bolivia</strong></p>";
			message.setContent(mensaje, "text/html;charset=UTF-8");
			Transport.send(message);
			j++;
		}

		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = i;
		vObjResultado.notificacion = j + " correos envidados";
		vObjResultado.datoAdicional = null;

		return vObjResultado;
	}
	//////////////////////////////////////
	/////////////////////////////
	
	@RequestMapping(path = "/correosprotocolo2", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Resultado envioPROT2(@RequestBody List<DatosProtocolos> pObjEnvioCorreo, BindingResult bindingResult)
			throws ValidationException, MessagingException {
		String certificatesTrustStorePath = "<JAVA HOME>/jre/lib/security/cacerts";
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		Resultado vObjResultado = new Resultado();
		if (pObjEnvioCorreo != null) {
			if (bindingResult.hasErrors()) {
				throw new ValidationException("La retroalimentación no es válida");
			}
		}

		final String username = this.envioCorreoConfiguracion.getCount();
		final String userCorreo = this.envioCorreoConfiguracion.getUsername();
		final String password = this.envioCorreoConfiguracion.getPassword();
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.store.protocol", "pop3");
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Authenticator authenticator = new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getDefaultInstance(prop, authenticator);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userCorreo));
		message.setSubject("Ministerio de Trabajo, Empleo y previsión Social");

		message.addRecipients(Message.RecipientType.CC,
				InternetAddress.parse("seguridadocupacional@mintrabajo.gob.bo"));
		int i = 0;
		int j = 0;

		for (i = pObjEnvioCorreo.size() - 1; i > -1; i--) {

			DatosProtocolos dato = pObjEnvioCorreo.get(i);

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dato.email));
			if ("".equals(dato.sucursal)) {
				dato.sucursal = "";
			}
			String mensaje = "<p>Se&ntilde;ores:</p>\r\n"
					+ "<p>"+dato.razonSocial + " Sucursal: "+ dato.sucursal 
					+ "<p style=\"text-align: justify;\">El presente correo electr&oacute;nico es emitido como CONSTANCIA DEL CUMPLIMIENTO DE LOS REQUISITOS PARA LA PRESENTACI&Oacute;N Y REGISTRO DEL PROTOCOLO DE BIOSEGURIDAD RECIBIDO, siendo la Implementaci&oacute;n del mismo entera Responsabilidad de su Empresa/ Establecimiento/ Instituci&oacute;n/ Entidad.</p>\r\n"
					+ "<p style=\"text-align: justify;\">El Anexo de la Resoluci&oacute;n Ministerial 320/21 de 31 de marzo de 2021, en el Par&aacute;grafo Segundo del Art&iacute;culo Tercero indica lo siguiente: &ldquo;Las empresas, establecimientos laborales e instituciones p&uacute;blicas y privadas nuevas que o se constituyan posterior al 31 de mayo de 2021, que cuenten con m&iacute;nimamente un (1) trabajador dependiente, podr&aacute;n presentar sus Protocolos de Bioseguridad en el plazo de quince (15) d&iacute;as h&aacute;biles a partir de la fecha de creaci&oacute;n de su Registro Obligatorio de Empleadores.&rdquo;.</p>\r\n"
					+ "<p style=\"text-align: justify;\">El Ministerio de Trabajo, Empleo y Previsi&oacute;n Social, a trav&eacute;s de las Jefaturas Departamentales y Regionales de Trabajo, realizar&aacute; las Verificaciones permanentes para garantizar el cumplimiento de lo dispuesto en el Decreto Supremo N&deg; 4451, Resoluci&oacute;n Biministerial 001/21 y dem&aacute;s disposiciones Reglamentarias.</p>\r\n"
					+ "<p>Sin otro particular, nos despedimos muy cordialmente.</p>\r\n"
					+ "<p>Ministerio de Trabajo, Empleo y Previsi&oacute;n Social<br />Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional<br />Calle Yanacocha esq. Mercado</p>\r\n"
					+ "<p style=\"text-align: center;\"><span style=\"color: #ff0000;\">Tenga en cuenta que este mensaje de correo electr&oacute;nico se envi&oacute; desde una direcci&oacute;n exclusivamente de avisos y notificaciones, por lo que no se responder&aacute; correos electr&oacute;nicos entrantes. Por favor no responda a este mensaje.</span></p>\r\n"
					+ "<p style=\"text-align: center;\"><strong>Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional<br />Telf: 2408606 - Int. 2164 <br />La Paz - Bolivia</strong></p>\r\n"
					+ "<p>-- Este mensaje se ha enviado desde un formulario en Ministerio de Trabajo, Empleo y Previsi&oacute;n social (www.mintrabajo.gob.bo).</p>";
			message.setContent(mensaje, "text/html;charset=UTF-8");
			Transport.send(message);
			j++;
		}

		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = i;
		vObjResultado.notificacion = j + " correos envidados";
		vObjResultado.datoAdicional = null;

		return vObjResultado;
	}
	
	
	//////////////////////////////////////
	/////////////////////////////
	
	@RequestMapping(path = "/protocoloregularizacion", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Resultado envioPROT3(@RequestBody List<DatosProtocolos> pObjEnvioCorreo, BindingResult bindingResult)
			throws ValidationException, MessagingException {
		String certificatesTrustStorePath = "<JAVA HOME>/jre/lib/security/cacerts";
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		Resultado vObjResultado = new Resultado();
		if (pObjEnvioCorreo != null) {
			if (bindingResult.hasErrors()) {
				throw new ValidationException("La retroalimentación no es válida");
			}
		}

		final String username = this.envioCorreoConfiguracion.getCount();
		final String userCorreo = this.envioCorreoConfiguracion.getUsername();
		final String password = this.envioCorreoConfiguracion.getPassword();
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.store.protocol", "pop3");
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Authenticator authenticator = new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getDefaultInstance(prop, authenticator);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userCorreo));
		message.setSubject("Ministerio de Trabajo, Empleo y previsión Social");

		message.addRecipients(Message.RecipientType.CC,
				InternetAddress.parse("seguridadocupacional@mintrabajo.gob.bo"));
		int i = 0;
		int j = 0;
		
		String vars = "";
		try {
		for (i = pObjEnvioCorreo.size() - 1; i > -1; i--) {

			DatosProtocolos dato = pObjEnvioCorreo.get(i);
			vars = dato.email;
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dato.email));
			if ("".equals(dato.sucursal)) {
				dato.sucursal = "";
			}
			String mensaje = "<p>Se&ntilde;ores:</p>\r\n"
					+ "<p>"+dato.razonSocial + " Sucursal: "+ dato.sucursal 
					+ "<p style=\"text-align: justify;\">El presente correo electr&oacute;nico es emitido como <strong>CONSTANCIA DEL CUMPLIMIENTO DEL PROCESO DE REGULARIZACI&Oacute;N DE LOS REQUISITOS PARA LA PRESENTACI&Oacute;N Y REGISTRO DEL PROTOCOLO DE BIOSEGURIDAD ESPEC&Iacute;FICO RECIBIDO</strong>, siendo la Implementaci&oacute;n del mismo entera Responsabilidad de su Empresa/ Establecimiento/ Instituci&oacute;n/ Entidad.</p>\r\n"
					+ "<p style=\"text-align: justify;\">El Anexo de la Resoluci&oacute;n Ministerial 320/21 de 31 de marzo de 2021, en el Par&aacute;grafo Segundo del Art&iacute;culo Tercero indica lo siguiente: &ldquo;Las empresas, establecimientos laborales e instituciones p&uacute;blicas y privadas nuevas que o se constituyan posterior al 31 de mayo de 2021, que cuenten con m&iacute;nimamente un (1) trabajador dependiente, podr&aacute;n presentar sus Protocolos de Bioseguridad en el plazo de quince (15) d&iacute;as h&aacute;biles a partir de la fecha de creaci&oacute;n de su Registro Obligatorio de Empleadores.&rdquo;.</p>\r\n"
					+ "<p style=\"text-align: justify;\">El Ministerio de Trabajo, Empleo y Previsi&oacute;n Social, a trav&eacute;s de las Jefaturas Departamentales y Regionales de Trabajo, realizar&aacute; las Verificaciones permanentes para garantizar el cumplimiento de lo dispuesto en el Decreto Supremo N&deg; 4451, Resoluci&oacute;n Biministerial 001/21 y dem&aacute;s disposiciones Reglamentarias.</p>\r\n"
					+ "<p style=\"text-align: justify;\">Sin otro particular, nos despedimos muy cordialmente.</p>\r\n"
					+ "<p style=\"text-align: center;\"><span style=\"color: #ff0000;\">Tenga en cuenta que este mensaje de correo electr&oacute;nico se envi&oacute; desde una direcci&oacute;n exclusivamente de avisos y notificaciones, por lo que no se responder&aacute; correos electr&oacute;nicos entrantes. <span style=\"text-decoration: underline;\">Por favor no responda a este mensaje</span>.</span></p>\r\n"
					+ "<p style=\"text-align: center;\"><strong>Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional<br />Telf: 2408606 - Int. 2164<br />La Paz - Bolivia</strong></p>\r\n"
					+ "<p>-- Este mensaje se ha enviado desde un formulario en Ministerio de Trabajo, Empleo y Previsi&oacute;n social (https://www.mintrabajo.gob.bo).</p>";
			message.setContent(mensaje, "text/html;charset=UTF-8");
			Transport.send(message);
			j++;
		}

		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = i;
		vObjResultado.notificacion = j + " correos envidados";
		vObjResultado.datoAdicional = null;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage()+ ": "+vars;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = j + " correos enviados";
		}
		return vObjResultado;
	}
	
	//////////////////////////////
	/// CORREOS PROTOCOLOS
	@RequestMapping(path = "/correosmteps", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Resultado envioMTEPS(@RequestBody List<DatosCorreoMteps> pObjEnvioCorreo, BindingResult bindingResult)
			throws ValidationException, MessagingException {
		String certificatesTrustStorePath = "<JAVA HOME>/jre/lib/security/cacerts";
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		Resultado vObjResultado = new Resultado();
		if (pObjEnvioCorreo != null) {
			if (bindingResult.hasErrors()) {
				throw new ValidationException("Feedback is not valid");
			}
		}


		final String username = "contacto";
		final String userCorreo = "contacto@mintrabajo.gob.bo";
		final String password = "Mt3Ps.21$";
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.store.protocol", "pop3");
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Authenticator authenticator = new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getDefaultInstance(prop, authenticator);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userCorreo));
		message.setSubject("Ministerio de Trabajo, Empleo y previsión Social");
		
		
		int i = 0;
		int j = 0;
		String vars = "";
		try {
		for (i = pObjEnvioCorreo.size() - 1; i > -1; i--) {

			DatosCorreoMteps dato = pObjEnvioCorreo.get(i);
			vars = dato.email;
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dato.email));
			
			String mensaje = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\r\n"
					+ "<meta name=\"x-apple-disable-message-reformatting\">\r\n"
					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
					+ "<meta content=\"telephone=no\" name=\"format-detection\">\r\n"
					+ "<title>Nuevo mensaje</title>\r\n"
					+ "<link href=\"https://fonts.googleapis.com/css?family=Lato:400,400i,700,700i\" rel=\"stylesheet\">\r\n"
					+ "<style type=\"text/css\">\r\n"
					+ "#outlook a {\r\n"
					+ "padding:0;\r\n"
					+ "}\r\n"
					+ ".ExternalClass {\r\n"
					+ "width:100%;\r\n"
					+ "}\r\n"
					+ ".ExternalClass,\r\n"
					+ ".ExternalClass p,\r\n"
					+ ".ExternalClass span,\r\n"
					+ ".ExternalClass font,\r\n"
					+ ".ExternalClass td,\r\n"
					+ ".ExternalClass div {\r\n"
					+ "line-height:100%;\r\n"
					+ "}\r\n"
					+ ".es-button {\r\n"
					+ "mso-style-priority:100!important;\r\n"
					+ "text-decoration:none!important;\r\n"
					+ "}\r\n"
					+ "a[x-apple-data-detectors] {\r\n"
					+ "color:inherit!important;\r\n"
					+ "text-decoration:none!important;\r\n"
					+ "font-size:inherit!important;\r\n"
					+ "font-family:inherit!important;\r\n"
					+ "font-weight:inherit!important;\r\n"
					+ "line-height:inherit!important;\r\n"
					+ "}\r\n"
					+ ".es-desk-hidden {\r\n"
					+ "display:none;\r\n"
					+ "float:left;\r\n"
					+ "overflow:hidden;\r\n"
					+ "width:0;\r\n"
					+ "max-height:0;\r\n"
					+ "line-height:0;\r\n"
					+ "mso-hide:all;\r\n"
					+ "}\r\n"
					+ "[data-ogsb] .es-button {\r\n"
					+ "border-width:0!important;\r\n"
					+ "padding:15px 25px 15px 25px!important;\r\n"
					+ "}\r\n"
					+ "[data-ogsb] .es-button.es-button-1 {\r\n"
					+ "padding:10px 25px!important;\r\n"
					+ "}\r\n"
					+ "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120%!important } h1 { font-size:30px!important; text-align:center } h2 { font-size:26px!important; text-align:center } h3 { font-size:20px!important; text-align:center } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-width:15px 25px 15px 25px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; max-height:inherit!important } .h-auto { height:auto!important } }\r\n"
					+ "</style>\r\n"
					+ "</head>\r\n"
					+ "<body style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;padding:0;Margin:0\">\r\n"
					+ "<div class=\"es-wrapper-color\" style=\"background-color:#F4F4F4\">\r\n"
					+ "<![endif]-->\r\n"
					+ "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\">\r\n"
					+ "<tr class=\"gmail-fix\" height=\"0\" style=\"border-collapse:collapse\">\r\n"
					+ "<td style=\"padding:0;Margin:0\">\r\n"
					+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:600px\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"padding:0;Margin:0;line-height:1px;min-width:600px\" height=\"0\"><img src=\"https://tedzlp.stripocdn.email/content/guids/CABINET_837dc1d79e3a5eca5eb1609bfe9fd374/images/41521605538834349.png\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;max-height:0px;min-height:0px;min-width:600px;width:600px\" alt width=\"600\" height=\"1\"></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td valign=\"top\" style=\"padding:0;Margin:0\">\r\n"
					+ "\r\n"
					+ "<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td style=\"padding:0;Margin:0;background-color:#cc2129\" bgcolor=\"#CC2129\" align=\"center\">\r\n"
					+ "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
					+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
					+ "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#ffffff;border-radius:4px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td align=\"center\" style=\"Margin:0;padding-bottom:5px;padding-left:30px;padding-right:30px;padding-top:35px\"><h1 style=\"Margin:0;line-height:58px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:48px;font-style:normal;font-weight:normal;color:#111111\">COMUNICADO</h1></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td bgcolor=\"#ffffff\" align=\"center\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:20px;padding-right:20px;font-size:0\">\r\n"
					+ "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td style=\"padding:0;Margin:0;border-bottom:1px solid #ffffff;background:#FFFFFF none repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table>\r\n"
					+ "<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td align=\"center\" style=\"padding:0;Margin:0\">\r\n"
					+ "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
					+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
					+ "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:4px;background-color:#ffffff\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\">\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"left\" style=\"padding:0;Margin:0;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Estimado(a)<br><br></p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px;text-align:justify\">La Dirección General de Trabajo, Higiene y Seguridad Ocupacional informa que a partir de la fecha estara habilitado el grupo&nbsp;en la red social Telegram para todas las Jefaturas Departamentales y Regionales del pais, donde se atenderan y trataran asuntos de indole laboral, para tal proposito a continuación se envia el enlace al grupo:<br><br></p></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"center\" style=\"padding:0;Margin:0;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\"><a href=\"https://t.me/+QWXZsAOcgGkxZmJh\">https://t.me/+QWXZsAOcgGkxZmJh</a></p></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"center\" style=\"padding:0;Margin:0;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">ó</p></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td align=\"center\" style=\"padding:0;Margin:0\"><!--[if mso]><a href=\"https://t.me/+QWXZsAOcgGkxZmJh\" target=\"_blank\" hidden>\r\n"
					+ "<v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" esdevVmlButton href=\"https://t.me/+QWXZsAOcgGkxZmJh\"\r\n"
					+ "style=\"height:44px; v-text-anchor:middle; width:294px\" arcsize=\"5%\" stroke=\"f\" fillcolor=\"#cc2129\">\r\n"
					+ "<w:anchorlock></w:anchorlock>\r\n"
					+ "<center style='color:#ffffff; font-family:helvetica, \"helvetica neue\", arial, verdana, sans-serif; font-size:18px; font-weight:400; line-height:18px; mso-text-raise:1px'>Ingresar a grupo: DGTHSO</center>\r\n"
					+ "</v:roundrect></a>\r\n"
					+ "<![endif]--><!--[if !mso]><!-- --><span class=\"msohide es-button-border\" style=\"border-style:solid;border-color:#FFA73B;background:#cc2129;border-width:0px;display:inline-block;border-radius:2px;width:auto;mso-hide:all\"><a href=\"https://t.me/+QWXZsAOcgGkxZmJh\" class=\"es-button es-button-1 msohide\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:20px;border-style:solid;border-color:#cc2129;border-width:10px 25px;display:inline-block;background:#cc2129;border-radius:2px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-weight:normal;font-style:normal;line-height:24px;width:auto;text-align:center;mso-hide:all\">Ingresar a grupo: DGTHSO</a></span><!--<![endif]--></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td class=\"es-m-txt-l\" align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:24px;color:#666666;font-size:16px\">Enlaces de aplicación:<br><br><strong>Telegram para PC - Windows 7, 8, 10, 11</strong>:&nbsp; <a href=\"https://desktop.telegram.org/\" target=\"_blank\">https://desktop.telegram.org/</a><br><strong>Tutorial de instalacion</strong>:&nbsp; <a href=\"https://www.mintrabajo.gob.bo/wp-content/uploads/2022/10/Telegram-Windows.mp4\" target=\"_blank\">https://www.mintrabajo.gob.bo/wp-content/uploads/2022/10/Telegram-Windows.mp4</a><br><strong>Telegram para Android: </strong><em></em><a href=\"https://play.google.com/store/apps/details?id=org.telegram.messenger&amp;hl=es_BO&amp;gl=US\" target=\"_blank\">https://play.google.com/store/apps/details?id=org.telegram.messenger&amp;hl=es_BO&amp;gl=US</a></p></td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"border-collapse:collapse\">\r\n"
					+ "<td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"left\" style=\"padding:0;Margin:0;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\"><br>En caso de tener algún inconveniente con la instalación de las herramientas detalladas comunicarse con la&nbsp;UTIC, al interno:&nbsp;2040.</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\"><br>Saludos Cordiales.</p></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table></td>\r\n"
					+ "</tr>\r\n"
					+ "</table>\r\n"
					+ "</div>\r\n"
					+ "</body>\r\n"
					+ "</html>";
			message.setContent(mensaje, "text/html;charset=UTF-8");
			Transport.send(message);
			j++;
			
		}

		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = i;
		vObjResultado.notificacion = j + " correos envidados";
		vObjResultado.datoAdicional = null;
		
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage()+ ": "+vars;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = j + " correos enviados";
		}
		return vObjResultado;
	}
	
	
	public Resultado enviarCorreo(String to, String asunto, String mensajeHtml) {
        Resultado resultado = new Resultado();
        
        final String username = this.envioCorreoConfiguracion.getCount();
		final String userCorreo = this.envioCorreoConfiguracion.getUsername();
		final String password = this.envioCorreoConfiguracion.getPassword();
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userCorreo));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(asunto);
            
            // Cargar la plantilla HTML del mensaje
            message.setContent(mensajeHtml, "text/html;charset=UTF-8");

            // Enviar el mensaje
            Transport.send(message);
            
            resultado.correcto=true;
            resultado.codigoResultado =200;
            resultado.notificacion = "El correo se ha enviado correctamente";
            resultado.datoAdicional = null;

        } catch (MessagingException e) {
            resultado.correcto=false;
            resultado.codigoResultado =500;
            resultado.notificacion = "Ocurrió un error al enviar el correo: " + e.getMessage();
            resultado.datoAdicional =null;
        }

        return resultado;
    }
	
	public Resultado enviarCorreov2(DatosCorreov2 datosObj) {
        Resultado resultado = new Resultado();
        
        final String username = datosObj.usuario;
		final String userCorreo = datosObj.cuentaUsuario;
		final String password = datosObj.clave;
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
        	
        	session.getTransport().connect(username, password);
        	
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userCorreo));
            if(datosObj.enviarA!=null) {
            for( String elementoIterable : datosObj.enviarA) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(elementoIterable));
            }}
            
            if(datosObj.cc!=null) {
            for( String elementoIterable : datosObj.cc) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(elementoIterable));
            }
            }
            
                        
            message.setSubject(datosObj.asunto);
            
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(datosObj.cuerpoMensaje, "text/html;charset=UTF-8");

            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if(datosObj.adjunto!=null) {
            	int i = 0;
            for (ByteArrayResource elementoIterable : datosObj.adjunto) {
                MimeBodyPart attachmentPart = new MimeBodyPart();

                // Convierte el ByteArrayResource a un DataSource
                DataSource source = new ByteArrayDataSource(elementoIterable.getByteArray(), "application/octet-stream");
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(datosObj.nombreAdjunto[i]); 
                multipart.addBodyPart(attachmentPart);
                i++;
            }
            }

            // Agregar el Multipart al mensaje
            message.setContent(multipart);
            // Enviar el mensaje
            Transport.send(message);
            
            resultado.correcto=true;
            resultado.codigoResultado =200;
            resultado.notificacion = "El correo se ha enviado correctamente";
            resultado.datoAdicional = null;

        } catch (AuthenticationFailedException e) {
            // Error de autenticación
            resultado.correcto = false;
            resultado.codigoResultado = 401; // Código 401 para error de autenticación
            resultado.notificacion = "Error de autenticación: Usuario o contraseña incorrectos";
            resultado.datoAdicional = null;
        }catch (MessagingException e) {
            resultado.correcto=false;
            resultado.codigoResultado =500;
            resultado.notificacion = "Ocurrió un error al enviar el correo: " + e.getMessage();
            resultado.datoAdicional =null;
        }

        return resultado;
    }
	
	@Async
	public Resultado enviarCorreov2Async(DatosCorreov2 datosObj) {
       
        return enviarCorreov2(datosObj);
    }
	
}
