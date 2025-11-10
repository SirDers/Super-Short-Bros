# Super-Short-Bros.
## Description
A short 2D platformer written in JavaFX. This was my Computer Science 2 final project during my first year at University of Wisconsin-Stout. I wanted to challenge myself with a tile-scrolling 2D platformer. This was my first time making a game in Java and I used several work arounds to finish it on time, so there are many areas of improvement in the code.

There are 4 levels:
1. **Grass** -- Easy; teaches controls (Has secret exit)
2. **Savannah** -- Easy; shows player existence of secret exits  (Has secret exit)
3. **Ring City** -- Very difficult; I wanted one challenging level for the project. A level like this would be a final challenge after the player has completed all levels before it.
4. **Chess World** -- Player changes into different chess pieces that have different abilities using L. (This is just a quick demo I made for an idea I had with chess-themed power-ups. It is not well implemented at this time.)

Make your own levels with the level editor too! (For a new empty level, delete or move the level1.bin file from the project folder. Opening the exe will create a new empty level1.bin for you to edit.)

### Features
- Custom level file format
- Fully integrated level editor
- Distinct sprite artwork
- Consistent and smooth game physics

## How to Run
1. Download and Extract the 'SuperShortBros_Muted.zip' under 'Releases'.
2. Extract the zip.
3. Double-click 'SuperShortBros.exe' to start the game.
4. (Windows Defender may warn about this executable because it’s an unsigned first release. The file is safe to run.)

## Controls
### Edit Mode
- '0' -- Toggle Edit Mode.
- 'P' -- Save changes to level.
- '1' to '8' -- Change currently selected block or object. (Press the same number for more blocks/objects of that category.)
- '9' -- Change current Edit Bar.
- 'Q' -- Select Erase tool. (Pressing Q again will turn the eraser orange, which deletes Objects instead -- like enemies or stellas.)
- 'T' -- Change Theme
- '-' -- Disable Edit Mode Toggling, and enable deaths to reset at spawn point.
- Arrow keys -- Change width and height of level.

### Player Movement
- 'A' 'D' -- Left-Right movement
- 'J' -- Run
- 'K' -- Jump
- 'L' -- (Only for Level 4) Use special ability
