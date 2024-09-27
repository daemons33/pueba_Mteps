package mteps.rrhh;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;
import mteps.rrhh.entity.RequestLogin;

@RestController
@RequestMapping("/rrhh")
public class rrhhREST {

	@Autowired
	rrhhCORE rrhhcore;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public Resultado rrhhLogin(@RequestBody RequestLogin vObject) throws JsonProcessingException, SQLException {
		return rrhhcore.rrhhLogin(vObject);
	}

/////////////////////////////////////////// OBTENER DATOS DE USUARIO

	@RequestMapping(path = "/obtenerdatos", method = RequestMethod.GET)
	public Resultado ObtenerDatos(@RequestParam(name = "usuario", required = true) String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.obtenerDatosUsuario(pVariable1);
	}

/////////////////////////////////////////// OBTENER DATOS DE USUARIO

	@RequestMapping(path = "/obtenerdatosv2", method = RequestMethod.GET)
	public Resultado ObtenerDatosv2(@RequestParam(name = "usuario", required = true) String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.obtenerDatosUsuariov2(pVariable1);
	}
/////////////////////////////////////////// OBTENER UNIDADES ORGANIZACIONALES

	@RequestMapping(path = "/obtenerunidades", method = RequestMethod.GET)
	public Resultado ObtenerUnidadesOrganizacionales()
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.obtenerUnidadOrganizacional();
	}
	
/////////////////////////////////////////// OBTENER UNIDADES ORGANIZACIONALES

@RequestMapping(path = "/obtenerUnidadesOrganizacionales", method = RequestMethod.GET)
public Resultado ObtenerUnidadesOrganizacionales2()
throws JsonProcessingException, SQLException, ClassNotFoundException {
return rrhhcore.UnidadOrganizacional();
}

/////////////////////////////////////////// OBTENER ID DATOS DE USUARIO

	@RequestMapping(path = "/obtenerunidad", method = RequestMethod.GET)
	public Resultado ObtenerUnidad(@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.obtenerDatosUnidad(pVariable1);
	}

/////////////////////////////////////////// OBTENER DEPENDIENTEs

	@RequestMapping(path = "/obtenerdependientes", method = RequestMethod.GET)
	public Resultado ObtenerDependientes(
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.obtenerDependientes(pVariable1);
	}
	
	
	@RequestMapping(path = "/listaPersonal", method = RequestMethod.GET)
	public Resultado listaPersonal()
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return rrhhcore.listaPersonal();
	}
	
}
