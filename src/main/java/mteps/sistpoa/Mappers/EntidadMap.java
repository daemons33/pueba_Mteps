package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.Entidad;
import mteps.sistpoa.Models.Gestion;
import mteps.sistpoa.Pojos.PLogo;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@Mapper
//@CacheNamespace
public interface EntidadMap {

    // lista de mision y vision
    @Select("select mision, vision from mteps_plan.f_lista_mision_vision()")
    List<Entidad> listaMisioVision();

    //actualizacion de mision y vision
    @Select("select * from mteps_plan.f_actualiza_mision_vision(#{mision}, #{vision})")
    List<Entidad> actualizaMisionVision(String mision, String vision);

    @Select("select * from mteps_plan.f_actualiza_logo(#{id_entidad}, #{imagen_logo})")
    List<PLogo> actualizaLogo(Integer id_entidad, String imagen_logo);

    @Select("select * from mteps_plan.f_lista_logo(#{id_entidad})")
    PLogo buscaLogo(Integer id_entidad);

}
