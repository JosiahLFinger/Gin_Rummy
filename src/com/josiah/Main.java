package com.josiah;;

import java.util.*;

public class Main {

    private static Scanner sc;

    public static void main(String[] args) {

        //creates arraylists for players and decks
        ArrayList stockDeck = new ArrayList();
        ArrayList discardDeck = new ArrayList();
        ArrayList myMeld = new ArrayList();
        ArrayList pcMeld = new ArrayList();
        ArrayList<ArrayList> meld = new ArrayList<ArrayList>();

        String card = "";

        sc = new Scanner(System.in);

        boolean gameOver = false;
        boolean firstTime = true;

        //figures out if you want to start the game (just 2 player for now)
        System.out.println("Player who reaches a score of 100 loses! Good Luck! \nPress enter to begin");
        String yesNo = sc.nextLine();
        yesNo.toLowerCase();
        Players computer = new Players("Bob");
        Players player = new Players("Me");

        //when enter is pressed
        try {
            if (yesNo != "n" && yesNo != "no") {
                //creates a new deck
                fillDeck(stockDeck, discardDeck, player, computer);
                //shuffles the deck and deals out 7 cards to all players
                shuffleAndDealNewHand(stockDeck, player, computer);
            } else {
                System.exit(0);
            }
            //displays computer's hand as "[ ] [ ] [ ] [ ]"
            computer.displayHand(computer);
            player.displayHand(player);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }


        //game begins. Ends as soon as a player hits a score of 100
        while (gameOver == false) {
            //in first draw discard is empty so this plays first
            while (firstTime == true) {
                //switches firstTime boolean to false so player can draw from discard now
                firstTime = false;
                System.out.println("There are no cards in the discard pile. \n "
                        + "Hit 1 to draw a stock card!");
                //gets player input and validates it
                int choice = getPositiveIntInput();
                //1 draws from stockDeck
                if (choice == 1) {
                    //player draws card from stock
                    drawFromStock(stockDeck, player);
                    //player discards a card from hand
                    discardCard(discardDeck, player);
                    //computer draws card and discards. AI is currently completely random.
                    computerTurn(computer, stockDeck, discardDeck);
                } else {
                    System.out.println("You can only draw from the stock deck on your first turn!");
                }
            }
            //after first turn, choices are then displayed
            System.out.println("\nPick a number:\n" + "1. Draw card from stock pile with " + stockDeck.size() + " cards \n" + "2. Draw card from discard pile.\n"
                    + "3. Display table/hands.\n" + "4. Knock and lay down some cards.\n");
            //validates user input
            int choice = getPositiveIntInput();
            //switches based on the number player chose
            switch (choice) {
                case 1: {
                    //draws from stock to player hand
                    drawFromStock(stockDeck, player);
                    //discards card from hand to discard deck
                    discardCard(discardDeck, player);
                    //computer cycles his turn
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                }
                case 2: {
                    //draws card from discard to player's hand
                    drawFromDiscard(discardDeck, player);
                    //discards card from player's hand to discard deck
                    discardCard(discardDeck, player);
                    //computer cycles his turn
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                }
                case 3: {
                    //displays computer hand, stockdeck size, discard size and top card, and player's hand
                    displayTable(stockDeck, discardDeck, player, computer);
                    break;
                }
                case 4: {
                    //displays player's hand
                    player.displayHand(player);
                    //method for choosing cards to lay down after knocking
                    meldCard(meld, myMeld, player);
                    //computer chooses cards to lay down (random)
                    meldCard(meld, pcMeld, computer);
                    //displays melds
                    displayMeld(meld);
                    //player decides if they can meld cards to current meld piles
                    System.out.println("Do you have any cards you can meld? 1 for yes. 2 for no");
                    //validates user input
                    int answer = getPositiveIntInput();
                    if (answer == 1) {
                        //allows players to add card to specific melds
                        meld(meld, myMeld, player);
                        meld(meld, pcMeld, computer);
                    } else {
                        //adds the total of cards still in hand and checks if the game is over
                        addScore(player, computer, gameOver);
                    }
                    //changes boolean to true if player or computer score reaches 100 or more
                    gameOver = checkScore(player, computer);
                    //clears hands, decks, and melds for next hand.
                    meld.clear();
                    myMeld.clear();
                    pcMeld.clear();
                    player.clearHand();
                    computer.clearHand();
                    //generates new deck
                    fillDeck(stockDeck, discardDeck, player, computer);
                    //shuffles and deals new hands
                    shuffleAndDealNewHand(stockDeck, player, computer);
                    //displays table
                    displayTable(stockDeck, discardDeck, player, computer);
                    //switches firstTime to true so new hand can begin
                    firstTime = true;
                    break;
                }
                default:
            }
        }
        //after game is over, checks who has 100 or more and declares the other player the winner.
        if (computer.getScore() >= 100) {
            System.out.println("Congratulations! You beat " + computer.getName());
            System.out.println(player.getName() + "'s score: " + computer.getScore());
            System.out.println("Your score: " + player.getScore());
        } else {
            System.out.println(computer.getName() + " beat you this time.");
            System.out.println(player.getName() + "'s score: " + computer.getScore());
            System.out.println("Your score: " + player.getScore());
        }
        sc.close();
    }

