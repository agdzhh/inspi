package com.inspi.app.di;

import com.inspi.app.data.local.InspiDatabase;
import com.inspi.app.data.local.dao.CoachMessageDao;
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
public final class DatabaseModule_ProvideCoachMessageDaoFactory implements Factory<CoachMessageDao> {
  private final Provider<InspiDatabase> dbProvider;

  public DatabaseModule_ProvideCoachMessageDaoFactory(Provider<InspiDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CoachMessageDao get() {
    return provideCoachMessageDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCoachMessageDaoFactory create(
      Provider<InspiDatabase> dbProvider) {
    return new DatabaseModule_ProvideCoachMessageDaoFactory(dbProvider);
  }

  public static CoachMessageDao provideCoachMessageDao(InspiDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCoachMessageDao(db));
  }
}
