package com.inspi.app.ui.profile;

import com.inspi.app.data.repository.SubmissionRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<UserRepository> userRepoProvider;

  private final Provider<SubmissionRepository> submissionRepoProvider;

  public ProfileViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider) {
    this.userRepoProvider = userRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(userRepoProvider.get(), submissionRepoProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider) {
    return new ProfileViewModel_Factory(userRepoProvider, submissionRepoProvider);
  }

  public static ProfileViewModel newInstance(UserRepository userRepo,
      SubmissionRepository submissionRepo) {
    return new ProfileViewModel(userRepo, submissionRepo);
  }
}
