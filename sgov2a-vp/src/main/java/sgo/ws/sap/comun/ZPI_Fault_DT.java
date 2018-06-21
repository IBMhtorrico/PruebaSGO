/**
 * ZPI_Fault_DT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package sgo.ws.sap.comun;

public class ZPI_Fault_DT  implements java.io.Serializable {
    private java.lang.String num_Msg;

    private java.lang.String tipo_Msg;

    private java.lang.String desc1;

    private java.lang.String desc2;

    private java.lang.String desc3;

    private java.lang.String desc4;

    public ZPI_Fault_DT() {
    }

    public ZPI_Fault_DT(
           java.lang.String num_Msg,
           java.lang.String tipo_Msg,
           java.lang.String desc1,
           java.lang.String desc2,
           java.lang.String desc3,
           java.lang.String desc4) {
           this.num_Msg = num_Msg;
           this.tipo_Msg = tipo_Msg;
           this.desc1 = desc1;
           this.desc2 = desc2;
           this.desc3 = desc3;
           this.desc4 = desc4;
    }


    /**
     * Gets the num_Msg value for this ZPI_Fault_DT.
     * 
     * @return num_Msg
     */
    public java.lang.String getNum_Msg() {
        return num_Msg;
    }


    /**
     * Sets the num_Msg value for this ZPI_Fault_DT.
     * 
     * @param num_Msg
     */
    public void setNum_Msg(java.lang.String num_Msg) {
        this.num_Msg = num_Msg;
    }


    /**
     * Gets the tipo_Msg value for this ZPI_Fault_DT.
     * 
     * @return tipo_Msg
     */
    public java.lang.String getTipo_Msg() {
        return tipo_Msg;
    }


    /**
     * Sets the tipo_Msg value for this ZPI_Fault_DT.
     * 
     * @param tipo_Msg
     */
    public void setTipo_Msg(java.lang.String tipo_Msg) {
        this.tipo_Msg = tipo_Msg;
    }


    /**
     * Gets the desc1 value for this ZPI_Fault_DT.
     * 
     * @return desc1
     */
    public java.lang.String getDesc1() {
        return desc1;
    }


    /**
     * Sets the desc1 value for this ZPI_Fault_DT.
     * 
     * @param desc1
     */
    public void setDesc1(java.lang.String desc1) {
        this.desc1 = desc1;
    }


    /**
     * Gets the desc2 value for this ZPI_Fault_DT.
     * 
     * @return desc2
     */
    public java.lang.String getDesc2() {
        return desc2;
    }


    /**
     * Sets the desc2 value for this ZPI_Fault_DT.
     * 
     * @param desc2
     */
    public void setDesc2(java.lang.String desc2) {
        this.desc2 = desc2;
    }


    /**
     * Gets the desc3 value for this ZPI_Fault_DT.
     * 
     * @return desc3
     */
    public java.lang.String getDesc3() {
        return desc3;
    }


    /**
     * Sets the desc3 value for this ZPI_Fault_DT.
     * 
     * @param desc3
     */
    public void setDesc3(java.lang.String desc3) {
        this.desc3 = desc3;
    }


    /**
     * Gets the desc4 value for this ZPI_Fault_DT.
     * 
     * @return desc4
     */
    public java.lang.String getDesc4() {
        return desc4;
    }


    /**
     * Sets the desc4 value for this ZPI_Fault_DT.
     * 
     * @param desc4
     */
    public void setDesc4(java.lang.String desc4) {
        this.desc4 = desc4;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ZPI_Fault_DT)) return false;
        ZPI_Fault_DT other = (ZPI_Fault_DT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.num_Msg==null && other.getNum_Msg()==null) || 
             (this.num_Msg!=null &&
              this.num_Msg.equals(other.getNum_Msg()))) &&
            ((this.tipo_Msg==null && other.getTipo_Msg()==null) || 
             (this.tipo_Msg!=null &&
              this.tipo_Msg.equals(other.getTipo_Msg()))) &&
            ((this.desc1==null && other.getDesc1()==null) || 
             (this.desc1!=null &&
              this.desc1.equals(other.getDesc1()))) &&
            ((this.desc2==null && other.getDesc2()==null) || 
             (this.desc2!=null &&
              this.desc2.equals(other.getDesc2()))) &&
            ((this.desc3==null && other.getDesc3()==null) || 
             (this.desc3!=null &&
              this.desc3.equals(other.getDesc3()))) &&
            ((this.desc4==null && other.getDesc4()==null) || 
             (this.desc4!=null &&
              this.desc4.equals(other.getDesc4())));
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
        if (getNum_Msg() != null) {
            _hashCode += getNum_Msg().hashCode();
        }
        if (getTipo_Msg() != null) {
            _hashCode += getTipo_Msg().hashCode();
        }
        if (getDesc1() != null) {
            _hashCode += getDesc1().hashCode();
        }
        if (getDesc2() != null) {
            _hashCode += getDesc2().hashCode();
        }
        if (getDesc3() != null) {
            _hashCode += getDesc3().hashCode();
        }
        if (getDesc4() != null) {
            _hashCode += getDesc4().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZPI_Fault_DT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:petroperu.com.pe:pmerp:pi:common", "ZPI_Fault_DT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_Msg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Num_Msg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_Msg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tipo_Msg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Desc1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Desc2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Desc3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc4");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Desc4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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

}
