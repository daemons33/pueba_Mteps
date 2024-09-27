package mteps.poa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.poa.entity.DatoEntradaGestionConciliacion;
import mteps.poa.entity.FReporteReformulacion;
import mteps.poa.entity.F_apertura_organizacion;
import mteps.poa.entity.F_busqueda_certificacion;
import mteps.poa.entity.F_conciliacion_activo;
import mteps.poa.entity.F_ejecucion_presupuestaria_actividad_partida;
import mteps.poa.entity.F_ejecucion_presupuestaria_consolidada;
import mteps.poa.entity.F_ejecucion_presupuestaria_mensual;
import mteps.poa.entity.F_ejecucion_presupuestaria_partida_reformulacion;
import mteps.poa.entity.F_ejecucion_presupuestaria_por_actividad;
import mteps.poa.entity.F_ejecucion_seguimiento_fisico_presupuestario;
import mteps.poa.entity.F_ejecucion_seguimiento_presupuestario;
import mteps.poa.entity.F_estado_ptto_ref;
import mteps.poa.entity.F_formulario_traspaso_presupuesto;
import mteps.poa.entity.F_lista_certificaciones;
import mteps.poa.entity.F_lista_conciliacion;
import mteps.poa.entity.F_lista_doc_conciliacion;
import mteps.poa.entity.F_lista_ejecucion_fisica;
import mteps.poa.entity.F_lista_estruct_json_meta;
import mteps.poa.entity.F_lista_estruct_json_presup;
import mteps.poa.entity.F_lista_ficha_seguimiento;
import mteps.poa.entity.F_lista_indicador;
import mteps.poa.entity.F_lista_proceso_gestion;
import mteps.poa.entity.F_lista_reformulaciones;
import mteps.poa.entity.F_lista_solicitud_certificacion_proceso;
import mteps.poa.entity.F_lista_te_memoria_ref;
import mteps.poa.entity.F_lista_te_seguimiento;
import mteps.poa.entity.F_lista_traspaso_destino;
import mteps.poa.entity.F_lista_traspaso_origen;
import mteps.poa.entity.F_programado_ejecutado;
import mteps.poa.entity.F_reporte_form_presupuesto_gasto;
import mteps.poa.entity.F_reporte_formulacion;
import mteps.poa.entity.F_reporte_reform_presupuesto_gasto;
import mteps.poa.entity.F_reporte_reformulacion_poa;
import mteps.poa.entity.F_uniorg_apertura;
import mteps.poa.entity.f_lista_segev;

@SpringBootApplication
public class PoaCore {
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Value("${direccion.front.sispoa}")
	private String direccionFront;

	@Value("${planpago.app.jwtSecret}")
	private String clave;

	@Autowired
	ConfigCORE configuracion;

	@Autowired
	public MtepsCorreos mtepsCorreo;

	@Autowired
	public PlantillasCorreos plantillaCorreo;

	@PersistenceContext
	private EntityManager entityManager;

	public List<F_lista_indicador> getListaIndicador(@PathVariable Integer id_proceso) {

		StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("mteps_plan.f_lista_indicador");

		procedure.setParameter("i_id_proceso", id_proceso);
		List<F_lista_indicador> resultadoDatos = procedure.getResultList();
		return resultadoDatos;

	}

	public F_reporte_formulacion getReporteFormulacionPoa(Integer v1, Integer v2) {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_formulacion_poa");

		procedure.setParameter("v_id_plan", v1);
		procedure.setParameter("v_id_estado", v2);

		F_reporte_formulacion resultadoDatos = (F_reporte_formulacion) procedure.getSingleResult();
		return resultadoDatos;

	}

	public F_reporte_reformulacion_poa reFormulacionPoa(Integer v2, Integer v3) {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_reformulacion_poa");

		procedure.setParameter("v_id_plan", v2);
		procedure.setParameter("v_id_estado", v3);

		F_reporte_reformulacion_poa resultadoDatos = (F_reporte_reformulacion_poa) procedure.getSingleResult();
		return resultadoDatos;

	}

