package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Aprobador;
import sgo.utilidades.Utilidades;
public class AprobadorMapper implements RowMapper<Aprobador>{
 public Aprobador mapRow(ResultSet rs, int arg1) throws SQLException 
 {
  Aprobador eAprobador = null;
   try {
    eAprobador = new Aprobador();
    eAprobador.setIdentidad(Utilidades.cleanXSS(rs.getString("identidad")));
    eAprobador.setUsuario(Utilidades.cleanXSS(rs.getString("nombre")));
    eAprobador.setInicioVigencia(rs.getDate("vigente_desde"));
    eAprobador.setFinVigencia(rs.getDate("vigente_hasta"));
   } catch(Exception ex){
     ex.printStackTrace();
   }
   return eAprobador;
 }
}