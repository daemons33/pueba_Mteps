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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RespuestaConsultaCertificacion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConsultaCertificacion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado}ResultadoExterno"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ReporteCertificacion" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConsultaCertificacion", namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", propOrder = {
    "reporteCertificacion"
})
public class RespuestaConsultaCertificacion
    extends ResultadoExterno
{

    @XmlElementRef(name = "ReporteCertificacion", namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> reporteCertificacion;

    /**
     * Obtiene el valor de la propiedad reporteCertificacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getReporteCertificacion() {
        return reporteCertificacion;
    }

    /**
     * Define el valor de la propiedad reporteCertificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setReporteCertificacion(JAXBElement<byte[]> value) {
        this.reporteCertificacion = value;
    }

}
