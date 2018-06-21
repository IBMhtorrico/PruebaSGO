package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.AforoTanque;
import sgo.utilidades.Utilidades;
public class AforoTanqueMapper implements RowMapper<AforoTanque>{
  public AforoTanque mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    AforoTanque eAforoTanque = null;
    try {
      eAforoTanque = new AforoTanque();
      eAforoTanque.setId(rs.getInt("id_atanque"));
      eAforoTanque.setCentimetros(rs.getFloat("centimetros"));
      eAforoTanque.setIdTanque(rs.getInt("id_tanque"));
      eAforoTanque.setVolumen(rs.getFloat("volumen"));
      //Parametros de auditoria
      eAforoTanque.setCreadoPor(rs.getInt("creado_por"));
      eAforoTanque.setCreadoEl(rs.getLong("creado_el"));
      eAforoTanque.setActualizadoPor(rs.getInt("actualizado_por"));
      eAforoTanque.setActualizadoEl(rs.getLong("actualizado_el"));
      eAforoTanque.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eAforoTanque.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
      eAforoTanque.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
      eAforoTanque.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
      
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eAforoTanque;
  }
}