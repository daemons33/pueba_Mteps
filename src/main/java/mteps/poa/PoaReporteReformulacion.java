package mteps.poa;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import mteps.poa.entity.F_reporte_reform_presupuesto_gasto;
import mteps.poa.entity.F_reporte_reformulacion_poa;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import net.sf.jasperreports.engine.JRException;
@RestController
@RequestMapping("sispoa/reporte")
public class PoaReporteReformulacion {

///////////////////////REPORTE FORMULACION POA
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ConfigReport repo;
	
	Map modelo = new HashMap();
	
	public Object esEntero(double numero){
	    if (numero % 1 == 0) {
	        return (int) numero ;
	    } else {
	    	 return numero;
	    }
	}
	
	@GetMapping("/reformulacionPoa")
	public ResponseEntity<byte[]>  reporteReformulacionPoaAccionCortoPlazo(HttpServletRequest datosEntrada) throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException  {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_reformulacion_poa");

		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));

		F_reporte_reformulacion_poa resultado = (F_reporte_reformulacion_poa) procedure.getSingleResult();
		
		XSSFWorkbook hssfw = new XSSFWorkbook();
		
			SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormat df = new DecimalFormat("#.##");
			Sheet sheet = hssfw.createSheet();
			hssfw.setSheetName(0, "ACCIONES DE CORTO PLAZO");
			Sheet hoja1 = hssfw.createSheet();
			hssfw.setSheetName(1, "OPERACIONES");
			Sheet hoja2 = hssfw.createSheet();
			hssfw.setSheetName(2, "ACTIVIDADES");
			Sheet hoja3 = hssfw.createSheet();
			hssfw.setSheetName(3, "TAREAS ESPECIFICAS");

			org.apache.poi.ss.usermodel.Cell celda;

			Integer filaNum = 0;

////// Tamaño de columnas
			Integer tamanoColumna[] = { 10000, 10000, 9000, 8000, 2500, 5400, 2500 };

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

////// estilos datos
			CellStyle estiloDatos = hssfw.createCellStyle();
			estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

////// estilos datos
			CellStyle estiloDatosJustificado = hssfw.createCellStyle();
			estiloDatosJustificado = repo.estiloCeldaTablaJustificado(estiloDatosJustificado, hssfw);

			CellStyle aa = hssfw.createCellStyle();
////// estilos datos
			CellStyle estiloFirma = hssfw.createCellStyle();
			estiloFirma = repo.estiloFirma(estiloFirma, hssfw);

			String tipInd = "", tipIndvalor = "";

///////// ESTILO NUMERICO
			CellStyle estiloNumero = hssfw.createCellStyle();
			DataFormat formatoNumero = hssfw.createDataFormat();
			estiloNumero = repo.estiloNumericoTabla(estiloNumero, hssfw);

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
			anchor.setDy1(0);
			anchor.setDx2(150);
			anchor.setDy2(150);
			anchor.setCol1(0);
			anchor.setRow1(0);
			anchor.setCol2(0);
			anchor.setRow2(0);
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize();

///////////////////////
			
