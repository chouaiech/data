package io.piveau.hub.search.util.hash;

import org.apache.commons.codec.digest.DigestUtils;

public class HashHelper {

    public static String hashId(String hashingAlgorithm, String originId) {
        DigestUtils digestUtils = new DigestUtils(hashingAlgorithm);
        return digestUtils.digestAsHex(originId);
    }

}
