package info.colinhan.sisyphus.server.utils;

import java.sql.Timestamp;

public class U {

    public static Timestamp timeNow() {
        return new Timestamp(new java.util.Date().getTime());
    }
}
