package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Operacion;
import sgo.entidad.Transportista;
import sgo.entidad.TransportistaOperacion;
import sgo.utilidades.Utilidades;
public class TransportistaOperacionMapper implements RowMapper<TransportistaOperacion>{
	public TransportistaOperacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		TransportistaOperacion eTransportistaOperacion = null;
		Transportista eTransportista = null;
		Operacion eOperacion = null;

		try {
			eTransportistaOperacion = new TransportistaOperacion();
			eTransportistaOperacion.setId(rs.getInt("id_toperacion"));
			eTransportistaOperacion.setIdTransportista(rs.getInt("id_transportista"));
			eTransportistaOperacion.setIdOperacion(rs.getInt("id_operacion"));
			
			eTransportista = new Transportista();
			eTransportista.setId(rs.getInt("id_transportista"));
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			eTransportista.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			eTransportista.setRuc(Utilidades.cleanXSS(rs.getString("ruc")));
			eTransportista.setEstado(rs.getInt("estado"));
			
			eOperacion = new Operacion();
			eOperacion.setId(rs.getInt("id_operacion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eOperacion.setIdCliente(rs.getInt("id_cliente"));
			eOperacion.setEta(rs.getInt("eta_origen"));
			
			eTransportistaOperacion.seteTransportista(eTransportista);
			eTransportistaOperacion.seteOperacion(eOperacion);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eTransportistaOperacion;
	}
}