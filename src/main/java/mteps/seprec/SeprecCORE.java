package mteps.seprec;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;

@SpringBootApplication
public class SeprecCORE {

	@Value("${seprec.token}")
	private String tokenSeprec;
	
	@Value("${seprec.url_nit}")
	private String urlSeprecNit;

	RestTemplate restTemplate = new RestTemplate();
	public Object obtenerDatosNIT(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenSeprec);

		String url = urlSeprecNit + pVariable1 ;
		ObjectMapper mapper = new ObjectMapper();
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			
			
			return  mapper.treeToValue(datosResultado, Object.class);
			

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
		        
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Ocurrio un error";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = httpClientOrServerExc.getResponseBodyAsString();
			return mapper.readValue(httpClientOrServerExc.getResponseBodyAsString(), Object.class);
		}
		
	}
	
	public Object obtenerDatosRepresentantes(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenSeprec);

		String url = urlSeprecNit + pVariable1 + "/representantes";
		ObjectMapper mapper = new ObjectMapper();
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			
			
			return  mapper.treeToValue(datosResultado, Object.class);
			

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
		        
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Ocurrio un error";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = httpClientOrServerExc.getResponseBodyAsString();
			return mapper.readValue(httpClientOrServerExc.getResponseBodyAsString(), Object.class);
		}
		
	}
}
