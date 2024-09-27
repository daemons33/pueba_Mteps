package mteps.poa;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

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

import mteps.poa.entity.F_reporte_formulacion;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("sispoa/reporte")
public class PoaReportes {
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;


	@PersistenceContext
	private EntityManager entityManager;
	
	Map modelo = new HashMap();
	
	@Autowired
	ConfigReport repo;
	
	public Object esEntero(double numero){
	    if (numero % 1 == 0) {
	        return (int) numero ;
	    } else {
	    	 return numero;
	    }
	}
	
	public double[][] calculosPE(double[] prog,double[] ejec ,double totalP, double[] dNTC, double[] dNCF ){
			double [][] res = new double[14] [12];
			
			for(int k =0; k<res.length;k++) {
				 for(int l =0; l<res[k].length;l++) {
					 
					switch (k) {
						case 0: res[k][l]=	prog[l];						
							break;
						
						case 1: res[k][l]=	ejec[l];
							break;
					
						case 2: res[k][l]= (ejec[l]!=0 && prog[l]!=0)? ejec[l]/prog[l] *100:0; 
								res[k][l]= res[k][l]>100?100:res[k][l];
							break;
							
						case 3: res[k][l]= l==0?prog[l]:res[k][l-1]+prog[l];
							break;
					
					    case 4: res[k][l]= l==0?ejec[l]:res[k][l-1]+ejec[l];
							break;
						
						case 5: res[k][l]= dNTC[l];
							break;
						
						case 6: res[k][l]= dNCF[l];
							break;
						
						case 7: res[k][l]= dNTC[l] - dNCF[l];
							break;
						
						case 8: res[k][l] = l==0? res[5][l]: res[k][l] + res[5][l];
							break;
						
						case 9: res[k][l] = l==0? res[6][l]: res[k][l] + res[6][l];
							break;
							
						case 10: res[k][l] = l==0? res[7][l]: res[k][l] + res[7][l];
							break;
						
						case 11: res[k][l] = l==0?  prog[l]/totalP *100 : res[k][l-1]+(prog[l]/totalP *100);
							break;
						
						case 12: res[k][l] = l==0?ejec[l]/totalP *100:res[k][l-1]+(ejec[l]/totalP *100);
								 res[k][l] = res[k][l]>100?100:res[k][l];
							break;
						
						case 13: 
								if(ejec[l]!=0 && prog[l]!=0) {
							res[k][l]= ejec[l]/prog[l]*100 ;
								}else {
									res[k][l]=0;
								}
								 
							break;
					
				}
					
			 }

			
		}
		return res;
	}
	
