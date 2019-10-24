package cs.hku.hk.card24;

/**
 *
 * COMP3330 Individual Assignment 1
 * @author: Michal J Sekulski, 3035650438
 * October 23 2019
 *
 * */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.Jep;
import com.singularsys.jep.ParseException;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //http://scripts.cac.psu.edu/staff/r/j/rjg5/scripts/Math24.pl

    Button rePick;
    Button checkInput;
    Button clear;
    Button left;
    Button right;
    Button plus;
    Button minus;
    Button multiply;
    Button divide;
    TextView expression;
    ImageButton[] cards;
    int[] data;
    int[] card;
    int[] imageCount;
    private Object ArrayList;
    //preRandCard - an array to filter and draw random numbers
    ArrayList<Integer> preRandCard = new ArrayList<Integer>();
    //entNum - int variable helping with receiving the value from the first activity
    int entNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign and pass the value from the first activity
        entNum = getIntent().getIntExtra(OpenScreen.PASSED_VALUE, 0);

        rePick = (Button)findViewById(R.id.repick);
        checkInput = (Button)findViewById(R.id.checkinput);
        left = (Button)findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        multiply = (Button)findViewById(R.id.multiply);
        divide = (Button)findViewById(R.id.divide);
        clear = (Button)findViewById(R.id.clear);
        expression = (TextView)findViewById(R.id.input);
        //Add the value to the hint
        expression.setHint("Please form an expression such that the result is "+entNum);
        //Enable and set the rules for the buttons
        TextWatcher expressionWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String expressionInput = expression.getText().toString().trim();

                //Opening bracket and cards
                left.setEnabled(expressionInput.isEmpty() || checkIfArithmeticSymbolBeforeTrue(expressionInput));
                cards[0].setEnabled(expressionInput.isEmpty() || checkIfArithmeticSymbolBeforeTrue(expressionInput));
                cards[1].setEnabled(expressionInput.isEmpty() || checkIfArithmeticSymbolBeforeTrue(expressionInput));
                cards[2].setEnabled(expressionInput.isEmpty() || checkIfArithmeticSymbolBeforeTrue(expressionInput));
                cards[3].setEnabled(expressionInput.isEmpty() || checkIfArithmeticSymbolBeforeTrue(expressionInput));

                //Disable if beginning of the expression or last character is arithmetic symbol
                divide.setEnabled(!expressionInput.isEmpty() && checkIfArithmeticSymbolBeforeFalse(expressionInput));
                minus.setEnabled(!expressionInput.isEmpty() && checkIfArithmeticSymbolBeforeFalse(expressionInput));
                plus.setEnabled(!expressionInput.isEmpty() && checkIfArithmeticSymbolBeforeFalse(expressionInput));
                multiply.setEnabled(!expressionInput.isEmpty() && checkIfArithmeticSymbolBeforeFalse(expressionInput));
                right.setEnabled(!expressionInput.isEmpty() && checkIfArithmeticSymbolBeforeFalse(expressionInput));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        expression.addTextChangedListener(expressionWatcher);

        cards = new ImageButton[4];
        cards[0] = (ImageButton) findViewById(R.id.card1);
        cards[1] = (ImageButton) findViewById(R.id.card2);
        cards[2] = (ImageButton) findViewById(R.id.card3);
        cards[3] = (ImageButton) findViewById(R.id.card4);

        cards[0].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(0);
            }
        });
        cards[1].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(1);
            }
        });
        cards[2].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(2);
            }
        });
        cards[3].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(3);
            }
        });

        imageCount = new int[4];

        left.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append("(");
            }
        });
        right.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append(")");
            }
        });
        plus.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append("+");
            }
        });
        minus.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append("-");
            }
        });
        multiply.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append("*");
            }
        });
        divide.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                    expression.append("/");
            }
        });
        clear.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                setClear();
            }
        });
        // "Restart" the activity
        rePick.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                finish();
                //overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });

        checkInput.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                String inputStr = expression.getText().toString();
                if (checkInput(inputStr)) {
                    Toast.makeText(MainActivity.this, "Correct Answer",
                            Toast.LENGTH_SHORT).show();
                    pickCard();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Answer",
                            Toast.LENGTH_SHORT).show();
                    setClear();
                }
            }
        });

        initCardImage();
        pickCard();
    }

    private void initCardImage() {
        for(int i = 0; i<4; i++){
            int resID = getResources().getIdentifier("back_0", "drawable", "cs.hku.hk.card24");
            cards[i].setImageResource(resID);
            //cards[i].setClickable(true);
        }
    }

    //Generate the randomised values using the arraylist that was created earlier
    private void pickCard(){
        data = new int[4];
        card = new int[4];

        Random randomValue = new Random();

        for (int iter=1; iter<= 51; iter++ ) {
            preRandCard.add(iter);
        }

        for (int it=0; it<= 3; it++ ) {
            int tempVal = randomValue.nextInt(preRandCard.size());
            System.out.println(tempVal);
            card[it] = tempVal;
            data[it] = card[it];
            //System.out.println(preRandCard);
            preRandCard.remove(preRandCard.indexOf(tempVal));
            //System.out.println(preRandCard);
        }
        setClear();
    }

    private void setClear(){
        int resID;
        expression.setText("");
        for (int i = 0; i < 4; i++) {
            imageCount[i] = 0;
            resID = getResources().getIdentifier("card"+card[i],"drawable", "cs.hku.hk.card24");
            cards[i].setImageResource(resID);
            cards[i].setClickable(true);
        }
    }

    private void clickCard(int i) {
        int resId;
        String num;
        Integer value;
        if (imageCount[i] == 0) {
            resId = getResources().getIdentifier("back_0","drawable", "cs.hku.hk.card24");
            cards[i].setImageResource(resId);
            cards[i].setClickable(false);
            value = validateValue(data[i]);
            num = value.toString();
            //if(correctExpressionRule())
                expression.append(num);
            imageCount[i] ++;
        }
    }

    private boolean checkInput(String input) {
        //Check if the sum of elements in the array is equal to 4 and if so move on to checking
        //whether the expression is correct
        int sumOfOnes = 0;
        Boolean cond = false;

        for (int i : imageCount){
            sumOfOnes += i;
        }

        if(sumOfOnes == 4) {
            Jep jep = new Jep();
            Object res;
            try {
                jep.parse(input);
                res = jep.evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "Wrong Expression", Toast.LENGTH_SHORT).show();
                return false;
            } catch (EvaluationException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "Wrong Expression", Toast.LENGTH_SHORT).show();
                return false;
            }

            Double ca = (Double) res;
            //Set the value from the first activity
            if (Math.abs(ca - entNum) < 1e-6)
                cond = true;

        } else {
            Toast.makeText(MainActivity.this,
                    "Not all of the cards were used", Toast.LENGTH_SHORT).show();
            cond = false;
        }
        return cond;
    }

    /********************-- Additional Methods -- ********************/

    //Assign values to the cards
    private int validateValue(int checkValue){
        int actualValue = 0;
        if((checkValue == 1)||(checkValue == 14)||(checkValue == 27)||(checkValue == 40))
            actualValue = 1;
        else if((checkValue == 2)||(checkValue == 15)||(checkValue == 28)||(checkValue == 41))
            actualValue = 2;
        else if((checkValue == 3)||(checkValue == 16)||(checkValue == 29)||(checkValue == 42))
            actualValue = 3;
        else if((checkValue == 4)||(checkValue == 17)||(checkValue == 30)||(checkValue == 43))
            actualValue = 4;
        else if((checkValue == 5)||(checkValue == 18)||(checkValue == 31)||(checkValue == 44))
            actualValue = 5;
        else if((checkValue == 6)||(checkValue == 19)||(checkValue == 32)||(checkValue == 45))
            actualValue = 6;
        else if((checkValue == 7)||(checkValue == 20)||(checkValue == 33)||(checkValue == 46))
            actualValue = 7;
        else if((checkValue == 8)||(checkValue == 21)||(checkValue == 34)||(checkValue == 47))
            actualValue = 8;
        else if((checkValue == 9)||(checkValue == 22)||(checkValue == 35)||(checkValue == 48))
            actualValue = 9;
        else if((checkValue == 10)||(checkValue == 23)||(checkValue == 36)||(checkValue == 49))
            actualValue = 10;
        else if((checkValue == 11)||(checkValue == 24)||(checkValue == 37)||(checkValue == 50))
            actualValue = 11;
        else if((checkValue == 12)||(checkValue == 25)||(checkValue == 38)||(checkValue == 51))
            actualValue = 12;
        else if((checkValue == 13)||(checkValue == 26)||(checkValue == 39)||(checkValue == 52))
            actualValue = 13;
        else {
            //Just small fun idea
            Toast.makeText(MainActivity.this,
                    "This should never happen :)!", Toast.LENGTH_SHORT).show();
        }

        return actualValue;
    }


    //Declarations of the methods used for enabling and disabling buttons

    private Boolean checkIfArithmeticSymbolBeforeFalse(String arySymbol){
        if (arySymbol.charAt(arySymbol.length() - 1) == '1'||
            arySymbol.charAt(arySymbol.length() - 1) == '2'||
            arySymbol.charAt(arySymbol.length() - 1) == '3'||
            arySymbol.charAt(arySymbol.length() - 1) == '4'||
            arySymbol.charAt(arySymbol.length() - 1) == '5'||
            arySymbol.charAt(arySymbol.length() - 1) == '6'||
            arySymbol.charAt(arySymbol.length() - 1) == '7'||
            arySymbol.charAt(arySymbol.length() - 1) == '8'||
            arySymbol.charAt(arySymbol.length() - 1) == '9'||
            arySymbol.charAt(arySymbol.length() - 1) == '0'||
            arySymbol.charAt(arySymbol.length() - 1) == ')'){
            return true;
        } else
            return false;
    }

    private Boolean checkIfArithmeticSymbolBeforeTrue(String arySymbol){
        if ((arySymbol.charAt(arySymbol.length() - 1) == '/')||
           (arySymbol.charAt(arySymbol.length() - 1) == '-') ||
           (arySymbol.charAt(arySymbol.length() - 1) == '+') ||
           (arySymbol.charAt(arySymbol.length() - 1) == '*') ||
           (arySymbol.charAt(arySymbol.length() - 1) == '(')){
            return true;
        } else
            return false;
    }


}
