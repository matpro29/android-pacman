package com.example.matpro29.android_pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PanelView extends View {
    private final int CZEKAJ_DUCH_CYKL = 5;//opuźnienie duchów na "skrzyżowaniach"
    private final int SZEROKOSC_PLANSZA = 19;//szerokosc planszy
    private final int WYSOKOSC_PLANSZA = 22;//wysokosc planszy
    private final int SZYBKOSC_GRY = 15;//szybkosc gry
    private int ROZMIAR_GRAFIKA;//rozmiar obrazków w px
    private final int LICZBA_POZIOM = 2;//liczba poziomów
    private final int[] PUNKTY_MAX = {156, 194};//liczba wszystkich punktów
    private int kierunekPacman;//kierunek poruszania się pacmana
    private int poziom;//numer planszy
    private int liczbaDuch;//liczba duchów
    private int pozycjaPacman;//początkowa pozycja gracza
    private int staraPozycjaPacman;//poprzednia pozycja gracza
    private int nastepnyKierunekPacman = 5;//przechowuje nastepny kierunek Pacmana, 5-brak następnego kierunku
    private boolean start = false;//czy gra wystartowała
    private int gra = 1;//stan gry|0-przegrana|1-gra|2-wygrana
    private int[] czekajDuch = new int[4];//przechowuje liczbe cykli, w których duch "zastanawiał się"
    private int[] spijDuch = new int[4];
    private int[] pozycjaDuch = new int[4];//pozycje duchów
    private int[] staraPozycjaDuch = new int[4];//poprzednie pozycje duchów
    private int[] kierunekDuch = new int[4];//kierunek poruszania się duchów
    private int[] historiaDuch = new int[4];//typy pól, na którch są duchy
    private int[] tempDuch = new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla duchów
    private int[] tempPacman = new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla Pacmana
    private int[][] Plansza = new int[][]{//plansze
            new int[418],//poziom 1
            new int[418]//poziom 2
    };
    private int[][] PlanszaReload = new int[][]{//plansze
            new int[]{//poziom 1
                    17, 11, 11, 11, 11, 11, 11, 11, 11, 22, 11, 11, 11, 11, 11, 11, 11, 11, 18,
                    10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10,
                    10, 8, 17, 18, 8, 17, 11, 18, 8, 10, 8, 17, 11, 18, 8, 17, 18, 8, 10,
                    10, 8, 16, 19, 8, 16, 11, 19, 8, 14, 8, 16, 11, 19, 8, 16, 19, 8, 10,
                    10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10,
                    10, 8, 15, 13, 8, 12, 8, 15, 11, 22, 11, 13, 8, 12, 8, 15, 13, 8, 10,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 10, 8, 8, 8, 10, 8, 8, 8, 8, 10,
                    16, 11, 11, 18, 8, 21, 11, 13, 9, 14, 9, 15, 11, 23, 8, 17, 11, 11, 19,
                    9, 9, 9, 10, 8, 10, 9, 9, 9, 9, 9, 9, 9, 10, 8, 10, 9, 9, 9,
                    11, 11, 11, 19, 8, 14, 9, 17, 13, 9, 15, 18, 9, 14, 8, 16, 11, 11, 11,
                    9, 9, 9, 9, 8, 9, 9, 10, 9, 9, 9, 10, 9, 9, 8, 9, 9, 9, 9,
                    11, 11, 11, 18, 8, 12, 9, 16, 11, 11, 11, 19, 9, 12, 8, 17, 11, 11, 11,
                    9, 9, 9, 10, 8, 10, 9, 9, 9, 9, 9, 9, 9, 10, 8, 10, 9, 9, 9,
                    17, 11, 11, 19, 8, 14, 9, 15, 11, 22, 11, 13, 9, 14, 8, 16, 11, 11, 18,
                    10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10,
                    10, 8, 15, 18, 8, 15, 11, 13, 8, 14, 8, 15, 11, 13, 8, 17, 11, 8, 10,
                    10, 8, 8, 10, 8, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 10, 8, 8, 10,
                    21, 13, 8, 14, 8, 12, 8, 15, 11, 22, 11, 13, 8, 12, 8, 14, 8, 15, 23,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 10, 8, 8, 8, 10, 8, 8, 8, 8, 10,
                    10, 8, 15, 11, 11, 20, 11, 13, 8, 14, 8, 15, 11, 20, 11, 11, 13, 8, 10,
                    10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10,
                    16, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 19
            },
            new int[]{//poziom 2
                    17, 11, 11, 11, 11, 22, 11, 11, 11, 11, 11, 11, 11, 11, 22, 11, 11, 11, 18,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 10,
                    10, 8, 17, 13, 8, 14, 8, 15, 11, 11, 11, 11, 13, 8, 14, 8, 12, 8, 10,
                    10, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 10,
                    10, 8, 14, 8, 15, 11, 13, 8, 17, 11, 11, 18, 8, 15, 11, 11, 19, 8, 10,
                    10, 8, 8, 8, 8, 8, 8, 8, 10, 9, 9, 10, 8, 8, 8, 8, 8, 8, 10,
                    10, 8, 12, 8, 15, 11, 13, 8, 14, 9, 15, 19, 8, 15, 11, 11, 18, 8, 10,
                    10, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 10,
                    10, 8, 16, 13, 8, 12, 8, 15, 11, 11, 11, 11, 13, 8, 12, 8, 14, 8, 10,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 10,
                    16, 11, 11, 11, 11, 23, 8, 17, 11, 11, 11, 11, 18, 8, 21, 11, 11, 11, 19,
                    17, 11, 11, 11, 11, 23, 8, 16, 11, 11, 11, 11, 19, 8, 21, 11, 11, 11, 18,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 10,
                    10, 8, 17, 13, 8, 14, 8, 15, 11, 11, 11, 11, 13, 8, 14, 8, 12, 8, 10,
                    10, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 10,
                    10, 8, 14, 8, 15, 11, 13, 8, 17, 13, 9, 12, 8, 15, 11, 11, 19, 8, 10,
                    10, 8, 8, 8, 8, 8, 8, 8, 10, 9, 9, 10, 8, 8, 8, 8, 8, 8, 10,
                    10, 8, 12, 8, 15, 11, 13, 8, 16, 11, 11, 19, 8, 15, 11, 11, 18, 8, 10,
                    10, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 10,
                    10, 8, 16, 13, 8, 12, 8, 15, 11, 11, 11, 11, 13, 8, 12, 8, 14, 8, 10,
                    10, 8, 8, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 10, 8, 8, 8, 10,
                    16, 11, 11, 11, 11, 20, 11, 11, 11, 11, 11, 11, 11, 11, 20, 11, 11, 11, 19
            }
    };
    private String[] nazwy = {//tablica z nazwami obrazków
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
    private Drawable gyroscope;//przyciks z zyroskopem
    private Drawable obrazy[] = new Drawable[nazwy.length];//tablica z obrazami
    private int height;//wysokość wyświetlacza w px
    private int width;//szerokosc wyświetlacza w px
    private int borderLeft;//ramka potrzebna do wyśrodkowania planszy od lewej
    private int borderTop;//ramka potrzebna do wyśrodkowania planszy od góry
    private int cykl;//licznik cykli
    private Context context;
    private int gyroscopeLeft;//odległość przycisku od lewej
    private int gyroscopeTop;//odległość przycisku od góry
    private int gyroscopeWidth;//szerokosc przycisku
    private boolean gyroscopeConfig = false;//konfiguracja zyroskopu

    public PanelView(Context context) {
        super(context);
        this.setFocusableInTouchMode(true);
        this.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        ROZMIAR_GRAFIKA = width / SZEROKOSC_PLANSZA;
        borderLeft = (width - ROZMIAR_GRAFIKA * SZEROKOSC_PLANSZA) / 2;
        borderTop = borderLeft;

        gyroscopeLeft = width;
        gyroscopeTop = height-ROZMIAR_GRAFIKA*WYSOKOSC_PLANSZA-borderLeft-borderTop;
        if (gyroscopeLeft < gyroscopeTop) {
            gyroscopeLeft /= 3;
            gyroscopeWidth = gyroscopeLeft;
            gyroscopeTop = ROZMIAR_GRAFIKA*WYSOKOSC_PLANSZA-borderLeft-borderTop+(gyroscopeTop-gyroscopeWidth)/2;
        } else {
            gyroscopeTop /= 3;
            gyroscopeWidth = gyroscopeTop;
            gyroscopeLeft = (gyroscopeLeft-gyroscopeWidth)/2;
            gyroscopeTop += ROZMIAR_GRAFIKA*WYSOKOSC_PLANSZA-borderLeft-borderTop;
        }

        gyroscope = this.getResources().getDrawable(context.getResources().getIdentifier("gyroscope", "drawable", context.getPackageName()));
        for (int i = 0; i < nazwy.length; i++) {//pobieranie obrazków do tablicy
            obrazy[i] = this.getResources().getDrawable(context.getResources().getIdentifier(nazwy[i], "drawable", context.getPackageName()));
        }

        cykl = 0;
        kopiaPlansza();
        startGra();
        ustawPlansza();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.setBackgroundColor(getResources().getColor(R.color.black));
        rysujPlansza(canvas);
        naprawPlansza();
        update();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (touchY < ROZMIAR_GRAFIKA*WYSOKOSC_PLANSZA+borderTop) {//jeśli plansza
                    if (gra == 0) {
                        kopiaPlansza();
                        startGra();
                        ustawPlansza();
                        setGra(1);
                    }
                    if (start) {
                        setStart(false);
                    } else {
                        setStart(true);
                    }
                } else if (touchX > gyroscopeLeft
                        && touchX < gyroscopeLeft+gyroscopeWidth
                        && touchY > gyroscopeTop
                        && touchY < gyroscopeTop+gyroscopeWidth) {//jeśli żyroskop
                    gyroscopeConfig = true;
                }
        }
        return true;
    }

    private void ustawPlansza() {//ustawia pacmana i duchy na planszy
        Plansza[poziom][pozycjaDuch[0]] = 4;
        Plansza[poziom][pozycjaDuch[1]] = 5;
        Plansza[poziom][pozycjaDuch[2]] = 6;
        Plansza[poziom][pozycjaDuch[3]] = 7;
        Plansza[poziom][pozycjaPacman] = kierunekPacman;
    }

    private void zmienPlansza()//aktualizuje obrazek pacmana na planszy
    {
        if (staraPozycjaPacman == pozycjaPacman) {
            return;
        }
        if (Plansza[poziom][pozycjaPacman] >= 10) {
            pozycjaPacman = staraPozycjaPacman;

            return;
        }
        if (Plansza[poziom][pozycjaPacman] == 8
                || Plansza[poziom][pozycjaPacman] == 9) {
            Plansza[poziom][staraPozycjaPacman] = 9;
            Plansza[poziom][pozycjaPacman] = kierunekPacman;
        }
    }

    private void naprawPlansza() {//Naprawia plansze
        for (int i = 0; i < SZEROKOSC_PLANSZA * WYSOKOSC_PLANSZA; i++) {
            if (Plansza[poziom][i] < 4) {
                Plansza[poziom][i] = 9;
            }
        }
        Plansza[poziom][pozycjaPacman] = kierunekPacman;
    }

    private void zmienPlanszaDuch(int i) {//aktualizuje obrazek ducha na planszy
        if (staraPozycjaDuch[i] == pozycjaDuch[i]) {
            return;
        }
        if (Plansza[poziom][pozycjaDuch[i]] >= 10) {
            pozycjaDuch[i] = staraPozycjaDuch[i];
            zmienKierunekDuch(i);
            return;
        }
        if (Plansza[poziom][pozycjaDuch[i]] == 8
                || Plansza[poziom][pozycjaDuch[i]] == 9) {
            Plansza[poziom][staraPozycjaDuch[i]] = historiaDuch[i];
            boolean test = true;
            for (int j = 0; j < i; j++) {
                if (Plansza[poziom][pozycjaDuch[i]] == Plansza[poziom][pozycjaDuch[j]]) {
                    if (Plansza[poziom][pozycjaDuch[i]] >= 4 || Plansza[poziom][pozycjaDuch[i]] <= 8) {
                        historiaDuch[i] = historiaDuch[j];
                    }
                    test = false;
                }
            }
            if (test) {
                historiaDuch[i] = Plansza[poziom][pozycjaDuch[i]];
            }
            Plansza[poziom][pozycjaDuch[i]] = 4 + i;
            int j, n;
            n = 0;
            sprawdzKierunekDuch(i);
            for (j = 0; j < 4; j++) {
                if (tempDuch[j] == 1) {
                    n++;
                }
            }
            if (n > 2) {
                zmienKierunekDuch(i);
            }
        }
    }

    public void rysujPlansza(Canvas canvas) {//rysowanie planszy
        zmienPlansza();
        for (int i = 0; i < liczbaDuch; i++) {
            zmienPlanszaDuch(i);
        }
        Rect rect = new Rect(gyroscopeLeft, gyroscopeTop, gyroscopeLeft+gyroscopeWidth, gyroscopeTop+gyroscopeWidth);
        gyroscope.setBounds(rect);
        gyroscope.draw(canvas);
        for (int i = 0; i < SZEROKOSC_PLANSZA; i++) {//Rysowanie obrazów na planszy po zmianie pozycji gracza
            for (int j = 0; j < WYSOKOSC_PLANSZA; j++) {
                rect = new Rect(borderLeft + i * ROZMIAR_GRAFIKA,
                        borderTop + j * ROZMIAR_GRAFIKA,
                        borderLeft + i * ROZMIAR_GRAFIKA + ROZMIAR_GRAFIKA,
                        borderTop + j * ROZMIAR_GRAFIKA + ROZMIAR_GRAFIKA);
                obrazy[Plansza[poziom][i + SZEROKOSC_PLANSZA * j]].setBounds(rect);
                obrazy[Plansza[poziom][i + SZEROKOSC_PLANSZA * j]].draw(canvas);
            }
        }
    }

    public void zmnienKierunekPacman(int nowyKierunekPacman) {//zmienia kierunek poruszania sie pacmana po naciśnięciu klawiszy sterowania
        switch (nowyKierunekPacman) {
            case 0:
                if (Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] == 8
                        || Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] == 9) {
                    kierunekPacman = 0;
                } else {
                    nastepnyKierunekPacman = 0;
                }
                break;
            case 1:
                if (Plansza[poziom][pozycjaPacman + 1] == 8
                        || Plansza[poziom][pozycjaPacman + 1] == 9) {
                    kierunekPacman = 1;
                } else {
                    nastepnyKierunekPacman = 1;
                }
                break;
            case 2:
                if (Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] == 8
                        || Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] == 9) {
                    kierunekPacman = 2;
                } else {
                    nastepnyKierunekPacman = 2;
                }
                break;
            case 3:
                if (Plansza[poziom][pozycjaPacman - 1] == 8
                        || Plansza[poziom][pozycjaPacman - 1] == 9) {
                    kierunekPacman = 3;
                } else {
                    nastepnyKierunekPacman = 3;
                }
                break;
        }
        Plansza[poziom][pozycjaPacman] = kierunekPacman;
    }

    public void update() {//aktualizacja gry, koleiny cykl
        if (start) {
            sprawdzPunkty();
            sprawdzPrzegrana();
            czyZmienicKierunekPacman();

            if (cykl == SZYBKOSC_GRY) {
                przesunPacman();
                switch (liczbaDuch) {
                    case 4:
                        przesunDuch(3);
                    case 3:
                        przesunDuch(2);
                    case 2:
                        przesunDuch(1);
                    case 1:
                        przesunDuch(0);
                }
                cykl = 0;
            } else {
                cykl++;
            }
        }
    }

    private void czyZmienicKierunekPacman() {
        sprawdzKierunekPacman();
        int n = 0;
        for (int j = 0; j < 4; j++) {//zlicza możliwe drogi Pacmana
            if (tempPacman[j] == 1) {
                n++;
            }
        }

        if (n > 2) {//jeśli "skrzyżowanie"
            if (nastepnyKierunekPacman != 5) {
                switch (nastepnyKierunekPacman) {
                    case 0:
                        if (Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] == 8
                                || Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] == 9) {
                            kierunekPacman = 0;
                            nastepnyKierunekPacman = 5;
                        }
                        break;
                    case 1:
                        if (Plansza[poziom][pozycjaPacman + 1] == 8
                                || Plansza[poziom][pozycjaPacman + 1] == 9) {
                            kierunekPacman = 1;
                            nastepnyKierunekPacman = 5;
                        }
                        break;
                    case 2:
                        if (Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] == 8
                                || Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] == 9) {
                            kierunekPacman = 2;
                            nastepnyKierunekPacman = 5;
                        }
                        break;
                    case 3:
                        if (Plansza[poziom][pozycjaPacman - 1] == 8
                                || Plansza[poziom][pozycjaPacman - 1] == 9) {
                            kierunekPacman = 3;
                            nastepnyKierunekPacman = 5;
                        }
                        break;
                }
            }
        }
    }

    private void przesunPacman() {//zmienia pozycja pacmana zgodnie z jego kierunkiem poruszania
        switch (kierunekPacman) {
            case 0:
                if (Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] < 10) {
                    staraPozycjaPacman = pozycjaPacman;
                    pozycjaPacman -= SZEROKOSC_PLANSZA;
                }
                break;
            case 1:
                if (poziom == 0 && ((pozycjaPacman == 208 && Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA + 1] < 10)
                        || (pozycjaPacman != 208 && Plansza[poziom][pozycjaPacman + 1] < 10))
                        || (poziom == 1 && Plansza[poziom][pozycjaPacman + 1] < 10)) {
                    staraPozycjaPacman = pozycjaPacman;
                    if (pozycjaPacman == 208 && poziom == 0) {//teleport
                        pozycjaPacman -= SZEROKOSC_PLANSZA;
                    }
                    pozycjaPacman += 1;
                }
                break;
            case 2:
                if (Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] < 10) {
                    staraPozycjaPacman = pozycjaPacman;
                    pozycjaPacman += SZEROKOSC_PLANSZA;
                }
                break;
            case 3:
                if ((poziom == 0 && ((pozycjaPacman == 190 && Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA - 1] < 10)
                        || (pozycjaPacman != 190 && Plansza[poziom][pozycjaPacman - 1] < 10)))
                        || (poziom == 1 && Plansza[poziom][pozycjaPacman - 1] < 10)) {
                    staraPozycjaPacman = pozycjaPacman;
                    if (pozycjaPacman == 190 && poziom == 0) {//teleport
                        pozycjaPacman += SZEROKOSC_PLANSZA;
                    }
                    pozycjaPacman -= 1;
                }
                break;
        }
    }

    private void przesunDuch(int i) {//zmienia pozycje wybranego ducha zgodnie z jego kierunkiem poruszania
        int n, j;
        sprawdzKierunekDuch(i);//sprawdza możliwe drogi
        n = 0;
        for (j = 0; j < 4; j++) {//zlicza możliwe drogi
            if (tempDuch[j] == 1) {
                n++;
            }
        }
        if (n > 2) {//jesli "skrzyowanie"
            if (czekajDuch[i] == CZEKAJ_DUCH_CYKL) {//duch "zastanawia się", którą drogę wybrać
                switch (kierunekDuch[i]) {
                    case 0:
                        staraPozycjaDuch[i] = pozycjaDuch[i];
                        pozycjaDuch[i] -= SZEROKOSC_PLANSZA;
                        break;
                    case 1:
                        staraPozycjaDuch[i] = pozycjaDuch[i];
                        pozycjaDuch[i] += 1;
                        break;
                    case 2:
                        staraPozycjaDuch[i] = pozycjaDuch[i];
                        pozycjaDuch[i] += SZEROKOSC_PLANSZA;
                        break;
                    case 3:
                        staraPozycjaDuch[i] = pozycjaDuch[i];
                        pozycjaDuch[i] -= 1;
                        break;
                }
                czekajDuch[i] = 0;
            } else {
                czekajDuch[i]++;
            }
        } else {
            switch (kierunekDuch[i]) {
                case 0:
                    staraPozycjaDuch[i] = pozycjaDuch[i];
                    pozycjaDuch[i] -= SZEROKOSC_PLANSZA;
                    break;
                case 1:
                    staraPozycjaDuch[i] = pozycjaDuch[i];
                    pozycjaDuch[i] += 1;
                    break;
                case 2:
                    staraPozycjaDuch[i] = pozycjaDuch[i];
                    pozycjaDuch[i] += SZEROKOSC_PLANSZA;
                    break;
                case 3:
                    staraPozycjaDuch[i] = pozycjaDuch[i];
                    pozycjaDuch[i] -= 1;
                    break;
            }
        }
    }

    private void zmienKierunekDuch(int i) {//analizuje i wybiera najlepszy z możliwych kierunków dla wybranego ducha w zależności od pozycji pacmana
        sprawdzKierunekDuch(i);
        int n, j, x, y;
        x = Math.abs(pozycjaPacman % SZEROKOSC_PLANSZA - pozycjaDuch[i] % SZEROKOSC_PLANSZA);
        y = Math.abs((int) (pozycjaPacman / SZEROKOSC_PLANSZA) - (int) (pozycjaDuch[i] / SZEROKOSC_PLANSZA));
        for (j = 0, n = 0; j < 4; j++) {
            if (tempDuch[j] == 1) {
                n++;
            }
        }
        if (pozycjaPacman < pozycjaDuch[i]) {
            if (tempDuch[2] != 0) {
                tempDuch[2] = 0;
                n--;
            }
        } else {
            if (tempDuch[0] != 0) {
                tempDuch[0] = 0;
                n--;
            }
        }
        if (n > 1) {
            if (pozycjaPacman % SZEROKOSC_PLANSZA < pozycjaDuch[i] % SZEROKOSC_PLANSZA) {
                if (tempDuch[1] != 0) {
                    tempDuch[1] = 0;
                    n--;
                }
            } else {
                if (tempDuch[3] != 0) {
                    tempDuch[3] = 0;
                    n--;
                }
            }
        }
        if (x == 0) {
            if (tempDuch[3] == 1 && n > 1) {
                tempDuch[3] = 0;
                n--;
            }
            if (tempDuch[1] == 1 && n > 1) {
                tempDuch[1] = 0;
                n--;
            }
        }
        if (y == 0) {
            if (tempDuch[0] == 1 && n > 1) {
                tempDuch[0] = 0;
                n--;
            }
            if (tempDuch[2] == 1 && n > 1) {
                tempDuch[2] = 0;
                n--;
            }
        }
        for (j = 0; j < 4; j++) {
            if (tempDuch[j] == 1) {
                kierunekDuch[i] = j;
                return;
            }
        }
    }

    private void sprawdzKierunekDuch(int i) {//sprawdza możliwe kierunki dla wybranego ducha
        for (int j = 0; j < 4; j++) {
            tempDuch[j] = 0;
        }
        if (Plansza[poziom][pozycjaDuch[i] - SZEROKOSC_PLANSZA] < 10) {//jeśli pole wyżej nie jest przeszkodą, ustaw 1
            tempDuch[0] = 1;
        }
        if (Plansza[poziom][pozycjaDuch[i] + 1] < 10) {//jeśli pole w prawo nie jest przeszkodą, ustaw 1
            tempDuch[1] = 1;
        }
        if (Plansza[poziom][pozycjaDuch[i] + SZEROKOSC_PLANSZA] < 10) {//jeśli pole niżej nie jest przeszkodą, ustaw 1
            if (pozycjaDuch[i] != 161) {//nie można wejść do domku duchów
                tempDuch[2] = 1;
            }
        }
        if (Plansza[poziom][pozycjaDuch[i] - 1] < 10) {//jeśli pole w lewo nie jest przeszkodą, ustaw 1
            tempDuch[3] = 1;
        }
    }

    private void sprawdzKierunekPacman() {//sprawdza możliwe kierunki dla Pacmana
        for (int j = 0; j < 4; j++) {
            tempPacman[j] = 0;
        }
        if (Plansza[poziom][pozycjaPacman - SZEROKOSC_PLANSZA] < 10) {//jeśli pole wyżej nie jest przeszkodą, ustaw 1
            tempPacman[0] = 1;
        }
        if (Plansza[poziom][pozycjaPacman + 1] < 10) {//jeśli pole w prawo nie jest przeszkodą, ustaw 1
            tempPacman[1] = 1;
        }
        if (Plansza[poziom][pozycjaPacman + SZEROKOSC_PLANSZA] < 10) {//jeśli pole niżej nie jest przeszkodą, ustaw 1
            if (pozycjaPacman != 161) {//nie można wejść do domku duchów
                tempPacman[2] = 1;
            }
        }
        if (Plansza[poziom][pozycjaPacman - 1] < 10) {//jeśli pole w lewo nie jest przeszkodą, ustaw 1
            tempPacman[3] = 1;
        }
    }

    private void sprawdzPrzegrana() {//sprawdza "sptkanie" pacmana z duchami
        for (int i = 0; i < liczbaDuch; i++) {
            if (pozycjaPacman == pozycjaDuch[i]) {//przegrana
                setGra(0);
                setStart(false);
                setPoziom(0);
                kopiaPlansza();
                startGra();
                ustawPlansza();
                Toast.makeText(context, "Przegrałeś!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sprawdzPunkty() {//sprawdza pozostałe punkty do zebrania
        int i;
        double pkt = 0;
        for (i = 0; i < SZEROKOSC_PLANSZA * WYSOKOSC_PLANSZA; i++) {//zlicza punkty
            if (Plansza[poziom][i] == 8) {//kod pola z punktem
                pkt++;
            }
        }
        for (i = 0; i < historiaDuch.length; i++) {//sprawdza, czy duch jest na polu z punktem
            if (historiaDuch[i] == 8) {
                pkt++;
            }
        }
        if (liczbaDuch < 4) {
            switch (liczbaDuch) {//zwiększa liczbę duchów, gdy:
                case 1:
                    if (pkt / PUNKTY_MAX[poziom] <= 0.75) {//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
                case 2:
                    if (pkt / PUNKTY_MAX[poziom] <= 0.5) {//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
                case 3:
                    if (pkt / PUNKTY_MAX[poziom] <= 0.25) {//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
            }
        }
        if (pkt == 0) {//wygrana, jeśli zebrano wszystkie punkty
            if (poziom + 1 == LICZBA_POZIOM) {
                setGra(3);
                setStart(false);
                Toast.makeText(context, "Wygrałeś!", Toast.LENGTH_SHORT).show();
                nastepnyPoziom();
            } else {
                Toast.makeText(context, "Wygrałeś " + (poziom + 1) + " poziom!", Toast.LENGTH_SHORT).show();
                setGra(2);
                setStart(false);
                nastepnyPoziom();
            }
        }
    }

    private void startGra() {//nadaje początkowe wartości
        poziom = 0;
        liczbaDuch = 1;
        kierunekPacman = 1;
        pozycjaPacman = 313;
        staraPozycjaPacman = 313;
        pozycjaDuch[0] = 180;
        pozycjaDuch[1] = 199;
        pozycjaDuch[2] = 198;
        pozycjaDuch[3] = 200;
        staraPozycjaDuch[0] = 180;
        staraPozycjaDuch[1] = 199;
        staraPozycjaDuch[2] = 198;
        staraPozycjaDuch[3] = 200;
        kierunekDuch[0] = 0;
        kierunekDuch[1] = 0;
        kierunekDuch[2] = 1;
        kierunekDuch[3] = 3;
        historiaDuch[0] = 9;
        historiaDuch[1] = 9;
        historiaDuch[2] = 9;
        historiaDuch[3] = 9;
        czekajDuch[0] = 0;
        czekajDuch[1] = 0;
        czekajDuch[2] = 0;
        czekajDuch[3] = 0;
    }

    private void nastepnyPoziom() {//ustawia następny poziom
        if (poziom + 1 < LICZBA_POZIOM) {
            poziom++;
            wczytajNastepnyPoziom();
        } else {
            setPoziom(0);
            kopiaPlansza();
            startGra();
            ustawPlansza();
        }
    }

    private void wczytajNastepnyPoziom() {
        switch (poziom) {
            case 1:
                liczbaDuch = 1;
                kierunekPacman = 1;
                pozycjaPacman = 235;
                staraPozycjaPacman = 235;
                pozycjaDuch[0] = 104;
                pozycjaDuch[1] = 105;
                pozycjaDuch[2] = 314;
                pozycjaDuch[3] = 313;
                staraPozycjaDuch[0] = 104;
                staraPozycjaDuch[1] = 105;
                staraPozycjaDuch[2] = 314;
                staraPozycjaDuch[3] = 313;
                kierunekDuch[0] = 2;
                kierunekDuch[1] = 3;
                kierunekDuch[2] = 0;
                kierunekDuch[3] = 1;
                historiaDuch[0] = 9;
                historiaDuch[1] = 9;
                historiaDuch[2] = 9;
                historiaDuch[3] = 9;
                czekajDuch[0] = 0;
                czekajDuch[1] = 0;
                czekajDuch[2] = 0;
                czekajDuch[3] = 0;
                ustawPlansza();
                break;
        }
    }

    private void setStart(boolean start) {
        this.start = start;
    }

    public boolean getStart() {
        return start;
    }

    public void setGra(int gra) {
        this.gra = gra;
    }

    public void setPoziom(int poziom) {
        this.poziom = poziom;
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderTop() {
        return borderTop;
    }

    public boolean getGyroscopeConfig() {
        return gyroscopeConfig;
    }

    public void setGyroscopeConfig(boolean gyroscopeConfig) {
        this.gyroscopeConfig = gyroscopeConfig;
    }

    public void kopiaPlansza() {
        for (int i = 0; i < LICZBA_POZIOM; i++) {
            for (int j = 0; j < Plansza[i].length; j++) {
                Plansza[i][j] = PlanszaReload[i][j];
            }
        }
    }
}
