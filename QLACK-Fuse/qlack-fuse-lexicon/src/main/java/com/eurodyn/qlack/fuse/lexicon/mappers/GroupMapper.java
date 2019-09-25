package com.eurodyn.qlack.fuse.lexicon.mappers;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import org.mapstruct.Mapper;

/** An interface Mapper tha is used to map keys to values.
 * @author European Dynamics
 */
@Mapper(componentModel = "spring")
public interface GroupMapper extends LexiconMapper<Group, GroupDTO> {

}
