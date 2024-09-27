package mteps.poa;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mteps.config.ConfigCORE;
import mteps.poa.entity.DatosValidacionConciliacion;
import mteps.poa.entity.F_ejecucion_presupuestaria_actividad_partida;
import mteps.poa.entity.F_ejecucion_presupuestaria_consolidada;
import mteps.poa.entity.F_ejecucion_presupuestaria_mensual;
import mteps.poa.entity.F_ejecucion_presupuestaria_partida_reformulacion;
import mteps.poa.entity.F_ejecucion_presupuestaria_por_actividad;
import mteps.poa.entity.F_ejecucion_seguimiento_fisico_presupuestario;
import mteps.poa.entity.F_ejecucion_seguimiento_presupuestario;
import mteps.poa.entity.F_estado_ptto_ref;
import mteps.poa.entity.F_formulario_traspaso_presupuesto;
import mteps.poa.entity.F_lista_conciliacion;
import mteps.poa.entity.F_reporte_form_presupuesto_gasto;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Pojos.ReporteMemoriaCalculo;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("sispoa/reporte")
public class PoaReportePresupuestario {

	Map modelo = new HashMap();

	@Autowired
	PlanMap planMap;

	@Autowired
	ConfigReport repo;

	@Autowired
	PoaCore poaCore;

	@Autowired
	ConfigCORE config;

	@Value("${directorio.archivos}")
	public String dir_archivos;

	@Value("${directorio.enlace}")
	public String dir_enlace;
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@PersistenceContext
	private EntityManager entityManager;

	////// REPORTE 1 FORMULACION PRESUPUESTO GASTO

	@GetMapping("/formPresupGasto2")
	public ModelAndView reportePoaAccionCortoPlazo(HttpServletRequest datosEntrada) throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_form_presupuesto_gasto");

		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));

		F_reporte_form_presupuesto_gasto resultadoDatos = (F_reporte_form_presupuesto_gasto) procedure
				.getSingleResult();

		modelo.put("registros", resultadoDatos);
		return new ModelAndView(new PoaReportePresupuestario.reportePoaAccionCortoPlazoExcel(), modelo);

	}

	public class reportePoaAccionCortoPlazoExcel extends AbstractXlsxView {

		@Override
		public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
				HttpServletResponse hsr1) throws Exception {

			DecimalFormat df = new DecimalFormat("#.##");
			Sheet sheet = hssfw.createSheet();
			hssfw.setSheetName(0, "FORMULACIÓN PRESUPUESTO DE GASTO");

			org.apache.poi.ss.usermodel.Cell celda;

			Integer filaNum = 0;

			////// Tamaño de columnas
			Integer tamanoColumna[] = { 3400, 20000, 3000, 4200, 4200 };

			for (int i = 0; i < tamanoColumna.length; i++) {
				sheet.setColumnWidth(i, tamanoColumna[i]);
			}

			////// estilos texto normal
			CellStyle estiloSuperiorLateral = hssfw.createCellStyle();
			estiloSuperiorLateral = repo.estiloSuperiorLateral(estiloSuperiorLateral, hssfw, false, "RIGHT");

			////// estilos titulo
			CellStyle estiloTitulo = hssfw.createCellStyle();
			estiloTitulo = repo.estiloTitulo(estiloTitulo, hssfw);

			////// estilos texto normal
			CellStyle estiloTextoNormal = hssfw.createCellStyle();
			estiloTextoNormal = repo.estiloTextoNormal(estiloTextoNormal, hssfw, false);

			CellStyle estiloTextoNormalNegrita = hssfw.createCellStyle();
			estiloTextoNormalNegrita = repo.estiloTextoNormal(estiloTextoNormalNegrita, hssfw, true);

			////// estilos Titulos Tabla
			CellStyle estiloTituloTabla = hssfw.createCellStyle();
			estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

			////// estilos Titulos Tabla
			XSSFCellStyle estiloSubTituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
			estiloSubTituloTabla = (XSSFCellStyle) repo.estiloSubTituloTabla(estiloSubTituloTabla, hssfw);

			////// estilos Titulos Tabla
			XSSFCellStyle estiloSubTituloTablaCentro = (XSSFCellStyle) hssfw.createCellStyle();
			estiloSubTituloTablaCentro = (XSSFCellStyle) repo.estiloSubTituloTablaCentro(estiloSubTituloTablaCentro,
					hssfw);

			////// estilos datos
			CellStyle estiloDatos = hssfw.createCellStyle();
			estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

			////// estilos datos
			CellStyle estiloDatosJustificado = hssfw.createCellStyle();
			estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

			////// estilos datos
			CellStyle estiloFirma = hssfw.createCellStyle();
			estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

			////// estilos numerico Titulos Tabla
			XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
			estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
					.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

			////// estilos numerico sub Titulos Tabla
			XSSFCellStyle estilonumericoSubTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
			estilonumericoSubTituloTabladerecha = (XSSFCellStyle) repo
					.estiloNumericoSubTituloTablaDerecha(estilonumericoSubTituloTabladerecha, hssfw);

			////// estilos numerico tabla
			CellStyle estiloNumericoDerecha = hssfw.createCellStyle();
			estiloNumericoDerecha = repo.estiloNumericoTablaDerecha(estiloNumericoDerecha, hssfw);

			String tipInd = "", tipIndvalor = "";

			///////// ESTILO NUMERICO
			CellStyle estiloNumero = hssfw.createCellStyle();
			DataFormat formatoNumero = hssfw.createDataFormat();
			estiloNumero = repo.estiloNumericoTabla(estiloNumero, hssfw);

			///// IMAGEN
			String rutaImg = hsr.getRealPath("") + "/assets/logo2.jpg";
			InputStream inputStream = new FileInputStream(rutaImg);
			byte[] bytes = IOUtils.toByteArray(inputStream);
			int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			inputStream.close();

			CreationHelper helper = hssfw.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();

			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize();
			////

			F_reporte_form_presupuesto_gasto resultado = (F_reporte_form_presupuesto_gasto) map.get("registros");

			XSSFRow header = (XSSFRow) sheet.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("CUADRO RESUMEN");
			celda.setCellStyle(estiloTitulo);
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

			XSSFRow header1 = (XSSFRow) sheet.createRow(filaNum++);
			celda = header1.createCell(0);
			celda.setCellValue("FORMULACIÓN DEL PRESUPUESTO DE GASTO");
			celda.setCellStyle(estiloTitulo);
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

			XSSFRow header3 = (XSSFRow) sheet.createRow(filaNum++);
			celda = header3.createCell(0);
			celda.setCellValue("GESTIÓN " + resultado.valor_gestion);
			celda.setCellStyle(estiloTitulo);
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

			XSSFRow header2 = (XSSFRow) sheet.createRow(filaNum++);
			celda = header2.createCell(0);
			celda.setCellValue("(Expresado en Bolivianos)");
			celda.setCellStyle(estiloTitulo);
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));
			filaNum++;
			XSSFRow header4 = (XSSFRow) sheet.createRow(filaNum++);
			celda = header4.createCell(0);
			celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
			celda.setCellStyle(estiloTextoNormal);

			XSSFRow header5 = (XSSFRow) sheet.createRow(filaNum++);
			celda = header5.createCell(0);
			celda.setCellValue("PROGRAMA: " + resultado.nombre_prog_presupuestario);
			celda.setCellStyle(estiloTextoNormal);

			filaNum++;
			int col = 0;
			XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("PARTIDA DE GASTO");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("DENOMINACIÓN");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("FUENTE DE FINANCIAMIENTO");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, col + 1));
			col++;
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("IMPORTE TOTAL");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));

			XSSFRow rowsub1 = (XSSFRow) sheet.createRow(filaNum);

			celda = rowsub1.createCell(col - 2);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("10 - 11 T.G.N.");

			celda = rowsub1.createCell(col - 1);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("11 - 0 T.G.N.");
			filaNum++;
			col = 0;
			if (resultado.partidas != null) {
				for (int i = 0; i < resultado.partidas.size(); i++) {

					XSSFRow row1 = (XSSFRow) sheet.createRow(filaNum);
					celda = row1.createCell(col++);
					celda.setCellStyle(estiloSubTituloTabla);
					celda.setCellValue(resultado.partidas.get(i).cod_partida);

					celda = row1.createCell(col++);
					celda.setCellStyle(estiloSubTituloTabla);
					celda.setCellValue(resultado.partidas.get(i).nombre_partida);

					celda = row1.createCell(col++);
					celda.setCellStyle(estilonumericoSubTituloTabladerecha);
					celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn);

					celda = row1.createCell(col++);
					celda.setCellStyle(estilonumericoSubTituloTabladerecha);
					celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn_otros);

					celda = row1.createCell(col++);
					celda.setCellStyle(estilonumericoSubTituloTabladerecha);
					celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn_total);
					filaNum++;
					col = 0;
					if (resultado.partidas.get(i).partidas != null) {
						for (int j = 0; j < resultado.partidas.get(i).partidas.size(); j++) {

							XSSFRow row2 = (XSSFRow) sheet.createRow(filaNum);
							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).cod_partida);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).nombre_partida);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn_otros);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn_total);
							filaNum++;
							col = 0;
						}
					}
					col = 0;

				}
			}
			XSSFRow rowPie = (XSSFRow) sheet.createRow(filaNum);

			celda = rowPie.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TOTALES");
			sheet.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 1));
			col++;
			col++;
			celda = rowPie.createCell(col);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn);
			col++;
			celda = rowPie.createCell(col);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn_otros);
			col++;
			celda = rowPie.createCell(col);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn_total);

			//////////////////////// MEMORIAS DE CALCULO DETALLE

			int numHoja = 1;
			double importe_total = 0;
			double total_cantidad = 0;
			double total_costo = 0;
			if (resultado.partidas != null) {
				for (int i = 0; i < resultado.partidas.size(); i++) {

					for (int j = 0; j < resultado.partidas.get(i).partidas.size(); j++) {

						Sheet hoja = hssfw.createSheet();
						hssfw.setSheetName(numHoja, resultado.partidas.get(i).partidas.get(j).cod_partida);
						numHoja++;
						filaNum = 0;

						if (resultado.partidas.get(i).partidas.get(j).id_partida_presupuesto != 58) {

							////// Tamaño de columnas
							Integer tamanoColumnas[] = { 2500, 3000, 8000, 2600, 3000, 3000, 3500, 8000 };

							for (int var = 0; var < tamanoColumnas.length; var++) {
								hoja.setColumnWidth(var, tamanoColumnas[var]);
							}

							XSSFRow headerM = (XSSFRow) hoja.createRow(filaNum++);
							celda = headerM.createCell(0);
							celda.setCellValue("FORMULACIÓN DE ANTEPROYECTO DE PRESUPUESTO " + resultado.valor_gestion);
							celda.setCellStyle(estiloTitulo);
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

							XSSFRow header1M = (XSSFRow) hoja.createRow(filaNum++);
							celda = header1M.createCell(0);
							celda.setCellValue("MEMORIA DE CALCULO");
							celda.setCellStyle(estiloTitulo);
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

							XSSFRow header2M = (XSSFRow) hoja.createRow(filaNum++);
							celda = header2M.createCell(0);
							celda.setCellValue("(Expresado en Bolivianos)");
							celda.setCellStyle(estiloTitulo);
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

							filaNum++;

							XSSFRow rowActividad = (XSSFRow) hoja.createRow(filaNum++);
							rowActividad.setHeight((short) 300);
							celda = rowActividad.createCell(0);
							celda.setCellValue("ACTIVIDAD: " + resultado.sigla_act_presupuestario + " "
									+ resultado.nombre_act_presupuestario);
							String fuente = "";
							if (resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento != null) {
								for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
										.size(); it++) {
									if (it == 0) {
										fuente = resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
												.get(it).clasificador;
									} else {
										fuente = fuente + ", "
												+ resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
														.get(it).clasificador;
									}
								}
							}

							XSSFRow rowFuente = (XSSFRow) hoja.createRow(filaNum++);
							rowFuente.setHeight((short) 300);
							celda = rowFuente.createCell(0);
							celda.setCellValue("FUENTE: " + fuente);

							String org = "";
							if (resultado.partidas.get(i).partidas.get(j).organismo_financiador != null) {
								for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).organismo_financiador
										.size(); it++) {
									if (it == 0) {
										org = resultado.partidas.get(i).partidas.get(j).organismo_financiador
												.get(it).clasificador;
									} else {
										org = fuente + ", "
												+ resultado.partidas.get(i).partidas.get(j).organismo_financiador
														.get(it).clasificador;
									}
								}
							}

							XSSFRow rowOrgFinanciero = (XSSFRow) hoja.createRow(filaNum++);
							rowOrgFinanciero.setHeight((short) 300);
							celda = rowOrgFinanciero.createCell(0);
							celda.setCellValue("ORG. FIN.: " + org);

							XSSFRow rowPartida = (XSSFRow) hoja.createRow(filaNum++);
							rowPartida.setHeight((short) 300);
							celda = rowPartida.createCell(0);
							celda.setCellValue("PARTIDA: " + resultado.partidas.get(i).partidas.get(j).cod_partida + " "
									+ resultado.partidas.get(i).partidas.get(j).nombre_partida);

							filaNum++;

							XSSFRow rowsubM = (XSSFRow) hoja.createRow(filaNum++);
							col = 0;
							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("Nro.");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("FUENTE DE FINANCIAMIENTO");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("DESCRIPCIÓN");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("CANTIDAD");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("UNIDAD MEDIDA");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("PRECIO UNITARIO");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("IMPORTE TOTAL");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("JUSTIFICACIÓN");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

							celda = rowsubM.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("PROGRAMACIÓN MENSUAL");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, 31));

							celda = rowsubM.createCell(32);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("TOTAL ");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 32, 33));

							String[] meses = { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO",
									"SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE" };

							XSSFRow rowsub1M = (XSSFRow) hoja.createRow(filaNum++);
							int var_j = 8;
							for (int var_i = 0; var_i < meses.length; var_i++) {
								celda = rowsub1M.createCell(var_j);
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue(meses[var_i]);
								hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, var_j, var_j + 1));
								var_j = var_j + 2;
							}
							String[] mesesUn = { "Costo", "Cant." };

							XSSFRow rowsub2 = (XSSFRow) hoja.createRow(filaNum++);
							var_j = 8;
							int var_s = 0;
							for (int var_i = 0; var_i <= (meses.length * 2) + 1; var_i++) {
								celda = rowsub2.createCell(var_j);
								celda.setCellStyle(estiloTituloTabla);
								if (var_j % 2 == 0) {
									var_s = 1;
								} else {
									var_s = 0;
								}
								celda.setCellValue(mesesUn[var_s]);
								var_j++;

							}
							col = 0;
							double[] total_cant = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
							double[] total_cost = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
							for (int k = 0; k < resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.size(); k++) {

								XSSFRow row2 = (XSSFRow) hoja.createRow(filaNum);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(k + 1);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).sigla_fuente_financiamiento);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(
										resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).descripcion);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(
										resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).cantidad);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).unidad_medida);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloNumericoDerecha);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).precio_unitario);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloNumericoDerecha);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).importe_total);
								importe_total = importe_total
										+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
												.get(k).importe_total;

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).justificacion);
								if (resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).programacion != null) {
									for (int l = 0; l < resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.size(); l++) {

										celda = row2.createCell(col);
										celda.setCellStyle(estiloDatos);
										celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
												.get(k).programacion.get(l).cantidad);
										total_cant[l] = total_cant[l]
												+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
														.get(k).programacion.get(l).cantidad;

										celda = row2.createCell(col + 1);
										celda.setCellStyle(estiloNumericoDerecha);
										celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
												.get(k).programacion.get(l).costo);
										total_cost[l] = total_cost[l]
												+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
														.get(k).programacion.get(l).costo;
										col = col + 2;
									}
								}
								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).total_cantidad);
								total_cantidad = total_cantidad
										+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
												.get(k).total_cantidad;

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloNumericoDerecha);
								celda.setCellValue(
										resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_costo);
								total_costo = total_costo
										+ resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_costo;
								col = 0;
								filaNum++;
							}

							XSSFRow rowTotal = (XSSFRow) hoja.createRow(filaNum++);

							celda = rowTotal.createCell(col);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("TOTALES");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 5));
							col = 6;
							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estilonumericoTituloTabladerecha);
							celda.setCellValue(importe_total);

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("");

							for (int l = 0; l < total_cant.length; l++) {

								celda = rowTotal.createCell(col++);
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue(total_cant[l]);

								celda = rowTotal.createCell(col++);
								celda.setCellStyle(estilonumericoTituloTabladerecha);
								celda.setCellValue(total_cost[l]);
							}

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue(total_cantidad);

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estilonumericoTituloTabladerecha);
							celda.setCellValue(total_costo);

							filaNum = filaNum + 4;
							importe_total = 0;
							total_cantidad = 0;
							total_costo = 0;

							XSSFRow rowFirmash3 = (XSSFRow) hoja.createRow(filaNum);

							celda = rowFirmash3.createCell(0);
							celda.setCellStyle(estiloFirma);
							celda.setCellValue("Firma y Sello");
							hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 13));

							celda = rowFirmash3.createCell(14);
							celda.setCellStyle(estiloFirma);
							celda.setCellValue("Firma y Sello");
							hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 14, 32));

							filaNum++;
							filaNum++;
							XSSFRow rowFirmas1h3 = (XSSFRow) hoja.createRow(filaNum);

							celda = rowFirmas1h3.createCell(0);
							celda.setCellStyle(estiloFirma);
							celda.setCellValue("RESPONSABLE DE ELABORACIÓN");
							hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 13));

							celda = rowFirmas1h3.createCell(14);
							celda.setCellStyle(estiloFirma);
							celda.setCellValue("RESPONSABLE DE AUTORIZACIÓN");
							hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 14, 32));

						} else
						////////////////////// PARA CONSULTORIA POR PRODUCTO
						{

							////// Tamaño de columnas
							Integer tamanoColumnas[] = { 7000, 16000, 6000, 6000, 5500, 3500, 4000, 3500, 3500, 3500,
									3500 };

							for (int var = 0; var < tamanoColumnas.length; var++) {
								hoja.setColumnWidth(var, tamanoColumnas[var]);
							}

							XSSFRow headerM = (XSSFRow) hoja.createRow(filaNum++);
							celda = headerM.createCell(0);
							celda.setCellValue("SUBPARTIDA " + resultado.partidas.get(i).partidas.get(j).cod_partida
									+ " " + resultado.partidas.get(i).partidas.get(j).nombre_partida);
							celda.setCellStyle(estiloTitulo);
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));
							filaNum++;

							XSSFRow rowActividad = (XSSFRow) hoja.createRow(filaNum++);
							rowActividad.setHeight((short) 300);
							celda = rowActividad.createCell(0);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("GESTION:");

							rowActividad.setHeight((short) 300);
							celda = rowActividad.createCell(1);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue(resultado.valor_gestion);

							XSSFRow rowFuente = (XSSFRow) hoja.createRow(filaNum++);
							rowFuente.setHeight((short) 300);
							celda = rowFuente.createCell(0);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("ENTIDAD:");

							rowFuente.setHeight((short) 300);
							celda = rowFuente.createCell(1);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("70 - MINISTERIO DE TRABAJO EMPLEO Y PREVISION SOCIAL");

							filaNum++;

							/// TABLA
							// TITULOS

							String titulosTabla[] = { "PRODUCTO", "ÁREA ORGANIZACIONAL QUE VALIDE LA CONSULTORIA",
									"OBJETIVO DE LA CONSULTORIA", "ACTIVIDADES Y FUNCIONES DE LA CONSULTORÍA",
									"RESULTADOS ESPERADOS EN TERM. CUANTITAT. Y/O CUALITAT.",
									"FUENTE DE FINANCIAMIENTO", "ORGANISMO FINANCIADOR",
									"DURACIÓN DEL CONTRATO (Meses)", "Nº DE CASOS", "COSTO MENSUAL (En Bs.)",
									"COSTO TOTAL (En Bs.)" };

							XSSFRow rowsubM = (XSSFRow) hoja.createRow(filaNum++);
							for (int columnaTabla = 0; columnaTabla < titulosTabla.length; columnaTabla++) {

								celda = rowsubM.createCell(columnaTabla);
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue(titulosTabla[columnaTabla]);

							}
							/// DATOS

							double v_total_mensual = 0, v_total_costo_total = 0;
							for (int k = 0; k < resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.size(); k++) {
								col = 0;

								XSSFRow row2 = (XSSFRow) hoja.createRow(filaNum);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(
										resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).descripcion);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.org_unidad_funcional);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).justificacion);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).act_func_consultoria);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).result_consultoria);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).sigla_fuente_financiamiento);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).organismo_financiador);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).duracion_consultoria_meses);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).nro_casos_consultoria);
								///
								double v_cant = 0, v_costo_mensual = 0, v_total_costo_mes = 0, v_total_memoria = 0,
										v_1 = 0, v_2 = 0;
								for (int p = 0; p < resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).programacion.size(); p++) {
									v_1 = (resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.get(p).cantidad != 0
													? resultado.partidas.get(i).partidas.get(j).memorias_calculo
															.get(k).programacion.get(p).cantidad
													: 0);
									v_2 = (resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.get(p).costo != 0
													? resultado.partidas.get(i).partidas.get(j).memorias_calculo
															.get(k).programacion.get(p).costo
													: 0);
									v_cant = v_cant + v_1;
									v_costo_mensual = v_costo_mensual + v_2;
									v_total_costo_mes = v_costo_mensual / v_cant;
									v_total_memoria = v_total_memoria + (v_1 * v_2);
								}

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloNumericoDerecha);
								celda.setCellValue(v_total_costo_mes);

								celda = row2.createCell(col++);
								celda.setCellStyle(estiloNumericoDerecha);
								celda.setCellValue(v_total_memoria);
								v_total_mensual = v_total_mensual + v_total_costo_mes;
								v_total_costo_total = v_total_costo_total + v_total_memoria;

								filaNum++;

							}
							col = 0;
							XSSFRow rowTotal = (XSSFRow) hoja.createRow(filaNum++);

							celda = rowTotal.createCell(col);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("TOTAL");
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 8));
							col = 9;
							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estilonumericoTituloTabladerecha);
							celda.setCellValue(v_total_mensual);

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estilonumericoTituloTabladerecha);
							celda.setCellValue(v_total_costo_total);

							filaNum++;

							String titulosPie[] = { "RESPONSABLES DE LOS COMPROMISOS", "CARGO", "FIRMA" };

							XSSFRow rowsubM1 = (XSSFRow) hoja.createRow(filaNum++);
							for (int columnaTabla = 0; columnaTabla < titulosPie.length; columnaTabla++) {

								celda = rowsubM1.createCell(columnaTabla);
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue(titulosPie[columnaTabla]);
							}
							col = 0;
							XSSFRow rowFirmaMae = (XSSFRow) hoja.createRow(filaNum++);
							celda = rowFirmaMae.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("MAE");

							celda = rowFirmaMae.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");

							celda = rowFirmaMae.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							col = 0;
							XSSFRow rowFirmaRP = (XSSFRow) hoja.createRow(filaNum++);
							celda = rowFirmaRP.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("RESPONSABLE DE PRESUPUESTO");

							celda = rowFirmaRP.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");

							celda = rowFirmaRP.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");

							col = 0;
							XSSFRow rowFirmaRE = (XSSFRow) hoja.createRow(filaNum++);
							celda = rowFirmaRE.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("RESPONSABLE QUE ELABORA");

							celda = rowFirmaRE.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");

							celda = rowFirmaRE.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");

						}

					}
				}
			}
			hsr1.setContentType("application/vnd.ms-excel"); // Tell the browser to expect an excel file
			hsr1.setHeader("Content-Disposition", "attachment; filename=[FORM_PRES_GASTO].xlsx");

		}

	}

