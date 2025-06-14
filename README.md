# Tweets Feed 📱

A modern Android application built with Jetpack Compose that displays categorized tweets in a clean
and intuitive interface.

## Features ✨

- **Category Browse**: View different tweet categories
- **Tweet Feed**: Display tweets with engagement metrics (likes, retweets)
- **Navigation**: Seamless navigation between categories and tweet details
- **Modern UI**: Built with Material Design 3 and Jetpack Compose
- **Clean Architecture**: Follows MVVM pattern with Repository pattern

## Tech Stack 🛠️

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Navigation**: Compose Navigation
- **Networking**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Build System**: Gradle with Kotlin DSL

## Project Structure 📁

```
app/src/main/java/com/deena/tweetsfeed/
├── data/                   # Data layer
│   ├── model/             # Data models
│   ├── network/           # Network API interfaces
│   └── repository/        # Repository implementations
├── domain/                # Domain layer
│   ├── repository/        # Repository interfaces
│   └── usecase/          # Business logic use cases
├── presentation/          # Presentation layer
│   ├── screen/           # Compose screens
│   └── viewmodel/        # ViewModels
├── di/                   # Dependency injection modules
├── ui/theme/             # UI theme and styling
└── utils/                # Utility classes
```

## Getting Started 🚀

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK API 24 or higher

### Installation

1. Clone the repository:

```bash
git clone <repository-url>
cd tweetsfeed
```

2. Open the project in Android Studio

3. Build and run the project:

```bash
./gradlew assembleDebug
```

## Usage 📋

1. **Browse Categories**: Launch the app to see available tweet categories
2. **View Tweets**: Tap on a category to view tweets in that category
3. **Navigate Back**: Use the back button to return to categories

## API Integration 🌐

The app fetches tweet data through a RESTful API using Retrofit. Tweet data includes:

- Tweet text content
- Category and subcategory
- Engagement metrics (likes, retweets)
- Motivational status flag

## Development 👩‍💻

### Running Tests

```bash
./gradlew test
```

### Code Style

The project follows standard Kotlin coding conventions and uses:

- ktlint for code formatting
- Detekt for static code analysis

## Contributing 🤝

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request
