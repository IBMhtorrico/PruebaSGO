package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.CargaTanque;
import sgo.entidad.Estacion;
import sgo.entidad.Tanque;
import sgo.utilidades.Utilidades;
public class CargaTanqueMapper implements RowMapper<CargaTanque>{
  public CargaTanque mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    CargaTanque eCargaTanque = null;
    Tanque eTanque = null;
    Estacion eEstacion = null;
    try {
      eCargaTanque = new CargaTanque();
      eCargaTanque.setId(rs.getInt("id_ctanque"));
      eCargaTanque.setIdTanque(rs.getInt("id_tanque"));      
      eCargaTanque.setIdEstacion(rs.getInt("id_estacion"));
      eCargaTanque.setIdDiaOperativo(rs.getInt("id_doperativo"));      
      eCargaTanque.setFechaHoraInicial(rs.getTimestamp("fecha_hora_inicio"));
      eCargaTanque.setFechaHoraFinal(rs.getTimestamp("fecha_hora_fin"));
      eCargaTanque.setAlturaInicial(rs.getInt("altura_inicial_producto"));
      eCargaTanque.setAlturaFinal(rs.getInt("altura_final_producto"));
      eCargaTanque.setTemperaturaInicialCentro(rs.getFloat("temperatura_inicial_centro"));
      eCargaTanque.setTemperaturaFinalCentro(rs.getFloat("temperatura_final_centro"));
      eCargaTanque.setTemperaturaIniciaProbeta(rs.getFloat("temperatura_inicial_probeta"));
      eCargaTanque.setTemperaturaFinalProbeta(rs.getFloat("temperatura_final_probeta"));
      eCargaTanque.setApiObservadoInicial(rs.getFloat("api_observado_inicial"));
      eCargaTanque.setApiObservadoFinal(rs.getFloat("api_observado_final"));
      eCargaTanque.setFactorCorreccionInicial(rs.getFloat("factor_correccion_inicial"));
      eCargaTanque.setFactorCorreccionFinal(rs.getFloat("factor_correccion_final"));
      eCargaTanque.setVolumenObservadoInicial(rs.getFloat("volumen_observado_inicial"));
      eCargaTanque.setVolumenObservadoFinal(rs.getFloat("volumen_observado_final"));
      eCargaTanque.setVolumenCorregidoInicial(rs.getFloat("volumen_corregido_inicial"));
      eCargaTanque.setVolumenCorregidoFinal(rs.getFloat("volumen_corregido_final"));
      //Parametros de auditoria
      eCargaTanque.setCreadoPor(rs.getInt("creado_por"));
      eCargaTanque.setCreadoEl(rs.getLong("creado_el"));
      eCargaTanque.setActualizadoPor(rs.getInt("actualizado_por"));
      eCargaTanque.setActualizadoEl(rs.getLong("actualizado_el"));
      eCargaTanque.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eCargaTanque.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
      eCargaTanque.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
      eCargaTanque.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
      //
      eEstacion = new Estacion();
      eEstacion.setId(eCargaTanque.getIdEstacion());
      eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
      eCargaTanque.setEstacion(eEstacion);
      
      eTanque=  new Tanque();
      eTanque.setId(eCargaTanque.getIdTanque());
      eTanque.setDescripcion(Utilidades.cleanXSS(rs.getString("nombre_tanque")));
//      eTanque.setTipo(rs.getInt("tipo_tanque"));
      
      eCargaTanque.setTanque(eTanque);
      
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eCargaTanque;
  }
}