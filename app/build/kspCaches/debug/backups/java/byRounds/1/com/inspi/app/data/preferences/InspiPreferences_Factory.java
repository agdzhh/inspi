package com.inspi.app.data.preferences;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class InspiPreferences_Factory implements Factory<InspiPreferences> {
  private final Provider<Context> contextProvider;

  public InspiPreferences_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public InspiPreferences get() {
    return newInstance(contextProvider.get());
  }

  public static InspiPreferences_Factory create(Provider<Context> contextProvider) {
    return new InspiPreferences_Factory(contextProvider);
  }

  public static InspiPreferences newInstance(Context context) {
    return new InspiPreferences(context);
  }
}
