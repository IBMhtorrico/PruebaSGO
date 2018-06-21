package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.DespachoCarga;
import sgo.entidad.Estacion;
import sgo.entidad.Operario;
import sgo.utilidades.Utilidades;

public class DespachoCargaMapper implements RowMapper<DespachoCarga>{
	public DespachoCarga mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		DespachoCarga eDespachoCarga = null;
		Operario eOperario = null;
		Estacion eEstacion = null;
		
		try {
			eDespachoCarga = new DespachoCarga();
			eDespachoCarga.setId(rs.getInt("id_dcarga"));
			eDespachoCarga.setNombreArchivo(Utilidades.cleanXSS(rs.getString("nombre_archivo")));
			eDespachoCarga.setFechaCarga(rs.getDate("fecha_carga"));
			eDespachoCarga.setComentario(Utilidades.cleanXSS(rs.getString("comentario")));
			eDespachoCarga.setIdOperario(rs.getInt("id_operario"));
			eDespachoCarga.setIdEstacion(rs.getInt("id_estacion"));
			eDespachoCarga.setIdJornada(rs.getInt("id_jornada"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eEstacion.setTipo(rs.getInt("tipo"));
			eEstacion.setIdOperacion(rs.getInt("id_operacion"));
			
			eOperario = new Operario();
			eOperario.setId(rs.getInt("id_operario"));
			eOperario.setNombreOperario(Utilidades.cleanXSS(rs.getString("nombre_operario")));
			eOperario.setApellidoPaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_paterno_operario")));
			eOperario.setApellidoMaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_materno_operario")));
			eOperario.setDniOperario(Utilidades.cleanXSS(rs.getString("dni_operario")));
			eOperario.setIdCliente(rs.getInt("id_cliente"));

			eDespachoCarga.setEstacion(eEstacion);
			eDespachoCarga.setOperario(eOperario);

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDespachoCarga;
	}
}