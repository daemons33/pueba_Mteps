package mteps.workflow;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/workflow")
public class WorkflowREST {
	
	@Autowired
	WorkflowCORE workflowCore;
	
	@RequestMapping(path = "/gestionWorkflow", method = RequestMethod.POST)
	public Resultado gestionPlan(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return workflowCore.gestionWorflow(vObject);
	}
	
	@RequestMapping(path = "/listaRolesPerfil", method= RequestMethod.GET)
	public Resultado listaRolesPerfil(
			
			 @RequestParam(name = "modulo", required = false,defaultValue = "")  String pVariable1,
			 @RequestParam(name = "perfil", required = false,defaultValue = "") String pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		

		return workflowCore.listaRolesPerfil(pVariable1,pVariable2);
		
	}
	
	@RequestMapping(path = "/obtenerListaPerfiles", method= RequestMethod.GET)
	public Resultado obtenerListaPerfiles(
			
			 @RequestParam(name = "modulo", required = false,defaultValue = "")  String pVariable1)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		

		return workflowCore.obtenerListaPerfiles(pVariable1);
		
	}
	
	@RequestMapping(path = "/obtenerListaMenuPorUsuario", method= RequestMethod.GET)
	public Resultado obtenerListaMenuPorUsuario(
			
			 @RequestParam(name = "login", required = false,defaultValue = "")  String pVariable1,
			 @RequestParam(name = "modulo", required = false,defaultValue = "") String pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		

		return workflowCore.obtenerListaMenuPorUsuario(pVariable1,pVariable2);
		
	}
	
	@RequestMapping(path = "/obtenerListaMenuPorUsuarioSGT", method= RequestMethod.GET)
	public Resultado obtenerListaMenuPorUsuarioSGT(
			
			 @RequestParam(name = "login", required = false,defaultValue = "")  String pVariable1,
			 @RequestParam(name = "modulo", required = false,defaultValue = "") String pVariable2)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		

		return workflowCore.obtenerListaMenuPorUsuarioSGT(pVariable1,pVariable2);
		
	}
	
}
