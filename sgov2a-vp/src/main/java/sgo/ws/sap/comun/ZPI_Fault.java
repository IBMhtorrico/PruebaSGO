/**
 * ZPI_Fault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.comun;

public class ZPI_Fault  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private sgo.ws.sap.comun.ExchangeFaultData standard;

    private sgo.ws.sap.comun.ZPI_Fault_DT addition;

    public ZPI_Fault() {
    }

    public ZPI_Fault(
           sgo.ws.sap.comun.ExchangeFaultData standard,
           sgo.ws.sap.comun.ZPI_Fault_DT addition) {
        this.standard = standard;
        this.addition = addition;
    }


    /**
     * Gets the standard value for this ZPI_Fault.
     * 
     * @return standard
     */
    public sgo.ws.sap.comun.ExchangeFaultData getStandard() {
        return standard;
    }


    /**
     * Sets the standard value for this ZPI_Fault.
     * 
     * @param standard
     */
    public void setStandard(sgo.ws.sap.comun.ExchangeFaultData standard) {
        this.standard = standard;
    }


    /**
     * Gets the addition value for this ZPI_Fault.
     * 
     * @return addition
     */
    public sgo.ws.sap.comun.ZPI_Fault_DT getAddition() {
        return addition;
    }


    /**
     * Sets the addition value for this ZPI_Fault.
     * 
     * @param addition
     */
    public void setAddition(sgo.ws.sap.comun.ZPI_Fault_DT addition) {
        this.addition = addition;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ZPI_Fault)) return false;
        ZPI_Fault other = (ZPI_Fault) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.standard==null && other.getStandard()==null) || 
             (this.standard!=null &&
              this.standard.equals(other.getStandard()))) &&
            ((this.addition==null && other.getAddition()==null) || 
             (this.addition!=null &&
              this.addition.equals(other.getAddition())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getStandard() != null) {
            _hashCode += getStandard().hashCode();
        }
        if (getAddition() != null) {
            _hashCode += getAddition().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZPI_Fault.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:common", ">ZPI_Fault"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standard");
        elemField.setXmlName(new javax.xml.namespace.QName("", "standard"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:common", "ExchangeFaultData"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addition");
        elemField.setXmlName(new javax.xml.namespace.QName("", "addition"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:common", "ZPI_Fault_DT"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
