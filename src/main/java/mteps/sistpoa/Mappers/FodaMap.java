package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.Entidad;
import mteps.sistpoa.Models.Foda;
import mteps.sistpoa.Models.Proceso;

import java.util.List;

@Mapper
//@CacheNamespace
public interface FodaMap {

    //Lista de mision y vision
    @Select("select id_foda, valor_gestion as gestion, fortaleza, oportunidad, debilidad, amenaza  from mteps_plan.f_lista_foda()")
    List<Foda> listaFoda();

    //Actualizacion Foda
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_foda(#{id_foda}, #{id_gestion}, #{fortaleza}, #{oportunidad}, #{debilidad}, #{amenaza}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<Proceso> actualizaFoda(Foda foda);
}