//////REPORTE 1 FORMULACION PRESUPUESTO GASTO

	@RequestMapping(path = "/formPresupGasto", method = RequestMethod.GET)
	public ResponseEntity<byte[]> formPresupGasto2(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_form_presupuesto_gasto");

		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));

		F_reporte_form_presupuesto_gasto resultado = (F_reporte_form_presupuesto_gasto) procedure.getSingleResult();

		String archivo = datosEntrada.getRealPath("") + "/assets/poa/docVacio.xlsx";
		// "C:/Users/MTEPS/Documents/workspace-spring-tool-suite-4-4.9.0.RELEASE/backend-mteps/src/main/resources/WEB-INF/pruebaSE.xlsx";

		FileInputStream inputStream = new FileInputStream(new File(archivo));
		XSSFWorkbook hssfw = (XSSFWorkbook) WorkbookFactory.create(inputStream);

		Sheet sheet = hssfw.getSheetAt(0);
		hssfw.setSheetName(0, "FORMULACIÓN PRESUPUESTO DE GASTO");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;

		////// Tamaño de columnas
		Integer tamanoColumna[] = { 3400, 20000, 3000, 4200, 4200 };

		for (int i = 0; i < tamanoColumna.length; i++) {
			sheet.setColumnWidth(i, tamanoColumna[i]);
		}

		////// estilos texto normal
		CellStyle estiloSuperiorLateral = hssfw.createCellStyle();
		estiloSuperiorLateral = repo.estiloSuperiorLateral(estiloSuperiorLateral, hssfw, false, "RIGHT");

		////// estilos titulo
		CellStyle estiloTitulo = hssfw.createCellStyle();
		estiloTitulo = repo.estiloTitulo(estiloTitulo, hssfw);

		////// estilos texto normal
		CellStyle estiloTextoNormal = hssfw.createCellStyle();
		estiloTextoNormal = repo.estiloTextoNormal(estiloTextoNormal, hssfw, false);

		CellStyle estiloTextoNormalNegrita = hssfw.createCellStyle();
		estiloTextoNormalNegrita = repo.estiloTextoNormal(estiloTextoNormalNegrita, hssfw, true);

		////// estilos Titulos Tabla
		CellStyle estiloTituloTabla = hssfw.createCellStyle();
		estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

		////// estilos Titulos Tabla
		XSSFCellStyle estiloSubTituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
		estiloSubTituloTabla = (XSSFCellStyle) repo.estiloSubTituloTabla(estiloSubTituloTabla, hssfw);

		////// estilos Titulos Tabla
		XSSFCellStyle estiloSubTituloTablaCentro = (XSSFCellStyle) hssfw.createCellStyle();
		estiloSubTituloTablaCentro = (XSSFCellStyle) repo.estiloSubTituloTablaCentro(estiloSubTituloTablaCentro, hssfw);

		////// estilos datos
		CellStyle estiloDatos = hssfw.createCellStyle();
		estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

		////// estilos datos
		CellStyle estiloDatosJustificado = hssfw.createCellStyle();
		estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

		////// estilos datos
		CellStyle estiloFirma = hssfw.createCellStyle();
		estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

		////// estilos numerico Titulos Tabla
		XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
				.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

		////// estilos numerico sub Titulos Tabla
		XSSFCellStyle estilonumericoSubTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estilonumericoSubTituloTabladerecha = (XSSFCellStyle) repo
				.estiloNumericoSubTituloTablaDerecha(estilonumericoSubTituloTabladerecha, hssfw);

		////// estilos numerico tabla
		CellStyle estiloNumericoDerecha = hssfw.createCellStyle();
		estiloNumericoDerecha = repo.estiloNumericoTablaDerecha(estiloNumericoDerecha, hssfw);

		String tipInd = "", tipIndvalor = "";

		///////// ESTILO NUMERICO
		CellStyle estiloNumero = hssfw.createCellStyle();
		DataFormat formatoNumero = hssfw.createDataFormat();
		estiloNumero = repo.estiloNumericoTabla(estiloNumero, hssfw);

		///// IMAGEN
		String rutaImg = datosEntrada.getRealPath("") + "/assets/logo2.jpg";
		InputStream inputStream1 = new FileInputStream(rutaImg);
		byte[] bytes = IOUtils.toByteArray(inputStream1);
		int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();

		CreationHelper helper = hssfw.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
		////

		XSSFRow header = (XSSFRow) sheet.createRow(filaNum++);
		celda = header.createCell(0);
		celda.setCellValue("CUADRO RESUMEN");
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

		XSSFRow header1 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header1.createCell(0);
		celda.setCellValue("FORMULACIÓN DEL PRESUPUESTO DE GASTO");
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

		XSSFRow header3 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header3.createCell(0);
		celda.setCellValue("GESTIÓN " + resultado.valor_gestion);
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

		XSSFRow header2 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header2.createCell(0);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));
		filaNum++;
		XSSFRow header4 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header4.createCell(0);
		celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
		celda.setCellStyle(estiloTextoNormal);

		XSSFRow header5 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header5.createCell(0);
		celda.setCellValue("PROGRAMA: " + resultado.nombre_prog_presupuestario);
		celda.setCellStyle(estiloTextoNormal);

		filaNum++;
		int col = 0;
		XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("PARTIDA DE GASTO");
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
		col++;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("DENOMINACIÓN");
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
		col++;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("FUENTE DE FINANCIAMIENTO");
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, col + 1));
		col++;
		col++;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("IMPORTE TOTAL");
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));

		XSSFRow rowsub1 = (XSSFRow) sheet.createRow(filaNum);

		celda = rowsub1.createCell(col - 2);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("10 - 111 T.G.N.");

		celda = rowsub1.createCell(col - 1);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("11 - 0 T.G.N.");
		filaNum++;
		col = 0;
		if (resultado.partidas != null) {
			for (int i = 0; i < resultado.partidas.size(); i++) {

				XSSFRow row1 = (XSSFRow) sheet.createRow(filaNum);
				celda = row1.createCell(col++);
				celda.setCellStyle(estiloSubTituloTabla);
				celda.setCellValue(resultado.partidas.get(i).cod_partida);

				celda = row1.createCell(col++);
				celda.setCellStyle(estiloSubTituloTabla);
				celda.setCellValue(resultado.partidas.get(i).nombre_partida);

				celda = row1.createCell(col++);
				celda.setCellStyle(estilonumericoSubTituloTabladerecha);
				celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn);

				celda = row1.createCell(col++);
				celda.setCellStyle(estilonumericoSubTituloTabladerecha);
				celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn_otros);

				celda = row1.createCell(col++);
				celda.setCellStyle(estilonumericoSubTituloTabladerecha);
				celda.setCellValue(resultado.partidas.get(i).fuente_financiamiento_tgn_total);
				filaNum++;
				col = 0;
				if (resultado.partidas.get(i).partidas != null) {
					for (int j = 0; j < resultado.partidas.get(i).partidas.size(); j++) {

						XSSFRow row2 = (XSSFRow) sheet.createRow(filaNum);
						celda = row2.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.partidas.get(i).partidas.get(j).cod_partida);

						celda = row2.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.partidas.get(i).partidas.get(j).nombre_partida);

						celda = row2.createCell(col++);
						celda.setCellStyle(estiloNumericoDerecha);
						celda.setCellValue(resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn);

						celda = row2.createCell(col++);
						celda.setCellStyle(estiloNumericoDerecha);
						celda.setCellValue(resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn_otros);

						celda = row2.createCell(col++);
						celda.setCellStyle(estiloNumericoDerecha);
						celda.setCellValue(resultado.partidas.get(i).partidas.get(j).fuente_financiamiento_tgn_total);
						filaNum++;
						col = 0;
					}
				}
				col = 0;

			}
		}
		XSSFRow rowPie = (XSSFRow) sheet.createRow(filaNum);

		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		sheet.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 1));
		col++;
		col++;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn);
		col++;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn_otros);
		col++;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(resultado.totales.total_fuente_financiamiento_tgn_total);

		//////////////////////// MEMORIAS DE CALCULO DETALLE

		int numHoja = 1;
		double importe_total = 0;
		double total_cantidad = 0;
		double total_costo = 0;
		if (resultado.partidas != null) {
			for (int i = 0; i < resultado.partidas.size(); i++) {

				for (int j = 0; j < resultado.partidas.get(i).partidas.size(); j++) {

					Sheet hoja = hssfw.createSheet();
					hssfw.setSheetName(numHoja,
							numHoja + " - " + resultado.partidas.get(i).partidas.get(j).cod_partida);
					numHoja++;
					filaNum = 0;

					if (resultado.partidas.get(i).partidas.get(j).id_partida_presupuesto != 58) {

						////// Tamaño de columnas
						Integer tamanoColumnas[] = { 2500, 3000, 8000, 2600, 3000, 3000, 3500, 8000 };

						for (int var = 0; var < tamanoColumnas.length; var++) {
							hoja.setColumnWidth(var, tamanoColumnas[var]);
						}

						XSSFRow headerM = (XSSFRow) hoja.createRow(filaNum++);
						celda = headerM.createCell(0);
						celda.setCellValue("FORMULACIÓN DE ANTEPROYECTO DE PRESUPUESTO " + resultado.valor_gestion);
						celda.setCellStyle(estiloTitulo);
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

						XSSFRow header1M = (XSSFRow) hoja.createRow(filaNum++);
						celda = header1M.createCell(0);
						celda.setCellValue("MEMORIA DE CALCULO");
						celda.setCellStyle(estiloTitulo);
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

						XSSFRow header2M = (XSSFRow) hoja.createRow(filaNum++);
						celda = header2M.createCell(0);
						celda.setCellValue("(Expresado en Bolivianos)");
						celda.setCellStyle(estiloTitulo);
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 32));

						filaNum++;

						XSSFRow rowActividad = (XSSFRow) hoja.createRow(filaNum++);
						rowActividad.setHeight((short) 300);
						celda = rowActividad.createCell(0);
						celda.setCellValue("ACTIVIDAD: " + resultado.sigla_act_presupuestario + " "
								+ resultado.nombre_act_presupuestario);
						String fuente = "";
						if (resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento != null) {
							for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
									.size(); it++) {
								if (it == 0) {
									fuente = resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
											.get(it).clasificador;
								} else {
									fuente = fuente + ", "
											+ resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento
													.get(it).clasificador;
								}
							}
						}

						XSSFRow rowFuente = (XSSFRow) hoja.createRow(filaNum++);
						rowFuente.setHeight((short) 300);
						celda = rowFuente.createCell(0);
						celda.setCellValue("FUENTE: " + fuente);

						String org = "";
						if (resultado.partidas.get(i).partidas.get(j).organismo_financiador != null) {
							for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).organismo_financiador
									.size(); it++) {
								if (it == 0) {
									org = resultado.partidas.get(i).partidas.get(j).organismo_financiador
											.get(it).clasificador;
								} else {
									org = fuente + ", "
											+ resultado.partidas.get(i).partidas.get(j).organismo_financiador
													.get(it).clasificador;
								}
							}
						}

						XSSFRow rowOrgFinanciero = (XSSFRow) hoja.createRow(filaNum++);
						rowOrgFinanciero.setHeight((short) 300);
						celda = rowOrgFinanciero.createCell(0);
						celda.setCellValue("ORG. FIN.: " + org);

						XSSFRow rowPartida = (XSSFRow) hoja.createRow(filaNum++);
						rowPartida.setHeight((short) 300);
						celda = rowPartida.createCell(0);
						celda.setCellValue("PARTIDA: " + resultado.partidas.get(i).partidas.get(j).cod_partida + " "
								+ resultado.partidas.get(i).partidas.get(j).nombre_partida);

						filaNum++;

						XSSFRow rowsubM = (XSSFRow) hoja.createRow(filaNum++);
						col = 0;
						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("Nro.");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("FUENTE DE FINANCIAMIENTO");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("DESCRIPCIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("CANTIDAD");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("UNIDAD MEDIDA");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("PRECIO UNITARIO");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("IMPORTE TOTAL");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("JUSTIFICACIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col - 1, col - 1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("PROGRAMACIÓN MENSUAL");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, 31));

						celda = rowsubM.createCell(32);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("TOTAL ");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 32, 33));

						String[] meses = { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO",
								"SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE" };

						XSSFRow rowsub1M = (XSSFRow) hoja.createRow(filaNum++);
						int var_j = 8;
						for (int var_i = 0; var_i < meses.length; var_i++) {
							celda = rowsub1M.createCell(var_j);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue(meses[var_i]);
							hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, var_j, var_j + 1));
							var_j = var_j + 2;
						}
						String[] mesesUn = { "Costo", "Cant." };

						XSSFRow rowsub2 = (XSSFRow) hoja.createRow(filaNum++);
						var_j = 8;
						int var_s = 0;
						for (int var_i = 0; var_i <= (meses.length * 2) + 1; var_i++) {
							celda = rowsub2.createCell(var_j);
							celda.setCellStyle(estiloTituloTabla);
							if (var_j % 2 == 0) {
								var_s = 1;
							} else {
								var_s = 0;
							}
							celda.setCellValue(mesesUn[var_s]);
							var_j++;

						}
						col = 0;
						double[] total_cant = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
						double[] total_cost = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
						for (int k = 0; k < resultado.partidas.get(i).partidas.get(j).memorias_calculo.size(); k++) {

							XSSFRow row2 = (XSSFRow) hoja.createRow(filaNum);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(k + 1);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).sigla_fuente_financiamiento);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).descripcion);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).cantidad);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).unidad_medida);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).precio_unitario);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).importe_total);
							importe_total = importe_total
									+ resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).importe_total;

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).justificacion);
							if (resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).programacion != null) {
								for (int l = 0; l < resultado.partidas.get(i).partidas.get(j).memorias_calculo
										.get(k).programacion.size(); l++) {

									celda = row2.createCell(col);
									celda.setCellStyle(estiloDatos);
									celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.get(l).cantidad);
									total_cant[l] = total_cant[l]
											+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
													.get(k).programacion.get(l).cantidad;

									celda = row2.createCell(col + 1);
									celda.setCellStyle(estiloNumericoDerecha);
									celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.get(l).costo);
									total_cost[l] = total_cost[l]
											+ resultado.partidas.get(i).partidas.get(j).memorias_calculo
													.get(k).programacion.get(l).costo;
									col = col + 2;
								}
							}
							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_cantidad);
							total_cantidad = total_cantidad
									+ resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_cantidad;

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_costo);
							total_costo = total_costo
									+ resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).total_costo;
							col = 0;
							filaNum++;
						}

						XSSFRow rowTotal = (XSSFRow) hoja.createRow(filaNum++);

						celda = rowTotal.createCell(col);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("TOTALES");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 5));
						col = 6;
						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estilonumericoTituloTabladerecha);
						celda.setCellValue(importe_total);

						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("");

						for (int l = 0; l < total_cant.length; l++) {

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue(total_cant[l]);

							celda = rowTotal.createCell(col++);
							celda.setCellStyle(estilonumericoTituloTabladerecha);
							celda.setCellValue(total_cost[l]);
						}

						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue(total_cantidad);

						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estilonumericoTituloTabladerecha);
						celda.setCellValue(total_costo);

						filaNum = filaNum + 4;
						importe_total = 0;
						total_cantidad = 0;
						total_costo = 0;

						XSSFRow rowFirmash3 = (XSSFRow) hoja.createRow(filaNum);

						celda = rowFirmash3.createCell(0);
						celda.setCellStyle(estiloFirma);
						celda.setCellValue("Firma y Sello");
						hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 13));

						celda = rowFirmash3.createCell(14);
						celda.setCellStyle(estiloFirma);
						celda.setCellValue("Firma y Sello");
						hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 14, 32));

						filaNum++;
						filaNum++;
						XSSFRow rowFirmas1h3 = (XSSFRow) hoja.createRow(filaNum);

						celda = rowFirmas1h3.createCell(0);
						celda.setCellStyle(estiloFirma);
						celda.setCellValue("RESPONSABLE DE ELABORACIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 13));

						celda = rowFirmas1h3.createCell(14);
						celda.setCellStyle(estiloFirma);
						celda.setCellValue("RESPONSABLE DE AUTORIZACIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 14, 32));

					} else
					////////////////////// PARA CONSULTORIA POR PRODUCTO
					{

						////// Tamaño de columnas
						Integer tamanoColumnas[] = { 7000, 16000, 6000, 6000, 5500, 3500, 4000, 3500, 3500, 3500,
								3500 };

						for (int var = 0; var < tamanoColumnas.length; var++) {
							hoja.setColumnWidth(var, tamanoColumnas[var]);
						}

						XSSFRow headerM = (XSSFRow) hoja.createRow(filaNum++);
						celda = headerM.createCell(0);
						celda.setCellValue("SUBPARTIDA " + resultado.partidas.get(i).partidas.get(j).cod_partida + " "
								+ resultado.partidas.get(i).partidas.get(j).nombre_partida);
						celda.setCellStyle(estiloTitulo);
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));
						filaNum++;

						XSSFRow rowActividad = (XSSFRow) hoja.createRow(filaNum++);
						rowActividad.setHeight((short) 300);
						celda = rowActividad.createCell(0);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("GESTION:");

						rowActividad.setHeight((short) 300);
						celda = rowActividad.createCell(1);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.valor_gestion);

						XSSFRow rowFuente = (XSSFRow) hoja.createRow(filaNum++);
						rowFuente.setHeight((short) 300);
						celda = rowFuente.createCell(0);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("ENTIDAD:");

						rowFuente.setHeight((short) 300);
						celda = rowFuente.createCell(1);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("70 - MINISTERIO DE TRABAJO EMPLEO Y PREVISION SOCIAL");

						filaNum++;

						/// TABLA
						// TITULOS

						String titulosTabla[] = { "PRODUCTO", "ÁREA ORGANIZACIONAL QUE VALIDE LA CONSULTORIA",
								"OBJETIVO DE LA CONSULTORIA", "ACTIVIDADES Y FUNCIONES DE LA CONSULTORÍA",
								"RESULTADOS ESPERADOS EN TERM. CUANTITAT. Y/O CUALITAT.", "FUENTE DE FINANCIAMIENTO",
								"ORGANISMO FINANCIADOR", "DURACIÓN DEL CONTRATO (Meses)", "Nº DE CASOS",
								"COSTO MENSUAL (En Bs.)", "COSTO TOTAL (En Bs.)" };

						XSSFRow rowsubM = (XSSFRow) hoja.createRow(filaNum++);
						for (int columnaTabla = 0; columnaTabla < titulosTabla.length; columnaTabla++) {

							celda = rowsubM.createCell(columnaTabla);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue(titulosTabla[columnaTabla]);

						}
						/// DATOS

						double v_total_mensual = 0, v_total_costo_total = 0;
						for (int k = 0; k < resultado.partidas.get(i).partidas.get(j).memorias_calculo.size(); k++) {
							col = 0;

							XSSFRow row2 = (XSSFRow) hoja.createRow(filaNum);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).descripcion);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.org_unidad_funcional);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).justificacion);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).act_func_consultoria);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).result_consultoria);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).sigla_fuente_financiamiento);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).organismo_financiador);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).duracion_consultoria_meses);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).nro_casos_consultoria);
							///
							double v_cant = 0, v_costo_mensual = 0, v_total_costo_mes = 0, v_total_memoria = 0, v_1 = 0,
									v_2 = 0;
							for (int p = 0; p < resultado.partidas.get(i).partidas.get(j).memorias_calculo
									.get(k).programacion.size(); p++) {
								v_1 = (resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).programacion
										.get(p).cantidad != 0
												? resultado.partidas.get(i).partidas.get(j).memorias_calculo
														.get(k).programacion.get(p).cantidad
												: 0);
								v_2 = (resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).programacion
										.get(p).costo != 0
												? resultado.partidas.get(i).partidas.get(j).memorias_calculo
														.get(k).programacion.get(p).costo
												: 0);
								v_cant = v_cant + v_1;
								v_costo_mensual = v_costo_mensual + v_2;
								v_total_costo_mes = v_costo_mensual / v_cant;
								v_total_memoria = v_total_memoria + (v_1 * v_2);
							}

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(v_total_costo_mes);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloNumericoDerecha);
							celda.setCellValue(v_total_memoria);
							v_total_mensual = v_total_mensual + v_total_costo_mes;
							v_total_costo_total = v_total_costo_total + v_total_memoria;

							filaNum++;

						}
						col = 0;
						XSSFRow rowTotal = (XSSFRow) hoja.createRow(filaNum++);

						celda = rowTotal.createCell(col);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("TOTAL");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 8));
						col = 9;
						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estilonumericoTituloTabladerecha);
						celda.setCellValue(v_total_mensual);

						celda = rowTotal.createCell(col++);
						celda.setCellStyle(estilonumericoTituloTabladerecha);
						celda.setCellValue(v_total_costo_total);

						filaNum++;

						String titulosPie[] = { "RESPONSABLES DE LOS COMPROMISOS", "CARGO", "FIRMA" };

						XSSFRow rowsubM1 = (XSSFRow) hoja.createRow(filaNum++);
						for (int columnaTabla = 0; columnaTabla < titulosPie.length; columnaTabla++) {

							celda = rowsubM1.createCell(columnaTabla);
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue(titulosPie[columnaTabla]);
						}
						col = 0;
						XSSFRow rowFirmaMae = (XSSFRow) hoja.createRow(filaNum++);
						celda = rowFirmaMae.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("MAE");

						celda = rowFirmaMae.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");

						celda = rowFirmaMae.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");
						col = 0;
						XSSFRow rowFirmaRP = (XSSFRow) hoja.createRow(filaNum++);
						celda = rowFirmaRP.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("RESPONSABLE DE PRESUPUESTO");

						celda = rowFirmaRP.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");

						celda = rowFirmaRP.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");

						col = 0;
						XSSFRow rowFirmaRE = (XSSFRow) hoja.createRow(filaNum++);
						celda = rowFirmaRE.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("RESPONSABLE QUE ELABORA");

						celda = rowFirmaRE.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");

						celda = rowFirmaRE.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue("");

					}

				}
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[FOR_PRE_GASTO].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

	////////////////////////////////////////////////////
	// REPORTE 1 MEMORIA DE CALCULO por tarea especifica

	@GetMapping("/memoriacalculo")
	public ResponseEntity<byte[]> reporteMemoriaCalculoExcel(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_memoria_calculo");

		procedure.setParameter("v_id_org", Integer.parseInt(datosEntrada.getParameter("idUnidadOrg")));

		List<ReporteMemoriaCalculo> resultado = procedure.getResultList();

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet sheet = hssfw.createSheet();
		hssfw.setSheetName(0, "FORMULACIÓN DE ANTEPROYECTO DE PRESUPUESTO - 2022");
		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 1;

		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 2700);
		sheet.setColumnWidth(2, 2700);
		sheet.setColumnWidth(3, 2700);
		sheet.setColumnWidth(4, 2700);
		sheet.setColumnWidth(5, 2700);
		sheet.setColumnWidth(6, 2700);

		////// estilos titulo
		CellStyle estiloTitulo = hssfw.createCellStyle();
		org.apache.poi.ss.usermodel.Font fuenteTitulo = hssfw.createFont();

		fuenteTitulo.setFontHeightInPoints((short) 16);
		fuenteTitulo.setFontName("Arial");
		fuenteTitulo.setBold(true);
		estiloTitulo.setFont(fuenteTitulo);
		estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
		estiloTitulo.setAlignment(HorizontalAlignment.CENTER);

		////// estilos subtitulos
		CellStyle style = hssfw.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.RED.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setWrapText(true);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		org.apache.poi.ss.usermodel.Font fuenteSubtitulo = hssfw.createFont();
		fuenteSubtitulo.setFontHeightInPoints((short) 8);
		fuenteSubtitulo.setFontName("Arial");
		fuenteSubtitulo.setBold(true);
		fuenteSubtitulo.setColor(IndexedColors.WHITE.index);
		style.setFont(fuenteSubtitulo);

		////// estilos datos
		CellStyle estiloDatos = hssfw.createCellStyle();
		estiloDatos.setAlignment(HorizontalAlignment.CENTER);
		estiloDatos.setWrapText(true);
		estiloDatos.setVerticalAlignment(VerticalAlignment.CENTER);

		org.apache.poi.ss.usermodel.Font fuenteDatos = hssfw.createFont();
		fuenteDatos.setFontHeightInPoints((short) 10);
		fuenteDatos.setFontName("Arial");
		estiloDatos.setFont(fuenteDatos);

		///// IMAGEN
		String rutaImg = datosEntrada.getRealPath("") + "/assets/logo2.jpg";
		InputStream inputStream = new FileInputStream(rutaImg);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		CreationHelper helper = hssfw.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setDx1(0);
		anchor.setDy1(1);
		anchor.setDx2(1);
		anchor.setDy2(1);
		anchor.setCol1(1);
		anchor.setRow1(1);
		anchor.setCol2(2);
		anchor.setRow2(2);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
		///////////////////////

		XSSFRow header = (XSSFRow) sheet.createRow(filaNum++);
		celda = header.createCell(15);
		celda.setCellValue("FORMULACIÓN DE ANTEPROYECTO DE PRESUPUESTO - 2022");
		celda.setCellStyle(estiloTitulo);

		XSSFRow header1 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header1.createCell(15);
		celda.setCellValue("MEMORIA DE CALCULO");
		celda.setCellStyle(estiloTitulo);

		XSSFRow header2 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header2.createCell(15);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo);

		CellRangeAddress cellMerge = new CellRangeAddress(0, 0, 0, 12);
		sheet.addMergedRegion(cellMerge);
		header.setHeight((short) 500);

		Integer cont = 0;

		for (Object o : resultado) {
			cont++;
			ReporteMemoriaCalculo datos = (ReporteMemoriaCalculo) o;
			filaNum++;

			XSSFRow rowActividad = (XSSFRow) sheet.createRow(filaNum++);
			rowActividad.setHeight((short) 300);
			celda = rowActividad.createCell(0);
			celda.setCellValue(
					"ACTIVIDAD: " + datos.sigla_actividad_presupuestaria + " " + datos.nombre_actividad_presupuestaria);

			XSSFRow rowFuente = (XSSFRow) sheet.createRow(filaNum++);
			rowFuente.setHeight((short) 300);
			celda = rowFuente.createCell(0);
			celda.setCellValue("FUENTE: " + datos.fuente_financiamiento);

			XSSFRow rowOrg = (XSSFRow) sheet.createRow(filaNum++);
			rowOrg.setHeight((short) 300);
			celda = rowOrg.createCell(0);
			celda.setCellValue("ORG. FIN.: " + datos.org_fin);

			XSSFRow rowPartida = (XSSFRow) sheet.createRow(filaNum++);
			rowPartida.setHeight((short) 300);
			celda = rowPartida.createCell(0);
			celda.setCellValue("PARTIDA: " + datos.cod_partida + " " + datos.partida);

			XSSFRow rowTarea = (XSSFRow) sheet.createRow(filaNum++);
			rowTarea.setHeight((short) 300);
			celda = rowTarea.createCell(0);
			celda.setCellValue("TAREA ESPECIFICA: " + datos.nombre_te + " " + datos.descripcion_te);

			filaNum++;

			XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowsub.createCell(0);
			celda.setCellStyle(style);
			celda.setCellValue("Nro.");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 0, 0));

			celda = rowsub.createCell(1);
			celda.setCellStyle(style);
			celda.setCellValue("DESCRIPCION");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 1, 1));

			celda = rowsub.createCell(2);
			celda.setCellStyle(style);
			celda.setCellValue("CANTIDAD");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 2, 2));

			celda = rowsub.createCell(3);
			celda.setCellStyle(style);
			celda.setCellValue("UNIDAD MEDIDA");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 3, 3));

			celda = rowsub.createCell(4);
			celda.setCellStyle(style);
			celda.setCellValue("PRECIO UNITARIO");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 4, 4));

			celda = rowsub.createCell(5);
			celda.setCellStyle(style);
			celda.setCellValue("IMPORTE TOTAL");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 5, 5));

			celda = rowsub.createCell(6);
			celda.setCellStyle(style);
			celda.setCellValue("JUSTIFICACION");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 6, 6));

			celda = rowsub.createCell(7);
			celda.setCellStyle(style);
			celda.setCellValue("PROGRAMACION MENSUAL");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 7, 30));

			celda = rowsub.createCell(31);
			celda.setCellStyle(style);
			celda.setCellValue("TOTAL ");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 31, 32));

			String[] meses = { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE",
					"OCTUBRE", "NOVIEMBRE", "DICIEMBRE" };

			XSSFRow rowsub1 = (XSSFRow) sheet.createRow(filaNum++);
			int j = 7;
			for (int i = 0; i < meses.length; i++) {
				celda = rowsub1.createCell(j);
				celda.setCellStyle(style);
				celda.setCellValue(meses[i]);
				sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, j, j + 1));
				j = j + 2;
			}

			String[] mesesUn = { "Cant.", "Costo" };

			XSSFRow rowsub2 = (XSSFRow) sheet.createRow(filaNum++);
			j = 7;
			int s = 0;
			for (int i = 0; i <= (meses.length * 2) + 1; i++) {
				celda = rowsub2.createCell(j);
				celda.setCellStyle(style);
				if (j % 2 == 0) {
					s = 1;
				} else {
					s = 0;
				}
				celda.setCellValue(mesesUn[s]);
				j++;

			}
			///////////////////////////// DATOS

			XSSFRow row = (XSSFRow) sheet.createRow(filaNum++);
			row.setHeight((short) 500);

			celda = row.createCell(0);
			celda.setCellValue(cont);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(1);
			celda.setCellValue(datos.descripcion_memoria_calculo);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(2);
			celda.setCellValue(datos.cantidad_memoria_calculo);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(3);
			celda.setCellValue(datos.unidad_medida_memoria_calculo);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(4);
			celda.setCellValue(datos.precio_unitario_memoria_calculo);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(5);
			celda.setCellValue(datos.importe_total_memoria_calculo);
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(6);
			celda.setCellValue(datos.justificacion_memoria_calculo);
			celda.setCellStyle(estiloDatos);
			int jCC = 7;
			for (int iCC = 0; iCC < datos.detalle_memoria_calculo.size(); iCC++) {

				celda = row.createCell(jCC);
				celda.setCellValue(datos.detalle_memoria_calculo.get(iCC).cantidad == null ? 0
						: datos.detalle_memoria_calculo.get(iCC).cantidad);
				celda.setCellStyle(estiloDatos);

				celda = row.createCell(jCC + 1);
				celda.setCellValue(datos.detalle_memoria_calculo.get(iCC).costo == null ? 0
						: datos.detalle_memoria_calculo.get(iCC).costo);
				celda.setCellStyle(estiloDatos);
				jCC = jCC + 2;
			}

			XSSFRow rowTotal = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowTotal.createCell(0);
			celda.setCellStyle(style);
			celda.setCellValue("TOTALES");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 3));
			for (int i = 4; i < 33; i++) {
				celda = rowTotal.createCell(i);
				celda.setCellStyle(style);
				celda.setCellValue("0");
			}
			filaNum++;
		}

		/////////////////////// END DATOS
		//////////////////////// TITULOS INFERIORES

		//////////////////////////////////
		filaNum++;
		filaNum++;
		filaNum++;
		XSSFRow firma1 = (XSSFRow) sheet.createRow(filaNum++);
		celda = firma1.createCell(8);
		celda.setCellValue("Responsable elaboración");
		celda.setCellStyle(estiloTitulo);

		celda = firma1.createCell(21);
		celda.setCellValue("Responsable Autorización");
		celda.setCellStyle(estiloTitulo);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[MEMORIA_DE_CALCULO].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;
	}

