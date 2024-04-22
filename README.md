# MentorMe

MentorMe is an Android application developed with Firebase that aims to connect mentees with mentors across various fields. The app enables users to book sessions, browse and filter mentors, participate in community chats, and manage their profiles.

## Features

### User Authentication
- **Sign Up:** Users can create an account by providing their email, password, and personal details.
- **Login:** Users return to their existing account using their credentials.
- **Logout:** Allows users to securely exit their account.

![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/396c42b2-7646-43a8-9d72-bbac61810013)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/0977e168-e22c-431a-8712-39309d0efedd)


### Profile Management
- **Profile Updates:** Users can update personal information such as their name, contact details, profile picture, and cover photo.
- **Privacy Settings:** Control who can view their profile and personal information.

![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/2842f741-e72b-4ff0-b44d-46c8cd8f7f66)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/4e7040a9-c886-44c4-af68-d041cc08e809)


### Mentor Interaction
- **Booking Sessions:** Users can book sessions with mentors based on their availability.
- **Favorites:** Users can add mentors to a favorite list for quicker access in future interactions.
- **Reviews:** After sessions, users can leave feedback on their experience with mentors.

![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/9274c2bd-a5c5-4e77-9c81-276dbc1ab562)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/16339ca6-9a96-4d03-b76a-98e4b95d31a9)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/47c33e35-854c-446f-9d42-751c8130cd67)



### Community Features
- **One-to-One Chat:** Private messaging feature to communicate directly with mentors.
- **Community Chat:** Group chat rooms where users can discuss topics and share information.

![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/55c804fb-345c-4750-917c-c6e98473ede7)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/d9ee73cc-5fbb-4f52-82f4-4419e0bf6abd)


### Real-Time Updates
- **Firebase Integration:** Utilizes Firebase for real-time data syncing, ensuring that all user interactions and updates are instant.
- **Notifications:** Receive push notifications for important events like session confirmations and messages.

### Enhanced Search
- **Filters:** Users can search for mentors by specialties, ratings, and availability.
- **Recent Searches:** Easily revisit previously searched terms.

![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/7a457d7b-7ac3-47c1-9903-93fca78dff09)
![image](https://github.com/MuhammadSaadHasan/MentorMe/assets/109513669/a917c214-8cf8-46d9-8c09-48569d8e0419)


### Accessibility and Offline Support
- **Accessibility:** Implements accessibility features such as text scaling and color contrast to cater to all users.
- **Offline Mode:** The app caches data locally to allow users to access and write data without an internet connection. Changes are synced once connectivity is restored.

## Installation

### Cloning the Repository
```bash
git clone https://github.com/i210566/mentorme.git
```

### Setting up in Android Studio
- Open Android Studio and select "Open an existing project", then navigate to the cloned repository.

### Configuring Firebase
- Create a Firebase project via the [Firebase Console](https://console.firebase.google.com/).
- Download the `google-services.json` file and place it in the `app/` directory to connect the app with Firebase.

### Building the Project
- Navigate to Build > Rebuild Project in Android Studio to prepare the app for running.

### Running the Application
- Select Run > Run 'app' to launch the application on an emulator or connected device.

## Project Structure

- **`/app/src/main/java/com/yourname/i210566`:** Java source files for the application's functionalities.
- **`/app/src/main/res/layout`:** XML files defining the UI of the app.
- **`/app/src/main/res/values`:** Resource files for strings, dimensions, and styling that help maintain consistency throughout the app.

## Testing

### Espresso Framework
- **Location:** `/app/src/androidTest/java/com/yourname/i210566`
- **Purpose:** Automated UI tests to ensure that critical user interactions like navigation and button clicks perform as intended.

## GitHub Integration

- **Commit Strategy:** Regular commits are made to document the development process, with each significant change or addition to the application being committed separately. This ensures a clear history and easy rollback if necessary.

## License

This project is made available under the MIT License which permits reuse, distribution, and modification.
