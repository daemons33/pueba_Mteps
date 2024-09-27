package mteps.rtep;

import java.io.IOException;
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
import mteps.parametro.ParametroCORE;

@RestController
@RequestMapping("/rtep")
public class RtepREST {
	
	@Autowired
	private RtepCORE rtepCore;
	
	@Autowired
	ParametroCORE parametros;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER AREA - CATEGORIA - SUB CATEGORIA

	@RequestMapping(path = "/auth/clasificador", method = RequestMethod.GET)
	public Resultado ObtenerCategoria(
			@RequestParam(name = "idclasificador", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException {
		return parametros.obtenerClasificador(pVariable1);

	}
	
    @RequestMapping(path = "/auth/ci/login", method = RequestMethod.POST)
    public Resultado authCI(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
    	vObjEntradaDatos.put("ciudadaniaDigital", false);
        return rtepCore.authCI(vObjEntradaDatos);
    }
    
    @RequestMapping(path = "/auth/cd/login", method = RequestMethod.POST)
    public Resultado authCd(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
    	vObjEntradaDatos.put("ciudadaniaDigital", true);
    	
        return rtepCore.authCI(vObjEntradaDatos);
    }
    
    @RequestMapping(path = "/auth/registro", method = RequestMethod.POST)
    public Resultado registro(@RequestBody ObjectNode vObjEntradaDatos)
    		
            throws JsonProcessingException, SQLException {
    		   	
    	
        return rtepCore.gestionRegistro(vObjEntradaDatos);
    }
    
    @RequestMapping(path = "/auth/validar", method = RequestMethod.GET)
	public Resultado ObtenerCategoria(
			@RequestParam(name = "codigo", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException {
		return rtepCore.verificarCodigo(pVariable1);

	}
    
    @RequestMapping(path = "/auth/actualizarClave", method = RequestMethod.POST)
    public Resultado crearClave(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return rtepCore.crearClave(vObjEntradaDatos);
    }
    
    @RequestMapping(path = "/auth/olvideClave", method = RequestMethod.POST)
    public Resultado olvideClave(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return rtepCore.olvideClave(vObjEntradaDatos);
    }
    
    @RequestMapping(path = "/gestionUsuario", method = RequestMethod.POST)
    public Resultado gestionUsuario(@RequestBody ObjectNode vObjEntradaDatos)
    		
            throws JsonProcessingException, SQLException {
    		   	
    	
        return rtepCore.gestionUsuario(vObjEntradaDatos);
    }
    

	@RequestMapping(path = "/obtenerPersona", method = RequestMethod.GET)
	public Resultado ObtenerCategoria(
			@RequestParam(name = "idUsuario", required = true, defaultValue = "0") Long pVariable1)
			throws JsonProcessingException, SQLException {
		return rtepCore.obtenerPersona(pVariable1);

	}
	
	@RequestMapping(path = "/obtenerFormulario", method = RequestMethod.GET)
	public Resultado obtenerFormulario()
			throws SQLException, IOException {
		return rtepCore.obtenerFormulario();

	}
	
	@RequestMapping(path = "/gestionFormulario", method = RequestMethod.POST)
    public Resultado gestionFormulario(@RequestBody ObjectNode vObjEntradaDatos)
    		
            throws JsonProcessingException, SQLException {
    		   	
    	
        return rtepCore.gestionFormulario(vObjEntradaDatos);
    }
	
	@RequestMapping(path = "/obtenerListaFormularios", method = RequestMethod.GET)
	public Resultado listaFormularios(@RequestParam("idUsuario") Integer idUsuario)
			throws SQLException, IOException {
		return rtepCore.obtenerListaFormularios(idUsuario);

	}
	
	@RequestMapping(path = "/obtenerUsuarioFormulario", method = RequestMethod.GET)
	public Resultado obtenerUsuarioFormulario(
			@RequestParam(name = "idFormulario", required = true, defaultValue = "0") Integer pVariable2)
			throws SQLException, IOException {
		return rtepCore.obtenerFormularioUsuario(pVariable2);

	}
	
	@RequestMapping(path = "/actualizarClave", method = RequestMethod.POST)
    public Resultado actualizarClave(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return rtepCore.actualizarClave(vObjEntradaDatos);
    }

}
