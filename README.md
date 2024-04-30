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
