package mteps.tickets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;
import mteps.tickets.entity.FuncionLogTicket;
import mteps.tickets.entity.FuncionObtenerTicket;
import mteps.tickets.entity.PojoLogTicket;
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
@RequestMapping("/tickets")
public class ticketREST {
	@Autowired
	ticketCORE ticketCore;
     
	@PersistenceContext
	private EntityManager entityManager;
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA TICKET

	@RequestMapping(path = "/listaticket", method = RequestMethod.POST)
	public Resultado obtenerListaTicket(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return ticketCore.listaTicket(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION TICKETs
	@RequestMapping(path = "/gestionticket", method = RequestMethod.POST)
	public Resultado gestionTickets(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return ticketCore.gestionTickets(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER AREA - CATEGORIA - SUB CATEGORIA

	@RequestMapping(path = "/clasificador", method = RequestMethod.GET)
	public Resultado ObtenerCategoria(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return ticketCore.obtenerClasificador(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER TICKET

	@RequestMapping(path = "/obtenerticket", method = RequestMethod.GET)
	public Resultado ObtenerTicket(
			@RequestParam(name = "idticket", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return ticketCore.obtenerTicket(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER DEPENDIENTE

	@RequestMapping(path = "/obtenerdependiente", method = RequestMethod.GET)
	public Resultado ObtenerDependiente(
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException {
		return ticketCore.obtenerDependiente(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER HISTORIAL TICKET

	@RequestMapping(path = "/logticket", method = RequestMethod.GET)
	public Resultado ObtenerLogTicket(
			@RequestParam(name = "idticket", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return ticketCore.obtenerLogTicket(pVariable1);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// BUSCAR TICKET

@RequestMapping(path = "/buscarticket", method = RequestMethod.GET)
public Resultado buscarTicket(
@RequestParam(name = "dato", required = true, defaultValue = "") String pVariable1)
throws JsonProcessingException, SQLException {
return ticketCore.buscarTicket(pVariable1);
}
///////////////////////////////////////////////////////////////////
//////////// //////////////////REPORTE

	@RequestMapping(path = "/reporte/pdf", method = RequestMethod.GET)
	public @ResponseBody void reportePDF(HttpServletResponse response,
			@RequestParam(name = "idTicket", required = true, defaultValue = "0") Integer pVariable1,
	        @RequestParam(name = "login", required = true) String pVariable2)
			throws JRException, IOException, ScriptException, SQLException {
		 
		String archivo = "/WEB-INF/reporteTicket.jrxml";

		StoredProcedureQuery procedureTicket = entityManager
				.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_ticket");

		procedureTicket.setParameter("v_id_ticket", pVariable1);	
		
		List<FuncionObtenerTicket> result = procedureTicket.getResultList();
		FuncionObtenerTicket res =result.get(0);
		
		StoredProcedureQuery logTicket = entityManager
				.createNamedStoredProcedureQuery("mteps_tickets.f_obtener_log_ticket");
  
		logTicket.setParameter("v_id_ticket", pVariable1);
		 
		List<FuncionLogTicket> result2 = logTicket.getResultList();
		List<PojoLogTicket> ress2 = new ArrayList<PojoLogTicket>();
		if (!result2.isEmpty()) {
			for (int i = 0; i < result2.size(); i++) {
				FuncionLogTicket ress = result2.get(i);
				ress2.add(ress.jsonData);      
			}  

		}
		 
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
        // FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(res));

		parameterMap.put("detalleTicket", new JRBeanCollectionDataSource(result));
		parameterMap.put("usuario", pVariable2);
		parameterMap.put("logTicket", new JRBeanCollectionDataSource(ress2));
	 
		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/x-pdf");
		response.setHeader("Content-Disposition", "inLine; filename=ReportePDF.pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);  
	      
	}

}
