package com.descodeuses.planit.exercicetp;

import java.util.ArrayList;
import java.util.Scanner;

public class Exercice1 {

    // Liste pour stocker les tâches
    private static ArrayList<String> taches = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            // Affichage du menu
            System.out.println("\n=== MENU ===");
            System.out.println("1. Ajouter une tâche");
            System.out.println("2. Voir les tâches");
            System.out.println("3. Marquer une tâche comme faite");
            System.out.println("4. Supprimer une tâche");
            System.out.println("5. Quitter");
            System.out.print("Entrez votre choix : ");

            // Lecture du choix
            choix = scanner.nextInt();
            scanner.nextLine(); // consomme le retour à la ligne

            // Appel des méthodes en fonction du choix
            switch (choix) {
                case 1:
                    ajouterTache(scanner);
                    break;
                case 2:
                    voirTaches();
                    break;
                case 3:
                    marquerCommeFaite(scanner);
                    break;
                case 4:
                    supprimerTache(scanner);
                    break;
                case 5:
                    quitter();
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choix != 5);

        scanner.close();
    }

    // 1. Ajouter une tâche
    public static void ajouterTache(Scanner scanner) {
        System.out.print("Entrez la tâche à ajouter : ");
        String tache = scanner.nextLine();
        taches.add(tache);
        System.out.println("Tâche ajoutée !");
    }

    // 2. Voir les tâches
    public static void voirTaches() {
        if (taches.isEmpty()) {
            System.out.println("Aucune tâche enregistrée.");
        } else {
            System.out.println("Liste des tâches :");
            for (int i = 0; i < taches.size(); i++) {
                System.out.println((i + 1) + ". " + taches.get(i));
            }
        }
    }

    // 3. Marquer une tâche comme faite
    public static void marquerCommeFaite(Scanner scanner) {
        voirTaches();
        if (!taches.isEmpty()) {
            System.out.print("Numéro de la tâche à marquer comme faite : ");
            int index = scanner.nextInt();
            scanner.nextLine(); // consommer le retour à la ligne
            if (index >= 1 && index <= taches.size()) {
                String ancienne = taches.get(index - 1);
                taches.set(index - 1, "[FAIT] " + ancienne);
                System.out.println("Tâche marquée comme faite.");
            } else {
                System.out.println("Numéro invalide.");
            }
        }
    }

    // 4. Supprimer une tâche
    public static void supprimerTache(Scanner scanner) {
        voirTaches();
        if (!taches.isEmpty()) {
            System.out.print("Numéro de la tâche à supprimer : ");
            int index = scanner.nextInt();
            scanner.nextLine(); // consommer le retour à la ligne
            if (index >= 1 && index <= taches.size()) {
                taches.remove(index - 1);
                System.out.println("Tâche supprimée.");
            } else {
                System.out.println("Numéro invalide.");
            }
        }
    }

    // 5. Quitter
    public static void quitter() {
        System.out.println("Au revoir !");
    }
}

