package mteps.poa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import mteps.poa.entity.F_obtener_prog_ej_ptto;
import mteps.poa.entity.F_obtener_programado_ejecutado;
import mteps.poa.entity.F_programado_ejecutado;
import mteps.poa.entity.F_reporte_form_presupuesto_gasto;
import mteps.poa.entity.F_reporte_formulacion;
import mteps.poa.entity.F_reporte_te_seguimiento;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("sispoa/reporte")
public class PoaReporteSeguimiento {

	Map modelo = new HashMap();

	@Autowired
	PlanMap planMap;

	@Autowired
	ConfigReport repo;

	@Value("${directorio.archivos}")
	public String dir_archivos;

	@Value("${directorio.enlace}")
	public String dir_enlace;
	
	@Value("${ruta.archivos}")
    private String rutaprincipal;
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@GetMapping("/descargarArchivo/{fileName:.+}")
	public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) throws IOException {
		
		
		File file = new File(rutaprincipal +"/archivosBackEndMTEPS/"+ fileName);
		Path path = Paths.get(file.getAbsolutePath());
		
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
		return ResponseEntity.ok()
				.headers(header)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}
//////// SEGUIMIENTO DE EJECUCIÓN POA	
	@GetMapping("/seguimientoEjecucionPOA")
	public ModelAndView reporteSeguimientoEjecucionPOA(HttpServletRequest datosEntrada) throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_form_presupuesto_gasto");

		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));
		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		F_reporte_form_presupuesto_gasto resultadoDatos = (F_reporte_form_presupuesto_gasto) procedure
				.getSingleResult();

		modelo.put("registros", resultadoDatos);

		return new ModelAndView(new PoaReporteSeguimiento.reporteSeguimientoEjecucionPOAExcel(), modelo);

	}

	public class reporteSeguimientoEjecucionPOAExcel extends AbstractXlsxView {

		@Override
		public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
				HttpServletResponse hsr1) throws Exception {

			//// CONFIG ESTILO

			XSSFCellStyle Titulo = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|
			Titulo = (XSSFCellStyle) repo.generaEstilo(Titulo, hssfw, true, "LEFT", (short) 10, "Arial", "BLACK", false,
					"", false, "", false, "", false);

			XSSFCellStyle TituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			TituloTabla = (XSSFCellStyle) repo.generaEstilo(TituloTabla, hssfw, true, "CENTER", (short) 9, "Arial",
					"WHITE", true, "C00000", true, "WHITE", false, "", true);

			XSSFCellStyle datoTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			datoTabla = (XSSFCellStyle) repo.generaEstilo(datoTabla, hssfw, false, "CENTER", (short) 9, "Arial",
					"BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

			XSSFCellStyle caracterRefe = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|
			caracterRefe = (XSSFCellStyle) repo.generaEstilo(caracterRefe, hssfw, false, "LEFT", (short) 7, "Arial",
					"BLACK", false, "", false, "", false, "", false);

			//// Fin

			/// HOJAS
			String[][] titulosHojas = {
					{ "SEPOA - ACP", "SEGUIMIENTO A LA EJECUCION DEL POA - ACCIONES DE CORTO PLAZO Y PRESUPUESTO",
							"FORMULARIO Nº 7" },
					{ "SEPOA - OPE", "SEGUIMIENTO A LA EJECUCION DEL POA - OPERACIONES", "FORMULARIO Nº 8" },
					{ "SEPOA - ACT", "SEGUIMIENTO A LA EJECUCION DEL POA - ACTIVIDADES", "FORMULARIO Nº 9" },
					{ "SEPOA - TE", "SEGUIMIENTO A LA EJECUCION DEL POA - TAREAS ESPECIFICAS", "FORMULARIO Nº 10" } };

			for (int nHoja = 0; nHoja < titulosHojas.length; nHoja++) {
				
				Sheet hoja = hssfw.createSheet();
				hssfw.setSheetName(nHoja, titulosHojas[nHoja][0]);
				org.apache.poi.ss.usermodel.Cell celda;
				int filaNum = 0, col = 0;
				////// Tamaño de columnas
				Integer tamanoColumna[] = { 4000, 6000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000 };

				for (int i = 0; i < tamanoColumna.length; i++) {
					hoja.setColumnWidth(i, tamanoColumna[i]);
				}

				XSSFRow header = (XSSFRow) hoja.createRow(filaNum++);
				celda = header.createCell(col++);
				celda.setCellValue(titulosHojas[nHoja][2]);
				celda.setCellStyle(Titulo);
				filaNum++;
				XSSFRow titulo = (XSSFRow) hoja.createRow(filaNum++);
				celda = titulo.createCell(col);
				celda.setCellValue(titulosHojas[nHoja][1]);
				celda.setCellStyle(Titulo);
				filaNum++;
				col = 0;
				XSSFRow tablaEncabezado = (XSSFRow) hoja.createRow(filaNum++);
				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("Unidad Organizacional Responsable");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col);
				celda.setCellValue("Resultados");
				celda.setCellStyle(TituloTabla);
				col = col + 5;
				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("Presupuesto");
				celda.setCellStyle(TituloTabla);
				col = col + 3;
				celda = tablaEncabezado.createCell(col);
				celda.setCellValue("Relevación de Avance %");
				celda.setCellStyle(TituloTabla);
				col = 1;
				XSSFRow tablaEncabezado2 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Detalle de la Acción de Corto Plazo");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Esperado XX Trim.");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Logrado XX Trim.");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Eficacia %");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Eficacia %");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Desenpeño");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Cod. Actv/Prog");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Pres. Vigente");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Ejecutado XX Trim.");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("Ejecución %");
				celda.setCellStyle(TituloTabla);

				hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 1, 0, 0));
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 2, 1, 5));
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 2, 6, 9));
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 1, 10, 10));

				col = 0;

				for (int itDatos = 0; itDatos < 5; itDatos++) {
					XSSFRow tablaDatos = (XSSFRow) hoja.createRow(filaNum++);
					for (col = 0; col < 11; col++) {
						celda = tablaDatos.createCell(col);
						celda.setCellValue(col);
						celda.setCellStyle(datoTabla);
					}
				}
				col = 0;
				XSSFRow caracterRef = (XSSFRow) hoja.createRow(filaNum++);
				celda = caracterRef.createCell(col++);
				celda.setCellValue("*De carácter referencial");
				celda.setCellStyle(caracterRefe);

			}
			String nombrefile = "ejemplo.xlsx";

			hsr1.setContentType("application/vnd.ms-excel"); // Tell the browser to expect an excel file
			hsr1.setHeader("Content-Disposition", "attachment; filename=" + nombrefile);

		} // fin hoja
	}