	@GetMapping("/ejecucionFisica")
	public ResponseEntity<byte[]> ejecucionFisica(HttpServletRequest datosEntrada) throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {

		
		
			XSSFWorkbook hssfw = new XSSFWorkbook();

			Sheet sheet = hssfw.createSheet();
			hssfw.setSheetName(0, "ACCIONES DE CORTO PLAZO");
			Sheet hoja1 = hssfw.createSheet();
			hssfw.setSheetName(1, "OPERACIONES");
			Sheet hoja2 = hssfw.createSheet();
			hssfw.setSheetName(2, "ACTIVIDADES");
			Sheet hoja3 = hssfw.createSheet();
			hssfw.setSheetName(3, "TAREAS ESPECIFICAS");
			
			String[] estados = {"Programación","Ejecución","Eficacia","Programación Acumulada", "Ejecución Acumulada","Número Total de Casos","Número casos Favorables","Número Casos Desfavorables","Número Total de Casos Acumulado","Número casos Favorables Acumulado","Número Casos Desfavorables Acumulado","Programación Acumulada en Porcentaje", "Ejecución Acumulada en Porcentaje", "Eficacia de la Ejecución Acumulada"};
			String[] estadosAbrev = {"P","E","EF","PA", "EA","NTC","NCF","NCD","NTCA","NCFA","NCDA","%PA", "%EA", "EEA"};
			
			double[] dProg = new double[12], dEjec = new double [12], dNTC = new double [12], dNCF = new double [12];
			double [][] dResultados = new double[estados.length] [12];
			double totalProg=0;
			Cell celda;

			Integer filaNum = 0;

			Integer[] arrayABSOLUTO = {2,11,12,13};							
			Integer[] arrayRELATIVO = {0,1,2,3,4,11,12,13};
////// Tamaño de columnas
			Integer tamanoColumna[] = { 2000,2300,10000, 3200, 9000, 3500, 2500, 1500, 2500 };

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

			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize();

///////////////////////
			Integer idPlan =  Integer.parseInt(datosEntrada.getParameter("idPlan"));
			
					
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_plan.f_reporte_formulacion_poa");

			procedure.setParameter("v_id_plan", idPlan);
			procedure.setParameter("v_id_estado", 2);
			F_reporte_formulacion resultado = (F_reporte_formulacion) procedure.getSingleResult();
			
///////////////////			
			XSSFRow headerLeteralSup = (XSSFRow) sheet.createRow(filaNum);
			celda = headerLeteralSup.createCell(12);
			celda.setCellValue("");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
			filaNum++;
			filaNum++;
			filaNum++;
///////////////////////PLAN OPERATIVO ANUAL - POA - OBJETIVOS DE GESTION
			XSSFRow header = (XSSFRow) sheet.createRow(filaNum++);
			
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL (POA) - OBJETIVOS DE GESTION");
			celda.setCellStyle(estiloTitulo);
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));
			filaNum++;
			header = (XSSFRow) sheet.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ENTIDAD: 0070 - MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL ");
			celda.setCellStyle(estiloTextoNormalNegrita);
			
			header = (XSSFRow) sheet.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL: " + resultado.valor_gestion);
			celda.setCellStyle(estiloTextoNormal);
			
			header = (XSSFRow) sheet.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ACTIVIDAD PRESUPUESTARIA: " +resultado.sigla_act_presupuestario+ " " +resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
/////////////////////////////TABLA
			int col = 0;
			XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);
			
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Nº");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Código");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Acción de Corto Plazo");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea Base");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta");
			col++;
			celda = rowsub.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Temporización de la Meta");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, col, col + 4));

			

///////////////////////////// DATOS
			col = 0;
			int inicio = filaNum;
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {
				
				
				XSSFRow row = (XSSFRow) sheet.createRow(filaNum++);
				totalProg= 0;
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(n+1);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatos);
				celda.setCellValue(resultado.detalle_formulacion.get(0).sigla);
				
				celda = row.createCell(col++);
				celda.setCellStyle(estiloDatosJustificado);
				celda.setCellValue(resultado.detalle_formulacion.get(0).descripcion);
				
				for (int i = 0; i < resultado.detalle_formulacion.get(n).programacion_indicador_meta.size(); i++) {
					
					int inicio2 = filaNum;
					
					if(i>0) { row = (XSSFRow) sheet.createRow(filaNum++);}
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).tipo_indicador);
					if (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).id_tipo_identificador == 6) {
						tipInd = "%";
					} else {
						tipInd = "";
					}
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatosJustificado);
					celda.setCellValue(
							resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).denominacion);

										
					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).linea_base == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).linea_base
											+ tipInd));
					celda.setCellStyle(estiloNumero);
														
					celda = row.createCell(col++);
					celda.setCellValue((String) (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).meta == null ? ""
									: resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).meta
											+ tipInd));
					celda.setCellStyle(estiloNumero);
					
					celda = row.createCell(col++);
					celda.setCellValue("Edo.");
					celda.setCellStyle(estiloTituloTabla);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloTituloTabla);
					celda.setCellValue("1er Trimestre");
					celda = row.createCell(col++);
					celda.setCellStyle(estiloTituloTabla);
					celda.setCellValue("2do Trimestre");
					celda = row.createCell(col++);
					celda.setCellStyle(estiloTituloTabla);
					celda.setCellValue("3er Trimestre");
					celda = row.createCell(col++);
					celda.setCellStyle(estiloTituloTabla);
					celda.setCellValue("4to Trimestre");
					
					
					/// CALCULOS
					
					if (resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta != null) {
						
						for (int k = 0; k < resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.size(); k++) {
							
							dProg[k] = resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num != 0
												? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_programado_num
												: 0;
							totalProg = totalProg +dProg[k];
							
							dEjec[k] = resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_ejecutado_num != 0
									? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).valor_ejecutado_num
									: 0;
							
							dNTC[k] = resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).ejecutado_a != null
									? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).ejecutado_a
									: 0;
							
							dNCF[k] = resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).ejecutado_b != null
									? resultado.detalle_formulacion.get(n).programacion_indicador_meta.get(i).programacion_indicador_meta.get(k).ejecutado_b
									: 0;
							
						}
					} 
					
					dResultados = calculosPE(dProg, dEjec,totalProg,dNTC,dNCF);
													
						for (int k = 0; k < dResultados.length; k++) {
							if(tipInd == "" && k>=5 &&k<=10) {}else {
							col = col -5;
							row = (XSSFRow) sheet.createRow(filaNum++);
							celda = row.createCell(col++);
							celda.setCellValue(estadosAbrev[k]);
							celda.setCellStyle(estiloDatos);
							
							for (int l=0; l < 4; l++ ) {	
							
							celda = row.createCell(col++);
							celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
							if(tipInd == "") {
								celda.setCellValue((Math.round(dResultados[k][l]* 100d) / 100d) + (Arrays.asList(arrayABSOLUTO).contains(k)?"%":""));
							}else {
								celda.setCellValue((Math.round(dResultados[k][l]* 100d) / 100d) + (Arrays.asList(arrayRELATIVO).contains(k)?"%":""));
							}			
							}
							}
						}
					
					col = col - 9;
					
					for (int contador = 3; contador < 7; contador++) {

						CellRangeAddress region = new CellRangeAddress(i>0?inicio2:inicio2-1,	filaNum-1,
								contador, contador);
						sheet.addMergedRegion(region);
						RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
					}
				}

