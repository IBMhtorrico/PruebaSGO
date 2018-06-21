package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DescargaCisterna;
import sgo.utilidades.Utilidades;
public class DescargaCisternaMapper implements RowMapper<DescargaCisterna>{
  public DescargaCisterna mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    DescargaCisterna eDescargaCisterna = null;
    try {
      eDescargaCisterna = new DescargaCisterna();
      eDescargaCisterna.setId(rs.getInt("id_dcisterna"));
      eDescargaCisterna.setIdCargaTanque(rs.getInt("id_ctanque"));
      eDescargaCisterna.setIdTransporte(rs.getInt("id_transporte"));
      eDescargaCisterna.setFechaArribo(rs.getDate("fecha_arribo"));
      eDescargaCisterna.setFechaFiscalizacion(rs.getDate("fecha_fiscalizacion"));
      eDescargaCisterna.setMetodoDescarga(rs.getInt("metodo_descarga"));
      eDescargaCisterna.setNumeroComprobante(Utilidades.cleanXSS(rs.getString("numero_comprobante")));
      eDescargaCisterna.setLecturaInicial(rs.getFloat("lectura_inicial"));
      eDescargaCisterna.setLecturaFinal(rs.getFloat("lectura_final"));
      eDescargaCisterna.setPesajeInicial(rs.getFloat("pesaje_inicial"));
      eDescargaCisterna.setPesajeFinal(rs.getFloat("pesaje_final"));
      eDescargaCisterna.setFactorConversion(rs.getFloat("factor_conversion"));
      eDescargaCisterna.setPesoNeto(rs.getFloat("peso_neto"));
      eDescargaCisterna.setVolumenTotalDescargadoObservado(rs.getFloat("volumen_total_descargado_observado"));
      eDescargaCisterna.setVolumenTotalDescargadoCorregido(rs.getFloat("volumen_total_descargado_corregido"));
      eDescargaCisterna.setVariacionVolumen(rs.getFloat("variacion_volumen"));
      eDescargaCisterna.setMermaPorcentaje(rs.getFloat("merma_porcentaje"));
      eDescargaCisterna.setMermaPermisible(rs.getFloat("merma_permisible"));
      eDescargaCisterna.setNombreEstacion(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
      eDescargaCisterna.setNombreOperacion(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
      eDescargaCisterna.setDescripcionTanque(Utilidades.cleanXSS(rs.getString("descripcion_tanque")));
      eDescargaCisterna.setVolumenExcedenteCorregido(rs.getFloat("excedente_temperatura_base"));
      eDescargaCisterna.setVolumenExcedenteObservado(rs.getFloat("excedente_temperatura_observada"));
      //Parametros de auditoria
      eDescargaCisterna.setCreadoPor(rs.getInt("creado_por"));
      eDescargaCisterna.setCreadoEl(rs.getLong("creado_el"));
      eDescargaCisterna.setActualizadoPor(rs.getInt("actualizado_por"));
      eDescargaCisterna.setActualizadoEl(rs.getLong("actualizado_el"));
      eDescargaCisterna.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eDescargaCisterna.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
      eDescargaCisterna.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
      eDescargaCisterna.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
      
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDescargaCisterna;
  }
}