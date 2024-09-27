package mteps.ovt;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mteps.config.ConfigCORE;
/*
import bo.gob.softwarelibre.firmadorestatal.firma.Banderas;
import bo.gob.softwarelibre.firmadorestatal.firma.Firmador;
import bo.gob.softwarelibre.firmadorestatal.token.GestorSlot;
import bo.gob.softwarelibre.firmadorestatal.token.Slot;
import bo.gob.softwarelibre.firmadorestatal.token.Token;*/
import mteps.config.Resultado;
import mteps.config.entity.ObtenerArchivoRemotoResponse;
import mteps.firmadigital.FirmaDigitalCORE;
import mteps.firmadigital.entity.DatosTokenResponse;
import mteps.firmadigital.entity.FirmarPdfResponse;
import mteps.firmadigital.entity.ValidarFirmaResponse;
import mteps.ovt.entity.EntidadDepositos;
import mteps.ovt.entity.EntidadEmpresa;
import mteps.ovt.entity.EntidadPersona;
import mteps.ovt.entity.EntidadSucursal;
import mteps.ovt.entity.FechaFinDeclaracion;
import mteps.ovt.repository.DepositosInterface;
import mteps.ovt.repository.EmpresasInterface;
import mteps.ovt.repository.PersonasInterface;
import mteps.ovt.repository.SucursalesInterface;

