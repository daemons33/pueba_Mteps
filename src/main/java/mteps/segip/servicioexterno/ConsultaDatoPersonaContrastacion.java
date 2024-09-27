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
 *         &lt;element name="pListaCampo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pTipoPersona" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
    "pListaCampo",
    "pTipoPersona"
})
@XmlRootElement(name = "ConsultaDatoPersonaContrastacion")
public class ConsultaDatoPersonaContrastacion {

    protected Integer pCodigoInstitucion;
    @XmlElementRef(name = "pUsuario", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pUsuario;
    @XmlElementRef(name = "pContrasenia", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pContrasenia;
    @XmlElementRef(name = "pClaveAccesoUsuarioFinal", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pClaveAccesoUsuarioFinal;
    @XmlElementRef(name = "pNumeroAutorizacion", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pNumeroAutorizacion;
    @XmlElementRef(name = "pListaCampo", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pListaCampo;
    protected Integer pTipoPersona;

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
     * Obtiene el valor de la propiedad pListaCampo.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPListaCampo() {
        return pListaCampo;
    }

    /**
     * Define el valor de la propiedad pListaCampo.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPListaCampo(JAXBElement<String> value) {
        this.pListaCampo = value;
    }

    /**
     * Obtiene el valor de la propiedad pTipoPersona.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPTipoPersona() {
        return pTipoPersona;
    }

    /**
     * Define el valor de la propiedad pTipoPersona.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPTipoPersona(Integer value) {
        this.pTipoPersona = value;
    }

}
