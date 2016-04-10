package com.example.androidstudio.kalkulaator_ii.parcelable;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AndroidStudio on 03.04.2016.
 */
public class DayStatisticParcelable implements Parcelable {
    public long id;
    public String operand;
    public int count;
    public Date created;

    public DayStatisticParcelable(Parcel in){
        id = in.readLong();
        operand = in.readString();
        count = in.readInt();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            created = ft.parse(in.readString());
        }
        catch (Exception e){}
    }

    public DayStatisticParcelable(){}

    public static final Parcelable.Creator<DayStatisticParcelable> CREATOR
            = new Parcelable.Creator<DayStatisticParcelable>() {

        @Override
        public DayStatisticParcelable createFromParcel(Parcel source) {
            return new DayStatisticParcelable(source);
        }

        @Override
        public DayStatisticParcelable[] newArray(int size) {
            return new DayStatisticParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(operand);
        dest.writeInt(count);
        if(created == null){
            dest.writeString("");
        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            dest.writeString(dateFormat.format(created));
        }

    }

    @Override
    public int describeContents() {
        return 0;
        //return hashCode();
    }
}
