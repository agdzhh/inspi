package com.inspi.app.ui.coach;

import androidx.lifecycle.SavedStateHandle;
import com.inspi.app.data.repository.CoachRepository;
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
public final class CoachViewModel_Factory implements Factory<CoachViewModel> {
  private final Provider<CoachRepository> coachRepoProvider;

  private final Provider<UserRepository> userRepoProvider;

  private final Provider<SubmissionRepository> submissionRepoProvider;

  private final Provider<CoachApiService> coachApiProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public CoachViewModel_Factory(Provider<CoachRepository> coachRepoProvider,
      Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<CoachApiService> coachApiProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.coachRepoProvider = coachRepoProvider;
    this.userRepoProvider = userRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
    this.coachApiProvider = coachApiProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CoachViewModel get() {
    return newInstance(coachRepoProvider.get(), userRepoProvider.get(), submissionRepoProvider.get(), coachApiProvider.get(), savedStateHandleProvider.get());
  }

  public static CoachViewModel_Factory create(Provider<CoachRepository> coachRepoProvider,
      Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<CoachApiService> coachApiProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CoachViewModel_Factory(coachRepoProvider, userRepoProvider, submissionRepoProvider, coachApiProvider, savedStateHandleProvider);
  }

  public static CoachViewModel newInstance(CoachRepository coachRepo, UserRepository userRepo,
      SubmissionRepository submissionRepo, CoachApiService coachApi,
      SavedStateHandle savedStateHandle) {
    return new CoachViewModel(coachRepo, userRepo, submissionRepo, coachApi, savedStateHandle);
  }
}
