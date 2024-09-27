package mteps.tramites;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;

@RestController
@RequestMapping("/tramitesSitioWeb")
public class TramiteSitioWebREST {
	
	@Autowired
	tramiteCORE tramites;
	
	@RequestMapping(path = "/calculomulta", method = RequestMethod.POST)
	public ResponseEntity<?> obtenerCalculoMulta(@RequestBody Object vObjEntradaDatos,@RequestHeader(name="Origin", required=false) String origin,HttpServletRequest request ) throws JsonProcessingException {
		
		Resultado resultado = new Resultado();
		 if ("https://www.mintrabajo.gob.bo".equals(origin)) {
			 return new ResponseEntity<>( tramites.calculoMulta(vObjEntradaDatos),HttpStatus.OK);
		 }else {
			 resultado.codigoResultado=400;
			 resultado.correcto=false;
			 resultado.notificacion ="No autorizado";
			 return new ResponseEntity<>( resultado,HttpStatus.UNAUTHORIZED);
		 }
		
	}

}
