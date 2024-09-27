package mteps.tramites.fondoCustodia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import mteps.tramites.fondoCustodia.entity.DatosDocFC;
import mteps.tramites.fondoCustodia.entity.ListaFcIngreso;
import mteps.tramites.fondoCustodia.entity.MovimientoCuentaBancaria;
import mteps.tramites.fondoCustodia.entity.MovimientoCuentaBancaria.MovimientoDetalle;
import net.sf.jasperreports.engine.JRException;
import mteps.tramites.fondoCustodia.entity.PagosCpt;
import mteps.tramites.fondoCustodia.entity.SolicitudPago;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.config.entity.DatosDocumento;
import mteps.repo_nfs.RepoNfsCORE;
import mteps.sigec.entity.DatosDoc;

@RestController
@RequestMapping("/tramites/fondoCustodia")
public class fondCustodiaREST {

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;


	@Value("${ruta.archivos}")
	private String rutaprincipal;

	@Autowired
	fondCustodiaCORE fondoCustodia;

	@Autowired
	RepoNfsCORE repoNfsCore;

	@Autowired
	ConfigCORE configCore;

	@PersistenceContext
	private EntityManager entityManager;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION FONDOS EN CUSTODIA
	@RequestMapping(path = "/gestionFondoCustodia", method = RequestMethod.POST)
	public Resultado gestionMulta(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.gestionFondoCustodia(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// GESTION FONDOS EN CUSTODIA
	@RequestMapping(path = "/gestionFondoCustodiaCheque", method = RequestMethod.POST)
	public Resultado gestionCheques(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.gestionFondoCustodiaCheque(vObjEntradaDatos);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////OBTENER LISTA TRAMITES FONDOS EN CUSTODIA

	@RequestMapping(path = "/listaTramiteFc", method = RequestMethod.POST)
	public Resultado obtenerListaTramite(@RequestBody Object vObjEntradaDatos) throws JsonProcessingException {
		return fondoCustodia.listaTramiteFC(vObjEntradaDatos);
	}

	@RequestMapping(path = "/listaTramiteFc", method = RequestMethod.GET)
	public Resultado buscarTramiteFc(
			@RequestParam(name = "buscar", required = true, defaultValue = "") String pVariable1,
			@RequestParam(name = "login", required = false, defaultValue = "") String pVariable2)
			throws JsonProcessingException {
		return fondoCustodia.buscarTramiteFc(pVariable1, pVariable2);
	}

	@RequestMapping(path = "/requisitos", method = RequestMethod.GET)
	public Resultado ObtenerRequisitos(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.obtenerRequisitos(pVariable1);
	}

	@RequestMapping(path = "/identificarTramite", method = RequestMethod.GET)
	public Resultado identificarTramite(
			@RequestParam(name = "codTramite", required = true, defaultValue = "0") String pVariable1,
			@RequestParam(name = "tipo", required = true, defaultValue = "0") String pVariable2,
			@RequestParam(name = "transaccion", required = true, defaultValue = "0") String pVariable3)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.identificarTramite(pVariable1, pVariable2, pVariable3);
	}

	@RequestMapping(path = "/obtenerOpFc", method = RequestMethod.GET)
	public Resultado obtenerOpFc() throws JsonProcessingException, SQLException {
		return fondoCustodia.obtenerOpFc();
	}

	@RequestMapping(path = "/obtenerCtaFc", method = RequestMethod.GET)
	public Resultado obtenerCtaFc(
			@RequestParam(name = "jefatura", required = true, defaultValue = "0") String pVariable1,
			@RequestParam(name = "tipo", required = true, defaultValue = "0") Integer pVariable2)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.obtenerCtaFc(pVariable1, pVariable2);
	}

	@PostMapping("/cargarComprobante")
	public Resultado uploadFile2(@RequestParam("file") MultipartFile file) {
		Resultado vObjResultado = new Resultado();

		if (file.isEmpty()) {
			vObjResultado.notificacion = "Cargar archivo";
			vObjResultado.codigoResultado = 400;
		}

		long timestamp = System.currentTimeMillis();
		String ruta = rutaprincipal + "/archivosBackEndMTEPS/FOND_CUSTODIA";
		String nombre = file.getOriginalFilename();
		String extension = FileNameUtils.getExtension(nombre);
		String nuevoNombre = "DOC_FC_" + timestamp + "." + extension;
		Path rutaArchivo = Paths.get(ruta).resolve(nuevoNombre).toAbsolutePath();

		try {
			Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
			vObjResultado.notificacion = "Se cargo correctamente";
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = nuevoNombre;
		} catch (IOException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
		}

		return vObjResultado;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////// GESTIÓN CPT
	@RequestMapping(path = "/registrapagocptbusa/{codigoSeguimiento}", method = RequestMethod.POST)
	public ResponseEntity<?> gestioncptbusa(@RequestBody PagosCpt vObjEntradaDatos,
			@PathVariable String codigoSeguimiento) throws IOException, SQLException {
		System.out.println(vObjEntradaDatos);
		System.out.println(codigoSeguimiento);
		return fondoCustodia.gestioncptbusa(vObjEntradaDatos);
	}

	/*
	 * @RequestMapping(path = "/registrapagocptbusa/{codigoSeguimiento}", method =
	 * RequestMethod.POST) public ResponseEntity<?> gestioncptbusa(@RequestBody
	 * Object vObjEntradaDatos,
	 * 
	 * @PathVariable String codigoSeguimiento) throws IOException, SQLException {
	 * ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	 * String json = ow.writeValueAsString(vObjEntradaDatos);
	 * 
	 * System.out.println(json); return null; }
	 */

	@RequestMapping(path = "/generarcpt", method = RequestMethod.POST)
	public Resultado gestionCptPpePpte(@RequestBody SolicitudPago vObjEntradaDatos) throws IOException, SQLException {
		return fondoCustodia.gestionCptPpePpte(vObjEntradaDatos);
	}

	@RequestMapping(path = "/consultaestadocpt/{codigoTransaccion}", method = RequestMethod.GET)
	public Resultado consultaEstadoCpt(@PathVariable String codigoTransaccion) throws IOException, SQLException {
		return fondoCustodia.consultaEstadoCpt(codigoTransaccion);
	}

	@RequestMapping(path = "/uploadDocumento", method = RequestMethod.POST)
	public Resultado gestionDocumento(@RequestParam("data") String pData,
			@RequestParam(value = "fichero", required = false) MultipartFile file)
			throws IOException, SQLException, ClassNotFoundException {

		ObjectMapper objectMapper = new ObjectMapper();

		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;

		// Deserializar el JSON en un objeto DatosDocumento
		DatosDocumento datosDocumento = objectMapper.readValue(pData, DatosDocumento.class);

		Resultado vObjResultado = new Resultado();
		try {
			if ((datosDocumento.transaccion.equals("ADICIONAR_DOC") || datosDocumento.transaccion.equals("EDITAR_DOC"))
					&& file.getSize() < 1) {

				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Debe cargar documento";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
				return vObjResultado;
			}

			if (datosDocumento.transaccion.equals("ADICIONAR_DOC") || datosDocumento.transaccion.equals("EDITAR_DOC")) {

				long timestamp = System.currentTimeMillis();
				String nombre = file.getOriginalFilename();
				String extension = FileNameUtils.getExtension(nombre);

				datosDocumento.idTipoDocumento = datosDocumento.idTipoDocumento != null ? datosDocumento.idTipoDocumento
						: 169;
				datosDocumento.ruta = rutaprincipal + "/archivosBackEndMTEPS/SGT/FOND_CUSTODIA/DOCUMENTOS/";
				datosDocumento.nombreExtension = "DOC_" + timestamp + "." + extension;
				if (datosDocumento.tablaRelacion==null) {
				switch (datosDocumento.idTipoDocumento) {
				case 170:
					datosDocumento.tablaRelacion = "mteps_tramites.trm_fc_cheques";
					break;

				default:
					datosDocumento.tablaRelacion = "mteps_tramites.trm_fc_ingresos";
					break;
				}}

				datosDocumento.modulo = "TRAMITES";
				vObjResultado = repoNfsCore.gestionDocumento(datosDocumento, file);
				return vObjResultado;

			}
			if (datosDocumento.transaccion.equals("ANULAR_DOC")) {

				connection = null;
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(db_url, db_usuario, db_password);
				query = connection.prepareCall(
						"select * from mteps_nfs.nfs_repositorio_documento nrd where nrd.id_repositorio_documento = ?");

				query.setInt(1, datosDocumento.idRepositorioDocumento);
				resultDatos = query.executeQuery();

				if (!resultDatos.next()) {

					Path rutaArchivo = Paths.get(resultDatos.getString("ruta"))
							.resolve(resultDatos.getString("nombre_extension")).toAbsolutePath();
					if (Files.exists(rutaArchivo)) {
						Files.delete(rutaArchivo);
					}
				}

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(datosDocumento);
				StoredProcedureQuery procedureRepositorio = entityManager
						.createNamedStoredProcedureQuery("mteps_nfs.p_gestion_repositorio_doc");
				procedureRepositorio.setParameter("p_json", json);

				vObjResultado.correcto = (Boolean) procedureRepositorio.getOutputParameterValue("correcto");
				vObjResultado.notificacion = (String) procedureRepositorio.getOutputParameterValue("notificacion");
				vObjResultado.codigoResultado = (Integer) procedureRepositorio
						.getOutputParameterValue("codigoresultado");
				vObjResultado.datoAdicional = procedureRepositorio.getOutputParameterValue("datoadicional");
				return vObjResultado;
			}
		} catch (IOException e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			return vObjResultado;
		} finally {
			try {
				if (query != null)
					query.close();
				if (connection != null)
					connection.close();
				if (resultDatos != null)
					resultDatos.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vObjResultado;

	}

	@RequestMapping(path = "/validarDeposito", method = RequestMethod.GET)
	public Resultado validarDeposito(@RequestParam("tipo") Integer v1, @RequestParam("nroMovimiento") String v2,
			@RequestParam("fechaMovimiento") String v3, @RequestParam("glosa") String v4)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.validarDeposito(v1, v2, v3, v4);
	}

	@RequestMapping(path = "/movimientoCuentaBancaria", method = RequestMethod.POST)
	public Resultado movimientoCuentaBancaria(@RequestParam("data") String pData,
			@RequestParam(value = "fichero", required = false) MultipartFile file)
			throws IOException, SQLException, ClassNotFoundException {
		Resultado vObjResultado = new Resultado();
		ObjectMapper objectMapper = new ObjectMapper();

		MovimientoCuentaBancaria pDataObject = null;

		try {
			pDataObject = objectMapper.readValue(pData, MovimientoCuentaBancaria.class);

			return fondoCustodia.movimientoCuentaBancaria(pDataObject, file);
		} catch (JsonProcessingException e) {
			// TODO: handle exception
			vObjResultado.codigoResultado = 400;
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Los datos de extracto presentan error de deserialización (Un parametro no coincide con el tipo que deberia ser).";
			vObjResultado.datoAdicional = e.getMessage();
		}
		return vObjResultado;
	}

	@RequestMapping(path = "/listaCyt", method = RequestMethod.GET)
	public Resultado listaCyt(@RequestParam("criterio") String v1, @RequestParam("jefatura") String v2)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.listaCyt(v1, v2);
	}

	@RequestMapping(path = "/cuentabancariaFecha", method = RequestMethod.GET)
	public Resultado cuentasbancarias_fecha(
			@RequestParam(name = "fecha", required = true, defaultValue = "0") String pVariable1,
			@RequestParam(name = "jefatura", required = true, defaultValue = "0") String pVariable2)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.cuentasbancariaFecha(pVariable1, pVariable2);

	}

	@RequestMapping(path = "/listaExtractos", method = RequestMethod.GET)
	public Resultado listaExtractos(
			@RequestParam(name = "login", required = false, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.listaExtractos(pVariable1);

	}

	@RequestMapping(path = "/obtenerCuentasBancarias", method = RequestMethod.GET)
	public Resultado obtenerCuentaBancaria(
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException {
		return fondoCustodia.obtenerCuentaBancaria(pVariable1);

	}

	@RequestMapping(path = "/derivarTramiteFC", method = RequestMethod.POST)
	public Resultado gestioncptbusa(@RequestBody DatosDocFC vObjEntradaDatos)
			throws IOException, SQLException, ClassNotFoundException {
		return fondoCustodia.derivarTramiteFC(vObjEntradaDatos);
	}

	
	@GetMapping("/reporteFC")
	public Resultado reporteTramiteExcel(HttpServletRequest datosEntrada) throws JRException, IOException,
			ScriptException, SQLException, ClassNotFoundException, DecoderException, ParseException {
		Resultado vObjResultado = new Resultado();

		try {

			// Crear un ObjectNode para almacenar los valores
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode datos = objectMapper.createObjectNode();

			// Agregar los valores 
			datos.put("estado",datosEntrada.getParameter("estado"));
			datos.put("fechaInicio", datosEntrada.getParameter("fechaInicio"));
			datos.put("fechaFinal", datosEntrada.getParameter("fechaFinal"));
			datos.put("login", datosEntrada.getParameter("login"));
			datos.put("idUnidad", datosEntrada.getParameter("idUnidad"));
			datos.put("idCausal", datosEntrada.getParameter("idCausal"));	
				
				List<ListaFcIngreso> resultado = (List<ListaFcIngreso>) fondoCustodia.listaTramiteFCReporte(datos);
				if (!resultado.isEmpty()){
					
					vObjResultado.correcto = true;
					vObjResultado.notificacion = "La consulta se realizó exitosamente";
					vObjResultado.codigoResultado = 200;
					vObjResultado.datoAdicional = resultado;
					}else {
						vObjResultado.correcto = true;
						vObjResultado.notificacion = "No se encontraron resultados";
						vObjResultado.codigoResultado = 200;
						vObjResultado.datoAdicional = null;
					}
	} catch (Exception e) {
		vObjResultado.correcto = false;
		vObjResultado.notificacion = "No se pudo completar la operación.";
		vObjResultado.codigoResultado = 400;
		vObjResultado.datoAdicional = e.getMessage();
	}
		return vObjResultado;
	
	}
}
	
