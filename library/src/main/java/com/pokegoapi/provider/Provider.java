package com.pokegoapi.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;
import java.util.*;

import static java.util.Locale.ENGLISH;

public abstract class Provider extends Properties {
    
    private static final boolean debug = false;

    /**
     * The provider name.
     *
     * @serial
     */
    private String name;

    /**
     * A description of the provider and its services.
     *
     * @serial
     */
    private String info;

    /**
     * The provider version number.
     *
     * @serial
     */
    private double version;

    private transient boolean initialized;

    private transient Set<Map.Entry<Object,Object>> entrySet = null;
    private transient int entrySetCallCount = 0;

    // legacy properties changed since last call to any services method?
    private transient boolean legacyChanged;
    // serviceMap changed since last call to getServices()
    private transient boolean servicesChanged;

    // Map<String,String>
    private transient Map<String,String> legacyStrings;

    // Map<ServiceKey,Service>
    // used for services added via putService(), initialized on demand
    private transient Map<ServiceKey, Service> serviceMap;

    // Map<ServiceKey,Service>
    // used for services added via legacy methods, init on demand
    private transient Map<ServiceKey, Service> legacyMap;

    // Set<Service>
    // Unmodifiable set of all services. Initialized on demand.
    private transient Set<Service> serviceSet;

    /**
     * Constructs a provider with the specified name, version number,
     * and information.
     *
     * @param name the provider name.
     *
     * @param version the provider version number.
     *
     * @param info a description of the provider and its services.
     */
    protected Provider(String name, double version, String info) {
        this.name = name;
        this.version = version;
        this.info = info;
        putId();
        initialized = true;
    }

    /**
     * Returns the name of this provider.
     *
     * @return the name of this provider.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the version number for this provider.
     *
     * @return the version number for this provider.
     */
    public double getVersion() {
        return version;
    }

    /**
     * Returns a human-readable description of the provider and its
     * services.  This may return an HTML page, with relevant links.
     *
     * @return a description of the provider and its services.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Returns a string with the name and the version number
     * of this provider.
     *
     * @return the string with the name and the version number
     * for this provider.
     */
    public String toString() {
        return name + " version " + version;
    }