// BORDES DE TABLA
				if (resultado.detalle_formulacion.get(n).programacion_indicador_meta != null ) {
					for (int contador = 0; contador < 3; contador++) {

						CellRangeAddress region = new CellRangeAddress(inicio,	filaNum-1,
								contador, contador);
						sheet.addMergedRegion(region);
						RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
						RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
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
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 2, 5));

			celda = rowFirmas.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum, 6, 10));

			filaNum++;
			XSSFRow rowFirmas1 = (XSSFRow) sheet.createRow(filaNum++);

			celda = rowFirmas1.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 1));

			celda = rowFirmas1.createCell(2);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO POR");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 2, 5));

			celda = rowFirmas1.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("APROBADO POR");
			sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 6, 10));

//////////////////////// HOJA 1
			
			
////// Tamaño de columnas
			Integer tamanoColumna2[] = { 1500, 2300, 10000, 2500, 3500, 6500, 2500, 2500, 2500, 2500, 2500, 2500 };
			
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
			celda.setCellValue("");
			celda.setCellStyle(estiloSuperiorLateral);
			filaNum++;
						filaNum++;
			
			
///////////////////////
			header = (XSSFRow) hoja1.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL (POA) - OBJETIVOS DE GESTION");
			celda.setCellStyle(estiloTitulo);
			hoja1.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 10));
			filaNum++;
			
			header = (XSSFRow) hoja1.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ENTIDAD: 0070 - MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL ");
			celda.setCellStyle(estiloTextoNormalNegrita);
			XSSFRow header1h1 = (XSSFRow) hoja1.createRow(filaNum++);
			celda = header1h1.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL: " + resultado.valor_gestion);
			celda.setCellStyle(estiloTextoNormal);
			XSSFRow header2h1 = (XSSFRow) hoja1.createRow(filaNum++);
			celda = header2h1.createCell(0);
			celda.setCellValue("ACTIVIDAD PRESUPUESTARIA: " +resultado.sigla_act_presupuestario+ " " +resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;
/////////////////////////////TABLA

			
				col = 0;
				XSSFRow rowsub1h1 = (XSSFRow) hoja1.createRow(filaNum);

				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Nº");
				
				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Código");
				
				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Operación");
				
				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Pond.");		
				

				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("TI");

				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Indicador");

				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Linea Base");

				celda = rowsub1h1.createCell(col++);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Meta");

				celda = rowsub1h1.createCell(col);
				celda.setCellStyle(estiloTituloTabla);
				celda.setCellValue("Temporalización de las Metas");
				hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col+4));

				
				filaNum++;
