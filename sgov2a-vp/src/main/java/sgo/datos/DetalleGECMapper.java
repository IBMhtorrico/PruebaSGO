package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DetalleGEC;
import sgo.utilidades.Utilidades;
public class DetalleGECMapper implements RowMapper<DetalleGEC>{
  public DetalleGEC mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    DetalleGEC eDetalleGEC = null;
    try {
      eDetalleGEC = new DetalleGEC();
      eDetalleGEC.setId(rs.getInt("id_dgec"));
      eDetalleGEC.setIdGuiaCombustible(rs.getInt("id_gcombustible"));
      eDetalleGEC.setNumeroGuia(Utilidades.cleanXSS(rs.getString("numero_guia")));
      eDetalleGEC.setFechaEmision(rs.getDate("fecha_emision"));
      eDetalleGEC.setFechaRecepcion(rs.getDate("fecha_recepcion"));
      eDetalleGEC.setVolumenDespachado(rs.getFloat("volumen_despachado"));
      eDetalleGEC.setVolumenRecibido(rs.getFloat("volumen_recibido"));
      eDetalleGEC.setEstado(Utilidades.cleanXSS(rs.getString("estado")));

    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDetalleGEC;
  }
}