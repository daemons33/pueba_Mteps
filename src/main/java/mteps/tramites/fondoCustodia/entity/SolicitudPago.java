package mteps.tramites.fondoCustodia.entity;

import mteps.poa.entity.F_uniorg_apertura;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;

public class SolicitudPago {
    public String descripcion;
    public String codigoOrden;
    public DatosPago datosPago;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    public List<Productos> productos;
    public static class DatosPago{
        public String nombresCliente;
        public String numeroDocumentoCliente;
        public Number montoTotal;
        public String cuentaBancaria;
    }
    public static class Productos{
        public String actividadEconomica;
        public String descripcion;
        public Number precioUnitario;
        public Integer unidadMedida;
        public Integer cantidad;
    }
}
