# UniBites - Cart Implementation

## Overview
This project implements a shopping cart screen for the UniBites food delivery app. The cart allows users to:
- View items in their cart
- Increase/decrease item quantities
- Remove items
- Proceed to payment

## Firebase Integration Instructions
The current implementation uses hardcoded data for the cart items. To properly integrate with Firebase, follow these steps:

### Step 1: Set up Firebase Project
1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" and follow the setup wizard
3. Register your Android app with your package name (`com.example.unibites`)
4. Download the `google-services.json` file and replace the placeholder in the app directory

### Step 2: Enable Firebase Services
1. In the Firebase Console, navigate to your project
2. Enable Firestore Database
   - Go to Firestore Database and click "Create database"
   - Start in production mode and choose a location close to your users
3. Enable Authentication
   - Go to Authentication and click "Get started"
   - Enable Email/Password sign-in method (or any other methods you prefer)

### Step 3: Uncomment Firebase Code
1. In `app/build.gradle.kts`, uncomment the Firebase dependencies:
   ```kotlin
   plugins {
       alias(libs.plugins.android.application)
       id("com.google.gms.google-services")  // Uncomment this
   }

   dependencies {
       // Uncomment these Firebase dependencies
       implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
       implementation("com.google.firebase:firebase-firestore")
       implementation("com.google.firebase:firebase-auth") 
   }
   ```

2. In `CartActivity.java`, uncomment the Firebase code:
   - Uncomment the FirebaseManager import
   - Uncomment the Firebase initialization and data loading code
   - Uncomment the Firebase cart update methods

### Step 4: Add Food Images
1. Add food item images to the `app/src/main/res/drawable` directory
2. Update the image loading code in `CartAdapter.java`:
   ```java
   if (item.getName().equals("Chole Bhature")) {
       holder.itemImageView.setImageResource(R.drawable.img_chole_bhature);
   } else if (item.getName().equals("Chocolate Shake")) {
       holder.itemImageView.setImageResource(R.drawable.img_chocolate_shake);
   } else if (item.getName().equals("Cold Coffee")) {
       holder.itemImageView.setImageResource(R.drawable.img_cold_coffee);
   }
   ```

### Step 5: Test Firebase Integration
1. Build and run the app to verify Firebase integration
2. Test adding items to the cart
3. Test updating item quantities
4. Test removing items from the cart

## Adding New Menu Items
To add new menu items that can be added to the cart:

1. Create a Menu Items collection in Firestore with documents containing:
   - `itemId`: String - Unique ID for the item
   - `name`: String - Name of the food item
   - `price`: Double - Price of the item
   - `description`: String - Description of the item
   - `imageUrl`: String - URL to the item image (if using Cloud Storage)
   - `category`: String - Category of the item (e.g., "Food", "Drink")

2. Add a method in FirebaseManager to add items to cart:
   ```java
   public void addMenuItemToCart(String menuItemId, int quantity, OnCartOperationListener listener) {
       // Get menu item details and add to cart
   }
   ```

## Troubleshooting
If you encounter issues with Firebase integration:

1. Verify that the `google-services.json` file is correctly placed in the app directory
2. Check that all Firebase dependencies are correctly uncommented
3. Ensure your device/emulator has internet connectivity
4. Verify that Firebase project settings match your app's package name
5. Check Logcat for Firebase-related error messages 