package mteps.ovt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.ovt.entity.EntidadDepositos;


@Repository
public interface DepositosInterface extends JpaRepository<EntidadDepositos, String>{

	@Query(value = "select * from mteps_tramites.vm_ovt_depositos d where d.nro_movimiento = :nro_movimiento and DATE_TRUNC('day', d.fecha_movimiento) = DATE(:fecha_movimiento)", nativeQuery = true)
	 List<EntidadDepositos> obtenerDeposito(@Param("nro_movimiento") String nro_movimiento,@Param("fecha_movimiento") String fecha_movimiento);
	
	@Query(value = "select * from mteps_tramites.vm_ovt_depositos d where d.nro_movimiento = :nro_movimiento and DATE_TRUNC('day', d.fecha_movimiento) = DATE(:fecha_movimiento) and d.monto =:monto", nativeQuery = true)
	 List<EntidadDepositos> obtenerDepositoMonto(@Param("nro_movimiento") String nro_movimiento,@Param("fecha_movimiento") String fecha_movimiento, @Param("monto") Double monto);
}
