package mteps.rtep;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.config.security.jwt.JwtUtils;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.correos.entity.DatosCorreov2;
import mteps.dtickets.entity.ResponseLoginSin;
import mteps.rtep.entity.FObtenerFormularioUsuario;
import mteps.rtep.entity.PersonaEntity;
import mteps.rtep.entity.UsuarioEntity;
import mteps.rtep.repository.PersonaInterface;
import mteps.rtep.repository.UsuarioInterface;

@SpringBootApplication
public class RtepCORE {

	@Autowired
	private ConfigCORE configCORE;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private PersonaInterface personaInterface;

	@Autowired
	private UsuarioInterface usuarioInterface;

	@Autowired
	private PlantillasCorreos plantilla;

	@Autowired
	private MtepsCorreos servCorreo;

	@Autowired
	JwtUtils jwtUtils;

	@Value("${direccion.front.rtep}")
	private String direccionFront;

	private String usuarioMTEPS = "rtep";
	private String claveMTEPS = "MTEPS21$";
	private String claveEncriptacion = "Mt3Ps.24$.Utic*";

	@Autowired
	@Qualifier("bCryptPasswordEncoder") // Especifica el nombre del bean que se inyectar
	private PasswordEncoder bCryptPasswordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	public Resultado gestionUsuario(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			vObjResultado = configCORE.ejecutarProcedimiento("mteps_rtep.p_gestion_usuario", pVariable1);

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public String generarToken() {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(usuarioMTEPS, claveMTEPS));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwtUtils.generateJwtTokenDTicket(authentication);
	}

	public Resultado authCI(ObjectNode pData) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();

