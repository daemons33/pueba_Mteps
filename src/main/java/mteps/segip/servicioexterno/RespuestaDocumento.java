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
 * <p>Clase Java para RespuestaDocumento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaDocumento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/Framework.Comun.Resultado}BaseResultado"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Reporte" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaDocumento", namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", propOrder = {
    "reporte"
})
public class RespuestaDocumento
    extends BaseResultado
{

    @XmlElementRef(name = "Reporte", namespace = "http://schemas.datacontract.org/2004/07/Convenio.Core.Modelo.Dto.Proceso.ServicioExterno", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> reporte;

    /**
     * Obtiene el valor de la propiedad reporte.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getReporte() {
        return reporte;
    }

    /**
     * Define el valor de la propiedad reporte.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setReporte(JAXBElement<byte[]> value) {
        this.reporte = value;
    }

}
