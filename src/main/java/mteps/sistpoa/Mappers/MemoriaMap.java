package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.MemoriaCalculo;
import mteps.sistpoa.Models.Proceso;
import mteps.sistpoa.Models.ProgramaMemoriaCalculo;
import mteps.sistpoa.Models.TipoProceso;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@Mapper
//@CacheNamespace
public interface MemoriaMap {

    @Select("select id_memoria_calculo, descripcion, cantidad, unidad_medida, precio_unitario, " +
            "justificacion, id_partida_presupuesto, cod_partida, partida, importe_total, saldo,id_fuente_financiamiento , fuente_financiamiento from mteps_plan.f_lista_memoria_calc(#{id_proceso})")
    List<MemoriaCalculo> listaMemoriaCalculo(Integer id_proceso);
    
    @Select("select id_memoria_calculo, descripcion, cantidad, unidad_medida, precio_unitario, " +
            "justificacion, id_partida_presupuesto, cod_partida, partida, importe_total " +
            "from mteps_plan.f_lista_memoria_calc_ref(#{id_proceso})")
    List<MemoriaCalculo> listaMemoriaCalculoRef(Integer id_proceso);

    // lista de los tipos proceso
    @Select("select id_programa_mem_calculo, id_memoria_calculo, desc_mes_programa, mes_programa, " +
            "inicio::date, fin::date, cantidad, costo from mteps_plan.f_lista_seg_mem_calc(#{id_memoria_calculo})")
    List<ProgramaMemoriaCalculo> listaseguimientomemoriacalculo(Integer id_memoria_calculo);

    @Select("select id_mem_calc_ins as id_memoria_calculo, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_memoria_calculo(#{id_proceso}::integer, " +
            "#{id_partida_presupuesto}::integer, #{descripcion}::varchar, #{cantidad}::numeric,  #{unidad_medida}::varchar," +
            " #{precio_unitario}::numeric, #{justificacion}::varchar, #{importe_total}::numeric, " +
            "#{programa_mem_calc}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp,"
            + "#{id_organismo_financiador}::integer,#{id_fuente_financiamiento}::integer,#{id_unidad_organizacional}::integer,"
            + " #{act_func_consultoria}::varchar,#{result_consultoria}::varchar,#{duracion_consultoria_meses}::integer,"
            + "#{nro_casos_consultoria}::integer,#{transaccion}::varchar,#{estado}::varchar,#{observacion}::varchar,#{id_tipo_pasaje}::integer,#{id_tipo_viatico}::integer)")
    List<MemoriaCalculo> insertaMemoriaCalculo(MemoriaCalculo memoriaCalculo);
    
    @Select("select id_mem_calc_ins as id_memoria_calculo, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_memoria_calculo_ref(#{id_proceso}::integer,#{id_proceso_a}::integer, " +
            "#{id_partida_presupuesto}::integer, #{descripcion}::varchar, #{cantidad}::numeric,  #{unidad_medida}::varchar," +
            " #{precio_unitario}::numeric, #{justificacion}::varchar, #{importe_total}::numeric, " +
            "#{programa_mem_calc}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp,"
            + "#{id_organismo_financiador}::integer,#{id_fuente_financiamiento}::integer,#{id_unidad_organizacional}::integer,"
            + " #{act_func_consultoria}::varchar,#{result_consultoria}::varchar,#{duracion_consultoria_meses}::integer,"
            + "#{nro_casos_consultoria}::integer,#{transaccion}::varchar,#{estado}::varchar,#{observacion}::varchar,#{id_tipo_pasaje}::integer,#{id_tipo_viatico}::integer)")
    List<MemoriaCalculo> insertaMemoriaCalculoRef(MemoriaCalculo memoriaCalculo);

    @Select("select id_mem_calc_ins as id_memoria_calculo, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_memoria_calculo(#{id_memoria_calculo}::integer, #{id_proceso}::integer, " +
            "#{id_partida_presupuesto}::integer, #{descripcion}::varchar, #{cantidad}::numeric,  #{unidad_medida}::varchar," +
            " #{precio_unitario}::numeric, #{justificacion}::varchar, #{importe_total}::numeric, " +
            "#{programa_mem_calc}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp,"
            + "#{id_organismo_financiador}::integer,#{id_fuente_financiamiento}::integer,#{id_unidad_organizacional}::integer,"
            + " #{act_func_consultoria}::varchar,#{result_consultoria}::varchar,#{duracion_consultoria_meses}::integer,"
            + "#{nro_casos_consultoria}::integer,#{transaccion}::varchar,#{estado}::varchar,#{observacion}::varchar,#{id_tipo_pasaje}::integer,#{id_tipo_viatico}::integer)")
    List<MemoriaCalculo> actualizaMemoriaCalculo(MemoriaCalculo memoriaCalculo);
    
    @Select("select id_mem_calc_ins as id_memoria_calculo, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_memoria_calculo_ref(#{id_memoria_calculo}::integer, #{id_proceso}::integer, " +
            "#{id_partida_presupuesto}::integer, #{descripcion}::varchar, #{cantidad}::numeric,  #{unidad_medida}::varchar," +
            " #{precio_unitario}::numeric, #{justificacion}::varchar, #{importe_total}::numeric, " +
            "#{programa_mem_calc}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp,"
            + "#{id_organismo_financiador}::integer,#{id_fuente_financiamiento}::integer,#{id_unidad_organizacional}::integer,"
            + " #{act_func_consultoria}::varchar,#{result_consultoria}::varchar,#{duracion_consultoria_meses}::integer,"
            + "#{nro_casos_consultoria}::integer,#{transaccion}::varchar,#{estado}::varchar,#{observacion}::varchar,#{id_tipo_pasaje}::integer,#{id_tipo_viatico}::integer)")
    List<MemoriaCalculo> actualizaMemoriaCalculoRef(MemoriaCalculo memoriaCalculo);
    
  //BORRAR INDICADOR
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_memoria(#{id_memoria})")
    List<PResultado001> borrarMemoriaCalculo(Integer id_memoria);
    
    //BORRAR INDICADOR
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_memoria_ref(#{id_memoria})")
    List<PResultado001> borrarMemoriaCalculoRef(Integer id_memoria);
    
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_reducir_memoria_ref(#{id_memoria})")
    List<PResultado001> reducirMemoriaRef(Integer id_memoria);
    
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualizar_just_obs_ref(#{tipo},#{id_memoria_calculo},#{justificacion},#{observacion})")
    List<PResultado001> actualizaJustObsRef(MemoriaCalculo memoriaCalculo);
}