package com.inspi.app.ui.coach;

import com.inspi.app.data.repository.CoachRepository;
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
public final class CoachViewModel_Factory implements Factory<CoachViewModel> {
  private final Provider<CoachRepository> coachRepoProvider;

  private final Provider<UserRepository> userRepoProvider;

  private final Provider<CoachApiService> coachApiProvider;

  public CoachViewModel_Factory(Provider<CoachRepository> coachRepoProvider,
      Provider<UserRepository> userRepoProvider, Provider<CoachApiService> coachApiProvider) {
    this.coachRepoProvider = coachRepoProvider;
    this.userRepoProvider = userRepoProvider;
    this.coachApiProvider = coachApiProvider;
  }

  @Override
  public CoachViewModel get() {
    return newInstance(coachRepoProvider.get(), userRepoProvider.get(), coachApiProvider.get());
  }

  public static CoachViewModel_Factory create(Provider<CoachRepository> coachRepoProvider,
      Provider<UserRepository> userRepoProvider, Provider<CoachApiService> coachApiProvider) {
    return new CoachViewModel_Factory(coachRepoProvider, userRepoProvider, coachApiProvider);
  }

  public static CoachViewModel newInstance(CoachRepository coachRepo, UserRepository userRepo,
      CoachApiService coachApi) {
    return new CoachViewModel(coachRepo, userRepo, coachApi);
  }
}
