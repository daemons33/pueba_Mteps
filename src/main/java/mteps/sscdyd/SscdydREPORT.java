package mteps.sscdyd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sscdyd")
public class SscdydREPORT {
	
	@Value("${ruta.archivos}")
	private String rutaprincipal;
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptionGeneric(Exception exc) throws IOException {
        String errorMessage = "Detalle: " + exc.getMessage();

        // Lógica para mostrar la página de error personalizada
        ClassPathResource resource = new ClassPathResource("static/errorPage.html");
        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        htmlContent = htmlContent.replace("{{errorMessage}}", errorMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }

	@GetMapping("/crtf/{fileName:.+}")
	public ResponseEntity<Object> reporteCertificado(@PathVariable String fileName)
			throws  IOException, ScriptException, SQLException, ClassNotFoundException {		
		
		File file = new File(rutaprincipal + "/archivosBackEndMTEPS/SSCDYD/" + fileName+".pdf");
				
		 // Verifica si el archivo existe
        if (!file.exists() || !file.isFile()) {
            // Manejo de error si el archivo no existe
        	ClassPathResource resource = new ClassPathResource("static/errorPage.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("{{errorMessage}}", "No se encontro archivo.");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);

            return new ResponseEntity<>(htmlContent, headers, HttpStatus.NOT_FOUND);
        
        }

        // Carga el archivo en un InputStreamResource
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        // Construye las cabeceras de respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());

        // Configura el tipo de contenido
        MediaType mediaType = MediaType.APPLICATION_PDF;
        headers.setContentType(mediaType);

        // Construye la respuesta
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);

	}
}
