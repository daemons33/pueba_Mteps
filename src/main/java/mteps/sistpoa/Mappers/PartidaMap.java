package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import mteps.sistpoa.Models.*;

import java.util.List;

@Mapper
//@CacheNamespace
public interface PartidaMap {

    // actualiza medio verif
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_medio_verif(#{id_indicador}, #{interval_valor},#{id_sigec},#{cite_sigec},#{id_usuario},#{host},#{fecha_sistema}::timestamp)")
    List<IndicadorMeta> actualizaMedioVerif(IndicadorMeta indicadorMeta);

    // lista Partida Cod Presupuestarias
    @Select("select id_partida_presupuesto, cod_partida, partida, valor_presupuesto, sigla from mteps_plan.f_lista_cod_partida_presup()")
    List<PartidaPresupuesto> listaCodPartidaPresup();

    // lista Partida Cod Presupuestarias
    @Select("select id_partida_presupuesto::integer, id_partida_presupuesto_sup::integer, cod_partida::varchar, ruta::varchar, partida::varchar " +
            "from mteps_plan.f_lista_cod_partida_presup(#{id_partida_presupuesto_sup})")
    List<PartidaPresupuesto> listaCodPartidaPresup2(Integer id_partida_presupuesto_sup);
}
