# 📱 PocketPilot

PocketPilot is a modern, responsive mobile personal finance tracking companion engineered using an offline-first architecture. It synchronizes local persistence data models with live cloud servers while providing advanced behavioral analytics and budget boundary visualizations.



## ✨ Core System Features

* **🔐 Secure Authentication:** User registration and login pathways powered by remote Firebase Authentication.
* **📱 Hybrid Sync Architecture:** Dual-engine framework combining local Room Database storage with Cloud Firestore snapshot listeners for seamless real-time data streaming.
* **📊 Predictive Analytics & Graphing:** Programmatic canvas vector graph plotting real-time spending alongside budget bounds, complete with a monthly target compliance assessment system.
* **📸 Hardware Camera Integration:** Local bitmap verification tracking allowing users to capture and preview receipt photo assets instantly on-device.
* **🚀 Automation & Quality Traceability:** Built-in Android logging sub-systems matched with a continuous integration GitHub Actions pipeline workflow.



## 🛠️ Built With

* **Kotlin** – Core language syntax and asynchronous coroutine state flow operations.
* **Jetpack Compose** – Declarative Material 3 UI component building blocks.
* **Room Database** – Hardware disk persistence layer for offline usability.
* **Firebase (Auth & Cloud Firestore)** – Cloud identity validation and continuous real-time dataset clustering.
* **GitHub Actions** – Continuous integration workflow automated environment verification.



## ⚙️ How to Run the Project

1. Open the source project root folder inside **Android Studio (Ladybug or newer recommended)**.
2. Ensure your valid, registered `google-services.json` file is present inside the local `app/` folder directory.
3. Click the **"Sync Project with Gradle Files"** toolbar index option and allow dependencies to compile.
4. **Mandatory PoE Execution Rule:** Connect a **Physical Android Handset Device** via USB debugging (API Level 25 or higher) and click **Run ▶️** (Emulators are strictly prohibited for Part 3 final grading evaluation).



## 👨‍💻 Member 2 Contribution

* **Student Name:** Monwabisi Dlokweni  
* **Branch Git Identifier:** `ST10301110_Member2`

### 📂 Files Managed & Maintained
* `com.pocketpilot.pocketpilot.data.entities.Expense.kt`
* `com.pocketpilot.pocketpilot.ui.expense.AddExpenseScreen.kt`
* `com.pocketpilot.pocketpilot.ui.expense.ExpensesListScreen.kt`
* `com.pocketpilot.pocketpilot.SpendingAnalyticsScreen.kt`
* `com.pocketpilot.pocketpilot.ui.PocketViewModel.kt`

### 🚀 Advanced Features Implemented & Documented

* **Add Expense Form Matrix:** Captures secure inputs for transaction amount values, structural category drops, date metadata, and item note descriptions.
* **Category Selection Component:** Dropdown mapping menu allowing users to flag transactions under predefined tracking identifiers.
* **Dynamic Budget Usage Row Indicator:** Dashboard metric converting raw aggregate variables into direct percentage progression labels (`% Used`).
* **Advanced Category Bar Graph Engine:** Native `Canvas` drawing workspace parsing real-time Room lists into distinct horizontal data column blocks.
* **Target Boundary Indicator Lines:** Vector graph drawing utilizing dashed path effects to overlay a **🟢 Minimum Savings Goal Line (R400)** and a **🔴 Maximum Spending Cap Line (R2000)** directly on the analytics canvas interface.
* **Monthly Compliance Assessment Evaluation:** Automated conditional algorithm analyzing category totals against target boundaries to render an adaptive, color-coded trailing performance status notification block.
* **Hardware Result Camera Launcher:** Native activity result connection pipeline (`TakePicturePreview`) initializing device cameras to preview verification receipt bitmaps.
* **Production Status Logging Framework:** Explicit `Log.d`, `Log.i`, and `Log.e` trace flags reporting execution events and network database mutations straight into the system Logcat panel.
* **CI Automation Action YAML Script:** Complete automated environment check file (`.github/workflows/android.yml`) managing headless builds and testing operations.



## 📚 Academic References (Harvard Style)

Android Developers, 2024. Connect your app to the network: Manifest Permissions. [Online]  
Available at: https://android.com  
[Accessed 2 June 2026].

