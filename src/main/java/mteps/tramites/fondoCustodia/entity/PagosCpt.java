package mteps.tramites.fondoCustodia.entity;

import java.util.Date;

public class PagosCpt {
	public String numero_tramite;
	public String fecha_pago;
	public String fechaPago;
	public double monto_pago;
	public String transacciones;
	public String estado;
	public String observacion;
	public String transaccion;
	public String login;
	public String canal;
	public String cpt;
	public Integer idCpt;
	public DetalleCPT detalle;
	public String codigoSeguimiento;
	public String mensaje;
	public Boolean finalizado;
	public String fuente;
	public String fecha;
	public String datosMetodoPago;
	public String metodoPago;
	public String codigoOrden;
	public String codigoTransaccion;
	public String fechaSolicitudCpt;
	public String fechaVencimiento;
	public Integer idFc;
	public static class DetalleCPT{
		public String datosMetodoPago;
		public String metodoPago;
		public String codigoOrden;
		public String fecha;
	}
}
