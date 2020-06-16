package com.eurodyn.qlack.fuse.rules.service;

import java.util.List;
import java.util.Map;

/**
 * This service provides API methods related to the rules.
 *
 * @author European Dynamics
 */
public interface RuleService<T> {

    Object findById(String id);

    List<T> getAll();

    Object executeRules(String resourceId, List<byte[]> inputLibraries,
                        List<String> rules, Map<String, byte[]> inputGlobals,
                        List<byte[]> inputs, String toBeExecuted);

    Object executeRules(String resourceId, List<String> rules,
                        List<byte[]> inputs, String toBeExecuted);
}
