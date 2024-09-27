package mteps.ovt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.ovt.entity.EntidadUsuarioRup;

@Repository
public interface UsuarioRupInterface extends JpaRepository<EntidadUsuarioRup, Integer>{
	
		@Query(value = "select x.* from mteps_tramites.vm_ovt_usuarios_rup x where x.usuario = :usuario", nativeQuery = true)
		EntidadUsuarioRup obtenerUsuarioRup(@Param("usuario") String usuario);

}
