# Shattered Crown — Text Adventure

A branching, decision-driven text adventure for Android, built with Kotlin
and Jetpack Compose.

## How to open and run it

1. Install [Android Studio](https://developer.android.com/studio) (Hedgehog
   or newer recommended).
2. Unzip this project and choose **File > Open** in Android Studio, then
   select the `StoryGame` folder.
3. Let Gradle sync (first sync will download dependencies — needs internet).
4. Click **Run ▶** with an emulator or physical device connected.

Minimum SDK is 24 (Android 7.0), target/compile SDK is 34.

## How the game works

- `Story.kt` holds the entire story as a map of `StoryNode`s. Each node has
  text and a list of `Choice`s. A node with no choices is an ending.
- Choices can optionally `setsFlag` (e.g. picking up an item, gaining an
  ally) and other choices can `requiresFlag` that flag to appear later —
  this is how earlier decisions affect what's possible downstream.
- `MainActivity.kt` contains a small `GameState` class that tracks the
  current node and the set of flags, plus the Compose UI that renders the
  current passage and buttons for each available choice.

## Adding your own story content

You don't need to touch the UI code at all — just add new `StoryNode`
entries to the list inside `Story.kt`:

```kotlin
StoryNode(
    id = "your_new_node",
    text = "Something happens...",
    choices = listOf(
        Choice("Do the brave thing", "brave_result"),
        Choice("Do the safe thing", "safe_result")
    )
)
```

Point an existing choice's `nextId` at your new node's `id` to wire it in.
Endings are just nodes with `isEnding = true` and no choices.

## Ideas for extending it

- Add a title/menu screen before the story starts.
- Persist progress with `SharedPreferences` so players can resume later.
- Load story content from a JSON file instead of hardcoded Kotlin, so
  non-programmers can write new chapters.
- Add sound, background images per node, or a simple inventory screen.
