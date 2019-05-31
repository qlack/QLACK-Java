package com.eurodyn.qlack.fuse.settings.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends QlackBaseRepository<Setting, String> {

  @Override
  @NonNull
  List<Setting> findAll(@NonNull Predicate predicate);
}
