# Digital Bank KYC
App Link - https://appetize.io/app/b_ji4beo7xbxjr3i35dusm2x6gge

### ⧉ The Solution

Digital Bank KYC transforms account management into a seamless, high-performance mobile workflow:

- **Accounts Dashboard & Search:** Browse customer accounts instantly with Verified and Pending KYC tabs, 300ms debounced search, and smooth 15-item scroll pagination.
- **Live IFSC Resolution:** Automatically resolves real-time bank name, branch, city, and state details using the Razorpay IFSC API.
- **In-App CameraX Selfie Capture:** Complete KYC verification natively inside the app with a custom front-camera preview (no system intents used).
- **Offline Persistence & Smart Caching:** Saves KYC verification statuses and captured selfie paths in a local Room database with 1-minute in-memory caching.


| Kotlin | Jetpack Compose | Material 3 | CameraX |
|--------------------|------------------|------------------|----------------------|

| Retrofit | Room | MVVM | Kotlin Coroutines | Hilt |
|-------------------|-------------------|-----------|------------------|-----------------------|

#

<table>
  <tr>
    <td width="60%">

<h1>Accounts Screen</h1>
The Accounts screen acts as the Relationship Manager's main dashboard.
It shows cached customer accounts immediately for a faster experience, then quietly syncs with the network so the list stays up to date without feeling slow.

- Tabbed browsing between `Verified` and `Pending` KYC accounts using Material 3 filter chips.
- Real-time search by customer full name or masked IBAN with **300ms Coroutine debouncing**.
- Smooth **15-item scroll pagination** with infinite loading indicators.
- Pull-to-refresh to force-synchronize data directly from the network.
- Card view displaying customer avatar, name, masked account number, balance, and KYC status badge.

<h1> </h1>
    </td>
    <td width="40%">
      <img src="https://github.com/user-attachments/assets/8a54a90f-a5b0-492d-9941-a0310490a747" alt="Accounts Screen" />
    </td>
  </tr>
</table>

<table>
  <tr>
     <td width="40%">
      <img alt="Account Details Screen" src="https://github.com/user-attachments/assets/6a757725-2426-4b63-8335-7a514c6fc95d"/>
     </td>
    <td width="60%">

<h1>Account Details Screen</h1>
The Account Details screen renders full customer profile information, live bank branch data, and KYC selfie verification status.

The UI does not wait on slow network requests every time. Instead, the app saves KYC status and captured selfie file paths in a local Room database, and the screen observes that database via StateFlow.

- Displays full personal profile: Date of Birth, Phone, Email, Address, and Nationality.
- Account information card showing IBAN, Card Type, and Account Balance.
- Live Bank & Branch resolution card powered by Razorpay IFSC API.
- Dedicated KYC Selfie preview card showing captured photo with "CameraX" verification badge.
- Interactive "Do KYC" or "Re-take Selfie" button launching the native in-app camera.

<h1> </h1>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td width="60%">

<h1>In-App CameraX Screen</h1>
The Camera screen provides a secure, native camera preview to capture customer selfies directly within the app context.

- Strictly enforces in-app CameraX front-camera capture (no system camera intents or gallery pickers used).
- Graceful runtime camera permission handling (Request → Granted / Denied).
- Deep-links directly to App Settings if camera permission is permanently denied ("Don't ask again").
- Automatically persists the captured photo to app-private storage and marks the user as Verified in Room database.

<h1> </h1>
    </td>
    <td width="40%">
      <img src="https://github.com/user-attachments/assets/c047edcc-ee88-43a4-bd7c-11e0244082ea" alt="Camera Screen" />
    </td>
  </tr>
</table>




