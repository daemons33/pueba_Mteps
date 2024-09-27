package mteps.firmadigital;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.ConfigCORE;
import mteps.config.Resultado;
import mteps.config.entity.ObtenerArchivoRemotoResponse;
import mteps.sigec.entity.DatosHr;

@RestController
@RequestMapping("/firmaDigital")
public class FirmaDigitalREST {
	
	@Autowired
	FirmaDigitalCORE firmaCore;
	
	@Autowired
	ConfigCORE configCore;
	

	/*@RequestMapping(path = "/datosToken", method = RequestMethod.GET)
	public Object datosToken(@RequestParam(name = "slot", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return firmaCore.obtieneDatosToken(pVariable1);
	}
	
	@RequestMapping(path = "/firmarPDF", method = RequestMethod.GET)
	public Object firmarPDF(@RequestParam(name = "slot", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return firmaCore.firmaDocumento(pVariable1);
	}*/
	

	@GetMapping("/get")
    public ResponseEntity<byte[]> getRemotePdf(
            @RequestParam String host,
            @RequestParam int port,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String remoteFilePath) {

    	ObtenerArchivoRemotoResponse pdfContent = configCore.getRemotePdf(host, port, username, password, remoteFilePath);

        if (pdfContent.archivo != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent.archivo);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Error al obtener el documento PDF desde el servidor remoto.".getBytes(StandardCharsets.UTF_8));
        }
    }
}