/////////////////////// REPORTE 2 SEGUIMIENTO PRESUPUESTARIO
	@GetMapping("/seguimientoPresupuestario")
	public ResponseEntity<byte[]> reporteSeguimientoPresupuestarioExcel(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<F_ejecucion_seguimiento_presupuestario> resultado = poaCore
				.f_ejecucion_seguimiento_presupuestario(Integer.parseInt(datosEntrada.getParameter("idPlan")));

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "SEGUIMIENTO PRESUPUESTARIO");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 2700, 13700, 3000, 10700, 3000, 3200, 3200, 3200, 3200, 3200 };

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
		celda = header.createCell(3);
		celda.setCellValue("SEGUIMIENTO PRESUPUESTARIO");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(3);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;

/////////////////////// CABECERA			

		String fuente = "", organismo = "";
		if (resultado.get(0).fuente_f != null) {
			for (int i = 0; i < resultado.get(0).fuente_f.size(); i++) {
				if (i == 0) {
					fuente = resultado.get(0).fuente_f.get(i).clasificador;
				} else {
					fuente = fuente + ", " + resultado.get(0).fuente_f.get(i).clasificador;
				}
			}
		}
		if (resultado.get(0).organismo_f != null) {
			for (int i = 0; i < resultado.get(0).organismo_f.size(); i++) {
				if (i == 0) {
					organismo = resultado.get(0).organismo_f.get(i).clasificador;
				} else {
					organismo = organismo + ", " + resultado.get(0).organismo_f.get(i).clasificador;
				}
			}
		}

		XSSFRow Dato1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato1.createCell(0);
		celda.setCellValue("Actividad: " + resultado.get(0).nombre_act);
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		XSSFRow Dato2 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato2.createCell(0);
		celda.setCellValue("Fuente: " + fuente);
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		XSSFRow Dato3 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato3.createCell(0);
		celda.setCellValue("Org. Financiador: " + organismo);
		celda.setCellStyle(estiloTextoNormal);

		filaNum++;
		filaNum++;
