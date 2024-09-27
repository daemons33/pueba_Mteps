package mteps.sistpoa.Servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sun.jersey.spi.inject.Inject;

import mteps.sistpoa.Models.Sigec;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/servicios")
public class ExternoCtl {
    @Value("${token.sigec}")
    private String token_sigec;
    @Value("${url.sigec}")
    private String ruta_sigec;

    @Inject
    RestTemplate restTemplate;

    @GetMapping("/sigecbusca")
    public Sigec getServicioSigec(@RequestParam(value = "hr-cite", defaultValue = "") String hr_cite,
                                  @RequestParam(value = "fecha", defaultValue = "") String fecha) {
        String str_fecha = "";
        Sigec resultado = null;
        System.out.println("SIGEC= "+ hr_cite+"  -" +fecha+"-");
        if (!fecha.equals(""))
        try {
            //Date date = Calendar.getInstance().getTime();
            Date date1=new SimpleDateFormat("dd/mm/yyyy").parse(fecha);
            DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
            str_fecha = dateFormat.format(date1);
            System.out.println("SIGEC=-"+ str_fecha + "-");
        } catch (Exception e) {
            System.out.println("Los valores estan con mal formato");
        }
        String ruta_parametros = ruta_sigec + "?hr-cite=" + hr_cite + "&fecha=" + str_fecha;
        System.out.println(ruta_parametros);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token_sigec);
        HttpEntity<Sigec> entity = new HttpEntity<>(new Sigec(), headers);
        ResponseEntity<Sigec> response = restTemplate.exchange(ruta_parametros, HttpMethod.GET, entity, Sigec.class);
        resultado = response.getBody();
        return resultado;
    }
}
