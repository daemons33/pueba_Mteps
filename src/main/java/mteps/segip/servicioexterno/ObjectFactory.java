//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.11.08 a las 02:49:08 PM BOT 
//


package mteps.segip.servicioexterno;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pge.soap.api.servicioexterno package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _RespuestaConsultaCertificacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "RespuestaConsultaCertificacion");
    private final static QName _RespuestaConsultaJson_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "RespuestaConsultaJson");
    private final static QName _RespuestaConsultaContrastacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "RespuestaConsultaContrastacion");
    private final static QName _RespuestaVerificacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "RespuestaVerificacion");
    private final static QName _RespuestaDocumento_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "RespuestaDocumento");
    private final static QName _ResultadoExterno_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "ResultadoExterno");
    private final static QName _BaseResultado_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "BaseResultado");
    private final static QName _ConsultaDatoPersonaCertificacionPUsuario_QNAME = new QName("http://tempuri.org/", "pUsuario");
    private final static QName _ConsultaDatoPersonaCertificacionPContrasenia_QNAME = new QName("http://tempuri.org/", "pContrasenia");
    private final static QName _ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME = new QName("http://tempuri.org/", "pClaveAccesoUsuarioFinal");
    private final static QName _ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME = new QName("http://tempuri.org/", "pNumeroAutorizacion");
    private final static QName _ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME = new QName("http://tempuri.org/", "pNumeroDocumento");
    private final static QName _ConsultaDatoPersonaCertificacionPComplemento_QNAME = new QName("http://tempuri.org/", "pComplemento");
    private final static QName _ConsultaDatoPersonaCertificacionPNombre_QNAME = new QName("http://tempuri.org/", "pNombre");
    private final static QName _ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME = new QName("http://tempuri.org/", "pPrimerApellido");
    private final static QName _ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME = new QName("http://tempuri.org/", "pSegundoApellido");
    private final static QName _ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME = new QName("http://tempuri.org/", "pFechaNacimiento");
    private final static QName _ConsultaDatoPersonaCertificacionResponseConsultaDatoPersonaCertificacionResult_QNAME = new QName("http://tempuri.org/", "ConsultaDatoPersonaCertificacionResult");
    private final static QName _ConsultaDatoPersonaEnJsonResponseConsultaDatoPersonaEnJsonResult_QNAME = new QName("http://tempuri.org/", "ConsultaDatoPersonaEnJsonResult");
    private final static QName _ConsultaDatoPersonaContrastacionPListaCampo_QNAME = new QName("http://tempuri.org/", "pListaCampo");
    private final static QName _ConsultaDatoPersonaContrastacionResponseConsultaDatoPersonaContrastacionResult_QNAME = new QName("http://tempuri.org/", "ConsultaDatoPersonaContrastacionResult");
    private final static QName _VerificacionImagenDocumentoCiPNombreUsuario_QNAME = new QName("http://tempuri.org/", "pNombreUsuario");
    private final static QName _VerificacionImagenDocumentoCiPCodigoUnico_QNAME = new QName("http://tempuri.org/", "pCodigoUnico");
    private final static QName _VerificacionImagenDocumentoCiResponseVerificacionImagenDocumentoCiResult_QNAME = new QName("http://tempuri.org/", "VerificacionImagenDocumentoCiResult");
    private final static QName _VerificacionImagenDocumentoCieResponseVerificacionImagenDocumentoCieResult_QNAME = new QName("http://tempuri.org/", "VerificacionImagenDocumentoCieResult");
    private final static QName _VerificacionImagenDocumentoLicPNumeroLicencia_QNAME = new QName("http://tempuri.org/", "pNumeroLicencia");
    private final static QName _VerificacionImagenDocumentoLicResponseVerificacionImagenDocumentoLicResult_QNAME = new QName("http://tempuri.org/", "VerificacionImagenDocumentoLicResult");
    private final static QName _VerificacionImagenDocumentoLiceResponseVerificacionImagenDocumentoLiceResult_QNAME = new QName("http://tempuri.org/", "VerificacionImagenDocumentoLiceResult");
    private final static QName _VerificacionDatoDocumentoCiPNombres_QNAME = new QName("http://tempuri.org/", "pNombres");
    private final static QName _VerificacionDatoDocumentoCiResponseVerificacionDatoDocumentoCiResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoDocumentoCiResult");
    private final static QName _VerificacionDatoDocumentoCieResponseVerificacionDatoDocumentoCieResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoDocumentoCieResult");
    private final static QName _VerificacionDatoDocumentoLicPCategoriaLicencia_QNAME = new QName("http://tempuri.org/", "pCategoriaLicencia");
    private final static QName _VerificacionDatoDocumentoLicResponseVerificacionDatoDocumentoLicResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoDocumentoLicResult");
    private final static QName _VerificacionDatoDocumentoLiceResponseVerificacionDatoDocumentoLiceResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoDocumentoLiceResult");
    private final static QName _VerificacionDatoImagenDocumentoCiResponseVerificacionDatoImagenDocumentoCiResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoImagenDocumentoCiResult");
    private final static QName _VerificacionDatoImagenDocumentoCieResponseVerificacionDatoImagenDocumentoCieResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoImagenDocumentoCieResult");
    private final static QName _VerificacionDatoImagenDocumentoLicResponseVerificacionDatoImagenDocumentoLicResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoImagenDocumentoLicResult");
    private final static QName _VerificacionDatoImagenDocumentoLiceResponseVerificacionDatoImagenDocumentoLiceResult_QNAME = new QName("http://tempuri.org/", "VerificacionDatoImagenDocumentoLiceResult");
    private final static QName _ObtenerDocumentoPorCodigoUnicoPNumeroUnicoDocumento_QNAME = new QName("http://tempuri.org/", "pNumeroUnicoDocumento");
    private final static QName _ObtenerDocumentoPorCodigoUnicoResponseObtenerDocumentoPorCodigoUnicoResult_QNAME = new QName("http://tempuri.org/", "ObtenerDocumentoPorCodigoUnicoResult");
    private final static QName _BaseResultadoMensaje_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "Mensaje");
    private final static QName _BaseResultadoTipoMensaje_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "TipoMensaje");
    private final static QName _ResultadoExternoCodigoUnico_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "CodigoUnico");
    private final static QName _ResultadoExternoDescripcionRespuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", "DescripcionRespuesta");
    private final static QName _RespuestaDocumentoReporte_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "Reporte");
    private final static QName _RespuestaConsultaContrastacionContrastacionEnFormatoJson_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "ContrastacionEnFormatoJson");
    private final static QName _RespuestaConsultaJsonDatosPersonaEnFormatoJson_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "DatosPersonaEnFormatoJson");
    private final static QName _RespuestaConsultaJsonFotografia_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "Fotografia");
    private final static QName _RespuestaConsultaCertificacionReporteCertificacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", "ReporteCertificacion");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pge.soap.api.servicioexterno
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaCertificacion }
     * 
     */
    public ConsultaDatoPersonaCertificacion createConsultaDatoPersonaCertificacion() {
        return new ConsultaDatoPersonaCertificacion();
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaCertificacionResponse }
     * 
     */
    public ConsultaDatoPersonaCertificacionResponse createConsultaDatoPersonaCertificacionResponse() {
        return new ConsultaDatoPersonaCertificacionResponse();
    }

    /**
     * Create an instance of {@link RespuestaConsultaCertificacion }
     * 
     */
    public RespuestaConsultaCertificacion createRespuestaConsultaCertificacion() {
        return new RespuestaConsultaCertificacion();
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaEnJson }
     * 
     */
    public ConsultaDatoPersonaEnJson createConsultaDatoPersonaEnJson() {
        return new ConsultaDatoPersonaEnJson();
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaEnJsonResponse }
     * 
     */
    public ConsultaDatoPersonaEnJsonResponse createConsultaDatoPersonaEnJsonResponse() {
        return new ConsultaDatoPersonaEnJsonResponse();
    }

    /**
     * Create an instance of {@link RespuestaConsultaJson }
     * 
     */
    public RespuestaConsultaJson createRespuestaConsultaJson() {
        return new RespuestaConsultaJson();
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaContrastacion }
     * 
     */
    public ConsultaDatoPersonaContrastacion createConsultaDatoPersonaContrastacion() {
        return new ConsultaDatoPersonaContrastacion();
    }

    /**
     * Create an instance of {@link ConsultaDatoPersonaContrastacionResponse }
     * 
     */
    public ConsultaDatoPersonaContrastacionResponse createConsultaDatoPersonaContrastacionResponse() {
        return new ConsultaDatoPersonaContrastacionResponse();
    }

    /**
     * Create an instance of {@link RespuestaConsultaContrastacion }
     * 
     */
    public RespuestaConsultaContrastacion createRespuestaConsultaContrastacion() {
        return new RespuestaConsultaContrastacion();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoCi }
     * 
     */
    public VerificacionImagenDocumentoCi createVerificacionImagenDocumentoCi() {
        return new VerificacionImagenDocumentoCi();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoCiResponse }
     * 
     */
    public VerificacionImagenDocumentoCiResponse createVerificacionImagenDocumentoCiResponse() {
        return new VerificacionImagenDocumentoCiResponse();
    }

    /**
     * Create an instance of {@link RespuestaVerificacion }
     * 
     */
    public RespuestaVerificacion createRespuestaVerificacion() {
        return new RespuestaVerificacion();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoCie }
     * 
     */
    public VerificacionImagenDocumentoCie createVerificacionImagenDocumentoCie() {
        return new VerificacionImagenDocumentoCie();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoCieResponse }
     * 
     */
    public VerificacionImagenDocumentoCieResponse createVerificacionImagenDocumentoCieResponse() {
        return new VerificacionImagenDocumentoCieResponse();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoLic }
     * 
     */
    public VerificacionImagenDocumentoLic createVerificacionImagenDocumentoLic() {
        return new VerificacionImagenDocumentoLic();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoLicResponse }
     * 
     */
    public VerificacionImagenDocumentoLicResponse createVerificacionImagenDocumentoLicResponse() {
        return new VerificacionImagenDocumentoLicResponse();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoLice }
     * 
     */
    public VerificacionImagenDocumentoLice createVerificacionImagenDocumentoLice() {
        return new VerificacionImagenDocumentoLice();
    }

    /**
     * Create an instance of {@link VerificacionImagenDocumentoLiceResponse }
     * 
     */
    public VerificacionImagenDocumentoLiceResponse createVerificacionImagenDocumentoLiceResponse() {
        return new VerificacionImagenDocumentoLiceResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoCi }
     * 
     */
    public VerificacionDatoDocumentoCi createVerificacionDatoDocumentoCi() {
        return new VerificacionDatoDocumentoCi();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoCiResponse }
     * 
     */
    public VerificacionDatoDocumentoCiResponse createVerificacionDatoDocumentoCiResponse() {
        return new VerificacionDatoDocumentoCiResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoCie }
     * 
     */
    public VerificacionDatoDocumentoCie createVerificacionDatoDocumentoCie() {
        return new VerificacionDatoDocumentoCie();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoCieResponse }
     * 
     */
    public VerificacionDatoDocumentoCieResponse createVerificacionDatoDocumentoCieResponse() {
        return new VerificacionDatoDocumentoCieResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoLic }
     * 
     */
    public VerificacionDatoDocumentoLic createVerificacionDatoDocumentoLic() {
        return new VerificacionDatoDocumentoLic();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoLicResponse }
     * 
     */
    public VerificacionDatoDocumentoLicResponse createVerificacionDatoDocumentoLicResponse() {
        return new VerificacionDatoDocumentoLicResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoLice }
     * 
     */
    public VerificacionDatoDocumentoLice createVerificacionDatoDocumentoLice() {
        return new VerificacionDatoDocumentoLice();
    }

    /**
     * Create an instance of {@link VerificacionDatoDocumentoLiceResponse }
     * 
     */
    public VerificacionDatoDocumentoLiceResponse createVerificacionDatoDocumentoLiceResponse() {
        return new VerificacionDatoDocumentoLiceResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoCi }
     * 
     */
    public VerificacionDatoImagenDocumentoCi createVerificacionDatoImagenDocumentoCi() {
        return new VerificacionDatoImagenDocumentoCi();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoCiResponse }
     * 
     */
    public VerificacionDatoImagenDocumentoCiResponse createVerificacionDatoImagenDocumentoCiResponse() {
        return new VerificacionDatoImagenDocumentoCiResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoCie }
     * 
     */
    public VerificacionDatoImagenDocumentoCie createVerificacionDatoImagenDocumentoCie() {
        return new VerificacionDatoImagenDocumentoCie();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoCieResponse }
     * 
     */
    public VerificacionDatoImagenDocumentoCieResponse createVerificacionDatoImagenDocumentoCieResponse() {
        return new VerificacionDatoImagenDocumentoCieResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoLic }
     * 
     */
    public VerificacionDatoImagenDocumentoLic createVerificacionDatoImagenDocumentoLic() {
        return new VerificacionDatoImagenDocumentoLic();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoLicResponse }
     * 
     */
    public VerificacionDatoImagenDocumentoLicResponse createVerificacionDatoImagenDocumentoLicResponse() {
        return new VerificacionDatoImagenDocumentoLicResponse();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoLice }
     * 
     */
    public VerificacionDatoImagenDocumentoLice createVerificacionDatoImagenDocumentoLice() {
        return new VerificacionDatoImagenDocumentoLice();
    }

    /**
     * Create an instance of {@link VerificacionDatoImagenDocumentoLiceResponse }
     * 
     */
    public VerificacionDatoImagenDocumentoLiceResponse createVerificacionDatoImagenDocumentoLiceResponse() {
        return new VerificacionDatoImagenDocumentoLiceResponse();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoPorCodigoUnico }
     * 
     */
    public ObtenerDocumentoPorCodigoUnico createObtenerDocumentoPorCodigoUnico() {
        return new ObtenerDocumentoPorCodigoUnico();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoPorCodigoUnicoResponse }
     * 
     */
    public ObtenerDocumentoPorCodigoUnicoResponse createObtenerDocumentoPorCodigoUnicoResponse() {
        return new ObtenerDocumentoPorCodigoUnicoResponse();
    }

    /**
     * Create an instance of {@link RespuestaDocumento }
     * 
     */
    public RespuestaDocumento createRespuestaDocumento() {
        return new RespuestaDocumento();
    }

    /**
     * Create an instance of {@link ResultadoExterno }
     * 
     */
    public ResultadoExterno createResultadoExterno() {
        return new ResultadoExterno();
    }

    /**
     * Create an instance of {@link BaseResultado }
     * 
     */
    public BaseResultado createBaseResultado() {
        return new BaseResultado();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaCertificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "RespuestaConsultaCertificacion")
    public JAXBElement<RespuestaConsultaCertificacion> createRespuestaConsultaCertificacion(RespuestaConsultaCertificacion value) {
        return new JAXBElement<RespuestaConsultaCertificacion>(_RespuestaConsultaCertificacion_QNAME, RespuestaConsultaCertificacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaJson }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "RespuestaConsultaJson")
    public JAXBElement<RespuestaConsultaJson> createRespuestaConsultaJson(RespuestaConsultaJson value) {
        return new JAXBElement<RespuestaConsultaJson>(_RespuestaConsultaJson_QNAME, RespuestaConsultaJson.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaContrastacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "RespuestaConsultaContrastacion")
    public JAXBElement<RespuestaConsultaContrastacion> createRespuestaConsultaContrastacion(RespuestaConsultaContrastacion value) {
        return new JAXBElement<RespuestaConsultaContrastacion>(_RespuestaConsultaContrastacion_QNAME, RespuestaConsultaContrastacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "RespuestaVerificacion")
    public JAXBElement<RespuestaVerificacion> createRespuestaVerificacion(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_RespuestaVerificacion_QNAME, RespuestaVerificacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "RespuestaDocumento")
    public JAXBElement<RespuestaDocumento> createRespuestaDocumento(RespuestaDocumento value) {
        return new JAXBElement<RespuestaDocumento>(_RespuestaDocumento_QNAME, RespuestaDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoExterno }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "ResultadoExterno")
    public JAXBElement<ResultadoExterno> createResultadoExterno(ResultadoExterno value) {
        return new JAXBElement<ResultadoExterno>(_ResultadoExterno_QNAME, ResultadoExterno.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseResultado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "BaseResultado")
    public JAXBElement<BaseResultado> createBaseResultado(BaseResultado value) {
        return new JAXBElement<BaseResultado>(_BaseResultado_QNAME, BaseResultado.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pUsuario", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPUsuario(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPUsuario_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombre", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPNombre(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNombre_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = ConsultaDatoPersonaCertificacion.class)
    public JAXBElement<String> createConsultaDatoPersonaCertificacionPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, ConsultaDatoPersonaCertificacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaCertificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ConsultaDatoPersonaCertificacionResult", scope = ConsultaDatoPersonaCertificacionResponse.class)
    public JAXBElement<RespuestaConsultaCertificacion> createConsultaDatoPersonaCertificacionResponseConsultaDatoPersonaCertificacionResult(RespuestaConsultaCertificacion value) {
        return new JAXBElement<RespuestaConsultaCertificacion>(_ConsultaDatoPersonaCertificacionResponseConsultaDatoPersonaCertificacionResult_QNAME, RespuestaConsultaCertificacion.class, ConsultaDatoPersonaCertificacionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pUsuario", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPUsuario(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPUsuario_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombre", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPNombre(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNombre_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = ConsultaDatoPersonaEnJson.class)
    public JAXBElement<String> createConsultaDatoPersonaEnJsonPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, ConsultaDatoPersonaEnJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaJson }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ConsultaDatoPersonaEnJsonResult", scope = ConsultaDatoPersonaEnJsonResponse.class)
    public JAXBElement<RespuestaConsultaJson> createConsultaDatoPersonaEnJsonResponseConsultaDatoPersonaEnJsonResult(RespuestaConsultaJson value) {
        return new JAXBElement<RespuestaConsultaJson>(_ConsultaDatoPersonaEnJsonResponseConsultaDatoPersonaEnJsonResult_QNAME, RespuestaConsultaJson.class, ConsultaDatoPersonaEnJsonResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pUsuario", scope = ConsultaDatoPersonaContrastacion.class)
    public JAXBElement<String> createConsultaDatoPersonaContrastacionPUsuario(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPUsuario_QNAME, String.class, ConsultaDatoPersonaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = ConsultaDatoPersonaContrastacion.class)
    public JAXBElement<String> createConsultaDatoPersonaContrastacionPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, ConsultaDatoPersonaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = ConsultaDatoPersonaContrastacion.class)
    public JAXBElement<String> createConsultaDatoPersonaContrastacionPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, ConsultaDatoPersonaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = ConsultaDatoPersonaContrastacion.class)
    public JAXBElement<String> createConsultaDatoPersonaContrastacionPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, ConsultaDatoPersonaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pListaCampo", scope = ConsultaDatoPersonaContrastacion.class)
    public JAXBElement<String> createConsultaDatoPersonaContrastacionPListaCampo(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaContrastacionPListaCampo_QNAME, String.class, ConsultaDatoPersonaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConsultaContrastacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ConsultaDatoPersonaContrastacionResult", scope = ConsultaDatoPersonaContrastacionResponse.class)
    public JAXBElement<RespuestaConsultaContrastacion> createConsultaDatoPersonaContrastacionResponseConsultaDatoPersonaContrastacionResult(RespuestaConsultaContrastacion value) {
        return new JAXBElement<RespuestaConsultaContrastacion>(_ConsultaDatoPersonaContrastacionResponseConsultaDatoPersonaContrastacionResult_QNAME, RespuestaConsultaContrastacion.class, ConsultaDatoPersonaContrastacionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiPNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionImagenDocumentoCiResult", scope = VerificacionImagenDocumentoCiResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionImagenDocumentoCiResponseVerificacionImagenDocumentoCiResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionImagenDocumentoCiResponseVerificacionImagenDocumentoCiResult_QNAME, RespuestaVerificacion.class, VerificacionImagenDocumentoCiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionImagenDocumentoCiePNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionImagenDocumentoCieResult", scope = VerificacionImagenDocumentoCieResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionImagenDocumentoCieResponseVerificacionImagenDocumentoCieResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionImagenDocumentoCieResponseVerificacionImagenDocumentoCieResult_QNAME, RespuestaVerificacion.class, VerificacionImagenDocumentoCieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicPNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionImagenDocumentoLicResult", scope = VerificacionImagenDocumentoLicResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionImagenDocumentoLicResponseVerificacionImagenDocumentoLicResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionImagenDocumentoLicResponseVerificacionImagenDocumentoLicResult_QNAME, RespuestaVerificacion.class, VerificacionImagenDocumentoLicResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionImagenDocumentoLicePNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionImagenDocumentoLiceResult", scope = VerificacionImagenDocumentoLiceResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionImagenDocumentoLiceResponseVerificacionImagenDocumentoLiceResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionImagenDocumentoLiceResponseVerificacionImagenDocumentoLiceResult_QNAME, RespuestaVerificacion.class, VerificacionImagenDocumentoLiceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoDocumentoCiResult", scope = VerificacionDatoDocumentoCiResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoDocumentoCiResponseVerificacionDatoDocumentoCiResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoDocumentoCiResponseVerificacionDatoDocumentoCiResult_QNAME, RespuestaVerificacion.class, VerificacionDatoDocumentoCiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoDocumentoCiePFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoDocumentoCieResult", scope = VerificacionDatoDocumentoCieResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoDocumentoCieResponseVerificacionDatoDocumentoCieResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoDocumentoCieResponseVerificacionDatoDocumentoCieResult_QNAME, RespuestaVerificacion.class, VerificacionDatoDocumentoCieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCategoriaLicencia", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPCategoriaLicencia(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoLicPCategoriaLicencia_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoDocumentoLicResult", scope = VerificacionDatoDocumentoLicResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoDocumentoLicResponseVerificacionDatoDocumentoLicResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoDocumentoLicResponseVerificacionDatoDocumentoLicResult_QNAME, RespuestaVerificacion.class, VerificacionDatoDocumentoLicResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCategoriaLicencia", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePCategoriaLicencia(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoLicPCategoriaLicencia_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoDocumentoLicePFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoDocumentoLiceResult", scope = VerificacionDatoDocumentoLiceResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoDocumentoLiceResponseVerificacionDatoDocumentoLiceResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoDocumentoLiceResponseVerificacionDatoDocumentoLiceResult_QNAME, RespuestaVerificacion.class, VerificacionDatoDocumentoLiceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoImagenDocumentoCi.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoImagenDocumentoCi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoImagenDocumentoCiResult", scope = VerificacionDatoImagenDocumentoCiResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoImagenDocumentoCiResponseVerificacionDatoImagenDocumentoCiResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoImagenDocumentoCiResponseVerificacionDatoImagenDocumentoCiResult_QNAME, RespuestaVerificacion.class, VerificacionDatoImagenDocumentoCiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroDocumento", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePNumeroDocumento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroDocumento_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pComplemento", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePComplemento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPComplemento_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoImagenDocumentoCie.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoCiePFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoImagenDocumentoCie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoImagenDocumentoCieResult", scope = VerificacionDatoImagenDocumentoCieResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoImagenDocumentoCieResponseVerificacionDatoImagenDocumentoCieResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoImagenDocumentoCieResponseVerificacionDatoImagenDocumentoCieResult_QNAME, RespuestaVerificacion.class, VerificacionDatoImagenDocumentoCieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCategoriaLicencia", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPCategoriaLicencia(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoLicPCategoriaLicencia_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoImagenDocumentoLic.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicPFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoImagenDocumentoLic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoImagenDocumentoLicResult", scope = VerificacionDatoImagenDocumentoLicResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoImagenDocumentoLicResponseVerificacionDatoImagenDocumentoLicResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoImagenDocumentoLicResponseVerificacionDatoImagenDocumentoLicResult_QNAME, RespuestaVerificacion.class, VerificacionDatoImagenDocumentoLicResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroAutorizacion", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePNumeroAutorizacion(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPNumeroAutorizacion_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCodigoUnico", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePCodigoUnico(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPCodigoUnico_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroLicencia", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePNumeroLicencia(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoLicPNumeroLicencia_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pCategoriaLicencia", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePCategoriaLicencia(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoLicPCategoriaLicencia_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombres", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePNombres(String value) {
        return new JAXBElement<String>(_VerificacionDatoDocumentoCiPNombres_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pPrimerApellido", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePPrimerApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPPrimerApellido_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pSegundoApellido", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePSegundoApellido(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPSegundoApellido_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pFechaNacimiento", scope = VerificacionDatoImagenDocumentoLice.class)
    public JAXBElement<String> createVerificacionDatoImagenDocumentoLicePFechaNacimiento(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPFechaNacimiento_QNAME, String.class, VerificacionDatoImagenDocumentoLice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaVerificacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "VerificacionDatoImagenDocumentoLiceResult", scope = VerificacionDatoImagenDocumentoLiceResponse.class)
    public JAXBElement<RespuestaVerificacion> createVerificacionDatoImagenDocumentoLiceResponseVerificacionDatoImagenDocumentoLiceResult(RespuestaVerificacion value) {
        return new JAXBElement<RespuestaVerificacion>(_VerificacionDatoImagenDocumentoLiceResponseVerificacionDatoImagenDocumentoLiceResult_QNAME, RespuestaVerificacion.class, VerificacionDatoImagenDocumentoLiceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNombreUsuario", scope = ObtenerDocumentoPorCodigoUnico.class)
    public JAXBElement<String> createObtenerDocumentoPorCodigoUnicoPNombreUsuario(String value) {
        return new JAXBElement<String>(_VerificacionImagenDocumentoCiPNombreUsuario_QNAME, String.class, ObtenerDocumentoPorCodigoUnico.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pContrasenia", scope = ObtenerDocumentoPorCodigoUnico.class)
    public JAXBElement<String> createObtenerDocumentoPorCodigoUnicoPContrasenia(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPContrasenia_QNAME, String.class, ObtenerDocumentoPorCodigoUnico.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pClaveAccesoUsuarioFinal", scope = ObtenerDocumentoPorCodigoUnico.class)
    public JAXBElement<String> createObtenerDocumentoPorCodigoUnicoPClaveAccesoUsuarioFinal(String value) {
        return new JAXBElement<String>(_ConsultaDatoPersonaCertificacionPClaveAccesoUsuarioFinal_QNAME, String.class, ObtenerDocumentoPorCodigoUnico.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pNumeroUnicoDocumento", scope = ObtenerDocumentoPorCodigoUnico.class)
    public JAXBElement<String> createObtenerDocumentoPorCodigoUnicoPNumeroUnicoDocumento(String value) {
        return new JAXBElement<String>(_ObtenerDocumentoPorCodigoUnicoPNumeroUnicoDocumento_QNAME, String.class, ObtenerDocumentoPorCodigoUnico.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ObtenerDocumentoPorCodigoUnicoResult", scope = ObtenerDocumentoPorCodigoUnicoResponse.class)
    public JAXBElement<RespuestaDocumento> createObtenerDocumentoPorCodigoUnicoResponseObtenerDocumentoPorCodigoUnicoResult(RespuestaDocumento value) {
        return new JAXBElement<RespuestaDocumento>(_ObtenerDocumentoPorCodigoUnicoResponseObtenerDocumentoPorCodigoUnicoResult_QNAME, RespuestaDocumento.class, ObtenerDocumentoPorCodigoUnicoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "Mensaje", scope = BaseResultado.class)
    public JAXBElement<String> createBaseResultadoMensaje(String value) {
        return new JAXBElement<String>(_BaseResultadoMensaje_QNAME, String.class, BaseResultado.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "TipoMensaje", scope = BaseResultado.class)
    public JAXBElement<String> createBaseResultadoTipoMensaje(String value) {
        return new JAXBElement<String>(_BaseResultadoTipoMensaje_QNAME, String.class, BaseResultado.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "CodigoUnico", scope = ResultadoExterno.class)
    public JAXBElement<String> createResultadoExternoCodigoUnico(String value) {
        return new JAXBElement<String>(_ResultadoExternoCodigoUnico_QNAME, String.class, ResultadoExterno.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", name = "DescripcionRespuesta", scope = ResultadoExterno.class)
    public JAXBElement<String> createResultadoExternoDescripcionRespuesta(String value) {
        return new JAXBElement<String>(_ResultadoExternoDescripcionRespuesta_QNAME, String.class, ResultadoExterno.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "Reporte", scope = RespuestaDocumento.class)
    public JAXBElement<byte[]> createRespuestaDocumentoReporte(byte[] value) {
        return new JAXBElement<byte[]>(_RespuestaDocumentoReporte_QNAME, byte[].class, RespuestaDocumento.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "Reporte", scope = RespuestaVerificacion.class)
    public JAXBElement<byte[]> createRespuestaVerificacionReporte(byte[] value) {
        return new JAXBElement<byte[]>(_RespuestaDocumentoReporte_QNAME, byte[].class, RespuestaVerificacion.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "ContrastacionEnFormatoJson", scope = RespuestaConsultaContrastacion.class)
    public JAXBElement<String> createRespuestaConsultaContrastacionContrastacionEnFormatoJson(String value) {
        return new JAXBElement<String>(_RespuestaConsultaContrastacionContrastacionEnFormatoJson_QNAME, String.class, RespuestaConsultaContrastacion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "DatosPersonaEnFormatoJson", scope = RespuestaConsultaJson.class)
    public JAXBElement<String> createRespuestaConsultaJsonDatosPersonaEnFormatoJson(String value) {
        return new JAXBElement<String>(_RespuestaConsultaJsonDatosPersonaEnFormatoJson_QNAME, String.class, RespuestaConsultaJson.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "Fotografia", scope = RespuestaConsultaJson.class)
    public JAXBElement<byte[]> createRespuestaConsultaJsonFotografia(byte[] value) {
        return new JAXBElement<byte[]>(_RespuestaConsultaJsonFotografia_QNAME, byte[].class, RespuestaConsultaJson.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", name = "ReporteCertificacion", scope = RespuestaConsultaCertificacion.class)
    public JAXBElement<byte[]> createRespuestaConsultaCertificacionReporteCertificacion(byte[] value) {
        return new JAXBElement<byte[]>(_RespuestaConsultaCertificacionReporteCertificacion_QNAME, byte[].class, RespuestaConsultaCertificacion.class, ((byte[]) value));
    }

}