/////////////////////////////CABECERA TABLA

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		String tituloTabla[] = { "Código TE.", "Tarea Específica (TE)", "Código partida", "Detalle partida",
				"Presupuesto Inicial", "Modificaciones Aprobadas", "Presupuesto Vigente", "Preventivo", "Saldo" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}
		int col = 0;
		double[] total = { 0, 0, 0, 0, 0 };
///////////////////////////// DATOS TABLA		
		if (!resultado.isEmpty()) {
			for (int i = 0; i < resultado.size(); i++) {
				if (resultado.get(i).detalle != null) {

					col = 0;
					int fil = filaNum;
					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultado.get(i).te_sigla);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).te_descripcion);

					for (int j = 0; j < resultado.get(i).detalle.size(); j++) {
						if (j > 0) {
							col = 2;
							row = (XSSFRow) hoja1.createRow(filaNum++);
						}

						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.get(i).detalle.get(j).cod_partida);

						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.get(i).detalle.get(j).partida);

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(
								Math.round(resultado.get(i).detalle.get(j).presupuesto_inicial * 100d) / 100d);
						total[0] = total[0]
								+ Math.round(resultado.get(i).detalle.get(j).presupuesto_inicial * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(
								Math.round(resultado.get(i).detalle.get(j).modificacion_aprobada * 100d) / 100d);
						total[1] = total[1]
								+ Math.round(resultado.get(i).detalle.get(j).modificacion_aprobada * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(
								Math.round(resultado.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d);
						total[2] = total[2]
								+ Math.round(resultado.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(Math.round(resultado.get(i).detalle.get(j).preventivo * 100d) / 100d);
						total[3] = total[3] + Math.round(resultado.get(i).detalle.get(j).preventivo * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(Math.round(resultado.get(i).detalle.get(j).saldo * 100d) / 100d);
						total[4] = total[4] + Math.round(resultado.get(i).detalle.get(j).saldo * 100d) / 100d;
					}

					if (resultado.get(i).detalle.size() > 1) {
						repo.regionConBorde(hoja1,
								new CellRangeAddress(fil, fil + resultado.get(i).detalle.size() - 1, 0, 0));

						repo.regionConBorde(hoja1,
								new CellRangeAddress(fil, fil + resultado.get(i).detalle.size() - 1, 1, 1));
					}

				}
			}
		}

		// total
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum);
		col = 0;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 3));
		col = col + 4;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[0]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[1]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[2]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[3]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[4]);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[EJ_SEG_PRESUP]_"
				+ StringUtils.abbreviate(resultado.get(0).nombre_act.replace(".", "").replace(" ", "_"), 20) + ".xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 3 EJECUCION PRESUPUESTARIA CONSOLIDADA

	@GetMapping("/ejecucionPresupuestariaConsolidada")
	public ResponseEntity<byte[]> ejecucionPresupuestariaConsolidada(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<F_ejecucion_presupuestaria_consolidada> resultado = poaCore
				.f_ejecucion_presupuestaria_consolidada(Integer.parseInt(datosEntrada.getParameter("idGestion")));

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "EJECUCIÓN PRESUPUESTARIA CONSOLIDADA");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 2700, 10000, 3200, 3700, 2800, 3400, 2800 };

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
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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
		celda = header.createCell(3);
		celda.setCellValue("EJECUCIÓN PRESUPUESTARIA CONSOLIDADA");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(3);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;

/////////////////////////////CABECERA TABLA

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		String tituloTabla[] = { "Código", "Apertura Programática", "Presupuesto Inicial", "Modificación aprobada",
				"Presupuesto Vigente", "Preventivo", "Saldo" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}

