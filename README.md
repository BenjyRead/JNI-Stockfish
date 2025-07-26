# JNI-Stockfish
Easily implement Stockfish into your Android App using this repository (without having to write C++).


# How to implement Stockfish in your Android App

## Prerequisites / Assumptions

Most Android apps are written in Kotlin nowadays, and so this integration is done with Kotlin in mind. However, it can be used with Java as well, just might require some minor adjustments. I personally wrote this integration for my own app, which uses Gradle as the build system, so this guide assumes you are using Gradle as well.

## Step 1: Add the appropriate Stockfish code into your project
Go to `src/main/cpp` (you may need to create this directory) and add the `src` directory from the Stockfish repository. This will include the necessary C++ files for Stockfish. Please rename this `src` directory to `stockfish` to avoid confusion with the `src` directory of your Android project.

## Step 2: Add the JNI bridge code
From this repository, copy the `bridge.cpp` file into the `src/main/cpp` directory of your Android project (Note: NOT the `stockfish/` directory). This file contains the JNI bridge code that allows you to call Stockfish functions from Kotlin/Java. To compile this code correctly, a `CMakeLists.txt` file is also required. You can find this file in the root of this repository. Copy it into the `src/main/cpp` directory as well.

## Step 4: Add NNUE files
NNUE files are necessary for Stockfish to use the neural network evaluation, they are essentially pre-calculated weights meaning you can access ridiculous chess engine strength without having to access a server online. You need 2 files, one small and one big. You can find these files in [here](https://tests.stockfishchess.org/nns) or alternatively if you do not want to find them yourself, you can download them from this repository (in the `sample-nnue/` directory). Place these files in the `src/main/assets` directory.

## Step 5: Update your `app/build.gradle` file
In the root of this repository, you will find a `build.gradle` file. Please integrate the contents of this file into your `app/build.gradle` file. This file contains the necessary configurations to build the JNI code and link it with your Kotlin/Java code. To see an example of an "integrated" `build.gradle` file, you can check the `sample-gradle/` directory in this repository, which contains a `build.gradle` before and after integration.

## Step 6: Write a JNI "Stockfish Engine" object in your Kotlin/Java code
In your Kotlin/Java code, you can now create a class that interacts with the Stockfish engine. This class will use the JNI bridge to call Stockfish functions. Although this is the most customizable part, a simple and very practical example of one is provided in the `sample-kotlin/` directory of this repository. This has all the necessary functions to interact with Stockfish using UCI (Universal Chess Interface) commands.

## Step 7: Use the Stockfish engine in your app
Now that you have everything set up, you can use the Stockfish engine in your app. You can now interact with the Stockfish engine using the class you created in Step 6. Again in the `sample-kotlin/` directory, you can find an example of how to use my implementation of the Stockfish engine object in your app. This example shows how to start the engine, send commands, and receive responses.



<!-- build.gradle updates -->
<!-- bridge.cpp and JNI basic explanation -->
<!-- Stockfish engine object (and how to use) -->
<!-- nnue files (small and big) -->

