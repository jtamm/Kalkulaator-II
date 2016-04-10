package com.example.androidstudio.kalkulaator_ii.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by AndroidStudio on 03.04.2016.
 */
public class OperationParcelable implements Parcelable {
    public long id;
    public String operand;
    public float num1;
    public float num2;
    public float result;
    public String resultType;

    public OperationParcelable(){}

    public OperationParcelable(Parcel in){
        id = in.readLong();
        operand = in.readString();
        num1 = in.readFloat();
        num2 = in.readFloat();
        result = in.readFloat();
        resultType = in.readString();
    }

    public static final Parcelable.Creator<OperationParcelable> CREATOR
            = new Parcelable.Creator<OperationParcelable>() {

        @Override
        public OperationParcelable createFromParcel(Parcel source) {
            return new OperationParcelable(source);
        }

        @Override
        public OperationParcelable[] newArray(int size) {
            return new OperationParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(operand);
        dest.writeFloat(num1);
        dest.writeFloat(num2);
        dest.writeFloat(result);
        dest.writeString(resultType);
    }

    @Override
    public int describeContents() {
        return 0;
        //return hashCode();
    }
}
