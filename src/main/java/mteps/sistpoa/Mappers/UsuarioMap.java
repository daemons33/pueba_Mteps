package mteps.sistpoa.Mappers;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import mteps.sistpoa.Models.Usuario;
import mteps.sistpoa.Pojos.PResultado001;

@Mapper
//@CacheNamespace

public interface UsuarioMap {
    
    // listas Usuarios
    @Select("select o_nro as id_usuario, o_usuario as nombre, o_roles as roles, o_estado as estado,o_fecha_cre as fecha_cre,o_fecha_mod as fecha_mod from mteps_plan.f_lista_usuarios()")
    List<Usuario>listaUsuarios();

    @Select("select * from mteps_plan.f_actualiza_firma(#{id_usuario},#{firma})")
    List<PResultado001>actualizaFirma(Integer id_usuario, String firma);

    @Select("select * from mteps_plan.sis_usuario where id_usuario = #{id_usuario} and id_estado != 0")
    Usuario buscarUsuario(Integer id_usuario);



}
