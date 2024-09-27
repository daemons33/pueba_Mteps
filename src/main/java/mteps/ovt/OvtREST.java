package mteps.ovt;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/ovt")
public class OvtREST {
	@Autowired
	OvtCORE ovtCore;

	@RequestMapping(path = "/empresa", method = RequestMethod.GET)
	public Resultado ObtenerDatosNIT(

			@RequestParam(name = "nit", required = false, defaultValue = "") String pVariable1,
			@RequestParam(name = "matricula", required = false, defaultValue = "null") String pVariable2,
			@RequestParam(name = "razon_social", required = false, defaultValue = "null") String pVariable3)
			throws JsonProcessingException, ClassNotFoundException, SQLException {

		return ovtCore.obtenerDatosEmpresaNIT(pVariable1, pVariable2, pVariable3);

	}
	
	@GetMapping("/empresaID")
	public Resultado obtenerDatosEmpresaId(

			@RequestParam(name = "id", required = false, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {

		return ovtCore.obtenerDatosEmpresaId(pVariable1);

	}

	@RequestMapping(path = "/fechafinaldeclaracion", method = RequestMethod.GET)
	public Resultado ObtenerFechaDeclaracion(
			@RequestParam(name = "periodo", required = true, defaultValue = "") String pVariable1,
			@RequestParam(name = "gestion", required = true, defaultValue = "") Integer pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return ovtCore.obtenerFechaDeclaracion(pVariable1, pVariable2);
	}

	@RequestMapping(path = "/sucursal", method = RequestMethod.GET)
	public Resultado ObtenerDatosNIT(
			@RequestParam(name = "idempresa", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return ovtCore.obtenerDatosSucursalesNIT(pVariable1);
	}

	@RequestMapping(path = "/obtenerSucursales", method = RequestMethod.GET)
	public Resultado obtenerSucursales(
			@RequestParam(name = "idEmpresa", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return ovtCore.obtenerSucursales(pVariable1);
	}

	@RequestMapping(path = "/obtenerPersonaFC", method = RequestMethod.GET)
	public Resultado obtenerPersonaFC(@RequestParam(name = "nit", required = true, defaultValue = "") String pVariable1,
			@RequestParam(name = "fechaNacimiento", required = true, defaultValue = "") String pVariable2,
			@RequestParam(name = "nroDoc", required = true, defaultValue = "") String pVariable3)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return ovtCore.obtenerPersonaFC(pVariable1, pVariable2, pVariable3);
	}

	
	@RequestMapping(path = "/firmaDocumentos", method= RequestMethod.GET)
	public Resultado firmaDocumentos()
			throws ClassNotFoundException, SQLException, JSchException, SftpException, IOException {
		return ovtCore.firmaDocumentos();
	}
	
	@RequestMapping(path = "/firmaPgsst", method= RequestMethod.GET)
	public Resultado firmaPgsst()
			throws ClassNotFoundException, SQLException, JSchException, SftpException, IOException {
		return ovtCore.firmaPgsst();
	}
}