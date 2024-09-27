package mteps.tramites.fondoCustodia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.tramites.fondoCustodia.entity.ListaFcIngreso;

@Repository
public interface ListaFcIngresoInterface extends JpaRepository<ListaFcIngreso, Integer> {
	
	@Query(value = "select * from mteps_tramites.f_obtener_fc_ingresos(:p_json_pp)", nativeQuery = true)
	 List<ListaFcIngreso> listaFcIngresos(@Param("p_json_pp") String p_json_pp);

	
	@Query(value = "SELECT * from mteps_tramites.f_obtener_fc_ingresos_buscar(:buscar,:login)", nativeQuery = true)
	 List<ListaFcIngreso> busquedaTramiteFc(@Param("buscar") String buscar,@Param("login") String login);
	
	
	@Query(value = "select * from mteps_tramites.f_obtener_fc_reporte(:p_json_pp)", nativeQuery = true)
	 List<ListaFcIngreso> listaFcReporte(@Param("p_json_pp") String p_json_pp);
	
}
