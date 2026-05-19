package com.inspi.app.ui.hobbyselection;

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

  public HobbySelectionViewModel_Factory(Provider<UserRepository> userRepoProvider) {
    this.userRepoProvider = userRepoProvider;
  }

  @Override
  public HobbySelectionViewModel get() {
    return newInstance(userRepoProvider.get());
  }

  public static HobbySelectionViewModel_Factory create(Provider<UserRepository> userRepoProvider) {
    return new HobbySelectionViewModel_Factory(userRepoProvider);
  }

  public static HobbySelectionViewModel newInstance(UserRepository userRepo) {
    return new HobbySelectionViewModel(userRepo);
  }
}
