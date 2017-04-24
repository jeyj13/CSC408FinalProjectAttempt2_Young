package com.example.jey.csc408finalprojectattempt2_young;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;

import static android.R.attr.max;
//import static android.R.attr.aaView.VISIBLE;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {
    //Card Declarations
    Drawable[][] cardsMaster;
    //Secondary array
    Drawable[][] cardsPlay;
// Back of card
    Drawable cardBack;
    //Variable for value of the pot
    int thePot = 0;
    //Turn markers
    int turnMarker = 0;
    int cardMarkerpc = 0;
    int valDecpc = 0;
    //*************
    Object waiter = new Object();
    //min/max variables
    final int MINDEAL = 0;
    final int MAXDEAL = 11;
    final int MINARRAY = 0;
    final int MAXARRAY = 3;
    //AI personality thresholds
    //currently unused, intended for doing math to let the computer fold if the total pot value is too high and cards are not good enough
    int threshold1 = 4500;
    int threshold2 = 3000;
    int threshold3 = 2700;
    int threshold4 = 2500;
    int threshold5 = 1500;
    int threshold7 = 6000;
    int threshold8 = 8000;
    int threshold9 = 12000;
    //View Declarations
    EditText txtInput;
    Button btnDeal;
    Button btnBet;
    Button btnDraw;
    Button btnReset;
    TextView pot;
    TextView status;
    View[] pcViews = new View[5];
    View[] ai1Views = new View[5];
    View[] ai2Views = new View[5];
    View[] dealViews = new View[5];
    ImageView[] pcMaster;
    ImageView[] ai1Master;
    ImageView[] ai2Master;
    ImageView[] dealMaster;
    RelativeLayout vg;

    ImageView playerCard1, playerCard2, playerCard3, playerCard4, playerCard5, ai1Card1, ai1Card2, ai1Card3, ai1Card4, ai1Card5, ai2Card1, ai2Card2, ai2Card3, ai2Card4, ai2Card5, dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5;
    //Declaration of hands (array locations for drawables)
    int[][][] hands = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}
    };
    int[][][] Clearhands = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}
    };

    int[][] cardValsMaster = new int[][]{
            {0,1,2,3,4,5,6,7,8,9,10,11,12},
            {0,1,2,3,4,5,6,7,8,9,10,11,12},
            {0,1,2,3,4,5,6,7,8,9,10,11,12},
            {0,1,2,3,4,5,6,7,8,9,10,11,12}
    };
    int [][] cardValsPlay = cardValsMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //a long list of attaching views to Java code
        btnDeal = (Button) findViewById(R.id.btnBegin);
        cardBack = ResourcesCompat.getDrawable(getResources(), R.drawable.b1fv, null);
        btnBet = (Button) findViewById(R.id.btnBet);

        btnDraw = (Button) findViewById(R.id.btnDraw);
        btnReset = (Button) findViewById(R.id.btnReset);
        txtInput = (EditText) findViewById(R.id.editTextBet);
        pot = (TextView) findViewById(R.id.txtPot);
        playerCard1 = (ImageView) findViewById(R.id.imgpc1);
        playerCard2 = (ImageView) findViewById(R.id.imgpc2);
        playerCard3 = (ImageView) findViewById(R.id.imgpc3);
        playerCard4 = (ImageView) findViewById(R.id.imgpc4);
        playerCard5 = (ImageView) findViewById(R.id.imgpc5);
        ai1Card1 = (ImageView) findViewById(R.id.imgai11);
        ai1Card2 = (ImageView) findViewById(R.id.imgai12);
        ai1Card3 = (ImageView) findViewById(R.id.imgai13);
        ai1Card4 = (ImageView) findViewById(R.id.imgai14);
        ai1Card5 = (ImageView) findViewById(R.id.imgai15);
        ai2Card1 = (ImageView) findViewById(R.id.imgai21);
        ai2Card2 = (ImageView) findViewById(R.id.imgai22);
        ai2Card3 = (ImageView) findViewById(R.id.imgai23);
        ai2Card4 = (ImageView) findViewById(R.id.imgai24);
        ai2Card5 = (ImageView) findViewById(R.id.imgai25);
        dealerCard1 = (ImageView) findViewById(R.id.imgdc1);
        dealerCard2 = (ImageView) findViewById(R.id.imgdc2);
        dealerCard3 = (ImageView) findViewById(R.id.imgdc3);
        dealerCard4 = (ImageView) findViewById(R.id.imgdc4);
        dealerCard5 = (ImageView) findViewById(R.id.imgdc5);
        status = (TextView) findViewById(R.id.txtWinStatus);
        pcMaster = new ImageView[]{playerCard1, playerCard2, playerCard3, playerCard4, playerCard5};
        ai1Master = new ImageView[]{ai1Card1, ai1Card2, ai1Card3, ai1Card4, ai1Card5};
        ai2Master = new ImageView[]{ai2Card1, ai2Card2, ai2Card3, ai2Card4, ai2Card5};
        dealMaster = new ImageView[]{dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5};
        cardsMaster = new Drawable[][]{{
                ResourcesCompat.getDrawable(getResources(), R.drawable.c1, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c2, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c3, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c4, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c5, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c6, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c7, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c8, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c9, null), ResourcesCompat.getDrawable(getResources(), R.drawable.c10, null), ResourcesCompat.getDrawable(getResources(), R.drawable.cj, null), ResourcesCompat.getDrawable(getResources(), R.drawable.cq, null), ResourcesCompat.getDrawable(getResources(), R.drawable.ck, null)}
                , {ResourcesCompat.getDrawable(getResources(), R.drawable.d1, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d2, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d3, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d4, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d5, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d6, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d7, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d8, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d9, null), ResourcesCompat.getDrawable(getResources(), R.drawable.d10, null), ResourcesCompat.getDrawable(getResources(), R.drawable.dj, null), ResourcesCompat.getDrawable(getResources(), R.drawable.dq, null), ResourcesCompat.getDrawable(getResources(), R.drawable.dk, null)}
                , {ResourcesCompat.getDrawable(getResources(), R.drawable.h1, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h2, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h3, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h4, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h5, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h6, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h7, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h8, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h9, null), ResourcesCompat.getDrawable(getResources(), R.drawable.h10, null), ResourcesCompat.getDrawable(getResources(), R.drawable.hj, null), ResourcesCompat.getDrawable(getResources(), R.drawable.hq, null), ResourcesCompat.getDrawable(getResources(), R.drawable.hk, null)}
                , {ResourcesCompat.getDrawable(getResources(), R.drawable.s1, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s2, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s3, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s4, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s5, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s6, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s7, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s8, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s9, null), ResourcesCompat.getDrawable(getResources(), R.drawable.s10, null), ResourcesCompat.getDrawable(getResources(), R.drawable.sj, null), ResourcesCompat.getDrawable(getResources(), R.drawable.sq, null), ResourcesCompat.getDrawable(getResources(), R.drawable.sk, null)}
        };
        cardsPlay = cardsMaster;
        btnBet.setOnClickListener(setBet);
        btnDeal.setOnClickListener(beginGame);
        btnDraw.setOnClickListener(drawingCards);
        btnReset.setOnClickListener(reset);
        vg = (RelativeLayout) findViewById(R.id.activity_main);


    }
