package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static String[] importAndExport = new String[2];
    public static File importOnStart = new File("./" + importAndExport[0]);
    public static File exportOnExit = new File("./" + importAndExport[1]);

    public static Scanner userInput = new Scanner(System.in);

    public static boolean isRunning = true;
    public static State currentState = State.CHOOSING_AN_ACTION;

    public static Map<String, String> cardsByTerm = new HashMap<>();
    public static Map<String, String> cardsByDefinition = new HashMap<>();
    public static Map<String, Integer> difficultyByTerm = new HashMap<>();

    public static ArrayList<String> currentLog = new ArrayList<>();

    public static void main(String[] args) {
        int numberOfArgs = args.length;
        for (int index = 0; index < numberOfArgs; index++) {
            if ("-import".equals(args[index])) {
                importAndExport[0] = args[index + 1];
            } else if ("-export".equals(args[index])) {
                importAndExport[1] = args[index + 1];
            }
        }

        if (importAndExport[0] != null) {
            importCards(importOnStart);
        }

        while (isRunning) {
            programRuns(currentState);
        }

        if (importAndExport[1] != null) {
            exportCards(exportOnExit);
        }
    }

    enum State {
        CHOOSING_AN_ACTION, ADD, REMOVE, IMPORT, EXPORT, ASK, LOG, HARDEST_CARD, RESET_STATS
    }

    public static void programRuns(State state) {
        switch (state) {
            case CHOOSING_AN_ACTION:
                printAndLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
                String action = userInput.nextLine();
                logUserInput(action);
                switch (action) {
                    case "add":
                        currentState = State.ADD;
                        break;
                    case "remove":
                        currentState = State.REMOVE;
                        break;
                    case "import":
                        currentState = State.IMPORT;
                        break;
                    case "export":
                        currentState = State.EXPORT;
                        break;
                    case "ask":
                        currentState = State.ASK;
                        break;
                    case "exit":
                        System.out.println("Bye, bye!");
                        isRunning = false;
                        break;
                    case "log":
                        currentState = State.LOG;
                        break;
                    case "hardest card":
                        currentState = State.HARDEST_CARD;
                        break;
                    case "reset stats":
                        currentState = State.RESET_STATS;
                }
                break;
            case ADD:
                addCard();
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case REMOVE:
                removeCard();
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case IMPORT:
                File fileToImport = new File("./" + askFileName());
                importCards(fileToImport);
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case EXPORT:
                File fileToExport = new File("./" + askFileName());
                exportCards(fileToExport);
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case ASK:
                quizUser();
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case LOG:
                File logFile = new File("./" + askFileName());
                saveLog(logFile);
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case HARDEST_CARD:
                displayHardestCard();
                currentState = State.CHOOSING_AN_ACTION;
                break;
            case RESET_STATS:
                resetStats();
                currentState = State.CHOOSING_AN_ACTION;
                break;
        }
    }

    public static void printAndLog(String string) {
        System.out.println(string);
        currentLog.add(string);
    }

    public static void logUserInput(String string) {
        currentLog.add("> " + string);
    }

    public static String askFileName() {
        printAndLog("File name:");
        String filePath = userInput.nextLine();
        logUserInput(filePath);
        return filePath;
    }

    public static void addCard() {
        boolean hasValidTerm = false;
        boolean hasValidDefinition = false;
        printAndLog("The card:");
        String term = userInput.nextLine();
        logUserInput(term);

        if (cardsByTerm.containsKey(term)) {
            printAndLog("The card \"" + term + "\" already exists.");
        } else {
            hasValidTerm = true;
        }

        if (hasValidTerm) {
            printAndLog("The definition of the card:");
            String definition = userInput.nextLine();
            logUserInput(definition);

            if (cardsByTerm.containsValue(definition)) {
                printAndLog("The definition \"" + definition + "\" already exists.");
            } else {
                hasValidDefinition = true;
            }

            if (hasValidDefinition) {
                cardsByTerm.put(term, definition);
                cardsByDefinition.put(definition, term);
                difficultyByTerm.put(term, 0);
                printAndLog("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
            }
        }
    }

    public static void removeCard() {
        printAndLog("The card:");
        String cardToRemove = userInput.nextLine();
        logUserInput(cardToRemove);
        String cardDefinition = cardsByTerm.get(cardToRemove);

        if (cardsByTerm.containsKey(cardToRemove)) {
            cardsByTerm.remove(cardToRemove);
            cardsByDefinition.remove(cardDefinition);
            difficultyByTerm.remove(cardToRemove);
            printAndLog("The card has been removed.");
        } else {
            printAndLog("Can't remove \"" + cardToRemove + "\": there is no such card.");
        }
    }

    public static void importCards(File file) {
        int cardsLoaded = 0;
        boolean fileFound = true;

        try (Scanner fileInput = new Scanner(file)) {
            while (fileInput.hasNext()) {
                String[] card = fileInput.nextLine().split(":::");
                if (cardsByTerm.containsKey(card[0]) && !cardsByTerm.get(card[0]).equals(card[1])) {
                    cardsByDefinition.remove(cardsByTerm.get(card[0]));
                }
                cardsByTerm.put(card[0], card[1]);
                cardsByDefinition.put(card[1], card[0]);
                try {
                    difficultyByTerm.put(card[0], Integer.parseInt(card[2]));
                } catch (NumberFormatException e) {
                    printAndLog("Number format exception: " + e.getMessage());
                }
                cardsLoaded += 1;
            }
        } catch (FileNotFoundException e) {
            fileFound = false;
            printAndLog("File not found.");
        }

        if (fileFound) {
            printAndLog(cardsLoaded + " cards have been loaded.");
        }
    }

    public static void exportCards(File file) {
        int cardsSaved = 0;

        try (PrintWriter writer = new PrintWriter(file)) {
            for (var entry : cardsByTerm.entrySet()) {
                writer.println(entry.getKey() + ":::" + entry.getValue() + ":::" + difficultyByTerm.get(entry.getKey()));
                cardsSaved += 1;
            }
        } catch (IOException e) {
            printAndLog("An exception occurs " + e.getMessage());
        }

        printAndLog(cardsSaved + " cards have been saved.");
    }

    public static void quizUser() {
        List<String> quizPool = new ArrayList<>(cardsByDefinition.keySet());
        Random random = new Random();

        printAndLog("How many times to ask?");
        int timesToAsk = userInput.nextInt();
        String empty = userInput.nextLine();
        logUserInput(timesToAsk + empty);

        for (int timesAsked = 0; timesAsked < timesToAsk; timesAsked++) {
            String currentTerm = cardsByDefinition.get(quizPool.get(random.nextInt(quizPool.size())));
            String correctAnswer = cardsByTerm.get(currentTerm);
            int timesMissed = difficultyByTerm.get(currentTerm);
            printAndLog("Print the definition of \"" + currentTerm + "\":");
            String userAnswer = userInput.nextLine();
            logUserInput(userAnswer);

            if (userAnswer.equals(correctAnswer)) {
                printAndLog("Correct answer.");
            } else if (cardsByDefinition.containsKey(userAnswer)) {
                difficultyByTerm.put(currentTerm, timesMissed + 1);
                String correspondingTerm = cardsByDefinition.get(userAnswer);
                printAndLog("Wrong answer. The correct one is \"" + correctAnswer + "\", you've just written the definition of \"" + correspondingTerm + "\".");
            } else {
                difficultyByTerm.put(currentTerm, timesMissed + 1);
                printAndLog("Wrong answer. The correct one is \"" + correctAnswer + "\".");
            }
        }
    }

    public static void saveLog(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : currentLog) {
                writer.println(line);
            }
        } catch (IOException e) {
            printAndLog("An exception occurs " + e.getMessage());
        }

        printAndLog("The log has been saved.");
    }

    public static void displayHardestCard() {
        List<String> hardestCards = new ArrayList<>();
        int greatestErrors = 0;

        for (var entry : difficultyByTerm.entrySet()) {
            if (entry.getValue() > greatestErrors) {
                greatestErrors = entry.getValue();
            }
        }

        if (greatestErrors == 0) {
            printAndLog("There are no cards with errors.");
        } else {
            for (var entry : difficultyByTerm.entrySet()) {
                if (entry.getValue() == greatestErrors) {
                    hardestCards.add(entry.getKey());
                }
            }

            if (hardestCards.size() > 1) {
                List<String> textForLog = new ArrayList<>();
                System.out.print("The hardest cards are ");
                textForLog.add("The hardest cards are ");

                for (int index = 0; index < hardestCards.size(); index++) {
                    if (index < hardestCards.size() - 1) {
                        System.out.printf("\"%s\", ", hardestCards.get(index));
                        textForLog.add("\"" + hardestCards.get(index) + "\", ");
                    } else {
                        System.out.printf("\"%s\". You have %d errors answering them. %n", hardestCards.get(index), greatestErrors);
                        textForLog.add("\"" + hardestCards.get(index) + "\". You have " + greatestErrors + " errors answering them. ");
                    }
                }

                StringBuilder buildLogText = new StringBuilder();
                for (String string : textForLog) {
                    buildLogText.append(string);
                }
                currentLog.add(buildLogText.toString());
            } else {
                printAndLog("The hardest card is \"" + hardestCards.get(0) + "\". You have " + greatestErrors + " errors answering it.");
            }
        }
    }

    public static void resetStats() {
        difficultyByTerm.replaceAll((k, v) -> 0);
        printAndLog("Card statistics have been reset.");
    }
}
