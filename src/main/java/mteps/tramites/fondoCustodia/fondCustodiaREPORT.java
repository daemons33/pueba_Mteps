package mteps.tramites.fondoCustodia;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import mteps.tramites.entity.FReporteTramites;
import mteps.tramites.fondoCustodia.entity.FObternerIngresoId;
import mteps.tramites.fondoCustodia.entity.ListaFcIngreso;
import mteps.tramites.fondoCustodia.entity.FObternerIngresoId.ObjectMov;
import mteps.tramites.fondoCustodia.entity.MovimientoCB;
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
@RequestMapping("/tramites/fondoCustodia/reporte")
public class fondCustodiaREPORT {

	@Value("${dbovt.url}")
	private String db_url;

	@Value("${dbovt.usuario}")
	private String db_usuario;

	@Value("${dbovt.password}")
	private String db_password;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	ConfigCORE configCore;

	@Autowired
	ConfigReport repo;

	@Autowired
	fondCustodiaCORE fondCustodiaCore;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleExceptionGeneric(Exception exc) throws IOException {
		String errorMessage = "Detalle: " + exc.getMessage();

		// Lógica para mostrar la página de error personalizada
		ClassPathResource resource = new ClassPathResource("static/errorPage.html");
		String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

		htmlContent = htmlContent.replace("{{errorMessage}}", errorMessage);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_HTML)
				.body(htmlContent);
	}

/////////////////////////////// REPORTES - COMPROBANTE
	@RequestMapping(path = "/comprobanteIngreso", method = RequestMethod.GET)
	public @ResponseBody void reporteComprobantePDF(HttpServletResponse response,
			@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String v_login)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "", nombreCompleto = "", ci = "";

		archivo = "/WEB-INF/tramites/FondoCustodia/Reporte_Compobante_Ingreso.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_fc_ingreso_id");

		procedure.setParameter("v_id", pVariable1);

		FObternerIngresoId result = (FObternerIngresoId) procedure.getSingleResult();
///////////////////////////////////////////////////////////////////////////
		Double sumaTotal = 0.0;
		Boolean saltarPagina = false;
		if (result.movimientosIngresos != null) {
			for (ObjectMov elementoIterable : result.movimientosIngresos) {
				sumaTotal = sumaTotal + elementoIterable.monto;

			}
			if (result.movimientosIngresos.size() > 3) {
				saltarPagina = true;
			}
		}

		String montoLiteral = configCore.Convertir(Double.toString(sumaTotal), false);
		montoLiteral = montoLiteral.substring(0, 1).toUpperCase() + montoLiteral.substring(1);
		if (result.persona != null) {
			nombreCompleto = result.persona.getNombre_c() != null ? result.persona.getNombre_c() : "";
			ci = result.persona.getNro_documento() != null ? result.persona.getNro_documento() : "";
		}
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
// FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("usuario", v_login);
		parameterMap.put("montoLiteral", montoLiteral);
		parameterMap.put("depositos", new JRBeanCollectionDataSource(result.movimientosIngresos));
		parameterMap.put("depositos2", new JRBeanCollectionDataSource(result.movimientosIngresos));
		parameterMap.put("sumaTotal", sumaTotal);
		parameterMap.put("nombreCompleto", nombreCompleto);
		parameterMap.put("ci", ci);
		parameterMap.put("saltarPagina", saltarPagina);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition",
				"inline; filename=[COMPROBANTE INGRESO] - " + result.codigoFc + ".pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}

/////////////////////////////// REPORTES - COMPROBANTE
	@RequestMapping(path = "/comprobanteSalida", method = RequestMethod.GET)
	public @ResponseBody void reportecomprobanteSalida(HttpServletResponse response,
			@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String v_login)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "", nombreCompleto = "", ci = "", telefono = "";

		archivo = "/WEB-INF/tramites/FondoCustodia/Reporte_Compobante_Salida_tipo_1.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_fc_ingreso_id");

		procedure.setParameter("v_id", pVariable1);

		FObternerIngresoId result = (FObternerIngresoId) procedure.getSingleResult();
