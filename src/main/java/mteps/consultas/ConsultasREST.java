package mteps.consultas;

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

import mteps.config.Resultado;


@RestController
@RequestMapping("/consultas")
public class ConsultasREST {
	
	@Value("${spring.datasource.url}")
	private String db_url;

	@Value("${spring.datasource.username}")
	private String db_usuario;

	@Value("${spring.datasource.password}")
	private String db_password;
	
	@Autowired
	ConsultasCORE consultasCORE;

	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(path = "/listaconsultas", method = RequestMethod.POST)
	public Resultado listaConsultas(@RequestBody Object vObject) throws JsonProcessingException, SQLException {
		return consultasCORE.listaConsultas(vObject);
	}

	@RequestMapping(path = "/gestion", method = RequestMethod.POST)
	public Resultado gestionTtramites(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return consultasCORE.gestionConsultas(vObjEntradaDatos);
	}

	@RequestMapping(path = "/gestionprefiniquito", method = RequestMethod.POST)
	public Resultado gestionPrefiniquito(@RequestBody ObjectNode vObjEntradaDatos)
			throws JsonProcessingException, SQLException {
		return consultasCORE.gestionPrefiniquito(vObjEntradaDatos);
	}

		
	@RequestMapping(path = "/obtenerpreguntas", method = RequestMethod.GET)
	public Resultado gestionPrefiniquito(@RequestParam(name = "id", required = true, defaultValue = "0")  Integer pVar_1)
			throws JsonProcessingException, SQLException {
		return consultasCORE.obtenerPreguntas(pVar_1);
	}
	
	@RequestMapping(path = "/obtenerbancopregunta", method = RequestMethod.GET)
	public Resultado obtenerBancoPregunta(@RequestParam(name = "dato", required = true, defaultValue = "")  String pVar_1)
			throws JsonProcessingException, SQLException {
		return consultasCORE.obtenerBancoPregunta(pVar_1);
	}
	
	@RequestMapping(path = "/obtenerPersona", method = RequestMethod.GET)
	public Resultado obtenerPersona(@RequestParam(name = "numeroDoc", required = true, defaultValue = "")  String pVar_1,
			@RequestParam(name = "tipoDoc", required = true, defaultValue = "")  Integer pVar_2,
			@RequestParam(name = "complemento", required = false)  String pVar_3)
			throws JsonProcessingException, SQLException {
		return consultasCORE.obtenerPersona(pVar_1,pVar_2,pVar_3);
	}

}
