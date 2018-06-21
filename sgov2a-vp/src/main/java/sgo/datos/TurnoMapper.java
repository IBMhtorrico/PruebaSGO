package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Operario;
import sgo.entidad.Turno;
import sgo.utilidades.Utilidades;
public class TurnoMapper implements RowMapper<Turno>{
  public Turno mapRow(ResultSet rs, int arg1) throws SQLException 
  {
	  Turno eTurno = null;
	  Estacion eEstacion=null;
	  Jornada eJornada=null;
	  Operario eResponsable=null;
	  Operario eAyudante=null;

    try {
      eTurno = new Turno();
      eTurno.setId(rs.getInt("id_turno"));
      eTurno.setFechaHoraApertura(rs.getTimestamp("fecha_hora_apertura"));
      eEstacion=new Estacion();
      eEstacion.setId(rs.getInt("id_estacion"));
      eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("estacion")));
      eEstacion.setIdOperacion(rs.getInt("id_operacion"));
      eEstacion.setCantidadTurnos(rs.getInt("cantidad_turnos"));
      eJornada=new Jornada();
      eTurno.setIdJornada(rs.getInt("id_jornada"));
      eJornada.setId(rs.getInt("id_jornada"));
      eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));
      eJornada.setEstacion(eEstacion);
      eTurno.setJornada(eJornada);
      eResponsable=new Operario();
      eResponsable.setNombreOperario(Utilidades.cleanXSS(rs.getString("nombre_operario")));
      eResponsable.setApellidoPaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_paterno_operario")));
      eResponsable.setApellidoMaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_materno_operario")));
      eTurno.setResponsable(eResponsable);
      eAyudante=new Operario();
      eAyudante.setNombreOperario(Utilidades.cleanXSS(rs.getString("ayudante")));
      eAyudante.setApellidoPaternoOperario(Utilidades.cleanXSS(rs.getString("ayudante_paterno")));
      eAyudante.setApellidoMaternoOperario(Utilidades.cleanXSS(rs.getString("ayudante_materno")));
      eTurno.setAyudante(eAyudante);
      
      eTurno.setEstado(rs.getInt("estado"));
      eTurno.setComentario(Utilidades.cleanXSS(rs.getString("comentario")));
      eTurno.setFechaHoraCierre(rs.getTimestamp("fecha_hora_cierre"));
      eTurno.setObservacion(Utilidades.cleanXSS(rs.getString("observacion")));
      
    //Parametros de auditoria
      eTurno.setCreadoPor(rs.getInt("creado_por"));
      eTurno.setCreadoEl(rs.getLong("creado_el"));
      eTurno.setActualizadoPor(rs.getInt("actualizado_por"));
      eTurno.setActualizadoEl(rs.getLong("actualizado_el"));
      eTurno.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eTurno.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
      eTurno.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
      eTurno.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
      

    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eTurno;
  }
}