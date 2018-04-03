import sun.security.ssl.Debug;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class MatchSession {
    private DebugOutput output;
    private String dictFileName = null;
    private Dictionary dictionary = new Dictionary();
    private boolean singleStepDebug = true;
    private boolean displayLookupFailure = false;
    private boolean displayMatchingFailure = true;
    private boolean displayBothMatchingResult = true;
    private boolean displayDecisionLog = true;
    private boolean defaultRMMResult = false;

    protected static <E> List<E> listReverse(List<E> list) {
        if(list == null) return null;
        List<E> result = new ArrayList<>();
        int listLength = list.size();
        for(int i = listLength - 1; i >= 0; i --) {
            result.add(list.remove(i));
        }
        return result;
    }


    public boolean isDefaultRMMResult() {
        return defaultRMMResult;
    }
    public boolean isDisplayBothMatchingResult() {
        return displayBothMatchingResult;
    }
    public boolean isDisplayDecisionLog() {
        return displayDecisionLog;
    }
    public boolean isDisplayLookupFailure() {
        return displayLookupFailure;
    }
    public boolean isDisplayMatchingFailure() {
        return displayMatchingFailure;
    }
    public boolean isSingleStepDebug() {
        return singleStepDebug;
    }
    public void setSingleStepDebug(boolean singleStepDebug) {
        this.singleStepDebug = singleStepDebug;
    }
    public void setDefaultRMMResult(boolean defaultRMMResult) {
        this.defaultRMMResult = defaultRMMResult;
    }
    public void setDisplayBothMatchingResult(boolean displayBothMatchingResult) {
        this.displayBothMatchingResult = displayBothMatchingResult;
    }
    public void setDisplayDecisionLog(boolean displayDecisionLog) {
        this.displayDecisionLog = displayDecisionLog;
    }
    public void setDisplayLookupFailure(boolean displayLookupFailure) {
        this.displayLookupFailure = displayLookupFailure;
    }
    public void setDisplayMatchingFailure(boolean displayMatchingFailure) {
        this.displayMatchingFailure = displayMatchingFailure;
    }

    // dict [filename]
    public String getDictFileName() {
        return dictFileName;
    }
    public void setDictFileName(String dictFileName) {
        this.dictFileName = dictFileName;
    }

    // direct load.
    public Dictionary getDictionary() {
        return dictionary;
    }

    // load.
    public void loadDictionary() throws FileNotFoundException {
        this.dictionary = Dictionary.fromFile(this.dictFileName);
        this.output.println("dict " + this.dictFileName + " loaded.");
    }


    // match.
    public int getMaximumWordLength() {
        return this.dictionary.getMaximumWordLength();
    }
    // mm [string]
    public List<String> maximumMatch(String subject) {
        if(this.singleStepDebug) {
            output.println("maximumMatch: using maximumWordLength of current dictionary.");
        }

        return this.maximumMatch(subject, this.dictionary.getMaximumWordLength());
    }
    public List<String> maximumMatch(String subject, int maximumWordLength) {
        if(this.singleStepDebug) {
            output.println("maximumMatch: using maximumWordLength = " + maximumWordLength);
        }
        return maximumMatchRough(subject, maximumWordLength);
    }
    public List<String> maximumMatchRough(String subject, int maximumWordLength) {
        List<String> result = new ArrayList<>();
        while(subject.length() > 0) {
            boolean dividingSuccess = false;
            for(int i = Math.min(subject.length(), maximumWordLength); i >= 1; i--) {
                String firstFragment = subject.substring(0, i);
                if (this.dictionary.hasWord(firstFragment)) {
                    if(singleStepDebug) {
                        output.println("Dictionary contains " + firstFragment + ". Dividing.");
                    }
                    result.add(firstFragment);
                    subject = subject.substring(i);
                    dividingSuccess = true;
                    break;
                } else {
                    if(singleStepDebug && displayLookupFailure) {
                        output.println("Dictionary does not contain " + firstFragment + ". Check for other pieces.");
                    }
                }
            }
            if(subject.length() != 0 && !dividingSuccess) {
                if (singleStepDebug && displayMatchingFailure) {
                    output.println("Failed to find a matching word for " + subject.substring(0, 1) + "." +
                            " Skipping to the next character.");
                }
                result.add(subject.substring(0, 1));
                subject = subject.substring(1);
            }
        }
        return result;
    }

    // rmm [string]
    public List<String> reversedMaximumMatch(String subject) {
        if(singleStepDebug) {
            output.println("reversedMaximumMatch: using maximumWordLength of current dictionary.");
        }
        return this.reversedMaximumMatch(subject, this.dictionary.getMaximumWordLength());
    }
    public List<String> reversedMaximumMatch(String subject, int maximumWordLength) {
        if(singleStepDebug) {
            output.println("reversedMaximumMatch: using maximumWordLength = " + maximumWordLength);
        }
        return reversedMaximumMatchRough(subject, maximumWordLength);
    }
    public List<String> reversedMaximumMatchRough(String subject, int maximumWordLength) {
        List<String> result = new ArrayList<>();
        while(subject.length() > 0) {
            boolean dividingSuccess = false;
            for (int i = Math.min(subject.length(), maximumWordLength); i >= 1; i--) {
                int divisionPos = subject.length() - i;
                String firstFragment = subject.substring(divisionPos);
                if (this.dictionary.hasWord(firstFragment)) {
                    if(singleStepDebug) {
                        output.println("Dictionary contains " + firstFragment + ". Dividing.");
                    }
                    result.add(firstFragment);
                    subject = subject.substring(0, divisionPos);
                    dividingSuccess = true;
                    break;
                } else {
                    if(singleStepDebug && displayLookupFailure) {
                        output.println("Dictionary does not contain " + firstFragment + ". Check for other pieces.");
                    }
                }
            }
            if(subject.length() > 0 && !dividingSuccess) {
                if(singleStepDebug && displayMatchingFailure) {
                    output.println("Failed to find a matching word for "
                            + subject.substring(subject.length() - 1) +"." +
                            " Skipping to the next character.");
                }
                result.add(subject.substring(subject.length() - 1));
                subject = subject.substring(0, subject.length() - 1);
            }
        }
        return MatchSession.listReverse(result);
    }

    public List<String> bidirectionalMaximumMatch(String subject) {
        return bidirectionalMaximumMatch(subject, this.dictionary.getMaximumWordLength());
    }
    public List<String> bidirectionalMaximumMatch(String subject, int maximumWordLength) {
        List<String> mmResult = maximumMatch(subject, maximumWordLength);
        List<String> rmmResult = reversedMaximumMatch(subject, maximumWordLength);
        if(singleStepDebug && displayBothMatchingResult) {
            output.print("mmResult: "); displayMatchingResult(mmResult);
            output.print("rmmResult: "); displayMatchingResult(rmmResult);
        }
        final int[] mmResult_cnt = {0, 0, 0}, rmmResult_cnt = {0, 0, 0};
        mmResult.forEach(s -> {
            mmResult_cnt[0]++;
            if(s.length() == 1) {
                if(dictionary.hasWord(s)) {
                    mmResult_cnt[1]++;
                } else {
                    mmResult_cnt[2]++;
                }
            }
        });
        rmmResult.forEach(s -> {
            rmmResult_cnt[0]++;
            if(s.length() == 1) {
                if(dictionary.hasWord(s)) {
                    rmmResult_cnt[1]++;
                } else {
                    rmmResult_cnt[2]++;
                }
            }
        });
        switch (Integer.compare(mmResult_cnt[0], rmmResult_cnt[0])) {
            case 1: {
                if(singleStepDebug && displayDecisionLog) {
                    output.println("rmm has a smaller wordcount. Using rmm's result.");
                }
                return rmmResult;
            }
            case -1: {
                if (singleStepDebug && displayDecisionLog) {
                    output.println("mm has a smaller wordcount. Using mm's result.");
                }
                return mmResult;
            }
            case 0: {
                if(singleStepDebug && displayDecisionLog) {
                    output.println("both result has the same wordcount. " +
                            "Comparing non-registered wordcount.");
                }
                switch (Integer.compare(mmResult_cnt[2], mmResult_cnt[2])) {
                    case -1: {
                        if(singleStepDebug && displayDecisionLog) {
                            output.println("mm has a smaller non-registered wordcount. Using mm's result.");
                        }
                        return mmResult;
                    }
                    case 1: {
                        if(singleStepDebug && displayDecisionLog) {
                            output.println("rmm has a smaller non-registered wordcount. Using rmm's result");
                        }
                        return rmmResult;
                    }
                    case 0: {
                        if(singleStepDebug && displayDecisionLog) {
                            output.println("both results have the same non-registered wordcount. " +
                                    "Comparing single-char wordcount.");
                        }
                        switch (Integer.compare(mmResult_cnt[1],rmmResult_cnt[1])) {
                            case -1: {
                                if(singleStepDebug && displayDecisionLog) {
                                    output.println("mm has a smaller single-char wordcount. Using mm's result.");
                                }
                                return mmResult;
                            }
                            case 1: {
                                if(singleStepDebug && displayDecisionLog) {
                                    output.println("rmm has a smaller single-char wordcount. Using rmm's result.");
                                }
                                return rmmResult;
                            }
                            case 0: {
                                output.println("Failed to decide. Printing both results:");
                                displayMatchingResult(mmResult);
                                displayMatchingResult(rmmResult);
                                output.println("Checking default settings... ");
                                if(defaultRMMResult) {
                                    output.println("default is rmm. Using rmm's result.");
                                    return rmmResult;
                                } else {
                                    output.println("default is mm. Using mm's result.");
                                    return mmResult;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public void displayMatchingResult(List<String> strings) {
        output.println(strings.toString());
    }

    public MatchSession(DebugOutput output) {
        this.output = output;
    }
}
