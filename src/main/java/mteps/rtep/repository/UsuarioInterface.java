package mteps.rtep.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.rtep.entity.UsuarioEntity;

@Repository
public interface UsuarioInterface extends JpaRepository<UsuarioEntity, Long>{
	
	@Query(value = "select * from mteps_rtep.rtep_usuarios ru where ru.id_usuario = :id", nativeQuery = true)
	UsuarioEntity ObtenerUsuario(@Param("id") Long id);
	

}
