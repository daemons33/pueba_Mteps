package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.SeguimientoInterval;

import java.util.List;

@Mapper
//@CacheNamespace
public interface SeguimientoIntervalMap {

    @Select("select id_seguimiento_interval, nombre, denominativo from mteps_plan.f_lista_intervalo_seguimiento()")
    List<SeguimientoInterval> listaSeguimientoInterval();
}