	public F_reporte_form_presupuesto_gasto getReporteFormulacionPresupuestoGasto(Integer v1, Integer v2) {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_form_presupuesto_gasto");

		procedure.setParameter("v_id_plan", v1);
		procedure.setParameter("v_id_estado", v2);

		F_reporte_form_presupuesto_gasto resultadoDatos = (F_reporte_form_presupuesto_gasto) procedure
				.getSingleResult();
		return resultadoDatos;

	}

	public F_reporte_reform_presupuesto_gasto getReportereFormulacionPresupuestoGasto(Integer v2, Integer v3) {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_reform_presupuesto_gasto");

		procedure.setParameter("v_id_plan", v2);
		procedure.setParameter("v_id_estado", v3);

		F_reporte_reform_presupuesto_gasto resultadoDatos = (F_reporte_reform_presupuesto_gasto) procedure
				.getSingleResult();
		return resultadoDatos;

	}

	public List<F_uniorg_apertura> aperturaPorUnidadOrg(Integer pVariable1, Integer pVariable2, String login,
			Integer pVariable3) {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_programa_apertura");
		procedure.setParameter("v_id_organizacion", pVariable1);
		procedure.setParameter("v_id_gestion", pVariable2);
		procedure.setParameter("v_login", login);
		procedure.setParameter("v_tipo", pVariable3);
		List<F_uniorg_apertura> resultadoDatos = procedure.getResultList();
		return resultadoDatos;

	}

