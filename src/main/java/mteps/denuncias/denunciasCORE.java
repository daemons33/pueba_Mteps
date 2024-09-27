package mteps.denuncias;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.Resultado;
import mteps.denuncias.entity.FListaDenuncias;
import mteps.ovt.entity.EntidadEmpresa;

@SpringBootApplication
public class denunciasCORE {
	
	@Value("${dbovt.url}")
	private String db_url1;

	@Value("${dbovt.usuario}")
	private String db_usuario1;

	@Value("${dbovt.password}")
	private String db_password1;
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Resultado listaDenuncias(Object vObject) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncias");
			
			

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);
			procedure.setParameter("p_json_pp", json);
			
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	public Resultado gestionDenuncias(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vObject);
		
		Connection connection = null;
				
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call mteps_denuncias.p_gestion_denuncia(?,?,?,?,?)");
		procedure.setString(1, json);
		procedure.registerOutParameter(2, Types.BOOLEAN);
		procedure.registerOutParameter(3, Types.VARCHAR);
		procedure.registerOutParameter(4, Types.INTEGER);
		procedure.registerOutParameter(5, Types.INTEGER);

		procedure.executeUpdate();
		
		resultado.correcto = (boolean) procedure.getObject(2);
		resultado.notificacion = (String) procedure.getObject(3);
		resultado.codigoResultado = (Integer) procedure.getObject(4);
		resultado.datoAdicional = procedure.getObject(5);
		
		procedure.close();
		} catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion = "No se pudo concluir la operación";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
		}
	
	
	public Resultado obtenerTipoDenuncias() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_tipo_denuncia");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerTipoBeneficios() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_tipo_beneficios");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerTipoRetiro() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_tipo_retiro");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado obtenerTipoCitacion() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_tipo_citacion");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	public Resultado ObtenerSalarioMinimo() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_salario_minimo");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getSingleResult();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerPrefiniquito(Integer v1, String v2) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_prefiniquito");
			procedure.setParameter("i_id", v1);
			procedure.setParameter("i_tipo", v2);

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
	
	
	public Resultado listaSalarioMinimo() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_lista_salario_minimo");

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
}
