package mteps.sistpoa.Mappers;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.Proceso;
import mteps.sistpoa.Models.TipoProceso;

@Mapper
//@CacheNamespace
public interface ProcesoMap {

    @Select("select id_proceso,id_proceso_superior,id_plan,id_tipo_proceso,sigla,nombre,descripcion from mteps_plan.pln_proceso where id_plan=#{id_plan}")
    List<Proceso> listaPlanProcesos(Integer id_plan);

    // lista de los tipos proceso
    @Select("select id_tipo_proceso, id_tipo_proceso_superior, nivel_proceso, sigla, nombre from mteps_plan.f_lista_tipo_proceso(#{id_plan})")
    List<TipoProceso> listaTipoProceso(Integer id_plan);

    // verificacion de proceso superior
    @Select("select id_proceso_superior, ruta_proceso as ruta, descripcion,codigo_resp,mensaje_resp from mteps_plan.f_verifica_proceso_superior(#{id_tipo_proceso}, #{id_tipo_proceso_sup},#{id_plan})")
    List<Proceso> verificaProcesoSuperior(Integer id_tipo_proceso, Integer id_tipo_proceso_sup, Integer id_plan);

    // insertar Plan
    @Select("select id_proceso_ins as id_proceso, codigo_resp, mensaje_resp from mteps_plan.f_inserta_proceso(#{id_proceso_superior},#{id_plan}, #{id_tipo_proceso},#{sigla},#{nombre},#{descripcion}, #{codigo_orden},#{fecha_inicio}::date,#{fecha_fin}::date, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp, #{id_gestion})")
    List<Proceso> insertaProceso(Proceso proceso);
    
    // insertar Plan ref
    @Select("select id_proceso_ins as id_proceso, codigo_resp, mensaje_resp from mteps_plan.f_inserta_proceso_ref(#{id_proceso_superior},#{id_plan}, #{id_tipo_proceso},#{sigla},#{nombre},#{descripcion}, #{codigo_orden},#{fecha_inicio}::date,#{fecha_fin}::date, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<Proceso> insertaProcesoRef(Proceso proceso);

}