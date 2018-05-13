/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author bctnry
 */
public class ParseTree {
    private Token root;
    private List<ParseTree> child;
    public ParseTree() {
        this.root = null;
        this.child = new ArrayList<>();
    }
    public ParseTree(Token root, List<ParseTree> child) {
        this.root = root;
        this.child = child;
    }
    public ParseTree(Token root, ParseTree[] child) {
        this.root = root;
        this.child = new ArrayList<>();
        this.child.addAll(Arrays.asList(child));
    }
    public List<ParseTree> getChild() {
        return this.child;
    }
    public Token getRoot() {
        return this.root;
    }
    public void addChild(ParseTree newChild) {
        this.child.add(newChild);
    }
    public void setChild(List<ParseTree> childs) {
        this.child = childs;
    }
    public void setNthChild(int n, ParseTree newChild) {
        this.child.set(n, newChild);
    }
    public void setRoot(Token token) {
        this.root = token;
    }
    public static void display(ParseTree tree, String indent) {
        if(tree.getChild() == null || tree.getChild().isEmpty()) {
            System.out.println(indent + tree.getRoot());
        } else {
            System.out.println(indent + tree.getRoot() + "{");
            tree.getChild().forEach((subtree) -> {
                ParseTree.display(subtree, indent + "    ");
            });
            System.out.println(indent + "}");
        }
    }
}
