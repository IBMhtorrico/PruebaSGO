package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DetalleGecVista;
import sgo.utilidades.Utilidades;
public class DetalleGecVistaMapper implements RowMapper<DetalleGecVista>{
  public DetalleGecVista mapRow(ResultSet rs, int arg1) throws SQLException 
  {
   DetalleGecVista eDetalleGEC = null;
    try {
      eDetalleGEC = new DetalleGecVista();
      eDetalleGEC.setIdTransporte(rs.getInt("id_transporte"));
      eDetalleGEC.setIdDcisterna(rs.getInt("id_dcisterna"));
      eDetalleGEC.setNumeroGuiaRemision(Utilidades.cleanXSS(rs.getString("numero_guia_remision")));
      eDetalleGEC.setFechaArribo(rs.getDate("fecha_arribo"));
      eDetalleGEC.setFechaFiscalizacion(rs.getDate("fecha_fiscalizacion"));
      eDetalleGEC.setVolumenDespachadoObservado(rs.getFloat("volumen_despachado_observado"));
      eDetalleGEC.setVolumenDespachadoCorregido(rs.getFloat("volumen_despachado_corregido"));
      eDetalleGEC.setEstadoDiaOperativo(rs.getInt("estado_dia_operativo"));
      eDetalleGEC.setFechaOperativa(rs.getDate("fecha_operativa"));
      eDetalleGEC.setFechaEmision(rs.getDate("fecha_emision"));
      eDetalleGEC.setVolumenRecibidoObservado(rs.getFloat("volumen_recibido_observado"));
      eDetalleGEC.setVolumenRecibidoCorregido(rs.getFloat("volumen_recibido_corregido"));
      eDetalleGEC.setIdProducto(rs.getInt("id_producto"));
      eDetalleGEC.setIdTransportista(rs.getInt("id_transportista"));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDetalleGEC;
  }
}