package mteps.config.exception.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mteps.config.exception.entity.ExceptionEntity;

@Repository
public interface ExceptionEntityRepository extends JpaRepository<ExceptionEntity, Long>{

}
