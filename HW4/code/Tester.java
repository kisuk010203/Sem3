import java.util.*;

public class Tester {

    static final boolean printTestInfo = true;
    static final boolean doSort = true;
    static final boolean checkSort = true;
    static final boolean printResult = true;

    static int testNum;
    static int CAPACITY;
    static int rMin;
    static int rMax;

    static final String sortingOrderStr = "BIHMQR";
    static final int sortingNum = 6;
    static final boolean[] doEachSorting = new boolean[sortingNum];


    public static void main(String[] args) {

        System.out.println();
        System.out.println("####main() start");
        System.out.println();

        final int CAPACITY_mini = 16;
        int[] arr1 = new int[CAPACITY_mini];
        Random random_mini = new Random();
        int rMin_mini = -9;
        int rMax_mini = 9;

        for (int i = 0; i < arr1.length; i++)
            arr1[i] = random_mini.nextInt(rMax_mini - rMin_mini + 1) + rMin_mini;
        printArrBySpace(arr1, "arr1");
        int[] arr2 = SortingTest.DoRadixSort(arr1);
        printArrBySpace(arr2, "arr2");


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //@FIXME

        final boolean doTest = true;

        testNum = 16;               //  should bigger than (edge = 3) { 16, 128, 1024 }
        CAPACITY = 10000;            //  { 16, 1024, 4096, 16384, 65536, 262114, 1048576, 16777216 } : 4, 10, 12, 14, 16, 18, 20, 24
        rMin = -9;                 //  { 0, 1, 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, (2147483648-1)/2 } : 1, 1, 1, 2, 3, 4, 5, 6, 7, 8 2^31
        rMax = 9;
        final String whichSort = "Q";      // "BIHMQR"
        applyWhichSort(whichSort);              // fix doEachSorting[]

        Random random1 = new Random();
        final boolean cheat = false;
        int[] cheatArr = new int[0];
        if (cheat) {
            cheatArr = new int[CAPACITY];
            for (int i = 0; i < CAPACITY; i++) {
                cheatArr[i] = random1.nextInt(9);
            }

            int[] sortedCheatArr = SortingTest.DoHeapSort(cheatArr);
            System.arraycopy(sortedCheatArr, 0, cheatArr, 0, CAPACITY - 10000);
        }

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        long[][] runTimes = new long[0][0];
        System.out.println();
        System.out.println("================================");

        // print test info
        if (printTestInfo && doTest) {

            System.out.printf("##testInfo : doSort=%s, checkSort=%s, printResult=%s\n", doSort, checkSort, printResult);
            System.out.printf("testNum=%d, capacity=%d, range=%d~%d, cheat=%s\n", testNum, CAPACITY, rMin, rMax, cheat);
            for (int i = 0; i < sortingNum; i++)
                System.out.printf("%c: %s, ", sortingOrderStr.charAt(i), doEachSorting[i]);
            System.out.println();
            System.out.println();

        }

        if (doTest) {
            System.out.println("##test start");

            runTimes = new long[sortingNum][testNum];

            // test for each testCases
            for (int t = 0; t < testNum; t++) {
                //System.out.printf("now sorting %dth test...\n", t);

                Random random2 = new Random();
                int[] arr0 = new int[CAPACITY];

                for (int i = 0; i < arr0.length; i++){
                    arr0[i] = arr0.length - i;
                    
                }

                if (cheat) {
                    try {
                        if (cheatArr.length == CAPACITY)
                            System.arraycopy(cheatArr, 0, arr0, 0, CAPACITY);
                        else
                            throw new MyException("cheatArr is not initialized well");
                    } catch (MyException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                int[] arrB = new int[0];
                int[] arrI = new int[0];
                int[] arrH = new int[0];
                int[] arrM = new int[0];
                int[] arrQ = new int[0];
                int[] arrR = new int[0];

                // execute sorting
                if (doSort) {
                    long currTime;
                    long prevTime;

                    if (doEachSorting[0]) {
                        currTime = System.currentTimeMillis();
                        arrB = SortingTest.DoBubbleSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[0][t] = currTime - prevTime;
                    }

                    if (doEachSorting[1]) {
                        currTime = System.currentTimeMillis();
                        arrI = SortingTest.DoInsertionSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[1][t] = currTime - prevTime;
                    }

                    if (doEachSorting[2]) {
                        currTime = System.currentTimeMillis();
                        arrH = SortingTest.DoHeapSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[2][t] = currTime - prevTime;
                    }

                    if (doEachSorting[3]) {
                        currTime = System.currentTimeMillis();
                        arrM = SortingTest.DoMergeSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[3][t] = currTime - prevTime;
                    }

                    if (doEachSorting[4]) {
                        currTime = System.currentTimeMillis();
                        arrQ = SortingTest.DoQuickSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[4][t] = currTime - prevTime;
                    }

                    if (doEachSorting[5]) {
                        currTime = System.currentTimeMillis();
                        arrR = SortingTest.DoRadixSort(arr0);
                        prevTime = currTime;
                        currTime = System.currentTimeMillis();
                        runTimes[5][t] = currTime - prevTime;
                    }

                }           // end if(doSorting)


                // check the sorted array, if arr is not sorted well, return main()
                if (checkSort && doSort) {
                    ArrayList<int[]> sortedArrays = new ArrayList<>();
                    sortedArrays.add(arrB);
                    sortedArrays.add(arrI);
                    sortedArrays.add(arrH);
                    sortedArrays.add(arrM);
                    sortedArrays.add(arrQ);
                    sortedArrays.add(arrR);

                    int errIdx = -1;
                    try {
                        for (int i = 0; i < sortingNum; i++) {
                            if (doEachSorting[i]) {
                                if (!isSorted(sortedArrays.get(i), 0, CAPACITY)) {
                                    errIdx = i;
                                    throw new MyException(String.format("sort %c is incorrect", sortingOrderStr.charAt(i)));
                                }
                            }
                        }

                    } catch (MyException e) {
                        if (errIdx != -1) {
                            System.out.println("@@error");
                            System.out.println();
                            System.out.printf("%dth testcase\n", t);
                            System.out.println();
                            System.out.println("origin array : ");
                            printArrBySpace(arr0, "arr0");
                            System.out.println();
                            System.out.println("sorted array : ");
                            printArrBySpace(sortedArrays.get(errIdx), "arr" + sortingOrderStr.charAt(errIdx));
                            e.printStackTrace();
                            return;
                        } else {
                            e.printStackTrace();
                            return;
                        }
                    }
                }           // end if(checkSort)

                //System.out.printf("%dth test done\n", t);

            }   // end for(t), each testCases

            // if for(t) end without exception, all sorting is completed
            if (doSort) {
                if (checkSort)
                    System.out.println("@@success");
                System.out.println("##test end");
                System.out.println();
            }

        }   // end if(doTest)

        // print test result
        if (printResult && doTest) {
            if (doSort) {

                // print test info
                boolean printTestInfo = true;
                if (printTestInfo) {
                    System.out.printf("##testInfo : doSort=%s, checkSort=%s, printResult=%s\n", doSort, checkSort, printResult);
                    System.out.printf("testNum=%d, capacity=%d, range=%d~%d, cheat=%s\n", testNum, CAPACITY, rMin, rMax, cheat);
                    for (int i = 0; i < sortingNum; i++)
                        System.out.printf("%c: %s, ", sortingOrderStr.charAt(i), doEachSorting[i]);
                    System.out.println();
                    System.out.println();
                }

                // print runTimes
                boolean printRunTimes = true;
                if (printRunTimes) {
                    System.out.println("##runTimes");
                    int runtimesNum = 16;
                    for (int i = 0; i < sortingNum; i++) {
                        if (doEachSorting[i]) {
                            System.out.printf("%c sort : ", sortingOrderStr.charAt(i));
                            for (int j = 0; j < Math.min(testNum, runtimesNum); j++)
                                System.out.printf("%5d, ", runTimes[i][j]);
                            if (testNum > runtimesNum)
                                System.out.print("...");
                            System.out.println();
                        }
                    }
                    System.out.println();
                }

                // print statistics
                boolean printStatistics = true;
                int edge = 3;
                if (printStatistics) {

                    try {
                        if (testNum <= edge)
                            throw new MyException(String.format("testNum %d is less than edge %d", testNum, edge));
                    } catch (MyException e) {
                        e.printStackTrace();
                        return;
                    }

                    System.out.println("##statistics");
                    System.out.printf("(discard first %d values)\n", edge);
                    System.out.printf("%-15s %-15s %-15s %-15s %-15s\n", "sort type", "avg[ms]", "sd[ms]", "min[ms]", "max[ms]");

                    long sum;
                    double avg, var, sd;
                    long min, max;
                    long runTime;

                    for (int s = 0; s < sortingNum; s++) {
                        if (doEachSorting[s]) {
                            min = runTimes[s][edge];
                            max = runTimes[s][edge];

                            sum = 0;
                            for (int t = edge; t < testNum; t++) {

                                runTime = runTimes[s][t];
                                if (runTime < min)
                                    min = runTime;
                                if (runTime > max)
                                    max = runTime;
                                sum += runTime;
                            }
                            avg = sum / (double) (testNum - edge);

                            var = 0;
                            for (int t = edge; t < testNum; t++) {
                                var += (runTimes[s][t] - avg) * (runTimes[s][t] - avg);
                            }
                            sd = Math.pow(var / (testNum - edge), 0.5);

                            System.out.printf("%-15s %-15.2f %-15.2f %-15d %-15d\n", sortingOrderStr.charAt(s) + " sort :", avg, sd, min, max);
                        }

                    }
                    System.out.println();

                }   // end if(printStatistics)
            }


        }   // end if(printTestResult)

        System.out.println();
        System.out.println("================================");
        System.out.println("####main() end without errors");

    }   // end main

    private static boolean isSorted(int[] sortedArr, int start, int end) throws MyException {

        if (sortedArr == null) {
            throw new MyException("sortedArr is null");
        }

        if (start < 0 || start > sortedArr.length - 1 || end < 1 || end > sortedArr.length)
            throw new MyException("start or end index is invalid");

        for (int i = start; i < end - 1; i++) {
            if (sortedArr[i] > sortedArr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static void printArrBySpace(int[] arr, String arrName) {
        System.out.println(arrName);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ' ');
        }
    }

    private static void applyWhichSort(String whichSort) {

        if (whichSort == null)
            return;

        for (int i = 0; i < whichSort.length(); i++) {
            char c = whichSort.charAt(i);
            int idx = sortingOrderStr.indexOf(c);
            if (idx != -1) {
                doEachSorting[idx] = true;
            }
        }
    }

    private static class MyException extends Exception {
        MyException(String str) {
            super(str);
        }
    }

}