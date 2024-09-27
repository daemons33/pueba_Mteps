package mteps.tramites.fondoCustodia.entity;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;


public class MovimientoCuentaBancaria {
	
	public Integer tipo;
	public String login;
	public List<MovimientoDetalle> movimientoDetalle;
    public Integer idCuentaBancaria;
	
	public static class MovimientoDetalle{
	
		
		//tipo 1
		public String fecha;
		public String ag;
		public String descripcion;
		public String monto;
		public String nroDocumento;
		public String nroCuenta;
		public String saldo;		
		public String tipo;
		//tipo2
		
		public int idMovimiento;
	    public int idEstado;
	    public int idFc;
	    public String debito;
	    public String credito;
	    public boolean uninet;
	    public String nroMovimiento;
	    public String fechaMovimiento;
	    public String detalleUninet;
	    public int movimiento;
	    public boolean sigep;
	    public String operacionSigep;
	    public String detalleSigep;
	    public String cuentaTransferenciaSigep;
	    public Timestamp fechaCreacion;
	    public Timestamp fechaModificacion;
	    public int usuarioCreacion;
	    public int usuarioModificacion;
	    
	   	    
	    
	}
}
