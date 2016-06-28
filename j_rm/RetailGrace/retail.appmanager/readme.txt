[Proguard for Android Library Project]
You typically should not enable ProGuard in the library project. ProGuard processes the application and the library together in the application project, which is the most effective approach.
In the library project, you can specify any library-specific ProGuard configuration in build.gradle, e.g.:
defaultConfig {
    consumerProguardFiles 'proguard-rules.txt'
}
This file is then packaged in the library aar as proguard.txt and automatically applied in the application project.
If you do enable ProGuard in a library project (maybe because you want to distribute the library), then you also have to add the proper configuration for processing the library.
The Android Gradle build doesn't seem to do this automatically. You can:
1. Copy android-sdk/tools/proguard/examples/library.pro to proguard-rules.pro in your library project.
2. Remove the sample input/output lines -injars, -outjars, -libraryjars, -printmapping from the file.
   The Gradle build process automatically provides these options.
3. Reference the configuration from the build.gradle of the library project.
4. Enabling/disabling ProGuard independently for the library project and for the application project works fine for me.
defaultConfig {
  minifyEnabled true
  shrinkResources true
  proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
}
5. reference: http://stackoverflow.com/questions/26983248/proguard-ignores-config-file-of-library