///////////////////////////// DATOS TABLA		

		int col = 0;
		double[] total = { 0, 0, 0, 0, 0 };
		for (int i = 0; i < resultado.size(); i++) {
			col = 0;
			XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).sigla);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.get(i).nombre);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).presup_inicial * 100d) / 100d);
			total[0] = total[0] + Math.round(resultado.get(i).presup_inicial * 100d) / 100d;

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).modificacion_aprobada * 100d) / 100d);
			total[1] = total[1] + Math.round(resultado.get(i).modificacion_aprobada * 100d) / 100d;

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).presupuesto_vigente * 100d) / 100d);
			total[2] = total[2] + Math.round(resultado.get(i).presupuesto_vigente * 100d) / 100d;

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).preventivo * 100d) / 100d);
			total[3] = total[3] + Math.round(resultado.get(i).preventivo * 100d) / 100d;

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).saldo * 100d) / 100d);
			total[4] = total[4] + Math.round(resultado.get(i).saldo * 100d) / 100d;
		}

		// TOTALES
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum);
		col = 0;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 1));
		col = col + 2;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[0]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[1]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[2]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[3]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[4]);

/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[EJEC_PRESUP].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 4 EJECUCION PRESUPUESTARIA POR PROGRAMA - ACTIVIDAD

	@GetMapping("/ejecucionPresupuestariaPorActividad")
	public ResponseEntity<byte[]> ejecucionPresupuestariaPrograma(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<F_ejecucion_presupuestaria_por_actividad> resultado = poaCore.f_ejecucion_presupuestaria_por_actividad(
				Integer.parseInt(datosEntrada.getParameter("idClasificador")),
				Integer.parseInt(datosEntrada.getParameter("idGestion")));

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "EJECUCIÓN PRESUPUESTARIA POR PROGRAMA");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 700, 2400, 2400, 2400, 8800, 3400, 3400, 3400, 3400, 3400 };

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
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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

		anchor.setRow2(0);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;
		filaNum++;
		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(4);
		celda.setCellValue("EJECUCIÓN PRESUPUESTARIA POR PROGRAMA - ACTIVIDAD");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(4);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;

/////////////////////// CABECERA			

/////////////////////////////CABECERA TABLA

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		String tituloTabla[] = { "Nº", "Programa", "Proyecto", "Actividad", "Descripción", "Presupuesto Inicial",
				"Modificación aprobada", "Presupuesto Vigente", "Preventivo", "Saldo" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}

///////////////////////////// DATOS TABLA
		double[] total = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < resultado.size(); i++) {
			int col = 0;
			XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(i + 1);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).sigla);
			repo.regionConBorde(hoja1, new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
			col++;
			col++;
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.get(i).nombre);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).presup_inicial * 100d) / 100d);
			total[0] = total[0] + (Math.round(resultado.get(i).presup_inicial * 100d) / 100d);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).modificacion_aprobada * 100d) / 100d);
			total[1] = total[1] + (Math.round(resultado.get(i).modificacion_aprobada * 100d) / 100d);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).presupuesto_vigente * 100d) / 100d);
			total[2] = total[2] + (Math.round(resultado.get(i).presupuesto_vigente * 100d) / 100d);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).preventivo * 100d) / 100d);
			total[3] = total[3] + (Math.round(resultado.get(i).preventivo * 100d) / 100d);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(Math.round(resultado.get(i).saldo * 100d) / 100d);
			total[4] = total[4] + (Math.round(resultado.get(i).saldo * 100d) / 100d);
		}

		// total
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum);
		int col = 0;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 4));
		col = col + 5;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[0]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[1]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[2]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[3]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[4]);

		/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[EJEC_PRESUP_CONSOLIDADA].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;
	}

/////////////////////// REPORTE 5 EJECUCION PRESUPUESTARIA POR ACTIVIDAD Y PARTIDA

	@GetMapping("/ejecucionPresupuestariaActividadPartida")
	public ResponseEntity<byte[]> ejecucionPresupuestariaActividadPartida(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();

		F_ejecucion_presupuestaria_actividad_partida resultado = poaCore
				.f_ejecucion_presupuestaria_actividad_partida(Integer.parseInt(datosEntrada.getParameter("idPlan")));

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "EJECUCIÓN PRESUPUESTARIA POR ACTIVIDAD Y PARTIDA");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 700, 2700, 10500, 3700, 3400, 3400, 3400, 3400, 3400, 8500, 3400 };

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
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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

		anchor.setDx1(0);
		anchor.setDy1(0);
		anchor.setDx2(0);
		anchor.setDy2(0);
		anchor.setCol1(0);
		anchor.setRow1(0);
		anchor.setCol2(0);
		anchor.setRow2(0);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(4);
		celda.setCellValue("EJECUCIÓN PRESUPUESTARIA POR ACTIVIDAD Y PARTIDA");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(4);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;

/////////////////////// DATOS			

/////////////////////////////CABECERA TABLA

		header1 = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header1.createCell(0);
		celda.setCellValue("PROGRAMA: " + resultado.nombre_prog);
		celda.setCellStyle(estiloTextoNormal);

		header1 = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header1.createCell(0);
		celda.setCellValue("ACTIVIDAD: " + resultado.nombre_act);
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum);

		String tituloTabla[] = { "Nº", "Código Partida", "Detalle Partida", "Presupuesto Inicial",
				"Modificación Aprobada", "Presupuesto Vigente", "Preventivo", "Saldo" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, i, i));
		}
		celda = rowsub.createCell(tituloTabla.length);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Financiamiento");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, tituloTabla.length, tituloTabla.length + 2));
		filaNum++;
		XSSFRow rowsub1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = rowsub1.createCell(tituloTabla.length);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Cod.");

		celda = rowsub1.createCell(tituloTabla.length + 1);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Fuente");

		celda = rowsub1.createCell(tituloTabla.length + 2);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Monto");
		filaNum++;
///////////////////////////// DATOS TABLA
		double[] total1 = { 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < resultado.ejecucion.size(); i++) {
			int col = 0;
			int fil = filaNum;
			XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(i + 1);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.ejecucion.get(i).cod_partida);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.ejecucion.get(i).partida);

			for (int j = 0; j < resultado.ejecucion.get(i).detalle.size(); j++) {
				if (j > 0) {
					row = (XSSFRow) hoja1.createRow(filaNum++);
				}

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(
						Math.round(resultado.ejecucion.get(i).detalle.get(j).presupuesto_inicial * 100d) / 100d);
				total1[0] = total1[0]
						+ (Math.round(resultado.ejecucion.get(i).detalle.get(j).presupuesto_inicial * 100d) / 100d);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(
						Math.round(resultado.ejecucion.get(i).detalle.get(j).modificacion_aprobada * 100d) / 100d);
				total1[1] = total1[1]
						+ (Math.round(resultado.ejecucion.get(i).detalle.get(j).modificacion_aprobada * 100d) / 100d);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(
						Math.round(resultado.ejecucion.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d);
				total1[2] = total1[2]
						+ (Math.round(resultado.ejecucion.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(Math.round(resultado.ejecucion.get(i).detalle.get(j).preventivo * 100d) / 100d);
				total1[3] = total1[3]
						+ (Math.round(resultado.ejecucion.get(i).detalle.get(j).preventivo * 100d) / 100d);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(Math.round(resultado.ejecucion.get(i).detalle.get(j).saldo * 100d) / 100d);
				total1[4] = total1[4] + (Math.round(resultado.ejecucion.get(i).detalle.get(j).saldo * 100d) / 100d);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(resultado.ejecucion.get(i).detalle.get(j).sigla);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.ejecucion.get(i).detalle.get(j).nombre);

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(Math.round(resultado.ejecucion.get(i).detalle.get(j).saldo * 100d) / 100d);
				total1[5] = total1[5] + (Math.round(resultado.ejecucion.get(i).detalle.get(j).saldo * 100d) / 100d);

				col = 3;
			}
			if (resultado.ejecucion.get(i).detalle.size() > 1) {
				repo.regionConBorde(hoja1,
						new CellRangeAddress(fil, fil + resultado.ejecucion.get(i).detalle.size() - 1, 0, 0));
				repo.regionConBorde(hoja1,
						new CellRangeAddress(fil, fil + resultado.ejecucion.get(i).detalle.size() - 1, 1, 1));
				repo.regionConBorde(hoja1,
						new CellRangeAddress(fil, fil + resultado.ejecucion.get(i).detalle.size() - 1, 2, 2));
			}
		}

		// total
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum++);
		int col = 0;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 1));
		col = col + 2;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[0]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[1]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[2]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[3]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[4]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("");

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("");

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total1[5]);

///////////////////////////// RESULTADOS
		filaNum++;
		filaNum++;
		rowsub = (XSSFRow) hoja1.createRow(filaNum);
		String tituloTabla1[] = { "Codigo", "Fuente Financiamiento", "Total" };
		for (int i = 0; i < tituloTabla1.length; i++) {
			celda = rowsub.createCell(3 + i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla1[i]);
		}
		filaNum++;
		double total = 0;
		for (int i = 0; i < resultado.total.size(); i++) {
			col = 3;
			XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.total.get(i).sigla);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.total.get(i).nombre);

			celda = row.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.total.get(i).presupuesto_inicial);
			total = total + resultado.total.get(i).presupuesto_inicial;
		}

		XSSFRow rowResultados = (XSSFRow) hoja1.createRow(filaNum);
		celda = rowResultados.createCell(3);
		celda.setCellStyle(estiloDatosJustificado);
		celda.setCellValue("Total Proyecto/Actividad");

		celda = rowResultados.createCell(5);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(total);
		repo.regionConBorde(hoja1, new CellRangeAddress(filaNum, filaNum, 3, 4));

		filaNum++;

		rowResultados = (XSSFRow) hoja1.createRow(filaNum);
		celda = rowResultados.createCell(3);
		celda.setCellStyle(estiloDatosJustificado);
		celda.setCellValue("Total Fuente Financiamiento");

		celda = rowResultados.createCell(5);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(total);
		repo.regionConBorde(hoja1, new CellRangeAddress(filaNum, filaNum, 3, 4));

		/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment",
				"[EJEC. PRESUP. POR ACT. PART.] - " + resultado.nombre_act + ".xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 6 PROGRAMACIÓN PRESUPUESTARIA MENSUAL

	@GetMapping("/programacionPresupuestariaMensual")
	public ResponseEntity<byte[]> programacionPresupuestariaMensual(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		F_ejecucion_presupuestaria_mensual resultado = poaCore.f_ejecucion_presupuestaria_mensual(
				Integer.parseInt(datosEntrada.getParameter("idClasificador")),
				Integer.parseInt(datosEntrada.getParameter("idGestion")));

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "PROGRAMACIÓN PRESUPUESTARIA MENSUAL");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400, 3400,
				3400 };

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
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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

		anchor.setDx1(0);
		anchor.setDy1(0);
		anchor.setDx2(0);
		anchor.setDy2(0);
		anchor.setCol1(0);
		anchor.setRow1(0);
		anchor.setCol2(0);
		anchor.setRow2(0);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(6);
		celda.setCellValue("PROGRAMACIÓN PRESUPUESTARIA MENSUAL");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(6);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;

/////////////////////////////CABECERA TABLA

		header1 = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header1.createCell(0);
		celda.setCellValue("PROGRAMA: " + resultado.sigla + " " + resultado.nombre);
		celda.setCellStyle(estiloTextoNormal);

		int col = 0;

		for (int ind = 0; ind < resultado.detalle.size(); ind++) {
			double programado = 0, ejecutado = 0, programadoAcumt = 0, ejecutadoAcumt = 0;
			double[] programadoAcum = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					ejecutadoAcum = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

			header1 = (XSSFRow) hoja1.createRow(filaNum++);
			celda = header1.createCell(0);
			celda.setCellValue(
					"ACTIVIDAD: " + resultado.detalle.get(ind).sigla + " " + resultado.detalle.get(ind).nombre);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;

/////////////////////////////CABECERA TABLA

			XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum);

			String tituloTabla[] = { "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
					"Septiembre", "Octubre", "Noviembre", "Diciembre", "Total" };
			for (int i = 0; i < tituloTabla.length; i++) {
				celda = rowsub.createCell(i);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(tituloTabla[i]);
			}
			filaNum++;
///////////////////////////// DATOS TABLA
			if (resultado.detalle.get(ind).detalle_prog != null) {
				String primeracolumna[] = { "Prog.", "Ejec.", "Prog. Acum.", "Ejec. Acum.", "%Prog. Acum",
						"%Ejec. Acum" };

				col = 0;
				programado = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[0]);

				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(resultado.detalle.get(ind).detalle_prog.get(i).programado);
					programado = programado + resultado.detalle.get(ind).detalle_prog.get(i).programado;
					if (i == 0) {
						programadoAcum[i] = resultado.detalle.get(ind).detalle_prog.get(i).programado;
					} else {
						programadoAcum[i] = programadoAcum[i - 1]
								+ resultado.detalle.get(ind).detalle_prog.get(i).programado;
					}
					programadoAcumt = programadoAcum[i];
				}
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(programado);

				col = 0;
				ejecutado = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[1]);
				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(resultado.detalle.get(ind).detalle_prog.get(i).ejecutado);
					ejecutado = ejecutado + resultado.detalle.get(ind).detalle_prog.get(i).ejecutado;

					if (i == 0) {
						ejecutadoAcum[i] = resultado.detalle.get(ind).detalle_prog.get(i).ejecutado;
					} else {
						ejecutadoAcum[i] = ejecutadoAcum[i - 1]
								+ resultado.detalle.get(ind).detalle_prog.get(i).ejecutado;
					}
					ejecutadoAcumt = ejecutadoAcum[i];

				}
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloNumericoTablaDerecha);
				celda.setCellValue(ejecutado);

				col = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[2]);
				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(programadoAcum[i]);

				}

				col = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[3]);
				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue(ejecutadoAcum[i]);
				}

				col = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[4]);
				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue((Math.round((programadoAcum[i] * 100 / programado) * 100d) / 100d) + "%");
				}

				col = 0;
				rowsub = (XSSFRow) hoja1.createRow(filaNum++);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(primeracolumna[5]);
				for (int i = 0; i < resultado.detalle.get(ind).detalle_prog.size(); i++) {
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoTablaDerecha);
					celda.setCellValue((Math.round((ejecutadoAcum[i] / (programado)) * 100 * 100d) / 100d) + "%");
				}

			}

			filaNum++;
			filaNum++;

///////////////////////////// RESULTADOS
		}
/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[EJEC_PRESUP_MENSUAL]_"
				+ resultado.nombre.replace(".", "").replace(",", "").replace(" ", "_") + ".xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 7 CONCILIACION

	@GetMapping("/generaConciliacion")
	public ResponseEntity<byte[]> conciciliacionGenerada(HttpServletRequest datosEntrada) throws JRException,
			IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException, ParseException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateAsString = datosEntrada.getParameter("fechaCorte");
		Date date = sourceFormat.parse(dateAsString);

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_lista_conciliacion");

		procedure.setParameter("i_id_doc_conciliacion",
				Integer.parseInt(datosEntrada.getParameter("idDocConciliacion")));
		procedure.setParameter("i_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("i_fecha_corte", date);

		List<F_lista_conciliacion> resultado = procedure.getResultList();

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "CONCILIACIÓN GENERADA");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0, col = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 3000, 2500, 2500, 3400, 2800, 2800, 3400, 10300, 3400, 3400, 3400, 3400, 3400, 3400,
				3400, 3400 };

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
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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
		filaNum++;
		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(7);
		celda.setCellValue("CONCILIACIÓN GENERADA");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		filaNum++;