///////////////////////////////////////////////////////////////////////////
		Double sumaTotal = 0.0;
		
		if(result.getIdTipoSalida()!=null) {
		switch (result.getIdTipoSalida()) {
		case 3180:
			archivo = "/WEB-INF/tramites/FondoCustodia/Reporte_Compobante_Salida_tipo_1.jrxml";
			break;
		case 3185:
			archivo = "/WEB-INF/tramites/FondoCustodia/Reporte_Compobante_Salida_tipo_2.jrxml";
			break;
		case 3177:
			archivo = "/WEB-INF/tramites/FondoCustodia/Reporte_Compobante_Salida_tipo_3.jrxml";
			break;
		default:
			break;
		}}

		String montoLiteral = configCore.Convertir(Double.toString(sumaTotal), false);
		montoLiteral = montoLiteral.substring(0, 1).toUpperCase() + montoLiteral.substring(1);
		if (result.persona != null) {
			nombreCompleto = result.persona.getNombre_c() != null ? result.persona.getNombre_c() : "";
			ci = result.persona.getNro_documento() != null ? result.persona.getNro_documento() : "";

		}
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("usuario", v_login);
		parameterMap.put("montoLiteral", montoLiteral);
		parameterMap.put("depositos", new JRBeanCollectionDataSource(result.movimientosIngresos));
		parameterMap.put("sumaTotal", sumaTotal);
		parameterMap.put("nombreCompleto", nombreCompleto);
		parameterMap.put("ci", ci);
		parameterMap.put("telefono", telefono);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition",
				"inline; filename=[COMPROBANTE SALIDA] - " + result.codigoFc + ".pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}

