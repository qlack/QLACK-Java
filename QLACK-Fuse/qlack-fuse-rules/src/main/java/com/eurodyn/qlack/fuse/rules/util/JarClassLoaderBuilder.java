package com.eurodyn.qlack.fuse.rules.util;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Copied from drools.
 * <p>
 * This just flattens a list of JAR files to a list of class byte arrays ...
 */
public class JarClassLoaderBuilder {

    private final List<JarInputStream> jarInputStreams;

    private static final Map<String, MapBackedClassLoader> classLoadersMap = new ConcurrentHashMap<>();

    public JarClassLoaderBuilder() {
        this.jarInputStreams = new ArrayList<>();
    }

    public JarClassLoaderBuilder add(byte[] bytes) {
        return add(new ByteArrayInputStream(bytes));
    }

    public JarClassLoaderBuilder add(InputStream stream) {
        try {
            jarInputStreams.add(new JarInputStream(stream, false));
        } catch (IOException e) {
            throw new QRulesException(e);
        }
        return this;
    }

    public List<JarInputStream> getJarInputStreams() {
        return jarInputStreams;
    }

    public MapBackedClassLoader buildClassLoader(String uuid) {
        MapBackedClassLoader mapBackedClassLoader = null;

        if (uuid != null) {
            mapBackedClassLoader = classLoadersMap.get(uuid);
        }

        if (mapBackedClassLoader == null) {
            mapBackedClassLoader = getMapBackedClassLoader();

            try {
                for (JarInputStream jis : jarInputStreams) {
                    JarEntry entry = null;
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((entry = jis.getNextJarEntry()) != null) {
                        if (!entry.isDirectory()
                            && !entry.getName().endsWith(".java")) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            while ((len = jis.read(buf)) >= 0) {
                                out.write(buf, 0, len);
                            }

                            mapBackedClassLoader.addResource(entry.getName(),
                                out.toByteArray());
                        }
                    }

                }
            } catch (IOException e) {
                throw new QRulesException(e);
            }

            if (uuid != null) {
                // keep classloaders ( key-> RuntimeBaseState.uuid, value -> classloader )
                // in order to reuse them
                classLoadersMap.put(uuid, mapBackedClassLoader);
            }
        }

        return mapBackedClassLoader;
    }

    private MapBackedClassLoader getMapBackedClassLoader() {

        return AccessController
            .doPrivileged((PrivilegedAction<MapBackedClassLoader>) () -> {
                ClassLoader parent = getParentClassLoader();
                return new MapBackedClassLoader(parent);
            });

    }

    private ClassLoader getParentClassLoader() {
        // sun.misc.launcher$AppClassLoader -> sun.misc.launcher$ExtClassLoader
        // -> null
        // ClassLoader parent = Thread.currentThread().getContextClassLoader();
        // ClassLoader parent = ClassLoader.getSystemClassLoader();

        // BundleWiringImpl$BundleClassLoaderJava5 (jbpm-runtime) ->
        // java.net.URLClassLoader (karaf) -> sun.misc.launcher$AppClassLoader
        // -> ...
        // org.osgi.framework.wiring.BundleWiring
        // org.apache.felix.framework.BundleWiringImpl
        // BundleClassLoaderJava5 extends BundleClassLoader extends
        // java.security.SecureClassLoader
        ClassLoader parent = org.drools.core.common.ProjectClassLoader.class
            .getClassLoader();

        return parent;
    }

}
