package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DetalleProgramacionCorreos;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Operacion;
import sgo.entidad.Planta;
import sgo.entidad.Producto;
import sgo.entidad.Programacion;
import sgo.entidad.Tracto;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class DetalleProgramacionCorreoMapper implements RowMapper<DetalleProgramacionCorreos>{
	public DetalleProgramacionCorreos mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Planta ePlanta = null;		
		DetalleProgramacionCorreos eDetalleProgramacionCorreos = null;
				
		try {
			eDetalleProgramacionCorreos = new DetalleProgramacionCorreos();
			
		
			eDetalleProgramacionCorreos.setIdDoperativo(rs.getInt("id_doperativo"));
			eDetalleProgramacionCorreos.setIdProgramacion(rs.getInt("id_programacion"));
			eDetalleProgramacionCorreos.setIdDprogramacion(rs.getInt("id_dprogramacion"));
			eDetalleProgramacionCorreos.setIdPlanta(rs.getInt("id_planta"));
			eDetalleProgramacionCorreos.setComentario(Utilidades.cleanXSS(rs.getString("comentario")));
		      
			ePlanta = new Planta();
			ePlanta.setId(rs.getInt("id_planta"));
			ePlanta.setDescripcion(rs.getString("descripcion_planta")); 
			ePlanta.setCorreoPara(rs.getString("correopara")); 
			ePlanta.setCorreoCC(rs.getString("correocc")); 
			
			eDetalleProgramacionCorreos.setPlanta(ePlanta);

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDetalleProgramacionCorreos;
	}
}