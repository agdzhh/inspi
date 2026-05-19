package com.inspi.app.di;

import com.inspi.app.data.local.InspiDatabase;
import com.inspi.app.data.local.dao.SubmissionDao;
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
public final class DatabaseModule_ProvideSubmissionDaoFactory implements Factory<SubmissionDao> {
  private final Provider<InspiDatabase> dbProvider;

  public DatabaseModule_ProvideSubmissionDaoFactory(Provider<InspiDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SubmissionDao get() {
    return provideSubmissionDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideSubmissionDaoFactory create(
      Provider<InspiDatabase> dbProvider) {
    return new DatabaseModule_ProvideSubmissionDaoFactory(dbProvider);
  }

  public static SubmissionDao provideSubmissionDao(InspiDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSubmissionDao(db));
  }
}
