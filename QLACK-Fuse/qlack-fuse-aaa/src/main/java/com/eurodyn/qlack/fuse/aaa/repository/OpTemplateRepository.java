package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface written for OpTemplate.The usage of it to define a number of abstract crud
 * methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface OpTemplateRepository extends AAARepository<OpTemplate, String> {

  /**
   * A method to retrieve the name of OpTemplate
   *
   * @param name the name
   * @return the name of OpTemplate
   */
  OpTemplate findByName(String name);

}
