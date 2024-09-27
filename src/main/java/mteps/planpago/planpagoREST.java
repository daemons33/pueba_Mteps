package mteps.planpago;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.node.ObjectNode;



import mteps.planpago.entity.consultaEstado;
import mteps.planpago.entity.datosReporte;
import mteps.planpago.entity.infoDoc;
import mteps.planpago.entity.postDeposito;
import mteps.config.Resultado;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;

import net.sf.jasperreports.engine.xml.JRXmlLoader;

@RestController
@RequestMapping("/planpago")
public class planpagoREST {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PlanPagoCore planpagocore;

	////////////////////////////////////////////////// SUBIR ARCHIVOS

/////////////////////////////////////////// SERVICIO 1 AGETIC Consulta depositos

	@RequestMapping(path = "/serviciodeposito", method = RequestMethod.POST)
	public Resultado ObtenerDepositoServicioAgetic(@RequestBody postDeposito vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return planpagocore.obtenerDatosDepositoAgetic(vObjEntradaDatos);
	}

/////////////////////////////////////////// SERVICIO 2 AGETIC Consulta empresas

	@RequestMapping(path = "/servicioempresa", method = RequestMethod.GET)
	public Resultado ObtenerEmpresaServicioAgetic(
			@RequestParam(name = "nit", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "matriculaComercio", required = false, defaultValue = "") String pVariable2)
			throws JsonProcessingException {
		return planpagocore.obtenerDatosEmpresaAgetic2(pVariable1, pVariable2);
	}

	/////////////////////////////////////////// SERVICIO 2 AGETIC Consulta empresas
	/////////////////////////////////////////// 2
	@RequestMapping(path = "/servicioempresalista", method = RequestMethod.GET)
	public Resultado ObtenerEmpresaServicioAgeticlista() throws JsonProcessingException {
		return planpagocore.obtenerDatosEmpresaAgeticlista();
	}

	@RequestMapping(path = "/servicioempresalista/{pVariable1}", method = RequestMethod.GET)
	public Resultado ObtenerEmpresaServicioAgeticlista1(@PathVariable String pVariable1)
			throws JsonProcessingException {
		return planpagocore.obtenerDatosEmpresaAgeticlista1(pVariable1);
	}

