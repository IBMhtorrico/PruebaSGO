package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.ComboDesconche;
import sgo.utilidades.Utilidades;

public class ComboDesconcheMapper implements RowMapper<ComboDesconche>{
 public ComboDesconche mapRow(ResultSet rs, int arg1) throws SQLException 
 {
  ComboDesconche eComboDesconche = null;
   try {
    eComboDesconche = new ComboDesconche();
    eComboDesconche.setId(rs.getInt("id_dcisterna"));
    eComboDesconche.setFechaPlanificada(Utilidades.cleanXSS(rs.getString("dia_operativa")));
    eComboDesconche.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_tracto")));
    eComboDesconche.setPlacaCisterna(Utilidades.cleanXSS(rs.getString("placa")));
    eComboDesconche.setEstacion(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
    eComboDesconche.setTanque(Utilidades.cleanXSS(rs.getString("nombre_tanque")));
    eComboDesconche.setNumeroCompartimentos(rs.getInt("numero_compartimentos"));
    eComboDesconche.setNumeroMaximoDesconches(rs.getInt("numero_maximo_desconche"));
   } catch(Exception ex){
     ex.printStackTrace();
   }
   return eComboDesconche;
 }
}

