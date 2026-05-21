package com.inspi.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.inspi.app.data.local.InspiDatabase;
import com.inspi.app.data.local.dao.ChallengeDao;
import com.inspi.app.data.local.dao.CoachMessageDao;
import com.inspi.app.data.local.dao.FriendDao;
import com.inspi.app.data.local.dao.SubmissionDao;
import com.inspi.app.data.local.dao.UserProfileDao;
import com.inspi.app.data.preferences.InspiPreferences;
import com.inspi.app.data.repository.ChallengeRepository;
import com.inspi.app.data.repository.CoachRepository;
import com.inspi.app.data.repository.FriendRepository;
import com.inspi.app.data.repository.SubmissionRepository;
import com.inspi.app.data.repository.UserRepository;
import com.inspi.app.di.DatabaseModule_ProvideChallengeDaoFactory;
import com.inspi.app.di.DatabaseModule_ProvideCoachMessageDaoFactory;
import com.inspi.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.inspi.app.di.DatabaseModule_ProvideFriendDaoFactory;
import com.inspi.app.di.DatabaseModule_ProvideSubmissionDaoFactory;
import com.inspi.app.di.DatabaseModule_ProvideUserProfileDaoFactory;
import com.inspi.app.di.NetworkModule_ProvideCoachApiServiceFactory;
import com.inspi.app.di.NetworkModule_ProvideGeminiApiKeyFactory;
import com.inspi.app.network.CoachApiService;
import com.inspi.app.ui.coach.CoachViewModel;
import com.inspi.app.ui.coach.CoachViewModel_HiltModules;
import com.inspi.app.ui.friends.FriendsViewModel;
import com.inspi.app.ui.friends.FriendsViewModel_HiltModules;
import com.inspi.app.ui.gallery.GalleryViewModel;
import com.inspi.app.ui.gallery.GalleryViewModel_HiltModules;
import com.inspi.app.ui.hobbyselection.HobbySelectionViewModel;
import com.inspi.app.ui.hobbyselection.HobbySelectionViewModel_HiltModules;
import com.inspi.app.ui.home.HomeViewModel;
import com.inspi.app.ui.home.HomeViewModel_HiltModules;
import com.inspi.app.ui.navigation.NavViewModel;
import com.inspi.app.ui.navigation.NavViewModel_HiltModules;
import com.inspi.app.ui.nickname.NicknameViewModel;
import com.inspi.app.ui.nickname.NicknameViewModel_HiltModules;
import com.inspi.app.ui.onboarding.OnboardingViewModel;
import com.inspi.app.ui.onboarding.OnboardingViewModel_HiltModules;
import com.inspi.app.ui.profile.ProfileViewModel;
import com.inspi.app.ui.profile.ProfileViewModel_HiltModules;
import com.inspi.app.ui.taskcomplete.TaskCompleteViewModel;
import com.inspi.app.ui.taskcomplete.TaskCompleteViewModel_HiltModules;
import com.inspi.app.utils.DailyReminderWorker;
import com.inspi.app.utils.DailyReminderWorker_AssistedFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerInspyApplication_HiltComponents_SingletonC {
  private DaggerInspyApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public InspyApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements InspyApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements InspyApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements InspyApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements InspyApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements InspyApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements InspyApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements InspyApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public InspyApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends InspyApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends InspyApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends InspyApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends InspyApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(LazyClassKeyProvider.com_inspi_app_ui_coach_CoachViewModel, CoachViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_friends_FriendsViewModel, FriendsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_gallery_GalleryViewModel, GalleryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_hobbyselection_HobbySelectionViewModel, HobbySelectionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_navigation_NavViewModel, NavViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_nickname_NicknameViewModel, NicknameViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_profile_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_inspi_app_ui_taskcomplete_TaskCompleteViewModel, TaskCompleteViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_inspi_app_ui_friends_FriendsViewModel = "com.inspi.app.ui.friends.FriendsViewModel";

      static String com_inspi_app_ui_taskcomplete_TaskCompleteViewModel = "com.inspi.app.ui.taskcomplete.TaskCompleteViewModel";

      static String com_inspi_app_ui_home_HomeViewModel = "com.inspi.app.ui.home.HomeViewModel";

      static String com_inspi_app_ui_navigation_NavViewModel = "com.inspi.app.ui.navigation.NavViewModel";

      static String com_inspi_app_ui_coach_CoachViewModel = "com.inspi.app.ui.coach.CoachViewModel";

      static String com_inspi_app_ui_hobbyselection_HobbySelectionViewModel = "com.inspi.app.ui.hobbyselection.HobbySelectionViewModel";

      static String com_inspi_app_ui_onboarding_OnboardingViewModel = "com.inspi.app.ui.onboarding.OnboardingViewModel";

      static String com_inspi_app_ui_gallery_GalleryViewModel = "com.inspi.app.ui.gallery.GalleryViewModel";

      static String com_inspi_app_ui_nickname_NicknameViewModel = "com.inspi.app.ui.nickname.NicknameViewModel";

      static String com_inspi_app_ui_profile_ProfileViewModel = "com.inspi.app.ui.profile.ProfileViewModel";

      @KeepFieldType
      FriendsViewModel com_inspi_app_ui_friends_FriendsViewModel2;

      @KeepFieldType
      TaskCompleteViewModel com_inspi_app_ui_taskcomplete_TaskCompleteViewModel2;

      @KeepFieldType
      HomeViewModel com_inspi_app_ui_home_HomeViewModel2;

      @KeepFieldType
      NavViewModel com_inspi_app_ui_navigation_NavViewModel2;

      @KeepFieldType
      CoachViewModel com_inspi_app_ui_coach_CoachViewModel2;

      @KeepFieldType
      HobbySelectionViewModel com_inspi_app_ui_hobbyselection_HobbySelectionViewModel2;

      @KeepFieldType
      OnboardingViewModel com_inspi_app_ui_onboarding_OnboardingViewModel2;

      @KeepFieldType
      GalleryViewModel com_inspi_app_ui_gallery_GalleryViewModel2;

      @KeepFieldType
      NicknameViewModel com_inspi_app_ui_nickname_NicknameViewModel2;

      @KeepFieldType
      ProfileViewModel com_inspi_app_ui_profile_ProfileViewModel2;
    }
  }

  private static final class ViewModelCImpl extends InspyApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CoachViewModel> coachViewModelProvider;

    private Provider<FriendsViewModel> friendsViewModelProvider;

    private Provider<GalleryViewModel> galleryViewModelProvider;

    private Provider<HobbySelectionViewModel> hobbySelectionViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<NavViewModel> navViewModelProvider;

    private Provider<NicknameViewModel> nicknameViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<TaskCompleteViewModel> taskCompleteViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.coachViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.friendsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.galleryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.hobbySelectionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.navViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.nicknameViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.taskCompleteViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(LazyClassKeyProvider.com_inspi_app_ui_coach_CoachViewModel, ((Provider) coachViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_friends_FriendsViewModel, ((Provider) friendsViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_gallery_GalleryViewModel, ((Provider) galleryViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_hobbyselection_HobbySelectionViewModel, ((Provider) hobbySelectionViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_navigation_NavViewModel, ((Provider) navViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_nickname_NicknameViewModel, ((Provider) nicknameViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_profile_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_inspi_app_ui_taskcomplete_TaskCompleteViewModel, ((Provider) taskCompleteViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_inspi_app_ui_gallery_GalleryViewModel = "com.inspi.app.ui.gallery.GalleryViewModel";

      static String com_inspi_app_ui_navigation_NavViewModel = "com.inspi.app.ui.navigation.NavViewModel";

      static String com_inspi_app_ui_onboarding_OnboardingViewModel = "com.inspi.app.ui.onboarding.OnboardingViewModel";

      static String com_inspi_app_ui_nickname_NicknameViewModel = "com.inspi.app.ui.nickname.NicknameViewModel";

      static String com_inspi_app_ui_home_HomeViewModel = "com.inspi.app.ui.home.HomeViewModel";

      static String com_inspi_app_ui_profile_ProfileViewModel = "com.inspi.app.ui.profile.ProfileViewModel";

      static String com_inspi_app_ui_friends_FriendsViewModel = "com.inspi.app.ui.friends.FriendsViewModel";

      static String com_inspi_app_ui_hobbyselection_HobbySelectionViewModel = "com.inspi.app.ui.hobbyselection.HobbySelectionViewModel";

      static String com_inspi_app_ui_taskcomplete_TaskCompleteViewModel = "com.inspi.app.ui.taskcomplete.TaskCompleteViewModel";

      static String com_inspi_app_ui_coach_CoachViewModel = "com.inspi.app.ui.coach.CoachViewModel";

      @KeepFieldType
      GalleryViewModel com_inspi_app_ui_gallery_GalleryViewModel2;

      @KeepFieldType
      NavViewModel com_inspi_app_ui_navigation_NavViewModel2;

      @KeepFieldType
      OnboardingViewModel com_inspi_app_ui_onboarding_OnboardingViewModel2;

      @KeepFieldType
      NicknameViewModel com_inspi_app_ui_nickname_NicknameViewModel2;

      @KeepFieldType
      HomeViewModel com_inspi_app_ui_home_HomeViewModel2;

      @KeepFieldType
      ProfileViewModel com_inspi_app_ui_profile_ProfileViewModel2;

      @KeepFieldType
      FriendsViewModel com_inspi_app_ui_friends_FriendsViewModel2;

      @KeepFieldType
      HobbySelectionViewModel com_inspi_app_ui_hobbyselection_HobbySelectionViewModel2;

      @KeepFieldType
      TaskCompleteViewModel com_inspi_app_ui_taskcomplete_TaskCompleteViewModel2;

      @KeepFieldType
      CoachViewModel com_inspi_app_ui_coach_CoachViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.inspi.app.ui.coach.CoachViewModel 
          return (T) new CoachViewModel(singletonCImpl.coachRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.submissionRepositoryProvider.get(), singletonCImpl.provideCoachApiServiceProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.inspi.app.ui.friends.FriendsViewModel 
          return (T) new FriendsViewModel(singletonCImpl.friendRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.submissionRepositoryProvider.get());

          case 2: // com.inspi.app.ui.gallery.GalleryViewModel 
          return (T) new GalleryViewModel(singletonCImpl.submissionRepositoryProvider.get());

          case 3: // com.inspi.app.ui.hobbyselection.HobbySelectionViewModel 
          return (T) new HobbySelectionViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.inspiPreferencesProvider.get());

          case 4: // com.inspi.app.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.challengeRepositoryProvider.get(), singletonCImpl.submissionRepositoryProvider.get(), singletonCImpl.provideCoachApiServiceProvider.get(), singletonCImpl.inspiPreferencesProvider.get());

          case 5: // com.inspi.app.ui.navigation.NavViewModel 
          return (T) new NavViewModel(singletonCImpl.inspiPreferencesProvider.get());

          case 6: // com.inspi.app.ui.nickname.NicknameViewModel 
          return (T) new NicknameViewModel(singletonCImpl.inspiPreferencesProvider.get());

          case 7: // com.inspi.app.ui.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.inspiPreferencesProvider.get());

          case 8: // com.inspi.app.ui.profile.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.submissionRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.inspi.app.ui.taskcomplete.TaskCompleteViewModel 
          return (T) new TaskCompleteViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.submissionRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.savedStateHandle);

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends InspyApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends InspyApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends InspyApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<InspiPreferences> inspiPreferencesProvider;

    private Provider<InspiDatabase> provideDatabaseProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private Provider<DailyReminderWorker_AssistedFactory> dailyReminderWorker_AssistedFactoryProvider;

    private Provider<CoachRepository> coachRepositoryProvider;

    private Provider<SubmissionRepository> submissionRepositoryProvider;

    private Provider<String> provideGeminiApiKeyProvider;

    private Provider<CoachApiService> provideCoachApiServiceProvider;

    private Provider<FriendRepository> friendRepositoryProvider;

    private Provider<ChallengeRepository> challengeRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private UserProfileDao userProfileDao() {
      return DatabaseModule_ProvideUserProfileDaoFactory.provideUserProfileDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.inspi.app.utils.DailyReminderWorker", ((Provider) dailyReminderWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private CoachMessageDao coachMessageDao() {
      return DatabaseModule_ProvideCoachMessageDaoFactory.provideCoachMessageDao(provideDatabaseProvider.get());
    }

    private SubmissionDao submissionDao() {
      return DatabaseModule_ProvideSubmissionDaoFactory.provideSubmissionDao(provideDatabaseProvider.get());
    }

    private FriendDao friendDao() {
      return DatabaseModule_ProvideFriendDaoFactory.provideFriendDao(provideDatabaseProvider.get());
    }

    private ChallengeDao challengeDao() {
      return DatabaseModule_ProvideChallengeDaoFactory.provideChallengeDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.inspiPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<InspiPreferences>(singletonCImpl, 1));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<InspiDatabase>(singletonCImpl, 3));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 2));
      this.dailyReminderWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<DailyReminderWorker_AssistedFactory>(singletonCImpl, 0));
      this.coachRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CoachRepository>(singletonCImpl, 4));
      this.submissionRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SubmissionRepository>(singletonCImpl, 5));
      this.provideGeminiApiKeyProvider = DoubleCheck.provider(new SwitchingProvider<String>(singletonCImpl, 7));
      this.provideCoachApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<CoachApiService>(singletonCImpl, 6));
      this.friendRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FriendRepository>(singletonCImpl, 8));
      this.challengeRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChallengeRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectInspyApplication(InspyApplication inspyApplication) {
      injectInspyApplication2(inspyApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private InspyApplication injectInspyApplication2(InspyApplication instance) {
      InspyApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.inspi.app.utils.DailyReminderWorker_AssistedFactory 
          return (T) new DailyReminderWorker_AssistedFactory() {
            @Override
            public DailyReminderWorker create(Context context, WorkerParameters workerParams) {
              return new DailyReminderWorker(context, workerParams, singletonCImpl.inspiPreferencesProvider.get(), singletonCImpl.userRepositoryProvider.get());
            }
          };

          case 1: // com.inspi.app.data.preferences.InspiPreferences 
          return (T) new InspiPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.inspi.app.data.repository.UserRepository 
          return (T) new UserRepository(singletonCImpl.userProfileDao(), singletonCImpl.inspiPreferencesProvider.get());

          case 3: // com.inspi.app.data.local.InspiDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.inspi.app.data.repository.CoachRepository 
          return (T) new CoachRepository(singletonCImpl.coachMessageDao());

          case 5: // com.inspi.app.data.repository.SubmissionRepository 
          return (T) new SubmissionRepository(singletonCImpl.submissionDao());

          case 6: // com.inspi.app.network.CoachApiService 
          return (T) NetworkModule_ProvideCoachApiServiceFactory.provideCoachApiService(singletonCImpl.provideGeminiApiKeyProvider.get());

          case 7: // java.lang.String 
          return (T) NetworkModule_ProvideGeminiApiKeyFactory.provideGeminiApiKey();

          case 8: // com.inspi.app.data.repository.FriendRepository 
          return (T) new FriendRepository(singletonCImpl.friendDao(), singletonCImpl.inspiPreferencesProvider.get());

          case 9: // com.inspi.app.data.repository.ChallengeRepository 
          return (T) new ChallengeRepository(singletonCImpl.challengeDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
