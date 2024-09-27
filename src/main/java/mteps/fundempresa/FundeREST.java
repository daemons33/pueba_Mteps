package mteps.fundempresa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/fundempresa")
public class FundeREST {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	FundeCORE fundemcore;

	@RequestMapping(path = "/matricula", method = RequestMethod.GET)
	public Resultado ObtenerDatosMatricula(
			@RequestParam(name = "matricula", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return fundemcore.obtenerDatosEmpresaFundempresa(pVariable1);
	}
	
	@RequestMapping(path = "/nit", method = RequestMethod.GET)
	public Resultado ObtenerDatosNit(
			@RequestParam(name = "nit", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return fundemcore.obtenerDatosEmpresaFundempresaNIT(pVariable1);
	}
	
	@RequestMapping(path = "/representante", method = RequestMethod.GET)
	public Resultado ObtenerDatosRepresentante(
			@RequestParam(name = "matricula", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException {
		return fundemcore.obtenerDatosEmpresaFundempresaREPRESENTANTES(pVariable1);
	}
}