///////////////////			
			XSSFRow headerLeteralSup = (XSSFRow) sheet.createRow(filaNum);
			celda = headerLeteralSup.createCell(12);
			celda.setCellValue("ANEXO Nro. 1");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			XSSFRow headerLeteralSup1 = (XSSFRow) sheet.createRow(filaNum);
			celda = headerLeteralSup1.createCell(12);
			celda.setCellValue("REFORMULACIÓN PLAN OPERATIVO ANUAL - POA " + resultado.valor_gestion);
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
///////////////////////
			XSSFRow header = (XSSFRow) sheet.createRow(filaNum);
			celda = header.createCell(0);
			celda.setCellValue("ACCIONES DE CORTO PLAZO (ACP)");
			celda.setCellStyle(estiloTextoNormalNegrita);
			filaNum++;
			XSSFRow header1 = (XSSFRow) sheet.createRow(filaNum);
			celda = header1.createCell(0);
			celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			XSSFRow header2 = (XSSFRow) sheet.createRow(filaNum);
			celda = header2.createCell(0);
			celda.setCellValue(
					"PROGRAMA: " + resultado.sigla_act_presupuestario + " " + resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			filaNum++;
/////////////////////////////TABLA
			int col = 0;
			XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Articulación PGDES/PDES/PEM 2021 - 2025");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 0, 0));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Objetivo Estratégico PEI 2021 - 2025");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 1, 1));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Acción de Mediano Plazo PEI 2021-2025");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 2, 2));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Acciones de Corto Plazo");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Unidad de Medida");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Fórmula");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea Base 2022");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta 2023");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Programación 2023");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, col + 3));

			XSSFRow rowsub1 = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowsub1.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("1er Trimestre");
			col++;
			celda = rowsub1.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("2do Trimestre");
			col++;
			celda = rowsub1.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("3er Trimestre");
			col++;
			celda = rowsub1.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("4to Trimestre");

///////////////////////////// DATOS
			col = 0;
			int inicio = filaNum;
			if(resultado.detalle_formulacion != null) {
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {
				XSSFRow row = (XSSFRow) sheet.createRow(filaNum);

				if (resultado.pem_pei != null && n == 0) {

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.pem_pei.nombre_eje + " " + resultado.pem_pei.descripcion_eje + " "
							+ resultado.pem_pei.nombre_meta + " " + resultado.pem_pei.descripcion_meta + " "
							+ resultado.pem_pei.nombre_resultado + " " + resultado.pem_pei.descripcion_resultado + " "
							+ resultado.pem_pei.nombre_accion_pdes + " " + resultado.pem_pei.descripcion_accion_pdes);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.pem_pei.descripcion_res_pei);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.pem_pei.descripcion_accion_pei);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.detalle_formulacion.get(n).descripcion);
				}

				for (int i = 0; i < resultado.detalle_formulacion.get(n).programacion_indicador_meta.size(); i++) {

					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).tipo_indicador);
					if (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).tipo_indicador
							.equals("Indicador Relativo")) {
						tipInd = "%";
					} else {
						tipInd = "";
					}
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).denominacion);

					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta
							.get(i).unidad == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).unidad));
					celda.setCellStyle(estiloNumero);

					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta
							.get(i).formula == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).formula));
					celda.setCellStyle(estiloDatos);

					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta
							.get(i).linea_base == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).linea_base
											+ tipInd));
					celda.setCellStyle(estiloNumero);

					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta
							.get(i).meta == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).meta
											+ tipInd));
					celda.setCellStyle(estiloNumero);

					if (resultado.detalle_formulacion.get(n).programacion_indicador_meta
							.get(i).programacion_indicador_meta != null) {

						for (int k = 0; k < resultado.detalle_formulacion.get(n).programacion_indicador_meta
								.get(i).programacion_indicador_meta.size(); k++) {

							if (tipInd == "") {
								celda = row.createCell(col++);
								celda.setCellStyle(estiloNumero);
								celda.setCellValue(resultado.detalle_formulacion.get(n).programacion_indicador_meta
										.get(i).programacion_indicador_meta.get(k).valor_programado_num != 0
												? resultado.detalle_formulacion.get(n).programacion_indicador_meta
														.get(i).programacion_indicador_meta.get(k).valor_programado_num
												: 0);
							} else {
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).programacion_indicador_meta
										.get(i).programacion_indicador_meta.get(k).valor_programado_num != 0
												? resultado.detalle_formulacion.get(n).programacion_indicador_meta
														.get(i).programacion_indicador_meta.get(k).valor_programado_num
												: 0)
										+ tipInd;
								celda.setCellValue(tipIndvalor);
								tipIndvalor = "";
							}

						}

					} else {
						celda = row.createCell(col++);
						celda.setCellValue(0);
						celda.setCellStyle(estiloNumero);

						celda = row.createCell(col++);
						celda.setCellValue(0);
						celda.setCellStyle(estiloNumero);

						celda = row.createCell(col++);
						celda.setCellValue(0);
						celda.setCellStyle(estiloNumero);

						celda = row.createCell(col++);
						celda.setCellValue(0);
						celda.setCellStyle(estiloNumero);
					}
					filaNum++;
					col = col - 10;
					row = (XSSFRow) sheet.createRow(filaNum);
				}

