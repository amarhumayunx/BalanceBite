# **BalanceBite: Eat Well, Live Well**

BalanceBite is a comprehensive health and wellness mobile application designed to provide users with personalized nutrition and fitness recommendations. With a custom-built AI chatbot powered by the Naive Bayes algorithm, BalanceBite offers tailored diet plans, exercise routines, and ongoing guidance based on individual health metrics, all without relying on external APIs.

## **Table of Contents**
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture Overview](#architecture-overview)
- [Installation and Setup](#installation-and-setup)
- [Contributing](#contributing)
- [License](#license)

## **Features**
- **Personalized Nutrition Plans**: Customized meal recommendations based on dietary preferences, fitness goals, and health metrics.
- **Organic Diet and Exercise Suggestions**: Tailored advice on organic foods and exercise routines suited to individual needs.
- **AI Chatbot Support**: Instant health advice and motivational support through a custom-built AI chatbot using the Naive Bayes algorithm.
- **Health Progress Tracking**: Monitor daily intake, fitness activities, and receive actionable feedback on progress.
- **Real-Time Data Syncing**: Seamless syncing of user data across devices using Firebase.

## **Technology Stack**
- **Frontend**: Android (Kotlin)
- **Backend**: Firebase (Firestore, Authentication, Cloud Functions)
- **AI Integration**: Custom AI chatbot developed in Kotlin using the **Naive Bayes algorithm**.
- **Cloud Hosting**: Firebase Cloud

## **Architecture Overview**
The BalanceBite system follows a layered client-server architecture:
- **Client Layer**: Native Android mobile application developed in Kotlin, providing a rich user experience.
- **API Layer**: RESTful API Gateway for handling client-server communication.
- **Backend Layer**: Firebase handles user authentication, data storage, and serverless functions.
- **AI Layer**: A custom AI chatbot built using the Naive Bayes algorithm powers health advice and motivational interactions.

### **Key Components**:
- **Mobile Application**: Manages user interfaces and interactions.
- **API Gateway**: Facilitates communication between the mobile app and the backend.
- **User Management & Data Handling**: Secures and manages user profiles and health data in Firebase Firestore.
- **Custom AI Chatbot**: Provides personalized health advice using the Naive Bayes algorithm.

## **Installation and Setup**

### Prerequisites
- Android device (Android 6.0 or higher).

### Steps to Install the APK on Your Mobile

1. **Download the APK**:
   - Download the latest BalanceBite APK from the Google Play Store, When I release an build.

2. **Allow Installation from Unknown Sources**:
   - Go to your device's **Settings** > **Security**.
   - Enable **Install from Unknown Sources** to allow APK installation.

3. **Install the APK**:
   - Open the downloaded APK file on your Android device.
   - Follow the on-screen prompts to install the BalanceBite application.

4. **Launch the App**:
   - Once installed, open the BalanceBite app from your app drawer.
   - Complete the initial setup, including profile creation and personalization.

Now you can start using BalanceBite to manage your personalized diet and fitness plans, track progress, and receive health advice through the custom AI chatbot.

## **Contributing**
We welcome contributions to BalanceBite! To contribute:
1. Fork the repository.
2. Create a new branch with a descriptive name.
3. Commit your changes and push them to your branch.
4. Open a Pull Request for review.

Ensure your code adheres to the project’s coding standards and is well-documented.

## **License**
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
