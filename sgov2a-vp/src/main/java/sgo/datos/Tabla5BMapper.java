package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Tabla5B;
public class Tabla5BMapper implements RowMapper<Tabla5B>{
  public Tabla5B mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Tabla5B eTabla5B = null;
    try {
      eTabla5B = new Tabla5B();
      eTabla5B.setId(rs.getInt("id_tabla5b"));
      eTabla5B.setTemperaturaObservada(rs.getFloat("temperatura_observada"));
      eTabla5B.setApiObservado(rs.getFloat("api_observado"));
      eTabla5B.setApiCorregido(rs.getFloat("api_corregido"));
      //Parametros de auditoria
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eTabla5B;
  }
}