package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Planta;
import sgo.entidad.Recorrido;
import sgo.utilidades.Utilidades;

public class RecorridoMapper implements RowMapper<Recorrido>{
	public Recorrido mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Recorrido eRecorrido = null;
		Planta ePlantaOrigen = null;
		Planta ePlantaDestino = null;
		
		try {
			eRecorrido = new Recorrido();
			eRecorrido.setId(rs.getInt("id_recorrido"));
			eRecorrido.setIdPlantaOrigen(rs.getInt("planta_origen"));
			eRecorrido.setIdPlantaDestino(rs.getInt("planta_destino"));
			eRecorrido.setNumeroDias(rs.getInt("numero_dias"));
			eRecorrido.setEstado(rs.getInt("estado"));
						
			ePlantaOrigen = new Planta();
			ePlantaOrigen.setId(rs.getInt("planta_origen"));
			ePlantaOrigen.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_planta_origen")));
			eRecorrido.setPlantaOrigen(ePlantaOrigen);
			
			ePlantaDestino = new Planta();
			ePlantaDestino.setId(rs.getInt("planta_destino"));
			ePlantaDestino.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_planta_destino")));
			eRecorrido.setPlantaDestino(ePlantaDestino);
			
			//Parametros de auditoria
			eRecorrido.setCreadoPor(rs.getInt("creado_por"));
			eRecorrido.setCreadoEl(rs.getLong("creado_el"));
			eRecorrido.setActualizadoPor(rs.getInt("actualizado_por"));
			eRecorrido.setActualizadoEl(rs.getLong("actualizado_el"));
			eRecorrido.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eRecorrido.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eRecorrido;
	}
}