Android Developers, 2024. Build Layouts with Jetpack Compose. [Online]  
Available at: https://android.com  
[Accessed 3 June 2026].

Android Developers, 2024. Save data in a local database using Room. [Online]  
Available at: https://android.com  
[Accessed 3 June 2026].

Android Developers, 2024. Organizing Package Hierarchies and Component Layout Vectors in Android Studio. [Online]  
Available at: https://android.com  
[Accessed 3 June 2026].

Android Developers, 2026. Write and View System Logs with Logcat tools. [Online]  
Available at: https://android.com  
[Accessed 3 June 2026].

Cloud Firestore, 2026. Firestore Android Setup Documentation. [Online]  
Available at: https://google.com  
[Accessed 3 June 2026].

Firebase Auth, 2026. Get Started with Firebase Authentication on Android. [Online]  
Available at: https://google.com  
[Accessed 2 June 2026].

Firebase BoM, 2026. Firebase Android BoM (Bill of Materials) Release Notes. [Online]  
Available at: https://google.com  
[Accessed 1 June 2026].

GitHub Actions, 2026. Building and Testing Android Applications via Continuous Integration. [Online]  
Available at: https://github.com   
[Accessed 3 June 2026].

Jetpack Canvas, 2024. Graphics and Drawing operations inside Compose UI. [Online]  
Available at: https://android.com  
[Accessed 3 June 2026].

Smit, R., 2017. Reference Techniques: Harvard Style. [Online]  
Available at: https://www.uj.ac.za  
[Accessed 30 April 2026].


📖 Additional Technical References

* Jetpack Compose Documentation
    https://developer.android.com/develop/ui/compose/documentation
    Usage: UI components such as buttons, text fields, dropdowns
* Camera API
    https://developer.android.com/training/camera/photobasics 
    Usage: Capturing receipt images
* FileProvider
    https://developer.android.com/reference/androidx/core/content/FileProvider 
    Usage: Saving images securely to storage
* Date Picker
    https://developer.android.com/guide/topics/ui/controls/pickers 
    Usage: Date and time selection dialogs

AI Usage
Used chatGPT to improvr the read me file ----  https://chatgpt.com/share/69f3760f-97cc-83ea-a935-76312fac3b19
Google AI Mode was used for error handling see annexures below---
<img width="451" height="392" alt="Screenshot 2026-04-30 145333" src="https://github.com/user-attachments/assets/1bc2f833-e54c-4e3a-8a85-0f46e1269635" />
<img width="814" height="673" alt="Screenshot 2026-04-30 144540" src="https://github.com/user-attachments/assets/25f095f0-189e-41df-818d-c1886129ecac" />
<img width="739" height="545" alt="Screenshot 2026-04-30 141616" src="https://github.com/user-attachments/assets/32a67232-8b20-443b-ab65-1e31c9e84f01" />
<img width="1034" height="761" alt="Screenshot 2026-04-30 174015" src="https://github.com/user-attachments/assets/f786e112-61eb-4f3e-96e5-a0a931410a91" />
<img width="1021" height="705" alt="Screenshot 2026-04-30 173949" src="https://github.com/user-attachments/assets/37cb6c18-b22a-4b20-a07e-dd6f2e6d5f61" />
<img width="1182" height="767" alt="Screenshot 2026-04-30 173830" src="https://github.com/user-attachments/assets/4646078b-e118-4984-b3b6-d56ab094d01e" />
<img width="1151" height="767" alt="Screenshot 2026-04-30 173703" src="https://github.com/user-attachments/assets/59d78486-a45b-45c7-b17a-dc8dd568e9e3" />
<img width="1154" height="749" alt="Screenshot 2026-04-30 173625" src="https://github.com/user-attachments/assets/fbbc1f3c-a5d2-44b1-b286-fc19c70b2f8c" />
<img width="1363" height="767" alt="Screenshot 2026-04-30 173550" src="https://github.com/user-attachments/assets/cc793123-fab4-4271-bce8-63fd7d14f469" />
<img width="1335" height="710" alt="Screenshot 2026-04-30 173455" src="https://github.com/user-attachments/assets/c323d443-185e-45b7-90a7-2898164b2a7c" />
