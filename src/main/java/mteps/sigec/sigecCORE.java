package mteps.sigec;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mteps.config.Resultado;
import mteps.inspectoria.entity.EntDocumentoSigec;
import mteps.sigec.entity.BandejasHR;
import mteps.sigec.entity.DatosDoc;
import mteps.sigec.entity.DatosHr;
import mteps.sigec.entity.DocumentosHr;
import mteps.sigec.entity.InfoCite;
import mteps.sigec.entity.SalidaGeneraDoc;
import mteps.sigec.entity.SeguimientoDocumentos;
import mteps.sigec.entity.respuestaFuncion;
import mteps.sigec.entity.usuarioSigec;
import mteps.tramites.entity.FSeguimientoTramite;
import mteps.tramites.fondoCustodia.entity.DatosDocFC;

@SpringBootApplication
public class sigecCORE {
	
		@Value("${dbmq.url}")
		private String db_url;
	
		@Value("${dbmq.usuario}")
		private String db_usuario;
	
		@Value("${dbmq.password}")
		private String db_password;
		
		@Value("${esquema.sigec}")
		private String esquemaSigec;
		
		 @Autowired
		  private JdbcTemplate jdbcTemplate;
	 
		@PersistenceContext
		private EntityManager entityManager;
		public Resultado buscarHr(String pVariable1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".obtener_hr(?)");
	
		procedure.setString(1, pVariable1);
		procedure.executeUpdate();
		
		ResultSet resultDatos = procedure.getResultSet();
		List<respuestaFuncion> hrs=new ArrayList<respuestaFuncion>();

		while(resultDatos.next()) {
			respuestaFuncion hr = new respuestaFuncion(); 
			hr.setId(resultDatos.getInt("id"));
			hr.setHojaRuta(resultDatos.getString("nur"));
			
		  hrs.add(hr);
		}
		if(!hrs.isEmpty()) {
				vObjResultado.notificacion = "Se realizó correctamente la consulta";
		}else {
			vObjResultado.notificacion = "No se encontraron resultados";
		}
		
