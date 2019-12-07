/*
 * CSC139
 * Fall 2019
 * Fourth Assignment (Memory Management)
 * Hendryx, Samuel
 * Section # 02
 * OSs tested on: Athena, Windows 10 (IntelliJ IDE)
 */

package com.company;

import java.io.File;
import java.io.FileWriter;
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

            // Create an array (queue) of page requests <Page>
            Page[] queue = new Page[numRequests];
            for (int i = 0; i < numRequests; i++){
                queue[i] = new Page(scanner.nextInt());
            }

            // Create an array of frames <Page>
            Page[] frames = new Page[numFrames];
            for (int i = 0; i < numFrames; i++){
                frames[i] = new Page(-1);
            }

            setNextUse(queue);

            // Setup file writer
            FileWriter writer = new FileWriter("./output.txt");

            fifo(queue, frames, writer);
            cleanFrames(frames);
            writer.write("\n");
            optimal(queue, frames, writer);
            cleanFrames(frames);
            writer.write("\n");
            lru(queue, frames, writer);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    private static void fifo(Page[] queue, Page[] frames, FileWriter writer) throws IOException{
        writer.write("FIFO\n");

        // Process queue
        int j = 0;
        int frameIndex;
        int pageFaults = 0;
        for (int i = 0; i < queue.length; i++){
            // Test if the item in the queue is already in the frame (-1 = does not exist)
            frameIndex = ifExist(queue[i].getPageNum(), frames);
            if (frameIndex == -1){
                pageFaults++;
                if (frames[j].getPageNum() != -1){
                    writer.write("Page " + frames[j].getPageNum() + " unloaded from Frame " + j +", Page " + queue[i].getPageNum() + " loaded into Frame " + j +"\n");
                } else{
                    writer.write("Page " + queue[i].getPageNum() + " loaded into frame " + j + "\n");
                }
                frames[j].setPageNum(queue[i].getPageNum());
                j = (j + 1) % frames.length;
            } else{
                writer.write("Page " + queue[i].getPageNum() + " already in frame " + frameIndex + "\n");
            }
        }
        writer.write(pageFaults + " page faults\n");
    }

    private static void optimal(Page[] queue, Page[] frames, FileWriter writer) throws IOException{
        writer.write("Optimal\n");

        // Process queue
        int j = 0;
        int k = 0;
        int frameIndex;
        int pageFaults = 0;
        for (int i = 0; i < queue.length; i++){
            // Test if the item in the queue is already in the frame (-1 = does not exist)
            frameIndex = ifExist(queue[i].getPageNum(), frames);
            if (frameIndex == -1){
                pageFaults++;
                if (frames[j].getPageNum() == -1){
                    // If there are empty frames, fill them up
                    writer.write("Page " + queue[i].getPageNum() + " loaded into frame " + j + "\n");
                    frames[j].setPageNum(queue[i].getPageNum());
                    frames[j].setNextUseTime(queue[i].getNextUseTime());
                } else{
                    // Test for latest next used time
                    k = getLatestNextUse(frames);
                    writer.write("Page " + frames[k].getPageNum() + " unloaded from Frame " + k +", Page " + queue[i].getPageNum() + " loaded into Frame " + k + "\n");
                    frames[k].setPageNum(queue[i].getPageNum());
                    frames[k].setNextUseTime(queue[i].getNextUseTime());
                }
                j = (j + 1) % frames.length;
            } else{
                // Page exists in Frame already. Just update next use time.
                writer.write("Page " + queue[i].getPageNum() + " already in frame " + frameIndex + "\n");
                frames[frameIndex].setNextUseTime(queue[i].getNextUseTime());
            }
        }
        writer.write(pageFaults + " page faults\n");
    }

    private static void lru(Page[] queue, Page[] frames, FileWriter writer) throws IOException{
        writer.write("LRU\n");

        // Process queue
        int j = 0;
        int k = 0;
        int frameIndex;
        int pageFaults = 0;
        for (int i = 0; i < queue.length; i++){
            // Test if the item in the queue is already in the frame (-1 = does not exist)
            frameIndex = ifExist(queue[i].getPageNum(), frames);
            if (frameIndex == -1){
                pageFaults++;
                if (frames[j].getPageNum() == -1){
                    writer.write("Page " + queue[i].getPageNum() + " loaded into frame " + j + "\n");
                    frames[j].setPageNum(queue[i].getPageNum());
                    frames[j].setLastTimeUsed(i);
                } else{
                    // Test for longest access time
                    k = getLeastRecentlyUsed(frames);
                    writer.write("Page " + frames[k].getPageNum() + " unloaded from Frame " + k +", Page " + queue[i].getPageNum() + " loaded into Frame " + k + "\n");
                    frames[k].setPageNum(queue[i].getPageNum());
                    frames[k].setLastTimeUsed(i);
                }
                j = (j + 1) % frames.length;
            } else{
                writer.write("Page " + queue[i].getPageNum() + " already in frame " + frameIndex + "\n");
                frames[frameIndex].setLastTimeUsed(i);
            }
        }
        writer.write(pageFaults + " page faults\n");
    }

    private static void cleanFrames(Page[] frames){
            for (int i = 0; i < frames.length; i++){
                frames[i].setPageNum(-1);
                frames[i].setNextUseTime(-1);
                frames[i].setLastTimeUsed(-1);
            }
    }

    private static int ifExist(int pageNum, Page[] frames){
        for (int i = 0; i < frames.length; i++){
            if (frames[i].getPageNum() == pageNum){
                return i;
            }
        }
        return -1;
    }

    private static int getLeastRecentlyUsed(Page[] frames){
        int leastRecentlyUsed = 0;
        int frameIndex = 0;
        for (int i = 0; i < frames.length; i++){
            if (leastRecentlyUsed == 0){
                leastRecentlyUsed = frames[i].getLastTimeUsed();
                frameIndex = i;
            } else {
                if (frames[i].getLastTimeUsed() < leastRecentlyUsed){
                    leastRecentlyUsed = frames[i].getLastTimeUsed();
                    frameIndex = i;
                }
            }
        }
        return frameIndex;
    }

    private static void setNextUse(Page[] queue){
        int temp;
        for (int i = 0; i < queue.length; i++){
            temp = queue[i].getPageNum();

            for (int j = i + 1; j < queue.length; j++){
                if (queue[j].getPageNum() == temp){
                    queue[i].setNextUseTime(j);
                    break;
                } else{
                    queue[i].setNextUseTime(Integer.MAX_VALUE);
                }
            }
        }
    }

    private static int getLatestNextUse(Page[] frames){
        int latestNextUse = 0;
        int frameIndex = 0;
        for (int i = 0; i < frames.length; i++){
            if (frames[i].getNextUseTime() > latestNextUse){
                latestNextUse = frames[i].getNextUseTime();
                frameIndex = i;
            }
        }
        return frameIndex;
    }
}
