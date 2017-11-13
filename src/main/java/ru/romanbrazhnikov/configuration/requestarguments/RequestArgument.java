package ru.romanbrazhnikov.configuration.requestarguments;

import java.util.ArrayList;
import java.util.List;

public class RequestArgument {
    public String mParamName;
    public String mField;
    public List<RequestArgumentValues> mParamValueList = new ArrayList<>();
}
