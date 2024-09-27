package mteps.segip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import mteps.config.Resultado;
import mteps.segip.entity.ContrastacionPersona;
import mteps.segip.entity.datoPersonaCertificacion;
import mteps.segip.servicioexterno.ConsultaDatoPersonaCertificacion;
import mteps.segip.servicioexterno.ConsultaDatoPersonaCertificacionResponse;
import mteps.segip.servicioexterno.ConsultaDatoPersonaContrastacion;
import mteps.segip.servicioexterno.ConsultaDatoPersonaContrastacionResponse;
import mteps.segip.servicioexterno.ObjectFactory;
@Service
public class SegipCORE {
	
	@Autowired
	private Jaxb2Marshaller marshaller;
	
	private WebServiceTemplate template;
	
	private ObjectFactory segipConsulta = new ObjectFactory();
	
	@Value("{$segip.servicio}")
	private String servicio;
	
	public Resultado certificaPersona(datoPersonaCertificacion vObjDatosPersonaCertificacion) {
		Resultado vObjResultado = new Resultado();
		ConsultaDatoPersonaCertificacion request = new ConsultaDatoPersonaCertificacion();
		
		ConsultaDatoPersonaCertificacionResponse requestCertificacion = null;
		String servicios= "test";
		if(servicio.equals("prod")) {
			request.setPCodigoInstitucion(182);
			request.setPUsuario(segipConsulta.createConsultaDatoPersonaCertificacionPUsuario(""));
			request.setPContrasenia(segipConsulta.createConsultaDatoPersonaCertificacionPContrasenia(""));
			request.setPClaveAccesoUsuarioFinal(segipConsulta.createConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal(""));
			request.setPNumeroDocumento(segipConsulta.createConsultaDatoPersonaCertificacionPNumeroDocumento(vObjDatosPersonaCertificacion.getNumeroDocumento()));
			
			if (vObjDatosPersonaCertificacion.getComplemento() != null) {
				request.setPComplemento(segipConsulta.createConsultaDatoPersonaCertificacionPComplemento(vObjDatosPersonaCertificacion.getComplemento()));
			}else {
				request.setPComplemento(segipConsulta.createConsultaDatoPersonaCertificacionPComplemento(""));
			}
			
			request.setPNombre(segipConsulta.createConsultaDatoPersonaCertificacionPNombre(vObjDatosPersonaCertificacion.getNombre()));
			request.setPPrimerApellido(segipConsulta.createConsultaDatoPersonaCertificacionPPrimerApellido(vObjDatosPersonaCertificacion.getPrimerApellido()));
			request.setPSegundoApellido(segipConsulta.createConsultaDatoPersonaCertificacionPSegundoApellido(vObjDatosPersonaCertificacion.getSegundoApellido()));
			request.setPFechaNacimiento(segipConsulta.createConsultaDatoPersonaCertificacionPFechaNacimiento(vObjDatosPersonaCertificacion.getFechaNacimiento()));
			
			template = new WebServiceTemplate(marshaller);
			
		//	requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("http://172.30.4.171:86/ServicioExternoInstitucion.svc", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
			requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("https://wsconsultaruisegip.segip.gob.bo/ServicioExternoInstitucion.svc", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));

		}else if (servicios.equals("test")) {
			
			//Entorno de TESTING - SEGIP
			
			request.setPCodigoInstitucion(244);
			request.setPUsuario(segipConsulta.createConsultaDatoPersonaCertificacionPUsuario("miguel.calzada"));
			request.setPContrasenia(segipConsulta.createConsultaDatoPersonaContrastacionPContrasenia("Calzada2021"));
			request.setPClaveAccesoUsuarioFinal(segipConsulta.createConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal("R296367089306"));
			
			request.setPNumeroDocumento(segipConsulta.createConsultaDatoPersonaCertificacionPNumeroDocumento(vObjDatosPersonaCertificacion.getNumeroDocumento()));
			if (vObjDatosPersonaCertificacion.getComplemento() != null) {
				request.setPComplemento(segipConsulta.createConsultaDatoPersonaCertificacionPComplemento(vObjDatosPersonaCertificacion.getComplemento()));
			}
			request.setPNombre(segipConsulta.createConsultaDatoPersonaCertificacionPNombre(vObjDatosPersonaCertificacion.getNombre()));
			request.setPPrimerApellido(segipConsulta.createConsultaDatoPersonaCertificacionPPrimerApellido(vObjDatosPersonaCertificacion.getPrimerApellido()));
			request.setPSegundoApellido(segipConsulta.createConsultaDatoPersonaCertificacionPSegundoApellido(vObjDatosPersonaCertificacion.getSegundoApellido()));
			request.setPFechaNacimiento(segipConsulta.createConsultaDatoPersonaCertificacionPFechaNacimiento(vObjDatosPersonaCertificacion.getFechaNacimiento()));
			
			
			try {	
				template = new WebServiceTemplate(marshaller);
				
		//	requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("https://testconsultarui.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
		//		requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("https://wsconsultaruisegip.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("https://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
				requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("https://wsconsultarui.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
				
			vObjResultado.codigoResultado=200;
			vObjResultado.correcto=false;
			vObjResultado.notificacion= "correcto";
			vObjResultado.datoAdicional = requestCertificacion;
			}catch (Exception e) {
				// TODO: handle exception 
				
				vObjResultado.codigoResultado=200;
				vObjResultado.correcto=false;
				vObjResultado.notificacion= e.getMessage();
				vObjResultado.datoAdicional = requestCertificacion;
			}
		}
		
		return vObjResultado;
		
		
	}
	
	
	public Resultado contrastacionPersona(ContrastacionPersona vObjDatosPersonaCertificacion) throws JsonProcessingException {
		Resultado vObjResultado = new Resultado();
		ConsultaDatoPersonaContrastacion request= new ConsultaDatoPersonaContrastacion();
		
		ConsultaDatoPersonaContrastacionResponse requestCertificacion = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vObjDatosPersonaCertificacion);
			//Entorno de TESTING - SEGIP
			
