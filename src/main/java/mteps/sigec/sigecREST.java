package mteps.sigec;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;
import mteps.sigec.entity.DatosDoc;
import mteps.sigec.entity.DatosHr;
import mteps.sigec.entity.SeguimientoDocumentos;
import mteps.tramites.entity.FSeguimientoTramite;

@RestController
@RequestMapping("/sigec")
public class sigecREST {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	sigecCORE sigecCore;

/////////////////////////////////////////// CONSULTA SIGEC

	@RequestMapping(path = "/buscar", method = RequestMethod.GET)
	public Resultado ObtenerHr(@RequestParam(name = "hojaRuta", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.buscarHr(pVariable1);
	}
/////////////////////////////////////////// CONSULTA SIGEC

	@RequestMapping(path = "/buscarhr", method = RequestMethod.GET)
	public Resultado ObtenerHrFecha(
			@RequestParam(name = "hr-cite", required = true, defaultValue = "") String pVariable1,
			@RequestParam(name = "fecha", required = true, defaultValue = "") String pVariable2)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.buscarHrFecha(pVariable1, pVariable2);
	}

/////////////////////////////////////////// CONSULTA SIGEC ID

	@RequestMapping(path = "/buscarhrid", method = RequestMethod.GET)
	public Resultado ObtenerHrId(@RequestParam(name = "id", required = true, defaultValue = "0") Integer pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.buscarHrId(pVariable1);
	}

/////////////////////////////////////////// CONSULTA SIGEC ID

	@RequestMapping(path = "/usuariossigec", method = RequestMethod.GET)
	public Resultado obtenerUsuariosSigec() throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.obtenerUsuariosSigec();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////Nueva HR - TRAMITES

	@RequestMapping(path = "/nuevahr", method = RequestMethod.POST)
	public Resultado nuevaHojaRuta(@RequestBody DatosHr vObjEntradaDatos)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return sigecCore.nuevaHr(vObjEntradaDatos);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////NuevaCITE - çDUNUNCIAS

	@RequestMapping(path = "/buscarcite", method = RequestMethod.GET)
	public Resultado BuscarCite(@RequestParam(name = "cite", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.infoCite(pVariable1);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////Nueva HR - TRAMITES

	@RequestMapping(path = "/generaDoc", method = RequestMethod.POST)
	public Resultado generaDoc(@RequestBody DatosDoc vObjEntradaDatos)
			throws JsonProcessingException, ClassNotFoundException, SQLException {
		return sigecCore.generarDocumentoSIGECv2(vObjEntradaDatos);
	}

	@RequestMapping(path = "/registrarSeguimientos", method = RequestMethod.POST)
	public Resultado registrarSeguimientos(@RequestBody List<SeguimientoDocumentos> vObjEntradaDatos)
			throws SQLException, ClassNotFoundException {
		return sigecCore.registrarSeguimientos(vObjEntradaDatos);
	}

/////////////////////////////////////////// CONSULTA SIGEC ID

	@RequestMapping(path = "/obtenerBandejasPorUsuario", method = RequestMethod.GET)
	public Resultado obtenerBandejasUsuario(@RequestParam(name = "login", required = true, defaultValue = "") String pVariable1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.obtenerBandejasUsuario(pVariable1);
	}
	
	
	@RequestMapping(path = "/seguimiento", method = RequestMethod.GET)
	public Resultado seguimientoHr(@RequestParam(name = "hr", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.seguimientoHr(pVariable1);
		 		
	}

	@RequestMapping(path = "/documentos", method = RequestMethod.GET)
	public Resultado documentos(@RequestParam(name = "hr", required = true, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return sigecCore.obtenerDocumentesHR(pVariable1);
		 		
	}
}
