#!/bin/bash

# Base SDK path
export ANDROID_SDK_ROOT="/workspaces/cloudpc/android_sdk"
export ANDROID_HOME="$ANDROID_SDK_ROOT"

# Command-line tools
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"

# Platform tools (adb, fastboot, etc.)
export PATH="$ANDROID_SDK_ROOT/platform-tools:$PATH"

# Build tools
export PATH="$ANDROID_SDK_ROOT/build-tools/33.0.0:$PATH"

# NDK path
export ANDROID_NDK_HOME="$ANDROID_SDK_ROOT/ndk"
export PATH="$ANDROID_NDK_HOME:$PATH"

# Emulator tools
export PATH="$ANDROID_SDK_ROOT/emulator:$PATH"

# Tools for SDK and NDK
export PATH="$ANDROID_SDK_ROOT/tools/bin:$PATH"
export PATH="$ANDROID_SDK_ROOT/tools:$PATH"

# Java 17 Home
export JAVA_HOME="/usr/local/sdkman/candidates/java/17.0.0-tem"
export PATH="$JAVA_HOME/bin:$PATH"

echo "SDK, NDK, and Java 17 paths have been set!"
