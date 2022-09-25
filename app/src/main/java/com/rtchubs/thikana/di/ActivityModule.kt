package com.rtchubs.thikana.di

import com.rtchubs.thikana.ui.LoginActivity
import com.rtchubs.thikana.ui.MainActivity
import com.rtchubs.thikana.ui.SplashActivity
import com.rtchubs.thikana.ui.barcode_reader.LiveBarcodeScanningActivity
import com.rtchubs.thikana.ui.live_chat.LiveChatActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLiveChatActivity(): LiveChatActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLiveBarcodeScanningActivity(): LiveBarcodeScanningActivity
}