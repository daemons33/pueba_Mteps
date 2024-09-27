package mteps.impuestosnacionales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mteps.config.Resultado;
import mteps.config.security.jwt.JwtUtils;
import mteps.impuestosnacionales.entity.VObjJsonEntrada;
import mteps.rrhh.entity.RespuestaLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SinCORE {
    @Value("${sintoken}")
    private String tokenSIN;

    // Url fundempresa matricula
    @Value("${sin_url_status}")
    private String statusSIN;

    // Url fundempresa matricula
    @Value("${sin_url_login}")
    private String loginSIN;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;
    RestTemplate restTemplate = new RestTemplate();
    public Resultado validaEstadoServicioSIN() throws JsonProcessingException {
        Resultado vObjResultado = new Resultado();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + tokenSIN);

        try {
            ResponseEntity<ObjectNode> response = restTemplate.exchange(statusSIN, HttpMethod.GET,
                    new HttpEntity<>("parameters", headers), ObjectNode.class);
            

            if(response.getStatusCode() == HttpStatus.OK) {
            	ObjectNode datosResultado = response.getBody();
                vObjResultado.notificacion = datosResultado.get("estado").asText();
                vObjResultado.correcto = true;
                vObjResultado.codigoResultado = 200;
            }else
            {
                vObjResultado.notificacion = "El servicio de IMPUESTOS NACIONALES no se encuentra disponible";
                vObjResultado.correcto = false;
                vObjResultado.codigoResultado = 503;
            }

            vObjResultado.datoAdicional = "";

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            // return null;
            String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
            JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
            String vResponseMensage = jsonObject.get("mensaje").getAsString();

            vObjResultado.correcto = false;
            vObjResultado.notificacion = "No se pudo concluir la operación";
            vObjResultado.codigoResultado = 400;
            vObjResultado.datoAdicional = vResponseMensage;
        }

        return vObjResultado;
    }

    public Resultado loginSIN(ObjectNode pData) throws JsonProcessingException {
        Resultado vObjResultado = new Resultado();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + tokenSIN);

        HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(pData, headers);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("mcalzada", "MTEPS21$"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        try {
            ResponseEntity<ObjectNode> response = restTemplate.exchange(loginSIN, HttpMethod.POST,
                    requestEntity, ObjectNode.class);

                ObjectNode res = response.getBody();
                RespuestaLogin resp1 = new RespuestaLogin();
                if(res.get("Autenticado").asBoolean()){
               // if(true){
                    resp1.usuario = pData.get("nit").asText();
                    resp1.token = jwt;
                    vObjResultado.datoAdicional = resp1;
                    vObjResultado.notificacion = "La autenticación se realizó con exito";
                    vObjResultado.codigoResultado = 200;
                }else{
                    vObjResultado.notificacion = res.get("Mensaje").asText();
                    vObjResultado.codigoResultado = 400;
                }
//                vObjResultado.correcto = res.get("Autenticado").asBoolean();
                vObjResultado.correcto = true;
                

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
            JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
            String vResponseMensage = jsonObject.get("message").getAsString();

            vObjResultado.correcto = false;
            vObjResultado.notificacion = vResponseMensage;
            vObjResultado.codigoResultado = 400;
            vObjResultado.datoAdicional = "No se pudo concluir la operación";
        }

        return vObjResultado;
    }
}