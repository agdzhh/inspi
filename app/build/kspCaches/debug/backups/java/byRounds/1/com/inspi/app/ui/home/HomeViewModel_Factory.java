package com.inspi.app.ui.home;

import com.inspi.app.data.repository.ChallengeRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<UserRepository> userRepoProvider;

  private final Provider<ChallengeRepository> challengeRepoProvider;

  public HomeViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<ChallengeRepository> challengeRepoProvider) {
    this.userRepoProvider = userRepoProvider;
    this.challengeRepoProvider = challengeRepoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(userRepoProvider.get(), challengeRepoProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<ChallengeRepository> challengeRepoProvider) {
    return new HomeViewModel_Factory(userRepoProvider, challengeRepoProvider);
  }

  public static HomeViewModel newInstance(UserRepository userRepo,
      ChallengeRepository challengeRepo) {
    return new HomeViewModel(userRepo, challengeRepo);
  }
}
