package mteps.inspectoria;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.consultas.entity.FuncionObtenerConsultaId;
import mteps.denuncias.entity.FobtenerDenuncia;
import mteps.inspectoria.entity.EntDocumentoSigec;
import mteps.inspectoria.entity.FObtenerInspeccionId;
import mteps.sigec.sigecCORE;
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
@RequestMapping("/inspectoria")
public class inspectoriaREST {

	@Value("${dbovt.url}")
	private String db_url1;

	@Value("${dbovt.usuario}")
	private String db_usuario1;

	@Value("${dbovt.password}")
	private String db_password1;

	@Value("${directorio.archivos}")
	private String dir_archivos;

	@Value("${directorio.enlace}")
	private String dir_enlace;

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Autowired
	InspectoriaCORE inspectoria;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	sigecCORE sigecCore;
	
	@Autowired
	ConfigCORE config;

	/** SERVICIO OBTENER LISTA INSPECTORIA */

	@RequestMapping(path = "/listainspectoria", method = RequestMethod.POST)
	public Resultado obtenerListaInspectoria(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return inspectoria.listaInspectoria(vObjEntradaDatos);
	}

	/** SERVICIO GESTION INSPECTORIA */
	@RequestMapping(path = "/gestion", method = RequestMethod.POST)
	public Resultado gestionDenuncias(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return inspectoria.gestionInspectoria(vObject);
	}

