package com.company;

public class Frame {
    private Page page;

    public Frame(){
        this.page = new Page(-1);
    }

    public void setPage(Page page){
        this.page = page;
    }

    public void setNextUseTime(int time){
        this.page.setNextUseTime(time);
    }

    public void setLastTimeUsed(int time){
        this.page.setLastTimeUsed(time);
    }

    public int getNextUseTime(Page page){
        return page.getNextUseTime();
    }

    public int getLastTimeUsed(Page page){
        return page.getLastTimeUsed();
    }

}
