# Firebase Integration for UniBites App

## Overview
This document explains how to integrate Firebase with the UniBites app to enable real-time database functionality for the shopping cart.

## Files with Commented Firebase Code
The following files contain Firebase-related code that has been commented out:

1. **app/build.gradle.kts**
   - Firebase BoM (Bill of Materials) and dependencies
   - Google Services plugin

2. **build.gradle.kts**
   - Google Services classpath

3. **app/src/main/java/com/example/unibites/CartActivity.java**
   - Firebase imports
   - FirebaseManager initialization
   - Firebase data loading and updating methods

4. **app/src/main/java/com/example/unibites/adapter/CartAdapter.java**
   - Code to load images from Firebase Storage URLs
   - Code that would be replaced with Firebase categorization

5. **app/src/main/java/com/example/unibites/firebase/FirebaseManager.java**
   - Entire Firebase implementation is commented out
   - Currently contains stub methods that return dummy data
   - All Firebase imports are commented out

6. **app/google-services.json**
   - This is a placeholder file that needs to be replaced with a real one from your Firebase project

## Steps to Enable Firebase

### 1. Create a Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" and follow the setup instructions
3. Register your Android app with package name `com.example.unibites`
4. Download the real `google-services.json` file and replace the placeholder in the app directory

### 2. Enable Firebase Features
In the Firebase Console:
1. **Authentication**: Set up authentication methods (Email/Password, Google, etc.)
2. **Firestore Database**: Create a database for storing cart items and user data
3. **Storage**: Set up storage for food item images

### 3. Uncomment Firebase Code
1. In **build.gradle.kts**:
   ```kotlin
   buildscript {
       repositories {
           google()
           mavenCentral()
       }
       dependencies {
           classpath("com.google.gms:google-services:4.4.1")
       }
   }
   ```

2. In **app/build.gradle.kts**:
   ```kotlin
   plugins {
       alias(libs.plugins.android.application)
       id("com.google.gms.google-services")
   }
   
   dependencies {
       // Uncomment Firebase dependencies
       implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
       implementation("com.google.firebase:firebase-firestore")
       implementation("com.google.firebase:firebase-auth")
       // Other dependencies...
   }
   ```

3. In **CartActivity.java**:
   - Uncomment the FirebaseManager import
   - Uncomment the FirebaseManager initialization in onCreate()
   - Uncomment the Firebase data loading in loadCartItems()
   - Uncomment Firebase updating code in onQuantityIncreased() and onQuantityDecreased()
   - Uncomment the updateCartItemInFirebase() method

4. In **CartAdapter.java**:
   - Uncomment the image loading code that uses Picasso with Firebase Storage URLs

5. In **FirebaseManager.java**:
   - Uncomment all Firebase imports at the top of the file
   - Uncomment the FirebaseFirestore and FirebaseAuth member variables
   - Uncomment the initialization in the constructor
   - Uncomment the getCurrentUserId() method's Firebase implementation
   - Uncomment the getCartCollection() method
   - Uncomment the real implementations of all methods currently returning dummy data
   - Remove all the dummy data code and "Firebase disabled" log messages

### 4. Database Structure
Firestore database should be structured as follows:

```
users/
  {userId}/
    cart/
      {cartItemId}/
        itemId: "string"
        name: "string"
        price: number
        quantity: number
        imageUrl: "string"

menu/
  {menuItemId}/
    name: "string"
    price: number
    description: "string"
    category: "string"
    imageUrl: "string"
```

### 5. Testing
1. Build and run the app after uncommenting the Firebase code
2. Verify that cart items are loaded from Firestore
3. Test adding items to the cart
4. Test updating item quantities
5. Test removing items from the cart

## Troubleshooting
- If you see build errors related to Google Services, ensure the plugin is correctly added
- If you see Firebase connection errors, verify your `google-services.json` file is correct
- For authentication issues, check that you've enabled the appropriate authentication methods in Firebase Console
- If you encounter red underlines in FirebaseManager.java, make sure you've properly uncommented all Firebase imports and dependencies

## Additional Resources
- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Firestore Documentation](https://firebase.google.com/docs/firestore) 