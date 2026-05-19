package com.inspi.app.ui.friends;

import com.inspi.app.data.repository.FriendRepository;
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
public final class FriendsViewModel_Factory implements Factory<FriendsViewModel> {
  private final Provider<FriendRepository> friendRepoProvider;

  private final Provider<UserRepository> userRepoProvider;

  private final Provider<SubmissionRepository> submissionRepoProvider;

  public FriendsViewModel_Factory(Provider<FriendRepository> friendRepoProvider,
      Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider) {
    this.friendRepoProvider = friendRepoProvider;
    this.userRepoProvider = userRepoProvider;
    this.submissionRepoProvider = submissionRepoProvider;
  }

  @Override
  public FriendsViewModel get() {
    return newInstance(friendRepoProvider.get(), userRepoProvider.get(), submissionRepoProvider.get());
  }

  public static FriendsViewModel_Factory create(Provider<FriendRepository> friendRepoProvider,
      Provider<UserRepository> userRepoProvider,
      Provider<SubmissionRepository> submissionRepoProvider) {
    return new FriendsViewModel_Factory(friendRepoProvider, userRepoProvider, submissionRepoProvider);
  }

  public static FriendsViewModel newInstance(FriendRepository friendRepo, UserRepository userRepo,
      SubmissionRepository submissionRepo) {
    return new FriendsViewModel(friendRepo, userRepo, submissionRepo);
  }
}
