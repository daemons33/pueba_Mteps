package mteps.tramites;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.correos.MtepsCorreos;
import mteps.correos.PlantillasCorreos;
import mteps.correos.entity.EnvioCorreoConfiguracion;
import mteps.inspectoria.entity.EntDocumentoSigec;
import mteps.ovt.OvtCORE;
import mteps.ovt.entity.EntidadDepositos;
import mteps.ovt.entity.EntidadEmpresa;
import mteps.ovt.repository.DepositosInterface;
import mteps.ovt.repository.EmpresasInterface;
import mteps.sigec.sigecCORE;
import mteps.tramites.entity.DatosSigec;
import mteps.tramites.entity.FReporteTramites;
import mteps.tramites.entity.FSeguimientoTramite;
import mteps.tramites.entity.ObjDatosFiltro;
import mteps.tramites.entity.ObtieneCalculoMulta;
import mteps.tramites.entity.ObtieneMulta;
import mteps.tramites.entity.ObtieneMulta2;
import mteps.tramites.fondoCustodia.entity.DatosTrmVisados;
import mteps.tramites.repository.ListaTramitesInterface;

@SpringBootApplication
public class tramiteCORE {

	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;
	
	@Value("${spring.profiles.active}")
	private String perfilActivo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public sigecCORE sigecCore;

	@Autowired
	public MtepsCorreos mtepsCorreo;

	@Autowired
	public ConfigCORE configuracion;

	@Autowired
	public PlantillasCorreos plantillaCorreo;
		
	public EnvioCorreoConfiguracion envioCorreoConfiguracion;
	
	@Autowired
	private ListaTramitesInterface listaTramitesInterface;
	
	@Autowired
	EmpresasInterface empresasInterface;
	
	@Autowired
	DepositosInterface depositosInterface;
	
	@Autowired
	public OvtCORE ovtCore;
	
	
	public void MtepsCorreos(EnvioCorreoConfiguracion envioCorreoConfiguracion) {
		this.envioCorreoConfiguracion = envioCorreoConfiguracion;
	}

///////////////////////////////////////////////
///////////////////////////////////// OBTENER LISTA TRAMITES
	public Resultado listaTramite(Object vObjEntradaDatos) throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = listaTramitesInterface.listaTramites(json);
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerListaTramiteTotal(Object vObjEntradaDatos) throws JsonProcessingException {
		//Crea un objeto Resultado para vaciar la consulta.
				Resultado vObjResultado = new Resultado();

				try {
					
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(vObjEntradaDatos);
					
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "La consulta se realizó exitosamente";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = listaTramitesInterface.listaTramites(json).size();
				} catch (Exception e) {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "No se pudo completar la operación." ;
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = "Error:" + e.getMessage();
				}

				return vObjResultado;
			}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER REQUISITOS

