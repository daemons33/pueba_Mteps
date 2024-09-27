package mteps.planpago;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Collections;

import java.util.LinkedHashMap;
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
import javax.xml.bind.ValidationException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mteps.planpago.entity.postDeposito;
import mteps.config.Resultado;
import mteps.correos.entity.EnvioCorreoConfiguracion;
import mteps.planpago.entity.habilitarPlanillas;
import mteps.planpago.entity.infoDoc;
import mteps.planpago.entity.postAgeticHabiliar;

@SpringBootApplication
@ComponentScan({ "com.init.planpago" })
@EntityScan("com.init.planpago.entity")
@EnableJpaRepositories("com.init.planpago")

public class PlanPagoCore {

	// Token Agetic
	@Value("${agetic.tokenAgetic}")
	private String tokenAgetic;

	// Value Localizador Agetic
	@Value("${depositos.urlAgetic}")
	private String urlAgetic_depositos;

	// Value Localizador Agetic
	@Value("${empresas.urlAgetic}")
	private String urlAgetic_empresas;

	// Value localizador Agetic
	@Value("${planillaanterior.urlAgetic}")
	private String urlAgetic_planillaanterior;

	// Value localizador Agetic
	@Value("${periodo.urlAgetic}")
	private String urlAgetic_periodoRezagado;

	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();

	// private static final String DB_DRIVER_CLASS =
	// "oracle.jdbc.driver.OracleDriver";
	//DESARROLLO
	/*
	 private static final String DB_URL = "jdbc:postgresql://192.168.157.104:5432/mteps";
	   private static final String DB_USERNAME = "mteps";
	 private static final String DB_PASSWORD = "Mt3Ps.21$";
	 */
	//PRODUCCION
	
	private static final String DB_URL = "jdbc:postgresql://186.121.201.69:5432/mteps";
	private static final String DB_USERNAME = "mteps";
	private static final String DB_PASSWORD = "M1nTr4202103$";
	
