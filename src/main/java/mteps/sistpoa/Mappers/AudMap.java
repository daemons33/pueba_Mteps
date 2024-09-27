package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.*;

import mteps.sistpoa.Models.Indicador;
import mteps.sistpoa.Models.IndicadorMeta;
import mteps.sistpoa.Models.IndicadorPresupuesto;
import mteps.sistpoa.Models.Plan;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface AudMap {

    @Select("select * from mteps_plan.log_indicador where id_indicador=#{id_indicador} order by id desc")
    List<Indicador> listaAudIndicador(Integer id_indicador);

    @Select("select * from mteps_plan.log_indicador_meta where id_indicador_meta=#{id_indicador_meta} order by id desc")
    List<IndicadorMeta> listaAudIndicadorMeta(Integer id_indicador_meta);

    @Select("select * from mteps_plan.log_indicador_presupuesto where id_indicador_presupuesto=#{id_indicador_presupuesto} order by id desc")
    List<IndicadorPresupuesto> listaAudIndicadorPresupuesto(Integer id_indicador_presupuesto);

    /*
    @Select("select id, id_indicador_meta, id_plan_meta, id_indicador, interval_valor, inicio, fin, " +
            "valor_programado_num, valor_ejecutado_num, id_sigec, cite_sigec, id_estado, " +
            "usr_cre, host_cre, fecha_cre, usr_mod, host_mod, fecha_mod, " +
            "bd_usr, bd_host, bd_timestamp, id_indicador_meta_a " +
            "from mteps_plan.log_indicador_meta where id_indicador=#{id_indicador}  and id_estado !=0 order by interval_valor,id desc")
    List<IndicadorMeta> listaAudIndicadorMetaxIndicador(Integer id_indicador);
    */


    @Select("select * from mteps_plan.f_lista_log_indicador_meta(#{id}::timestamp)")
    List<IndicadorMeta> listaAudIndicadorMetaxIndicador(LocalDateTime id);

    @Select("select distinct i.id, i.id_indicador, i.id_proceso, i.id_tipo_identificador, i.sector, denominacion, " +
            "i.valor_num, i.valor_text, i.unidad, i.formula, i.lb_valor_num, i.lb_valor_text, i.est_meta_plan_ant_num, " +
            "i.est_meta_plan_ant_text, i.meta_plan_num, i.meta_plan_text, i.fuente_indicador, " +
            "i.responsable_indicador, i.presupuesto_ref, i.id_estado, i.usr_cre, i.host_cre, i.fecha_cre, " +
            "i.usr_mod, i.host_mod, i.fecha_mod, i.bd_usr, i.bd_host, i.bd_timestamp, " +
            "i.ponderacion, i.programado, i.ejecucion, i.id_indicador_a, i.editar_poa, i.nro_reformulacion " +
            "from mteps_plan.log_indicador i , mteps_plan.log_indicador_meta im " +
            "where i.id=im.id and i.id_indicador = #{id_indicador} and i.id_estado!=0 order by id desc")
    @Results(value={
            @Result(property = "indicadorMeta", column= "id", many = @Many(select = "com.kretco.mintrab.sistpoa.Mappers.AudMap.listaAudIndicadorMetaxIndicador"))
    })
    List<Indicador> listaAudIndicadorconIndicadoresmetas(Integer id_indicador);




}