			request.setPCodigoInstitucion(244);
			request.setPUsuario(segipConsulta.createConsultaDatoPersonaContrastacionPUsuario("miguel.calzada"));
			request.setPContrasenia(segipConsulta.createConsultaDatoPersonaContrastacionPContrasenia("Calzada2021"));
			request.setPClaveAccesoUsuarioFinal(segipConsulta.createConsultaDatoPersonaContrastacionPClaveAccesoUsuarioFinal("R296367089306"));
			request.setPNumeroAutorizacion(segipConsulta.createConsultaDatoPersonaContrastacionPNumeroAutorizacion(""));
			request.setPListaCampo(segipConsulta.createConsultaDatoPersonaContrastacionPListaCampo(json));
			request.setPTipoPersona(1);
			try {	
				template = new WebServiceTemplate(marshaller);
				
		//	requestCertificacion = ( ConsultaDatoPersonaCertificacionResponse) template.marshalSendAndReceive("https://testconsultarui.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
				requestCertificacion = ( ConsultaDatoPersonaContrastacionResponse) template.marshalSendAndReceive("https://wsconsultaruisegip.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("https://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaCertificacion"));
		//		requestCertificacion = ( ConsultaDatoPersonaContrastacionResponse) template.marshalSendAndReceive("https://wsconsultarui.segip.gob.bo/ServicioExternoInstitucion.svc?wsdl", request, new SoapActionCallback("http://tempuri.org/IServicioExternoInstitucion/ConsultaDatoPersonaContrastacion"));
				
			vObjResultado.codigoResultado=200;
			vObjResultado.correcto=false;
			vObjResultado.notificacion= "correcto";
			vObjResultado.datoAdicional = requestCertificacion;
			}catch (Exception e) {
				// TODO: handle exception 
				
				vObjResultado.codigoResultado=100;
				vObjResultado.correcto=false;
				vObjResultado.notificacion= e.getMessage();
				vObjResultado.datoAdicional = requestCertificacion;
			
		}
		
		return vObjResultado;
		
		
	}
	
}