    private static boolean checkScore(Players player, Players computer) {
        //checks if a player has more than 100 and changes gameOver boolean to true if someone does.
        if (player.getScore() > 100) {
            return true;
        } else if (computer.getScore() > 100) {
            return true;
        } else {
            return false;
        }
    }

    private static void shuffleAndDealNewHand(ArrayList stockDeck, Players player, Players computer) {
        // shuffle the deck before we begin.
        Collections.shuffle(stockDeck);

        for (int x = 0; x < 7; x++) {
            Random rand = new Random();
            //picks random number of the cards that are left in the "stock" pile.
            int random = rand.nextInt(stockDeck.size());
            //saves it temporarily
            String temp = String.valueOf(stockDeck.get(random));
            //adds the object to the players hand arraylist
            player.addCard(temp);
            //deletes the object from the "stock" pile of cards so it can't be drawn again.
            stockDeck.remove(random);
            //does the same for the computer
            random = rand.nextInt(stockDeck.size());
            temp = String.valueOf(stockDeck.get(random));
            computer.addCard(temp);
            stockDeck.remove(random);
        }
    }

    private static void addScore(Players player, Players computer, boolean gameOver) {
        //holds the players earned points
        int tempScore = 0;
        //for every card in the player's hand
        for (int x = 0; x < player.getHand().size(); x++) {
            int cardValue = 0;
            //gets the card string
            String card = player.getHand().get(x);
            //splits the number from the suit
            String[] parts = card.split(" ");
            //checks the first part which holds either a number or a face card.
            //If it is a face card, it checks how much that face card is worth.
            if (parts[0].equals("Ace")) {
                cardValue = 1;
            } else if (parts[0].equals("Jack")) {
                cardValue = 10;
            } else if (parts[0].equals("Queen")) {
                cardValue = 10;
            } else if (parts[0].equals("King")) {
                cardValue = 10;
            } else {
                cardValue = Integer.parseInt(parts[0]);
            }
            //adds cardvalue to the tempScore
            tempScore = tempScore + cardValue;
        }
        //displays points gained
        System.out.println("Your hand had " + (tempScore) + " points.");
        player.setScore(player.getScore() + tempScore);
        tempScore = 0;
        //repeats for computer
        for (int x = 0; x < computer.getHand().size(); x++) {
            int cardValue = 0;
            String card = computer.getHand().get(x);
            String[] parts = card.split(" ");
            if (parts[0].equals("Ace")) {
                cardValue = 1;
            } else if (parts[0].equals("Jack")) {
                cardValue = 10;
            } else if (parts[0].equals("Queen")) {
                cardValue = 10;
            } else if (parts[0].equals("King")) {
                cardValue = 10;
            } else {
                cardValue = Integer.parseInt(parts[0]);
            }
            tempScore = tempScore + cardValue;
        }
        //displays final scores gained that round
        System.out.println("Your hand had " + tempScore + " points.");
        computer.setScore(computer.getScore() + tempScore);

        System.out.println(computer.getName() + "'s total score: " + computer.getScore());
        System.out.println("Your total score: " + player.getScore());
    }

