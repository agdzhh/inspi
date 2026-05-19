package com.inspi.app.data.repository;

import com.inspi.app.data.local.dao.FriendDao;
import com.inspi.app.data.preferences.InspiPreferences;
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
public final class FriendRepository_Factory implements Factory<FriendRepository> {
  private final Provider<FriendDao> daoProvider;

  private final Provider<InspiPreferences> prefsProvider;

  public FriendRepository_Factory(Provider<FriendDao> daoProvider,
      Provider<InspiPreferences> prefsProvider) {
    this.daoProvider = daoProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public FriendRepository get() {
    return newInstance(daoProvider.get(), prefsProvider.get());
  }

  public static FriendRepository_Factory create(Provider<FriendDao> daoProvider,
      Provider<InspiPreferences> prefsProvider) {
    return new FriendRepository_Factory(daoProvider, prefsProvider);
  }

  public static FriendRepository newInstance(FriendDao dao, InspiPreferences prefs) {
    return new FriendRepository(dao, prefs);
  }
}
