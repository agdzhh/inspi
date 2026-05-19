package com.inspi.app.data.repository;

import com.inspi.app.data.local.dao.UserProfileDao;
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
public final class UserRepository_Factory implements Factory<UserRepository> {
  private final Provider<UserProfileDao> daoProvider;

  private final Provider<InspiPreferences> prefsProvider;

  public UserRepository_Factory(Provider<UserProfileDao> daoProvider,
      Provider<InspiPreferences> prefsProvider) {
    this.daoProvider = daoProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public UserRepository get() {
    return newInstance(daoProvider.get(), prefsProvider.get());
  }

  public static UserRepository_Factory create(Provider<UserProfileDao> daoProvider,
      Provider<InspiPreferences> prefsProvider) {
    return new UserRepository_Factory(daoProvider, prefsProvider);
  }

  public static UserRepository newInstance(UserProfileDao dao, InspiPreferences prefs) {
    return new UserRepository(dao, prefs);
  }
}
