package mteps.config.procedimiento;

import mteps.config.ConfigEntity;

public interface CustomConfiguracionRepository {
	ConfigEntity callProcedure(String procedimiento, String jsonInput);
}
