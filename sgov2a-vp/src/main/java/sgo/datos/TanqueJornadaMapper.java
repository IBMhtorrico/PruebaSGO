package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Producto;
import sgo.entidad.Tanque;
import sgo.entidad.TanqueJornada;
import sgo.utilidades.Utilidades;
public class TanqueJornadaMapper implements RowMapper<TanqueJornada>{
	public TanqueJornada mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		TanqueJornada eTanqueJornada = null;
		Producto eProducto = null;
		Tanque eTanque = null;
		Jornada eJornada = null;
		Estacion eEstacion = null;
		
		try {
			eTanqueJornada = new TanqueJornada();
			eTanqueJornada.setIdTjornada(rs.getInt("id_tjornada"));
			eTanqueJornada.setIdTanque(rs.getInt("id_tanque"));
			eTanqueJornada.setIdProducto(rs.getInt("id_producto"));
			eTanqueJornada.setMedidaInicial(rs.getInt("medida_inicial"));
			eTanqueJornada.setMedidaFinal(rs.getInt("medida_final"));
			eTanqueJornada.setVolumenObservadoInicial(rs.getFloat("volumen_observado_inicial"));
			eTanqueJornada.setVolumenObservadoFinal(rs.getFloat("volumen_observado_final"));
			eTanqueJornada.setApiCorregidoInicial(rs.getFloat("api_corregido_inicial"));
			eTanqueJornada.setApiCorregidoFinal(rs.getFloat("api_corregido_final"));
			eTanqueJornada.setTemperaturaInicial(rs.getFloat("temperatura_inicial"));
			eTanqueJornada.setTemperaturaFinal(rs.getFloat("temperatura_final"));
			eTanqueJornada.setFactorCorreccionInicial(rs.getFloat("factor_correccion_inicial"));
			eTanqueJornada.setFactorCorreccionFinal(rs.getFloat("factor_correccion_final"));
			eTanqueJornada.setVolumenCorregidoInicial(rs.getFloat("volumen_corregido_inicial"));
			eTanqueJornada.setVolumenCorregidoFinal(rs.getFloat("volumen_corregido_final"));
			eTanqueJornada.setEstadoServicio(rs.getInt("estado_servicio"));
			eTanqueJornada.setEnLinea(rs.getInt("en_linea"));
			eTanqueJornada.setVolumenAguaFinal(rs.getFloat("volumen_agua_final"));
			eTanqueJornada.setIdJornada(rs.getInt("id_jornada"));
			eTanqueJornada.setDescripcionTanque(Utilidades.cleanXSS(rs.getString("nombre_tanque")));
			eTanqueJornada.setHoraInicial(rs.getTimestamp("hora_inicial"));
			eTanqueJornada.setHoraFinal(rs.getTimestamp("hora_final"));
			eTanqueJornada.setApertura(rs.getInt("apertura"));
			eTanqueJornada.setCierre(rs.getInt("cierre"));
			
			eProducto = new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
			eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura")));
			eProducto.setIndicadorProducto(rs.getInt("indicador_producto"));
			eTanqueJornada.setProducto(eProducto);
			
			eTanque = new Tanque();
			eTanque.setId(rs.getInt("id_tanque"));
			eTanque.setDescripcion(Utilidades.cleanXSS(rs.getString("nombre_tanque")));
			eTanque.setVolumenTotal(rs.getInt("volumen_total"));
			eTanque.setVolumenTrabajo(rs.getInt("volumen_trabajo"));
			eTanqueJornada.setTanque(eTanque);
			
			eJornada = new Jornada();
			eJornada.setId(rs.getInt("id_jornada"));
			eJornada.setIdEstacion(rs.getInt("id_estacion"));
			eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));
			eJornada.setEstado(rs.getInt("estado_jornada"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eEstacion.setIdOperacion(rs.getInt("id_operacion"));
			eJornada.setEstacion(eEstacion);
			
			eTanqueJornada.setJornada(eJornada);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eTanqueJornada;
	}
}