	public Resultado listaSegEv(String v1, String v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("mteps_plan.f_lista_segev");

			procedure.setParameter("i_gestion", v1);
			procedure.setParameter("i_estado", v2);
			List<f_lista_segev> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaProcesoGestion(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_procesos_gestion");

			procedure.setParameter("p_idtipoproceso", v1);
			procedure.setParameter("p_gestion", v2);
			List<F_lista_proceso_gestion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado gestionSegEv(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_segev(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionPlan(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_plan(?,?,?,?,?)");
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
			resultado.notificacion ="No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional =  "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionPlanRef(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_plan_ref(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionMatrizSegEv(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_matriz_segev(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionReformulacion(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_reformulacion(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionFichaSegEv(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_ficha_segev(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionConciliacion(DatoEntradaGestionConciliacion vObject)
			throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			ObjectWriter ow1 = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json1 = ow1.writeValueAsString(vObject.file);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_conciliacion(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.INTEGER);

			procedure.executeUpdate();

			if ((boolean) procedure.getObject(2) && vObject.estado.equals("INICIAL")) {
				for (int i = 0; i < vObject.file.size(); i++) {
					CallableStatement add = connection.prepareCall("INSERT INTO mteps_plan.pln_conciliacion_ptto\r\n"
							+ "(id_conciliacion, id_doc_conciliacion, entidad, da, ue, categoria_programatica, fuente, org, partida, nombre_partida, ptto_inicial, ptto_modificaciones, ptto_vigente, ptto_preventivo, estado, observaciones)\r\n"
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

					add.setInt(1, vObject.file.get(i).nro);
					add.setInt(2, (int) procedure.getObject(5));
					add.setString(3, vObject.file.get(i).entidad);
					add.setString(4, vObject.file.get(i).da);
					add.setString(5, vObject.file.get(i).ue);
					add.setString(6, vObject.file.get(i).cat_prg);
					add.setString(7, vObject.file.get(i).fte);
					add.setString(8, vObject.file.get(i).org);
					add.setString(9, vObject.file.get(i).objeto);
					add.setString(10, vObject.file.get(i).descripcion);
					add.setDouble(11, vObject.file.get(i).pres_inicial);
					add.setDouble(12, vObject.file.get(i).mod_aprobadas);
					add.setDouble(13, vObject.file.get(i).pres_vigente);
					add.setDouble(14, vObject.file.get(i).preventivo);
					add.setString(15, "INICIAL");
					add.setString(16, "");
					add.execute();

				}

			}

			resultado.correcto = (boolean) procedure.getObject(2);
			resultado.notificacion = (String) procedure.getObject(3);
			resultado.codigoResultado = (Integer) procedure.getObject(4);
			resultado.datoAdicional = procedure.getObject(5);

			procedure.close();
			connection.close();
		} catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionEjecucion(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_ejecucion(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado gestionIndicadorPni(Object vObject) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObject);

			Connection connection = null;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.p_gestion_indicador_pni(?,?,?,?,?)");
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
			resultado.notificacion = "No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}
		return resultado;
	}

	public Resultado f_lista_te_seguimiento(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_te_seguimiento");

			procedure.setParameter("i_plan", v1);
			procedure.setParameter("i_id_seguimiento", v2);
			List<F_lista_te_seguimiento> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado f_lista_certificaciones(String v1, String v2,Integer v3) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_certificaciones");

			procedure.setParameter("v_estado", v1);
			procedure.setParameter("v_login", v2);
			procedure.setParameter("v_gestion", v3);
			List<F_lista_certificaciones> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado lista_ficha_seguimiento(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_ficha_seguimiento");

			procedure.setParameter("i_id_seguimiento", v1);
			procedure.setParameter("i_id_plan", v2);
			List<F_lista_ficha_seguimiento> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaDocConciliacion(String v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_doc_conciliacion");

			procedure.setParameter("i_gestion", v1);
			List<F_lista_doc_conciliacion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaConciliacion(Integer v1, Integer v2, String v3) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();
		
		try {
			
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			Date fecha = formatoFecha.parse(v3);
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_conciliacion");

			procedure.setParameter("i_id_doc_conciliacion", v1);
			procedure.setParameter("i_id_plan", v2);
			procedure.setParameter("i_fecha_corte", fecha);
			List<F_lista_conciliacion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaEstructJsonMeta(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_estruct_json_meta");

			procedure.setParameter("i_id_proceso", v1);
			procedure.setParameter("i_id_indicador", v2);
			List<F_lista_estruct_json_meta> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaEstructJsonPresup(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_estruct_json_presup");

			procedure.setParameter("i_id_proceso", v1);
			procedure.setParameter("i_id_indicador", v2);
			List<F_lista_estruct_json_presup> resultadoDatos = procedure.getResultList();

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

	public Resultado listaEjecucionFisica(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_ejecucion_fisica");

			procedure.setParameter("i_id_plan", v1);
			procedure.setParameter("i_tipo_proceso", v2);
			List<F_lista_ejecucion_fisica> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado lista_reformulaciones(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_reformulaciones");

			procedure.setParameter("i_id_plan", v1);
			List<F_lista_reformulaciones> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado estadoPttoRef(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_estado_ptto_ref");

			procedure.setParameter("i_id_plan", v1);

			List<F_estado_ptto_ref> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaTeMemoriaRef(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_te_memoria_ref");

			procedure.setParameter("i_id_plan", v1);

			List<F_lista_te_memoria_ref> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado busquedaCertificacion(Integer v1, String v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_busqueda_certificacion");

			procedure.setParameter("i_id_plan", v1);
			procedure.setParameter("i_partida", v2);

			List<F_busqueda_certificacion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado f_conciliacion_activo() throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_conciliacion_activo");

			List<F_conciliacion_activo> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado aperturaNombre(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("mteps_plan.f_apertura");

			procedure.setParameter("i_id_apertura", v1);

			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerDatoFirma(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_obtener_dato_firma");

			procedure.setParameter("i_id_usuario", v1);

			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getSingleResult();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerNroActivosSeg(String v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_obtener_nro_activos_seg");

			procedure.setParameter("i_estado", v1);

			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getSingleResult();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado programadoEjecutado(Integer v1, Integer v2) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_programado_ejecutado");

			procedure.setParameter("i_id_plan", v1);
			procedure.setParameter("i_id_seguimiento", v2);
			List<F_programado_ejecutado> resultadoDatos = procedure.getResultList();

			double[] programado = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			double[] ejecutado = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			String medidadC = "";
			for (int i = 0; i < resultadoDatos.size(); i++) {

				programado[0] = programado[0] + resultadoDatos.get(i).p_ene;
				programado[1] = programado[1] + resultadoDatos.get(i).p_feb;
				programado[2] = programado[2] + resultadoDatos.get(i).p_mar;
				programado[3] = programado[3] + resultadoDatos.get(i).p_abr;
				programado[4] = programado[4] + resultadoDatos.get(i).p_may;
				programado[5] = programado[5] + resultadoDatos.get(i).p_jun;
				programado[6] = programado[6] + resultadoDatos.get(i).p_jul;
				programado[7] = programado[7] + resultadoDatos.get(i).p_ago;
				programado[8] = programado[8] + resultadoDatos.get(i).p_sep;
				programado[9] = programado[9] + resultadoDatos.get(i).p_oct;
				programado[10] = programado[10] + resultadoDatos.get(i).p_nov;
				programado[11] = programado[11] + resultadoDatos.get(i).p_dic;

				ejecutado[0] = ejecutado[0] + resultadoDatos.get(i).e_ene;
				ejecutado[1] = ejecutado[1] + resultadoDatos.get(i).e_feb;
				ejecutado[2] = ejecutado[2] + resultadoDatos.get(i).e_mar;
				ejecutado[3] = ejecutado[3] + resultadoDatos.get(i).e_abr;
				ejecutado[4] = ejecutado[4] + resultadoDatos.get(i).e_may;
				ejecutado[5] = ejecutado[5] + resultadoDatos.get(i).e_jun;
				ejecutado[6] = ejecutado[6] + resultadoDatos.get(i).e_jul;
				ejecutado[7] = ejecutado[7] + resultadoDatos.get(i).e_ago;
				ejecutado[8] = ejecutado[8] + resultadoDatos.get(i).e_sep;
				ejecutado[9] = ejecutado[9] + resultadoDatos.get(i).e_oct;
				ejecutado[10] = ejecutado[10] + resultadoDatos.get(i).e_nov;
				ejecutado[11] = ejecutado[11] + resultadoDatos.get(i).e_dic;

				medidadC = medidadC + (resultadoDatos.get(i).medidas_correctivas == null ? ""
						: resultadoDatos.get(i).medidas_correctivas);

			}

			for (int i = 0; i < programado.length; i++) {
				if (i != 0) {
					programado[i] = programado[i - 1] + programado[i];
					ejecutado[i] = ejecutado[i - 1] + ejecutado[i];
				}
			}

			F_programado_ejecutado resultado = new F_programado_ejecutado();
			resultado.id = 1;
			resultado.id_plan = resultadoDatos.get(0).id_plan;
			resultado.unidad_funcional = resultadoDatos.get(0).unidad_funcional;
			resultado.descripcion = resultadoDatos.get(0).descripcion;

			resultado.p_ene = programado[0];
			resultado.p_feb = programado[1];
			resultado.p_mar = programado[2];
			resultado.p_abr = programado[3];
			resultado.p_may = programado[4];
			resultado.p_jun = programado[5];
			resultado.p_jul = programado[6];
			resultado.p_ago = programado[7];
			resultado.p_sep = programado[8];
			resultado.p_oct = programado[9];
			resultado.p_nov = programado[10];
			resultado.p_dic = programado[11];
			resultado.e_ene = ejecutado[0];
			resultado.e_feb = ejecutado[1];
			resultado.e_mar = ejecutado[2];
			resultado.e_abr = ejecutado[3];
			resultado.e_may = ejecutado[4];
			resultado.e_jun = ejecutado[5];
			resultado.e_jul = ejecutado[6];
			resultado.e_ago = ejecutado[7];
			resultado.e_sep = ejecutado[8];
			resultado.e_oct = ejecutado[9];
			resultado.e_nov = ejecutado[10];
			resultado.e_dic = ejecutado[11];
			resultado.medidas_correctivas = medidadC;

			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultado;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public List<F_ejecucion_presupuestaria_consolidada> f_ejecucion_presupuestaria_consolidada(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_presupuestaria_consolidada");
		procedure.setParameter("v_id_gestion", v1);
		List<F_ejecucion_presupuestaria_consolidada> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public List<F_ejecucion_presupuestaria_por_actividad> f_ejecucion_presupuestaria_por_actividad(Integer v1,
			Integer v2) throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_presupuestaria_por_actividad");
		procedure.setParameter("v_id_clasificador", v1);
		procedure.setParameter("v_id_gestion", v2);
		List<F_ejecucion_presupuestaria_por_actividad> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public List<F_ejecucion_seguimiento_presupuestario> f_ejecucion_seguimiento_presupuestario(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_seguimiento_presupuestario");
		procedure.setParameter("v_id_plan", v1);
		List<F_ejecucion_seguimiento_presupuestario> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public F_ejecucion_presupuestaria_actividad_partida f_ejecucion_presupuestaria_actividad_partida(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_presupuestaria_actividad_partida");
		procedure.setParameter("v_id_plan", v1);
		F_ejecucion_presupuestaria_actividad_partida resultadoDatos = (F_ejecucion_presupuestaria_actividad_partida) procedure
				.getSingleResult();

		return resultadoDatos;
	}

	public F_ejecucion_presupuestaria_mensual f_ejecucion_presupuestaria_mensual(Integer v1, Integer v2)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_presupuestaria_mensual");
		procedure.setParameter("v_id_clasificador", v1);
		procedure.setParameter("v_id_gestion", v2);
		F_ejecucion_presupuestaria_mensual resultadoDatos = (F_ejecucion_presupuestaria_mensual) procedure
				.getSingleResult();

		return resultadoDatos;
	}

	public List<F_lista_traspaso_origen> f_lista_traspaso_origen(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_lista_traspaso_origen");
		procedure.setParameter("i_id_origen", v1);
		List<F_lista_traspaso_origen> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public List<F_formulario_traspaso_presupuesto> f_formulario_traspaso_presupuesto(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_formulario_traspaso_presupuesto");
		procedure.setParameter("i_id_plan", v1);
		List<F_formulario_traspaso_presupuesto> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public List<F_lista_traspaso_destino> f_lista_traspaso_destino(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_lista_traspaso_destino");
		procedure.setParameter("i_id_destino", v1);
		List<F_lista_traspaso_destino> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public List<F_lista_solicitud_certificacion_proceso> listaSolicitudCertificacionxProceso(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_lista_solicitud_certificacion_proceso");
		procedure.setParameter("v_id_proceso", v1);
		List<F_lista_solicitud_certificacion_proceso> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}

	public Resultado listaActPresUnidOrg() throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_lista_apertura_organizacion");

			List<F_apertura_organizacion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado correoActivacion(String usuario, String correo) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String salt = "12";

			TextEncryptor encryptor = Encryptors.text(clave, salt);
			String textoEncriptado = encryptor.encrypt(usuario);

			String url = direccionFront + "activacion/" + textoEncriptado;

			String[] datos = { usuario, url };
			String asunto = "Activación de usuario SISPOA - MTEPS";

			String plantilla = plantillaCorreo.correoActivacionSISPOA(datos);

			if (configuracion.validarCorreoElectronico(correo)) {
				vObjResultado = mtepsCorreo.enviarCorreo(correo, asunto, plantilla);
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Correo electrónico no valido";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado activarUsuario(String codigo) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String salt = "12";

			TextEncryptor encryptor = Encryptors.text(clave, salt);

			String textoDesencriptado = encryptor.decrypt(codigo);

			Connection connection = null;
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);

			CallableStatement procedure1 = connection.prepareCall(
					"SELECT id_usuario, login, fecha_fin, estado, id_persona, estado_registro, correo, usuario_creacion, usuario_modificacion, fecha_creacion, fecha_modificacion\r\n"
							+ "FROM mteps_plan.pln_usuarios pu\r\n" + "WHERE pu.login=?");

			procedure1.setString(1, textoDesencriptado);
			ResultSet resultDatosMteps = procedure1.executeQuery();

			if (resultDatosMteps.next()) {

				CallableStatement update = connection.prepareCall("UPDATE mteps_plan.pln_usuarios pu\r\n"
						+ "						SET estado=true, estado_registro=true,fecha_modificacion=now()\r\n"
						+ "						WHERE pu.login=?");

				update.setString(1, textoDesencriptado);
				update.execute();

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "Usuario activado correctamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;

				connection.close();
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El usuario no se encuentra registrado o token invalido";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}

/////////////////////////////////////////////////////////////
///////////////////////////// OBTENER DATOS USUARIO
	public Resultado obtenerDatosUsuario(String pVariable1) throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		if (pVariable1 != "") {
			try {
				StoredProcedureQuery procedureEmpresa = entityManager
						.createNamedStoredProcedureQuery("mteps_plan.f_obtener_datos_usuario");
				procedureEmpresa.setParameter("p_login", pVariable1);

				vObjResultado.correcto = true;
				vObjResultado.notificacion = "La consulta se realizó exitosamente";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = procedureEmpresa.getResultList();
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "El registro no existe en la BD";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = e.getMessage();
			}
		} else {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Se requiere login";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}
		return vObjResultado;
	}
	
	public F_ejecucion_presupuestaria_partida_reformulacion ejecucionPresupuestariaPartidaReformulacion(Integer v1)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_presupuesto_reformulaciones_partida");
		procedure.setParameter("v_id_plan", v1);
F_ejecucion_presupuestaria_partida_reformulacion resultadoDatos = (F_ejecucion_presupuestaria_partida_reformulacion) procedure.getSingleResult();

		return resultadoDatos;
	}

	
	public Resultado fReporteReformulacion(Integer v1) throws JsonProcessingException, SQLException {
		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_reporte_reformulacion");
			
			procedure.setParameter("i_id_gestion", v1);

			List<FReporteReformulacion> resultadoDatos = procedure.getResultList();

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
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error: " + e.getMessage();
		}

		return vObjResultado;
	}
	
	public List<F_ejecucion_seguimiento_fisico_presupuestario> f_ejecucion_seguimiento_fisico_presupuestario(Integer v1,Date v2,Date v3)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_ejecucion_seguimiento_fisico_presupuestario");
		procedure.setParameter("v_id_plan", v1);
		procedure.setParameter("v_fecha_inicio", v2);
		procedure.setParameter("v_fecha_final", v3);
		List<F_ejecucion_seguimiento_fisico_presupuestario> resultadoDatos = procedure.getResultList();

		return resultadoDatos;
	}
	
	
	public Resultado validacionConciliacion(Integer idPlan,String fechaCorte) throws JsonProcessingException, SQLException {
		Resultado resultado = new Resultado();
		
		try {
			/*
			 try {
				 LocalDate.parse(fechaCorte);
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			        date = dateFormat.parse(fechaCorte);
				 
				 }catch (Exception e) {
					 resultado.codigoResultado=400;
					 resultado.correcto=false;
					 resultado.notificacion = "La fecha es incorrecta: "+fechaCorte;
					 resultado.datoAdicional = e.getMessage();
						return resultado;
				}
				 
			*/
			Connection connection = null;
			
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call mteps_plan.f_validar_conciliacion(?,?,?,?,?,?)");
			procedure.setInt(1, idPlan);
			procedure.setString(2, fechaCorte);
			procedure.registerOutParameter(3, Types.BOOLEAN);
			procedure.registerOutParameter(4, Types.VARCHAR);
			procedure.registerOutParameter(5, Types.INTEGER);
			procedure.registerOutParameter(6, Types.VARCHAR);

			procedure.executeUpdate();

			resultado.correcto = (boolean) procedure.getObject(3);
			resultado.notificacion = (String) procedure.getObject(4);
			resultado.codigoResultado = (Integer) procedure.getObject(5);
			resultado.datoAdicional = procedure.getObject(6);

			procedure.close();
			connection.close();
		} catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion ="No se pudo completar la operación.";
			resultado.codigoResultado = 400;
			resultado.datoAdicional =  "Error:" + e.getMessage();
		}
		return resultado;
	}
}
