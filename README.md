# TheGreatEscape

Welcome to the manual for "The Great Escape" game. "The Great Escape" is an
adventure game where your main aim is to break out of a prison. This game was inspired by
The Binding of Isaac and based on a top-down perspective, offering various levels
with different difficulties.
## Getting Started
To begin playing "The Great Escape," follow these steps:
1. Install Maven if not already installed.
   ```
   mvn install
   ```
3. Go to the game directory (you have to be in package TheGreatEscape).
4. Run the following command to start the game:
   ```
   mvn exec:java -Dexec.mainClass="org.game.thegreatescape.view.Game"
   ```
   Add -Dexec.args="--disable-logging" to play without logging and nothing to play with

## Character Controls
Once in a game, you can control character by pressing keyboard:
 * Key "D": Move right.
 * Key "W": Move up.
 * Key "A": Move left.
 * Key "S": Move down.
 * Key "F": Press and collect items.

![MyMovie-ezgif com-video-to-gif-converter](https://github.com/ofgot/TheGreatEscape/assets/113288163/c7d25b53-4276-41f8-bf62-4e15c3d98e0d)

# Level Editor

The game provides players with the opportunity to create new levels.
## Getting started
To begin creating new levels follow these steps:
1. Go to the game directory (you have to be in package TheGreatEscape).
2. Run the following command to start the editor:
   ```
   Dexec.mainClass="org.game.thegreatescape.levelEditor.LevelEditor"
   ```
After starting, two windows will appear: 
* The aside item stage displays a collection of image items that are used for level editing.
* The canvas stage is where the player designs his game level.

  <img width="647" alt="Screenshot 2024-05-01 at 20 46 28" src="https://github.com/ofgot/TheGreatEscape/assets/113288163/d2a7a9a6-0084-40f7-baf0-bbe35adaab99">

# Object Oriented Design
<img width="509" alt="Screenshot 2024-05-01 at 20 47 26" src="https://github.com/ofgot/TheGreatEscape/assets/113288163/d0fb8215-b3e6-4725-b46d-04a3e8942e89">


