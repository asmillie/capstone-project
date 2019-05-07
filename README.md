# capstone-project
Capstone Project for Udacity Android Developer Nanodegree Program

A simple news reader app for Android that displays the latest technology headlines in the US 
from NewsApi.org. Also includes a search feature to search all US technology headlines and a 
home screen widget.

# API Usage
An API key from NewsApi.org is required to run this app which can be obtained 
at the following website: https://newsapi.org/ 

See the next section on how to include your API key when running the app.

# gradle.properties
Once you have your API key from NewsApi.org you can add it to the app in your local 
**gradle.properties** file. Here you will also need to add the keystore information 
if you wish to install and run the release build of the app.

```
#Newsapi.org API Key
newsApiKey="{API KEY HERE}"

#Signing Config
RELEASE_KEYSTORE_RELATIVE_PATH=keystore.jks
RELEASE_KEYSTORE_PASSWORD=password
RELEASE_KEY_ALIAS=whatstrending
RELEASE_KEY_PASSWORD=password
```

# Firebase
Firebase analytics are utilized by this app so a new Firebase project will need to be created 
and the **google-services.json** file provided during project creation included in the app files.

For detailed instructions visit the official documention here: 
https://firebase.google.com/docs/android/setup (Option 1, Steps 1-3) 
