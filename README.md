# BactMan Adventures

[![BactMan Adventures](/../screenshots/img/poster.png "Optional Title")](https://play.google.com/store/apps/details?id=fr.plnech.igem)


BactMan Adventures is an Android game popularizing biology and science.
It has been developed by the [2015 IONIS iGEM Team](http://2015.igem.org/Team:IONIS_Paris),
who was awarded a gold medal for their participation in the 2015 competition. 

>This program is free software: you can redistribute it and/or modify
>it under the terms of the GNU General Public License as published by
>the Free Software Foundation, either version 3 of the License, or
>(at your option) any later version.  

See accompanying [license](LICENSE.txt) and [disclaimer](DISCLAIMER.txt).

--------
 


### Contributing

* All contributions are welcome as pull requests from a [feature branch](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow).
* This project has no formal coding style, yet **please do your best** to be consistent with its codebase.


### Developer setup guide

* Ensure you have the last version of Android SDK installed, with the [NDK Bundle](https://developer.android.com/tools/sdk/ndk/index.html)
* Clone this repository and cd in its folder
* run `git submodule init && git submodule update`
* If you are using Android Studio, select `File`->`New`->`Import project` and select the *build.gradle* file at the project root
* If you are *not* using Android Studio, create a file called `local.properties` with the following content:

    
```
#!ini
sdk.dir=C\:\\Path\\To\\Your\\AndroidSDK\\sdk
ndk.dir=C\:\\Path\\To\\Your\\AndroidSDK\\sdk\\ndk-bundle   
```
