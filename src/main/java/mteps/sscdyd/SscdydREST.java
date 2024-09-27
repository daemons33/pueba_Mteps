package mteps.sscdyd;

import java.io.IOException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;

@RestController
@RequestMapping("/sscdyd")
public class SscdydREST {
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Autowired
	ConfigCORE configCore;
	
	@Autowired
	SscdydCORE sscdydCore;

	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(path = "/gestion", method = RequestMethod.POST)
	public Resultado gestionMulta(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return sscdydCore.gestionSscdyd(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/listar", method = RequestMethod.POST)
	public Resultado listar(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return sscdydCore.lista(vObjEntradaDatos);
	}
	
	@RequestMapping(path = "/enviarCorreo", method = RequestMethod.GET)
	public Resultado enviarCorreo(@RequestParam("id") Integer id)
			throws JsonProcessingException, SQLException {
		return sscdydCore.enviarCorreo(id);
	}
	
	@RequestMapping(path = "/firmaDocumento", method = RequestMethod.GET)
	public Resultado firmaDocumento()
			throws JsonProcessingException, SQLException {
		return sscdydCore.firmaDocumento();
	}
	
	@RequestMapping(path = "/firmaMasiva", method = RequestMethod.GET)
	public Resultado firmaMasiva()
			throws SQLException, ClassNotFoundException, IOException, JSchException, SftpException {
		return sscdydCore.firmaMasiva();
	}
}
