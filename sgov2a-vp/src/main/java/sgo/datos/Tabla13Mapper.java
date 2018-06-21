package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Tabla13;
public class Tabla13Mapper implements RowMapper<Tabla13>{
  public Tabla13 mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Tabla13 eTabla13 = null;
    try {
      eTabla13 = new Tabla13();
      eTabla13.setId(rs.getInt("id_tabla13"));
      eTabla13.setApi(rs.getFloat("api"));
      eTabla13.setFactor(rs.getFloat("factor"));
      //Parametros de auditoria
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eTabla13;
  }
}