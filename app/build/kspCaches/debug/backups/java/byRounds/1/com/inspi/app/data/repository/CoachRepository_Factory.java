package com.inspi.app.data.repository;

import com.inspi.app.data.local.dao.CoachMessageDao;
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
public final class CoachRepository_Factory implements Factory<CoachRepository> {
  private final Provider<CoachMessageDao> daoProvider;

  public CoachRepository_Factory(Provider<CoachMessageDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public CoachRepository get() {
    return newInstance(daoProvider.get());
  }

  public static CoachRepository_Factory create(Provider<CoachMessageDao> daoProvider) {
    return new CoachRepository_Factory(daoProvider);
  }

  public static CoachRepository newInstance(CoachMessageDao dao) {
    return new CoachRepository(dao);
  }
}
