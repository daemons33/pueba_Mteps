package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.PartidaPresupuesto;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Models.PlanMeta;
import mteps.sistpoa.Pojos.*;

import java.util.List;

@Mapper
//@CacheNamespace
public interface PlanMetaMap {

    // lista verifica seguimiento meta
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_verifica_seguimiento_meta(#{id_plan}, #{id_tipo_proceso})")
    List<PlanMeta> verificaSeguimientoMeta(Integer id_plan, Integer id_tipo_proceso);

    // lista verifica seguimiento presupuesto
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_verifica_seguimiento_presupuesto(#{id_plan}, #{id_tipo_proceso})")
    List<PlanMeta> verificaSeguimientoPresupuesto(Integer id_plan, Integer id_tipo_proceso);

    // insertar Conf Meta
    @Select("select id_plan_meta_ins as id_plan_meta, codigo_resp, mensaje_resp from mteps_plan.f_inserta_conf_meta(" +
            "#{id_plan}, #{id_tipo_proceso},#{fecha_linea_base}::date,#{id_seguimiento_interval}," +
            "#{seguimiento_interval}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PlanMeta> insertaConfMeta(PlanMeta planMeta);

    // insertar Conf Presupuesto
    @Select("select id_plan_meta_ins as id_plan_meta, codigo_resp, mensaje_resp from mteps_plan.f_inserta_conf_presupuesto(" +
            "#{id_plan}, #{id_tipo_proceso},#{id_seguimiento_interval}," +
            "#{seguimiento_interval}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PlanMeta> insertaConfPresupuesto(PlanMeta planMeta);

    // lista Estructura json Meta
    @Select("select interval_valor, etiqueta::varchar, inicio::date, fin::date, valor_programado, valor_ejecutado,nombre from mteps_plan.f_lista_estruct_json_meta(#{id_proceso})")
    List<PResultado003> listaEstructuraJsonMeta(Integer id_proceso);
    
 // lista Estructura json Meta ref
    @Select("select interval_valor, etiqueta::varchar, inicio::date, fin::date, valor_programado, valor_ejecutado from mteps_plan.f_lista_estruct_json_meta_ref(#{id_proceso})")
    List<PResultado003> listaEstructuraJsonMetaRef(Integer id_proceso);

    // lista Estructura json Presupuesto
    @Select("select interval_valor, inicio::date, fin::date ,inversion_publica, gasto_corriente from mteps_plan.f_lista_estruct_json_presup(#{id_proceso},#{id_indicador})")
    List<PResultado004> listaEstructuraJsonPresupuesto(Integer id_proceso,Integer id_indicador);
    
    
    // lista Estructura json Presupuesto ref
    @Select("select interval_valor, inicio::date, fin::date ,inversion_publica, gasto_corriente from mteps_plan.f_lista_estruct_json_presup_ref(#{id_proceso})")
    List<PResultado004> listaEstructuraJsonPresupuestoRef(Integer id_proceso);

    // lista Estructura json Tesp
    @Select("select interval_valor, inicio::date, fin::date, valor_programado, valor_ejecutado,id_sigec,cite_sigec from mteps_plan.f_lista_estruct_json_tesp(#{id_proceso})")
    List<PResultado005> listaEstructuraJsonTesp(Integer id_proceso);

    // lista Estructura json Tesp ref
    @Select("select interval_valor, inicio::date, fin::date, valor_programado, valor_ejecutado,id_sigec,cite_sigec from mteps_plan.f_lista_estruct_json_tesp_ref(#{id_proceso})")
    List<PResultado005> listaEstructuraJsonTespRef(Integer id_proceso);
    
    // lista Estructura json memorias de calculo
    @Select("select interval_valor::integer, etiqueta::varchar, inicio::date, fin::date, cantidad::numeric, costo::numeric from mteps_plan.f_lista_estruct_json_mem_calc(#{id_proceso})")
    List<PResultado002> listaEstructuraJsonMemCalc(Integer id_proceso);
    
    // lista Estructura json memorias de calculo ref
    @Select("select interval_valor::integer, etiqueta::varchar, inicio::date, fin::date, cantidad::numeric, costo::numeric from mteps_plan.f_lista_estruct_json_mem_calc_ref(#{id_proceso})")
    List<PResultado002> listaEstructuraJsonMemCalcRef(Integer id_proceso);

}

