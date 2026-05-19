package com.inspi.app;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class InspyApplication_MembersInjector implements MembersInjector<InspyApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public InspyApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<InspyApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new InspyApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(InspyApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.inspi.app.InspyApplication.workerFactory")
  public static void injectWorkerFactory(InspyApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
