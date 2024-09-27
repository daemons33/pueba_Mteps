package mteps.sistpoa.Mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mteps.sistpoa.Models.AccionPoaOrg;
import mteps.sistpoa.Models.FinProgPresupuesto;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Pojos.PPoa;
import mteps.sistpoa.Pojos.PResultado001;
import mteps.sistpoa.Pojos.PResultado006;
import mteps.sistpoa.Pojos.PResultado007;

import java.util.List;

@Mapper
public interface PoaMap {

    @Select("select id_proceso, sigla, nombre from mteps_plan.f_lista_accion_poa(#{id_plan})")
    List<PResultado006> listaAccionPoa(Integer id_plan);

    @Select("select id_accion_poa_org, id_estado, id_unidad_organizacional, sigla, nombre " +
            "from mteps_plan.f_lista_uniorg_accion_poa(#{id_proceso})")
    List<PResultado007> listaUniorgAccionPoa(Integer id_proceso);

    @Select("select codigo_resp, mensaje_resp " +
            "from mteps_plan.f_actualiza_uniorg_accion_poa(#{id_proceso}::integer,#{uniorg_accion_poa}::json,#{id_usuario}::integer,#{host},#{fecha_sistema}::timestamp)")
    List<PResultado001> actualizaUnionUniOrgAccionPoa(AccionPoaOrg accionPoaOrg);


    @Select("select pl.id_plan,pl.id_plan_superior,pl.id_tipo_plan,tp.sigla as sigla_tipo,tp.nombre as nombre_tipo, " +
            "pl.id_unidad_organizacional,pl.sigla as sigla_plan,pl.nombre as nombre_plan, pl.inicio_gestion,pl.fin_gestion, " +
            "pl.id_gestion, valor_gestion, pl.id_plan_a, pl.editar_poa, pl.evaluacion, pl.fecha_evaluacion, pl.nro_reformulacion " +
            "from mteps_plan.pln_plan pl " +
            "        inner join mteps_plan.par_unidad_organizacional uo on pl.id_unidad_organizacional=uo.id_unidad_organizacional " +
            "        inner join mteps_plan.pln_tipo_plan tp on tp.id_tipo_plan=pl.id_tipo_plan " +
            "        inner join mteps_plan.par_gestion g on g.id_gestion=pl.id_gestion " +
            "        where pl.id_unidad_organizacional = #{id_unidad_organizacional} and pl.id_gestion=#{id_gestion} and pl.id_estado!=0")
    List<PPoa> listaPoaUnidadOrganizacional(Integer id_unidad_organizacional, Integer id_gestion);
}



