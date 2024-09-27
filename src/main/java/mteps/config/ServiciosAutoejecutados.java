package mteps.config;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import mteps.rtep.RtepCORE;
import mteps.tramites.repository.ListaTramitesInterface;


@Component
@EnableScheduling
public class ServiciosAutoejecutados {
	

	@Value("${spring.profiles.active}")
	private String servAuto;
	
	@Autowired
	public ListaTramitesInterface listaTramitesInterface;
	
	@Autowired
	
	public RtepCORE rtepCore;
	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	
	

	//@Scheduled(cron = "0 * * * * ?") //cada min
	@Scheduled(cron = "0 0 2 * * ?")
    public void refreshMaterializedViews() {
        try {
        	
        	LocalDateTime now = LocalDateTime.now();

            // Definir el formato deseado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            // Formatear la hora actual a String
            String formattedNow = now.format(formatter);

            // Mostrar la hora actual
            System.out.println("[CRON_VM] Hora inicio [1/7: EMPRESAS]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_empresas");
          //  
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [2/7: SUCURSALES]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_sucursales");
            
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [3/7: PERSONA EMPLEADOR]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_persona_empleador");
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [4/7: PERSONA]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_persona");
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [5/7: DEPOSITOS]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_depositos");
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [6/7: PERIODO DECLARACION]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.vm_ovt_periodo_declaracion");
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM] Hora inicio [7/7: PERIODO DECLARACION]: " + formattedNow);
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW mteps_tramites.mteps_tramites.vm_ovt_usuarios_rup");
            
            
            now = LocalDateTime.now(); formattedNow = now.format(formatter);
            System.out.println("[CRON_VM FINALIZADO]: " + formattedNow);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[CRON_VM ERROR_VISTAS_M]: " + e.getMessage());
        }
    }
	
	
	
		
	@Scheduled(cron = "0 5 0 * * ?") // 00:05:00
	public void actualizarDticketsNoAtendidos() throws ClassNotFoundException, SQLException {
		
		listaTramitesInterface.cronActualizarTickets();
		
	}
	/*
	@Scheduled(cron = "0 * * * * ?") //cada min
	    public void verificacionSegip() throws JsonProcessingException, SQLException {
		
	    	rtepCore.verificacionSEGIP();
		}
	*/
}
