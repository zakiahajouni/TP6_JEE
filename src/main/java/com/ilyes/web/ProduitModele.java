package com.ilyes.web;

import entities.Produits;

import java.util.ArrayList;
import java.util.List;

public class ProduitModele {
    private String motCle;
    List<Produits> produits = new ArrayList<>();

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public List<Produits> getProduits() {
        return produits;
    }

    public void setProduits(List<Produits> produits) {
        this.produits = produits;
    }
}
