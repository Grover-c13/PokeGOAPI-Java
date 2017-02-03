package com.pokegoapi;

import java.util.List;

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
        // Return Provider and implementation as an array as used in the
        // old Security.getImpl() methods.
        public Object[] toArray() {
            return new Object[] {impl, provider};
        }
    }

    public static Provider.Service getService(String type)  throws NoSuchTypeException {
        //TODO: implement default
//        ProviderList list = Providers.getProviderList();
//        Provider.Service s = list.getService(type);
//        if (s == null) {
//            throw new NoSuchTypeException
//                    (type + " not available");
//        }
        return null;
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

    public static Instance getInstance(String type, Class clazz) throws NoSuchTypeException {
        //TODO: implement default
//        // in the almost all cases, the first service will work
//        // avoid taking long path if so
//        ProviderList list = Providers.getProviderList();
//        Provider.Service firstService = list.getService(type, algorithm);
//        if (firstService == null) {
//            throw new NoSuchTypeException
//                    (algorithm + " " + type + " not available");
//        }
//        NoSuchTypeException failure;
//        try {
//            return getInstance(firstService, clazz);
//        } catch (NoSuchTypeException e) {
//            failure = e;
//        }
//        // if we cannot get the service from the prefered provider,
//        // fail over to the next
//        for (Provider.Service s : list.getServices(type, algorithm)) {
//            if (s == firstService) {
//                // do not retry initial failed service
//                continue;
//            }
//            try {
//                return getInstance(s, clazz);
//            } catch (NoSuchTypeException e) {
//                failure = e;
//            }
//        }
//        throw failure;
        return null;
    }

    public static Instance getInstance(String type, Class clazz, Object param) throws NoSuchTypeException {
        Provider.Service services = getService(type);
        NoSuchTypeException failure = null;
        try {
            return getInstance(services, clazz, param);
        } catch (NoSuchTypeException e) {
            failure = e;
        }

        if (failure != null) {
            throw failure;
        } else {
            throw new NoSuchTypeException(type + " not available");
        }
    }

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
