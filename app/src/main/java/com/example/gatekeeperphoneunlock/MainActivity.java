package com.example.gatekeeperphoneunlock;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Variables to keep track of state
    private int strikes = 0;
    private int numUselessHugs = 0;
    private int timesFed = 0;
    private boolean checkpoint1 = false;    // sheep is fed 3 times
    private boolean checkpoint2 = false;    // sheep is hugged after feeding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemUI();
    }

    // Code taken from https://developer.android.com/training/system-ui/immersive#java
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    ///////// RETURN TO DEFAULT IMAGE OF SHEEP /////////
    private void returnToDefault() {
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView sb = findViewById(R.id.speechBubble);

                // Change to picture with appropriate number of crumbs
                ImageView image = findViewById(R.id.sheep);
                if (checkpoint2 == true) {
                    image.setImageResource(R.drawable.sheep5);
                    sb.setImageResource(R.drawable.default1);
                    sb.setImageResource(R.drawable.default2);
                } else {
                    sb.setImageResource(R.drawable.default1);
                    if (timesFed == 0) {
                        image.setImageResource(R.drawable.sheep);
                    } else if (timesFed == 1) {
                        image.setImageResource(R.drawable.sheep2);
                    } else if (timesFed == 2) {
                        image.setImageResource(R.drawable.sheep3);
                    } else {
                        image.setImageResource(R.drawable.sheep4);
                    }
                }
            }
        }, 3000);
    }


    ///////// IF RAMMED BY SHEEP (UNSHAVED) /////////
    private void die() {
        final Handler handler2 = new Handler();

        // First image change
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image = findViewById(R.id.sheep);
                image.setImageResource(R.drawable.attacksheep);
            }
        }, 1500);

        // Locked out image + timer
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image2 = (ImageView) findViewById(R.id.attacking);
                image2.setImageResource(R.drawable.attacksheep2);

                new CountDownTimer(10000, 1000) {
                    TextView timer = findViewById(R.id.lockedOut);
                    public void onTick(long millisUntilFinished) {
                        timer.setText("You have been locked out.\nTry again in " + millisUntilFinished / 1000 + " seconds.");
                    }

                    public void onFinish() {
                        timer.setText(" ");
                    }
                }.start();
            }
        }, 3200);

        // Reset after timer finishes
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 13300);
    }

    ///////// IF RAMMED BY SHEEP (SHAVED) /////////
    private void dieShaved() {
        final Handler handler2 = new Handler();

        // First image change
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image = findViewById(R.id.sheep);
                image.setImageResource(R.drawable.shavedattack);
            }
        }, 1500);

        // Shaved locked out screen + timer
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image2 = (ImageView) findViewById(R.id.attacking);
                image2.setImageResource(R.drawable.shavedattack2);

                new CountDownTimer(10000, 1000) {
                    TextView timer = findViewById(R.id.lockedOut);
                    public void onTick(long millisUntilFinished) {
                        timer.setText("You have been locked out.\nTry again in " + millisUntilFinished / 1000 + " seconds.");
                    }

                    public void onFinish() {
                        timer.setText(" ");
                    }
                }.start();
            }
        }, 3200);

        // Reset after timer runs out
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 13300);
    }

    ///////// RESET AFTER DYING /////////
    private void reset() {
        ImageView sb = findViewById(R.id.speechBubble);
        sb.setImageResource(R.drawable.default1);

        // Change to default image of sheep (no crumbs)
        ImageView image = findViewById(R.id.sheep);
        image.setImageResource(R.drawable.sheep);

        // Hide locked out screens
        ImageView image2 = findViewById(R.id.attacking);
        image2.setImageResource(R.drawable.empty);

        strikes = 0;
        numUselessHugs = 0;
        timesFed = 0;
        checkpoint1 = false;
        checkpoint2 = false;
    }

    ///////// FIGHT /////////
    public void attack(View v) {
        strikes += 1;
        ImageView sb = findViewById(R.id.speechBubble);
        ImageView image = findViewById(R.id.sheep);

        if (strikes == 1) {
            // Change speech text
            sb.setImageResource(R.drawable.fight1);

            // Change image to angry sheep
            image.setImageResource(R.drawable.angrysheep);

            returnToDefault();
        }

        // change the message a little bit as a warning
        else if (strikes == 2) {
            sb.setImageResource(R.drawable.fight2);

            // Change image to angrier sheep
            image.setImageResource(R.drawable.angrysheep);

            returnToDefault();
        }

        // if you attack too many times, the sheep gores you
        else {
            sb.setImageResource(R.drawable.fight3);

            image.setImageResource(R.drawable.angrysheep2);

            die();
        }
    }

    ///////// HUG /////////
    public void hugged(View v) {
        ImageView sb = findViewById(R.id.speechBubble);
        ImageView image = findViewById(R.id.sheep);

        // If not fed 3 treats yet, do useless reactions
        if (checkpoint1 == false) {
            numUselessHugs += 1;

            if (numUselessHugs == 1) {
                sb.setImageResource(R.drawable.hug1);

                // Change to picture with appropriate number of crumbs
                if (timesFed == 0) {
                    image.setImageResource(R.drawable.relaxedsheep2);
                } else if (timesFed == 1) {
                    image.setImageResource(R.drawable.foodsheep);
                } else if (timesFed == 2) {
                    image.setImageResource(R.drawable.foodsheep2);
                } else {
                    image.setImageResource(R.drawable.foodsheep3);
                }
            }

            else if (numUselessHugs == 2) {
                sb.setImageResource(R.drawable.hug2);

                // Change to picture with appropriate number of crumbs
                if (timesFed == 0) {
                    image.setImageResource(R.drawable.relaxedsheep);
                } else if (timesFed == 1) {
                    image.setImageResource(R.drawable.relaxedsheep1i);
                } else if (timesFed == 2) {
                    image.setImageResource(R.drawable.relaxedsheep1ii);
                } else {
                    image.setImageResource(R.drawable.relaxedsheep1iii);
                }
            } else {
                sb.setImageResource(R.drawable.hug3);

                // Change to picture with appropriate number of crumbs
                if (timesFed == 0) {
                    image.setImageResource(R.drawable.relaxedsheep3);
                } else if (timesFed == 1) {
                    image.setImageResource(R.drawable.relaxedsheep3i);
                } else {
                    image.setImageResource(R.drawable.relaxedsheep3ii);
                }   // don't worry about 3x crumbs since it would go to the else statement below
            }
        }

        else {
            // Move on to next stage (change image/speech bubble)
            checkpoint2 = true;

            sb.setImageResource(R.drawable.hug4);
            image.setImageResource(R.drawable.sheep5);
        }

        returnToDefault();
    }


    ///////// TREAT /////////
    public void eat(View v) {
        timesFed += 1;

        // Change image to indicate state changes
        ImageView sb = findViewById(R.id.speechBubble);
        ImageView image = findViewById(R.id.sheep);

        if (timesFed == 1) {
            sb.setImageResource(R.drawable.eat1);
            image.setImageResource(R.drawable.foodsheep);
        }

        else if (timesFed == 2) {
            sb.setImageResource(R.drawable.eat2);
            image.setImageResource(R.drawable.foodsheep2);
        }

        else {
            sb.setImageResource(R.drawable.eat3);
            image.setImageResource(R.drawable.foodsheep3);

            checkpoint1 = true;
        }

        returnToDefault();
    }

    ///////// SHEARS /////////
    public void shaved(View v) {
        // Change speech bubble
        ImageView sb = findViewById(R.id.speechBubble);
        sb.setImageResource(R.drawable.shear);

        // Change sheep image
        ImageView image = findViewById(R.id.sheep);
        image.setImageResource(R.drawable.shavedsheep);

        dieShaved();    // Play lock out animation
    }

    ///////// SING /////////
    public void serenaded(View v) {
        ImageView sb = findViewById(R.id.speechBubble);

        // If not fed + hugged, do useless reaction
        if (checkpoint2 == false) {
            sb.setImageResource(R.drawable.sing);

            // Change to picture with appropriate number of crumbs
            ImageView image = findViewById(R.id.sheep);
            if (timesFed == 0) {
                image.setImageResource(R.drawable.disgustedsheep);
            } else if (timesFed == 1) {
                image.setImageResource(R.drawable.disgustedsheep2);
            } else if (timesFed == 2) {
                image.setImageResource(R.drawable.disgustedsheep3);
            } else {
                image.setImageResource(R.drawable.disgustedsheep4);
            }

            returnToDefault();
        }

        // Unlock phone
        else {
            sb.setImageResource(R.drawable.unlock);

            ImageView image = findViewById(R.id.sheep);
            image.setImageResource(R.drawable.sleepysheep);

            // Unlock by closing app
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //ImageView bg = findViewById(R.id.unlockedScreen);
                    //bg.setImageResource(R.drawable.bg);
                    finishAffinity();
                    System.exit(0);
                }
            }, 3000);
        }

    }

}
