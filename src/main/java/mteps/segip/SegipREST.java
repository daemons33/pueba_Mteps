package mteps.segip;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.Resultado;
import mteps.segip.entity.ContrastacionPersona;
import mteps.segip.entity.datoPersonaCertificacion;
import mteps.segip.servicioexterno.ConsultaDatoPersonaCertificacionResponse;


@RestController
@RequestMapping("/segip")
public class SegipREST {
	    
		@Autowired
		SegipCORE client;
		
		@RequestMapping(path="/cosultapersona", method = RequestMethod.POST)
		public Resultado invokeSoap(@RequestBody datoPersonaCertificacion vObjDatosPersona)throws JsonProcessingException {

			return  client.certificaPersona(vObjDatosPersona);
		}
	
		@RequestMapping(path="/contrastacionPersona", method = RequestMethod.POST)
		public Resultado contrastacionSoap(@RequestBody ContrastacionPersona vObjDatosPersona)throws JsonProcessingException {

			return  client.contrastacionPersona(vObjDatosPersona);
		}
	
	
}
