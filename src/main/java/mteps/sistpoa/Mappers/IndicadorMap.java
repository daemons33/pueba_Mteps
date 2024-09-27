package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PMatriz;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@Mapper
//@CacheNamespace
public interface IndicadorMap {


    // lista de tipo indicador
    @Select("select id_tipo_identificador, sigla, nombre from mteps_plan.f_lista_tipo_indicador()")
    List<TipoIndicador> listaTipoIndicador();

    // insertar Indicador
    @Select("select id_indicador_ins as id_indicador, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_indicador(#{id_indicador}::integer,#{id_proceso}::integer, #{id_tipo_identificador}::integer, #{sector}::varchar, " +
            "#{denominacion}::varchar, #{unidad}::varchar, #{formula}::varchar, #{lb_valor_text}::varchar, #{meta_plan_text}::varchar, #{ponderacion}::numeric, " +
            "#{fuente_indicador}::varchar, #{responsable_indicador}::varchar, #{seguimiento_meta}::json, " +
            "#{seguimiento_presup}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp, #{programado}::numeric, #{ejecucion}::numeric, #{evaluacion_indicador}::varchar)")
    List<Indicador> insertaIndicador(Indicador indicador);

    // insertar Indicador
    @Select("select id_indicador_ins as id_indicador, codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_indicador_ref(#{id_proceso}::integer, #{id_tipo_identificador}::integer, #{sector}::varchar, " +
            "#{denominacion}::varchar, #{unidad}::varchar, #{formula}::varchar, #{lb_valor_text}::varchar, #{meta_plan_text}::varchar, #{ponderacion}::numeric, " +
            "#{fuente_indicador}::varchar, #{responsable_indicador}::varchar, #{seguimiento_meta}::json, " +
            "#{seguimiento_presup}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp, #{programado}::numeric, #{ejecucion}::numeric, #{evaluacion_indicador}::varchar)")
    List<Indicador> insertaIndicadorRef(Indicador indicador);

    
    // lista Indicador
    @Select("select  * from mteps_plan.f_lista_indicador(#{id_proceso})")
    List<Indicador> listaIndicador(Integer id_proceso);

    // lista Indicador corto plazo
    @Select("select id_indicador, id_tipo_identificador, nombre, sector, denominacion, valor_text, unidad, formula, lb_valor_text, " +
            "fuente_indicador, responsable_indicador, meta_plan_text, ponderacion::numeric, programado::numeric, ejecucion::numeric " +
            "from mteps_plan.f_lista_indicador_cortoplazo(#{id_proceso})")
    List<Indicador> listaIndicadorCortoPlazo(Integer id_proceso);

    // lista Seguimiento Indicador Meta
    @Select("select id_indicador_meta, id_plan_meta, id_indicador, nombre, desc_intervalo, interval_valor, inicio::date, " +
            "fin::date, valor_programado_num, valor_ejecutado_num, id_sigec, cite_sigec, evaluacion, revision, observacion, estado_meta, medidas_correctivas, justificacion " +
            "from mteps_plan.f_lista_seg_metas(#{id_indicador})")
    List<IndicadorMeta> listaSegMeta(Integer id_indicador);
    
 // lista Seguimiento Indicador Meta ref
    @Select("select id_indicador_meta, id_plan_meta, id_indicador, nombre, desc_intervalo, interval_valor, inicio::date, " +
            "fin::date, valor_programado_num, valor_ejecutado_num, id_sigec, cite_sigec, evaluacion, revision, observacion, estado_meta, medidas_correctivas, justificacion " +
            "from mteps_plan.f_lista_seg_metas_ref(#{id_indicador})")
    List<IndicadorMeta> listaSegMetaRef(Integer id_indicador);

    // lista Seguimiento Indicador Presupuesto
    @Select("select id_indicador_presupuesto, id_plan_presupuesto, id_indicador, nombre, desc_intervalo, interval_valor, " +
            "inicio::date, fin::date, valor_inv_publica, valor_gasto_corriente from mteps_plan.f_lista_seg_presup(#{id_indicador})")
    List<IndicadorPresupuesto> listaSegPresupuesto(Integer id_indicador);

    // inserta ejecucion adjunto tarea especifica
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_ejec_adj_tarea_esp(id_indicador_meta::integer, valor_ejecutado_num::numeric, nombre::varchar, descripcion::varchar, es_sigec::integer, cite_ruta::varchar, id_usuario::integer, host::varchar, fecha_sistema::timestamp)")
    List<PResultado001> insertaEjcAdjTareaEsp(RespaldoEjecucionMeta respaldoEjecucionMeta);


