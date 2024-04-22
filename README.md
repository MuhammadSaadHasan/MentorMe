# MentorMe

MentorMe is an Android application built with Firebase that connects mentees with mentors. This project is designed to facilitate mentorship in various fields by allowing users to book sessions, search for mentors, and participate in community chats.

## Features

- **User Authentication:** Signup, login, and logout functionalities.
- **Profile Management:** Users can update their profiles, including display pictures and cover photos.
- **Mentor Interaction:** Users can add mentors to their favorites, book sessions, and review mentors.
- **Community Features:** Includes one-to-one and community chat functionalities, with the ability to send text, voice notes, and images.
- **Real-Time Updates:** Utilizes Firebase for real-time updates, push notifications for chats, and mentor status updates.
- **Enhanced Search:** Search and filter mentors based on various criteria.
- **Accessibility and Offline Support:** Ensures the app is accessible and functions in offline mode with cached data.

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/i210566/mentorme.git
   ```
2. **Open in Android Studio:**
   - Open Android Studio and select "Open an existing project", then navigate to the cloned repository.

3. **Configure Firebase:**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
   - Download the `google-services.json` file and place it in the `app/` directory.

4. **Build the project:**
   - Build > Rebuild Project.

5. **Run the application:**
   - Run > Run 'app'.

## Project Structure

- **`/app/src/main/java/com/yourname/i210566`:** Contains all Java files.
- **`/app/src/main/res/layout`:** Contains all XML layout files.
- **`/app/src/main/res/values`:** Contains resource files for strings, dimensions, and styles.

## Testing

- **Espresso Tests:** Located in `/app/src/androidTest/java/com/yourname/i210566`. Run these tests from Android Studio to ensure features like navigation and button click functionality work as expected.

## GitHub Integration

- Commit changes frequently to ensure each screen and feature is updated in the repository. This practice helps in tracking changes and collaborating effectively.

## Contribution

- Contributions are welcome. For major changes, please open an issue first to discuss what you would like to change.
- Please make sure to update tests as appropriate.

## License

Distributed under the MIT License. See `LICENSE` for more information.
