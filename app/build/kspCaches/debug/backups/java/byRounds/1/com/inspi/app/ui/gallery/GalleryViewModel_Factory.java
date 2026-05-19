package com.inspi.app.ui.gallery;

import com.inspi.app.data.repository.SubmissionRepository;
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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<SubmissionRepository> submissionRepoProvider;

  public GalleryViewModel_Factory(Provider<SubmissionRepository> submissionRepoProvider) {
    this.submissionRepoProvider = submissionRepoProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(submissionRepoProvider.get());
  }

  public static GalleryViewModel_Factory create(
      Provider<SubmissionRepository> submissionRepoProvider) {
    return new GalleryViewModel_Factory(submissionRepoProvider);
  }

  public static GalleryViewModel newInstance(SubmissionRepository submissionRepo) {
    return new GalleryViewModel(submissionRepo);
  }
}