///////////////////////////////// DATOS
				filaNum++;
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {
				
				col = 0;
				XSSFRow row = (XSSFRow) hoja1.createRow(filaNum++);
				
				celda = row.createCell(col);
				celda.setCellStyle(estiloSubTituloTabla);
				celda.setCellValue("ACP: "+resultado.detalle_formulacion.get(n).descripcion);
				hoja1.addMergedRegion(new CellRangeAddress(filaNum-1, filaNum-1, col, col+12));
				row.setHeight((short) 500);
				filaNum++;

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {
					inicio =filaNum;
					totalProg= 0;
					row = (XSSFRow) hoja1.createRow(filaNum++);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(i+1);
					
					celda = row.createCell(col++);
					celda.setCellStyle(estiloDatos);
					celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).sigla);
					
					if (resultado.detalle_formulacion != null) {
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
					} else {
						col++;
					}
					
					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.size(); j++) {
												
						
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
								.get(j).id_tipo_identificador == 6) {
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
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("Edo.");
						celda = row.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("1er Trimestre");
						celda = row.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("2do Trimestre");
						celda = row.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("3er Trimestre");
						celda = row.createCell(col++);
						celda.setCellStyle(estiloTituloTabla);
						celda.setCellValue("4to Trimestre");
						
						
						/// CALCULOS
						
						if (resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta != null) {
							
							for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.size(); k++) {
								
								dProg[k] = resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).valor_programado_num != 0 
										? resultado.detalle_formulacion.get(n).operaciones.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).valor_programado_num
												: 0;
								totalProg = totalProg +dProg[k];
								
								dEjec[k] = resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).valor_ejecutado_num != 0
										?  resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).valor_ejecutado_num
										: 0;
								
								dNTC[k] =  resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).ejecutado_a != null
										?  resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).ejecutado_a
										: 0;
								
								dNCF[k] =  resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).ejecutado_b != null
										?  resultado.detalle_formulacion.get(n).operaciones	.get(i).programacion_indicador_meta.get(j).programacion_indicador_meta.get(k).ejecutado_b
										: 0;
								
							}
						} 
						
						dResultados = calculosPE( dProg, dEjec,totalProg,dNTC,dNCF);
						
						for (int k = 0; k < dResultados.length; k++) {
							if(tipInd == "" && k>=5 &&k<=10) {}else {
							col = col -5;
							row = (XSSFRow) hoja1.createRow(filaNum++);
							celda = row.createCell(col++);
							celda.setCellValue(estadosAbrev[k]);
							celda.setCellStyle(estiloDatos);
							
							for (int l=0; l < 4; l++ ) {	
							
							celda = row.createCell(col++);
							celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
							if(tipInd == "") {
								celda.setCellValue((Math.round(dResultados[k][l]* 100d) / 100d) + (Arrays.asList(arrayABSOLUTO).contains(k)?"%":""));
							}else {
								celda.setCellValue((Math.round(dResultados[k][l]* 100d) / 100d) + (Arrays.asList(arrayRELATIVO).contains(k)?"%":""));
							}				
							}
							}
						}
						
						if (resultado.detalle_formulacion.get(n).programacion_indicador_meta != null ) {
							for (int contador = 0; contador < 8; contador++) {

								CellRangeAddress region = new CellRangeAddress(inicio,	inicio + (tipInd == ""? estados.length-6:estados.length),
										contador, contador);
								hoja1.addMergedRegion(region);
								RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja1);
								RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja1);
								RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja1);
								RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja1);
							}
						}
						
						

					}
					col = 0;
					filaNum++;

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
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 3, 6));

			celda = rowFirmash1.createCell(7);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("Firma y Sello");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 7, 11));

			filaNum++;
			filaNum++;
			XSSFRow rowFirmas1h1 = (XSSFRow) hoja1.createRow(filaNum);

			celda = rowFirmas1h1.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 2));

			celda = rowFirmas1h1.createCell(3);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO POR");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 3, 6));

			celda = rowFirmas1h1.createCell(7);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			hoja1.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 7, 11));

