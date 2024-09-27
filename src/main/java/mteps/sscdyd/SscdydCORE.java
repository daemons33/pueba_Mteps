package mteps.sscdyd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.correos.entity.DatosCorreov2;
import mteps.firmadigital.FirmaDigitalCORE;
import mteps.firmadigital.entity.DatosTokenResponse;
import mteps.firmadigital.entity.FirmarPdfResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@SpringBootApplication
public class SscdydCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;

	@Value("${spring.profiles.active}")
	private String entorno;

	@Value("${ruta.archivos}")
	private String rutaprincipal;

	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	MtepsCorreos servCorreo;

	@Autowired
	PlantillasCorreos plantilla;

	@Autowired
	ConfigCORE configCore;

	@Autowired
	FirmaDigitalCORE firmaCORE;

	public Resultado gestionSscdyd(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
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
			query = connection.prepareCall("call mteps_sscdyd.p_gestion_comites(?,?,?,?,?)");
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
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
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

	public Resultado lista(Object vObjEntradaDatos) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_sscdyd.f_obtener_lista");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedureTramite.setParameter("p_json_pp", json);

			if (!procedureTramite.getResultList().isEmpty()) {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureTramite.getResultList();
			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
		} catch (NoResultException e) {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "No se encontraron resultados";
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	public Resultado firmaDigital() {
		Resultado vObjResultado = new Resultado();
		try {
			JasperPrint jasperPrints = generaReporte(2);

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;

	}

	public Resultado enviarCorreo(Integer v_id) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			query = connection.prepareCall("select * from mteps_sscdyd.certificados where id=?");
			query.setInt(1, v_id);
			resultDatos = query.executeQuery();
			if (resultDatos.next()) {
				DatosCorreov2 datos = new DatosCorreov2();

				datos.usuario = "seguridadocupacional";
				datos.clave = "Mt3Ps.21$";
				datos.cuentaUsuario = "seguridadocupacional@mintrabajo.gob.bo";

				//
				if (entorno == "prod") {
					datos.enviarA = new String[] { resultDatos.getString("correo") };
				} else {
					datos.enviarA = new String[] { "rchallco@mintrabajo.gob.bo" };
				}

				datos.cc = null;
				datos.asunto = "Comites Mixtos - Ministerio de Trabajo, Empleo y Previsión Social";

				datos.cuerpoMensaje = plantilla.plantillaCorreoComiteMixto(null);

				// DocumentoPDF
				JasperPrint jasperPrints = generaReporte(v_id);

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(jasperPrints, outputStream);

				// Crear un recurso ByteArrayResource
				ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

				datos.adjunto = new ByteArrayResource[] { resource };
				datos.nombreAdjunto = new String[] { "Certificado.pdf" };

				//
				return servCorreo.enviarCorreov2(datos);

			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		} finally {
			try {
				if (query != null)
					query.close();
				if (connection != null)
					connection.close();
				if (resultDatos != null)
					resultDatos.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return vObjResultado;
	}

	public JasperPrint generaReporte(Integer v_id) throws JRException, ClassNotFoundException, SQLException {

		String nombre = "", empresa = "", dia = "", mes = "", gestion = "";

		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;

		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		query = connection.prepareCall("select * from mteps_comites.com_capacitaciones where id=?");
		query.setInt(1, v_id);
		resultDatos = query.executeQuery();
		if (resultDatos.next()) {

			nombre = resultDatos.getString("nombre") + " " + resultDatos.getString("apellidos");
			empresa = resultDatos.getString("empresa");
			dia = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"), 5);
			mes = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"), 6);
			gestion = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"), 7);

		}

		String archivo = "/WEB-INF/sscdyd/reporteCertificado.jrxml";

		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);

		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(null));

		parameterMap.put("nombre", nombre);
		parameterMap.put("empresa", empresa);
		parameterMap.put("dia", dia);
		parameterMap.put("mes", mes);
		parameterMap.put("gestion", gestion);

		connection.close();
		query.close();
		resultDatos.close();
		return JasperFillManager.fillReport(report, parameterMap, jrDataSource);

	}

	public Resultado firmaDocumento() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		// configCore.firmaDocPDF();

		return vObjResultado;

	}

	public Resultado firmaMasiva()
			throws SQLException, ClassNotFoundException, IOException, JSchException, SftpException {

		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		CallableStatement procedure2 = null;
		ResultSet resultDatos = null;


		String pinToken = "234567890";
		Integer slotToken = 1;

		Integer id = 0;

		Integer iteradorFirmado = 0, iteradorNoFirmado = 0;

		String certificado = "";

		String codigo = "";

		try {

			DatosTokenResponse datosToken = (DatosTokenResponse) firmaCORE.obtieneDatosToken(slotToken, pinToken);

			String alias = "";

			String docFirmado = "";

			if (datosToken.finalizado) {

				alias = datosToken.datos.dataToken.data.get(0).alias;

				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);

				procedure = connection.prepareCall("select * from mteps_sscdyd.sscdyd_certificados sc where sc.firma = false order by sc.id ");

				resultDatos = procedure.executeQuery();

				while (resultDatos.next()) {

					id = resultDatos.getInt("id");
					codigo = resultDatos.getString("documento");

					File file = new File(resultDatos.getString("ruta"));
					
					System.out.println(id+ " " +file.exists() );
					 
					if (file.exists() || file.isFile()) {
						byte[] documentoPdfByte2 = FileUtils.readFileToByteArray(file);
						byte[] documentoPdfByte3 =Base64.encodeBase64(documentoPdfByte2);
						String documentoPdfBase64 = new String(documentoPdfByte3);
						
						//System.out.println(documentoPdfBase64);
						FirmarPdfResponse documentoFirmadoServ = firmaCORE.firmaDocumento(slotToken, pinToken,documentoPdfBase64, alias);

						if (documentoFirmadoServ.isFinalizado()) {

							docFirmado = documentoFirmadoServ.getDatos().getPdfFirmado();

							firmaCORE.guardarPdfServer(docFirmado,	resultDatos.getString("ruta")); // Almacenar en propioserv
/*
							ValidarFirmaResponse firmaValida = firmaCORE.validarPdf(docFirmado);

							if (certificado.strip() == "") {

								if (firmaValida.isFinalizado()) {

									if (!firmaValida.getDatos().getFirmas().isEmpty()) {
										ObjectMapper objectMapper = new ObjectMapper();
										certificado = objectMapper.writeValueAsString(
										firmaValida.getDatos().getFirmas().get(0).getCertificado());
									}

								}

							}*/
							
							
						   procedure2 = connection.prepareCall(
									"UPDATE mteps_sscdyd.sscdyd_certificados\r\n"
									+ "SET firma=true, fecha_firma=now(), certificado=?, fecha_modificacion=now()\r\n"
									+ "where id = ?;");
						    
						    procedure2.setString(1, certificado);
						    procedure2.setInt(2, id);
							procedure2.execute();
							
							iteradorFirmado++;
							
							
						} else {

							vObjResultado.codigoResultado = 400;
							vObjResultado.notificacion = documentoFirmadoServ.getMensaje();
							vObjResultado.correcto = documentoFirmadoServ.isFinalizado();
							vObjResultado.datoAdicional = null;
							return vObjResultado;
						}
					} else {
						
						iteradorNoFirmado++;
					}
				}
				//////////// FIN FIRMA

				vObjResultado.codigoResultado = 200;
				vObjResultado.notificacion = "Se concluyo satisfactoriamente";
				vObjResultado.correcto = true;
				vObjResultado.datoAdicional = "Documentos firmados: " + iteradorFirmado + ", No firmados: "
						+ iteradorNoFirmado;

			} else {

				vObjResultado.codigoResultado = 400;
				vObjResultado.notificacion = datosToken.mensaje;
				vObjResultado.correcto = datosToken.finalizado;
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
		} finally {
			try {
				if (procedure != null)
					procedure.close();
				if (connection != null)
					connection.close();
				if (resultDatos != null)
					resultDatos.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vObjResultado;

	}
}
