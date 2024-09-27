//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.11.08 a las 02:49:08 PM BOT 
//


package mteps.segip.servicioexterno;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pCodigoInstitucion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="pUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pContrasenia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pClaveAccesoUsuarioFinal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pNumeroAutorizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pNumeroDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pComplemento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pNombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pPrimerApellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pSegundoApellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pFechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pCodigoInstitucion",
    "pUsuario",
    "pContrasenia",
    "pClaveAccesoUsuarioFinal",
    "pNumeroAutorizacion",
    "pNumeroDocumento",
    "pComplemento",
    "pNombre",
    "pPrimerApellido",
    "pSegundoApellido",
    "pFechaNacimiento"
})
@XmlRootElement(name = "ConsultaDatoPersonaCertificacion")
public class ConsultaDatoPersonaCertificacion {

    protected Integer pCodigoInstitucion;
    @XmlElementRef(name = "pUsuario", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pUsuario;
    @XmlElementRef(name = "pContrasenia", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pContrasenia;
    @XmlElementRef(name = "pClaveAccesoUsuarioFinal", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pClaveAccesoUsuarioFinal;
    @XmlElementRef(name = "pNumeroAutorizacion", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pNumeroAutorizacion;
    @XmlElementRef(name = "pNumeroDocumento", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pNumeroDocumento;
    @XmlElementRef(name = "pComplemento", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pComplemento;
    @XmlElementRef(name = "pNombre", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pNombre;
    @XmlElementRef(name = "pPrimerApellido", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pPrimerApellido;
    @XmlElementRef(name = "pSegundoApellido", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pSegundoApellido;
    @XmlElementRef(name = "pFechaNacimiento", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pFechaNacimiento;

    /**
     * Obtiene el valor de la propiedad pCodigoInstitucion.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPCodigoInstitucion() {
        return pCodigoInstitucion;
    }

    /**
     * Define el valor de la propiedad pCodigoInstitucion.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPCodigoInstitucion(Integer value) {
        this.pCodigoInstitucion = value;
    }

    /**
     * Obtiene el valor de la propiedad pUsuario.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPUsuario() {
        return pUsuario;
    }

    /**
     * Define el valor de la propiedad pUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPUsuario(JAXBElement<String> value) {
        this.pUsuario = value;
    }

    /**
     * Obtiene el valor de la propiedad pContrasenia.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPContrasenia() {
        return pContrasenia;
    }

    /**
     * Define el valor de la propiedad pContrasenia.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPContrasenia(JAXBElement<String> value) {
        this.pContrasenia = value;
    }

    /**
     * Obtiene el valor de la propiedad pClaveAccesoUsuarioFinal.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPClaveAccesoUsuarioFinal() {
        return pClaveAccesoUsuarioFinal;
    }

    /**
     * Define el valor de la propiedad pClaveAccesoUsuarioFinal.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPClaveAccesoUsuarioFinal(JAXBElement<String> value) {
        this.pClaveAccesoUsuarioFinal = value;
    }

    /**
     * Obtiene el valor de la propiedad pNumeroAutorizacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPNumeroAutorizacion() {
        return pNumeroAutorizacion;
    }

    /**
     * Define el valor de la propiedad pNumeroAutorizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPNumeroAutorizacion(JAXBElement<String> value) {
        this.pNumeroAutorizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad pNumeroDocumento.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPNumeroDocumento() {
        return pNumeroDocumento;
    }

    /**
     * Define el valor de la propiedad pNumeroDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPNumeroDocumento(JAXBElement<String> value) {
        this.pNumeroDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad pComplemento.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPComplemento() {
        return pComplemento;
    }

    /**
     * Define el valor de la propiedad pComplemento.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPComplemento(JAXBElement<String> value) {
        this.pComplemento = value;
    }

    /**
     * Obtiene el valor de la propiedad pNombre.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPNombre() {
        return pNombre;
    }

    /**
     * Define el valor de la propiedad pNombre.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPNombre(JAXBElement<String> value) {
        this.pNombre = value;
    }

    /**
     * Obtiene el valor de la propiedad pPrimerApellido.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPPrimerApellido() {
        return pPrimerApellido;
    }

    /**
     * Define el valor de la propiedad pPrimerApellido.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPPrimerApellido(JAXBElement<String> value) {
        this.pPrimerApellido = value;
    }

    /**
     * Obtiene el valor de la propiedad pSegundoApellido.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPSegundoApellido() {
        return pSegundoApellido;
    }

    /**
     * Define el valor de la propiedad pSegundoApellido.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPSegundoApellido(JAXBElement<String> value) {
        this.pSegundoApellido = value;
    }

    /**
     * Obtiene el valor de la propiedad pFechaNacimiento.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPFechaNacimiento() {
        return pFechaNacimiento;
    }

    /**
     * Define el valor de la propiedad pFechaNacimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPFechaNacimiento(JAXBElement<String> value) {
        this.pFechaNacimiento = value;
    }

}