/////////////////////// DATOS
		double[] total = { 0, 0, 0, 0, 0, 0, 0, 0 };

/////////////////////////////CABECERA TABLA
		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		String tituloTabla[] = { "Entidad", "DA", "UE", "Cat. Prg.", "FTE", "Org.", "Objeto",
				"Descripción Objeto del Gasto", "Presupuesto Inicial", "Mod. Aprobadas", "Presup. Vig. SIGEP",
				"Presup. Vig. SISPOA", "Diferencias", "Preventivo SIGEP", "Preventivo SISPOA", "Diferencias" };
		for (int i = 0; i < tituloTabla.length; i++) {
			celda = rowsub.createCell(i);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(tituloTabla[i]);
		}

		for (int i = 0; i < resultado.size(); i++) {
			col = 0;
			rowsub = (XSSFRow) hoja1.createRow(filaNum++);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue("70");

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue("1");

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue("1");

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).cat_prog);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).fuente);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).org);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).nro_partida);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).nombre);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_inicial);
			total[0] = total[0] + resultado.get(i).ptto_inicial;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_modificaciones);
			total[1] = total[1] + resultado.get(i).ptto_modificaciones;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_vigente);
			total[2] = total[2] + resultado.get(i).ptto_vigente;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).vigente_sispoa);
			total[3] = total[3] + resultado.get(i).vigente_sispoa;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_vigente - resultado.get(i).vigente_sispoa);
			total[4] = total[4] + (resultado.get(i).ptto_vigente - resultado.get(i).vigente_sispoa);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_preventivo);
			total[5] = total[5] + resultado.get(i).ptto_preventivo;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).devengado_sispoa);
			total[6] = total[6] + resultado.get(i).devengado_sispoa;

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(resultado.get(i).ptto_preventivo - resultado.get(i).devengado_sispoa);
			total[7] = total[7] + (resultado.get(i).ptto_preventivo - resultado.get(i).devengado_sispoa);

		}
		col = 0;
		rowsub = (XSSFRow) hoja1.createRow(filaNum);

		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloDatos);
		celda.setCellValue("TOTAL");
		repo.regionConBorde(hoja1, new CellRangeAddress(filaNum, filaNum, 0, 7));
		col = 8;
		for (int i = 0; i < total.length; i++) {
			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(total[i]);
		}

///////////////////////////// RESULTADOS

/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[CONCILIACION_GENERADA].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;
	}

////////FORMULARIO DE SOLICITUD TRASPASOS ORIGEN DESTINO

	@GetMapping("/formSolTraspOrigenDestino")
	public ResponseEntity<byte[]> formSolTraspPresup(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<F_formulario_traspaso_presupuesto> resultado = poaCore
				.f_formulario_traspaso_presupuesto(Integer.parseInt(datosEntrada.getParameter("idPlan")));

		modelo.put("idPlan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		modelo.put("tipo", Integer.parseInt(datosEntrada.getParameter("tipo")));

		//// CONFIG ESTILO

		XSSFCellStyle Titulo = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		Titulo = (XSSFCellStyle) repo.generaEstilo(Titulo, hssfw, true, "CENTER", (short) 16, "Arial", "BLACK", false,
				"", false, "", false, "", true);

		XSSFCellStyle TituloTabla = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		TituloTabla = (XSSFCellStyle) repo.generaEstilo(TituloTabla, hssfw, true, "CENTER", (short) 11, "Arial",
				"WHITE", true, "595959", true, "BLACK", true, "BLACK", true);

		XSSFCellStyle datoTabla = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTabla = (XSSFCellStyle) repo.generaEstilo(datoTabla, hssfw, false, "CENTER", (short) 11, "Arial", "BLACK",
				false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaNumero = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTablaNumero = (XSSFCellStyle) repo.generaEstilo(datoTablaNumero, hssfw, false, "RIGTH", (short) 11, "Arial",
				"BLACK", false, "", true, "BLACK", true, "#,##0.00", true);

		XSSFCellStyle datoTablaIzq = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTablaIzq = (XSSFCellStyle) repo.generaEstilo(datoTablaIzq, hssfw, false, "LEFT", (short) 11, "Arial",
				"BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaCentroNegrita = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		datoTablaCentroNegrita = (XSSFCellStyle) repo.generaEstilo(datoTablaCentroNegrita, hssfw, true, "CENTER",
				(short) 11, "Arial", "BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaCentro = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		datoTablaCentro = (XSSFCellStyle) repo.generaEstilo(datoTablaCentro, hssfw, false, "CENTER", (short) 11,
				"Arial", "BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaCentroFirma = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		datoTablaCentroFirma = (XSSFCellStyle) repo.generaEstilo(datoTablaCentroFirma, hssfw, false, "CENTER",
				(short) 8, "Arial", "BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaJustificado = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		datoTablaJustificado = (XSSFCellStyle) repo.generaEstilo(datoTablaJustificado, hssfw, false, "JUSTIFY",
				(short) 9, "Arial", "BLACK", false, "", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaCentroVerde = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTablaCentroVerde = (XSSFCellStyle) repo.generaEstilo(datoTablaCentroVerde, hssfw, false, "CENTER",
				(short) 11, "Arial", "BLACK", true, "C6E0B4", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle datoTablaNumeroVerde = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTablaNumeroVerde = (XSSFCellStyle) repo.generaEstilo(datoTablaNumeroVerde, hssfw, false, "RIGTH",
				(short) 11, "Arial", "BLACK", true, "C6E0B4", true, "BLACK", true, "#,##0.00", true);

		XSSFCellStyle datoTablaJustificadoVerde = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		datoTablaJustificadoVerde = (XSSFCellStyle) repo.generaEstilo(datoTablaJustificadoVerde, hssfw, false, "CENTER",
				(short) 11, "Arial", "BLACK", true, "C6E0B4", true, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle caracterRefe = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
		/////// | Ajustar texto
		caracterRefe = (XSSFCellStyle) repo.generaEstilo(caracterRefe, hssfw, false, "LEFT", (short) 7, "Arial",
				"BLACK", false, "", false, "", false, "", true);

		XSSFCellStyle textoJustificadoNegrita = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		textoJustificadoNegrita = (XSSFCellStyle) repo.generaEstilo(textoJustificadoNegrita, hssfw, true, "JUSTIFY",
				(short) 11, "Arial", "BLACK", false, "", false, "BLACK", false, "#,##0.00", true);

		XSSFCellStyle textoJustificadoNegritaRojo = (XSSFCellStyle) hssfw.createCellStyle();
		/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
		/////// letra|colorLetra|Bool-Bordes |color borde|bool numerico |formato
		/////// numerico | Ajustar texto
		textoJustificadoNegritaRojo = (XSSFCellStyle) repo.generaEstilo(textoJustificadoNegritaRojo, hssfw, true,
				"JUSTIFY", (short) 11, "Arial", "RED", false, "", false, "BLACK", false, "#,##0.00", true);

		////

		Integer tipo = Integer.parseInt(datosEntrada.getParameter("tipo"));
		/// HOJAS
		String[][] titulosHojas = {
				{ "Formulario de Traspaso SISPOA", "FORMULARIO DE SOLICITUD DE TRASPASOS PRESUPUESTARIOS" },
				{ "Resumen DGAA", "RESUMEN DE TRASPASO PRESUPUESTARIO" }

		};

		for (int nHoja = 0; nHoja < titulosHojas.length; nHoja++) {

			Sheet hoja = hssfw.createSheet();
			hssfw.setSheetName(nHoja, titulosHojas[nHoja][0]);
			org.apache.poi.ss.usermodel.Cell celda;
			int filaNum = 0, col = 0;
			if (nHoja == 0) {
				////// Tamaño de columnas
				Integer tamanoColumna[] = { 2500, 3000, 3000, 5500, 5500, 3000, 3000, 3000, 3000, 3000, 4000 };

				for (int i = 0; i < tamanoColumna.length; i++) {
					hoja.setColumnWidth(i, tamanoColumna[i]);
				}

				filaNum++;
				XSSFRow fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(col);
				celda.setCellValue(titulosHojas[nHoja][1]);
				celda.setCellStyle(Titulo);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 1));
				filaNum++;
				col = 0;
				fila = (XSSFRow) hoja.createRow(filaNum++);

				celda = fila.createCell(col);
				celda.setCellValue("Fecha de solicitud: ");
				celda.setCellStyle(TituloTabla);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 3));

				col = 7;
				celda = fila.createCell(col);
				celda.setCellValue("");
				celda.setCellStyle(datoTablaIzq);
				repo.regionConBorde(hoja,
						new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 2));

				col = 0;
				filaNum++;
				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(col);
				celda.setCellValue("APERTURA PROGRAMATICA:");
				celda.setCellStyle(datoTablaCentroNegrita);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 3));

				col = 4;
				celda = fila.createCell(col);
				celda.setCellValue(resultado.get(0).apertura);
				celda.setCellStyle(datoTablaCentroVerde);

				repo.regionConBorde(hoja,
						new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 1));

				col = 0;
				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(col);
				celda.setCellValue("UNIDAD SOLICITANTE:");
				celda.setCellStyle(datoTablaCentroNegrita);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 2));

				col = 3;
				celda = fila.createCell(col);
				celda.setCellValue(resultado.get(0).unidad);
				celda.setCellStyle(datoTablaCentro);
				repo.regionConBorde(hoja,
						new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 1));

				col = 0;
				filaNum++;
				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(col);
				celda.setCellValue("ORIGEN DE:");
				celda.setCellStyle(TituloTabla);
				repo.regionConBorde(hoja,
						new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 1));

				int it = 0;
				while (it < resultado.size()) {
					int posicion = it;
					if (resultado.get(it).tipo.equals("Origen")) {

						String origen = resultado.get(it).te_nombre;
						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);
						fila.setHeight((short) 1200);
						celda = fila.createCell(col);
						celda.setCellValue("CODIGO ACTIVIDAD:");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 2));

						col = 3;
						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).act_sigla);
						celda.setCellStyle(datoTablaCentro);

						celda = fila.createCell(col++);
						celda.setCellValue("DESCRIPCION:");
						celda.setCellStyle(datoTablaCentroNegrita);

						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).act_nombre);
						celda.setCellStyle(datoTablaCentro);
						repo.regionConBorde(hoja,
								new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, tamanoColumna.length - 1));

						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);
						fila.setHeight((short) 1200);
						celda = fila.createCell(col);
						celda.setCellValue("CODIGO TAREA ESPECIFICA:");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 2));

						col = 3;
						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).te_sigla);
						celda.setCellStyle(datoTablaCentro);

						celda = fila.createCell(col++);
						celda.setCellValue("DESCRIPCION:");
						celda.setCellStyle(datoTablaCentroNegrita);

						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).te_nombre);
						celda.setCellStyle(datoTablaCentro);
						repo.regionConBorde(hoja,
								new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, tamanoColumna.length - 1));

						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);

						celda = fila.createCell(col++);
						celda.setCellValue("Nro.");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("COD. INSUMO");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("NRO. DE PARTIDA");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("FTE. FINANC.");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("NOMBRE DE LA PARTIDA");
						celda.setCellStyle(TituloTabla);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 4));
						col = col + 5;
						celda = fila.createCell(col++);
						celda.setCellValue("MONTO REDUCIR");
						celda.setCellStyle(TituloTabla);

						double suma = 0;
						int cons = 1;
						for (int iterador = posicion; iterador < resultado.size(); iterador++) {

							if (resultado.get(iterador).te_nombre.equals(origen)
									&& resultado.get(iterador).tipo.equals("Origen")) {

								col = 0;
								fila = (XSSFRow) hoja.createRow(filaNum++);

								celda = fila.createCell(col++);
								celda.setCellValue(cons++);
								celda.setCellStyle(datoTablaCentroNegrita);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).cod_mc);
								celda.setCellStyle(datoTablaCentroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).partida);
								celda.setCellStyle(datoTablaNumeroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).ff);
								celda.setCellStyle(datoTablaNumeroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).nombre_partida);
								celda.setCellStyle(datoTablaCentro);
								repo.regionConBorde(hoja,
										new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 4));
								col = col + 5;
								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).importe);
								celda.setCellStyle(datoTablaNumeroVerde);

								suma = suma + resultado.get(iterador).importe;
								it++;
							}

						}

						fila = (XSSFRow) hoja.createRow(filaNum++);
						col = 8;
						celda = fila.createCell(col++);
						celda.setCellValue("SUBTOTAL");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col));
						col = 10;
						celda = fila.createCell(col++);
						celda.setCellValue(suma);
						celda.setCellStyle(datoTablaNumero);
						filaNum++;
					} else {
						it++;
					}
				}

				////////// DESTINO

				col = 0;
				filaNum++;
				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(col);
				celda.setCellValue("DESTINO A:");
				celda.setCellStyle(TituloTabla);
				repo.regionConBorde(hoja,
						new CellRangeAddress(filaNum - 1, filaNum - 1, col, tamanoColumna.length - 1));

				it = 0;
				while (it < resultado.size()) {
					int posicion = it;
					if (resultado.get(posicion).tipo.equals("Destino")) {

						String origen = resultado.get(it).te_nombre;
						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);
						fila.setHeight((short) 1200);
						celda = fila.createCell(col);
						celda.setCellValue("CODIGO ACTIVIDAD:");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 2));

						col = 3;
						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).act_sigla);
						celda.setCellStyle(datoTablaCentro);

						celda = fila.createCell(col++);
						celda.setCellValue("DESCRIPCION:");
						celda.setCellStyle(datoTablaCentroNegrita);

						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).act_nombre);
						celda.setCellStyle(datoTablaCentro);
						repo.regionConBorde(hoja,
								new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, tamanoColumna.length - 1));

						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);
						fila.setHeight((short) 1200);
						celda = fila.createCell(col);
						celda.setCellValue("CODIGO TAREA ESPECIFICA:");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col, 2));

						col = 3;
						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).te_sigla);
						celda.setCellStyle(datoTablaCentro);

						celda = fila.createCell(col++);
						celda.setCellValue("DESCRIPCION:");
						celda.setCellStyle(datoTablaCentroNegrita);

						celda = fila.createCell(col++);
						celda.setCellValue(resultado.get(posicion).te_nombre);
						celda.setCellStyle(datoTablaCentro);
						repo.regionConBorde(hoja,
								new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, tamanoColumna.length - 1));

						col = 0;
						fila = (XSSFRow) hoja.createRow(filaNum++);

						celda = fila.createCell(col++);
						celda.setCellValue("Nro.");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("COD. INSUMO");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("NRO. DE PARTIDA");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("FTE. FINANC.");
						celda.setCellStyle(TituloTabla);

						celda = fila.createCell(col++);
						celda.setCellValue("NOMBRE DE LA PARTIDA");
						celda.setCellStyle(TituloTabla);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 4));
						col = col + 5;
						celda = fila.createCell(col++);
						celda.setCellValue("MONTO INCREMENTAR");
						celda.setCellStyle(TituloTabla);

						double suma = 0;
						int cons = 1;
						for (int iterador = posicion; iterador < resultado.size(); iterador++) {

							if (resultado.get(iterador).te_nombre.equals(origen)
									&& resultado.get(iterador).tipo.equals("Destino")) {

								col = 0;
								fila = (XSSFRow) hoja.createRow(filaNum++);

								celda = fila.createCell(col++);
								celda.setCellValue(cons++);
								celda.setCellStyle(datoTablaCentroNegrita);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).cod_mc);
								celda.setCellStyle(datoTablaCentroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).partida);
								celda.setCellStyle(datoTablaNumeroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).ff);
								celda.setCellStyle(datoTablaNumeroVerde);

								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).nombre_partida);
								celda.setCellStyle(datoTablaCentro);
								repo.regionConBorde(hoja,
										new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col + 4));
								col = col + 5;
								celda = fila.createCell(col++);
								celda.setCellValue(resultado.get(iterador).importe);
								celda.setCellStyle(datoTablaNumeroVerde);

								suma = suma + resultado.get(iterador).importe;
								it++;
							}

						}

						fila = (XSSFRow) hoja.createRow(filaNum++);
						col = 8;
						celda = fila.createCell(col++);
						celda.setCellValue("SUBTOTAL");
						celda.setCellStyle(datoTablaCentroNegrita);
						repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, col - 1, col));
						col = 10;
						celda = fila.createCell(col++);
						celda.setCellValue(suma);
						celda.setCellStyle(datoTablaNumero);
						filaNum++;
					} else {
						it++;
					}
				}

				/////
				filaNum++;

				/////
				filaNum++;

				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(0);
				celda.setCellValue("Justificación de la(s) modificación(es) de la Unidad:");
				celda.setCellStyle(textoJustificadoNegrita);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));

				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(0);
				celda.setCellValue(resultado.get(0).justificacion);
				celda.setCellStyle(datoTablaJustificadoVerde);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum + 1, 0, 10));
				filaNum++;
				filaNum++;

				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(0);
				celda.setCellValue("Observaciones de la DGP:");
				celda.setCellStyle(textoJustificadoNegrita);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));

				fila = (XSSFRow) hoja.createRow(filaNum++);
				celda = fila.createCell(0);
				celda.setCellValue(resultado.get(0).observacion);
				celda.setCellStyle(datoTablaJustificadoVerde);
				repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum, 0, 10));

				filaNum++;
				filaNum++;

				if (tipo == 2) {

					fila = (XSSFRow) hoja.createRow(filaNum++);
					fila.setHeight((short) 1400);
					celda = fila.createCell(0);
					celda.setCellValue("\r\n\r\n\r\nFIRMA Y SELLO\r\nSOLICITANTE");
					repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 2));
					celda.setCellStyle(datoTablaCentroFirma);

					celda = fila.createCell(3);
					celda.setCellValue("\r\n\r\n\r\nFIRMA Y SELLO\r\nINMEDIATO SUPERIOR DEL SOLICITANTE");

					repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 3, 4));
					celda.setCellStyle(datoTablaCentroFirma);

					celda = fila.createCell(5);
					celda.setCellValue(
							"\r\n\r\n\r\nFIRMA Y SELLO\r\n RESPONSABLE DEL ÁREA DE PRESUPUESTO - DGAA\r\n(SI CORRESPONDE)");
					repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 5, 10));
					celda.setCellStyle(datoTablaCentroFirma);
				} else {
					fila = (XSSFRow) hoja.createRow(filaNum++);
					fila.setHeight((short) 1400);
					celda = fila.createCell(0);
					celda.setCellValue("\r\n\r\n\r\nFIRMA Y SELLO\r\nSOLICITANTE");
					repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));
					celda.setCellStyle(datoTablaCentroFirma);

					celda = fila.createCell(5);
					celda.setCellValue("\r\n\r\n\r\nFIRMA Y SELLO\r\nINMEDIATO SUPERIOR DEL SOLICITANTE");
					repo.regionConBorde(hoja, new CellRangeAddress(filaNum - 1, filaNum - 1, 5, 10));
					celda.setCellStyle(datoTablaCentroFirma);

				}

				filaNum++;

			} else {
				//// HOJA 2

				////// estilos titulo
				CellStyle estiloTitulo = hssfw.createCellStyle();
				estiloTitulo = repo.estiloTitulo(estiloTitulo, hssfw);

				////// estilos Titulos Tabla
				CellStyle estiloTituloTabla = hssfw.createCellStyle();
				estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

				////// estilos datos
				CellStyle estiloDatos = hssfw.createCellStyle();
				estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

				////// estilos datos
				CellStyle estiloDatosJustificado = hssfw.createCellStyle();
				estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

				////// estilos numerico Titulos Tabla
				XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
				estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
						.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

				////// estilos numerico tabla
				CellStyle estiloNumericoDerecha = hssfw.createCellStyle();
				estiloNumericoDerecha = repo.estiloNumericoTablaDerecha(estiloNumericoDerecha, hssfw);

				Integer[] tamanoColumna = { 3000, 10000, 3000, 3000, 3000 };

				for (int i = 0; i < tamanoColumna.length; i++) {
					hoja.setColumnWidth(i, tamanoColumna[i]);
				}

				Integer idP = Integer.parseInt(datosEntrada.getParameter("idPlan"));

				StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("mteps_plan.f_estado_ptto_ref");

				procedure.setParameter("i_id_plan", idP);

				List<F_estado_ptto_ref> resultadoDatos = procedure.getResultList();

				XSSFRow header = (XSSFRow) hoja.createRow(filaNum++);
				celda = header.createCell(0);
				celda.setCellValue("RESUMEN DE TRASPASO PRESUPUESTARIO");
				celda.setCellStyle(estiloTitulo);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

				header = (XSSFRow) hoja.createRow(filaNum++);
				celda = header.createCell(0);
				celda.setCellValue("APERTURA PROGRAMÁTICA: " + resultado.get(0).apertura);
				celda.setCellStyle(estiloTitulo);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

				header = (XSSFRow) hoja.createRow(filaNum++);
				celda = header.createCell(0);
				celda.setCellValue("UNIDAD SOLICITANTE: " + resultado.get(0).unidad);
				celda.setCellStyle(estiloTitulo);
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 4));

				XSSFRow rowsub = (XSSFRow) hoja.createRow(filaNum++);

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("FF");
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col - 1, col - 1));

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Partida");
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col - 1, col - 1));

				celda = rowsub.createCell(col);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Presupuesto");
				hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, col + 2));

				rowsub = (XSSFRow) hoja.createRow(filaNum++);

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Actual");

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Movimientos");

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Reformulado");

				double[] totales = { 0, 0, 0 };
				for (int i = 0; i < resultadoDatos.size(); i++) {
					col = 0;
					rowsub = (XSSFRow) hoja.createRow(filaNum++);

					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultadoDatos.get(i).ff);

					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultadoDatos.get(i).partida + " - " + resultadoDatos.get(i).nombre_partida);

					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoDerecha);
					celda.setCellValue(resultadoDatos.get(i).ptto_actual);
					totales[0] = totales[0] + resultadoDatos.get(i).ptto_actual;
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoDerecha);
					celda.setCellValue(resultadoDatos.get(i).diferencias);
					totales[1] = totales[1] + resultadoDatos.get(i).diferencias;
					celda = rowsub.createCell(col++);
					celda.setCellStyle(estiloNumericoDerecha);
					celda.setCellValue(resultadoDatos.get(i).reformulado);
					totales[2] = totales[2] + resultadoDatos.get(i).reformulado;

				}
				col = 0;
				rowsub = (XSSFRow) hoja.createRow(filaNum);
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Subtotales");
				hoja.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col - 1, col));
				col++;
				celda = rowsub.createCell(col++);
				celda.setCellStyle(estilonumericoTituloTabladerecha);
				celda.setCellValue(totales[0]);

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estilonumericoTituloTabladerecha);
				celda.setCellValue(totales[1]);

				celda = rowsub.createCell(col++);
				celda.setCellStyle(estilonumericoTituloTabladerecha);
				celda.setCellValue(totales[2]);

			}

		}
