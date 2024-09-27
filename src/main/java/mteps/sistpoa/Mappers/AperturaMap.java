package mteps.sistpoa.Mappers;

import org.apache.ibatis.annotations.*;

import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.POperacionProcesoUnidad;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@Mapper
//@CacheNamespace
public interface AperturaMap {

    //Lista de aperturas
    @Select("select id_apertura, id_apertura_superior, id_gestion, valor_gestion, cod_entidad, cod_programa, " +
            "cod_proyecto, cod_actividad, descripcion, ponderacion from mteps_plan.f_lista_aperturas(#{id_gestion})")
    List<Apertura> listaAperturas(Integer id_gestion);

    @Select("select id_apertura_organizacion,id_apertura,id_unidad_organizacional " +
            "from mteps_plan.f_lista_apertura_organizacional(#{id_apertura})")
    List<AperturaOrganizacion> listaAperturaOrganizacion(Integer id_apertura);

    @Select("select id_accion_poa_ape, id_proceso, id_apertura " +
            "from mteps_plan.f_lista_accion_poa_ape(#{id_proceso}, #{id_apertura})")
    List<AccionPoaApertura> listaAccionPoaApertura(Integer id_proceso, Integer id_apertura);

    //Lista de aperturas
    @Select("select id_apertura, id_apertura_superior, id_gestion, valor_gestion, cod_entidad, cod_programa, " +
            "cod_proyecto, cod_actividad, descripcion, ponderacion from mteps_plan.f_lista_aperturas()")
    List<Apertura> listaAperturas1();

//    @Select("select id_accion_poa_ape, id_proceso, id_apertura, sigla, nombre, descripcion " +
//            "from mteps_plan.f_lista_accion_poa_ape(#{id_apertura})")
//    List<AccionPoaApertura> listaAccionPoaApertura1(Integer id_apertura);
    
    @Select("select id_apertura_organizacion, id_plan,id_proceso_superior, id_proceso, sigla, nombre, descripcion " +
            "from mteps_plan.f_lista_acc_procesos_poa(#{id_apertura},#{id_tipo_proceso})")
    List<AccionPoaApertura> listaAccionPoaApertura1(Integer id_apertura,Integer id_tipo_proceso);
    

    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_ins_act_sol_certificacion( " +
            "#{id_solicitud}::integer, #{id_proceso}, #{codigo}, #{fecha_solicitud}::timestamp, " +
            "#{descripcion_procesos}::json, #{estado_solicitud}, #{id_usuario}, #{id_estado}, #{observacion}, #{fecha_aprobacion}::timestamp, #{gestion})")
    List<PResultado001>InsertaActualizaSolicitudCertificacion(Solicitud solicitud);

    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_ins_act_det_certificacion( " +
            "#{id_detalle}::integer, #{id_solicitud}::integer, #{codigo}::varchar, #{detalle_descripcion}::varchar, #{cantidad}::numeric, " +
            "#{precio_referencial}::numeric, #{total_precio_referencial}::numeric, " +
            "#{saldo_certificacion}::numeric, #{id_estado}::integer, #{partida}::varchar, #{fuente}::varchar, #{id_memoria_calculo}::integer)")
    List<PResultado001>InsertaActualizaDetalleSolicitudCertificacion(SolicitudDetalle detalle);

    @Select("select id_solicitud, id_proceso, codigo, fecha_solicitud, descripcion_procesos, " +
            " estado_solicitud, id_usuario, observacion, fecha_cre, fecha_aprobacion, justificacion, estado_anterior " +
            "from mteps_plan.pln_solicitud_certificacion " +
            " where id_solicitud=#{id_solicitud} and id_estado=1")
    List<Solicitud>BuscaSolicitudCertificacion(Integer id_solicitud);

//    @Select("select id_solicitud, id_proceso, codigo, fecha_solicitud, descripcion_procesos, " +
//            " estado_solicitud, id_usuario, observacion,fecha_cre, fecha_aprobacion, justificacion, estado_anterior " +
//            " from mteps_plan.pln_solicitud_certificacion " +
//            " where estado_solicitud=#{estado_solicitud} and id_estado=1")
//    List<Solicitud>ListaSolicitudCertificacionEstado(String estado_solicitud);
    
    @Select("select c.id_solicitud, c.id_proceso, c.codigo, c.fecha_solicitud, c.descripcion_procesos,c.estado_solicitud, c.id_usuario, c.observacion,c.fecha_cre, c.fecha_aprobacion, c.justificacion, c.estado_anterior, c.transaccion as transaccion , c.estado ,\r\n"
    		+ "             (pe.per_nombres || ' ' || pe.per_paterno  || ' ' ||  pe.per_materno) as nombreUsuario, o.org_unidad_funcional, o2.org_unidad_funcional as org_unidad_funcional2\r\n"
    		+ "            from mteps_plan.pln_solicitud_certificacion c left join  workflow.wf_usuario w on c.id_usuario = w.id_usuario left join   mteps_rrhh.rrhh_persona pe on w.id_persona = pe.id_persona\r\n"
    		+ "            left join mteps_rrhh.rrhh_item_persona i on i.id_persona = pe.id_persona left join mteps_rrhh.rrhh_item it on it.id_item = i.id_item left join mteps_rrhh.rrhh_item_organigrama io\r\n"
    		+ "            on io.id_item = it.id_item left join mteps_rrhh.rrhh_organigrama o on o.id_org  = io.id_org left join mteps_rrhh.rrhh_organigrama o2 on o.org_depende = o2.id_org \r\n"
    		+ "             where estado_solicitud= #{estado_solicitud} and id_estado=1")
    List<Solicitud>ListaSolicitudCertificacionEstado(String estado_solicitud);

