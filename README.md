# Homework 4: Mobile Mess

## Introduction

Your organization has decided that they want to make an Android application
available for students who want to purchase NYU GiftCards. They took the liberty
of hiring a contractor to create the application, but the code came back less
useful than desired. Though your boss never told you which contracting company
was hired, you're pretty sure it as Shoddy Corp's Cut-Rate Contracting. They
also created a back-end for the application to interact with, but that was given
to another member of your team at work to fix.

Like previously, it's up to you to fix the messy code and ensure that the
application works as intended. Luckily, Kevin Gallagher (KG) has gone through
the code and compiled a list of things that need to change before your company
is ready to ship the application.

## Part 1: Setting up Your Environment

Like previous assignments, you are required to use the Git VCS. Start by cloning
this repository onto your machine, as you have done previously.

Next you will need to install Android Studio at the following link:

```
https://developer.android.com/studio/
```

**Unlike previous assignments, we recommend doing this on your Host Machine, not
a Virtual Machine running Linux.** Android studio works for Windows, Linux, Mac,
and Chrome OS, so most of your platforms should be covered. If you have a
different platform than listed above, please reach out to your instructor or TA.

We have prepared a visual walkthrough of the setup process on Mac (it should be
very similar on Linux and Windows), which you can look through [here](https://imgur.com/a/noDTMNj).

After installing Android Studio, you should import the project. You can do this
from the "Welcome to Android Studio" window by clicking "More Actions" ->
"Import Project (Gradle, Eclipse ADT, etc.)". This will open up a file chooser
window. Navigate to the homework repository, and choose the `GiftcardSite`
folder inside `AppSecAssignment4.1`. **Important: don't try to import a different
folder, like the top-level repository or the `src` folder; it needs
to be the `GiftcardSite` so that Android Studio will recognize the project
correctly.**

The project should now load in the IDE. You may see a dialog window that asks if
you want to "Trust Gradle Project?" - if so, answer yes. Now give the IDE a
minute or two to go download the project dependencies and set up the build
system. When it finishes, you should see the file pane on the left side of the
IDE populate with entries like "app", "manifests", and "Gradle Scripts".

Next, you should set up the Android Emulator. To do this go to:

```
Tools->AVD Manager
```

This will open a new panel. In this panel you should see a button that says:

```
Create virtual device
```

Click this button. Another window will open asking you to choose a device. We
tested this application on an emulated `Pixel 3a`, so we suggest selecting that
model, then clicking next. It will then ask you to select a system image. We
recommend downloading the image labeled R, which has an API Level of 30, the
x86 ABI, and a Target of Android 11.0 (Google Play). You may need to download
this image before you can use it by clicking the Download link next to the image
name. This will open a window that will ask you to accept the terms and
conditions. After you accept the image will download.

After downloading the image, the Android Virtual Device Manager will ask you to
name the virtual device and select between Portrait or Landscape. Ensure
Portrait is selected, leave Graphics at Automatic, and ensure Enable Device
Frame is checked. Then click on the button labeled Finish.

Note that the first time you run the Android Emulator it will take some time.
The emulator will set up the device for you, so let it do its work.

After you have created an emulated Android device, you can now build the project
and launch it in the emulator by pressing the green play button in the Android
Studio toolbar. The first time you launch the emulator it may take a while to
start up, but it should eventually appear and then start the app.

While the app is running, you may want to click on the "Run" and "Logcat" tabs
on the bottom of the IDE to see debug messages printed out by the app from
`Log.d()`.


## Part 2: It's all about intent

As you may remember from class, Android uses Intents to move in between parts of
an application, or to communicate between applications (thus providing
functionality the app doesn't naively support, like Web browsing).

### Part 2.1: What's the difference?

Intents, when not handled correctly, can cause problems. Take a look at the code
on lines 69 to 73 of SecondFragment.kt and lines 68 to 70 of ThirdFragment.kt.
These are two different ways of handling intents. For this portion of the
assignment, you should create a text file, called difference.txt, which answers
the following questions in 3 sentences or fewer.

1. What are the two types of Intents?
2. Which of the two types of Intents is (generally) more secure?
3. What type of Intent is shown on lines 69 to 73 of SecondFragment.kt?
4. What type of Intent is shown on lines 68 to 70 of ThirdFragment.kt?
5. Which of these two Intents is the proper way to do an Intent?

As the last question above hinted, one of these two Intents is not correct.
Fix the incorrect Intent, then in 3 sentences or fewer discuss in difference.txt
which file you modified and why.

### Part 2.2: Shutting out the world

It seems that the developers of the application wanted to allow other
applications to use Intents to launch the GiftCard application. However, this
isn't what your company wants. At this moment your company does not anticipate
a need for other applications to use Intents to launch Activities within the
GiftCard application.

For this part, you should remove the possibility of other applications using
Intents to launch activities of your application. To do this, changes will need
to be made to the AndroidManifest.xml file.

## Part 3: Can you read me out there?

Communication of data in transit is especially important. If communications are
not secured in transit, then network adversaries can read confidential data such
as passwords, or modify data in transit without worry. Unfortunately, the
developers of this application did not include any https encryption in calls to
the REST API that it is using in the backend. For this part of the application,
please secure all communication with the REST API using HTTPS. This modification
will require changes to the following files:

1. SecondFragment.kt
2. ThirdFragment.kt
3. CardScrollingActivity.kt
4. ProductScrollingActivity.kt
5. UseCard.kt
6. GetCard.kt
7. CardRecyclerViewAdapter.kt
8. RecyclerViewAdapter.kt
9. Reporter.kt
10. strings.xml

These changes should not be large. If you find yourself including new libraries,
or writing more lines of code instead of just modifying code that already exits
you are likely overthinking the problem. This one really is simple!

## Part 4: Oops, was that card yours?

There exists a vulnerability in the REST API that allows users to GiftCards that
do not belong to them. In a file called BUG.txt, explain why this vulnerability
may be occurring, and how it can be fixed. Your explanation should be no larger
than a paragraph.

To get an idea of how the app uses the REST API to invoke the use card
functionality, you can look at the following files:

1. UseCard.kt
2. CardInterface.kt

Think about how the application is telling the server which card to use, and how that may be problematic.

Hints:

* Think about how the application is telling the server which card to use, and how
  that may be problematic.
* You may want to try using `curl` or the Python `requests`
  library to interact with the API directly.

**Note**: You do *not* need to actually fix the vulnerability. In fact, once
you understand the vulnerability in detail, you should be able to see why it
cannot be fixed just by changing the client-side code.

## Part 5: Privacy is Important

Many modern Android applications collect large amounts of privacy-invasive
metrics about their users. This is very problematic, since many users carry
their devices at all times, and are unaware of the implications of granting a
permission.

In this section your goal is to remove all privacy invasive code. This is done
by removing all metric collecting code, all areas that needlessly interact with
sensors, and all permissions that are not needed for the basic functionality of
the application (buying, browsing, and using gift cards).

You should remove all necessary code in (at least) the following files:

1. AndroidManifest.xml
2. UserInfo.kt
3. CardScrollingActivity.kt
4. ProductScrollingActivity.kt

## Grading

Total points: 100

Part 2 is worth 30 points:

* 10 points for answering the questions.
* 10 points for fixing the correct intent.
* 10 points for closing the application to outside intents

Part 3 is worth 20 points:

* 2 points for each file correctly modified to use HTTPS.

Part 4 is worth 20 points:

* 10 points for identifying the cause of the vulnerability.
* 10 points for describing a potential solution.

Part 5 is worth 30 points:

* 10 points for removing unneeded permissions.
* 10 points for removing metric collection API calls.
* 10 points for removing interaction with sensors.

## What to Submit

Submit your GitHub Repository to Gradescope. 
There is an auto-grader that can detect the code changes, and we will manually grade the write-ups.

The repository should contain all the files of the Android project, plus the
text files BUG.txt and difference.txt.

## Concluding Remarks

Despite the fixes you've made, there are almost certainly still many
bugs lurking in the application, and the overall design of the application could
be better done. With enough changes, this application could serve as a decent
front-end for a REST API, but that API would also have to be audited.
