package mteps.repo_nfs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repoNfs/reporte")
public class RepoNfsREPORT {
	
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;
	
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
	
	
	// Visualizar en LINEA
	@RequestMapping(path = "/file", method = RequestMethod.GET)
	public void obtenerPDF(@RequestParam(name = "id", required = true, defaultValue = "null") Integer pVariable1, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {
        
		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;
		
		
		
		connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);

		query = connection
				.prepareCall("SELECT * FROM mteps_nfs.nfs_repositorio_documento\r\n"
						+ "WHERE id_repositorio_documento= ?");

		query.setInt(1, pVariable1);
		resultDatos = query.executeQuery();

		connection.close();
		if (resultDatos.next()) {
			
			
       
        Path rutaArchivo = Paths.get(resultDatos.getString("ruta")).resolve(resultDatos.getString("nombre_extension")).toAbsolutePath();
 
        File archivo = rutaArchivo.toFile();

        if (Files.exists(rutaArchivo) && archivo.isFile()) {
            String nombreArchivo = rutaArchivo.getFileName().toString();
            String mimeType = Files.probeContentType(rutaArchivo);

            if (mimeType != null) {
                // Establece el tipo de contenido en función del MIME type
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", "inline; filename=" + nombreArchivo);

                // Lee el contenido del archivo
                byte[] contenidoArchivo = Files.readAllBytes(rutaArchivo);
                response.getOutputStream().write(contenidoArchivo);
            } else {
                // Tipo de archivo desconocido, puedes manejarlo según tus necesidades
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                response.getWriter().println("Tipo de archivo no compatible.");
            }
        } else {
            // El archivo no existe, puedes manejarlo según tus necesidades
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("Archivo no encontrado.");
        }
		
		}
       
    }
	
	@GetMapping("/descargarArchivo")
	public void downloadFileFromLocal(@RequestParam(name = "id", required = true, defaultValue = "null") Integer pVariable1, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException {

		Connection connection = null;
		CallableStatement query = null;
		ResultSet resultDatos = null;
		
		
		
		connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);

		query = connection
				.prepareCall("SELECT * FROM mteps_nfs.nfs_repositorio_documento\r\n"
						+ "WHERE id_repositorio_documento= ?");

		query.setInt(1, pVariable1);
		resultDatos = query.executeQuery();

		connection.close();
		if (resultDatos.next()) {
			
			
       
        Path rutaArchivo = Paths.get(resultDatos.getString("ruta")).resolve(resultDatos.getString("nombre_extension")).toAbsolutePath();
 
        File archivo = rutaArchivo.toFile();

        if (Files.exists(rutaArchivo) && archivo.isFile()) {
            String nombreArchivo = rutaArchivo.getFileName().toString();
            String mimeType = Files.probeContentType(rutaArchivo);

            if (mimeType != null) {
                // Establece el tipo de contenido en función del MIME type
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

                // Lee el contenido del archivo
                byte[] contenidoArchivo = Files.readAllBytes(rutaArchivo);
                response.getOutputStream().write(contenidoArchivo);
            } else {
                // Tipo de archivo desconocido, puedes manejarlo según tus necesidades
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                response.getWriter().println("Tipo de archivo no compatible.");
            }
        } else {
            // El archivo no existe, puedes manejarlo según tus necesidades
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("Archivo no encontrado.");
        }
		
		}
		
		
	}
}


