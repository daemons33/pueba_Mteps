package mteps.sistpoa.Models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traspaso {
	public Integer id_origen;
	public Long id_reformulacion;
	public Long id_memoria_calculo;
	public String descripcion;
	public double cantidad;
	public String unidad_medida;
	public double precio_unitario;
	public double importe_total;
	public double saldo_memoria;
	public String transaccion;
	public String estado;
	public String observacion;
	public Integer fid_origen;
	public Integer fid_destino;
	public Integer usr_cre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_cre;
	public Integer usr_mod;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_mod;
}
