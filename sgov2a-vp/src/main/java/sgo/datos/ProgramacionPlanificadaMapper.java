package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.ProgramacionPlanificada;

public class ProgramacionPlanificadaMapper implements RowMapper<ProgramacionPlanificada> {

	@Override
	public ProgramacionPlanificada mapRow(ResultSet rs, int arg1)
			throws SQLException {
		ProgramacionPlanificada eProgramacionPlanificada = new ProgramacionPlanificada();
		try {
			eProgramacionPlanificada.setId_doperativo(rs.getInt("id_doperativo"));
			eProgramacionPlanificada.setVolumen_solicitado(rs.getFloat("volumen_solicitado"));
			eProgramacionPlanificada.setCantidad_cisternas(rs.getInt("cantidad_cisternas"));
			eProgramacionPlanificada.setVolumen_asignado(rs.getFloat("volumen_asignado"));
			eProgramacionPlanificada.setCisternas_asignadas(rs.getInt("cisterna_asignada"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eProgramacionPlanificada;
	}

}
