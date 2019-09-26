package com.eurodyn.qlack.fuse.scheduler.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Properties class for qlack-fuse-scheduler module.
 *
 * @author European Dynamics SA.
 */
@Configuration
@ConfigurationProperties(prefix = "qlack.fuse.scheduler")
@PropertySource("qlack.fuse.scheduler.application.properties")
public class SchedulerProperties {

}
