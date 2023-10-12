package io.piveau.metrics.similarities;

import java.util.Arrays;

public class TLSH {

    /**
     * Number of buckets for TLSH.
     * Originally 256; reduced to account for shorter strings.
     */
    public static final int N_BUCKETS = 64;

    /**
     * Pearson's hash function of three bytes.
     *
     * @return Hash value in range 0..N_BUCKETS - 1.
     */
    private static int pearson3b(int pc0, int pc1, int pc2) {
        return (PEARSON_TABLE[PEARSON_TABLE[pc0 & 255] ^ (pc1 & 255)] ^ (pc2 & 255)) % N_BUCKETS;
    }

    /**
     * "Random" permutation of bytes, for use in Pearson's hash function.
     */
    private static final int[] PEARSON_TABLE = {
            98, 6, 85, 150, 36, 23, 112, 164, 135, 207, 169, 5, 26, 64, 165, 219, //  1
            61, 20, 68, 89, 130, 63, 52, 102, 24, 229, 132, 245, 80, 216, 195, 115, //  2
            90, 168, 156, 203, 177, 120, 2, 190, 188, 7, 100, 185, 174, 243, 162, 10, //  3
            237, 18, 253, 225, 8, 208, 172, 244, 255, 126, 101, 79, 145, 235, 228, 121, //  4
            123, 251, 67, 250, 161, 0, 107, 97, 241, 111, 181, 82, 249, 33, 69, 55, //  5
            59, 153, 29, 9, 213, 167, 84, 93, 30, 46, 94, 75, 151, 114, 73, 222, //  6
            197, 96, 210, 45, 16, 227, 248, 202, 51, 152, 252, 125, 81, 206, 215, 186, //  7
            39, 158, 178, 187, 131, 136, 1, 49, 50, 17, 141, 91, 47, 129, 60, 99, //  8
            154, 35, 86, 171, 105, 34, 38, 200, 147, 58, 77, 118, 173, 246, 76, 254, //  9
            133, 232, 196, 144, 198, 124, 53, 4, 108, 74, 223, 234, 134, 230, 157, 139, // 10
            189, 205, 199, 128, 176, 19, 211, 236, 127, 192, 231, 70, 233, 88, 146, 44, // 11
            183, 201, 22, 83, 13, 214, 116, 109, 159, 32, 95, 226, 140, 220, 57, 12, // 12
            221, 31, 209, 182, 143, 92, 149, 184, 148, 62, 113, 65, 37, 27, 106, 166, // 13
            3, 14, 204, 72, 21, 41, 56, 66, 28, 193, 40, 217, 25, 54, 179, 117, // 14
            238, 87, 240, 155, 180, 170, 242, 212, 191, 163, 78, 218, 137, 194, 175, 110, // 15
            43, 119, 224, 71, 122, 142, 42, 160, 104, 48, 247, 103, 15, 11, 138, 239  // 16
    };

    /**
     * computes TLSH fingerprint of string fingerprintText (which should not be too short)
     * and stores it as values 0..3 in int[N_BUCKETS] fingerprint.
     * Not reentrant in same class instance, due to use of class variables.
     */
    public static void fingerprint(String fingerprintText, int[] fingerprint) throws IllegalArgumentException {
        int i;

        final int[] bucketCount = new int[N_BUCKETS];
        final int[] tmpAux = new int[N_BUCKETS];

        //reset counters:
        for (i = 0; i < N_BUCKETS; i++)
            bucketCount[i] = 0;

        //initialize sliding 5-char window:
        int c0 = fingerprintText.charAt(0), c1 = fingerprintText.charAt(1), c2 = fingerprintText.charAt(2),
                c3 = fingerprintText.charAt(3), c4 = fingerprintText.charAt(4);

        i = 5;

        while (true) {
            bucketCount[pearson3b(c0, c1, c2)]++;
            bucketCount[pearson3b(c0, c3, c1)]++;
            bucketCount[pearson3b(c1, c0, c4)]++;
            bucketCount[pearson3b(c3, c2, c0)]++;
            bucketCount[pearson3b(c4, c0, c2)]++;
            bucketCount[pearson3b(c3, c4, c0)]++;

            //exit when end of string reached:
            if (i >= fingerprintText.length())
                break;

            // slide window forward:
            c0 = c1;
            c1 = c2;
            c2 = c3;
            c3 = c4;
            c4 = fingerprintText.charAt(i);
            i++;
        }

        // do remaining 4 triples:
        bucketCount[pearson3b(c1, c2, c3)]++;
        bucketCount[pearson3b(c1, c4, c2)]++;
        bucketCount[pearson3b(c4, c3, c1)]++;
        bucketCount[pearson3b(c2, c3, c4)]++;

        //sort bucketCount, get quartils:
        System.arraycopy(bucketCount, 0, tmpAux, 0, bucketCount.length);
        Arrays.sort(tmpAux);

        int qutil_1 = tmpAux[(tmpAux.length) / 4],
                qutil_2 = tmpAux[(tmpAux.length) / 2],
                qutil_3 = tmpAux[(3 * tmpAux.length) / 4];

        if (qutil_1 < 0 || qutil_1 > qutil_2 || qutil_2 > qutil_3)
            throw new IllegalArgumentException("Failed to generate TLSH fingerprint. Quartiles inconsistent.");

        //encode :
        for (i = 0; i < N_BUCKETS; i++) {
            int bi = bucketCount[i];
            // ensure 0→0:
            fingerprint[i] = (bi <= qutil_1 ? 0 : bi >= qutil_3 ? 3 : bi >= qutil_2 ? 2 : 1);
        }
    }

}
