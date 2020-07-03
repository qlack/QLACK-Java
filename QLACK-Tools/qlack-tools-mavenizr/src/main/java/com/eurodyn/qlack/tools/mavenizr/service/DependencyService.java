package com.eurodyn.qlack.tools.mavenizr.service;

import com.eurodyn.qlack.tools.mavenizr.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * Provides services of generating sections of a pom file concerning dependencies
 * and local files installation.
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Slf4j
@Service
public class DependencyService {

    private static final String JAR_EXT = ".jar";
    private static final String JAR = "jar";
    private static final String SLASH = "/";

    /**
     * Generates {@link Dependency} definition nodes for pom file using the files and groupId provided.
     * The artifactId is created by the filename and the version is a hash of the file.
     *
     * @param files the jar files
     * @param groupId the groupId of the generated dependency definitions
     * @return a {@link Dependencies} containing a collection of {@link Dependency} objects
     */
    public Dependencies getDependencies(Resource[] files, String groupId) {

        Dependencies dependencies = new Dependencies();
        try {

            for (Resource file : files) {

                String filename = Objects.requireNonNull(file.getFilename()).substring(0, file.getFilename().lastIndexOf(JAR_EXT));
                String version = DigestUtils.sha256Hex(file.getInputStream());

                Dependency dep = new Dependency()
                        .setGroupId(groupId)
                        .setArtifactId(filename)
                        .setVersion(version);

                dependencies.getDependencies().add(dep);
            }
        } catch (IOException e) {
            log.error("An error occurred during a file I/O operation: {}", e.toString());
        }

        return dependencies;
    }

    /**
     * Generates {@link Execution} definition nodes for pom file using the files and groupId provided.
     * The artifactId is created by the filename and the version is a hash of the file.
     * @param files the jar files
     * @param groupId the groupId of the generated dependency definitions
     * @param confdir the directory of the jar files, usually relative to the project pom file
     * @return a {@link Executions} containing a collection of {@link Execution} objects
     */
    public Executions getExecutions(Resource[] files, String groupId, String confdir) {

        Executions executions = new Executions();
        try {

            for (Resource file : files) {

                String filename = Objects.requireNonNull(file.getFilename()).substring(0, file.getFilename().lastIndexOf(JAR_EXT));
                String version = DigestUtils.sha256Hex(file.getInputStream());

                Configuration conf = new Configuration()
                        .setFile(confdir.concat(SLASH).concat(file.getFilename()))
                        .setGroupId(groupId)
                        .setArtifactId(filename)
                        .setVersion(version)
                        .setPackaging(JAR);

                Execution ex = new Execution()
                        .setId(file.getFilename())
                        .setConfiguration(conf);

                executions.getExecutions().add(ex);
            }
        } catch (IOException e) {
            log.error("An error occurred during a file I/O operation: {}", e.toString());
        }

        return executions;
    }
}
