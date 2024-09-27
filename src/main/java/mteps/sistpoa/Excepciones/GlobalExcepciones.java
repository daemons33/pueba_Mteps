package mteps.sistpoa.Excepciones;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;


@ControllerAdvice
public class GlobalExcepciones {
	
	
	
    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(NoSuchElementException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, exc);
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(DuplicateKeyException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, exc);
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(IllegalArgumentException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, exc);
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(DatosInvalidosExcepciones exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> errors = exc.getResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return buildResponseEntity(httpStatus,
                //new RuntimeException("Data enviada es invalida"), errors);
        new RuntimeException(exc.getMessage()), errors);
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(MethodArgumentTypeMismatchException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus,
                //new RuntimeException("Tipo de Argumento invalido"));
                new RuntimeException(exc.getMessage()));
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(Exception exc) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildResponseEntity(httpStatus,
                //new RuntimeException("Se presento un problema, reporte e intente luego"));
        new RuntimeException(exc.getMessage()));
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(IOException exc) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildResponseEntity(httpStatus,
                //new RuntimeException("Error al generar el archivo"));
        new RuntimeException(exc.getMessage()));
    }

    @ExceptionHandler
    protected ResponseEntity<Mensajes> handleException(DataAccessException exc) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildResponseEntity(httpStatus,
                //new RuntimeException("Error al realizar la consulta en la Base de Datos"));
        new RuntimeException(exc.getMessage()));
    }

    private ResponseEntity<Mensajes> buildResponseEntity(HttpStatus httpStatus, Exception exc) {
        return buildResponseEntity(httpStatus, exc, null);
    }

    private ResponseEntity<Mensajes> buildResponseEntity(HttpStatus httpStatus, Exception exc, List<String> errores) {
        Mensajes error = new Mensajes();
        error.mensaje = "Revisar - " + exc.getMessage();
        // error.setMensaje("El sistema no se encuentra disponible en este momento");
        error.estado = httpStatus.value();
        error.tiempo = LocalDate.now();
        error.errores =errores;
        return new ResponseEntity<>(error, httpStatus);
    }
    

    
	/* otro ejemplo
	  @ExceptionHandler
    public ResponseEntity<String> handleExceptionGeneric(Exception exc) {
        String errorMessage = "Ocurrió un error en la aplicación: " + exc.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body("<html><body><h1>Error</h1><p>" + errorMessage + "</p></body></html>");
    }*/
	
	
		
	/*
	 @ExceptionHandler
	    public ResponseEntity<String> handleExceptionGeneric(Exception exc) throws IOException {
	        String errorMessage = "Detalle: " + exc.getMessage();

	        ClassPathResource resource = new ClassPathResource("static/errorPage.html");
	        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

	        htmlContent = htmlContent.replace("{{errorMessage}}", errorMessage);

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .contentType(MediaType.TEXT_HTML)
	                .body(htmlContent);
	    }*/
}
