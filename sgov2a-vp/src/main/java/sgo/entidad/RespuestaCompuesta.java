package sgo.entidad;

public class RespuestaCompuesta extends Respuesta {
		public Contenido<?> contenido;		
		public RespuestaCompuesta(){
			super();
			this.contenido= null;
		}
		
		public Contenido<?> getContenido(){
			return contenido;		
		}


}
