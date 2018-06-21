package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Propietario;
import sgo.entidad.Vehiculo;
import sgo.utilidades.Utilidades;
public class VehiculoMapper implements RowMapper<Vehiculo>{
	public Vehiculo mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Vehiculo eVehiculo = null;
		Propietario ePropietario =null;
		try {
			eVehiculo = new Vehiculo();
			eVehiculo.setId(rs.getInt("id_vehiculo"));
			eVehiculo.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			eVehiculo.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
			eVehiculo.setIdPropietario(rs.getInt("id_propietario"));
			eVehiculo.setEstado(rs.getInt("estado"));
			eVehiculo.setRazonSocialPropietario(Utilidades.cleanXSS(rs.getString("razon_social_propietario")));
			ePropietario = new Propietario();
			ePropietario.setId(rs.getInt("id_propietario"));
			ePropietario.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social_propietario")));
			eVehiculo.setPropietario(ePropietario);
			//Parametros de auditoria
			eVehiculo.setCreadoPor(rs.getInt("creado_por"));
			eVehiculo.setCreadoEl(rs.getLong("creado_el"));
			eVehiculo.setActualizadoPor(rs.getInt("actualizado_por"));
			eVehiculo.setActualizadoEl(rs.getLong("actualizado_el"));
			eVehiculo.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eVehiculo.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eVehiculo.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eVehiculo.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eVehiculo;
	}
}