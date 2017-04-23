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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //a long list of attaching views to Java code
        btnDeal = (Button) findViewById(R.id.btnBegin);
        Drawable cardBack = ResourcesCompat.getDrawable(getResources(), R.drawable.b1fv, null);
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
        vg = (RelativeLayout) findViewById(R.id.activity_main);


    }

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

    ;
    View.OnClickListener drawingCards = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  if(cardMarkerpc != 0) {
                playerRedealer(getMarks(pcViews), hands, cardsPlay);
          //  }

        }
    };
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


    CountDownTimer ctd = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

        }
    };

    View.OnClickListener reset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pot.setText("0");
            btnBet.setEnabled(false);
            btnDeal.setEnabled(true);
            btnDraw.setEnabled(false);
            btnReset.setEnabled(false);
            for(int r = 0; r<5; r++)
            {
                if (r != 5)
                {
                    pcMaster[r].setVisibility(View.INVISIBLE);
                    ai1Master[r].setVisibility(View.INVISIBLE);
                    ai2Master[r].setVisibility(View.INVISIBLE);
                    dealMaster[r].setVisibility(View.INVISIBLE);
                }
            }
            cardsPlay = cardsMaster;
            thePot = 0;
        }
        };
    View.OnClickListener setBet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String hold1 = txtInput.getText().toString();
            if (turnMarker == 0) {
                if (hold1 != null) {
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //    SystemClock.sleep(1000);
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    Dealer(cardsPlay, hands);
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
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //    SystemClock.sleep(1000);
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e1)
                    {

                    }
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e1)
                    {

                    }
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e1)
                    {

                    }
                    thePot += Integer.parseInt(hold1);
                    pot.setText(Integer.toString(thePot));
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                    //SystemClock.sleep(1000);
                    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

                    determineWinner(hands);
                    turnMarker++;
                }


            }
        }
        }

        ;

        public void Dealer(Drawable[][] d, int[][][] h) {

            for (int x = 0; x < 20; x++) {
                Random random1 = new Random();
                int r1 = (random1.nextInt(12));
                int r2 = (random1.nextInt(4));
                if (d[r2][r1] == null) {
                    for (int g = 0; g < 52; g++) {
                        r1 = (random1.nextInt(12));
                        r2 = (random1.nextInt(4));
                        if (d[r2][r1] != null) {
                            g = 53;
                        }
                    }
                }
                if (x == 0 || x == 4 || x == 8 || x == 12 || x == 16) {
                    if (x == 0) {
                        playerCard1.setVisibility(View.VISIBLE);
                        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

                        h[0][0][0] = r2;
                        h[0][0][1] = r1;
                        playerCard1.setImageDrawable(d[r2][r1]);


                        d[r2][r1] = null;
                    } else if (x == 4) {
                        playerCard2.setVisibility(View.VISIBLE);

                        h[0][1][0] = r2;
                        h[0][1][1] = r1;
                        playerCard2.setImageDrawable(d[r2][r1]);

                        d[r2][r1] = null;
                    } else if (x == 8) {
                        playerCard3.setVisibility(View.VISIBLE);
                        h[0][2][0] = r2;
                        h[0][2][1] = r1;
                        playerCard3.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 12) {
                        playerCard4.setVisibility(View.VISIBLE);
                        h[0][3][0] = r2;
                        h[0][3][1] = r1;
                        playerCard4.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 16) {
                        playerCard5.setVisibility(View.VISIBLE);
                        h[0][4][0] = r2;
                        h[0][4][1] = r1;
                        playerCard5.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    }
                } else if (x == 1 || x == 5 || x == 9 || x == 13 || x == 17) {
                    if (x == 1) {
                        ai1Card1.setVisibility(View.VISIBLE);
                        h[1][0][0] = r2;
                        h[1][0][1] = r1;
                  //      ai1Card1.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 5) {
                        ai1Card2.setVisibility(View.VISIBLE);
                        h[1][1][0] = r2;
                        h[1][1][1] = r1;
                  //      ai1Card2.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 9) {
                        ai1Card3.setVisibility(View.VISIBLE);
                        h[1][2][0] = r2;
                        h[1][2][1] = r1;
                    //    ai1Card3.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 13) {
                        ai1Card4.setVisibility(View.VISIBLE);
                        h[1][3][0] = r2;
                        h[1][3][1] = r1;
                   //     ai1Card4.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 17) {
                        ai1Card5.setVisibility(View.VISIBLE);
                        h[1][4][0] = r2;
                        h[1][4][1] = r1;
                  //      ai1Card5.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    }
                } else if (x == 2 || x == 6 || x == 10 || x == 14 || x == 18) {
                    if (x == 2) {
                        ai2Card1.setVisibility(View.VISIBLE);
                        h[2][0][0] = r2;
                        h[2][0][1] = r1;
                  //      ai2Card1.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 6) {
                        ai2Card2.setVisibility(View.VISIBLE);
                        h[2][1][0] = r2;
                        h[2][1][1] = r1;
                      //  ai2Card2.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 10) {
                        ai2Card3.setVisibility(View.VISIBLE);
                        h[2][2][0] = r2;
                        h[2][2][1] = r1;
                      //  ai2Card3.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 14) {
                        ai2Card4.setVisibility(View.VISIBLE);
                        h[2][3][0] = r2;
                        h[2][3][1] = r1;
                     //   ai2Card4.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 18) {
                        ai2Card5.setVisibility(View.VISIBLE);
                        h[2][4][0] = r2;
                        h[2][4][1] = r1;
                      //  ai2Card5.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    }
                } else if (x == 3 || x == 7 || x == 11 || x == 15 || x == 19) {
                    if (x == 3) {
                        dealerCard1.setVisibility(View.VISIBLE);
                        h[3][0][0] = r2;
                        h[3][0][1] = r1;
                    //    dealerCard1.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 7) {
                        dealerCard2.setVisibility(View.VISIBLE);
                        h[3][1][0] = r2;
                        h[3][1][1] = r1;
                   //     dealerCard2.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 11) {
                        dealerCard3.setVisibility(View.VISIBLE);
                        h[3][2][0] = r2;
                        h[3][2][1] = r1;
                     //   dealerCard3.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 15) {
                        dealerCard4.setVisibility(View.VISIBLE);
                        h[3][3][0] = r2;
                        h[3][3][1] = r1;
                        //dealerCard4.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    } else if (x == 19) {
                        dealerCard5.setVisibility(View.VISIBLE);
                        h[3][4][0] = r2;
                        h[3][4][1] = r1;
                        //dealerCard5.setImageDrawable(d[r2][r1]);
                        d[r2][r1] = null;
                    }
                }
            }

        }


        public ImageView[] getMarks(View[] vbox) {
            ImageView[] holdBox = new ImageView[vbox.length];
            for (int g = 0; g < vbox.length - 1; g++) {
                holdBox[g] = (ImageView) vbox[g];

            }

            return holdBox;
        }

      public void determineWinner(int[][][] h)
      {
          int pcWinholder=0;
          int ai1winHolder=0;
          int ai2winHolder=0;
          int dealWinholder=0;

          //player Calc
          for(int pm = 0; pm < 4; pm++)
          {
             pcWinholder += h[0][pm][1];
          }
          for(int ai1 = 0; ai1 < 4; ai1++)
          {
              ai1winHolder += h[1][ai1][1];
          }
          for(int ai2 = 0; ai2 < 4; ai2++)
          {
              ai2winHolder += h[2][ai2][1];
          }
          for(int dw = 0; dw < 4; dw++)
          {
              dealWinholder += h[3][dw][1];
          }
          if (pcWinholder <= ai1winHolder || pcWinholder <= ai2winHolder || pcWinholder <= dealWinholder || ai1winHolder <= ai2winHolder || ai1winHolder <= dealWinholder || ai2winHolder <= dealWinholder)
          {
              if((pcWinholder <= ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder) || (pcWinholder > ai1winHolder && pcWinholder <= ai2winHolder && pcWinholder > dealWinholder) || (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder <= dealWinholder) )
              {
                  if ((pcWinholder <= ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder))
                  {
                      pcWinholder += winSuperLogic(pcWinholder,0,hands);
                      ai1winHolder += winSuperLogic(ai1winHolder,1,hands);
                      if(pcWinholder > ai1winHolder)
                      {
                          pot.setText("You Win.");
                      }
                      else if (pcWinholder < ai1winHolder)
                      {
                          pot.setText("Left Computer Player Wins.");
                      }
                      else {
                          pot.setText("Draw");
                      }


                  }
                  else if (pcWinholder > ai1winHolder && pcWinholder <= ai2winHolder && pcWinholder > dealWinholder)
                  {
                      pcWinholder += winSuperLogic(pcWinholder,0,hands);
                      ai2winHolder += winSuperLogic(ai2winHolder,2,hands);
                      if(pcWinholder > ai2winHolder)
                      {
                          pot.setText("You Win.");
                      }
                      else if (pcWinholder < ai2winHolder)
                      {
                          pot.setText("Right Computer Player Wins.");
                      }
                      else {
                          pot.setText("Draw");
                      }
                  }
                  else if (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder <= dealWinholder)
                  {
                      pcWinholder += winSuperLogic(pcWinholder,0,hands);
                      dealWinholder += winSuperLogic(dealWinholder ,3,hands);
                      if(pcWinholder > dealWinholder )
                      {
                          pot.setText("You Win.");
                      }
                      else if (pcWinholder < dealWinholder )
                      {
                          pot.setText("Dealer Wins.");
                      }
                      else {
                          pot.setText("Draw");
                      }

                  }
              }
              else if ((ai1winHolder <= ai2winHolder && ai1winHolder > dealWinholder) || (ai1winHolder > ai2winHolder && ai1winHolder <= dealWinholder))
              {
                  if ((ai1winHolder <= ai2winHolder && ai1winHolder > dealWinholder))
                  {
                      ai1winHolder += winSuperLogic(ai1winHolder,1,hands);
                      ai2winHolder += winSuperLogic(ai2winHolder,2,hands);
                      if(ai1winHolder > ai2winHolder)
                      {
                          pot.setText("Left Computer Player Wins.");
                      }
                      else if (ai1winHolder < ai2winHolder)
                      {
                          pot.setText("Right Computer Player Wins.");
                      }
                      else {
                          pot.setText("Draw");
                      }


                  }
                  else if ((pcWinholder > ai2winHolder && pcWinholder <= dealWinholder))
                  {
                      ai1winHolder += winSuperLogic(ai1winHolder,1,hands);
                      dealWinholder += winSuperLogic(ai2winHolder,3,hands);
                      if(ai1winHolder > dealWinholder)
                      {
                          pot.setText("Left Computer Player Wins.");
                      }
                      else if (ai1winHolder < dealWinholder)
                      {
                          pot.setText("DealerWins.");
                      }
                      else {
                          pot.setText("Draw");
                      }


                  }
              }
              else if (ai2winHolder <= dealWinholder)
              {

                      ai2winHolder += winSuperLogic(ai2winHolder,2,hands);
                      dealWinholder += winSuperLogic(dealWinholder,3,hands);
                      if(ai2winHolder > ai2winHolder)
                      {
                          pot.setText("Left Computer Player Wins.");
                      }
                      else if (ai2winHolder < dealWinholder)
                      {
                          pot.setText("Right Computer Player Wins.");
                      }
                      else {
                          pot.setText("Draw");
                      }


                  }

          }
          else if (pcWinholder > ai1winHolder && pcWinholder > ai2winHolder && pcWinholder > dealWinholder)
          {
            pot.setText("You Win.");
          }
          else if (ai1winHolder > pcWinholder && ai1winHolder > ai2winHolder && ai1winHolder > dealWinholder)
          {
            pot.setText("Left Computer Player Wins.");
          }
          else if (ai2winHolder > pcWinholder && ai2winHolder > ai1winHolder && ai2winHolder > dealWinholder)
          {
              pot.setText("Right Computer Player Wins.");
          }
          else if (dealWinholder> pcWinholder && dealWinholder > ai1winHolder &&  dealWinholder > ai2winHolder)
          {
            pot.setText("Dealer wins");
          }
          btnDraw.setEnabled(false);
          btnDeal.setEnabled(false);
          btnBet.setEnabled(false);
          txtInput.setEnabled(false);
      }

    public int winSuperLogic (int a, int e, int [][][] h)
    {
       // int mult1 = 0;
       // int mult2 = 0;
        int multTot = 0;
        for(int w = 0; w<4; w++)
        {
            for(int c = 0; c<4;c++)
            {
                if(w != c)
                {
                    if (h[e][w][1] == h[e][c][1] && multTot == 0)
                    {
                        multTot = h[e][w][1] * h[e][c][1];
                    }
                    else if (h[e][w][1] == h[e][c][1] && multTot != 0)
                    {
                        multTot *= h[e][w][1] * h[e][c][1];
                    }
                }
                else {

                }
            }

            //asList(h).contains(v);
        }
        a += multTot;
        return a;
    }

        public void playerRedealer(ImageView[] vbox, int[][][] h, Drawable[][] d) {

            for (int p = 0; p < vbox.length - 1; p++) {
                for (int x = 0; x < 4; x++) {

                    if (vbox[p] == pcMaster[x]) {

                        Random random1 = new Random();
                        int r1 = (random1.nextInt(12));
                        int r2 = (random1.nextInt(4));
                        if (d[r2][r1] == null) {
                            for (int g = 0; g < 52; g++) {
                                r1 = (random1.nextInt(12));
                                r2 = (random1.nextInt(4));
                                if (d[r2][r1] != null) {
                                    g = 53;
                                }
                            }
                        } else if (d[r2][r1] != null) {
                            h[0][x][0] = r2;
                            h[0][x][1] = r1;
                            pcMaster[x].setImageDrawable(d[r2][r1]);
                            d[r2][r1] = null;
                        }
                    } else {

                    }

                }
            }
        }


    }

