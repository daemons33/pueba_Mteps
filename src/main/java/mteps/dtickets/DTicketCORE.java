package mteps.dtickets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.config.security.jwt.JwtUtils;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.correos.entity.DatosCorreov2;
import mteps.dtickets.entity.FObtenerDatosTicket;
import mteps.dtickets.entity.ResponseLoginSin;
import mteps.ovt.OvtCORE;
import mteps.ovt.entity.EntidadEmpresa;
import mteps.ovt.entity.EntidadUsuarioRup;
import mteps.ovt.repository.UsuarioRupInterface;
import mteps.tramites.repository.ListaTramitesInterface;

@SpringBootApplication
public class DTicketCORE {

	@Value("${spring.profiles.active}")
	private String entorno;

	@Value("${sintoken}")
	private String tokenSIN;

	// Url fundempresa matricula
	@Value("${sin_url_status}")
	private String statusSIN;

	// Url fundempresa matricula
	@Value("${sin_url_login}")
	private String loginSIN;


	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;

	private String usuarioMTEPS = "dtickets";
	private String claveMTEPS = "MTEPS21$";

	@Autowired
	PlantillasCorreos plantilla;

	@Autowired
	MtepsCorreos servCorreo;
	
	@Autowired
	ConfigCORE configCore;

	@Autowired
	@Qualifier("bCryptPasswordEncoder") // Especifica el nombre del bean que se inyectar
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ListaTramitesInterface listaTramitesInterface;
	
	@Autowired
	private UsuarioRupInterface usuarioRupInterface;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private OvtCORE ovtCore;

	@Autowired
	AuthenticationManager authenticationManager;
	RestTemplate restTemplate = new RestTemplate();


	public Resultado gestionEmpleador(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		CallableStatement procedure = null;

		try {

			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			procedure = connection.prepareCall("call mteps_d_tickets.p_gestion_empleador(?,?,?,?,?)");
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
		} finally {
			try {
				if (procedure != null)
					procedure.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return vObjResultado;
	}

	public Resultado loginSIN(ObjectNode pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenSIN);

		HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(pData, headers);
		

		try {
			ResponseEntity<ObjectNode> response = restTemplate.exchange(loginSIN, HttpMethod.POST, requestEntity,
					ObjectNode.class);

			
			ObjectNode res = response.getBody();
			if(response.getStatusCode() == HttpStatus.OK) {		
				
				ResponseLoginSin datosLogin = new ResponseLoginSin();
			
			if ( res.get("Autenticado").asBoolean()) { // quitar en prod
																										// ||
																										// pData.get("clave").asText().equals("12345")
				// if(true){
				try {
					ObjectMapper objectMapper = new ObjectMapper();

					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(usuarioMTEPS, claveMTEPS));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = jwtUtils.generateJwtTokenDTicket(authentication);

					datosLogin.token = jwt;

					// Crear un ObjectNode para almacenar los valores
					ObjectNode datos = objectMapper.createObjectNode();

					// Agregar los valores "transaccion" e "id" al mapa
					datos.put("transaccion", "LOGIN_NIT");
					datos.put("nit", pData.get("nit").asText());

					Resultado gestionEmpleador = gestionEmpleador(datos);

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 200) { // usuario encontrado

						datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
						datosLogin.nombre = gestionEmpleador.notificacion;
						datosLogin.usuario = pData.get("nit").asText();

						vObjResultado.datoAdicional = datosLogin;
						vObjResultado.notificacion = "La autenticación se realizó con exito";
						vObjResultado.codigoResultado = 200;
						vObjResultado.correcto = true;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 401) { // usuario bloqueado
						return gestionEmpleador;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 402) { // sin usuario


						EntidadEmpresa resultEmpresa = ovtCore.obtenerDatosEmpresa(pData.get("nit").asText());

						if (resultEmpresa!=null) {

							datos.put("idEmpresa", resultEmpresa.getIdEmpresa());
							datos.put("matricula", resultEmpresa.getMatriculaComercio());
							datos.put("razonSocial", resultEmpresa.getRazonSocial());
							datos.put("transaccion", "REGISTRAR_NIT");

							gestionEmpleador = gestionEmpleador(datos);

							if (gestionEmpleador.correcto) {

								datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
								datosLogin.nombre = resultEmpresa.getRazonSocial();
								datosLogin.usuario = resultEmpresa.getNit();

								vObjResultado.datoAdicional = datosLogin;
								vObjResultado.notificacion = "La autenticación se realizó con exito";
								vObjResultado.codigoResultado = 200;
								vObjResultado.correcto = true;

							} else {
								vObjResultado.correcto = gestionEmpleador.correcto;
								vObjResultado.notificacion = gestionEmpleador.notificacion;
								vObjResultado.codigoResultado = gestionEmpleador.codigoResultado;
								vObjResultado.datoAdicional = null;

							}

						} else {
							vObjResultado.correcto = true;
							vObjResultado.notificacion = "Debe aperturar su ROE en la OVT";
							vObjResultado.codigoResultado = 403;

						}

						return vObjResultado;

					}
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "No se pudo concluir la operación";
					vObjResultado.codigoResultado = 300;
					vObjResultado.datoAdicional = e.getMessage();
				} 

			} else {
				vObjResultado.notificacion = res.get("Mensaje").asText();
				vObjResultado.codigoResultado = 400;
			}
			}else {
				vObjResultado.notificacion = "La solicitud no pudo realizarse correctamente, verifique sus datos";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = res.get("Mensaje").asText();
			}
			vObjResultado.correcto = true;

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("message").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 300;
			vObjResultado.datoAdicional = vResponseMensage;
		}

