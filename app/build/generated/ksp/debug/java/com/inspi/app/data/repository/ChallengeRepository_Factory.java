package com.inspi.app.data.repository;

import com.inspi.app.data.local.dao.ChallengeDao;
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
public final class ChallengeRepository_Factory implements Factory<ChallengeRepository> {
  private final Provider<ChallengeDao> daoProvider;

  public ChallengeRepository_Factory(Provider<ChallengeDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ChallengeRepository get() {
    return newInstance(daoProvider.get());
  }

  public static ChallengeRepository_Factory create(Provider<ChallengeDao> daoProvider) {
    return new ChallengeRepository_Factory(daoProvider);
  }

  public static ChallengeRepository newInstance(ChallengeDao dao) {
    return new ChallengeRepository(dao);
  }
}
