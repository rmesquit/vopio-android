# vopio-android

**Project Goals**:  to make on-campus lectures more fulfilling to students and instructors.

**SRS**: Software Requirements Specification document available upon request.

**Etymology**: the word vopio is a combination of **v** for voice and **oppia**, which means "to learn" in the Finnish language.

## Demo Video
https://youtube.com/shorts/y0wFcO1Slsg?feature=share

## Tech Stack
### Platform
* Android Studio
* Kotlin (most of the app)
* Java (for integration with Speech API)
* Fragments
* AndroidX

### Cloud Services
* Google Cloud Speech API
* Firebase Database
* Firebase Auth
* Firebase Remote Config

### Networking
* OkHttp for Speech API

### Version control
* Git CLI
* Android Studio's Git tools

### Accessibility
* Compliance with WCAG 2.1 for proper contrast ratios, readability, and clickability

### Testing and Distribution
* App has been tested on modern smartphone and tablet
* Android R8 was enabled for reduced footprint
* R8 mapping file enabled for deobfuscation
* Google Play Console > Release > Testing > Pre-launch report > Overview
* App is available in Google Play > Open Testing
