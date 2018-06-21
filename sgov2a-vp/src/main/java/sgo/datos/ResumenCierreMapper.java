package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.ResumenCierre;
import sgo.utilidades.Utilidades;

public class ResumenCierreMapper implements RowMapper<ResumenCierre>{
 public ResumenCierre mapRow(ResultSet rs, int arg1) throws SQLException 
 {
  ResumenCierre eResumenCierre = null;
   try {
    eResumenCierre = new ResumenCierre();    
    eResumenCierre.setId(Utilidades.cleanXSS(rs.getString("id")));
    eResumenCierre.setIdDiaOperativo(rs.getInt("dia_operativo_descarga"));
    eResumenCierre.setIdTracto(rs.getInt("id_tracto"));
    eResumenCierre.setIdCisterna(rs.getInt("id_cisterna"));
    eResumenCierre.setIdTransportista(rs.getInt("id_transportista"));
    eResumenCierre.setIdTransporte(rs.getInt("id_transporte"));    
    eResumenCierre.setIdEstacion(rs.getInt("id_estacion"));
    eResumenCierre.setMetodoDescarga(rs.getInt("metodo_descarga"));
    eResumenCierre.setEntradaTotal(rs.getInt("entrada_total"));    
    eResumenCierre.setSalidaTotal(rs.getFloat("salida_total"));
    eResumenCierre.setVariacion(rs.getFloat("variacion"));
    eResumenCierre.setResultado(rs.getFloat("resultado"));
    eResumenCierre.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_tracto")));
    eResumenCierre.setPlacaCisterna(Utilidades.cleanXSS(rs.getString("placa_cisterna")));
    eResumenCierre.setRazonSocialTransportista(Utilidades.cleanXSS(rs.getString("razon_social_transportista")));    
    eResumenCierre.setNombreEstacion(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
    
    eResumenCierre.setFechaOperativa(rs.getDate("fecha_operativa"));
    eResumenCierre.setEstadoDescarga(rs.getInt("estado_descarga"));  
    eResumenCierre.setVolumenObservadoDespachado(rs.getFloat("volumen_observado_despachado"));
    eResumenCierre.setVolumenCorregidoDespachado(rs.getFloat("volumen_corregido_despachado"));
    eResumenCierre.setVolumenObservadoRecibido(rs.getFloat("volumen_observado_recibido"));
    eResumenCierre.setVolumenCorregidoRecibido(rs.getFloat("volumen_corregido_recibido"));
    eResumenCierre.setLimitePermisible(rs.getFloat("limite_permisible"));
    eResumenCierre.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
    eResumenCierre.setIdOperacion(rs.getInt("id_operacion"));
   } catch(Exception ex){
     ex.printStackTrace();
   }
   return eResumenCierre;
 }
}