package com.inspi.app.ui.navigation;

import com.inspi.app.data.preferences.InspiPreferences;
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
public final class NavViewModel_Factory implements Factory<NavViewModel> {
  private final Provider<InspiPreferences> prefsProvider;

  public NavViewModel_Factory(Provider<InspiPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public NavViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static NavViewModel_Factory create(Provider<InspiPreferences> prefsProvider) {
    return new NavViewModel_Factory(prefsProvider);
  }

  public static NavViewModel newInstance(InspiPreferences prefs) {
    return new NavViewModel(prefs);
  }
}
