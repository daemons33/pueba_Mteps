package mteps.fundempresa;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mteps.config.Resultado;
@SpringBootApplication
public class FundeCORE {
	// Token Fundempresa
	@Value("${seprec.token}")
	private String tokenFunde;

	// Url fundempresa matricula
	@Value("${seprec.url_nit}")
	private String urlFunde_matricula;
	
	// Url fundempresa matricula
	@Value("${seprec.url_nit}")
	private String urlFunde_nit;
	
	@PersistenceContext
	private EntityManager entityManager;

	RestTemplate restTemplate = new RestTemplate();
	public Resultado obtenerDatosEmpresaFundempresa(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenFunde);

		String url = "";

		url = urlFunde_matricula + pVariable1;
		
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			Object consultaEmpresaFundem = datosResultado.get("detalle");
			Object respuestaError = datosResultado.get("error").asText();
			
			if(respuestaError.equals("0000")) {
				vObjResultado.datoAdicional = consultaEmpresaFundem;
				}else
				{
					vObjResultado.datoAdicional = "Error en matricula: " + datosResultado.get("texto").asText(); ;
				}
				

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	public Resultado obtenerDatosEmpresaFundempresaNIT(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenFunde);

		String url = "";

		url = urlFunde_nit + pVariable1+"/matriculas";
		
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			Object consultaEmpresaFundem = datosResultado.get("detalle");
			Object respuestaError = datosResultado.get("error").asText();
			
			if(respuestaError.equals("0000")) {
				vObjResultado.datoAdicional = consultaEmpresaFundem;
				}else
				{
					vObjResultado.datoAdicional = "Error en nit: " + datosResultado.get("texto").asText(); ;
				}
				

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;


			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Ocurrio un error";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
	
	
	public Resultado obtenerDatosEmpresaFundempresaREPRESENTANTES(String pVariable1) throws JsonProcessingException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + tokenFunde);

		String url = "";

		url = urlFunde_matricula + pVariable1 + "/representantes" ;
		
		try {
			ResponseEntity<ObjectNode> responce = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<>("parameters", headers), ObjectNode.class);
			ObjectNode datosResultado = responce.getBody();
			Object consultaEmpresaFundem = datosResultado.get("detalle");
			Object respuestaError = datosResultado.get("error").asText();
			
			if(respuestaError.equals("0000")) {
				vObjResultado.datoAdicional = consultaEmpresaFundem;
				}else
				{
					vObjResultado.datoAdicional = "Error en matricula: " + datosResultado.get("texto").asText(); ;
				}
				

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}
	
}
