package com.inspi.app.ui.hobbyselection;

import com.inspi.app.data.preferences.InspiPreferences;
import com.inspi.app.data.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class HobbySelectionViewModel_Factory implements Factory<HobbySelectionViewModel> {
  private final Provider<UserRepository> userRepoProvider;

  private final Provider<InspiPreferences> prefsProvider;

  public HobbySelectionViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<InspiPreferences> prefsProvider) {
    this.userRepoProvider = userRepoProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public HobbySelectionViewModel get() {
    return newInstance(userRepoProvider.get(), prefsProvider.get());
  }

  public static HobbySelectionViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<InspiPreferences> prefsProvider) {
    return new HobbySelectionViewModel_Factory(userRepoProvider, prefsProvider);
  }

  public static HobbySelectionViewModel newInstance(UserRepository userRepo,
      InspiPreferences prefs) {
    return new HobbySelectionViewModel(userRepo, prefs);
  }
}
