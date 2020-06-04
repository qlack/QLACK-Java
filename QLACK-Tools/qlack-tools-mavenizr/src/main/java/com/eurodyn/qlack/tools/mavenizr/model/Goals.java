package com.eurodyn.qlack.tools.mavenizr.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * The Goals class. Marshalled to {@code <goals></goals>}
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Data
@RequiredArgsConstructor
public class Goals {
    private String goal = "install-file";
}