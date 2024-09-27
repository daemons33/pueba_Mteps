package mteps.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.codec.binary.Base64;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcraft.jsch.*;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

/*import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
*/
import mteps.config.entity.EjecutarQuery;
import mteps.config.entity.ObtenerArchivoRemotoResponse;
import mteps.config.entity.RequestPassword;
import mteps.config.procedimiento.ConfiguracionRepository;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@SpringBootApplication
public class ConfigCORE {
	@Value("${db.url}")
	private String db_url;

	@Value("${db.usuario}")
	private String db_usuario;

	@Value("${db.password}")
	private String db_password;

	@Autowired
	PasswordEncoder passEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	public Resultado cambioPassword(RequestPassword vObject) throws JsonProcessingException {

		Resultado vObjResultado = new Resultado();
		vObject.gestion = 1;
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vObject);

		Boolean parear;
		Connection connection = null;
		try {
			if (vObject.passwordNuevo1 == vObject.passwordNuevo2) {

			}
			connection = DriverManager.getConnection(db_url, db_usuario, db_password);
			CallableStatement procedure = connection.prepareCall("call workflow.p_gestion_password(?,?,?,?,?)");
			procedure.setString(1, json);
			procedure.registerOutParameter(2, Types.BOOLEAN);
			procedure.registerOutParameter(3, Types.VARCHAR);
			procedure.registerOutParameter(4, Types.INTEGER);
			procedure.registerOutParameter(5, Types.VARCHAR);

			procedure.executeUpdate();

			parear = passEncoder.matches(vObject.passwordActual, (String) procedure.getObject(5));
			if (parear == true) {
				if (vObject.passwordNuevo1.equals(vObject.passwordNuevo2)) {
					if (vObject.passwordNuevo1.length() > 6) {
						vObject.gestion = 2;
						vObject.passwordNuevo1 = passEncoder.encode(vObject.passwordNuevo1);
						ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json2 = ow2.writeValueAsString(vObject);
						CallableStatement procedure2 = connection
								.prepareCall("call workflow.p_gestion_password(?,?,?,?,?)");
						procedure2.setString(1, json2);
						procedure2.registerOutParameter(2, Types.BOOLEAN);
						procedure2.registerOutParameter(3, Types.VARCHAR);
						procedure2.registerOutParameter(4, Types.INTEGER);
						procedure2.registerOutParameter(5, Types.VARCHAR);

						procedure2.executeUpdate();

						vObjResultado.correcto = (Boolean) procedure2.getObject(2);
						vObjResultado.notificacion = (String) procedure2.getObject(3);
						vObjResultado.codigoResultado = (Integer) procedure2.getObject(4);
						vObjResultado.datoAdicional = (String) procedure2.getObject(5);
					} else {
						vObjResultado.correcto = false;
						vObjResultado.notificacion = "La nueva contraseña debe contener al menos 7 caracteres";
						vObjResultado.codigoResultado = 400;
						vObjResultado.datoAdicional = "";
					}

				} else {
					vObjResultado.correcto = false;
					vObjResultado.notificacion = "La nueva contraseña no coincide";
					vObjResultado.codigoResultado = 400;
					vObjResultado.datoAdicional = "";
				}

			} else {
				vObjResultado.correcto = false;
				vObjResultado.notificacion = "La contraseña actual no coincide";
				vObjResultado.codigoResultado = 400;
				vObjResultado.datoAdicional = "";
			}

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;

	}
	
	@Autowired
    private ConfiguracionRepository configuracionRepository;
	
	@Transactional
	public Resultado ejecutarProcedimiento(String procedimiento,ObjectNode pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar la consulta.
		Resultado vObjResultado = new Resultado();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pVariable1);


		try {

			ConfigEntity results =configuracionRepository.callProcedure(procedimiento,json);
			
	        // Obtener los valores de los parámetros de salida
	        vObjResultado.correcto = results.correcto;
	        vObjResultado.notificacion = results.notificacion;
	        vObjResultado.codigoResultado = results.codigoresultado;
	        vObjResultado.datoAdicional = results.datoadicional;

		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "No se pudo completar la operación.";
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = e.getMessage();
		} 

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION TICKET

