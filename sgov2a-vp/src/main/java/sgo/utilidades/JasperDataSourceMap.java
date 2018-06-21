package sgo.utilidades;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class JasperDataSourceMap implements JRDataSource {

	private List<Object> lista = new ArrayList<Object>();
	private int index = -1;
	private Class clase = null;	
	
	public JasperDataSourceMap() {
		// TODO Apéndice de constructor generado automáticamente
		
	}
	
	public JasperDataSourceMap(Class<?> clase) {
		// TODO Apéndice de constructor generado automáticamente
		this.clase = clase;
	}

	@SuppressWarnings("unchecked")
	public Object getFieldValue(JRField jrField) throws JRException {
		// TODO Apéndice de método generado automáticamente
		Object data = null; 
		
		Map<String, Object> record = (Map<String, Object>)lista.get(index);
		data = record.get(jrField.getName());
	    return data;
	}

	public boolean next() throws JRException {
		// TODO Apéndice de método generado automáticamente
		return ++index < lista.size();
	}
	
	public void addListData( List<Object> data)
	{
	    this.lista = data;
	}
	
	public void addRecord(Object data)
	{
	    this.lista.add(data);
	}
	
	private static boolean isGetter(Method method){
		  if(!method.getName().startsWith("get"))      return false;
		  if(method.getParameterTypes().length != 0)   return false;  
		  if(void.class.equals(method.getReturnType())) return false;
		  return true;
		}

}