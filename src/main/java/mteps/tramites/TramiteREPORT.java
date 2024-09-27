package mteps.tramites;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.ConfigCORE;
import mteps.ovt.entity.EntidadEmpresa;
import mteps.ovt.repository.EmpresasInterface;
import mteps.poa.entity.F_ejecucion_seguimiento_fisico_presupuestario;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import mteps.tramites.entity.FReporteTramites;
import mteps.tramites.entity.ListaTramites;
import mteps.tramites.entity.ObjetoEntradaDatos;
import mteps.tramites.entity.obtenerTramite;
//import net.glxn.qrgen.core.image.ImageType;
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
@RequestMapping("/tramites/reporte")
public class TramiteREPORT {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	ConfigCORE configCore;

	@Autowired
	ConfigReport repo;

	@Autowired
	tramiteCORE tramiteCore;
	
	@Autowired
	EmpresasInterface empresasInterface;
	
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - COMPROBANTE
	@RequestMapping(path = "/comprobante", method = RequestMethod.GET)
	public @ResponseBody void reporteComprobantePDF(HttpServletResponse response,
			@RequestParam(name = "idtramite", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "null") String pVariable2)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "";

		archivo = "/WEB-INF/tramites/Reporte_Compobante.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_id");

		procedure.setParameter("id", pVariable1);

		obtenerTramite result = (obtenerTramite) procedure.getSingleResult();
///////////////////////////////////////////////////////////////////////////

		Boolean mostrarCodigoVisado = (result.idClasificadorTramite == 173 || result.idClasificadorTramite == 174
				|| result.idClasificadorTramite == 181) ? true : false;


		EntidadEmpresa empresa = new EntidadEmpresa();
		empresa = empresasInterface.obtenerEmpresa(result.getIdEmpresa());
		
		
		if (empresa ==null) {
			empresa = new EntidadEmpresa();
			empresa.nit=result.nit;
			empresa.razonSocial=result.razonSocial;
			empresa.nroPatronal="";
		}

		if (result.getPersonaNatural() || result.idClasificadorTramite == 181) {
			empresa.setNit(result.nit);
			empresa.setRazonSocial(result.razonSocial);
			empresa.setNroPatronal("");
		}

		String montoLit = configCore.Convertir(Double.toString(result.montoDeposito), false);
		montoLit = montoLit.substring(0, 1).toUpperCase() + montoLit.substring(1);

		// DESARROLLO

		if (result.getIdClasificadorTramite() == 190) {
			result.setObservacion("# Hojas legalizadas: " + result.getNroHojasLeg() + "\n" + ", Documento legalizado: "
					+ result.getDocumentoLeg());
		}

		if (result.getIdClasificadorTramite() == 200) {
			result.setTramite(result.getTramite() + "\n"
					+ (result.getPersonaNatural() ? "- Persona Natural" : "- Persona Jurídica"));
			result.setObservacion("Nro boleta: " + result.getNroBoletaErr() + ", Fecha: "
					+ configCore.generarFecha(result.getFechaBoletaErr(), 8) + ", Monto: " + result.getMontoErr());
		}
		// DESARROLLO
		result.setDescripcionSucursal(result.getDescripcionSucursal() != null ? result.getDescripcionSucursal() : "1");
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
// FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("usuario", pVariable2);
		parameterMap.put("razonSocial", empresa.getRazonSocial());
		parameterMap.put("nit", empresa.getNit());
		parameterMap.put("nroPatronal", empresa.getNroPatronal());
		parameterMap.put("montoLiteral", montoLit);
		parameterMap.put("codigoVisado", result.getCodigoVisado() != null ? result.getCodigoVisado() : "");
		parameterMap.put("mostrarCodigoVisado", mostrarCodigoVisado);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=[COMPROBANTE] - " + result.codigoTramite + ".pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - LISTA DE TRAMITES
	@RequestMapping(path = "/listatramites", method = RequestMethod.GET)
	public @ResponseBody void reporteListaTramites(HttpServletResponse response,
			@RequestParam(name = "estado", required = true, defaultValue = "null") String pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "null") String pVariable2)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		ObjetoEntradaDatos objetoED = new ObjetoEntradaDatos();
		objetoED.estado = pVariable1;
		objetoED.login = pVariable2;

		String archivo = "";

		archivo = "/WEB-INF/tramites/ListadoGenerall.jrxml";

		StoredProcedureQuery procedureEmpresa = entityManager
				.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramites");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objetoED);
		procedureEmpresa.setParameter("p_json_pp", json);

		List<ListaTramites> result = procedureEmpresa.getResultList();
		ListaTramites res = result.get(0);
