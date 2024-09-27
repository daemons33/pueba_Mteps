package mteps.consultas;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;

@SpringBootApplication
public class ConsultasCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;


	@PersistenceContext
	private EntityManager entityManager;

	public Resultado listaConsultas(Object vObject) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_consultas");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);
			procedure.setParameter("p_json_pp", json);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION CONSULTAS

	public Resultado gestionConsultas(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		try {

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_denuncias.p_gestion_consulta(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);

			procedure.executeUpdate();

			vObjResultado.correcto = (boolean) procedure.getObject(2);
			vObjResultado.notificacion = (String) procedure.getObject(3);
			vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
			vObjResultado.datoAdicional = procedure.getObject(5);

			procedure.close();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION PRE FINIQUITO

	public Resultado gestionPrefiniquito(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);

		Connection connection = null;
		try {

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection
					.prepareCall("call mteps_denuncias.p_gestion_pre_finiquito(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);

			procedure.executeUpdate();

			vObjResultado.correcto = (boolean) procedure.getObject(2);
			vObjResultado.notificacion = (String) procedure.getObject(3);
			vObjResultado.codigoResultado = (Integer) procedure.getObject(4);
			vObjResultado.datoAdicional = procedure.getObject(5);

			procedure.close();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	/////////////////////////////////////////////////////////////////////////////////////

	public Resultado obtenerPreguntas(Integer vId) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_preguntas");

			procedure.setParameter("v_id_consulta", vId);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////////////////////////////

	public Resultado obtenerBancoPregunta(String vDato) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_banco");

			procedure.setParameter("v_dato", vDato);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	
	public Resultado obtenerPersona(String v1,Integer v2, String v3) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("externos_mteps.f_obtener_persona");

			procedure.setParameter("i_nro_documento", v1);
			procedure.setParameter("i_tipo_documento", v2);
			procedure.setParameter("i_complemento", v3.equals("null")?null:v3);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getSingleResult();

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
}
