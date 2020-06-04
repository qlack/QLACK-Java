package com.eurodyn.qlack.tools.mavenizr.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Configurations class. Marshalled to {@code <configuration></configuration>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@XmlRootElement(name = "configuration")
public class Configuration {
    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;
    private String file;
}