//*************************************************************************************************************************************
    public void pickingCards(View v) {

        if (asList(pcViews).contains(v) == false && pcViews[cardMarkerpc] == null) {
            pcViews[cardMarkerpc] = v;
            cardMarkerpc++;

        } else if (asList(pcViews).contains(v) == true) {
            int indhold = Arrays.asList(pcViews).indexOf(v);
            pcViews[indhold] = null;
            cardMarkerpc--;
        } else if (asList(pcViews).contains(v) == false && pcViews[cardMarkerpc] != null && cardMarkerpc > pcViews.length || cardMarkerpc != Arrays.asList(pcViews).indexOf(null)) {
            int indhold = Arrays.asList(pcViews).indexOf(null);
            pcViews[indhold] = v;
            cardMarkerpc = Arrays.asList(pcViews).indexOf(null);
        }


    }

    //*******************************************************************************************************
    View.OnClickListener drawingCards = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  if(cardMarkerpc != 0) {
                playerRedealer(getMarks(pcViews), hands, cardsPlay, cardValsPlay);
          //  }

        }
    };
    //*************************************************************************************************************
    View.OnClickListener beginGame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cardsPlay = cardsMaster;
            btnBet.setEnabled(true);
            txtInput.setEnabled(true);
            btnReset.setEnabled(true);
            btnDeal.setEnabled(false);
        }
    };

//*********************************************************************************************************
    //Unused countdown timer
    CountDownTimer ctd = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

        }
    };