		try {

			pData.put("transaccion", "LOGIN");

			Resultado gestionUsuario = gestionUsuario(pData);

			if (gestionUsuario.correcto && gestionUsuario.codigoResultado == 200) {

				ObjectMapper objectMapper = new ObjectMapper();

				// Convertir la cadena JSON en un JsonNode
				JsonNode jsonNode = objectMapper.readTree(gestionUsuario.notificacion);

				ResponseLoginSin datosLogin = new ResponseLoginSin();

				datosLogin.token = generarToken();
				datosLogin.idUsuario = (Integer) gestionUsuario.datoAdicional;
				datosLogin.usuario = jsonNode.get("usuario").asText();
				datosLogin.nombre = jsonNode.get("nombre").asText();
				String claveBd = jsonNode.get("clave").asText();

				if (bCryptPasswordEncoder.matches( pData.get("clave").toString(),claveBd)) {

					vObjResultado.datoAdicional = datosLogin;
					vObjResultado.notificacion = "La autenticación se realizó con exito";
					vObjResultado.codigoResultado = 200;
					vObjResultado.correcto = true;
				} else {
					vObjResultado.datoAdicional = null;
					vObjResultado.notificacion = "Contraseña incorrecta";
					vObjResultado.codigoResultado = 403;
					vObjResultado.correcto = false;
				}

				return vObjResultado;
			}

			return gestionUsuario;
		} catch (SQLException e) {

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado gestionRegistro(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			vObjResultado = gestionUsuario(pVariable1);

			if (!vObjResultado.correcto) {
				return vObjResultado;
			}

			switch (vObjResultado.codigoResultado) {
			case 200:
				vObjResultado.notificacion = "La transacción se realizo correctamente, el usuario debe esperar la validación de información por correo electrónico";
				vObjResultado.datoAdicional = null;
				break;
			case 201:

				ObjectMapper objectMapper = new ObjectMapper();

				// Convertir la cadena JSON en un JsonNode
				JsonNode jsonNode = objectMapper.readTree(vObjResultado.notificacion);
				ResponseLoginSin datosLogin = new ResponseLoginSin();

				datosLogin.token = generarToken();
				datosLogin.idUsuario = (Integer) vObjResultado.datoAdicional;
				datosLogin.usuario = jsonNode.get("usuario").asText();
				datosLogin.nombre = jsonNode.get("nombre").asText();
				vObjResultado.datoAdicional = datosLogin;
				vObjResultado.notificacion = "La transacción se realizo correctamente";
				break;

			default:
				break;
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public void verificacionSEGIP() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		String variablesCorreo[] = { "", "", "", "", "", "", "", "", "", "" };

		try {

			List<PersonaEntity> personas = personaInterface.obtenerUsuariosContrastacionSegip();

			for (PersonaEntity persona : personas) {

				UsuarioEntity usuario = usuarioInterface.ObtenerUsuario(persona.idUsuario);

				//System.out.println("validando... " + persona.nombres);
				boolean vari = true;
				ObjectMapper mapper = new ObjectMapper();

				// Crear un ObjectNode vacío
				ObjectNode pdata = mapper.createObjectNode();
				pdata.put("transaccion", "REGISTRAR_CONTRASTACION_SEGIP");
				pdata.put("idPersona", persona.idPersona);

				/// Ruta correo

				String salt = "12";

				TextEncryptor encryptor = Encryptors.text(claveEncriptacion, salt);
				String textoEncriptado = encryptor.encrypt(usuario.login);

				String url = direccionFront + "activacion/" + textoEncriptado;

				//

				if (vari) { // si validación con segip fue correcta
					variablesCorreo[0] = "Confirmación de registro de Usuario";// titulo
					variablesCorreo[1] = "Bienvenido, " + persona.nombres + " " + (persona.apellidoPaterno!=null?persona.apellidoPaterno:"") + " "
							+ (persona.apellidoMaterno!=null?persona.apellidoMaterno:"");
					variablesCorreo[2] = "Estas recibiendo este mensaje por que tu usuario fue correctamente verificado en el Sistema de Registro de Trabajadores de la Economía Plural. Ingresa al sistema haciendo click en el siguiente enlace: ";
					variablesCorreo[3] = "Ingresar";
					variablesCorreo[4] = url;
					persona.validacionSegip = true;

					pdata.put("validacionSegip", true);

				} else {
					variablesCorreo[0] = "Validación de usuario inconcluso";// titulo
					variablesCorreo[1] = "Error en ...";
					variablesCorreo[2] = "Estas recibiendo este mensaje por que tu usuario no fue correctamente verificado en el Sistema de Registro de Trabajadores de la Economía Plural. Ingresa al siguiente enlace para solucionarlo: ";
					variablesCorreo[3] = "Actualizar información";
					variablesCorreo[4] = direccionFront;
					pdata.put("validacionSegip", false);
					pdata.put("observacion", "Error en ...");

				}

				pdata.put("notificado", true);

				Resultado res = gestionUsuario(pdata);

				DatosCorreov2 datos = new DatosCorreov2();
				datos.enviarA = new String[] { usuario.correoElectronico };
				datos.usuario = "seguridadocupacional";
				datos.clave = "Mt3Ps.21$";
				datos.cuentaUsuario = "seguridadocupacional@mintrabajo.gob.bo";
				datos.cc = null;
				datos.asunto = "RETEP - Ministerio de Trabajo, Empleo y Previsión Social";
				datos.cuerpoMensaje = plantilla.plantillaRTEP(variablesCorreo);

				servCorreo.enviarCorreov2Async(datos);

			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		// return vObjResultado;
	}

	public Resultado verificarCodigo(String codigo) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		String salt = "12";

		TextEncryptor encryptor = Encryptors.text(claveEncriptacion, salt);
		String textoDesencriptado = "";
		try {
			textoDesencriptado = encryptor.decrypt(codigo);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Código inválido";
			vObjResultado.codigoResultado = 401;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		}
		try {
			
			
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode pdata = mapper.createObjectNode();
			if (!textoDesencriptado.contains("REST_")) {
			pdata.put("transaccion", "VALIDAR_CODIGO_USUARIO");
			pdata.put("login", textoDesencriptado);
			}else {
				pdata.put("transaccion", "VALIDAR_OLVIDE_CLAVE");
				pdata.put("login", textoDesencriptado.replace("REST_", ""));
			}
			

			vObjResultado = gestionUsuario(pdata);

			if (vObjResultado.codigoResultado == 200) {
				vObjResultado.datoAdicional = personaInterface
						.obtenerPersonaId(Long.valueOf((Integer) vObjResultado.datoAdicional));
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();

		}

		return vObjResultado;
	}

	public Resultado obtenerPersona(Long id) {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			PersonaEntity persona = personaInterface.obtenerPersonaId(id);
			if (persona != null) {
				vObjResultado.notificacion = "La consulta se realizo exitosamente";
				vObjResultado.codigoResultado = 200;
			} else {
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
			}
			vObjResultado.datoAdicional = persona;

			vObjResultado.correcto = true;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;

	}

	public Resultado obtenerFormulario() throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_rtep.f_obtener_formulario");

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

	public Resultado gestionFormulario(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			vObjResultado = configCORE.ejecutarProcedimiento("mteps_rtep.p_gestion_formulario", pVariable1);

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerListaFormularios(Integer pVariable) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query = "select json_agg(to_json(consulta.*)) from (select ruf.id_formulario as \"idUsuarioFormulario\", row_number() over ( order by ruf.id_formulario desc ) as correlativo, codigo, gestion,TO_CHAR(fecha_declaracion_jurada, 'DD/MM/YYYY HH24:MI:SS') as \"fechaDeclaracionJurada\", ruf.estado, declaracion_jurada  as \"declaracionJurada\", pc_t.nombre as \"tipoTrabajo\", pc_a.nombre as \"actividadEconomica\"\r\n"
					+ "						from mteps_rtep.rtep_usuarios_formulario ruf \r\n"
					+ "						left join mteps_rtep.rtep_usuarios_formulario_respuesta rufr on ruf.id_formulario = rufr.id_formulario and rufr.id_pregunta= 1\r\n"
					+ "						left join parametro.par_clasificador pc_t on pc_t.id_clasificador =any( rufr.respuesta_clasificador)\r\n"
					+ "						left join mteps_rtep.rtep_usuarios_formulario_respuesta rufr2 on ruf.id_formulario = rufr2.id_formulario and rufr2.id_pregunta= 45\r\n"
					+ "						left join parametro.par_clasificador pc_a on pc_a.id_clasificador =any( rufr2.respuesta_clasificador)\r\n"
					+ "						where ruf.id_usuario =" + pVariable
					+ " and activo = true order by fecha_declaracion_jurada desc) as consulta";

			Object resultado = configCORE.ejecutarquey(query);

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

	public FObtenerFormularioUsuario obtenerFormularioU(Integer pVariable2) throws IOException, SQLException {

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_rtep.f_obtener_formulario_usuario");
			ejecutarFuncion.setParameter("v_id_usuario_formulario", pVariable2);

			return (FObtenerFormularioUsuario) ejecutarFuncion.getSingleResult();

		} catch (Exception e) {
			return null;
		}
	}

	public Resultado obtenerFormularioUsuario(Integer pVariable2) throws IOException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery ejecutarFuncion = entityManager
					.createNamedStoredProcedureQuery("mteps_rtep.f_obtener_formulario_usuario");
			ejecutarFuncion.setParameter("v_id_usuario_formulario", pVariable2);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = obtenerFormularioU(pVariable2);

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}

	public Resultado crearClave(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		try {

			String claveCodificada = bCryptPasswordEncoder.encode(pVariable1.get("clave").toString());

			pVariable1.put("transaccion", "CREAR_CLAVE");
			pVariable1.put("clave", claveCodificada);

			vObjResultado = gestionUsuario(pVariable1);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado olvideClave(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		try {

			pVariable1.put("transaccion", "VERIFICAR_USUARIO");
			vObjResultado = gestionUsuario(pVariable1);

			if (vObjResultado.correcto && vObjResultado.codigoResultado==200) {
				
				UsuarioEntity usuario = usuarioInterface.ObtenerUsuario(Long.valueOf( (Integer)vObjResultado.datoAdicional));
				
				String variablesCorreo[] = { "", "", "", "", "", "", "", "", "", "" };
				
				String salt = "12";

				TextEncryptor encryptor = Encryptors.text(claveEncriptacion, salt);
				String textoEncriptado = encryptor.encrypt("REST_"+usuario.login);

				String url = direccionFront + "activacion/" + textoEncriptado;

					variablesCorreo[0] = "Restablece tu contraseña";// titulo
					variablesCorreo[1] = "Estimado(a), " ;
					variablesCorreo[2] = "Hemos recibido una solicitud de restablecimiento de contraseña de su cuenta. Restablece tu contraseña haciendo click en el siguiente enlace: ";
					variablesCorreo[3] = "Restablecer contraseña";
					variablesCorreo[4] = url;

				DatosCorreov2 datos = new DatosCorreov2();
				datos.enviarA = new String[] { usuario.correoElectronico };
				datos.usuario = "seguridadocupacional";
				datos.clave = "Mt3Ps.21$";
				datos.cuentaUsuario = "seguridadocupacional@mintrabajo.gob.bo";
				datos.cc = null;
				datos.asunto = "RETEP - Restablecer contraseña";
				datos.cuerpoMensaje = plantilla.plantillaRTEP(variablesCorreo);

				servCorreo.enviarCorreov2Async(datos);
				
				vObjResultado.notificacion = "Se envió un correo de restablecimiento de contraseña a la cuenta de: "+ usuario.correoElectronico;
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
	
	public Resultado actualizarClave(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		try {

			UsuarioEntity usuario = usuarioInterface.ObtenerUsuario(pVariable1.get("idUsuario").asLong());
			
			
			if(!bCryptPasswordEncoder.matches( pVariable1.get("claveAntigua").toString(),usuario.clave)) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Contraseña actual incorrecta";
				vObjResultado.codigoResultado = 401;
				vObjResultado.datoAdicional = null;
				return vObjResultado;
			}
				
						
			String claveCodificada = bCryptPasswordEncoder.encode(pVariable1.get("clave").toString());

			pVariable1.put("transaccion", "CREAR_CLAVE");
			pVariable1.put("clave", claveCodificada);
			
			vObjResultado = gestionUsuario(pVariable1);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		}

		return vObjResultado;
	}
}