//////// MATRIZ DE MEDIO DE VERIFICACIÓN

	@GetMapping("/matrizMedioVerificacion")
	public ModelAndView matrizMedioVerificacion(HttpServletRequest datosEntrada) throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_te_seguimiento");

		procedure.setParameter("i_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("i_id_seguimiento", Integer.parseInt(datosEntrada.getParameter("idSeguimiento")));
		List<F_reporte_te_seguimiento> resultadoDatos = procedure.getResultList();

		modelo.put("registros", resultadoDatos);
		return new ModelAndView(new PoaReporteSeguimiento.matrizMedioVerificacionExcel(), modelo);

	}

	public class matrizMedioVerificacionExcel extends AbstractXlsxView {

		@Override
		public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
				HttpServletResponse hsr1) throws Exception {

			//// CONFIG ESTILO

			XSSFCellStyle Titulo = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			Titulo = (XSSFCellStyle) repo.generaEstilo(Titulo, hssfw, true, "LEFT", (short) 10, "Arial", "BLACK", false,
					"", false, "", false, "", false);

			XSSFCellStyle TituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			TituloTabla = (XSSFCellStyle) repo.generaEstilo(TituloTabla, hssfw, true, "CENTER", (short) 9, "Arial",
					"WHITE", true, "C00000", true, "WHITE", false, "", true);

			XSSFCellStyle datoTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			datoTabla = (XSSFCellStyle) repo.generaEstilo(datoTabla, hssfw, false, "CENTER", (short) 9, "Arial",
					"BLACK", false, "", true, "BLACK", false, "#,##0.00", true);
			
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
					/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
					/////// | Ajustar texto
			XSSFCellStyle datoTablaFondoRojo = (XSSFCellStyle) hssfw.createCellStyle();
					datoTablaFondoRojo = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoRojo, hssfw, false, "CENTER", (short) 9, "Arial",
							"BLACK", true, "FA4646", true, "BLACK", false, "#,##0.00", true);
					
					XSSFCellStyle datoTablaFondoNaranja = (XSSFCellStyle) hssfw.createCellStyle();
					datoTablaFondoNaranja = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoNaranja, hssfw, false, "CENTER", (short) 9, "Arial",
							"BLACK", true, "F67226", true, "BLACK", false, "#,##0.00", true);		
					
					XSSFCellStyle datoTablaFondoAmarilllo = (XSSFCellStyle) hssfw.createCellStyle();
					datoTablaFondoAmarilllo = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoAmarilllo, hssfw, false, "CENTER", (short) 9, "Arial",
							"BLACK", true, "F3F626", true, "BLACK", false, "#,##0.00", true);	
					
					XSSFCellStyle datoTablaFondoverde = (XSSFCellStyle) hssfw.createCellStyle();
					datoTablaFondoverde = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoverde, hssfw, false, "CENTER", (short) 9, "Arial",
							"BLACK", true, "8BE184", true, "BLACK", false, "#,##0.00", true);	

			XSSFCellStyle caracterRefe = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			caracterRefe = (XSSFCellStyle) repo.generaEstilo(caracterRefe, hssfw, false, "LEFT", (short) 7, "Arial",
					"BLACK", false, "", false, "", false, "", false);
			
		////// estilos datos
					CellStyle estiloFirma = hssfw.createCellStyle();
					estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

			List<F_reporte_te_seguimiento> resultado = (List<F_reporte_te_seguimiento>) map.get("registros");
			
			/// HOJAS
			
			
			
			String[][] titulosHojas = { { "MAT-MED-VER", "MATRIZ DE MEDIO DE VERIFICACIÓN - "+(resultado.size()>0?resultado.get(0).nombre_seguimiento:""), "" } };
			
			for (int nHoja = 0; nHoja < titulosHojas.length; nHoja++) {
				Sheet hoja = hssfw.createSheet();
				hssfw.setSheetName(nHoja, titulosHojas[nHoja][0]);
				org.apache.poi.ss.usermodel.Cell celda;
				int filaNum = 0, col = 0;
				////// Tamaño de columnas
				Integer tamanoColumna[] = { 4000, 4000, 15000, 3000, 6000, 6000, 3100, 3000, 6000, 3000, 3000, 6000,
						3500 };

				for (int i = 0; i < tamanoColumna.length; i++) {
					hoja.setColumnWidth(i, tamanoColumna[i]);
				}
				
				filaNum++;
				XSSFRow titulo = (XSSFRow) hoja.createRow(filaNum++);
				celda = titulo.createCell(col + 4);
				celda.setCellValue(titulosHojas[nHoja][1]);
				celda.setCellStyle(Titulo);
				filaNum++;
				col = 0;
				XSSFRow tablaEncabezado = (XSSFRow) hoja.createRow(filaNum++);
				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("COD. ACTIVIDAD");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("COD. TAREA ESPECÍFICA");
				celda.setCellStyle(TituloTabla);
				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("OBJETIVO TAREA ESPECÍFICA\n  (Detallar todas las programadas de "+(resultado.size()>0?resultado.get(0).mes_inicio:"") +" a "+(resultado.size()>0?resultado.get(0).mes_fin:"")+")");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("TIPO DE INDICADOR");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("NOMBRE DEL INDICADOR");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("MEDIOS DE VERIFICACIÓN DETERMINADO EN EL POA");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("META PROGRAMADA");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col++);
				celda.setCellValue("META EJECUTADA");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado.createCell(col);
				celda.setCellValue("MEDIOS DE VERIFICACIÓN DE ("+(resultado.size()>0?resultado.get(0).mes_inicio.toUpperCase():"") +" A "+(resultado.size()>0?resultado.get(0).mes_fin.toUpperCase():"")+")");
				celda.setCellStyle(TituloTabla);

				XSSFRow tablaEncabezado2 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("DETALLE DEL DOCUMENTO");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("CITE");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("FECHA DEL DOCUMENTO");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("ESTADO ACTUAL (calculo de la eficacia)");
				celda.setCellStyle(TituloTabla);

				celda = tablaEncabezado2.createCell(col++);
				celda.setCellValue("MEDIDAS CORRECTIVAS");
				celda.setCellStyle(TituloTabla);

				for (col = 0; col < 8; col++) {
					hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 1, col, col));
				}
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 2, filaNum - 2, col, col + 4));
				

				
				
				String eficiencia= "";
				if(resultado!=null) {
				for (int itDatos = 0; itDatos < resultado.size(); itDatos++) {
					col = 0;
					XSSFRow tablaDatos = (XSSFRow) hoja.createRow(filaNum++);
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).actsigla);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).tesigla);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).tedescripcion);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).tipo_indicador);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).nombre_indicador);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).fuente_indicador);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).meta_programada);
						celda.setCellStyle(datoTabla);
												
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).meta_ejecutada);
						celda.setCellStyle(datoTabla);
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).medios_verificacion);
						celda.setCellStyle(datoTabla);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col-1, col+1 ));
						col++;
						col++;
						celda = tablaDatos.createCell(col++);
						if(resultado.get(itDatos).eficiencia>1) {
							resultado.get(itDatos).eficiencia= 1;
						}
						DecimalFormat formato = new DecimalFormat("#.##");
						
						eficiencia =  formato.format(resultado.get(itDatos).eficiencia*100);
						celda.setCellValue( eficiencia +"%\n"+ resultado.get(itDatos).calificacion);
						if(resultado.get(itDatos).eficiencia<0.45) {celda.setCellStyle(datoTablaFondoRojo); }
						if(resultado.get(itDatos).eficiencia>0.45&&resultado.get(itDatos).eficiencia<0.69 ) {celda.setCellStyle(datoTablaFondoNaranja); }
						if(resultado.get(itDatos).eficiencia>0.70&&resultado.get(itDatos).eficiencia<0.94 ) {celda.setCellStyle(datoTablaFondoAmarilllo); }
						if(resultado.get(itDatos).eficiencia>0.94 ) {celda.setCellStyle(datoTablaFondoverde); }
						
						
						celda = tablaDatos.createCell(col++);
						celda.setCellValue(resultado.get(itDatos).medidas_correctivas);
						celda.setCellStyle(datoTabla);
				}
			}
				col = 0;
				XSSFRow caracterRef = (XSSFRow) hoja.createRow(filaNum++);
				celda = caracterRef.createCell(col++);
				celda.setCellValue("*De carácter referencial");
				celda.setCellStyle(caracterRefe);
				filaNum +=6;
				XSSFRow rowFirmas1h3 = (XSSFRow) hoja.createRow(filaNum);

				celda = rowFirmas1h3.createCell(2);
				celda.setCellStyle(estiloFirma);
				celda.setCellValue("Reportado por:");
				RegionUtil.setBorderTop(BorderStyle.THIN, (new CellRangeAddress(filaNum, filaNum, 2, 2)), hoja);
				
				celda = rowFirmas1h3.createCell(7);
				celda.setCellStyle(estiloFirma);
				celda.setCellValue("Aprobado por:");
				hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 7, 9));
				RegionUtil.setBorderTop(BorderStyle.THIN, (new CellRangeAddress(filaNum, filaNum, 7, 9)), hoja);
				
			}
	 		hsr1.setContentType("application/vnd.ms-excel"); //Tell the browser to expect an excel file
	 		hsr1.setHeader("Content-Disposition", "attachment; filename=[MATRIZ MEDIO DE VERIFICACIÓN].xlsx");
			
		} // fin hoja
	}

	/*
	@RequestMapping(path = "/seguimientoEvaluacion", method = RequestMethod.GET)
	public @ResponseBody void reportePrimeraCitacion(HttpServletResponse response,HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		
		String archivo = datosEntrada.getRealPath("") + "/assets/poa/reporteSegEv.xlsx";
				//"C:/Users/MTEPS/Documents/workspace-spring-tool-suite-4-4.9.0.RELEASE/backend-mteps/src/main/resources/WEB-INF/pruebaSE.xlsx";
		
	         FileInputStream inputStream = new FileInputStream(new File(archivo));
	         XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);

	         Sheet hoja = workbook.getSheetAt(0);
	         Sheet hoja2 = workbook.getSheetAt(1);
	         
	         StoredProcedureQuery procedure = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_programado_ejecutado");

	 		procedure.setParameter("i_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		procedure.setParameter("i_id_seguimiento", Integer.parseInt(datosEntrada.getParameter("idSeguimiento")));
	 		
	 		List<F_programado_ejecutado> resultadoDatos = procedure.getResultList();
	 		
	 		
	 		StoredProcedureQuery procedure2 = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_prog_ej_ptto");

	 		procedure2.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		
	 		F_obtener_prog_ej_ptto resultadoDatos2 = (F_obtener_prog_ej_ptto) procedure2.getSingleResult();
	 		
	 		int posicionEfiIni=0, posicionEfiFin=0;
	 		Object[][] meses = { { "Enero", 0 }, { "Febrero", 1 }, { "Marzo", 2 }, { "Abril", 3 }, { "Mayo", 4 }, { "Junio", 5 }, { "Julio", 6 },
	 							{ "Agosto", 7 }, { "Septiembre", 8 }, { "Octubre", 9 }, { "Noviembre", 10 }, { "Diciembre", 11 }};
	 		
	 		for (int i = 0; i < meses.length; i++) {
	 			
	 			if(meses[i][0].equals(resultadoDatos.get(0).mes_fin)) {
	 				posicionEfiFin=(int) meses[i][1];
	 			}
	 			
	 			if(meses[i][0].equals(resultadoDatos.get(0).mes_ini)) {
	 				posicionEfiIni=(int) meses[i][1];
	 			}
	 			
	 		}
	 		
	 		
	 		double[] programado= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado={0,0,0,0,0,0,0,0,0,0,0,0};
	 		String medidadC= "";
	 		
	 		for(int i=0;i<resultadoDatos.size();i++) {
	 			
	 				programado[0]=programado[0] + resultadoDatos.get(i).p_ene;
	 				programado[1]=programado[1] + resultadoDatos.get(i).p_feb;
	 				programado[2]=programado[2] + resultadoDatos.get(i).p_mar;
	 				programado[3]=programado[3] + resultadoDatos.get(i).p_abr;
	 				programado[4]=programado[4] + resultadoDatos.get(i).p_may;
	 				programado[5]=programado[5] + resultadoDatos.get(i).p_jun;
	 				programado[6]=programado[6] + resultadoDatos.get(i).p_jul;
	 				programado[7]=programado[7] + resultadoDatos.get(i).p_ago;
	 				programado[8]=programado[8] + resultadoDatos.get(i).p_sep;
	 				programado[9]=programado[9] + resultadoDatos.get(i).p_oct;
	 				programado[10]=programado[10] + resultadoDatos.get(i).p_nov;
	 				programado[11]=programado[11] + resultadoDatos.get(i).p_dic;
	 				
	 				ejecutado[0]=ejecutado[0] + resultadoDatos.get(i).e_ene;
	 				ejecutado[1]=ejecutado[1] + resultadoDatos.get(i).e_feb;
	 				ejecutado[2]=ejecutado[2] + resultadoDatos.get(i).e_mar;
	 				ejecutado[3]=ejecutado[3] + resultadoDatos.get(i).e_abr;
	 				ejecutado[4]=ejecutado[4] + resultadoDatos.get(i).e_may;
	 				ejecutado[5]=ejecutado[5] + resultadoDatos.get(i).e_jun;
	 				ejecutado[6]=ejecutado[6] + resultadoDatos.get(i).e_jul;
	 				ejecutado[7]=ejecutado[7] + resultadoDatos.get(i).e_ago;
	 				ejecutado[8]=ejecutado[8] + resultadoDatos.get(i).e_sep;
	 				ejecutado[9]=ejecutado[9] + resultadoDatos.get(i).e_oct;
	 				ejecutado[10]=ejecutado[10] + resultadoDatos.get(i).e_nov;
	 				ejecutado[11]=ejecutado[11] + resultadoDatos.get(i).e_dic;
	 				
	 				medidadC= medidadC +"\r\n"+ (resultadoDatos.get(i).medidas_correctivas == null ?"":resultadoDatos.get(i).medidas_correctivas);
	 				
	 		}
	 		
	 		for(int i=0;i<programado.length;i++) {
	 			if(i!=0) {
	 				programado[i] = programado[i-1]+programado[i];
	 				ejecutado[i] = ejecutado[i-1]+ejecutado[i];
	 			}
	 		}
	 		
	 		double[] programado2= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado2={0,0,0,0,0,0,0,0,0,0,0,0};
	 		double totalprog2 = 0;
	 		
	 		for(int i=0;i<resultadoDatos2.detalle.size();i++) {
	 			if(i>0) {
		 			programado2[i]= programado2[i-1] + resultadoDatos2.detalle.get(i).programado;
		 			ejecutado2[i]= ejecutado2[i-1] + resultadoDatos2.detalle.get(i).ejecutado;
		 			 			}else {
		 			 				programado2[i]= resultadoDatos2.detalle.get(i).programado;
		 			 				ejecutado2[i]	=resultadoDatos2.detalle.get(i).ejecutado;		
		 			 			}
	 			totalprog2 = totalprog2 + resultadoDatos2.detalle.get(i).programado;
	 			System.out.println(programado2[i]);
	 		}
	 		
	 		
	 		
	 		double eficiencia= ((ejecutado[posicionEfiFin]/programado[posicionEfiFin]*100)>100?100:((Math.round(ejecutado[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado[posicionEfiFin]* 10.0) / 10.0)*100));
	 	
	 		double eficiencia2 = ((ejecutado2[posicionEfiFin]/programado2[posicionEfiFin]*100)>100?100:((Math.round(ejecutado2[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado2[posicionEfiFin]* 10.0) / 10.0)*100));
//	 		if(totalprog2!=0) {
//	 		eficiencia2= (ejecutado2[posicionEfiFin]*100/totalprog2)>100?100:(ejecutado2[posicionEfiFin]*100/totalprog2);
//	 		}else {
//	 		eficiencia2=0;
//	 		}
	 		
	 		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 		 		  
	 		 
	         Cell celda = hoja.getRow(1).getCell(0);
	         celda.setCellValue(resultadoDatos.get(0).nombre_seguimiento +": "  +resultadoDatos.get(0).mes_ini+ " - "+resultadoDatos.get(0).mes_fin);
	         
	         celda = hoja.getRow(2).getCell(0);
	         celda.setCellValue("Fecha de reporte: " +dtf.format(LocalDateTime.now()));
	        
	         celda = hoja.getRow(3).getCell(2);
	         celda.setCellValue( resultadoDatos.get(0).unidad_funcional );
	         
	         celda = hoja.getRow(4).getCell(2);
	         celda.setCellValue(resultadoDatos.get(0).descripcion );
	         
	         celda = hoja.getRow(31).getCell(0);
	         celda.setCellValue(resultadoDatos.get(0).resultados );
	         
	         celda = hoja.getRow(33).getCell(0);
	         celda.setCellValue(resultadoDatos.get(0).dificultades );
	         
	         celda = hoja.getRow(35).getCell(0);
	         celda.setCellValue(medidadC );
	         
	         celda = hoja.getRow(15).getCell(7);
	         celda.setCellValue((eficiencia<0)?0:eficiencia/100); 
	         
	         celda = hoja.getRow(27).getCell(7);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2/100); 
	         
	         
	         
	         celda = hoja2.getRow(9).getCell(1);
	         celda.setCellValue((eficiencia<0)?0:eficiencia); 
	         
	         celda = hoja2.getRow(9).getCell(4);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2); 
	         
	         int col=17;
	         double prg =0,ejec=0;
	         
	         for(int i=0;i<programado.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 
		         celda.setCellValue((programado[i]>=100?100:programado[i])/100 );
	         }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {
		         celda = hoja2.getRow(col+1).getCell(4+i);
		         prg = (programado[i]>=100?100:programado[i])/100 ;
		         ejec= (ejecutado[i]>=100?100:ejecutado[i])/100;
		         if(ejec > prg) {
		         celda.setCellValue(prg );
		         }else {
		        	 celda.setCellValue(ejec ); 
		         }
	         }
	         
	         col=21;
	         double prog2=0, ejec2=0;
	         for(int i=0;i<programado2.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 prog2=0;
	        	 if(programado2[i]!=0){
	        		 prog2=(programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2; 
	        	 }
		         celda.setCellValue(prog2);
		         
		      }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {    
		         celda = hoja2.getRow(col+1).getCell(4+i);
		        
		         prog2 = (programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2;
		           
		     //    if(ejecutado2[i]!=0){
		        	 ejec2=(ejecutado2[i]>=totalprog2?totalprog2:ejecutado2[i]) / totalprog2; 
	        //	 }else {
	        //		 ejec2=0;
	        //	 }
		         
		    //     if(ejec2 > prog2) {
			//         celda.setCellValue(prog2 );
			//         }else {
			        	 celda.setCellValue(ejec2 ); 
			 //        }
		         
	         }
	         
	         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateAll();
	         
	 		////
	        inputStream.close();
	        response.setContentType("application/vnd.ms-excel");
	  		response.setHeader("Content-Disposition", "inLine; filename=[SEG. EV.]-"+ StringUtils.abbreviate(resultadoDatos.get(0).unidad_funcional,20)+".xlsx");
	        OutputStream  outputStream = response.getOutputStream();
	 		workbook.write(outputStream);
	        workbook.close();
	        outputStream.close();         
	}


	 //repo 2
	@RequestMapping(path = "/seguimientoEvaluacion", method = RequestMethod.GET)
	public @ResponseBody void seguimientoEvaluacion2(HttpServletResponse response,HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		
		String archivo = datosEntrada.getRealPath("") + "/assets/poa/reporteSegEv.xlsx";
				//"C:/Users/MTEPS/Documents/workspace-spring-tool-suite-4-4.9.0.RELEASE/backend-mteps/src/main/resources/WEB-INF/pruebaSE.xlsx";
		
	         FileInputStream inputStream = new FileInputStream(new File(archivo));
	         XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);

	         Sheet hoja = workbook.getSheetAt(0);
	         Sheet hoja2 = workbook.getSheetAt(1);
	         
	         StoredProcedureQuery procedure = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_programado_ejecutado");

	 		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		procedure.setParameter("v_id_seguimiento", Integer.parseInt(datosEntrada.getParameter("idSeguimiento")));
	 		
	 		F_obtener_programado_ejecutado resultadoDatos = (F_obtener_programado_ejecutado) procedure.getSingleResult();
	 		
	 		
	 		StoredProcedureQuery procedure2 = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_prog_ej_ptto");

	 		procedure2.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		
	 		F_obtener_prog_ej_ptto resultadoDatos2 = (F_obtener_prog_ej_ptto) procedure2.getSingleResult();
	 		
	 		int posicionEfiIni=0, posicionEfiFin=0;
	 		Object[][] meses = { { "Enero", 0 }, { "Febrero", 1 }, { "Marzo", 2 }, { "Abril", 3 }, { "Mayo", 4 }, { "Junio", 5 }, { "Julio", 6 },
	 							{ "Agosto", 7 }, { "Septiembre", 8 }, { "Octubre", 9 }, { "Noviembre", 10 }, { "Diciembre", 11 }};
	 		
	 		for (int i = 0; i < meses.length; i++) {
	 			
	 			if(meses[i][0].equals(resultadoDatos.mesFin)) {
	 				posicionEfiFin=(int) meses[i][1];
	 			}
	 			
	 			if(meses[i][0].equals(resultadoDatos.mesInicio)) {
	 				posicionEfiIni=(int) meses[i][1];
	 			}
	 			
	 		}
	 		
	 		
	 		double[] programado= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado={0,0,0,0,0,0,0,0,0,0,0,0};
	 		String medidadC= "";
	 		
	 		for(int i=0;i<resultadoDatos.detalle.size();i++) {
	 			
	 			medidadC= medidadC +"\r\n"+ (resultadoDatos.detalle.get(i).medidaCorrectiva== null ?"":resultadoDatos.detalle.get(i).medidaCorrectiva);
	 			
	 			for (int j = 0; j<resultadoDatos.detalle.get(i).progEjec.size();j++ ) {

	 				programado[j]=programado[j] + resultadoDatos.detalle.get(i).progEjec.get(j).valorProgramado;
	 				
	 				ejecutado[j]=ejecutado[j] + (resultadoDatos.detalle.get(i).progEjec.get(j).valorEjecutado!=null?resultadoDatos.detalle.get(i).progEjec.get(j).valorEjecutado:0.0);
	 			}
	 			
	 				
	 		}
	 		
	 		for(int i=0;i<programado.length;i++) {
	 			if(i!=0) {
	 				programado[i] = programado[i-1]+programado[i];
	 				ejecutado[i] = ejecutado[i-1]+ejecutado[i];
	 			}
	 		}
	 		
	 		double[] programado2= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado2={0,0,0,0,0,0,0,0,0,0,0,0};
	 		double totalprog2 = 0;
	 		
	 		for(int i=0;i<resultadoDatos2.detalle.size();i++) {
	 			if(i>0) {
		 			programado2[i]= programado2[i-1] + resultadoDatos2.detalle.get(i).programado;
		 			ejecutado2[i]= ejecutado2[i-1] + resultadoDatos2.detalle.get(i).ejecutado;
		 			 			}else {
		 			 				programado2[i]= resultadoDatos2.detalle.get(i).programado;
		 			 				ejecutado2[i]	=resultadoDatos2.detalle.get(i).ejecutado;		
		 			 			}
	 			totalprog2 = totalprog2 + resultadoDatos2.detalle.get(i).programado;
	 		}
	 		
	 		
	 		
	 		double eficiencia= ((ejecutado[posicionEfiFin]/programado[posicionEfiFin]*100)>100?100:((Math.round(ejecutado[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado[posicionEfiFin]* 10.0) / 10.0)*100));
	 	
	 		double eficiencia2 = ((ejecutado2[posicionEfiFin]/programado2[posicionEfiFin]*100)>100?100:((Math.round(ejecutado2[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado2[posicionEfiFin]* 10.0) / 10.0)*100));
//	 		if(totalprog2!=0) {
//	 		eficiencia2= (ejecutado2[posicionEfiFin]*100/totalprog2)>100?100:(ejecutado2[posicionEfiFin]*100/totalprog2);
//	 		}else {
//	 		eficiencia2=0;
//	 		}
	 		
	 		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 		 		  
	 		 
	         Cell celda = hoja.getRow(1).getCell(0);
	         celda.setCellValue(resultadoDatos.nombreSeguimiento +": "  +resultadoDatos.mesInicio+ " - "+resultadoDatos.mesFin);
	         
	         celda = hoja.getRow(2).getCell(0);
	         celda.setCellValue("Fecha de reporte: " +dtf.format(LocalDateTime.now()));
	        
	         celda = hoja.getRow(3).getCell(2);
	         celda.setCellValue( resultadoDatos.unidadFuncional );
	         
	         celda = hoja.getRow(4).getCell(2);
	         celda.setCellValue(resultadoDatos.nombreAcp.get(0).descripcion );
	         
	         celda = hoja.getRow(31).getCell(0);
	         celda.setCellValue(resultadoDatos.detalle.get(0).resultados );
	         
	         celda = hoja.getRow(33).getCell(0);
	         celda.setCellValue(resultadoDatos.detalle.get(0).dificultades );
	         
	         celda = hoja.getRow(35).getCell(0);
	         celda.setCellValue(medidadC );
	         
	         celda = hoja.getRow(15).getCell(7);
	         celda.setCellValue((eficiencia<0)?0:eficiencia/100); 
	         
	         celda = hoja.getRow(27).getCell(7);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2/100); 
	         
	         
	         
	         celda = hoja2.getRow(9).getCell(1);
	         celda.setCellValue((eficiencia<0)?0:eficiencia); 
	         
	         celda = hoja2.getRow(9).getCell(4);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2); 
	         
	         int col=17;
	         double prg =0,ejec=0;
	         
	         for(int i=0;i<programado.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 
		         celda.setCellValue((programado[i]>=100?100:programado[i])/100 );
	         }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {
		         celda = hoja2.getRow(col+1).getCell(4+i);
		         prg = (programado[i]>=100?100:programado[i])/100 ;
		         ejec= (ejecutado[i]>=100?100:ejecutado[i])/100;
		         if(ejec > prg) {
		         celda.setCellValue(prg );
		         }else {
		        	 celda.setCellValue(ejec ); 
		         }
	         }
	         
	         col=21;
	         double prog2=0, ejec2=0;
	         for(int i=0;i<programado2.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 prog2=0;
	        	 if(programado2[i]!=0){
	        		 prog2=(programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2; 
	        	 }
		         celda.setCellValue(prog2);
		         
		      }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {    
		         celda = hoja2.getRow(col+1).getCell(4+i);
		        
		         prog2 = (programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2;
		           
		     //    if(ejecutado2[i]!=0){
		        	 ejec2=(ejecutado2[i]>=totalprog2?totalprog2:ejecutado2[i]) / totalprog2; 
	        //	 }else {
	        //		 ejec2=0;
	        //	 }
		         
		    //     if(ejec2 > prog2) {
			//         celda.setCellValue(prog2 );
			//         }else {
			        	 celda.setCellValue(ejec2 ); 
			 //        }
		         
	         }
	         
	         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateAll();
	         
	 		////
	        inputStream.close();
	        response.setContentType("application/vnd.ms-excel");
	  		response.setHeader("Content-Disposition", "inLine; filename=[SEG. EV.]-"+ StringUtils.abbreviate(resultadoDatos.unidadFuncional,20)+".xlsx");
	        OutputStream  outputStream = response.getOutputStream();
	 		workbook.write(outputStream);
	        workbook.close();
	        outputStream.close();         
	}
	
	*/
	
	@RequestMapping(path = "/seguimientoEvaluacion", method = RequestMethod.GET)
	public @ResponseBody void seguimientoEvaluacion2(HttpServletResponse response,HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		
		String archivo = datosEntrada.getRealPath("") + "/assets/poa/reporteSegEv.xlsx";
				//"C:/Users/MTEPS/Documents/workspace-spring-tool-suite-4-4.9.0.RELEASE/backend-mteps/src/main/resources/WEB-INF/pruebaSE.xlsx";
		
	         FileInputStream inputStream = new FileInputStream(new File(archivo));
	         XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);

	         Sheet hoja = workbook.getSheetAt(0);
	         Sheet hoja2 = workbook.getSheetAt(1);
	         
	         StoredProcedureQuery procedure = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_programado_ejecutado");

	 		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		procedure.setParameter("v_id_seguimiento", Integer.parseInt(datosEntrada.getParameter("idSeguimiento")));
	 		
	 		F_obtener_programado_ejecutado resultadoDatos = (F_obtener_programado_ejecutado) procedure.getSingleResult();
	 		
	 		
	 		StoredProcedureQuery procedure2 = entityManager
	 				.createNamedStoredProcedureQuery("mteps_plan.f_obtener_prog_ej_ptto");

	 		procedure2.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
	 		
	 		F_obtener_prog_ej_ptto resultadoDatos2 = (F_obtener_prog_ej_ptto) procedure2.getSingleResult();
	 		
	 		int posicionEfiIni=0, posicionEfiFin=0;
	 		Object[][] meses = { { "Enero", 0 }, { "Febrero", 1 }, { "Marzo", 2 }, { "Abril", 3 }, { "Mayo", 4 }, { "Junio", 5 }, { "Julio", 6 },
	 							{ "Agosto", 7 }, { "Septiembre", 8 }, { "Octubre", 9 }, { "Noviembre", 10 }, { "Diciembre", 11 }};
	 		
	 		for (int i = 0; i < meses.length; i++) {
	 			
	 			if(meses[i][0].equals(resultadoDatos.mesFin)) {
	 				posicionEfiFin=(int) meses[i][1];
	 			}
	 			
	 			if(meses[i][0].equals(resultadoDatos.mesInicio)) {
	 				posicionEfiIni=(int) meses[i][1];
	 			}
	 			
	 		}
	 		
	 		
	 		double[] programado= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado={0,0,0,0,0,0,0,0,0,0,0,0};
	 		String medidadC= "";
	 		
	 		for(int i=0;i<resultadoDatos.detalle.size();i++) {
	 			
	 			medidadC= medidadC +"\r\n"+ (resultadoDatos.detalle.get(i).medidaCorrectiva== null ?"":resultadoDatos.detalle.get(i).medidaCorrectiva);
	 			
	 			
	 			
	 				
	 		}
	 		
	 		for (int j = 0; j<resultadoDatos.detalle_p_e.size();j++ ) {

 				programado[j]= resultadoDatos.detalle_p_e.get(j).valorProgramado;
 				
 				ejecutado[j]=resultadoDatos.detalle_p_e.get(j).valorEjecutado;
 			}
	 		
	 		double[] programado2= {0,0,0,0,0,0,0,0,0,0,0,0};
	 		double[] ejecutado2={0,0,0,0,0,0,0,0,0,0,0,0};
	 		double totalprog2 = 0;
	 		
	 		for(int i=0;i<resultadoDatos2.detalle.size();i++) {
	 			if(i>0) {
		 			programado2[i]= programado2[i-1] + resultadoDatos2.detalle.get(i).programado;
		 			ejecutado2[i]= ejecutado2[i-1] + resultadoDatos2.detalle.get(i).ejecutado;
		 			 			}else {
		 			 				programado2[i]= resultadoDatos2.detalle.get(i).programado;
		 			 				ejecutado2[i]	=resultadoDatos2.detalle.get(i).ejecutado;		
		 			 			}
	 			totalprog2 = totalprog2 + resultadoDatos2.detalle.get(i).programado;
	 		}
	 		
	 		
	 		
	 		double eficiencia= ((ejecutado[posicionEfiFin]/programado[posicionEfiFin]*100)>100?100:((Math.round(ejecutado[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado[posicionEfiFin]* 10.0) / 10.0)*100));
	 	
	 		double eficiencia2 = ((ejecutado2[posicionEfiFin]/programado2[posicionEfiFin]*100)>100?100:((Math.round(ejecutado2[posicionEfiFin]* 10.0) / 10.0)/(Math.round(programado2[posicionEfiFin]* 10.0) / 10.0)*100));
//	 		if(totalprog2!=0) {
//	 		eficiencia2= (ejecutado2[posicionEfiFin]*100/totalprog2)>100?100:(ejecutado2[posicionEfiFin]*100/totalprog2);
//	 		}else {
//	 		eficiencia2=0;
//	 		}
	 		
	 		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 		 		  
	 		 
	         Cell celda = hoja.getRow(1).getCell(0);
	         celda.setCellValue(resultadoDatos.nombreSeguimiento +": "  +resultadoDatos.mesInicio+ " - "+resultadoDatos.mesFin);
	         
	         celda = hoja.getRow(2).getCell(0);
	         celda.setCellValue("Fecha de reporte: " +dtf.format(LocalDateTime.now()));
	        
	         celda = hoja.getRow(3).getCell(2);
	         celda.setCellValue( resultadoDatos.unidadFuncional );
	         
	         celda = hoja.getRow(4).getCell(2);
	         celda.setCellValue(resultadoDatos.nombreAcp.get(0).descripcion );
	         
	         celda = hoja.getRow(31).getCell(0);
	         celda.setCellValue(resultadoDatos.detalle.get(0).resultados );
	         
	         celda = hoja.getRow(33).getCell(0);
	         celda.setCellValue(resultadoDatos.detalle.get(0).dificultades );
	         
	         celda = hoja.getRow(35).getCell(0);
	         celda.setCellValue(medidadC );
	         
	         celda = hoja.getRow(15).getCell(7);
	         celda.setCellValue((eficiencia<0)?0:eficiencia/100); 
	         
	         celda = hoja.getRow(27).getCell(7);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2/100); 
	         
	         
	         
	         celda = hoja2.getRow(9).getCell(1);
	         celda.setCellValue((eficiencia<0)?0:eficiencia); 
	         
	         celda = hoja2.getRow(9).getCell(4);
	         celda.setCellValue((eficiencia2<0)?0:eficiencia2); 
	         
	         int col=17;
	         double prg =0,ejec=0;
	         
	         for(int i=0;i<programado.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 
		         celda.setCellValue((programado[i]>=100?100:programado[i])/100 );
	         }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {
		         celda = hoja2.getRow(col+1).getCell(4+i);
		         prg = (programado[i]>=100?100:programado[i])/100 ;
		         ejec= (ejecutado[i]>=100?100:ejecutado[i])/100;
		         if(ejec > prg) {
		         celda.setCellValue(prg );
		         }else {
		        	 celda.setCellValue(ejec ); 
		         }
	         }
	         
	         col=21;
	         double prog2=0, ejec2=0;
	         for(int i=0;i<programado2.length;i++) {
	        	 celda = hoja2.getRow(col).getCell(4+i);
	        	 prog2=0;
	        	 if(programado2[i]!=0){
	        		 prog2=(programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2; 
	        	 }
		         celda.setCellValue(prog2);
		         
		      }
	         for(int i=posicionEfiIni;i<=posicionEfiFin;i++) {    
		         celda = hoja2.getRow(col+1).getCell(4+i);
		        
		         prog2 = (programado2[i]>=totalprog2?totalprog2:programado2[i]) / totalprog2;
		           
		     //    if(ejecutado2[i]!=0){
		        	 ejec2=(ejecutado2[i]>=totalprog2?totalprog2:ejecutado2[i]) / totalprog2; 
	        //	 }else {
	        //		 ejec2=0;
	        //	 }
		         
		    //     if(ejec2 > prog2) {
			//         celda.setCellValue(prog2 );
			//         }else {
			        	 celda.setCellValue(ejec2 ); 
			 //        }
		         
	         }
	         
	         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateAll();
	         
	 		////
	        inputStream.close();
	        response.setContentType("application/vnd.ms-excel");
	  		response.setHeader("Content-Disposition", "inLine; filename=[SEG. EV.]-"+ StringUtils.abbreviate(resultadoDatos.unidadFuncional,20)+".xlsx");
	        OutputStream  outputStream = response.getOutputStream();
	 		workbook.write(outputStream);
	        workbook.close();
	        outputStream.close();         
	}

	
	///// SEGUIMIENTO Y EVALUACION

	@GetMapping("/seguimientEvaluacion")
	public ModelAndView seguimientEvacuacion(HttpServletRequest datosEntrada) throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_form_presupuesto_gasto");

		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));
		procedure.setParameter("v_id_apertura", Integer.parseInt(datosEntrada.getParameter("idApertura")));
		F_reporte_form_presupuesto_gasto resultadoDatos = (F_reporte_form_presupuesto_gasto) procedure
				.getSingleResult();

		modelo.put("registros", resultadoDatos);
		return new ModelAndView(new PoaReporteSeguimiento.seguimientEvacuacionExcel(), modelo);

	}

	public class seguimientEvacuacionExcel extends AbstractXlsxView {

		@Override
		protected void buildExcelDocument(Map<String, Object> model, Workbook hssfw, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			//// CONFIG ESTILO

			XSSFCellStyle Titulo = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			Titulo = (XSSFCellStyle) repo.generaEstilo(Titulo, hssfw, true, "CENTER", (short) 10, "Arial", "BLACK",
					false, "", false, "", false, "", false);

			XSSFCellStyle TituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			TituloTabla = (XSSFCellStyle) repo.generaEstilo(TituloTabla, hssfw, false, "CENTER", (short) 10, "Arial",
					"BLACK", true, "FCE4D6", true, "WHITE", false, "", true);

			XSSFCellStyle datoTabla = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			datoTabla = (XSSFCellStyle) repo.generaEstilo(datoTabla, hssfw, false, "JUSTIFY", (short) 10, "Arial",
					"BLACK", true, "EDEDED", true, "WHITE", false, "#,##0.00", true);

			XSSFCellStyle caracterRefe = (XSSFCellStyle) hssfw.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			caracterRefe = (XSSFCellStyle) repo.generaEstilo(caracterRefe, hssfw, false, "LEFT", (short) 7, "Arial",
					"BLACK", false, "", false, "", false, "", false);

			//// Fin

			/// HOJAS
			String[][] titulosHojas = { { "SEG. Y EV.", "SEGUIMIENTO Y EVALUACIÓN", "" } };

			for (int nHoja = 0; nHoja < titulosHojas.length; nHoja++) {
				XSSFSheet hoja = (XSSFSheet) hssfw.createSheet();
				hssfw.setSheetName(nHoja, titulosHojas[nHoja][0]);
				org.apache.poi.ss.usermodel.Cell celda;
				int filaNum = 0, col = 0;
				////// Tamaño de columnas
				Integer tamanoColumna[] = { 9000, 9000, 9000 };

				for (int i = 0; i < tamanoColumna.length; i++) {
					hoja.setColumnWidth(i, tamanoColumna[i]);
				}

				filaNum++;
				XSSFRow titulo = (XSSFRow) hoja.createRow(filaNum++);
				celda = titulo.createCell(1);
				celda.setCellValue("MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL");
				celda.setCellStyle(Titulo);
				XSSFRow titulo2 = (XSSFRow) hoja.createRow(filaNum++);
				celda = titulo2.createCell(1);
				celda.setCellValue("SEGUIMIENTO Y EVALUACIÓN DE XX TRIMESTRE (?)/SEMESTRE (?) POA 2022 (?)");
				celda.setCellStyle(Titulo);

				filaNum++;
				col = 0;
				XSSFRow tabla1 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla1.createCell(col++);
				celda.setCellValue("UNIDAD ORGANIZACIONAL");
				celda.setCellStyle(TituloTabla);

				celda = tabla1.createCell(col++);
				celda.setCellValue("");
				celda.setCellStyle(datoTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col));
				col = 0;
				XSSFRow tabla2 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla2.createCell(col++);
				celda.setCellValue("ACCIÓN DE CORTO PLAZO");
				celda.setCellStyle(TituloTabla);

				celda = tabla2.createCell(col++);
				celda.setCellValue("");
				celda.setCellStyle(datoTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col));
				col = 0;
				XSSFRow tabla3 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla3.createCell(col++);
				celda.setCellValue("SEGUIMIENTO");
				celda.setCellStyle(TituloTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col));
				col++;
				celda = tabla3.createCell(col++);
				celda.setCellValue("EVALUACIÓN");
				celda.setCellStyle(TituloTabla);

				filaNum++;
				filaNum++;
				col = 0;
				XSSFRow tabla4 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla4.createCell(col++);
				celda.setCellValue("PRINCIPALES RESULTADOS LOGRADOS AL PRIMER SEMESTRE 2022(?)");
				celda.setCellStyle(TituloTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
				col = 0;
				XSSFRow tabla5 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla5.createCell(col++);
				celda.setCellValue("Datos");
				celda.setCellStyle(datoTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
				col = 0;
				XSSFRow tabla6 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla6.createCell(col++);
				celda.setCellValue("PRINCIPALES DIFICULTADES PARA LA EJECUCIÓN AL PRIMER SEMESTRE");
				celda.setCellStyle(TituloTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
				col = 0;
				XSSFRow tabla7 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla7.createCell(col++);
				celda.setCellValue("Datos");
				celda.setCellStyle(datoTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));

				col = 0;
				XSSFRow tabla8 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla8.createCell(col++);
				celda.setCellValue("MEDIDAS CORRECTIVAS");
				celda.setCellStyle(TituloTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
				col = 0;
				XSSFRow tabla9 = (XSSFRow) hoja.createRow(filaNum++);
				celda = tabla9.createCell(col++);
				celda.setCellValue("Datos");
				celda.setCellStyle(datoTabla);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));

				// GRAFICOOO
				String sheetName = "Datos";

				XSSFSheet sheet = (XSSFSheet) hssfw.createSheet(sheetName);

				// Mes Names
				Row row = sheet.createRow((short) 0);

				Cell cell = row.createCell((short) 0);
				cell.setCellValue("Ene.");

				cell = row.createCell((short) 1);
				cell.setCellValue("Feb.");

				cell = row.createCell((short) 2);
				cell.setCellValue("Mar.");

				cell = row.createCell((short) 3);
				cell.setCellValue("Abr.");

				cell = row.createCell((short) 4);
				cell.setCellValue("May.");

				cell = row.createCell((short) 5);
				cell.setCellValue("Jun.");

				cell = row.createCell((short) 6);
				cell.setCellValue("Jul.");

				cell = row.createCell((short) 7);
				cell.setCellValue("Ago.");

				cell = row.createCell((short) 8);
				cell.setCellValue("Sep.");

				cell = row.createCell((short) 9);
				cell.setCellValue("Oct.");

				cell = row.createCell((short) 10);
				cell.setCellValue("Nov.");

				cell = row.createCell((short) 11);
				cell.setCellValue("Dic.");

				// Programación Acumulada Area
				row = sheet.createRow((short) 1);

				cell = row.createCell((short) 0);
				cell.setCellValue(1);

				cell = row.createCell((short) 1);
				cell.setCellValue(3);

				cell = row.createCell((short) 2);
				cell.setCellValue(3);

				cell = row.createCell((short) 3);
				cell.setCellValue(8);

				cell = row.createCell((short) 4);
				cell.setCellValue(9);

				cell = row.createCell((short) 5);
				cell.setCellValue(10);

				cell = row.createCell((short) 6);
				cell.setCellValue(11);

				cell = row.createCell((short) 7);
				cell.setCellValue(12);

				cell = row.createCell((short) 8);
				cell.setCellValue(13);

				cell = row.createCell((short) 9);
				cell.setCellValue(14);

				cell = row.createCell((short) 10);
				cell.setCellValue(15);

				cell = row.createCell((short) 11);
				cell.setCellValue(16);

				// Country Population
				row = sheet.createRow((short) 2);

				cell = row.createCell((short) 0);
				cell.setCellValue(10);

				cell = row.createCell((short) 1);
				cell.setCellValue(30);

				cell = row.createCell((short) 2);
				cell.setCellValue(35);

				cell = row.createCell((short) 3);
				cell.setCellValue(40);

				cell = row.createCell((short) 4);
				cell.setCellValue(43);

				cell = row.createCell((short) 5);
				cell.setCellValue(52);

				cell = row.createCell((short) 6);
				cell.setCellValue(68);

				cell = row.createCell((short) 7);
				cell.setCellValue(60);

				cell = row.createCell((short) 8);
				cell.setCellValue(70);

				cell = row.createCell((short) 9);
				cell.setCellValue(82);

				cell = row.createCell((short) 10);
				cell.setCellValue(91);

				cell = row.createCell((short) 11);
				cell.setCellValue(100);

				XSSFDrawing drawing = hoja.createDrawingPatriarch();
				XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 7, 2, 9);

				XSSFChart chart = drawing.createChart(anchor);
				chart.setTitleText("Programación y Ejecución Fisica");
				chart.setTitleOverlay(false);

				XDDFChartLegend legend = chart.getOrAddLegend();
				legend.setPosition(LegendPosition.TOP_RIGHT);

				XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
				bottomAxis.setTitle("Mes");
				XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
				leftAxis.setTitle("Porcentaje");

				XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet,
						new CellRangeAddress(0, 0, 0, 11));

				XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
						new CellRangeAddress(1, 1, 0, 11));

				XDDFNumericalDataSource<Double> population = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
						new CellRangeAddress(2, 2, 0, 11));

				XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

				XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
				series1.setTitle("PROGRAMACIÓN ACUMULADA EN %", null);
				series1.setSmooth(false);
				series1.setMarkerStyle(MarkerStyle.STAR);

				XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) data.addSeries(countries, population);
				series2.setTitle("EJECUCIÓN ACUMULADA EN %", null);
				series2.setSmooth(true);
				series2.setMarkerSize((short) 6);
				series2.setMarkerStyle(MarkerStyle.SQUARE);

				chart.plot(data);
			}

		}

	}







}