//********************************************************************************************************
    View.OnClickListener reset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pot.setText("0");
            btnBet.setEnabled(false);
            btnDeal.setEnabled(true);
            btnDraw.setEnabled(false);
            btnReset.setEnabled(false);
            txtInput.setEnabled(false);
            status.setText("");
            for(int r = 0; r<5; r++)
            {
                if (r != 5)
                {
                    pcMaster[r].setVisibility(View.INVISIBLE);
                    ai1Master[r].setVisibility(View.INVISIBLE);
                    ai2Master[r].setVisibility(View.INVISIBLE);
                    dealMaster[r].setVisibility(View.INVISIBLE);
                    pcMaster[r].setImageDrawable(cardBack);
                    ai1Master[r].setImageDrawable(cardBack);
                    ai2Master[r].setImageDrawable(cardBack);
                    dealMaster[r].setImageDrawable(cardBack);
                    pcViews[r] = null;
                }
            }
            //cardsPlay = cardsMaster;
            thePot = 0;
            turnMarker = 0;
            cardMarkerpc = 0;
            hands = Clearhands;
            cardValsPlay = cardValsMaster;
        }
        };
    //******************************************************************************************************
    View.OnClickListener setBet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String hold1 = txtInput.getText().toString();
            if (turnMarker == 0) {
                if (hold1 != null) {

                    pot.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));
                        }
                    }, 100);

                    //    SystemClock.sleep(1000);

                    pot.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));
                        }
                    }, 300);

                    //SystemClock.sleep(1000);

                    pot.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));
                        }
                    }, 500);

                    //SystemClock.sleep(1000);

                    pot.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));
                        }
                    }, 700);

                    //SystemClock.sleep(1000);
                    Dealer(cardsPlay, hands, cardValsPlay);
                    btnDraw.setEnabled(true);
                    btnBet.setEnabled(false);
                    txtInput.setEnabled(false);
                    SystemClock.sleep(300);
                    btnBet.setEnabled(true);
                    txtInput.setEnabled(true);
                    turnMarker++;

                } else {

                }
            } else if (turnMarker == 1) {
                if (hold1 != null) {
                    thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));

                    thePot += Integer.parseInt(hold1);

                            pot.setText(Integer.toString(thePot));

                    thePot += Integer.parseInt(hold1);
                            pot.setText(Integer.toString(thePot));


                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));

                    determineWinner(hands);
                    turnMarker++;
                }


            }
        }
        }

        ;
