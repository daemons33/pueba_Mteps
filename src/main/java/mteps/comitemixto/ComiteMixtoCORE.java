package mteps.comitemixto;

import java.io.ByteArrayOutputStream;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.correos.entity.DatosCorreov2;
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
public class ComiteMixtoCORE {

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;
	
	@Value("${spring.profiles.active}")
	private String entorno;

	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	MtepsCorreos servCorreo;

	@Autowired
	PlantillasCorreos plantilla;
	
	@Autowired
	ConfigCORE configCore;

	public Resultado gestionComiteMixto(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
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
			query = connection.prepareCall("call mteps_comites.p_gestion_comites(?,?,?,?,?)");
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
					.createNamedStoredProcedureQuery("mteps_comites.f_obtener_capacitadoscomites");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedureTramite.setParameter("p_json_pp", json);
			
			if(!procedureTramite.getResultList().isEmpty()) {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();}
			else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados" ;
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}
		} catch (NoResultException e) {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "No se encontraron resultados" ;
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;		
		}catch (Exception e) {
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
			query = connection.prepareCall("select * from mteps_comites.com_capacitaciones where id=?");
			query.setInt(1, v_id);
			resultDatos = query.executeQuery();
			if (resultDatos.next()) {
				DatosCorreov2 datos = new DatosCorreov2();

				datos.usuario = "comitemixto";
				datos.clave = "Mt3p$-2023&.Ut1c*21";
				datos.cuentaUsuario = "comitemixto@mintrabajo.gob.bo";

				//
				Boolean res = entorno.equals("prod");
				
				if(res) {
					datos.enviarA = new String[] { resultDatos.getString("correo") };	
				}else{
				datos.enviarA = new String[] { "rchallco@mintrabajo.gob.bo" };}
				
				datos.cc = null;
				datos.asunto = "Comites Mixtos - Ministerio de Trabajo, Empleo y Previsión Social";

				datos.cuerpoMensaje = plantilla.plantillaCorreoComiteMixto(null);

				// DocumentoPDF
				JasperPrint jasperPrints = generaReporte(v_id);
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(jasperPrints, outputStream);

		        // Crear un recurso ByteArrayResource
		        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
		        
		        datos.adjunto = new ByteArrayResource[] {resource};
		        datos.nombreAdjunto = new String[] { "Certificado.pdf" };
				
				//
				return servCorreo.enviarCorreov2(datos);

			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se encontraron resultados";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;
			}

		}catch (Exception e) {
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
		
		String nombre ="", empresa="",dia="",mes="",gestion="";
		
		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;
		
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		query = connection.prepareCall("select * from mteps_comites.com_capacitaciones where id=?");
		query.setInt(1, v_id);
		resultDatos = query.executeQuery();
		if (resultDatos.next()) {
			
			nombre=resultDatos.getString("nombre") +" " +resultDatos.getString("apellidos");
			empresa = resultDatos.getString("empresa");
			dia = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"),5);
			mes = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"),6);
			gestion = configCore.generarFecha(resultDatos.getDate("fecha_capacitacion"),7);
			
		}
		
		String archivo = "/WEB-INF/comiteMixto/reporteComiteMixto.jrxml";
		
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

}
