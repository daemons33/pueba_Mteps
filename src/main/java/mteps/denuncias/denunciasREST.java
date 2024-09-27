package mteps.denuncias;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
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

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;
import mteps.denuncias.entity.FReporteDenuncias;
import mteps.denuncias.entity.FobtenerDenuncia;
import mteps.inspectoria.inspectoriaREST;
import mteps.sigec.sigecCORE;
import mteps.sistpoa.Servicios.impl.ConfigReport;
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
import mteps.rrhh.entity.FuncionDatosUsuariov2;
@RestController
@RequestMapping("/denuncias")
public class denunciasREST {


	@Autowired
	denunciasCORE denunciasCORE;

	@Autowired
	inspectoriaREST inspectoria;

	@Autowired
	sigecCORE sigecCore;

	@Value("${directorio.archivos}")
	private String dir_archivos;

	@Value("${directorio.enlace}")
	private String dir_enlace;

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(path = "/listadenuncias", method = RequestMethod.POST)
	public Resultado listaConsultas(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return denunciasCORE.listaDenuncias(vObject);
	}

	@RequestMapping(path = "/gestion", method = RequestMethod.POST)
	public Resultado gestionDenuncias(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return denunciasCORE.gestionDenuncias(vObject);
	}

	@RequestMapping(path = "/tipodenuncia", method = RequestMethod.GET)
	public Resultado ObtenerTipoDenuncia() throws JsonProcessingException, SQLException {
		return denunciasCORE.obtenerTipoDenuncias();
	}

	@RequestMapping(path = "/tipobeneficio", method = RequestMethod.GET)
	public Resultado ObtenerTipobeneficio() throws JsonProcessingException, SQLException {
		return denunciasCORE.obtenerTipoBeneficios();
	}

	@RequestMapping(path = "/tiporetiro", method = RequestMethod.GET)
	public Resultado ObtenerTipoRetiro() throws JsonProcessingException, SQLException {
		return denunciasCORE.obtenerTipoRetiro();
	}

	@RequestMapping(path = "/tipocitacion", method = RequestMethod.GET)
	public Resultado ObtenerTipoCitacion() throws JsonProcessingException, SQLException {
		return denunciasCORE.obtenerTipoCitacion();
	}
	
	@RequestMapping(path = "/obtenerSalarioMinimo", method = RequestMethod.GET)
	public Resultado ObtenerSalarioMinimo() throws JsonProcessingException, SQLException {
		return denunciasCORE.ObtenerSalarioMinimo();
	}
	
	@RequestMapping(path = "/obtenerPrefiniquito", method = RequestMethod.GET)
	public Resultado obtenerPrefiniquito(@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1,
										 @RequestParam(name = "tipo", required = true, defaultValue = "") String pVariable2) throws JsonProcessingException, SQLException {
		return denunciasCORE.obtenerPrefiniquito(pVariable1,pVariable2);
	}
	
	@RequestMapping(path = "/listaSalarioMinimo", method = RequestMethod.GET)
	public Resultado listaSalarioMinimo() throws JsonProcessingException, SQLException {
		return denunciasCORE.listaSalarioMinimo();
	}