//******************************************************************************************************************************************
        public void Dealer(Drawable[][] d, int[][][] h, int[][] cardVals) {

            for (int x = 0; x < 20; x++) {
                Random random1 = new Random();
                int r1 = (random1.nextInt(12));
                int r2 = (random1.nextInt(4));
                if (cardVals[r2][r1] == -1) {
                    for (int g = 0; g < 52; g++) {
                        r1 = (random1.nextInt(12));
                        r2 = (random1.nextInt(4));
                        if (cardVals[r2][r1] != -1) {
                            g = 53;
                        }
                    }
                }
                if (x == 0 || x == 4 || x == 8 || x == 12 || x == 16) {
                    if (x == 0) {
                        playerCard1.setVisibility(View.INVISIBLE);
                        playerCard1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerCard1.setVisibility(View.VISIBLE);
                            }
                        }, 1000);


                        h[0][0][0] = r2;
                        h[0][0][1] = r1;
                        playerCard1.setImageDrawable(d[r2][r1]);


                        cardVals[r2][r1] = -1;
                    } else if (x == 4) {
                        playerCard2.setVisibility(View.INVISIBLE);
                        playerCard2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerCard2.setVisibility(View.VISIBLE);
                            }
                        }, 2000);

                        h[0][1][0] = r2;
                        h[0][1][1] = r1;
                        playerCard2.setImageDrawable(d[r2][r1]);

                        cardVals[r2][r1] = -1;
                    } else if (x == 8) {
                        playerCard3.setVisibility(View.INVISIBLE);
                        playerCard3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerCard3.setVisibility(View.VISIBLE);
                            }
                        }, 3000);
                        h[0][2][0] = r2;
                        h[0][2][1] = r1;
                        playerCard3.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 12) {
                        playerCard4.setVisibility(View.INVISIBLE);
                        playerCard4.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerCard4.setVisibility(View.VISIBLE);
                            }
                        }, 4000);
                        h[0][3][0] = r2;
                        h[0][3][1] = r1;
                        playerCard4.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 16) {
                        playerCard5.setVisibility(View.INVISIBLE);
                        playerCard5.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playerCard5.setVisibility(View.VISIBLE);
                            }
                        }, 5000);
                        h[0][4][0] = r2;
                        h[0][4][1] = r1;
                        playerCard5.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    }
                } else if (x == 1 || x == 5 || x == 9 || x == 13 || x == 17) {
                    if (x == 1) {
                        ai1Card1.setVisibility(View.INVISIBLE);
                        ai1Card1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai1Card1.setVisibility(View.VISIBLE);
                            }
                        }, 1100);
                        h[1][0][0] = r2;
                        h[1][0][1] = r1;
                  //      ai1Card1.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 5) {
                        ai1Card2.setVisibility(View.INVISIBLE);
                        ai1Card2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai1Card2.setVisibility(View.VISIBLE);
                            }
                        }, 2100);
                        h[1][1][0] = r2;
                        h[1][1][1] = r1;
                  //      ai1Card2.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 9) {
                        ai1Card3.setVisibility(View.INVISIBLE);
                        ai1Card3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai1Card3.setVisibility(View.VISIBLE);
                            }
                        }, 3100);
                        h[1][2][0] = r2;
                        h[1][2][1] = r1;
                    //    ai1Card3.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 13) {
                        ai1Card4.setVisibility(View.INVISIBLE);
                        ai1Card4.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai1Card4.setVisibility(View.VISIBLE);
                            }
                        }, 4100);
                        h[1][3][0] = r2;
                        h[1][3][1] = r1;
                   //     ai1Card4.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 17) {
                        ai1Card5.setVisibility(View.INVISIBLE);
                        ai1Card5.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai1Card5.setVisibility(View.VISIBLE);
                            }
                        }, 5100);
                        h[1][4][0] = r2;
                        h[1][4][1] = r1;
                  //      ai1Card5.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    }
                } else if (x == 2 || x == 6 || x == 10 || x == 14 || x == 18) {
                    if (x == 2) {
                        ai2Card1.setVisibility(View.INVISIBLE);
                        ai2Card1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai2Card1.setVisibility(View.VISIBLE);
                            }
                        }, 1200);
                        h[2][0][0] = r2;
                        h[2][0][1] = r1;
                  //      ai2Card1.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 6) {
                        ai2Card2.setVisibility(View.INVISIBLE);
                        ai2Card2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai2Card2.setVisibility(View.VISIBLE);
                            }
                        }, 2200);
                        h[2][1][0] = r2;
                        h[2][1][1] = r1;
                      //  ai2Card2.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 10) {
                        ai2Card3.setVisibility(View.INVISIBLE);
                        ai2Card3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai2Card3.setVisibility(View.VISIBLE);
                            }
                        }, 3200);
                        h[2][2][0] = r2;
                        h[2][2][1] = r1;
                      //  ai2Card3.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 14) {
                        ai2Card4.setVisibility(View.INVISIBLE);
                        ai2Card4.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai2Card4.setVisibility(View.VISIBLE);
                            }
                        }, 4200);
                        h[2][3][0] = r2;
                        h[2][3][1] = r1;
                     //   ai2Card4.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 18) {
                        ai2Card5.setVisibility(View.INVISIBLE);
                        ai2Card5.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ai2Card5.setVisibility(View.VISIBLE);
                            }
                        }, 5200);
                        h[2][4][0] = r2;
                        h[2][4][1] = r1;
                      //  ai2Card5.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    }
                } else if (x == 3 || x == 7 || x == 11 || x == 15 || x == 19) {
                    if (x == 3) {
                        dealerCard1.setVisibility(View.INVISIBLE);
                        dealerCard1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCard1.setVisibility(View.VISIBLE);
                            }
                        }, 1300);
                        h[3][0][0] = r2;
                        h[3][0][1] = r1;
                    //    dealerCard1.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 7) {
                        dealerCard2.setVisibility(View.INVISIBLE);
                        dealerCard2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCard2.setVisibility(View.VISIBLE);
                            }
                        }, 2300);
                        h[3][1][0] = r2;
                        h[3][1][1] = r1;
                   //     dealerCard2.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 11) {
                        dealerCard3.setVisibility(View.INVISIBLE);
                        dealerCard3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCard3.setVisibility(View.VISIBLE);
                            }
                        }, 3300);
                        h[3][2][0] = r2;
                        h[3][2][1] = r1;
                     //   dealerCard3.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 15) {
                        dealerCard4.setVisibility(View.INVISIBLE);
                        dealerCard4.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCard4.setVisibility(View.VISIBLE);
                            }
                        }, 4300);
                        h[3][3][0] = r2;
                        h[3][3][1] = r1;
                        //dealerCard4.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    } else if (x == 19) {
                        dealerCard5.setVisibility(View.INVISIBLE);
                        dealerCard5.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCard5.setVisibility(View.VISIBLE);
                            }
                        }, 5300);
                        h[3][4][0] = r2;
                        h[3][4][1] = r1;
                        //dealerCard5.setImageDrawable(d[r2][r1]);
                        cardVals[r2][r1] = -1;
                    }
                }
            }

        }

