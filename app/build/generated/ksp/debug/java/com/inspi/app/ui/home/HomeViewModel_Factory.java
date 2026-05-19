package com.inspi.app.ui.home;

import com.inspi.app.data.preferences.InspiPreferences;
import com.inspi.app.data.repository.ChallengeRepository;
import com.inspi.app.data.repository.SubmissionRepository;
import com.inspi.app.data.repository.UserRepository;
import com.inspi.app.network.CoachApiService;
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

  private final Provider<SubmissionRepository> submissionRepoProvider;

  private final Provider<CoachApiService> coachApiProvider;

  private final Provider<InspiPreferences> prefsProvider;

  public HomeViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<ChallengeRepository> challengeRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<CoachApiService> coachApiProvider, Provider<InspiPreferences> prefsProvider) {
    this.userRepoProvider = userRepoProvider;
    this.challengeRepoProvider = challengeRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
    this.coachApiProvider = coachApiProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(userRepoProvider.get(), challengeRepoProvider.get(), submissionRepoProvider.get(), coachApiProvider.get(), prefsProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<ChallengeRepository> challengeRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<CoachApiService> coachApiProvider, Provider<InspiPreferences> prefsProvider) {
    return new HomeViewModel_Factory(userRepoProvider, challengeRepoProvider, submissionRepoProvider, coachApiProvider, prefsProvider);
  }

  public static HomeViewModel newInstance(UserRepository userRepo,
      ChallengeRepository challengeRepo, SubmissionRepository submissionRepo,
      CoachApiService coachApi, InspiPreferences prefs) {
    return new HomeViewModel(userRepo, challengeRepo, submissionRepo, coachApi, prefs);
  }
}
