package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.UnidadOrganizacional;

import java.util.List;

@Mapper
//@CacheNamespace
public interface UnidadOrganizacionalMap {

    @Select("select * from mteps_plan.f_lista_unidades_org_poa(#{id_tipo_plan},#{id_gestion},#{id_usuario})")
    List<UnidadOrganizacional> listaUnidadOrganizacional(Integer id_tipo_plan,Integer id_gestion,Integer id_usuario);
}