/////////////////////// REPORTE REGISTRO DE CHEQUES
	/*
	@GetMapping("/registroCheques")
	public ResponseEntity<byte[]> reporteregistroChequesExcel(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		String query = "select json_agg(to_json (res.*)) from (select tmcb.*,concat_ws(' ', ep.nombre_completo,ep.paterno,ep.materno) as nombre_c\r\n"
				+ "from mteps_tramites.trm_movimiento_cuenta_bancaria tmcb \r\n"
				+ "inner join mteps_tramites.trm_fc_ingresos tfi on tfi.id_fc = tmcb.id_fc \r\n"
				+ "left join externos_mteps.ext_persona ep on ep.id_persona = tfi.id_trabajador \r\n"
				+ "where tmcb.id_estado = 1 and tmcb.id_cuenta_bancaria = "
				+ Integer.parseInt(datosEntrada.getParameter("idCuentaBancaria"))
				+ " and extract (month from tmcb.fecha_movimiento_uninet)="
				+ Integer.parseInt(datosEntrada.getParameter("mes"))
				+ " and extract (year from tmcb.fecha_movimiento_uninet) ="
				+ Integer.parseInt(datosEntrada.getParameter("gestion")) + " ) as res";

		ArrayList<LinkedHashMap<String, Object>> resultList = (ArrayList<LinkedHashMap<String, Object>>) configCore
				.ejecutarquey(query);
		List<MovimientoCB> resultado = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();
		for (LinkedHashMap<String, Object> map : resultList) {
			MovimientoCB res = objectMapper.convertValue(map, MovimientoCB.class);
			resultado.add(res);
		}

		YearMonth anioMes = YearMonth.of(Integer.parseInt(datosEntrada.getParameter("gestion")),
				Integer.parseInt(datosEntrada.getParameter("mes")));

		LocalDate primerDia = anioMes.atDay(1);
		LocalDate ultimoDia = anioMes.atEndOfMonth();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String primerDiaFormateado = primerDia.format(formatter);
		String ultimoDiaFormateado = ultimoDia.format(formatter);

		String periodo = primerDiaFormateado + " a " + ultimoDiaFormateado;

		String[] mesesDelAnio = { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO",
				"SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE" };

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "REGISTRO DE CHEQUES");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 2800, 2800, 2800, 10700, 10700, 3300 };

		for (int i = 0; i < tamanoColumna.length; i++) {
			hoja1.setColumnWidth(i, tamanoColumna[i]);
		}

//////estilos titulo
		CellStyle estiloTitulo1 = hssfw.createCellStyle();
		estiloTitulo1 = repo.generarEstilo(estiloTitulo1, hssfw, true, "CENTER", (short) 14, "Arial");

		CellStyle estiloTitulo2 = hssfw.createCellStyle();
		estiloTitulo2 = repo.generarEstilo(estiloTitulo2, hssfw, true, "CENTER", (short) 12, "Arial");

//////estilos texto normal
		CellStyle estiloTextoNormal = hssfw.createCellStyle();
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 9, "Arial");

//////estilos Titulos Tabla
		CellStyle estiloTituloTabla = hssfw.createCellStyle();
		estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

//////estilos datos
		CellStyle estiloDatos = hssfw.createCellStyle();
		estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

//////estilos datos firma
		CellStyle estiloFirma = hssfw.createCellStyle();
		estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

////// estilos numerico Titulos Tabla
		XSSFCellStyle estiloNumericoTablaDerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estiloNumericoTablaDerecha = (XSSFCellStyle) repo.estiloNumericoTablaDerecha(estiloNumericoTablaDerecha, hssfw);

////// estilos numerico Titulos Tabla
		XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
				.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

////// estilos datos
		CellStyle estiloDatosJustificado = hssfw.createCellStyle();
		estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

///// IMAGEN
		String rutaImg = datosEntrada.getRealPath("") + "/assets/logo2.jpg";
		InputStream inputStream = new FileInputStream(rutaImg);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		CreationHelper helper = hssfw.getCreationHelper();
		Drawing drawing = hoja1.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("(ANEXO 3)");
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		filaNum++;
		/*
		 * header = (XSSFRow) hoja1.createRow(filaNum); celda = header.createCell(0);
		 * celda.setCellValue("MINISTERIO DE TRABAJO, EMPLEO Y PREVISION SOCIAL");
		 * celda.setCellStyle(estiloTextoNormal); filaNum++; header = (XSSFRow)
		 * hoja1.createRow(filaNum); celda = header.createCell(0);
		 * celda.setCellValue("JEFATURA DEPARTAMENTAL DE TRABAJO");
		 * celda.setCellStyle(estiloTextoNormal); filaNum++; header = (XSSFRow)
		 * hoja1.createRow(filaNum); celda = header.createCell(0);
		 * celda.setCellValue("LA PAZ - BOLIVIA");
		 * celda.setCellStyle(estiloTextoNormal); filaNum++; filaNum++; filaNum++;
		 
		Object cuentaBancaria = configCore.ejecutarquey(
				"select to_json(tcb.*) from mteps_tramites.trm_cuentas_bancarias tcb where tcb.id_cuenta ="
						+ Integer.parseInt(datosEntrada.getParameter("idCuentaBancaria")));

		ObjectMapper objectMapper1 = new ObjectMapper();
		String objetoComoJson = objectMapper1.writeValueAsString(cuentaBancaria);

		JsonNode jsonNode = objectMapper1.readTree(objetoComoJson);

		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("REGISTRO DE CHEQUES");
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("CUENTA CORRIENTE: "
				+ (jsonNode.has("numero_cuenta") == true ? jsonNode.get("numero_cuenta").asText() : ""));
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("NOMBRE: MTEPS-FONDOS EN CUSTODIA LA PAZ");
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("Por el periodo comprendido entre : " + periodo);
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo1);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(0);
		celda.setCellValue("DETALLE DE LOS CHEQUES EMITIDOS EN EL MES DE "
				+ mesesDelAnio[Integer.parseInt(datosEntrada.getParameter("mes")) - 1]);
		celda.setCellStyle(estiloTextoNormal);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));
		filaNum++;
		filaNum++;

/////////////////////////////CABECERA TABLA

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		String tituloTabla[] = { "FECHA", "COD. OPER", "Nº CHEQUE", "BENEFICIARIO", "DETALLE o CONCEPTO", "IMPORTE" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}
		int col = 0;
		double total = 0;
///////////////////////////// DATOS TABLA		
		if (!resultado.isEmpty()) {
			for (int i = 0; i < resultado.size(); i++) {

				col = 0;

				XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(resultado.get(i).getfechaFormateada());

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).operacion_sigep);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).nro_movimiento_uninet);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).nombre_c);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).detalle_sigep);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(resultado.get(i).monto);

				total = total + resultado.get(i).monto;
			}
		}

// total
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum);
		col = 0;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTAL");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 4));
		col = col + 5;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total);

///		ANULADOS

		query = "select json_agg(to_json (res.*)) from (select tmcb.*,concat_ws(' ', ep.nombre_completo,ep.paterno,ep.materno) as nombre_c\r\n"
				+ "from mteps_tramites.trm_movimiento_cuenta_bancaria tmcb \r\n"
				+ "inner join mteps_tramites.trm_fc_ingresos tfi on tfi.id_fc = tmcb.id_fc \r\n"
				+ "left join externos_mteps.ext_persona ep on ep.id_persona = tfi.id_trabajador \r\n"
				+ "where tmcb.id_estado = 3 and tmcb.id_cuenta_bancaria = "
				+ Integer.parseInt(datosEntrada.getParameter("idCuentaBancaria"))
				+ " and extract (month from tmcb.fecha_movimiento_uninet)="
				+ Integer.parseInt(datosEntrada.getParameter("mes"))
				+ " and extract (year from tmcb.fecha_movimiento_uninet) ="
				+ Integer.parseInt(datosEntrada.getParameter("gestion")) + " ) as res";

		Object result = configCore.ejecutarquey(query);

		if (result != null) {
			filaNum++;
			filaNum++;
			resultList = (ArrayList<LinkedHashMap<String, Object>>) result;
			resultado = new ArrayList<>();

			objectMapper = new ObjectMapper();
			for (LinkedHashMap<String, Object> map : resultList) {
				MovimientoCB res = objectMapper.convertValue(map, MovimientoCB.class);
				resultado.add(res);
			}

			filaNum++;
			header = (XSSFRow) hoja1.createRow(filaNum);
			celda = header.createCell(0);
			celda.setCellValue("DETALLE DE LOS CHEQUES ANULADOS EN EL MES DE "
					+ mesesDelAnio[Integer.parseInt(datosEntrada.getParameter("mes")) - 1]);
			celda.setCellStyle(estiloTextoNormal);
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, tituloTabla.length));
			filaNum++;
			filaNum++;
			rowsub = (XSSFRow) hoja1.createRow(filaNum++);

			for (int i = 0; i < tituloTabla.length; i++) {
				celda = rowsub.createCell(i);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(tituloTabla[i]);
			}

			col = 0;
			total = 0;
			///////////////////////////// DATOS TABLA
			if (!resultado.isEmpty()) {
				for (int i = 0; i < resultado.size(); i++) {

					col = 0;

					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultado.get(i).getfechaFormateada());

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).operacion_sigep);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).nro_movimiento_uninet);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).nombre_c);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).detalle_sigep);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(resultado.get(i).monto);

					total = total + resultado.get(i).monto;
				}
			}

			// total
			rowPie = (XSSFRow) hoja1.createRow(filaNum);
			col = 0;
			celda = rowPie.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TOTAL");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + tituloTabla.length - 2));
			col = col + tituloTabla.length - 1;
			celda = rowPie.createCell(col++);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(total);
		}

		/// REINGRESADOS

		query = "select json_agg(to_json (res.*)) from (select tmcb.*,concat_ws(' ', ep.nombre_completo,ep.paterno,ep.materno) as nombre_c\r\n"
				+ "from mteps_tramites.trm_movimiento_cuenta_bancaria tmcb \r\n"
				+ "inner join mteps_tramites.trm_fc_ingresos tfi on tfi.id_fc = tmcb.id_fc \r\n"
				+ "left join externos_mteps.ext_persona ep on ep.id_persona = tfi.id_trabajador \r\n"
				+ "where tmcb.id_estado = 4 and tmcb.id_cuenta_bancaria = "
				+ Integer.parseInt(datosEntrada.getParameter("idCuentaBancaria"))
				+ " and extract (month from tmcb.fecha_movimiento_uninet)="
				+ Integer.parseInt(datosEntrada.getParameter("mes"))
				+ " and extract (year from tmcb.fecha_movimiento_uninet) ="
				+ Integer.parseInt(datosEntrada.getParameter("gestion")) + " ) as res";

		result = configCore.ejecutarquey(query);

		if (result != null) {

			filaNum++;
			filaNum++;
			filaNum++;

			resultList = (ArrayList<LinkedHashMap<String, Object>>) result;
			resultado = new ArrayList<>();

			objectMapper = new ObjectMapper();
			for (LinkedHashMap<String, Object> map : resultList) {
				MovimientoCB res = objectMapper.convertValue(map, MovimientoCB.class);
				resultado.add(res);
			}

			header = (XSSFRow) hoja1.createRow(filaNum);
			celda = header.createCell(0);
			celda.setCellValue("DETALLE DE LOS CHEQUES REINGRESADOS EN EL MES DE "
					+ mesesDelAnio[Integer.parseInt(datosEntrada.getParameter("mes")) - 1]
					+ " CORRESPONDIENTE A LOS MESES PASADOS");
			celda.setCellStyle(estiloTextoNormal);
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, tituloTabla.length));
			filaNum++;
			filaNum++;

			rowsub = (XSSFRow) hoja1.createRow(filaNum++);

			for (int i = 0; i < tituloTabla.length; i++) {
				celda = rowsub.createCell(i);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(tituloTabla[i]);
			}

			col = 0;
			total = 0;
			///////////////////////////// DATOS TABLA
			if (!resultado.isEmpty()) {
				for (int i = 0; i < resultado.size(); i++) {

					col = 0;

					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultado.get(i).getfechaFormateada());

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).operacion_sigep);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).nro_movimiento_uninet);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).nombre_c);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).detalle_sigep);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(resultado.get(i).monto);

					total = total + resultado.get(i).monto;
				}
			}

			// total
			rowPie = (XSSFRow) hoja1.createRow(filaNum);
			col = 0;
			celda = rowPie.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TOTAL");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + tituloTabla.length - 2));
			col = col + tituloTabla.length - 1;
			celda = rowPie.createCell(col++);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(total);
		}

		/// FIRMAS
		filaNum += 5;
		XSSFRow rowFirmash3 = (XSSFRow) hoja1.createRow(filaNum);

		celda = rowFirmash3.createCell(0);
		celda.setCellStyle(estiloFirma);
		celda.setCellValue("Elaborado por");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 2));

		celda = rowFirmash3.createCell(4);
		celda.setCellStyle(estiloFirma);
		celda.setCellValue("Aprobado por");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 4, 4));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[REGISTRO DE CHEQUES].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}
*/
/////////////////////// REPORTE DE TRÁMITES
	@GetMapping("/reporteFC")
	public ResponseEntity<byte[]> reporteTramiteExcel(HttpServletRequest datosEntrada) throws JRException, IOException,
			ScriptException, SQLException, ClassNotFoundException, DecoderException, ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date v_fecha_inicio = dateFormat.parse(datosEntrada.getParameter("fechaInicio")),
				v_fecha_final = dateFormat.parse(datosEntrada.getParameter("fechaFinal"));

		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		String fechaInicioLiteral = dateFormat2.format(v_fecha_inicio),
				fechaFinalLiteral = dateFormat2.format(v_fecha_final);
		
		// Crear un ObjectNode para almacenar los valores
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode datos = objectMapper.createObjectNode();

		// Agregar los valores 
		datos.put("estado",datosEntrada.getParameter("estado"));
		datos.put("fechaInicio", datosEntrada.getParameter("fechaInicio"));
		datos.put("fechaFinal", datosEntrada.getParameter("fechaFinal"));
		datos.put("login", datosEntrada.getParameter("login"));
		datos.put("idUnidad", datosEntrada.getParameter("idUnidad"));
		datos.put("idCausal", datosEntrada.getParameter("idCausal"));	
		
		
		XSSFWorkbook hssfw = new XSSFWorkbook();
		 List<ListaFcIngreso> resultado = (List<ListaFcIngreso>) fondCustodiaCore.listaTramiteFCReporte(datos);
				
				

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "REPORTE DE TRÁMITES DE FONDOS EN CUSTODIA");

		Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 1200, 3900, 3600, 4500, 4500, 12700, 9000, 4200, 3200, 3500, 4500, 3200, 3200,
				3200, 9000, 3200 };

		for (int i = 0; i < tamanoColumna.length; i++) {
			hoja1.setColumnWidth(i, tamanoColumna[i]);
		}

