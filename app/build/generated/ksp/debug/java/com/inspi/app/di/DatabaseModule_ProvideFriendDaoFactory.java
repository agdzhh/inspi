package com.inspi.app.di;

import com.inspi.app.data.local.InspiDatabase;
import com.inspi.app.data.local.dao.FriendDao;
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
public final class DatabaseModule_ProvideFriendDaoFactory implements Factory<FriendDao> {
  private final Provider<InspiDatabase> dbProvider;

  public DatabaseModule_ProvideFriendDaoFactory(Provider<InspiDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FriendDao get() {
    return provideFriendDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideFriendDaoFactory create(Provider<InspiDatabase> dbProvider) {
    return new DatabaseModule_ProvideFriendDaoFactory(dbProvider);
  }

  public static FriendDao provideFriendDao(InspiDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFriendDao(db));
  }
}