// BORDES DE TABLA
				if (resultado.detalle_formulacion.get(n).programacion_indicador_meta != null
						&& resultado.detalle_formulacion.get(n).programacion_indicador_meta.size() > 1) {
					for (int contador = 0; contador < 4; contador++) {

						CellRangeAddress region = new CellRangeAddress(inicio,
								inicio + resultado.detalle_formulacion.get(n).programacion_indicador_meta.size() - 1,
								contador, contador);
						sheet.addMergedRegion(region);
						RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
					}
				}

			}
		}

//////////////////////////////PIE FIRMAS			
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			XSSFRow rowFirmas = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowFirmas.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 0, 1));

			celda = rowFirmas.createCell(2);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 2, 7));

			celda = rowFirmas.createCell(8);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 8, 12));

			filaNum++;
			XSSFRow rowFirmas1 = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowFirmas1.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 1));

			celda = rowFirmas1.createCell(2);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REACP");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 2, 7));

			celda = rowFirmas1.createCell(8);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 8, 12));

//////////////////////// HOJA 1

////// Tamaño de columnas
			Integer tamanoColumna2[] = { 10000, 2500, 2500, 10000, 6500, 2500, 2500, 2500, 2500, 2500, 2500 };

			for (int i = 0; i < tamanoColumna2.length; i++) {
				hoja1.setColumnWidth(i, tamanoColumna2[i]);
			}
///////////////////////
			filaNum = 0;
///// IMAGEN

			CreationHelper helper1 = hssfw.getCreationHelper();
			Drawing drawing1 = hoja1.createDrawingPatriarch();
			ClientAnchor anchor1 = helper1.createClientAnchor();

			Picture pict1 = drawing1.createPicture(anchor1, pictureIdx);
			pict1.resize();

			XSSFRow headerLeteralSuph1 = (XSSFRow) hoja1.createRow(filaNum);
			celda = headerLeteralSuph1.createCell(9);
			celda.setCellValue("ANEXO Nro. 1");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			XSSFRow headerLeteralSup1h1 = (XSSFRow) hoja1.createRow(filaNum);
			celda = headerLeteralSup1h1.createCell(9);
			celda.setCellValue("REFORMUACIÓN PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
///////////////////////
			XSSFRow headerh1 = (XSSFRow) hoja1.createRow(filaNum);
			celda = headerh1.createCell(0);
			celda.setCellValue("OPERACIONES");
			celda.setCellStyle(estiloTextoNormalNegrita);
			filaNum++;
			XSSFRow header1h1 = (XSSFRow) hoja1.createRow(filaNum);
			celda = header1h1.createCell(0);
			celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			XSSFRow header2h1 = (XSSFRow) hoja1.createRow(filaNum);
			celda = header2h1.createCell(0);
			celda.setCellValue(
					"PROGRAMA:  " + resultado.sigla_act_presupuestario + " " + resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			filaNum++;
/////////////////////////////TABLA

			for (int j = 0; j <= 0; j++) {

				XSSFRow rowsub1h1 = (XSSFRow) hoja1.createRow(filaNum);

				celda = rowsub1h1.createCell(0);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Operación");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 0));

				celda = rowsub1h1.createCell(1);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Pond.");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 1, 1));

				celda = rowsub1h1.createCell(2);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("TI");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 2, 2));

				celda = rowsub1h1.createCell(3);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Indicador");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 3, 3));

				celda = rowsub1h1.createCell(4);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Linea Base");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 4, 4));

				celda = rowsub1h1.createCell(5);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Meta");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 5, 5));

				celda = rowsub1h1.createCell(6);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Programación");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 6, 9));

				filaNum++;
				XSSFRow rowsub2h1 = (XSSFRow) hoja1.createRow(filaNum);

				celda = rowsub2h1.createCell(6);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("1er Trimestre");
				celda = rowsub2h1.createCell(7);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("2do Trimestre");
				celda = rowsub2h1.createCell(8);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("3er Trimestre");
				celda = rowsub2h1.createCell(9);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("4to Trimestre");
				filaNum++;

			}
