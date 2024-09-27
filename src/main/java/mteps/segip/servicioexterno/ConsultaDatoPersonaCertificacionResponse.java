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
 *         &lt;element name="ConsultaDatoPersonaCertificacionResult" type="{http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno}RespuestaConsultaCertificacion" minOccurs="0"/&gt;
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
    "consultaDatoPersonaCertificacionResult"
})
@XmlRootElement(name = "ConsultaDatoPersonaCertificacionResponse")
public class ConsultaDatoPersonaCertificacionResponse {

    @XmlElementRef(name = "ConsultaDatoPersonaCertificacionResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<RespuestaConsultaCertificacion> consultaDatoPersonaCertificacionResult;

    /**
     * Obtiene el valor de la propiedad consultaDatoPersonaCertificacionResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RespuestaConsultaCertificacion }{@code >}
     *     
     */
    public JAXBElement<RespuestaConsultaCertificacion> getConsultaDatoPersonaCertificacionResult() {
        return consultaDatoPersonaCertificacionResult;
    }

    /**
     * Define el valor de la propiedad consultaDatoPersonaCertificacionResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RespuestaConsultaCertificacion }{@code >}
     *     
     */
    public void setConsultaDatoPersonaCertificacionResult(JAXBElement<RespuestaConsultaCertificacion> value) {
        this.consultaDatoPersonaCertificacionResult = value;
    }

}