    // lista plan indicador
    @Select("select id_plan::integer, sigla_plan::varchar, nombre_plan::varchar, duracion_plan::varchar, inicio_gestion::date, fin_gestion::date, estado_plan::varchar, nivel_proceso::int4, id_proceso::integer, id_proceso_superior::integer, codigo_orden::integer, ruta_proceso::varchar, sigla_proceso::varchar, procesos_superiores::text, nombre_proceso::varchar, descripcion_proceso::varchar, id_indicador::integer, tipo_identificador::varchar, sector::varchar, denominacion::varchar, unidad::varchar, formula::varchar, lb_valor_text::varchar, meta_plan_text::varchar, ponderacion::numeric, fuente_indicador::varchar, responsable_indicador::varchar " +
            "from mteps_plan.f_lista_plan_indicador(#{id_usuario}, #{id_tipo_plan}, #{id_gestion})")
    List<PlanIndicador> listaPlanIndicador(Integer id_usuario, Integer id_tipo_plan, Integer id_gestion);

    // modificar Indicador
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_indicador(#{id_indicador}::integer,#{id_proceso}::integer, #{id_tipo_identificador}::integer, " +
            "#{sector}::varchar, #{denominacion}::varchar, #{unidad}::varchar, #{formula}::varchar, " +
            "#{lb_valor_text}::varchar, #{meta_plan_text}::varchar, #{ponderacion}::numeric, #{fuente_indicador}::varchar, " +
            "#{responsable_indicador}::varchar, #{seguimiento_meta}::json, #{seguimiento_presup}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp, #{programado}::numeric, #{ejecucion}::numeric, #{evaluacion_indicador})")
    List<PResultado001> modificarIndicador(Indicador indicador);

    // modificar Indicador Ref
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_indicador_ref(#{id_indicador}::integer, #{id_tipo_identificador}::integer, " +
            "#{sector}::varchar, #{denominacion}::varchar, #{unidad}::varchar, #{formula}::varchar, " +
            "#{lb_valor_text}::varchar, #{meta_plan_text}::varchar, #{ponderacion}::numeric, #{fuente_indicador}::varchar, " +
            "#{responsable_indicador}::varchar, #{seguimiento_meta}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp, #{programado}::numeric, #{ejecucion}::numeric, #{evaluacion_indicador})")
    List<PResultado001> modificarIndicadorRef(Indicador indicador);
    
    
    // modificar Indicador
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_indicador1(#{id_indicador}::integer, #{id_tipo_identificador}::integer, " +
            "#{sector}::varchar, #{denominacion}::varchar, #{unidad}::varchar, #{formula}::varchar, " +
            "#{lb_valor_text}::varchar, #{meta_plan_text}::varchar, #{ponderacion}::numeric, #{fuente_indicador}::varchar, " +
            "#{responsable_indicador}::varchar, #{seguimiento_meta}::json, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp, #{programado}::numeric, #{ejecucion}::numeric, #{evaluacion_indicador}, #{nro_reformulacion})")
    List<PResultado001> modificarIndicador1(Indicador indicador);

    // modificar Indicador programado y ejecutado
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_indicador_prog_ejec(#{id_indicador}::integer, #{programado}::numeric, #{ejecucion}::numeric)")
    List<PResultado001> modificarIndicadorProgEjec(Indicador indicador);

    //MODIFICA ESTADO EDICION INDICADOR
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_edicion_indicador(#{id_indicador},#{editar_poa})")
    List<PResultado001> estadoEditarIndicador(Integer id_indicador, Integer editar_poa);

    //MODIFICA EVALUACION EDICION INDICADOR
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_evaluacion_indicador(#{id_indicador},#{evaluacion_indicador})")
    List<PResultado001> evaluacionIndicador(Integer id_indicador, String evaluacion_indicador);

    //MODIFICA MEDIDAS INDICADOR META
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_medidas_indicador_meta(#{id_indicador_meta}, #{evaluacion}, #{revision},#{observacion}, #{estado_meta}, #{medidas_correctivas}, #{justificacion})")
    List<PResultado001> medidaIndicadorMeta(IndicadorMeta indicadorMeta);

    //BORRAR INDICADOR
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_indicador(#{id_indicador})")
    List<PResultado001> borrarIndicador(Integer id_indicador);
    
   
    //BORRAR INDICADOR REF
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_indicador_ref(#{id_indicador})")
    List<PResultado001> borrarIndicadorRef(Integer id_indicador);
    
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_proceso(#{id_proceso})")
    List<PResultado001> borrarProceso(Integer id_proceso);
    
    
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_borrar_proceso_ref(#{id_proceso})")
    List<PResultado001> borrarProcesoRef(Integer id_proceso);
    

    @Select("SELECT i.id_proceso, i.id_tipo_identificador, i.sector, i.denominacion, " +
            "id_indicador_meta, id_indicador, id_plan_meta, extract(month from inicio) as mes_inicio, " +
            "extract(year from inicio) as anio_inicio, extract(month from fin) as mes_fin, " +
            "extract(year from fin) as anio_fin, valor_programado_num, valor_ejecutado_num,  " +
            "FROM mteps_plan.pln_indicador_meta im " +
            "INNER JOIN mteps_plan.pln_indicador i USING (id_indicador) " +
            "WHERE i.id_proceso=#{id_proceso} AND im.id_estado != 0 AND i.id_estado != 0")
    List<PMatriz> listadoIndicadorMatriz(Integer id_proceso);

}
