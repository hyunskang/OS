package kvstore;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import kvstore.xml.KVCacheEntry;
import kvstore.xml.KVCacheType;
import kvstore.xml.KVSetType;
import kvstore.xml.ObjectFactory;

import java.util.ArrayList;

/**
 * A set-associate cache which has a fixed maximum number of sets (numSets).
 * Each set has a maximum number of elements (MAX_ELEMS_PER_SET).
 * If a set is full and another entry is added, an entry is dropped based on
 * the eviction policy.
 */
public class KVCache implements KeyValueInterface {

    private int MAX_ELEMS_PER_SET;
    private KVCacheType cacheSetWrapper;
    private ArrayList<Lock> lockList;

    /**
     * Constructs a second-chance-replacement cache.
     *
     * @param numSets the number of sets this cache will have
     * @param maxElemsPerSet the size of each set
     */
    @SuppressWarnings("unchecked")
    public KVCache(int numSets, int maxElemsPerSet) {
        // implement me
        this.MAX_ELEMS_PER_SET = maxElemsPerSet;
        this.cacheSetWrapper = new KVCacheType();
        this.lockList = new ArrayList<Lock>();
        ArrayList<KVSetType> cache = (ArrayList<KVSetType>) this.cacheSetWrapper.getSet();
        KVSetType set = null;
        for (int i = 0; i < numSets; i++) {
            set = new KVSetType();
            set.setId(Integer.toString(i));
            cache.add(set);
            this.lockList.add(new ReentrantLock());
        }
    }

    /**
     * Retrieves an entry from the cache.
     * Assumes access to the corresponding set has already been locked by the
     * caller of this method.
     *
     * @param  key the key whose associated value is to be returned.
     * @return the value associated to this key or null if no value is
     *         associated with this key in the cache
     */
    @Override
    public String get(String key) {
        // implement me
        ArrayList<KVSetType> cacheSet = (ArrayList<KVSetType>) this.cacheSetWrapper.getSet();
        int set_id = generate_hashcode(key, cacheSet.size());
        KVSetType set = cacheSet.get(set_id);
        ArrayList<KVCacheEntry> cache = (ArrayList<KVCacheEntry>) set.getCacheEntry();
        KVCacheEntry entry = null;
        for (int i = 0; i < cache.size(); i++) {
            entry = cache.get(i);
            if (entry.getKey().equals(key)) {
                entry.setIsReferenced("true");
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Adds an entry to this cache.
     * If an entry with the specified key already exists in the cache, it is
     * replaced by the new entry. When an entry is replaced, its reference bit
     * will be set to True. If the set is full, an entry is removed from
     * the cache based on the eviction policy. If the set is not full, the entry
     * will be inserted behind all existing entries. For this policy, we suggest
     * using a LinkedList over an array to keep track of entries in a set since
     * deleting an entry in an array will leave a gap in the array, likely not
     * at the end. More details and explanations in the spec. Assumes access to
     * the corresponding set has already been locked by the caller of this
     * method.
     *
     * @param key the key with which the specified value is to be associated
     * @param value a value to be associated with the specified key
     */
    @Override
    public void put(String key, String value) {
        ArrayList<KVSetType> cacheSet = (ArrayList<KVSetType>) this.cacheSetWrapper.getSet();
        int set_id = generate_hashcode(key, cacheSet.size());
        KVSetType set = cacheSet.get(set_id);
        ArrayList<KVCacheEntry> cache = (ArrayList<KVCacheEntry>) set.getCacheEntry();
        KVCacheEntry entry = null;
        for (int i = 0; i < cache.size(); i++) {
            entry = cache.get(i);
            if (entry.getKey().equals(key)) {
                entry.setIsReferenced("false");
                entry.setValue(value);
                return;
            }
        }
        KVCacheEntry new_entry = new KVCacheEntry();
        new_entry.setKey(key);
        new_entry.setValue(value);
        new_entry.setIsReferenced("false");
        if (cache.size() == this.MAX_ELEMS_PER_SET) {
            for (int i = 0; i < cache.size(); i++) {
                entry = cache.get(i);
                if (entry.getIsReferenced().equals("false")) {
                    cache.remove(i);
                    cache.add(new_entry);
                    return;
                }
                entry.setIsReferenced("false");
            }
            cache.remove(0);
        }
        cache.add(new_entry);
        return;
    }

    /**
     * Removes an entry from this cache.
     * Assumes access to the corresponding set has already been locked by the
     * caller of this method. Does nothing if called on a key not in the cache.
     *
     * @param key key with which the specified value is to be associated
     */
    @Override
    public void del(String key) {
        ArrayList<KVSetType> cacheSet = (ArrayList<KVSetType>) this.cacheSetWrapper.getSet();
        int set_id = generate_hashcode(key, cacheSet.size());
        KVSetType set = cacheSet.get(set_id);
        ArrayList<KVCacheEntry> cache = (ArrayList<KVCacheEntry>) set.getCacheEntry();
        KVCacheEntry entry = null;
        for (int i = 0; i < cache.size(); i++) {
            entry = cache.get(i);
            if (entry.getKey().equals(key)) {
                cache.remove(i);
                return;
            }
        }
    }

    /**
     * Get a lock for the set corresponding to a given key.
     * The lock should be used by the caller of the get/put/del methods
     * so that different sets can be #{modified|changed} in parallel.
     *
     * @param  key key to determine the lock to return
     * @return lock for the set that contains the key
     */

    public Lock getLock(String key) {
        //implement me
        ArrayList<KVSetType> cacheSet = (ArrayList<KVSetType>) this.cacheSetWrapper.getSet();
        int set_id = generate_hashcode(key, cacheSet.size());
        return this.lockList.get(set_id);
    }
    
    /**
     * Get the size of a given set in the cache.
     * @param cacheSet Which set.
     * @return Size of the cache set.
     */
    int getCacheSetSize(int cacheSet) {
        // implement me
        KVSetType set = this.cacheSetWrapper.getSet().get(cacheSet);
        return set.getCacheEntry().size();
    }

    private void marshalTo(OutputStream os) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(KVCacheType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.marshal(getXMLRoot(), os);
    }

    private JAXBElement<KVCacheType> getXMLRoot() throws JAXBException {
        ObjectFactory factory = new ObjectFactory();
        KVCacheType xmlCache = factory.createKVCacheType();
        // implement me

        // ArrayList<KVSetType> sets = (ArrayList<KVSetType>) xmlCache.getSet();
        // sets = this.cacheSetWrapper.clone();

        //throw new JAXBException(KVConstants.ERROR_INVALID_FORMAT);

        return factory.createKVCache(xmlCache);
    }

    /**
     * Serialize this store to XML. See spec for details on output format.
     */
    public String toXML() {
        // implement me
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            marshalTo(os);
        } catch (JAXBException e) {
            //e.printStackTrace();
        }
        return os.toString();
    }
    @Override
    public String toString() {
        return this.toXML();
    }

    private int generate_hashcode(String key, int size) {
        int set_id = key.hashCode() % size;
        if (set_id < 0) {
            set_id = set_id + size;
        } else if (set_id == size) {
            set_id = 0;
        }
        return set_id;
    }

}