package mteps.parametro;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/parametros")
public class ParametroREST {

	@Autowired
	ParametroCORE parametros;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER AREA - CATEGORIA - SUB CATEGORIA

	@RequestMapping(path = "/clasificador", method = RequestMethod.GET)
	public Resultado ObtenerCategoria(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return parametros.obtenerClasificador(pVariable1);

	}
	
	@RequestMapping(path = "/clasificadorPorPerfil", method = RequestMethod.GET)
	public Resultado obtenerClasificadorPerfil(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1,
			@RequestParam(name = "login", required = true, defaultValue = "0") String pVariable2)
			throws JsonProcessingException, SQLException {
		return parametros.obtenerClasificadorPerfil(pVariable1,pVariable2);

	}
	
}