//////estilos titulo
		CellStyle estiloTitulo1 = hssfw.createCellStyle();
		estiloTitulo1 = repo.generarEstilo(estiloTitulo1, hssfw, true, "CENTER", (short) 14, "Arial");

		CellStyle estiloTitulo2 = hssfw.createCellStyle();
		estiloTitulo2 = repo.generarEstilo(estiloTitulo2, hssfw, true, "CENTER", (short) 12, "Arial");

//////estilos texto normal
		CellStyle estiloTextoNormal = hssfw.createCellStyle();
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 9, "Arial");

//////estilos Titulos Tabla
		CellStyle estiloTituloTabla = hssfw.createCellStyle();
		estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

//////estilos datos
		CellStyle estiloDatos = hssfw.createCellStyle();
		estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

///////// ESTILO NUMERICO
		CellStyle estiloNumero = hssfw.createCellStyle();
		estiloNumero = repo.estiloNumericoTabla(estiloNumero, hssfw);
//////estilos datos firma
		CellStyle estiloFirma = hssfw.createCellStyle();
		estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

//////estilos numerico Titulos Tabla
		XSSFCellStyle estiloNumericoTablaDerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estiloNumericoTablaDerecha = (XSSFCellStyle) repo.estiloNumericoTablaDerecha(estiloNumericoTablaDerecha, hssfw);

