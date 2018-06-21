package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Compartimento;
public class CompartimentoMapper implements RowMapper<Compartimento>{
  public Compartimento mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Compartimento eCompartimento = null;
    try {
      eCompartimento = new Compartimento();
      eCompartimento.setId(rs.getInt("id_compartimento"));
      eCompartimento.setIdentificador(rs.getInt("identificador"));
      eCompartimento.setCapacidadVolumetrica(rs.getFloat("capacidad_volumetrica"));
      eCompartimento.setAlturaFlecha(rs.getInt("altura_flecha"));
      eCompartimento.setIdCisterna(rs.getInt("id_cisterna"));
      eCompartimento.setIdTracto(rs.getInt("id_tracto"));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eCompartimento;
  }
}