	public Resultado obtenerClasificador(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		String mensajeResp;

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("public.f_obtener_clasificador");

			procedure.setParameter("p_clasificador", pVariable1);
			if (!procedure.getResultList().isEmpty()) {
				mensajeResp = "La consulta se realizó exitosamente";
			} else {
				mensajeResp = "No se encontraron resultados";
			}

			vObjResultado.correcto = true;
			vObjResultado.notificacion = mensajeResp;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////// GESTION TICKET

	public Resultado obtenerClasificador2(Integer pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.
		Resultado vObjResultado = new Resultado();
		String mensajeResp;

		try {
			StoredProcedureQuery procedure = entityManager
					.createNamedStoredProcedureQuery("public.f_obtener_id_clasificador");

			procedure.setParameter("v_id", pVariable1);
			if (!procedure.getResultList().isEmpty()) {
				mensajeResp = "La consulta se realizó exitosamente";
			} else {
				mensajeResp = "No se encontraron resultados";
			}

			vObjResultado.correcto = true;
			vObjResultado.notificacion = mensajeResp;
			vObjResultado.codigoResultado = 200;
			vObjResultado.datoAdicional = procedure.getResultList();
		} catch (Exception e) {
			vObjResultado.correcto = false;
			vObjResultado.notificacion = "Error:" + e.getMessage();
			vObjResultado.codigoResultado = 400;
			vObjResultado.datoAdicional = null;
		}

		return vObjResultado;
	}

	////////////// CONVETIR UN NUMERO EN LITERAL
	////

	public static String convertirNumeroALetras(double numero) {
		String[] unidades = { "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve" };
		String[] decenas = { "diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete",
				"dieciocho", "diecinueve", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta",
				"ochenta", "noventa" };
		String[] centenas = { "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos",
				"setecientos", "ochocientos", "novecientos" };

		String[] miles = { "", "mil", "dos mil", "tres mil", "cuatro mil", "cinco mil", "seis mil", "siete mil",
				"ocho mil", "nueve mil" };

		int entero = (int) numero;
		int decimal = (int) ((numero - entero) * 100);

		String parteEntera = "";
		String parteDecimal = "";

		if (entero == 0) {
			parteEntera = "cero";
		} else if (entero < 0) {
			parteEntera = "menos " + convertirNumeroALetras(-entero);
		} else {
			if (entero >= 1000) {
				int milesEntero = entero / 1000;
				parteEntera += miles[milesEntero] + " ";
				entero %= 1000;
			}

			if (entero >= 100) {
				int centenasEntero = entero / 100;
				parteEntera += centenas[centenasEntero] + " ";
				entero %= 100;
			}

			if (entero >= 10) {
				if (entero <= 20) {
					parteEntera += decenas[entero - 10] + " ";
					entero = 0;
				} else {
					int decenasEntero = entero / 10;
					parteEntera += decenas[decenasEntero + 8] + " ";
					entero %= 10;
					if (entero > 0) {
						parteEntera += "y ";
					}
				}
			}

			if (entero > 0) {
				parteEntera += unidades[entero] + " ";
			}
		}

		parteDecimal = (decimal < 10 ? "0" : "") + decimal + "/100";

		return parteEntera.trim() + " " + parteDecimal;
	}

	///// V2 numero a letras
	private final String[] UNIDADES = { "", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ",
			"nueve " };
	private final String[] DECENAS = { "diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
			"diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ", "cincuenta ", "sesenta ",
			"setenta ", "ochenta ", "noventa " };
	private final String[] CENTENAS = { "", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ",
			"seiscientos ", "setecientos ", "ochocientos ", "novecientos " };

	public String Convertir(String numero, boolean mayusculas) {
		String literal = "";
		String parte_decimal;
		// si el numero utiliza (.) en lugar de (,) -> se reemplaza
		numero = numero.replace(".", ",");
		// si el numero no tiene parte decimal, se le agrega ,00
		if (numero.indexOf(",") == -1) {
			numero = numero + ",00";
		}
		// se valida formato de entrada -> 0,00 y 999 999 999,00
		if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
			// se divide el numero 0000000,00 -> entero y decimal
			String Num[] = numero.split(",");
			// de da formato al numero decimal
			parte_decimal = " 0" + Num[1] + "/100";
			// se convierte el numero a literal
			if (Integer.parseInt(Num[0]) == 0) {// si el valor es cero
				literal = "cero ";
			} else if (Integer.parseInt(Num[0]) > 999999) {// si es millon
				literal = getMillones(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 999) {// si es miles
				literal = getMiles(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 99) {// si es centena
				literal = getCentenas(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 9) {// si es decena
				literal = getDecenas(Num[0]);
			} else {// sino unidades -> 9
				literal = getUnidades(Num[0]);
			}
			// devuelve el resultado en mayusculas o minusculas
			if (mayusculas) {
				return (literal + parte_decimal).toUpperCase();
			} else {
				return (literal + parte_decimal);
			}
		} else {// error, no se puede convertir
			return literal = null;
		}
	}

	/* funciones para convertir los numeros a literales */
	private String getUnidades(String numero) {// 1 - 9
		// si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
		String num = numero.substring(numero.length() - 1);
		return UNIDADES[Integer.parseInt(num)];
	}

	private String getDecenas(String num) {// 99
		int n = Integer.parseInt(num);
		if (n < 10) {// para casos como -> 01 - 09
			return getUnidades(num);
		} else if (n > 19) {// para 20...99
			String u = getUnidades(num);
			if (u.equals("")) { // para 20,30,40,50,60,70,80,90
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
			} else {
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
			}
		} else {// numeros entre 11 y 19
			return DECENAS[n - 10];
		}
	}

	private String getCentenas(String num) {// 999 o 099
		if (Integer.parseInt(num) > 99) {// es centena
			if (Integer.parseInt(num) == 100) {// caso especial
				return " cien ";
			} else {
				return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
			}
		} else {// por Ej. 099
			// se quita el 0 antes de convertir a decenas
			return getDecenas(Integer.parseInt(num) + "");
		}
	}

	private String getMiles(String numero) {// 999 999
		// obtiene las centenas
		String c = numero.substring(numero.length() - 3);
		// obtiene los miles
		String m = numero.substring(0, numero.length() - 3);
		String n = "";
		// se comprueba que miles tenga valor entero
		if (Integer.parseInt(m) > 0) {
			n = getCentenas(m);
			return n + "mil " + getCentenas(c);
		} else {
			return "" + getCentenas(c);
		}

	}

	private String getMillones(String numero) { // 000 000 000
		// se obtiene los miles
		String miles = numero.substring(numero.length() - 6);
		// se obtiene los millones
		String millon = numero.substring(0, numero.length() - 6);
		String n = "";
		if (millon.length() > 1) {
			n = getCentenas(millon) + "millones ";
		} else {
			n = getUnidades(millon) + "millon ";
		}
		return n + getMiles(miles);
	}

	// FIN NUMERO A LETRAS
	/// VALIDAR CORREO ELECTRONICO
	public static boolean validarCorreoElectronico(String correoElectronico) {
		// expresión regular para validar un correo electrónico
		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(correoElectronico);

		return matcher.matches(); // devolver true si es un correo electrónico válido, false en caso contrario
	}

	public String formatoFecha(Date fecha, Boolean hora) {

		String formato = hora ? "dd/MM/yyyy HH:mm" : "dd/MM/yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		return dateFormat.format(fecha);
	}

	// Redondeo de decimales
	public Double numeroConDecimales(Double var, Integer cantidad) {
		var = var != null ? var : 0;
		BigDecimal bigDecimal = new BigDecimal(var);
		BigDecimal roundedNumber = bigDecimal.setScale(cantidad, RoundingMode.HALF_UP);

		return roundedNumber.doubleValue();

	}

	// numero a letras V3

	public Object ejecutarquey(String pVariable1) throws JsonProcessingException, SQLException {
		// Crea un objeto Resultado para vaciar datos en la consulta.

		try {

			StoredProcedureQuery procedure = entityManager.createNamedStoredProcedureQuery("workflow.ejecutar_query");

			procedure.setParameter("query", pVariable1);

			EjecutarQuery resultado = (EjecutarQuery) procedure.getSingleResult();

			return resultado != null ? resultado.var : null;
		} catch (NoResultException e) {
			return null;
		}
	}

	// Generar QR

	public byte[] generarCodigoQR(String content) {
		QRCode qrCode = (QRCode) QRCode.from(content).to(ImageType.PNG);
		return qrCode.stream().toByteArray();
	}

	public String generarFecha(Date fecha, Integer tipo) {
		String fechafinal = null, dialiteral, dia, mes, anio, hora, mesNumerico,horaSegundo;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE", new Locale("es", "ES"));
		dialiteral = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("dd", new Locale("es", "ES"));
		dia = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("MMMMM", new Locale("es", "ES"));
		mes = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("yyyy", new Locale("es", "ES"));
		anio = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("HH:mm", new Locale("es", "ES"));
		hora = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("HH:mm:ss", new Locale("es", "ES"));
		horaSegundo = simpleDateFormat.format(fecha);
		simpleDateFormat = new SimpleDateFormat("MM", new Locale("es", "ES"));
		mesNumerico = simpleDateFormat.format(fecha);
		switch (tipo) {
		case 1:
			fechafinal = dialiteral.substring(0, 1).toUpperCase() + dialiteral.substring(1, dialiteral.length()) + ", "
					+ dia + " de " + mes + " de " + anio;
			break;
		case 2:
			fechafinal = dia + " de " + mes + " de " + anio;
			break;
		case 3:
			fechafinal = dia + " días del mes de " + mes + " del año " + anio;
			break;
		case 4:
			fechafinal = hora;
			break;
		case 5:
			fechafinal = dia;
			break;
		case 6:
			fechafinal = mes;
			break;
		case 7:
			fechafinal = anio;
			break;
		case 8:
			fechafinal = dia + "/" + mesNumerico + "/" + anio;
			break;
		case 9:
			fechafinal = dia + "/" + mesNumerico + "/" + anio+" "+horaSegundo;
			break;				
		case 10:
			fechafinal = dia + "/" + mesNumerico + "/" + anio+" "+hora;
			break;
		case 11:
			fechafinal = horaSegundo;
			break;
					
		default:
			fechafinal = "";
			break;
		}

		return fechafinal;
	}

	public ObtenerArchivoRemotoResponse getRemotePdf(String host, int port, String username, String password,
			String remoteFilePath) {
		ObtenerArchivoRemotoResponse resultado = new ObtenerArchivoRemotoResponse();
		Session session = null;
		Channel channel = null;
		try {
		JSch jsch = new JSch();
		session = jsch.getSession(username, host, port);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no"); // Desactiva la comprobación estricta del host para
															// simplificar

		session.connect();
		
		
		channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand("cat " + remoteFilePath);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		channel.setOutputStream(outputStream);

		InputStream inputStream = channel.getInputStream();
		
			channel.connect();
									
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			
			resultado.archivo = outputStream.toByteArray();
			resultado.correcto = true;
			
			if(channel.getExitStatus() == 1){
				resultado.mensaje ="El archivo no existe en la ruta especificada: "+remoteFilePath;
				resultado.correcto = false;
				return resultado;
		    }
			return resultado;
		} catch (JSchException | IOException e) {
			
			
		    	resultado.mensaje =e.getMessage();
				resultado.correcto = false;
		   
			
			return resultado;
		} finally {
			if(channel!=null) {  channel.disconnect();}
			if(session!=null) { session.disconnect();}
		}
	}

	public void almacenarDocumento(String documentoBase64, String host, int puerto, String usuario, String password,
			String rutaDestino) throws JSchException, SftpException, IOException {

		// Decodificar Base64 a bytes
		byte[] contenidoBytes = Base64.decodeBase64(documentoBase64);

		// Establecer la conexión SSH
		JSch jsch = new JSch();
		Session sesion = jsch.getSession(usuario, host, puerto);
		sesion.setPassword(password);
		sesion.setConfig("StrictHostKeyChecking", "no");
		sesion.connect();

		// Transferir el contenido a través de SSH
		try (InputStream inputStream = new ByteArrayInputStream(contenidoBytes)) {
			ChannelSftp canalSftp = (ChannelSftp) sesion.openChannel("sftp");
			canalSftp.connect();
			canalSftp.put(inputStream, rutaDestino);
			canalSftp.disconnect();
		} finally {
			sesion.disconnect();
		}
	}
	
	public double calcularPorcentaje(int valorEspecifico, int total) {
        // Verificar si el total es mayor que cero para evitar divisiones por cero
        if (total > 0) {
            return ((double) valorEspecifico / total) * 100;
        } else {
            // En caso de que el total sea cero, el porcentaje no tiene sentido, se podría manejar de otra manera según el caso
        	return 0.0;
        }
    }

}
