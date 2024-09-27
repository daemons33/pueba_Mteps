package mteps.tramites;


import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.tramites.entity.DatosSigec;
import mteps.tramites.entity.FReporteTramites;
import mteps.tramites.entity.ObjDatosFiltro;
import mteps.tramites.fondoCustodia.entity.DatosTrmVisados;

@RestController
@RequestMapping("/tramites")
public class tramiteREST {


	@Autowired
	tramiteCORE tramites;

	@Autowired
	ConfigCORE configCore;

	@PersistenceContext
	private EntityManager entityManager;

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA TRAMITES

	@RequestMapping(path = "/listatramites", method = RequestMethod.POST)
	public Resultado obtenerListaTramite(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return tramites.listaTramite(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/listatramitesTotalRegistros", method = RequestMethod.POST)
	public Resultado obtenerListaTramiteTotal(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return tramites.obtenerListaTramiteTotal(vObjEntradaDatos);		
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER REQUISITOS

	@RequestMapping(path = "/requisitos", method = RequestMethod.GET)
	public Resultado ObtenerRequisitos(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerRequisitos(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER COSTO DE TRAMITE

	@RequestMapping(path = "/costotramite", method = RequestMethod.GET)
	public Resultado ObtenerCostoTramite(
			@RequestParam(name = "monto", required = true, defaultValue = "0") Number pVariable1,
			@RequestParam(name = "codigotramite", required = true, defaultValue = "") String pVariable2)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerCostoTramite(pVariable1, pVariable2);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER TRAMITE

	@RequestMapping(path = "/obtenertramite", method = RequestMethod.GET)
	public Resultado ObtenerTramite(@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerTramite(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER TRAMITE

	@RequestMapping(path = "/obtenerTramitePorCM", method = RequestMethod.GET)
	public Resultado obtenerTramiteCodigoManual(
			@RequestParam(name = "codigo", required = true, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerTramiteCodigoManual(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER TRAMITE

	@RequestMapping(path = "/obtenerTramitePorCodigo", method = RequestMethod.GET)
	public Resultado obtenerTramiteCodigoTramite(
			@RequestParam(name = "codigo", required = true, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerTramiteCodigoTramite(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER SEGUIMIENTO TRAMITE

	@RequestMapping(path = "/buscarTramite", method = RequestMethod.POST)
	public Resultado buscarTramite(@RequestBody ObjDatosFiltro pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.buscarTramite(pVariable1);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER SEGUIMIENTO TRAMITE

	@RequestMapping(path = "/seguimientoTramite", method = RequestMethod.GET)
	public Resultado obtenerSeguimientoTramite(
			@RequestParam(name = "idTramite", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerSeguimientoTramite(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER CALCULO DE MULTA

	@RequestMapping(path = "/calculomulta", method = RequestMethod.POST)
	public Resultado obtenerCalculoMulta(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return tramites.calculoMulta(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION TRAMITES
	@RequestMapping(path = "/gestiontramite", method = RequestMethod.POST)
	public Resultado gestionTramites(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return tramites.gestionTramites(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION MULTAS
	@RequestMapping(path = "/gestionMultas", method = RequestMethod.POST)
	public Resultado gestionMulta(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return tramites.gestionMultas(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER SEGUIMIENTO TRAMITE

	@RequestMapping(path = "/controlMultasVencidas", method = RequestMethod.GET)
	public Resultado controlMultasVencidas() throws JsonProcessingException, SQLException {
		return tramites.controlMultasVencidas();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER CODIGO DE PAGO MULTA

	@RequestMapping(path = "/obtenercodigopagomulta", method = RequestMethod.GET)
	public Resultado ObtenerCodigoPagoMulta(
			@RequestParam(name = "codigoTramite", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerCodigoPagoMulta(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA POR PAGAR

	@RequestMapping(path = "/listatramitesporpagar", method = RequestMethod.GET)
	public Resultado obtenerListaPorPagar() throws JsonProcessingException, SQLException {
		return tramites.obtenerListaPorPagar();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER DEPOSITOS BANCARIOS

	@RequestMapping(path = "/obtenerdepositos", method = RequestMethod.GET)
	public Resultado obtenerDepositos(
			@RequestParam(name = "idpago", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerDepositos(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/listarPlanillas", method = RequestMethod.GET)
	public Resultado listarPlanillas() throws JsonProcessingException, SQLException {
		return tramites.listaPlanillas();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/obtenerMultaTramite", method = RequestMethod.GET)
	public Resultado obtenerMultaTramite(
			@RequestParam(name = "codigo", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerMultaTramite(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/listarPeriodos", method = RequestMethod.GET)
	public Resultado listarPeriodos() throws JsonProcessingException, SQLException {
		return tramites.listaPeriodos();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/listarClasificador", method = RequestMethod.GET)
	public Resultado listarPeriodos(
			@RequestParam(name = "dominio", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "descripcion", required = true, defaultValue = "null") String pVariable2)
			throws JsonProcessingException, SQLException {
		return tramites.listaPeriodoMes(pVariable1, pVariable2);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/obtenerPlanillas", method = RequestMethod.GET)
	public Resultado obtenerPlanillas(
			@RequestParam(name = "idClasificadorRaiz", required = true, defaultValue = "null") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.obtenerPlanillas(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER PLANILLAS

	@RequestMapping(path = "/listarMultas", method = RequestMethod.GET)
	public Resultado listarMultas(@RequestParam(name = "login", required = true, defaultValue = "") String pVariable)
			throws JsonProcessingException, SQLException {
		return tramites.listarMultas(pVariable);
	}


	@RequestMapping(path = "/correoCalculoMulta", method = RequestMethod.GET)
	public Resultado correoCalculoMulta(
			@RequestParam(name = "idMulta", required = true, defaultValue = "null") Integer pVariable1,
			@RequestParam(name = "correo", required = true, defaultValue = "null") String pVariable2)
			throws JsonProcessingException, SQLException {
		return tramites.correoCalculoMulta(pVariable1, pVariable2);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////Nueva HR - TRAMITES

	@RequestMapping(path = "/generaDoc", method = RequestMethod.POST)
	public Resultado generaDoc(@RequestBody DatosSigec vObjEntradaDatos)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return tramites.generarDocumentoSIGEC(vObjEntradaDatos);
	}

	@RequestMapping(path = "/actualizaRazonSocial", method = RequestMethod.GET)
	public Resultado actualizaRazonSocial() throws JsonProcessingException, SQLException {
		return tramites.actualizaRazonSocial();
	}

	@RequestMapping(path = "/listaTrmVisados", method = RequestMethod.POST)
	public Resultado listaTrmVisados(@RequestBody DatosTrmVisados vObjEntradaDatos)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return tramites.listaTrmVisados(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/verificarPago", method = RequestMethod.GET)
	public Resultado verificarPago(
			@RequestParam(name = "nroDeposito", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "fecha", required = true, defaultValue = "null") String pVariable2,
			@RequestParam(name = "personaNatural", required = false, defaultValue = "null") Boolean pVariable3,			
			@RequestParam(name = "monto", required = false, defaultValue = "0") String pVariable4)
			throws JsonProcessingException, SQLException {
		return tramites.verificarPago(pVariable1, pVariable2,pVariable3,pVariable4);
	}
	
	@RequestMapping(path = "/obtenerCtasExtracto", method = RequestMethod.GET)
	public Resultado f_obtener_ctas_fc_extracto(
			@RequestParam(name = "tipo", required = true, defaultValue = "null") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.f_obtener_ctas_fc_extracto(pVariable1);
	}
	
	@GetMapping("/reporteTramite")
	public Resultado reporteTramiteExcel(HttpServletRequest datosEntrada) {
		
		Resultado resultado = new Resultado();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date v_fecha_inicio=null, v_fecha_final=null;
		try {
			v_fecha_inicio = dateFormat.parse(datosEntrada.getParameter("fechaInicio"));
			v_fecha_final = dateFormat.parse(datosEntrada.getParameter("fechaFinal"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			resultado.correcto = false;
			resultado.notificacion = "No se pudo completar la operación." ;
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e1.getMessage();
		}
		String login = datosEntrada.getParameter("login");
		String idUnidad =  datosEntrada.getParameter("idUnidad");
		
		String estado = datosEntrada.getParameter("estado");
		Integer idTipo = Integer.parseInt( datosEntrada.getParameter("idTipo"));
		
		try {
		List<FReporteTramites> respReporte = (List<FReporteTramites>) tramites.f_reporte_tramites(login, v_fecha_inicio, v_fecha_final,idUnidad,estado,idTipo);
		
		resultado.correcto = true;
		resultado.notificacion = "La consulta se realizó exitosamente";
		resultado.codigoResultado = 200;
		resultado.datoAdicional = respReporte;
		
		}catch (Exception e) {
			resultado.correcto = false;
			resultado.notificacion = "No se pudo completar la operación." ;
			resultado.codigoResultado = 400;
			resultado.datoAdicional = "Error:" + e.getMessage();
		}

		return resultado;
		
	}
	
	
	@RequestMapping(path = "/obtenerUnidadesUsuario", method = RequestMethod.GET)
	public Resultado f_obtener_ctas_fc_extracto(
			@RequestParam(name = "login", required = true, defaultValue = "null") String pVariable1)
			throws JsonProcessingException, SQLException {
		return tramites.f_obtener_unidades_por_usuario(pVariable1);
	}
	

}
