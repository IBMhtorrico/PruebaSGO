package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.GecAprobacion;
import sgo.entidad.Usuario;
import sgo.utilidades.Utilidades;

public class GecAprobacionMapper implements RowMapper<GecAprobacion>{
  public GecAprobacion mapRow(ResultSet rs, int arg1) throws SQLException 
  {
	  GecAprobacion eGecAprobacion = null;
	  Usuario eRegistrador = null;
	  Usuario eEmisor = null;
	  Usuario eAprobador = null;
    try {
      eGecAprobacion = new GecAprobacion();
      eGecAprobacion.setId(rs.getInt("id_aprobacion_gec"));
      eGecAprobacion.setIdGcombustible(rs.getInt("id_gcombustible"));
      eGecAprobacion.setObservacionCliente(Utilidades.cleanXSS(rs.getString("observacion_cliente")));
      eGecAprobacion.setEstado(rs.getInt("estado_usuario_aprobador"));
      
      //Usuario Registrador
      eGecAprobacion.setIdUsuarioRegistrado(rs.getInt("id_usuario_registrado"));
      eGecAprobacion.setFechaHoraRegistrado(rs.getTimestamp("fecha_hora_registrado"));
      
      eRegistrador = new Usuario();
      eRegistrador.setId(rs.getInt("id_usuario_registrado"));
      eRegistrador.setIdentidad(Utilidades.cleanXSS(rs.getString("usuario_registrador")));
      eRegistrador.setEmail(Utilidades.cleanXSS(rs.getString("correo_registrador")));
      
      //Usuario Emisor
      eGecAprobacion.setIdUsuarioEmitido(rs.getInt("id_usuario_emitido"));
      eGecAprobacion.setFechaHoraEmitido(rs.getTimestamp("fecha_hora_emitido"));
      
      eEmisor = new Usuario();
      eEmisor.setId(rs.getInt("id_usuario_emitido"));
      eEmisor.setIdentidad(Utilidades.cleanXSS(rs.getString("usuario_emisor")));
      eEmisor.setEmail(Utilidades.cleanXSS(rs.getString("correo_emisor")));
      
      //Usuario Aprobador
      eGecAprobacion.setIdUsuarioAprobado(rs.getInt("id_usuario_aprobado"));
      eGecAprobacion.setFechaHoraAprobado(rs.getTimestamp("fecha_hora_aprobado"));
      
      eAprobador = new Usuario();
      eAprobador.setId(rs.getInt("id_usuario_aprobado"));
      eAprobador.setIdentidad(Utilidades.cleanXSS(rs.getString("usuario_aprobador")));
      eAprobador.setEmail(Utilidades.cleanXSS(rs.getString("correo_aprobador")));
      
      eGecAprobacion.setRegistrador(eRegistrador);
      eGecAprobacion.setEmisor(eEmisor);
      eGecAprobacion.setAprobador(eAprobador);
      
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eGecAprobacion;
  }
}