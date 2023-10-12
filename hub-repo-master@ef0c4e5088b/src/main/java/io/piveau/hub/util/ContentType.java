package io.piveau.hub.util;

import io.piveau.rdf.RDFMimeTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ContentType {

    RDF_XML(RDFMimeTypes.RDFXML, "rdf"),
    N3(RDFMimeTypes.N3, "n3"),
    N_TRIPLES(RDFMimeTypes.NTRIPLES, "nt"),
    N_QUADS(RDFMimeTypes.NQUADS, "nq"),
    TURTLE(RDFMimeTypes.TURTLE, "ttl"),
    TRIG(RDFMimeTypes.TRIG, "trig"),
    TRIX(RDFMimeTypes.TRIX, "trix"),
    JSON("application/json", "json"),
    JSON_LD(RDFMimeTypes.JSONLD, "jsonld");

    private final String mimeType;
    private final String fileExt;

    public static final ContentType DEFAULT = JSON_LD;
    public static final Map<String, ContentType> BY_FILE_EXT = new HashMap<>();
    private static final Map<String, ContentType> BY_MIME_TYPE = new HashMap<>();

    static {
        for (ContentType contentType : values()) {
            BY_FILE_EXT.put(contentType.fileExt, contentType);
            BY_MIME_TYPE.put(contentType.mimeType, contentType);
        }
    }

    ContentType(String mimeType, String fileExt) {
        this.mimeType = mimeType;
        this.fileExt = fileExt;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileExtension() {
        return fileExt;
    }

    public static ContentType valueOfFileExtension(String fileExt) {
        return BY_FILE_EXT.get(fileExt);
    }

    public static ContentType valueOfMimeType(String mimeType) {
        return BY_MIME_TYPE.get(mimeType);
    }

    public static Set<String> getFileExtensions() {
        return BY_FILE_EXT.keySet();
    }

}
