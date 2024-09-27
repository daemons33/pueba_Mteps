package mteps.config.procedimiento;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mteps.config.ConfigEntity;

@Repository
public interface ConfiguracionRepository extends JpaRepository<ConfigEntity, Integer>,CustomConfiguracionRepository  {
	/*
	@Query(value = "call mteps_rtep.p_gestion_usuario(:json_input)", nativeQuery = true)
	ConfigEntity callGestionUsuario(@Param("json_input") String json_input);*/

}
