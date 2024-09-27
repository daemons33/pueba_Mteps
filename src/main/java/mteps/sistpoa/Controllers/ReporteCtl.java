package mteps.sistpoa.Controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import mteps.poa.entity.F_reporte_formulacion;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Pojos.ReporteMemoriaCalculo;
import mteps.sistpoa.Servicios.impl.ConfigReport;

@RestController
@RequestMapping("sispoa/reporte")
public class ReporteCtl {

	Map modelo = new HashMap();

	@Autowired
	PlanMap planMap;

	@Autowired
	ConfigReport repo;

	@Value("${directorio.archivos}")
	public String dir_archivos;

	@Value("${directorio.enlace}")
	public String dir_enlace;

	@PersistenceContext
	private EntityManager entityManager;

	// Prueba
	@GetMapping("/prueba")
	public List<ReporteMemoriaCalculo> reportePrueba(HttpServletRequest request) throws ParseException {
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_memoria_calculo");

		procedure.setParameter("v_id_org", Integer.parseInt(request.getParameter("idUnidadOrg")));

		List<ReporteMemoriaCalculo> resultadoDatos = procedure.getResultList();
		return resultadoDatos;

	}


	////// REPORTE APERTURA ESTRUCTURA PROGRAMATICA

	@GetMapping("/estructuraprogramatica")
	public ModelAndView reporteEstructuraProgramaticaCalculoExcel(HttpServletRequest datosEntrada)
			throws ParseException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_memoria_calculo");

		procedure.setParameter("v_id_org", Integer.parseInt(datosEntrada.getParameter("idUnidadOrg")));

		List<ReporteMemoriaCalculo> resultadoDatos = procedure.getResultList();

		modelo.put("registros", resultadoDatos);
		return new ModelAndView(new ReporteCtl.reporteEstructuraProgramaticaCalculoExcel(), modelo);

	}

	public class reporteEstructuraProgramaticaCalculoExcel extends AbstractXlsxView {

		@Override
		public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
				HttpServletResponse hsr1) throws Exception {
			SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormat df = new DecimalFormat("#.##");
			Sheet sheet = hssfw.createSheet();
			hssfw.setSheetName(0, "ESTRUCTURA MODIFICADA");
			org.apache.poi.ss.usermodel.Cell celda;

			Integer filaNum = 6;

			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 10000);

			////// estilos titulo
			CellStyle estiloTitulo = hssfw.createCellStyle();
			estiloTitulo = repo.estiloTitulo(estiloTitulo, hssfw);

			////// estilos Titulos Tabla
			CellStyle estiloTituloTabla = hssfw.createCellStyle();
			estiloTituloTabla = repo.estiloTituloTabla(estiloTituloTabla, hssfw);

			////// estilos datos
			CellStyle estiloDatos = hssfw.createCellStyle();
			estiloDatos = repo.estiloCeldaTabla(estiloDatos, hssfw);

			///// IMAGEN
			String rutaImg = hsr.getRealPath("") + "/assets/logo2.jpg";
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
			String gestion = "2022";
			XSSFRow header = (XSSFRow) sheet.createRow(filaNum);
			celda = header.createCell(0);
			celda.setCellValue("APERTURA ESTRUCTURA PROGRAMATICA - " + gestion);
			celda.setCellStyle(estiloTitulo);
			CellRangeAddress cellMerge = new CellRangeAddress(filaNum, filaNum, 0, 1);
			sheet.addMergedRegion(cellMerge);
			header.setHeight((short) 500);

			filaNum++;
			/////////////////////////////

			XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowsub.createCell(0);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Descripción de la Acción Estratégica Institucional");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 0, 0));

			celda = rowsub.createCell(1);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("CATEGORÍA PROGRAMÁTICA (Presupuestaria)");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum + 1, 1, 1));

			///////////////////////////// DATOS

			Integer i = 0;
			List resultado = (List) map.get("registros");
			int rowNum = 2;
			for (Object o : resultado) {
				i++;
				// Plan datos = (Plan) o;

//				XSSFRow row = (XSSFRow) sheet.createRow(rowNum++);
//				row.setHeight((short) 500);
//
//				celda = row.createCell(0);
//				celda.setCellValue(datos);
//				celda.setCellStyle(estiloDatos);
//
//				celda = row.createCell(1);
//				celda.setCellValue(datos.getSigla());
//				celda.setCellStyle(estiloDatos);

			}

		}
	}
	
	public Object esEntero(double numero){
	    if (numero % 1 == 0) {
	        return (int) numero ;
	    } else {
	    	 return numero;
	    }
	}