	/** FUNCION COPIAR ARCHIVO */
	private boolean copyFile(String fromFile, String toFile) {
		File origin = new File(fromFile);
		File destination = new File(toFile);
		// if (origin.exists()) {
		try {
			InputStream in = new FileInputStream(origin);
			OutputStream out = new FileOutputStream(destination);
			// We use a buffer for the copy (Usamos un buffer para la copia).
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

	}

	/** SERVICIO GENERAR MEMORANDUM */

	@RequestMapping(path = "/reporte/memorandumverificacion", method = RequestMethod.GET)
	public void generarMemo(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException {

		String vFromFile = dir_archivos + "denuncias"+ dir_enlace +"memorandum-verificacion.docx";
		String vToFile = dir_archivos + "denuncias"+ dir_enlace +"edit-memorandum-verificacion.docx";
		String fileName = "memorandum-verificacion.docx";

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));
			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$cite", "MTEPS/INSP/001/2022");
						text = text.replace("$nombre", "Edgar Martinez");
						text = text.replace("$fecha", "Viernes, 3 de Julio de 2022");
					}
					run.setText(text, 0);
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "denuncias\\output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "denuncias\\output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}

	}
	
	////////// RETORNAR DIRECCIONES DE JEFATURAS
	
	public String direccionDEPREG(String usuario){
		
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("rrhh.f_obtener_usuario");

		procedure.setParameter("v_dato", usuario);
		List<FuncionDatosUsuariov2> datos= procedure.getResultList();
		
		String direccion="";
		String[][] direccionesJEF = {
		 {"LA PAZ","Calle Yanacocha esq. Mercado - Zona Centro"}
		,{"COCHABAMBA","Esquina Av. Aniceto arce y Pasaje R. Mejía, Nro. 828"}
		,{"SANTA CRUZ","Calle Quijarro No. 72, entre calles Bolívar y Sucre al lado de Migración"}
		,{"ORURO","Calle Arce N°152 Entre Velasco Galvarro y 6 de Agosto"}
		,{"POTOSI","Calle Bustillos Esq. 10 de Noviembre. Z. San Roque"}
		,{"BENI","Zona Central, calle Manuel Limpias, esquina Av. Cipriano Barace Nº 10"}
		,{"PANDO","Calle Columna Porvenir S/N frente a Sedcam"}
		,{"TARIJA","Calle Delgadillo s/n, entre 15 de abril y Virgilio Lema"}
		,{"CHUQUISACA","Calle Rene Calvo Arana, Nº 59"}
		,{"EL ALTO","Calle A s/n entre avenidas Alfredo Sanjines y Diego de Ocaña, Zona Ciudad Satélite (ex instalaciones del Colegio \"San Ignacio de Loyola\")"}
		,{"CHAPARE","Municipio de Villa Tunari, Av. Cochabamba, ambientes Exusaid"}
		,{"CAMIRI","Busch entre Calle Capitan Manchego y Calle Capitan Uztarez"}
		,{"MONTERO","Av. Kennedy pasillo Ensueño detrás del Palacio de Justicia"}
		,{"PUERTO SUAREZ","Calle Vanguardia entre Av. Bolivar y 6 de Agosto"}
		,{"WARNES","Calle Jose Molina Campos N° 139, entre Av. 25 de Mayo y Cuarto Centenario"}
		,{"BERMEJO","Avenida Luis de Fuentes entre calles Junín y Virgen de Copacabana Barrio 1ro. de Mayo"}
		,{"YACUIBA","Calle Sucre entre Avenida Libertadores y Calle Beni, Barrio Los Lapachos"}
		,{"VILLAMONTES","Calle Ismanel Montes entre Calle Cochabamba y Avenida Heroes del Chaco"}
		,{"LLALLAGUA","Calle Rafael Bustillos esq. Camacho - Campamento 6B - Siglo XXI"}
		,{"TUPIZA","Calle Avaroa esq. Avenida Chichas"}
		,{"VILLAZON","C/ Oruro N° 222"}
		,{"UYUNI","Av. Ferroviaria S/N, entre Calles Abaroa y Bolivar"}
		,{"MONTEAGUDO","Plaza 20 de Agosto"}
		,{"GUAYARAMERIN","Calle Julio Viera entre Av. Federico Román y Beni"}
		,{"RIBERALTA","Barrio 25 de Marzo, Av Pablo Oyola Frente a la Casa de Acogida a la Mujer"}};
		datos.get(0).cargo=datos.get(0).cargo==null?"":datos.get(0).cargo;
		for(int i = 0;i< direccionesJEF.length;i++) {
			if (datos.get(0).cargo.contains(direccionesJEF[i][0])) {
				direccion=direccionesJEF[i][1];
			} 
		}
		if(usuario.equals("psantos")) {
			direccion="Calle Quijarro No. 72, entre calles Bolívar y Sucre al lado de Migración";
		}
		return direccion;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - primera citacion
	@RequestMapping(path = "/reporte/primeracitacion", method = RequestMethod.GET)
	public @ResponseBody void reportePrimeraCitacion(HttpServletResponse response,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "";

		archivo = "/WEB-INF/inspectoria/citacionuno.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();
		
		String fechaC ="", horaC="";
		if(result.citaciones !=null) {
			for(int i = 0;i< result.citaciones.size();i++) {
				if(result.citaciones.get(i).idTipo == 827 ) {
			        fechaC = inspectoria.generarFecha(result.citaciones.get(i).fecha,2);
			        
			        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
			        horaC = formatter1.format(result.citaciones.get(i).hora);
				}
			}
		}

//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);
		

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("codigo", result.codigoDenuncia);
		parameterMap.put("empresa", result.razonSocial);
		parameterMap.put("nombreEmpleado", result.denunciantes==null? "":result.denunciantes.get(0).nombreCompleto);
		parameterMap.put("direccionMTEPS", direccionDEPREG( result.nombreUsuarioModificacion)!=""? "ubicada en "+ direccionDEPREG( result.nombreUsuarioModificacion)+" ":"");
		parameterMap.put("direccionEmpleado",result.denunciantes==null? "": result.denunciantes.get(0).domicilio);
		parameterMap.put("direccionEmpresa", result.direccion);
		parameterMap.put("tipo", result.nombreTipoDenuncia);
		parameterMap.put("fecha",fechaC);
		parameterMap.put("hora", horaC);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=1raCitacion-" + result.codigoDenuncia + ".pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - primera citacion v2
	@RequestMapping(path = "/reporte/primeracitacionv2", method = RequestMethod.GET)
	public void primeracitacionv2(HttpServletRequest request,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1,
			HttpServletResponse response) throws FileNotFoundException, IOException, InvalidFormatException,
			JRException, ClassNotFoundException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();

		
		String vFromFile = dir_archivos + "denuncias" + dir_enlace + "reporte-1.docx";
		String vToFile = dir_archivos + "denuncias" + dir_enlace + "edit-reporte-1.docx";
		
		String direccionMTEPS = direccionDEPREG( result.nombreUsuarioModificacion)!=""? "ubicada en "+ direccionDEPREG( result.nombreUsuarioModificacion)+", ":",";
		Date date = new Date();
		
		String fechaC ="", horaC="";
		if(result.citaciones !=null) {
			for(int i = 0;i< result.citaciones.size();i++) {
				if(result.citaciones.get(i).idTipo == 829 ) {
					fechaC = inspectoria.generarFecha(result.citaciones.get(i).fecha,2);
			        
			        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
			        horaC = formatter1.format(result.citaciones.get(i).hora);
				}
			}
		}
		
		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));
			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$dato_001", (result.codigoDenuncia == null)?"":result.codigoDenuncia);
						text = text.replace("$dato_002",  (inspectoria.generarFecha(date, 2) == null)?"":inspectoria.generarFecha(date, 2));
						text = text.replace("$representanteLegal", (result.representanteLegal== null)?"":result.representanteLegal);
						text = text.replace("$razonSocial", (result.razonSocial==null)?"":result.razonSocial);
						text = text.replace("$direccionLegal",( result.direccion==null)?"":result.direccion);
						text = text.replace("$cargo", "");
						text = text.replace("$dato_003", (result.citaciones == null) ? "" : fechaC);
						text = text.replace("$dato_004", (result.citaciones == null) ? "" : horaC);
						text = text.replace("$dato_005",direccionMTEPS);
					}
					run.setText(text, 0);
				}
			}

			/// reemp en tabla

			for (XWPFTable tbl : doc.getTables()) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph p : cell.getParagraphs()) {
							for (XWPFRun r : p.getRuns()) {
								String text = r.getText(0);
								if (text != null) {
									if(result.denunciantes!=null) {
									text = text.replace("$denunciante1", result.denunciantes.get(0).nombreCompleto);
									if(result.denunciantes.size()==1) {
									text = text.replace("$denunciante2", "");
									text = text.replace("$denunciante3", "");}
									if(result.denunciantes.size()==2) {
										text = text.replace("$denunciante2", result.denunciantes.get(1).nombreCompleto);
										text = text.replace("$denunciante3", "");
									}
									if(result.denunciantes.size()>=3){
										text = text.replace("$denunciante2", result.denunciantes.get(1).nombreCompleto);
										text = text.replace("$denunciante3", result.denunciantes.get(2).nombreCompleto);
									}}else {
										text = text.replace("$denunciante1", "");
										text = text.replace("$denunciante2", "");
										text = text.replace("$denunciante3", "");
										
									}
									
									text = text.replace("$tipoDenuncia",( result.nombreTipoDerecho== null)? "":result.nombreTipoDerecho);
									text = text.replace("$detalleDenuncia", ( result.nombreSubtipoDerecho== null)? "":result.nombreSubtipoDerecho);

									r.setText(text, 0);
								}
							}
						}
					}
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "denuncias" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "denuncias" + dir_enlace + "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition",
					"attachment; filename=[" + result.codigoDenuncia + "] PRIMERA CITACIÓN.docx");
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - primera citacion v2
	@RequestMapping(path = "/reporte/actaAudiencia", method = RequestMethod.GET)
	public void actaAudiencia(HttpServletRequest request,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1,
			HttpServletResponse response) throws FileNotFoundException, IOException, InvalidFormatException,
			JRException, ClassNotFoundException, SQLException {

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();
		
		String fechaC ="", horaC="";
		if(result.citaciones !=null) {
			for(int i = 0;i< result.citaciones.size();i++) {
				if(result.citaciones.get(i).idTipo == 827 ) {
					fechaC = inspectoria.generarFecha(result.citaciones.get(i).fecha,2);
			        
			        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
			        horaC = formatter1.format(result.citaciones.get(i).hora);
				}
			}
		}
		
		

		String vFromFile = dir_archivos + "denuncias" + dir_enlace + "reporte-2.docx";
		String vToFile = dir_archivos + "denuncias" + dir_enlace + "edit-reporte-2.docx";

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));
			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$dato_001", result.codigoDenuncia);
						text = text.replace("$dato_002", (result.citaciones == null) ? "" : inspectoria.generarFecha(result.citaciones.get(0).fecha, 2));
						text = text.replace("$representanteLegal", result.representanteLegal);
						text = text.replace("$razonSocial", result.razonSocial);
						text = text.replace("$direccionLegal", result.direccion);
						text = text.replace("$cargo", "");
						text = text.replace("$departamento", "La Paz");
						text = text.replace("$hora", horaC);
						text = text.replace("$fecha", fechaC);
						
						text = text.replace("$denunciante1", result.denunciantes.get(0).nombreCompleto);
						text = text.replace("$trabajador1", "");
					}
					run.setText(text, 0);
				}
			}