	@RequestMapping(path = "/obteneracta", method = RequestMethod.GET)
	public Resultado ObtenerTipoDenuncia(
			@RequestParam(name = "id", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return inspectoria.obteneracta(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// REPORTES - PREFINIQUITO
	@RequestMapping(path = "/reporte/prefiniquito", method = RequestMethod.GET)
	public @ResponseBody void reporteComprobantePDF(HttpServletResponse response,
			@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "") String pVariable2,
			@RequestParam(name = "tipo", required = true, defaultValue = "den") String pVariable3)
			throws JRException, IOException, ScriptException, SQLException, ClassNotFoundException {

		FuncionObtenerConsultaId consulta = null;
		FobtenerDenuncia denuncia = null;
		
		String nombreTrabajador="", razonSocial="",concepto1="",concepto2="",concepto3="";
		Integer tiempoTrabajadoAnio=0,tiempoTrabajadoMes=0,tiempoTrabajadoDia=0;
		Date fechaIngreso=null, fechaRetiro=null;
		Double final_total=0.0,multa=0.0,total=0.0,otros1=0.0,otros2=0.0,otros3=0.0,desahucio=0.0,bonoAntiguedad=0.0, sueldoPromedio=0.0 ,aguinaldoDiasMonto=0.0,aguinaldoMesesMonto=0.0,indemnizacionDiasMonto=0.0,indemnizacionAniosMonto=0.0,indemnizacionMesesMonto=0.0,vacacionesDuodecimasMonto=0.0;
		Integer vacacionesDias=0,aguinaldoDias=0, aguinaldoMeses=0,indemnizacionDias=0,tiempoTrabajoDias=0,indemnizacionAnios=0,indemnizacionMeses=0,tiempoTrabajoAnios=0,tiempoTrabajoMeses=0;
		  
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		
		
		if(pVariable3.equals("con")) {
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_consulta_id");

		procedure.setParameter("v_id", pVariable1);
		
		consulta = (FuncionObtenerConsultaId) procedure.getSingleResult();
		
		nombreTrabajador = consulta.nombrePersona;
		razonSocial=consulta.razonSocial; 
		
		
		if(consulta.preFiniquito!=null) {
			if(consulta.preFiniquito.fechaIngreso!=null) {fechaIngreso=consulta.preFiniquito.fechaIngreso;} else {
																fechaIngreso=new Date();} 
			if(consulta.preFiniquito.fechaRetiro!=null) {fechaRetiro=consulta.preFiniquito.fechaRetiro;} else {fechaRetiro=new Date();} 
			
			aguinaldoDias=consulta.preFiniquito.aguinaldoDias;
			aguinaldoMeses=consulta.preFiniquito.aguinaldoMeses;
			indemnizacionDias=consulta.preFiniquito.indemnizacionDias;
			tiempoTrabajoDias=consulta.preFiniquito.tiempoTrabajoDias;
			indemnizacionAnios=consulta.preFiniquito.indemnizacionAnios;
			indemnizacionMeses=consulta.preFiniquito.indemnizacionMeses;
			tiempoTrabajoAnios=consulta.preFiniquito.tiempoTrabajoAnios;
			tiempoTrabajoMeses=consulta.preFiniquito.tiempoTrabajoMeses;
			vacacionesDias=consulta.preFiniquito.vacacionesDias;
			
			
			final_total=consulta.preFiniquito.final_total;
			multa=consulta.preFiniquito.multa;
			total=consulta.preFiniquito.total;
			otros1=consulta.preFiniquito.otros1 ;
			otros2=consulta.preFiniquito.otros2 ;
			otros3=consulta.preFiniquito.otros3 ;
			concepto1=consulta.preFiniquito.concepto1;
			concepto2=consulta.preFiniquito.concepto2;
			concepto3=consulta.preFiniquito.concepto3;
			desahucio=consulta.preFiniquito.desahucio ;
			bonoAntiguedad=consulta.preFiniquito.bonoAntiguedad ;
			sueldoPromedio=consulta.preFiniquito.sueldoPromedio ;
			aguinaldoDiasMonto=consulta.preFiniquito.aguinaldoDiasMonto ;
			aguinaldoMesesMonto=consulta.preFiniquito.aguinaldoMesesMonto ;
			indemnizacionDiasMonto=consulta.preFiniquito.indemnizacionDiasMonto ;
			indemnizacionAniosMonto=consulta.preFiniquito.indemnizacionAniosMonto ;
			indemnizacionMesesMonto=consulta.preFiniquito.indemnizacionMesesMonto ;
			vacacionesDuodecimasMonto=consulta.preFiniquito.vacacionesDuodecimasMonto ;
			
		}
		
		
		
		if (!consulta.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + consulta.idEmpleador);
			ResultSet resultDatos = procedures.executeQuery();
			
			if (resultDatos.next()) {
				razonSocial = resultDatos.getString("razon_social");
			
				}
			}
			
					
		}
		else {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

			procedure.setParameter("v_id", pVariable1);
			
			denuncia = (FobtenerDenuncia) procedure.getSingleResult();
			nombreTrabajador = denuncia.denunciantes.get(0).nombreCompleto;
			razonSocial=denuncia.razonSocial; 
			
			
			if(denuncia.jsonPreFiniquito!=null) {
				
				if(denuncia.jsonPreFiniquito.fechaIngreso!=null) {fechaIngreso=denuncia.jsonPreFiniquito.fechaIngreso;} else {
					fechaIngreso=new Date();} 
				if(denuncia.jsonPreFiniquito.fechaRetiro!=null) {fechaIngreso=denuncia.jsonPreFiniquito.fechaRetiro;} else {
					fechaIngreso=new Date();} 
				
				aguinaldoDias=denuncia.jsonPreFiniquito.aguinaldoDias;
				aguinaldoMeses=denuncia.jsonPreFiniquito.aguinaldoMeses;
				indemnizacionDias=denuncia.jsonPreFiniquito.indemnizacionDias;
				tiempoTrabajoDias=denuncia.jsonPreFiniquito.tiempoTrabajoDias;
				indemnizacionAnios=denuncia.jsonPreFiniquito.indemnizacionAnios;
				indemnizacionMeses=denuncia.jsonPreFiniquito.indemnizacionMeses;
				tiempoTrabajoAnios=denuncia.jsonPreFiniquito.tiempoTrabajoAnios;
				tiempoTrabajoMeses=denuncia.jsonPreFiniquito.tiempoTrabajoMeses;
				
				final_total=denuncia.jsonPreFiniquito.final_total;
				multa=denuncia.jsonPreFiniquito.multa;
				total=denuncia.jsonPreFiniquito.total;
				otros1=denuncia.jsonPreFiniquito.otros1 ;
				otros2=denuncia.jsonPreFiniquito.otros2 ;
				otros3=denuncia.jsonPreFiniquito.otros3 ;
				concepto1=denuncia.jsonPreFiniquito.concepto1 ;
				concepto2=denuncia.jsonPreFiniquito.concepto2 ;
				concepto3=denuncia.jsonPreFiniquito.concepto3 ;
				desahucio=denuncia.jsonPreFiniquito.desahucio ;
				bonoAntiguedad=denuncia.jsonPreFiniquito.bonoAntiguedad ;
				sueldoPromedio=denuncia.jsonPreFiniquito.sueldoPromedio ;
				aguinaldoDiasMonto=denuncia.jsonPreFiniquito.aguinaldoDiasMonto ;
				aguinaldoMesesMonto=denuncia.jsonPreFiniquito.aguinaldoMesesMonto ;
				indemnizacionDiasMonto=denuncia.jsonPreFiniquito.indemnizacionDiasMonto ;
				indemnizacionAniosMonto=denuncia.jsonPreFiniquito.indemnizacionAniosMonto ;
				indemnizacionMesesMonto=denuncia.jsonPreFiniquito.indemnizacionMesesMonto ;
				vacacionesDuodecimasMonto=denuncia.jsonPreFiniquito.vacacionesDuodecimasMonto ;
				vacacionesDias=denuncia.jsonPreFiniquito.vacacionesDias;
				
			}
			
			if (!denuncia.empresaExterna) {
				CallableStatement procedures = connection.prepareCall(
						"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
								+ "from public.empresa e \r\n"
								+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
								+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
								+ "where e.id_empresa =" + denuncia.idEmpresa);
				ResultSet resultDatos = procedures.executeQuery();
				
				if (resultDatos.next()) {
					razonSocial = resultDatos.getString("razon_social");
				
					}
				}
		}
//////////////////////////////////////////////////////////////////////////////
		String archivo = "";
		if (total == null) {total = (double) 3582;}
		archivo = "/WEB-INF/inspectoria/prefiniquito.jrxml";
		if(fechaIngreso==null) {fechaIngreso=new Date();} 
		if(fechaRetiro==null) {fechaRetiro=new Date();} 
		Period fechaDif = Period.between( fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),fechaRetiro.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		tiempoTrabajadoAnio = fechaDif.getYears();
		tiempoTrabajadoMes = fechaDif.getMonths();
		tiempoTrabajadoDia =fechaDif.getDays();
		
		InputStream jasperStream = this.getClass().getResourceAsStream(archivo);
//FileInputStream jasperStream = new FileInputStream(archivo);
		JasperDesign design = JRXmlLoader.load(jasperStream);
		JasperReport report = JasperCompileManager.compileReport(design);

		Map<String, Object> parameterMap = new HashMap<>();
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(Collections.singleton(consulta));
		parameterMap.put("usuario", pVariable2);
		parameterMap.put("nombreTrabajador", nombreTrabajador);
		parameterMap.put("razonSocial", razonSocial);
		parameterMap.put("fechaIngreso",generarFecha(fechaIngreso, 2) );
		parameterMap.put("fechaRetiro", generarFecha(fechaRetiro, 2) );
		parameterMap.put("tiempoTrabajadoAnio", tiempoTrabajadoAnio );
		parameterMap.put("tiempoTrabajadoMes", tiempoTrabajadoMes );
		parameterMap.put("tiempoTrabajadoDia", tiempoTrabajadoDia );
		
		
		parameterMap.put("aguinaldoDias",aguinaldoDias);
		parameterMap.put("aguinaldoMeses",aguinaldoMeses);
		parameterMap.put("indemnizacionDias",indemnizacionDias);
		parameterMap.put("tiempoTrabajoDias",tiempoTrabajoDias);
		parameterMap.put("indemnizacionAnios",indemnizacionAnios);
		parameterMap.put("indemnizacionMeses",indemnizacionMeses);
		parameterMap.put("tiempoTrabajoAnios",tiempoTrabajoAnios);
		parameterMap.put("tiempoTrabajoMeses",tiempoTrabajoMeses);
		parameterMap.put("vacacionesDias",vacacionesDias);
		
		parameterMap.put("final_total",config.numeroConDecimales(final_total,2));
		parameterMap.put("multa",config.numeroConDecimales(multa,2));
		parameterMap.put("total",config.numeroConDecimales(total,2));
		parameterMap.put("otros1",config.numeroConDecimales(otros1,2));
		parameterMap.put("otros2",config.numeroConDecimales(otros2,2));
		parameterMap.put("otros3",config.numeroConDecimales(otros3,2));
		parameterMap.put("concepto1",concepto1);
		parameterMap.put("concepto2",concepto2);
		parameterMap.put("concepto3",concepto3);
		parameterMap.put("desahucio",config.numeroConDecimales(desahucio,2));
		parameterMap.put("bonoAntiguedad",config.numeroConDecimales(bonoAntiguedad,2));
		parameterMap.put("sueldoPromedio",sueldoPromedio);
		parameterMap.put("aguinaldoDiasMonto",config.numeroConDecimales(aguinaldoDiasMonto,2));
		parameterMap.put("aguinaldoMesesMonto",config.numeroConDecimales(aguinaldoMesesMonto,2));
		parameterMap.put("indemnizacionDiasMonto",config.numeroConDecimales(indemnizacionDiasMonto,2));
		parameterMap.put("indemnizacionAniosMonto",config.numeroConDecimales(indemnizacionAniosMonto,2));
		parameterMap.put("indemnizacionMesesMonto",config.numeroConDecimales(indemnizacionMesesMonto,2));
		parameterMap.put("vacacionesDuodecimasMonto",config.numeroConDecimales(vacacionesDuodecimasMonto,2));
				

		JasperPrint jasperPrints = JasperFillManager.fillReport(report, parameterMap, jrDataSource);
		response.setContentType("application/x-pdf");
		response.setHeader("Content-Disposition", "inLine; filename=Prefiniquito.pdf");
		final OutputStream output = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrints, output);
		response.flushBuffer();	

	}

