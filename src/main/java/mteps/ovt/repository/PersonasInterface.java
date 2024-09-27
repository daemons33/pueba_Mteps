package mteps.ovt.repository;



import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.ovt.entity.EntidadPersona;

@Repository
public interface PersonasInterface extends JpaRepository<EntidadPersona,Integer> {
	
	@Query(value = "SELECT * FROM ((SELECT * from mteps_tramites.vm_ovt_persona_empleador vope \r\n"
			+ "							where vope.nit = :nit and vope.fecha_nacimiento =:fecha_nacimiento  and vope.nro_documento =:nro_documento order by vope.id_trabajador desc LIMIT 1)\r\n"
			+ "                     UNION ALL\r\n"
			+ "					     SELECT *  from mteps_tramites.vm_ovt_persona vop  where vop.fecha_nacimiento =:fecha_nacimiento and vop.nro_documento =:nro_documento LIMIT 1 \r\n"
			+ "							) AS combinado ORDER BY tipo LIMIT 1", nativeQuery = true)
	 EntidadPersona obtenerPersona(@Param("nit") String nit, @Param("fecha_nacimiento") Date fecha_nacimiento,@Param("nro_documento") String nro_documento );

}