@SpringBootApplication
public class OvtCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;

	@Autowired
	ConfigCORE configCore;

	@Autowired
	FirmaDigitalCORE firmaCORE;

	@Autowired
	EmpresasInterface empresasOvtInterface;

	@Autowired
	SucursalesInterface sucursalesOvtInterface;

	@Autowired
	PersonasInterface personasInterface;

	@Autowired
	DepositosInterface depositosInterface;

	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();

	public Resultado obtenerDatosEmpresaNIT(String pVariable1, String pVariable2, String pVariable3)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		ResultSet resultDatos = null;
		CallableStatement procedure = null;
		try {

			if (pVariable1.equals("null")) {
				pVariable1 = "";
			}
			if (pVariable2.equals("null")) {
				pVariable2 = "";
			}
			if (pVariable3.equals("null")) {
				pVariable3 = "";
			}

			String total = pVariable1 + pVariable2 + pVariable3;

			if (total.length() < 3) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "La busqueda debe contener al menos 3 caracteres.";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
				return vObjResultado;
			}

			List<EntidadEmpresa> empresas = empresasOvtInterface.listaEmpresas(pVariable1, pVariable2, pVariable3);

			if (empresas.isEmpty()) {
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);
				procedure = connection.prepareCall("select * from mteps_denuncias.f_obtener_empresa_externa(?,?,?)");
				procedure.setString(1, pVariable1);
				procedure.setString(2, pVariable2);
				procedure.setString(3, pVariable3);
				resultDatos = procedure.executeQuery();
				while (resultDatos.next()) {
					EntidadEmpresa empresa = new EntidadEmpresa();

					empresa.setIdEmpresa(resultDatos.getInt("id_empresa"));
					empresa.setNit(resultDatos.getString("nit_empresa"));
					empresa.setMatriculaComercio(resultDatos.getString("matricula_comercio"));
					empresa.setEstado("");
					empresa.setRazonSocial(resultDatos.getString("razon_social"));
					empresa.setNombreComercial("");
					empresa.setRepresentanteLegal("");
					empresa.setTipoDocumento("");
					empresa.setNroDocumento("");
					empresa.setDescActividadDeclarada("");
					empresa.setFechaConstitucion(null);
					empresa.setSindicato("");
					empresa.setComiteMixto("");
					empresa.setCodigoMteps("");
					empresa.setFechaInicioActividad(null);
					empresa.setFechaInscripcion(null);
					empresa.setFechaDeclaracion(null);
					empresa.setFechaLimiteActualizacion(null);
					empresa.setEstadoActualizacionRoe("");
					empresa.setFechaIniDdjjPlanillas(null);
					empresa.setPaisOrigen("");
					empresa.setFechaHabilitacionImpuestos(null);
					empresa.setSinTrabajadores(false);
					empresa.setFechaHabilitacionPlanillas(null);
					empresa.setNitInactivo(false);
					empresa.setFechaSincronizacion(null);
					empresa.setTipoSociedad("");
					empresa.setActividad("");
					empresa.setNroPatronal("");
					empresa.setDepartamento(resultDatos.getString("departamento"));
					empresa.setDireccion(resultDatos.getString("direccion"));
					empresa.setTelefono(resultDatos.getString("telefono"));
					empresa.setCorreoElectronico(resultDatos.getString("email"));
					empresa.setCategoriaLucro("");
					empresa.setTipoEmpresa("");
					empresa.setEmpresaExterna(true);
					empresas.add(empresa);
				}

				vObjResultado.notificacion = "No se encontraron resultados";
			} else {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = empresas;
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

				if (connection != null)
					connection.close();
				if (procedure != null)
					procedure.close();
				if (resultDatos != null)
					resultDatos.close();

			} catch (SQLException e) {
				return null;
			}
		}

		return vObjResultado;
	}
	
	public EntidadEmpresa obtenerDatosEmpresa(String pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		
		try {
			
			EntidadEmpresa empresa = empresasOvtInterface.obtenerEmpresaNIT(pVariable1);
			return empresa;
			
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			return null;
		} 
	}
	
	public Resultado obtenerDatosEmpresaId(Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		
		Resultado vObjResultado = new Resultado();

		try {

			EntidadEmpresa empresa = empresasOvtInterface.obtenerEmpresa(pVariable1);
	
			vObjResultado.notificacion = "La consulta se realizó exitosamente";			
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = empresa;

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
	

	public Resultado obtenerFechaDeclaracion(String pVariable1, Integer pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		ResultSet resultDatos = null;
		CallableStatement procedure = null;
		try {

			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			procedure = connection.prepareCall("select * from public.f_obtener_fecha_final_declaracion(?,?)");
			procedure.setString(1, pVariable1);
			procedure.setInt(2, pVariable2);

			resultDatos = procedure.executeQuery();

			FechaFinDeclaracion findeclaracion = new FechaFinDeclaracion();

			resultDatos.next();
			findeclaracion.setFechaFinDeclaracion(resultDatos.getDate("fecha_fin_declaracion"));

			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = findeclaracion;
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
				if (connection != null)
					connection.close();
				if (procedure != null)
					resultDatos.close();
				if (resultDatos != null)
					resultDatos.close();
			} catch (SQLException e) {
				return null;
			}
		}

		return vObjResultado;
	}

	public Resultado obtenerDatosSucursalesNIT(Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			List<EntidadSucursal> sucursales = sucursalesOvtInterface.listaSucursales(pVariable1);

			if (sucursales.isEmpty()) {

				vObjResultado.notificacion = "No se encontraron resultados para: " + pVariable1;
			} else {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = sucursales;

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

	public Resultado obtenerSucursales(Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			List<EntidadSucursal> sucursales = sucursalesOvtInterface.listaSucursales(pVariable1);

			if (sucursales.isEmpty()) {

				vObjResultado.notificacion = "No se encontraron resultados para: " + pVariable1;
			} else {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = sucursales;

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

	public Resultado obtenerPersonaFC(String pVariable1, String pVariable2, String pVariable3)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

			EntidadPersona persona = personasInterface.obtenerPersona(pVariable1, formatoFecha.parse(pVariable2),
					pVariable3);

			if (persona != null) {

				vObjResultado.correcto = true;
				vObjResultado.codigoResultado = 200;
				vObjResultado.notificacion = persona.tipo == 1
						? "Datos obtenidos desde declaración de planillas de la empresa con NIT: " + pVariable1
						: "Datos obtenidos de la Oficina Virtual de Trámites";
				vObjResultado.datoAdicional = persona;
				return vObjResultado;

			}

			vObjResultado.notificacion = "No se encontraron resultados para: "
					+ (pVariable1 != null ? pVariable1 + ", " : "") + "número de documento: " + pVariable3
					+ ", fecha de nacimiento: " + pVariable2
					+ " en la Oficina Virtual de Trámites. (Registrar manualmente).";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public List<EntidadDepositos> obtenerDeposito(String pVariable1, String pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {

		try {

			return depositosInterface.obtenerDeposito(pVariable1, pVariable2);

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			return null;
		}
	}

	public Resultado firmaDocumentos()
			throws SQLException, ClassNotFoundException, IOException, JSchException, SftpException {

		DecimalFormat formatoDosDecimales = new DecimalFormat("#.##");

		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		ResultSet resultDatos = null;
		CallableStatement procedure2 = null;
		CallableStatement procedureDJ = null;
		ResultSet resultDatosDJ = null;

		Integer contador = 0;
		double porcentaje = 0.0;
		String mensajeLog = "";

		String pinToken = "37773888";
		Integer slotToken = 1;

		Integer id = 0;
		String ruta = "", rutaSalida = "", codigo = "";

//DESA

//		String host="186.121.201.148";
//		Integer port =22;
//		String username="mteps";
//		String password = "Mt3Ps-23&-Ut1c*";
//		String path="/home/mteps/prueba/com.pdf";
//		String pathSalida= "/home/mteps/prueba/";

		String urlOVT = "jdbc:postgresql://192.168.241.12:5432/bkpovt";
		String usuarioOVT = "mintrabajo";
		String passwordOVT = "mintrabajo";

// PROD		

		String host = "192.168.241.24";
		Integer port = 22;
		String username = "mintrabajo";
		String password = "123456";

//		String urlOVT="jdbc:postgresql://192.168.241.35:5432/mintrabajo_ovt";
//		String usuarioOVT="mintrabajo_ovt";
//		String passwordOVT="6USz8Iz50y39Yi";

		String path = "/home/mteps/prueba/com.pdf";
		String pathSalida = "/home/mteps/prueba/";

		String fechaInicial = "2023-02-07";
		String fechaFinal = "2024-01-10";

		Integer iteradorFirmado = 0, iteradorNoFirmado = 0, iteradorFirmadoDDJJ = 0, iteradorNoFirmadoDDJJ = 0;

		String certificado = "";

		try {
			ruta = "/nfs_ovt/docs/";
			rutaSalida = "C:\\Users\\JUTIC\\Desktop\\OVTFirmaDigital\\docs\\";
			DatosTokenResponse datosToken = (DatosTokenResponse) firmaCORE.obtieneDatosToken(slotToken, pinToken);

			String alias = "";

			String docFirmado = "";

			if (datosToken.finalizado) {

				alias = datosToken.datos.dataToken.data.get(0).alias;

				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(urlOVT, usuarioOVT, passwordOVT);

				procedure = connection.prepareCall(
						"select count(*) from public.documento where estado <> 'FIRMADO' and  DATE_TRUNC('day',fecha_creacion) between DATE(?) and DATE(?)");
				procedure.setString(1, fechaInicial);
				procedure.setString(2, fechaFinal);
				resultDatos = procedure.executeQuery();
				Integer cantidadRegistrosTotal = 0;
				if (resultDatos.next()) {
					cantidadRegistrosTotal = resultDatos.getInt(1);
				}

				procedure.close();
				resultDatos.close();

				/// INICIO ROE
				// procedure = connection.prepareCall("select * from mteps_ovt.documento where
				/// firma_digital = false order by id_documento limit 1");
				procedure = connection.prepareCall(
						"select * from public.documento where estado <> 'FIRMADO' and  DATE_TRUNC('day',fecha_creacion) between DATE(?) and DATE(?) order by id_documento limit 0");
				procedure.setString(1, fechaInicial);
				procedure.setString(2, fechaFinal);
				resultDatos = procedure.executeQuery();

				while (resultDatos.next()) {

					id = resultDatos.getInt("id_documento");
					// ruta = resultDatos.getString("ruta");
					codigo = resultDatos.getString("codigo_documento");
					path = ruta + codigo + ".pdf";

					// byte[] documentoPdfByte1 = configCore.getRemotePdf(host, port, username,
					// password, path);
					ObtenerArchivoRemotoResponse resultadoArchivo = configCore.getRemotePdf(host, port, username,
							password, path);

					if (resultadoArchivo.correcto && resultadoArchivo.archivo.length > 0) {

						byte[] documentoPdfByte2 = Base64.encodeBase64(resultadoArchivo.archivo);
						String documentoPdfBase64 = new String(documentoPdfByte2);

						FirmarPdfResponse documentoFirmadoServ = firmaCORE.firmaDocumento(slotToken, pinToken,
								documentoPdfBase64, alias);

						if (documentoFirmadoServ.isFinalizado()) {

							docFirmado = documentoFirmadoServ.getDatos().getPdfFirmado();

							firmaCORE.guardarPdfServer(docFirmado, rutaSalida + codigo + ".pdf"); // Almacenar en
																									// propioserv
							// configCore.almacenarDocumento(docFirmado, host, port, username, password,
							// path);

							ValidarFirmaResponse firmaValida = firmaCORE.validarPdf(docFirmado);

							if (certificado.strip() == "") {

								if (firmaValida.isFinalizado()) {

									if (!firmaValida.getDatos().getFirmas().isEmpty()) {
										ObjectMapper objectMapper = new ObjectMapper();
										certificado = objectMapper.writeValueAsString(
												firmaValida.getDatos().getFirmas().get(0).getCertificado());
									}

								}

							}
							// System.out.println(certificado);
//				    procedure2 = connection.prepareCall(
//							"UPDATE mteps_ovt.documento SET fecha_firma=now(), firma_digital=true, certificado = ?::json\r\n"
//							+ "WHERE id_documento=?;");
							procedure2 = connection.prepareCall(
									"update documento set  estado = 'FIRMADO' , certificado = ?::json, fecha_firma = now() \r\n"
											+ "where id_documento =?;");

							procedure2.setString(1, certificado);
							procedure2.setInt(2, id);
							procedure2.execute();

							iteradorFirmado++;
							mensajeLog = "EXITO";
						} else {

							vObjResultado.codigoResultado = 400;
							vObjResultado.notificacion = documentoFirmadoServ.getMensaje();
							vObjResultado.correcto = documentoFirmadoServ.isFinalizado();
							vObjResultado.datoAdicional = null;
							return vObjResultado;
						}
					} else {
						iteradorNoFirmado++;
						mensajeLog = "SIN EXITO";
					}
					contador++;
					porcentaje = configCore.calcularPorcentaje(contador, cantidadRegistrosTotal);
					mensajeLog = " [" + formatoDosDecimales.format(porcentaje) + "%]  |  Bloque: 1 de 2  |  Documento "
							+ contador + " de " + cantidadRegistrosTotal + " |  " + "ROE: " + rutaSalida + codigo
							+ ".pdf" + "   |   " + configCore.generarFecha(new Date(), 9) + "   |  " + mensajeLog + "!";
					System.out.println(mensajeLog);

				}
				//////////// FIN ROE

				/// INICIO DDJJ
				// procedure = connection.prepareCall("select * from mteps_ovt.documento where
				/// firma_digital = false order by id_documento limit 1");
				contador = 0;
				ruta = "/nfs_ovt/planillas/";
				rutaSalida = "C:\\Users\\JUTIC\\Desktop\\OVTFirmaDigital\\planillas\\";

				procedureDJ = connection.prepareCall(
						"select count(*) from public.ddjj_planilla where estado_declaracion = 'DECLARADO' and estado_revision <> 'FIRMADO' and DATE_TRUNC('day',fecha_creacion) between DATE(?) and DATE(?)");
				procedureDJ.setString(1, fechaInicial);
				procedureDJ.setString(2, fechaFinal);
				resultDatosDJ = procedureDJ.executeQuery();

				if (resultDatosDJ.next()) {
					cantidadRegistrosTotal = resultDatosDJ.getInt(1);
				}

				procedureDJ.close();
				resultDatosDJ.close();

				procedureDJ = connection.prepareCall(
						"select * from public.ddjj_planilla where estado_declaracion = 'DECLARADO' and estado_revision <> 'FIRMADO' and DATE_TRUNC('day',fecha_creacion) between DATE(?) and DATE(?) order by id_ddjj_planilla asc limit 1");
				procedureDJ.setString(1, fechaInicial);
				procedureDJ.setString(2, fechaFinal);
				resultDatosDJ = procedureDJ.executeQuery();

				while (resultDatosDJ.next()) {

					id = resultDatosDJ.getInt("id_ddjj_planilla");

					codigo = resultDatosDJ.getString("documento_declaracion_jurada");

					path = ruta + codigo + ".pdf";

					// byte[] documentoPdfByte1 = configCore.getRemotePdf(host, port, username,
					// password, path);
					ObtenerArchivoRemotoResponse resultadoArchivo = configCore.getRemotePdf(host, port, username,
							password, path);

					if (resultadoArchivo.correcto && resultadoArchivo.archivo.length > 0) {

						byte[] documentoPdfByte2 = Base64.encodeBase64(resultadoArchivo.archivo);
						String documentoPdfBase64 = new String(documentoPdfByte2);

						FirmarPdfResponse documentoFirmadoServ = firmaCORE.firmaDocumento(slotToken, pinToken,
								documentoPdfBase64, alias);

						if (documentoFirmadoServ.isFinalizado()) {

							docFirmado = documentoFirmadoServ.getDatos().getPdfFirmado();

							firmaCORE.guardarPdfServer(docFirmado, rutaSalida + codigo + ".pdf"); // Almacenar en
																									// propioserv
							// configCore.almacenarDocumento(docFirmado, host, port, username, password,
							// path);

							ValidarFirmaResponse firmaValida = firmaCORE.validarPdf(docFirmado);

							if (certificado.strip() == "") {

								if (firmaValida.isFinalizado()) {

									if (!firmaValida.getDatos().getFirmas().isEmpty()) {
										ObjectMapper objectMapper = new ObjectMapper();
										certificado = objectMapper.writeValueAsString(
												firmaValida.getDatos().getFirmas().get(0).getCertificado());
									}

								}

							}

//				    procedure2 = connection.prepareCall(
//							"UPDATE mteps_ovt.documento SET fecha_firma=now(), firma_digital=true, certificado = ?::json\r\n"
//							+ "WHERE id_documento=?;");
							procedure2 = connection.prepareCall(
									"update ddjj_planilla set  estado_revision  = 'FIRMADO' , certificado_pem  = ?::json, fecha_firma_declaracion  = now() , firma_desatendida = true\r\n"
											+ "where id_ddjj_planilla = ?;");

							procedure2.setString(1, certificado);
							procedure2.setInt(2, id);
							procedure2.execute();

							iteradorFirmadoDDJJ++;
							mensajeLog = "EXITO";
						} else {

							vObjResultado.codigoResultado = 400;
							vObjResultado.notificacion = vObjResultado.notificacion + documentoFirmadoServ.getMensaje();
							vObjResultado.correcto = documentoFirmadoServ.isFinalizado();
							vObjResultado.datoAdicional = null;
							return vObjResultado;
						}
					} else {
						iteradorNoFirmadoDDJJ++;
						mensajeLog = "SIN EXITO";
					}

					contador++;
					porcentaje = configCore.calcularPorcentaje(contador, cantidadRegistrosTotal);
					mensajeLog = " [" + formatoDosDecimales.format(porcentaje) + "%]  |  Bloque: 2 de 2  |  Documento "
							+ contador + " de " + cantidadRegistrosTotal + " |  " + "DDJJ: " + rutaSalida + codigo
							+ ".pdf   |   " + configCore.generarFecha(new Date(), 9) + "   |  " + mensajeLog + "!";
					System.out.println(mensajeLog);
				}
				//////////// FIN DJJ

				vObjResultado.codigoResultado = 200;
				vObjResultado.notificacion = "Se concluyo satisfactoriamente";
				vObjResultado.correcto = true;
				vObjResultado.datoAdicional = "Documentos firmados: " + iteradorFirmado + ", No firmados: "
						+ iteradorNoFirmado + " | Documentos DJJfirmados: " + iteradorFirmadoDDJJ + ", No firmados: "
						+ iteradorNoFirmadoDDJJ;

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
				if (procedure2 != null)
					procedure2.close();
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
	
	
	
	public Resultado firmaPgsst()
			throws SQLException, ClassNotFoundException, IOException, JSchException, SftpException {

		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();


		String pinToken = "1234567890";
		Integer slotToken = 1;

		Integer id = 0;

		Integer iteradorFirmado = 0, iteradorNoFirmado = 0;


		try {

			DatosTokenResponse datosToken = (DatosTokenResponse) firmaCORE.obtieneDatosToken(slotToken, pinToken);

			String alias = "";

			String docFirmado = "";

			if (datosToken.finalizado) {

				alias = datosToken.datos.dataToken.data.get(0).alias;

					File file = new File("C:\\Users\\MTEPS\\Desktop\\formulario_2.pdf");
					
					System.out.println(id+ " " +file.exists() );
					 
					if (file.exists() || file.isFile()) {
						byte[] documentoPdfByte2 = FileUtils.readFileToByteArray(file);
						byte[] documentoPdfByte3 =Base64.encodeBase64(documentoPdfByte2);
						String documentoPdfBase64 = new String(documentoPdfByte3);
						
						//System.out.println(documentoPdfBase64);
						FirmarPdfResponse documentoFirmadoServ = firmaCORE.firmaDocumento(slotToken, pinToken,documentoPdfBase64, alias);

						if (documentoFirmadoServ.isFinalizado()) {

							docFirmado = documentoFirmadoServ.getDatos().getPdfFirmado();

							firmaCORE.guardarPdfServer(docFirmado,	"C:\\Users\\MTEPS\\Desktop\\firmado_digital.pdf"); // Almacenar en propioserv
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
		}
		return vObjResultado;

	}
}