///////////////////////////////// DATOS
			if(resultado.detalle_formulacion!= null) {
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {

				XSSFRow rowsubh1 = (XSSFRow) hoja1.createRow(filaNum);

				celda = rowsubh1.createCell(0);
				celda.setCellStyle(estiloSubTituloTabla);
				celda.setCellValue("ACP: " + resultado.detalle_formulacion.get(n).descripcion);
				rowsubh1.setHeight((short) 800);
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 9));

				col = 0;
				filaNum++;

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {
					XSSFRow row = (XSSFRow) hoja1.createRow(filaNum);

					if (resultado.detalle_formulacion != null) {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
					} else {
						col++;
					}
					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones
							.get(i).programacion_indicador_meta.size(); j++) {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(Integer.toString((int) resultado.detalle_formulacion.get(n).operaciones
								.get(i).programacion_indicador_meta.get(j).ponderacion) + "%");
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
										.get(j).tipo_indicador);
						if (resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
								.get(j).tipo_indicador.equals("Indicador Relativo")) {
							tipInd = "%";
						} else {
							tipInd = "";
						}
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
										.get(j).denominacion);

						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumero);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
										.get(j).linea_base + tipInd);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumero);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
										.get(j).meta + tipInd);

						for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones
								.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.size(); k++) {
							celda = row.createCell(col++);
							if (tipInd == "") {
								celda.setCellStyle(estiloNumero);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones
										.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta
												.get(k).valor_programado_num != 0
														? (int) resultado.detalle_formulacion.get(n).operaciones
																.get(i).programacion_indicador_meta
																		.get(j).programacion_indicador_meta
																				.get(k).valor_programado_num
														: 0);
							} else {
								celda.setCellStyle(estiloDatos);
								tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).operaciones
										.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta
												.get(k).valor_programado_num != 0
														? resultado.detalle_formulacion.get(n).operaciones
																.get(i).programacion_indicador_meta
																		.get(j).programacion_indicador_meta
																				.get(k).valor_programado_num
														: 0)
										+ tipInd;
								celda.setCellValue(tipIndvalor);
								tipIndvalor = "";
							}

						}

					}
					col = 0;
					filaNum++;

				}

			}
		}
///////////////////////////////// FIRMAS

			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			XSSFRow rowFirmash1 = (XSSFRow) hoja1.createRow(filaNum);

			celda = rowFirmash1.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 2));

			celda = rowFirmash1.createCell(3);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 3, 5));

			celda = rowFirmash1.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 6, 9));

			filaNum++;
			filaNum++;
			XSSFRow rowFirmas1h1 = (XSSFRow) hoja1.createRow(filaNum);

			celda = rowFirmas1h1.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 2));

			celda = rowFirmas1h1.createCell(3);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REACP");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 3, 5));

			celda = rowFirmas1h1.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 6, 9));

/////////////////////////////// HOJA 2			
////// Tamaño de columnas
			Integer tamanoColumna3[] = { 11000, 3000, 6000, 6000, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500,
					2500, 2500, 2500 };

			for (int i = 0; i < tamanoColumna3.length; i++) {
				hoja2.setColumnWidth(i, tamanoColumna3[i]);
			}
			filaNum = 0;

///// IMAGEN

			CreationHelper helper2 = hssfw.getCreationHelper();
			Drawing drawing2 = hoja2.createDrawingPatriarch();
			ClientAnchor anchor2 = helper2.createClientAnchor();

			Picture pict2 = drawing2.createPicture(anchor2, pictureIdx);
			pict2.resize();

