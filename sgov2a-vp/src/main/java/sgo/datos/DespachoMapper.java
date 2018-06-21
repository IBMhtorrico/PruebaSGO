package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Contometro;
import sgo.entidad.Despacho;
import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Operacion;
import sgo.entidad.Producto;
import sgo.entidad.Propietario;
import sgo.entidad.Tanque;
import sgo.entidad.Vehiculo;
import sgo.utilidades.Utilidades;
public class DespachoMapper implements RowMapper<Despacho>{
	public Despacho mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Despacho eDespacho = null;
		Jornada eJornada = null;
		Estacion eEstacion = null;
		Operacion eOperacion = null;
		Vehiculo eVehiculo = null;
		Propietario ePropietario = null;
		Tanque eTanque = null;
		Contometro eContometro = null;
		Producto eProducto = null;
				
		try {
			eDespacho = new Despacho();
			eDespacho.setId(rs.getInt("id_despacho"));
			eDespacho.setIdJornada(rs.getInt("id_jornada"));
			eDespacho.setIdVehiculo(rs.getInt("id_vehiculo"));
			eDespacho.setKilometroHorometro(rs.getInt("kilometro_horometro"));
			eDespacho.setNumeroVale(Utilidades.cleanXSS(rs.getString("numero_vale")));
			eDespacho.setTipoRegistro(rs.getInt("tipo_registro"));
			eDespacho.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio"));
			eDespacho.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin"));
			eDespacho.setClasificacion(Utilidades.cleanXSS(rs.getString("clasificacion")));
			eDespacho.setIdProducto(rs.getInt("id_producto"));
			eDespacho.setLecturaInicial(rs.getLong("lectura_inicial"));
			eDespacho.setLecturaFinal(rs.getLong("lectura_final"));
			eDespacho.setFactorCorreccion(rs.getFloat("factor_correccion"));
			eDespacho.setApiCorregido(rs.getFloat("api_corregido"));
			eDespacho.setTemperatura(rs.getFloat("temperatura"));
			eDespacho.setVolumenCorregido(rs.getFloat("volumen_corregido"));
			eDespacho.setVolumenObservado(rs.getFloat("volumen_observado"));
			eDespacho.setIdTanque(rs.getInt("id_tanque"));
			eDespacho.setIdContometro(rs.getInt("id_contometro"));
			eDespacho.setCodigoArchivoOrigen(rs.getInt("codigo_archivo_origen"));
			eDespacho.setEstado(rs.getInt("estado"));
			
			eOperacion = new Operacion();
			eOperacion.setId(rs.getInt("id_operacion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
			eOperacion.setIdCliente(rs.getInt("id_cliente"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eEstacion.setIdOperacion(rs.getInt("id_operacion"));
			eEstacion.setOperacion(eOperacion);
			
			eJornada = new Jornada();
			eJornada.setId(rs.getInt("id_jornada"));
			eJornada.setIdEstacion(rs.getInt("id_estacion"));
			eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));
			eJornada.setEstacion(eEstacion);

			ePropietario = new Propietario();
			ePropietario.setId(rs.getInt("id_propietario"));
			ePropietario.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			ePropietario.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto_propietario")));
			
			eVehiculo = new Vehiculo();
			eVehiculo.setId(rs.getInt("id_vehiculo"));
			eVehiculo.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			eVehiculo.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
			eVehiculo.setIdPropietario(rs.getInt("id_propietario"));
			eVehiculo.setPropietario(ePropietario);
			
			eTanque = new Tanque();
			eTanque.setId(rs.getInt("id_tanque"));
			eTanque.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_tanque")));
			
			eContometro = new Contometro();
			eContometro.setId(rs.getInt("id_contometro"));
			eContometro.setAlias(Utilidades.cleanXSS(rs.getString("alias_contometro")));
			eContometro.setTipoContometro(rs.getInt("tipo_contometro"));
			
			eProducto = new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
			eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura")));
			
			eDespacho.setJornada(eJornada);
			eDespacho.setVehiculo(eVehiculo);
			eDespacho.setTanque(eTanque);
			eDespacho.setContometro(eContometro);
			eDespacho.setProducto(eProducto);
			
			//Parametros de auditoria
			eDespacho.setCreadoPor(rs.getInt("creado_por"));
			eDespacho.setCreadoEl(rs.getLong("creado_el"));
			eDespacho.setActualizadoPor(rs.getInt("actualizado_por"));
			eDespacho.setActualizadoEl(rs.getLong("actualizado_el"));
			eDespacho.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eDespacho.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eDespacho.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eDespacho.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDespacho;
	}
}