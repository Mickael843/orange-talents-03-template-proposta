package com.mikkaeru.proposal.utils;

public abstract class DocumentReplace {

    public static String replaceAll(String document) {
        return document
                .replace(".", "")
                .replace("-", "")
                .replace("/", "");
    }
}
