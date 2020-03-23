package com.tilacyn.search;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<String> STOP_WORDS = Arrays.asList("-1", "exit",  "q", "quit");

    public static void main(String[] args) {
        StubServerWrapper stubServerWrapper = new StubServerWrapper(false);
        stubServerWrapper.start();
        QueryManager queryManager = new QueryManager();
        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            String query = in.nextLine();
            if (STOP_WORDS.contains(query.trim())) {
                System.out.println("stop word found");
                break;
            }
            queryManager.processQuery(query);
        }
        in.close();
        queryManager.shutdown();
        stubServerWrapper.stop();
    }
}
