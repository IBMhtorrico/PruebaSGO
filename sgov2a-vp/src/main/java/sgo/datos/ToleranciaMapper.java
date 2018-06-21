package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Producto;
import sgo.entidad.Tolerancia;
import sgo.utilidades.Utilidades;
public class ToleranciaMapper implements RowMapper<Tolerancia>{
  public Tolerancia mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Tolerancia eTolerancia = null;
    Producto producto = null;
    try {
      eTolerancia = new Tolerancia();
      eTolerancia.setId(Utilidades.cleanXSS(rs.getString("id_tolerancia")));
      eTolerancia.setIdEstacion(rs.getInt("id_estacion"));
      eTolerancia.setIdProducto(rs.getInt("id_producto"));
      eTolerancia.setTipoVolumen(rs.getInt("tipo_volumen"));
      eTolerancia.setPorcentajeActual(rs.getFloat("porcentaje_actual"));
      producto = new Producto();
      producto.setId(rs.getInt("id_producto"));
      producto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
      producto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura_producto")));
      eTolerancia.setProducto(producto);
      //Parametros de auditoria
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eTolerancia;
  }
}