package com.inspi.app.ui.taskcomplete;

import android.content.Context;
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
public final class TaskCompleteViewModel_Factory implements Factory<TaskCompleteViewModel> {
  private final Provider<UserRepository> userRepoProvider;

  private final Provider<SubmissionRepository> submissionRepoProvider;

  private final Provider<Context> contextProvider;

  public TaskCompleteViewModel_Factory(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider, Provider<Context> contextProvider) {
    this.userRepoProvider = userRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public TaskCompleteViewModel get() {
    return newInstance(userRepoProvider.get(), submissionRepoProvider.get(), contextProvider.get());
  }

  public static TaskCompleteViewModel_Factory create(Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider, Provider<Context> contextProvider) {
    return new TaskCompleteViewModel_Factory(userRepoProvider, submissionRepoProvider, contextProvider);
  }

  public static TaskCompleteViewModel newInstance(UserRepository userRepo,
      SubmissionRepository submissionRepo, Context context) {
    return new TaskCompleteViewModel(userRepo, submissionRepo, context);
  }
}
