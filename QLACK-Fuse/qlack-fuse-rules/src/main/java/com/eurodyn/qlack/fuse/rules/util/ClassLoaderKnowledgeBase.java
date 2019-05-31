package com.eurodyn.qlack.fuse.rules.util;

import org.kie.api.KieBase;

public class ClassLoaderKnowledgeBase {

    public final MapBackedClassLoader classLoader;

    public final KieBase knowledgeBase;

    public ClassLoaderKnowledgeBase(MapBackedClassLoader classLoader, KieBase knowledgeBase) {
        this.classLoader = classLoader;
        this.knowledgeBase = knowledgeBase;
    }

}
