package mteps.ovt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mteps.ovt.entity.EntidadSucursal;

public interface SucursalesInterface extends JpaRepository<EntidadSucursal, Integer> {

	
	@Query(value = "select * from mteps_tramites.vm_ovt_sucursales s where s.fid_empresa = :id_empresa order by s.nro_sucursal", nativeQuery = true)
	 List<EntidadSucursal> listaSucursales(@Param("id_empresa") Integer id_empresa);
	
}
