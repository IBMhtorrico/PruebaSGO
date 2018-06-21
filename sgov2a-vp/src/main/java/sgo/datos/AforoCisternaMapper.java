package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.AforoCisterna;
import sgo.entidad.Cisterna;
import sgo.entidad.Compartimento;
import sgo.entidad.Tracto;
import sgo.utilidades.Utilidades;
public class AforoCisternaMapper implements RowMapper<AforoCisterna>{
  public AforoCisterna mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    AforoCisterna eAforoCisterna = null;
    Tracto eTracto = null;
    Cisterna eCisterna = null;
    Compartimento eCompartimento = null;
    try {
      eAforoCisterna = new AforoCisterna();
      eAforoCisterna.setId(rs.getInt("id_acisterna"));
      eAforoCisterna.setIdCisterna(rs.getInt("id_cisterna"));
      eAforoCisterna.setIdTracto(rs.getInt("id_tracto"));
      eAforoCisterna.setIdCompartimento(rs.getInt("id_compartimento"));
      eAforoCisterna.setMilimetros(rs.getFloat("milimetros"));
      eAforoCisterna.setVolumen(rs.getFloat("volumen"));
      eAforoCisterna.setVariacionMilimetros(rs.getFloat("variacion_milimetros"));
      eAforoCisterna.setVariacionVolumen(rs.getFloat("variacion_volumen"));
      
      eTracto = new Tracto();
      eTracto.setId(rs.getInt("id_tracto"));
      eTracto.setPlaca(Utilidades.cleanXSS(rs.getString("placa_tracto")));
      eAforoCisterna.setTracto(eTracto);
      
      eCisterna = new Cisterna();
      eCisterna.setId(rs.getInt("id_cisterna"));
      eCisterna.setPlaca(Utilidades.cleanXSS(rs.getString("placa_cisterna")));
      eAforoCisterna.setCisterna(eCisterna);
      
      eCompartimento = new Compartimento();
      eCompartimento.setId(rs.getInt("id_compartimento"));
      eCompartimento.setIdentificador(rs.getInt("numero_compartimento"));
      eCompartimento.setAlturaFlecha(rs.getInt("altura_flecha"));
      eCompartimento.setCapacidadVolumetrica(rs.getFloat("capacidad_volumetrica"));
      eAforoCisterna.setCompartimento(eCompartimento);
      
      //Parametros de auditoria
      eAforoCisterna.setCreadoPor(rs.getInt("creado_por"));
      eAforoCisterna.setCreadoEl(rs.getLong("creado_el"));
      eAforoCisterna.setActualizadoPor(rs.getInt("actualizado_por"));
      eAforoCisterna.setActualizadoEl(rs.getLong("actualizado_el"));
      eAforoCisterna.setUsuarioActualizacion(rs.getString("usuario_actualizacion"));
      eAforoCisterna.setUsuarioCreacion(rs.getString("usuario_creacion"));
      eAforoCisterna.setIpCreacion(rs.getString("ip_creacion"));
      eAforoCisterna.setIpActualizacion(rs.getString("ip_actualizacion"));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eAforoCisterna;
  }
}