///////////////////////REPORTE FORMULACION POA
	@GetMapping("/poa-acp")
	public ResponseEntity<byte[]> reportePoaAccionCortoPlazo(HttpServletRequest datosEntrada) throws ParseException, IOException, DecoderException {
	
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_formulacion_poa");

		procedure.setParameter("v_id_plan", Integer.parseInt(datosEntrada.getParameter("idPlan")));
		procedure.setParameter("v_id_estado", Integer.parseInt(datosEntrada.getParameter("idEstado")));
		F_reporte_formulacion resultado = (F_reporte_formulacion) procedure.getSingleResult();
		
		XSSFWorkbook hssfw = new XSSFWorkbook();
		
		Sheet sheet = hssfw.createSheet();
		hssfw.setSheetName(0, "ACCIONES DE CORTO PLAZO");
		Sheet hoja1 = hssfw.createSheet();
		hssfw.setSheetName(1, "OPERACIONES");
		Sheet hoja2 = hssfw.createSheet();
		hssfw.setSheetName(2, "ACTIVIDADES");
		Sheet hoja3 = hssfw.createSheet();
		hssfw.setSheetName(3, "TAREAS ESPECIFICAS");

	    Cell celda;

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
		celda.setCellValue("PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
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
		celda.setCellValue("PROGRAMA: "  + resultado.sigla_act_presupuestario + " " +resultado.nombre_act_presupuestario);
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
		celda.setCellValue("Linea Base " +(resultado.valor_gestion-1));
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
		col++;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Meta "+resultado.valor_gestion);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, col, col));
		col++;
		celda = rowsub.createCell(col);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Programación "+resultado.valor_gestion);
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
			
			if(resultado.detalle_formulacion.get(n).programacion_indicador_meta !=null) {
			for(int i=0; i< resultado.detalle_formulacion.get(n).programacion_indicador_meta.size(); i++) {
				
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).tipo_indicador);
			if (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).id_tipo_identificador == 6) {
				tipInd = "%";
			} else {
				tipInd = "";
			}
			celda = row.createCell(col++);
			celda.setCellStyle(estiloDatosJustificado);
			celda.setCellValue(resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).denominacion);

			celda = row.createCell(col++);
			celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).unidad == null ? ""
					: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).unidad));
			celda.setCellStyle(estiloNumero);

			celda = row.createCell(col++);
			celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).formula == null ? ""
					: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).formula));
			celda.setCellStyle(estiloDatos);

			celda = row.createCell(col++);
			celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).linea_base == null ? ""
					: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).linea_base+tipInd));
			celda.setCellStyle(estiloNumero);

			celda = row.createCell(col++);
			celda.setCellValue(
					(String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).meta == null ? "" : resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).meta+tipInd));
			celda.setCellStyle(estiloNumero);

			if (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta != null) {

				for (int k = 0; k < resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.size(); k++) {
					
					
					
					if (tipInd == "") {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloNumero);
						celda.setCellValue(resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num != 0
										? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num
										: 0);
					} else {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num != 0
										? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num
										: 0)
								+ tipInd;
						celda.setCellValue(tipIndvalor);
						tipIndvalor = "";
					}

				}

			}else {
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
		}}
			
			// BORDES DE TABLA
			if(resultado.detalle_formulacion.get(n).programacion_indicador_meta!=null && resultado.detalle_formulacion.get(n).programacion_indicador_meta.size()>1 ) {
			for(int contador = 0;contador<4;contador++) {
			
			CellRangeAddress  region = new CellRangeAddress(inicio , inicio+resultado.detalle_formulacion.get(n).programacion_indicador_meta.size()-1, contador, contador);
			sheet.addMergedRegion( region);
			RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
			}}	
			
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
		Integer tamanoColumna2[] = { 10000, 2500, 2500, 10000,6500, 2500, 2500, 2500, 2500, 2500, 2500 };

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
		celda.setCellValue("PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
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
		celda.setCellValue("PROGRAMA:  " + resultado.sigla_act_presupuestario + " " +resultado.nombre_act_presupuestario );
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
		
		for(int n=0 ; n<resultado.detalle_formulacion.size();n++){
			
		XSSFRow rowsubh1 = (XSSFRow) hoja1.createRow(filaNum);
	
		celda = rowsubh1.createCell(0);
		celda.setCellStyle(estiloSubTituloTabla);
		celda.setCellValue("ACP: " + resultado.detalle_formulacion.get(n).descripcion);
		rowsubh1.setHeight((short) 800);
		hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 9));
		
		col = 0;
		filaNum++;
		if(resultado.detalle_formulacion.get(n).operaciones !=null) {
		for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {
			XSSFRow row = (XSSFRow) hoja1.createRow(filaNum);

			if (resultado.detalle_formulacion != null) {
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
			} else {
				col++;
			}
			
			if(resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta != null) {
				
			
			for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.size(); j++) {
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(Integer.toString(
						(int) resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).ponderacion)
						+ "%");
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(
						resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).tipo_indicador);
				if (resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
						.get(j).id_tipo_identificador == 6) {
					tipInd = "%";
				} else {
					tipInd = "";
				}
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(
						resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).denominacion);
				

				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumero);
				celda.setCellValue(
						resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).linea_base+tipInd);
				celda = row.createCell(col++);
				celda.setCellStyle(estiloNumero);
				celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).meta+tipInd);
				
				if(resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta!=null) {
				for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
						.get(j).programacion_indicador_meta.size(); k++) {
					celda = row.createCell(col++);
					if (tipInd == "") {
						celda.setCellStyle(estiloNumero);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
								.get(j).programacion_indicador_meta.get(k).valor_programado_num != 0
										? (int) resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
												.get(j).programacion_indicador_meta.get(k).valor_programado_num
										: 0);
					} else {
						celda.setCellStyle(estiloDatos);
						tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
										.get(j).programacion_indicador_meta.get(k).valor_programado_num != 0
												? resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta
																.get(j).programacion_indicador_meta
																		.get(k).valor_programado_num
												: 0)
								+ tipInd;
						celda.setCellValue(tipIndvalor);
						tipIndvalor = "";
					}

				}}

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
		celda.setCellValue("PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
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
		celda.setCellValue("PROGRAMA: "  + resultado.sigla_act_presupuestario + " " +resultado.nombre_act_presupuestario);
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
		
		for(int n=0 ; n<resultado.detalle_formulacion.size();n++){
		if(resultado.detalle_formulacion.get(n).operaciones != null) {
		for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {

			XSSFRow row1 = (XSSFRow) hoja2.createRow(filaNum);
			celda = row1.createCell(0);
			celda.setCellStyle(estiloSubTituloTabla);
			celda.setCellValue("OPERACIÓN: " + resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, h2d));
			filaNum++;
			if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades != null) {
			for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.size(); j++) {
				XSSFRow row = (XSSFRow) hoja2.createRow(filaNum++);
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).descripcion);
				if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
						.get(j).programacion_indicador_meta !=null) {
				for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
						.get(j).programacion_indicador_meta.size(); k++) {
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
									.get(k).tipo_indicador);
					if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
							.get(k).id_tipo_identificador==6) {
						tipInd = "%";
					} else {
						tipInd = "";
					}
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
									.get(k).denominacion);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
									.get(k).medio_verificacion);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloNumero);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
									.get(k).linea_base+tipInd);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloNumero);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
									.get(k).meta+tipInd);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(Integer.toString((int) resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).ponderacion) + "%");
					if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta != null) {
					for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.size(); l++) {
						celda = row.createCell(col++);
						if (tipInd == "") {
							celda.setCellStyle(estiloNumero);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta
											.get(l).valor_programado_num != 0
													?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
																	.get(k).programacion_indicador_meta
																			.get(l).valor_programado_num
													: 0);
						} else {
							celda.setCellStyle(estiloDatos);
							tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta
											.get(l).valor_programado_num != 0
													?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta
																	.get(k).programacion_indicador_meta
																			.get(l).valor_programado_num
													: 0)
									+ tipInd;
							celda.setCellValue(tipIndvalor);
						}
						tipIndvalor = "";

					}}
				}
			}
				col = 0;
			}
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
		celda.setCellValue("PLAN OPERATIVO  ANUAL - POA " + resultado.valor_gestion);
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
		celda.setCellValue("PROGRAMA: "  + resultado.sigla_act_presupuestario + " " +resultado.nombre_act_presupuestario);
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
		
		for(int n=0 ; n<resultado.detalle_formulacion.size();n++){
		if(resultado.detalle_formulacion.get(n).operaciones != null) {
		for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {
			if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades !=null) {
			for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.size(); j++) {

				XSSFRow row1 = (XSSFRow) hoja3.createRow(filaNum);
				celda = row1.createCell(0);
				celda.setCellStyle(estiloSubTituloTabla);
				celda.setCellValue(
						"ACTIVIDAD: " + resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).descripcion);
				hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, h2d));
				filaNum++;
				if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica!=null) {
				for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
						.size(); k++) {

					XSSFRow row = (XSSFRow) hoja3.createRow(filaNum++);
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
							.get(k).descripcion);
					if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
							.get(k).programacion_indicador_meta != null) {
					for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
							.get(k).programacion_indicador_meta.size(); l++) {

						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).tipo_indicador);
						if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).id_tipo_identificador==6) {
							tipInd = "%";
						} else {
							tipInd = "";
						}
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).denominacion);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).medio_verificacion);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).linea_base+tipInd);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).meta+tipInd);
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(esEntero(
								 resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
										.get(k).programacion_indicador_meta.get(l).ponderacion)
								+ "%");

						if(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta!=null) {
						for (int m = 0; m < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
								.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta
										.size(); m++) {
							celda = row.createCell(col++);

							if (tipInd == "") {
								celda.setCellStyle(estiloNumero);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).programacion_indicador_meta.get(m).valor_programado_num != 0
														? (int) resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
																		.get(k).programacion_indicador_meta
																				.get(l).programacion_indicador_meta
																						.get(m).valor_programado_num
														: 0);
							} else {
								celda.setCellStyle(estiloDatos);
								tipIndvalor = esEntero(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).programacion_indicador_meta.get(m).valor_programado_num != 0
														?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica
																		.get(k).programacion_indicador_meta
																				.get(l).programacion_indicador_meta
																						.get(m).valor_programado_num
														: 0)
										+ tipInd;
								celda.setCellValue(tipIndvalor);
							}

							tipIndvalor = "";
						}
						}else {
							
							for (int m = 0; m < 12; m++) {
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue("NH");
								
							}
						}
						col = 0;
					}}
					col = 0;
				}}

				col = 0;
			}
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
	    headers.setContentDispositionFormData("attachment", "[POA]_"+ StringUtils.abbreviate(resultado.org_unidad_funcional.replace(".", "").replace(" ", "_"), 20) + ".xlsx");
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
	    return response;
		
	}
	
	
	//////////----////////////
	
	
	
}
