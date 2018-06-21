package sgo.utilidades;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Formula {
 public static double calcularInterpolacion(double X1, double X2, double Y1, double Y2, double X){
  double respuesta = 0;
  try {   
   respuesta = Y1 +  (((X-X1)/(X2-X1))*(Y2-Y1));
  }catch(Exception ex){
   
  }
    return respuesta;
 }
 
 public static double calcularFactorCorreccion(double apiCorregido, double temperaturaObservada){
  double K1=0;
  double K0=0;
  double coeficienteDilatacion=0;
  double factorCorrecion=0;
  //apiCorregido = redondearDouble(apiCorregido,1);
  double densidad =  (double) (141.5 * 999.012/(apiCorregido+131.5));
  densidad = (double) Math.round(densidad*100000)/100000;
  //BigDecimal densidadBig= new BigDecimal(String.valueOf(densidad)).setScale(2, BigDecimal.ROUND_HALF_UP); 
  if (apiCorregido < 37.1){
    K1=(double) 0.2701;
  } else if ((apiCorregido>= 37.1) &&(apiCorregido < 48)){
    K1=0;
  } else if ((apiCorregido>= 48) &&(apiCorregido < 52.1)){
    K1= (double) (-0.0018684 * densidad);
  } else if ((apiCorregido>= 52.1) &&(apiCorregido < 85.1)){
    K1=(double) 0.2438;
  }     

  if (apiCorregido < 37.1){
    K0=(double) 103.872;
  } else if ((apiCorregido>= 37.1) &&(apiCorregido < 48)){
    K0=(double) 330.301;
  } else if ((apiCorregido>= 48) &&(apiCorregido < 52.1)){
    K0= (double) (1489.067);
  } else if ((apiCorregido>= 52.1) &&(apiCorregido < 85.1)){
    K0=(double) 192.4571;
  }
  
  coeficienteDilatacion=((K0 / densidad + K1 )/ densidad ); 
  coeficienteDilatacion = (double)Math.round(coeficienteDilatacion* 10000000)/10000000;
  factorCorrecion= (double) Math.exp(-coeficienteDilatacion*(temperaturaObservada-60)*(1+0.8*coeficienteDilatacion*(temperaturaObservada-60))) ;
  double seisDigitos=(double)Math.round(factorCorrecion* 1000000)/1000000;
  double redondeado5=redondearDouble(factorCorrecion,5); 
  double redondeado6=redondearDouble(factorCorrecion,6); 
  factorCorrecion = (double)Math.round(factorCorrecion* 100000)/100000;
  
  System.out.println("factorCorrecion: "+factorCorrecion);
  System.out.println("seisDigitos: "+seisDigitos);
  System.out.println("redondeado5: "+redondeado5);
  System.out.println("redondeado6: "+redondeado6);
  return factorCorrecion;
}
 
 public static double redondearDouble(double value, int cantidadDecimales) {
  if (cantidadDecimales < 0) throw new IllegalArgumentException();
  BigDecimal bd = new BigDecimal(value);
  bd = bd.setScale(cantidadDecimales, RoundingMode.HALF_UP);
  return bd.doubleValue();
}
}
