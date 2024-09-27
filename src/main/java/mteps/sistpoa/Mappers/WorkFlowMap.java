package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.Usuario;
import mteps.sistpoa.Models.WfRolEstado;

import java.util.List;

@Mapper
//@CacheNamespace
public interface WorkFlowMap {

    // listas Usuarios
    @Select("select id_estado, accion from mteps_plan.f_verifica_flujo_plan(#{id_tipo_plan}, #{id_plan}, #{id_usuario})")
    List<WfRolEstado> verificaFlujoPlan(Integer id_tipo_plan, Integer id_plan, Integer id_usuario);
}