/////////////////////////////// HOJA 2			
////// Tamaño de columnas
			Integer tamanoColumna3[] = { 1500, 2300,11000, 3000, 6000, 6000, 4000, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500 };

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
			
			filaNum++;
			filaNum++;
			filaNum++;
			header = (XSSFRow) hoja2.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL (POA) - OBJETIVOS DE GESTION");
			celda.setCellStyle(estiloTitulo);
			hoja2.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 21));
			filaNum++;
			
			header = (XSSFRow) hoja2.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ENTIDAD: 0070 - MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL ");
			celda.setCellStyle(estiloTextoNormalNegrita);
			
			header = (XSSFRow) hoja2.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL: " + resultado.valor_gestion);
			celda.setCellStyle(estiloTextoNormal);
			
			header = (XSSFRow) hoja2.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ACTIVIDAD PRESUPUESTARIA: " +resultado.sigla_act_presupuestario+ " " +resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;

//////////////////TABLA
			col = 0;
			XSSFRow rowsub1h2 = (XSSFRow) hoja2.createRow(filaNum);
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Nº");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Código");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Actividad");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Pond.");		
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Medio de Verificación");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea Base");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta");

			celda = rowsub1h2.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Temporalización de las Metas");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col+12));



/////////////////////////////////DATOS
			String[] mesesh2 = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
			col = 0;
			filaNum++;
			int secuecial = 1;
			filaNum++;
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {
					
					col = 0;
					XSSFRow row = (XSSFRow) hoja2.createRow(filaNum++);
					
					celda = row.createCell(col);
					celda.setCellStyle(estiloSubTituloTabla);
					celda.setCellValue("OP: "+resultado.detalle_formulacion.get(n).operaciones.get(i).descripcion);
					hoja2.addMergedRegion(new CellRangeAddress(filaNum-1, filaNum-1, col, col+21));
					row.setHeight((short) 500);
					filaNum++;

					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.size(); j++) {
						totalProg= 0;
						inicio =filaNum;
						row = (XSSFRow) hoja2.createRow(filaNum++);
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(secuecial++);
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatos);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).sigla);
						
						celda = row.createCell(col++);
						celda.setCellStyle(estiloDatosJustificado);
						celda.setCellValue(
								resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).descripcion);

						for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
								.get(j).programacion_indicador_meta.size(); k++) {
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(Integer.toString((int) resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).ponderacion) + "%");
							
							celda = row.createCell(col++);
							celda.setCellStyle(estiloDatos);
							celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).tipo_indicador);
							if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades	.get(j).programacion_indicador_meta.get(k).id_tipo_identificador == 6) {
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
							celda.setCellStyle(estiloTituloTabla);
							celda.setCellValue("Edo.");
														
							for (int s = 0; s < mesesh2.length; s++) {
								celda = row.createCell(col++);
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue(mesesh2[s]);
							}

							/// CALCULOS
							
							if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta != null) {
								
								for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.size(); l++) {
									
									dProg[l] = resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).valor_programado_num != 0 
											? resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).valor_programado_num
													: 0;
									totalProg = totalProg +dProg[l];
									
									dEjec[l] = resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).valor_ejecutado_num != 0
											?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).valor_ejecutado_num
											: 0;
									
									dNTC[l] =  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).ejecutado_a != null
											?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).ejecutado_a
											: 0;
									
									dNCF[l] =  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).ejecutado_b != null
											?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta.get(l).ejecutado_b
											: 0;
									
								}
							} 
							
							dResultados = calculosPE( dProg, dEjec,totalProg,dNTC,dNCF);
							
							
							
							for (int l = 0; l < dResultados.length; l++) {
																
								
															
								
								//
								if(tipInd == "" && l>=5 &&l<=10) {}else {
								col = col -13;
								row = (XSSFRow) hoja2.createRow(filaNum++);
								celda = row.createCell(col++);
								celda.setCellValue(estadosAbrev[l]);
								celda.setCellStyle(estiloDatos);
								
									for (int s=0; s < 12; s++ ) {	
									
										celda = row.createCell(col++);
										celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
										if(tipInd == "") {
												celda.setCellValue((Math.round(dResultados[l][s]* 100d) / 100d) + (Arrays.asList(arrayABSOLUTO).contains(l)?"%":""));
											}else {
												celda.setCellValue((Math.round(dResultados[l][s]* 100d) / 100d) + (Arrays.asList(arrayRELATIVO).contains(l)?"%":""));
											}			
									}
								}
								//
								
							}
							
							
							if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).programacion_indicador_meta.get(k).programacion_indicador_meta != null ) {
								for (int contador = 0; contador < 9; contador++) {

									CellRangeAddress region = new CellRangeAddress(inicio,	inicio + (tipInd == ""? estados.length-6:estados.length),
											contador, contador);
									hoja2.addMergedRegion(region);
									RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja2);
									RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja2);
									RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja2);
									RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja2);
								}
							}
							filaNum++;
							
							
						}
						col = 0;
					}
					col = 0;

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
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum + 1, 12, 19));

			filaNum++;
			filaNum++;
			XSSFRow rowFirmas1h2 = (XSSFRow) hoja2.createRow(filaNum);

			celda = rowFirmas1h2.createCell(0);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("ELABORADO POR");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 0, 5));

			celda = rowFirmas1h2.createCell(6);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO POR");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 6, 11));

			celda = rowFirmas1h2.createCell(12);
			celda.setCellStyle(estiloFirma);
			celda.setCellValue("REVISADO DGP");
			hoja2.addMergedRegion(new CellRangeAddress(filaNum, filaNum, 12, 19));

