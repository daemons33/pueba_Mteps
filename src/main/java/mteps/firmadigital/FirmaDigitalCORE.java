package mteps.firmadigital;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.*;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import mteps.config.ConfigCORE;
import mteps.firmadigital.entity.DatosTokenResponse;
import mteps.firmadigital.entity.FirmarPdfResponse;
import mteps.firmadigital.entity.ValidarFirmaResponse;

@SpringBootApplication
public class FirmaDigitalCORE {
	
	public String urlToken = "https://localhost:9000";
	public Integer slot = 1;
	//public String pdfSavePath = "C:\\Users\\MTEPS\\Desktop\\DOCS_BACKEND\\ovt\\";
	public String pdfSavePath = "/home/mteps/prueba/";
	
	@Autowired
	ConfigCORE configCore;

	public Object obtieneDatosToken(Integer slot,String pin) {
		// Define la URL del servicio
		String url = urlToken + "/api/token/data";

		// Crea el cuerpo de la solicitud en formato JSON
		String requestBody = "{\"slot\": " + slot + ", \"pin\": \"" + pin + "\"}";

		// Crea los encabezados de la solicitud
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		// Realiza la solicitud POST
		try {

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new NoopX509TrustManager() }, new java.security.SecureRandom());

			// Crea un HttpClient con el SSLContext personalizado
			HttpClient httpClient = HttpClient.newBuilder().sslContext(sslContext).build();

			// Crea una solicitud HTTP POST con la URL proporcionada
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();

			// Agrega encabezados adicionales a la solicitud
			// headers.forEach(request::header);

			// Envía la solicitud y obtiene la respuesta
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			String responseBody = response.body();

			// Deserializa la respuesta JSON en un objeto TokenResponse
			ObjectMapper objectMapper = new ObjectMapper();
			DatosTokenResponse tokenResponse = objectMapper.readValue(responseBody, DatosTokenResponse.class);

			return tokenResponse;

		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private static class NoopX509TrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
			// Noop - No realiza ninguna verificación
		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
			// Noop - No realiza ninguna verificación
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	
	
public FirmarPdfResponse firmaDocumento(Integer slot,String pin, String pdfBase64,String alias) {
		
		String url = urlToken + "/api/token/firmar_pdf";
		try {
			

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new NoopX509TrustManager() }, new java.security.SecureRandom());

				

				String requestBody = "{\"alias\": " + alias + ", \"pin\": \"" + pin + "\", \"slot\": " 
				+ slot + ", \"pdf\":\""+ pdfBase64+"\"}";
				// Crea una solicitud HTTP POST con la URL proporcionada
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
						.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody))
						.build();

				// Agrega encabezados adicionales a la solicitud
				// headers.forEach(request::header);

				// Envía la solicitud y obtiene la respuesta
				HttpClient httpClient = HttpClient.newBuilder().sslContext(sslContext).build();
				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

				String responseBody = response.body();
				
				System.out.println(responseBody);
				
				// Deserializa la respuesta JSON en un objeto TokenResponse
				ObjectMapper objectMapper = new ObjectMapper();
				FirmarPdfResponse responseEntity = objectMapper.readValue(responseBody, FirmarPdfResponse.class);

				// Obtiene la respuesta del servicio				

			return responseEntity;
			
		} catch (Exception e) {
			//return e.getMessage();
			return null;
		}
	}
	
	public ValidarFirmaResponse validarPdf( String pdfBase64) {
			
			String url = urlToken + "/api/validar_pdf";
			
				/*
					//desactirvar ssl
					 TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			                public X509Certificate[] getAcceptedIssuers() {
			                    return null;
			                }
	
			                public void checkClientTrusted(X509Certificate[] certs, String authType) {
			                }
	
			                public void checkServerTrusted(X509Certificate[] certs, String authType) {
			                }
			            }};
	
			            SSLContext sslContext = SSLContext.getInstance("SSL");
			            sslContext.init(null, trustAllCerts, new SecureRandom());
			            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

					*/
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
	
					// Configura el cuerpo de la solicitud
					Map<String, Object> requestMap = new HashMap<>();
					requestMap.put("pdf", pdfBase64);
	
					HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);
	
					// Realiza la llamada al servicio
					ResponseEntity<ValidarFirmaResponse> responseEntity = new RestTemplate().postForEntity(url, requestEntity,
							ValidarFirmaResponse.class);
	
				return responseEntity.getBody();
				
			
		}

	public void guardarPdfServer(String pdfBase64, String fileName) {
		try {
			byte[] decodedBytes = Base64.decodeBase64(pdfBase64);

			// Guarda el PDF en una ubicación específica en el servidor
			String filePath = fileName;
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				fos.write(decodedBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
			// Manejo de errores
		}
	}

	
	

}
