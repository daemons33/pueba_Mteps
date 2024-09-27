package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@Mapper
//@CacheNamespace
public interface EjecucionMetaMap {

    // inserta ejecucion adjunto tarea especifica
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_ejec_adj_tarea_esp(#{id_indicador_meta}::integer, #{valor_ejecutado_num}::numeric, #{nombre}::varchar, " +
            "#{descripcion}::varchar, #{es_sigec}::integer, #{cite_ruta}::varchar, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp)")
    List<PResultado001> insertaEjcAdjTareaEsp(RespaldoEjecucionMeta respaldoEjecucionMeta);

    // inserta ejecucion adjunto tarea especifica
    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_inserta_adj_actividad(#{id_indicador}::integer, #{nombre}::varchar, " +
            "#{descripcion}::varchar, #{es_sigec}::integer, #{cite_ruta}::varchar, #{id_usuario}::integer, #{host}::varchar, #{fecha_sistema}::timestamp)")
    List<PResultado001> insertaAdjActividad(RespaldoEjecucionMeta respaldoEjecucionMeta);


    // inserta adjunto acividad
    @Select("select id_respaldo_ejecucion_meta::integer, id_indicador_meta::integer, id_indicador::integer, " +
            "nombre::varchar, descripcion::varchar, es_sigec::integer, cite_ruta::varchar " +
            "from mteps_plan.f_lista_adj_verificacion(#{id_indicador}, #{id_indicador_meta}) ")
    List<RespaldoEjecucionMeta> listaAdjVerificacion(Integer id_indicador, Integer id_indicador_meta);

}
