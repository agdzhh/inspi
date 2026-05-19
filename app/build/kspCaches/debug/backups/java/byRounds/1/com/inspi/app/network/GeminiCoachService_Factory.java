package com.inspi.app.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GeminiCoachService_Factory implements Factory<GeminiCoachService> {
  private final Provider<String> apiKeyProvider;

  public GeminiCoachService_Factory(Provider<String> apiKeyProvider) {
    this.apiKeyProvider = apiKeyProvider;
  }

  @Override
  public GeminiCoachService get() {
    return newInstance(apiKeyProvider.get());
  }

  public static GeminiCoachService_Factory create(Provider<String> apiKeyProvider) {
    return new GeminiCoachService_Factory(apiKeyProvider);
  }

  public static GeminiCoachService newInstance(String apiKey) {
    return new GeminiCoachService(apiKey);
  }
}