    private static void meld(ArrayList<ArrayList> meld, ArrayList hand, Players player) {
        Scanner sc = new Scanner(System.in);
        //checks whose turn it is
        String name = String.valueOf(player.getName());
        //if it's not the computer's turn...
        if (name != "Bob") {
            System.out.println("How many cards do you want to meld?");
            //validates user input
            int cardNum = getPositiveIntInput();
            System.out.println("Which meld pile do you want to use?");
            //validates user input - 1 to match array index
            int meldPile = getPositiveIntInput() - 1;
            //displays player hand so they can see new indexes
            player.displayHand(player);
            //displays the meld pile
            System.out.println("Meld Pile: " + meld.get(meldPile) + "\n");
            //saves the size of meld pile
            int pileSize = meld.get(meldPile).size();
            //compares current meld pile size with what it will be after player adds the number of cards they wanted to meld.
            while (meld.get(meldPile).size() < pileSize + cardNum) {
                System.out.println("Select a card from your hand");
                //validates user input - 1 to match array indexes
                int value = getPositiveIntInput() - 1;
                //saves card string and asks to put it in the front of the meld pile or the back.
                String card = String.valueOf(player.getHand().get(value));
                System.out.println("Do you want to add " + card + " to the front or the back of meld.\n"
                        + "Hit 1 for front or 2 for back.");
                int frontBack = getPositiveIntInput();
                //puts card where player wants it then deletes the copy from their hand
                if (frontBack == 1) {
                    meld.get(meldPile).add(0, card);
                    player.getHand().remove(card);
                    continue;
                } else {
                    meld.get(meldPile).add(card);
                    player.getHand().remove(card);
                    continue;
                }
            }
            //displays the new meldpile
            System.out.println("Meld Pile: " + meld.get(meldPile) + "\n");
            //computer's turn
        } else {
            //random is for a simple computer AI. He will either meld a card or not.
            Random rand = new Random();
            int choice = rand.nextInt(2);
            int meldPile = rand.nextInt(meld.size() - 1);
            //1 means he will meld
            if (choice == 1) {
                // before rules are set in place, computer just picks a random card from his hand and melds it.
                int cardNum = rand.nextInt(player.getHand().size());
                String card = String.valueOf(player.getHand().get(cardNum));
                int frontBack = rand.nextInt(2);
                // 0 adds in front of pile
                if (frontBack == 0) {
                    meld.get(meldPile).add(0, card);
                    player.getHand().remove(card);
                    System.out.println(player.getName() + " added " + card + " to the front of meld pile " + meld.get(meldPile));
                    //1 adds to the back of the pile
                } else {
                    meld.get(meldPile).add(card);
                    player.getHand().remove(card);
                    System.out.println(player.getName() + " added " + card + " to the back of meld pile " + meld.get(meldPile));
                }
            } else {
                System.out.println(player.getName() + " did not meld on his turn.");
            }
        }
    }

    private static void displayTable(ArrayList stockDeck, ArrayList discardDeck, Players player, Players computer) {
        computer.displayHand(computer);
        int temp = discardDeck.size();
        System.out.println("\n");
        System.out.println("Stock Deck:");
        System.out.println(stockDeck.size() + " cards\n");
        System.out.println("Discard Deck:");
        if (discardDeck.size() == 0) {
            System.out.println(discardDeck.size() + " cards\n");
        } else {
            System.out.println(discardDeck.get(temp) + "\n" + discardDeck.size() + " cards\n");
        }
        player.displayHand(player);
    }

