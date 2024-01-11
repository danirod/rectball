Expected to see an Android project here? We have to go deeper!

In the past I used to use flavors for this, but they became difficult to maintain. I am switching
to completely isolated submodules to see if it is better this way, and to understand my build
pipeline. If I suck at this, I'll go back to flavors.

Subprojects:
* :android:core -- contains the base library, used by all versions
* :android:osp -- contains the open source project (free software, free as in freedom)
* :android:gpe -- contains the Google Play edition (integrates with Google Play games)

Differences between :osp and :gpe
* :osp does not integrate with Google Game Services, while :gpe does.
* :gpe contains extra res/ for the required Google Play Games string IDs.
* :gpe contains extra assets/ for the additional Gdx Textures and I18nBundles in use.

Gradle notes:
* Each subproject has its own build.gradle, but since they are child projects of :android,
  they inherit from :android/build.gradle, which is set to configure :osp and :gpe subprojects.
* Each subproject will source from :android/android.gradle by using apply(), because for some
  reason, you cannot declare tasks inside a configure(subprojects) block in Gradle.

Other notes:
* I was under the impression that you could add <use-permission /> into an AndroidManifest.xml for
  an Android library and then the manifest merging would make your app inherit that permission,
  but for some reason if I request vibration permission in core/AndroidManifest.xml, the game will
  crash. I have to add the permission on each AndroidManifest.xml for the final applications.