///////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(res));

		parameterMap.put("usuario", pVariable2);
		parameterMap.put("listaTramites", new JRBeanCollectionDataSource(result));

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=Lista_tramites.pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}

	@RequestMapping(path = "/hojaRuta", method = RequestMethod.GET)
	public void reportehojaRuta0(
			@RequestParam(name = "idTramite", required = true, defaultValue = "null") Integer pVariable1,
			HttpServletResponse response)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		try {

			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_id");

			procedureTramite.setParameter("id", pVariable1);

			obtenerTramite resultado = (obtenerTramite) procedureTramite.getSingleResult();

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("code", resultado.nurSigec);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<byte[]> responseEntity = restTemplate.exchange("https://sigec.mteps.gob.bo/servicio/",
					HttpMethod.POST, entity, byte[].class);

			response.setHeader("Content-Disposition", "inline; filename=\"[" + resultado.nurSigec + "].pdf\"");
			response.setContentType("application/pdf");

			ServletOutputStream outputStream = response.getOutputStream();
			byte[] responseBytes = responseEntity.getBody();
			outputStream.write(responseBytes, 0, responseBytes.length);
			outputStream.flush();
		} catch (HttpStatusCodeException e) {
			HttpStatus statusCode = e.getStatusCode();
			response.setHeader("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + statusCode.getReasonPhrase();
			response.setStatus(statusCode.value());
			response.getWriter().write(errorMessage);
		} catch (Exception e) {
			response.setHeader("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + e.getMessage();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(errorMessage);
		}
	}

	@RequestMapping(path = "/hojaRuta2", method = RequestMethod.GET)
	public void reportehojaRuta2(@RequestParam(name = "hr", required = true, defaultValue = "null") String pVariable1,
			HttpServletResponse response)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("code", pVariable1);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			ResponseEntity<byte[]> responseEntity = restTemplate.exchange("https://sigec.mteps.gob.bo/servicio/",
					HttpMethod.POST, entity, byte[].class);

			response.setHeader("Content-Disposition", "inline; filename=\"[" + pVariable1 + "].pdf\"");
			response.setContentType("application/pdf");

			ServletOutputStream outputStream = response.getOutputStream();
			byte[] responseBytes = responseEntity.getBody();
			outputStream.write(responseBytes, 0, responseBytes.length);
			outputStream.flush();
		} catch (HttpStatusCodeException e) {
			HttpStatus statusCode = e.getStatusCode();
			response.setHeader("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + statusCode.getReasonPhrase();
			response.setStatus(statusCode.value());
			response.getWriter().write(errorMessage);
		} catch (Exception e) {
			response.setHeader("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + e.getMessage();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(errorMessage);
		}
	}

	@GetMapping("/generaQRenWord")
	public void generaQRenWord(@RequestParam String text, HttpServletResponse response)
			throws IOException, InvalidFormatException {
		// Genera el código QR
		byte[] qrCodeBytes = configCore.generarCodigoQR("Hola mundo");

		// Convierte los bytes de la imagen en BufferedImage
		BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));

		// Crea un documento Word
		XWPFDocument document = new XWPFDocument();
		XWPFRun run = document.createParagraph().createRun();

		// Agrega el código QR al documento Word
		ByteArrayOutputStream qrCodeOutputStream = new ByteArrayOutputStream();
		ImageIO.write(qrCodeImage, "png", qrCodeOutputStream);
		try (ByteArrayInputStream qrCodeStream = new ByteArrayInputStream(qrCodeOutputStream.toByteArray())) {

			XWPFPicture picture = run.addPicture(qrCodeStream, XWPFDocument.PICTURE_TYPE_PNG, "qr-code.png",
					Units.toEMU(300), Units.toEMU(300));
		}

		// Guarda el documento Word en la respuesta HTTP
		response.setContentType("application/msword");
		response.setHeader("Content-Disposition", "attachment; filename=qr-code-document.docx");
		document.write(response.getOutputStream());
	}

	@GetMapping("/resolucionAdministrativa")
	public ResponseEntity<byte[]> resAdmElectronico(
			@RequestParam(name = "idTramite", required = true, defaultValue = "null") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		try {

			StoredProcedureQuery procedureTramite = entityManager
					.createNamedStoredProcedureQuery("mteps_tramites.f_obtener_tramite_id");

			procedureTramite.setParameter("id", pVariable1);

			obtenerTramite resultado = (obtenerTramite) procedureTramite.getSingleResult();

			// Ruta del documento Word en el proyecto
			String rutaDocumentoWord = "";
			if (resultado.getIdClasificadorTramite() == 178) {

				rutaDocumentoWord = "WEB-INF/tramites/R_A_ELECTRONICOS.docx";

			} else {
				rutaDocumentoWord = "WEB-INF/tramites/R_A_LIBRO_DE_ASISTENCIA.docx";
			}
			String[] datos = { resultado.getNurSigec(), configCore.generarFecha(resultado.getFechaResAdm(), 2),
					resultado.getRazonSocial(), resultado.getNombreUsuarioCreacion() + ",",
					resultado.getDescripcionSucursal() + ".", resultado.getCiteRa(), (resultado.getCodigoVisado()!=null?resultado.getCodigoVisado():"") };

			byte[] documentoModificado = escribirDocumento(rutaDocumentoWord, datos);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "[RES. ADM. " + resultado.getCiteRa() + "].docx");

			return new ResponseEntity<>(documentoModificado, headers, HttpStatus.OK);

		} catch (HttpStatusCodeException e) {
			HttpStatus statusCode = e.getStatusCode();
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + statusCode.getReasonPhrase();
			return new ResponseEntity<>(errorMessage.getBytes(), responseHeaders, statusCode);
		} catch (Exception e) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			String errorMessage = "Error al obtener el archivo PDF: " + e.getMessage();
			return new ResponseEntity<>(errorMessage.getBytes(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public byte[] escribirDocumento(String rutaDocumentoWord, String[] datos) throws IOException {
		// String rutaDocumentoWord = "WEB-INF/tramites/R_A_ELECTRONICOS.docx";

		// Cargar el documento Word desde el recurso de clase
		InputStream inputStream = new ClassPathResource(rutaDocumentoWord).getInputStream();
		XWPFDocument doc = new XWPFDocument(inputStream);
		String var = "";
		// Modificar el texto en los párrafos
		for (XWPFParagraph p : doc.getParagraphs()) {
			for (XWPFRun run : p.getRuns()) {
				String text = run.getText(0);
				if (text != null) {
					for (int i = 0; i < datos.length; i++) {
						var = "{" + i + "}";
						;
						text = text.replace(var, datos[i]);
					}

				}
				run.setText(text, 0);
			}
		}

		// Convertir el documento modificado a bytes
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		doc.write(outputStream);
		byte[] documentoModificado = outputStream.toByteArray();

		return documentoModificado;
	}

/////////////////////// REPORTE DE TRÁMITES
	@GetMapping("/reporteTramiteExcel")
	public ResponseEntity<byte[]> reporteTramiteExcel(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException,
			ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date v_fecha_inicio = dateFormat.parse(datosEntrada.getParameter("fechaInicio")),
				v_fecha_final = dateFormat.parse(datosEntrada.getParameter("fechaFinal"));

		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		String fechaInicioLiteral = dateFormat2.format(v_fecha_inicio),
				fechaFinalLiteral = dateFormat2.format(v_fecha_final);

		String login = datosEntrada.getParameter("login");
		String idUnidad =  datosEntrada.getParameter("idUnidad");
		String estado = datosEntrada.getParameter("estado");
		Integer idTipo = Integer.parseInt( datosEntrada.getParameter("idTipo"));
		
		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<FReporteTramites> resultado = (List<FReporteTramites>) tramiteCore.f_reporte_tramites(login, v_fecha_inicio, v_fecha_final,idUnidad,estado,idTipo);

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "REPORTE DE TRÁMITES");

		Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 1200, 3600,3000, 4500, 8500, 12700,10000, 6200, 3200, 11200, 8000, 3200, 3200, 3200, 3200 , 3200};

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
		celda.setCellValue("REPORTE DE TRÁMITES");
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

		String tituloTabla[] = { "Nº", "Código de trámite", "Hoja de ruta", "Fecha de registro" , "Nombre de usuario","Cargo", "Unidad organizacional", "Tipo de trámite","NIT","Razón social","Observaciones","Estado","Costo de trámite","Fecha de operación","Nro. Operación","Monto de operación"
				 };

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}

		int col = 0;


		Integer filaInicial = 0,filaFinal=0,filaInicialPago=0;
