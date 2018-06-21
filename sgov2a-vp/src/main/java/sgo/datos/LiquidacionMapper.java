package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Liquidacion;
import sgo.utilidades.Utilidades;
public class LiquidacionMapper implements RowMapper<Liquidacion>{
  public Liquidacion mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Liquidacion eLiquidacion = null;
    try {
      eLiquidacion = new Liquidacion();
      eLiquidacion.setIdOperacion(rs.getInt("id_operacion"));
      eLiquidacion.setFechaOperativa(rs.getDate("fecha_operativa"));
      eLiquidacion.setIdProducto(rs.getInt("id_producto"));
      eLiquidacion.setPorcentajeActual(rs.getFloat("porcentaje_actual"));
      eLiquidacion.setStockFinal(rs.getFloat("stock_final"));
      eLiquidacion.setStockInicial(rs.getFloat("stock_inicial"));
      eLiquidacion.setVolumenDescargado(rs.getFloat("volumen_descargado"));
      eLiquidacion.setVolumenDespacho(rs.getFloat("volumen_despacho"));
      eLiquidacion.setTolerancia(rs.getFloat("tolerancia"));
      eLiquidacion.setStockFinalCalculado(rs.getFloat("stock_final_calculado"));
      eLiquidacion.setVariacion(rs.getFloat("variacion"));
      eLiquidacion.setVariacionAbsoluta(rs.getFloat("variacion_absoluta"));
      eLiquidacion.setNombreProducto(Utilidades.cleanXSS(rs.getString("nombre_producto")));
      eLiquidacion.setNombreOperacion(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
      eLiquidacion.setNombreCliente(Utilidades.cleanXSS(rs.getString("nombre_cliente")));
      eLiquidacion.setFaltante(rs.getFloat("faltante"));
      eLiquidacion.setIdEstacion(rs.getInt("id_estacion"));
      eLiquidacion.setIdTanque(rs.getInt("id_tanque"));
      eLiquidacion.setNombreEstacion(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
      eLiquidacion.setNombreTanque(Utilidades.cleanXSS(rs.getString("nombre_tanque")));
      //Parametros de auditoria
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eLiquidacion;
  }
}