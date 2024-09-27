package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class WfRolEstado implements Serializable {
    
   public Integer id_rol_estado;
   public Integer id_rol;
   public Integer id_estado_anterior;
   public Integer id_estado_actual;
   public Integer id_estado;
   public String accion;

   public Rol rol;

   //public Integer codigo_resp;
   //public String mensaje_resp;

   /*public WfRolEstado(Integer codigo_resp, String mensaje_resp) {
      this.codigo_resp = codigo_resp;
      this.mensaje_resp = mensaje_resp;
   }*/
}
