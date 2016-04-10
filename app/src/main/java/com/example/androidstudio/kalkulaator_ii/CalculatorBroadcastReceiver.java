package com.example.androidstudio.kalkulaator_ii;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;

import com.example.androidstudio.kalkulaator_ii.parcelable.ExpressionParcelable;
import com.example.androidstudio.kalkulaator_ii.parcelable.OperationParcelable;

import java.util.ArrayList;

/**
 * Created by AndroidStudio on 19.03.2016.
 */
public class CalculatorBroadcastReceiver extends BroadcastReceiver {

    private static String calcTextData = "";
    private boolean bResetEnabled = false;
    private boolean bBackEnabled = false;
    private boolean bDivideEnabled = false;
    private boolean bMultipleEnabled = false;
    private boolean bSubtractEnabled = false;
    private boolean bAddEnabled = false;
    private boolean bResultEnabled = false;
    private boolean bCommaEnabled = false;
    private boolean bZeroEnabled = false;
    private boolean bOneEnabled = false;
    private boolean bTwoEnabled = false;
    private boolean bThreeEnabled = false;
    private boolean bFourEnabled = false;
    private boolean bFiveEnabled = false;
    private boolean bSixEnabled = false;
    private boolean bSevenEnabled = false;
    private boolean bEightEnabled = false;
    private boolean bNineEnabled = false;
    private ExpressionParcelable expressionParcelable;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOrderedBroadcast()) {
            expressionParcelable = null;
            Bundle bundle1 = intent.getExtras();
            if (bundle1 != null) {
                calcCommand(bundle1.getString("BUTTON_COMMAND"));
            }
            checkValid();

            Bundle bundle2 = new Bundle();
            bundle2.putString("CALC_TEXT", calcTextData);
            bundle2.putBoolean("BUTTON_RESET_ENABLED", bResetEnabled);
            bundle2.putBoolean("BUTTON_BACK_ENABLED", bBackEnabled);
            bundle2.putBoolean("BUTTON_DIVIDE_ENABLED", bDivideEnabled);
            bundle2.putBoolean("BUTTON_MULTIPLE_ENABLED", bMultipleEnabled);
            bundle2.putBoolean("BUTTON_SUBTRACT_ENABLED", bSubtractEnabled);
            bundle2.putBoolean("BUTTON_ADD_ENABLED", bAddEnabled);
            bundle2.putBoolean("BUTTON_RESULT_ENABLED", bResultEnabled);
            bundle2.putBoolean("BUTTON_COMMA_ENABLED", bCommaEnabled);
            bundle2.putBoolean("BUTTON_ZERO_ENABLED", bZeroEnabled);
            bundle2.putBoolean("BUTTON_ONE_ENABLED", bOneEnabled);
            bundle2.putBoolean("BUTTON_TWO_ENABLED", bTwoEnabled);
            bundle2.putBoolean("BUTTON_THREE_ENABLED", bThreeEnabled);
            bundle2.putBoolean("BUTTON_FOUR_ENABLED", bFourEnabled);
            bundle2.putBoolean("BUTTON_FIVE_ENABLED", bFiveEnabled);
            bundle2.putBoolean("BUTTON_SIX_ENABLED", bSixEnabled);
            bundle2.putBoolean("BUTTON_SEVEN_ENABLED", bSevenEnabled);
            bundle2.putBoolean("BUTTON_EIGHT_ENABLED", bEightEnabled);
            bundle2.putBoolean("BUTTON_NINE_ENABLED", bNineEnabled);
            if (expressionParcelable != null) {
                Parcel parcel = Parcel.obtain();
                expressionParcelable.writeToParcel(parcel, 0);
                bundle2.putByteArray("EXPRESSION_DATA", parcel.marshall());
                //bundle2.putParcelable("EXPRESSION_DATA", expressionParcelable);
            }
            setResultExtras(bundle2);
            setResultCode(Activity.RESULT_OK);
        }
    }

    public void calcCommand(String command) {
        String[] calcTextArrayData = calcTextData.split("=");
        boolean answered = calcTextArrayData.length > 1;
        if (command.equals("Back")) {
            if (calcTextData.length() != 0) {
                if (answered) {
                    answered = false;
                    calcTextData = calcTextArrayData[0];
                } else {
                    if (calcTextData.length() == 1) {
                        calcTextData = "";
                    } else {
                        calcTextData = calcTextData.substring(0, calcTextData.length() - 1);
                    }
                }
            }
        } else if (command.equals("C")) {
            calcTextData = "";
            answered = false;
        } else if (command.equals("=")) {
            if (!answered) {
                try {
                    MathNode mathNode = MathNode.parse(calcTextData.replace('/', ':').replace(',', '.'));
                    double answeredValue = mathNode.answer();
                    ArrayList<DoubleStatistic> dm = mathNode.getDoubleStatistics();
                    setExpressionParcelable(dm, calcTextData, answeredValue);
                    if (Double.isInfinite(answeredValue)) {
                        if (answeredValue == Double.POSITIVE_INFINITY) {
                            calcTextData = String.format("%s=%s", calcTextData, "\u221E");
                        } else {
                            calcTextData = String.format("%s=%s", calcTextData, "-\u221E");
                        }
                    } else {
                        calcTextData = String.format("%s=%s", calcTextData, doubleToString(answeredValue).replace('.', ','));
                    }
                } catch (Exception e) {
                    calcTextData = String.format("%s=%s", calcTextData, "error");
                }
                answered = true;
            }
        } else if (command.equals("/") || command.equals("*") || command.equals("-") || command.equals("+")) {
            if (answered) {
                calcTextData = calcTextArrayData[1] + command;
            } else {
                if (calcTextData.length() != 0) {
                    Character lastChar = calcTextData.charAt(calcTextData.length() - 1);
                    if (lastChar == '/' || lastChar == '*' || lastChar == '-' || lastChar == '+') {
                        calcTextData = calcTextData.substring(0, calcTextData.length() - 1) + command;
                    } else {
                        calcTextData += command;
                    }
                } else if (command.equals("-")) {
                    calcTextData += command;
                }
            }
        } else {
            if (answered) {
                calcTextData = command;
                answered = false;
            } else {
                calcTextData += command;
            }
        }
    }

    private String doubleToString(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    private void setExpressionParcelable(ArrayList<DoubleStatistic> doubleStatistics, String expression, double result) {
        expressionParcelable = new ExpressionParcelable();
        expressionParcelable.data = expression;
        if (Double.isInfinite(result)) {
            if (result == Double.POSITIVE_INFINITY) {
                expressionParcelable.result = "\u221E";
            } else {
                expressionParcelable.result = "-\u221E";
            }
        } else {
            expressionParcelable.result = doubleToString(result).replace('.', ',');
        }
        expressionParcelable.operationParcelables = new ArrayList<OperationParcelable>();
        for (DoubleStatistic doubleStatistic : doubleStatistics) {
            OperationParcelable operationParcelable = new OperationParcelable();
            operationParcelable.num1 = (float) doubleStatistic.val1;
            operationParcelable.num2 = (float) doubleStatistic.val2;
            operationParcelable.result = (float) doubleStatistic.result;
            operationParcelable.operand = doubleStatistic.op;
            if (Double.isInfinite(doubleStatistic.result)) {
                if (Double.POSITIVE_INFINITY == doubleStatistic.result) {
                    operationParcelable.resultType = "POSITIVE_INFINITY";
                } else {
                    operationParcelable.resultType = "NEGATIVE_INFINITY";
                }
            } else if (Double.isNaN(doubleStatistic.result)) {
                operationParcelable.resultType = "NaN";
            } else {
                operationParcelable.resultType = "NORMAL";
            }
            expressionParcelable.operationParcelables.add(operationParcelable);
        }
    }

    private void checkValid() {
        bSubtractEnabled = true;
        String[] calcTextArrayData = calcTextData.split("=");
        boolean answered = calcTextArrayData.length > 1;
        bZeroEnabled = bOneEnabled = bTwoEnabled = bThreeEnabled = bFourEnabled = bFiveEnabled = bSixEnabled = bSevenEnabled = bEightEnabled = bNineEnabled = true;
        if (calcTextData.equals("")) {
            bResetEnabled = bBackEnabled = bCommaEnabled = bResultEnabled = bDivideEnabled = bMultipleEnabled = bAddEnabled = false;
        } else {
            bResetEnabled = bBackEnabled = bDivideEnabled = bMultipleEnabled = bAddEnabled = bCommaEnabled = true;
            bResultEnabled = false;
            Character lastChar = calcTextData.charAt(calcTextData.length() - 1);
            if (lastChar == '-') {
                bSubtractEnabled = bCommaEnabled = false;
                if (calcTextData.equals("-")) {
                    bDivideEnabled = bMultipleEnabled = bAddEnabled = false;
                } else if (calcTextData.length() > 1) {
                    Character c = calcTextData.charAt(calcTextData.length() - 2);
                    if (c == '(') {
                        bDivideEnabled = bMultipleEnabled = bAddEnabled = false;
                    }
                }
            } else if (lastChar == '+') {
                bAddEnabled = bCommaEnabled = false;
            } else if (lastChar == '/') {
                bDivideEnabled = bCommaEnabled = false;
            } else if (lastChar == '*') {
                bMultipleEnabled = bCommaEnabled = false;
            } else if (lastChar == ',') {
                bCommaEnabled = bDivideEnabled = bMultipleEnabled = bSubtractEnabled = bAddEnabled = false;
            } else if (lastChar == '(') {
                bDivideEnabled = bMultipleEnabled = bAddEnabled = bCommaEnabled = false;
            } else if (lastChar == ')') {
                bCommaEnabled = false;
                bResultEnabled = !answered;
            } else if (Character.isLetter(lastChar)) {
                bDivideEnabled = bMultipleEnabled = bAddEnabled = bSubtractEnabled = bCommaEnabled = false;
            } else {
                bResultEnabled = bCommaEnabled = !answered;
                bZeroEnabled = true;
                Character beforeChar = lastChar;
                if (!answered) {
                    for (int i = calcTextData.length() - 1; i >= 0; i--) {
                        Character c = calcTextData.charAt(i);
                        if (c == '0') {
                            beforeChar = c;
                            bZeroEnabled = false;
                            bOneEnabled = bTwoEnabled = bThreeEnabled = bFourEnabled = bFiveEnabled = bSixEnabled = bSevenEnabled = bEightEnabled = bNineEnabled = false;
                        } else if (c == '/' || c == '*' || c == '-' || c == '+' || c == '(' || c == ')') {
                            if (beforeChar != '0') {
                                bZeroEnabled = true;
                                bOneEnabled = bTwoEnabled = bThreeEnabled = bFourEnabled = bFiveEnabled = bSixEnabled = bSevenEnabled = bEightEnabled = bNineEnabled = true;
                            }
                            break;
                        } else {
                            beforeChar = c;
                        }
                    }
                }
                for (int i = calcTextData.length() - 1; i > 0; i--) {
                    Character c = calcTextData.charAt(i);
                    if (c == '/' || c == '*' || c == '-' || c == '+' || c == '(' || c == ')') {
                        break;
                    } else if (c == ',') {
                        bCommaEnabled = false;
                        bZeroEnabled = true;
                        bOneEnabled = bTwoEnabled = bThreeEnabled = bFourEnabled = bFiveEnabled = bSixEnabled = bSevenEnabled = bEightEnabled = bNineEnabled = true;
                        break;
                    }
                }
            }
        }
    }
}
