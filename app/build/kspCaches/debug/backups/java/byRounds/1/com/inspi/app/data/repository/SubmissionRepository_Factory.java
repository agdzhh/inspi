package com.inspi.app.data.repository;

import com.inspi.app.data.local.dao.SubmissionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SubmissionRepository_Factory implements Factory<SubmissionRepository> {
  private final Provider<SubmissionDao> daoProvider;

  public SubmissionRepository_Factory(Provider<SubmissionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public SubmissionRepository get() {
    return newInstance(daoProvider.get());
  }

  public static SubmissionRepository_Factory create(Provider<SubmissionDao> daoProvider) {
    return new SubmissionRepository_Factory(daoProvider);
  }

  public static SubmissionRepository newInstance(SubmissionDao dao) {
    return new SubmissionRepository(dao);
  }
}
