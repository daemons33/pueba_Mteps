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
 * <p>Clase Java para ResultadoExterno complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ResultadoExterno"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado}BaseResultado"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="CodigoUnico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DescripcionRespuesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoExterno", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", propOrder = {
    "codigoRespuesta",
    "codigoUnico",
    "descripcionRespuesta"
})
@XmlSeeAlso({
    RespuestaConsultaCertificacion.class,
    RespuestaConsultaJson.class,
    RespuestaConsultaContrastacion.class,
    RespuestaVerificacion.class
})
public class ResultadoExterno
    extends BaseResultado
{

    @XmlElement(name = "CodigoRespuesta")
    protected Integer codigoRespuesta;
    @XmlElementRef(name = "CodigoUnico", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codigoUnico;
    @XmlElementRef(name = "DescripcionRespuesta", namespace = "http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descripcionRespuesta;

    /**
     * Obtiene el valor de la propiedad codigoRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * Define el valor de la propiedad codigoRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoRespuesta(Integer value) {
        this.codigoRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoUnico.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodigoUnico() {
        return codigoUnico;
    }

    /**
     * Define el valor de la propiedad codigoUnico.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodigoUnico(JAXBElement<String> value) {
        this.codigoUnico = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    /**
     * Define el valor de la propiedad descripcionRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescripcionRespuesta(JAXBElement<String> value) {
        this.descripcionRespuesta = value;
    }

}