		return vObjResultado;
	}
	
	public Resultado loginOVT(ObjectNode pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();
			
				ResponseLoginSin datosLogin = new ResponseLoginSin();
				
				try {
					ObjectMapper objectMapper = new ObjectMapper();					
					
					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(usuarioMTEPS, claveMTEPS));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = jwtUtils.generateJwtTokenDTicket(authentication);

					datosLogin.token = jwt;
					// Crear un ObjectNode para almacenar los valores
					ObjectNode datos = objectMapper.createObjectNode();

					// Agregar los valores "transaccion" e "id" al mapa
					datos.put("transaccion", "LOGIN_NIT");
					datos.put("nit", pData.get("nit").asText());

					Resultado gestionEmpleador = gestionEmpleador(datos);

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 200) { // usuario encontrado

						datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
						datosLogin.nombre = gestionEmpleador.notificacion;
						datosLogin.usuario = pData.get("nit").asText();

						vObjResultado.datoAdicional = datosLogin;
						vObjResultado.notificacion = "La autenticación se realizó con exito";
						vObjResultado.codigoResultado = 200;
						vObjResultado.correcto = true;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 401) { // usuario bloqueado
						return gestionEmpleador;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 402) { // sin usuario


						EntidadEmpresa resultEmpresa = ovtCore.obtenerDatosEmpresa(pData.get("nit").asText());

						if (resultEmpresa!=null) {

							datos.put("idEmpresa", resultEmpresa.getIdEmpresa());
							datos.put("matricula", resultEmpresa.getMatriculaComercio());
							datos.put("razonSocial", resultEmpresa.getRazonSocial());
							datos.put("transaccion", "REGISTRAR_NIT");

							gestionEmpleador = gestionEmpleador(datos);

							if (gestionEmpleador.correcto) {

								datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
								datosLogin.nombre = resultEmpresa.getRazonSocial();
								datosLogin.usuario = resultEmpresa.getNit();

								vObjResultado.datoAdicional = datosLogin;
								vObjResultado.notificacion = "La autenticación se realizó con exito";
								vObjResultado.codigoResultado = 200;
								vObjResultado.correcto = true;

							} else {
								vObjResultado.correcto = gestionEmpleador.correcto;
								vObjResultado.notificacion = gestionEmpleador.notificacion;
								vObjResultado.codigoResultado = gestionEmpleador.codigoResultado;
								vObjResultado.datoAdicional = null;

							}

						} else {
							vObjResultado.correcto = true;
							vObjResultado.notificacion = "Debe aperturar su ROE en la OVT";
							vObjResultado.codigoResultado = 403;

						}

						return vObjResultado;

					}
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "No se pudo concluir la operación";
					vObjResultado.codigoResultado = 300;
					vObjResultado.datoAdicional = e.getMessage();
				} 

			vObjResultado.correcto = true;

	

		return vObjResultado;
	}

	public Resultado authCI(ObjectNode pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		try {

			pData.put("transaccion", "LOGIN_CI");

			Resultado gestionEmpleador = gestionEmpleador(pData);

			boolean nuevo = false;
			if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 402) {

				pData.put("transaccion", "REGISTRAR_CI");

				gestionEmpleador = gestionEmpleador(pData);
				nuevo = true;
			}

			if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 200) {

				ObjectMapper objectMapper = new ObjectMapper();

				// Convertir la cadena JSON en un JsonNode
				JsonNode jsonNode = objectMapper.readTree(gestionEmpleador.notificacion);

				ResponseLoginSin datosLogin = new ResponseLoginSin();
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(usuarioMTEPS, claveMTEPS));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				datosLogin.token = jwtUtils.generateJwtTokenDTicket(authentication);
				datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
				datosLogin.usuario = jsonNode.get("usuario").asText();
				datosLogin.nombre = jsonNode.get("nombre").asText();

				vObjResultado.datoAdicional = datosLogin;
				vObjResultado.notificacion = "La autenticación se realizó con exito";
				vObjResultado.codigoResultado = nuevo ? 201 : 200;
				vObjResultado.correcto = true;

			} else {
				vObjResultado = gestionEmpleador;
			}

			return vObjResultado;
		} catch (SQLException e) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 300;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado authSUP(ObjectNode pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		try {
			
			EntidadUsuarioRup usuarioRup = usuarioRupInterface.obtenerUsuarioRup(pData.get("usuario").asText());
			
			if (usuarioRup !=null) {

				if (bCryptPasswordEncoder.matches(pData.get("clave").asText(), usuarioRup.contrasena)) {

					ObjectMapper objectMapper = new ObjectMapper();
					ObjectNode datos = objectMapper.createObjectNode();
					ResponseLoginSin datosLogin = new ResponseLoginSin();

					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(usuarioMTEPS, claveMTEPS));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = jwtUtils.generateJwtTokenDTicket(authentication);

					datosLogin.token = jwt;

					// Agregar los valores "transaccion" e "id" al mapa
					datos.put("transaccion", "LOGIN_NIT");
					datos.put("nit", pData.get("usuario").asText());

					Resultado gestionEmpleador = gestionEmpleador(datos);

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 200) { // usuario encontrado

						datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
						datosLogin.nombre = gestionEmpleador.notificacion;
						datosLogin.usuario = pData.get("usuario").asText();

						vObjResultado.datoAdicional = datosLogin;
						vObjResultado.notificacion = "La autenticación se realizó con exito";
						vObjResultado.codigoResultado = 200;
						vObjResultado.correcto = true;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 401) { // usuario bloqueado
						return gestionEmpleador;
					}

					if (gestionEmpleador.correcto && gestionEmpleador.codigoResultado == 402) {

						datos.put("idEmpresa", usuarioRup.idEmpresa);
						datos.put("matricula", usuarioRup.matriculaComercio);
						datos.put("razonSocial", usuarioRup.razonSocial);
						datos.put("transaccion", "REGISTRAR_NIT");

						gestionEmpleador = gestionEmpleador(datos);

						if (gestionEmpleador.correcto) {

							datosLogin.idUsuario = (Integer) gestionEmpleador.datoAdicional;
							datosLogin.nombre = usuarioRup.razonSocial;
							datosLogin.usuario = usuarioRup.nit;

							vObjResultado.datoAdicional = datosLogin;
							vObjResultado.notificacion = "La autenticación se realizó con exito";
							vObjResultado.codigoResultado = 200;
							vObjResultado.correcto = true;

						} else {
							vObjResultado.correcto = gestionEmpleador.correcto;
							vObjResultado.notificacion = gestionEmpleador.notificacion;
							vObjResultado.codigoResultado = gestionEmpleador.codigoResultado;
							vObjResultado.datoAdicional = null;

						}

					}
				} else {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "Crendeciales incorrectas";
					vObjResultado.codigoResultado = 201;
				}
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Crendeciales incorrectas";
				vObjResultado.codigoResultado = 201;
			}

			return vObjResultado;

		} catch (SQLException e) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 300;
			vObjResultado.datoAdicional = e.getMessage();
		} 

		return vObjResultado;
	}

	public Resultado gestionTicket(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			vObjResultado = configCore.ejecutarProcedimiento("mteps_d_tickets.p_gestion_ticket", pVariable1);
			FObtenerDatosTicket resultado = new FObtenerDatosTicket();
			if (vObjResultado.correcto == true &&   vObjResultado.codigoResultado == 208) {

				StoredProcedureQuery ejecutarFuncion = entityManager
						.createNamedStoredProcedureQuery("mteps_d_tickets.f_obtener_datos_ticket");
				ejecutarFuncion.setParameter("v_id_ticket", vObjResultado.datoAdicional);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La transacción se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				resultado = (FObtenerDatosTicket) ejecutarFuncion.getSingleResult();
				vObjResultado.datoAdicional = resultado;
				
			}

				
			if (vObjResultado.correcto == true &&  pVariable1.get("transaccion").asText().equals("NOTIFICAR_D_TICKET") && vObjResultado.codigoResultado == 210) {
				
				StoredProcedureQuery ejecutarFuncion = entityManager
						.createNamedStoredProcedureQuery("mteps_d_tickets.f_obtener_datos_ticket");
				ejecutarFuncion.setParameter("v_id_ticket",  pVariable1.get("id").asInt());
				resultado = (FObtenerDatosTicket) ejecutarFuncion.getSingleResult();
				vObjResultado.datoAdicional = resultado;
				Boolean res = entorno.equals("prod");				
				
				
				DatosCorreov2 datos = new DatosCorreov2();
				if (res) {
					datos.enviarA = new String[] { pVariable1.get("correo").asText() };
				} else {
					datos.enviarA = new String[] { pVariable1.get("correo").asText() };//, "jpizarro@mintrabajo.gob.bo","lcarvajal@mintrabajo.gob.bo","mcalzada@mintrabajo.gob.bo"};
				}
				// ENVIAR CORREO
				
				datos.usuario = "soportedticket";
				datos.clave = "Mt3Ps21*";
				datos.cuentaUsuario = "soportedticket@mintrabajo.gob.bo";

				datos.cc = null;
				datos.asunto = "D-Tickets - Ministerio de Trabajo, Empleo y Previsión Social";
				String[] datosCorreo= {resultado.tipoTramite,resultado.codigo,configCore.generarFecha(resultado.fechaAtencion,8), configCore.generarFecha(resultado.horaInicio,11),resultado.nroTramites.toString(),resultado.nombre,resultado.usuario,configCore.generarFecha(resultado.fechaSolicitudTicket,10),resultado.departamento};
				datos.cuerpoMensaje = plantilla.plantillaDtickets(datosCorreo);

				//
				if (configCore.validarCorreoElectronico(pVariable1.get("correo").asText()) ) {
				servCorreo.enviarCorreov2Async(datos);
				}
					
				vObjResultado.codigoResultado = 200;

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

	public Resultado obtenerUsuario(Integer v_id) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_d_tickets.f_obtener_usuario");
			ejecutarFuncion.setParameter("v_id_usuario", v_id);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = ejecutarFuncion.getSingleResult();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}

	public Resultado f_obtener_departamentos() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg( json_build_object( 'idDepartamento',  pc.id_clasificador, 'departamento', pc.nombre, 'tramites', (select json_agg(json_build_object('idTipoTramite',pc2.id_clasificador,'tramite',pc2.nombre,'nroTramitesMinimo', dct.nro_tramite_minimo,'nroTramitesMaximo', dct.nro_tramite_maximo)) from mteps_d_tickets.dtck_configuracion_tipotramite dct inner join parametro.par_clasificador pc2 on pc2.id_clasificador = dct.id_tipo_tramite where dct.estado ='ACTIVO' and dct.id_configuracion = dc.id )))\r\n"
					+ "from mteps_d_tickets.dtck_configuracion dc \r\n"
					+ "inner join parametro.par_clasificador pc on pc.id_clasificador = dc.id_departamento  where dc.estado = 'ACTIVO' and current_date between dc.fecha_inicio and coalesce(dc.fecha_fin, '2030-01-01'::date)";

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
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaDTickets(Integer v_id, String v_estado) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_d_tickets.f_lista_d_tickets");
			ejecutarFuncion.setParameter("v_id_usuario", v_id);
			ejecutarFuncion.setParameter("v_estado", v_estado);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = ejecutarFuncion.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}
	
	public Resultado listarDTickets(String v_login, String v_estado) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_d_tickets.f_lista_d_tickets_mteps");
			ejecutarFuncion.setParameter("v_login", v_login);
			ejecutarFuncion.setParameter("v_estado", v_estado);

			
	        if (!ejecutarFuncion.getResultList().isEmpty()) {
	            vObjResultado.correcto = true;
	            vObjResultado.notificacion = "La consulta se realizó exitosamente";
	            vObjResultado.codigoResultado = 200;
	            vObjResultado.datoAdicional = ejecutarFuncion.getResultList(); 
	        } else {
	            vObjResultado.correcto = true;
	            vObjResultado.notificacion = "No se encontraron resultados.";
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
	
	public Resultado listaTramite(Integer id) throws JsonProcessingException {
		//Crea un objeto Resultado para vaciar la consulta.
				Resultado vObjResultado = new Resultado();

				try {
					
					
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "La consulta se realizó exitosamente";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = listaTramitesInterface.listaTramitesTickets(id);
				} catch (Exception e) {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "No se pudo completar la operación." ;
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = "Error:" + e.getMessage();
				}

				return vObjResultado;
			}
	
	public Resultado obtenerDetalleTickets(String pVariable) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select to_json(consulta.*) from mteps_d_tickets.f_obtener_detalle_ticket('"+pVariable+"') as consulta";

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
	

}
