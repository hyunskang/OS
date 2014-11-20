//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.12 at 08:09:11 PM PDT 
//


package kvstore.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the kvstore.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _KVMessage_QNAME = new QName("", "KVMessage");
    private final static QName _KVCache_QNAME = new QName("", "KVCache");
    private final static QName _KVStore_QNAME = new QName("", "KVStore");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: kvstore.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link KVCacheType }
     * 
     */
    public KVCacheType createKVCacheType() {
        return new KVCacheType();
    }

    /**
     * Create an instance of {@link KVMessageType }
     * 
     */
    public KVMessageType createKVMessageType() {
        return new KVMessageType();
    }

    /**
     * Create an instance of {@link KVStoreType }
     * 
     */
    public KVStoreType createKVStoreType() {
        return new KVStoreType();
    }

    /**
     * Create an instance of {@link KVPairType }
     * 
     */
    public KVPairType createKVPairType() {
        return new KVPairType();
    }

    /**
     * Create an instance of {@link KVCacheEntry }
     * 
     */
    public KVCacheEntry createKVCacheEntry() {
        return new KVCacheEntry();
    }

    /**
     * Create an instance of {@link KVSetType }
     * 
     */
    public KVSetType createKVSetType() {
        return new KVSetType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KVMessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "KVMessage")
    public JAXBElement<KVMessageType> createKVMessage(KVMessageType value) {
        return new JAXBElement<KVMessageType>(_KVMessage_QNAME, KVMessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KVCacheType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "KVCache")
    public JAXBElement<KVCacheType> createKVCache(KVCacheType value) {
        return new JAXBElement<KVCacheType>(_KVCache_QNAME, KVCacheType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KVStoreType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "KVStore")
    public JAXBElement<KVStoreType> createKVStore(KVStoreType value) {
        return new JAXBElement<KVStoreType>(_KVStore_QNAME, KVStoreType.class, null, value);
    }

}