//***********************************************************************************************************************************************
        public ImageView[] getMarks(View[] vbox) {
            ImageView[] holdBox = new ImageView[vbox.length];
            for (int g = 0; g < vbox.length - 1; g++) {
                holdBox[g] = (ImageView) vbox[g];

            }

            return holdBox;
        }
//***********************************************************************************************************************************************
      public void determineWinner(int[][][] h)
      {
          int pcWinholder=0;
          int ai1winHolder=0;
          int ai2winHolder=0;
          int dealWinholder=0;

          //player Calc
          for(int pm = 0; pm < 4; pm++)
          {
              if(h[0][pm][1] != 0)
             pcWinholder += h[0][pm][1];
              else if(h[0][pm][1] == 10)
              {
                  pcWinholder += 12;
              }
              else if(h[0][pm][1] == 11)
              {
                  pcWinholder += 13;
              }
              else if(h[0][pm][1] == 12)
              {
                  pcWinholder += 14;
              }
              else if (h[0][pm][1] == 0)
                  pcWinholder += 15;
          }
          for(int ai1 = 0; ai1 < 4; ai1++)
          {
              if(h[1][ai1][1] != 0)
              ai1winHolder += h[1][ai1][1];
              else if(h[0][ai1][1] == 10)
              {
                  ai1winHolder += 12;
              }
              else if(h[0][ai1][1] == 11)
              {
                  ai1winHolder += 13;
              }
              else if(h[0][ai1][1] == 12)
              {
                  ai1winHolder += 14;
              }
              else if (h[0][ai1][1] == 0)
                  ai1winHolder += 15;

          }
          for(int ai2 = 0; ai2 < 4; ai2++)
          {
              if(h[2][ai2][1] != 0)
              ai2winHolder += h[2][ai2][1];
              else if(h[0][ai2][1] == 10)
              {
                  ai2winHolder += 12;
              }
              else if(h[0][ai2][1] == 11)
              {
                  ai2winHolder += 13;
              }
              else if(h[0][ai2][1] == 12)
              {
                  ai2winHolder += 14;
              }
              else if (h[0][ai2][1] == 0)
                  ai2winHolder+=15;
          }
          for(int dw = 0; dw < 4; dw++)
          {
              if(h[3][dw][1] != 0)
              dealWinholder += h[3][dw][1];
              else if(h[0][dw][1] == 10)
              {
                  dealWinholder += 12;
              }
              else if(h[0][dw][1] == 11)
              {
                  dealWinholder += 13;
              }
              else if(h[0][dw][1] == 12)
              {
                  dealWinholder += 14;
              }
              else if (h[0][dw][1] == 0)
                  dealWinholder += 15;
          }

              pcWinholder = winSuperLogic(pcWinholder, 0, hands);



              ai1winHolder = winSuperLogic(ai1winHolder, 1, hands);

              ai2winHolder = winSuperLogic(ai2winHolder, 2, hands);

              dealWinholder = winSuperLogic(dealWinholder, 3, hands);

          if (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder)
          {
            status.setText("You Win.");
          }
          else if (ai1winHolder > pcWinholder && ai1winHolder > ai2winHolder && ai1winHolder > dealWinholder)
          {
            status.setText("Left Computer Player Wins.");
          }
          else if (ai2winHolder > pcWinholder && ai2winHolder > ai1winHolder && ai2winHolder > dealWinholder)
          {
              status.setText("Right Computer Player Wins.");
          }
          else if (dealWinholder> pcWinholder && dealWinholder > ai1winHolder &&  dealWinholder > ai2winHolder)
          {
           status.setText("Dealer wins");
          }
          else if (pcWinholder <= ai1winHolder || pcWinholder <= ai2winHolder || pcWinholder <= dealWinholder || ai1winHolder <= ai2winHolder || ai1winHolder <= dealWinholder || ai2winHolder <= dealWinholder)
          {
              if((pcWinholder <= ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder) || (pcWinholder > ai1winHolder && pcWinholder <= ai2winHolder && pcWinholder > dealWinholder) || (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder <= dealWinholder) )
              {
                  if ((pcWinholder <= ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder))
                  {

                      if(pcWinholder > ai1winHolder)
                      {
                          status.setText("You Win.");
                      }
                      else if (pcWinholder < ai1winHolder)
                      {
                          status.setText("Left Computer Player Wins.");
                      }
                      else {
                          status.setText("Draw between you and left");
                      }


                  }
                  else if (pcWinholder > ai1winHolder && pcWinholder <= ai2winHolder && pcWinholder > dealWinholder)
                  {

                      if(pcWinholder > ai2winHolder)
                      {
                          status.setText("You Win.");
                      }
                      else if (pcWinholder < ai2winHolder)
                      {
                          status.setText("Right Computer Player Wins.");
                      }
                      else {
                          status.setText("Draw between you and right");
                      }
                  }
                  else if (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder <= dealWinholder)
                  {

                      if(pcWinholder > dealWinholder )
                      {
                          status.setText("You Win.");
                      }
                      else if (pcWinholder < dealWinholder )
                      {
                          status.setText("Dealer Wins.");
                      }
                      else {
                          status.setText("Draw you and dealer");
                      }

                  }
              }
              else if ((ai1winHolder <= ai2winHolder && ai1winHolder > dealWinholder) || (ai1winHolder > ai2winHolder && ai1winHolder <= dealWinholder))
              {
                  if ((ai1winHolder <= ai2winHolder && ai1winHolder > dealWinholder))
                  {

                      if(ai1winHolder > ai2winHolder)
                      {
                          status.setText("Left Computer Player Wins.");
                      }
                      else if (ai1winHolder < ai2winHolder)
                      {
                          status.setText("Right Computer Player Wins.");
                      }
                      else {
                          status.setText("Draw Between Left and Right");
                      }


                  }
                  else if ((pcWinholder > ai2winHolder && pcWinholder <= dealWinholder))
                  {

                      if(ai1winHolder > dealWinholder)
                      {
                          status.setText("Left Computer Player Wins.");
                      }
                      else if (ai1winHolder < dealWinholder)
                      {
                          status.setText("DealerWins.");
                      }
                      else {
                          status.setText("Draw between Right and Dealer");
                      }


                  }
              }
              else if (ai2winHolder <= dealWinholder)
              {


                  if(ai2winHolder > ai2winHolder)
                  {
                      status.setText("Right Computer Player Wins.");
                  }
                  else if (ai2winHolder < dealWinholder)
                  {
                      status.setText("Dealer Player Wins.");
                  }
                  else {
                      status.setText("Draw between Left and Dealer");
                  }


              }


          }
          for(int end = 0; end < 5; end++)
          {
              // pcMaster[end].setVisibility(View.VISIBLE);
              ai1Master[end].setImageDrawable(cardsMaster[h[1][end][0]][h[1][end][1]]);
              ai2Master[end].setImageDrawable(cardsMaster[h[2][end][0]][h[2][end][1]]);
              dealMaster[end].setImageDrawable(cardsMaster[h[3][end][0]][h[3][end][1]]);
          }
          btnDraw.setEnabled(false);
          btnDeal.setEnabled(false);
          btnBet.setEnabled(false);
          txtInput.setEnabled(false);
      }
