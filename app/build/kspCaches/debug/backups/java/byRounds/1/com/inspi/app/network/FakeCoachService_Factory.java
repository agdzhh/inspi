package com.inspi.app.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class FakeCoachService_Factory implements Factory<FakeCoachService> {
  @Override
  public FakeCoachService get() {
    return newInstance();
  }

  public static FakeCoachService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeCoachService newInstance() {
    return new FakeCoachService();
  }

  private static final class InstanceHolder {
    private static final FakeCoachService_Factory INSTANCE = new FakeCoachService_Factory();
  }
}
