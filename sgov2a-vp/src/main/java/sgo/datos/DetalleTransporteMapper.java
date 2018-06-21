package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DetalleTransporte;
import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class DetalleTransporteMapper implements RowMapper<DetalleTransporte>{
	public DetalleTransporte mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		DetalleTransporte eDetalleTransporte = null;
		try {
			eDetalleTransporte = new DetalleTransporte();
			eDetalleTransporte.setId(Utilidades.cleanXSS(rs.getString("id_dtransporte")));
			eDetalleTransporte.setIdTransporte(rs.getInt("id_transporte"));
			eDetalleTransporte.setIdProducto(rs.getInt("id_producto"));
			eDetalleTransporte.setDescripcionProducto(Utilidades.cleanXSS(rs.getString("descripcion_producto")));
			eDetalleTransporte.setCapacidadVolumetricaCompartimento(rs.getFloat("capacidad_volumetrica_compartimento"));
			eDetalleTransporte.setUnidadMedida(Utilidades.cleanXSS(rs.getString("unidad_medida_volumen")));
			eDetalleTransporte.setNumeroCompartimento(rs.getInt("numero_compartimento"));
			eDetalleTransporte.setVolumenTemperaturaObservada(rs.getFloat("volumen_temperatura_observada"));
			eDetalleTransporte.setTemperaturaObservada(rs.getFloat("temperatura_observada"));
			eDetalleTransporte.setApiTemperaturaBase(rs.getFloat("api_temperatura_base"));
			eDetalleTransporte.setFactorCorrecion(rs.getFloat("factor_correcion"));
			eDetalleTransporte.setVolumenTemperaturaBase(rs.getFloat("volumen_temperatura_base"));

			//eDetalleTransporte.setAlturaCompartimento(rs.getInt("altura_compartimento"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDetalleTransporte;
	}
}