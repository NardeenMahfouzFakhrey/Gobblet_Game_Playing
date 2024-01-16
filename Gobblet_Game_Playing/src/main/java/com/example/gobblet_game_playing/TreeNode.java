package com.example.gobblet_game_playing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TreeNode {
    Board board = null;
    GameMove move = null;
    ArrayList<TreeNode> children;
    int score = 0;

    public TreeNode(GameMove move, boolean isMaximizer) {
        this.move = move;
        children = null;
        score = isMaximizer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public GameMove getMove() {
        return move;
    }

    public void sortChildren(boolean isMaximizer) {
        if (isMaximizer) {
            children.sort(Comparator.comparing(TreeNode::getScore).reversed());
        }else{
            children.sort(Comparator.comparing(TreeNode::getScore));
        }
    }


    public void printChildren(TreeNode node){

        if(node.children == null)
            return;

        for (TreeNode child: node.children){
            System.out.print(child.score + " ");
        }
        System.out.println();
        for (TreeNode child: node.children){
            printChildren(child);
        }
        System.out.println();




    }


}