/// reemp en tabla

			for (XWPFTable tbl : doc.getTables()) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph p : cell.getParagraphs()) {
							for (XWPFRun r : p.getRuns()) {
								String text = r.getText(0);
								if (text != null) {
									
									text = text.replace("$direccion", result.direccion);
									text = text.replace("$correoelectronico", "");
									text = text.replace("$numero", "");

									r.setText(text, 0);
								}
							}
						}
					}
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "denuncias" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "denuncias" + dir_enlace + "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition",
					"attachment; filename=[" + result.codigoDenuncia + "] ACTA DE AUDIENCIA Y PRESENTACIÓN DE PRUEBAS.docx");
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - segunda citacion
	@RequestMapping(path = "/reporte/segundacitacion", method = RequestMethod.GET)
	public @ResponseBody void reporteSegundaCitacion(HttpServletResponse response,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "";

		archivo = "/WEB-INF/inspectoria/citaciondos.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();
		
		String fechaC ="", horaC="";
		if(result.citaciones !=null) {
			for(int i = 0;i< result.citaciones.size();i++) {
				if(result.citaciones.get(i).idTipo == 828 ) {
					fechaC = inspectoria.generarFecha(result.citaciones.get(i).fecha,2);
			        
			        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
			        horaC = formatter1.format(result.citaciones.get(i).hora);
				}
			}
		}
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		
		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("codigo", result.codigoDenuncia);
		parameterMap.put("empresa", result.razonSocial);
		parameterMap.put("nombreEmpleado",result.denunciantes ==null?"": result.denunciantes.get(0).nombreCompleto);
		parameterMap.put("direccionEmpleado",result.denunciantes ==null?"":  result.denunciantes.get(0).domicilio);
		parameterMap.put("direccionMTEPS", direccionDEPREG( result.nombreUsuarioModificacion)!=""? "ubicada en "+ direccionDEPREG( result.nombreUsuarioModificacion)+" ":"");
		parameterMap.put("direccionEmpresa", result.direccion);
		parameterMap.put("tipo", result.nombreTipoDenuncia);
		parameterMap.put("fecha",fechaC);
		parameterMap.put("hora", horaC);
		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=2daCitacion [" + result.codigoDenuncia + "].pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - unica citacion
	@RequestMapping(path = "/reporte/unicacitacion", method = RequestMethod.GET)
	public @ResponseBody void reporteUnicaCitacion(HttpServletResponse response,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "";

		archivo = "/WEB-INF/inspectoria/unicacitacion.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();
		
		String fechaC ="", horaC="";
		if(result.citaciones !=null) {
			for(int i = 0;i< result.citaciones.size();i++) {
				if(result.citaciones.get(i).idTipo == 829 ) {
					fechaC = inspectoria.generarFecha(result.citaciones.get(i).fecha,2);
			        
			        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
			        horaC = formatter1.format(result.citaciones.get(i).hora);
				}
			}
		}
//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		
		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("codigo", result.codigoDenuncia);
		parameterMap.put("empresa", result.razonSocial);
		parameterMap.put("nombreEmpleado", result.denunciantes.get(0).nombreCompleto);
		parameterMap.put("direccionEmpleado", result.denunciantes.get(0).domicilio);
		parameterMap.put("direccionMTEPS", direccionDEPREG( result.nombreUsuarioModificacion)!=""? "ubicada en "+ direccionDEPREG( result.nombreUsuarioModificacion)+" ":"");
		parameterMap.put("direccionEmpresa", result.direccion);
		parameterMap.put("tipo", result.nombreTipoDenuncia);

		parameterMap.put("fecha",fechaC);
		parameterMap.put("hora", horaC);

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition",
				"inline; filename=UNICA CITACION - [" + result.codigoDenuncia + "] .pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - cuarto intermedio
	@RequestMapping(path = "/reporte/cuartointermedio", method = RequestMethod.GET)
	public @ResponseBody void reporteCuartoIntermedio(HttpServletResponse response,
			@RequestParam(name = "idDenuncia", required = true, defaultValue = "0") Integer pVariable1)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		String archivo = "";

		archivo = "/WEB-INF/inspectoria/cuartointermedio.jrxml";

		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure.setParameter("v_id", pVariable1);

		FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();

//////////////////////////////////////////////////////////////////////////////
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		
		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(result));

		parameterMap.put("codigo", result.codigoDenuncia);
		parameterMap.put("empresa", result.razonSocial);
		parameterMap.put("nombreEmpleado", result.denunciantes.get(0).nombreCompleto);
		parameterMap.put("direccionEmpleado", result.denunciantes.get(0).domicilio);
		parameterMap.put("direccionEmpresa", result.direccion);
		parameterMap.put("tipo", result.nombreTipoDenuncia);
		parameterMap.put("fecha", inspectoria.generarFecha(result.fecCuartoIntermedio, 2));
		parameterMap.put("hora", inspectoria.generarFecha(result.fecCuartoIntermedio, 4));

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition",
				"inline; filename=CUARTO INTERMEDIO - [" + result.codigoDenuncia + "].pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();

	}
	
	
