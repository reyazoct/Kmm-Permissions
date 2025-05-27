# Changelog

All notable changes to this project will be documented in this file.

## [0.11.0] - 2025-05-27

### Added
- Add logic to show KImage in Android
- Add logic to show KImage in iOS
- Add logic to show KImage in Wasm
- Add Single Instance cache on image library in Image module

---

## [0.10.0] - 2025-05-26

### Improvements
- Add Image to take full width in iOS in PDFViewer module
- Add option to show UI components module is Wasm also

---

## [0.9.2] - 2025-05-23

### Improvements
- Add Image to take full width in Android in PDFViewer module

---

## [0.9.1] - 2025-05-22

### Added
- Add modifier in PdfViewer in PDFViewer module
- Add option to pass any pdfViewerState in PDFViewer module
- Add circular progress while pdf url is loading in PDFViewer module

---

## [0.9.0] - 2025-05-22

### Added
- Create UI for pdf viewer in Android in PDFViewer module
- Create UI for pdf viewer in iOS in PDFViewer module

---

## [0.8.2] - 2025-05-16

### Improvements
- Add logic to hold and press to increase and decrease quantity in Quantity Changer in UI module
- Fix ripple effect issue in icon in Quantity Changer in UI module

### Fixed
- Add proper not implemented error when PermissionInitiation.setActivity(this) is not added.

## [0.8.1] - 2025-05-10

### Added
- Add option to flip the shape on next in Quantity Changer in UI module

## [0.8.0] - 2025-05-10

### Added
- Add Quantity Changer in UI module

## [0.7.3] - 2025-05-07

### Added
- Add record audio permission for android
- Add record audio permission for iOS

## [0.7.2] - 2025-05-06

### Bugfixes
- Fix alignment issue for integer and decimal part

### Added
- Add option to handle animation speed in Amount Text

## [0.7.1] - 2025-05-06

### Improvements
- Add slide in and out animation when amount changes
- Add logic to slide up and down if amount is increasing or decreasing

## [0.7.0] - 2025-05-06

### Added
- Add slide in and out animation when amount changes

## [0.6.0] - 2025-04-25

### Added
- Add feature to switch on flash with camera preview in Android and iOS

## [0.5.0] - 2025-04-23

### Added

- Add camera content for Android and iOS
- Add Qr code scanning 

## [0.4.0] - 2025-04-22

### Added
- Add Camera preview library which shows camera preview in Android.
- Add Camera preview library which shows camera preview in iOS.

## [0.3.0] - 2025-04-22

### Added
- Add open settings helper method which can be used when user denies permission.

## [0.2.0] - 2025-04-18

### Added
- Add logic to show request and check camera permission in iOS

## [0.1.1] - 2025-04-18

### Fix
- Fix Not Allowed issue while checking permission for first time in Android

## [0.1.0] - 2025-04-17

### Added
- Add logic to show request and check notification permission in android
- Add logic to show request and check notification permission in iOS

## [0.0.8] - 2025-04-15

### Added
- Add command to pull change log from CHANGELOG.md

---

## [0.0.7] - 2025-04-15

### Changed
- Change location request from PRIORITY_HIGH_ACCURACY to PRIORITY_BALANCED_POWER_ACCURACY for Android

---

## [0.0.6] - 2025-04-11

### Added
- Initial release with Permissions for Location
- Feature to fetch last known location
