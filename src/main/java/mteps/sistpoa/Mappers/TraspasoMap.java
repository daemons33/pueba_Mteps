package mteps.sistpoa.Mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.Traspaso;
import mteps.sistpoa.Pojos.PResultado008;

@Mapper
//@CacheNamespace
public interface TraspasoMap {
	
	@Select("select id_traspaso_dest as id,codigo_resp, mensaje_resp from mteps_plan.f_inserta_traspaso_destino(#{id_reformulacion}::bigint,#{id_memoria_calculo}::bigint, #{descripcion}::character varying,#{cantidad}::numeric,#{unidad_medida}::character varying,#{precio_unitario}::numeric, #{importe_total}::numeric, #{saldo_memoria}::numeric, #{transaccion}, #{estado}, #{observacion},#{fid_origen},#{usr_cre})")
    List<PResultado008> insertaTraspasoDestino(Traspaso traspaso); 

	
	@Select("select id_traspaso_ori as id, codigo_resp, mensaje_resp from mteps_plan.f_inserta_traspaso_origen(#{id_reformulacion}::bigint,#{id_memoria_calculo}::bigint, #{descripcion}::character varying,#{cantidad}::numeric,#{unidad_medida}::character varying,#{precio_unitario}::numeric, #{importe_total}::numeric, #{saldo_memoria}::numeric, #{transaccion}, #{estado}, #{observacion},#{fid_destino},#{usr_cre})")
    List<PResultado008> insertaTraspasoOrigen(Traspaso traspaso); 
}
