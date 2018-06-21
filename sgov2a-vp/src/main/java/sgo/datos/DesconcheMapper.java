package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Desconche;
import sgo.utilidades.Utilidades;
public class DesconcheMapper implements RowMapper<Desconche>{
  public Desconche mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    Desconche eDesconche = null;
    try {
      eDesconche = new Desconche();
      eDesconche.setId(rs.getInt("id_desconche"));
      eDesconche.setIdCompartimento(rs.getInt("id_compartimento"));
      eDesconche.setIdTanque(rs.getInt("id_tanque"));
      eDesconche.setNumeroDesconche(rs.getInt("numero_desconche"));
      eDesconche.setVolumen(rs.getInt("volumen"));
      //Parametros de auditoria
      eDesconche.setCreadoPor(rs.getInt("creado_por"));
      eDesconche.setCreadoEl(rs.getLong("creado_el"));
      eDesconche.setActualizadoPor(rs.getInt("actualizado_por"));
      eDesconche.setActualizadoEl(rs.getLong("actualizado_el"));
      eDesconche.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eDesconche.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDesconche;
  }
}
