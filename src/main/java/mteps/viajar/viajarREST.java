package mteps.viajar;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/viajar")
public class viajarREST {
	@Autowired
	viajarCORE viajarCore;

	@PersistenceContext
	private EntityManager entityManager;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// OBTENER TRAMITE

	@RequestMapping(path = "/conexion", method = RequestMethod.GET)
	public Resultado conexion()
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return viajarCore.conexionVIAJAR();
	}

}
