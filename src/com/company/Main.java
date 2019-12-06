package com.company;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File inputFile;
        int numPages;
        int numFrames;
        int numRequests;

        if (args.length != 1){
            inputFile = new File("./input.txt");
        } else{
            inputFile = new File(args[0]);
        }

        if (!inputFile.exists()){
            System.out.println("Input File: " + inputFile.getName() + " does not exist!\nTerminating...");
            System.exit(1);
        } else{
            if (!inputFile.canRead()){
                System.out.println("Cannot read from File: " + inputFile.getName());
                System.out.println("Terminating...");
                System.exit(1);
            }
        }

        try{
            Scanner scanner = new Scanner(inputFile);
            numPages = scanner.nextInt();
            numFrames = scanner.nextInt();
            numRequests = scanner.nextInt();

            fifo(scanner, numPages, numFrames, numRequests);


        } catch (IOException e){
            e.printStackTrace();
        }
        /*
        System.out.println("Num Pages: " + numPages);
        System.out.println("Num Frames: " + numFrames);
        System.out.println("Num Requests: " + numRequests);
        */
        System.exit(0);
    }

    public static void fifo(Scanner scanner, int numPages, int numFrames, int numRequests) {
        System.out.println("FIFO");

        // Create an array of page numbers <Page>
        Page[] pages = new Page[numPages];
        for (int i = 0; i < numPages; i++){
            pages[i] = new Page(i);
        }

        // Create an array of frames <Page>
        Page[] frames = new Page[numFrames];
        for (int i = 0; i < numFrames; i++){
            frames[i] = new Page(-1);
        }

        // Create an array (queue) of page requests <Page>
        Page[] queue = new Page[numRequests];
        for (int i = 0; i < numRequests; i++){
            queue[i] = new Page(scanner.nextInt());
        }

        // Process queue
        int j = 0;
        int frameIndex;
        int pageFaults = 0;
        for (int i = 0; i < numRequests; i++){
            // Test if the item in the queue is already in the frame (-1 = does not exist)
            frameIndex = ifExist(queue[i], frames);
            if (frameIndex == -1){
                pageFaults++;
                if (frames[j].getPageNum() != -1){
                    System.out.println("Page " + frames[j].getPageNum() + " unloaded from Frame " + j +", Page " + queue[i].getPageNum() + " loaded into Frame " + j);
                } else{
                    System.out.println("Page " + queue[i].getPageNum() + " loaded into frame " + j);
                }
                frames[j] = queue[i];
                j = (j + 1) % numFrames;
            } else{
                System.out.println("Page " + queue[i].getPageNum() + " already in frame " + frameIndex);
            }
        }
        System.out.println(pageFaults + " page faults");
    }

    public static void LRU(Scanner scanner, int numPages, int numFrames, int numRequests) {
        System.out.println("LRU");

        // Create an array of page numbers <Page>
        Page[] pages = new Page[numPages];
        for (int i = 0; i < numPages; i++){
            pages[i] = new Page(i);
        }

        // Create an array of frames <Page>
        Page[] frames = new Page[numFrames];
        for (int i = 0; i < numFrames; i++){
            frames[i] = new Page(-1);
        }

        // Create an array (queue) of page requests <Page>
        Page[] queue = new Page[numRequests];
        for (int i = 0; i < numRequests; i++){
            queue[i] = new Page(scanner.nextInt());
        }

        // Process queue
        int j = 0;
        int frameIndex;
        int pageFaults = 0;
        for (int i = 0; i < numRequests; i++){
            // Test if the item in the queue is already in the frame (-1 = does not exist)
            frameIndex = ifExist(queue[i], frames);
            if (frameIndex == -1){
                pageFaults++;
                if (frames[j].getPageNum() != -1){
                    System.out.println("Page " + frames[j].getPageNum() + " unloaded from Frame " + j +", Page " + queue[i].getPageNum() + " loaded into Frame " + j);
                } else{
                    System.out.println("Page " + queue[i].getPageNum() + " loaded into frame " + j);
                }
                frames[j] = queue[i];
                j = (j + 1) % numFrames;
            } else{
                System.out.println("Page " + queue[i].getPageNum() + " already in frame " + frameIndex);
            }
        }
        System.out.println(pageFaults + " page faults");
    }

    private static int ifExist(Page page, Page[] frames){
        for (int i = 0; i < frames.length; i++){
            if (frames[i].getPageNum() == page.getPageNum()){
                return i;
            }
        }
        return -1;
    }
}
