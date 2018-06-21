package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.VistaDesconche;
import sgo.utilidades.Utilidades;

public class VistaDesconcheMapper implements RowMapper<VistaDesconche>{
 public VistaDesconche mapRow(ResultSet rs, int arg1) throws SQLException 
 {
  VistaDesconche vistaDesconche = null;
   try {
    vistaDesconche = new VistaDesconche();
    vistaDesconche.setId(rs.getInt("id_desconche"));
    vistaDesconche.setFechaPlanificada(rs.getDate("fecha_operativa"));
    vistaDesconche.setPlacaCisterna(Utilidades.cleanXSS(rs.getString("placa")));
    vistaDesconche.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_tracto")));
    vistaDesconche.setNumeroDesconche(rs.getInt("numero_desconche"));
    vistaDesconche.setNumeroCompartimento(rs.getInt("numero_compartimento"));
    vistaDesconche.setVolumenDesconche(rs.getFloat("volumen"));
    vistaDesconche.setEstacion(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
    vistaDesconche.setTanque(Utilidades.cleanXSS(rs.getString("descripcion_tanque")));
    vistaDesconche.setIdOperacion(rs.getInt("id_operacion"));
    //
    vistaDesconche.setNumeroCompartimentos(rs.getInt("numero_compartimentos"));
    vistaDesconche.setNumeroMaximoDesconches(rs.getInt("numero_maximo_desconche"));
    vistaDesconche.setIdDescargaCisterna(rs.getInt("id_dcisterna"));
     //Parametros de auditoria
    vistaDesconche.setCreadoPor(rs.getInt("desconche_creado_por"));
    vistaDesconche.setCreadoEl(rs.getLong("desconche_creado_el"));
    vistaDesconche.setActualizadoPor(rs.getInt("desconche_actualizado_por"));
    vistaDesconche.setActualizadoEl(rs.getLong("desconche_actualizado_el"));
    vistaDesconche.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("desconche_usuario_actualizacion")));
    vistaDesconche.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("desconche_usuario_creacion")));
    vistaDesconche.setIpCreacion(Utilidades.cleanXSS(rs.getString("desconche_ip_creacion")));
    vistaDesconche.setIpActualizacion(Utilidades.cleanXSS(rs.getString("desconche_ip_actualizacion")));
     
   } catch(Exception ex){
     ex.printStackTrace();
   }
   return vistaDesconche;
 }
}