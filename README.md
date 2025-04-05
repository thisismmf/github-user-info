# GitHub User Info Terminal Application

This Kotlin terminal application retrieves GitHub user information using the GitHub API. It features a menu-driven interface for user interactions, caching of user data in memory, and robust error handling.

## Features

- Retrieve GitHub user information including username, followers, following, and repositories.
- Caching of user data to minimize API requests and improve performance.
- User-friendly menu interface for easy navigation.
- Error handling to manage API request failures and user input errors.

## Technologies Used

- Kotlin
- Retrofit for API communication
- Gson for JSON parsing

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd github-user-info
   ```

2. **Build the project:**
   Ensure you have Gradle installed, then run:
   ```bash
   ./gradlew build
   ```

3. **Run the application:**
   Execute the main class:
   ```bash
   ./gradlew run
   ```

## Usage

- Upon running the application, you will be presented with a menu.
- Enter the corresponding number to select an option:
  1. Retrieve user information by username.
  2. Exit the application.

## Error Handling

The application includes error handling for:
- Invalid user input.
- API request failures (e.g., user not found, network issues).

## Caching

User data is cached in memory to avoid repeated API calls for the same user, enhancing performance and reducing load on the GitHub API.

## Contribution

Feel free to submit issues or pull requests for improvements or bug fixes.