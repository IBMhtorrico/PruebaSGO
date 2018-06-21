package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.GuiaCombustible;
import sgo.utilidades.Utilidades;
public class GuiaCombustibleMapper implements RowMapper<GuiaCombustible>{
  public GuiaCombustible mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    GuiaCombustible eGuiaCombustible = null;
    try {
      eGuiaCombustible = new GuiaCombustible();
      eGuiaCombustible.setId(rs.getInt("id_gcombustible"));
      eGuiaCombustible.setOrdenCompra(Utilidades.cleanXSS(rs.getString("orden_compra")));
      eGuiaCombustible.setReferencia_planta_recepcion(Utilidades.cleanXSS(rs.getString("referencia_planta_recepcion")));
      eGuiaCombustible.setFechaGuiaCombustible(rs.getDate("fecha_guia_combustible"));
      eGuiaCombustible.setIdTransportista(rs.getInt("id_transportista"));
      eGuiaCombustible.setNumeroGEC(Utilidades.cleanXSS(rs.getString("numero_gec")));
      eGuiaCombustible.setNumeroContrato(Utilidades.cleanXSS(rs.getString("numero_contrato")));
      eGuiaCombustible.setDescripcionContrato(Utilidades.cleanXSS(rs.getString("descripcion_contrato")));
      eGuiaCombustible.setEstado(rs.getInt("estado"));
      eGuiaCombustible.setComentarios(Utilidades.cleanXSS(rs.getString("comentarios")));
      eGuiaCombustible.setIdProducto(rs.getInt("id_producto"));
      eGuiaCombustible.setIdCliente(rs.getInt("cliente"));
      eGuiaCombustible.setIdOperacion(rs.getInt("operacion"));
      eGuiaCombustible.setNumeroSerie(Utilidades.cleanXSS(rs.getString("serie_gec")));
      eGuiaCombustible.setNombreOperacion(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
      eGuiaCombustible.setNombreCliente(Utilidades.cleanXSS(rs.getString("nombre_cliente")));
      eGuiaCombustible.setNombreProducto(Utilidades.cleanXSS(rs.getString("nombre_producto")));
      eGuiaCombustible.setNombreTransportista(Utilidades.cleanXSS(rs.getString("nombre_transportista")));
      eGuiaCombustible.setTotalVolumenDespachado(rs.getFloat("total_volumen_despachado"));
      eGuiaCombustible.setTotalVolumenRecibido(rs.getFloat("total_volumen_recibido"));
      //Parametros de auditoria
      eGuiaCombustible.setCreadoPor(rs.getInt("creado_por"));
      eGuiaCombustible.setCreadoEl(rs.getLong("creado_el"));
      eGuiaCombustible.setActualizadoPor(rs.getInt("actualizado_por"));
      eGuiaCombustible.setActualizadoEl(rs.getLong("actualizado_el"));
      eGuiaCombustible.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
      eGuiaCombustible.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
      eGuiaCombustible.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
      eGuiaCombustible.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eGuiaCombustible;
  }
}