	@RequestMapping(path = "/servicioempresalista/{pVariable1}" + "/" + "{pVariable2}", method = RequestMethod.GET)
	public Resultado ObtenerEmpresaServicioAgeticlista2(@PathVariable String pVariable1,
			@PathVariable String pVariable2) throws JsonProcessingException {
		return planpagocore.obtenerDatosEmpresaAgeticlista2(pVariable1, pVariable2);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// SERVICIO 3 AGETIC PLANILLA ANTERIOR

	@RequestMapping(path = "/planillaanterior", method = RequestMethod.GET)

	public Resultado ObtenerPlanillaAnteriorAgetic(
			@RequestParam(name = "nit", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "matriculaComercio", required = false, defaultValue = "") String pVariable2,
			@RequestParam(name = "periodo", required = true, defaultValue = "null") String pVariable3,
			@RequestParam(name = "gestion", required = true, defaultValue = "0") Number pVariable4,
			@RequestParam(name = "codigoSucursal", required = false, defaultValue = "-1") Integer pVariable5) {
		return planpagocore.obtenerPlanillaAnteriorAgetic(pVariable1, pVariable2, pVariable3, pVariable4, pVariable5);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER CALCULO MULTA
	@RequestMapping(path = "/calculomulta", method = RequestMethod.GET)
	public Resultado obtenerCalculoMulta(@RequestParam(name = "periodo", required = true) String pVariable1,
			@RequestParam(name = "gestion", required = true) Integer pVariable2,
			@RequestParam(name = "montoTotalGanado", required = true) BigDecimal pVariable3,
			@RequestParam(name = "casoLp", required = true) Boolean pVariable4) throws JsonProcessingException {
		return planpagocore.obtenerCalculoMulta(pVariable1, pVariable2, pVariable3, pVariable4);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////HABILITAR PLANILLA
	@RequestMapping(path = "/habilitarplanilla", method = RequestMethod.GET)
	public Resultado habilitarPlanilla(
			@RequestParam(name = "idplanpago", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, ValidationException {
		return planpagocore.habilitarPlanilla(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA PLAN PAGO

	@RequestMapping(path = "/listaplanpago", method = RequestMethod.POST)
	public Resultado obtenerListaPlanPago(@RequestBody consultaEstado vObjEntradaDatos) throws JsonProcessingException {
		return planpagocore.listaPlanPago(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA DETALLE PLAN PAGO

	@RequestMapping(path = "/listadetalleplanpago", method = RequestMethod.GET)
	public Resultado obtenerListaDetallePlanPago(@RequestParam(name = "idplanpago", required = true) Integer pVariable1)
			throws JsonProcessingException {
		return planpagocore.listaDetallePlanPago(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////// GESTION PLAN PAGOS
	@RequestMapping(path = "/gestionplanpago", method = RequestMethod.POST)
	public Resultado gestionPlanPagos(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return planpagocore.gestionPlanPagos(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION PLAN PAGOS
	@RequestMapping(path = "/gestiondetalleplanpago", method = RequestMethod.POST)
	public Resultado gestionDetallePlanPagos(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return planpagocore.gestionDetallePlanPagos(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION WORK FLOW OBTENER LISTA TRANSACCIONES
	@RequestMapping(path = "/obtenerlistatransacciones", method = RequestMethod.GET)
	public Resultado obtenerListaTransacciones(@RequestParam(name = "login", required = true) String pVariable1,
			@RequestParam(name = "tabla", required = true) String pVariable2,
			@RequestParam(name = "estado", required = true) String pVariable3,
			@RequestParam(name = "modulo", required = false, defaultValue = "") String pVariable4)
		
			throws JsonProcessingException, SQLException {
		return planpagocore.obtenerListaTransacciones(pVariable1, pVariable2, pVariable3, pVariable4);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION WORK FLOW OBTENER LISTA TRANSACCIONES
	@RequestMapping(path = "/obtenermenuporusuario", method = RequestMethod.GET)
	public Resultado MenuPorUsuario(@RequestParam(name = "login", required = true) String pVariable1)
			throws JsonProcessingException, SQLException {
		return planpagocore.obtenerMenuPorUsuario(pVariable1);
	}

/////////////////////////////////////////// SERVICIO 
	////////////////////////////// PLANILLA PENDIENTE EMPRESA

	@RequestMapping(path = "/planillapendienteempresa", method = RequestMethod.GET)
	public Resultado obtenerPlanillaPendiente(
			@RequestParam(name = "nit", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "matriculaComercio", required = false, defaultValue = "") String pVariable2)
			throws JsonProcessingException {
		return planpagocore.obtenerPlanillaPendiente(pVariable1, pVariable2);
	}

/////////////////////////////////////////// SERVICIO 
////////////////////////////// PLANILLA PENDIENTE EMPRESA

	@RequestMapping(path = "/habilitarplanillas", method = RequestMethod.GET)
	public Resultado habilitarPlanillas() throws JsonProcessingException {
		return planpagocore.habilitarPlanillas();
	}

/////////////////////////////////////////// SERVICIO 
////////////////////////////////////REPORTE DE INCUMPLIMIENTO

	@RequestMapping(path = "/reporteincumplimiento", method = RequestMethod.GET)
	public Resultado reporteIncumplimiento(
			@RequestParam(name = "idplanpago", required = true, defaultValue = "null") Integer pVariable1)
			throws JsonProcessingException {
		return planpagocore.reporteIncumplimiento(pVariable1);
	}

/////////////////////////////////////////// SERVICIO 
////////////////////////////////////obtener dato ususario

	@RequestMapping(path = "/obtenerdatosusuario", method = RequestMethod.GET)
	public Resultado obtenerDatosUsuario(
			@RequestParam(name = "login", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return planpagocore.obtenerDatosUsuario(pVariable1);
	}
/////////////////////////////////////////// SERVICIO 
////////////////////////////// DESCARGAR DOCUMENTO

	@RequestMapping(path = "/reporte/convenios", method = RequestMethod.GET)
	public void convenioWORDPOI(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException {

		String fileName = "convenio-empresa.docx";

		XWPFDocument doc = new XWPFDocument(OPCPackage.open("WEB-INF/modelo-convenio.docx"));
		for (XWPFParagraph p : doc.getParagraphs()) {
			for (XWPFRun run : p.getRuns()) {
				String text = run.getText(0);
				if (text != null) {
					text = text.replace("prueba", "haystack");
				}
				run.setText(text, 0);
			}

		}

		doc.write(new FileOutputStream(new File("WEB-INF/output.docx")));

		String rutaConvenio = "WEB-INF/output.docx";
		InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
		response.setContentType("application/x-docx");
		response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();

	}

	//////////////////

	@RequestMapping(path = "/reporte/convenio", method = RequestMethod.GET)
	public Resultado convenioWORDPOIs(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException {
		Resultado vObjResultado = new Resultado();
		String fileName = "convenio-empresa.docx";
		try {/*
				 * XWPFDocument doc = new
				 * XWPFDocument(OPCPackage.open("/home/mteps/descargas/modelo-convenio.docx"));
				 * for (XWPFParagraph p : doc.getParagraphs()) { for (XWPFRun run : p.getRuns())
				 * { String text = run.getText(0); if (text != null) { text =
				 * text.replace("prueba", "haystack"); } run.setText(text, 0); }
				 * 
				 * }
				 * 
				 * doc.write(new FileOutputStream(new
				 * File("/home/mteps/Descargas/output.docx")));
				 */
			/////   DESARROLLO
			//String rutaConvenio = "/home/mteps/Descargas/modelo-convenio.docx";
			//////// PRODUCCION
			String rutaConvenio = "/home/mteps/RepoPlanPago/modelo-convenio.docx";
			
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error " + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	///////////////////////////////////////////////////////////////////
	//////////// //////////////////REPORTE

	@RequestMapping(path = "/reporte/pdf", method = RequestMethod.GET)
	public @ResponseBody void reportePDF(HttpServletResponse response,
			@RequestParam(name = "idplanpago", required = true, defaultValue = "null") Integer pVariable1,
			@RequestParam(name = "tipo", required = true, defaultValue = "") String pVariable2)
			throws JRException, IOException, ScriptException, SQLException {
		String archivo = "";

		if (pVariable2.equals("seguimiento")) {
			archivo = "/WEB-INF/reporteSeguimiento.jrxml";

		} else if (pVariable2.equals("planpago")) {
			archivo = "/WEB-INF/reportePlanPago.jrxml";
		}

		StoredProcedureQuery procedureEmpresa = entityManager
				.createNamedStoredProcedureQuery("mteps_pagos.f_reporte_incumplimiento");
		procedureEmpresa.setParameter("p_id_plan_pago", pVariable1);

		List<mteps.planpago.entity.reporteIncumplimiento> result = procedureEmpresa.getResultList();
		mteps.planpago.entity.reporteIncumplimiento res = (mteps.planpago.entity.reporteIncumplimiento) result
				.get(0);
		datosReporte dRepo = new datosReporte();
		dRepo.sumaMulta = 0.0;
		dRepo.sumaAcuenta = 0.0;
		dRepo.sumaSaldo = 0.0;
		int cuotasIncumplidas = 0;
		int cuotasCumplidas = 0;
		String estado = "";

		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				mteps.planpago.entity.reporteIncumplimiento ress = result.get(i);
				dRepo.sumaMulta = dRepo.sumaMulta + ress.dplMonto;
				dRepo.sumaAcuenta = dRepo.sumaAcuenta + ress.dplAcuenta;

				estado = ress.estadoDet;
				if (estado.equals("EN_MORA")) {
					cuotasIncumplidas++;
					dRepo.sumaSaldo = dRepo.sumaSaldo + ress.dplSaldo;
				}
				if (estado.equals("PAGADO")) {
					cuotasCumplidas++;
					dRepo.ultimoPago = ress.fechaPagoCuota;
				}
				if (estado.equals("VIGENTE") || estado.equals("EN_MORA")) {

					dRepo.sumaPorPagar = dRepo.sumaPorPagar + ress.dplSaldo;
				}

			}

		}

		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
		// FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(res));

		parameterMap.put("detalleplanpago", new JRBeanCollectionDataSource(result));
		parameterMap.put("sumaMonto", dRepo.sumaMulta);
		parameterMap.put("sumaAcuenta", dRepo.sumaAcuenta);
		parameterMap.put("sumaSaldo", dRepo.sumaSaldo);
		parameterMap.put("cuotasIncumplidas", cuotasIncumplidas);
		parameterMap.put("cuotasCumplidas", cuotasCumplidas);
		parameterMap.put("ultimopago", dRepo.ultimoPago);
		parameterMap.put("sumaPorPagar", dRepo.sumaPorPagar);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/x-pdf");
		response.setHeader("Content-Disposition", "inLine; filename=ReportePDF.pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
	}


	
	
	///////////////////////////////////
///		GUARDAR DOC opc 2
/*	@Autowired
	private UploadFileService uploadFileService;

	@PostMapping("/cargardocumento")
	public Resultado uploadFile(@RequestParam("file") MultipartFile file) {
		Resultado vObjResultado = new Resultado();

		vObjResultado.datoAdicional = null;
		if (file.isEmpty()) {
			vObjResultado.notificacion = "Cargar archivo";
			vObjResultado.codigoResultado = 400;
		}

		try {
			uploadFileService.saveFile(file);
			vObjResultado.notificacion = "Se subio correctamente";
			vObjResultado.correcto = true;
		} catch (IOException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
		}

		return vObjResultado;
	}
*/
/////////////////////////////////////////// SERVICIO 
////////////////////////////////////guardar base64

	@RequestMapping(path = "/guardardoc", method = RequestMethod.POST)
	public Resultado guardarDocumento(@RequestBody infoDoc pVariable1) throws IOException {
		return planpagocore.guardarDocumento(pVariable1);
	}

}
