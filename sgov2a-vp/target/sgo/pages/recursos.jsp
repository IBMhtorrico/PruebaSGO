<%@ page language="java" contentType="application/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page trimDirectiveWhitespaces="true" %>
<% HashMap<String,String> hmRecursos =  (HashMap<String,String>)request.getAttribute("recursos"); %>
<%
Iterator<String> keySetIterator = hmRecursos.keySet().iterator(); 
out.print("var cadenas={");
String separador="";
while(keySetIterator.hasNext()){
	String key = keySetIterator.next(); 
	out.print(separador+key+":'"+hmRecursos.get(key)+"'");
	separador=",";
}
out.print("};");
%>