///////////////////////
			filaNum = 0;
			XSSFRow headerLeteralSuph2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = headerLeteralSuph2.createCell(18);
			celda.setCellValue("ANEXO Nro. 1");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			XSSFRow headerLeteralSup1h2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = headerLeteralSup1h2.createCell(18);
			celda.setCellValue("REFORMUACIÓN PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
///////////////////////
			XSSFRow headerh2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = headerh2.createCell(0);
			celda.setCellValue("ACTIVIDADES");
			celda.setCellStyle(estiloTextoNormalNegrita);
			filaNum++;
			XSSFRow header1h2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = header1h2.createCell(0);
			celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			XSSFRow header2h2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = header2h2.createCell(0);
			celda.setCellValue(
					"PROGRAMA: " + resultado.sigla_act_presupuestario + " " + resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			filaNum++;

//////////////////TABLA

			XSSFRow rowsub1h2 = (XSSFRow) hoja2.createRow(filaNum);

			celda = rowsub1h2.createCell(0);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Objetivo ACT");

			celda = rowsub1h2.createCell(1);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");

			celda = rowsub1h2.createCell(2);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");

			celda = rowsub1h2.createCell(3);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Medio de verificación");

			celda = rowsub1h2.createCell(4);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea");

			celda = rowsub1h2.createCell(5);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta");

			celda = rowsub1h2.createCell(6);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Pond.");

			String[] mesesh2 = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
			int h2d = 7;
			for (int j = 0; j < mesesh2.length; j++) {
				celda = rowsub1h2.createCell(h2d);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(mesesh2[j]);
				h2d++;
			}

/////////////////////////////////DATOS
			col = 0;
			filaNum++;
			h2d--;
			if(resultado.detalle_formulacion!= null) {
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {

					XSSFRow row1 = (XSSFRow) hoja2.createRow(filaNum);
					celda = row1.createCell(0);
					celda.setCellStyle(estiloSubTituloTabla);
					celda.setCellValue(
							"OPERACIÓN: " + resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
					hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, h2d));
					filaNum++;
					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
							.size(); j++) {
						XSSFRow row = (XSSFRow) hoja2.createRow(filaNum++);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).descripcion);

						for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
								.get(j).programacion_indicador_meta.size(); k++) {
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).tipo_indicador);
							if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).tipo_indicador
											.equals("Indicador Relativo")) {
								tipInd = "%";
							} else {
								tipInd = "";
							}
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).denominacion);
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).medio_verificacion);
							celda = row.createCell(col++);
							celda.setCellStyle(estiloNumero);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).linea_base + tipInd);
							celda = row.createCell(col++);
							celda.setCellStyle(estiloNumero);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).meta + tipInd);
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(Integer
									.toString((int) resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
											.get(j).programacion_indicador_meta.get(k).ponderacion)
									+ "%");

							for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta
											.size(); l++) {
								celda = row.createCell(col++);
								if (tipInd == "") {
									celda.setCellStyle(estiloNumero);
									celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones
											.get(i).actividades.get(j).programacion_indicador_meta
													.get(k).programacion_indicador_meta.get(l).valor_programado_num != 0
															? resultado.detalle_formulacion.get(n).operaciones
																	.get(i).actividades
																			.get(j).programacion_indicador_meta
																					.get(k).programacion_indicador_meta
																							.get(l).valor_programado_num
															: 0);
								} else {
									celda.setCellStyle(estiloDatos);
									tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).operaciones
											.get(i).actividades.get(j).programacion_indicador_meta
													.get(k).programacion_indicador_meta.get(l).valor_programado_num != 0
															? resultado.detalle_formulacion.get(n).operaciones
																	.get(i).actividades
																			.get(j).programacion_indicador_meta
																					.get(k).programacion_indicador_meta
																							.get(l).valor_programado_num
															: 0)
											+ tipInd;
									celda.setCellValue(tipIndvalor);
								}
								tipIndvalor = "";

							}
						}
						col = 0;
					}
					col = 0;

				}
			}
		}
