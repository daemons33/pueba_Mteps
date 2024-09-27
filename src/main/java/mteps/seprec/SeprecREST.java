package mteps.seprec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/seprec")
public class SeprecREST {

	@Autowired
	SeprecCORE seprecCore;

	@RequestMapping(path = "/matricula", method = RequestMethod.GET)
	public Object ObtenerDatos(
			@RequestParam(name = "nit", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return seprecCore.obtenerDatosNIT(pVariable1);
	}
	
	@RequestMapping(path = "/representante", method = RequestMethod.GET)
	public Object ObtenerDatosRepresentante(
			@RequestParam(name = "nit", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return seprecCore.obtenerDatosRepresentantes(pVariable1);
	}
	
}