///////////////////////////// DATOS TABLA	

		if (!resultado.isEmpty()) {
			for (int i = 0; i < resultado.size(); i++) {
				
					col = 0;
					
					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(i+1);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).codigoTramite);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).nurSigec);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( configCore.formatoFecha(resultado.get(i).fechaCreacion,true));

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).nombreUsuarioCreacion);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).cargo);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).unidadFuncional);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).tipoTramite);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).nit);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).razonSocial);
					
					if(resultado.get(i).datosPersona != null) {
						resultado.get(i).observacion = resultado.get(i).datosPersona.nombre_completo+ ", C.I.:"+ resultado.get(i).datosPersona.nro_documento+"\nDirección: "+ (resultado.get(i).datosPersona.domicilio!=null?resultado.get(i).datosPersona.domicilio:"");
					}
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).observacion);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue( resultado.get(i).estado);
					
					
					if(resultado.get(i).datosPagos !=null) {
						
				    filaInicial=filaNum-1;
				    filaFinal=0;
				    
					for(int j = 0; j<resultado.get(i).datosPagos.size();j++) {
						
						filaInicialPago=filaNum-1;
						if(j>0) {row = (XSSFRow) hoja1.createRow(filaNum++); col= col-4;}
												
							celda = row.createCell(col++);
							celda.setCellStyle(estiloNumericoTablaDerecha);
							celda.setCellValue(resultado.get(i).datosPagos.get(j).costo);
													
						if(resultado.get(i).datosPagos.get(j).json_agg !=null) {
							
							
							
							for (int k=0; k<resultado.get(i).datosPagos.get(j).json_agg.size();k++) {
								
								if(k>0) {row = (XSSFRow) hoja1.createRow(filaNum++); col= col-3;}
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatosJustificado);
								celda.setCellValue(configCore.formatoFecha(resultado.get(i).datosPagos.get(j).json_agg.get(k).fecha_deposito,false));
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatosJustificado);
								celda.setCellValue(resultado.get(i).datosPagos.get(j).json_agg.get(k).nro_deposito);
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloNumericoTablaDerecha);
								celda.setCellValue(resultado.get(i).datosPagos.get(j).json_agg.get(k).monto);
								filaFinal++; 
							}
							
							if (resultado.get(i).datosPagos.get(j).json_agg.size()>1) {
								
								repo.regionConBorde(hoja1,
										new CellRangeAddress(filaInicialPago, filaInicialPago + resultado.get(i).datosPagos.get(j).json_agg.size() - 1, col-4, col-4));
							}
							
						}
						
							
					}
					
					}else {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue( "");
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue( "");
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue( "");
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue( "");
					}
					
					if(filaFinal>1) {
						
						for(int s=0;s<12;s++) {

						repo.regionConBorde(hoja1,
								new CellRangeAddress(filaInicial, filaInicial + filaFinal-1, s, s));
						
						}
						
					}
					
					filaFinal=0;
									
			}
		}


		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[REPORTE DE TRAMITES]_.xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

}