///////////////////////////////// FIRMAS

			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			XSSFRow rowFirmash2 = (XSSFRow) hoja2.createRow(filaNum);

			celda = rowFirmash2.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 5));

			celda = rowFirmash2.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 6, 11));

			celda = rowFirmash2.createCell(12);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 12, 18));

			filaNum++;
			filaNum++;
			XSSFRow rowFirmas1h2 = (XSSFRow) hoja2.createRow(filaNum);

			celda = rowFirmas1h2.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));

			celda = rowFirmas1h2.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REACP");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 6, 11));

			celda = rowFirmas1h2.createCell(12);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 12, 18));

/////////////////////////////// HOJA 3	
////// Tamaño de columnas
			Integer tamanoColumna4[] = { 11000, 3000, 6000, 6000, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500,
					2500, 2500, 2500 };

			for (int i = 0; i < tamanoColumna4.length; i++) {
				hoja3.setColumnWidth(i, tamanoColumna4[i]);
			}
			filaNum = 0;
///// IMAGEN

			CreationHelper helper3 = hssfw.getCreationHelper();
			Drawing drawing3 = hoja3.createDrawingPatriarch();
			ClientAnchor anchor3 = helper3.createClientAnchor();

			Picture pict3 = drawing3.createPicture(anchor3, pictureIdx);
			pict3.resize();
