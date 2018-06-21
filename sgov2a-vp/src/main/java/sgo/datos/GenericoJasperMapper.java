package sgo.datos;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.core.RowMapper;

import sgo.utilidades.Utilidades;

public class GenericoJasperMapper implements RowMapper<HashMap<String, Object>>{
 public HashMap<String, Object> mapRow(ResultSet rsRegistro, int arg1) throws SQLException 
 {
  HashMap<String,Object> hmRegistro = new HashMap<String,Object>();
  String nombreClase="", clave="";
  Object valor=null;
  BigDecimal valorDecimal=null;
  try {
   ResultSetMetaData rsMetadatos = rsRegistro.getMetaData();
   int numeroColumnas= rsMetadatos.getColumnCount();
   
   for(int contador=1; contador<= numeroColumnas;contador++){
    valor="";
    nombreClase = rsMetadatos.getColumnClassName(contador);
    clave= rsMetadatos.getColumnName(contador);
    
    if (nombreClase.equals(java.lang.String.class.getName())){
     if (rsRegistro.getString(contador)!=null){
    	 valor= Utilidades.cleanXSS(rsRegistro.getString(contador));
      
     }     
    }
    if (nombreClase.equals(java.math.BigDecimal.class.getName())) {
     if (rsRegistro.getBigDecimal(contador) == null){
       valor=null;
     } else {
       valorDecimal = rsRegistro.getBigDecimal(contador);
       valorDecimal = valorDecimal.setScale(6, BigDecimal.ROUND_HALF_UP);
       valor = valorDecimal;
     }
    }
    if (nombreClase.equals(java.sql.Date.class.getName())){
     if (rsRegistro.getDate(contador)!= null){
      valor=rsRegistro.getDate(contador);
      //String[] partesFecha = valor.split("-");
      //valor = partesFecha[2]+"/"+partesFecha[1]+"/"+partesFecha[0];
      
     }
   }
   if (nombreClase.equals(java.lang.Integer.class.getName())){
     valor = rsRegistro.getInt(contador) ;
    
   }
   if (nombreClase.equals(java.lang.Double.class.getName())){
	     valor = rsRegistro.getDouble(contador) ;
	    
	   }
   if (nombreClase.equals(java.lang.Float.class.getName())){
	     valor = rsRegistro.getFloat(contador) ;
	    
	   }
   
   if (nombreClase.equals(java.lang.Long.class.getName())){
     valor = rsRegistro.getLong(contador) ;
   }
   System.out.println("clave:"+clave);
   System.out.println("valor:"+valor);
   hmRegistro.put(clave, valor);
   }
  } catch(Exception ex){
   ex.printStackTrace();
  }
  return hmRegistro;
 }
}
