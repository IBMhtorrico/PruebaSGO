package sgo.entidad;

import java.sql.Date;

public class Aprobador {
 private String identidad;
 private String usuario;
 private Date inicioVigencia;
 private Date FinVigencia;
 public String getIdentidad() {
  return identidad;
 }
 public void setIdentidad(String identidad) {
  this.identidad = identidad;
 }
 public String getUsuario() {
  return usuario;
 }
 public void setUsuario(String usuario) {
  this.usuario = usuario;
 }
 public Date getInicioVigencia() {
  return inicioVigencia;
 }
 public void setInicioVigencia(Date inicioVigencia) {
  this.inicioVigencia = inicioVigencia;
 }
 public Date getFinVigencia() {
  return FinVigencia;
 }
 public void setFinVigencia(Date finVigencia) {
  FinVigencia = finVigencia;
 }

}