//***********************************************************************************************************************************************
    public int winSuperLogic (int a, int e, int [][][] h)
    {
        int[] mult1  = new int[1];
        int[] mult2  = new int[1];
        int[] mult3  = new int[1];
        int[] mult4  = new int[1];
        int multcounter= 0;
        int multTot = 0;

        for(int w = 0; w<4; w++)
        {
            for(int c = 0; c<4;c++)
            {
                if(w != c)
                {
                    if(h[e][w][1] == h[e][c][1])
                    {
                        multcounter++;
                    }
                    if (h[e][w][1] == h[e][c][1] && multTot == 0)
                    {
                        if(h[e][w][1] != 0 || h[e][c][1] != 0 && h[e][w][1] == h[e][c][1]) {
                            multTot = h[e][w][1] * h[e][c][1];
                            mult1 = h[e][w];
                            mult2 = h[e][c];
                        }
                        else if(h[e][w][1] == 0 && h[e][c][1] == 0 && h[e][w][1] == h[e][c][1]) {
                            multTot = 21 * 21;
                            mult1 = h[e][w];
                            mult2 = h[e][c];
                        }
                        else if(h[e][w][1] == 10 && h[e][c][1] == 10 && h[e][w][1] == h[e][c][1]) {
                            multTot = 13 * 13;
                            mult1 = h[e][w];
                            mult2 = h[e][c];
                        }
                        else if(h[e][w][1] == 11 && h[e][c][1] == 11 && h[e][w][1] == h[e][c][1]) {
                                multTot = 15 * 15;
                                mult1 = h[e][w];
                                mult2 = h[e][c];
                            }
                        else if(h[e][w][1] == 12 && h[e][c][1] == 12 && h[e][w][1] == h[e][c][1]) {
                                multTot = 19 * 19;
                                mult1 = h[e][w];
                                mult2 = h[e][c];
                            }
                    }
                    else if (h[e][w][1] == h[e][c][1] && multTot != 0)
                    {
                        if(h[e][w][1] != 0 || h[e][c][1] != 0 && h[e][w][1] == h[e][c][1] && h[e][c] != mult1 && h[e][w] != mult2 && h[e][c] != mult2 )
                        multTot *= h[e][w][1] * h[e][c][1];
                        else if(h[e][w][1] == 0 && h[e][c][1] == 0 && h[e][w][1] == h[e][c][1]&& h[e][c] != mult1 && h[e][w] != mult2 && h[e][c] != mult2 )
                            multTot *= 21*21;
                        else if(h[e][w][1] == 10 && h[e][c][1] == 10 && h[e][w][1] == h[e][c][1]&& h[e][c] != mult1 && h[e][w] != mult2 && h[e][c] != mult2 )
                            multTot *= 13*13;
                        else if(h[e][w][1] == 11 && h[e][c][1] == 11 && h[e][w][1] == h[e][c][1]&& h[e][c] != mult1 && h[e][w] != mult2 && h[e][c] != mult2 )
                            multTot *= 15*15;
                        else if(h[e][w][1] == 12 && h[e][c][1] == 12 && h[e][w][1] == h[e][c][1]&& h[e][c] != mult1 && h[e][w] != mult2 && h[e][c] != mult2 )
                            multTot *= 19*19;

                    }
                }
                else if  (h[e][w][1] != h[e][c][1]) {

                }
            }

            //asList(h).contains(v);
        }

        if(multTot != 0){
        a += multTot * (multcounter/2);
        return a;}
        else{
        return a;
    }
    }
