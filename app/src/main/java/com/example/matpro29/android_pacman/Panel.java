package com.example.matpro29.android_pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

public class Panel extends View {
    private final int CZEKAJ_DUCH_CYKL=5;//opuźnienie duchów na "skrzyżowaniach"
    private final int SZEROKOSC_PLANSZA=19;//szerokosc planszy
    private final int WYSOKOSC_PLANSZA=22;//wysokosc planszy
    private int ROZMIAR_GRAFIKA;//15;//rozmiar obrazków w px
    private final int LICZBA_POZIOM=2;//liczba poziomów
    private final int[] PUNKTY_MAX={156,193};//liczba wszystkich punktów
    private int kierunekPacman;//kierunek poruszania się pacmana
    private int poziom;//numer planszy
    private int liczbaDuch;//liczba duchów
    private int pozycjaPacman;//początkowa pozycja gracza
    private int staraPozycjaPacman;//poprzednia pozycja gracza
    private int nastepnyKierunekPacman=5;//przechowuje nastepny kierunek Pacmana, 5-brak następnego kierunku
    private boolean start=false;//czy gra wystartowała
    public int gra=1;//stan gry|0-przegrana|1-gra|2-wygrana
    private int[] czekajDuch=new int[4];//przechowuje liczbe cykli, w których duch "zastanawiał się"
    private int[] spijDuch=new int[4];
    private int[] pozycjaDuch=new int[4];//pozycje duchów
    private int[] staraPozycjaDuch=new int[4];//poprzednie pozycje duchów
    private int[] kierunekDuch=new int[4];//kierunek poruszania się duchów
    private int[] historiaDuch=new int[4];//typy pól, na którch są duchy
    private int[] tempDuch=new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla duchów
    private int[] tempPacman=new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla Pacmana
    private int[][] Plansza=new int[][]{//plansze
        new int[]{//poziom 1
            17,11,11,11,11,11,11,11,11,22,11,11,11,11,11,11,11,11,18,
            10,8,8,8,8,8,8,8,8,10,8,8,8,8,8,8,8,8,10,
            10,8,17,18,8,17,11,18,8,10,8,17,11,18,8,17,18,8,10,
            10,8,16,19,8,16,11,19,8,14,8,16,11,19,8,16,19,8,10,
            10,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,
            10,8,15,13,8,12,8,15,11,22,11,13,8,12,8,15,13,8,10,
            10,8,8,8,8,10,8,8,8,10,8,8,8,10,8,8,8,8,10,
            16,11,11,18,8,21,11,13,9,14,9,15,11,23,8,17,11,11,19,
            9,9,9,10,8,10,9,9,9,9,9,9,9,10,8,10,9,9,9,
            11,11,11,19,8,14,9,17,13,9,15,18,9,14,8,16,11,11,11,
            9,9,9,9,8,9,9,10,9,9,9,10,9,9,8,9,9,9,9,
            11,11,11,18,8,12,9,16,11,11,11,19,9,12,8,17,11,11,11,
            9,9,9,10,8,10,9,9,9,9,9,9,9,10,8,10,9,9,9,
            17,11,11,19,8,14,9,15,11,22,11,13,9,14,8,16,11,11,18,
            10,8,8,8,8,8,8,8,8,10,8,8,8,8,8,8,8,8,10,
            10,8,15,18,8,15,11,13,8,14,8,15,11,13,8,17,11,8,10,
            10,8,8,10,8,8,8,8,8,9,8,8,8,8,8,10,8,8,10,
            21,13,8,14,8,12,8,15,11,22,11,13,8,12,8,14,8,15,23,
            10,8,8,8,8,10,8,8,8,10,8,8,8,10,8,8,8,8,10,
            10,8,15,11,11,20,11,13,8,14,8,15,11,20,11,11,13,8,10,
            10,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,
            16,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,19
        },
        new int[]{//poziom 2
            17,11,11,11,11,22,11,11,11,11,11,11,11,11,22,11,11,11,18,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            10,8,17,13,8,14,8,15,11,11,11,11,13,8,14,8,12,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,14,8,15,11,13,8,17,11,11,18,8,15,11,11,19,8,10,
            10,8,8,8,8,8,8,8,10,9,9,10,8,8,8,8,8,8,10,
            10,8,12,8,15,11,13,8,14,9,15,19,8,15,11,11,18,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,16,13,8,12,8,15,11,11,11,11,13,8,12,8,14,8,10,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            16,11,11,11,11,23,8,17,11,11,11,11,18,8,21,11,11,11,19,
            17,11,11,11,11,23,8,16,11,11,11,11,19,8,21,11,11,11,18,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            10,8,17,13,8,14,8,15,11,11,11,11,13,8,14,8,12,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,14,8,15,11,13,8,17,13,9,12,8,15,11,11,19,8,10,
            10,8,8,8,8,8,8,8,10,9,9,10,8,8,8,8,8,8,10,
            10,8,12,8,15,11,13,8,16,11,11,19,8,15,11,11,18,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,16,13,8,12,8,15,11,11,11,11,13,8,12,8,14,8,10,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            16,11,11,11,11,20,11,11,11,11,11,11,11,11,20,11,11,11,19
        }
    };

    private String[] nazwy={//tablica z nazwami obrazków
        "pacmanw", //kod 0
        "pacmand", //kod 1
        "pacmans", //kod 2
        "pacmana", //kod 3
        "duch1", //kod 4
        "duch2", //kod 5
        "duch3", //kod 6
        "duch4", //kod 7
        "tlo1", //kod 8, tło z punktem
        "tlo2", //kod 9, puste tło
        "sciana1", //kod 10
        "sciana2", //kod 11
        "sciana3", //kod 12
        "sciana4", //kod 13
        "sciana5", //kod 14
        "sciana6", //kod 15
        "sciana7", //kod 16
        "sciana8", //kod 17
        "sciana9", //kod 18
        "sciana10", //kod 19
        "sciana11", //kod 20
        "sciana12", //kod 21
        "sciana13", //kod 22
        "sciana14", //kod 23
        "sciana15" //kod 24
    };
    private Drawable obrazy[]=new Drawable[nazwy.length];//tablica z obrazami
    private int width;
    private int height;
    private Context context;
    private int border;



    public Panel(Context context) {
        super(context);this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels;
        ROZMIAR_GRAFIKA = width/SZEROKOSC_PLANSZA;
        border = (width-ROZMIAR_GRAFIKA*SZEROKOSC_PLANSZA)/2;


        for (int i=0; i<nazwy.length; i++) {//pobieranie obrazków do tablicy
            obrazy[i] = this.getResources().getDrawable(context.getResources().getIdentifier(nazwy[i], "drawable", context.getPackageName()));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0; i<SZEROKOSC_PLANSZA; i++) {//Rysowanie obrazów na planszy po zmianie pozycji gracza
            for (int j=0; j<WYSOKOSC_PLANSZA; j++) {
                obrazy[Plansza[poziom][i+SZEROKOSC_PLANSZA*j]].setBounds(border+i*ROZMIAR_GRAFIKA, border+j*ROZMIAR_GRAFIKA, border+i*ROZMIAR_GRAFIKA+ROZMIAR_GRAFIKA, border+j*ROZMIAR_GRAFIKA+ROZMIAR_GRAFIKA);
                obrazy[Plansza[poziom][i+SZEROKOSC_PLANSZA*j]].draw(canvas);
            }
        }
        invalidate();
    }
}
