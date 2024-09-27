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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para BaseResultado complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="BaseResultado"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EsValido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="Mensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TipoMensaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResultado", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", propOrder = {
    "esValido",
    "mensaje",
    "tipoMensaje"
})
@XmlSeeAlso({
    RespuestaDocumento.class,
    ResultadoExterno.class
})
public class BaseResultado {

    @XmlElement(name = "EsValido")
    protected Boolean esValido;
    @XmlElementRef(name = "Mensaje", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mensaje;
    @XmlElementRef(name = "TipoMensaje", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tipoMensaje;

    /**
     * Obtiene el valor de la propiedad esValido.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsValido() {
        return esValido;
    }

    /**
     * Define el valor de la propiedad esValido.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsValido(Boolean value) {
        this.esValido = value;
    }

    /**
     * Obtiene el valor de la propiedad mensaje.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMensaje() {
        return mensaje;
    }

    /**
     * Define el valor de la propiedad mensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMensaje(JAXBElement<String> value) {
        this.mensaje = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMensaje.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTipoMensaje() {
        return tipoMensaje;
    }

    /**
     * Define el valor de la propiedad tipoMensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTipoMensaje(JAXBElement<String> value) {
        this.tipoMensaje = value;
    }

}
