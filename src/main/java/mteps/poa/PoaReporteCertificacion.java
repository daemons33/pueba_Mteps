package mteps.poa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mteps.poa.entity.F_reporte_certificacion_poa;
import mteps.poa.entity.F_reporte_certificacion_poa.Detalle_solicitud;
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
@RequestMapping("sispoa/reporte")
public class PoaReporteCertificacion {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - certificacion POA
	@RequestMapping(path = "/certificacionPOA", method = RequestMethod.GET)
	public @ResponseBody void reportePrimeraCitacion(HttpServletResponse response,
			@RequestParam(name = "idSolicitud", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {
		String nombreArch="", nombreUsrCre="", nombreApr="";
		String archivo = "/WEB-INF/poa/certificacionPOA.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_plan.f_reporte_certificacion_poa");

		procedure.setParameter("v_id_solicitud", pVariable1);

		F_reporte_certificacion_poa result = (F_reporte_certificacion_poa) procedure.getSingleResult();
		
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);
		
		List<Detalle_solicitud> datos = result.detalle_solicitud;
		
		Connection connection = null;
  		connection = DriverManager.getConnection(db_url, db_usuario, db_password);	
  		
  		if(result.solicitud.usr_firma!=null) {
  		CallableStatement  resultado= connection.prepareCall("select pf.imagen_firma from mteps_plan.pln_firma pf where pf.id_usuario = ? and pf.estado =2");
  		resultado.setInt(1, result.solicitud.usr_firma);
        ResultSet resultadoU = resultado.executeQuery();
        if(resultadoU.next()) {
        	nombreArch = resultadoU.getString(1);
        }
        }
  		
  		if(result.solicitud.usr_cre!=null) {
  		CallableStatement  resultado1= connection.prepareCall("select concat_ws(' ',p.primernombre,p.primerapellido,p.segundoapellido) from mteps_plan.pln_usuarios wu \r\n"
  				+ "  				left join seguridad.usuarios u on wu.login = u.usuario\r\n"
  				+ "  				left join nucleo.personas p on p.idpersona = u.idpersona\r\n"
  				+ "  				where wu.id_usuario = ?");
  		resultado1.setInt(1, result.solicitud.usr_cre);
        ResultSet resultadoU1 = resultado1.executeQuery();
        if(resultadoU1.next()) {
        	nombreUsrCre = resultadoU1.getString(1);
        }
  		}
  		
  		if(result.solicitud.id_usuario_aprueba!=null) {
  	  		CallableStatement  resultado2= connection.prepareCall("select concat_ws(' ',p.primernombre,p.primerapellido,p.segundoapellido) from mteps_plan.pln_usuarios wu \r\n"
  	  				+ "  				left join seguridad.usuarios u on wu.login = u.usuario\r\n"
  	  				+ "  				left join nucleo.personas p on p.idpersona = u.idpersona\r\n"
  	  				+ "  				where wu.id_usuario =?");
  	  		resultado2.setInt(1, result.solicitud.id_usuario_aprueba);
  	        ResultSet resultadoU2 = resultado2.executeQuery();
  	        if(resultadoU2.next()) {
  	        	nombreApr = resultadoU2.getString(1);
  	        }
  	  		}
  		
		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("codigo", result.solicitud.codigo);
		parameterMap.put("f_registro", result.solicitud.fecha_cre);
		parameterMap.put("f_solicitud", result.solicitud.fecha_solicitud);
		parameterMap.put("f_aprobacion", result.solicitud.fecha_aprobacion);
		parameterMap.put("actividad_presupuestaria", result.formulacion.actividad_presupuestaria);
		parameterMap.put("programa_presupuestario", result.formulacion.programa_presupuestario);
		parameterMap.put("acp_sigla", result.formulacion.acp_sigla);
		parameterMap.put("acp", result.formulacion.acp);
		parameterMap.put("op_sigla", result.formulacion.op_sigla);
		parameterMap.put("op", result.formulacion.op);
		parameterMap.put("act_sigla", result.formulacion.act_sigla);
		parameterMap.put("act", result.formulacion.act);
		parameterMap.put("te_sigla", result.formulacion.te_sigla);
		parameterMap.put("te", result.formulacion.te);
		parameterMap.put("estado", result.solicitud.estado_solicitud);
		parameterMap.put("firma", "/home/mteps/archivosBackEndMTEPS/firmasPOA/"+nombreArch);
		parameterMap.put("usrCre", nombreUsrCre);
		parameterMap.put("usrRev", nombreApr);
		
		parameterMap.put("detalleFormulacion", new JRBeanCollectionDataSource(Collections.singleton(result.formulacion)));
		parameterMap.put("detalleSolicitud", new JRBeanCollectionDataSource(datos));

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/x-pdf");
		response.setHeader("Content-Disposition", "inLine; filename=[CERT-POA] - " + result.solicitud.codigo + ".pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();

	}

/////////////////////////////// REPORTES - certificacion POA
	 @GetMapping("/pruebaCpo")
	public  F_reporte_certificacion_poa aperturaPorUnidadOrg(@RequestParam(name = "idSolicitud", required = true, defaultValue = "0")Integer pVariable1) {

		 StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("mteps_plan.f_reporte_certificacion_poa");
			procedure.setParameter("v_id_solicitud", pVariable1);

		F_reporte_certificacion_poa result = (F_reporte_certificacion_poa) procedure.getSingleResult();
		return result;

	}

}