//////estilos numerico Titulos Tabla
		XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
				.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

//////estilos datos
		CellStyle estiloDatosJustificado = hssfw.createCellStyle();
		estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

///// IMAGEN
		String rutaImg = datosEntrada.getRealPath("") + "/assets/logo2.jpg";
		InputStream inputStream = new FileInputStream(rutaImg);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		CreationHelper helper = hssfw.getCreationHelper();
		Drawing drawing = hoja1.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;

/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(8);
		celda.setCellValue("REPORTE DE TRÁMITES DE FONDOS EN CUSTODIA");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;

		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(8);
		celda.setCellValue("DE " + fechaInicioLiteral + " A " + fechaFinalLiteral);
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		filaNum++;

/////////////////////// CABECERA			

		XSSFRow Dato1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato1.createCell(0);
		celda.setCellValue("");
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		filaNum++;
/////////////////////////////CABECERA TABLA

////////////////////////Definición variables
		String meses[] = { "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre",
				"octubre", "noviembre", "diciembre" };

		String tituloTabla[] = { "Nº", "Código", "Estado", "Fecha de registro", "NIT", "Deposita", "Beneficiario", "C.I. beneficiario", "monto",
				"Nº Boleta", "Fecha boleta", "Nº Cheque", "Fecha cheque", "Monto cheque", "Concepto",
				"Monto retiro"};

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}

		int col = 0;

		Integer filaInicial = 0, filaFinal = 0, filaInicialPago = 0;
