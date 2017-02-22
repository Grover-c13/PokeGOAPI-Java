package com.pokegoapi.provider;

/**
 * Collection of utility methods to facilitate implementing getInstance()
 * methods in the framework.
 */
public class GetInstance {

    private GetInstance() {
        // empty
    }

    /**
     * Static inner class representing a newly created instance.
     */
    public static final class Instance {
        // public final fields, access directly without accessors
        public final Provider provider;
        public final Object impl;
        private Instance(Provider provider, Object impl) {
            this.provider = provider;
            this.impl = impl;
        }
    }


    public static Provider.Service getService(String type, Provider provider) throws NoSuchTypeException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Provider.Service s = provider.getService(type);
        if (s == null) {
            throw new NoSuchTypeException("no such type: "
                    + type + " for provider " + provider.getName());
        }
        return s;
    }

    /*
     * For all the getInstance() methods below:
     * @param type the type of engine (e.g. MessageDigest)
     * @param clazz the Spi class that the implementation must subclass
     *   (e.g. MessageDigestSpi.class) or null if no superclass check
     *   is required
     * @param algorithm the name of the algorithm (or alias), e.g. MD5
     * @param provider the provider (String or Provider object)
     * @param param the parameter to pass to the Spi constructor
     *   (for CertStores)
     *
     * There are overloaded methods for all the permutations.
     */

    public static Instance getInstance(String type, Class clazz, Provider provider)
            throws NoSuchTypeException {
        return getInstance(getService(type, provider), clazz);
    }

    public static Instance getInstance(String type, Class clazz, Object param, Provider provider)
            throws NoSuchTypeException {
        return getInstance(getService(type, provider), clazz, param);
    }

    /*
     * The two getInstance() methods below take a service. They are
     * intended for classes that cannot use the standard methods, e.g.
     * because they implement delayed provider selection like the
     * Signature class.
     */

    public static Instance getInstance(Provider.Service s, Class clazz)
            throws NoSuchTypeException {
        Object instance = s.newInstance(null);
        checkSuperClass(s, instance.getClass(), clazz);
        return new Instance(s.getProvider(), instance);
    }

    public static Instance getInstance(Provider.Service s, Class clazz, Object param) throws NoSuchTypeException {
        Object instance = s.newInstance(param);
        checkSuperClass(s, instance.getClass(), clazz);
        return new Instance(s.getProvider(), instance);
    }

    /**
     * Check is subClass is a subclass of superClass. If not,
     * throw a NoSuchTypeException.
     */
    public static void checkSuperClass(Provider.Service s, Class subClass,
                                       Class superClass) throws NoSuchTypeException {
        if (superClass == null) {
            return;
        }
        if (!superClass.isAssignableFrom(subClass)) {
            throw new NoSuchTypeException
                    ("class configured for " + s.getType() + ": "
                            + s.getClassName() + " not a " + s.getType());
        }
    }

}
