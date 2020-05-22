package com.example.tuitionapp_surji.calendar;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


@SuppressWarnings("serial")
public class SerializableCalendarClass  implements Parcelable {


    GoogleAccountCredential credential;

    public SerializableCalendarClass( GoogleAccountCredential credential)
    {

        this.credential = credential;
    }


    protected SerializableCalendarClass(Parcel in) {
    }

    public static final Creator<SerializableCalendarClass> CREATOR = new Creator<SerializableCalendarClass>() {
        @Override
        public SerializableCalendarClass createFromParcel(Parcel in) {
            return new SerializableCalendarClass(in);
        }

        @Override
        public SerializableCalendarClass[] newArray(int size) {
            return new SerializableCalendarClass[size];
        }
    };

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}


/**
 * Constructor.
 *
 * <p>
 * Use {@link com.google.api.services.calendar.Calendar.Builder} if you need to specify any of the optional parameters.
 * </p>
 *
 * @param transport              HTTP transport, which should normally be:
 *                               <ul>
 *                               <li>Google App Engine:
 *                               {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
 *                               <li>Android: {@code newCompatibleTransport} from
 *                               {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
 *                               <li>Java: {@link GoogleNetHttpTransport#newTrustedTransport()}
 *                               </li>
 *                               </ul>
 * @param jsonFactory            JSON factory, which may be:
 *                               <ul>
 *                               <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
 *                               <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
 *                               <li>Android Honeycomb or higher:
 *                               {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
 *                               </ul>
 * @param credential HTTP request initializer or {@code null} for none
 * @since 1.7
 */