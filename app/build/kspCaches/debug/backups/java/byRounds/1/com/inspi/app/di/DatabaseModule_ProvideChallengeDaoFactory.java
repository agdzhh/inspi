package com.inspi.app.di;

import com.inspi.app.data.local.InspiDatabase;
import com.inspi.app.data.local.dao.ChallengeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideChallengeDaoFactory implements Factory<ChallengeDao> {
  private final Provider<InspiDatabase> dbProvider;

  public DatabaseModule_ProvideChallengeDaoFactory(Provider<InspiDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ChallengeDao get() {
    return provideChallengeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideChallengeDaoFactory create(
      Provider<InspiDatabase> dbProvider) {
    return new DatabaseModule_ProvideChallengeDaoFactory(dbProvider);
  }

  public static ChallengeDao provideChallengeDao(InspiDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideChallengeDao(db));
  }
}
