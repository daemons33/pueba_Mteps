package mteps.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.config.entity.DBconeccionINFO;
import mteps.config.entity.RequestPassword;



@RestController
@RequestMapping("/config")
public class ConfigREST {

	@Autowired ConfigCORE configuracion;
	
	@RequestMapping(path = "/cambiarpassword", method = RequestMethod.POST)
	public Resultado cambiarPassword(@RequestBody RequestPassword vObject) throws JsonProcessingException {
		return configuracion.cambioPassword(vObject);
	}
	
	
	@RequestMapping(path = "/clasificadorfinanciero", method = RequestMethod.GET)
	public Resultado ObtenerHrFecha(
			@RequestParam(name = "parametro", required = false, defaultValue = "") String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return configuracion.obtenerClasificador(pVariable1);
	}
	
	
	@RequestMapping(path = "/clasificadorfinancieroid", method = RequestMethod.GET)
	public Resultado Obtenerclasificador2(
			@RequestParam(name = "id", required = true, defaultValue = "") Integer pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		return configuracion.obtenerClasificador2(pVariable1);
	}
	
	 @PostMapping("/bkpPostgresql")
	    public ResponseEntity<String> backupPostgreSQL(@RequestBody DBconeccionINFO connectionInfo, HttpServletResponse response) throws IOException {
	        //String backupFileName = "C:\\Users\\MTEPS\\Desktop\\DOCS_RUOS\\backup.sql";
	        String backupFileName = "backup.sql";
	        String jdbcUrl = "jdbc:postgresql://" + connectionInfo.host + ":" + connectionInfo.puerto + "/" + connectionInfo.databaseNombre;

	        try (Connection connection = DriverManager.getConnection(jdbcUrl, connectionInfo.usuario, connectionInfo.password)){
	        	

	            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Program Files\\PostgreSQL\\13\\bin\\pg_dump.exe", "-h", connectionInfo.host, "-p", String.valueOf(connectionInfo.puerto),
	                    "-U", connectionInfo.usuario, "-F", "c", "-b", "-v", "-f", backupFileName,
	                    connectionInfo.databaseNombre);
	            processBuilder.environment().put("PGPASSWORD", connectionInfo.password);

	            Process process = processBuilder.start();

	            int exitCode = process.waitFor();
	            
	            try {
	                exitCode = process.waitFor();
	            } catch (InterruptedException e) {
	                exitCode = process.exitValue();
	            }

	            if (exitCode == 0) {
	              
	                return ResponseEntity.status(HttpStatus.OK).body("Backup creado y descargado correctamente.");
	            } else {
	            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud: Error al crear el respaldo.");
	            }

	        } catch (SQLException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de conexión a la base de datos: " + e.getMessage());
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el respaldo: " + e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud: " + e.getMessage());
	        }
	    }
	 
	 
	   @PostMapping(value = "/bkpPostgresql2", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	    public ResponseEntity<String> backupPostgreSQL(@RequestBody DBconeccionINFO connectionInfo) throws SQLException, IOException, InterruptedException {
	        String jdbcUrl = "jdbc:postgresql://" + connectionInfo.host+ ":" + connectionInfo.puerto + "/" + connectionInfo.databaseNombre;
	        String backupFileName = "backup.sql";
	        Connection connection = DriverManager.getConnection(jdbcUrl, connectionInfo.usuario, connectionInfo.password);

	            String pgDumpCommand = "C:\\Program Files\\PostgreSQL\\13\\bin\\pg_dump.exe -h " + connectionInfo.host + " -p " + connectionInfo.puerto +
	                    " -U " + connectionInfo.usuario + " -F c -b -v -f " + backupFileName +
	                    " " + connectionInfo.databaseNombre;
	            String[] cmd = { "cmd", "/c", pgDumpCommand };

	            Process process = Runtime.getRuntime().exec(cmd);
	            int exitCode = process.waitFor();
	            System.out.println(exitCode);
	            if (exitCode == 0) {
	            	
	                return ResponseEntity.ok("Backup creado correctamente.");
	            } else {
	                return ResponseEntity.badRequest().body("Error en la solicitud: Error al crear el respaldo.");
	            }
	        
	    }
	   
	   @GetMapping(value = "/bkp3")
	   public Resultado realizarBackup() throws IOException, InterruptedException {
	       
	        Resultado vObjResultado = new Resultado();
			
	        String nombreArchivo ="backup_s.sql";

			try {
				
				String comando = "pg_dump -h 186.121.201.148 -p 5432 -U mteps -d desa -F c -b -v -f " + nombreArchivo;

		        // Ejecutar el comando
		        ProcessBuilder processBuilder = new ProcessBuilder();
		        processBuilder.environment().put("PGPASSWORD", "0M1nTr4202103$");
		        processBuilder.command("cmd", "/c", comando);
		        
		     // Redirigir la salida y el error a archivos de registro
		        File logFile = new File("C:\\log.txt");
		        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
		        processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(logFile));
		        
		        // Capturar la salida del proceso
		        Process process = processBuilder.start();
		        int exitCode = process.waitFor();
		        
		     // Después de ejecutar el proceso
		        InputStream errorStream = process.getErrorStream();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
		        String line;
		        while ((line = reader.readLine()) != null) {
		            System.err.println(line);
		        }

		        if (exitCode == 0) {
		            // Mover el archivo a la ruta local especificada
		            Path origen = Paths.get(nombreArchivo);
		            Path destino = Paths.get("C:\\Users\\MTEPS\\Desktop\\DOCS_BACKEND\\", nombreArchivo);

		            Files.move(origen, destino);
		        	vObjResultado.notificacion="Backup completado con éxito.";
		        } else {
		        	vObjResultado.notificacion="Error al realizar el backup. Código de salida: " + exitCode;
		        }
				
				vObjResultado.correcto = true;
				 
				vObjResultado.codigoResultado = 200;
				vObjResultado.datoAdicional = null;
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Ocurrio un error";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = "Error:" + e.getMessage();
			}
			return vObjResultado;
	    }
}
