package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.BusquedaProgramado;
public class BusquedaProgramadoMapper implements RowMapper<BusquedaProgramado>{
  public BusquedaProgramado mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    BusquedaProgramado eBusquedaProgramado = null;
    try {
      eBusquedaProgramado = new BusquedaProgramado();
      eBusquedaProgramado.setIdDiaOperativo(rs.getInt("id_doperativo"));
      eBusquedaProgramado.setIdOperacion(rs.getInt("id_operacion"));
      eBusquedaProgramado.setFechaOperativa(rs.getDate("fecha_operativa"));
      eBusquedaProgramado.setIdTransportista(rs.getInt("id_transportista"));
      eBusquedaProgramado.setIdConductor(rs.getInt("id_conductor"));
      eBusquedaProgramado.setIdCisterna(rs.getInt("id_cisterna"));
      eBusquedaProgramado.setProgramacion(rs.getInt("programacion"));
      eBusquedaProgramado.setIdTransporte(rs.getInt("id_transporte"));
      eBusquedaProgramado.setEstado(rs.getInt("estado"));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eBusquedaProgramado;
  }
}