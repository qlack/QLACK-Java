package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.eurodyn.qlack.util.querydsl.GenericQuerydslBinder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends QlackBaseRepository<Audit, String>, GenericQuerydslBinder<QAudit> {

  void deleteByCreatedOnBefore(Long date);

  @Override
  default void customize(@NonNull QuerydslBindings bindings, @NonNull QAudit audit) {
    // Add generic bindings.
    GenericQuerydslBinder.super.addGenericBindings(bindings);

    // Add specific bindings.
    bindings.bind(audit.createdOn).all((final NumberPath<Long> path, final Collection<? extends Long> values) -> {
      final List<? extends Long> dates = new ArrayList<>(values);
      Collections.sort(dates);
      return dates.size() == 2 ? Optional.of(path.between(dates.get(0), dates.get(1))) : Optional.of(path.eq(dates.get(0)));
    });

    // Exclude fields from filter.
    bindings.excluding(audit.shortDescription);
  }

  default List<String> findDistinctEventsByReferenceId(String referenceId) {
    QAudit qAudit = QAudit.audit;

    Predicate predicate = qAudit.referenceId.eq(referenceId);

    return findAll(predicate, Sort.by("event").ascending()).stream()
      .map(Audit::getEvent)
      .collect(Collectors.toList());
  }
}
