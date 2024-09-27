package mteps.sistpoa.Mappers;


import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.FinProgPresupuesto;
import mteps.sistpoa.Models.Proceso;
import mteps.sistpoa.Models.TipoProceso;

@Mapper
public interface ProgramaPresupuestoMap {

    @Select("select id_prog_presupuesto, id_prog_presupuesto_superior, codigo, ruta, descripcion, ponderacion::numeric from mteps_plan.f_lista_prog_presup(#{id_gestion})")
    List<FinProgPresupuesto> listaProgramaPresupuesto(Integer id_gestion);

}