/////////////////////////////////////////

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[FORM_SOL_ORIGEN].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 8 PROGRAMACIÓN PRESUPUESTARIA DE REFORMULACIONES

	@GetMapping("/ejecPresupPartidasReformulaciones")
	public ResponseEntity<byte[]> ejecPresupPartidasReformulaciones(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		XSSFWorkbook hssfw = new XSSFWorkbook();
		F_ejecucion_presupuestaria_partida_reformulacion resultado = poaCore
				.ejecucionPresupuestariaPartidaReformulacion(Integer.parseInt(datosEntrada.getParameter("idPlan")));

		SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#.##");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0,
				"HISTÓRICO DE MODIFICACIONES PRESUPUETARIAS APLICADAS MEDIANTE REFORMULACIÓN, POR PARTIDA");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 3400, 8400, 6300 };

		for (int i = 0; i < tamanoColumna.length + resultado.detalle_plan.size(); i++) {
			if (i < tamanoColumna.length) {
				hoja1.setColumnWidth(i, tamanoColumna[i]);
			} else {
				hoja1.setColumnWidth(i, 3400);
			}

		}

//////estilos titulo
		CellStyle estiloTitulo1 = hssfw.createCellStyle();
		estiloTitulo1 = repo.generarEstilo(estiloTitulo1, hssfw, true, "CENTER", (short) 14, "Arial");

		CellStyle estiloTitulo2 = hssfw.createCellStyle();
		estiloTitulo2 = repo.generarEstilo(estiloTitulo2, hssfw, true, "CENTER", (short) 12, "Arial");

//////estilos texto normal
		CellStyle estiloTextoNormal = hssfw.createCellStyle();
		estiloTextoNormal = repo.generarEstilo(estiloTextoNormal, hssfw, false, "LEFT", (short) 12, "Arial");

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

////// estilos datos
		CellStyle estiloDatosJustificado = hssfw.createCellStyle();
		estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

		////// estilos numerico Titulos Tabla
		XSSFCellStyle estilonumericoTituloTabladerecha = (XSSFCellStyle) hssfw.createCellStyle();
		estilonumericoTituloTabladerecha = (XSSFCellStyle) repo
				.estiloNumericoTituloTablaDerecha(estilonumericoTituloTabladerecha, hssfw);

		XSSFCellStyle datoTablaFondoRojo = (XSSFCellStyle) hssfw.createCellStyle();
		datoTablaFondoRojo = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoRojo, hssfw, false, "RIGHT", (short) 9,
				"Arial", "BLACK", true, "FF8C8C", true, "CENTER", true, "#,##0.00", true);

		XSSFCellStyle datoTablaFondoverde = (XSSFCellStyle) hssfw.createCellStyle();
		datoTablaFondoverde = (XSSFCellStyle) repo.generaEstilo(datoTablaFondoverde, hssfw, false, "RIGHT", (short) 9,
				"Arial", "BLACK", true, "86FE6C", true, "CENTER", true, "#,##0.00", true);

		XSSFCellStyle datoTablaFondorRosado = (XSSFCellStyle) hssfw.createCellStyle();
		datoTablaFondorRosado = (XSSFCellStyle) repo.generaEstilo(datoTablaFondorRosado, hssfw, false, "CENTER",
				(short) 9, "Arial", "BLACK", true, "FEE8E8", true, "CENTER", false, "#,##0.00", true);

///// IMAGEN
		String rutaImg = datosEntrada.getRealPath("") + "/assets/logo2.jpg";
		InputStream inputStream = new FileInputStream(rutaImg);
		byte[] bytes = IOUtils.toByteArray(inputStream);
		int pictureIdx = hssfw.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		CreationHelper helper = hssfw.getCreationHelper();
		Drawing drawing = hoja1.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setDx1(0);
		anchor.setDy1(0);
		anchor.setDx2(0);
		anchor.setDy2(0);
		anchor.setCol1(0);
		anchor.setRow1(0);
		anchor.setCol2(0);
		anchor.setRow2(0);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		filaNum++;
/////////////////////// Titulo
		XSSFRow header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(6);
		celda.setCellValue("HISTÓRICO DE MODIFICACIONES PRESUPUETARIAS APLICADAS MEDIANTE REFORMULACIÓN, POR PARTIDA");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(6);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;
		header = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header.createCell(0);
		celda.setCellValue("ENTIDAD: 0070 - MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL ");
		celda.setCellStyle(estiloTextoNormal);
		XSSFRow header1h1 = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header1h1.createCell(0);
		celda.setCellValue("PROGRAMA PRESUPUESTARIO: " + resultado.sigla_p + " " + resultado.nombre_p);
		celda.setCellStyle(estiloTextoNormal);
		XSSFRow header2h1 = (XSSFRow) hoja1.createRow(filaNum++);
		celda = header2h1.createCell(0);
		celda.setCellValue("ACTIVIDAD PRESUPUESTARIA: " + resultado.sigla_a + " " + resultado.nombre_a);
		celda.setCellStyle(estiloTextoNormal);

		String titulo = "";

/////////////////////////////DATOS

		Double[] totales = new Double[resultado.detalle_plan.size()];
		Arrays.fill(totales, 0.0);

		filaNum++;
		int col = 0;

		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("CODIGO");

		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("PARTIDA");

		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("FUENTE");

		for (int i = 0; i < resultado.detalle_plan.size(); i++) {

			if (i > 0) {
				titulo = "Reformulación ";
			} else {
				titulo = "Ptto Inicial ";
			}
			if ((i + 1) == resultado.detalle_plan.size()) {
				titulo = "Ptto Vigente ";
			}

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue(titulo + "\n" + resultado.detalle_plan.get(i).id + "\n"
					+ config.formatoFecha(resultado.detalle_plan.get(i).fecha, true));
		}

		for (int i = 0; i < resultado.detalle_partidas.size(); i++) {
			col = 0;
			rowsub = (XSSFRow) hoja1.createRow(filaNum);

			celda = rowsub.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.detalle_partidas.get(i).codigo);

			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.detalle_partidas.get(i).partida);

			int fila = 0;
			if (resultado.detalle_partidas.get(i).detalle_f != null) {
				for (int j = 0; j < resultado.detalle_partidas.get(i).detalle_f.size(); j++) {
					col = 2;

					if (j > 0) {
						rowsub = (XSSFRow) hoja1.createRow(filaNum);
					}

					celda = rowsub.createCell(col++);
					celda.setCellStyle(datoTablaFondorRosado);
					celda.setCellValue(resultado.detalle_partidas.get(i).detalle_f.get(j).fuente);

					for (int k = 0; k < resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t.size(); k++) {

						celda = rowsub.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);

						if (k > 0) {

							if (resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t
									.get(k).total > resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t
											.get(k - 1).total) {
								celda.setCellStyle(datoTablaFondoverde);
							} else if (resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t
									.get(k).total < resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t
											.get(k - 1).total) {
								celda.setCellStyle(datoTablaFondoRojo);
							}

						}

						celda.setCellValue(resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t.get(k).total);

						totales[k] = totales[k]
								+ resultado.detalle_partidas.get(i).detalle_f.get(j).detalle_t.get(k).total;

					}
					fila++;
					if ((j + 1) == resultado.detalle_partidas.get(i).detalle_f.size() && j > 0) {
						repo.regionConBorde(hoja1, new CellRangeAddress(filaNum + 1 - fila, filaNum, 0, 0));

						repo.regionConBorde(hoja1, new CellRangeAddress(filaNum + 1 - fila, filaNum, 1, 1));
					}
					filaNum++;
				}
			}

		}

		col = 0;
		rowsub = (XSSFRow) hoja1.createRow(filaNum);

		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 2));
		col = col + 3;
		for (int j = 0; j < resultado.detalle_plan.size(); j++) {
			celda = rowsub.createCell(col++);
			celda.setCellStyle(estilonumericoTituloTabladerecha);
			celda.setCellValue(totales[j]);
		}
