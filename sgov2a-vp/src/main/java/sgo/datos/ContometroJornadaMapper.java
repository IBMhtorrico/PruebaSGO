package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Contometro;
import sgo.entidad.ContometroJornada;
import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class ContometroJornadaMapper implements RowMapper<ContometroJornada>{
	public ContometroJornada mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		ContometroJornada eContometroJornada = null;
		Estacion eEstacion = null;
		Jornada eJornada=null;
		Contometro eContometro=null;
		Producto eProducto=null;
		try {
			eContometroJornada = new ContometroJornada();
			eContometroJornada.setId(rs.getInt("id_cjornada"));
			eContometroJornada.setIdJornada(rs.getInt("id_jornada"));	
			eContometroJornada.setDescripcionContometro(Utilidades.cleanXSS(rs.getString("alias_contometro")));
			
			eJornada=new Jornada();
			eJornada.setId(rs.getInt("id_jornada"));
			eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));		
			eJornada.setIdOperario1(rs.getInt("operario1"));
			eJornada.setIdOperario2(rs.getInt("operario2"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eEstacion.setIdOperacion(rs.getInt("id_operacion"));
			eJornada.setEstacion(eEstacion);
			
			eContometroJornada.setJornada(eJornada);
			eContometroJornada.setLecturaInicial(rs.getFloat("lectura_inicial"));
			eContometroJornada.setLecturaFinal(rs.getFloat("lectura_final"));
			eContometroJornada.setEstadoServicio(rs.getInt("estado_servicio"));
			eContometroJornada.setIdContometro(rs.getInt("id_contometro"));
			
			eContometro=new Contometro();
			eContometro.setId(rs.getInt("id_contometro"));
			eContometro.setAlias(Utilidades.cleanXSS(rs.getString("alias_contometro")));
			eContometro.setIdEstacion(rs.getInt("id_estacion"));
			eContometroJornada.setContometro(eContometro);
			eContometroJornada.setIdContometro(rs.getInt("id_contometro"));
			
			eProducto=new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
			eContometroJornada.setProducto(eProducto);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eContometroJornada;
	}
}