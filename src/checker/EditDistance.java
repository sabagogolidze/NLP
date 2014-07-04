package checker;




public class EditDistance 
{
//    implements Distance<CharSequence>, Proximity<CharSequence> {

    private final boolean mAllowTransposition;

    /**
     * Construct an edit distance with or without transposition based
     * on the specified flag.
     *
     * @param allowTransposition Set to <code>true</code> to allow
     * transposition edits in the constructed distance.
     */
    public EditDistance(boolean allowTransposition) {
        mAllowTransposition = allowTransposition;
    }

    /**
     * Returns the edit distance between the specified character
     * sequences.  Whether transposition is allowed or not is set at
     * construction time.  This method may be accessed concurrently
     * without synchronization.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return Edit distance between the character sequences.
     */
    public double distance(CharSequence cSeq1, CharSequence cSeq2) {
        return editDistance(cSeq1,cSeq2,mAllowTransposition);
    }


    /**
     * Returns the proximity between the character sequences.
     * Proximity is defined as the negation of the distance:
     *
     * <blockquote><pre>
     * proximity(cs1,cs2) = -distance(cs1,cs2)
     * </pre></blockquote>
     *
     * and thus proximities will all be negative or zero.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return Proximity between the character sequences.
     */
    public double proximity(CharSequence cSeq1, CharSequence cSeq2) {
        return -distance(cSeq1,cSeq2);
    }

    /**
     * Returns a string representation of this edit distance.
     *
     * @return A string representation of this edit distance.
     */
    @Override
    public String toString() {
        return "EditDistance(" + mAllowTransposition + ")";
    }

    /**
     * Returns the edit distance between the character sequences with
     * or without transpositions as specified.  This distance is
     * symmetric.  This method is thread safe and may be accessed
     * concurrently.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @param allowTransposition Set to <code>true</code> to allow
     * transposition edits.
     * @return Edit distance between the character sequences.
     */
    public static int editDistance(CharSequence cSeq1, 
                                   CharSequence cSeq2,
                                   boolean allowTransposition) {
        // switch for min sized lattice slices
        if (cSeq1.length() < cSeq2.length()) {
            CharSequence temp = cSeq1;
            cSeq1 = cSeq2;
            cSeq2 = temp;
        }

        // compute small array cases
        if (cSeq2.length() == 0) return cSeq1.length();
        if (cSeq2.length() == 1) {
            char c = cSeq2.charAt(0);
            for (int i = 0; i < cSeq1.length(); ++i)
                if (cSeq1.charAt(i) == c) 
                    return cSeq1.length()-1; // one match
            return cSeq1.length(); // one subst, other deletes
        }

        if (allowTransposition) 
            return editDistanceTranspose(cSeq1,cSeq2);
        return editDistanceNonTranspose(cSeq1,cSeq2);
    }

    private static int editDistanceNonTranspose(CharSequence cSeq1,
                                                CharSequence cSeq2) {
        // cSeq1.length >= cSeq2.length > 1
        int xsLength = cSeq1.length() + 1; // > ysLength
        int ysLength = cSeq2.length() + 1; // > 2

        int[] lastSlice = new int[ysLength];
        int[] currentSlice = new int[ysLength];

        // first slice is just inserts
        for (int y = 0; y < ysLength; ++y)
            currentSlice[y] = y;  // y inserts down first column of lattice
    
        for (int x = 1; x < xsLength; ++x) {
            char cX = cSeq1.charAt(x-1);
            int[] lastSliceTmp = lastSlice;
            lastSlice = currentSlice;
            currentSlice = lastSliceTmp;
            currentSlice[0] = x; // x deletes across first row of lattice
            for (int y = 1; y < ysLength; ++y) {
                int yMinus1 = y - 1;
                // unfold this one step further to put 1 + outside all mins on match
                currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1)
                                           ? lastSlice[yMinus1] // match
                                           : 1 + lastSlice[yMinus1], // subst
                                           1 + Math.min(lastSlice[y], // delelte
                                                        currentSlice[yMinus1])); // insert
            }
        }
        return currentSlice[currentSlice.length-1];
    }

    private static int editDistanceTranspose(CharSequence cSeq1,
                                             CharSequence cSeq2) {

        // cSeq1.length >= cSeq2.length > 1
        int xsLength = cSeq1.length() + 1; // > ysLength
        int ysLength = cSeq2.length() + 1; // > 2

        int[] twoLastSlice = new int[ysLength];
        int[] lastSlice = new int[ysLength];
        int[] currentSlice = new int[ysLength];

        // x=0: first slice is just inserts
        for (int y = 0; y < ysLength; ++y)
            lastSlice[y] = y;  // y inserts down first column of lattice

        // x=1:second slice no transpose
        currentSlice[0] = 1; // insert x[0]
        char cX = cSeq1.charAt(0);
        for (int y = 1; y < ysLength; ++y) {
            int yMinus1 = y-1;
            currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1)
                                       ? lastSlice[yMinus1] // match
                                       : 1 + lastSlice[yMinus1], // subst
                                       1 + Math.min(lastSlice[y], // delelte
                                                    currentSlice[yMinus1])); // insert
        }

        char cYZero = cSeq2.charAt(0);
    
        // x>1:transpose after first element
        for (int x = 2; x < xsLength; ++x) {
            char cXMinus1 = cX;
            cX = cSeq1.charAt(x-1);

            // rotate slices
            int[] tmpSlice = twoLastSlice;
            twoLastSlice = lastSlice;
            lastSlice = currentSlice;
            currentSlice = tmpSlice;

            currentSlice[0] = x; // x deletes across first row of lattice

            // y=1: no transpose here
            currentSlice[1] = Math.min(cX == cYZero
                                       ? lastSlice[0] // match
                                       : 1 + lastSlice[0], // subst
                                       1 + Math.min(lastSlice[1], // delelte
                                                    currentSlice[0])); // insert

            // y > 1: transpose
            char cY = cYZero;
            for (int y = 2; y < ysLength; ++y) {
                int yMinus1 = y-1;
                char cYMinus1 = cY;
                cY = cSeq2.charAt(yMinus1);
                currentSlice[y] = Math.min(cX == cY
                                           ? lastSlice[yMinus1] // match
                                           : 1 + lastSlice[yMinus1], // subst
                                           1 + Math.min(lastSlice[y], // delelte
                                                        currentSlice[yMinus1])); // insert
                if (cX == cYMinus1 && cY == cXMinus1)
                    currentSlice[y] = Math.min(currentSlice[y],1+twoLastSlice[y-2]);
            }
        }
        return currentSlice[currentSlice.length-1];
    }

    /**
     * Edit distance allowing transposition.  The implementation is
     * thread safe and may be accessed concurrently.
     */
    //public static final Distance<CharSequence> TRANSPOSING
    public static final EditDistance TRANSPOSING
        = new EditDistance(true);

    ///**
    // * Edit distance disallowing transposition.  The implementation is
    // * thread safe and may be accessed concurrently.
    // */
    //public static final Distance<CharSequence> NON_TRANSPOSING
    //    = new EditDistance(false);
    
}


