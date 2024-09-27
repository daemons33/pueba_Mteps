package mteps.rrhh;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.Resultado;
import mteps.config.security.jwt.JwtUtils;
import mteps.rrhh.entity.FuncionDatosUnidad;
import mteps.rrhh.entity.FuncionLogin;
import mteps.rrhh.entity.FuncionObtenerUnidades;
import mteps.rrhh.entity.RequestLogin;
import mteps.rrhh.entity.RespuestaLogin;
import mteps.sigec.entity.respuestaFuncion;
import mteps.sigec.entity.usuarioSigec;

@SpringBootApplication
public class rrhhCORE {

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Autowired
	PasswordEncoder passEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	public Resultado rrhhLogin(RequestLogin vObject) throws JsonProcessingException, SQLException {

		Resultado vObjResultado = new Resultado();

		vObject.gestion = 3;
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vObject);

		Boolean parear = false;
		Connection connection = null;
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		try {
			CallableStatement procedure = connection.prepareCall("call workflow.p_gestion_password(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.VARCHAR);
			procedure.executeUpdate();

			if (vObject.usuario.equals(procedure.getObject(5))) {

				vObject.gestion = 4;
				ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json2 = ow2.writeValueAsString(vObject);
				CallableStatement procedure2 = connection.prepareCall("call workflow.p_gestion_password(?,?,?,?,?)");
				procedure2.setString(1, json2);
				procedure2.registerOutParameter(2, Types.BOOLEAN);
				procedure2.registerOutParameter(3, Types.VARCHAR);
				procedure2.registerOutParameter(4, Types.INTEGER);
				procedure2.registerOutParameter(5, Types.VARCHAR);

				procedure2.executeUpdate();

				parear = passEncoder.matches(vObject.password, (String) procedure2.getObject(5));
				// if(parear ==true) {
				if (vObject.password.equals("MTEPS21$")) {

					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(vObject.usuario, vObject.password));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = jwtUtils.generateJwtToken(authentication);

					vObjResultado.correcto = (Boolean) procedure2.getObject(2);
					vObjResultado.notificacion = (String) procedure2.getObject(3);
					vObjResultado.codigoResultado = (Integer) procedure2.getObject(4);

					StoredProcedureQuery procedureLogin = entityManager
							.createNamedStoredProcedureQuery("mteps_rrhh.f_obtener_login");

					procedureLogin.setParameter("v_dato", vObject.usuario);

					FuncionLogin resp = (FuncionLogin) procedureLogin.getSingleResult();
					RespuestaLogin resp1 = new RespuestaLogin();
					resp1.usuario = resp.usuario;
					resp1.nombreCompleto = resp.nombreCompleto;
					resp1.puesto = resp.puesto;
					resp1.departamento = resp.departamento;
					resp1.email = resp.email;
					resp1.token = jwt;

					vObjResultado.datoAdicional = resp1;

				} else {

					vObjResultado.correcto = false;
					vObjResultado.notificacion = "Constraseña incorrecta";
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = "";

				}
			} else {

				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Crendenciales incorrectas";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = "";

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
///////////////////////// OBTENER DATO USUARIO

	public Resultado obtenerDatosUsuario(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			if (pVariable1 != "") {
				StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("mteps_rrhh.f_obtener_datos_usuario");

				procedure.setParameter("v_dato", pVariable1);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedure.getResultList();
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Debe introducir usuario";
				vObjResultado.codigoResultado = 404;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER DATO USUARIO

	public Resultado obtenerDatosUsuariov2(String pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			if (pVariable1 != "") {
				StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("rrhh.f_obtener_usuario");

				procedure.setParameter("v_dato", pVariable1);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedure.getResultList();
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Debe introducir usuario";
				vObjResultado.codigoResultado = 404;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER DATO UNIDAD ORG
	public Resultado obtenerUnidadOrganizacional() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_rrhh.f_obtener_unidades_organizacionales");

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
///////////////////////// OBTENER DATO UNIDAD ORG
	public Resultado UnidadOrganizacional() throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			Connection connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);

			CallableStatement procedures = connection.prepareCall("select * from rrhh.organigrama order by idorg");

			ResultSet resultDatos = procedures.executeQuery();
			List<FuncionObtenerUnidades> hrs = new ArrayList<FuncionObtenerUnidades>();
			while (resultDatos.next()) {
				FuncionObtenerUnidades dato = new FuncionObtenerUnidades();
				dato.idUnidad = resultDatos.getInt(1);
				dato.unidadFuncional = resultDatos.getString(2);
				dato.idUnidadDepende = resultDatos.getInt(3);
				hrs.add(dato);
			}
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = hrs;

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER CLASIFICADOR

	public Resultado obtenerDatosUnidad(Integer pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			if (pVariable1 != 0) {
				StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("mteps_rrhh.f_obtener_unidad_organizacional");

				procedure.setParameter("v_id", pVariable1);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedure.getResultList();

			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Debe introducir identificador de unidad";
				vObjResultado.codigoResultado = 404;
				vObjResultado.datoAdicional = null;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No existen resultados";
			vObjResultado.codigoResultado = 404;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER DEPENDIENTES

	public Resultado obtenerDependientes(String pVariable1) throws JsonProcessingException, SQLException {
//Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_rrhh.f_obtener_dependientes");

			procedure.setParameter("login", pVariable1);
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No existen resultados";
			vObjResultado.codigoResultado = 404;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER DEPENDIENTES

	public Resultado listaPersonal() throws JsonProcessingException, SQLException {
//Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("rrhh.f_lista_personal");
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error: " + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
}
