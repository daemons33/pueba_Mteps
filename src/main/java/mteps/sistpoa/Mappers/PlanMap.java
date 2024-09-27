package mteps.sistpoa.Mappers;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PResultado001;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

@Mapper
//@CacheNamespace
public interface PlanMap {

    // todos los planes
    @Select("select o_id_plan as id_plan, o_id_plan_sup as id_plan_superior, o_sigla_plan as sigla, o_nombre_plan as nombre from mteps_plan.f_lista_plan_proc(null, null)")
    List<Plan> findAll();

    // lista de las gestion
    @Select("select id_gestion, valor_gestion from mteps_plan.f_lista_gestion()")
    List<Gestion> listaGestion();

    // lista de los tipos planes
    @Select("select id_tipo_plan, nivel_plan, sigla, nombre, categoria from mteps_plan.f_lista_tipo_plan(#{id_usuario})")
    List<TipoPlan> listaTipoPlan(Integer id_usuario);

    // lista de plan - procesos
    @Select("select o_nivel_plan as nivel_plan, o_id_plan as id_plan, o_id_plan_sup as id_plan_superior, o_sigla_plan as sigla, o_nombre_plan as nombre from mteps_plan.f_lista_plan_proc(#{id_plan},#{id_proceso})")
    List<Plan> listaPlanProceso(Integer id_plan, Integer id_proceso);

    // verificacion de plan superior
    @Select("select id_plan, sigla,duracion_plan as rango_gestion, mensaje_resp, codigo_resp from mteps_plan.f_verifica_plan_superior(#{id_usuario}, #{id_tipo_plan},#{id_gestion})")
    List<Plan> verificaPlanSuperior(Integer id_usuario, Integer id_tipo_plan, Integer id_gestion);

    //lista plan procesos
    @Select("select id_plan,sigla_plan, nombre_plan,duracion_plan, inicio_gestion, fin_gestion, " +
            "estado_plan, nivel_proceso, id_proceso, id_proceso_superior, codigo_orden, " +
            "ruta_proceso, sigla_proceso, procesos_superiores, nombre_proceso, " +
            "descripcion_proceso, editar_poa, evaluacion, fecha_evaluacion::date, nro_reformulacion::integer " +
            "from mteps_plan.f_lista_plan(#{id_usuario},#{id_tipo_plan},#{id_gestion})")
    List<ProcesoPlan> listaPlan(Integer id_usuario,Integer id_tipo_plan,Integer id_gestion);
    
    @Select("select * from mteps_plan.f_lista_estructura_plan()")
    List<Estructura> estructuraPlan();

    @Select("select id_plan, id_plan_superior, id_tipo_plan, id_unidad_organizacional, id_gestion, sigla, nombre from mteps_plan.pln_plan where id_plan = #{id_plan}")
    @Results(value={
        @Result(property = "proceso", column= "id_plan", many = @Many(select = "com.kretco.mintrab.sistpoa.Mappers.ProcesoMap.listaPlanProcesos"))
    })
    List<Plan> procesosPlan(Integer id_plan);
    
    // insertar Plan
    @Select("select id_plan_ins as id_plan, codigo_resp, mensaje_resp from mteps_plan.f_inserta_plan(#{id_plan_superior}, #{id_tipo_plan},#{id_unidad_organizacional}, #{id_gestion}, #{sigla}, #{nombre}, #{inicio_gestion}::date,  #{fin_gestion}::date, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<Plan> insertaPlan(Plan plan);

    //actualiza Plan
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_plan(#{id_plan}, #{sigla}, #{nombre}, " +
            "#{inicio_gestion}::date, #{fin_gestion}::date, #{mensaje_estado}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PResultado001> actualizaPlan(Plan plan);

    // actualiza Proceso
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_proceso(#{id_proceso}, #{sigla}, #{nombre}, #{descripcion}, " +
            "#{codigo_orden}, #{fecha_inicio}::date, #{fecha_fin}::date, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PResultado001> actualizaProceso(Proceso proceso);
    
 // actualiza Proceso Ref
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_proceso_ref(#{id_proceso}, #{sigla}, #{nombre}, #{descripcion}, " +
            "#{codigo_orden}, #{fecha_inicio}::date, #{fecha_fin}::date, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PResultado001> actualizaProcesoRef(Proceso proceso);
    
    // actualiza Estado Proceso
    @Select("select codigo_resp , mensaje_resp from mteps_plan.f_actualiza_estado_pl_pr(#{id_estado},#{id_plan}, #{mensaje_estado},#{id_proceso}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    List<PResultado001> actualizaEstadoPlanProceso(Plan plan);

    //REFORMULACION
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_reformulado(#{id_plan})")
    List<PResultado001> reformulacionPoa(Integer id_plan);

    //ESTADO EDICION PLAN
    /*
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_edicion_plan(#{id_plan},#{editar_poa})")
    List<PResultado001> estadoEditarPlan(Integer id_plan, Integer editar_poa);
    */

    //ESTADO EDICION PLAN
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_edicion_plan(#{id_plan},#{editar_poa},#{nro_reformulacion})")
    List<PResultado001> estadoEditarPlan(Integer id_plan, Integer editar_poa, Integer nro_reformulacion);



    //
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_evaluacion_plan(#{id_plan},#{evaluacion},#{fecha_evaluacion}::date)")
    List<PResultado001> evaluacionEditarPlan(Plan plan);

    // List<Plan>insertaPlan(Integer id_plan_superior, Integer id_tipo_plan, Integer
    // id_unidad_organizacional, Integer id_gestion, String sigla, String nombre,
    // Date inicio_gestion, Date fin_gestion, Integer id_usuario, String host, Date
    // fecha_sistema);

    /*
    //Lista que sirve
    @Select("select o_id_plan as id_plan, o_desc_plan as nombre, o_duracion_plan as  rango_gestion, o_estado_plan as estado_plan, o_sigla_sup as sigla,o_rango_gestion_sup as rango_gestion, o_mensaje_resp as mensaje_resp, o_codigo_resp as codigo_resp from mteps_plan.f_lista_plan(#{id_plan_superior}, #{id_tipo_plan},#{id_unidad_organizacional}, #{id_gestion}, #{sigla}, #{nombre}, #{inicio_gestion},  #{fin_gestion}, #{id_usuario}, #{host}, #{fecha_sistema})")
    @Results(value={
        @Result(property = "proceso", column= "{id_plan=id_plan}", many = @Many(select = "com.kretco.mintrab.sistpoa.Mappers.Proceso.listaPlanProcesos"))
    })


    List<Plan> listaPlan1(Integer id_plan);
     * @Insert("insert into mteps_plan.pln_plan(id_plan, id_plan_superior,sigla,nombre) values (#{id_plan}, #{id_plan_superior}, #{sigla}, #{nombre})"
     * ) void adicionar(Plan dato);
     * 
     * @Update("update mteps_plan.pln_plan set id_plan=#{id_plan}, id_plan_superior=#{id_plan_superior}, sigla=#{sigla}, nombre=#{nombre} where id_plan=#{id_plan} "
     * ) void modificar(Plan dato);
     */
}
