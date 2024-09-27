package mteps.dtickets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import mteps.config.ConfigCORE;
import mteps.dtickets.entity.FObtenerDatosTicket;
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
@RequestMapping("/dticket/reporte")
public class STicketREPORT {
		
	@Autowired
	DTicketCORE dticketCore;
	
	@Autowired
	ConfigCORE configCore;
	
	@PersistenceContext
	private EntityManager entityManager;	
	
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

	
	@RequestMapping(path = "/comprobanteDTicket", method = RequestMethod.GET)
	public @ResponseBody void reporteListaTramites(HttpServletResponse response,
			@RequestParam(name = "id", required = true, defaultValue = "null") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		StoredProcedureQuery ejecutarFuncion = entityManager
				.createNamedStoredProcedureQuery("mteps_d_tickets.f_obtener_datos_ticket");
		ejecutarFuncion.setParameter("v_id_ticket",  pVariable1);
		FObtenerDatosTicket resultado = (FObtenerDatosTicket) ejecutarFuncion.getSingleResult();

		String archivo = "/WEB-INF/tramites/Dtickets/ReporteComprobanteDTicket.jrxml";
///////////////////////////////////////////////////////////////////////////

		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(resultado));

		parameterMap.put("tipoTramite", resultado.tipoTramite);
		parameterMap.put("codigo", resultado.codigo);
		parameterMap.put("fecha", configCore.generarFecha(resultado.fechaAtencion,8));
		parameterMap.put("hora", configCore.generarFecha(resultado.horaInicio,11));
		parameterMap.put("departamento", resultado.departamento);
		parameterMap.put("nroTramites", resultado.nroTramites.toString());
		parameterMap.put("nombre", resultado.nombre);
		

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=comprobante.pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);

	}

}
