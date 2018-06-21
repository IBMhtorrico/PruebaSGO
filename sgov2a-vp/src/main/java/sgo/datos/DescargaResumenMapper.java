package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.DescargaResumen;
import sgo.utilidades.Utilidades;
public class DescargaResumenMapper implements RowMapper<DescargaResumen>{
  public DescargaResumen mapRow(ResultSet rs, int arg1) throws SQLException 
  {
   DescargaResumen eDescarga= null;
    try {
      eDescarga = new DescargaResumen();
      eDescarga.setId(rs.getInt("id_dcisterna")); 
      eDescarga.setIdCargaTanque(rs.getInt("id_ctanque"));
      eDescarga.setIdTransporte(rs.getInt("id_transporte"));
      eDescarga.setPlacaCisterna(Utilidades.cleanXSS(rs.getString("placa_tracto")));
      eDescarga.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_cisterna")));
      eDescarga.setNumeroGuia(Utilidades.cleanXSS(rs.getString("numero_guia_remision")));
      eDescarga.setDespachado(rs.getFloat("despachado"));
      eDescarga.setRecibido(rs.getFloat("recibido"));
      eDescarga.setVariacion(rs.getFloat("variacion"));
      eDescarga.setEstado(rs.getInt("estado"));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDescarga;
  }
}