///////////////////////
			filaNum = 0;
			XSSFRow headerLeteralSuph3 = (XSSFRow) hoja3.createRow(filaNum);
			celda = headerLeteralSuph3.createCell(18);
			celda.setCellValue("ANEXO Nro. 1");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			XSSFRow headerLeteralSup1h3 = (XSSFRow) hoja3.createRow(filaNum);
			celda = headerLeteralSup1h3.createCell(18);
			celda.setCellValue("REFORMUACIÓN PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
///////////////////////
			XSSFRow headerh3 = (XSSFRow) hoja3.createRow(filaNum);
			celda = headerh3.createCell(0);
			celda.setCellValue("TAREAS ESPECÍFICAS");
			celda.setCellStyle(estiloTextoNormalNegrita);
			filaNum++;
			XSSFRow header1h3 = (XSSFRow) hoja3.createRow(filaNum);
			celda = header1h3.createCell(0);
			celda.setCellValue("UNIDAD ORGANIZACIONAL: " + resultado.org_unidad_funcional);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			XSSFRow header2h3 = (XSSFRow) hoja3.createRow(filaNum);
			celda = header2h3.createCell(0);
			celda.setCellValue(
					"PROGRAMA: " + resultado.sigla_act_presupuestario + " " + resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
			filaNum++;

//////////////////TABLA

			XSSFRow rowsub1h3 = (XSSFRow) hoja3.createRow(filaNum);

			celda = rowsub1h3.createCell(0);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Objetivo TE");

			celda = rowsub1h3.createCell(1);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");

			celda = rowsub1h3.createCell(2);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");

			celda = rowsub1h3.createCell(3);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Medio de verificación");

			celda = rowsub1h3.createCell(4);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea");

			celda = rowsub1h3.createCell(5);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta");

			celda = rowsub1h3.createCell(6);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Pond.");

			h2d = 7;
			for (int j = 0; j < mesesh2.length; j++) {
				celda = rowsub1h3.createCell(h2d);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue(mesesh2[j]);
				h2d++;
			}

//////////////////////////////// DATOS TABLA
			filaNum++;
			h2d--;
			if(resultado.detalle_formulacion!= null) {
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {

					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
							.size(); j++) {

						XSSFRow row1 = (XSSFRow) hoja3.createRow(filaNum);
						celda = row1.createCell(0);
						celda.setCellStyle(estiloSubTituloTabla);
						celda.setCellValue(
								"ACTIVIDAD: " + resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).descripcion);
						hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, h2d));
						filaNum++;

						for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
								.get(j).tarea_especifica.size(); k++) {

							XSSFRow row = (XSSFRow) hoja3.createRow(filaNum++);
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatosJustificado);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).tarea_especifica.get(k).descripcion);

							for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
									.get(j).tarea_especifica.get(k).programacion_indicador_meta.size(); l++) {

								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).tipo_indicador);
								if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).tipo_indicador.equals("Indicador Relativo")) {
									tipInd = "%";
								} else {
									tipInd = "";
								}
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).denominacion);
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).medio_verificacion);
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).linea_base
										+ tipInd);
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).meta
										+ tipInd);
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(
										esEntero(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
												.get(j).tarea_especifica.get(k).programacion_indicador_meta
														.get(l).ponderacion)
												+ "%");

								if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).programacion_indicador_meta != null) {
									for (int m = 0; m < resultado.detalle_formulacion.get(n).operaciones
											.get(i).actividades.get(j).tarea_especifica
													.get(k).programacion_indicador_meta
															.get(l).programacion_indicador_meta.size(); m++) {
										celda = row.createCell(col++);

										if (tipInd == "") {
											celda.setCellStyle(estiloNumero);
											celda.setCellValue(
													resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
															.get(j).tarea_especifica.get(k).programacion_indicador_meta
																	.get(l).programacion_indicador_meta
																			.get(m).valor_programado_num != 0
																					? (int) resultado.detalle_formulacion
																							.get(n).operaciones.get(
																									i).actividades.get(
																											j).tarea_especifica
																													.get(k).programacion_indicador_meta
																															.get(l).programacion_indicador_meta
																																	.get(m).valor_programado_num
																					: 0);
										} else {
											celda.setCellStyle(estiloDatos);
											tipIndvalor = esEntero(
													resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
															.get(j).tarea_especifica.get(k).programacion_indicador_meta
																	.get(l).programacion_indicador_meta
																			.get(m).valor_programado_num != 0
																					? resultado.detalle_formulacion
																							.get(n).operaciones.get(
																									i).actividades.get(
																											j).tarea_especifica
																													.get(k).programacion_indicador_meta
																															.get(l).programacion_indicador_meta
																																	.get(m).valor_programado_num
																					: 0)
													+ tipInd;
											celda.setCellValue(tipIndvalor);
										}

										tipIndvalor = "";
									}
								} else {

									for (int m = 0; m < 12; m++) {
										celda = row.createCell(col++);
										celda.setCellStyle(estiloDatos);
										celda.setCellValue("NH");

									}
								}
								col = 0;
							}
							col = 0;
						}

						col = 0;
					}
					col = 0;

				}
			}
		}
///////////////////////////////// FIRMAS

			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
			XSSFRow rowFirmash3 = (XSSFRow) hoja3.createRow(filaNum);

			celda = rowFirmash3.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 0, 5));

			celda = rowFirmash3.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 6, 11));

			celda = rowFirmash3.createCell(12);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 12, 18));

			filaNum++;
			filaNum++;
			XSSFRow rowFirmas1h3 = (XSSFRow) hoja3.createRow(filaNum);

			celda = rowFirmas1h3.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));

			celda = rowFirmas1h3.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REACP");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 6, 11));

			celda = rowFirmas1h3.createCell(12);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 12, 18));

						
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    hssfw.write(outputStream);
		    hssfw.close();

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		    headers.setContentDispositionFormData("attachment", "[REFORM. POA] "+ resultado.org_unidad_funcional + ".xlsx");
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
		    return response;

	}
	
	
