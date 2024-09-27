package mteps.parametro;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
@SpringBootApplication
public class ParametroCORE {
	

	@Autowired
	ConfigCORE configCore;
	
	@PersistenceContext
	private EntityManager entityManager;
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER CLASIFICADOR

	public Resultado obtenerClasificador(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_clasificadores");

			procedureEmpresa.setParameter("v_id_clasificador", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	public Resultado obtenerClasificadorPerfil(Integer pVariable1,String pVariable2) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query ="select jsonb_agg(json_build_object('idClasificador',idclasificador,'dominio',dominio,'nombre',nombre,'idClasificadorRaiz',idclasificadorraiz,'variables',variables))  from parametro.f_clasificador_perfil_tra("+pVariable1+", '"+pVariable2+"')";
			
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
