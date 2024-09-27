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
 *         &lt;element name="ConsultaDatoPersonaEnJsonResult" type="{http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno}RespuestaConsultaJson" minOccurs="0"/&gt;
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
    "consultaDatoPersonaEnJsonResult"
})
@XmlRootElement(name = "ConsultaDatoPersonaEnJsonResponse")
public class ConsultaDatoPersonaEnJsonResponse {

    @XmlElementRef(name = "ConsultaDatoPersonaEnJsonResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<RespuestaConsultaJson> consultaDatoPersonaEnJsonResult;

    /**
     * Obtiene el valor de la propiedad consultaDatoPersonaEnJsonResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RespuestaConsultaJson }{@code >}
     *     
     */
    public JAXBElement<RespuestaConsultaJson> getConsultaDatoPersonaEnJsonResult() {
        return consultaDatoPersonaEnJsonResult;
    }

    /**
     * Define el valor de la propiedad consultaDatoPersonaEnJsonResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RespuestaConsultaJson }{@code >}
     *     
     */
    public void setConsultaDatoPersonaEnJsonResult(JAXBElement<RespuestaConsultaJson> value) {
        this.consultaDatoPersonaEnJsonResult = value;
    }

}
