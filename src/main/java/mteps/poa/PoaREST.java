package mteps.poa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;
import mteps.poa.entity.DatoEntradaGestionConciliacion;
import mteps.poa.entity.EntradaCorreo;
import mteps.poa.entity.F_lista_solicitud_certificacion_proceso;
import mteps.poa.entity.F_reporte_form_presupuesto_gasto;
import mteps.poa.entity.F_reporte_formulacion;
import mteps.poa.entity.F_reporte_reform_presupuesto_gasto;
import mteps.poa.entity.F_reporte_reformulacion_poa;
import mteps.poa.entity.F_uniorg_apertura;

@RestController
@RequestMapping("/poa")
public class PoaREST {

	@Autowired
	PoaCore poaCore;

	@Value("${ruta.archivos}")
	private String rutaprincipal;

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@GetMapping("/apertura")
	public F_reporte_formulacion getReporteFormulacionPoa(
			@RequestParam(name = "idPlan", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "idEstado", required = true, defaultValue = "0") Integer pVariable2) {

		return poaCore.getReporteFormulacionPoa(pVariable1, pVariable2);
	}

	@GetMapping("/reformulacion")
	public F_reporte_reformulacion_poa reFormulacionPoa(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable2,
			@RequestParam(name = "idEstado", required = true, defaultValue = "") Integer pVariable3) {

		return poaCore.reFormulacionPoa(pVariable2, pVariable3);
	}

	@GetMapping("/formulacionPresupuestoGasto")
	public F_reporte_form_presupuesto_gasto getReporteFormulacionPresupuestoGasto(
			@RequestParam(name = "idPlan", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "idEstado", required = true, defaultValue = "0") Integer pVariable3) {

		return poaCore.getReporteFormulacionPresupuestoGasto(pVariable1, pVariable3);
	}

	@GetMapping("/reformulacionPresupuestoGasto")
	public F_reporte_reform_presupuesto_gasto getReportereFormulacionPresupuestoGasto(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable2,
			@RequestParam(name = "idEstado", required = true, defaultValue = "") Integer pVariable3) {

		return poaCore.getReportereFormulacionPresupuestoGasto(pVariable2, pVariable3);
	}

	@GetMapping("/seguimientoEvaluacion")
	public Resultado listaConsultas(@RequestParam(name = "gestion", required = true, defaultValue = "") String gestion,
			@RequestParam(name = "estado", required = true, defaultValue = "") String estado)
			throws JsonProcessingException, SQLException {
		return poaCore.listaSegEv(gestion, estado);
	}

