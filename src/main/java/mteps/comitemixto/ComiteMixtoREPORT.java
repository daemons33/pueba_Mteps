package mteps.comitemixto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.comitemixto.entity.DatosLista;
import mteps.comitemixto.entity.ListaComiteMixtos;
import mteps.config.ConfigCORE;
import mteps.sistpoa.Servicios.impl.ConfigReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/comiteMixto/reporte")
public class ComiteMixtoREPORT {
		
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ConfigCORE configCore;
	
	@Autowired
	ConfigReport repo;
	
	@Autowired
	ComiteMixtoCORE comiteMixtoCORE;
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptionGeneric(Exception exc) throws IOException {
        String errorMessage = "Detalle: " + exc.getMessage();

        // Lógica para mostrar la página de error personalizada
        ClassPathResource resource = new ClassPathResource("static/errorPage.html");
        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        htmlContent = htmlContent.replace("{{errorMessage}}", errorMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
	
	@RequestMapping(path = "/certificado", method = RequestMethod.GET)
	public @ResponseBody void reporteComprobantePDF(HttpServletResponse response,@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {		

		JasperPrint jasperPrints = comiteMixtoCORE.generaReporte(pVariable1);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=[Certificado].pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}
	
	
	//LISTA EXCEL
	
	@RequestMapping(path = "/comiteMixto", method = RequestMethod.GET)
	public @ResponseBody void reportePrimeraCitacion(HttpServletResponse response,HttpServletRequest datosEntrada)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException, DecoderException {
		
		//String archivo = "/WEB-INF/sscdyd/Reporte_comites_mixtos.xlsx";
		//datosEntrada.getRealPath("") + 
				//"C:/Users/MTEPS/Documents/workspace-spring-tool-suite-4-4.9.0.RELEASE/backend-mteps/src/main/resources/WEB-INF/pruebaSE.xlsx";
			
		ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("WEB-INF/sscdyd/Reporte_comites_mixtos.xlsx");

	        // FileInputStream inputStream = new FileInputStream(new File(archivo));
	         XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);

	         Sheet hoja = workbook.getSheetAt(0);
	         
	     	XSSFCellStyle datoTabla = (XSSFCellStyle) workbook.createCellStyle();
			/////// estilo|hoja| Bool-Negrita| alineado horizontal|tam. letra|tipo de
			/////// letra|colorLetra|Bool-Bordes|color borde|bool numerico|formato numerico
			/////// | Ajustar texto
			datoTabla = (XSSFCellStyle) repo.generaEstilo(datoTabla, workbook, false, "CENTER", (short) 9, "Arial",
					"BLACK", false, "", true, "BLACK", false, "#,##0.00", true);
	         
	         DatosLista vObjEntradaDatos = new DatosLista();
	         vObjEntradaDatos.estado= datosEntrada.getParameter("estado").toString();
	         vObjEntradaDatos.login=datosEntrada.getParameter("login").toString();
	         vObjEntradaDatos.unidad=datosEntrada.getParameter("unidad").toString();
	         
	         StoredProcedureQuery procedure = entityManager
						.createNamedStoredProcedureQuery("mteps_comites.f_obtener_capacitadoscomites");

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(vObjEntradaDatos);
				procedure.setParameter("p_json_pp", json);
				List<ListaComiteMixtos> datos = procedure.getResultList();
				
				
	       
	 		
	 		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 		 		  
	 		Cell celda;
	 		Row row = hoja.getRow(3); 

	 		if (row == null) {
	 		    row = hoja.createRow(3); 
	 		}

	 		celda = row.getCell(11); 

	 		if (celda == null) {
	 		    celda = row.createCell(11); 
	 		}
	         celda.setCellValue(dtf.format(LocalDateTime.now()));
	         
	         row = hoja.getRow(4); 

		 		if (row == null) {
		 		    row = hoja.createRow(4); 
		 		}

		 		celda = row.getCell(11); 

		 		if (celda == null) {
		 		    celda = row.createCell(11); 
		 		}
	         
	         celda.setCellValue(vObjEntradaDatos.login );
	        
	         
	         int col=0;
	         int filaNum=8;
	         for(ListaComiteMixtos datoIterable: datos ) {
	        	 col=0;
	        	 XSSFRow datosExcel = (XSSFRow) hoja.createRow(filaNum++);
	        	 celda = datosExcel.createCell(col++);        	 
		         celda.setCellValue(datoIterable.id);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.estado);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.jefatura);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.usuarioCreacion);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);        	 
		         celda.setCellValue(configCore.formatoFecha(datoIterable.fechaCreacion, false));
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.empresa);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.nombre+" " + datoIterable.apellidos);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);        	 
		         celda.setCellValue(datoIterable.cargo);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);       	 
		         celda.setCellValue(datoIterable.ci);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);        	 
		         celda.setCellValue(datoIterable.celular);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(datoIterable.correo);
		         celda.setCellStyle(datoTabla);
		         
		         celda = datosExcel.createCell(col++);	        	 
		         celda.setCellValue(configCore.formatoFecha(datoIterable.fechaCapacitacion, false));
		         celda.setCellStyle(datoTabla);
		         
		         
	         }
	       
	         
	         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateAll();
	         
	 		////
	        inputStream.close();
	        response.setContentType("application/vnd.ms-excel");
	  		response.setHeader("Content-Disposition", "inLine; filename=[REPORTE COMITE MIXTO].xlsx");
	        OutputStream  outputStream = response.getOutputStream();
	 		workbook.write(outputStream);
	        workbook.close();
	        outputStream.close();         
	}
	
}
