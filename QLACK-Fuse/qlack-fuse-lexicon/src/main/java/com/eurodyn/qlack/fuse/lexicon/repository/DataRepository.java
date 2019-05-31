package com.eurodyn.qlack.fuse.lexicon.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends QlackBaseRepository<Data, String> {

  Data findByKeyIdAndLanguageId(String keyId, String languageId);

  Data findByKeyNameAndLanguageId(String keyName, String languageId);

  Data findByKeyIdAndLanguageLocale(String keyId, String locale);

  Data findByKeyNameAndLanguageLocale(String keyName, String locale);

  List<Data> findByKeyGroupIdAndLanguageLocale(String groupId, String locale);
}
