package com.inspi.app.utils;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.inspi.app.data.preferences.InspiPreferences;
import com.inspi.app.data.repository.UserRepository;
import dagger.internal.DaggerGenerated;
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
public final class DailyReminderWorker_Factory {
  private final Provider<InspiPreferences> prefsProvider;

  private final Provider<UserRepository> userRepoProvider;

  public DailyReminderWorker_Factory(Provider<InspiPreferences> prefsProvider,
      Provider<UserRepository> userRepoProvider) {
    this.prefsProvider = prefsProvider;
    this.userRepoProvider = userRepoProvider;
  }

  public DailyReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, prefsProvider.get(), userRepoProvider.get());
  }

  public static DailyReminderWorker_Factory create(Provider<InspiPreferences> prefsProvider,
      Provider<UserRepository> userRepoProvider) {
    return new DailyReminderWorker_Factory(prefsProvider, userRepoProvider);
  }

  public static DailyReminderWorker newInstance(Context context, WorkerParameters workerParams,
      InspiPreferences prefs, UserRepository userRepo) {
    return new DailyReminderWorker(context, workerParams, prefs, userRepo);
  }
}
