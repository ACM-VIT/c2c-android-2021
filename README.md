![ACM-VIT](https://user-images.githubusercontent.com/14032427/92643737-e6252e00-f2ff-11ea-8a51-1f1b69caba9f.png)



<h1 align="center"> Code2Create App</h1>


&nbsp;&nbsp;&nbsp; [![Android-master Actions Status](https://github.com/ACM-VIT/c2c-android-2021/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/ACM-VIT/c2c-android-2021/actions) &nbsp;&nbsp;&nbsp;[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/ACM-VIT/c2c-android-2021/blob/main/LICENSE) &nbsp;&nbsp;&nbsp; [![Figma Designs](https://img.shields.io/badge/Design-Figma-important)](https://www.figma.com/file/v32aTQuQWoNXKv0nyZpJhD/C2C-App?node-id=10%3A0)



## Screenshots

| <img src="https://github.com/ACM-VIT/c2c-android-2021/blob/master/screenshots/countdown.png" alt="countdown" style="zoom:25%;" /> | <img src="https://github.com/ACM-VIT/c2c-android-2021/blob/master/screenshots/notifications.png" alt="notifications" style="zoom:25%;" /> | <img src="https://github.com/ACM-VIT/c2c-android-2021/blob/master/screenshots/timeline.png" alt="timeline" style="zoom:25%;" /> | <img src="https://github.com/ACM-VIT/c2c-android-2021/blob/master/screenshots/tracks.png" alt="tracks" style="zoom:25%;" /> |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
|                                                              |                                                              |                                                              |                                                              |

## Structure

* `build.gradle` - root gradle config file
* `settings.gradle` - root gradle settings file
* `app` - our only project in this repo
* `app/build.gradle` - project gradle config file
* `app/src` - main project source directory
* `app/src/main` - main project flavour
* `app/src/main/AndroidManifest.xml` - manifest file
* `app/src/main/java` - java source directory
* `app/src/main/res` - resources directory



## Unpacking

This repository uses secrets that aren't pushed to version control. However they are needed to build the project. Follow the below steps to obtain the files.

From the project root, execute:

```
.github/encrypted_secrets/decrypt_secrets.sh SECRET_KEY
```

If you are not on the development team contact us at  outreach.acmvit@gmail.com to obtain the SECRET_KEY used to unlock the secrets.

## Building

It is recommended that you run Gradle with the `--daemon` option, as starting
up the tool from scratch often takes at least a few seconds. You can kill the
java process that it leaves running once you are done running your commands.

Tasks work much like Make targets, so you may concatenate them. Tasks are not
re-done if multiple targets in a single command require them. For example,
running `assemble install` will not compile the apk twice even though
`install` depends on `assemble`.

#### Clean

	gradle clean

#### Debug

This compiles a debugging apk in `build/outputs/apk/` signed with a debug key,
ready to be installed for testing purposes.

	gradle assembleDebug

You can also install it on your attached device:

	gradle installDebug

#### Release

This compiles an unsigned release (non-debugging) apk in `build/outputs/apk/`.
It's not signed, you must sign it before it can be installed by any users.

	gradle assembleRelease

#### Test

Were you to add automated java tests, you could configure them in your
`build.gradle` file and run them within gradle as well.

	gradle test

#### Lint

This analyses the code and produces reports containing warnings about your
application in `build/outputs/lint/`.

	gradle lint

## Further reading

* [Build System Overview](https://developer.android.com/sdk/installing/studio-build.html)
* [Gradle Plugin User Guide](http://tools.android.com/tech-docs/new-build-system/user-guide)
* [Gradle Plugin Release Notes](http://tools.android.com/tech-docs/new-build-system)
