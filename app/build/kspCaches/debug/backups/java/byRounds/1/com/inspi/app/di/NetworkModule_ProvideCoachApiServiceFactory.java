package com.inspi.app.di;

import com.inspi.app.network.CoachApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideCoachApiServiceFactory implements Factory<CoachApiService> {
  private final Provider<String> apiKeyProvider;

  public NetworkModule_ProvideCoachApiServiceFactory(Provider<String> apiKeyProvider) {
    this.apiKeyProvider = apiKeyProvider;
  }

  @Override
  public CoachApiService get() {
    return provideCoachApiService(apiKeyProvider.get());
  }

  public static NetworkModule_ProvideCoachApiServiceFactory create(
      Provider<String> apiKeyProvider) {
    return new NetworkModule_ProvideCoachApiServiceFactory(apiKeyProvider);
  }

  public static CoachApiService provideCoachApiService(String apiKey) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideCoachApiService(apiKey));
  }
}
