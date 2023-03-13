package com.ilyes.web;

import com.ilyes.dao.CategorieDao;
import com.ilyes.dao.CategorieDaoImpl;
import com.ilyes.dao.ProduitDao;
import com.ilyes.dao.ProduitDaoImpl;
import entities.Categorie;
import entities.Produits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "produitServlet", urlPatterns = {"/produits", "*.do"})
public class ProduitServlet extends HttpServlet {
    ProduitDao metier;
    CategorieDao metierCategorie;
    @Override
    public void init() throws ServletException {
        metier = new ProduitDaoImpl();
        metierCategorie = new CategorieDaoImpl();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();
        if (path.equals("/index.do"))
        {
            request.getRequestDispatcher("produits.jsp").forward(request,response);
        }
        else if (path.equals("/chercher.do"))
        {
            String motCle=request.getParameter("motCle");
            ProduitModele model= new ProduitModele();
            model.setMotCle(motCle);
            List<Produits> prods = metier.produitsParMC(motCle);
            model.setProduits(prods);
            request.setAttribute("model", model);

            request.getRequestDispatcher("produits.jsp").forward(request,response);
        }
        else if (path.equals("/saisie.do")) {
            List<Categorie> categories = metierCategorie.getAllCategories();
            CategorieModele model = new CategorieModele();
            model.setCategories(categories);
            request.setAttribute("categorieModel", model);
            request.getRequestDispatcher("saisieProduit.jsp").forward(request,response);
        }
        else if (path.equals("/save.do") && request.getMethod().equals("POST")) {
            String nom = request.getParameter("nom");
            int categorieId = Integer.parseInt(request.getParameter("categorie"));
            double prix = Double.parseDouble(request.getParameter("prix"));
            Categorie categorie = metierCategorie.getCategorie(categorieId);
            Produits p = metier.save(new Produits(nom, prix, categorie));
            request.setAttribute("produit", p);
            response.sendRedirect("chercher.do?motCle=");
        }
        else if (path.equals("/supprimer.do")) {
            int idProduit = Integer.parseInt(request.getParameter("id"));
            metier.deleteProduit(idProduit);
            response.sendRedirect("chercher.do?motCle=");
        } else if (path.equals("/editer.do")) {
            int idProduit = Integer.parseInt(request.getParameter("id"));
            Produits p = metier.getProduit(idProduit);
            request.setAttribute("produit", p);
            List<Categorie> categories = metierCategorie.getAllCategories();
            CategorieModele model = new CategorieModele();
            model.setCategories(categories);
            request.setAttribute("categorieModel", model);
            request.getRequestDispatcher("editerProduit.jsp").forward(request,response);
        } else if (path.equals("/update.do") && request.getMethod().equals("POST")) {
            int idProduit = Integer.parseInt(request.getParameter("id"));
            String nom = request.getParameter("nom");
            double prix = Double.parseDouble(request.getParameter("prix"));
            int categorieId = Integer.parseInt(request.getParameter("categorie"));
            Produits p = new Produits();
            p.setIdProduit(idProduit);
            p.setNomProduit(nom);
            p.setPrix(prix);
            Categorie categorie = metierCategorie.getCategorie(categorieId);
            p.setCategorieByIdCategorie(categorie);
            metier.updateProduit(p);
            request.setAttribute("produit", p);
            response.sendRedirect("chercher.do?motCle=");
        }
        else {
            response.sendError(response.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}