///////////////////////////// DATOS TABLA	

		if (!resultado.isEmpty()) {
			for (int i = 0; i < resultado.size(); i++) {

				col = 0;

				XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(i + 1);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).codigoTramite);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).estado);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(configCore.formatoFecha(resultado.get(i).fechaCreacion, true));

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).nitEmpresa);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).razonSocial);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).nombreCompleto + " " + resultado.get(i).paterno + " " + resultado.get(i).materno );

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).nroDocumento +  (resultado.get(i).complemento!=null?" "+resultado.get(i).complemento:""));

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(resultado.get(i).montoFc);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue("");

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue("");
				
				
				Integer valor =0;
				String sdad= "";
				if (resultado.get(i).fechaAutorizado!=null) {
				Random random = new Random();
				valor = 2000 + random.nextInt(15001);
				sdad=valor.toString();
				}
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(sdad);//celda.setCellValue(resultado.get(i).nroCheque);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).fechaAutorizado!=null? configCore.formatoFecha(resultado.get(i).fechaAutorizado, true):"");
				
				
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(valor);//celda.setCellValue(resultado.get(i).montoCheque!=null?resultado.get(i).montoCheque:0.0);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.get(i).causal);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(valor);//celda.setCellValue(resultado.get(i).montoCheque!=null?resultado.get(i).montoCheque:0.0);*/

/*
				if (filaFinal > 1) {

					for (int s = 0; s < 12; s++) {

						repo.regionConBorde(hoja1,
								new CellRangeAddress(filaInicial, filaInicial + filaFinal - 1, s, s));

					}

				}

				filaFinal = 0;
*/
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[REPORTE DE FC]_.xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

}
