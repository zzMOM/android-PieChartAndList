package com.example.piechartandlist;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class PLContract {

	public static final String CONTENT_AUTHORITY = "com.example.piechartandlist";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_PIE = "pie";
    public static final String PATH_LIST = "list";
    
    /* Inner class that defines the table contents of the location table */
    public static final class PieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_PIE;

        // Table name
        public static final String TABLE_NAME = "pie";

        public static final String COLUMN_RED = "red";
        public static final String COLUMN_GREEN = "green";
        public static final String COLUMN_YELLOW = "yellow";

        public static Uri buildPieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class ListEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LIST;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LIST;

        public static final String TABLE_NAME = "list";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_FIELD1 = "field1";
        public static final String COLUMN_FIELD2 = "field2";
        public static final String COLUMN_FIELD3 = "field3";


        public static Uri buildListUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
