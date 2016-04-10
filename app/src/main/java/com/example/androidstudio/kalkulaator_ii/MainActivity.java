package com.example.androidstudio.kalkulaator_ii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.BroadcastReceiver;

import com.example.androidstudio.kalkulaator_ii.dao.DataSource;
import com.example.androidstudio.kalkulaator_ii.model.DayStatistic;
import com.example.androidstudio.kalkulaator_ii.model.Expression;
import com.example.androidstudio.kalkulaator_ii.parcelable.DayStatisticParcelable;
import com.example.androidstudio.kalkulaator_ii.parcelable.ExpressionParcelable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AutoResizeTextView calcText;
    Button bReset;
    Button bBack;
    Button bDivide;
    Button bMultiple;
    Button bSubtract;
    Button bAdd;
    Button bResult;
    Button bComma;
    Button bZero;
    Button bOne;
    Button bTwo;
    Button bThree;
    Button bFour;
    Button bFive;
    Button bSix;
    Button bSeven;
    Button bEight;
    Button bNine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calcText = (AutoResizeTextView) findViewById(R.id.calc_text);
        bReset = (Button) findViewById(R.id.button_reset);
        bBack = (Button) findViewById(R.id.button_back);
        bDivide = (Button) findViewById(R.id.button_divide);
        bMultiple = (Button) findViewById(R.id.button_multiple);
        bSubtract = (Button) findViewById(R.id.button_subtract);
        bAdd = (Button) findViewById(R.id.button_add);
        bResult = (Button) findViewById(R.id.button_result);
        bComma = (Button) findViewById(R.id.button_comma);
        bZero = (Button) findViewById(R.id.button_zero);
        bOne = (Button) findViewById(R.id.button_one);
        bTwo = (Button) findViewById(R.id.button_two);
        bThree = (Button) findViewById(R.id.button_three);
        bFour = (Button) findViewById(R.id.button_four);
        bFive = (Button) findViewById(R.id.button_five);
        bSix = (Button) findViewById(R.id.button_six);
        bSeven = (Button) findViewById(R.id.button_seven);
        bEight = (Button) findViewById(R.id.button_eight);
        bNine = (Button) findViewById(R.id.button_nine);
        sendCalculatorBroadcast(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public void calcButtonClicked(View view) {
        Button b = (Button) view;
        sendCalculatorBroadcast(b.getText().toString());
        sendDatabaseCalculatorBroadcast("GET_ALL_EXPRESSION", null); //GET_ALL_DAYSTATISTIC
        sendDatabaseCalculatorBroadcast("GET_ALL_DAYSTATISTIC", null);
    }

    private void sendCalculatorBroadcast(String command) {
        Intent intent = new Intent();
        intent.setAction("com.ee.calculatorBroadcastRequest");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        if (command != null) {
            intent.putExtra("BUTTON_COMMAND", command);
        }
        sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = getResultExtras(true);
                if (bundle != null) {
                    calcText.setText(bundle.getString("CALC_TEXT"));
                    bReset.setEnabled(bundle.getBoolean("BUTTON_RESET_ENABLED"));
                    bBack.setEnabled(bundle.getBoolean("BUTTON_BACK_ENABLED"));
                    bDivide.setEnabled(bundle.getBoolean("BUTTON_DIVIDE_ENABLED"));
                    bMultiple.setEnabled(bundle.getBoolean("BUTTON_MULTIPLE_ENABLED"));
                    bSubtract.setEnabled(bundle.getBoolean("BUTTON_SUBTRACT_ENABLED"));
                    bAdd.setEnabled(bundle.getBoolean("BUTTON_ADD_ENABLED"));
                    bResult.setEnabled(bundle.getBoolean("BUTTON_RESULT_ENABLED"));
                    bComma.setEnabled(bundle.getBoolean("BUTTON_COMMA_ENABLED"));
                    bZero.setEnabled(bundle.getBoolean("BUTTON_ZERO_ENABLED"));
                    bOne.setEnabled(bundle.getBoolean("BUTTON_ONE_ENABLED"));
                    bTwo.setEnabled(bundle.getBoolean("BUTTON_TWO_ENABLED"));
                    bThree.setEnabled(bundle.getBoolean("BUTTON_THREE_ENABLED"));
                    bFour.setEnabled(bundle.getBoolean("BUTTON_FOUR_ENABLED"));
                    bFive.setEnabled(bundle.getBoolean("BUTTON_FIVE_ENABLED"));
                    bSix.setEnabled(bundle.getBoolean("BUTTON_SIX_ENABLED"));
                    bSeven.setEnabled(bundle.getBoolean("BUTTON_SEVEN_ENABLED"));
                    bEight.setEnabled(bundle.getBoolean("BUTTON_EIGHT_ENABLED"));
                    bNine.setEnabled(bundle.getBoolean("BUTTON_NINE_ENABLED"));
                    byte[] expressionParcelableData = bundle.getByteArray("EXPRESSION_DATA");
                    if (expressionParcelableData != null) {
                        Parcel parcel = Parcel.obtain();
                        parcel.unmarshall(expressionParcelableData, 0, expressionParcelableData.length);
                        parcel.setDataPosition(0);
                        ExpressionParcelable expressionParcelable = new ExpressionParcelable(parcel);
                        if (expressionParcelable != null) {
                            sendDatabaseCalculatorBroadcast("PUT_EXPRESSION", expressionParcelable);
                        }
                    }
                }
            }
        }, null, Activity.RESULT_OK, null, null);
    }

    private void sendDatabaseCalculatorBroadcast(String command, Object obj) {
        Intent intent = new Intent();
        intent.setAction("com.ee.databaseCalculatorBroadcastRequest");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("COMMAND", command);
        if (command.equals("PUT_EXPRESSION")) {
            ExpressionParcelable expressionParcelable = (ExpressionParcelable) obj;
            Parcel parcel = Parcel.obtain();
            expressionParcelable.writeToParcel(parcel, 0);
            intent.putExtra("DATA", parcel.marshall());
        }
        sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = getResultExtras(true);
                if (bundle != null) {
                    String resultType = bundle.getString("RESULT_TYPE");
                    if (resultType.equals("OK")) {

                    } else if (resultType.equals("ERROR")) {

                    } else {
                        if (resultType.equals("ALL_EXPRESSION_DATA")) {
                            int arrayCount = bundle.getInt("DATA_ARRAY_COUNT");
                            List<ExpressionParcelable> expressionParcelables = new ArrayList<ExpressionParcelable>();
                            for (int i = 0; i < arrayCount; i++) {
                                Parcel parcel = Parcel.obtain();
                                byte[] data = bundle.getByteArray("DATA_" + i);
                                parcel.unmarshall(data, 0, data.length);
                                parcel.setDataPosition(0);
                                ExpressionParcelable expressionParcelable = new ExpressionParcelable(parcel);
                                if (expressionParcelable != null) {
                                    expressionParcelables.add(expressionParcelable);
                                }
                            }
                            if (expressionParcelables != null) {

                            }
                        }
                    }
                }
            }
        }, null, Activity.RESULT_OK, null, null);
    }
}