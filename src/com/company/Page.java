package com.company;

public class Page {
    // Instance Variables
    int pageNum;
    int nextUseTime;
    int lastTimeUsed;

    // Constructors
    public Page(){
        this.pageNum = -1;
        this.nextUseTime = 0;
        this.lastTimeUsed = 0;
    }

    public Page(int pageNum){
        this.pageNum = pageNum;
        this.nextUseTime = 0;
        this.lastTimeUsed = 0;
    }

    public Page(int pageNum, int nextUseTime){
        this.pageNum = pageNum;
        this.nextUseTime = nextUseTime;
        this.lastTimeUsed = 0;
    }

    // Getter functions
    public int getPageNum(){
        return pageNum;
    }

    public int getNextUseTime(){
        return nextUseTime;
    }

    public int getLastTimeUsed(){
        return lastTimeUsed;
    }

    // Setter functions
    public void setPageNum(int pageNum){
        this.pageNum = pageNum;
    }

    public void setNextUseTime(int time){
        this.nextUseTime = time;
    }

    public void setLastTimeUsed(int time){
        this.lastTimeUsed = time;
    }

    // toString
    @Override
    public String toString(){
        return( "Page Num: " + pageNum +"\n" +
                "Next Use Time: " + nextUseTime +"\n" +
                "Last Time Used: " + lastTimeUsed + "\n");
    }
}
