package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.DiaOperativo;
import sgo.entidad.Programacion;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class ProgramacionMapper implements RowMapper<Programacion>{
	public Programacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Programacion eProgramacion = null;
		Transportista eTransportista = null;
		DiaOperativo diaOperativo = null;
		try {
			eProgramacion = new Programacion();	
			
			eProgramacion.setTransportista(eTransportista);				
			eProgramacion.setId(rs.getInt("id_programacion"));	
///			eProgramacion.setIdTransportista(rs.getInt("id_transportista"));
			eProgramacion.setIdDiaOperativo(rs.getInt("id_doperativo"));
			eProgramacion.setEstado(rs.getInt("estado"));
			eProgramacion.setComentario(Utilidades.cleanXSS(rs.getString("comentario")));
					
			diaOperativo=new DiaOperativo();
			diaOperativo.setId(rs.getInt("id_doperativo"));
			diaOperativo.setTotalCisternasProgramados(rs.getInt("candidadcisternas"));
			eProgramacion.setDiaOperativo(diaOperativo);	
			
			eTransportista=new Transportista();
			eTransportista.setId(rs.getInt("id_transportista"));
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			eProgramacion.setTransportista(eTransportista);
			
			//Parametros de auditoria
			eProgramacion.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eProgramacion.setActualizadoEl(rs.getLong("actualizado_el"));
						
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eProgramacion;
	}
}