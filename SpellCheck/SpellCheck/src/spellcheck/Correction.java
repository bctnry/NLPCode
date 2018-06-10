/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellcheck;

import java.util.List;

/**
 *
 * @author bctnry
 */
public class Correction {
    private final int row, col;
    private final List<String> correctionList;
    private final String strRepresentation;
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public List<String> getCorrectionList() { return this.correctionList; }
    public Correction(int row, int col, List<String> correctionList) {
        this.row = row; this.col = col; this.correctionList = correctionList;
        StringBuilder strbuilder = new StringBuilder();
        strbuilder.append("Correction{Row ").append(row).append(",Col ").append(col)
                .append(",");
        if(correctionList == null) {
            strbuilder.append("empty");
        } else {
            strbuilder.append(correctionList.size()).append(":");
            correctionList.forEach((str) -> {
                strbuilder.append('<').append(str).append('>');
            });
        }
        strbuilder.append("}");
        this.strRepresentation = strbuilder.toString();
    }
    @Override
    public String toString() {
        return this.strRepresentation;
    }
}