		vObjResultado.correcto = true;
		
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = hrs;
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
			
		}
		
	return vObjResultado;
	}

	
	
	
	public Resultado buscarHrFecha(String pVariable1,String pVariable2) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".obtener_hr_cite(?,?,?,?)");
	
		procedure.setString(1, pVariable1);
		procedure.setString(2, pVariable2);
		procedure.registerOutParameter(3,Types.VARCHAR);
		procedure.registerOutParameter(4,Types.INTEGER);
		procedure.executeUpdate();
		
		vObjResultado.notificacion = (String) procedure.getObject(3);
		
		vObjResultado.correcto = true;
		
		vObjResultado.codigoResultado =(Integer) procedure.getObject(4);
		vObjResultado.datoAdicional =  "";
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}
	
	public Resultado buscarHrId(Integer pVariable1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("select * from documentos d where d.id=?");
	
		procedure.setInt(1, pVariable1);
		procedure.executeQuery();
		
		ResultSet resultDatos = procedure.getResultSet();
		List<respuestaFuncion> hrs=new ArrayList<respuestaFuncion>();

		while(resultDatos.next()) {
			respuestaFuncion hr = new respuestaFuncion(); 
			hr.setId(resultDatos.getInt("id"));
			hr.setHojaRuta(resultDatos.getString("nur"));
			
			
		  hrs.add(hr);
		}
		if(!hrs.isEmpty()) {
				vObjResultado.notificacion = "Se realizó correctamente la consulta";
		}else {
			vObjResultado.notificacion = "No se encontraron resultados";
		}
		
		vObjResultado.correcto = true;
		
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = hrs;
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}
	
	public Resultado obtenerUsuariosSigec() throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		//cambiar sigec_final para produc
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".obtener_usuarios()");
			
		procedure.executeUpdate();
		
		ResultSet resultDatos = procedure.getResultSet();
		List<usuarioSigec> usuariosSigec=new ArrayList<usuarioSigec>();

		while(resultDatos.next()) {
			usuarioSigec usuarioSigec = new usuarioSigec(); 
			
			usuarioSigec.setId(resultDatos.getInt("id")); 
			usuarioSigec.setSuperior(resultDatos.getInt("superior"));
			usuarioSigec.setIdOficina(resultDatos.getInt("id_oficina"));
			usuarioSigec.setDependencia(resultDatos.getInt("dependencia"));
		usuarioSigec.setUsername(resultDatos.getString("username"));
		usuarioSigec.setPassword(resultDatos.getString("password")); 
		usuarioSigec.setNombre(resultDatos.getString("nombre"));
		usuarioSigec.setLastLogin(resultDatos.getInt("last_login"));
		usuarioSigec.setMosca(resultDatos.getString("mosca"));
		usuarioSigec.setCargo(resultDatos.getString("cargo"));
		usuarioSigec.setEmail(resultDatos.getString("email"));
		usuarioSigec.setLogins(resultDatos.getInt("logins"));
		usuarioSigec.setFechaCreacion(resultDatos.getInt("fecha_creacion"));
		usuarioSigec.setHabilitado(resultDatos.getInt("habilitado"));
		usuarioSigec.setNivel(resultDatos.getInt("nivel"));
		usuarioSigec.setGenero(resultDatos.getString("genero"));
		usuarioSigec.setPrioridad(resultDatos.getInt("prioridad"));
		usuarioSigec.setIdEntidad(resultDatos.getInt("id_entidad"));
		usuarioSigec.setSupers(resultDatos.getInt("super"));
		usuarioSigec.setCedulaIdentidad(resultDatos.getInt("cedula_identidad"));
		usuarioSigec.setExpedido(resultDatos.getString("expedido"));
		usuarioSigec.setTheme(resultDatos.getString("theme"));
		usuarioSigec.setCiteDespacho(resultDatos.getInt("cite_despacho"));
			
			
			usuariosSigec.add(usuarioSigec);
		}
		if(!usuariosSigec.isEmpty()) {
				vObjResultado.notificacion = "Se realizó correctamente la consulta";
		}else {
			vObjResultado.notificacion = "No se encontraron resultados";
		}
		
		vObjResultado.correcto = true;
		
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = usuariosSigec;
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}

	public Resultado nuevaHr(DatosHr vObjEntradaDatos) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".p_genera_hr(?,?,?,?,?,?,?,?)");
		procedure.setString(1, vObjEntradaDatos.getLogin());
		procedure.setInt(2, vObjEntradaDatos.getIdDestinatario() );
		procedure.setInt(3, vObjEntradaDatos.getGestion() );
		procedure.setString(4, vObjEntradaDatos.getNombreTramite());
		procedure.setString(5, vObjEntradaDatos.getProveido());
		procedure.setInt(6, vObjEntradaDatos.getFojas());
		procedure.setString(7, vObjEntradaDatos.getCodigoTramite());
		procedure.registerOutParameter(8,Types.VARCHAR);
		procedure.executeUpdate();
				
		
		vObjResultado.notificacion = "Se realizó correctamente la consulta";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = procedure.getObject(8);
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}
	
	// GENERA HR
	
	public Resultado nuevoDocumento(DatosHr vObjEntradaDatos) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".p_genera_hr(?,?,?,?,?,?,?,?)");
		procedure.setString(1, vObjEntradaDatos.getLogin());
		procedure.setInt(2, vObjEntradaDatos.getIdDestinatario() );
		procedure.setInt(3, vObjEntradaDatos.getGestion() );
		procedure.setString(4, vObjEntradaDatos.getNombreTramite());
		procedure.setString(5, vObjEntradaDatos.getProveido());
		procedure.setInt(6, vObjEntradaDatos.getFojas());
		procedure.setString(7, vObjEntradaDatos.getCodigoTramite());
		procedure.registerOutParameter(8,Types.VARCHAR);
		procedure.executeUpdate();
				
		
		vObjResultado.notificacion = "Se realizó correctamente la consulta";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = procedure.getObject(8);
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}

	
	/** FUNCION GENERAR HR Y DOCUMENTO EN SIGEC */
	public Resultado generarDocumentoSIGEC(String pVar_1,Integer pVar_2,Integer pVar_3,Integer pVar_4,String pVar_5,Integer pVar_6,Boolean pVar_7,String pVar_8) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();

		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".p_genera_documento(?,?,?,?,?,?,?,?,?,?)");
	
		procedure.setString(1, pVar_1);
		procedure.setInt(2, pVar_2);
		procedure.setInt(3, pVar_3);
		procedure.setInt(4, pVar_4);
		procedure.setString(5, pVar_5);
		procedure.setInt(6, pVar_6);
		procedure.setBoolean(7, pVar_7);
		procedure.setString(8, pVar_8);
		procedure.registerOutParameter(9,Types.VARCHAR);
		procedure.registerOutParameter(10,Types.INTEGER);
		procedure.executeUpdate();
		
		if((Integer) procedure.getObject(10)==1) {
			
		vObjResultado.notificacion = "Se registro correctamente";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado =(Integer) procedure.getObject(10);
		vObjResultado.datoAdicional =  (String) procedure.getObject(9);
		}else if((Integer) procedure.getObject(10)==2) {
			vObjResultado.notificacion = "Su usuario no coincide con el usuario registrado en SIGEC";
			vObjResultado.correcto = false;
			vObjResultado.codigoResultado =(Integer) procedure.getObject(10);
			vObjResultado.datoAdicional =  "";
		}
		procedure.close();
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}	
	
	public EntDocumentoSigec infoDocSigec(String pVar_1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		EntDocumentoSigec documentoSigec = new EntDocumentoSigec(); 
		Connection connection = null;
		CallableStatement procedure = null;
		ResultSet resultDatos=null;
		try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		procedure = connection.prepareCall("select * from "+esquemaSigec+".documentos d where d.codigo= ?");
		procedure.setString(1, pVar_1);
		resultDatos = procedure.executeQuery();
		if (resultDatos.next()) {
			documentoSigec.id= resultDatos.getInt("id");
			documentoSigec.cite= resultDatos.getString("codigo"); 
			documentoSigec.hr= resultDatos.getString("nur"); 
			documentoSigec.nombreRemitente= resultDatos.getString("nombre_remitente"); 
			documentoSigec.cargoRemitente= resultDatos.getString("cargo_remitente"); 
			documentoSigec.nombreVia= resultDatos.getString("nombre_via"); 
			documentoSigec.cargoVia= resultDatos.getString("cargo_via"); 
			documentoSigec.nombreDestinatario= resultDatos.getString("nombre_destinatario"); 
			documentoSigec.cargoDestinatario= resultDatos.getString("cargo_destinatario"); 
			documentoSigec.mosca= resultDatos.getString("mosca_remitente"); 
			documentoSigec.referencia= resultDatos.getString("referencia"); 
			documentoSigec.fechaCreacionSigec= resultDatos.getTimestamp("fecha_creacion");
		} else {
	        return null;
	    }	
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			return null;
		}finally {
			procedure.close();
			connection.close();
			resultDatos.close();
		}
		
	return documentoSigec;
	}	
	
	
	public Resultado infoCite(String pVar_1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado =new Resultado();
		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("select d.id,d.codigo,d.fecha_creacion from "+esquemaSigec+".documentos d where UPPER(d.codigo)  LIKE UPPER(CONCAT('%',?,'%')) limit 15");
		procedure.setString(1, pVar_1);
		ResultSet resultDatos = procedure.executeQuery();
		List<InfoCite> cites = new ArrayList<InfoCite>();
		String pattern = "dd/MM/yyyy HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		while(resultDatos.next()) {
			
			InfoCite cite = new InfoCite(); 
			cite.setId(resultDatos.getInt("id"));
			cite.setCite(resultDatos.getString("codigo"));
						
			//cite.setFechaRegistro(simpleDateFormat.format( resultDatos.getDate("fecha_creacion")));
			cite.setFechaRegistro( resultDatos.getTimestamp("fecha_creacion"));
			cites.add(cite);
		}
		vObjResultado.notificacion = "Se realizo correctamente la consulta";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado =200;
		vObjResultado.datoAdicional =  cites;
		procedure.close();
		connection.close();
		}
		catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			vObjResultado.notificacion = "Existio un error:" + httpClientOrServerExc.getMessage();
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado =400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
			
		}
		
	return vObjResultado;
	}	
	
	/** FUNCION GENERAR HR Y DOCUMENTO EN SIGEC v2 HOJAS DE RUTA INTERNAS */
	public Resultado generarDocumentoSIGECv2(DatosDoc vObj) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
			 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		//cambiar sigec_final para produccion
		CallableStatement procedure = connection.prepareCall("call "+esquemaSigec+".p_genera_documento_v2(?,?,?,?,?,?,?,?,?,?,?)");
	
		procedure.setString(1, vObj.usuario );
		procedure.setInt(2, vObj.idVia);
		procedure.setInt(3, vObj.idDestinatario);
		procedure.setInt(4, vObj.idTipoDocumento);
		procedure.setString(5, vObj.referencia);
		procedure.setInt(6, vObj.fojas);
		procedure.setBoolean(7, vObj.hojaRuta);
		procedure.setString(8, vObj.proveido);
		procedure.registerOutParameter(9,Types.VARCHAR);
		procedure.registerOutParameter(10,Types.INTEGER);
        procedure.registerOutParameter(11,Types.VARCHAR);
		procedure.executeUpdate();
		
		SalidaGeneraDoc salidaD = new SalidaGeneraDoc();
		
		if((Integer) procedure.getObject(10)==1) {
			salidaD.cite = (String) procedure.getObject(9);
			salidaD.hojaRuta = (String) procedure.getObject(11); 
		vObjResultado.notificacion = "Se registro correctamente";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado =(Integer) procedure.getObject(10);
		vObjResultado.datoAdicional =  salidaD;
		}else if((Integer) procedure.getObject(10)==2) {
			vObjResultado.notificacion = "Su usuario no coincide con el usuario registrado en SIGEC";
			vObjResultado.correcto = false;
			vObjResultado.codigoResultado =(Integer) procedure.getObject(10);
			vObjResultado.datoAdicional =  "";
		}
		procedure.close();
		connection.close();
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";
		}
		
	return vObjResultado;
	}	
	
	
	public List<FSeguimientoTramite> seguimientoHR(String pVar_1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		
		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		CallableStatement procedure = connection.prepareCall("SELECT s.id as id,'SIGEC' as sistema,(case when s.indice = 1 then (SELECT d.codigo from sigec.documentos d where d.nur = s.nur and d.id_seguimiento = 0 order by d.id desc limit 1) \r\n"
				+ "														     else (SELECT d.codigo from sigec.documentos d where d.nur = s.nur and d.id_seguimiento = s.id order by d.id desc limit 1) end), s.nombre_emisor, s.nombre_receptor, s.cargo_emisor, s.cargo_receptor, s.de_oficina, s.a_oficina,s.fecha_emision, s.fecha_recepcion, s.adjuntos, c.accion, UPPER(e.estado), s.proveido\r\n"
				+ "				FROM (SELECT ROW_NUMBER() OVER (ORDER BY s.id) as indice,s.* FROM seguimiento s WHERE s. nur=? ) as s \r\n"
				+ "				INNER JOIN acciones c ON s.accion=c.id \r\n"
				+ "				INNER JOIN estados e ON s.estado=e.id \r\n"
				+ "				INNER JOIN users r ON s.derivado_por=r.id \r\n"
				+ "				INNER JOIN users d ON s.derivado_a=d.id \r\n"
				+ "				ORDER BY s.id ASC,s.fecha_emision ASC");
		procedure.setString(1, pVar_1);
		ResultSet resultDatos = procedure.executeQuery();
		List<FSeguimientoTramite> seguimientoHR = new ArrayList<FSeguimientoTramite>();
		String pattern = "dd/MM/yyyy HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		while(resultDatos.next()) {
			
			FSeguimientoTramite registro = new FSeguimientoTramite(); 
			registro.id =resultDatos.getInt(1);
			registro.sistema =resultDatos.getString(2);
			registro.codigo =resultDatos.getString(3);
			registro.nombre_emisor =resultDatos.getString(4);
			registro.cargo_emisor =resultDatos.getString(6);
			registro.de_oficina =resultDatos.getString(8);
			registro.nombre_receptor =resultDatos.getString(5);
			registro.cargo_receptor =resultDatos.getString(7);
			registro.a_oficina =resultDatos.getString(9);
			registro.fecha_emision =resultDatos.getTimestamp(10);
			registro.fecha_recepcion =resultDatos.getTimestamp(11);
			registro.adjuntos =resultDatos.getString(12);
			registro.accion =resultDatos.getString(13);
			registro.estado =resultDatos.getString(14);
			registro.proveido =resultDatos.getString(15);
									
			//cite.setFechaRegistro(simpleDateFormat.format( resultDatos.getDate("fecha_creacion")));
			//regsitro.setFechaRegistro( resultDatos.getTimestamp("fecha_creacion"));
			seguimientoHR.add(registro);
		}
		
		procedure.close();
		connection.close();
		return seguimientoHR;
		}
		catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			return null;
			
		}
		
	}
	
	public Resultado seguimientoHr(String pVariable1)
			throws JsonProcessingException, SQLException, ClassNotFoundException {
		
		Resultado vObjResultado = new Resultado();
		try {
			List<FSeguimientoTramite> lista = seguimientoHR(pVariable1);
		vObjResultado.correcto = true;
		vObjResultado.notificacion = "La consulta se realizó exitosamente";
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = lista;


	} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

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
	

	public Boolean actualizarCiteHr(String codigo, String hr,String nombreRemitente,String cargoRemitente,String institucionRemitente) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		 
		try {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		//cambiar sigec_final para produccion
		CallableStatement procedure = connection.prepareCall("UPDATE sigec.documentos\r\n"
				+ "SET codigo=?, cite_original=?, nombre_remitente=?,cargo_remitente=?,institucion_remitente=?,id_tipo=6 \r\n"
				+ "WHERE codigo=?");
	
		procedure.setString(1, codigo );
		procedure.setString(2, codigo );
		procedure.setString(3, nombreRemitente );
		procedure.setString(4, cargoRemitente );
		procedure.setString(5, institucionRemitente );
		procedure.setString(6, hr );
		procedure.execute();
	
		procedure.close();
		connection.close();
		return true;
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			return false;
		}
		
	}	
	
	
	public Resultado registrarSeguimientos(List<SeguimientoDocumentos> vObj) throws ClassNotFoundException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		String nur = "",noRegistrados="";
		Integer contador=0,contadorNR=0;
		Long id=(long) 0,idAnterior=(long) 0;
		try {
			Connection connection = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("select MAX(s.id)+1 from seguimiento s ");
			ResultSet resultDatos = procedure.executeQuery();
			
			if(resultDatos.next()) {
				
				id=resultDatos.getLong(1);
					
				 for(int i=0; i<vObj.size();i++) {
					 if(idAnterior ==0) {					 
					 procedure = connection.prepareCall("select COUNT(*),(select count(*) from seguimiento where nur=?) from documentos d where d.nur= ? ");
					 procedure.setString(1, vObj.get(i).nur);
					 procedure.setString(2, vObj.get(i).nur);
					 resultDatos = procedure.executeQuery();
					 resultDatos.next();}
					 
					  if(resultDatos.getInt(1)>0) {
						  nur=vObj.get(i).nur;
						  if(resultDatos.getInt(2)==0) {  
						  						  
						  procedure = connection.prepareCall("INSERT INTO sigec.seguimiento\r\n"
									+ "(id, id_seguimiento, nur, derivado_por, nombre_emisor, cargo_emisor, fecha_emision, derivado_a, nombre_receptor, cargo_receptor, fecha_recepcion, estado, accion, oficial, hijo, proveido, archivos, adjuntos, de_oficina, padre, a_oficina, id_archivo, id_de_oficina, id_a_oficina, prioridad)\r\n"
									+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '0',?, NULL, '[]', ?, 0, ?, 0, ?, ?, 0)");
							//                1  2  3  4  5  6  7  8  9  10 11              12             13    14    15 16    
							procedure.setLong(1, id);
							procedure.setLong(2, idAnterior);
							procedure.setString(3, nur);
							procedure.setInt(4, vObj.get(i).derivadoPor);
							procedure.setString(5, vObj.get(i).nombreEmisor);
							procedure.setString(6, vObj.get(i).cargoEmisor);
							procedure.setString(7, vObj.get(i).fechaEmision);
							procedure.setInt(8, vObj.get(i).derivadoA);
							procedure.setString(9, vObj.get(i).nombreReceptor);
							procedure.setString(10, vObj.get(i).cargoReceptor);
							procedure.setString(11, vObj.get(i).fechaRecepcion);
							
							procedure.setInt(12, vObj.get(i).estado);
							procedure.setInt(13, vObj.get(i).accion);
							procedure.setInt(14, vObj.get(i).oficial);
							
							procedure.setString(15, vObj.get(i).proveido);
							procedure.setString(16, vObj.get(i).deOficina);
							procedure.setString(17, vObj.get(i).aOficina);
							procedure.setInt(18, vObj.get(i).idDeOficina);
							procedure.setInt(19, vObj.get(i).idAOficina);
							procedure.execute();
							if(i<vObj.size()-1) {
							if(!nur.equals(vObj.get(i+1).nur) ) {
								  idAnterior=(long) 0;
							  }else {
								  idAnterior=id;
							  }}
							contador++;
							id++;
						  }
					  }else {						  
						  noRegistrados = noRegistrados+ ", " + vObj.get(i).nur;
						  contadorNR++;
					  }
					 
					 
				 }
				 
				/*	
				
				
					procedure = connection.prepareCall("INSERT INTO sigec.seguimiento\r\n"
							+ "(id, id_seguimiento, nur, derivado_por, nombre_emisor, cargo_emisor, fecha_emision, derivado_a, nombre_receptor, cargo_receptor, fecha_recepcion, estado, accion, oficial, hijo, proveido, archivos, adjuntos, de_oficina, padre, a_oficina, id_archivo, id_de_oficina, id_a_oficina, prioridad)\r\n"
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 1, 1, '0',?, NULL, '[]', ?, 0, ?, 0, ?, ?, 0)");
					//                1  2  3  4  5  6  7  8  9  10 11              12             13    14    15 16    
					procedure.setLong(1, vObj.id);
					procedure.setLong(2, vObj.idSeguimiento);
					procedure.setString(3, vObj.nur);
					procedure.setInt(4, vObj.derivadoPor);
					procedure.setString(5, vObj.nombreEmisor);
					procedure.setString(6, vObj.cargoEmisor);
					procedure.setTimestamp(7, vObj.fechaRecepcion);
					procedure.setInt(8, vObj.derivadoA);
					procedure.setString(9, vObj.nombreReceptor);
					procedure.setString(10, vObj.cargoReceptor);
					procedure.setTimestamp(11, null);
					procedure.setString(12, vObj.proveido);
					procedure.setString(13, vObj.deOficina);
					procedure.setString(14, vObj.aOficina);
					procedure.setInt(15, vObj.idDeOficina);
					procedure.setInt(16, vObj.idAOficina);
					procedure.execute();
					
					procedure.close();
					
				
				
				
			}else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "No se encontro identificador";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = "";*/
			}
			
			vObjResultado.correcto = true;
			vObjResultado.notificacion = "Se registro correctamente: "+contador + " | No registrados: "+ contadorNR;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = noRegistrados;

			procedure.close();
			connection.close();

		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = vResponseMensage;
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = "";

		}
		return vObjResultado;

	}
	
	public Resultado obtenerBandejasUsuario(String pVar_1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado =new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		ResultSet resultDatos = null;
		try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		procedure = connection.prepareCall("select * from sigec.users u where u.username = ?");
		procedure.setString(1, pVar_1);
		resultDatos = procedure.executeQuery();
		Integer idUsuario;
		if(resultDatos.next()) {
			idUsuario = resultDatos.getInt(1);
			procedure = null;
			resultDatos = null;
		}else {
			vObjResultado.notificacion = "Su usuario no coincide con el usuario registrado en el sistema SIGEC. Contactar con soporte.";
			vObjResultado.correcto = false;
			vObjResultado.codigoResultado =400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
		}
		
		
		procedure = connection.prepareCall("select d.id,\r\n"
				+ "case when s.estado = 1 then 'ENTRANTE' when  s.estado = 2 then 'PENDIENTE' end as estado,\r\n"
				+ "d.nur,d.referencia, d.nombre_remitente,d.cargo_remitente,d.institucion_remitente,d.cite_original,s.nombre_emisor,s.cargo_emisor,s.de_oficina \r\n"
				+ "from sigec.documentos d \r\n"
				+ "inner join sigec.seguimiento s on s.nur = d.nur \r\n"
				+ "inner join sigec.users u on s.derivado_a =u.id \r\n"
				+ "where u.id =? and d.estado =1 and s.estado in(1,2) order by s.estado desc,1 desc ");
		procedure.setInt(1, idUsuario);
		resultDatos = procedure.executeQuery();
		
		List<BandejasHR> hrs = new ArrayList<BandejasHR>();
		while(resultDatos.next()) {
			
			BandejasHR hr = new BandejasHR(); 
			hr.Id =resultDatos.getInt(1);
			hr.estado=resultDatos.getString(2);
			hr.nur=resultDatos.getString(3);
			hr.referencia=resultDatos.getString(4);
			hr.nombreRemitente=resultDatos.getString(5);
			hr.cargoRemitente=resultDatos.getString(6);
			hr.institucionRemitente=resultDatos.getString(7);
			hr.citeOriginal=resultDatos.getString(8);
			hr.nombreEmisor=resultDatos.getString(9);
			hr.cargoEmisor=resultDatos.getString(10);
			hr.deOficina=resultDatos.getString(11);
			hrs.add(hr);
		}
		vObjResultado.notificacion = "La consulta se realizó exitosamente";
		vObjResultado.correcto = true;
		vObjResultado.codigoResultado =200;
		vObjResultado.datoAdicional =  hrs;
		}
		catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			
			vObjResultado.notificacion = "Existio un error:" + httpClientOrServerExc.getMessage();
			vObjResultado.correcto = true;
			vObjResultado.codigoResultado =400;
			vObjResultado.datoAdicional = null;
			return vObjResultado;
			
		}finally {
			try {
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
                if (resultDatos != null) resultDatos.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		
	return vObjResultado;
	}	
	
	/** PROCEDIMIENTO GENERAR  DEREIVACION DE HOJA DE RUTA */
	public Resultado generarDerivacion(DatosDocFC vObj) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		//cambiar sigec_final para produccion
		procedure = connection.prepareCall("call sigec.p_derivar_hr(?,?,?,?,?,?,?,?)");
	
		procedure.setString(1, vObj.login );
		procedure.setInt(2, vObj.idDestinatario);
		procedure.setString(3, vObj.hojaRuta);
		procedure.setString(4, vObj.proveido);
		procedure.registerOutParameter(5,Types.BOOLEAN);
		procedure.registerOutParameter(6,Types.INTEGER);
        procedure.registerOutParameter(7,Types.VARCHAR);
        procedure.registerOutParameter(8,Types.VARCHAR);
		procedure.executeUpdate();
		
		 
		vObjResultado.notificacion = (String) procedure.getObject(7);
		vObjResultado.correcto = (Boolean) procedure.getObject(5);
		vObjResultado.codigoResultado =(Integer) procedure.getObject(6);
		vObjResultado.datoAdicional =  (String) procedure.getObject(8);
		
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional =vResponseMensage;
		}finally {
			try {
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                
                vObjResultado.correcto = false;
    			vObjResultado.notificacion = "No se pudo concluir la operación";
    			vObjResultado.codigoResultado = 400;
    			vObjResultado.datoAdicional =e.getMessage();
            }
		}
		
	return vObjResultado;
	}	
	
	/** OBTENER DOCUMENTOS DE HOJA DE RUTA */
	public Resultado obtenerDocumentesHR(String v1) throws JsonProcessingException, SQLException, ClassNotFoundException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		Connection connection = null;
		CallableStatement procedure = null;
		ResultSet resultDatos = null;
		try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(db_url, db_usuario, db_password);
		//cambiar sigec_final para produccion
		procedure = connection.prepareCall("SELECT * from sigec.documentos d where d.nur = ? ");
	
		procedure.setString(1, v1 );
		resultDatos = procedure.executeQuery();
		List<DocumentosHr> lista = new ArrayList<>();
		while(resultDatos.next()) {
			DocumentosHr registro = new DocumentosHr();
			registro.id = resultDatos.getInt(1);
			registro.codigo = resultDatos.getString(6);
			registro.nur = resultDatos.getString(7);
			lista.add(registro);
		}
		
		 
		vObjResultado.correcto = true;
		vObjResultado.notificacion = "La consulta se realizó exitosamente";
		vObjResultado.codigoResultado = 200;
		vObjResultado.datoAdicional = lista;
		
		}catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			// return null;
			String vResponseBodyString = httpClientOrServerExc.getResponseBodyAsString();
			JsonObject jsonObject = new JsonParser().parse(vResponseBodyString).getAsJsonObject();
			String vResponseMensage = jsonObject.get("mensaje").getAsString();

			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo concluir la operación";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional =vResponseMensage;
		}finally {
			try {
                if (procedure != null) procedure.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                
                vObjResultado.correcto = false;
    			vObjResultado.notificacion = "No se pudo concluir la operación";
    			vObjResultado.codigoResultado = 400;
    			vObjResultado.datoAdicional =e.getMessage();
            }
		}
		
	return vObjResultado;
	}	
	
}
