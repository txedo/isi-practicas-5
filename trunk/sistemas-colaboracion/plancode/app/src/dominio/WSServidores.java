/**
 * WSServidores.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dominio;

public interface WSServidores extends javax.xml.rpc.Service {
    public java.lang.String getWSServidoresHttpSoap12EndpointAddress();

    public dominio.WSServidoresPortType getWSServidoresHttpSoap12Endpoint() throws javax.xml.rpc.ServiceException;

    public dominio.WSServidoresPortType getWSServidoresHttpSoap12Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getWSServidoresHttpSoap11EndpointAddress();

    public dominio.WSServidoresPortType getWSServidoresHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException;

    public dominio.WSServidoresPortType getWSServidoresHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
