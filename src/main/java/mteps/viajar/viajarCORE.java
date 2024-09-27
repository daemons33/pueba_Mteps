package mteps.viajar;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import mteps.config.Resultado;
import mteps.viajar.entity.configviajar;

@SpringBootApplication
public class viajarCORE {

	@Value("${dbviajar.url}")
	private String db_url;

	@Value("${dbviajar.usuario}")
	private String db_usuario;

	@Value("${dbviajar.password}")
	private String db_password;

	@PersistenceContext
	private EntityManager entityManager;

///////////////////////////////////////////////
///////////////////////////////////// CONEXION VIAJAR
	public Resultado conexionVIAJAR() throws JsonProcessingException, ClassNotFoundException, SQLException {
//Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();


		try {
			Connection connection = null;
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection
					.prepareCall("select * from cnfConfiguracion");

			configviajar conf = new configviajar();
			ResultSet resultDatos = procedure.executeQuery();
			resultDatos.next();
			conf.setConf_id(resultDatos.getInt("conf_id"));

			vObjResultado.correcto = true;
			vObjResultado.notificacion = "La consulta se realizó exitosamente";
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = conf;
			
	} catch (Exception e) {
		vObjResultado.correcto = false;
		vObjResultado.notificacion = "Error:" + e.getMessage();
		vObjResultado.codigoResultado = 400;
		vObjResultado.datoAdicional = null;
	}


		return vObjResultado;
	}
}