    @Select("select id_detalle, id_solicitud, codigo, detalle_descripcion, cantidad, " +
            "precio_referencial, total_precio_referencial, saldo_certificacion, id_estado, partida, fuente, monto_revertido, id_memoria_calculo " +
            " from mteps_plan.pln_detalle_certificacion where id_solicitud=#{id_solicitud} and id_estado=1")
    List<SolicitudDetalle>ListaDetalleCertificacion(Integer id_solicitud);


    @Select("select opo.id_operacion_poa_org, apo.id_apertura, opo.id_proceso, opo.id_unidad_organizacional, " +
            "org.sigla as sigla_unidad_organizacional, org.nombre as nombre_unidad_organizacional, " +
            "pro.sigla as sigla_proceso,pro.nombre as nombre_proceso, pro.descripcion as descripcion_proceso, " +
            "opo.id_estado from mteps_plan.pln_operacion_poa_organizacion opo " +
            "inner join mteps_plan.pln_proceso pro using (id_proceso) " +
            "inner join mteps_plan.par_unidad_organizacional org using (id_unidad_organizacional) " +
            "inner join mteps_plan.pln_apertura_organizacion apo using (id_unidad_organizacional) " +
            "where id_unidad_organizacional=#{id_unidad_organizacional} AND id_apertura=#{id_apertura} and opo.id_estado=1")
    List<POperacionProcesoUnidad>ListaOperacionPoaxAperturaUnidad(Integer id_apertura, Integer id_unidad_organizacional);


    @Select("select opo.id_operacion_poa_org, apo.id_apertura, opo.id_proceso, opo.id_unidad_organizacional, " +
            "org.sigla as sigla_unidad_organizacional, org.nombre as nombre_unidad_organizacional, " +
            "pro.sigla as sigla_proceso,pro.nombre as nombre_proceso, pro.descripcion as descripcion_proceso, " +
            "opo.id_estado from mteps_plan.pln_operacion_poa_organizacion opo " +
            "inner join mteps_plan.pln_proceso pro using (id_proceso) " +
            "inner join mteps_plan.par_unidad_organizacional org using (id_unidad_organizacional) " +
            "inner join mteps_plan.pln_apertura_organizacion apo using (id_unidad_organizacional) " +
            "where id_apertura=#{id_apertura} and opo.id_estado=1")
    List<POperacionProcesoUnidad>ListaOperacionPoaxApertura(Integer id_apertura);


    @Select("SELECT opo.id_operacion_poa_org, opo.id_proceso, opo.id_unidad_organizacional, " +
            "org.sigla as sigla_unidad_organizacional, org.nombre as nombre_unidad_organizacional, " +
            "pro.sigla as sigla_proceso,pro.nombre as nombre_proceso, pro.descripcion as descripcion_proceso , opo.id_estado " +
            "from mteps_plan.pln_operacion_poa_organizacion opo " +
            "inner join mteps_plan.pln_proceso pro using (id_proceso) " +
            "inner join mteps_plan.par_unidad_organizacional org using (id_unidad_organizacional) " +
            "where id_unidad_organizacional=#{id_unidad_organizacional} and opo.id_estado=1")
    List<POperacionProcesoUnidad>ListaOperacionPoaxUnidadOrganizacional(Integer id_unidad_organizacional);

    //ESTADO SOLICITUD CERTIFICACION
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_estado_solicitud(#{id_solicitud},#{estado_solicitud},#{id_usuario},#{justificacion},#{observacion},#{usr_firma})")
    List<PResultado001> actualizaEstadoSolicitud(Integer id_solicitud, String estado_solicitud,Integer id_usuario, String justificacion, String observacion, Integer usr_firma);

    //Actualizacion Foda
    //@Select("select codigo_resp, mensaje_resp from mteps_plan.f_actualiza_foda(#{id_foda}, #{id_gestion}, #{fortaleza}, #{oportunidad}, #{debilidad}, #{amenaza}, #{id_usuario}, #{host}, #{fecha_sistema}::timestamp)")
    //List<Proceso> actualizaFoda(Foda foda);

    @Select("select * from mteps_plan.pln_detalle_certificacion where id_solicitud=#{id_solicitud} and id_estado != 0")
    List<SolicitudDetalle>listaDetalleCertificacionxProceso(Integer id_solicitud);

    //REVERTIR 
    @Select("select codigo_resp, mensaje_resp from mteps_plan.f_registra_reversion_detalle(#{id_detalle},#{id_estado},#{id_solicitud},#{id_usuario},#{monto}::numeric)")
    List<PResultado001> getRegistraRevision(Integer id_detalle, Integer id_estado,Integer id_solicitud, Integer id_usuario, double monto);

    //Actualizacion Foda
    
    @Select("select psc.*, (select json_agg(to_json(pdc.*))::json as solicitud_detalle from mteps_plan.pln_detalle_certificacion pdc where pdc.id_solicitud = psc.id_solicitud) from mteps_plan.pln_solicitud_certificacion psc where id_proceso=#{id_proceso} and psc.id_estado != 0")
    List<Solicitud>listaSolicitudCertificacionxProceso(Integer id_proceso);

}
