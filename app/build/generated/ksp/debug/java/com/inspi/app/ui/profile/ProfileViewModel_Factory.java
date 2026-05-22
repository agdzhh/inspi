package com.inspi.app.ui.profile;

import android.content.Context;
import com.inspi.app.data.preferences.InspiPreferences;
import com.inspi.app.data.repository.SubmissionRepository;
import com.inspi.app.data.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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

  private final Provider<InspiPreferences> prefsProvider;

  private final Provider<Context> contextProvider;

  public ProfileViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<InspiPreferences> prefsProvider, Provider<Context> contextProvider) {
    this.userRepoProvider = userRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
    this.prefsProvider = prefsProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(userRepoProvider.get(), submissionRepoProvider.get(), prefsProvider.get(), contextProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider,
      Provider<InspiPreferences> prefsProvider, Provider<Context> contextProvider) {
    return new ProfileViewModel_Factory(userRepoProvider, submissionRepoProvider, prefsProvider, contextProvider);
  }

  public static ProfileViewModel newInstance(UserRepository userRepo,
      SubmissionRepository submissionRepo, InspiPreferences prefs, Context context) {
    return new ProfileViewModel(userRepo, submissionRepo, prefs, context);
  }
}