/////////////////////////////////////////
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[HISTORICO_MOD_PRESUP_POR_PARTIDA].xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

/////////////////////// REPORTE 9 SEGUIMIENTO FISICO PRESUPUESTARIO
	@GetMapping("/seguimientoFisicoPresupuestario")
	public ResponseEntity<byte[]> reporteSeguimientoFisicoPresupuestarioExcel(HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException, ParseException {

	
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        
		Date v_fecha_inicio = dateFormat.parse( datosEntrada.getParameter("fechaInicio")),v_fecha_final = dateFormat.parse( datosEntrada.getParameter("fechaFinal") );
		
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        String fechaInicioLiteral = dateFormat2.format(v_fecha_inicio),fechaFinalLiteral = dateFormat2.format(v_fecha_final);
		
		Integer v_id_plan = Integer.parseInt(datosEntrada.getParameter("idPlan"));
		
		XSSFWorkbook hssfw = new XSSFWorkbook();
		List<F_ejecucion_seguimiento_fisico_presupuestario> resultado = poaCore
				.f_ejecucion_seguimiento_fisico_presupuestario(v_id_plan, v_fecha_inicio,v_fecha_final);
		
		// obtener datos
		
		Double DatoViaticos=0.0;
		Double DatoPasajes=0.0;
		
		Connection connection = null;

		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call mteps_plan.f_validar_conciliacion(?,?,?,?,?,?)");
		procedure.setInt(1, v_id_plan);
		procedure.setString(2, datosEntrada.getParameter("fechaFinal"));
		procedure.registerOutParameter(3, Types.BOOLEAN);
		procedure.registerOutParameter(4, Types.VARCHAR);
		procedure.registerOutParameter(5, Types.INTEGER);
		procedure.registerOutParameter(6, Types.VARCHAR);
		procedure.executeUpdate();
		String resultadoString =  (String) procedure.getObject(6);
		System.out.println(resultadoString);
		if(  (Boolean) procedure.getObject(3)) {
		if(resultadoString !=null) {
			 ObjectMapper objectMapper = new ObjectMapper();
			 System.out.println(resultadoString);
		        // Convertir el JSON en un objeto Java
			 List<DatosValidacionConciliacion> objetos = objectMapper.readValue(resultadoString, new TypeReference<List<DatosValidacionConciliacion>>(){});

			  DatoViaticos=objetos.get(0).difViaticos;
			  DatoPasajes=objetos.get(0).difPasajes;
		}}
		///
		
		Double totalSumaDato = DatoViaticos +DatoPasajes;

		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(0, "SEGUIMIENTO PRESUPUESTARIO");

		Cell celda;

		Integer filaNum = 0;
//////Tamaño de columnas
		Integer tamanoColumna[] = { 13700, 13700, 3000, 3000, 13700, 3200, 3200, 3200, 3200, 3200 };

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
		
	////// estilos datos
			CellStyle estiloDatosCentroRojo = hssfw.createCellStyle();
			estiloDatosCentroRojo = repo.estiloCeldaTablaCentroRojo(estiloDatosCentroRojo, hssfw);
			
			CellStyle estiloCeldaTablaCentroRojoSinBorde = hssfw.createCellStyle();
			estiloCeldaTablaCentroRojoSinBorde = repo.estiloCeldaTablaCentroRojoSinBorde(estiloCeldaTablaCentroRojoSinBorde, hssfw);

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
		celda = header.createCell(3);
		celda.setCellValue("SEGUIMIENTO FÍSICO Y PRESUPUESTARIO");
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		
		header = (XSSFRow) hoja1.createRow(filaNum);
		celda = header.createCell(3);
		celda.setCellValue("DE "+fechaInicioLiteral+ " A " + fechaFinalLiteral);
		celda.setCellStyle(estiloTitulo1);
		filaNum++;
		XSSFRow header1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = header1.createCell(3);
		celda.setCellValue("(Expresado en Bolivianos)");
		celda.setCellStyle(estiloTitulo2);
		filaNum++;
		filaNum++;

/////////////////////// CABECERA			

		String fuente = "", organismo = "";
		if (resultado.get(0).fuente_f != null) {
			for (int i = 0; i < resultado.get(0).fuente_f.size(); i++) {
				if (i == 0) {
					fuente = resultado.get(0).fuente_f.get(i).clasificador;
				} else {
					fuente = fuente + ", " + resultado.get(0).fuente_f.get(i).clasificador;
				}
			}
		}
		if (resultado.get(0).organismo_f != null) {
			for (int i = 0; i < resultado.get(0).organismo_f.size(); i++) {
				if (i == 0) {
					organismo = resultado.get(0).organismo_f.get(i).clasificador;
				} else {
					organismo = organismo + ", " + resultado.get(0).organismo_f.get(i).clasificador;
				}
			}
		}

		XSSFRow Dato1 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato1.createCell(0);
		celda.setCellValue("Actividad: " + resultado.get(0).nombre_act);
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		XSSFRow Dato2 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato2.createCell(0);
		celda.setCellValue("Fuente: " + fuente);
		celda.setCellStyle(estiloTextoNormal);
		filaNum++;
		XSSFRow Dato3 = (XSSFRow) hoja1.createRow(filaNum);
		celda = Dato3.createCell(0);
		celda.setCellValue("Org. Financiador: " + organismo);
		celda.setCellStyle(estiloTextoNormal);

		filaNum++;
		filaNum++;
/////////////////////////////CABECERA TABLA
		
		Boolean mostrarAsterisco =false;

////////////////////////Definición variables
		String meses[] = { "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre",
				"octubre", "noviembre", "diciembre" };
		
		String tituloTabla[] = { "Tarea Específica (TE)", "Indicador", "De "+ fechaInicioLiteral+ " a " + fechaFinalLiteral,
				"Detalle partida","Presupuesto Vigente "+resultado.get(0).gestion, "Programado ("+fechaInicioLiteral+ " a " + fechaFinalLiteral+")", "Preventivo (certificado)", "Saldo sin certificar" };
	
		XSSFRow rowsub = (XSSFRow) hoja1.createRow(filaNum++);

		int col = 0;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("PROGRAMACIÓN Y EJECUCIÓN FÍSICA ("+fechaInicioLiteral+ " a " + fechaFinalLiteral+ ")");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 0, 3));
		
		
		celda = rowsub.createCell(col+4);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("PROGRAMACIÓN Y EJECUCIÓN PRESUPUESTARIA ("+fechaInicioLiteral+ " a " + fechaFinalLiteral+")");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1,filaNum-1, 4, 9));

		rowsub = (XSSFRow) hoja1.createRow(filaNum++);
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Tarea Específica (TE)");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum, col-1, col-1));		
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Indicador");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum, col-1, col-1));	
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("De "+fechaInicioLiteral+ " a " + fechaFinalLiteral);
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, col-1, col));	
		col++;
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Detalle partida");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum, col-1, col-1));	
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Presupuesto Vigente "+resultado.get(0).gestion);
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum, col-1, col-1));	
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue( "Ejecutado (De "+fechaInicioLiteral+ " a " + fechaFinalLiteral+")");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, col-1, col+2));	
		
		rowsub = (XSSFRow) hoja1.createRow(filaNum++);
		col=2;
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Prog.");
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Ejec.");
		col=6;
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Programado (De "+fechaInicioLiteral+ " a " + fechaFinalLiteral+")");
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Preventivo (certificado)");
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Saldo sin Certificar");
		
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Conciliación Pendiente DGAA");
		
		String tipInd="";
		col = 0;
		double[] total = { 0, 0, 0, 0 };
///////////////////////////// DATOS TABLA	
		
		if (!resultado.isEmpty()) {
			for (int i = 0; i < resultado.size(); i++) {
				if (resultado.get(i).detalle != null) {

					col = 0;
					int fil = filaNum;
					
					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.get(i).te_descripcion);
					
					
					Integer tamFilas = resultado.get(i).programado_fisico.size()>= resultado.get(i).detalle.size()? resultado.get(i).programado_fisico.size():resultado.get(i).detalle.size();
					
					for (int j = 0; j < tamFilas; j++ ) {
						
						if (j > 0) {
							col = 1;
							row = (XSSFRow) hoja1.createRow(filaNum++);
						}
						if(j<resultado.get(i).programado_fisico.size()) {
						if(resultado.get(i).programado_fisico.get(j)!=null ) {
						
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue(resultado.get(i).programado_fisico.get(j).denominacion);
							
							if (resultado.get(i).programado_fisico.get(j).id_tipo_identificador == 6) {
								tipInd = "%";
							} else {
								tipInd = "";
							}
							celda = row.createCell(col++);
							celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
							celda.setCellValue(resultado.get(i).programado_fisico.get(j).programado!=null? String.format("%.0f",resultado.get(i).programado_fisico.get(j).programado) + tipInd:"0"+ tipInd);
							
							celda = row.createCell(col++);
							celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
							celda.setCellValue(resultado.get(i).programado_fisico.get(j).ejecutado!=null?String.format("%.0f",resultado.get(i).programado_fisico.get(j).ejecutado) + tipInd:"0"+ tipInd);
							
						}
						}
						else {
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
						}
						
					
						if(j<resultado.get(i).detalle.size()) {


						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.get(i).detalle.get(j).partida);

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(
								Math.round(resultado.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d);
						total[0] = total[0]
								+ Math.round(resultado.get(i).detalle.get(j).presupuesto_vigente * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(
								Math.round(resultado.get(i).detalle.get(j).presupuesto_mes * 100d) / 100d);
						total[1] = total[1]
								+ Math.round(resultado.get(i).detalle.get(j).presupuesto_mes * 100d) / 100d;

						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(Math.round(resultado.get(i).detalle.get(j).preventivo * 100d) / 100d);
						total[2] = total[2] + Math.round(resultado.get(i).detalle.get(j).preventivo * 100d) / 100d;

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumericoTablaDerecha);
						celda.setCellValue(Math.round(resultado.get(i).detalle.get(j).saldo * 100d) / 100d);
						total[3] = total[3] + Math.round(resultado.get(i).detalle.get(j).saldo * 100d) / 100d;
						
						//logica
						if(resultado.get(i).detalle.get(j).cod_partida.equals("22110")) {
							if(DatoPasajes !=0 ) {
								mostrarAsterisco = true;
							}
						}
						if(resultado.get(i).detalle.get(j).cod_partida.equals("22210")) {
							if(DatoViaticos !=0 ) {
								mostrarAsterisco = true;
							}
						}
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosCentroRojo);
						celda.setCellValue(mostrarAsterisco?"*":"");
						mostrarAsterisco = false;
						
						}else {
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue("");
						}
						
					}

					if (tamFilas > 1) {
						repo.regionConBorde(hoja1,
								new CellRangeAddress(fil, fil + tamFilas - 1, 0, 0));
						
						
						if(resultado.get(i).programado_fisico.size()< resultado.get(i).detalle.size()) {
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil +resultado.get(i).programado_fisico.size() -1, fil + resultado.get(i).detalle.size() - 1, 1, 1));
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).programado_fisico.size() -1, fil + resultado.get(i).detalle.size() - 1, 2, 2));

							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).programado_fisico.size() -1, fil + resultado.get(i).detalle.size() - 1, 3, 3));
						}
						
						if( resultado.get(i).detalle.size()<resultado.get(i).programado_fisico.size()) {
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil +resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 4, 4));
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 5, 5));

							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 6, 6));
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 7, 7));
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 8, 8));
							
							repo.regionConBorde(hoja1,
									new CellRangeAddress(fil+resultado.get(i).detalle.size() -1, fil + resultado.get(i).programado_fisico.size() - 1, 9, 9));
						}
						

					}
					
					

				}
			}
		}

// total
		XSSFRow rowPie = (XSSFRow) hoja1.createRow(filaNum);
		col = 0;
		celda = rowPie.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TOTALES");
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col + 4));
		col = col + 5;
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[0]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[1]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[2]);

		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(total[3]);
		
		celda = rowPie.createCell(col++);
		celda.setCellStyle(estilonumericoTituloTabladerecha);
		celda.setCellValue(totalSumaDato);
		
		filaNum = filaNum +2;
		
		XSSFRow rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		col = 7;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("RESUMEN COMPORTAMIENTO PRESUPUESTARIO DE "+fechaInicioLiteral+ " A " + fechaFinalLiteral);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		col++;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Bs.");
		rowResumen.setHeightInPoints(55);
		
		rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		col = 7; 
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloDatos);
		celda.setCellValue("PROGRAMADO");
		
		col++;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(total[1]);
		
		repo.regionConBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		
		rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		col = 7; 
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloDatos);
		celda.setCellValue("PREVENTIVO CERTIFICADO");

		col++;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(total[2]);
		
		repo.regionConBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		
		rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		col = 7; 
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloDatos);
		celda.setCellValue("SALDO SIN CERTIFICAR");
		col++;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(total[3]);
		
		repo.regionConBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
				
		rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		col = 7; 
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloDatos);
		celda.setCellValue("% DE EJECUCIÓN");
		
		Double porcentaje = total[1]!=0?(total[2]/total[1]*100):0;
		
		col++;
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloNumericoTablaDerecha);
		celda.setCellValue(porcentaje);
		
		repo.regionConBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		
		if(DatoViaticos!=0) {
			rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
			col = 7; 
			celda = rowResumen.createCell(col++);
			celda.setCellStyle(estiloDatosCentroRojo);
			celda.setCellValue("* Viaticos");
				
			col++;
			celda = rowResumen.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(DatoViaticos);
			
			repo.regionConBorde(hoja1,
					new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		}
		
		
		if(DatoPasajes!=0) {
			rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
			col = 7; 
			celda = rowResumen.createCell(col++);
			celda.setCellStyle(estiloDatosCentroRojo);
			celda.setCellValue("* Viaticos");
				
			col++;
			celda = rowResumen.createCell(col++);
			celda.setCellStyle(estiloNumericoTablaDerecha);
			celda.setCellValue(DatoPasajes);
			
			repo.regionConBorde(hoja1,
					new CellRangeAddress(filaNum-1, filaNum-1, 7,8));
		}
		
		col=0;
		rowResumen = (XSSFRow) hoja1.createRow(filaNum++);
		celda = rowResumen.createCell(col++);
		celda.setCellStyle(estiloCeldaTablaCentroRojoSinBorde);
		celda.setCellValue("* SE ENCUENTRAN EN TRANSITO, SUJETOS A APROBACIÓN DE DESCARGOS");
		repo.regionSinBorde(hoja1,
				new CellRangeAddress(filaNum-1, filaNum-1, 0,4));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		hssfw.write(outputStream);
		hssfw.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "[EJ_SEG_FISICO_PRESUP]_"
				+ StringUtils.abbreviate(resultado.get(0).nombre_act.replace(".", "").replace(" ", "_"), 20) + ".xlsx");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers,
				HttpStatus.OK);
		return response;

	}

}