//////REPORTE REFORMULACION PRESUPUESTO GASTO

	@GetMapping("/reformPresupGasto")
	public ModelAndView reportePoaAccionCortoPlazo(HttpServletRequest datosEntrada) throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_reform_presupuesto_gasto");

		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));
		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		F_reporte_reform_presupuesto_gasto resultadoDatos = (F_reporte_reform_presupuesto_gasto) procedure
				.getSingleResult();

		modelo.put("registros", resultadoDatos);
		return new ModelAndView(new PoaReporteReformulacion.reportePoaAccionCortoPlazoExcel(), modelo);

	}

	public class reportePoaAccionCortoPlazoExcel extends AbstractXlsxView {

		@Override
		public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
				HttpServletResponse hsr1) throws Exception {

			DecimalFormat df = new DecimalFormat("#.##");
			Sheet sheet = hssfw.createSheet();
			hssfw.setSheetName(0, "REFORMULACIÓN PRESUPUESTO DE GASTO");

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

			F_reporte_reform_presupuesto_gasto resultado = (F_reporte_reform_presupuesto_gasto) map.get("registros");

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
			if(resultado.partidas != null) {
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
						String fuente="";
						if(resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento != null) {
						for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento.size()  ; it++) {
							if (it == 0) {
								fuente = resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento.get(it).clasificador;
							} else {
								fuente = fuente + ", " + resultado.partidas.get(i).partidas.get(j).fuentes_financiamiento.get(it).clasificador;
							}
						}
						}
						
						XSSFRow rowFuente = (XSSFRow) hoja.createRow(filaNum++);
						rowFuente.setHeight((short) 300);
						celda = rowFuente.createCell(0);
						celda.setCellValue("FUENTE: " + fuente);
						
						String org="";
						if(resultado.partidas.get(i).partidas.get(j).organismo_financiador != null) {
						for (int it = 0; it < resultado.partidas.get(i).partidas.get(j).organismo_financiador.size()  ; it++) {
							if (it == 0) {
								org = resultado.partidas.get(i).partidas.get(j).organismo_financiador.get(it).clasificador;
							} else {
								org = fuente + ", " + resultado.partidas.get(i).partidas.get(j).organismo_financiador.get(it).clasificador;
							}
						}
						}

						XSSFRow rowOrgFinanciero = (XSSFRow) hoja.createRow(filaNum++);
						rowOrgFinanciero.setHeight((short) 300);
						celda = rowOrgFinanciero.createCell(0);
						celda.setCellValue("ORG. FIN.: "+ org);

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
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, col -1,col -1));
						
						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("FUENTE DE FINANCIAMIENTO");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));
						
						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("DESCRIPCIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("CANTIDAD");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("UNIDAD MEDIDA");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("PRECIO UNITARIO");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("IMPORTE TOTAL");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("JUSTIFICACIÓN");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1,  col -1,col -1));

						celda = rowsubM.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("PROGRAMACIÓN MENSUAL");
						hoja.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col -1, 31));
						
						
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
						double[] total_cant = {0,0,0,0,0,0,0,0,0,0,0,0};
						double[]total_cost= {0,0,0,0,0,0,0,0,0,0,0,0};
						for (int k = 0; k < resultado.partidas.get(i).partidas.get(j).memorias_calculo.size(); k++) {

							XSSFRow row2 = (XSSFRow) hoja.createRow(filaNum);

							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(k + 1);
							
							celda = row2.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(
									 resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).sigla_fuente_financiamiento);
							
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
									total_cant[l]= total_cant[l] +resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).programacion.get(l).cantidad;
									
									celda = row2.createCell(col + 1);
									celda.setCellStyle(estiloNumericoDerecha);
									celda.setCellValue(resultado.partidas.get(i).partidas.get(j).memorias_calculo
											.get(k).programacion.get(l).costo);
									total_cost[l]= total_cost[l] +resultado.partidas.get(i).partidas.get(j).memorias_calculo.get(k).programacion.get(l).costo;
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
			hsr1.setContentType("application/vnd.ms-excel"); // Tell the browser to expect an excel file
			hsr1.setHeader("Content-Disposition",
					"attachment; filename=[REFORM. PRESUP. GASTO] - " + resultado.org_unidad_funcional + ".xlsx");

		}

	}

	
	
}
