package org.bdgenomics.suffixarrays;

import java.io.*;

public class Main {

  public static void main(String[] args) throws IOException {
    String referenceFilename = args[0];
    String alphabet = "ACGT";

    File referenceFile = new File(referenceFilename);

    String[] input = readContents(referenceFile);
    String header = input[0];
    String reference = input[1];

    SuffixArray array = new SimpleSuffixArray(reference, alphabet);
    Searcher searcher = new Searcher(header, array);

    String query;
    try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      while((query = br.readLine()) != null) {
        searcher.search(query).forEach(System.out::println);
      }
    }
  }

  public static String[] readContents(File f) throws IOException {
    String line, header;
    StringBuilder sb = new StringBuilder();
    try(BufferedReader br = new BufferedReader(new FileReader(f))) {
      header = br.readLine();
      while((line = br.readLine()) != null) {
        sb.append(line.toUpperCase());
      }
    }
    return new String[] { header, sb.toString() };
  }
}

