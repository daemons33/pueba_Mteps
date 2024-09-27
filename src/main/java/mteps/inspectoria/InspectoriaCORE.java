package mteps.inspectoria;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mteps.config.Resultado;
import mteps.inspectoria.entity.EntDocumentoSigec;
import mteps.inspectoria.entity.FObtenerInspecciones;
import mteps.sigec.entity.usuarioSigec;

@SpringBootApplication
public class InspectoriaCORE {

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Value("${dbovt.url}")
	private String db_url_ovt;

	@Value("${dbovt.usuario}")
	private String db_usuario_ovt;

	@Value("${dbovt.password}")
	private String db_password_ovt;
	
	/** SIGEC */
	@Value("${dbmq.url}")
	private String db_url_sigec;

	@Value("${dbmq.usuario}")
	private String db_usuario_sigec;

	@Value("${dbmq.password}")
	private String db_password_sigec;
	
	@PersistenceContext
	private EntityManager entityManager;

 /**           OBTENER LISTA INSPECTORIA  */
	
	public Resultado listaInspectoria(Object vObjEntradaDatos) throws JsonProcessingException {
		//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		
		try {
			
			Connection connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url_ovt, db_usuario_ovt, db_password_ovt);
			
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspecciones");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedure.setParameter("p_json_pp", json);
			
			List<FObtenerInspecciones> resultadoDatos = procedure.getResultList();
			
			
			if (!resultadoDatos.isEmpty()) {
				for (int i = 0; i < resultadoDatos.size(); i++) {
					FObtenerInspecciones denuncia = resultadoDatos.get(i);
					if(!denuncia.empresaExterna) {
						CallableStatement procedures = connection.prepareCall("select e.nit, e.razon_social , e.matricula_comercio, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion from public.empresa e inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 where e.id_empresa ="+denuncia.idEmpresa);
						ResultSet resultDatos = procedures.executeQuery();
						
						if(resultDatos.next()) {
						denuncia.razonSocial = resultDatos.getString("razon_social");
						denuncia.nit = resultDatos.getString("nit");
						denuncia.matriculaComercio = resultDatos.getString("matricula_comercio");
						denuncia.direccionEmpresa = resultDatos.getString("direccion");
						resultadoDatos.set(i, denuncia);}
					}
				}
				}
			
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultadoDatos;
			connection.close();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	
	/**           GESTION DE INSPECTORIA  */
	public Resultado gestionInspectoria(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vObject);
		
		Connection connection = null;
				
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call mteps_inspecciones.p_gestion_inspecciones(?,?,?,?,?)");
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
		connection.close();
		} catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion = "Error:" + e.getMessage();
			resultado.codigoResultado = 400;
			resultado.datoAdicional = null;
		}
		return resultado;
		}
	
	 /**           OBTENER ACTA  */
	
		public Resultado obteneracta(Integer v_dato) throws JsonProcessingException {
			//Crea un objeto Resultado para vaciar la consulta.
			Resultado vObjResultado = new Resultado();
			
			try {
				StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_acta");

				procedure.setParameter("v_id", v_dato);
				 									
				
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
	
		
		
		
		/**           VERIFICAR CITE */
		public String verificarCite(Integer vId,Integer vTipo) throws JsonProcessingException, SQLException {
			String cite =null;
			try {
					
			Connection connection = null;
					
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("select * from mteps_inspecciones.ins_inspecciones ii  where ii.id_inspeccion = ?");
			procedure.setInt(1, vId);

			ResultSet resultDatos = procedure.executeQuery();
			resultDatos.next();
			
			if (vTipo == 1) {
				//MEMO
				cite =resultDatos.getString("sigec_cite_memo"); 
			}else if(vTipo == 2) {
				//INFORME
				cite = resultDatos.getString("cite_sigec_informe_insp");
			}else if(vTipo == 3) {
				//INFORME OBSTRUCCION
				cite = resultDatos.getString("cite_sigec_informe_obst");
			}
			
			procedure.close();
			connection.close();
			} catch (Exception e) {
				cite = e.getMessage();
			}
			
			return cite;
			}
		
}
