package mteps.repo_nfs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.Resultado;
import mteps.config.entity.DatosDocumento;

@SpringBootApplication
public class RepoNfsCORE {

	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	 // GESTION DOCUMENTO
	 
	 public Resultado gestionDocumento(DatosDocumento pData, MultipartFile file) throws IOException, SQLException {
			Resultado vObjResultado = new Resultado();
			
			
			try {
				
				if(pData.transaccion.equals("ADICIONAR_DOC") ||pData.transaccion.equals("EDITAR_DOC")) {
						Path rutaArchivo = Paths.get(pData.ruta).resolve(pData.nombreExtension).toAbsolutePath();
						Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
						} 
				
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
               String json = ow.writeValueAsString(pData);
				StoredProcedureQuery procedureRepositorio = entityManager
						.createNamedStoredProcedureQuery("mteps_nfs.p_gestion_repositorio_doc");
				procedureRepositorio.setParameter("p_json", json);
									
				vObjResultado.correcto = (Boolean) procedureRepositorio.getOutputParameterValue("correcto");
				vObjResultado.notificacion = (String) procedureRepositorio.getOutputParameterValue("notificacion");
				vObjResultado.codigoResultado = (Integer) procedureRepositorio.getOutputParameterValue("codigoresultado");
				vObjResultado.datoAdicional = procedureRepositorio.getOutputParameterValue("datoadicional");
				
				if(pData.transaccion.equals("ANULAR_DOC") && vObjResultado.correcto.equals(true)) {
					
					Connection connection = DriverManager.getConnection(db_url, db_usuario, db_password);
					CallableStatement procedure = connection.prepareCall("select * from mteps_nfs.nfs_repositorio_documento nrd where nrd.id_repositorio_documento = ?");
					procedure.setInt(1, pData.idRepositorioDocumento);
					ResultSet resultDatos = procedure.executeQuery();

					if(resultDatos.next()) {
					
					Path rutaArchivo = Paths.get( resultDatos.getString("ruta") ).resolve(resultDatos.getString("nombre_extension")).toAbsolutePath();
					
					Files.delete(rutaArchivo);	
					
					}
					connection.close();
				}
				
			} catch (Exception e) {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "Ocurrio un error: "+ e.getMessage();
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = null;
			}
			return vObjResultado;
		}
}