//////// REPORTE DENUNCIAS
	
	Map modelo = new HashMap();
	
	@Autowired
	ConfigReport repo;
	
@GetMapping("/reporte/listaDenuncias")
public ModelAndView reportelistaDenuncias(HttpServletRequest datosEntrada) throws ParseException {

	StoredProcedureQuery procedure = entityManager
			.createNamedStoredProcedureQuery("mteps_denuncias.f_reporte_denuncias");
	DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	procedure.setParameter("v_login", datosEntrada.getParameter("login"));
	procedure.setParameter("v_fecha_ini", formatoFecha.parse( datosEntrada.getParameter("fechaInicio")));
	procedure.setParameter("v_fecha_fin",formatoFecha.parse( datosEntrada.getParameter("fechaFin")));
	List<FReporteDenuncias> resultadoDatos =  procedure.getResultList();

	modelo.put("registros", resultadoDatos);
	modelo.put("fechaInicio", datosEntrada.getParameter("fechaInicio"));
	modelo.put("fechaFin", datosEntrada.getParameter("fechaFin"));

	return new ModelAndView(new denunciasREST.reportereportelistaDenunciasExcel(), modelo);

}

public class reportereportelistaDenunciasExcel extends AbstractXlsxView {

	@Override
	public void buildExcelDocument(Map<String, Object> map, Workbook hssfw, HttpServletRequest hsr,
			HttpServletResponse hsr1) throws Exception {

		Sheet sheet = hssfw.createSheet();
		hssfw.setSheetName(0, "DENUNCIAS");

		org.apache.poi.ss.usermodel.Cell celda;

		Integer filaNum = 0;

		////// Tamaño de columnas
		Integer tamanoColumna[] = { 1400, 8000, 8000, 8000, 8000, 8000 };

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
		 DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		List<FReporteDenuncias> resultado = (List<FReporteDenuncias>) map.get("registros");
		
		String fechaIni = (String) map.get("fechaInicio");
		String fechaFin = (String) map.get("fechaFin");

		XSSFRow header = (XSSFRow) sheet.createRow(filaNum++);
		celda = header.createCell(0);
		celda.setCellValue("CUADRO RESUMEN");
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 5));

		XSSFRow header1 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header1.createCell(0);
		celda.setCellValue("REPORTE DE DENUNCIAS");
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 5));

		XSSFRow header3 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header3.createCell(0);
		celda.setCellValue("DE " +fechaIni+" A "+ fechaFin);
		celda.setCellStyle(estiloTitulo);
		sheet.addMergedRegion(new CellRangeAddress(filaNum - 1, filaNum - 1, 0, 5));
		
		filaNum++;
		int cant = resultado.size();
		if(cant==1) {
		XSSFRow header4 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header4.createCell(0);
		celda.setCellValue("Nombre: " + resultado.get(0).detalle.get(0).responsable);
		celda.setCellStyle(estiloTextoNormal);
		
		header4 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header4.createCell(0);
		celda.setCellValue("Cargo: " + resultado.get(0).detalle.get(0).cargo);
		celda.setCellStyle(estiloTextoNormal);
		
		}
		
		for(int i = 0;i<resultado.size();i++) {
		
			filaNum++;
			
		XSSFRow header5 = (XSSFRow) sheet.createRow(filaNum++);
		celda = header5.createCell(0);
		celda.setCellValue("Departamento: " + resultado.get(i).departamento);
		celda.setCellStyle(estiloTextoNormal);
		
		filaNum++;
		int col = 0;
		XSSFRow rowsub = (XSSFRow) sheet.createRow(filaNum++);

		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("Nº");
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("CODIGO");
		if(cant>1) {
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("INSPECTOR");
		}
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("FECHA DE INICIO");
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("TIPO DE DENUNCIA");
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("DESCRIPCIÓN");
		celda = rowsub.createCell(col++);
		celda.setCellStyle(estiloTituloTabla);
		celda.setCellValue("ESTADO");
		
		col = 0;
		for(int j = 0; j<resultado.get(i).detalle.size();j++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(filaNum++);
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(j+1);
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).detalle.get(j).codigo_denuncia);
			
			if(cant>1) {
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).detalle.get(j).responsable + "\n"+resultado.get(i).detalle.get(j).cargo);}
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(formatoFecha.format(resultado.get(i).detalle.get(j).fecha_creacion));
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).detalle.get(j).nombret  + (resultado.get(i).detalle.get(j).nombrest == null? "":"\n"+resultado.get(i).detalle.get(j).nombrest));
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).detalle.get(j).detalle_denuncia);
			
			celda = row1.createCell(col++);
			celda.setCellStyle(estiloDatos);
			celda.setCellValue(resultado.get(i).detalle.get(j).estado);
					
			col = 0;
		}
		
		}
		
		
		hsr1.setContentType("application/vnd.ms-excel"); // Tell the browser to expect an excel file
		hsr1.setHeader("Content-Disposition",
				"attachment; filename=[REPORTE DE DENUNCIAS].xlsx");

	}
}
	

}