	@GetMapping("/aperturaPorUnidadOrg")
	public List<F_uniorg_apertura> aperturaPorUnidadOrg(
			@RequestParam(name = "idOrg", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idGestion", required = true, defaultValue = "") Integer pVariable2,
			@RequestParam(name = "login", required = true, defaultValue = "") String pVariable3,
			@RequestParam(name = "tipo", required = true, defaultValue = "") Integer pVariable4) {

		return poaCore.aperturaPorUnidadOrg(pVariable1, pVariable2, pVariable3, pVariable4);
	}

	@GetMapping("/listaProcesoGestion")
	public Resultado listaProcesoGestion(
			@RequestParam(name = "idTipoProceso", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "gestion", required = true, defaultValue = "") Integer pVariable2)
			throws JsonProcessingException, SQLException {

		return poaCore.listaProcesoGestion(pVariable1, pVariable2);
	}

	@RequestMapping(path = "/gestionSegEv", method = RequestMethod.POST)
	public Resultado gestionSegEv(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionSegEv(vObject);
	}

	@RequestMapping(path = "/gestionPlan", method = RequestMethod.POST)
	public Resultado gestionPlan(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionPlan(vObject);
	}

	@RequestMapping(path = "/gestionPlanRef", method = RequestMethod.POST)
	public Resultado gestionPlanRef(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionPlanRef(vObject);
	}

	@RequestMapping(path = "/gestionMatrizSegEv", method = RequestMethod.POST)
	public Resultado gestionMatrizSegEv(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionMatrizSegEv(vObject);
	}

	@RequestMapping(path = "/gestionReformulacion", method = RequestMethod.POST)
	public Resultado gestionReformulacion(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionReformulacion(vObject);
	}

	@RequestMapping(path = "/gestionFichaSegEv", method = RequestMethod.POST)
	public Resultado gestionFichaSegEv(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionFichaSegEv(vObject);
	}

	@RequestMapping(path = "/gestionConciliacion", method = RequestMethod.POST)
	public Resultado gestionConciliacion(@RequestBody DatoEntradaGestionConciliacion vObject)
			throws JsonProcessingException, SQLException {
		return poaCore.gestionConciliacion(vObject);
	}

	@RequestMapping(path = "/gestionEjecucion", method = RequestMethod.POST)
	public Resultado gestionEjecucion(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionEjecucion(vObject);
	}

	@RequestMapping(path = "/gestionIndicadorPni", method = RequestMethod.POST)
	public Resultado gestionIndicadorPni(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return poaCore.gestionIndicadorPni(vObject);
	}

	@GetMapping("/listaTeSeguimiento")
	public Resultado f_lista_te_seguimiento(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idSeguimiento", required = true, defaultValue = "") Integer pVariable2)
			throws JsonProcessingException, SQLException {

		return poaCore.f_lista_te_seguimiento(pVariable1, pVariable2);
	}

	@GetMapping("/listaCertificaciones")
	public Resultado f_lista_certificaciones(
			@RequestParam(name = "estado", required = true, defaultValue = "") String pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "") String pVariable2,
			@RequestParam(name = "gestion", required = true, defaultValue = "") Integer pVariable3)
			throws JsonProcessingException, SQLException {

		return poaCore.f_lista_certificaciones(pVariable1, pVariable2, pVariable3);
	}

	@GetMapping("/listaFichaSeguimiento")
	public Resultado lista_ficha_seguimiento(
			@RequestParam(name = "idSeguimiento", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable2)
			throws JsonProcessingException, SQLException {

		return poaCore.lista_ficha_seguimiento(pVariable1, pVariable2);
	}

	@GetMapping("/listaDocConciliacion")
	public Resultado listaDocConciliacion(
			@RequestParam(name = "gestion", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException {

		return poaCore.listaDocConciliacion(pVariable1);
	}

	@GetMapping("/listaConciliacion")
	public Resultado listaConciliacion(
			@RequestParam(name = "idDocConciliacion", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable2,
			@RequestParam(name = "fechaCorte", required = true, defaultValue = "") String pVariable3)

			throws JsonProcessingException, SQLException {

		return poaCore.listaConciliacion(pVariable1, pVariable2, pVariable3);
	}

	@GetMapping("/listaEstructJsonMeta")
	public Resultado listaEstructJsonMeta(
			@RequestParam(name = "idProceso", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idIndicador", required = true, defaultValue = "") Integer pVariable2)

			throws JsonProcessingException, SQLException {

		return poaCore.listaEstructJsonMeta(pVariable1, pVariable2);
	}

	@GetMapping("/listaEstructJsonPresup")
	public Resultado listaEstructJsonPresup(
			@RequestParam(name = "idProceso", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idIndicador", required = true, defaultValue = "") Integer pVariable2)

			throws JsonProcessingException, SQLException {

		return poaCore.listaEstructJsonPresup(pVariable1, pVariable2);
	}

	@GetMapping("/listaEjecucionFisica")
	public Resultado listaEjecucionFisica(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "tipoProceso", required = true, defaultValue = "") Integer pVariable2)

			throws JsonProcessingException, SQLException {

		return poaCore.listaEjecucionFisica(pVariable1, pVariable2);
	}

	@GetMapping("/listaReformulaciones")
	public Resultado lista_reformulaciones(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.lista_reformulaciones(pVariable1);
	}

	@GetMapping("/listaTeMemoriaRef")
	public Resultado listaTeMemoriaRef(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.listaTeMemoriaRef(pVariable1);
	}

	@GetMapping("/busquedaCertificacion")
	public Resultado busquedaCertificacion(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "partida", required = true, defaultValue = "") String pVariable2)

			throws JsonProcessingException, SQLException {

		return poaCore.busquedaCertificacion(pVariable1, pVariable2);
	}

	@GetMapping("/estadoPttoRef")
	public Resultado estadoPttoRef(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.estadoPttoRef(pVariable1);
	}

	@GetMapping("/conciliacionActivo")
	public Resultado f_conciliacion_activo() throws JsonProcessingException, SQLException {

		return poaCore.f_conciliacion_activo();
	}

	@GetMapping("/aperturaNombre")
	public Resultado aperturaNombre(
			@RequestParam(name = "idApertura", required = true, defaultValue = "") Integer pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.aperturaNombre(pVariable1);
	}

	@GetMapping("/obtenerDatoFirma")
	public Resultado obtenerDatoFirma(
			@RequestParam(name = "idUsuario", required = true, defaultValue = "") Integer pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.obtenerDatoFirma(pVariable1);
	}

	@GetMapping("/obtenerNroActivosSeg")
	public Resultado obtenerNroActivosSeg(
			@RequestParam(name = "estado", required = true, defaultValue = "") String pVariable1)

			throws JsonProcessingException, SQLException {

		return poaCore.obtenerNroActivosSeg(pVariable1);
	}

	/// GUARDAR DOC opc 2

	@PostMapping("/cargarArchivo")
	public Resultado uploadFile2(@RequestParam("file") MultipartFile file) {
		Resultado vObjResultado = new Resultado();
		
		if (file.isEmpty()) {
			vObjResultado.notificacion = "Cargar archivo";
			vObjResultado.codigoResultado = 400;
		}
		
		
		/*
		LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String fechaHoraActualFormateada = fechaHoraActual.format(formato);
       */
		
		long timestamp = System.currentTimeMillis();
		String ruta = rutaprincipal + "/archivosBackEndMTEPS";
		String nombre = file.getOriginalFilename();
		String extension = FileNameUtils.getExtension(nombre);
		String nuevoNombre = "DOC_SISPOA_"+timestamp+"."+extension;
		Path rutaArchivo = Paths.get(ruta).resolve(nuevoNombre).toAbsolutePath();

		try {
			Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
			vObjResultado.notificacion = "Se cargo correctamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional=nuevoNombre;
		} catch (IOException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
		}

		return vObjResultado;
	}

	@GetMapping("/descargarArchivo/{fileName:.+}")
	public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) throws IOException {

		File file = new File(rutaprincipal + "/archivosBackEndMTEPS/" + fileName);
		Path path = Paths.get(file.getAbsolutePath());

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return ResponseEntity.ok().headers(header).contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}

	@GetMapping("/eliminarArchivo/{nombreArchivo:.+}")
	public Resultado eliminarArchivo(@PathVariable String nombreArchivo) {
		Resultado vObjResultado = new Resultado();

		String ruta = rutaprincipal + "/archivosBackEndMTEPS";
		Path rutaArchivo = Paths.get(ruta).resolve(nombreArchivo).toAbsolutePath();

		try {
			Files.delete(rutaArchivo);
			vObjResultado.notificacion = "Se elimino correctamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
		} catch (IOException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
		}

		return vObjResultado;
	}

	@GetMapping("/programadoEjecutado")
	public Resultado programadoEjecutado(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "idSeguimiento", required = true, defaultValue = "") Integer pVariable2)
			throws JsonProcessingException, SQLException {

		return poaCore.programadoEjecutado(pVariable1, pVariable2);
	}

	@GetMapping(value = "/listasolicitudxproceso/{id_proceso}")
	public List<F_lista_solicitud_certificacion_proceso> getListaSolicitudxProceso(@PathVariable Integer id_proceso)
			throws JsonProcessingException, SQLException {
		// Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
		return poaCore.listaSolicitudCertificacionxProceso(id_proceso);
	}

	///

	@PostMapping("/cargarFirma/{id_usuario}")
	public Resultado uploadFirma(@PathVariable Integer id_usuario, @RequestParam("datosImagen") MultipartFile file)
			throws SQLException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fechaComoCadena = sdf.format(new Date());
		Resultado vObjResultado = new Resultado();

		String ruta = rutaprincipal + "/archivosBackEndMTEPS/firmasPOA";
		String nombre = file.getOriginalFilename();
		String extension = FileNameUtils.getExtension(nombre);

		nombre = id_usuario + " " + fechaComoCadena + "." + extension;

		if (file.isEmpty()) {
			vObjResultado.notificacion = "Cargar archivo";
			vObjResultado.codigoResultado = 400;
		}

		try {
			Connection connection = null;
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement firma = connection
					.prepareCall("UPDATE mteps_plan.pln_firma SET estado=1 where id_usuario =?");
			firma.setInt(1, id_usuario);
			firma.execute();
			firma.close();

			CallableStatement firma2 = connection.prepareCall("INSERT INTO mteps_plan.pln_firma\r\n"
					+ "(id_firma, id_usuario, estado, fecha_creacion, imagen_firma)\r\n"
					+ "VALUES((select max(id_firma)+1 from  mteps_plan.pln_firma), ?, 2, now(), ?);");
			firma2.setInt(1, id_usuario);
			firma2.setString(2, nombre);
			firma2.execute();
			firma2.close();

			Path rutaArchivo = Paths.get(ruta).resolve(nombre).toAbsolutePath();

			Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
			vObjResultado.notificacion = "Se almacenó correctamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	@GetMapping("/actPresUnidOrg")
	public Resultado listaActPresUnidOrg() throws JsonProcessingException, SQLException {
		return poaCore.listaActPresUnidOrg();
	}

	@RequestMapping(path = "/correoActivacion", method = RequestMethod.POST)
	public Resultado correoActivacion(@RequestBody EntradaCorreo vObject) throws JsonProcessingException, SQLException {
		return poaCore.correoActivacion(vObject.usuario, vObject.correo);
	}

	@GetMapping(value = "usuario/activarUsuario")
	public Resultado activarUsuario(@RequestParam(name = "token", required = true, defaultValue = "") String var)
			throws JsonProcessingException, SQLException {
		return poaCore.activarUsuario(var);
	}

/////////////////////////////////////////// SERVICIO 
////////////////////////////////////obtener dato ususario

	@RequestMapping(path = "/obtenerdatosusuario", method = RequestMethod.GET)
	public Resultado obtenerDatosUsuario(
			@RequestParam(name = "login", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return poaCore.obtenerDatosUsuario(pVariable1);
	}
	
	
	@GetMapping("/reporteReformulacion")
	public Resultado fReporteReformulacion(
			@RequestParam(name = "idGestion", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException {

		return poaCore.fReporteReformulacion(pVariable1);
	}
	
	@GetMapping("/validarConciliacion")
	public Resultado validarConciliacion(
			@RequestParam(name = "idPlan", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "fechaFinal", required = true, defaultValue = "") String pVariable2)
			throws JsonProcessingException, SQLException {

		return poaCore.validacionConciliacion(pVariable1,pVariable2);
	}

}
