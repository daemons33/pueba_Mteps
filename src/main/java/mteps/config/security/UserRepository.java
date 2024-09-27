package mteps.config.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.config.security.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "select u.idusuario,u.usuario,u.clave from seguridad.usuarios u where u.usuario= ?1 ",nativeQuery = true)
	Optional<User> findByUsuario(@Param("login") String usuario);
	
	@Query(value = "select u.idusuario,u.usuario,u.clave from seguridad.usuarios u where u.usuario= ?1 ",nativeQuery = true)
	User BuscarUsuario(@Param("login") String usuario);
	Boolean existsByUsuario(String usuario);

}