	/////////////////// Obtener servicio agetic DEPOSITO
	public Resultado obtenerDatosDepositoAgetic(postDeposito pVariable) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);

		String url = urlAgetic_depositos + pVariable.nroDeposito + "/" + pVariable.fechaDeposito;

		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			JsonNode consultaDepositosAgetic = datosResultado.get("datos");

			String estado = consultaDepositosAgetic.get("estado").asText();

			if (estado.equals("DISPONIBLE")) {
				pVariable.monto = consultaDepositosAgetic.get("monto").asDouble();
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(pVariable);

				Connection connection = null;

				connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
				CallableStatement procedure = connection.prepareCall("call mteps_pagos.p_gestion_depositos(?,?,?,?,?)");
				procedure.setString(1, json);
				procedure.registerOutParameter(2, Types.BOOLEAN);
				procedure.registerOutParameter(3, Types.VARCHAR);
				procedure.registerOutParameter(4, Types.INTEGER);
				procedure.registerOutParameter(5, Types.INTEGER);
				procedure.executeUpdate();

				vObjResultado.correcto = (Boolean) procedure.getObject(2);
				vObjResultado.notificacion = (String) procedure.getObject(3);
				vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
				vObjResultado.datoAdicional = procedure.getObject(5);

			} else if (estado.equals("USADO")) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El Nro de deposito ya fue usado";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			} else if (estado.equals("NO_EXISTE")) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Nro de deposito o fecha incorrecta";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			}
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}

	/////////////////// Obtener servicio agetic EMPRESAS
	/////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////
	public Resultado obtenerDatosEmpresaAgetic2(String pVariable1, String pVariable2) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);

		String url = "";

		if (pVariable2 == "") {
			url = urlAgetic_empresas + pVariable1;
		} else {
			url = urlAgetic_empresas + pVariable1 + "?matriculaComercio=" + pVariable2;
		}
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();

			Object consultaEmpresasAgetic = datosResultado.get("datos");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = consultaEmpresasAgetic;

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	/////////////////// Obtener servicio agetic EMPRESAS
	public Resultado obtenerDatosEmpresaAgeticlista() throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		vObjResultado.correcto = false;
		vObjResultado.notificacion = "No se encontraron registros";
		vObjResultado.codigoResultado = 400;
		vObjResultado.datoAdicional = null;

		return vObjResultado;
	}

	////////////////////////////////////////
	public Resultado obtenerDatosEmpresaAgeticlista1(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);

		String url = urlAgetic_empresas + pVariable1;

		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();

			Object consultaEmpresasAgetic = datosResultado.get("datos");
			try {
				StoredProcedureQuery procedureEmpresa = entityManager
						.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_lista_plan_pagos");

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(consultaEmpresasAgetic);

				procedureEmpresa.setParameter("p_json_pp", json);
				procedureEmpresa.setParameter("var_estado", "SOLICITADO");

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizo exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El registro no existe en la BD";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			}
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	///////////////////////////////////////
	public Resultado obtenerDatosEmpresaAgeticlista2(String pVariable1, String pVariable2)
			throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);

		String url = urlAgetic_empresas + pVariable1 + "?matriculaComercio=" + pVariable2;

		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();

			Object consultaEmpresasAgetic = datosResultado.get("datos");
			try {
				StoredProcedureQuery procedureEmpresa = entityManager
						.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_lista_plan_pagos");

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(consultaEmpresasAgetic);

				procedureEmpresa.setParameter("p_json_pp", json);
				procedureEmpresa.setParameter("var_estado", "SOLICITADO");

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El registro no existe en la BD";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			}
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

///////////////////////////////////////////////
	///////////////////////////////////// OBTENER LISTA PLAN PAGO
	public Resultado listaPlanPago(Object vObjEntradaDatos) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_lista_plan_pagos");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedureEmpresa.setParameter("p_json_pp", json);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "El registro no existe en la BD";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

///////////////////////////////////////////////
///////////////////////////////////// OBTENER LISTA DETALLE PLAN PAGO
	public Resultado listaDetallePlanPago(Integer variable1) throws JsonProcessingException {
// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_lista_detalle_plan_pago");

			procedureEmpresa.setParameter("p_id_plan_pago", variable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "El registro no existe en la BD";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	///////////////////////////////////////////////////////////////////////
/////////////////// Obtener servicio agetic calculo multa con PLANILLA anterior
	public Resultado obtenerPlanillaAnteriorAgetic(String pVariable1, String pVariable2, String pVariable3,
			Number pVariable4, Integer pVariable5) {
// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		String url = "";
		if (pVariable5 <= 0) {
			url = urlAgetic_planillaanterior + pVariable1 + "?matriculaComercio=" + pVariable2 + "&periodo="
					+ pVariable3 + "&gestion=" + pVariable4;
		} else {
			url = urlAgetic_planillaanterior + pVariable1 + "?matriculaComercio=" + pVariable2 + "&periodo="
					+ pVariable3 + "&gestion=" + pVariable4 + "&codigoSucursal=" + pVariable5;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);

		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			JsonNode consultaplanillaaAgetic = datosResultado.get("datos");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = consultaplanillaaAgetic;

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			if (vResponseMensage.equals("No se encontraron declaraciones.")) {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = vResponseMensage;
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = vResponseMensage;
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}

		}

		return vObjResultado;
	}

	/////////////////////////////////////////////////////////////////////////////
	////////////////////////// //////////// METODO CALCULO DE MULTA

	public Resultado obtenerCalculoMulta(String pVariable1, Integer pVariable2, BigDecimal pVariable3,
			Boolean pVariable4) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_pagos.f_calculo_multas");

			procedureEmpresa.setParameter("p_periodo", pVariable1);
			procedureEmpresa.setParameter("p_gestion", pVariable2);
			procedureEmpresa.setParameter("p_monto_total_ganado", pVariable3);
			procedureEmpresa.setParameter("p_caso_lp", pVariable4);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "El calculo se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se realizo el calculo";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	/////////////////////////////////////////////////////////////////////////////
	////////////////////////// //////////// METODO HABILITAR PLANILLA
	private EnvioCorreoConfiguracion envioCorreoConfiguracion;

	public PlanPagoCore(EnvioCorreoConfiguracion envioCorreoConfiguracion) {
		this.envioCorreoConfiguracion = envioCorreoConfiguracion;
	}

	public Resultado habilitarPlanilla(Integer pVariable1) throws JsonProcessingException, ValidationException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + tokenAgetic);
		
		String url = urlAgetic_periodoRezagado;
		try {
		StoredProcedureQuery procedureEmpresa = entityManager
				.createNamedStoredProcedureQuery("mteps_pagos.f_habilitar_planillas");

		procedureEmpresa.setParameter("p_id_plan_pago", pVariable1);

		habilitarPlanillas resultado = (habilitarPlanillas) procedureEmpresa.getSingleResult();

		postAgeticHabiliar envDatos = new postAgeticHabiliar();
		envDatos.gestion = resultado.pPlaGestionPlanilla;
		envDatos.periodo = resultado.pPlaPeriodoPlanilla;
		envDatos.nit = resultado.pPlaNitEmpresa;
		envDatos.matriculaComercio = resultado.pPlaMatricula;
		envDatos.tipoPlanilla = resultado.pPcTipoSigla;
		envDatos.modalidad = resultado.pPcNombre;
		envDatos.fechaHabilitacion = resultado.pFechaModificacion;
		envDatos.codigoSucursal = resultado.pPlaCodigoSucursal;
		
		
		//////////////////////// servicio agetic de habilitacion de planillas
		try {
			HttpHeaders vHeaders = new HttpHeaders();
			vHeaders.setContentType(MediaType.APPLICATION_JSON);
			vHeaders.add("Authorization", "Bearer " + tokenAgetic);

			HttpEntity vRequest = new HttpEntity(envDatos, vHeaders);
			RestTemplate restTemplate = new RestTemplate();

			ObjectNode vDato = restTemplate.postForObject(url, vRequest, ObjectNode.class);

			JsonNode datosResultado = vDato.get("datos");
			String respMnsj = vDato.get("mensaje").asText();
			// String vCorreosDestinatario = resultado.pEmail;
			String vCorreosDestinatario = "contacto@mintrabajo.gob.bo";
			
			final String username = this.envioCorreoConfiguracion.getCount();
			final String userCorreo = this.envioCorreoConfiguracion.getUsername();
			final String password = this.envioCorreoConfiguracion.getPassword();
			
			//String vCorreosCopia = resultado.copia;

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

			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(vCorreosDestinatario));

			//message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(vCorreosCopia[i]));

			message.setSubject("Ministerio de Trabajo, Empleo y previsión Social");
			// message.setText(asuntoHtml);
			String msn = "<p>Se&ntilde;or:<br />Jefe Departamental "+ resultado.pItmCiudad +"</p>\r\n" 
					+ "<p style=\"text-align: justify;\">En el marco de la Resoluci&oacute;n Ministerial 190/21 de 05 de marzo de 2021, en el par&aacute;grafo I del Art&iacute;culo Octavo del Reglamento de implementaci&oacute;n \"Las empresas o establecimientos laborales beneficiados por el plan de pagos que hayan incurrido en mora, ser&aacute;n notificados mediante correo electr&oacute;nico o email consignado al momento de registrarse en el ROE o alternativamente otro se&ntilde;alado en el plan de pagos\".</p>\r\n"
					+ "<p style=\"text-align: justify;\"><br />El presente correo electr&oacute;nico es emitido como constancia del cumplimiento de plan de pago y habilitaci&oacute;n de planilla en la OVT con el siguiente detalle:</p>\r\n"
					+ "<p style=\"text-align: justify;\">Empresa/Establecimiento/Instituci&oacute;n: <strong>"+ resultado.pPlaRazonSocial +"</strong></p>\r\n"
					+ "<p style=\"text-align: justify;\">NIT: <strong>"+ resultado.pPlaNitEmpresa +"</strong></p>\r\n" 
					+ "<p style=\"text-align: justify;\">N&deg; de Matricula: <strong>"+ resultado.pPlaMatricula+"</strong></p>\r\n" 
					+ "<p style=\"text-align: justify;\">Periodo: <strong>"+ resultado.pPlaPeriodoPlanilla +"/"+resultado.pPlaGestionPlanilla+"</strong></p>\r\n" 
					+ "<p style=\"text-align: justify;\">A partir de la fecha la empresa tiene 5 d&iacute;as para realizar la declaraci&oacute;n de la planilla en la OVT.</p>\r\n"
					+ "<p style=\"text-align: justify;\">El Ministerio de Trabajo, Empleo y Previsi&oacute;n Social, a trav&eacute;s de las Jefaturas Departamentales y Regionales de Trabajo, realizar&aacute; la notificaci&oacute;n correspondiente para el cumplimiento de lo dispuesto en la Resoluci&oacute;n y dem&aacute;s disposiciones Reglamentarias.</p>\r\n"
					+ "<p style=\"text-align: justify;\">Sin otro particular, nos despedimos muy cordialmente.</p>\r\n"
					+ "<p>&nbsp;</p>\r\n"
					+ "<p>Ministerio de Trabajo, Empleo y Previsi&oacute;n Social</p>\r\n"
					+ "<p>Direcci&oacute;n General de Trabajo, Higiene y Seguridad Ocupacional Calle Yanacocha esq. Mercado.</p>\r\n"
					+ "<p>Central Piloto: (2) 2408606.</p>\r\n"
					+ "<p><br />--<br />Este mensaje se ha enviado desde el Ministerio de Trabajo, Empleo y Previsi&oacute;n Social (<a href=\"https://www.mintrabajo.gob.bo/\">https://www.mintrabajo.gob.bo</a>).</p>";
			message.setContent(msn, "text/html");

			Transport.send(message);
				vObjResultado.correcto = true;
				vObjResultado.notificacion = respMnsj + " - Se habilito la planilla correctamente, y se envio un correo electrónico de notificiación a: "+vCorreosDestinatario;
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = datosResultado;
			
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();


				vObjResultado.correcto = false;
				vObjResultado.notificacion = vResponseMensage;
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			
		}
		}catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se encontraron resultados";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION PLAN PAGO

	public Resultado gestionPlanPagos(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			CallableStatement procedure = connection.prepareCall("call mteps_pagos.p_gestion_plan_pagos(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);

			procedure.executeUpdate();

			vObjResultado.correcto = true;
			vObjResultado.notificacion = (String) procedure.getObject(3);
			vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
			vObjResultado.datoAdicional = procedure.getObject(5);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION PLAN PAGO

	public Resultado gestionDetallePlanPagos(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			CallableStatement procedure = connection
					.prepareCall("call mteps_pagos.p_gestion_plan_det_pagos(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);

			procedure.executeUpdate();

			vObjResultado.correcto = true;
			vObjResultado.notificacion = (String) procedure.getObject(3);
			vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
			vObjResultado.datoAdicional = procedure.getObject(5);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	//////////////////////////////////// WORKFLOW////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER LISTA TRANSACCIONES

	public Resultado obtenerListaTransacciones(String pVariable1, String pVariable2, String pVariable3, String pVariable4)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("workflow.f_obtener_lista_transacciones");

			procedureEmpresa.setParameter("p_login", pVariable1);
			procedureEmpresa.setParameter("p_tabla", pVariable2);
			procedureEmpresa.setParameter("p_estado", pVariable3);
			procedureEmpresa.setParameter("p_modulo", pVariable4);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	/////////////////////////////////////// WORKFLOW/////////////////////////////////////////////////////////////
	///////////////////////// OBTENER JSON MENU POR USUARIO

	public Resultado obtenerMenuPorUsuario(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("workflow.f_obtener_json_menu_por_usuario");

			procedureEmpresa.setParameter("p_login", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getSingleResult();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////
/////////////////////////////////////////OBTENER PLANILLAS PENDIENTES
	public Resultado obtenerPlanillaPendiente(String pVariable1, String pVariable2) throws JsonProcessingException {
// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenAgetic);
		String url = "";
		if (pVariable2 == "") {
			url = urlAgetic_empresas + pVariable1;
		} else {
			url = urlAgetic_empresas + pVariable1 + "?matriculaComercio=" + pVariable2;
		}
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			Object planillaspendientes = datosResultado.at("/datos/planillasPendientes");
			Object sucursales = datosResultado.at("/datos/sucursales");

			try {

				StoredProcedureQuery procedureEmpresa = entityManager
						.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_planillas_pendientes");

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(planillaspendientes);
				ObjectWriter aw = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json2 = aw.writeValueAsString(sucursales);

				procedureEmpresa.setParameter("p_json_pp", json);
				procedureEmpresa.setParameter("p_json_cd", json2);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = e.getMessage();
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////
/////////////////////////////////////////HABILITAR PLANILLAS
	public Resultado habilitarPlanillas() throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_datos_empresa");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "El registro no existe en la BD";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////
/////////////////////////////////////////REPORTE  SEGUIMIENTO
	public Resultado reporteIncumplimiento(Integer pVariable1) throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_pagos.f_reporte_incumplimiento");
			procedureEmpresa.setParameter("p_id_plan_pago", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "El registro no existe en la BD";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////
///////////////////////////// OBTENER DATOS USUARIO
	public Resultado obtenerDatosUsuario(String pVariable1) throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		if (pVariable1 != "") {
			try {
				StoredProcedureQuery procedureEmpresa = entityManager
						.createNamedStoredProcedureQuery("mteps_pagos.f_obtener_datos_usuario");
				procedureEmpresa.setParameter("p_login", pVariable1);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El registro no existe en la BD";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = e.getMessage();
			}
		} else {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Se requiere login";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}
	
	
	/////////////////// guardar documentos
	/////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////
	public Resultado guardarDocumento(infoDoc pVariable1 ) throws IOException  {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		try {
		byte[] decoded =Base64.getDecoder().decode(pVariable1.file);
		String extension = FilenameUtils.getExtension(pVariable1.nombreExtension);
		FileOutputStream fos = new FileOutputStream("/home/mteps/RepoPlanPago/"+pVariable1.idPlanPago+"."+extension);
		fos.write(decoded);
		fos.flush();
		fos.close();
		
		vObjResultado.codigoResultado = 200;
		vObjResultado.notificacion = "Se guardo Correctamente";
		vObjResultado.correcto = true;
		vObjResultado.datoAdicional = null;
		} catch (Exception e) {
			vObjResultado.codigoResultado = 200;
			vObjResultado.notificacion = "Error: "+ e.getMessage();
			vObjResultado.correcto = true;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}



}
