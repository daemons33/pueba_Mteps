package mteps.comitemixto;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;

@RestController
@RequestMapping("/comiteMixto")
public class ComiteMixtoREST {
	
	@Autowired
	ComiteMixtoCORE comiteMixtoCORE;
	
	@RequestMapping(path = "/gestion", method = RequestMethod.POST)
	public Resultado gestionMulta(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return comiteMixtoCORE.gestionComiteMixto(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/listar", method = RequestMethod.POST)
	public Resultado listar(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return comiteMixtoCORE.lista(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/enviarCorreo", method = RequestMethod.GET)
	public Resultado enviarCorreo(@RequestParam("id") Integer id)
			throws JsonProcessingException, SQLException {
		return comiteMixtoCORE.enviarCorreo(id);
	}
		   
    
}