/////////////////////////////// HOJA 3	
////// Tamaño de columnas
			Integer tamanoColumna4[] = {1500,2300, 11000, 3000, 6000, 6000, 4000, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500,
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
			
			filaNum++;
			filaNum++;
			filaNum++;
			
			header = (XSSFRow) hoja3.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL (POA) - OBJETIVOS DE GESTION");
			celda.setCellStyle(estiloTitulo);
			hoja3.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 18));
			filaNum++;
			
			header = (XSSFRow) hoja3.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ENTIDAD: 0070 - MINISTERIO DE TRABAJO, EMPLEO Y PREVISIÓN SOCIAL ");
			celda.setCellStyle(estiloTextoNormalNegrita);

			header = (XSSFRow) hoja3.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("PLAN OPERATIVO ANUAL: " + resultado.valor_gestion);
			celda.setCellStyle(estiloTextoNormal);

			header = (XSSFRow) hoja3.createRow(filaNum++);
			celda = header.createCell(0);
			celda.setCellValue("ACTIVIDAD PRESUPUESTARIA: " +resultado.sigla_act_presupuestario+ " " +resultado.nombre_act_presupuestario);
			celda.setCellStyle(estiloTextoNormal);
			filaNum++;

//////////////////TABLA
			col = 0;
			rowsub1h2 = (XSSFRow) hoja3.createRow(filaNum);
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Nº");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Código");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Tarea Especifica");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Pond.");		
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("TI");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Indicador");
			
			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Medio de Verificación");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Linea Base");

			celda = rowsub1h2.createCell(col++);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Meta");

			celda = rowsub1h2.createCell(col);
			celda.setCellStyle(estiloTituloTabla);
			celda.setCellValue("Temporalización de las Metas");
			hoja3.addMergedRegion(new CellRangeAddress(filaNum, filaNum, col, col+12));

