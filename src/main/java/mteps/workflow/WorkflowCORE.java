package mteps.workflow;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.workflow.entity.F_lista_roles_perfil;
import mteps.workflow.entity.F_obtener_lista_menu_por_usuario;
import mteps.workflow.entity.F_obtener_lista_perfiles;

@SpringBootApplication
public class WorkflowCORE {
	
	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ConfigCORE configCore;
	
	
	public Resultado gestionWorflow(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("workflow.p_gestion_workflow");
			
			procedureEmpresa.setParameter("p_json", json);
			
			resultado.correcto = (Boolean) procedureEmpresa.getOutputParameterValue("correcto");
			resultado.notificacion = (String) procedureEmpresa.getOutputParameterValue("notificacion");
			resultado.codigoResultado = (Integer) procedureEmpresa.getOutputParameterValue("codigoresultado");
			resultado.datoAdicional = procedureEmpresa.getOutputParameterValue("datoadicional");
			
		} catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion = "Error:" + e.getMessage();
			resultado.codigoResultado = 400;
			resultado.datoAdicional = null;
		}
		return resultado;
	}
	
	
	public Resultado listaRolesPerfil(String v1, String v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("workflow.f_lista_roles_perfil");

			procedure.setParameter("p_modulo", v1);
			procedure.setParameter("p_perfil", v2);
			List<F_lista_roles_perfil> resultadoDatos = procedure.getResultList();

			if (!resultadoDatos.isEmpty()) {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			} else {
				vObjResultado.notificacion = "No se encontraron registros";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultadoDatos;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error: " + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerListaPerfiles(String v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("workflow.f_obtener_lista_perfiles");

			procedure.setParameter("p_modulo", v1);
			List<F_obtener_lista_perfiles> resultadoDatos = procedure.getResultList();

			if (!resultadoDatos.isEmpty()) {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			} else {
				vObjResultado.notificacion = "No se encontraron registros";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultadoDatos;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error: " + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerListaMenuPorUsuario(String v1, String v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("workflow.f_obtener_lista_menu_por_usuario");

			procedure.setParameter("p_login", v1);
			procedure.setParameter("p_modulo", v2);
			List<F_obtener_lista_menu_por_usuario> resultadoDatos = procedure.getResultList();

			if (!resultadoDatos.isEmpty()) {
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
			} else {
				vObjResultado.notificacion = "No se encontraron registros";
			}
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultadoDatos;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error: " + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	public Resultado obtenerListaMenuPorUsuarioSGT(String pVariable1,String pVariable2) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query ="select json_agg(to_json (res.*)) from (select * from workflow.f_obtener_lista_menu_por_usuario_sgt ('"+pVariable1+"', '"+pVariable2+"')) as res ";
			
			Object resultado =configCore.ejecutarquey(query);
			
			if (resultado != null) {
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultado;
			}else {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "No se encontraron resultados";
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;	}
			return vObjResultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
}
