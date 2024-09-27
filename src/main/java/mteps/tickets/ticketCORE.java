package mteps.tickets;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;
import mteps.planpago.entity.obtenerDatosCorreo;

@CrossOrigin(origins = "*", maxAge = 3600)
@SpringBootApplication
@ComponentScan({ "mteps.tickets" })
@EntityScan("mteps.tickets.entity")
@EnableJpaRepositories("mteps.tickets")

public class ticketCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;

	@PersistenceContext
	private EntityManager entityManager;

///////////////////////////////////////////////
///////////////////////////////////// OBTENER LISTA PLAN PAGO
	public Resultado listaTicket(Object vObjEntradaDatos) throws JsonProcessingException {
// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_lista_tickets");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedureEmpresa.setParameter("p_json_pp", json);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
 
		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION TICKET

	public Resultado gestionTickets(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_tickets.p_gestion_ticket(?,?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);
			procedure.registerOutParameter(6, Types.BOOLEAN);

			procedure.executeUpdate();

			vObjResultado.correcto = (boolean) procedure.getObject(2);
			vObjResultado.notificacion = (String) procedure.getObject(3);
			vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
			vObjResultado.datoAdicional = procedure.getObject(5);
			
			//if((boolean) procedure.getObject(6)) {
			boolean ff = false;	
			if(ff) {	
				StoredProcedureQuery procedure2 = entityManager.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_datos_correo");
				procedure2.setParameter("v_id", procedure.getObject(5));
				
				obtenerDatosCorreo respCorreo = (obtenerDatosCorreo) procedure2.getSingleResult();
				
				String username = "utic.soporte";
				String userCorreo = "utic.soporte@mintrabajo.gob.bo";
				String password = "Mt3Ps-21$";
				Properties prop = new Properties();
				prop.put("mail.smtp.host", "mail.mintrabajo.gob.bo");
				prop.put("mail.smtp.port", "587");
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.store.protocol", "pop3");
				prop.put("mail.transport.protocol", "smtp");
				prop.put("mail.smtp.starttls.enable", "true"); 
				
				
				Authenticator authenticator = new Authenticator() {
					@Override
					protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
						return new javax.mail.PasswordAuthentication(username, password);
					}
				};
				Session session = Session.getDefaultInstance(prop, authenticator);
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(userCorreo));
				message.setSubject("(1) Tienes una nueva solicitud de atención - SAT");

				message.addRecipients(Message.RecipientType.CC,
						InternetAddress.parse("utic.soporte@mintrabajo.gob.bo"));
				
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("rchallco@mintrabajo.gob.bo"));
				String mensaje = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\r\n"
						+ "<head>\r\n"
						+ "<meta charset=\"UTF-8\">\r\n"
						+ "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\r\n"
						+ "<meta name=\"x-apple-disable-message-reformatting\">\r\n"
						+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
						+ "<meta content=\"telephone=no\" name=\"format-detection\">\r\n"
						+ "<title>Nueva plantilla</title>\r\n"
						+ "<!--[if (mso 16)]>\r\n"
						+ "<style type=\"text/css\">\r\n"
						+ "a {text-decoration: none;}\r\n"
						+ "</style>\r\n"
						+ "<![endif]-->\r\n"
						+ "<!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->\r\n"
						+ "<!--[if gte mso 9]>\r\n"
						+ "<xml>\r\n"
						+ "<o:OfficeDocumentSettings>\r\n"
						+ "<o:AllowPNG></o:AllowPNG>\r\n"
						+ "<o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
						+ "</o:OfficeDocumentSettings>\r\n"
						+ "</xml>\r\n"
						+ "<![endif]-->\r\n"
						+ "<!--[if !mso]><!-- -->\r\n"
						+ "<link href=\"https://fonts.googleapis.com/css?family=Lato:400,400i,700,700i\" rel=\"stylesheet\">\r\n"
						+ "<!--<![endif]-->\r\n"
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
						+ "padding:15px 25px!important;\r\n"
						+ "}\r\n"
						+ "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120%!important } h1 { font-size:30px!important; text-align:center } h2 { font-size:26px!important; text-align:center } h3 { font-size:20px!important; text-align:center } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-width:15px 25px 15px 25px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }\r\n"
						+ "</style>\r\n"
						+ "</head>\r\n"
						+ "<body style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\r\n"
						+ "<div class=\"es-wrapper-color\" style=\"background-color:#F4F4F4\">\r\n"
						+ "<!--[if gte mso 9]>\r\n"
						+ "<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\r\n"
						+ "<v:fill type=\"tile\" color=\"#f4f4f4\"></v:fill>\r\n"
						+ "</v:background>\r\n"
						+ "<![endif]-->\r\n"
						+ "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\">\r\n"
						+ "<tr class=\"gmail-fix\" height=\"0\" style=\"border-collapse:collapse\">\r\n"
						+ "<td style=\"padding:0;Margin:0\">\r\n"
						+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:600px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"padding:0;Margin:0;line-height:1px;min-width:600px\" height=\"0\"><img src=\"https://kfebyx.stripocdn.email/content/guids/CABINET_837dc1d79e3a5eca5eb1609bfe9fd374/images/41521605538834349.png\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;max-height:0px;min-height:0px;min-width:600px;width:600px\" alt width=\"600\" height=\"1\"></td>\r\n"
						+ "</tr>\r\n"
						+ "</table></td>\r\n"
						+ "</tr>\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" style=\"padding:0;Margin:0\">\r\n"
						+ "<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td style=\"padding:0;Margin:0;background-color:#cc2129\" bgcolor=\"#CC2129\" align=\"center\">\r\n"
						+ "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
						+ "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#ffffff;border-radius:4px\" width=\"100%\" bgcolor=\"#ffffff\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"center\" style=\"Margin:0;padding-bottom:5px;padding-left:30px;padding-right:30px;padding-top:35px\"><h1 style=\"Margin:0;line-height:58px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:48px;font-style:normal;font-weight:normal;color:#111111\">Sistema de Atención TIC</h1></td>\r\n"
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
						+ "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#ffffff;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
						+ "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#ffffff\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"left\" style=\"Margin:0;padding-bottom:15px;padding-top:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Hola&nbsp;<strong>"+respCorreo.nombre+"</strong><br><br>Tienes una nueva solicitud de atención TIC:&nbsp;</p>\r\n"
						+ "<ul>\r\n"
						+ "<li style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;Margin-bottom:15px;color:#666666;font-size:18px\">Ticket:&nbsp;<strong>"+respCorreo.codigoTicket+"</strong></li>\r\n"
						+ "<li style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;Margin-bottom:15px;color:#666666;font-size:18px\">Sub - Categoría:&nbsp;<strong>"+respCorreo.subCategoria+"</strong></li>\r\n"
						+ "<li style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;Margin-bottom:15px;color:#666666;font-size:18px\">Fecha y Hora:&nbsp;&nbsp;<strong>"+respCorreo.fechaModificacion+"</strong></li>\r\n"
						+ "</ul></td>\r\n"
						+ "</tr>\r\n"
						+ "</table></td>\r\n"
						+ "</tr>\r\n"
						+ "</table></td>\r\n"
						+ "</tr>\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-left:30px;padding-right:30px\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:540px\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:40px;padding-bottom:40px\"><span class=\"es-button-border\" style=\"border-style:solid;border-color:#ec6d64;background:#cc2129;border-width:1px;display:inline-block;border-radius:32px;width:auto\"><a href=\"http://sat.mteps.gob.bo/login\" class=\"es-button es-button-1\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:20px;border-style:solid;border-color:#cc2129;border-width:15px 25px;display:inline-block;background:#cc2129;border-radius:32px;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;font-weight:normal;font-style:normal;line-height:24px;width:auto;text-align:center\">Atender ahora</a></span></td>\r\n"
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
						+ "<table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
						+ "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:4px;background-color:#111111\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#111111\" role=\"presentation\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td class=\"es-m-txt-l\" bgcolor=\"#111111\" align=\"center\" style=\"padding:0;Margin:0;padding-left:30px;padding-right:30px;padding-top:35px\"><h2 style=\"Margin:0;line-height:29px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:24px;font-style:normal;font-weight:normal;color:#ffffff\">Soporte UTIC<br></h2></td>\r\n"
						+ "</tr>\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td class=\"es-m-txt-l\" align=\"center\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666;font-size:18px\">Para cualquier duda o consulta comunicarse a los internos:&nbsp;<span style=\"color:#FFFFFF\">2037, 2038, 2040, 2058, 2059, 2060</span>.</p></td>\r\n"
						+ "</tr>\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"center\" style=\"Margin:0;padding-top:20px;padding-left:30px;padding-right:30px;padding-bottom:40px\"><a target=\"_blank\" href=\"https://www.mintrabajo.gob.bo/\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#EC6D64;font-size:18px\">Ministerio de Trabajo, Empleo y Previsión Social</a></td>\r\n"
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
						+ "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"left\" style=\"padding:0;Margin:0\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\">\r\n"
						+ "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td align=\"center\" style=\"Margin:0;padding-top:10px;padding-bottom:20px;padding-left:20px;padding-right:20px;font-size:0\">\r\n"
						+ "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\r\n"
						+ "<tr style=\"border-collapse:collapse\">\r\n"
						+ "<td style=\"padding:0;Margin:0;border-bottom:1px solid #f4f4f4;background:#FFFFFF none repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td>\r\n"
						+ "</tr>\r\n"
						+ "</table></td>\r\n"
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
			}
			connection.close();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER CLASIFICADOR

	public Resultado obtenerClasificador(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_clasificadores");

			procedureEmpresa.setParameter("v_id_clasificador", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER TICKET

	public Resultado obtenerTicket(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_ticket");

			procedureEmpresa.setParameter("v_id_ticket", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER DEPENDIENTE

	public Resultado obtenerDependiente(String pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_dependiente");

			procedure.setParameter("usuario", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER LOG TICKET

	public Resultado obtenerLogTicket(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery logTicket = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_log_ticket");

			logTicket.setParameter("v_id_ticket", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = logTicket.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// BUSCAR TICKET

	public Resultado buscarTicket(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_buscador_ticket");

			procedureEmpresa.setParameter("v_dato", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
}
