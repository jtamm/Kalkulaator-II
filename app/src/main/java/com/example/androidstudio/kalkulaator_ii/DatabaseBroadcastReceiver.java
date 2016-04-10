package com.example.androidstudio.kalkulaator_ii;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;

import com.example.androidstudio.kalkulaator_ii.dao.DataSource;
import com.example.androidstudio.kalkulaator_ii.model.DayStatistic;
import com.example.androidstudio.kalkulaator_ii.model.DoubleType;
import com.example.androidstudio.kalkulaator_ii.model.Expression;
import com.example.androidstudio.kalkulaator_ii.model.Operand;
import com.example.androidstudio.kalkulaator_ii.model.Operation;
import com.example.androidstudio.kalkulaator_ii.parcelable.DayStatisticParcelable;
import com.example.androidstudio.kalkulaator_ii.parcelable.ExpressionParcelable;
import com.example.androidstudio.kalkulaator_ii.parcelable.OperationParcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidStudio on 03.04.2016.
 */
public class DatabaseBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        Bundle bundle1 = intent.getExtras();
        if(bundle1 != null){
            String command = bundle1.getString("COMMAND");
            Bundle bundle2 = new Bundle();
            if(command != null){
                if(command.equals("GET_ALL_EXPRESSION")){
                    List<Expression> expressions = dataSource.expressionDataSource.getAll();
                    byte[][] parcelsData = new byte[expressions.size()][];
                    int i = 0;
                    for (Expression expression : expressions) {
                        ExpressionParcelable expressionParcelable = new ExpressionParcelable();
                        expressionParcelable.id = expression.getId();
                        expressionParcelable.data = expression.getData();
                        expressionParcelable.result = expression.getResult();
                        expressionParcelable.created = expression.getCreated();
                        List<OperationParcelable> operationParcelables = new ArrayList<OperationParcelable>();
                        for (Operation operation : expression.getOperations()) {
                            OperationParcelable operationParcelable = new OperationParcelable();
                            operationParcelable.id = operation.getId();
                            operationParcelable.num1 = operation.getNum1();
                            operationParcelable.num2 = operation.getNum2();
                            if(operation.getOperand() != null)
                                operationParcelable.operand = operation.getOperand().getName().replace(':', '/');
                            operationParcelable.result = operation.getResult();
                            if(operation.getResultType() != null)
                                operationParcelable.resultType = operation.getResultType().getName();
                            operationParcelables.add(operationParcelable);
                        }
                        expressionParcelable.operationParcelables = operationParcelables;
                        Parcel parcel = Parcel.obtain();
                        expressionParcelable.writeToParcel(parcel, 0);
                        bundle2.putByteArray("DATA_" + i, parcel.marshall());
                        i++;
                    }
                    bundle2.putInt("DATA_ARRAY_COUNT", expressions.size());
                    bundle2.putString("RESULT_TYPE", "ALL_EXPRESSION_DATA");
                }
                else if(command.equals("GET_ALL_DAYSTATISTIC")){
                    List<DayStatistic> dayStatistics = dataSource.dayStatistics.getAll();
                    for(int i = 0; i < dayStatistics.size(); i++){
                        DayStatistic dayStatistic = dayStatistics.get(i);
                        DayStatisticParcelable dayStatisticParcelable = new DayStatisticParcelable();
                        dayStatisticParcelable.id = dayStatistic.getId();
                        if(dayStatistic.getOperand() != null)
                            dayStatisticParcelable.operand = dayStatistic.getOperand().getName().replace(':','/');
                        dayStatisticParcelable.count = dayStatistic.getCount();
                        dayStatisticParcelable.created = dayStatistic.getCreated();
                        Parcel parcel = Parcel.obtain();
                        dayStatisticParcelable.writeToParcel(parcel, 0);
                        bundle2.putByteArray("DATA_" + i, parcel.marshall());
                    }
                    bundle2.putInt("DATA_ARRAY_COUNT", dayStatistics.size());
                    bundle2.putString("RESULT_TYPE", "ALL_DAYSTATISTIC_DATA");
                }
                else if(command.equals("PUT_EXPRESSION")){
                    Parcel parcel = Parcel.obtain();
                    byte [] data = bundle1.getByteArray("DATA");
                    parcel.unmarshall(data, 0, data.length);
                    parcel.setDataPosition(0);
                    ExpressionParcelable expressionParcelable = new ExpressionParcelable(parcel);
                    if(expressionParcelable != null){
                        Expression expression = new Expression();
                        expression.setData(expressionParcelable.data);
                        expression.setResult(expressionParcelable.result);
                        List<Operation> operations = new ArrayList<Operation>();
                        for (OperationParcelable operationParcelable: expressionParcelable.operationParcelables) {
                            Operation operation = new Operation();
                            operation.setNum1(operationParcelable.num1);
                            operation.setNum2(operationParcelable.num2);
                            Operand operand = new Operand();
                            operand.setName(operationParcelable.operand);
                            operation.setOperand(operand);
                            dataSource.dayStatistics.addCountByCreatedNowOperand(operand);
                            operation.setResult(operationParcelable.result);
                            DoubleType doubleType = new DoubleType();
                            doubleType.setName(operationParcelable.resultType);
                            operation.setResultType(doubleType);
                            operations.add(operation);
                        }
                        expression.setOperations(operations);
                        dataSource.expressionDataSource.create(expression);
                        bundle2.putString("RESULT_TYPE", "OK");
                    }
                    else{
                        bundle2.putString("RESULT_TYPE", "ERROR");
                    }
                }
                else {
                    bundle2.putString("RESULT_TYPE", "UNKNOWN_COMMAND");
                }
            }
            setResultExtras(bundle2);
        }
        dataSource.close();
        setResultCode(Activity.RESULT_OK);
    }
}
