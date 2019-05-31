package com.eurodyn.qlack.fuse.rules.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copied from drools.
 * <p>
 * This just extends Christina from single to multiple classes (map) ...
 */
public class MapBackedClassLoader extends ClassLoader {

    private static final Logger logger = Logger.getLogger(MapBackedClassLoader.class.getName());

    private static final ProtectionDomain PROTECTION_DOMAIN;

    private Map<String, byte[]> store;

    static {
        PROTECTION_DOMAIN = AccessController
            .doPrivileged((PrivilegedAction<ProtectionDomain>) () -> MapBackedClassLoader.class.getProtectionDomain());
    }

    public MapBackedClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
        this.store = new HashMap<>();
    }

    public Map<String, byte[]> getStore() {
        return this.store;
    }

    public void addResource(String className, byte[] bytes) {
        addClass(className, bytes);
    }

    public void addClass(final String className, byte[] bytes) {
        synchronized (this.store) {
            String path = convertResourcePathToClassName(className);
            this.store.put(path, bytes);
        }
    }

    private static String convertResourcePathToClassName(final String pName) {
        return pName.replaceAll(".java$|.class$", "").replace('/', '.');
    }

    public Class<?> fastFindClass(final String name) {
        logger.log(Level.FINER, "Loading class {0}", name);

        final Class<?> clazz = findLoadedClass(name);

        if (clazz == null) {
            byte[] clazzBytes;
            synchronized (this.store) {
                clazzBytes = this.store.get(name);
            }

            if (clazzBytes != null) {
                // new class definition ...
                logger.log(Level.FINER, "Define class {0}", name);
                return defineClass(name, clazzBytes, 0, clazzBytes.length, PROTECTION_DOMAIN);
            }
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(final String name) {
        return fastFindClass(name);
    }

    @Override
    public synchronized Class<?> loadClass(final String name, final boolean resolve)
        throws ClassNotFoundException {
        Class<?> clazz = fastFindClass(name);

        if (clazz == null) {
            ClassLoader parent = this.getParent();
            if (parent != null) {
                clazz = Class.forName(name, true, parent);
            }
        }

        if (resolve) {
            this.resolveClass(clazz);
        }

        return clazz;
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        byte[] bytes;
        synchronized (this.store) {
            String path = convertResourcePathToClassName(name);
            bytes = this.store.get(path);
        }

        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            InputStream input = this.getParent().getResourceAsStream(name);
            if (input == null) {
                input = super.getResourceAsStream(name);
            }
            return input;
        }
    }

    @Override
    public URL getResource(final String name) {
        // override getResource for drools dialect "java" compilation

        byte[] bytes;
        synchronized (this.store) {
            String path = convertResourcePathToClassName(name);
            bytes = this.store.get(path);
        }

        if (bytes != null) {
            return createURL(name, bytes);
        } else {
            URL url = this.getParent().getResource(name);
            if (url == null) {
                url = super.getResource(name);
            }
            return url;
        }
    }

    private URL createURL(final String name, final byte[] bytes) {

        URLStreamHandler handler = new URLStreamHandler() {
            final InputStream stream = new ByteArrayInputStream(bytes);

            @Override
            protected URLConnection openConnection(URL u) {
                return new URLConnection(u) {
                    @Override
                    public void connect() {
                    }

                    @Override
                    public InputStream getInputStream() {
                        return stream;
                    }
                };
            }
        };

        URL url;
        try {
            url = new URL("", "", 0, name, handler);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

}
