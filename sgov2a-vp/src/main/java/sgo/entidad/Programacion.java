package sgo.entidad;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.MessageSource;

import sgo.utilidades.Constante;
import sgo.utilidades.Utilidades;

public class Programacion extends EntidadBase {
	private int idProgramacion;
	private int idTransportista;
	private Transportista transportista;
	private int estado;
	private int idDiaOperativo;
	private DiaOperativo diaOperativo;
	private ArrayList<DetalleProgramacion> programaciones;	
	private ArrayList<DetalleProgramacionCorreos> correosProgramaciones;	
	private float total_volumen_cisterna;
	private String comentario;
	
	static final int MAXIMA_LONGITUD_COMENTARIO=700;
	
	public DiaOperativo getDiaOperativo() {
		return diaOperativo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public void setDiaOperativo(DiaOperativo diaOperativo) {
		this.diaOperativo = diaOperativo;
	}

	public int getId() {
		return idProgramacion;
	}

	public void setId(int Id) {
		this.idProgramacion = Id;
	}

	public Transportista getTransportista() {
		return transportista;
	}

	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}
	public ArrayList<DetalleProgramacion> getProgramaciones() {
		return programaciones;
	}

	public void setProgramaciones(ArrayList<DetalleProgramacion> programaciones) {
		this.programaciones = programaciones;
	}

	public int getIdDiaOperativo() {
		return idDiaOperativo;
	}

	public void setIdDiaOperativo(int idDiaOperativo) {
		this.idDiaOperativo = idDiaOperativo;
	}

	public int getIdTransportista() {
		return idTransportista;
	}

	public void setIdTransportista(int idTransportista) {
		this.idTransportista = idTransportista;
	}

	public float getTotalVolumenCisterna() {
		return total_volumen_cisterna;
	}

	public void setTotalVolumenCisterna(float total_volumen_propuesto) {
		this.total_volumen_cisterna = total_volumen_propuesto;
	}

	public ArrayList<DetalleProgramacionCorreos> getCorreosProgramaciones() {
		return correosProgramaciones;
	}

	public void setCorreosProgramaciones(
			ArrayList<DetalleProgramacionCorreos> correosProgramaciones) {
		this.correosProgramaciones = correosProgramaciones;
	}

	public final String getComentario() {
		return comentario;
	}

	public final void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public Respuesta validar(MessageSource gestorDiccionario, Locale locale){
		  Respuesta respuesta = new Respuesta();
			try {
			  
			  if (this.comentario.length() > MAXIMA_LONGITUD_COMENTARIO){			
				  respuesta.estado = false;
				  respuesta.valor = gestorDiccionario.getMessage("sgo.errorValoresNulosEntidad", new Object[] { "Comentario" }, locale);
				  return respuesta;
			  }
			  respuesta.estado = true;
			  respuesta.valor = null;
		  } catch (Exception excepcionGenerica) {
		   excepcionGenerica.printStackTrace();
		   respuesta.error = Constante.EXCEPCION_GENERICA;
		   respuesta.valor = gestorDiccionario.getMessage("sgo.errorGenericoServidor", null, locale);
		   respuesta.estado = false;
		  }
		  return respuesta;
		}
	
	public String getCadena() {
		String cadena="";
		if(!Utilidades.esValido(this.getComentario())){ this.setComentario(""); };
		
		cadena = this.getComentario();

		return cadena;
	}

}