    /**
     * Reads a property list (key and element pairs) from the input stream.
     *
     * @param inStream   the input stream.
     * @exception IOException  if an error occurred when reading from the
     *               input stream.
     * @see java.util.Properties#load
     */
    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        if (debug) {
            System.out.println("Load " + name + " provider properties");
        }
        Properties tempProperties = new Properties();
        tempProperties.load(inStream);
        implPutAll(tempProperties);
    }

    /**
     * Copies all of the mappings from the specified Map to this provider.
     * These mappings will replace any properties that this provider had
     * for any of the keys currently in the specified Map.
     */
    @Override
    public synchronized void putAll(Map<?,?> t) {
        if (debug) {
            System.out.println("Put all " + name + " provider properties");
        }
        implPutAll(t);
    }

    /**
     * Returns an unmodifiable Set view of the property entries contained
     * in this Provider.
     *
     * @see   java.util.Map.Entry
     */
    @Override
    public synchronized Set<Map.Entry<Object,Object>> entrySet() {
        checkInitialized();
        if (entrySet == null) {
            if (entrySetCallCount++ == 0)  // Initial call
                entrySet = Collections.unmodifiableMap(this).entrySet();
            else
                return super.entrySet();   // Recursive call
        }

        // This exception will be thrown if the implementation of
        // Collections.unmodifiableMap.entrySet() is changed such that it
        // no longer calls entrySet() on the backing Map.  (Provider's
        // entrySet implementation depends on this "implementation detail",
        // which is unlikely to change.
        if (entrySetCallCount != 2)
            throw new RuntimeException("Internal error.");

        return entrySet;
    }

    /**
     * Returns an unmodifiable Set view of the property keys contained in
     * this provider.
     */
    @Override
    public Set<Object> keySet() {
        checkInitialized();
        return Collections.unmodifiableSet(super.keySet());
    }

    /**
     * Returns an unmodifiable Collection view of the property values
     * contained in this provider.
     */
    @Override
    public Collection<Object> values() {
        checkInitialized();
        return Collections.unmodifiableCollection(super.values());
    }

    /**
     * Sets the {@code key} property to have the specified
     * {@code value}.
     */
    @Override
    public synchronized Object put(Object key, Object value) {
        if (debug) {
            System.out.println("Set " + name + " provider property [" +
                    key + "/" + value +"]");
        }
        return implPut(key, value);
    }

    /**
     * Removes the {@code key} property (and its corresponding
     * {@code value}).
     */
    @Override
    public synchronized Object remove(Object key) {
        if (debug) {
            System.out.println("Remove " + name + " provider property " + key);
        }
        return implRemove(key);
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException();
        }
    }

    private boolean checkLegacy(Object key) {
        String keyString = (String)key;
        if (keyString.startsWith("Provider.")) {
            return false;
        }

        legacyChanged = true;
        if (legacyStrings == null) {
            legacyStrings = new LinkedHashMap<>();
        }
        return true;
    }

    // register the id attributes for this provider
    // this is to ensure that equals() and hashCode() do not incorrectly
    // report to different provider objects as the same
    private void putId() {
        // note: name and info may be null
        super.put("Provider.id name", String.valueOf(name));
        super.put("Provider.id version", String.valueOf(version));
        super.put("Provider.id info", String.valueOf(info));
        super.put("Provider.id className", this.getClass().getName());
    }

    /**
     * Copies all of the mappings from the specified Map to this provider.
     * Internal method to be called AFTER the security check has been
     * performed.
     */
    private void implPutAll(Map<?,?> t) {
        for (Map.Entry<?,?> e : t.entrySet()) {
            implPut(e.getKey(), e.getValue());
        }
    }

    private Object implRemove(Object key) {
        if (key instanceof String) {
            if (!checkLegacy(key)) {
                return null;
            }
            legacyStrings.remove(key);
        }
        return super.remove(key);
    }

    private Object implPut(Object key, Object value) {
        if ((key instanceof String) && (value instanceof String)) {
            if (!checkLegacy(key)) {
                return null;
            }
            legacyStrings.put((String)key, (String)value);
        }
        return super.put(key, value);
    }

    private void parsePut(String name, String value) {
        String type = getEngineName(name);
        ServiceKey key = new ServiceKey(type);
        Service s = serviceMap.get(key);
        if (s == null) {
            s = new Service(this);
            s.type = type;
            serviceMap.put(key, s);
        }
        s.className = value;
    }

    /**
     * Ensure all the legacy String properties are fully parsed into
     * service objects.
     */
    private void ensureLegacyParsed() {
        if ((!legacyChanged) || (legacyStrings == null)) {
            return;
        }
        serviceSet = null;
        if (legacyMap == null) {
            legacyMap = new LinkedHashMap<>();
        } else {
            legacyMap.clear();
        }
        for (Map.Entry<String,String> entry : legacyStrings.entrySet()) {
            parsePut(entry.getKey(), entry.getValue());
        }
        removeInvalidServices(legacyMap);
        legacyChanged = false;
    }

    /**
     * Remove all invalid services from the Map. Invalid services can only
     * occur if the legacy properties are inconsistent or incomplete.
     */
    private void removeInvalidServices(Map<ServiceKey, Provider.Service> map) {
        for (Iterator<Map.Entry<ServiceKey, Service>> t =
             map.entrySet().iterator(); t.hasNext(); ) {
            Provider.Service s = t.next().getValue();
            if (!s.isValid()) {
                t.remove();
            }
        }
    }

    /**
     * Get the service describing this Provider's implementation of the
     * specified type of this algorithm or alias. If no such
     * implementation exists, this method returns null. If there are two
     * matching services, one added to this provider using
     * {@link #putService putService()} and one added via {@link #put put()},
     * the service added via {@link #putService putService()} is returned.
     *
     * @param type the type of {@link Service service} requested
     * (for example, {@code Map})
     *
     * @return the service describing this Provider's matching service
     * or null if no such service exists
     *
     * @throws NullPointerException if type or algorithm is null
     */
    public synchronized Service getService(String type) {
        checkInitialized();
        // avoid allocating a new key object if possible
        ServiceKey key = previousKey;
        if (!key.matches(type)) {
            key = new ServiceKey(type);
            previousKey = key;
        }
        if (serviceMap != null) {
            Service service = serviceMap.get(key);
            if (service != null) {
                return service;
            }
        }
        ensureLegacyParsed();
        return (legacyMap != null) ? legacyMap.get(key) : null;
    }

    // ServiceKey from previous getService() call
    // by re-using it if possible we avoid allocating a new object
    // and the toUpperCase() call.
    // re-use will occur e.g. as the framework traverses the provider
    // list and queries each provider with the same values until it finds
    // a matching service
    private static volatile ServiceKey previousKey =
            new ServiceKey("");

    /**
     * Get an unmodifiable Set of all services supported by
     * this Provider.
     *
     * @return an unmodifiable Set of all services supported by
     * this Provider
     *
     * @since 1.5
     */
    public synchronized Set<Service> getServices() {
        checkInitialized();
        if (legacyChanged || servicesChanged) {
            serviceSet = null;
        }
        if (serviceSet == null) {
            ensureLegacyParsed();
            Set<Service> set = new LinkedHashSet<>();
            if (serviceMap != null) {
                set.addAll(serviceMap.values());
            }
            if (legacyMap != null) {
                set.addAll(legacyMap.values());
            }
            serviceSet = Collections.unmodifiableSet(set);
            servicesChanged = false;
        }
        return serviceSet;
    }

    /**
     * Add a service. If a service of the same type with the same algorithm
     * name exists and it was added using {@link #putService putService()},
     * it is replaced by the new service.
     * This method also places information about this service
     * in the provider's Hashtable values
     *
     * @param s the Service to add
     *
     * @throws NullPointerException if s is null
     */
    protected synchronized void putService(Service s) {
        if (debug) {
            System.out.println(name + ".putService(): " + s);
        }
        if (s == null) {
            throw new NullPointerException();
        }
        if (s.getProvider() != this) {
            throw new IllegalArgumentException
                    ("service.getProvider() must match this Provider object");
        }
        if (serviceMap == null) {
            serviceMap = new LinkedHashMap<>();
        }
        servicesChanged = true;
        String type = s.getType();
        ServiceKey key = new ServiceKey(type);
        // remove existing service
        implRemoveService(serviceMap.get(key));
        serviceMap.put(key, s);
        putPropertyStrings(s);
    }

    /**
     * Put the string properties for this Service in this Provider's
     * Hashtable.
     */
    private void putPropertyStrings(Service s) {
        String type = s.getType();
        // use super() to avoid permission check and other processing
        super.put(type, s.getClassName());
        for (Map.Entry<WrappedString,String> entry : s.attributes.entrySet()) {
            String key = type + "." + entry.getKey();
            super.put(key, entry.getValue());
        }
    }

    /**
     * Remove the string properties for this Service from this Provider's
     * Hashtable.
     */
    private void removePropertyStrings(Service s) {
        String type = s.getType();
        // use super() to avoid permission check and other processing
        super.remove(type);
        for (Map.Entry<WrappedString, String> entry : s.attributes.entrySet()) {
            String key = type + "." + entry.getKey();
            super.remove(key);
        }
    }

    /**
     * Remove a service previously added using
     * {@link #putService putService()}. The specified service is removed from
     * this provider. It will no longer be returned by
     * {@link #getService getService()} and its information will be removed
     * from this provider's Hashtable.
     *
     * @param s the Service to be removed
     *
     * @throws NullPointerException if s is null
     *
     * @since 1.5
     */
    protected synchronized void removeService(Service s) {
        if (debug) {
            System.out.println(name + ".removeService(): " + s);
        }
        if (s == null) {
            throw new NullPointerException();
        }
        implRemoveService(s);
    }

    private void implRemoveService(Service s) {
        if ((s == null) || (serviceMap == null)) {
            return;
        }
        String type = s.getType();
        ServiceKey key = new ServiceKey(type);
        Service oldService = serviceMap.get(key);
        if (s != oldService) {
            return;
        }
        servicesChanged = true;
        serviceMap.remove(key);
        removePropertyStrings(s);
    }

    private static String getEngineName(String s) {
        // try original case first, usually correct
        EngineDescription e = knownEngines.get(s);
        if (e == null) {
            e = knownEngines.get(s.toLowerCase(ENGLISH));
        }
        return (e == null) ? s : e.name;
    }

    // used as key in the serviceMap and legacyMap HashMaps
    private static class ServiceKey {
        private final String type;
        private ServiceKey(String type) {
            this.type = type;
        }
        public int hashCode() {
            return type.hashCode();
        }
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ServiceKey)) {
                return false;
            }
            ServiceKey other = (ServiceKey)obj;
            return this.type.equals(other.type);
        }
        public boolean matches(String type){
            return this.type.equals(type);
        }
    }


    // describe relevant properties of a type of engine
    private static class EngineDescription {
        final String name;
        final boolean supportsParameter;
        final String constructorParameterClassName;
        private volatile Class<?> constructorParameterClass;

        EngineDescription(String name, boolean sp, String paramName) {
            this.name = name;
            this.supportsParameter = sp;
            this.constructorParameterClassName = paramName;
        }
        Class<?> getConstructorParameterClass() throws ClassNotFoundException {
            Class<?> clazz = constructorParameterClass;
            if (clazz == null) {
                clazz = Class.forName(constructorParameterClassName);
                constructorParameterClass = clazz;
            }
            return clazz;
        }
    }

    // built in knowledge of the engine types shipped as part of the JDK
    private static final Map<String, EngineDescription> knownEngines;

    private static void addEngine(String name, boolean sp, String paramName) {
        EngineDescription ed = new EngineDescription(name, sp, paramName);
        // also index by canonical name to avoid toLowerCase() for some lookups
        knownEngines.put(name.toLowerCase(ENGLISH), ed);
        knownEngines.put(name, ed);
    }

    static {
        knownEngines = new HashMap<>();
        addEngine("PokemonGoClient", false, null);
    }

    // Wrapped String that behaves in a case insensitive way for equals/hashCode
    private static class WrappedString {
        final String string;
        final String lowerString;

        WrappedString(String s) {
            this.string = s;
            this.lowerString = s.toLowerCase(ENGLISH);
        }

        public int hashCode() {
            return lowerString.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WrappedString)) {
                return false;
            }
            WrappedString other = (WrappedString)obj;
            return lowerString.equals(other.lowerString);
        }

        public String toString() {
            return string;
        }
    }

    public static class Service {

        private String type, className;
        private final Provider provider;
        private Map<WrappedString, String> attributes;

        // Reference to the cached implementation Class object
        private volatile Reference<Class<?>> classRef;

        // whether this service has been registered with the Provider
        private boolean registered;

        // this constructor and these methods are used for parsing
        // the legacy string properties.

        private Service(Provider provider) {
            this.provider = provider;
            attributes = Collections.emptyMap();
        }

        private boolean isValid() {
            return (type != null) && (className != null);
        }

        void addAttribute(String type, String value) {
            if (attributes.isEmpty()) {
                attributes = new HashMap<>(8);
            }
            attributes.put(new WrappedString(type), value);
        }

        /**
         * Construct a new service.
         *
         * @param provider the provider that offers this service
         * @param type the type of this service
         * @param className the name of the class implementing this service
         * @param attributes Map of attributes or null if this implementation
         *                   has no attributes
         *
         * @throws NullPointerException if provider, type, algorithm, or
         * className is null
         */
        public Service(Provider provider, String type, String className,
                       Map<String,String> attributes) {
            if ((provider == null) || (type == null) || (className == null)) {
                throw new NullPointerException();
            }
            this.provider = provider;
            this.type = getEngineName(type);
            this.className = className;
            if (attributes == null) {
                this.attributes = Collections.emptyMap();
            } else {
                this.attributes = new HashMap<>();
                for (Map.Entry<String,String> entry : attributes.entrySet()) {
                    this.attributes.put(new WrappedString(entry.getKey()), entry.getValue());
                }
            }
        }

        /**
         * Get the type of this service.
         *
         * @return the type of this service
         */
        public final String getType() {
            return type;
        }

        /**
         * Return the Provider of this service.
         *
         * @return the Provider of this service
         */
        public final Provider getProvider() {
            return provider;
        }

        /**
         * Return the name of the class implementing this service.
         *
         * @return the name of the class implementing this service
         */
        public final String getClassName() {
            return className;
        }

        /**
         * Return the value of the specified attribute or null if this
         * attribute is not set for this Service.
         *
         * @param name the name of the requested attribute
         *
         * @return the value of the specified attribute or null if the
         *         attribute is not present
         *
         * @throws NullPointerException if name is null
         */
        public final String getAttribute(String name) {
            if (name == null) {
                throw new NullPointerException();
            }
            return attributes.get(new WrappedString(name));
        }

        /**
         * Return a new instance of the implementation described by this
         * service. The provider framework uses this method to
         * construct implementations. Applications will typically not need
         * to call it.
         *
         * <p>The default implementation uses reflection to invoke the
         * standard constructor for this type of service.
         * Security providers can override this method to implement
         * instantiation in a different way.
         *
         * @param constructorParameter the value to pass to the constructor,
         * or null if this type of service does not use a constructorParameter.
         *
         * @return a new implementation of this service
         *
         * @throws InvalidParameterException if the value of
         * constructorParameter is invalid for this type of service.
         * @throws NoSuchTypeException if instantiation failed for
         * any other reason.
         */
        public Object newInstance(Object constructorParameter)
                throws NoSuchTypeException {
            if (!registered) {
                if (provider.getService(type) != this) {
                    throw new NoSuchTypeException
                            ("Service not registered with Provider "
                                    + provider.getName() + ": " + this);
                }
                registered = true;
            }
            try {
                EngineDescription cap = knownEngines.get(type);
                if (cap == null) {
                    // unknown engine type, use generic code
                    // this is the code path future for non-core
                    // optional packages
                    return newInstanceGeneric(constructorParameter);
                }
                if (cap.constructorParameterClassName == null) {
                    if (constructorParameter != null) {
                        throw new InvalidParameterException
                                ("constructorParameter not used with " + type
                                        + " engines");
                    }
                    Class<?> clazz = getImplClass();
                    Class<?>[] empty = {};
                    Constructor<?> con = clazz.getConstructor(empty);
                    return con.newInstance();
                } else {
                    Class<?> paramClass = cap.getConstructorParameterClass();
                    if (constructorParameter != null) {
                        Class<?> argClass = constructorParameter.getClass();
                        if (!paramClass.isAssignableFrom(argClass)) {
                            throw new InvalidParameterException
                                    ("constructorParameter must be instanceof "
                                            + cap.constructorParameterClassName.replace('$', '.')
                                            + " for engine type " + type);
                        }
                    }
                    Class<?> clazz = getImplClass();
                    Constructor<?> cons = clazz.getConstructor(paramClass);
                    return cons.newInstance(constructorParameter);
                }
            } catch (NoSuchTypeException e) {
                throw e;
            } catch (InvocationTargetException e) {
                throw new NoSuchTypeException
                        ("Error constructing implementation (provider: " + provider.getName()
                                + ", class: " + className + ")", e.getCause());
            } catch (Exception e) {
                throw new NoSuchTypeException("Error constructing implementation (provider: " + provider.getName()
                                + ", class: " + className + ")", e);
            }
        }

        // return the implementation Class object for this service
        private Class<?> getImplClass() throws NoSuchTypeException {
            try {
                Reference<Class<?>> ref = classRef;
                Class<?> clazz = (ref == null) ? null : ref.get();
                if (clazz == null) {
                    ClassLoader cl = provider.getClass().getClassLoader();
                    if (cl == null) {
                        clazz = Class.forName(className);
                    } else {
                        clazz = cl.loadClass(className);
                    }
                    if (!Modifier.isPublic(clazz.getModifiers())) {
                        throw new NoSuchTypeException
                                ("class configured for " + type + " (provider: " +
                                        provider.getName() + ") is not public.");
                    }
                    classRef = new WeakReference<Class<?>>(clazz);
                }
                return clazz;
            } catch (ClassNotFoundException e) {
                throw new NoSuchTypeException
                        ("class configured for " + type + " (provider: " +
                                provider.getName() + ") cannot be found.", e);
            }
        }

        /**
         * Generic code path for unknown engine types. Call the
         * no-args constructor if constructorParameter is null, otherwise
         * use the first matching constructor.
         */
        private Object newInstanceGeneric(Object constructorParameter)
                throws Exception {
            Class<?> clazz = getImplClass();
            if (constructorParameter == null) {
                // create instance with public no-arg constructor if it exists
                try {
                    Class<?>[] empty = {};
                    Constructor<?> con = clazz.getConstructor(empty);
                    return con.newInstance();
                } catch (NoSuchMethodException e) {
                    throw new NoSuchTypeException("No public no-arg "
                            + "constructor found in class " + className);
                }
            }
            Class<?> argClass = constructorParameter.getClass();
            Constructor[] cons = clazz.getConstructors();
            // find first public constructor that can take the
            // argument as parameter
            for (Constructor<?> con : cons) {
                Class<?>[] paramTypes = con.getParameterTypes();
                if (paramTypes.length != 1) {
                    continue;
                }
                if (!paramTypes[0].isAssignableFrom(argClass)) {
                    continue;
                }
                return con.newInstance(constructorParameter);
            }
            throw new NoSuchTypeException("No public constructor matching "
                    + argClass.getName() + " found in class " + className);
        }

        /**
         * Return a String representation of this service.
         *
         * @return a String representation of this service.
         */
        public String toString() {
            String attrs = attributes.isEmpty()
                    ? "" : "\r\n  attributes: " + attributes.toString();
            return provider.getName() + ": " + type + "." +
                    " -> " + className + attrs + "\r\n";
        }

    }
}
