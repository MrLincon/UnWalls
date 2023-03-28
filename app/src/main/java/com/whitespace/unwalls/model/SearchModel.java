package com.whitespace.unwalls.model;

import java.util.ArrayList;

public class SearchModel {

    private ArrayList<ImageModel> results;

    public ArrayList<ImageModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ImageModel> results) {
        this.results = results;
    }

    public SearchModel(ArrayList<ImageModel> results) {
        this.results = results;
    }
}