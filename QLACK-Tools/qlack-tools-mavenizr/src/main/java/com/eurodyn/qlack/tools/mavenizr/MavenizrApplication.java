package com.eurodyn.qlack.tools.mavenizr;

import com.eurodyn.qlack.tools.mavenizr.service.DependencyService;
import com.eurodyn.qlack.tools.mavenizr.util.JAXBMarshalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;

/**
 * Command-line tool that helps to minimize the time needed to install local jar dependencies
 * to your local maven repository and use them directly in your maven project pom file
 *
 * @author EUROPEAN DYNAMICS SA.
 */
@Slf4j
@SpringBootApplication
public class MavenizrApplication implements ApplicationRunner {

    String libdir;
    String confdir;
    String groupId;
    Resource[] files;

    @Autowired
    DependencyService dependencyService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JAXBMarshalUtil jaxbMarshalUtil;

    @Autowired
    private ConfigurableApplicationContext ctx;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MavenizrApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (validateArgsInput(args)) {
            try {
                files = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("file:".concat(libdir).concat("/*.jar"));
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            for (String option : args.getOptionNames()) {
                switch (option) {

                    case "exec":
                        jaxbMarshalUtil.marshal(dependencyService.getExecutions(files, groupId, confdir));
                        break;

                    case "deps":
                        jaxbMarshalUtil.marshal(dependencyService.getDependencies(files, groupId));
                        break;
                }
            }
        }
        exit();
    }

    private void initArgsValues(ApplicationArguments args) {
        libdir = checkOptionAndGetValue(args, "d");
        groupId = checkOptionAndGetValue(args, "g");
        if (args.containsOption("exec")) {
            confdir = checkOptionAndGetValue(args, "c");
        }
    }

    private String checkOptionAndGetValue(ApplicationArguments args, String optionName) {
        if (args.getOptionValues(optionName) != null && args.getOptionValues(optionName).isEmpty()) {
            throw new RuntimeException("Option --" + optionName + " has no value. Please run with --u to see usage information.");
        }
        return args.getOptionValues(optionName).get(0);
    }

    private boolean validateArgsInput(ApplicationArguments args) {
        if (args.getSourceArgs().length == 0) {
            printNoArgumentFoundWarning();
            return false;
        } else if (args.getSourceArgs().length == 1 && args.containsOption("u")) {
            printUsageInformation();
            return false;
        } else if (requiredParametersAbsent(args)) {
            log.error("A required argument was not found. Please run with --u to see usage information.");
            return false;
        } else {
            if (requiredExecutionOptionsAbsent(args)) {
                log.error("No execution option provided. Please run with --u to see usage information.");
                return false;
            }

            initArgsValues(args);
            return true;
        }

    }

    private boolean requiredParametersAbsent(ApplicationArguments args) {
        return !args.containsOption("d") || (args.containsOption("exec") && !args.containsOption("c")) || !args.containsOption("g");
    }

    private boolean requiredExecutionOptionsAbsent(ApplicationArguments args) {
        return !args.containsOption("exec") && !args.containsOption("deps");
    }

    private void printNoArgumentFoundWarning() {
        log.info("\n");
        log.info("___________________________________________________________");
        log.error("No arguments found.");
        log.info("___________________________________________________________");
    }

    private void printUsageInformation() {
        log.info("\n");
        log.info("___________________________________________________________");
        log.info("Usage");
        log.info("___________________________________________________________");
        log.info("Parameters:");
        log.info(" --d\t the jar libraries directory (required)");
        log.info(" --c\t the execution configuration file directory, i.e. 'src/main/resources/lib/' (required for --exec option)");
        log.info(" --g\t the default groupId for the created dependency/execution nodes (required)");
        log.info("___________________________________________________________");
        log.info("Execution options:");
        log.info(" --deps\t generates dependency nodes");
        log.info(" --exec\t generates execution nodes");
        log.info(" --u\t usage information");
        log.info("___________________________________________________________");
    }

    private void exit() {
        log.info("Exiting..");
        ctx.close();
        System.exit(0);
    }

}
