package com.example.androidstudio.kalkulaator_ii.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpressionParcelable implements Parcelable {
    public long id;
    public String data;
    public String result;
    public Date created;
    public List<OperationParcelable> operationParcelables;

    public ExpressionParcelable(){}

    public ExpressionParcelable(Parcel in){
        id = in.readLong();
        data = in.readString();
        result = in.readString();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            created = ft.parse(in.readString());
        }
        catch (Exception e){}
        operationParcelables = new ArrayList<OperationParcelable>();
        in.readTypedList(operationParcelables, OperationParcelable.CREATOR);
    }

    public static final Parcelable.Creator<ExpressionParcelable> CREATOR
            = new Parcelable.Creator<ExpressionParcelable>() {

        @Override
        public ExpressionParcelable createFromParcel(Parcel source) {
            return new ExpressionParcelable(source);
        }

        @Override
        public ExpressionParcelable[] newArray(int size) {
            return new ExpressionParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(data);
        dest.writeString(result);
        if(created == null){
            dest.writeString("");
        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            dest.writeString(dateFormat.format(created));
        }
        dest.writeTypedList(operationParcelables);
    }

    @Override
    public int describeContents() {
        return 0;
        //return hashCode();
    }
}
