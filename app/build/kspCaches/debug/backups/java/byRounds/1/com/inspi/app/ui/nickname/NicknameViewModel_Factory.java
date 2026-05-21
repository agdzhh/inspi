package com.inspi.app.ui.nickname;

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
public final class NicknameViewModel_Factory implements Factory<NicknameViewModel> {
  private final Provider<InspiPreferences> prefsProvider;

  public NicknameViewModel_Factory(Provider<InspiPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public NicknameViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static NicknameViewModel_Factory create(Provider<InspiPreferences> prefsProvider) {
    return new NicknameViewModel_Factory(prefsProvider);
  }

  public static NicknameViewModel newInstance(InspiPreferences prefs) {
    return new NicknameViewModel(prefs);
  }
}
