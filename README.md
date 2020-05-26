Flashcards
==========

A program that allows for the creation, storage, and testing of simple text flashcards.
Supports multiple flashcard sets and tracks how many times a card has been missed.

Prerequisites
--------------
This program requires at least Java 10 to compile and run properly.

Installation
------------

1. Download this repository and unzip the .zip file in your desired location.
2. Using the command line, navigate to \Flashcards-master\src\flashcards.
3. Compile the program using the command `javac Main.java`.

Usage
-----

Once Flashcards has been compiled, it can be run from the command line by navigating to \Flashcards-master\src and using the command `java flashcards.Main`.

The set of cards initially loaded by the program can be selected by running the program with the argument `-import file` where `file` is the path and name of the file containing the cards to be loaded.

Running Flashcards with the argument `-export file` where `file` is a file path and name of a .txt file will cause the program to save any currently loaded cards when the program exits.
If the specified file does not already exist, it will be created; otherwise, the existing file will be overwritten.

Once the program is running, the user is offered the following options: add, remove, import, export, ask, exit, log, hardest card, and reset stats.
An option is selected by inputting the text corresponding to the action to take.

**Add:**

Select this option to add a card. The program will ask for a card name and then a card definition.

Flashcards does not allow for the creation of multiple cards with the same name or definition.
If the user attempts to create a card with a duplicate name or definition, they will be redirected to the main menu.

Card names and definitions should not contain `:::` (three colons consecutively).
This series of characters is used in storing the card sets in external files.

**Remove:**

Select this option to remove a card. The program will ask for name of the card to be removed.
If there is no card with the specified name, the user will be redirected to the main options menu.

**Import:**

Select this option to import cards from a file. The program will ask for the file name. It should be a .txt file.

**Export:**

Select this option to save all currently loaded cards to a file. The program will ask for the file name. It should be a .txt file.
If the specified file does not already exist, it will be created; otherwise, the existing file will be overwritten.

**Ask:**

Select this option to be quizzed on the currently loaded cards. Flashcards will ask how many cards it should ask the user about.
This number should be input as an integer, i.e. `11`.
Flashcards will then ask the user a randomly selected card the number of times entered. Cards may be repeated.

Flashcards keeps track of how many times the user answers each card mistakenly.

**Exit:**

Select this option to exit the program.
If Flashcards was run with the argument `-export`, all currently loaded cards will be saved to the file specified at start up.

**Log:**

Select this option to save a log of the current session. The program will ask for the file name. It should be a .txt file.
If the specified file does not already exist, it will be created; otherwise, the existing file will be overwritten.

**Hardest Card:**

Select this option to find out which card has been missed the most times.
The card name and the number of times it has been missed will be displayed, and then the user will be redirected to the main menu.

**Reset Stats:**

Select this option to reset the number of mistakes for all currently loaded cards to zero.

Authors
-------

* **Kate Jordan** - [sinnenicht](https://github.com/sinnenicht/)

License
-------

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](https://github.com/sinnenicht/Flashcards/blob/master/LICENSE) for details.