	public Resultado obtenerRequisitos(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_requisitos");

			procedureEmpresa.setParameter("v_id_clasificador", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER VALOR DE TRAMITE

	public Resultado obtenerCostoTramite(Number pVariable1, String pVariable2)
			throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("parametro.f_valor_tramite");

			procedureEmpresa.setParameter("monto", pVariable1);
			procedureEmpresa.setParameter("codigo_tramite", pVariable2);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER TRAMITE

	public Resultado obtenerTramite(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_id");

			procedureEmpresa.setParameter("id", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER TRAMITE

	public Resultado obtenerTramiteCodigoManual(String pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_codigo_manual");

			procedureEmpresa.setParameter("id", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER TRAMITE

	public Resultado obtenerTramiteCodigoTramite(String pVariable1) throws JsonProcessingException, SQLException {
//Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_codigo_tramite");

			procedureEmpresa.setParameter("v_codigo", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getSingleResult();
		} catch (NoResultException e) {
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "No se encontraron resultados" ;
			vObjResultado.codigoResultado = 201;
			vObjResultado.datoAdicional = null;	
			return vObjResultado;	
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// BUSCAR DE TRAMITE

	public Resultado buscarTramite(ObjDatosFiltro pVariable1) throws JsonProcessingException, SQLException {
//Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_buscar_tramite");

			procedureTramite.setParameter("v_codigo", pVariable1.v_codigo);
			procedureTramite.setParameter("v_tramite", pVariable1.v_tramite);
			procedureTramite.setParameter("v_estado", pVariable1.v_estado);
			procedureTramite.setParameter("v_responsable", pVariable1.v_responsable);
			procedureTramite.setParameter("v_nit", pVariable1.v_nit);
			procedureTramite.setParameter("v_hr", pVariable1.v_hr);
			procedureTramite.setParameter("v_fecha_ini", pVariable1.v_fecha_ini);
			procedureTramite.setParameter("v_fecha_fin", pVariable1.v_fecha_fin);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
			;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// OBTENER SEGUIMIENTO DE TRAMITE

	public Resultado obtenerSeguimientoTramite(Integer pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_seguimiento_tramite");

			procedureTramite.setParameter("v_id_tramite", pVariable1);

			List<FSeguimientoTramite> resSGT = procedureTramite.getResultList();

			List<FSeguimientoTramite> resSIGEC = sigecCore.seguimientoHR(resSGT.get(0).hr);

			List<FSeguimientoTramite> listaCombinada = new ArrayList<>();
			listaCombinada.addAll(resSGT);
			listaCombinada.addAll(resSIGEC);

			Collections.sort(listaCombinada, Comparator.comparing(FSeguimientoTramite::getFecha_emision));

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = listaCombinada;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

///////////////////////////////////////////////
///////////////////////////////////// OBTENER CALCULO MULTA
	public Resultado calculoMulta(Object vObjEntradaDatos) throws JsonProcessingException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		
		Connection connection = null;
		CallableStatement procedure = null;
		ResultSet resultDatos = null;
		try {

			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			procedure = connection.prepareCall("select * from mteps_tramites.f_obtener_fecha_fin_declaracion(?)");

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(vObjEntradaDatos);
			procedure.setString(1, json);
			resultDatos = procedure.executeQuery();
			ObtieneCalculoMulta datos = new ObtieneCalculoMulta();
			resultDatos.next();
			datos.setV_fecha_fin_declaracion(resultDatos.getString("v_fecha_fin_declaracion"));
			datos.setV_fecha_pago_prima(resultDatos.getString("v_fecha_pago_prima"));
			datos.setV_minera(resultDatos.getBoolean("v_minera"));
			datos.setV_monto_planilla(resultDatos.getBigDecimal("v_monto_planilla"));

			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_multa_retraso");

			procedureEmpresa.setParameter("var_montoplanilla", datos.v_monto_planilla);
			procedureEmpresa.setParameter("var_minera", datos.v_minera);
			procedureEmpresa.setParameter("var_fechapagoprima", datos.v_fecha_pago_prima);
			procedureEmpresa.setParameter("var_fecha_fin_declaracion", datos.v_fecha_fin_declaracion);

			ObtieneMulta res = (ObtieneMulta) procedureEmpresa.getSingleResult();

			ObtieneMulta2 res2 = new ObtieneMulta2();
			res2.fechaDeclaracion = resultDatos.getString("v_fecha_fin_declaracion");
			res2.vTotalDiasRetraso = res.vTotalDiasRetraso;
			res2.vTotalMulta = res.vTotalMulta;
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = res2;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}finally {
            try {
                if (resultDatos != null) resultDatos.close();
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION TRAMITE

	public Resultado gestionTramites(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement queryValidacion = null;
		CallableStatement procedure = null;
		ResultSet resultDatosOvt = null;
		ResultSet resultDatosMteps = null;
		try {

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(pVariable1);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(pVariable1));
			String transaccion = jsonNode.get("transaccion").asText();

			if (transaccion.equals("REGISTRAR_PAGO")||transaccion.equals("REGISTRAR_DEPOSITO")) {
				String nroDeposito = jsonNode.get("nroDeposito").asText();
				String fechaDeposito = jsonNode.get("fechaDeposito").asText();
				
				
				List<EntidadDepositos> depositoOvt = ovtCore.obtenerDeposito(nroDeposito, fechaDeposito);
				
				if (!depositoOvt.isEmpty() && depositoOvt!=null ) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito
							+ "' fue utilizado en declaración de planillas en la OVT por la empresa "
							+ depositoOvt.get(0).razonSocial + " con NIT: " + depositoOvt.get(0).nit;
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = null;
					return vObjResultado;
				}

				/////// CONSULTA PAGOS SGT

				connection = null;
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);

				CallableStatement procedure2 = connection
						.prepareCall("select * from mteps_tramites.trm_depositos td\r\n"
								+ "								inner join mteps_tramites.trm_pagos tp on tp.id_pago = td.id_pago \r\n"
								+ "								inner join mteps_tramites.trm_tramite tt on tp.id_tramite = tt.id_tramite\r\n"
								+ "								where td.nro_deposito = ? and  td.fecha_deposito=DATE(?) and tt.estado <> 'ANULADO' ");

				procedure2.setString(1, nroDeposito);
				procedure2.setString(2, fechaDeposito);
				 resultDatosMteps = procedure2.executeQuery();

				
				if (resultDatosMteps.next()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito 
							+ "' fue utilizado en trámite: " + resultDatosMteps.getString("codigo_tramite");
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = null;

					return vObjResultado;
				}

			}

			if (transaccion.equals("ENVIAR_CORREO")) {
				Integer idTramite = jsonNode.get("idTramite").asInt();
				StoredProcedureQuery procedureTramite = entityManager
						.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_id");

				procedureTramite.setParameter("id", idTramite);

				mteps.tramites.entity.obtenerTramite tramite = (mteps.tramites.entity.obtenerTramite) procedureTramite
						.getSingleResult();

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String[] datos = { "Resolución Ministerial XX/2023", tramite.tramite, tramite.codigoTramite,
						dateFormat.format(tramite.fechaCreacion), tramite.estado, "" };

				String plantilla = plantillaCorreo.tramiteAprobObs(datos);
				String notificacion = "";
				if (tramite.estado.equals("OBSERVADO")) {
					notificacion = "Observado";
				} else {
					notificacion = "Aprobado";
				}

				return mtepsCorreo.enviarCorreo("rchallco@mintrabajo.gob.bo", "Notificación de Trámites " + notificacion
						+ " – Ministerio de Trabajo, Empleo y Previsión Social", plantilla);
			}

			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.p_gestion_tramite");

			procedureEmpresa.setParameter("p_json", json);

			vObjResultado.correcto = (Boolean) procedureEmpresa.getOutputParameterValue("correcto");
			vObjResultado.notificacion = (String) procedureEmpresa.getOutputParameterValue("notificacion");
			vObjResultado.codigoResultado = (Integer) procedureEmpresa.getOutputParameterValue("codigoresultado");
			vObjResultado.datoAdicional = procedureEmpresa.getOutputParameterValue("datoadicional");
			return vObjResultado;

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
			return vObjResultado;
		}finally {
            try {
                if (resultDatosMteps != null) resultDatosMteps.close();
                if (queryValidacion != null) queryValidacion.close();
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
                if (resultDatosOvt != null) resultDatosOvt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// GESTION MULTAS

	public Resultado gestionMultas(ObjectNode pVariable1) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);
		
		Connection connection = null;
		CallableStatement procedure = null;
		
		try {
			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			procedure = connection.prepareCall("call mteps_tramites.p_gestion_multas(?,?,?,?,?)");
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
			vObjResultado.datoAdicional =  e.getMessage();
		}finally {
            try {
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}

	public Resultado controlMultasVencidas() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();
		
		Connection connection = null;
		ResultSet resultDatos = null;
		CallableStatement procedure = null;
		try {
			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			procedure = connection
					.prepareCall("select * from mteps_tramites.f_control_multas_vencidas()");

			resultDatos = procedure.executeQuery();

			resultDatos.next();
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "Ejecutado correctamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = resultDatos.getInt(1);

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}finally {
            try {
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
                if (resultDatos != null) resultDatos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// OBTENER CODIGO PAGO MULTA

	public Resultado obtenerCodigoPagoMulta(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_pago_multa");

			procedureEmpresa.setParameter("codigo", pVariable1);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getSingleResult();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// /OBTENER LISTA POR PAGAR
	public Resultado obtenerListaPorPagar() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_lista_tramites_xp_multa");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// /OBTENER LISTA POR PAGAR
	public Resultado obtenerDepositos(Integer idpago) throws JsonProcessingException, SQLException {
// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_depositos");

			procedureTramite.setParameter("v_id_pago", idpago);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaPlanillas() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("parametro.f_lista_planillas");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerMultaTramite(String codigo) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_multa_tramite");

			procedureTramite.setParameter("i_codigo", codigo);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getSingleResult();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaPeriodos() throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureEmpresa = entityManager
					.createNamedStoredProcedureQuery("parametro.f_lista_periodos");

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureEmpresa.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listaPeriodoMes(String dominio, String descripcion) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("parametro.f_lista_clasificador");

			procedureTramite.setParameter("i_dominio", dominio);
			procedureTramite.setParameter("i_descripcion", descripcion);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado obtenerPlanillas(Integer idRaiz) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("parametro.f_obtener_planillas");

			procedureTramite.setParameter("v_id_clasificador", idRaiz);

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	public Resultado listarMultas(String v1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		
		Connection connection = null;
		CallableStatement procedures = null;
		ResultSet resultDatos = null;
		
		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_lista_multas");
			procedureTramite.setParameter("i_login", v1);
						

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}finally {
            try {
                if (procedures != null) procedures.close();
                if (connection != null) connection.close();
                if (resultDatos != null) resultDatos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}

	public Resultado correoCalculoMulta(Integer idMulta, String correo) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		
		if (!perfilActivo.equals("prod")) {
			correo = "rchallco@mintrabajo.gob.bo";
		}
		
		Resultado vObjResultado = new Resultado();
		
		Connection connection = null;
		CallableStatement procedures = null;
		ResultSet resultDatos = null;
		try {

			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);

			CallableStatement procedureTramite = connection.prepareCall(
					"select tmp.*,pc.nombre as tipo_planilla  from mteps_tramites.trm_multas_planillas tmp inner join parametro.par_clasificador pc on pc.id_clasificador = tmp.id_tipo_planilla where tmp.id_multa =?");
			procedureTramite.setInt(1, idMulta);
			ResultSet resultDatosMteps = procedureTramite.executeQuery();

			String[] datos = { "", "", "", "", "", "", "", "", "", "", "" };

			if (resultDatosMteps.next()) {
				datos[0] = configuracion.formatoFecha(resultDatosMteps.getDate("fecha_calculo"), true);
				datos[1] = resultDatosMteps.getString("matricula_comercio");
				datos[2] = resultDatosMteps.getString("nit");
				
				if (!resultDatosMteps.getBoolean("empresa_externa")) {
					
						EntidadEmpresa empresa = empresasInterface.obtenerEmpresa(resultDatosMteps.getInt("id_empleador"));						
						datos[1] = empresa.razonSocial;
						datos[2] = empresa.nit;
					
				}

				datos[3] = resultDatosMteps.getString("tipo_planilla");
				datos[4] = resultDatosMteps.getString("mes");
				datos[5] = Integer.toString(resultDatosMteps.getInt("gestion"));
				datos[6] = configuracion.formatoFecha(resultDatosMteps.getDate("fecha_calculo"), true);
				datos[7] = Integer.toString(resultDatosMteps.getInt("dias_retraso"));
				datos[8] = Double.toString(resultDatosMteps.getDouble("monto_planilla"));
				datos[9] = Double.toString(resultDatosMteps.getDouble("multa_calculada"));
				datos[10] =  resultDatosMteps.getString("codigo");

				String plantilla = plantillaCorreo.correoCalculoMulta(datos);

				String asunto = "Calculo de Pago de Multa Impuesta - Ministerio de Trabajo, Empleo y Previsión Social";
				if (configuracion.validarCorreoElectronico(correo)) {
					vObjResultado = mtepsCorreo.enviarCorreo(correo, asunto, plantilla);
				} else {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "Correo electrónico no valido";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = null;
				}

			} else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontro ningun resultado del calculo de multa";
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}
		finally {
            try {
                if (procedures != null) procedures.close();
                if (connection != null) connection.close();
                if (resultDatos != null) resultDatos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

		return vObjResultado;
	}

	public Resultado generarDocumentoSIGEC(DatosSigec v_datos) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			EntDocumentoSigec datosCite = sigecCore.infoDocSigec(v_datos.codigoTramite);
			if (datosCite==null) {
			Resultado vDatSigec = sigecCore.generarDocumentoSIGEC(v_datos.usuario, 0, v_datos.id_destinatario, 85,
					v_datos.referencia, v_datos.fojas, true, v_datos.proveido);

			if (vDatSigec.correcto) {
				
				v_datos.institucionRemitente = v_datos.institucionRemitente.length()>100?v_datos.institucionRemitente.substring(0,Math.min(v_datos.institucionRemitente.length(),100)):v_datos.institucionRemitente;
				
				Boolean actualizarCite = sigecCore.actualizarCiteHr(v_datos.codigoTramite,
						(String) vDatSigec.datoAdicional, v_datos.nombreRemitente, v_datos.cargoRemitente, v_datos.institucionRemitente);

				if (actualizarCite) {
					datosCite = sigecCore.infoDocSigec(v_datos.codigoTramite);
					
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "Hoja de ruta creada correctamente";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = datosCite.hr;
				} else {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "Error al actualizar cite";
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = null;
				}
			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = vDatSigec.notificacion;
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}
			}else {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "Hoja de ruta creada anteriormente";
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = datosCite.hr;
			}
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}

	
	public Resultado actualizaRazonSocial() throws JsonProcessingException {
		//Crea un objeto Resultado para vaciar la consulta.
				Resultado vObjResultado = new Resultado();
			Integer total = 0;
			
			Connection connection2 = null;
			ResultSet resultDatosMteps = null;
			CallableStatement procedureTramite = null;
				try {
					
					Class.forName("org.postgresql.Driver");
					connection2 = DriverManager.getConnection(db_url, db_usuario, db_password);
											
					
					procedureTramite = connection2.prepareCall(
							"select * from mteps_tramites.trm_tramite ");
					resultDatosMteps = procedureTramite.executeQuery();
					
					while(resultDatosMteps.next()) {
						
						if(resultDatosMteps.getInt("id_empresa")>0 && resultDatosMteps.getBoolean("empresa_externa")==false ){
											
							EntidadEmpresa empresa = empresasInterface.obtenerEmpresa(resultDatosMteps.getInt("id_empresa"));
							
							if(empresa !=null) {
								procedureTramite = connection2.prepareCall(
										"UPDATE mteps_tramites.trm_tramite\r\n"
										+ "SET razon_social=?\r\n"
										+ "WHERE id_tramite=?;");
								procedureTramite.setString(1, empresa.razonSocial);
								procedureTramite.setInt(2, resultDatosMteps.getInt("id_tramite"));
								procedureTramite.execute();
								total++;
							}
						}
						
					}
										
					
					connection2.close();
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "La consulta se realizó exitosamente";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = total;
				} catch (Exception e) {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "No se pudo completar la operación." ;
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = "Error:" + e.getMessage();
				}finally {
		            try {
		                if (procedureTramite != null) procedureTramite.close();
		                if (connection2 != null) connection2.close();
		                if (resultDatosMteps != null) resultDatosMteps.close();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }

				return vObjResultado;
			}
	
	public Resultado listaTrmVisados(DatosTrmVisados v_obj) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_lista_trm_visados");

			procedureTramite.setParameter("v_fecha_ini", v_obj.fechaIni);
			procedureTramite.setParameter("v_fecha_fin", v_obj.fechaFin);
			procedureTramite.setParameter("v_codigo_tram", v_obj.codigoTramite);
			procedureTramite.setParameter("v_codigo_visado", v_obj.codigoVisado);
			procedureTramite.setParameter("v_nit", v_obj.nit);
			procedureTramite.setParameter("v_razon_social", v_obj.razonSocial);
			procedureTramite.setParameter("v_nombre_trabajador", v_obj.nombreTrabajador);
			procedureTramite.setParameter("v_ci", v_obj.ci);
			

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedureTramite.getResultList();
			
			return vObjResultado;
		}
		catch (NoResultException e) {
				vObjResultado.correcto = true;
				vObjResultado.notificacion = "No se encontraron resultados" ;
				vObjResultado.codigoResultado = 201;
				vObjResultado.datoAdicional = null;	
				return vObjResultado;		
			
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	
	public Resultado verificarPago(String nroDeposito, String fechaDeposito, Boolean personaNatural, String montoVerificar) {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };
		LocalDate fechaEntrada =null;
		for (DateTimeFormatter formato : formatos) {
			try {
			fechaEntrada = LocalDate.parse(fechaDeposito, formato);
			break;
			}catch(DateTimeParseException e){				
			}			
		}			
		
        LocalDate fechaActual = LocalDate.now();
        Boolean excede=fechaEntrada.plusYears(2).isBefore(fechaActual);
        
        String strNumeroFinal =montoVerificar;
        if(montoVerificar.contains(".")||montoVerificar.contains(".")) {
        String strNumero = montoVerificar.replace(",", ".");       
        int lastIndex = strNumero.lastIndexOf('.');
        String parteAntes = strNumero.substring(0, lastIndex);
        String parteDespues = strNumero.substring(lastIndex + 1); 
        strNumeroFinal = parteAntes.replace(".", "") + "." + parteDespues; }
        Double numero = Double.parseDouble(strNumeroFinal);
		
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		CallableStatement procedure1 = null;
		CallableStatement procedure2 = null;
		CallableStatement procedure3 = null;		
		ResultSet resultDatosMteps = null;
		ResultSet resultDatosMteps2 = null;
		Boolean utilizado=false;
		Double monto =numero;
		try {				
       
				///// CONSULTA OVT
		
				 List<EntidadDepositos> depositos = depositosInterface.obtenerDepositoMonto(nroDeposito, fechaDeposito, numero);
				 
				if (!depositos.isEmpty()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito
							+ "' fue utilizado en declaración de planillas en la OVT por la empresa "
							+ depositos.get(0).razonSocial + " con NIT: " + depositos.get(0).nit ;
					vObjResultado.codigoResultado = 201;
					
					utilizado=true;
					return vObjResultado;
				}
		
				/////// CONSULTA PAGOS SGT
		
				connection = null;
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);
				
				procedure2 = connection
						.prepareCall("select * from mteps_tramites.trm_depositos td\r\n"
								+ "								inner join mteps_tramites.trm_pagos tp on tp.id_pago = td.id_pago \r\n"
								+ "								inner join mteps_tramites.trm_tramite tt on tp.id_tramite = tt.id_tramite\r\n"
								+ "								where td.nro_deposito = ? and  td.fecha_deposito=DATE(?) and tt.estado not in ('ANULADO','ELABORADO') and monto = ?");
		
				procedure2.setString(1, nroDeposito);
				procedure2.setString(2, fechaDeposito);
				procedure2.setDouble(3, numero);
				resultDatosMteps = procedure2.executeQuery();
		
				
				if (resultDatosMteps.next()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito 
							+ "' fue utilizado en trámite (SGT): " + resultDatosMteps.getString("codigo_tramite");
					vObjResultado.codigoResultado = 202;
					utilizado=true;
					return vObjResultado;
				}
				
				procedure3 = connection
						.prepareCall("select * from mteps_tramites.trm_cyt_tramites tct where tct.numboleta::varchar = ? and DATE_TRUNC('day', tct.fechabol) = DATE(?) and tct.montodep = ? ");
		
				procedure3.setString(1, nroDeposito);
				procedure3.setString(2, fechaDeposito);
				procedure3.setDouble(3, numero);
				resultDatosMteps2 = procedure3.executeQuery();
		
				
				if (resultDatosMteps2.next()) {
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "El deposito bancario con Nro. de transacción '" + nroDeposito 
							+ "' fue utilizado en trámite (CyT): " + resultDatosMteps2.getString("codigo");
					vObjResultado.codigoResultado = 203;
					utilizado=true;
					return vObjResultado;
				}		
		
				
						if(!utilizado) {
							if(!excede) {	
						vObjResultado.correcto = true;
						vObjResultado.notificacion = "El deposito no fue utilizado";
						vObjResultado.codigoResultado = 200;
						vObjResultado.datoAdicional = monto;
						}else {
							vObjResultado.correcto = true;
							vObjResultado.notificacion = "La fecha de transacción excede los 2 años de antigüedad";
							vObjResultado.codigoResultado = 206;
							vObjResultado.datoAdicional = null;
						}
						}else {
							
								if(personaNatural) {
									if(monto<=50) {
										vObjResultado.notificacion = "Trámite de Persona Natural con deposito menor a 50 Bs.";
										vObjResultado.codigoResultado = 204;
									}
								}else {
									if(monto<=100) {
										vObjResultado.notificacion = "Trámite de Persona Juridica con deposito menor a 100 Bs.";
										vObjResultado.codigoResultado = 204;
									}
								}
							
							
							vObjResultado.datoAdicional = monto;
						}
				
			
		}catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
			return vObjResultado;
		}finally {
            try {
                if (resultDatosMteps != null) resultDatosMteps.close();
                if (procedure != null) procedure.close();
                if (procedure1 != null) procedure.close();
                if (procedure2 != null) procedure2.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return vObjResultado;
	}
	
	public Resultado f_obtener_ctas_fc_extracto(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query ="select json_agg( to_json(fe.*)) from mteps_tramites.f_obtener_ctas_fc_extracto("+pVariable1+") as fe";
			
			Object resultado =configuracion.ejecutarquey(query);
			
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
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
	
	
	public List<FReporteTramites> f_reporte_tramites(String v1, Date v2, Date v3, String v4, String v5, Integer v6) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		

		try {
			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_reporte_tramites");

			procedureTramite.setParameter("v_login", v1);
			procedureTramite.setParameter("v_fecha_ini", v2);
			procedureTramite.setParameter("v_fecha_fin", v3);
			procedureTramite.setParameter("v_id_unidad", v4);
			procedureTramite.setParameter("v_estado", v5);
			procedureTramite.setParameter("v_id_tipo", v6);
			

			return (List<FReporteTramites>)  procedureTramite.getResultList();
			
			
		} catch (Exception e) {
			return null;
		}

	}
	
	public Resultado f_obtener_unidades_por_usuario(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		try {
			String query ="select json_agg(json_build_object ('idOrg',u.idorg,'unidadFuncional',u.unidad_funcional)) from mteps_tramites.f_reporte_obtener_unidades_por_usuario('"+pVariable1+"') as u";
			
			Object resultado =configuracion.ejecutarquey(query);
			
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
			vObjResultado.notificacion = "No se pudo completar la operación." ;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "Error:" + e.getMessage();
		}

		return vObjResultado;
	}
}
