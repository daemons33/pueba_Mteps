package mteps.tramites.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.tramites.entity.ListaTramites;


@Repository
public interface ListaTramitesInterface extends JpaRepository<ListaTramites, Integer> {
	

	 @Query(value = "select * from mteps_tramites.f_obtener_tramites(:p_json_pp)", nativeQuery = true)
	 List<ListaTramites> listaTramites(@Param("p_json_pp") String p_json_pp);
	 
	 @Query(value = "select * from mteps_tramites.f_obtener_tramites_tickets(:id_ticket)", nativeQuery = true)
	 List<ListaTramites> listaTramitesTickets(@Param("id_ticket") Integer id_ticket);

	 @Query(value = "select * from mteps_d_tickets.f_cron_actualizacion_tickets_no_atendidos()", nativeQuery = true)
	 int cronActualizarTickets();
	 
}