//////////////////////////////// DATOS TABLA
			filaNum++;
			secuecial = 1;
			filaNum++;
			for (int n = 0; n < resultado.detalle_formulacion.size(); n++) {

				for (int i = 0; i < resultado.detalle_formulacion.get(n).operaciones.size(); i++) {

					for (int j = 0; j < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.size(); j++) {

						col = 0;
						XSSFRow row = (XSSFRow) hoja3.createRow(filaNum++);
						
						celda = row.createCell(col);
						celda.setCellStyle(estiloSubTituloTabla);
						celda.setCellValue("ACT: "+resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).descripcion);
						hoja3.addMergedRegion(new CellRangeAddress(filaNum-1, filaNum-1, col, col+21));
						row.setHeight((short) 500);
						filaNum++;
						
						for (int k = 0; k < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades	.get(j).tarea_especifica.size(); k++) {
							
												
							
							for (int l = 0; l < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.size(); l++) {
								totalProg= 0;
								inicio = filaNum;
								col = 0;
								row = (XSSFRow) hoja3.createRow(filaNum++);
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(secuecial++);
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).sigla);
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).descripcion);
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(
										esEntero(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
												.get(j).tarea_especifica.get(k).programacion_indicador_meta	.get(l).ponderacion) + "%");
								
								celda = row.createCell(col++);
								celda.setCellStyle(estiloDatos);
								celda.setCellValue(resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).tipo_indicador);
								if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades
										.get(j).tarea_especifica.get(k).programacion_indicador_meta
												.get(l).id_tipo_identificador == 6) {
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
								celda.setCellStyle(estiloTituloTabla);
								celda.setCellValue("Edo.");
															
								for (int s = 0; s < mesesh2.length; s++) {
									celda = row.createCell(col++);
									celda.setCellStyle(estiloTituloTabla);
									celda.setCellValue(mesesh2[s]);
								}

								
								if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta != null) {
									
									for (int m = 0; m < resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.size(); m++) {
										
										dProg[m] = resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).valor_programado_num != 0 
												? resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).valor_programado_num
														: 0;
										totalProg = totalProg +dProg[m];
										
										dEjec[m] = resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).valor_ejecutado_num != 0
												?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).valor_ejecutado_num
												: 0;
										
										dNTC[m] =  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).ejecutado_a != null
												?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).ejecutado_a
												: 0;
										
										dNCF[m] =  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).ejecutado_b != null
												?  resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta.get(m).ejecutado_b
												: 0;
										
									}
								} 
								
								dResultados = calculosPE( dProg, dEjec,totalProg,dNTC,dNCF);
								
								
								for (int m = 0; m < dResultados.length; m++) {
									if(tipInd == "" && m>=5 &&m<=10) {}else {
									col = col -13;
									row = (XSSFRow) hoja3.createRow(filaNum++);
									celda = row.createCell(col++);
									celda.setCellValue(estadosAbrev[m]);
									celda.setCellStyle(estiloDatos);
									
										for (int s=0; s < 12; s++ ) {	
										
										celda = row.createCell(col++);
										celda.setCellStyle(tipInd == ""? estiloNumero:estiloDatos);
										if(tipInd == "") {
											celda.setCellValue((Math.round(dResultados[m][s]* 100d) / 100d) + (Arrays.asList(arrayABSOLUTO).contains(m)?"%":""));
										}else {
											celda.setCellValue((Math.round(dResultados[m][s]* 100d) / 100d) + (Arrays.asList(arrayRELATIVO).contains(m)?"%":""));
										}			
										}
									}
								}
								
								
								if (resultado.detalle_formulacion.get(n).operaciones.get(i).actividades.get(j).tarea_especifica.get(k).programacion_indicador_meta.get(l).programacion_indicador_meta != null ) {
									for (int contador = 0; contador < 9; contador++) {

										CellRangeAddress region = new CellRangeAddress(inicio,	inicio + (tipInd == ""? estados.length-6:estados.length),
												contador, contador);
										hoja3.addMergedRegion(region);
										RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja3);
										RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja3);
										RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja3);
										RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja3);
									}
								}
								filaNum++;
								
								
								
								col = 0;
							}
							col = 0;
						}

						col = 0;
					}
					col = 0;

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
		    headers.setContentDispositionFormData("attachment", "[POA_OBJ_GESTION]_"+ StringUtils.abbreviate(resultado.org_unidad_funcional.replace(".", "").replace(" ", "_"), 20) + ".xlsx");
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
		    return response;
			
					

	}

}
