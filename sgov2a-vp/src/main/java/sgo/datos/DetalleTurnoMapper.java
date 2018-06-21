package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Contometro;
import sgo.entidad.DetalleTurno;
import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Producto;
import sgo.entidad.Turno;
import sgo.utilidades.Utilidades;
public class DetalleTurnoMapper implements RowMapper<DetalleTurno>{
  public DetalleTurno mapRow(ResultSet rs, int arg1) throws SQLException 
  {
	  DetalleTurno eDetalleTurno = null;
	  Turno eTurno=null;
	  Producto eProducto=null;
	  Contometro eContometro=null;
	  Jornada eJornada=null;
	  Estacion eEstacion=null;
      try {
    	eDetalleTurno = new DetalleTurno();
    	eDetalleTurno.setId(rs.getInt("id_dturno"));
    	eDetalleTurno.setIdTurno(rs.getInt("id_turno"));
    	eTurno=new Turno();
    	eTurno.setId(rs.getInt("id_turno"));
    	eTurno.setEstado(rs.getInt("estado"));
    	eTurno.setIdJornada(rs.getInt("id_jornada"));
    	eTurno.setObservacion(Utilidades.cleanXSS(rs.getString("observacion")));
 
    	eJornada=new Jornada();
    	eJornada.setId(rs.getInt("id_jornada"));
    	eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));
    	eEstacion=new Estacion();
    	eEstacion.setId(rs.getInt("id_estacion"));
    	eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("estacion")));
    	eJornada.setEstacion(eEstacion);
    	eTurno.setJornada(eJornada);

    	eTurno.setCreadoEl(rs.getLong("creado_el"));
    	eTurno.setCreadoPor(rs.getInt("creado_por"));
    	eTurno.setActualizadoPor(rs.getInt("actualizado_por"));
    	eTurno.setActualizadoEl(rs.getLong("actualizado_el"));
    	eTurno.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
    	eTurno.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
    	eTurno.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
    	eTurno.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
    	eTurno.setFechaHoraApertura(rs.getTimestamp("fecha_hora_apertura"));
    	eTurno.setFechaHoraCierre(rs.getTimestamp("fecha_hora_cierre"));
    	
    	
    	eDetalleTurno.setTurno(eTurno);
    	eDetalleTurno.setLecturaInicial(rs.getFloat("lectura_inicial"));
    	eDetalleTurno.setLecturaFinal(rs.getFloat("lectura_final"));
    	eProducto=new Producto();
    	eProducto.setId(rs.getInt("id_producto"));
    	eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
    	eDetalleTurno.setProducto(eProducto);
    	eContometro=new Contometro();
    	eContometro.setId(rs.getInt("id_contometro"));
    	eContometro.setAlias(Utilidades.cleanXSS(rs.getString("alias_contometro")));
    	eDetalleTurno.setContometro(eContometro);
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDetalleTurno;
  }
}