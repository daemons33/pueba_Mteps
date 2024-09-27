package mteps.tramites.fondoCustodia.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MovimientoCB {
	public int id_movimiento;
	public int id_estado;
	public int id_fc;
	public Double monto;
	public boolean uninet;
	public String nro_movimiento_uninet;
	public Timestamp fecha_movimiento_uninet;
	public String detalle_uninet;
	public int movimiento;
	public boolean sigep;
	public String operacion_sigep;
	public String detalle_sigep;
	public Timestamp fecha_sigep;
	public String cuenta_transferencia_sigep;
	public int id_cuenta_bancaria;
	public Timestamp fecha_creacion;
	public Timestamp fecha_modificacion;
	public int usuario_creacion;
	public int usuario_modificacion;
	public String nombre_c;
	
	public String getfechaFormateada() {
        if (fecha_movimiento_uninet != null) {
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
            return fecha.format(fecha_movimiento_uninet);
        }
        return null;
    }
}
