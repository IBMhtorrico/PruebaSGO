package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DescargaCompartimento;
import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class DescargaCompartimentoMapper implements RowMapper<DescargaCompartimento>{
  public DescargaCompartimento mapRow(ResultSet rs, int arg1) throws SQLException 
  {
    DescargaCompartimento eDescargaCompartimento = null;
    try {
      eDescargaCompartimento = new DescargaCompartimento();
      eDescargaCompartimento.setId(rs.getInt("id_dcompartimento"));
      eDescargaCompartimento.setIdDescargaCisterna(rs.getInt("id_dcisterna"));
      eDescargaCompartimento.setIdProducto(rs.getInt("id_producto"));
      eDescargaCompartimento.setCapacidadVolumetricaCompartimento(rs.getFloat("capacidad_volumetrica_compartimento"));
      eDescargaCompartimento.setAlturaCompartimento(rs.getInt("altura_compartimento"));
      eDescargaCompartimento.setAlturaFlecha(rs.getInt("altura_flecha"));
      eDescargaCompartimento.setAlturaProducto(rs.getInt("altura_producto"));
      eDescargaCompartimento.setUnidadMedida(Utilidades.cleanXSS(rs.getString("unidad_medida_volumen")));
      eDescargaCompartimento.setNumeroCompartimento(rs.getInt("numero_compartimento"));
      eDescargaCompartimento.setIdCompartimento(rs.getInt("id_compartimento"));
      eDescargaCompartimento.setTemperaturaObservada(rs.getFloat("temperatura_centro_cisterna"));
      eDescargaCompartimento.setTemperaturaProbeta(rs.getFloat("temperatura_probeta"));
      eDescargaCompartimento.setApiTemperaturaObservada(rs.getFloat("api_temperatura_observada"));
      eDescargaCompartimento.setApiTemperaturaBase(rs.getFloat("api_temperatura_base"));
      eDescargaCompartimento.setFactorCorreccion(rs.getFloat("factor_correccion"));
      eDescargaCompartimento.setTipoVolumen(rs.getInt("tipo_volumen"));
      eDescargaCompartimento.setMermaPorcentaje(rs.getFloat("merma_porcentaje"));
      eDescargaCompartimento.setVolumenRecibidoObservado(rs.getFloat("volumen_recibido_observado"));
      eDescargaCompartimento.setVolumenRecibidoCorregido(rs.getFloat("volumen_recibido_corregido"));
      Producto eProducto = new Producto();
      eProducto.setId(rs.getInt("id_producto"));
      eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
      eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura_producto")));
      eDescargaCompartimento.setProducto(eProducto);
    } catch(Exception ex){
      ex.printStackTrace();
    }
    return eDescargaCompartimento;
  }
}