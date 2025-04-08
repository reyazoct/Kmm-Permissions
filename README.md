## KMM Permission

![Maven Central Version](https://img.shields.io/maven-central/v/tech.kotlinlang/permission?style=for-the-badge&logo=jetpackcompose&logoColor=FFFFFF)

### Permissions Library for Compose Multiplatform

Kmm-Permissions is a Kotlin Multiplatform library designed to simplify permission handling in Android and iOS applications. By providing a unified API, it enables developers to manage permissions seamlessly across both platforms.

## How to use

1.  Add the KMM Permissions dependency to your project's `build.gradle.kts` file.


    ```kotlin
    commonMain.dependencies {
      implementation("tech.kotlinlang:permission:<version>")
    }
    ```

2.  Use `getPermissionHelper` extension function from `commonMain` which helps to create new instance for permission helper class.

    ```kotlin
    val permissionHelper = getPermissionHelper()
    ```

3.  Use `permissionHelper` instance to check permission state or request permission. Both functions are suspended, use `Dispatcher.Main` for access permissions.

    ```kotlin
    // Check Permission
    val checkPermssionResult = permissionHelper.checkIsPermissionGranted(Permission.Location)

    // Request Permssion
    val requestPermissionResult = permissionHelper.requestForPermission(Permission.Location)
    
    // result provide different states according to permission
    ```


## Useful functions

#### List of Permission Result States

| S. No. | Permssion Type          | State          | Description                                                                                                                                        |
|--------|-------------------------|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| 1      | Location                | LocationPermissionResult.Granted      | Location Permission is Granted by user      |
| 2      | Location         | LocationPermissionResult.Denied | Location Permission is Denied by user. But developer can request again for permission.  |
| 2      | Location         | LocationPermissionResult.NotAllowed | Location Permission is Denied by user. Developer cannot request again for permission. Developer have to navigate user to settings page. |


## About Developer

>  #### Reyaz Ahmad
> [![GitHub followers](https://img.shields.io/github/followers/reyazoct?style=for-the-badge&logo=github&label=Github)](https://github.com/reyazoct) [![Website](https://img.shields.io/website?url=https%3A%2F%2Freyaz.live&style=for-the-badge&label=reyaz.live)](https://reyaz.live) [![Website](https://img.shields.io/website?url=https%3A%2F%2Fwww.linkedin.com&style=for-the-badge&logo=linkedin&label=linkedin)](https://www.linkedin.com/in/ahmad-reyaz) [![Website](https://img.shields.io/website?url=https%3A%2F%2Fleetcode.com&style=for-the-badge&logo=leetcode&label=Leetcode)](https://leetcode.com/u/reyazoct)