	/** FUNCION GENERAR FECHA */
	public String generarFecha(Date fecha, Integer tipo) {
		String fechafinal = null, dialiteral, dia, mes, anio, hora;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE", new Locale("es", "ES"));
		dialiteral = simpleDateFormat.format(fecha);
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd", new Locale("es", "ES"));
		dia = simpleDateFormat1.format(fecha);
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMMM", new Locale("es", "ES"));
		mes = simpleDateFormat2.format(fecha);
		SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy", new Locale("es", "ES"));
		anio = simpleDateFormat3.format(fecha);
		SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("HH:mm", new Locale("es", "ES"));
		hora = simpleDateFormat4.format(fecha);
		switch (tipo) {
		case 1:
			fechafinal = dialiteral.substring(0, 1).toUpperCase() + dialiteral.substring(1, dialiteral.length()) + ", "
					+ dia + " de " + mes + " de " + anio;
			break;
		case 2:
			fechafinal = dia + " de " + mes + " de " + anio;
			break;
		case 3:
			fechafinal = dia + " días del mes de " + mes + " del año " + anio;
			break;
		case 4:
			fechafinal = hora;
			break;
		default:
			fechafinal = "";
			break;
		}

		return fechafinal;
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

	/**
	 * SERVICIO GENERAR MEMORANDUM
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/memodesignacion", method = RequestMethod.GET)
	public void generarMemo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String vLogin)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {

		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);
		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();
		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social , e.matricula_comercio from public.empresa e where e.id_empresa ="
							+ inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {
				inspeccion.razonSocial = resultDatos.getString("razon_social");
				inspeccion.nit = resultDatos.getString("nit");
				inspeccion.matriculaComercio = resultDatos.getString("matricula_comercio");
			}
		}

		String vtipo = "";
		String vfile = "";
		String vtiempo = "";
		switch (inspeccion.idTipoInspeccion) {
		case 259:
			vtipo = "INSPECCIÓN TÉCNICA";
			vtiempo = "3 días habiles";
			break;
		case 260:
			vtipo = "INSPECCIÓN TÉCNICA";
			vtiempo = "3 días habiles";
			break;
		case 261:
			vtipo = "INSPECCIÓN TÉCNICA DE SEGURIDAD EN LA CONSTRUCCIÓN";
			vtiempo = "3 días habiles";
			break;
		case 262:
			vtipo = "RE-INSPECCIÓN TÉCNICA";
			vtiempo = "3 días habiles";
			break;
		case 263:
			vtipo = "INSPECCIÓN LABORAL";
			vtiempo = "3 días habiles";
			break;
		case 264:
			vtipo = "INVESTIGACIÓN DE ACCIDENTE DE TRABAJO";
			vtiempo = "3 días habiles";
			break;
		case 265:
			vtipo = "VERIFICACIÓN DE IMPLEMENTACACIÓN Y CUMPLIMIENTO DE PROTOCOLOS DE BIOSEGURIDAD";
			vtiempo = "3 días habiles";
			break;
		case 266:
			vtipo = "VERIFICACIÓN DE ACOSO LABORAL/ACOSO SEXUAL A MUJERES EN EL AMBITO LABORAL";
			vtiempo = "24 horas";
			break;

		}
		if (inspeccion.sigecCiteMemo == null) {
			Resultado vDatSigec = sigecCore.generarDocumentoSIGEC(vLogin, 0, inspeccion.inspectores.get(0).idPersona,
					2, "ASIGNACIÓN DE " + vtipo, 1, false, "");
			inspeccion.sigecCiteMemo = (String) vDatSigec.datoAdicional;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure2 = connection.prepareCall(
					"UPDATE mteps_inspecciones.ins_inspecciones SET sigec_cite_memo= ? where id_inspeccion = ?");
			procedure2.setString(1, inspeccion.sigecCiteMemo);
			procedure2.setInt(2, pVariable1);

			procedure2.execute();
			procedure2.close();
		}

		String fechamemo = generarFecha(inspeccion.fechaModificacion, 1);
		String vFromFile = "";
		String vToFile = "";
		String fileName = "MEMORANDUM-DESIGNACION.docx";

		Integer variable = inspeccion.inspectores.size();
		if (variable == 1) {
			vFromFile = dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + fileName;
			vToFile = dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + "edit-" + fileName;

		} else {

			vFromFile = dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + "2" + fileName;
			vToFile = dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + "edit-2" + fileName;
		}

		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.sigecCiteMemo);

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));
			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {

						text = text.replace("$v2", vtipo);
						text = text.replace("$5", inspeccion.unidadOrganizacional);
						text = text.replace("$6", inspeccion.razonSocial);
						text = text.replace("$7", generarFecha(vDSigec.fechaCreacionSigec, 2));
						text = text.replace("$dato009", vtiempo);

					}
					run.setText(text, 0);
				}
			}

			for (XWPFTable tbl : doc.getTables()) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph p : cell.getParagraphs()) {
							for (XWPFRun r : p.getRuns()) {
								String text = r.getText(0);
								if (text != null) {
									text = text.replace("$cite", inspeccion.sigecCiteMemo);
									text = text.replace("$palabra", inspeccion.inspectores.get(0).nombre);
									text = text.replace("$-1", inspeccion.inspectores.get(0).cargo);
									if (inspeccion.inspectores.size() > 1) {
										text = text.replace("$2", inspeccion.inspectores.get(1).nombre);
										text = text.replace("$3", inspeccion.inspectores.get(1).cargo);
									}
									text = text.replace("$0", generarFecha(vDSigec.fechaCreacionSigec, 1));

									r.setText(text, 0);
								}
							}
						}
					}
				}
			}

			doc.write(new FileOutputStream(
					new File(dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "inspectoria" + dir_enlace + "memorandum" + dir_enlace + "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition",
					"attachment; filename= [" + inspeccion.sigecCiteMemo + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * SERVICIO GENERAR ACTA
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/actainspeccion", method = RequestMethod.GET)
	public void generarActa(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {
		String nit = "";
		String razonSocial = "";
		String nombreComercial = "";
		String matriculaComercio = "";
		String direccion = "";
		String descActividadDeclarada = "";
		String nroPatronal = "";
		String representanteLegal = "";
		String nrDoc = "";
		String telefono = "";
		String fechaInicioActividad = "";
		String codigoMteps = "";
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);
		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();

		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {
				nit = resultDatos.getString("nit");
				razonSocial = resultDatos.getString("razon_social");
				nombreComercial = resultDatos.getString("nombre_comercial");
				if (resultDatos.getString("matricula_comercio") != null) {
					matriculaComercio = resultDatos.getString("matricula_comercio");
				}
				direccion = resultDatos.getString("direccion");
				descActividadDeclarada = resultDatos.getString("desc_actividad_declarada");
				nroPatronal = resultDatos.getString("nro_patronal");
				representanteLegal = resultDatos.getString("nombre_completo");
				nrDoc = resultDatos.getString("nro_documento");
				if (resultDatos.getString("telefonos") != null) {
					telefono = resultDatos.getString("telefonos");
				}

				// fechaInicioActividad=resultDatos.getDate("fecha_inicio_actividad").toString();}
				codigoMteps = resultDatos.getString("codigo_mteps");

			} else {
				nit = inspeccion.nit;
				razonSocial = inspeccion.razonSocial;
				nombreComercial = inspeccion.razonSocial;
				matriculaComercio = inspeccion.matriculaComercio;
				direccion = inspeccion.direccionEmpresa;
			}
		}

		String vfile = "";
		switch (inspeccion.idTipoInspeccion) {
		case 259:
			vfile = "acta-it-servicios.docx";
			break;
		case 260:
			vfile = "acta-it-industria.docx";
			break;
		case 261:
			vfile = "acta-it-construccion.docx";
			break;
		case 262:
			vfile = "acta-rit.docx";
			break;
		case 263:
			vfile = "acta-i-laboral.docx";
			break;
		case 264:
			vfile = "acta-iacc-trabajo.docx";
			break;
		case 265:
			vfile = "acta-ver-prot-bioseg.docx";
			break;
		case 266:
			vfile = "acta-ver-acoso-lab.docx";
			break;

		}

		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.sigecCiteMemo);
		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "actas" + dir_enlace + vfile;
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "actas" + dir_enlace + "edit-" + vfile;
		String fileName = vfile;
		String suma = "";
		Integer suma1 = 0;
		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFTable tbl : doc.getTables()) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph p : cell.getParagraphs()) {
							for (XWPFRun r : p.getRuns()) {
								String text = r.getText(0);
								if (text != null) {
									text = text.replace("$dato000", inspeccion.unidadOrganizacional);
									text = text.replace("$dato001", inspeccion.sigecCiteMemo);
									text = text.replace("$dato002", generarFecha(vDSigec.fechaCreacionSigec, 4));
									text = text.replace("$dato003", generarFecha(inspeccion.fechaCreacion, 2));
									text = text.replace("$dato004", razonSocial);
									text = text.replace("$dato005", nit);
									text = text.replace("$dato006", codigoMteps);
									text = text.replace("$dato_006", nombreComercial);
									text = text.replace("$dato007", nroPatronal);
									text = text.replace("$dato008", descActividadDeclarada);
									text = text.replace("$dato009", direccion);
									text = text.replace("$dato010", representanteLegal);
									text = text.replace("$dato011", nrDoc);
									text = text.replace("$dato012", fechaInicioActividad);
									text = text.replace("$dato013", telefono);
									if (inspeccion.detalleActas != null) {
										if (inspeccion.detalleActas.nroAdministrativos == null) {
											inspeccion.detalleActas.nroAdministrativos = 0;
										}
										text = text.replace("$dato014",
												inspeccion.detalleActas.nroAdministrativos.toString());
										if (inspeccion.detalleActas.nroOperativos == null) {
											inspeccion.detalleActas.nroOperativos = 0;
										}
										text = text.replace("$dato015",
												inspeccion.detalleActas.nroOperativos.toString());
										if (inspeccion.detalleActas.nroEventuales == null) {
											inspeccion.detalleActas.nroEventuales = 0;
										}
										text = text.replace("$dato016",
												inspeccion.detalleActas.nroEventuales.toString());
										if (inspeccion.detalleActas.nroMenoresEdad == null) {
											inspeccion.detalleActas.nroMenoresEdad = 0;
										}
										text = text.replace("$dato017",
												inspeccion.detalleActas.nroMenoresEdad.toString());
										suma1 = inspeccion.detalleActas.nroAdministrativos
												+ inspeccion.detalleActas.nroOperativos
												+ inspeccion.detalleActas.nroEventuales
												+ inspeccion.detalleActas.nroMenoresEdad;
										text = text.replace("$dato018", suma1.toString());
										text = text.replace("$dato019",
												generarFecha(inspeccion.detalleActas.fechaVisita, 1));
										text = text.replace("$dato020",
												generarFecha(inspeccion.detalleActas.fechaVisita, 4));
										text = text.replace("$dato021",
												generarFecha(inspeccion.detalleActas.fechaPlazoParalizacion, 1));
									}
									String ds = "tabla";
									for (int i = 1; i <= 4; i++) {
										ds = '$' + i + ds;
										text = text.replace(ds, "1");
										ds = "$dato0";
									}

									r.setText(text, 0);
								}
							}
						}
					}
				}
			}
			if (inspeccion.detalleActas != null) {
				String ds = "$emp";
				String ds2 = "d";
				String ps = "";
				for (int i = 0; i <= 6; i++) {

					for (int j = 1; j <= 3; j++) {

						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.personasEmpleador.size()) {

							if (j == 1) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).cargo;
							}
							if (j == 2) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).nombreCompleto;
							}
							if (j == 3) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).nroDocumento;
							}

						} else {
							ps = "";
						}

						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$emp";
						ds2 = "d";
					}
				}
				//////////////////////////////
				ds = "$empt";
				ds2 = "d";

				for (int i = 0; i <= 7; i++) {

					for (int j = 1; j <= 3; j++) {
						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.personasTrabajadores.size()) {
							if (j == 1) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).cargo;
							}
							if (j == 2) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).nombreCompleto;
							}
							if (j == 3) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).nroDocumento;
							}

						} else {
							ps = "";
						}
						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$empt";
						ds2 = "d";
					}
				}
				////////////////////////////////

				ds = "$area";
				ds2 = "d";
				String coma = "";

				for (int i = 0; i <= 9; i++) {

					for (int j = 1; j <= 3; j++) {
						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.areasEmpresa.size()) {
							if (j == 1) {
								ps = inspeccion.detalleActas.areasEmpresa.get(i).area;
							}
							if (j == 2) {
								ps = "Si";
							}
							if (j == 3) {
								if (inspeccion.detalleActas.areasEmpresa.get(i).paralizacion) {
									ps = coma + inspeccion.detalleActas.areasEmpresa.get(i).area;
									coma = ", ";
								} else {
									ps = "";
								}
							}

						} else {
							ps = "";
						}
						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$area";
						ds2 = "d";
					}
				}

				//////////////////////////////////

				ds = "$da";
				ds2 = "to";
				ps = "";
				//
				for (int i = 0; i < inspeccion.detalleActas.detalleActa.size(); i++) {

					for (int j = 1; j <= 4; j++) {
						ds = ds + i + ds2 + j;
						if (inspeccion.detalleActas.detalleActa.get(i).na == null) {
							if (inspeccion.detalleActas.detalleActa.get(i).cumple != null) {
								if (j == 1) {
									if (inspeccion.detalleActas.detalleActa.get(i).cumple) {
										ps = "X";
									} else {
										ps = "";
									}
								}
								if (j == 2) {
									if (inspeccion.detalleActas.detalleActa.get(i).cumple) {
										ps = "";
									} else {
										ps = "X";
									}
								}
							} else {
								ps = "";
							}
						} else {
							ps = "";
							if (j == 3) {
								if (inspeccion.detalleActas.detalleActa.get(i).na) {
									ps = "X";
								} else {
									ps = "";
								}
							}
						}
						if (j == 4) {
							if (inspeccion.detalleActas.detalleActa.get(i).observacion != null) {
								ps = inspeccion.detalleActas.detalleActa.get(i).observacion;
							} else {
								ps = "";
							}
						}

						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$da";
						ds2 = "to";
					}
				}
			} // fin if de detalle acta = null
			doc.write(new FileOutputStream(new File(
					dir_archivos + dir_enlace + "inspectoria" + dir_enlace + "actas" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + dir_enlace + "inspectoria" + dir_enlace + "actas" + dir_enlace
					+ "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=[" + inspeccion.codigo + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * SERVICIO GENERAR CONMINATORIA
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/conminatoria", method = RequestMethod.GET)
	public void generarConminatoria(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable2,
			@RequestParam(name = "fechaAudiencia", required = true, defaultValue = "0") String pVariable3,
			@RequestParam(name = "horaAudiencia", required = true, defaultValue = "0") String pVariable4)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {

		String razonSocial = "";

		String fechaInicioActividad = "";
		String codigoMteps = "";
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);

		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();

		StoredProcedureQuery procedure2 = entityManager
				.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

		procedure2.setParameter("v_id", inspeccion.idDenuncia);

		FobtenerDenuncia denuncia = (FobtenerDenuncia) procedure2.getSingleResult();

		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {

				razonSocial = resultDatos.getString("razon_social");
				if (resultDatos.getString("matricula_comercio") != null) {

					// fechaInicioActividad=resultDatos.getDate("fecha_inicio_actividad").toString();}
					codigoMteps = resultDatos.getString("codigo_mteps");

				} else {
					razonSocial = inspeccion.razonSocial;
				}
			}
		}

		if (inspeccion.citeSigecConminatoria == null) {
			Resultado vDatSigec = sigecCore.generarDocumentoSIGEC(pVariable2, 0, 0, 84, "CONMINATORIA", 1, false, "");
			inspeccion.citeSigecConminatoria = (String) vDatSigec.datoAdicional;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure3 = connection.prepareCall(
					"UPDATE mteps_inspecciones.ins_inspecciones SET cite_sigec_conminatoria= ? where id_inspeccion = ?");
			procedure3.setString(1, inspeccion.citeSigecConminatoria);
			procedure3.setInt(2, pVariable1);

			procedure3.execute();
			procedure3.close();
		}

		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "conminatoria" + dir_enlace
				+ "conminatoria.docx";
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "conminatoria" + dir_enlace
				+ "edit-conminatoria.docx";
		String fileName = "conminatoria.docx";

		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.citeSigecConminatoria);

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$dato001", inspeccion.unidadOrganizacional);
						text = text.replace("$dato002", inspeccion.citeSigecConminatoria);
						text = text.replace("$dato003", generarFecha(vDSigec.fechaCreacionSigec, 2));
						text = text.replace("$dato004", denuncia.denunciantes.get(0).nombreCompleto);
						text = text.replace("$dato005", inspeccion.razonSocial);
						text = text.replace("$dato006", generarFecha(inspeccion.fechaCreacion, 2));
						text = text.replace("$dato007", inspeccion.inspectores.get(0).nombre);
						text = text.replace("$dato008", inspeccion.razonSocial);
						text = text.replace("$dato009", denuncia.denunciados.get(0).nombreCompleto);
						text = text.replace("$dato010", pVariable3);
						text = text.replace("$dato011", pVariable4);

					}
					run.setText(text, 0);
				}
			}

			doc.write(new FileOutputStream(
					new File(dir_archivos + "inspectoria" + dir_enlace + "conminatoria" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "inspectoria" + dir_enlace + "conminatoria" + dir_enlace
					+ "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition",
					"attachment; filename=[" + inspeccion.citeSigecConminatoria + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * SERVICIO GENERAR INFORME
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/informeInspeccion", method = RequestMethod.GET)
	public void generarInforme(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable2,
			@RequestParam(name = "idVia", required = false, defaultValue = "0") Integer pVariable3,
			@RequestParam(name = "idDestinatario", required = true, defaultValue = "0") Integer pVariable4,
			@RequestParam(name = "hr", required = false, defaultValue = "false") Boolean pVariable5,
			@RequestParam(name = "proveido", required = true, defaultValue = "") String pVariable6)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {
		String nit = "", razonSocial = "", nombreComercial = "", matriculaComercio = "", direccion = "",
				descActividadDeclarada = "", nroPatronal = "", representanteLegal = "", nrDoc = "", telefono = "",
				fechaInicioActividad = "", codigoMteps = "";

		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);
		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();
		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {
				nit = resultDatos.getString("nit");
				razonSocial = resultDatos.getString("razon_social");
				nombreComercial = resultDatos.getString("nombre_comercial");
				if (resultDatos.getString("matricula_comercio") != null) {
					matriculaComercio = resultDatos.getString("matricula_comercio");
				}
				direccion = resultDatos.getString("direccion");
				descActividadDeclarada = resultDatos.getString("desc_actividad_declarada");
				nroPatronal = resultDatos.getString("nro_patronal");
				representanteLegal = resultDatos.getString("nombre_completo");
				nrDoc = resultDatos.getString("nro_documento");
				if (resultDatos.getString("telefonos") != null) {
					telefono = resultDatos.getString("telefonos");
				}

				// fechaInicioActividad=resultDatos.getDate("fecha_inicio_actividad").toString();}
				codigoMteps = resultDatos.getString("codigo_mteps");

			} else {
				nit = inspeccion.nit;
				razonSocial = inspeccion.razonSocial;
				nombreComercial = inspeccion.razonSocial;
				matriculaComercio = inspeccion.matriculaComercio;
				direccion = inspeccion.direccionEmpresa;
			}
		}

		String vfile = "", referencia = "";
		Integer vtipoSigec = 0;
		switch (inspeccion.idTipoInspeccion) {
		case 259:
			vfile = "informe-it-servicios.docx";
			referencia = "Inspección Técnica de Seguridad en la Construcción realizada en la Empresa o Establecimiento Laboral "
					+ razonSocial;
			vtipoSigec = 77;
			break;
		case 260:
			vfile = "informe-it-industria.docx";
			referencia = "Inspección Técnica de Seguridad en la Construcción realizada en la Empresa o Establecimiento Laboral "
					+ razonSocial;
			vtipoSigec = 76;
			break;
		case 261:
			vfile = "informe-it-construccion.docx";
			referencia = "Inspección Técnica de Seguridad en la Construcción realizada en la Empresa o Establecimiento Laboral "
					+ razonSocial;
			vtipoSigec = 79;
			break;
		case 262:
			vfile = "informe-rit.docx";
			referencia = "Re-Inspección Técnica de Seguridad en la Construcción realizada en la Empresa o Establecimiento Laboral "
					+ razonSocial;
			vtipoSigec = 78;
			break;
		case 263:
			vfile = "informe-i-laboral.docx";
			referencia = "Informe de inspección laboral realizada  a la empresa o establecimiento laboral "
					+ razonSocial;
			;

			vtipoSigec = 81;
			break;
		case 264:
			vfile = "informe-iacc-trabajo.docx";
			referencia = "Investigación de accidente de Trabajo realizado en la Empresa o Establecimiento Laboral "
					+ razonSocial;
			vtipoSigec = 80;
			break;
		case 265:
			vfile = "informe-ver-prot-bioseg.docx";
			referencia = "Verificación de Protocolos de Bioseguridad realizada a la Empresa o Establacimiento Laboral "
					+ razonSocial;
			vtipoSigec = 82;
			break;
		case 266:
			vfile = "informe-ver-acoso-lab.docx";
			referencia = "Verificación de Acoso Laboral y/o acoso sexual a mujeres en la Empresa, Establecimiento Laboral o Entidad Pública"
					+ razonSocial;
			vtipoSigec = 83;
			break;

		}

		if (inspeccion.citeSigecInformeInsp == null) {
			Resultado vDatSigec = sigecCore.generarDocumentoSIGEC(pVariable2, pVariable3, pVariable4, vtipoSigec,
					referencia, 1, pVariable5, pVariable6);
			inspeccion.citeSigecInformeInsp = (String) vDatSigec.datoAdicional;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure2 = connection.prepareCall(
					"UPDATE mteps_inspecciones.ins_inspecciones SET cite_sigec_informe_insp= ? where id_inspeccion = ?");
			procedure2.setString(1, inspeccion.citeSigecInformeInsp);
			procedure2.setInt(2, inspeccion.idInspeccion);

			procedure2.execute();
			procedure2.close();
		}
		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.citeSigecInformeInsp);
		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "informes" + dir_enlace + vfile;
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "informes" + dir_enlace + "edit-" + vfile;
		String fileName = vfile;

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$dato000",
								((inspeccion.citeSigecInformeInsp == null) ? "" : inspeccion.citeSigecInformeInsp));
						text = text.replace("$dato001", vDSigec.nombreDestinatario);
						text = text.replace("$dato002", vDSigec.cargoDestinatario);
						text = text.replace("$dato003", ((vDSigec.nombreVia == null) ? "" : vDSigec.nombreVia));
						text = text.replace("$dato004", ((vDSigec.cargoVia == null) ? "" : vDSigec.cargoVia));
						text = text.replace("$dato005", vDSigec.nombreRemitente);
						text = text.replace("$dato006", vDSigec.cargoRemitente);
						text = text.replace("$dato007", referencia);
						text = text.replace("$dato008", ((generarFecha(vDSigec.fechaCreacionSigec, 1) == null) ? ""
								: generarFecha(vDSigec.fechaCreacionSigec, 1)));
						text = text.replace("$dato009",
								((inspeccion.sigecCiteMemo == null) ? "" : inspeccion.sigecCiteMemo));
						text = text.replace("$dato010", ((generarFecha(inspeccion.fechaCreacion, 2) == null) ? ""
								: generarFecha(inspeccion.fechaCreacion, 2)));
						text = text.replace("$dato011", ((generarFecha(inspeccion.fechaInspeccion, 1) == null) ? ""
								: generarFecha(inspeccion.fechaInspeccion, 1)));
						text = text.replace("$dato012", nombreComercial);
						text = text.replace("$dato013", representanteLegal);
						text = text.replace("$dato014", nrDoc);
						text = text.replace("$dato015", nroPatronal);
						text = text.replace("$dato016", codigoMteps);
						text = text.replace("$dato017", nit);
						text = text.replace("$dato018", descActividadDeclarada);
						text = text.replace("$dato019", direccion);
						text = text.replace("$dato020", "");
						text = text.replace("$dato021", "");
						text = text.replace("$dato022", telefono);
						if (inspeccion.detalleActas != null) {
							if (inspeccion.detalleActas.nroAdministrativos == null) {
								inspeccion.detalleActas.nroAdministrativos = 0;
							}
							text = text.replace("$dato023", inspeccion.detalleActas.nroAdministrativos.toString());
							if (inspeccion.detalleActas.nroOperativos == null) {
								inspeccion.detalleActas.nroOperativos = 0;
							}
							text = text.replace("$dato024", inspeccion.detalleActas.nroOperativos.toString());

							text = text.replace("$dato025",
									generarFecha(inspeccion.detalleActas.fechaPlazoParalizacion, 1));
							text = text.replace("$dato026", generarFecha(inspeccion.detalleActas.fechaVisita, 1));
							text = text.replace("$dato027", generarFecha(inspeccion.detalleActas.fechaVisita, 4));
						}

					}
					run.setText(text, 0);
				}
			}

			if (inspeccion.detalleActas != null) {
				String ds = "$emp";
				String ds2 = "d";
				String ps = "";
				for (int i = 0; i <= 6; i++) {

					for (int j = 1; j <= 3; j++) {

						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.personasEmpleador.size()) {

							if (j == 1) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).cargo;
							}
							if (j == 2) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).nombreCompleto;
							}
							if (j == 3) {
								ps = inspeccion.detalleActas.personasEmpleador.get(i).nroDocumento;
							}

						} else {
							ps = "";
						}

						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$emp";
						ds2 = "d";
					}
				}
				//////////////////////////////
				ds = "$empt";
				ds2 = "d";

				for (int i = 0; i <= 7; i++) {

					for (int j = 1; j <= 3; j++) {
						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.personasTrabajadores.size()) {
							if (j == 1) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).cargo;
							}
							if (j == 2) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).nombreCompleto;
							}
							if (j == 3) {
								ps = inspeccion.detalleActas.personasTrabajadores.get(i).nroDocumento;
							}

						} else {
							ps = "";
						}
						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$empt";
						ds2 = "d";
					}
				}
				////////////////////////////////

				ds = "$area";
				ds2 = "d";
				String coma = "";

				for (int i = 0; i <= 9; i++) {

					for (int j = 1; j <= 3; j++) {
						ds = ds + i + ds2 + j;
						if (i < inspeccion.detalleActas.areasEmpresa.size()) {
							if (j == 1) {
								ps = inspeccion.detalleActas.areasEmpresa.get(i).area;
							}
							if (j == 2) {
								ps = "Si";
							}
							if (j == 3) {
								if (inspeccion.detalleActas.areasEmpresa.get(i).paralizacion) {
									ps = coma + inspeccion.detalleActas.areasEmpresa.get(i).area;
									coma = ", ";
								} else {
									ps = "";
								}
							}

						} else {
							ps = "";
						}
						for (XWPFTable tbl : doc.getTables()) {
							for (XWPFTableRow row : tbl.getRows()) {
								for (XWPFTableCell cell : row.getTableCells()) {
									for (XWPFParagraph p : cell.getParagraphs()) {
										for (XWPFRun r : p.getRuns()) {
											String text = r.getText(0);
											if (text != null) {

												text = text.replace(ds, ps);

												r.setText(text, 0);
											}
										}
									}
								}
							}
						}
						ds = "$area";
						ds2 = "d";
					}
				}

			}
			doc.write(new FileOutputStream(new File(
					dir_archivos + dir_enlace + "inspectoria" + dir_enlace + "informes" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + dir_enlace + "inspectoria" + dir_enlace + "informes" + dir_enlace
					+ "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition",
					"attachment; filename=[" + inspeccion.citeSigecInformeInsp + "]" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * INSPECCION LABORAL SERVICIO REQUERIMIENTO DE DOCUMENTACIÓN
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/requerimientoDocumentacion", method = RequestMethod.GET)
	public void generarRequerimientoDocumentacion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "direccionOficina", required = true, defaultValue = "") String pVariable2)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {

		String razonSocial = "";

		String fechaInicioActividad = "";
		String codigoMteps = "";
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);

		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();

		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {

				razonSocial = resultDatos.getString("razon_social");
				if (resultDatos.getString("matricula_comercio") != null) {

					// fechaInicioActividad=resultDatos.getDate("fecha_inicio_actividad").toString();}
					codigoMteps = resultDatos.getString("codigo_mteps");

				} else {
					razonSocial = inspeccion.razonSocial;
				}
			}
		}
		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.sigecCiteMemo);
		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "requerimientodedocumentacion" + dir_enlace
				+ "requerimiento-de-documentacion.docx";
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "requerimientodedocumentacion" + dir_enlace
				+ "edit-requerimiento-de-documentacion.docx";
		String fileName = "requerimiento-de-documentacion.docx";
		LocalDateTime fechahoy = LocalDateTime.now();
		Instant instant = fechahoy.atZone(ZoneId.systemDefault()).toInstant();
		Date dFecha = Date.from(instant);
		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {
						text = text.replace("$dato000", generarFecha(dFecha, 2));
						text = text.replace("$dato001", razonSocial);
						text = text.replace("$dato003", inspeccion.sigecCiteMemo);
						text = text.replace("$dato004", generarFecha(vDSigec.fechaCreacionSigec, 2));
						text = text.replace("$dato005", inspeccion.inspectores.get(0).nombre);
						text = text.replace("$dato006", inspeccion.unidadOrganizacional);
						text = text.replace("$dato007", pVariable2);

					}
					run.setText(text, 0);
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "inspectoria" + dir_enlace
					+ "requerimientodedocumentacion" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "inspectoria" + dir_enlace + "requerimientodedocumentacion"
					+ dir_enlace + "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=[" + inspeccion.codigo + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * INSPECCION LABORAL SERVICIO ACTA DE IMPEDIMIENTO Y/O OBSTRUCCION
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/actImpObstruccion", method = RequestMethod.GET)
	public void generarImpedimentoObstruccion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {

		String nit = "";
		String razonSocial = "";
		String nombreComercial = "";
		String matriculaComercio = "";
		String direccion = "";
		String descActividadDeclarada = "";
		String nroPatronal = "";
		String representanteLegal = "";
		String nrDoc = "";
		String telefono = "";
		String fechaInicioActividad = "";
		String codigoMteps = "";
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);

		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();

		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {

				nit = resultDatos.getString("nit");
				razonSocial = resultDatos.getString("razon_social");
				nombreComercial = resultDatos.getString("nombre_comercial");
				if (resultDatos.getString("matricula_comercio") != null) {
					matriculaComercio = resultDatos.getString("matricula_comercio");
				}
				direccion = resultDatos.getString("direccion");
				descActividadDeclarada = resultDatos.getString("desc_actividad_declarada");
				nroPatronal = resultDatos.getString("nro_patronal");
				representanteLegal = resultDatos.getString("nombre_completo");
				nrDoc = resultDatos.getString("nro_documento");
				if (resultDatos.getString("telefonos") != null) {
					telefono = resultDatos.getString("telefonos");

				} else {
					razonSocial = inspeccion.razonSocial;
				}
			}
		}

		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "impedimento-obstruccion" + dir_enlace
				+ "acta-impedimento-obstruccion.docx";
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "impedimento-obstruccion" + dir_enlace
				+ "edit-acta-impedimento-obstruccion.docx";
		String fileName = "acta-impedimento-obstruccion.docx";

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {

						text = text.replace("$dato000", inspeccion.sigecCiteMemo);
						text = text.replace("$dato001", inspeccion.unidadOrganizacional);
						text = text.replace("$dato002", "LA PAZ");
						text = text.replace("$dato003", generarFecha(inspeccion.fechaCreacion, 3));
						text = text.replace("$dato004", "10:20");
						text = text.replace("$dato005", razonSocial);
						text = text.replace("$dato006", nit);
						text = text.replace("$dato007", direccion);
						text = text.replace("$dato008", descActividadDeclarada);
						text = text.replace("$dato009", representanteLegal);
						text = text.replace("$dato010", nrDoc);
						text = text.replace("$dato011", "Representante Legal");

					}
					run.setText(text, 0);
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "inspectoria" + dir_enlace
					+ "requerimientodedocumentacion" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "inspectoria" + dir_enlace + "requerimientodedocumentacion"
					+ dir_enlace + "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=[" + inspeccion.codigo + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/**
	 * INSPECCION LABORAL SERVICIO INFORME DE IMPEDIMIENTO Y/O OBSTRUCCION
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	@RequestMapping(path = "/reporte/infImpObstruccion", method = RequestMethod.GET)
	public void generarInformeImpedimentoObstruccion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "idInspeccion", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable2,
			@RequestParam(name = "idVia", required = false, defaultValue = "0") Integer pVariable3,
			@RequestParam(name = "idDestinatario", required = true, defaultValue = "0") Integer pVariable4,
			@RequestParam(name = "hr", required = false, defaultValue = "false") Boolean pVariable5,
			@RequestParam(name = "proveido", required = true, defaultValue = "") String pVariable6)
			throws FileNotFoundException, IOException, InvalidFormatException, JRException, ClassNotFoundException,
			SQLException {

		String nit = "";
		String razonSocial = "";
		String nombreComercial = "";
		String matriculaComercio = "";
		String direccion = "";
		String descActividadDeclarada = "";
		String nroPatronal = "";
		String representanteLegal = "";
		String nrDoc = "";
		String telefono = "";
		String fechaInicioActividad = "";
		String codigoMteps = "";
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url1, db_usuario1, db_password1);
		StoredProcedureQuery procedure = entityManager
				.createNamedStoredProcedureQuery("mteps_inspecciones.f_obtener_inspeccion_id");

		procedure.setParameter("v_id", pVariable1);

		FObtenerInspeccionId inspeccion = (FObtenerInspeccionId) procedure.getSingleResult();

		if (!inspeccion.empresaExterna) {
			CallableStatement procedures = connection.prepareCall(
					"select e.nit, e.razon_social ::character varying, e.matricula_comercio::character varying, concat((case when s2.zona is null then '' else 'Z. '||s2.zona end),(case when s2.avenida_calle is null then '' else ','||s2.avenida_calle end),(case when s2.uv is null or s2.uv::character varying ='S/N' then '' else ', UV. '||s2.uv end), (case when s2.manzana is null or s2.manzana ::character varying ='S/N' then '' else ', MZN. '||s2.manzana end), (case when s2.edificio is null or s2.edificio ::character varying ='S/N' then '' else ', ED. '||s2.edificio end), (case when s2.piso is null or s2.piso ::character varying ='S/N' then '' else ', PISO '||s2.piso end), (case when s2.nro_oficina is null or s2.nro_oficina ::character varying ='S/N' then '' else ', OF. Nº '||s2.nro_oficina end), (case when s2.numero is null or s2.numero ::character varying ='S/N' then '' else ', Nº '||s2.numero end) )::character varying as direccion,e.desc_actividad_declarada::character varying ,s2.nro_patronal::character varying,rl.nombre_completo::character varying, rl.nro_documento ::character varying,e.nombre_comercial::character varying ,s2.telefonos::character varying,e.fecha_inicio_actividad::character varying,e.codigo_mteps::character varying \r\n"
							+ "from public.empresa e \r\n"
							+ "inner join sucursal s2 on e.id_empresa = s2.fid_empresa and s2.nro_sucursal = 1 \r\n"
							+ "inner join representante_legal rl on s2.fid_representante_legal =rl.id_representante_legal \r\n"
							+ "where e.id_empresa =" + inspeccion.idEmpresa);
			ResultSet resultDatos = procedures.executeQuery();

			if (resultDatos.next()) {

				nit = resultDatos.getString("nit");
				razonSocial = resultDatos.getString("razon_social");
				nombreComercial = resultDatos.getString("nombre_comercial");
				if (resultDatos.getString("matricula_comercio") != null) {
					matriculaComercio = resultDatos.getString("matricula_comercio");
				}
				direccion = resultDatos.getString("direccion");
				descActividadDeclarada = resultDatos.getString("desc_actividad_declarada");
				nroPatronal = resultDatos.getString("nro_patronal");
				representanteLegal = resultDatos.getString("nombre_completo");
				nrDoc = resultDatos.getString("nro_documento");
				if (resultDatos.getString("telefonos") != null) {
					telefono = resultDatos.getString("telefonos");

				} else {
					razonSocial = inspeccion.razonSocial;
				}
			}
		}

		if (inspeccion.citeSigecInformeObst == null) {
			Resultado vDatSigec = sigecCore.generarDocumentoSIGEC(pVariable2, pVariable3, pVariable4, 81,
					"Informe de Impedimento y/o obstrucción a la Empresa o Establecimiento Laboral " + razonSocial, 1,
					pVariable5, pVariable6);
			inspeccion.citeSigecInformeObst = (String) vDatSigec.datoAdicional;

			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure2 = connection.prepareCall(
					"UPDATE mteps_inspecciones.ins_inspecciones SET cite_sigec_informe_obst= ? where id_inspeccion = ?");
			procedure2.setString(1, inspeccion.citeSigecInformeObst);
			procedure2.setInt(2, inspeccion.idInspeccion);

			procedure2.execute();
			procedure2.close();
		}
		EntDocumentoSigec vDSigec = sigecCore.infoDocSigec(inspeccion.citeSigecInformeInsp);

		String vFromFile = dir_archivos + "inspectoria" + dir_enlace + "impedimento-obstruccion" + dir_enlace
				+ "informe-impedimento-obstruccion.docx";
		String vToFile = dir_archivos + "inspectoria" + dir_enlace + "impedimento-obstruccion" + dir_enlace
				+ "edit-informe-impedimento-obstruccion.docx";
		String fileName = "informe-impedimento-obstruccion.docx";

		if (copyFile(vFromFile, vToFile)) {

			XWPFDocument doc = new XWPFDocument(OPCPackage.open(vToFile));

			for (XWPFParagraph p : doc.getParagraphs()) {
				for (XWPFRun run : p.getRuns()) {
					String text = run.getText(0);
					if (text != null) {

						text = text.replace("$dato001",
								(inspeccion.citeSigecInformeObst == null) ? "" : inspeccion.citeSigecInformeObst);
						text = text.replace("$dato002", vDSigec.nombreDestinatario);
						text = text.replace("$dato_002", vDSigec.cargoDestinatario);
						text = text.replace("$dato003", ((vDSigec.nombreVia == null) ? "" : vDSigec.nombreVia));
						text = text.replace("$dato_003", ((vDSigec.cargoVia == null) ? "" : vDSigec.cargoVia));
						text = text.replace("$dato004", vDSigec.nombreRemitente);
						text = text.replace("$dato_004", vDSigec.cargoRemitente);
						text = text.replace("$dato005", razonSocial);
						text = text.replace("$dato006", generarFecha(inspeccion.fechaCreacion, 2));
						text = text.replace("$dato007", inspeccion.sigecCiteMemo);
						text = text.replace("$dato008", generarFecha(inspeccion.fechaCreacion, 1));
						text = text.replace("$dato010", nrDoc);
						text = text.replace("$dato_10", representanteLegal);
						text = text.replace("$dato011", nit);
						text = text.replace("$dato012", direccion);
						text = text.replace("$dato013", descActividadDeclarada);
						// por determinar
						text = text.replace("$dato014", (inspeccion.fechaInspeccion == null) ? ""
								: generarFecha(inspeccion.fechaInspeccion, 2));
						text = text.replace("$dato015", generarFecha(inspeccion.fechaInspeccion, 4));

					}
					run.setText(text, 0);
				}
			}

			doc.write(new FileOutputStream(new File(dir_archivos + "inspectoria" + dir_enlace
					+ "impedimento-obstruccion" + dir_enlace + "output.docx")));
			doc.close();
			String rutaConvenio = dir_archivos + "inspectoria" + dir_enlace + "impedimento-obstruccion" + dir_enlace
					+ "output.docx";
			InputStream inputStream = new BufferedInputStream(new FileInputStream(rutaConvenio));
			response.setContentType("application/x-docx");
			response.addHeader("Content-Disposition", "attachment; filename=[" + inspeccion.codigo + "] " + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	/** SERVICIO PRUEBA */
	@RequestMapping(path = "/reporte/prueba", method = RequestMethod.GET)
	public Resultado pruebaGenerarRepo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1)
			throws SQLException, FileNotFoundException, InvalidFormatException, ClassNotFoundException, IOException,
			JRException {

		Resultado vObjResultado = new Resultado();

		try {

			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("mteps_denuncias.f_obtener_denuncia_id");

			procedure.setParameter("v_id", pVariable1);

			FobtenerDenuncia result = (FobtenerDenuncia) procedure.getSingleResult();

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = result;
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 100;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

}
