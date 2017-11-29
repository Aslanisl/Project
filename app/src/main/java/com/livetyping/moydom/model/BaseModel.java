package com.livetyping.moydom.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Ivan on 29.11.2017.
 */

@Root(name = "table", strict = false)
public class BaseModel {
    @ElementList(name = "records")
    private List<Error> records;

    public List<Error> getErrorRecords() {
        return records;
    }
}