    private static void discardCard(ArrayList discardDeck, Players player) {
        String name = String.valueOf(player.getName());
        if (name != "Bob") {
            Scanner discardScanner = new Scanner(System.in);
            System.out.println("Enter the number that is to the left of the card you would like to discard.");
            int cardNum = discardScanner.nextInt() - 1;
            String card = String.valueOf(player.getHand().get(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
            System.out.println(player.getName() + " discarded " + card);
            //computer ai is random so this plays for the computer to discard a card.
        } else {
            Random rand = new Random();
            int cardNum = rand.nextInt(player.getHand().size());
            String card = String.valueOf(player.getHand().get(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
        }
    }

    private static void drawFromStock(ArrayList stockDeck, Players player) {
        //saves top card temporarily
        String temp = String.valueOf(stockDeck.get(stockDeck.size() - 1));
        //adds the object to the players hand arraylist
        player.addCard(temp);
        //displays players hand
        player.displayHand(player);
        //removes the top card from the stock deck so there are no duplicate cards in the current game.
        stockDeck.remove(stockDeck.size() - 1);
    }

    private static void drawFromDiscard(ArrayList discardDeck, Players player) {
        //saves it temporarily
        String temp = String.valueOf(discardDeck.get(discardDeck.size() - 1));
        //adds the object to the players hand arraylist, displays their hand, and removes the copy from the discard
        player.addCard(temp);
        player.displayHand(player);
        discardDeck.remove(discardDeck.size() - 1);
    }

    //TODO add computer ai rather than just random

    private static void computerTurn(Players computer, ArrayList stockDeck, ArrayList discardDeck) {
        //computer's AI is random
        Random rand = new Random();
        boolean choice = rand.nextBoolean();
        //if true draws from stock, if false, draws from discard.
        if (choice == true) {
            System.out.println(computer.getName() + " draws from the stock pile.");
            drawFromStock(stockDeck, computer);
            discardCard(discardDeck, computer);
            //if discard is empty, automatically draws from stock
        } else if (discardDeck.size() == 0) {
            System.out.println(computer.getName() + " draws from the stock pile.");
            drawFromStock(stockDeck, computer);
            discardCard(discardDeck, computer);


        } else if (choice == false) {
            System.out.println(computer.getName() + " draws from the discard pile.");
            drawFromDiscard(discardDeck, computer);
            discardCard(discardDeck, computer);
        }


    }

    private static void meldCard(ArrayList<ArrayList> meld, ArrayList hand, Players player) {
        String name = String.valueOf(player.getName());
        //first if statement runs if it is not the computer's turn
        if (name != "Bob") {
            System.out.println("How many cards do you want to lay down?");
            //validates user input for number of cards they want to lay down
            int temp = getPositiveIntInput();
            //while the hand size is less than or equal to the number of cards chosen...
            while (hand.size() <= temp - 1) {
                System.out.println("Select a card from your hand");
                int value = getPositiveIntInput() - 1;
                //chosen card is copied, then added to meld pile, then removed from the players hand
                String card = String.valueOf(player.getHand().get(value));
                hand.add(card);
                player.getHand().remove(card);
                //players new hand without card is displayed
                player.displayHand(player);
            }

            //displays meld piles
            System.out.println(player.getName() + " 's meld");
            System.out.print("[");
            for (Object i : hand) {
                System.out.print(String.valueOf(i) + "; ");
            }
            System.out.print("] \n");
            //adds hand to meld pile
            meld.add(hand);
        } else {
            //same thing as above, but with random choices for computer
            System.out.println(player.getName() + " 's meld");
            Random compMeld = new Random();
            boolean choice = compMeld.nextBoolean();
            if (choice = true) {
                while (hand.size() <= 3) {
                    Random ran = new Random();
                    int temp = ran.nextInt(player.getHand().size() - 1);
                    String card = String.valueOf(player.getHand().get(temp));
                    while (hand.contains(card)) {
                        temp = ran.nextInt(player.getHand().size() - 1);
                        card = String.valueOf(player.getHand().get(temp));
                    }
                    hand.add(card);
                    player.getHand().remove(card);
                }

                System.out.print("[");
                for (Object i : hand) {
                    System.out.print(String.valueOf(i) + "; ");
                }
                System.out.print("]");
                meld.add(hand);
            } else {
                System.out.println(player.getName() + " didn't want to lay and cards down.");
            }
        }
    }

    //TODO check cards to make sure play is legal

    private static void displayMeld(ArrayList<ArrayList> meld) {

        //formats and displays meld
        System.out.println("here is your present meld: \n");
        System.out.print("[ ");
        for (int y = 0; y < meld.size(); y++) {
            System.out.print(+(y + 1) + ". " + meld.get(y) + " | ");
        }
        System.out.println(" ]");
        System.out.println();
    }

    public static void fillDeck(ArrayList stockDeck, ArrayList discardDeck, Players player, Players computer) {
        //clears all hands and decks everytime new deck is made
        stockDeck.clear();
        discardDeck.clear();
        player.getHand().clear();
        //changes numbers to face values
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 4; y++) {
                String face = "";
                if (x == 0) {
                    face = "Ace";
                } else if (x == 10) {
                    face = "Jack";
                } else if (x == 11) {
                    face = "Queen";
                } else if (x == 12) {
                    face = "King";
                } else {
                    face = Integer.toString(x) + 1;
                }
        //adds a suit to the number
                String temp = "";
                if (y == 0) {
                    temp = "Hearts";
                } else if (y == 1) {
                    temp = "Clubs";
                } else if (y == 2) {
                    temp = "Diamonds";
                } else if (y == 3) {
                    temp = "Spades";
                }
                //mashes into one string
                String card = face + " " + temp;
                //adds it to the stock deck
                stockDeck.add(card);
            }
        }
    }

    //validation
    private static int getPositiveIntInput() {
        while (true) {
            try {
                String stringInput = sc.nextLine();
                int intInput = Integer.parseInt(stringInput);
                if (intInput >= 0) {
                    return intInput;
                } else {
                    System.out.println("Please enter a positive number");
                    continue;
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please type a positive number");
                String dumpRestOfInput = sc.nextLine();  //Force scanner to throw away the last (invalid) input
            }
        }
    }
}