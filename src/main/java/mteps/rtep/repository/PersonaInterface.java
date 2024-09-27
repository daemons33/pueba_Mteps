package mteps.rtep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.rtep.entity.PersonaEntity;

@Repository
public interface PersonaInterface extends JpaRepository<PersonaEntity, Long>{
	
	
	@Query(value = "SELECT rp.*,null as tipo_documento_nombre,null as lugar_expedido_nombre ,null as genero_nombre,null as pais_nombre ,null as departamento_nombre,null as municipio_nombre,null as provincia_nombre, null as correo_electronico \r\n"
			+ "FROM mteps_rtep.rtep_persona rp \r\n"
			+ "INNER JOIN mteps_rtep.rtep_usuarios ru ON ru.id_usuario = rp.id_usuario \r\n"
			+ "WHERE current_timestamp >= rp.fecha_programada_validacion_segip\r\n"
			+ "AND rp.validacion_segip = false\r\n"
			+ "AND ru.estado = true\r\n"
			+ "AND ru.ciudadania_digital = false and notificado=false and rp.activo=true", nativeQuery = true)
	 List<PersonaEntity> obtenerUsuariosContrastacionSegip();
	
	@Query(value = "SELECT rp.*,pc1.nombre as tipo_documento_nombre,pc2.nombre as lugar_expedido_nombre ,pc3.nombre as genero_nombre,pc4.nombre as pais_nombre ,pc6.nombre as departamento_nombre,pc7.nombre as municipio_nombre,pc8.nombre as provincia_nombre,ru.correo_electronico \r\n"
			+ "			FROM mteps_rtep.rtep_persona rp \r\n"
			+ "			INNER JOIN mteps_rtep.rtep_usuarios ru ON ru.id_usuario = rp.id_usuario \r\n"
			+ "			left join parametro.par_clasificador pc1 on pc1.id_clasificador =  rp.tipo_documento \r\n"
			+ "			left join parametro.par_clasificador pc2 on pc2.id_clasificador =  rp.lugar_expedido \r\n"
			+ "			left join parametro.par_clasificador pc3 on pc3.id_clasificador =  rp.genero \r\n"
			+ "			left join parametro.par_clasificador pc4 on pc4.id_clasificador = rp.pais \r\n"
			+ "			left join parametro.par_clasificador pc6 on pc6.id_clasificador = rp.departamento \r\n"
			+ "			left join parametro.par_clasificador pc7 on pc7.id_clasificador =  rp.municipio \r\n"
			+ "			left join parametro.par_clasificador pc8 on pc8.id_clasificador = rp.provincia \r\n"
			+ "			WHERE ru.id_usuario=:id and rp.activo=true", nativeQuery = true)
	PersonaEntity obtenerPersonaId(@Param("id") Long id);

}
