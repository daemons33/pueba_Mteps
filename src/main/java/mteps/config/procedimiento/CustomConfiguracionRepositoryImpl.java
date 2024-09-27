package mteps.config.procedimiento;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mteps.config.ConfigEntity;

@Repository
public class CustomConfiguracionRepositoryImpl implements CustomConfiguracionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ConfigEntity callProcedure(String procedimiento, String jsonInput) {
        String sql = String.format("call %s(:json_input)", procedimiento);
        Query query = entityManager.createNativeQuery(sql, ConfigEntity.class);
        query.setParameter("json_input", jsonInput);
        return (ConfigEntity) query.getSingleResult();
    }
}