//*****************************************************************************************************************************************
        public void playerRedealer(ImageView[] vbox, int[][][] h, Drawable[][] d, int[][] cardVals) {

            for (int p = 0; p < vbox.length; p++) {
               // for (int x = 0; x < 5; x++) {

                  //  if (vbox[x] == pcMaster[p] && vbox[x]!=null) {
                        if(vbox[p] != null)
                        {
                        Random random1 = new Random();
                        int r1 = (random1.nextInt(12));
                        int r2 = (random1.nextInt(4));
                        if (cardVals[r2][r1] == -1) {
                            for (int g = 0; g < 52; g++) {
                                r1 = (random1.nextInt(12));
                                r2 = (random1.nextInt(4));
                                if (d[r2][r1] != null) {
                                    for(int x = 0; x <5; x++) {
                                        if(vbox[p] == pcMaster[x]) {
                                            h[0][x][0] = r2;
                                            h[0][x][1] = r1;
                                        }
                                    }
                                    vbox[p].setImageDrawable(d[r2][r1]);
                                    cardVals[r2][r1]= -1;
                                    g = 53;
                                }
                            }
                        } else if (cardVals[r2][r1] != -1) {
                            h[0][p][0] = r2;
                            h[0][p][1] = r1;
                            vbox[p].setImageDrawable(d[r2][r1]);
                            cardVals[r2][r1] = -1;
                        }
                    } else {

                    }

              //  }
            }
        }


    }