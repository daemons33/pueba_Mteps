package mteps.tramites.fondoCustodia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import mteps.tramites.fondoCustodia.entity.SolicitudPago;
import mteps.tramites.fondoCustodia.repository.ListaFcIngresoInterface;
import mteps.tramites.fondoCustodia.entity.FObternerIngresoId.ObjectMov;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.config.entity.DatosDocumento;
import mteps.ovt.entity.EntidadDepositos;
import mteps.ovt.repository.DepositosInterface;
import mteps.repo_nfs.RepoNfsCORE;
import mteps.sigec.sigecCORE;
import mteps.sigec.entity.DatosDoc;
import mteps.tramites.fondoCustodia.entity.DatosDocFC;
import mteps.tramites.fondoCustodia.entity.FIdentificartramites;
import mteps.tramites.fondoCustodia.entity.FObtenerCtaFc;
import mteps.tramites.fondoCustodia.entity.FValidarPago;
import mteps.tramites.fondoCustodia.entity.ListaFcIngreso;
import mteps.tramites.fondoCustodia.entity.MovimientoCuentaBancaria;
import mteps.tramites.fondoCustodia.entity.MovimientoCuentaBancaria.MovimientoDetalle;
import mteps.tramites.fondoCustodia.entity.PagosCpt;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
public class fondCustodiaCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;


	@Value("${ppe.token}")
	private String tokenPPE;

	@Value("${cpt.token}")
	private String tokenCPT;
	@Value("${cpt.tokencbba}")
	private String tokenCPTCBBA;
	@Value("${cpt.tokenchq}")
	private String tokenCPTCHQ;
	@Value("${cpt.tokenlp}")
	private String tokenCPTLP;
	@Value("${cpt.tokenpt}")
	private String tokenCPTPT;
	@Value("${cpt.tokentj}")
	private String tokenCPTTJ;
	@Value("${cpt.tokensc}")
	private String tokenCPTSC;

	@Value("${ppe.epoint}")
	private String endpointPPE;

	@Value("${ppe.epoint.consulta}")
	private String endpointPpeConsulta;

	@Value("${ruta.archivos}")
	private String rutaprincipal;

	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// GESTION MULTAS
	
	@Autowired
	ListaFcIngresoInterface listaFcIngresoInterface;
	
	@Autowired
	DepositosInterface depositosInterface;	
	
	@Autowired
	ConfigCORE configCore;

	@Autowired
	RepoNfsCORE repoNfsCore;
	
	@Autowired
	sigecCORE sigecCore;

	public Resultado gestionFondoCustodia(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		Connection connection = null;
		CallableStatement query = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		try {
			connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			query = connection.prepareCall("call mteps_tramites.p_gestion_fc_ingresos(?,?,?,?,?)");
			query.setString(1, json);
			query.registerOutParameter(2, Types.BOOLEAN);
			query.registerOutParameter(3, Types.VARCHAR);
			query.registerOutParameter(4, Types.INTEGER);
			query.registerOutParameter(5, Types.INTEGER);

			query.executeUpdate();

			vObjResultado.correcto = (Boolean) query.getObject(2);
			vObjResultado.notificacion = (String) query.getObject(3);
			vObjResultado.codigoResultado = (Integer) query.getObject(4);
			vObjResultado.datoAdicional = query.getObject(5);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		} finally {
			try {
				if (query != null)
					query.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return vObjResultado;
	}

	public Resultado gestionFondoCustodiaCheque(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		Connection connection = null;
		CallableStatement query = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		try {
			connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			query = connection.prepareCall("call mteps_tramites.p_gestion_fc_cheques(?,?,?,?,?)");
			query.setString(1, json);
			query.registerOutParameter(2, Types.BOOLEAN);
			query.registerOutParameter(3, Types.VARCHAR);
			query.registerOutParameter(4, Types.INTEGER);
			query.registerOutParameter(5, Types.INTEGER);

			query.executeUpdate();

			vObjResultado.correcto = (Boolean) query.getObject(2);
			vObjResultado.notificacion = (String) query.getObject(3);
			vObjResultado.codigoResultado = (Integer) query.getObject(4);
			vObjResultado.datoAdicional = query.getObject(5);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		} finally {
			try {
				if (query != null)
					query.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return vObjResultado;
	}

	public Resultado listaTramiteFC(Object vObjEntradaDatos) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = listaFcIngresoInterface.listaFcIngresos(json);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado buscarTramiteFc(String vObjEntradaDatos, String login) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			List<ListaFcIngreso> resultado = listaFcIngresoInterface.busquedaTramiteFc(vObjEntradaDatos,login);
			
			if (!resultado.isEmpty()){
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultado;
			}else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados para: "+ vObjEntradaDatos;
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
	
	public List<ListaFcIngreso> listaTramiteFCReporte(Object vObjEntradaDatos) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.

		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);

			return listaFcIngresoInterface.listaFcReporte(json);
		} catch (Exception e) {
			return null;
		}

	}

	public Resultado obtenerRequisitos(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_requisitos_fc");

			procedureEmpresa.setParameter("v_id_clasificador", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerCtaFc(String pVariable1, Integer pVariable2) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_cta_fc");

			procedureEmpresa.setParameter("i_jefatura", pVariable1);
			procedureEmpresa.setParameter("i_tipo", pVariable2);

			FObtenerCtaFc resultado = (FObtenerCtaFc) procedureEmpresa.getSingleResult();
			vObjResultado.correcto = resultado.correcto;
			vObjResultado.notificacion = resultado.notificacion;
			vObjResultado.codigoResultado = resultado.resultado;
			vObjResultado.datoAdicional = resultado.datoadicional;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado identificarTramite(String pVariable1, String pVariable2, String pVariable3)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_identificartramites");

			procedureEmpresa.setParameter("i_cod_tramite", pVariable1);
			procedureEmpresa.setParameter("i_tipo", pVariable2);
			procedureEmpresa.setParameter("i_transaccion", pVariable3);

			FIdentificartramites resultado = (FIdentificartramites) procedureEmpresa.getSingleResult();

			if (resultado != null) {				

					vObjResultado.correcto = true;
					vObjResultado.notificacion = "Se encontró trámite de " + resultado.tramite;
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = resultado;
					return vObjResultado;
				
			} else {

				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se encontraron resultados para: " + pVariable1;
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}

		} catch (NoResultException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se encontraron resultados para: " + pVariable1;
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerOpFc() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			Connection connection2 = null;
			Class.forName("org.postgresql.Driver");
			connection2 = DriverManager.getConnection(db_url, db_usuario, db_password);

			CallableStatement procedureTramite = connection2
					.prepareCall("select * from mteps_tramites.f_obtener_op_fc()");
			ResultSet resultDatosMteps = procedureTramite.executeQuery();

			if (resultDatosMteps.next()) {

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = resultDatosMteps.getString(1);
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
			procedureTramite.close();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	// CPT
	public Resultado gestionCptPpePpte(SolicitudPago pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();
		String tkn = "";

		switch (pData.datosPago.cuentaBancaria) {
		case "10000006053578":
			tkn = tokenCPTCBBA;
			break;
		case "10000003622029":
			tkn = tokenCPTCHQ;
			break;
		case "10000004669707":
			tkn = tokenCPTLP;
			break;
		case "10000006036433":
			tkn = tokenCPTPT;
			break;
		case "10000003628457":
			tkn = tokenCPTSC;
			break;
		case "10000006030584":
			tkn = tokenCPTTJ;
			break;
		}

		String pCta = pData.datosPago.cuentaBancaria;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenPPE);
		headers.add("x-cpt-authorization", tkn);
		HttpEntity<SolicitudPago> requestEntity = new HttpEntity<>(pData, headers);
		try {
			ResponseEntity<ObjectNode> response = restTemplate.exchange(endpointPPE, HttpMethod.POST, requestEntity,
					ObjectNode.class);

			ObjectNode res = response.getBody();
			if (res.get("finalizado").asBoolean()) {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = res.get("mensaje").asText();
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = res.get("datos");
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = res.get("mensaje").asText();
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}

	public Resultado consultaEstadoCpt(String pCodigoTransaccion) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenPPE);
		headers.add("x-cpt-authorization", tokenCPT);
		HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(headers);
		try {
			ResponseEntity<ObjectNode> response = restTemplate.exchange(endpointPpeConsulta + pCodigoTransaccion,
					HttpMethod.GET, requestEntity, ObjectNode.class);

			ObjectNode res = response.getBody();
			if (res.get("finalizado").asBoolean()) {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = res.get("mensaje").asText();
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = res.get("datos");
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = res.get("mensaje").asText();
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}

	public ResponseEntity<?> gestioncptbusa(PagosCpt pData) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();
		ObjectMapper objectMapper = new ObjectMapper();

		if (pData.finalizado) {
			try {
				if ((pData.fuente).equals("PPE")) {
					String lstPago = pData.detalle.datosMetodoPago;
					JsonNode jsonNode = objectMapper.readTree(lstPago);

					pData.login = "busa";
					pData.transaccion = "PROCESAR";
					pData.estado = "EN_PROCESO";
					pData.codigoOrden = pData.detalle.codigoOrden;
					pData.idCpt = jsonNode.get("idCpt").asInt();
					pData.cpt = jsonNode.get("cpt").asText();
					pData.fechaPago = jsonNode.get("fecha").asText();
					pData.canal = jsonNode.get("canal").asText();
				}

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(pData);

				Connection connection = null;
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);
				CallableStatement procedure = connection.prepareCall("call mteps_tramites.p_gestion_fc_cpt(?,?,?,?,?)");
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

			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se pudo completar la operación.";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = e.getMessage();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vObjResultado);
			}
		} else {
			vObjResultado.correcto = false;
			vObjResultado.notificacion ="No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = pData.mensaje;
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vObjResultado);
		}
		return ResponseEntity.status(HttpStatus.OK).body(vObjResultado);
	}

	public Resultado gestionDocumento(String pData, MultipartFile file) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();
		File tmpFile = File.createTempFile("sgt", ".pdf");
		String tmpFileName = tmpFile.getName();
		tmpFile.deleteOnExit();
		String fileName = StringUtils.cleanPath(tmpFileName);
		String uploadDir = "uploads";
//		String uploadDir = "/home/mteps/sgt";
		Path uploadPath = Paths.get(uploadDir);

		try (InputStream inputStream = file.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName).toAbsolutePath();
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			StoredProcedureQuery procedureRepositorio = entityManager
					.createNamedStoredProcedureQuery("mteps_nfs.p_gestion_repositorio_doc");
			procedureRepositorio.setParameter("p_json", pData);
			procedureRepositorio.setParameter("p_namefileu", tmpFileName);

			vObjResultado.correcto = (Boolean) procedureRepositorio.getOutputParameterValue("correcto");
			vObjResultado.notificacion = (String) procedureRepositorio.getOutputParameterValue("notificacion");
			vObjResultado.codigoResultado = (Integer) procedureRepositorio.getOutputParameterValue("codigoresultado");
			vObjResultado.datoAdicional = procedureRepositorio.getOutputParameterValue("datoadicional");

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo guardar el archivo: " + fileName;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}

	public Resultado validarDeposito(Integer tipo, String nroDeposito, String fechaDeposito, String glosa)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.

		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement queryValidacion = null;
		CallableStatement procedure = null;
		ResultSet resultDatosOvt = null;
		ResultSet resultDatosMteps = null;
		try {

			if (tipo == 1) {

				///// Validacion OVT
							
				List<EntidadDepositos> depositos = depositosInterface.obtenerDeposito(nroDeposito, fechaDeposito);
								
				if (!depositos.isEmpty()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito
							+ "' fue utilizado en declaración de planillas en la OVT por la empresa "
							+ depositos.get(0).razonSocial + " con NIT: " + depositos.get(0).nit;
					vObjResultado.codigoResultado = 202;
					vObjResultado.datoAdicional = null;
					return vObjResultado;
				}

				//// Validacion Tramites
				
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);

				queryValidacion = connection.prepareCall("select * from mteps_tramites.trm_depositos td\r\n"
						+ "								inner join mteps_tramites.trm_pagos tp on tp.id_pago = td.id_pago \r\n"
						+ "								inner join mteps_tramites.trm_tramite tt on tp.id_tramite = tt.id_tramite\r\n"
						+ "								where td.nro_deposito = ? and  td.fecha_deposito=DATE(?) and tt.estado <> 'ANULADO' ");

				queryValidacion.setString(1, nroDeposito);
				queryValidacion.setString(2, fechaDeposito);
				resultDatosMteps = queryValidacion.executeQuery();

				connection.close();
				if (resultDatosMteps.next()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito
							+ "' fue utilizado en trámite: " + resultDatosMteps.getString("codigo_tramite");
					vObjResultado.codigoResultado = 202;
					vObjResultado.datoAdicional = null;

					return vObjResultado;
				}
			}
			/// Validacion Movimientos fondos en custodia
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_validar_pago");

			procedureEmpresa.setParameter("v_tipo", tipo);
			procedureEmpresa.setParameter("v_nro_movimiento", nroDeposito);
			procedureEmpresa.setParameter("v_fecha_movimiento", fechaDeposito);
			procedureEmpresa.setParameter("v_glosa", glosa);

			if (!procedureEmpresa.getResultList().isEmpty()) {

				List<FValidarPago> resultado = procedureEmpresa.getResultList();

				for (FValidarPago elementoIterable : resultado) {

					if (elementoIterable.idFc != null) {
						vObjResultado.correcto = true;
						vObjResultado.notificacion = "El deposito bancario con Nº transacción: "
								+ elementoIterable.nroMovimiento
								+ " fue utilizado en otro trámite de Fondo en Custodia";
						vObjResultado.codigoResultado = 202;
						vObjResultado.datoAdicional = null;
						return vObjResultado;
					}
				}

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "Deposito bancario encontrado!!";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}

			return vObjResultado;

		} catch (NoResultException e) {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "No se encontraron resultados";
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
			return vObjResultado;
		} finally {
			try {
				if (resultDatosMteps != null)
					resultDatosMteps.close();
				if (queryValidacion != null)
					queryValidacion.close();
				if (procedure != null)
					procedure.close();
				if (connection != null)
					connection.close();
				if (resultDatosOvt != null)
					resultDatosOvt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public Resultado movimientoCuentaBancaria( MovimientoCuentaBancaria pData, MultipartFile file) throws JsonProcessingException, SQLException, ClassNotFoundException {
		Resultado vObjResultado = new Resultado();

		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;
		 connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		if ( file.getSize() < 1) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Debe cargar extracto bancario.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		} 
		long timestamp = System.currentTimeMillis();
		String nombre = file.getOriginalFilename();
		String extension = FileNameUtils.getExtension(nombre);
		if ( !extension.equals("xlsx") && !extension.equals("txt")) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Tipos de documentos permitidos: (.txt, .xlsx).";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		}
		Integer contador= 0;
		 for(MovimientoDetalle datos: pData.movimientoDetalle) {
			 contador++;
			 if (pData.tipo ==1 && !datos.descripcion.equals("CSaldo Inicial")) {
				 
				 try {
				 LocalDate.parse(datos.fecha);
				 }catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", la fecha es incorrecta: "+datos.fecha;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}
				 
				 try {
					 Double.parseDouble(datos.monto);
				 }catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", el monto es incorrecto: "+datos.monto;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}

				Integer idCuenta = -1;
				 if(idCuenta==-1) {
					
						query = connection
								.prepareCall("select * from mteps_tramites.trm_cuentas_bancarias c where c.estado ='ACTIVO' and c.numero_cuenta = ?");
						
						query.setString(1, datos.nroCuenta.replace("BUNCC", ""));
						
						resultDatos = query.executeQuery();
						
						if(resultDatos.next()) {
							pData.idCuentaBancaria = resultDatos.getInt(1);
							idCuenta=1;
							query= null;
							resultDatos=null;
						}else {
							vObjResultado.correcto = false;
							vObjResultado.notificacion = "Error en la fila "+contador+". La cuenta bancaria "+ datos.nroCuenta.replace("BUNCC", "")+ ", no se encuentra registrada ó activa en el sistema.";
							vObjResultado.codigoResultado = 400;
							vObjResultado.datoAdicional = null;
							return vObjResultado;
							
						}
						
					}
			 }
			 if (pData.tipo ==2) {
				 try {
					 LocalDate.parse(datos.fechaMovimiento);
				} catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", la fecha es incorrecta: "+datos.fechaMovimiento;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}
				 try {
					 Double.parseDouble(datos.saldo);
				 }catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", el monto es incorrecto: "+datos.saldo;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}
				 
				 try {
					 Double.parseDouble(datos.debito);
				 }catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", el monto es incorrecto: "+datos.debito;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}
				 
				 try {
					 Double.parseDouble(datos.credito);
				 }catch (Exception e) {
					 vObjResultado.codigoResultado=400;
						vObjResultado.correcto=false;
						vObjResultado.notificacion = "Error en la fila "+contador+", el monto es incorrecto: "+datos.credito;
						vObjResultado.datoAdicional = e.getMessage();
						return vObjResultado;
				}
				 
			 }
		 }
	/*	if ( pData.idCuentaBancaria == null) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Debe selecciónar una cuenta bancaria.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		}*/
		Integer insertSuma=0;
		try {
			
			
			query = connection
					.prepareCall("select wu.id_usuario from workflow.wf_usuario wu where login = ?");
			
			query.setString(1, pData.login);
			resultDatos = query.executeQuery();
			Integer usuarioCreacion = resultDatos.next()?resultDatos.getInt(1):0;
			
			query=null;
			
			Double montoFinal = 0.0;
			if(pData.tipo.equals(1)) {
			for (MovimientoDetalle elementoIterable : pData.movimientoDetalle) {
				
				if(!elementoIterable.descripcion.equals("CSaldo Inicial")) {
					
					
					
				query = connection
						.prepareCall("select * from mteps_tramites.trm_movimiento_cuenta_bancaria td where td.nro_movimiento = ? and date_trunc('day', td.fecha_movimiento)=DATE(?) and td.id_estado =1 and td.id_cuenta_bancaria=?");
				
				query.setString(1, elementoIterable.nroDocumento);
				query.setString(2, elementoIterable.fecha);
				query.setInt(3, pData.idCuentaBancaria);
				resultDatos = query.executeQuery();
				
				if(elementoIterable.tipo.equals("3")) {
					elementoIterable.monto = "-"+elementoIterable.monto;
				}
				
				query=null;
				if(!resultDatos.next()) {
					
					
					
					query = connection.prepareCall(
							"INSERT INTO mteps_tramites.trm_movimiento_cuenta_bancaria\r\n"
							+ "( id_estado, monto, uninet, nro_movimiento, fecha_movimiento, detalle_uninet, movimiento, sigep, operacion_sigep, detalle_sigep, cuenta_transferencia_sigep, id_cuenta_bancaria, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, id_fc,transaccion,estado,agencia,saldo)\r\n"
							+ "VALUES( 1, ?, true, ?, DATE(?), ?, ?, false, NULL, NULL, NULL, ?, now(), NULL,? , NULL,NULL,'ADICIONAR','REGISTRADO',null,null);");
					query.setDouble(1, Double.parseDouble(elementoIterable.monto.replaceAll("[,\\s]+", "")));
					query.setString(2, elementoIterable.nroDocumento);
					query.setString(3, elementoIterable.fecha);
					query.setString(4, elementoIterable.descripcion);
					query.setInt(5, 0);// elementoIterable.movimiento);
					query.setInt(6, pData.idCuentaBancaria);
					query.setInt(7,usuarioCreacion);
					//query.setString(8, elementoIterable.ag);
					//query.setDouble(9, 0)// Double.parseDouble(elementoIterable.saldo.replaceAll("[,\\s]+", "")));
					query.execute();
					insertSuma++;				
				}			
				}
				}
			}else {
				
				for (MovimientoDetalle elementoIterable : pData.movimientoDetalle) {
					
					if(Double.parseDouble(elementoIterable.debito)>0) {
						montoFinal=Double.parseDouble(elementoIterable.debito) * -1;
						elementoIterable.movimiento=2;
					}else {
						montoFinal= Double.parseDouble(elementoIterable.credito);
						elementoIterable.movimiento=1;
					}
					
					query = connection
							.prepareCall("select * from mteps_tramites.trm_movimiento_cuenta_bancaria td  where td.detalle_sigep = ? and date_trunc('day', td.fecha_movimiento)=DATE(?) and td.nro_movimiento=? and td.id_cuenta_bancaria=?");
					query.setString(1, elementoIterable.detalleSigep);
					query.setString(2, elementoIterable.fechaMovimiento);
					query.setString(3, elementoIterable.nroMovimiento);
					query.setInt(4, pData.idCuentaBancaria);
					resultDatos = query.executeQuery();
					query=null;
					if(!resultDatos.next()) {
						
						query = connection.prepareCall(
								"INSERT INTO mteps_tramites.trm_movimiento_cuenta_bancaria\r\n"
								+ "( id_estado, monto, uninet, nro_movimiento, fecha_movimiento, detalle_uninet, movimiento, sigep, operacion_sigep, detalle_sigep,  cuenta_transferencia_sigep, id_cuenta_bancaria, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, id_fc,transaccion,estado,saldo)\r\n"
								+ "VALUES( ?, ?, false, ?, DATE(?), NULL,?, true, ?, ?,  ?, ?, now(), NULL, ?, NULL,NULL,'ADICIONAR','REGISTRADO',?);");
						query.setInt(1, elementoIterable.idEstado);
						query.setDouble(2, montoFinal);
						query.setString(3, elementoIterable.nroMovimiento);	
						query.setString(4, elementoIterable.fechaMovimiento);
						query.setInt(5, elementoIterable.movimiento);
						query.setString(6, elementoIterable.operacionSigep);
						query.setString(7, elementoIterable.detalleSigep);
						query.setString(8, elementoIterable.cuentaTransferenciaSigep);
						query.setInt(9, pData.idCuentaBancaria);
						query.setInt(10,usuarioCreacion);
						query.setDouble(11,Double.parseDouble(elementoIterable.saldo.replaceAll("[,\\s]+", "")));
						query.execute();
						insertSuma++;					
					}			
				}
				
			}
			
			DatosDocumento datosDocumento = new DatosDocumento();
			
				
			
			query = null;
			resultDatos = null;
			query = connection.prepareCall(
					"INSERT INTO mteps_tramites.trm_extracto\r\n"
					+ "( nombre_original, usuario, estado, fecha_creacion, tipo, id_cuenta_bancaria)\r\n"
					+ "VALUES( ?, ?, 'ACTIVO', now(), ?, ?) returning id_extracto");
			query.setString(1,nombre );
			query.setString(2,pData.login);
			query.setString(3,pData.tipo.equals(1)?"UNINET":"SIGEP");
			query.setInt(4, pData.idCuentaBancaria);
			resultDatos = query.executeQuery();
			
			if(resultDatos.next()) {									
			
			datosDocumento.transaccion = "ADICIONAR_DOC";
			datosDocumento.estado ="INICIAL";
			datosDocumento.login = pData.login;
			datosDocumento.idTipoDocumento = 230;
			datosDocumento.ruta = rutaprincipal + "/archivosBackEndMTEPS/SGT/FOND_CUSTODIA/EXTRACTOS/";
			datosDocumento.nombreExtension = "DOC_" + timestamp + "." + extension;
			datosDocumento.tablaRelacion = "mteps_tramites.trm_extracto";
			datosDocumento.modulo = "TRAMITES";
			datosDocumento.tipoMime= file.getContentType();
			datosDocumento.tamanoArchivo =(int) file.getSize();
			datosDocumento.idTablaRelacion  = resultDatos.getInt(1);
			vObjResultado = repoNfsCore.gestionDocumento(datosDocumento, file);
			
			if(!vObjResultado.correcto) {
				return vObjResultado;
			}
			
			}
			
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "Transacción exitosa - Registros nuevos: "+ insertSuma;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = null;
		
		return vObjResultado;


		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}finally {
            try {
                if (query != null) query.close();
                if (connection != null) connection.close();
                if (resultDatos != null) resultDatos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return vObjResultado;
	}

	public Resultado listaCyt(String pVariable1, String pVariable2) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg(fd.*) from mteps_tramites.f_lista_cyt_fc ('" + pVariable1 + "','"
					+ pVariable2 + "') as fd";

			Object resultado = configCore.ejecutarquey(query);

			if (resultado != null) {

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = resultado;
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados para " + pVariable1;
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado cuentasbancariaFecha(String pVariable1, String pVariable2)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg(to_json (res.*)) from (select * from mteps_tramites.f_cuentasbancarias_fecha ('"
					+ pVariable1 + "', '" + pVariable2 + "')) as res ";

			Object resultado = configCore.ejecutarquey(query);

			if (resultado != null) {

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = resultado;
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaExtractos(String pVariable) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg(to_json (res.*))  from (select te.id_extracto as \"idExtracto\", te.nombre_original \"nombreOriginal\", te.usuario, te.estado, te.fecha_creacion as \"fechaCreacion\",TO_CHAR(te.fecha_creacion , 'DD/MM/YYYY HH24:MI:SS') as \"fechaCreacionFormato\", te.tipo, te.id_cuenta_bancaria as \"idCuentaBancaria\",\r\n"
					+ "tcb.numero_cuenta as \"numeroCuenta\",tcb.nombre_cuenta as \"nombreCuenta\",nrd.id_repositorio_documento as \"idRepositorioDocumento\"\r\n"
					+ "from mteps_tramites.trm_extracto te \r\n"
					+ "left join mteps_tramites.trm_cuentas_bancarias tcb on tcb.id_cuenta = te.id_cuenta_bancaria \r\n"
					+ "left join mteps_nfs.nfs_relacion_documento nrd on nrd.id_tabla_relacion = te.id_extracto and nrd.tabla_relacion = 'mteps_tramites.trm_extracto'\r\n"
					+ "where te.usuario = '" + pVariable + "' order by 1 desc) as res";

			Object resultado = configCore.ejecutarquey(query);

			if (resultado != null) {

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = resultado;
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado obtenerCuentaBancaria(String pVariable1)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg(to_json (res.*)) from (select c.id_cuenta as \"idCuenta\",c.numero_cuenta as \"numeroCuenta\" ,c.nombre_cuenta as \"nombreCuenta\" ,b.nombre ,c.estado \r\n"
					+ "		from mteps_tramites.trm_cuentas_bancarias c\r\n"
					+ "		left join parametro.par_clasificador b on b.id_clasificador = c.id_banco and b.id_clasificador_raiz =3166\r\n"
					+ "		where c.estado = 'ACTIVO' and c.jefatura = (select o.unidad_funcional \r\n"
					+ "from rrhh.organigrama o \r\n"
					+ "inner join rrhh.asignaciones a on a.idunidad = o.idorg and a.estado =1\r\n"
					+ "inner join seguridad.usuarios u on u.idpersona = a.idpersona \r\n"
					+ "where u.usuario = '"+pVariable1+"')) as res ";

			Object resultado = configCore.ejecutarquey(query);

			if (resultado != null) {

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = resultado;
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado derivarTramiteFC(DatosDocFC pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement query = null;
		 
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);

		try {
			
			vObjResultado = sigecCore.generarDerivacion(pVariable1);
			
			if(vObjResultado.correcto) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(pVariable1);
				
				
				query = connection.prepareCall("call mteps_tramites.p_gestion_fc_ingresos(?,?,?,?,?)");
				query.setString(1, json);
				query.registerOutParameter(2, Types.BOOLEAN);
				query.registerOutParameter(3, Types.VARCHAR);
				query.registerOutParameter(4, Types.INTEGER);
				query.registerOutParameter(5, Types.INTEGER);

				query.executeUpdate();

				vObjResultado.correcto = (Boolean) query.getObject(2);
				vObjResultado.notificacion = (String) query.getObject(3);
				vObjResultado.codigoResultado = (Integer) query.getObject(4);
				vObjResultado.datoAdicional = query.getObject(5);
			}
			
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}finally {
            try {
                if (query != null) query.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}
}
