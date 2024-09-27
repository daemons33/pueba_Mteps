package mteps.ovt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mteps.ovt.entity.EntidadEmpresa;

@Repository
public interface EmpresasInterface extends JpaRepository<EntidadEmpresa, Integer>{
	

	@Query(value = "select *, false as empresa_externa, e.nit_empresa as nit from mteps_tramites.vm_ovt_empresas e where UPPER(e.nit_empresa||coalesce (e.matricula_comercio,'')||e.razon_social) like UPPER('%'||replace(trim(concat(:nit,:matricula,:razon_social)), ' ', '%')||'%')", nativeQuery = true)
	 List<EntidadEmpresa> listaEmpresas(@Param("nit") String nit,@Param("matricula") String matricula,@Param("razon_social") String razon_social);
	
	@Query(value = "select *, false as empresa_externa, e.nit_empresa as nit from mteps_tramites.vm_ovt_empresas e where e.id_empresa=:id_empresa", nativeQuery = true)
	 EntidadEmpresa obtenerEmpresa(@Param("id_empresa") Integer id_empresa);
	
	@Query(value = "select *, false as empresa_externa, e.nit_empresa as nit from mteps_tramites.vm_ovt_empresas e where e.nit_empresa=:nit limit 1", nativeQuery = true)
	 EntidadEmpresa obtenerEmpresaNIT(@Param("nit") String nit);
	
}
